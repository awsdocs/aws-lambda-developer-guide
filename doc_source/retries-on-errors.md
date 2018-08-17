# Understanding Retry Behavior<a name="retries-on-errors"></a>

A Lambda function can fail for any of the following reasons:
+ The function times out while trying to reach an endpoint\.

   
+ The function fails to successfully parse input data\.

   
+ The function experiences resource constraints, such as out\-of\-memory errors or other timeouts\.

If any of these failures occur, your function will throw an exception\. How the exception is handled depends upon how the Lambda function was invoked:
+ **Event sources that aren't stream\-based** – Some of these event sources are set up to invoke a Lambda function synchronously and others invoke it asynchronously\. Accordingly, exceptions are handled as follows:

   
  + **Synchronous invocation** – The invoking application receives a 429 error and is responsible for retries\. For a list of supported event sources and the invocation types they use, see [Supported Event Sources](http://docs.aws.amazon.com/lambda/latest/dg/invoking-lambda-function.html)\. These event sources may have additional retries built into the integration\. 

    If you invoked the Lambda function directly through AWS SDKs, your client receives the error and can choose to retry\.

     
  + **Asynchronous invocation** – Asynchronous events are queued before being used to invoke the Lambda function\. If AWS Lambda is unable to fully process the event, it will automatically retry the invocation twice, with delays between retries\. If you have specified a Dead Letter Queue for your function, then the failed event is sent to the specified Amazon SQS queue or Amazon SNS topic\. If you don't specify a Dead Letter Queue \(DLQ\), which is not required and is the default setting, then the event will be discarded\. For more information, see [Dead Letter Queues](dlq.md)\. 

     
+ **Poll\-based \(or pull model\) event sources that are stream\-based**: These consist of Kinesis Data Streams or DynamoDB\. When a Lambda function invocation fails, AWS Lambda attempts to process the erring batch of records until the time the data expires, which can be up to seven days\. 

  The exception is treated as blocking, and AWS Lambda will not read any new records from the shard until the failed batch of records either expires or is processed successfully\. This ensures that AWS Lambda processes the stream events in order\.
+ **Poll\-based event sources that are not stream\-based:** This consists of Amazon Simple Queue Service\. If you configure an Amazon SQS queue as an event source, AWS Lambda will poll a batch of records in the queue and invoke your Lambda function\. If the invocation fails or times out, every message in the batch will be returned to the queue, and each will be available for processing once the [Visibility Timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html) period expires\. \(Visibility timeouts are a period of time during which Amazon Simple Queue Service prevents other consumers from receiving and processing the message\)\.

   Once an invocation successfully processes a batch, each message in that batch will be removed from the queue\. When a message is not successfully processed, it is either discarded or if you have configured an [Amazon SQS Dead Letter Queue](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configure-dead-letter-queue.html), the failure information will be directed there for you to analyze\. 

If you don't require ordered processing of events, the advantage of using Amazon SQS queues is that AWS Lambda will continue to process new messages, regardless of a failed invocation of a previous message\. In other words, processing of new messages will not be blocked\. 

For more information about invocation modes, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\.