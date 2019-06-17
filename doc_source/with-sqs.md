# Using AWS Lambda with Amazon SQS<a name="with-sqs"></a>

You can use an AWS Lambda function to process messages in a [standard Amazon Simple Queue Service \(Amazon SQS\) queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/standard-queues.html)\. With Amazon SQS, you can offload tasks from one component of your application by sending them to a queue and processing them asynchronously\.

Lambda polls the queue and invokes your function [synchronously](invocation-options.md) with an event that contains queue messages\. Lambda reads messages in batches and invokes your function once for each batch\. When your function successfully processes a batch, Lambda deletes its messages from the queue\.

**Example Amazon SQS Message Event**  

```
{
    "Records": [
        {
            "messageId": "059f36b4-87a3-44ab-83d2-661975830a7d",
            "receiptHandle": "AQEBwJnKyrHigUMZj6rYigCgxlaS3SLy0a...",
            "body": "test",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1545082649183",
                "SenderId": "AIDAIENQZJOLO23YVJ4VO",
                "ApproximateFirstReceiveTimestamp": "1545082649185"
            },
            "messageAttributes": {},
            "md5OfBody": "098f6bcd4621d373cade4e832627b4f6",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:us-east-2:123456789012:my-queue",
            "awsRegion": "us-east-2"
        },
        {
            "messageId": "2e1424d4-f796-459a-8184-9c92662be6da",
            "receiptHandle": "AQEBzWwaftRI0KuVm4tP+/7q1rGgNqicHq...",
            "body": "test",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1545082650636",
                "SenderId": "AIDAIENQZJOLO23YVJ4VO",
                "ApproximateFirstReceiveTimestamp": "1545082650649"
            },
            "messageAttributes": {},
            "md5OfBody": "098f6bcd4621d373cade4e832627b4f6",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:us-east-2:123456789012:my-queue",
            "awsRegion": "us-east-2"
        }
    ]
}
```

Lambda uses [long polling](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-long-polling.html) to poll a queue until it becomes active\. When messages are available, Lambda increases the rate at which it reads batches, and invokes your function until it reaches a concurrency limit\. For more information on how Lambda scales to process messages in your Amazon SQS queue, see [Understanding Scaling Behavior](scaling.md)\.

When Lambda reads a message from the queue, it stays in the queue but becomes hidden until Lambda deletes it\. If your function returns an error, or doesn't finish processing before the queue's [visibility timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html), it becomes visible again\. Then Lambda sends it to your Lambda function again\. All messages in a failed batch return to the queue, so your function code must be able to process the same message multiple times without side effects\.

## Configuring a Queue for Use With Lambda<a name="events-sqs-queueconfig"></a>

[Create a standard Amazon SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/) to serve as an event source for your Lambda function\. Then configure the queue to allow time for your Lambda function to process each batch of events—and for Lambda to retry in response to throttling errors as it scales up\.

To allow your function time to process each batch of records, set the source queue's visibility timeout to at least 6 times the [timeout](resource-model.md) that you configure on your function\. The extra time allows for Lambda to retry if your function execution is throttled while your function is processing a previous batch\.

If a message fails processing multiple times, Amazon SQS can send it to a [dead letter queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)\. Configure a dead letter queue on your source queue to retain messages that failed processing for troubleshooting\. Set the `maxReceiveCount` on the queue's redrive policy to at least **5** to avoid sending messages to the dead letter queue due to throttling\.

## Execution Role Permissions<a name="events-sqs-permissions"></a>

Lambda needs the following permissions to manage messages in your Amazon SQS queue\. Add them to your function's execution role\.
+ [sqs:ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [sqs:DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [sqs:GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)

For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.

## Configuring a Queue as an Event Source<a name="events-sqs-eventsource"></a>

Create an event source mapping to tell Lambda to send items from your queue to a Lambda function\. You can create multiple event source mappings to process items from multiple queues with a single function\. When Lambda invokes the target function, the event can contain multiple items, up to a configurable maximum *batch size*\.

To configure your function to read from Amazon SQS in the Lambda console, create an **SQS** trigger\.

**To create a trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose a trigger type to add a trigger to your function\.

1. Under **Configure triggers**, configure the required options and then choose **Add**\.

1. Choose **Save**\.

Lambda supports the following options for Amazon SQS event sources\.

**Event Source Options**
+ **SQS queue** – The Amazon SQS queue to read records from\.
+ **Batch size** – The number of items to read from the queue in each batch, up to 10\. The event may contain fewer items if the batch that Lambda read from the queue had fewer items\.
+ **Enabled** – Disable the event source to stop processing items\.

To manage the event source configuration later, choose the trigger in the designer\.

Configure your function timeout to allow enough time to process an entire batch of items\. If items take a long time to process, choose a smaller batch size\. A large batch size can improve efficiency for workloads that are very fast or have a lot of overhead\. However, if your function returns an error, all items in the batch return to the queue\. If you configure [reserved concurrency](concurrent-executions.md#per-function-concurrency) on your function, set a minimum of 5 concurrent executions to reduce the chance of throttling errors when Lambda invokes your function\.

## Event Source Mapping APIs<a name="services-dynamodb-api"></a>

To manage event source mappings with the AWS CLI or AWS SDK, use the following APIs\.
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

The following example uses the AWS Command Line Interface to map a function named `my-function` to an Amazon SQS queue specified by Amazon Resource Name \(ARN\), with a batch size of 5\.

```
$ aws lambda create-event-source-mapping --function-name my-function --batch-size 5 \
--event-source-arn arn:aws:sqs:us-east-2:123456789012:my-queue
{
    "UUID": "2b733gdc-8ac3-cdf5-af3a-1827b3b11284",
    "BatchSize": 5,
    "EventSourceArn": "arn:aws:sqs:us-east-2:123456789012:my-queue",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "LastModified": 1541139209.351,
    "State": "Creating",
    "StateTransitionReason": "USER_INITIATED"
}
```