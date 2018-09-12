# Dead Letter Queues<a name="dlq"></a>

Any Lambda function invoked **asynchronously** is retried twice before the event is discarded\. If the retries fail and you're unsure why, use Dead Letter Queues \(DLQ\) to direct unprocessed events to an Amazon SQS queue or an Amazon SNS topic to analyze the failure\. 

 AWS Lambda directs events that cannot be processed to the specified [Amazon SNS topic](http://docs.aws.amazon.com/sns/latest/gsg/CreateTopic.html) topic or [Amazon SQS queue](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-create-queue.html)\. Functions that don't specify a DLQ will discard events after they have exhausted their retries\. For more information about retry policies, see [Understanding Retry Behavior](retries-on-errors.md)\.

You configure a DLQ by specifying the Amazon Resource Name *TargetArn* value on the Lambda function's `DeadLetterConfig` parameter \(whether it's an [Amazon SQS](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-create-queue.html) queue or an [Amazon SNS](http://docs.aws.amazon.com/sns/latest/dg/CreateTopic.html) topic\)\. 

```
{
    "Code": {
        "ZipFile": blob,
        "S3Bucket": “string”,
        "S3Key": “string”,
        "S3ObjectVersion": “string”
    },
    "Description": "string",
    "FunctionName": "string",
    "Handler": "string",
    "MemorySize": number,
    "Role": "string",
    "Runtime": "string",
    "Timeout": number
    "Publish": bool,
    "DeadLetterConfig": {
        "TargetArn": "string" 
    }
}
```

In addition, you need to add permissions to the [execution role](intro-permission-model.md) of your Lambda function, depending on which service you have directed unprocessed events:
+ **For Amazon SQS:**[ SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) 
+ **For Amazon SNS:**[ Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html) 

The payload written to the DLQ target ARN is the original event payload with no modifications to the message body\. The attributes of the message, described next, contain information to help you understand why the event wasn’t processed: 


| Name | Type | Value | 
| --- | --- | --- | 
| RequestID  | String | Unique request identifier  | 
| ErrorCode | Number | 3\-digit HTTP error code | 
| ErrorMessage | String | Error message \(truncated to 1 KB\)  | 

If the event payload consistently fails to reach the target ARN, AWS Lambda increments a [CloudWatch metric](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring//viewing_metrics_with_cloudwatch.html) called `DeadLetterErrors` and then deletes the event payload\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/DLQ.png)

**Note**  
If you are using Amazon SQS as an event source, we recommend configuring a DLQ on the Amazon SQS queue itself and not the Lambda function\. For more information, see [Amazon SQS Dead\-Letter Queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)\.