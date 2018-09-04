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

## Implementation Summary<a name="with-cloudtrail-example-impl-summary"></a>

Upon completing this tutorial, you will have Amazon S3, AWS Lambda, Amazon SNS, and AWS Identity and Access Management \(IAM\) resources in your account:

**Note**  
This tutorial assumes that you create these resources in the `us-west-2` region\.

In Lambda:
+ A Lambda function\.
+  An access policy associated with your Lambda function – You grant Amazon S3 permissions to invoke the Lambda function using this permissions policy\. You will also restrict the permissions so that Amazon S3 can invoke the Lambda function only for object\-created events from a specific bucket that is owned by a specific AWS account\. 
**Note**  
 It is possible for an AWS account to delete a bucket and some other AWS account to later create a bucket with same name\. The additional conditions ensure that Amazon S3 can invoke the Lambda function only if Amazon S3 detects object\-created events from a specific bucket owned by a specific AWS account\. 

In IAM:
+  An IAM role \(execution role\) – You grant permissions that your Lambda function needs through the permissions policy associated with this role\. 

In Amazon S3:
+ A bucket – In this tutorial, the bucket name is *examplebucket*\. When you turn the trail on in the CloudTrail console, you specify this bucket for CloudTrail to save the logs\. 
+ Notification configuration on the *examplebucket* – In the configuration, you direct Amazon S3 to publish object\-created events to Lambda, by invoking your Lambda function\. For more information about the Amazon S3 notification feature, see [Setting Up Notification of Bucket Events](http://docs.aws.amazon.com/AmazonS3/latest/dev/NotificationHowTo.html) in *Amazon Simple Storage Service Developer Guide*\.
+ Sample CloudTrail log object \(`ExampleCloudTrailLog.json`\) in *examplebucket* bucket – In the first half of this exercise, you create and test your Lambda function by manually invoking it using a sample S3 event\. This sample event identifies *examplebucket* as the bucket name and this sample object key name\. Your Lambda function then reads the object and sends you email notifications using an SNS topic\. 

In Amazon SNS
+ An SNS topic – You subscribe to this topic by specifying email as the protocol\.

Now you are ready to start the tutorial\. 

## Next Step<a name="with-cloudtrail-example-impl-summary-next-step"></a>

[Step 1: Prepare](with-cloudtrail-example-prepare.md)