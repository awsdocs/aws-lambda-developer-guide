# Using AWS Lambda with Amazon SQS<a name="with-sqs"></a>

If you want to build asynchronous workflows, you can configure your Lambda function to process queued messages from [Amazon SQS \(Simple Queue Service\)](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html)\. AWS Lambda polls the Amazon SQS queue, reads the new messages and invokes any Lambda function subscribed to that queue\. 

You can also customize your Amazon SQS queue attributes to control how and when your Lambda function is invoked\. For example:
+ [Visibility Timeouts](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html)
+ [Delay Queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-delay-queues.html)

 For more information, see [Amazon SQS](https://aws.amazon.com/sqs/)\. 

Note the following about how Amazon Simple Queue Service and AWS Lambda integration works:
+ **Synchronous invocation** – AWS Lambda invokes a Lambda function using the `RequestResponse` invocation type\. For more information about invocation types, see 