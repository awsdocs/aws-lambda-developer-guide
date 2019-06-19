# Invocation Types<a name="invocation-options"></a>

AWS Lambda supports synchronous and asynchronous invocation of a Lambda function\. You can control the invocation type only when you invoke a Lambda function \(referred to as *on\-demand invocation*\)\. The following examples illustrate on\-demand invocations:
+ Your custom application invokes a Lambda function\.
+ You manually invoke a Lambda function \(for example, using the AWS CLI\) for testing purposes\.

In both cases, you invoke your Lambda function using the [Invoke](API_Invoke.md) operation, and you can specify the invocation type as synchronous or asynchronous\.

When you use AWS services as a trigger, the invocation type is predetermined for each service\. You have no control over the invocation type that these event sources use when they invoke your Lambda function\.

For example, Amazon S3 always invokes a Lambda function asynchronously and Amazon Cognito always invokes a Lambda function synchronously\. For poll\-based AWS services \(Amazon Kinesis, Amazon DynamoDB, Amazon Simple Queue Service\), AWS Lambda polls the stream or message queue and invokes your Lambda function synchronously\.