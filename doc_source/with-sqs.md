# Using Lambda with Amazon SQS<a name="with-sqs"></a>

You can use a Lambda function to process messages in an Amazon Simple Queue Service \(Amazon SQS\) queue\. Lambda [event source mappings](invocation-eventsourcemapping.md) support [standard queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/standard-queues.html) and [first\-in, first\-out \(FIFO\) queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/FIFO-queues.html)\. With Amazon SQS, you can offload tasks from one component of your application by sending them to a queue and processing them asynchronously\.

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

By default, Lambda polls up to 10 messages in your queue at once and sends that batch to your function\. To avoid invoking the function with a small number of records, you can tell the event source to buffer records for up to 5 minutes by configuring a batch window\. Before invoking the function, Lambda continues to poll messages from the SQS standard queue until the batch window expires, the [invocation payload size quota](gettingstarted-limits.md) is reached, or the configured maximum batch size is reached\.

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

When Lambda reads a batch, the messages stay in the queue but are hidden for the length of the queue's [visibility timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html)\. If your function successfully processes the batch, Lambda deletes the messages from the queue\. By default, if your function encounters an error while processing a batch, all messages in that batch become visible in the queue again\. For this reason, your function code must be able to process the same message multiple times without unintended side effects\. You can modify this reprocessing behavior by configuring your event source mapping to include [batch item failures](#services-sqs-batchfailurereporting) in your function response\.

**Topics**
+ [Scaling and processing](#events-sqs-scaling)
+ [Configuring a queue to use with Lambda](#events-sqs-queueconfig)
+ [Execution role permissions](#events-sqs-permissions)
+ [Configuring a queue as an event source](#events-sqs-eventsource)
+ [Event source mapping APIs](#events-sqs-api)
+ [Reporting batch item failures](#services-sqs-batchfailurereporting)
+ [Amazon SQS configuration parameters](#services-sqs-params)
+ [Tutorial: Using Lambda with Amazon SQS](with-sqs-example.md)
+ [Tutorial: Using a cross\-account Amazon SQS queue as an event source](with-sqs-cross-account-example.md)
+ [Sample Amazon SQS function code](with-sqs-create-package.md)
+ [AWS SAM template for an Amazon SQS application](with-sqs-example-use-app-spec.md)

## Scaling and processing<a name="events-sqs-scaling"></a>

For standard queues, Lambda uses [long polling](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-short-and-long-polling.html) to poll a queue until it becomes active\. When messages are available, Lambda reads up to five batches and sends them to your function\. If messages are still available, Lambda increases the number of processes that are reading batches by up to 60 more instances per minute\. The maximum number of batches that an event source mapping can process simultaneously is 1,000\.

For FIFO queues, Lambda sends messages to your function in the order that it receives them\. When you send a message to a FIFO queue, you specify a [message group ID](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/using-messagegroupid-property.html)\. Amazon SQS ensures that messages in the same group are delivered to Lambda in order\. Lambda sorts the messages into groups and sends only one batch at a time for a group\. If your function returns an error, the function attempts all retries on the affected messages before Lambda receives additional messages from the same group\.

Your function can scale in concurrency to the number of active message groups\. For more information, see [SQS FIFO as an event source](http://aws.amazon.com/blogs/compute/new-for-aws-lambda-sqs-fifo-as-an-event-source/) on the AWS Compute Blog\.

## Configuring a queue to use with Lambda<a name="events-sqs-queueconfig"></a>

[Create an SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configure-create-queue.html) to serve as an event source for your Lambda function\. Then configure the queue to allow time for your Lambda function to process each batch of events—and for Lambda to retry in response to throttling errors as it scales up\.

To allow your function time to process each batch of records, set the source queue's visibility timeout to at least six times the [timeout that you configure](configuration-function-common.md#configuration-common-summary) on your function\. The extra time allows for Lambda to retry if your function is throttled while processing a previous batch\.

If your function fails to process a message multiple times, Amazon SQS can send it to a [dead\-letter queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)\. When your function returns an error, Lambda leaves it in the queue\. After the visibility timeout occurs, Lambda receives the message again\. To send messages to a second queue after a number of receives, configure a dead\-letter queue on your source queue\.

**Note**  
Make sure that you configure the dead\-letter queue on the source queue, not on the Lambda function\. The dead\-letter queue that you configure on a function is used for the function's [asynchronous invocation queue](invocation-async.md), not for event source queues\.

If your function returns an error, or can't be invoked because it's at maximum concurrency, processing might succeed with additional attempts\. To give messages a better chance to be processed before sending them to the dead\-letter queue, set the `maxReceiveCount` on the source queue's redrive policy to at least **5**\.

## Execution role permissions<a name="events-sqs-permissions"></a>

Lambda needs the following permissions to manage messages in your Amazon SQS queue\. Add them to your function's [execution role](lambda-intro-execution-role.md)\.
+ [sqs:ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [sqs:DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [sqs:GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)

## Configuring a queue as an event source<a name="events-sqs-eventsource"></a>

Create an event source mapping to tell Lambda to send items from your queue to a Lambda function\. You can create multiple event source mappings to process items from multiple queues with a single function\. When Lambda invokes the target function, the event can contain multiple items, up to a configurable maximum *batch size*\.

To configure your function to read from Amazon SQS in the Lambda console, create an **SQS** trigger\.

**To create a trigger**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of a function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Choose the **SQS** trigger type\.

1. Configure the required options, and then choose **Add**\.

Lambda supports the following options for Amazon SQS event sources\.

**Event source options**
+ **SQS queue** – The Amazon SQS queue to read records from\.
+ **Batch size** – The number of records to send to the function in each batch\. For a standard queue, this can be up to 10,000 records\. For a FIFO queue, the maximum is 10\. For a batch size over 10, you must also set the `MaximumBatchingWindowInSeconds` parameter to at least 1 second\. Lambda passes all of the records in the batch to the function in a single call, as long as the total size of the events doesn't exceed the [invocation payload size quota](gettingstarted-limits.md) for synchronous invocation \(6 MB\)\.

  Both Lambda and Amazon SQS generate metadata for each record\. This additional metadata is counted towards the total payload size and can cause the total number of records sent in a batch to be lower than your configured batch size\. The metadata fields that Amazon SQS sends can be variable in length\. For more information about the Amazon SQS metadata fields, see the [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) API operation documentation in the *Amazon Simple Queue Service API Reference*\.
+ **Batch window ** – The maximum amount of time to gather records before invoking the function, in seconds\. This applies only to standard queues\.

  If you're using a batch window greater than 0 seconds, you must account for the increased processing time in your queue visibility timeout\. We recommend setting your queue visibility timeout to six times your function timeout, plus the value of `MaximumBatchingWindowInSeconds`\. This allows time for your Lambda function to process each batch of events and to retry in the event of a throttling error\.
**Note**  
If your batch window is greater than 0, and `(batch window) + (function timeout) > (queue visibility timeout)`, then your effective queue visibility timeout is `(batch window) + (function timeout) + 30s`\.

  Lambda processes up to five batches at a time\. This means that there are a maximum of five workers available to batch and process messages in parallel at any one time\. If messages are still available, Lambda increases the number of processes that are reading batches by up to 60 more instances per minute\. The maximum number of batches that an event source mapping can process simultaneously is 1,000\. For more information, see [Scaling and processing](#events-sqs-scaling)\.
+ **Enabled** – The status of the event source mapping\. Set to true to enable the event source mapping\. Set to false to stop processing records\.

**Note**  
Amazon SQS has a perpetual free tier for requests\. Beyond the free tier, Amazon SQS charges per million requests\. While your event source mapping is active, Lambda makes requests to the queue to get items\. For pricing details, see [Amazon SQS pricing](http://aws.amazon.com/sqs/pricing)\.

To manage the event source configuration later, in the Lambda console, choose the **SQS** trigger in the designer\.

Configure your function timeout to allow enough time to process an entire batch of items\. If items take a long time to process, choose a smaller batch size\. A large batch size can improve efficiency for workloads that are very fast or have a lot of overhead\. However, if your function returns an error, all items in the batch return to the queue\. If you configure [reserved concurrency](configuration-concurrency.md) on your function, set a minimum of five concurrent executions to reduce the chance of throttling errors when Lambda invokes your function\. To eliminate the chance of throttling errors, set the reserved concurrency value to 1,000, which is the maximum number of concurrent executions for an Amazon SQS event source\.

## Event source mapping APIs<a name="events-sqs-api"></a>

To manage an event source with the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
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

## Reporting batch item failures<a name="services-sqs-batchfailurereporting"></a>

When your Lambda function encounters an error while processing a batch, all messages in that batch become visible in the queue again by default, including messages that Lambda processed successfully\. As a result, your function can end up processing the same message several times\.

To avoid reprocessing all messages in a failed batch, you can configure your event source mapping to make only the failed messages visible again\. To do this, when configuring your event source mapping, include the value `ReportBatchItemFailures` in the `FunctionResponseTypes` list\. This lets your function return a partial success, which can help reduce the number of unnecessary retries on records\.

### Report syntax<a name="sqs-batchfailurereporting-syntax"></a>

After you include `ReportBatchItemFailures` in your event source mapping configuration, you can return a list of the failed message IDs in your function response\. For example, suppose you have a batch of five messages, with message IDs `id1`, `id2`, `id3`, `id4`, and `id5`\. Your function successfully processes `id1`, `id3`, and `id5`\. To make messages `id2` and `id4` visible again in your queue, your response syntax should look like the following:

```
{ 
  "batchItemFailures": [ 
        {
            "itemIdentifier": "id2"
        },
        {
            "itemIdentifier": "id4"
        }
    ]
}
```

To return the list of failed message IDs in the batch, you can use a `SQSBatchResponse` class object or create your own custom class\. Here is an example of a response that uses the `SQSBatchResponse` object\.

```
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
 
import java.util.ArrayList;
import java.util.List;
 
public class ProcessSQSMessageBatch implements RequestHandler<SQSEvent, SQSBatchResponse> {
    @Override
    public SQSBatchResponse handleRequest(SQSEvent sqsEvent, Context context) {
 
         List<SQSBatchResponse.BatchItemFailure> batchItemFailures = new ArrayList<SQSBatchResponse.BatchItemFailure>();
         String messageId = "";
         for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
             try {
                 //process your message
                 messageId = message.getMessageId();
             } catch (Exception e) {
                 //Add failed message identifier to the batchItemFailures list
                 batchItemFailures.add(new SQSBatchResponse.BatchItemFailure(messageId));
             }
         }
         return new SQSBatchResponse(batchItemFailures);
     }
}
```

To use this feature, your function must gracefully handle errors\. Have your function logic catch all exceptions and report the messages that result in failure in `batchItemFailures` in your function response\. If your function throws an exception, the entire batch is considered a complete failure\.

**Note**  
If you're using this feature with a FIFO queue, your function should stop processing messages after the first failure and return all failed and unprocessed messages in `batchItemFailures`\. This helps preserve the ordering of messages in your queue\.

### Success and failure conditions<a name="sqs-batchfailurereporting-conditions"></a>

Lambda treats a batch as a complete success if your function returns any of the following:
+ An empty `batchItemFailures` list
+ A null `batchItemFailures` list
+ An empty `EventResponse`
+ A null `EventResponse`

Lambda treats a batch as a complete failure if your function returns any of the following:
+ An invalid JSON response
+ An empty string `itemIdentifier`
+ A null `itemIdentifier`
+ An `itemIdentifier` with a bad key name
+ An `itemIdentifier` value with a message ID that doesn't exist

### CloudWatch metrics<a name="sqs-batchfailurereporting-metrics"></a>

To determine whether your function is correctly reporting batch item failures, you can monitor the `NumberOfMessagesDeleted` and `ApproximateAgeOfOldestMessage` Amazon SQS metrics in Amazon CloudWatch\.
+ `NumberOfMessagesDeleted` tracks the number of messages removed from your queue\. If this drops to 0, this is a sign that your function response is not correctly returning failed messages\.
+ `ApproximateAgeOfOldestMessage` tracks how long the oldest message has stayed in your queue\. A sharp increase in this metric can indicate that your function is not correctly returning failed messages\.

## Amazon SQS configuration parameters<a name="services-sqs-params"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Amazon SQS\.


**Event source parameters that apply to Amazon SQS**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  BatchSize  |  N  |  10  |  For standard queues, the maximum is 10,000\. For FIFO queues, the maximum is 10\.  | 
|  Enabled  |  N  |  true  |   | 
|  EventSourceArn  |  Y  |  |  The ARN of the data stream or a stream consumer  | 
|  FunctionName  |  Y  |   |   | 
|  FunctionResponseTypes  |  N  |   |  To let your function report specific failures in a batch, include the value `ReportBatchItemFailures` in `FunctionResponseTypes`\. For more information, see [Reporting batch item failures](#services-sqs-batchfailurereporting)\.  | 
|  MaximumBatchingWindowInSeconds  |  N  |  0  |   | 