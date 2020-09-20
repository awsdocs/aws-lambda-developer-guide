# Using AWS Lambda with Amazon DynamoDB<a name="with-ddb"></a>

You can use an AWS Lambda function to process records in an [Amazon DynamoDB stream](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.html)\. With DynamoDB Streams, you can trigger a Lambda function to perform additional work each time a DynamoDB table is updated\.

Lambda reads records from the stream and invokes your function [synchronously](invocation-sync.md) with an event that contains stream records\. Lambda reads records in batches and invokes your function to process records from the batch\.

**Example DynamoDB Streams record event**  

```
{
  "Records": [
    {
      "eventID": "1",
      "eventVersion": "1.0",
      "dynamodb": {
        "Keys": {
          "Id": {
            "N": "101"
          }
        },
        "NewImage": {
          "Message": {
            "S": "New item!"
          },
          "Id": {
            "N": "101"
          }
        },
        "StreamViewType": "NEW_AND_OLD_IMAGES",
        "SequenceNumber": "111",
        "SizeBytes": 26
      },
      "awsRegion": "us-west-2",
      "eventName": "INSERT",
      "eventSourceARN": eventsourcearn,
      "eventSource": "aws:dynamodb"
    },
    {
      "eventID": "2",
      "eventVersion": "1.0",
      "dynamodb": {
        "OldImage": {
          "Message": {
            "S": "New item!"
          },
          "Id": {
            "N": "101"
          }
        },
        "SequenceNumber": "222",
        "Keys": {
          "Id": {
            "N": "101"
          }
        },
        "SizeBytes": 59,
        "NewImage": {
          "Message": {
            "S": "This item has changed"
          },
          "Id": {
            "N": "101"
          }
        },
        "StreamViewType": "NEW_AND_OLD_IMAGES"
      },
      "awsRegion": "us-west-2",
      "eventName": "MODIFY",
      "eventSourceARN": sourcearn,
      "eventSource": "aws:dynamodb"
    }
```

Lambda polls shards in your DynamoDB stream for records at a base rate of 4 times per second\. When records are available, Lambda invokes your function and waits for the result\. If processing succeeds, Lambda resumes polling until it receives more records\.

By default, Lambda invokes your function as soon as records are available in the stream\. If the batch that Lambda reads from the stream only has one record in it, Lambda sends only one record to the function\. To avoid invoking the function with a small number of records, you can tell the event source to buffer records for up to five minutes by configuring a *batch window*\. Before invoking the function, Lambda continues to read records from the stream until it has gathered a full batch, or until the batch window expires\.

If your function returns an error, Lambda retries the batch until processing succeeds or the data expires\. To avoid stalled shards, you can configure the event source mapping to retry with a smaller batch size, limit the number of retries, or discard records that are too old\. To retain discarded events, you can configure the event source mapping to send details about failed batches to an SQS queue or SNS topic\.

You can also increase concurrency by processing multiple batches from each shard in parallel\. Lambda can process up to 10 batches in each shard simultaneously\. If you increase the number of concurrent batches per shard, Lambda still ensures in\-order processing at the partition\-key level\.

**Topics**
+ [Execution role permissions](#events-dynamodb-permissions)
+ [Configuring a stream as an event source](#services-dynamodb-eventsourcemapping)
+ [Event source mapping APIs](#services-dynamodb-api)
+ [Error handling](#services-dynamodb-errors)
+ [Amazon CloudWatch metrics](#events-dynamodb-metrics)
+ [Tutorial: Using AWS Lambda with Amazon DynamoDB streams](with-ddb-example.md)
+ [Sample function code](with-ddb-create-package.md)
+ [AWS SAM template for a DynamoDB application](kinesis-tutorial-spec.md)

## Execution role permissions<a name="events-dynamodb-permissions"></a>

Lambda needs the following permissions to manage resources related to your DynamoDB stream\. Add them to your function's execution role\.
+ [dynamodb:DescribeStream](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_DescribeStream.html)
+ [dynamodb:GetRecords](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_GetRecords.html)
+ [dynamodb:GetShardIterator](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_GetShardIterator.html)
+ [dynamodb:ListStreams](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_ListStreams.html)

The `AWSLambdaDynamoDBExecutionRole` managed policy includes these permissions\. For more information, see [AWS Lambda execution role](lambda-intro-execution-role.md)\.

To send records of failed batches to a queue or topic, your function needs additional permissions\. Each destination service requires a different permission, as follows:
+ **Amazon SQS** – [sqs:SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html)
+ **Amazon SNS** – [sns:Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html)

## Configuring a stream as an event source<a name="services-dynamodb-eventsourcemapping"></a>

Create an event source mapping to tell Lambda to send records from your stream to a Lambda function\. You can create multiple event source mappings to process the same data with multiple Lambda functions, or to process items from multiple streams with a single function\.

To configure your function to read from DynamoDB Streams in the Lambda console, create a **DynamoDB** trigger\.

**To create a trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose **Add trigger**\.

1. Choose a trigger type\.

1. Configure the required options and then choose **Add**\.

Lambda supports the following options for DynamoDB event sources\.

**Event source options**
+ **DynamoDB table** – The DynamoDB table to read records from\.
+ **Batch size** – The number of records to send to the function in each batch, up to 1,000\. Lambda passes all of the records in the batch to the function in a single call, as long as the total size of the events doesn't exceed the [payload limit](gettingstarted-limits.md) for synchronous invocation \(6 MB\)\.
+ **Batch window** – Specify the maximum amount of time to gather records before invoking the function, in seconds\.
+ **Starting position** – Process only new records, or all existing records\.
  + **Latest** – Process new records that are added to the stream\.
  + **Trim horizon** – Process all records in the stream\.

  After processing any existing records, the function is caught up and continues to process new records\.
+ **On\-failure destination** – An SQS queue or SNS topic for records that can't be processed\. When Lambda discards a batch of records because it's too old or has exhausted all retries, it sends details about the batch to the queue or topic\.
+ **Retry attempts** – The maximum number of times that Lambda retries when the function returns an error\. This doesn't apply to service errors or throttles where the batch didn't reach the function\.
+ **Maximum age of record** – The maximum age of a record that Lambda sends to your function\.
+ **Split batch on error** – When the function returns an error, split the batch into two before retrying\.
+ **Concurrent batches per shard** – Process multiple batches from the same shard concurrently\.
+ **Enabled** – Set to true to enable the event source mapping\. Set to false to stop processing records\. Lambda keeps track of the last record processed and resumes processing from that point when the mapping is reenabled\.

**Note**  
You are not charged for GetRecords API calls invoked by Lambda as part of DynamoDB triggers\.

To manage the event source configuration later, choose the trigger in the designer\.

## Event source mapping APIs<a name="services-dynamodb-api"></a>

To manage event source mappings with the AWS CLI or AWS SDK, use the following API actions:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

The following example uses the AWS CLI to map a function named `my-function` to a DynamoDB stream that is specified by its Amazon Resource Name \(ARN\), with a batch size of 500\.

```
$ aws lambda create-event-source-mapping --function-name my-function --batch-size 500 --starting-position LATEST \
--event-source-arn arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525
{
    "UUID": "14e0db71-5d35-4eb5-b481-8945cf9d10c2",
    "BatchSize": 500,
    "MaximumBatchingWindowInSeconds": 0,
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
    "EventSourceArn": "arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525",
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
    "EventSourceArn": "arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525",
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

## Error handling<a name="services-dynamodb-errors"></a>

The event source mapping that reads records from your DynamoDB stream invokes your function synchronously and retries on errors\. If the function is throttled or the Lambda service returns an error without invoking the function, Lambda retries until the records expire or exceed the maximum age that you configure on the event source mapping\.

If the function receives the records but returns an error, Lambda retries until the records in the batch expire, exceed the maximum age, or reach the configured retry quota\. For function errors, you can also configure the event source mapping to split a failed batch into two batches\. Retrying with smaller batches isolates bad records and works around timeout issues\. Splitting a batch does not count towards the retry quota\.

If the error handling measures fail, Lambda discards the records and continues processing batches from the stream\. With the default settings, this means that a bad record can block processing on the affected shard for up to one day\. To avoid this, configure your function's event source mapping with a reasonable number of retries and a maximum record age that fits your use case\.

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

The following example shows an invocation record for a DynamoDB stream\.

**Example Invocation Record**  

```
{
    "requestContext": {
        "requestId": "316aa6d0-8154-xmpl-9af7-85d5f4a6bc81",
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
    "timestamp": "2019-11-14T00:13:49.717Z",
    "DDBStreamBatchInfo": {
        "shardId": "shardId-00000001573689847184-864758bb",
        "startSequenceNumber": "800000000003126276362",
        "endSequenceNumber": "800000000003126276362",
        "approximateArrivalOfFirstRecord": "2019-11-14T00:13:19Z",
        "approximateArrivalOfLastRecord": "2019-11-14T00:13:19Z",
        "batchSize": 1,
        "streamArn": "arn:aws:dynamodb:us-east-2:123456789012:table/mytable/stream/2019-11-14T00:04:06.388"
    }
}
```

You can use this information to retrieve the affected records from the stream for troubleshooting\. The actual records aren't included, so you must process this record and retrieve them from the stream before they expire and are lost\.

## Amazon CloudWatch metrics<a name="events-dynamodb-metrics"></a>

Lambda emits the `IteratorAge` metric when your function finishes processing a batch of records\. The metric indicates how old the last record in the batch was when processing finished\. If your function is processing new events, you can use the iterator age to estimate the latency between when a record is added and when the function processes it\.

An increasing trend in iterator age can indicate issues with your function\. For more information, see [Working with AWS Lambda function metrics](monitoring-metrics.md)\.