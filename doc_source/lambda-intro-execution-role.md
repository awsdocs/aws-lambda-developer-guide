# AWS Lambda Execution Role<a name="lambda-intro-execution-role"></a>

An AWS Lambda function's execution role grants it permission to access AWS services and resources\. You provide this role when you create a function, and Lambda assumes the role when your function is invoked\. You can create an execution role for development that has permission to send logs to Amazon CloudWatch, and upload trace data to AWS X\-Ray\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties:
   + **Trusted entity** – **AWS Lambda**
   + **Permissions** – **AWSLambdaBasicExecutionRole**, **AWSXrayWriteOnlyAccess**
   + **Role name** – **lambda\-role**

You can add or remove permissions from a function's execution role at any time, or configure your function to use a different role\. Add permissions for any services that your function calls with the AWS SDK, and for services that Lambda uses to enable optional features\.

The following managed policies provide permissions that are required to use Lambda features:
+ **AWSLambdaBasicExecutionRole** – Permission to upload logs to CloudWatch\.
+ **AWSLambdaKinesisExecutionRole** – Permission to read events from an Amazon Kinesis data stream or consumer\.
+ **AWSLambdaDynamoDBExecutionRole** – Permission to read records from an Amazon DynamoDB stream\.
+ **AWSLambdaSQSQueueExecutionRole** – Permission to read a message from an Amazon Simple Queue Service \(Amazon SQS\) queue\.
+ **AWSLambdaVPCAccessExecutionRole** – Permission to manage elastic network interfaces to connect your function to a VPC\.
+ **AWSXrayWriteOnlyAccess** – Permission to upload trace data to X\-Ray\.

When you use an [event source mapping](intro-invocation-modes.md) to invoke your function, Lambda uses the execution role to read event data\. For example, an event source mapping for Amazon Kinesis reads events from a data stream and sends them to your function in batches\. You can use event source mappings with the following services:

**Services That Lambda Reads Events From**
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Simple Queue Service](with-sqs.md)

In addition to the managed policies, the Lambda console provides templates for creating a custom policy that has the permissions related to additional use cases\. When you create a function, you can choose to create a new execution role with permissions from one or more templates\. These templates are also applied automatically when you create a function from a blueprint, or when you configure options that require access to other services\. Example templates are available in this guide's [GitHub repository](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/iam-policies)\.