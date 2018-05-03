# Using AWS Lambda with Amazon SQS<a name="with-sqs"></a>

If you want to build asynchronous workflows, you can configure your Lambda function to process queued messages from [Amazon SQS \(Simple Queue Service\)](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html)\. AWS Lambda polls the Amazon SQS queue, reads the new messages and invokes any Lambda function subscribed to that queue\. 

You can also customize your Amazon SQS queue attributes to control how and when your Lambda function is invoked\. For example:
+ [Visibility Timeouts](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html)
+ [Delay Queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-delay-queues.html)

 For more information, see [Amazon SQS](https://aws.amazon.com/sqs/)\. 

Note the following about how Amazon Simple Queue Service and AWS Lambda integration works:
+ **Synchronous invocation** – AWS Lambda invokes a Lambda function using the `RequestResponse` invocation type\. For more information about invocation types, see [Invocation Types](java-invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is a new message on the Amazon SQS queue that AWS Lambda reads\. For an example, see [Amazon SQS Event](eventsources.md#eventsources-sqs)\.

 To configure your Lambda function to process these messages, use the following API operations:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)

When using these operations to map your Lambda function to an Amazon SQS queue, note the the following configuration parameters:
+ **BatchSize**: The largest number of records that AWS Lambda will retrieve from each [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) call\. The maximum batch size supported by Amazon Simple Queue Service is up to 10 queue messages per batch\. The maximum allowed individual message size and the maximum total payload size \(the sum of the individual lengths of all of the batched messages\) are both 256 KB \(262,144 bytes\)\. The default setting is 1\.
+ **Enabled**: A flag to signal AWS Lambda that it should start polling your specified Amazon SQS queue\. 
+ **EventSourceArn**: The ARN \(Amazon Resource Name\) of your Amazon SQS queue that AWS Lambda is monitoring for new messages\. 
+ **FunctionName**: The Lambda function to invoke when AWS Lambda detects new messages on your configured Amazon SQS queue\. 

**Important**  
Amazon Simple Queue Service supports both Standard and FIFO queues\. For this release, only Standard queues are supported\. For more information on the difference, see [What is Amazon Simple Queue Service?](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html)

Regardless of the event source that invokes a Lambda function, AWS Lambda will invoke it on your behalf\. If your Lambda function needs to access any other AWS resources, you need to grant the relevant permissions to access those resources\.

 You specifically need to grant AWS Lambda permissions to poll your Amazon SQS queue\. You grant these permissions to an IAM role \(execution role\) that AWS Lambda can assume to poll the queue and execute the Lambda function\. **NOTE TO REVIEWERS: MORE INFORMATION ON THIS AND A LINK TO THIS SPECIFIC USE WILL BE ADDED PRIOR TO TEAM\-WIDE DOC BASH:** You create this role first and then enable it at the time you create the Lambda function\. For more information, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.