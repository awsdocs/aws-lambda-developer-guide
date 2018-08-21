# Using AWS Lambda with AWS CloudTrail<a name="with-cloudtrail"></a>

AWS CloudTrail is a service that provides a record of actions taken by a user, role, or an AWS service\. CloudTrail captures API calls as events\. For an ongoing record of events in your AWS account, you create a trail\. A trail enables CloudTrail to deliver log files of events to an Amazon S3 bucket\.

You can take advantage of Amazon S3's bucket notification feature and direct Amazon S3 to publish object\-created events to AWS Lambda\. Whenever CloudTrail writes logs to your S3 bucket, Amazon S3 can then invoke your Lambda function by passing the Amazon S3 object\-created event as a parameter\. The S3 event provides information, including the bucket name and key name of the log object that CloudTrail created\. Your Lambda function code can read the log object and process the access records logged by CloudTrail\. For example, you might write Lambda function code to notify you if specific API call was made in your account\. 

In this scenario, CloudTrail writes access logs to your S3 bucket\. As for AWS Lambda, Amazon S3 is the event source so Amazon S3 publishes events to AWS Lambda and invokes your Lambda function\. 

**Note**  
Amazon S3 can only support one event destination\.

For detailed information about how to configure Amazon S3 as the event source, see [Using AWS Lambda with Amazon S3](with-s3.md)\.

The following diagram summarizes the flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/wt-cloudtrail-100.png)

1. AWS CloudTrail saves logs to an S3 bucket \(object\-created event\)\.

1. Amazon S3 detects the object\-created event\.

1. Amazon S3 publishes the `s3:ObjectCreated:*` event to AWS Lambda by invoking the Lambda function, as specified in the bucket notification configuration\. Because the Lambda function's access permissions policy includes permissions for Amazon S3 to invoke the function, Amazon S3 can invoke the function\.

1. AWS Lambda executes the Lambda function by assuming the execution role that you specified at the time you created the Lambda function\.

1. The Lambda function reads the Amazon S3 event it receives as a parameter, determines where the CloudTrail object is, reads the CloudTrail object, and then it processes the log records in the CloudTrail object\.

1. If the log includes a record with specific `eventType` and `eventSource` values, it publishes the event to your Amazon SNS topic\. In [Tutorial: Using AWS Lambda with AWS CloudTrail](with-cloudtrail-example.md), you subscribe to the SNS topic using the email protocol, so you get email notifications\.

For a tutorial that walks you through an example scenario, see [Tutorial: Using AWS Lambda with AWS CloudTrail](with-cloudtrail-example.md)\. 