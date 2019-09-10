# Asynchronous Invocation<a name="invocation-async"></a>

When you invoke a function asynchronously, Lambda sends the event to a queue\. A separate process reads events from the queue and runs your function\. When the event is added to the queue, Lambda returns a success response without additional information\. To invoke a function asynchronously, set the invocation type parameter to `Event`\.

```
$ aws lambda invoke --function-name my-function  --invocation-type Event --payload '{ "key": "value" }' response.json
{
    "StatusCode": 202
}
```

The output file \(`response.json`\) does not contain any information, but is still created when you run this command\. If Lambda is not able to add the event to the queue, the error message appears in the command output\.

Lambda manages the function's asynchronous invocation queue and attempts to retry failed events automatically\. If the function returns an error, Lambda attempts to run it two more times, with a one minute wait between the first two attempts, and two minutes between the second and third\. Function errors include errors returned by the function's code, and errors returned by the function's runtime, such as timeouts\. If all 3 attempts fail, Lambda sends the event to a [dead\-letter queue](#dlq), if configured\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-types-retries.png)

If the function does not have enough concurrency available to process all events, additional requests are throttled\. For throttling errors \(429\) and system errors \(500\-series\), Lambda returns the event to the queue and attempts to run the function again for up to 6 hours\. The retry interval increases exponentially from 1 second after the first attempt to a maximum of 5 minutes, but may be longer if the queue is backed up\. Lambda also reduces the rate at which it reads events from the queue, and events further back in the queue may not be read\. The maximum amount of time that an event can be in the queue is 4 days\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-types-throttle.png)

Even if your function does not return an error, it is possible for it to receive the same event from Lambda multiple times, as the queue itself is eventually consistent\. If the function can't keep up with incoming events, events may also be deleted from the queue without being sent to the function\. Ensure that your function code gracefully handles duplicate events, and that you have enough concurrency available to handle all invocations\. Configure a dead\-letter queue to retain deleted events\.

## AWS Lambda Function Dead Letter Queues<a name="dlq"></a>

When all three attempts to process an asynchronous invocation fail, Lambda can send the event to an Amazon SQS queue or an Amazon SNS topic\. Configure your function with a dead\-letter queue to save these events for further processing\.

If you don't have a queue or topic, create one\. Choose the target type that matches your use case\.
+ [Amazon SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-create-queue.html) – A queue holds failed events until they are retrieved\. You can retrieve events manually, or [configure Lambda to read from the queue](with-sqs.md) and invoke a function\.

  Create a queue in the [Amazon SQS console](https://console.aws.amazon.com/sqs)\.
+ [Amazon SNS topic](https://docs.aws.amazon.com/sns/latest/gsg/CreateTopic.html) – A topic relays failed events to one or more destinations\. You can configure a topic to send events to an email address, a Lambda function, or an HTTP endpoint\.

  Create a topic in the [Amazon SNS console](https://console.aws.amazon.com/sns/home)\.

To send events to a queue or topic, your function needs additional permissions\. Add a policy with the required permissions to your function's [execution role](lambda-intro-execution-role.md)\.
+ **Amazon SQS** – [sqs:SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) 
+ **Amazon SNS** – [sns:Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html) 

If the target queue or topic is encrypted with a customer managed key, the execution role must also be a user in the key's [resource\-based policy](https://docs.aws.amazon.com/kms/latest/developerguide/key-policies.html)\.

After creating the target and updating your function's execution role, add the dead\-letter queue to your function\. You can configure multiple functions to send events to the same target\.

**To configure a dead\-letter queue**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Debugging and error handling**, set **DLQ resource** to **Amazon SQS** or **Amazon SNS**\.

1. Choose the target queue or topic\.

1. Choose **Save**\.

To configure a dead\-letter queue with the AWS CLI, use the `update-function-configuration` command\.

```
$ aws lambda update-function-configuration --function-name my-function \
--dead-letter-config TargetArn=arn:aws:sns:us-east-2:123456789012:my-topic
```

Lambda sends the event to the dead\-letter queue as\-is, with additional information in attributes\. You can use this information to identify the error that the function returned, or correlate the event with logs or an AWS X\-Ray trace\.

**DLQ Message Attributes**
+ **RequestID** \(String\) – The ID of the invocation request\. Request IDs appear in function logs\. You can also use the X\-Ray SDK to record the request ID on an attribute in the trace, which you can then search for by request ID in the X\-Ray console\. See the [error processor sample](sample-errorprocessor.md) for an example\.
+ **ErrorCode** \(Number\) – The HTTP status code\.
+ **ErrorMessage** \(String\) – The first 1 KB of the error message\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-dlq-attributes.png)

If Lambda can't send a message to the dead\-letter queue, it deletes the event and emits the [DeadLetterErrors](monitoring-functions-metrics.md) metric\. This can happen due to lack of permissions, or if the total size of the message exceeds the limit for the target queue or topic\. For example, if an Amazon SNS notification with a body close to 256 KB triggers a function that results in an error, the additional event data added by Amazon SNS, combined with the attributes added by Lambda, can cause the message to exceed the maximum size allowed in the DLQ\. 

If you are using Amazon SQS as an event source, configure a DLQ on the Amazon SQS queue itself and not the Lambda function\. For more information, see [Using AWS Lambda with Amazon SQS](with-sqs.md)\.