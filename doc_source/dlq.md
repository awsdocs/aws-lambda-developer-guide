# Dead Letter Queues<a name="dlq"></a>

Any Lambda function invoked **asynchronously** is retried twice before the event is discarded\. If the retries fail and you're unsure why, use Dead Letter Queues \(DLQ\) to direct unprocessed events to an [Amazon SQS](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html) queue or an [ Amazon SNS ](lcome.html)topic to analyze the failure\. 

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

 You need to explicitly provide receive/delete/sendMessage access to your DLQ resource as part of the [execution role](intro-permission-model.html) for your Lambda function\. The payload written to the DLQ target ARN is the original event payload with no modifications to the message body\. The attributes of the message, described next, contain information to help you understand why the event wasn’t processed: 


| Name | Type | Value | 
| --- | --- | --- | 
| RequestID  | String | Unique request identifier  | 
| ErrorCode | Number | 3\-digit HTTP error code | 
| ErrorMessage | String | Error message \(truncated to 1 KB\)  | 

If the event payload consistently fails to reach the target ARN, AWS Lambda increments a [CloudWatch metric](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring//viewing_metrics_with_cloudwatch.html) called `DeadLetterErrors` and then deletes the event payload\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/DLQ.png)