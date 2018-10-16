# Tutorial: Using AWS Lambda with AWS CloudTrail<a name="with-cloudtrail-example"></a>

In this scenario, AWS CloudTrail will maintain records \(logs\) of AWS API calls made on your account and notify you anytime an API call is made to create an SNS topic\. As API calls are made in your account, CloudTrail writes logs to an Amazon S3 bucket that you configured\. In this scenario, you want Amazon S3 to publish the object\-created events to AWS Lambda and invoke your Lambda function as CloudTrail creates log objects\. 

When Amazon S3 invokes your Lambda function, it passes an S3 event identifying, among other things, the bucket name and key name of the object that CloudTrail created\. Your Lambda function can read the log object, and it knows the API calls that were reported in the log\.

Each object CloudTrail creates in your S3 bucket is a JSON object, with one or more event records\. Each record, among other things, provides `eventSource` and `eventName`\. 

```
{
    "Records":[
 
        {
            "eventVersion":"1.02",
            "userIdentity":{
               ...
            },
            "eventTime":"2014-12-16T19:17:43Z",
            "eventSource":"sns.amazonaws.com", 
            "eventName":"CreateTopic",
            "awsRegion":"us-west-2",
            "sourceIPAddress":"72.21.198.64",
             ...
         },
         {
            ...
         },
         ...
}
```

For illustration, the Lambda function notifies you by email if an API call to create an Amazon SNS topic is reported in the log\. That is, when your Lambda function parses the log, it looks for records with the following:
+ `eventSource = "sns.amazonaws.com"`
+ `eventName = "CreateTopic"`

 If found, it publishes the event to your Amazon SNS topic \(you configure this topic to notify you by email\)\.

Your Lambda function uses an S3 event that provides the bucket name and key name of the object CloudTrail created\. Your Lambda function then reads that object to process CloudTrail records\.

## Prerequisites<a name="with-cloudtrail-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Turn on CloudTrail<a name="with-cloudtrail-example-prepare-create-buckets"></a>

In the AWS CloudTrail console, turn on the trail in your account by specifying *examplebucket* in the `us-west-2` region for CloudTrail to save logs\. When configuring the trail, do not enable SNS notifications\. 

For instructions, see [Creating and Updating Your Trail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-and-update-a-trail.html) in the *AWS CloudTrail User Guide*\.

## Create the Execution Role<a name="with-cloudtrail-create-execution-role"></a>

Create the [execution role](intro-permission-model.md#lambda-intro-execution-role) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Role name** – **lambda\-cloudtrail\-role**\.
   + **Permissions** – Custom policy\.

     ```
     {
       "Version": "2012-10-17",
       "Statement": [
         {
           "Effect": "Allow",
           "Action": [
             "logs:*"
           ],
           "Resource": "arn:aws:logs:*:*:*"
         },
         {
           "Effect": "Allow",
           "Action": [
             "s3:GetObject"
           ],
           "Resource": "arn:aws:s3:::examplebucket/*"
         },
         {
           "Effect": "Allow",
           "Action": [
             "sns:Publish"
           ],
           "Resource": "your sns topic ARN"
         }
       ]
     }
     ```

The policy has the permissions that the function needs to read items from Amazon S3 and write logs to CloudWatch Logs\.

## Create the Function<a name="with-cloudtrail-example-create-function"></a>

The following example processes CloudTrail logs, and sends a notification when an Amazon SNS topic was created\.

**Example index\.js**  

```
var aws  = require('aws-sdk');
var zlib = require('zlib');
var async = require('async');

var EVENT_SOURCE_TO_TRACK = /sns.amazonaws.com/;  
var EVENT_NAME_TO_TRACK   = /CreateTopic/; 
var DEFAULT_SNS_REGION  = 'us-west-2';
var SNS_TOPIC_ARN       = 'The ARN of your SNS topic';

var s3 = new aws.S3();
var sns = new aws.SNS({
    apiVersion: '2010-03-31',
    region: DEFAULT_SNS_REGION
});

exports.handler = function(event, context, callback) {
    var srcBucket = event.Records[0].s3.bucket.name;
    var srcKey = event.Records[0].s3.object.key;
   
    async.waterfall([
        function fetchLogFromS3(next){
            console.log('Fetching compressed log from S3...');
            s3.getObject({
               Bucket: srcBucket,
               Key: srcKey
            },
            next);
        },
        function uncompressLog(response, next){
            console.log("Uncompressing log...");
            zlib.gunzip(response.Body, next);
        },
        function publishNotifications(jsonBuffer, next) {
            console.log('Filtering log...');
            var json = jsonBuffer.toString();
            console.log('CloudTrail JSON from S3:', json);
            var records;
            try {
                records = JSON.parse(json);
            } catch (err) {
                next('Unable to parse CloudTrail JSON: ' + err);
                return;
            }
            var matchingRecords = records
                .Records
                .filter(function(record) {
                    return record.eventSource.match(EVENT_SOURCE_TO_TRACK)
                        && record.eventName.match(EVENT_NAME_TO_TRACK);
                });
                
            console.log('Publishing ' + matchingRecords.length + ' notification(s) in parallel...');
            async.each(
                matchingRecords,
                function(record, publishComplete) {
                    console.log('Publishing notification: ', record);
                    sns.publish({
                        Message:
                            'Alert... SNS topic created: \n TopicARN=' + record.responseElements.topicArn + '\n\n' + 
                            JSON.stringify(record),
                        TopicArn: SNS_TOPIC_ARN
                    }, publishComplete);
                },
                next
            );
        }
    ], function (err) {
        if (err) {
            console.error('Failed to publish notifications: ', err);
        } else {
            console.log('Successfully published all notifications.');
        }
        callback(null,"message");
    });
};
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   $ zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   $ aws lambda create-function --function-name CloudTrailEventProcessing \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs8.10 \
   --role role-arn
   --timeout 10 --memory-size 1024
   ```

## Test the Lambda Function<a name="walkthrough-cloudtrail-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

In this section, you invoke the Lambda function manually using sample Amazon S3 event data\. When the Lambda function executes, it reads the S3 object \(a sample CloudTrail log\) from the bucket identified in the S3 event data, and then it publishes an event to your SNS topic if the sample CloudTrail log reports use a specific API\. For this tutorial, the API is the SNS API used to create a topic\. That is, the CloudTrail log reports a record identifying `sns.amazonaws.com` as the `eventSource`, and `CreateTopic` as the `eventName`\.

Note that one of events in this log has `sns.amazonaws.com` as the `eventSource` and `CreateTopic` as the `eventName`\. Your Lambda function reads the logs and if it finds an event of this type, it publishes the event to the Amazon SNS topic that you created and then you receive one email when you invoke the Lambda function manually\.

**Example input\.txt**  

```
{  
   "Records":[  
      {  
         "eventVersion":"1.02",
         "userIdentity":{  
            "type":"Root",
            "principalId":"account-id",
            "arn":"arn:aws:iam::account-id:root",
            "accountId":"account-id",
            "accessKeyId":"access-key-id",
            "sessionContext":{  
               "attributes":{  
                  "mfaAuthenticated":"false",
                  "creationDate":"2015-01-24T22:41:54Z"
               }
            }
         },
         "eventTime":"2015-01-24T23:26:50Z",
         "eventSource":"sns.amazonaws.com",
         "eventName":"CreateTopic",
         "awsRegion":"us-west-2",
         "sourceIPAddress":"205.251.233.176",
         "userAgent":"console.amazonaws.com",
         "requestParameters":{  
            "name":"dropmeplease"
         },
         "responseElements":{  
            "topicArn":"arn:aws:sns:us-west-2:account-id:exampletopic"
         },
         "requestID":"3fdb7834-9079-557e-8ef2-350abc03536b",
         "eventID":"17b46459-dada-4278-b8e2-5a4ca9ff1a9c",
         "eventType":"AwsApiCall",
         "recipientAccountId":"account-id"
      },
      {  
         "eventVersion":"1.02",
         "userIdentity":{  
            "type":"Root",
            "principalId":"account-id",
            "arn":"arn:aws:iam::account-id:root",
            "accountId":"account-id",
            "accessKeyId": "access key id",
            "sessionContext":{  
               "attributes":{  
                  "mfaAuthenticated":"false",
                  "creationDate":"2015-01-24T22:41:54Z"
               }
            }
         },
         "eventTime":"2015-01-24T23:27:02Z",
         "eventSource":"sns.amazonaws.com",
         "eventName":"GetTopicAttributes",
         "awsRegion":"us-west-2",
         "sourceIPAddress":"205.251.233.176",
         "userAgent":"console.amazonaws.com",
         "requestParameters":{  
            "topicArn":"arn:aws:sns:us-west-2:account-id:exampletopic"
         },
         "responseElements":null,
         "requestID":"4a0388f7-a0af-5df9-9587-c5c98c29cbec",
         "eventID":"ec5bb073-8fa1-4d45-b03c-f07b9fc9ea18",
         "eventType":"AwsApiCall",
         "recipientAccountId":"account-id"
      }
   ]
}
```

Manually invoke the Lambda function using AWS CLI as follows:

```
$ aws lambda  invoke --function-name CloudTrailEventProcessing \
--payload file://input.txt outputfile.txt
```

## Add Permissions to the Function Policy<a name="with-cloudtrail-example-configure-event-source-add-permission"></a>

Add permissions to the Lambda function's resource policy to allow Amazon S3 to invoke the function\.

1. Run the following `add-permission` command to grant Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Note that permission is granted to Amazon S3 to invoke the function only if the following conditions are met:
   + An object\-created event is detected on a specific bucket\.
   + The bucket is owned by a specific AWS account\. If a bucket owner deletes a bucket, some other AWS account can create a bucket with the same name\. This condition ensures that only a specific AWS account can invoke your Lambda function\.

   ```
   $ aws lambda add-permission --function-name CloudTrailEventProcessing \
   --statement-id Id-1 --action "lambda:InvokeFunction" --principal s3.amazonaws.com \
   --source-arn arn:aws:s3:::examplebucket \
   --source-account examplebucket-owner-account-id
   ```

1. Verify the function's access policy with the `get-policy` command\.

   ```
   $ aws lambda get-policy --function-name function-name
   ```

## Configure Notification on the Bucket<a name="with-cloudtrail-example-configure-event-source-add-notif-config"></a>

Add notification configuration on the *examplebucket* to request Amazon S3 to publish object\-created events to Lambda\. In the configuration, you specify the following:
+ Event type – Any event types that create objects\.
+ Lambda function ARN – This is your Lambda function that you want Amazon S3 to invoke\.

  ```
  arn:aws:lambda:us-west-2:123456789012:function:CloudTrailEventProcessing
  ```

For instructions on adding notification configuration to a bucket, see [Enabling Event Notifications](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/SettingBucketNotifications.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Test the Setup<a name="with-cloudtrail-example-final-integration-test-no-iam"></a>

Now you can test the setup as follows:

1. Create an Amazon SNS topic\.

1. AWS CloudTrail creates a log object in your bucket\.

1. Amazon S3 invokes your Lambda function by passing in the log object's location as event data\.

1. Lambda executes your function\. The function retrieves the log, finds a `CreateTopic` event, and sends a notification\.