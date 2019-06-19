# Using AWS Lambda with Amazon Kinesis<a name="with-kinesis"></a>

You can use an AWS Lambda function to process records in an [Amazon Kinesis data stream](https://docs.aws.amazon.com/kinesis/latest/dev/amazon-kinesis-streams.html)\. With Kinesis, you can collect data from many sources and process them with multiple consumers\. Lambda supports standard data stream iterators and HTTP/2 stream consumers\.

Lambda reads records from the data stream and invokes your function [synchronously](invocation-options.md) with an event that contains stream records\. Lambda reads records in batches and invokes your function to process records from the batch\.

**Example Kinesis Record Event**  

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

If you have multiple applications that are reading records from the same stream, you can use Kinesis stream consumers instead of standard iterators\. Consumers have dedicated read throughput so they don't have to compete with other consumers of the same data\. With consumers, Kinesis pushes records to Lambda over an HTTP/2 connection, which can also reduce latency between adding a record and function invocation\.

If your function returns an error, Lambda retries the batch until processing succeeds or the data expires\. Until the issue is resolved, no data in the shard is processed\. To avoid stalled shards and potential data loss, make sure to handle and record processing errors in your code\.

## Configuring Your Data Stream and Function<a name="services-kinesis-configure"></a>

Your Lambda function is a consumer application for your data stream\. It processes one batch of records at a time from each shard\. You can map a Lambda function to a data stream \(standard iterator\), or to a consumer of a stream \([enhanced fan\-out](https://docs.aws.amazon.com/kinesis/latest/dev/introduction-to-enhanced-consumers.html)\)\.

For standard iterators, Lambda polls each shard in your Kinesis stream for records at a base rate of once per second\. When more records are available, Lambda keeps processing batches until it receives a batch that's smaller than the configured maximum batch size\. The function shares read throughput with other consumers of the shard\.

To minimize latency and maximize read throughput, create a data stream consumer\. Stream consumers get a dedicated connection to each shard that doesn't impact other applications reading from the stream\. The dedicated throughput can help if you have many applications reading the same data, or if you're reprocessing a stream with large records\.

Stream consumers use HTTP/2 to reduce latency by pushing records to Lambda over a long\-lived connection and compressing request headers\. You can create a stream consumer with the Kinesis [RegisterStreamConsumer](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_RegisterStreamConsumer.html) API\.

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

To increase the speed at which your function processes records, add shards to your data stream\. Lambda processes records in each shard in order, and stops processing additional records in a shard if your function returns an error\. With more shards, there are more batches being processed at once, which lowers the impact of errors on concurrency\.

If your function can't scale up to handle one concurrent execution per shard, [request a limit increase](limits.md) or [reserve concurrency](concurrent-executions.md) for your function\. The concurrency available to your function should match or exceed the number of shards in your Kinesis data stream\.

## Execution Role Permissions<a name="events-kinesis-permissions"></a>

Lambda needs the following permissions to manage resources that are related to your Kinesis data stream\. Add them to your function's execution role\.
+ [kinesis:DescribeStream](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_DescribeStream.html)
+ [kinesis:DescribeStreamSummary](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_DescribeStreamSummary.html)
+ [kinesis:GetRecords](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetRecords.html)
+ [kinesis:GetShardIterator](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetShardIterator.html)
+ [kinesis:ListShards](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_ListShards.html)
+ [kinesis:ListStreams](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_ListStreams.html)
+ [kinesis:SubscribeToShard](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_SubscribeToShard.html)

The `AWSLambdaKinesisExecutionRole` managed policy includes these permissions\. For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.

## Configuring a Stream as an Event Source<a name="services-kinesis-eventsourcemapping"></a>

Create an event source mapping to tell Lambda to send records from your data stream to a Lambda function\. You can create multiple event source mappings to process the same data with multiple Lambda functions, or process items from multiple data streams with a single function\.

To configure your function to read from Kinesis in the Lambda console, create a **Kinesis** trigger\.

**To create a trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose a trigger type to add a trigger to your function\.

1. Under **Configure triggers**, configure the required options and then choose **Add**\.

1. Choose **Save**\.

Lambda supports the following options for Kinesis event sources\.

**Event Source Options**
+ **Kinesis stream** – The Kinesis stream to read records from\.
+ **Consumer** \(optional\) – Use a stream consumer to read from the stream over a dedicated connection\.
+ **Batch size** – The number of records to read from a shard in each batch, up to 10,000\. Lambda passes all of the records in the batch to the function in a single call, as long as the total size of the events doesn't exceed the [payload limit](limits.md) for synchronous invocation \(6 MB\)\.
+ **Starting position** – Process only new records, all existing records, or records created after a certain date\.
  + **Latest** – Process new records that are added to the stream\.
  + **Trim horizon** – Process all records in the stream\.
  + **At timestamp** – Process records starting from a specific time\.

  After processing any existing records, the function is caught up and continues to process new records\.
+ **Enabled** – Disable the event source to stop processing records\. Lambda keeps track of the last record processed and resumes processing from that point when it's re\-enabled\.

To manage the event source configuration later, choose the trigger in the designer\.

## Event Source Mapping APIs<a name="services-kinesis-api"></a>

To create the event source mapping with the AWS CLI, use the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) API\. The following example uses the AWS CLI to map a function named `my-function` to a Kinesis data stream\. The data stream is specified by an Amazon Resource Name \(ARN\), with a batch size of 500, starting from the timestamp in Unix time\.

```
$ aws lambda create-event-source-mapping --function-name my-function \
--batch-size 500 --starting-position AT_TIMESTAMP --starting-position-timestamp 1541139109 \
--event-source-arn arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream
{
    "UUID": "2b733gdc-8ac3-cdf5-af3a-1827b3b11284",
    "BatchSize": 500,
    "EventSourceArn": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1541139209.351,
    "LastProcessingResult": "No records processed",
    "State": "Creating",
    "StateTransitionReason": "User action"
}
```

To use a consumer, specify the consumer's ARN instead of the stream's ARN\.

## Amazon CloudWatch Metrics<a name="events-kinesis-metrics"></a>

Lambda emits the `IteratorAge` metric when your function finishes processing a batch of records\. The metric indicates how old the last record in the batch was when processing finished\. If your function is processing new events, you can use the iterator age to estimate the latency between when a record is added, and when the function processes it\.

An increasing trend in iterator age can indicate issues with your function\. For more information, see [Monitoring and Troubleshooting Lambda Applications](troubleshooting.md)\.