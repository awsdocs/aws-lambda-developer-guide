# Understanding Scaling Behavior<a name="scaling"></a>

Concurrent executions refers to the number of executions of your function code that are happening at any given time\. You can estimate the concurrent execution count, but the concurrent execution count will differ depending on whether or not your Lambda function is processing events from a stream\-based event source\. 
+ **Poll\-based event sources that are stream\-based**
  + Amazon Kinesis Data Streams
  + Amazon DynamoDB

  For Lambda functions that process Kinesis or DynamoDB streams the number of shards is the unit of concurrency\. If your stream has 100 active shards, there will be at most 100 Lambda function invocations running concurrently\. This is because Lambda processes each shard’s events in sequence\. **Poll\-based event sources that are not stream\-based**: For Lambda functions that process Amazon SQS queues, AWS Lambda will automatically scale the polling on the queue until the maximum concurrency level is reached, where each message batch can be considered a single concurrent unit\. AWS Lambda's automatic scaling behavior is designed to keep polling costs low when a queue is empty while simultaneously enabling you to achieve high throughput when the queue is being used heavily\. 

  Here is how it works:
  + When an Amazon SQS event source mapping is initially enabled, Lambda begins long\-polling the Amazon SQS queue\. Long polling helps reduce the cost of polling Amazon Simple Queue Service by reducing the number of empty responses, while proving optimal processing latency when messages arrive\. As the influx of messages to a queue increases, AWS Lambda automatically scales up polling activity until the number of concurrent function executions reaches 1000, the account concurrency limit, or the \(optional\) function concurrency limit, whichever is lower\. SQS Event Sources support an initial burst of 5 concurrent function invocations and increase concurrency by 60 concurrent invocations per minute\.
  + Lambda monitors the number of [inflight messages](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html#inflight-messages), and when it detects that this number is increasing, it will increase the polling frequency by 20 [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) requests per minute and the function concurrency by 60 calls per minute\. As long as the queue remains busy, scale up continues until at least one of the following occurs:
    + Polling frequency reaches 100 simultaneous ReceiveMessage requests and function invocation concurrency reaches 1,000\.
    + The account concurrency maximum has been reached\.
    + The per\-function concurrency limit of the function attached to the SQS queue \(if any\) has been reached\.

  When AWS Lambda detects that the number of inflight messages is decreasing, it will decrease the polling frequency by 10 ReceiveMessage requests per minute and decrease the concurrency used to invoke your function by 30 calls per minute\. 
**Note**  
[Account\-level limits](http://docs.aws.amazon.com/lambda/latest/dg/limits.html) are impacted by other functions in the account, and per\-function concurrency applies to all events sent to a function\. For more information, see [Managing Concurrency](concurrent-executions.md)\.
+ **Event sources that aren't stream\-based** – If you create a Lambda function to process events from event sources that aren't stream\-based \(for example, Lambda can process every event from other sources, like Amazon S3 or API Gateway\), each published event is a unit of work, in parallel, up to your account limits\. Therefore, the number of events \(or requests\) these event sources publish influences the concurrency\. You can use the this formula to estimate your concurrent Lambda function invocations:

  ```
  events (or requests) per second * function duration
  ```

  For example, consider a Lambda function that processes Amazon S3 events\. Suppose that the Lambda function takes on average three seconds and Amazon S3 publishes 10 events per second\. Then, you will have 30 concurrent executions of your Lambda function\.

## Request Rate<a name="concurrent-executions-request-rate"></a>

Request rate refers to the rate at which your Lambda function is invoked\. For all services except the stream\-based services, the request rate is the rate at which the event sources generate the events\. For stream\-based services, AWS Lambda calculates the request rate as follow:

```
request rate = number of concurrent executions / function duration
```

For example, if there are five active shards on a stream \(that is, you have five Lambda functions running in parallel\) and your Lambda function takes about two seconds, the request rate is 2\.5 requests/second\.

## Scaling<a name="scaling-behavior"></a>

AWS Lambda will dynamically scale capacity in response to increased traffic, subject to your account's [Account Level Concurrent Execution Limit](concurrent-executions.md#concurrent-execution-safety-limit)\. To handle any burst in traffic, Lambda will immediately increase your concurrently executing functions by a predetermined amount, dependent on which region it's executed\.

 If the default **Immediate Concurrency Increase** value is not sufficient to accommodate the traffic surge, AWS Lambda will continue to increase the number of concurrent function executions by **500 per minute** until your account safety limit has been reached or the number of concurrently executing functions is sufficient to successfully process the increased load\. 

See [AWS Lambda Limits](limits.md) for Immediate Concurrency Increase limits for all regions\.

**Note**  
Because Lambda depends on Amazon EC2 to provide Elastic Network Interfaces for VPC\-enabled Lambda functions, these functions are also subject to Amazon EC2's rate limits as they scale\. If your Amazon EC2 rate limits prevent VPC\-enabled functions from adding **500 concurrent invocations per minute**, please request a limit increase by following the instructions on the [AWS Lambda Limits](limits.md) page\.  
Beyond this rate \(i\.e\. for applications taking advantage of the full Immediate concurrency increase\), your application should handle Amazon EC2 throttling \(502 EC2ThrottledException\) through client\-side retry and backoff\. For more details, see [Error Retries and Exponential Backoff in AWS](http://docs.aws.amazon.com/general/latest/gr/api-retries.html)\.

To learn how to view and manage the concurrent executions for your function, see [Managing Concurrency](concurrent-executions.md)