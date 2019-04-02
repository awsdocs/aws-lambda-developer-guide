# Using AWS Lambda With Other Services<a name="use-cases"></a>

The use cases for AWS Lambda can be grouped into the following categories:
+ **Using AWS Lambda with AWS services as event sources** – *Event sources* publish events that cause the Lambda function to be invoked\. These can be AWS services such as Amazon S3\. For more information and tutorials, see the following topics:

   [Using AWS Lambda with Amazon S3](with-s3.md) 

   [Using AWS Lambda with Amazon Kinesis](with-kinesis.md) 

   [Using AWS Lambda with Amazon SQS](with-sqs.md) 

   [Using AWS Lambda with Amazon DynamoDB](with-ddb.md) 

   [Using AWS Lambda with AWS CloudTrail](with-cloudtrail.md) 

   [Using AWS Lambda with Amazon SNS from Different Accounts](with-sns.md) 
+ **On\-demand Lambda function invocation over HTTPS \(Amazon API Gateway\)** – In addition to invoking Lambda functions using event sources, you can also invoke your Lambda function over HTTPS\. You can do this by defining a custom REST API and endpoint using API Gateway\. For more information and a tutorial, see [Using AWS Lambda with Amazon API Gateway](with-on-demand-https.md)\.
+ **On\-demand Lambda function invocation \(build your own event sources using custom apps\)** – User applications such as client, mobile, or web applications can publish events and invoke Lambda functions using the AWS SDKs or AWS Mobile SDKs, such as the AWS Mobile SDK for Android\. For more information and a tutorial, see [Getting Started with AWS Lambda](getting-started.md) and [Using AWS Lambda as an Android Mobile Application Backend](with-on-demand-custom-android.md)
+ **Scheduled events** – You can also set up AWS Lambda to invoke your code on a regular, scheduled basis using the AWS Lambda console\. You can specify a fixed rate \(number of hours, days, or weeks\) or you can specify a cron expression\. For more information and a tutorial, see [Using AWS Lambda with Amazon CloudWatch Events](with-scheduled-events.md)\.

In addition, you also can use a Lambda State Machine\. For more information, see [Using a State Machine](http://docs.aws.amazon.com/step-functions/latest/dg/tutorial-creating-lambda-state-machine.html)\.