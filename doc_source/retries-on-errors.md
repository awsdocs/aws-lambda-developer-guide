# AWS Lambda Retry Behavior<a name="retries-on-errors"></a>

Function invocation can result in an error for several reasons\. Your code might raise an exception, time out, or run out of memory\. The runtime executing your code might encounter an error and stop\. You might run out concurrency and be throttled\.

When an error occurs, your code might have run completely, partially, or not at all\. In most cases, the client or service that invokes your function retries if it encounters an error, so your code must be able to process the same event repeatedly without unwanted effects\. If your function manages resources or writes to a database, you need to handle cases where the same request is made several times\.

Lambda handles retries in the following manner, depending on the source of the invocation\.
+ **Event sources that aren't stream\-based** – Some of these event sources are set up to invoke a Lambda function synchronously and others invoke it asynchronously\. Accordingly, exceptions are handled as follows:
  + **Synchronous invocation** – Lambda includes the `FunctionError` field in the response body, with details about the error in the `X-Amz-Function-Error` header\. The status code is 200 for function errors\. Lambda only returns error status codes if there is an issue with the request, function, or permissions that prevents the handler from processing the event\. See [Invoke Errors](API_Invoke.md#API_Invoke_Errors) for details\.

    [AWS service triggers](lambda-services.md) can retry depending on the service\. If you invoke the Lambda function directly from your application, you can choose whether to retry or not\.
  + **Asynchronous invocation** – Asynchronous events are queued before being used to invoke the Lambda function\. If AWS Lambda is unable to fully process the event, it will automatically retry the invocation twice, with delays between retries\. Configure a [dead letter queue](dlq.md) for your function to capture requests that fail all three attempts\.
+ **Poll\-based event sources that are stream\-based** – These consist of Kinesis Data Streams or DynamoDB\. When a Lambda function invocation fails, AWS Lambda attempts to process the erring batch of records until the time the data expires, which can be up to seven days\. 

  The exception is treated as blocking, and AWS Lambda will not read any new records from the shard until the failed batch of records either expires or is processed successfully\. This ensures that AWS Lambda processes the stream events in order\.
+ **Poll\-based event sources that are not stream\-based** – This consists of Amazon Simple Queue Service\. If you configure an Amazon SQS queue as an event source, AWS Lambda will poll a batch of records in the queue and invoke your Lambda function\. If the invocation fails or times out, every message in the batch will be returned to the queue, and each will be available for processing once the [Visibility Timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html) period expires\. \(Visibility timeouts are a period of time during which Amazon Simple Queue Service prevents other consumers from receiving and processing the message\)\.

  Once an invocation successfully processes a batch, each message in that batch will be removed from the queue\. When a message is not successfully processed, it is either discarded or if you have configured an [Amazon SQS Dead Letter Queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configure-dead-letter-queue.html), the failure information will be directed there for you to analyze\.

If you don't require ordered processing of events, the advantage of using Amazon SQS queues is that AWS Lambda will continue to process new messages, regardless of a failed invocation of a previous message\. In other words, processing of new messages will not be blocked\. 

For more information about invocation modes, see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\.