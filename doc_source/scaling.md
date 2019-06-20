# Understanding Scaling Behavior<a name="scaling"></a>

Concurrent executions refers to the number of executions of your function code that are happening at any given time\. You can estimate the concurrent execution count, but the concurrent execution count will differ depending on whether or not your Lambda function is processing events from a poll\-based event source\. 

If you create a Lambda function to process events from event sources that aren't poll\-based \(for example, Lambda can process every event from other sources, like Amazon S3 or API Gateway\), each published event is a unit of work, in parallel, up to your account limits\. Therefore, the number of invocations these event sources make influences the concurrency\. You can use this formula to estimate the capacity used by your function:

```
invocations per second * average execution duration in seconds
```

For example, consider a Lambda function that processes Amazon S3 events\. Suppose that the Lambda function takes on average three seconds and Amazon S3 publishes 10 events per second\. Then, you will have 30 concurrent executions of your Lambda function\.

The number of concurrent executions for poll\-based event sources also depends on additional factors, as noted following:
+  **Poll\-based event sources that are stream\-based** 
  + Amazon Kinesis Data Streams
  + Amazon DynamoDB

  For Lambda functions that process Kinesis or DynamoDB streams the number of shards is the unit of concurrency\. If your stream has 100 active shards, there will be at most 100 Lambda function invocations running concurrently\. This is because Lambda processes each shard’s events in sequence\. 
+ **Poll\-based event sources that are not stream\-based**: For Lambda functions that process Amazon SQS queues, AWS Lambda will automatically scale the polling on the queue until the maximum concurrency level is reached, where each message batch can be considered a single concurrent unit\. AWS Lambda's automatic scaling behavior is designed to keep polling costs low when a queue is empty while simultaneously enabling you to achieve high throughput when the queue is being used heavily\. 

  When an Amazon SQS event source mapping is initially enabled, Lambda begins long\-polling the Amazon SQS queue\. Long polling helps reduce the cost of polling Amazon Simple Queue Service by reducing the number of empty responses, while providing optimal processing latency when messages arrive\.

  When messages are available, Lambda initially launches up to 5 instances of your function, to handle 5 batches simultaneously\. Then, Lambda launches up to 60 more instances per minute, up to 1000 total, as long as you have concurrency available at the account and function level\.

  [Account\-level limits](https://docs.aws.amazon.com/lambda/latest/dg/limits.html) are impacted by other functions in the account, and per\-function concurrency applies to all events sent to a function\. 

## Request Rate<a name="concurrent-executions-request-rate"></a>

Request rate refers to the rate at which your Lambda function is invoked\. For all services except the poll\-based services, the request rate is the rate at which the event sources generate the events\. For poll\-based services, AWS Lambda calculates the request rate as follows:

```
request rate = number of concurrent executions / function duration
```

For example, if there are five active shards on a stream \(that is, you have five Lambda functions running in parallel\) and your Lambda function takes about two seconds, the request rate is 2\.5 requests/second\.

## Automatic Scaling<a name="scaling-behavior"></a>

AWS Lambda dynamically scales function execution in response to increased traffic, up to your [concurrency limit](limits.md)\. Under sustained load, your function's concurrency bursts to an initial level between 500 and 3000 concurrent executions that varies per region\. After the initial burst, the function's capacity increases by an additional 500 concurrent executions each minute until either the load is accommodated, or the total concurrency of all functions in the region hits the limit\.


****  

| Region | Initial concurrency burst | 
| --- | --- | 
| US East \(Ohio\), US West \(N\. California\), Canada \(Central\) | 500 | 
| US West \(Oregon\), US East \(N\. Virginia\) | 3000 | 
| Asia Pacific \(Seoul\), Asia Pacific \(Mumbai\), Asia Pacific \(Singapore\), Asia Pacific \(Sydney\) | 500 | 
| Asia Pacific \(Tokyo\) | 1000 | 
| EU \(London\), EU \(Paris\), EU \(Stockholm\) | 500 | 
| EU \(Frankfurt\) | 1000 | 
| EU \(Ireland\) | 3000 | 
| South America \(São Paulo\) | 500 | 
| China \(Beijing\), China \(Ningxia\) | 500 | 
| AWS GovCloud \(US\-West\) | 500 | 

**Note**  
If your function is connected to a VPC, the [Amazon VPC network interface limit](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html#limits_vpc) can prevent it from scaling\. For more information, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)\.

To limit scaling, you can configure functions with *reserved concurrency*\. For more information, see [Managing Concurrency](concurrent-executions.md)\.