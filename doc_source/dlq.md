# AWS Lambda Function Dead Letter Queues<a name="dlq"></a>

Any Lambda function invoked **asynchronously** is retried twice before the event is discarded\. If the retries fail and you're unsure why, use Dead Letter Queues \(DLQ\) to direct unprocessed events to an Amazon SQS queue or an Amazon SNS topic to analyze the failure\. 

 AWS Lambda directs events that cannot be processed to the specified [Amazon SNS topic](https://docs.aws.amazon.com/sns/latest/gsg/CreateTopic.html) or [Amazon SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-create-queue.html)\. Functions that don't specify a DLQ will discard events after they have exhausted their retries\. For more information about retry policies, see [AWS Lambda Retry Behavior](retries-on-errors.md)\.

You configure a DLQ by specifying the Amazon Resource Name *TargetArn* value on the Lambda function's `DeadLetterConfig` parameter\.

```
{
    "Code": {
        "ZipFile": blob,
        "S3Bucket": "string",
        "S3Key": "string",
        "S3ObjectVersion": "string"
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

In addition, you need to add permissions to the [execution role](lambda-intro-execution-role.md) of your Lambda function, depending on which service you have directed unprocessed events:
+ **For Amazon SQS:**[ SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) 
+ **For Amazon SNS:**[ Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html) 

The payload written to the DLQ target ARN is the original event payload with no modifications to the message body\. The attributes of the message contain information to help you understand why the event wasn’t processed: 


**DLQ Message Attributes**  

| Name | Type | Value | 
| --- | --- | --- | 
| RequestID  | String | Unique request identifier  | 
| ErrorCode | Number | 3\-digit HTTP error code | 
| ErrorMessage | String | Error message \(truncated to 1 KB\)  | 

DLQ messages can fail to reach their target due to permissions issues, or if the total size of the message exceeds the limit for the target queue or topic\. For example, if an Amazon SNS notification with a body close to 256 KB triggers a function that results in an error, the additional event data added by Amazon SNS, combined with the attributes added by Lambda, can cause the message to exceed the maximum size allowed in the DLQ\. When it can't write to the DLQ, Lambda deletes the event and emits the [DeadLetterErrors](monitoring-functions-metrics.md) metric\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/DLQ.png)

If you are using Amazon SQS as an event source, configure a DLQ on the Amazon SQS queue itself and not the Lambda function\. For more information, see [Using AWS Lambda with Amazon SQS](with-sqs.md)\.