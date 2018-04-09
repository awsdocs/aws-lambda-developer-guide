# Step 3: Add an Event Source \(Configure Amazon S3 to Publish Events\)<a name="with-s3-example-configure-event-source"></a>

In this step, you add the remaining configuration so that Amazon S3 can publish object\-created events to AWS Lambda and invoke your Lambda function\. You do the following in this step:
+ Add permissions to the Lambda function access policy to allow Amazon S3 to invoke the function\.
+ Add notification configuration to your source bucket\. In the notification configuration, you provide the following:
  + Event type for which you want Amazon S3 to publish events\. For this tutorial, you specify the `s3:ObjectCreated:*` event type so that Amazon S3 publishes events when objects are created\.
  + Lambda function to invoke\.

## Step 3\.1: Add Permissions to the Lambda Function's Access Permissions Policy<a name="with-s3-example-configure-event-source-add-permission"></a>

1. Run the following Lambda CLI `add-permission` command to grant Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Note that permission is granted to Amazon S3 to invoke the function only if the following conditions are met:
   + An object\-created event is detected on a specific bucket\.
   + The bucket is owned by a specific AWS account\. If a bucket owner deletes a bucket, some other AWS account can create a bucket with the same name\. This condition ensures that only a specific AWS account can invoke your Lambda function\.

   ```
   $ aws lambda add-permission \
   --function-name CreateThumbnail \
   --region region \
   --statement-id some-unique-id \
   --action "lambda:InvokeFunction" \
   --principal s3.amazonaws.com \
   --source-arn arn:aws:s3:::sourcebucket \
   --source-account bucket-owner-account-id \
   --profile adminuser
   ```

1. Verify the function's access policy by running the AWS CLI `get-policy` command\.

   ```
   $ aws lambda get-policy \
   --function-name function-name \
   --profile adminuser
   ```

## Step 3\.2: Configure Notification on the Bucket<a name="with-s3-example-configure-event-source-attach-notification-configuration"></a>

Add notification configuration on the source bucket to request Amazon S3 to publish object\-created events to Lambda\. In the configuration, you specify the following:
+ Event type – For this tutorial, select the `ObjectCreated (All)` Amazon S3 event type\.
+ Lambda function – This is your Lambda function that you want Amazon S3 to invoke\. 

For instructions on adding notification configuration to a bucket, see [Enabling Event Notifications](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/SettingBucketNotifications.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Step 3\.3: Test the Setup<a name="with-s3-example-configure-event-source-test-end-to-end"></a>

You're all done\! Now **adminuser** can test the setup as follows:

1. Upload \.jpg or \.png objects to the source bucket using the Amazon S3 console\.

1. Verify that the thumbnail was created in the target bucket using the `CreateThumbnail` function\.

1. The **adminuser** can also verify the CloudWatch Logs\. You can monitor the activity of your Lambda function in the AWS Lambda console\. For example, choose the **logs** link in the console to view logs, including logs your function wrote to CloudWatch Logs\. 