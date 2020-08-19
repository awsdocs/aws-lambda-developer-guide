# Using AWS Lambda with Amazon Kinesis<a name="with-kinesis"></a>

You can use an AWS Lambda function to process records in an [Amazon Kinesis data stream](https://docs.aws.amazon.com/kinesis/latest/dev/amazon-kinesis-streams.html)\. 

A Kinesis data stream is a set of [shards](https://docs.aws.amazon.com/streams/latest/dev/key-concepts.html#shard)\. Each shard contains a sequence of data records\. A **consumer** is an application that processes the data from a Kinesis data stream\. You can map a Lambda function to a shared\-throughput consumer \(standard iterator\), or to a dedicated\-throughput consumer with [enhanced fan\-out](https://docs.aws.amazon.com/kinesis/latest/dev/enhanced-consumers.html)\. 

For standard iterators, Lambda polls each shard in your Kinesis stream for records using HTTP protocol\. The event source mapping shares read throughput with other consumers of the shard\. 

To minimize latency and maximize read throughput, you can create a data stream consumer with enhanced fan\-out\. Stream consumers get a dedicated connection to each shard that doesn't impact other applications reading from the stream\. The dedicated throughput can help if you have many applications reading the same data, or if you're reprocessing a stream with large records\. Kinesis pushes records to Lambda over HTTP/2\. 

 For details about Kinesis data streams, see [Reading Data from Amazon Kinesis Data Streams](https://docs.aws.amazon.com/kinesis/latest/dev/building-consumers.html)\. 

**Note**  
Error handling is not available for HTTP/2 stream consumers\.

Lambda reads records from the data stream and invokes your function [synchronously](invocation-sync.md) with an event that contains stream records\. Lambda reads records in batches and invokes your function to process records from the batch\.

**Example Kinesis record event**  

```
{
    "Records": [
        {
            "kinesis": {
                "kinesisSchemaVersion": "1.0",
                "partitionKey": "1",
                "sequenceNumber": "49590338271490256608559692538361571095921575989136588898",
                "data": "SGVsbG8sIHRoaXMgaXMgYSB0ZXN0Lg==",
                "approximateArrivalTimestamp": 1545084650.987
            },
            "eventSource": "aws:kinesis",
            "eventVersion": "1.0",
            "eventID": "shardId-000000000006:49590338271490256608559692538361571095921575989136588898",
            "eventName": "aws:kinesis:record",
            "invokeIdentityArn": "arn:aws:iam::123456789012:role/lambda-role",
            "awsRegion": "us-east-2",
            "eventSourceARN": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"
        },
        {
            "kinesis": {
                "kinesisSchemaVersion": "1.0",
                "partitionKey": "1",
                "sequenceNumber": "49590338271490256608559692540925702759324208523137515618",
                "data": "VGhpcyBpcyBvbmx5IGEgdGVzdC4=",
                "approximateArrivalTimestamp": 1545084711.166
            },
            "eventSource": "aws:kinesis",
            "eventVersion": "1.0",
            "eventID": "shardId-000000000006:49590338271490256608559692540925702759324208523137515618",
            "eventName": "aws:kinesis:record",
            "invokeIdentityArn": "arn:aws:iam::123456789012:role/lambda-role",
            "awsRegion": "us-east-2",
            "eventSourceARN": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"
        }
    ]
}
```

By default, Lambda invokes your function as soon as records are available in the stream\. If the batch that Lambda reads from the stream only has one record in it, Lambda sends only one record to the function\. To avoid invoking the function with a small number of records, you can tell the event source to buffer records for up to five minutes by configuring a *batch window*\. Before invoking the function, Lambda continues to read records from the stream until it has gathered a full batch, or until the batch window expires\.

If your function returns an error, Lambda retries the batch until processing succeeds or the data expires\. To avoid stalled shards, you can configure the event source mapping to retry with a smaller batch size, limit the number of retries, or discard records that are too old\. To retain discarded events, you can configure the event source mapping to send details about failed batches to an SQS queue or SNS topic\.

You can also increase concurrency by processing multiple batches from each shard in parallel\. Lambda can process up to 10 batches in each shard simultaneously\. If you increase the number of concurrent batches per shard, Lambda still ensures in\-order processing at the partition\-key level\.

**Topics**
+ [Configuring your data stream and function](#services-kinesis-configure)
+ [Execution role permissions](#events-kinesis-permissions)
+ [Configuring a stream as an event source](#services-kinesis-eventsourcemapping)
+ [Event source mapping API](#services-kinesis-api)
+ [Error handling](#services-kinesis-errors)
+ [Amazon CloudWatch metrics](#events-kinesis-metrics)
+ [Tutorial: Using AWS Lambda with Amazon Kinesis](with-kinesis-example.md)
+ [Sample function code](with-kinesis-create-package.md)
+ [AWS SAM template for a Kinesis application](with-kinesis-example-use-app-spec.md)

## Configuring your data stream and function<a name="services-kinesis-configure"></a>

Your Lambda function is a consumer application for your data stream\. It processes one batch of records at a time from each shard\. You can map a Lambda function to a data stream \(standard iterator\), or to a consumer of a stream \([enhanced fan\-out](https://docs.aws.amazon.com/kinesis/latest/dev/enhanced-consumers.html)\)\.

For standard iterators, Lambda polls each shard in your Kinesis stream for records at a base rate of once per second\. When more records are available, Lambda keeps processing batches until the function catches up with the stream\. The event source mapping shares read throughput with other consumers of the shard\.

To minimize latency and maximize read throughput, create a data stream consumer with enhanced fan\-out\. Enhanced fan\-out consumers get a dedicated connection to each shard that doesn't impact other applications reading from the stream\. Stream consumers use HTTP/2 to reduce latency by pushing records to Lambda over a long\-lived connection and by compressing request headers\. You can create a stream consumer with the Kinesis [RegisterStreamConsumer](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_RegisterStreamConsumer.html) API\.

```
$ aws kinesis register-stream-consumer --consumer-name con1 \
--stream-arn arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream
{
    "Consumer": {
        "ConsumerName": "con1",
        "ConsumerARN": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream/consumer/con1:1540591608",
        "ConsumerStatus": "CREATING",
        "ConsumerCreationTimestamp": 1540591608.0
    }
}
```

To increase the speed at which your function processes records, add shards to your data stream\. Lambda processes records in each shard in order\. It stops processing additional records in a shard if your function returns an error\. With more shards, there are more batches being processed at once, which lowers the impact of errors on concurrency\.

If your function can't scale up to handle the total number of concurrent batches, [request a quota increase](gettingstarted-limits.md) or [reserve concurrency](configuration-concurrency.md) for your function\.

## Execution role permissions<a name="events-kinesis-permissions"></a>

Lambda needs the following permissions to manage resources that are related to your Kinesis data stream\. Add them to your function's [execution role](lambda-intro-execution-role.md)\.
+ [kinesis:DescribeStream](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_DescribeStream.html)
+ [kinesis:DescribeStreamSummary](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_DescribeStreamSummary.html)
+ [kinesis:GetRecords](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetRecords.html)
+ [kinesis:GetShardIterator](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetShardIterator.html)
+ [kinesis:ListShards](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_ListShards.html)
+ [kinesis:ListStreams](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_ListStreams.html)
+ [kinesis:SubscribeToShard](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_SubscribeToShard.html)

The `AWSLambdaKinesisExecutionRole` managed policy includes these permissions\. For more information, see [AWS Lambda execution role](lambda-intro-execution-role.md)\.

To send records of failed batches to a queue or topic, your function needs additional permissions\. Each destination service requires a different permission, as follows:
+ **Amazon SQS** – [sqs:SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html)
+ **Amazon SNS** – [sns:Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html)

## Configuring a stream as an event source<a name="services-kinesis-eventsourcemapping"></a>

Create an event source mapping to tell Lambda to send records from your data stream to a Lambda function\. You can create multiple event source mappings to process the same data with multiple Lambda functions, or to process items from multiple data streams with a single function\.

To configure your function to read from Kinesis in the Lambda console, create a **Kinesis** trigger\.

**To create a trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose **Add trigger**\.

1. Choose a trigger type\.

1. Configure the required options and then choose **Add**\.

Lambda supports the following options for Kinesis event sources\.

**Event source options**
+ **Kinesis stream** – The Kinesis stream to read records from\.
+ **Consumer** \(optional\) – Use a stream consumer to read from the stream over a dedicated connection\.
+ **Batch size** – The number of records to send to the function in each batch, up to 10,000\. Lambda passes all of the records in the batch to the function in a single call, as long as the total size of the events doesn't exceed the [payload limit](gettingstarted-limits.md) for synchronous invocation \(6 MB\)\.
+ **Batch window** – Specify the maximum amount of time to gather records before invoking the function, in seconds\.
+ **Starting position** – Process only new records, all existing records, or records created after a certain date\.
  + **Latest** – Process new records that are added to the stream\.
  + **Trim horizon** – Process all records in the stream\.
  + **At timestamp** – Process records starting from a specific time\.

  After processing any existing records, the function is caught up and continues to process new records\.
+ **On\-failure destination** – An SQS queue or SNS topic for records that can't be processed\. When Lambda discards a batch of records because it's too old or has exhausted all retries, it sends details about the batch to the queue or topic\.
+ **Retry attempts** – The maximum number of times that Lambda retries when the function returns an error\. This doesn't apply to service errors or throttles where the batch didn't reach the function\.
+ **Maximum age of record** – The maximum age of a record that Lambda sends to your function\.
+ **Split batch on error** – When the function returns an error, split the batch into two before retrying\.
+ **Concurrent batches per shard** – Process multiple batches from the same shard concurrently\.
+ **Enabled** – Set to true to enable the event source mapping\. Set to false to stop processing records\. Lambda keeps track of the last record processed and resumes processing from that point when it's reenabled\.

**Note**  
Kinesis charges for each shard and, for enhanced fan\-out, data read from the stream\. For pricing details, see [Amazon Kinesis pricing](https://aws.amazon.com/kinesis/data-streams/pricing)\.

To manage the event source configuration later, choose the trigger in the designer\.

## Event source mapping API<a name="services-kinesis-api"></a>

To manage event source mappings with the AWS CLI or AWS SDK, use the following API actions:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

To create the event source mapping with the AWS CLI, use the `create-event-source-mapping` command\. The following example uses the AWS CLI to map a function named `my-function` to a Kinesis data stream\. The data stream is specified by an Amazon Resource Name \(ARN\), with a batch size of 500, starting from the timestamp in Unix time\.

```
$ aws lambda create-event-source-mapping --function-name my-function \
--batch-size 500 --starting-position AT_TIMESTAMP --starting-position-timestamp 1541139109 \
--event-source-arn arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream
{
    "UUID": "2b733gdc-8ac3-cdf5-af3a-1827b3b11284",
    "BatchSize": 500,
    "MaximumBatchingWindowInSeconds": 0,
    "ParallelizationFactor": 1,
    "EventSourceArn": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1541139209.351,
    "LastProcessingResult": "No records processed",
    "State": "Creating",
    "StateTransitionReason": "User action",
    "DestinationConfig": {},
    "MaximumRecordAgeInSeconds": 604800,
    "BisectBatchOnFunctionError": false,
    "MaximumRetryAttempts": 10000
}
```

To use a consumer, specify the consumer's ARN instead of the stream's ARN\.

Configure additional options to customize how batches are processed and to specify when to discard records that can't be processed\. The following example updates an event source mapping to send a failure record to an SQS queue after two retry attempts, or if the records are more than an hour old\.

```
$ aws lambda update-event-source-mapping --uuid f89f8514-cdd9-4602-9e1f-01a5b77d449b \
--maximum-retry-attempts 2  --maximum-record-age-in-seconds 3600
--destination-config '{"OnFailure": {"Destination": "arn:aws:sqs:us-east-2:123456789012:dlq"}}'
{
    "UUID": "f89f8514-cdd9-4602-9e1f-01a5b77d449b",
    "BatchSize": 100,
    "MaximumBatchingWindowInSeconds": 0,
    "ParallelizationFactor": 1,
    "EventSourceArn": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1573243620.0,
    "LastProcessingResult": "PROBLEM: Function call failed",
    "State": "Updating",
    "StateTransitionReason": "User action",
    "DestinationConfig": {},
    "MaximumRecordAgeInSeconds": 604800,
    "BisectBatchOnFunctionError": false,
    "MaximumRetryAttempts": 10000
}
```

Updated settings are applied asynchronously and aren't reflected in the output until the process completes\. Use the `get-event-source-mapping` command to view the current status\.

```
$ aws lambda get-event-source-mapping --uuid f89f8514-cdd9-4602-9e1f-01a5b77d449b
{
    "UUID": "f89f8514-cdd9-4602-9e1f-01a5b77d449b",
    "BatchSize": 100,
    "MaximumBatchingWindowInSeconds": 0,
    "ParallelizationFactor": 1,
    "EventSourceArn": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1573244760.0,
    "LastProcessingResult": "PROBLEM: Function call failed",
    "State": "Enabled",
    "StateTransitionReason": "User action",
    "DestinationConfig": {
        "OnFailure": {
            "Destination": "arn:aws:sqs:us-east-2:123456789012:dlq"
        }
    },
    "MaximumRecordAgeInSeconds": 3600,
    "BisectBatchOnFunctionError": false,
    "MaximumRetryAttempts": 2
}
```

To process multiple batches concurrently, use the `--parallelization-factor` option\.

```
$ aws lambda update-event-source-mapping --uuid 2b733gdc-8ac3-cdf5-af3a-1827b3b11284 \
--parallelization-factor 5
```

## Error handling<a name="services-kinesis-errors"></a>

The event source mapping that reads records from your Kinesis stream invokes your function synchronously and retries on errors\. If the function is throttled or the Lambda service returns an error without invoking the function, Lambda retries until the records expire or exceed the maximum age that you configure on the event source mapping\.

If the function receives the records but returns an error, Lambda retries until the records in the batch expire, exceed the maximum age, or reach the configured retry quota\. For function errors, you can also configure the event source mapping to split a failed batch into two batches\. Retrying with smaller batches isolates bad records and works around timeout issues\. Splitting a batch does not count towards the retry quota\.

If the error handling measures fail, Lambda discards the records and continues processing batches from the stream\. With the default settings, this means that a bad record can block processing on the affected shard for up to one week\. To avoid this, configure your function's event source mapping with a reasonable number of retries and a maximum record age that fits your use case\.

To retain a record of discarded batches, configure a failed\-event destination\. Lambda sends a document to the destination queue or topic with details about the batch\.

**To configure a destination for failed\-event records**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose **Add destination**\.

1. For **Source**, choose **Stream invocation**\.

1. For **Stream**, choose a stream that is mapped to the function\.

1. For **Destination type**, choose the type of resource that receives the invocation record\.

1. For **Destination**, choose a resource\.

1. Choose **Save**\.

The following example shows an invocation record for a Kinesis stream\.

**Example invocation Record**  

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

You can use this information to retrieve the affected records from the stream for troubleshooting\. The actual records aren't included, so you must process this record and retrieve them from the stream before they expire and are lost\.

## Amazon CloudWatch metrics<a name="events-kinesis-metrics"></a>

Lambda emits the `IteratorAge` metric when your function finishes processing a batch of records\. The metric indicates how old the last record in the batch was when processing finished\. If your function is processing new events, you can use the iterator age to estimate the latency between when a record is added and when the function processes it\.

An increasing trend in iterator age can indicate issues with your function\. For more information, see [Working with AWS Lambda function metrics](monitoring-metrics.md)\.