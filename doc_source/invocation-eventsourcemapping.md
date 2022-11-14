# Lambda event source mappings<a name="invocation-eventsourcemapping"></a>

An event source mapping is a Lambda resource that reads from an event source and invokes a Lambda function\. You can use event source mappings to process items from a stream or queue in services that don't invoke Lambda functions directly\. Lambda provides event source mappings for the following services\.

**Services that Lambda reads events from**
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon MQ](with-mq.md)
+ [Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](with-msk.md)
+ [Self\-managed Apache Kafka](with-kafka.md)
+ [Amazon Simple Queue Service \(Amazon SQS\)](with-sqs.md)

An event source mapping uses permissions in the function's [execution role](lambda-intro-execution-role.md) to read and manage items in the event source\. Permissions, event structure, settings, and polling behavior vary by event source\. For more information, see the linked topic for the service that you use as an event source\.

To manage an event source with the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+  [CreateEventSourceMapping](API_CreateEventSourceMapping.md) 
+  [ListEventSourceMappings](API_ListEventSourceMappings.md) 
+  [GetEventSourceMapping](API_GetEventSourceMapping.md) 
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) 
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md) 

The following example uses the AWS CLI to map a function named `my-function` to a DynamoDB stream that its Amazon Resource Name \(ARN\) specifies, with a batch size of 500\.

```
aws lambda create-event-source-mapping --function-name my-function --batch-size 500 --maximum-batching-window-in-seconds 5 --starting-position LATEST \
--event-source-arn arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525
```

You should see the following output:

```
{
    "UUID": "14e0db71-5d35-4eb5-b481-8945cf9d10c2",
    "BatchSize": 500,
    "MaximumBatchingWindowInSeconds": 5,
    "ParallelizationFactor": 1,
    "EventSourceArn": "arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1560209851.963,
    "LastProcessingResult": "No records processed",
    "State": "Creating",
    "StateTransitionReason": "User action",
    "DestinationConfig": {},
    "MaximumRecordAgeInSeconds": 604800,
    "BisectBatchOnFunctionError": false,
    "MaximumRetryAttempts": 10000
}
```

Lambda event source mappings process events at least once due to the distributed nature of its pollers\. As a result, your Lambda function may receive duplicate events in rare situations\. Follow [Best practices for working with AWS Lambda functions](best-practices.md) and build idempotent functions to avoid issues related to duplicate events\.

## Batching behavior<a name="invocation-eventsourcemapping-batching"></a>

Event source mappings read items from a target event source\. By default, an event source mapping batches records together into a single payload that Lambda sends to your function\. To fine\-tune batching behavior, you can configure a batching window \(`MaximumBatchingWindowInSeconds`\) and a batch size \(`BatchSize`\)\. A batching window is the maximum amount of time to gather records into a single payload\. A batch size is the maximum number of records in a single batch\. Lambda invokes your function when one of the following three criteria is met:
+ **The batching window reaches its maximum value\.** Batching window behavior varies depending on the specific event source\.
  + **For Kinesis, DynamoDB, and Amazon SQS event sources:** The default batching window is 0 seconds\. This means that Lambda sends batches to your function as quickly as possible\. If you configure a `MaximumBatchingWindowInSeconds`, the next batching window begins as soon as the previous function invocation completes\.
  + **For Amazon MSK, self\-managed Apache Kafka, and Amazon MQ event sources:** The default batching window is 500 ms\. You can configure `MaximumBatchingWindowInSeconds` to any value from 0 seconds to 300 seconds in increments of seconds\. A batching window begins as soon as the first record arrives\.
**Note**  
Because you can only change `MaximumBatchingWindowInSeconds` in increments of seconds, you cannot revert back to the 500 ms default batching window after you have changed it\. To restore the default batching window, you must create a new event source mapping\.
+ **The batch size is met\.** The minimum batch size is 1\. The default and maximum batch size depend on the event source\. For details about these values, see the [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize) specification for the `CreateEventSourceMapping` API operation\.
+ **The payload size reaches [6 MB](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-limits.html)\.** You cannot modify this limit\.

The following diagram illustrates these three conditions\. Suppose a batching window begins at `t = 7` seconds\. In the first scenario, the batching window reaches its 40 second maximum at `t = 47` seconds after accumulating 5 records\. In the second scenario, the batch size reaches 10 before the batching window expires, so the batching window ends early\. In the third scenario, the maximum payload size is reached before the batching window expires, so the batching window ends early\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/batching-window.png)

The following example shows an event source mapping that reads from a Kinesis stream\. If a batch of events fails all processing attempts, the event source mapping sends details about the batch to an SQS queue\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-eventsourcemapping.png)

The event batch is the event that Lambda sends to the function\. It is a batch of records or messages compiled from the items that the event source mapping reads up until the current batching window expires\.

For streams, an event source mapping creates an iterator for each shard in the stream and processes items in each shard in order\. You can configure the event source mapping to read only new items that appear in the stream, or to start with older items\. Processed items aren't removed from the stream, and other functions or consumers can process them\.

By default, if your function returns an error, the event source mapping reprocesses the entire batch until the function succeeds, or the items in the batch expire\. To ensure in\-order processing, the event source mapping pauses processing for the affected shard until the error is resolved\. You can configure the event source mapping to discard old events, restrict the number of retries, or process multiple batches in parallel\. If you process multiple batches in parallel, in\-order processing is still guaranteed for each partition key, but the event source mapping simultaneously processes multiple partition keys in the same shard\.

You can also configure the event source mapping to send an invocation record to another service when it discards an event batch\. Lambda supports the following [destinations](invocation-async.md#invocation-async-destinations) for event source mappings\.
+ **Amazon SQS** – An SQS queue\.
+ **Amazon SNS** – An SNS topic\.

The invocation record contains details about the failed event batch in JSON format\.

The following example shows an invocation record for a Kinesis stream\.

**Example invocation record**  

```
{
    "requestContext": {
        "requestId": "c9b8fa9f-5a7f-xmpl-af9c-0c604cde93a5",
        "functionArn": "arn:aws:lambda:us-east-2:123456789012:function:myfunction",
        "condition": "RetryAttemptsExhausted",
        "approximateInvokeCount": 1
    },
    "responseContext": {
        "statusCode": 200,
        "executedVersion": "$LATEST",
        "functionError": "Unhandled"
    },
    "version": "1.0",
    "timestamp": "2019-11-14T00:38:06.021Z",
    "KinesisBatchInfo": {
        "shardId": "shardId-000000000001",
        "startSequenceNumber": "49601189658422359378836298521827638475320189012309704722",
        "endSequenceNumber": "49601189658422359378836298522902373528957594348623495186",
        "approximateArrivalOfFirstRecord": "2019-11-14T00:38:04.835Z",
        "approximateArrivalOfLastRecord": "2019-11-14T00:38:05.580Z",
        "batchSize": 500,
        "streamArn": "arn:aws:kinesis:us-east-2:123456789012:stream/mystream"
    }
}
```

Lambda also supports in\-order processing for [FIFO \(first\-in, first\-out\) queues](with-sqs.md), scaling up to the number of active message groups\. For standard queues, items aren't necessarily processed in order\. Lambda scales up to process a standard queue as quickly as possible\. When an error occurs, Lambda returns batches to the queue as individual items and might process them in a different grouping than the original batch\. Occasionally, the event source mapping might receive the same item from the queue twice, even if no function error occurred\. Lambda deletes items from the queue after they're processed successfully\. You can configure the source queue to send items to a dead\-letter queue if Lambda can't process them\.

For information about services that invoke Lambda functions directly, see [Using AWS Lambda with other services](lambda-services.md)\.