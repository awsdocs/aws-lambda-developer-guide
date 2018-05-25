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

     
+ **Stream\-based event sources** – For stream\-based event sources \(Amazon Kinesis Data Streams and DynamoDB streams\), AWS Lambda polls your stream and invokes your Lambda function\. Therefore, if a Lambda function fails, AWS Lambda attempts to process the erring batch of records until the time the data expires, which can be up to seven days for Amazon Kinesis Data Streams\. The exception is treated as blocking, and AWS Lambda will not read any new records from the stream until the failed batch of records either expires or processed successfully\. This ensures that AWS Lambda processes the stream events in order\.

For more information about invocation modes, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\.