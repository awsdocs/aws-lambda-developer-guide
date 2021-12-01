# Using AWS Lambda with Amazon SQS<a name="with-sqs"></a>

You can use an AWS Lambda function to process messages in an Amazon Simple Queue Service \(Amazon SQS\) queue\. Lambda [event source mappings](invocation-eventsourcemapping.md) support [standard queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/standard-queues.html) and [first\-in, first\-out \(FIFO\) queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/FIFO-queues.html)\. With Amazon SQS, you can offload tasks from one component of your application by sending them to a queue and processing them asynchronously\.

Lambda polls the queue and invokes your Lambda function [synchronously](invocation-sync.md) with an event that contains queue messages\. Lambda reads messages in batches and invokes your function once for each batch\. When your function successfully processes a batch, Lambda deletes its messages from the queue\. The following example shows an event for a batch of two messages\.

**Example Amazon SQS message event \(standard queue\)**  

```
{
    "Records": [
        {
            "messageId": "059f36b4-87a3-44ab-83d2-661975830a7d",
            "receiptHandle": "AQEBwJnKyrHigUMZj6rYigCgxlaS3SLy0a...",
            "body": "Test message.",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1545082649183",
                "SenderId": "AIDAIENQZJOLO23YVJ4VO",
                "ApproximateFirstReceiveTimestamp": "1545082649185"
            },
            "messageAttributes": {},
            "md5OfBody": "e4e68fb7bd0e697a0ae8f1bb342846b3",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:us-east-2:123456789012:my-queue",
            "awsRegion": "us-east-2"
        },
        {
            "messageId": "2e1424d4-f796-459a-8184-9c92662be6da",
            "receiptHandle": "AQEBzWwaftRI0KuVm4tP+/7q1rGgNqicHq...",
            "body": "Test message.",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1545082650636",
                "SenderId": "AIDAIENQZJOLO23YVJ4VO",
                "ApproximateFirstReceiveTimestamp": "1545082650649"
            },
            "messageAttributes": {},
            "md5OfBody": "e4e68fb7bd0e697a0ae8f1bb342846b3",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:us-east-2:123456789012:my-queue",
            "awsRegion": "us-east-2"
        }
    ]
}
```

By default, Lambda polls up to 10 messages in your queue at once and sends that batch to your function\. To avoid invoking the function with a small number of records, you can tell the event source to buffer records for up to five minutes by configuring a batch window\. Before invoking the function, Lambda continues to poll messages from the SQS standard queue until batch window expires, the [payload limit](gettingstarted-limits.md) is reached or full batch size is reached\.

**Note**  
If you're using a batch window and your SQS queue contains very low traffic, Lambda might wait for up to 20 seconds before invoking your function\. This is true even if you set a batch window lower than 20 seconds\.

For FIFO queues, records contain additional attributes that are related to deduplication and sequencing\.

**Example Amazon SQS message event \(FIFO queue\)**  

```
{
    "Records": [
        {
            "messageId": "11d6ee51-4cc7-4302-9e22-7cd8afdaadf5",
            "receiptHandle": "AQEBBX8nesZEXmkhsmZeyIE8iQAMig7qw...",
            "body": "Test message.",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1573251510774",
                "SequenceNumber": "18849496460467696128",
                "MessageGroupId": "1",
                "SenderId": "AIDAIO23YVJENQZJOL4VO",
                "MessageDeduplicationId": "1",
                "ApproximateFirstReceiveTimestamp": "1573251510774"
            },
            "messageAttributes": {},
            "md5OfBody": "e4e68fb7bd0e697a0ae8f1bb342846b3",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:us-east-2:123456789012:fifo.fifo",
            "awsRegion": "us-east-2"
        }
    ]
}
```

When Lambda reads a batch, the messages stay in the queue but become hidden for the length of the queue's [visibility timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html)\. If your function successfully processes the batch, Lambda deletes the messages from the queue\. If your function is [throttled](invocation-scaling.md), returns an error, or doesn't respond, the message becomes visible again\. All messages in a failed batch return to the queue, so your function code must be able to process the same message multiple times without side effects\.

**Topics**
+ [Scaling and processing](#events-sqs-scaling)
+ [Configuring a queue to use with Lambda](#events-sqs-queueconfig)
+ [Execution role permissions](#events-sqs-permissions)
+ [Configuring a queue as an event source](#events-sqs-eventsource)
+ [Event source mapping APIs](#services-dynamodb-api)
+ [Amazon SQS configuration parameters](#services-sqs-params)
+ [Tutorial: Using Lambda with Amazon SQS](with-sqs-example.md)
+ [Tutorial: Using a cross\-account Amazon SQS queue as an event source](with-sqs-cross-account-example.md)
+ [Sample Amazon SQS function code](with-sqs-create-package.md)
+ [AWS SAM template for an Amazon SQS application](with-sqs-example-use-app-spec.md)

## Scaling and processing<a name="events-sqs-scaling"></a>

For standard queues, Lambda uses [long polling](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-long-polling.html) to poll a queue until it becomes active\. When messages are available, Lambda reads up to 5 batches and sends them to your function\. If messages are still available, Lambda increases the number of processes that are reading batches by up to 60 more instances per minute\. The maximum number of batches that can be processed simultaneously by an event source mapping is 1000\.

For FIFO queues, Lambda sends messages to your function in the order that it receives them\. When you send a message to a FIFO queue, you specify a [message group ID](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/using-messagegroupid-property.html)\. Amazon SQS ensures that messages in the same group are delivered to Lambda in order\. Lambda sorts the messages into groups and sends only one batch at a time for a group\. If the function returns an error, all retries are attempted on the affected messages before Lambda receives additional messages from the same group\.

Your function can scale in concurrency to the number of active message groups\. For more information, see [SQS FIFO as an event source](https://aws.amazon.com/blogs/compute/new-for-aws-lambda-sqs-fifo-as-an-event-source/) on the AWS Compute Blog\.

## Configuring a queue to use with Lambda<a name="events-sqs-queueconfig"></a>

[Create an SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/) to serve as an event source for your Lambda function\. Then configure the queue to allow time for your Lambda function to process each batch of events—and for Lambda to retry in response to throttling errors as it scales up\.

To allow your function time to process each batch of records, set the source queue's visibility timeout to at least 6 times the [ timeout](https://docs.aws.amazon.com/whitepapers/latest/serverless-architectures-lambda/timeout.html) that you configure on your function\. The extra time allows for Lambda to retry if your function execution is throttled while your function is processing a previous batch\.

If a message fails to be processed multiple times, Amazon SQS can send it to a [dead\-letter queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)\. When your function returns an error, Lambda leaves it in the queue\. After the visibility timeout occurs, Lambda receives the message again\. To send messages to a second queue after a number of receives, configure a dead\-letter queue on your source queue\.

**Note**  
Make sure that you configure the dead\-letter queue on the source queue, not on the Lambda function\. The dead\-letter queue that you configure on a function is used for the function's [asynchronous invocation queue](invocation-async.md), not for event source queues\.

If your function returns an error, or can't be invoked because it's at maximum concurrency, processing might succeed with additional attempts\. To give messages a better chance to be processed before sending them to the dead\-letter queue, set the `maxReceiveCount` on the source queue's redrive policy to at least **5**\.

## Execution role permissions<a name="events-sqs-permissions"></a>

Lambda needs the following permissions to manage messages in your Amazon SQS queue\. Add them to your function's execution role\.
+ [sqs:ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [sqs:DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [sqs:GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)

For more information, see [AWS Lambda execution role](lambda-intro-execution-role.md)\.

## Configuring a queue as an event source<a name="events-sqs-eventsource"></a>

Create an event source mapping to tell Lambda to send items from your queue to a Lambda function\. You can create multiple event source mappings to process items from multiple queues with a single function\. When Lambda invokes the target function, the event can contain multiple items, up to a configurable maximum *batch size*\.

To configure your function to read from Amazon SQS in the Lambda console, create an **SQS** trigger\.

**To create a trigger**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Choose a trigger type\.

1. Configure the required options and then choose **Add**\.

Lambda supports the following options for Amazon SQS event sources\.

**Event source options**
+ **SQS queue** – The Amazon SQS queue to read records from\.
+ **Batch size** – The number of records to send to the function in each batch\. For a standard queue this can be up to 10,000 records\. For a FIFO queue the maximum is 10\. For a batch size over 10, you must also set the `MaximumBatchingWindowInSeconds` parameter to at least 1 second\. Lambda passes all of the records in the batch to the function in a single call, as long as the total size of the events doesn't exceed the [payload limit](gettingstarted-limits.md) for synchronous invocation \(6 MB\)\.

  Metadata is generated by both Lambda and Amazon SQS for each record\. This additional metadata is counted towards the total payload size and may cause the total number of records sent in a batch to be lower than your configured batch size\. The metadata fields sent by Amazon SQS can be variable in length\. For more information about the Amazon SQS metadata fields, see the [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) documentation in the *Amazon Simple Queue Service API Reference*\.
+ **Batch window ** – Specify the maximum amount of time to gather records before invoking the function, in seconds\. Only applicable to standard queues\.

  If you are using a batch window greater than 0 seconds, you must account for the increased processing time in your queue visibility timeout\. We recommend setting your queue visibility timeout to 6 times your function timeout, plus the value of `MaximumBatchingWindowInSeconds`\. This allows time for your Lambda function to process each batch of events and to retry in the event of a throttling error\.
**Note**  
If your batch window is greater than 0, and `(batch window) + (function timeout) > (queue visibility timeout)`, your effective queue visibility timeout will be `(batch window) + (function timeout) + 30s`\.

  Lambda processes up to 5 batches at a time\. This means there are a maximum of 5 workers available to batch and process messages in parallel at any one time\. Each worker will show a distinct Lambda invocation for its current batch of messages\. 
+ **Enabled** – Set to true to enable the event source mapping\. Set to false to stop processing records\.

**Note**  
Amazon SQS has a perpetual free tier for requests\. Beyond the free tier, Amazon SQS charges per million requests\. While your event source mapping is active, Lambda makes requests to the queue to get items\. For pricing details, see [Amazon Simple Queue Service pricing](https://aws.amazon.com/sqs/pricing)\.

To manage the event source configuration later, choose the trigger in the designer\.

Configure your function timeout to allow enough time to process an entire batch of items\. If items take a long time to process, choose a smaller batch size\. A large batch size can improve efficiency for workloads that are very fast or have a lot of overhead\. However, if your function returns an error, all items in the batch return to the queue\. If you configure [reserved concurrency](configuration-concurrency.md) on your function, set a minimum of 5 concurrent executions to reduce the chance of throttling errors when Lambda invokes your function\. To eliminate the chance of throttling errors, set the [reserved concurrency](configuration-concurrency.md) value to 1000, which is the maximum number of concurrent executions for an Amazon SQS event source\. 

## Event source mapping APIs<a name="services-dynamodb-api"></a>

To manage an event source with the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+  [CreateEventSourceMapping](API_CreateEventSourceMapping.md) 
+  [ListEventSourceMappings](API_ListEventSourceMappings.md) 
+  [GetEventSourceMapping](API_GetEventSourceMapping.md) 
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) 
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md) 

The following example uses the AWS CLI to map a function named `my-function` to an Amazon SQS queue that is specified by its Amazon Resource Name \(ARN\), with a batch size of 5 and a batch window of 60 seconds\.

```
aws lambda create-event-source-mapping --function-name my-function --batch-size 5 \
--maximum-batching-window-in-seconds 60 \
--event-source-arn arn:aws:sqs:us-east-2:123456789012:my-queue
```

You should see the following output:

```
{
    "UUID": "2b733gdc-8ac3-cdf5-af3a-1827b3b11284",
    "BatchSize": 5,
    "MaximumBatchingWindowInSeconds": 60,
    "EventSourceArn": "arn:aws:sqs:us-east-2:123456789012:my-queue",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1541139209.351,
    "State": "Creating",
    "StateTransitionReason": "USER_INITIATED"
}
```

## Amazon SQS configuration parameters<a name="services-sqs-params"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Amazon SQS\.


**Event source parameters that apply to Amazon SQS**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  BatchSize  |  N  |  100  |  Maximum: 10000  | 
|  Enabled  |  N  |  true  |   | 
|  EventSourceArn  |  Y  |  |  ARN of the data stream or a stream consumer  | 
|  FunctionName  |  Y  |   |   | 
|  MaximumBatchingWindowInSeconds  |  N  |  0  |   | 