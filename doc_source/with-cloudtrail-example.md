# Tutorial: Using AWS Lambda with AWS CloudTrail<a name="with-cloudtrail-example"></a>

In this scenario, AWS CloudTrail will maintain records \(logs\) of AWS API calls made on your account and notify you anytime an API call is made to create an SNS topic\. As API calls are made in your account, CloudTrail writes logs to an Amazon S3 bucket that you configured\. In this scenario, you want Amazon S3 to publish the object\-created events to AWS Lambda and invoke your Lambda function as CloudTrail creates log objects\. 

The following diagram summarizes the flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/wt-cloudtrail-100.png)

1. AWS CloudTrail saves logs to an S3 bucket \(object\-created event\)\.

1. Amazon S3 detects the object\-created event\.

1. Amazon S3 publishes the `s3:ObjectCreated:*` event to AWS Lambda by invoking the Lambda function, as specified in the bucket notification configuration\. Because the Lambda function's access permissions policy includes permissions for Amazon S3 to invoke the function, Amazon S3 can invoke the function\.

1. AWS Lambda executes the Lambda function by assuming the execution role that you specified at the time you created the Lambda function\.

1. The Lambda function reads the Amazon S3 event it receives as a parameter, determines where the CloudTrail object is, reads the CloudTrail object, and then it processes the log records in the CloudTrail object\.

1. If the log includes a record with specific `eventType` and `eventSource` values, it publishes the event to your Amazon SNS topic\. In [Tutorial: Using AWS Lambda with AWS CloudTrail](#with-cloudtrail-example), you subscribe to the SNS topic using the email protocol, so you get email notifications\.

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
            "awsRegion":"us-east-2",
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

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started with AWS Lambda](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Turn on CloudTrail<a name="with-cloudtrail-example-prepare-create-buckets"></a>

In the AWS CloudTrail console, turn on the trail in your account by specifying a bucket for CloudTrail to save logs\. When configuring the trail, do not enable SNS notifications\. 

For instructions, see [Creating and Updating Your Trail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-and-update-a-trail.html) in the *AWS CloudTrail User Guide*\.

## Create the Execution Role<a name="with-cloudtrail-create-execution-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

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
           "Resource": "arn:aws:s3:::my-bucket/*"
         },
         {
           "Effect": "Allow",
           "Action": [
             "sns:Publish"
           ],
           "Resource": "arn:aws:sns:us-west-2:123456789012:my-topic"
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
var DEFAULT_SNS_REGION  = 'us-east-2';
var SNS_TOPIC_ARN       = 'arn:aws:sns:us-west-2:123456789012:my-topic';

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

1. Copy the sample code into a file named `index.js` in a folder named `lambda-cloudtrail`\.

1. Install `async` with npm\.

   ```
   ~/lambda-cloudtrail$ npm install async
   ```

1. Create a deployment package\.

   ```
   ~/lambda-cloudtrail$ zip -r function.zip .
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   $ aws lambda create-function --function-name CloudTrailEventProcessing \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs8.10 --timeout 10 --memory-size 1024 \
   --role arn:aws:iam::123456789012:role/lambda-cloudtrail-role
   ```

## Add Permissions to the Function Policy<a name="with-cloudtrail-example-configure-event-source-add-permission"></a>

Add permissions to the Lambda function's resource policy to allow Amazon S3 to invoke the function\.

1. Run the following `add-permission` command to grant Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Note that permission is granted to Amazon S3 to invoke the function only if the following conditions are met:
   + An object\-created event is detected on a specific bucket\.
   + The bucket is owned by a specific AWS account\. If a bucket owner deletes a bucket, some other AWS account can create a bucket with the same name\. This condition ensures that only a specific AWS account can invoke your Lambda function\.

   ```
   $ aws lambda add-permission --function-name CloudTrailEventProcessing \
   --statement-id Id-1 --action "lambda:InvokeFunction" --principal s3.amazonaws.com \
   --source-arn arn:aws:s3:::my-bucket \
   --source-account 123456789012
   ```

1. Verify the function's access policy with the `get-policy` command\.

   ```
   $ aws lambda get-policy --function-name function-name
   ```

## Configure Notification on the Bucket<a name="with-cloudtrail-example-configure-event-source-add-notif-config"></a>

Add notification configuration on the bucket to request Amazon S3 to publish object\-created events to Lambda\. In the configuration, you specify the following:
+ Event type – Any event types that create objects\.
+ Lambda function ARN – This is your Lambda function that you want Amazon S3 to invoke\.

  ```
  arn:aws:lambda:us-east-2:123456789012:function:CloudTrailEventProcessing
  ```

For instructions on adding notification configuration to a bucket, see [Enabling Event Notifications](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/SettingBucketNotifications.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Test the Setup<a name="with-cloudtrail-example-final-integration-test-no-iam"></a>

Now you can test the setup as follows:

1. Create an Amazon SNS topic\.

1. AWS CloudTrail creates a log object in your bucket\.

1. Amazon S3 invokes your Lambda function by passing in the log object's location as event data\.

1. Lambda executes your function\. The function retrieves the log, finds a `CreateTopic` event, and sends a notification\.