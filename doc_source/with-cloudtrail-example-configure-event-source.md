# Step 3: Add Event Source \(Configure Amazon S3 to Publish Events\)<a name="with-cloudtrail-example-configure-event-source"></a>

 In this section, you add the remaining configuration so Amazon S3 can publish object\-created events to AWS Lambda and invoke your Lambda function\. You will do the following:

+ Add permissions to the Lambda function's access policy to allow Amazon S3 to invoke the function\.

+  Add notification configuration to your source bucket\. In the notification configuration, you provide the following: 

  + Event type for which you want Amazon S3 to publish events\. For this tutorial, you specify the `s3:ObjectCreated:*` event type\.

  + Lambda function to invoke\.

## Step 3\.1: Add Permissions to the Lambda Function's Access Permissions Policy<a name="with-cloudtrail-example-configure-event-source-add-permission"></a>

1. Run the following Lambda CLI `add-permission` command to grant Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Note that permission is granted to Amazon S3 to invoke the function only if the following conditions are met:

   + An object\-created event is detected on a specific bucket\.

   + The bucket is owned by a specific AWS account\. If a bucket owner deletes a bucket, some other AWS account can create a bucket with the same name\. This condition ensures that only a specific AWS account can invoke your Lambda function\.
**Note**  
If you have not already created the `adminuser` profile, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)\.

   ```
   $ aws lambda add-permission \
   --function-name CloudTrailEventProcessing \
   --region region \
   --statement-id Id-1 \
   --action "lambda:InvokeFunction" \
   --principal s3.amazonaws.com \
   --source-arn arn:aws:s3:::examplebucket \
   --source-account examplebucket-owner-account-id \
   --profile adminuser
   ```

1. Verify the function's access policy by running the AWS CLI `get-policy` command\.

   ```
   $ aws lambda get-policy \
   --function-name function-name \
   --profile adminuser
   ```

## Step 3\.2: Configure Notification on the Bucket<a name="with-cloudtrail-example-configure-event-source-add-notif-config"></a>

Add notification configuration on the *examplebucket* to request Amazon S3 to publish object\-created events to Lambda\. In the configuration, you specify the following:

+ Event type – For this tutorial, these can be any event types that create objects\.

+ Lambda function ARN – This is your Lambda function that you want Amazon S3 to invoke\. The ARN is of the following form:

  ```
  arn:aws:lambda:aws-region:account-id:function:function-name
  ```

  For example, the function `CloudTrailEventProcessing` created in us\-west\-2 region has the following ARN:

  ```
  arn:aws:lambda:us-west-2:account-id:function:CloudTrailEventProcessing
  ```

For instructions on adding notification configuration to a bucket, see [Enabling Event Notifications](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/SettingBucketNotifications.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Step 3\.3: Test the Setup<a name="with-cloudtrail-example-final-integration-test-no-iam"></a>

You're all done\! Now you can test the setup as follows:

1. Perform some action in your AWS account\. For example, add another topic in the Amazon SNS console\.

1. You receive an email notification about this event\. 

1. AWS CloudTrail creates a log object in your bucket\.

1. If you open the log object \(\.gz file\), the log shows the `CreateTopic` SNS event\.

1. For each object AWS CloudTrail creates, Amazon S3 invokes your Lambda function by passing in the log object as event data\.

1. Lambda executes your function\. The function parses the log, finds a `CreateTopic` SNS event, and then you receive an email notification\. 

   You can monitor the activity of your Lambda function by using CloudWatch metrics and logs\. For more information about CloudWatch monitoring, see [Using Amazon CloudWatch](monitoring-functions.md)\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/wt-cloudtrail-200.png)