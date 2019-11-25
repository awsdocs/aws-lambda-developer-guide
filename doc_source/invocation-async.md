# Asynchronous Invocation<a name="invocation-async"></a>

When you invoke a function asynchronously, Lambda sends the event to a queue\. A separate process reads events from the queue and runs your function\. When the event is added to the queue, Lambda returns a success response without additional information\. To invoke a function asynchronously, set the invocation type parameter to `Event`\.

```
$ aws lambda invoke --function-name my-function  --invocation-type Event --payload '{ "key": "value" }' response.json
{
    "StatusCode": 202
}
```

The output file \(`response.json`\) doesn't contain any information, but is still created when you run this command\. If Lambda isn't able to add the event to the queue, the error message appears in the command output\.

Lambda manages the function's asynchronous invocation queue and attempts to retry failed events automatically\. If the function returns an error, Lambda attempts to run it two more times, with a one\-minute wait between the first two attempts, and two minutes between the second and third attempts\. Function errors include errors returned by the function's code and errors returned by the function's runtime, such as timeouts\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-types-retries.png)

If the function doesn't have enough concurrency available to process all events, additional requests are throttled\. For throttling errors \(429\) and system errors \(500\-series\), Lambda returns the event to the queue and attempts to run the function again for up to 6 hours\. The retry interval increases exponentially from 1 second after the first attempt to a maximum of 5 minutes\. However, it might be longer if the queue is backed up\. Lambda also reduces the rate at which it reads events from the queue\. Events further back in the queue might not be read\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-types-throttle.png)

Even if your function doesn't return an error, it's possible for it to receive the same event from Lambda multiple times because the queue itself is eventually consistent\. If the function can't keep up with incoming events, events might also be deleted from the queue without being sent to the function\. Ensure that your function code gracefully handles duplicate events, and that you have enough concurrency available to handle all invocations\.

You can [configure error handling](#invocation-async-errors) for a function to reduce the number of retries that Lambda performs, or to discard unprocessed events more quickly\. You can also configure the function to send an invocation record to another service\. Lambda supports the following [destinations](#invocation-async-destinations) for asynchronous invocation\.
+ **Amazon SQS** – An SQS queue\.
+ **Amazon SNS** – An SNS topic\.
+ **AWS Lambda** – A Lambda function\.
+ **Amazon EventBridge** – An EventBridge event bus\.

The invocation record contains details about the request and response in JSON format\. It can be sent for both failed and successful invocations\. Alternatively, you can configure an SQS queue or SNS topic as a [dead\-letter queue](#dlq) for events that fail processing\. For dead\-letter queues, Lambda only sends the event, without details about the response\.

**Topics**
+ [Configuring Error Handling for Asynchronous Invocation](#invocation-async-errors)
+ [Configuring Destinations for Asynchronous Invocation](#invocation-async-destinations)
+ [Asynchronous Invocation Configuration API](#invocation-async-api)
+ [AWS Lambda Function Dead Letter Queues](#dlq)

## Configuring Error Handling for Asynchronous Invocation<a name="invocation-async-errors"></a>

Use the Lambda console to configure error handling settings on a function, a version, or an alias\.

**To configure error handling**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Asynchronous invocation**, choose **Edit**\.

1. Configure the following settings\.
   + **Maximum age of event** – The maximum amount of time Lambda retains an event in the asynchronous invocation queue, up to 6 hours\.
   + **Retry attempts** – The number of times Lambda retries when the function returns an error, between 0 and 2\.

1. Choose **Save**\.

When an invocation event exceeds the maximum age or fails all retry attempts, Lambda discards it\. To retain a copy of discarded events, configure an on\-failure destination\.

## Configuring Destinations for Asynchronous Invocation<a name="invocation-async-destinations"></a>

To send records of asynchronous invocations to another service, add a destination to your function\. You can configure separate destinations for events that fail processing and events that are successfully processed\. Add destinations to your function in the Lambda console's function designer\.

To send events to a destination, your function needs additional permissions\. Add a policy with the required permissions to your function's [execution role](lambda-intro-execution-role.md)\.
+ **Amazon SQS** – [sqs:SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) 
+ **Amazon SNS** – [sns:Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html) 
+ **Lambda** – [lambda:InvokeFunction](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html)
+ **EventBridge** – [events:PutEvents](https://docs.aws.amazon.com/eventbridge/latest/APIReference/API_PutEvents.html)

**To configure a destination for asynchronous invocation records**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose **Add destination**\.

1. For **Source**, choose **Asynchronous invocation**\.

1. For **Condition** choose from the following options\.
   + **On failure** – Send a record when the event fails all processing attempts or exceeds the maximum age\.
   + **On success** – Send a record when the function successfully processes an asynchronous invocation\.

1. For **Destination**, enter the ARN of an SQS queue, SNS topic, Lambda function, or EventBridge event bus\.

1. Choose **Save**\.

When an invocation matches the condition, Lambda sends a JSON document with details about the invocation to the destination\. The following example shows an invocation record for an event that failed processing due to a function error\.

**Example Invocation Record**  

```
{
    "version": "1.0",
    "timestamp": "2019-11-14T18:16:05.568Z",
    "requestContext": {
        "requestId": "e4b46cbf-b738-xmpl-8880-a18cdf61200e",
        "functionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function:$LATEST",
        "condition": "RetriesExhausted",
        "approximateInvokeCount": 3
    },
    "requestPayload": {
        "ORDER_IDS": [
            "9e07af03-ce31-4ff3-xmpl-36dce652cb4f",
            "637de236-e7b2-464e-xmpl-baf57f86bb53",
            "a81ddca6-2c35-45c7-xmpl-c3a03a31ed15"
        ]
    },
    "responseContext": {
        "statusCode": 200,
        "executedVersion": "$LATEST",
        "functionError": "Unhandled"
    },
    "responsePayload": {
        "errorMessage": "RequestId: e4b46cbf-b738-xmpl-8880-a18cdf61200e Process exited before completing request"
    }
}
```

The invocation record contains details about the event, the response, and the reason that the record was sent\.

## Asynchronous Invocation Configuration API<a name="invocation-async-api"></a>

To manage asynchronous invocation settings with the AWS CLI or AWS SDK, use the following API operations\.
+ [PutFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/PutFunctionEventInvokeConfig.html)
+ [GetFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/GetFunctionEventInvokeConfig.html)
+ [UpdateFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/UpdateFunctionEventInvokeConfig.html)
+ [ListFunctionEventInvokeConfigs](https://docs.aws.amazon.com/lambda/latest/dg/ListFunctionEventInvokeConfigs.html)
+ [DeleteFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/DeleteFunctionEventInvokeConfig.html)

To configure asynchronous invocation with the AWS CLI, use the `put-function-event-invoke-config` command\. The following example configures a function with a maximum event age of 1 hour and no retries\.

```
$ aws lambda put-function-event-invoke-config --function-name error \
--maximum-event-age-in-seconds 3600 --maximum-retry-attempts 0
{
    "LastModified": 1573686021.479,
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:error:$LATEST",
    "MaximumRetryAttempts": 0,
    "MaximumEventAgeInSeconds": 3600,
    "DestinationConfig": {
        "OnSuccess": {},
        "OnFailure": {}
    }
}
```

The `put-function-event-invoke-config` command overwrites any existing configuration on the function, version, or alias\. To configure an option without resetting others, use `update-function-event-invoke-config`\. The following example configures Lambda to send a record to an SQS queue named `destination` when an event can'ta be processed\.

```
$ aws lambda update-function-event-invoke-config --function-name error \
--destination-config '{"OnFailure":{"Destination": "arn:aws:sqs:us-east-2:123456789012:destination"}}'
{
    "LastModified": 1573687896.493,
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:error:$LATEST",
    "MaximumRetryAttempts": 0,
    "MaximumEventAgeInSeconds": 3600,
    "DestinationConfig": {
        "OnSuccess": {},
        "OnFailure": {
            "Destination": "arn:aws:sqs:us-east-2:123456789012:destination"
        }
    }
}
```

## AWS Lambda Function Dead Letter Queues<a name="dlq"></a>

When all attempts to process an asynchronous invocation fail, Lambda can send the event to an Amazon SQS queue or an Amazon SNS topic\. Configure your function with a dead\-letter queue to save these events for further processing\.

If you don't have a queue or topic, create one\. Choose the target type that matches your use case\.
+ [Amazon SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-create-queue.html) – A queue holds failed events until they're retrieved\. You can retrieve events manually, or you can [configure Lambda to read from the queue](with-sqs.md) and invoke a function\.

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

1. Under **Asynchronous invocation**, choose **Edit**\.

1. Set **DLQ resource** to **Amazon SQS** or **Amazon SNS**\.

1. Choose the target queue or topic\.

1. Choose **Save**\.

To configure a dead\-letter queue with the AWS CLI, use the `update-function-configuration` command\.

```
$ aws lambda update-function-configuration --function-name my-function \
--dead-letter-config TargetArn=arn:aws:sns:us-east-2:123456789012:my-topic
```

Lambda sends the event to the dead\-letter queue as\-is, with additional information in attributes\. You can use this information to identify the error that the function returned, or to correlate the event with logs or an AWS X\-Ray trace\.

**Dead\-Letter Queue Message Attributes**
+ **RequestID** \(String\) – The ID of the invocation request\. Request IDs appear in function logs\. You can also use the X\-Ray SDK to record the request ID on an attribute in the trace\. You can then search for traces by request ID in the X\-Ray console\. For an example, see the [error processor sample](sample-errorprocessor.md)\.
+ **ErrorCode** \(Number\) – The HTTP status code\.
+ **ErrorMessage** \(String\) – The first 1 KB of the error message\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-dlq-attributes.png)

If Lambda can't send a message to the dead\-letter queue, it deletes the event and emits the [DeadLetterErrors](monitoring-functions-metrics.md) metric\. This can happen because of lack of permissions, or if the total size of the message exceeds the limit for the target queue or topic\. For example, if an Amazon SNS notification with a body close to 256 KB triggers a function that results in an error, the additional event data added by Amazon SNS, combined with the attributes added by Lambda, can cause the message to exceed the maximum size allowed in the dead\-letter queue\. 

If you're using Amazon SQS as an event source, configure a dead\-letter queue on the Amazon SQS queue itself and not on the Lambda function\. For more information, see [Using AWS Lambda with Amazon SQS](with-sqs.md)\.