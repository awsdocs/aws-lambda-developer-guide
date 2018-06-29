# Using AWS Lambda with Amazon SQS<a name="with-sqs"></a>

Attaching an [Amazon SQS queue ](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html) as an AWS Lambda event source is an easy way to process the queue’s content using a Lambda function\. Lambda takes care of: 
+ Automatically retrieving messages and directing them to the target Lambda function\.
+ Deleting them once your Lambda function successfully completes\.

 Most Amazon SQS capabilities, such as DLQ, retry limits, and other features, work as expected\. Once set up as an event source, AWS Lambda polls your Amazon SQS queue and when it detects new messages, invokes a Lambda function by passing the new message\(s\) as a parameter\. Lambda calls SQS [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) and, once your function completes successfully, calls the SQS [DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html) API on your behalf\. You are billed for these APIs calls, just as if you had made them yourself\. For more information on how Lambda scales to process messages in your Amazon SQS queue, see [Understanding Scaling Behavior](scaling.md)\.

You can also customize your Amazon SQS queue attributes to control how and when your Lambda function is invoked using the following Amazon SQS options:
+ [Visibility Timeouts](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html)
+ [Delay Queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-delay-queues.html)

 For more information, see the [Amazon Simple Queue Service Developer Guide](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/)\. 

Note the following about how Amazon Simple Queue Service and AWS Lambda integration works:
+ **Synchronous invocation** – AWS Lambda invokes a Lambda function using the `RequestResponse` invocation type\. For more information about invocation types, see [Invocation Types](invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is a message on the Amazon SQS queue that AWS Lambda reads\. For an example, see [Amazon SQS Event](eventsources.md#eventsources-sqs)\.

 To configure your Lambda function to process these messages, use the following API operations:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)

When using these operations to map your Lambda function to an Amazon SQS queue, note the following configuration parameters:
+ **BatchSize**: The largest number of records that AWS Lambda will retrieve from each [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) call\. The maximum batch size supported by Amazon Simple Queue Service is up to 10 queue messages per batch\.
+ **Enabled**: A flag to signal AWS Lambda that it should start polling your specified Amazon SQS queue\. 
+ **EventSourceArn**: The ARN \(Amazon Resource Name\) of your Amazon SQS queue that AWS Lambda is monitoring for new messages\. 
+ **FunctionName**: The Lambda function to invoke when AWS Lambda detects new messages on your configured Amazon SQS queue\. 

**Important**  
Amazon Simple Queue Service supports both Standard and FIFO queues\. AWS Lambda supports only standard queues\. For more information on the difference, see [What Type of Queue Do I Need?](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html#sqs-queue-types)

## Next Step<a name="wt-sqs-next-step-1"></a>

[Tutorial: Using AWS Lambda with Amazon Simple Queue Service](with-sqs-example.md)