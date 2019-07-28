# AWS Lambda Event Source Mapping<a name="invocation-eventsourcemapping"></a>

An event source mapping is an AWS Lambda resource that reads from an event source and invokes a Lambda function\. You can use event source mappings to process items from a stream or queue in services that don't invoke Lambda functions directly\. Lambda provides event source mappings for the following services\.

**Services That Lambda Reads Events From**
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Simple Queue Service](with-sqs.md)

An event source mapping uses permissions in the function's [execution role](lambda-intro-execution-role.md) to read and manage items in the event source\. Permissions, event structure, settings, and polling behavior vary by event source\. For more information, see the linked topic for the service that you use as an event source\.

To manage event source mappings with the AWS CLI or AWS SDK, use the following APIs\.
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

The following example uses the AWS Command Line Interface to map a function named `my-function` to an DynamoDB Streams stream specified by Amazon Resource Name \(ARN\), with a batch size of 500\.

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

Event source mappings read items from a stream or queue in batches, and include multiple items in the event that your function receives\. You can configure the size of the batch that the event source mapping reads, up to a maximum that varies by service\. The number of items in the event can be smaller than the batch size if there aren't enough items available, or if the batch is too large to send in one event and has to be split up\.

For streams, an event source mapping creates an iterator for each shard in the stream and processes items in each shard in order\. You can configure the event source mapping to read only new items that appear in the stream, or to start with older items\. If your function returns an error, the entire batch is reprocessed until the function succeeds, or the items in the batch expire\. To ensure in\-order processing, processing for the affected shard is paused until the error is resolved\. Processed items are not removed from the stream and may be processed by other functions or consumers\.

For queues, items are not necessarily processed in order\. Failed batches are returned to the queue as individual items and may be processed in a different grouping than the original batch\. Occasionally, the event source mapping might receive the same item from the queue twice, even if no function error occurred\. Lambda deletes items from the queue after they are processed successfully\.

For information about services that invoke Lambda functions directly, see [Using AWS Lambda with Other Services](lambda-services.md)\.