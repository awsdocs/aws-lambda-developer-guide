# Using AWS Lambda with Amazon SQS<a name="with-sqs"></a>

You can use an AWS Lambda function to process messages in a [standard Amazon Simple Queue Service \(Amazon SQS\) queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/standard-queues.html)\. With Amazon SQS, you can offload tasks from one component of your application by sending them to a queue and processing them asynchronously\.

Lambda polls the queue and invokes your function [synchronously](invocation-options.md) with an event that contains queue messages\. Lambda reads messages in batches and invokes your function once for each batch\. When your function successfully processes a batch, Lambda deletes its messages from the queue\.

**Example Amazon SQS Message Event**  

```
{
     "Records": [
        {
            "messageId": "c80e8021-a70a-42c7-a470-796e1186f753",
            "receiptHandle": "AQEBJQ+/u6NsnT5t8Q/VbVxgdUl4TMKZ5FqhksRdIQvLBhwNvADoBxYSOVeCBXdnS9P+erlTtwEALHsnBXynkfPLH3BOUqmgzP25U8kl8eHzq6RAlzrSOfTO8ox9dcp6GLmW33YjO3zkq5VRYyQlJgLCiAZUpY2D4UQcE5D1Vm8RoKfbE+xtVaOctYeINjaQJ1u3mWx9T7tork3uAlOe1uyFjCWU5aPX/1OHhWCGi2EPPZj6vchNqDOJC/Y2k1gkivqCjz1CZl6FlZ7UVPOx3AMoszPuOYZ+Nuqpx2uCE2MHTtMHD8PVjlsWirt56oUr6JPp9aRGo6bitPIOmi4dX0FmuMKD6u/JnuZCp+AXtJVTmSHS8IXt/twsKU7A+fiMK01NtD5msNgVPoe9JbFtlGwvTQ==",
            "body": "{\"foo\":\"bar\"}",
            "attributes": {
                "ApproximateReceiveCount": "3",
                "SentTimestamp": "1529104986221",
                "SenderId": "594035263019",
                "ApproximateFirstReceiveTimestamp": "1529104986230"
            },
            "messageAttributes": {},
            "md5OfBody": "9bb58f26192e4ba00f01e2e7b136bbd8",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:us-west-2:123456789012:MyQueue",
            "awsRegion": "us-west-2"
        }
    ]
}
```

Lambda uses [long polling](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-long-polling.html) to poll a queue until it becomes active\. When messages are available, Lambda increases the rate at which it reads batches, and invokes your function until it reaches a concurrency limit\. For more information on how Lambda scales to process messages in your Amazon SQS queue, see [Understanding Scaling Behavior](scaling.md)\.

When Lambda reads a message from the queue, it stays in the queue but becomes hidden until Lambda deletes it\. If your function returns an error, or doesn't finish processing before the queue's [visibility timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html), it becomes visible again\. Then Lambda sends it to your Lambda function again\. All messages in a failed batch return to the queue, so your function code must be able to process the same message multiple times without side effects\.

**Topics**
+ [Configuring a Queue for Use With Lambda](#events-sqs-queueconfig)
+ [Configuring a Queue as an Event Source](#events-sqs-eventsource)
+ [Execution Role Permissions](#events-sqs-permissions)
+ [Tutorial: Using AWS Lambda with Amazon Simple Queue Service](with-sqs-example.md)
+ [Sample Amazon SQS Function Code](with-sqs-create-package.md)
+ [AWS SAM Template for an Amazon SQS Application](with-sqs-example-use-app-spec.md)

## Configuring a Queue for Use With Lambda<a name="events-sqs-queueconfig"></a>

[Create a standard Amazon SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/) to serve as an event source for your Lambda function\. Then configure the queue to allow time for your Lambda function to process each batch of events—and for Lambda to retry in response to throttling errors as it scales up\.

To allow your function time to process each batch of records, set the source queue's visibility timeout to at least 6 times the [timeout](resource-model.md) that you configure on your function\. The extra time allows for Lambda to retry if your function execution is throttled while your function is processing a previous batch\.

If a message fails processing multiple times, Amazon SQS can send it to a [dead letter queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)\. Configure a dead letter queue on your source queue to retain messages that failed processing for troubleshooting\. Set the `maxReceiveCount` on the queue's redrive policy to at least **5** to avoid sending messages to the dead letter queue due to throttling\.

## Configuring a Queue as an Event Source<a name="events-sqs-eventsource"></a>

Create an event source mapping to tell Lambda to send items from your queue to a Lambda function\. You can create multiple event source mappings to process items from multiple queues with a single function\. When Lambda invokes the target function, the event can contain multiple items, up to a configurable maximum batch size\.

**To add an event source mapping for an Amazon SQS queue**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Add triggers**, choose **SQS**\.

1. Under **Configure triggers**, configure the event source\.
   + **SQS queue** – Specify the source queue\.
   + **Batch size** – Specify the maximum number of items to read from the queue and send to your function, in a single invocation\.
   + **Enabled** – Clear the check box to disable the event source\.

1. Choose **Add**\.

1. Choose **Save**\.

Configure your function timeout to allow enough time to process an entire batch of items\. If items take a long time to process, choose a smaller batch size\. A large batch size can improve efficiency for workloads that are very fast or have a lot of overhead\. However, if your function returns an error, all items in the batch return to the queue\. If you configure [reserved concurrency](concurrent-executions.md#per-function-concurrency) on your function, set a minimum of 5 concurrent executions to reduce the chance of throttling errors when Lambda invokes your function\.

To configure an event source with the Lambda API or the AWS SDK, use the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) actions\.

## Execution Role Permissions<a name="events-sqs-permissions"></a>

Lambda needs the following permissions to manage messages in your Amazon SQS queue\. Add them to your function's [execution role](intro-permission-model.md#lambda-intro-execution-role)\.
+ [sqs:ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [sqs:DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [sqs:GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)

For more information, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.