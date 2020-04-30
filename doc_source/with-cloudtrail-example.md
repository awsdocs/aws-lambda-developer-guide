# Tutorial: Triggering a Lambda function with AWS CloudTrail events<a name="with-cloudtrail-example"></a>

You can configure Amazon S3 to publish events to AWS Lambda when AWS CloudTrail stores API call logs\. Your Lambda function can read the log object and process the access records logged by CloudTrail\.

Use the following instructions to create a Lambda function that notifies you when a specific API call is made in your account\. The function processes notification events from Amazon S3, reads logs from a bucket, and publishes alerts through an Amazon SNS topic\. For this tutorial, you create: 
+ A CloudTrail trail and an S3 bucket to save logs to\.
+ An Amazon SNS topic to publish alert notifications\.
+ An IAM user role with permissions to read items from an S3 bucket and write logs to Amazon CloudWatch\.
+ A Lambda function that processes CloudTrail logs and sends a notification whenever an Amazon SNS topic is created\.

## Requirements<a name="with-cloudtrail-tutorial-requirements"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting started with AWS Lambda](getting-started.md) to create your first Lambda function\.

Before you begin, make sure that you have the following tools:
+ [Node\.js 8 with `npm`](https://nodejs.org/en/download/releases/)\.
+ The Bash shell\. For Linux and macOS, this is included by default\. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.
+ [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

## Step 1: Creating a trail in CloudTrail<a name="with-cloudtrail-example-prepare-create-buckets"></a>

When you create a trail, CloudTrail records the API calls in log files and stores them in Amazon S3\. A CloudTrail log is an unordered array of events in JSON format\. For each call to a supported API action, CloudTrail records information about the request and the entity that made it\. Log events include the action name, parameters, response values, and details about the requester\.

**To create a trail**

1. Open the [**Trails** page of the CloudTrail console](https://console.aws.amazon.com/cloudtrail/home#/configuration)\.

1. Choose **Create trail**\.

1. For **Trail name**, enter a name\.

1. For **S3 bucket**, enter a name\.

1. Choose **Create**\.

1. Save the bucket Amazon Resource Name \(ARN\) to add it to the IAM execution role, which you create later\.

## Step 2: Creating an Amazon SNS topic<a name="with-cloudtrail-create-sns-topic"></a>

 Create an Amazon SNS topic to send out a notification when new object events have occurred\. 

**To create a topic**

1. Open the [**Topics** page of the Amazon SNS console](https://console.aws.amazon.com/sns/home#/topics)\.

1. Choose **Create topic**\.

1. For **Topic name**, enter a name\.

1. Choose **Create topic**\.

1. Record the topic ARN\. You will need it when you create the IAM execution role and Lambda function\.

## Step 3: Creating an IAM execution role<a name="with-cloudtrail-create-execution-role"></a>

An [execution role](lambda-intro-execution-role.md) gives your function permission to access AWS resources\. Create an execution role that grants the function permission to access CloudWatch Logs, Amazon S3, and Amazon SNS\.

**To create an execution role**

1. Open the [**Roles** page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties:
   + For **Trusted entity**, choose **Lambda**\.
   + For **Role name**, enter **lambda\-cloudtrail\-role**\.
   + For **Permissions**, create a custom policy with the following statements\. Replace the highlighted values with the names of your bucket and topic\.

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

1. Record the role ARN\. You will need it when you create the Lambda function\.

## Step 4: Creating the Lambda function<a name="with-cloudtrail-example-create-function"></a>

The following Lambda function processes CloudTrail logs, and sends a notification through Amazon SNS when a new Amazon SNS topic is created\.

**To create the function**

1. Create a folder and give it a name that indicates that it's your Lambda function \(for example, *lambda\-cloudtrail*\)\.

1. In the folder, create a file named `index.js`\.

1. Paste the following code into `index.js`\. Replace the Amazon SNS topic ARN with the ARN that Amazon S3 created when you created the Amazon SNS topic\.

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

1. In the *lambda\-cloudtrail* folder, run the following script\. It creates a `package-lock.json` file and a `node_modules` folder, which handle all dependencies\.

   ```
   $ npm install async
   ```

1. Run the following script to create a deployment package\.

   ```
   $ zip -r function.zip .
   ```

1. Create a Lambda function named CloudTrailEventProcessing with the `create-function` command by running the following script\. Make the indicated replacements\.

   ```
   $ aws lambda create-function --function-name CloudTrailEventProcessing \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x --timeout 10 --memory-size 1024 \
   --role arn:aws:iam::123456789012:role/lambda-cloudtrail-role
   ```

## Step 5: Adding permissions to the Lambda function policy<a name="with-cloudtrail-example-configure-event-source-add-permission"></a>

The Lambda function's resource policy needs permissions to allow Amazon S3 to invoke the function\.

**To give Amazon S3 permissions to invoke the function**

1. Run the following `add-permission` command\. Replace the ARN and account ID with your own\.

   ```
   $ aws lambda add-permission --function-name CloudTrailEventProcessing \
   --statement-id Id-1 --action "lambda:InvokeFunction" --principal s3.amazonaws.com \
   --source-arn arn:aws:s3:::my-bucket \
   --source-account 123456789012
   ```

   This command grants the Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Invoke permissions are granted to Amazon S3 only if the following conditions are met:
   + CloudTrail stores a log object in the specified bucket\.
   + The bucket is owned by the specified AWS account\. If the bucket owner deletes a bucket, another AWS account can create a bucket with the same name\. This condition ensures that only the specified AWS account can invoke your Lambda function\.

1. To view the Lambda function's access policy, run the following `get-policy` command, and replace the function name\.

   ```
   $ aws lambda get-policy --function-name function-name
   ```

## Step 6: Configuring notifications on an Amazon S3 bucket<a name="with-cloudtrail-example-configure-event-source-add-notif-config"></a>

To request that Amazon S3 publishes object\-created events to Lambda, add a notification configuration to the S3 bucket\. In the configuration, you specify the following:
+ Event type – Any event types that create objects\.
+ Lambda function – The Lambda function that you want Amazon S3 to invoke\.

**To configure notifications**

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Choose the source bucket\.

1. Choose **Properties**\.

1. Under **Events**, configure a notification with the following settings:
   + **Name** – **lambda\-trigger**
   + **Events** – **All object create events**
   + **Send to** – **Lambda function**
   + **Lambda** – **CloudTrailEventProcessing**

When CloudTrail stores logs in the bucket, Amazon S3 sends an event to the function\. The event provides information, including the bucket name and key name of the log object that CloudTrail created\.