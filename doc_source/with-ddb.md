# Using AWS Lambda with Amazon DynamoDB<a name="with-ddb"></a>

You can use a AWS Lambda function to process records in a [Amazon DynamoDB Streams stream](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.html)\. With DynamoDB Streams, you can trigger a Lambda function to perform additional work each time a DynamoDB table is updated\.

Lambda reads records from the stream and invokes your function [synchronously](invocation-options.md) with an event that contains stream records\. Lambda reads records in batches and invokes your function to process records from the batch\.

**Example DynamoDB Streams Record Event**  

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

Lambda polls shards in your DynamoDB Streams stream for records at a base rate of 4 times per second\. When records are available, Lambda invokes your function and waits for the result\. If processing succeeds, Lambda resumes polling until it receives more records\.

If your function returns an error, Lambda retries the batch until processing succeeds or the data expires\. Until the issue is resolved, no data in the shard is processed\. Handle any record processing errors in your code to avoid stalled shards and potential data loss\.

## Execution Role Permissions<a name="events-dynamodb-permissions"></a>

Lambda needs the following permissions to manage resources related to your DynamoDB Streams stream\. Add them to your function's execution role\.
+ [dynamodb:DescribeStream](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_DescribeStream.html)
+ [dynamodb:GetRecords](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_GetRecords.html)
+ [dynamodb:GetShardIterator](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_GetShardIterator.html)
+ [dynamodb:ListStreams](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_ListStreams.html)

The `AWSLambdaDynamoDBExecutionRole` managed policy includes these permissions\. For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.

## Configuring a Stream as an Event Source<a name="services-dynamodb-eventsourcemapping"></a>

Create an event source mapping to tell Lambda to send records from your stream to a Lambda function\. You can create multiple event source mappings to process the same data with multiple Lambda functions, or process items from multiple streams with a single function\.

To configure your function to read from DynamoDB Streams in the Lambda console, create a **DynamoDB** trigger\.

**To create a trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose a trigger type to add a trigger to your function\.

1. Under **Configure triggers**, configure the required options and then choose **Add**\.

1. Choose **Save**\.

Lambda supports the following options for DynamoDB event sources\.

**Event Source Options**
+ **DynamoDB table** – The DynamoDB table to read records from\.
+ **Batch size** – The number of records to read from a shard in each batch, up to 1,000\. Lambda passes all of the records in the batch to the function in a single call, as long as the total size of the events doesn't exceed the [payload limit](limits.md) for synchronous invocation \(6 MB\)\.
+ **Starting position** – Process only new records, or all existing records\.
  + **Latest** – Process new records added to the stream\.
  + **Trim horizon** – Process all records in the stream\.

  After processing any existing records, the function is caught up and continues to process new records\.
+ **Enabled** – Disable the event source to stop processing records\. Lambda keeps track of the last record processed and resumes processing from that point when the mapping is re\-enabled\.

To manage the event source configuration later, choose the trigger in the designer\.

## Event Source Mapping APIs<a name="services-dynamodb-api"></a>

To manage event source mappings with the AWS CLI or AWS SDK, use the following APIs\.
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

The following example uses the AWS Command Line Interface to map a function named `my-function` to an DynamoDB Streams stream specified by Amazon Resource Name \(ARN\), with a batch size of 500

```
$ aws lambda create-event-source-mapping --function-name my-function --batch-size 500 --starting-position LATEST \
--event-source-arn arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525
{
    "UUID": "14e0db71-5d35-4eb5-b481-8945cf9d10c2",
    "BatchSize": 500,
    "EventSourceArn": "arn:aws:dynamodb:us-east-2:123456789012:table/my-table/stream/2019-06-10T19:26:16.525",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1560209851.963,
    "LastProcessingResult": "No records processed",
    "State": "Creating",
    "StateTransitionReason": "User action"
}
```

## Amazon CloudWatch Metrics<a name="events-dynamodb-metrics"></a>

Lambda emits the `IteratorAge` metric when your function finishes processing a batch of records\. The metric indicates how old the last record in the batch was when processing finished\. If your function is processing new events, you can use the iterator age to estimate the latency between when a record is added, and when the function processes it\.

An increasing trend in iterator age can indicate issues with your function\. For more information, see [Monitoring and Troubleshooting Lambda Applications](troubleshooting.md)\.