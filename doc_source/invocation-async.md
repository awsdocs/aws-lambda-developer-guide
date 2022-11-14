# Asynchronous invocation<a name="invocation-async"></a>

Several AWS services, such as Amazon Simple Storage Service \(Amazon S3\) and Amazon Simple Notification Service \(Amazon SNS\), invoke functions asynchronously to process events\. When you invoke a function asynchronously, you don't wait for a response from the function code\. You hand off the event to Lambda and Lambda handles the rest\. You can configure how Lambda handles errors, and can send invocation records to a downstream resource to chain together components of your application\.

**Topics**
+ [How Lambda handles asynchronous invocations](#async-overview)
+ [Configuring error handling for asynchronous invocation](#invocation-async-errors)
+ [Configuring destinations for asynchronous invocation](#invocation-async-destinations)
+ [Asynchronous invocation configuration API](#invocation-async-api)
+ [Dead\-letter queues](#invocation-dlq)

## How Lambda handles asynchronous invocations<a name="async-overview"></a>

The following diagram shows clients invoking a Lambda function asynchronously\. Lambda queues the events before sending them to the function\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-async.png)

For asynchronous invocation, Lambda places the event in a queue and returns a success response without additional information\. A separate process reads events from the queue and sends them to your function\. To invoke a function asynchronously, set the invocation type parameter to `Event`\.

```
aws lambda invoke \
  --function-name my-function  \
      --invocation-type Event \
          --cli-binary-format raw-in-base64-out \
              --payload '{ "key": "value" }' response.json
```

The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.

```
{
    "StatusCode": 202
}
```

The output file \(`response.json`\) doesn't contain any information, but is still created when you run this command\. If Lambda isn't able to add the event to the queue, the error message appears in the command output\.

Lambda manages the function's asynchronous event queue and attempts to retry on errors\. If the function returns an error, Lambda attempts to run it two more times, with a one\-minute wait between the first two attempts, and two minutes between the second and third attempts\. Function errors include errors returned by the function's code and errors returned by the function's runtime, such as timeouts\.

If the function doesn't have enough concurrency available to process all events, additional requests are throttled\. For throttling errors \(429\) and system errors \(500\-series\), Lambda returns the event to the queue and attempts to run the function again for up to 6 hours\. The retry interval increases exponentially from 1 second after the first attempt to a maximum of 5 minutes\. If the queue contains many entries, Lambda increases the retry interval and reduces the rate at which it reads events from the queue\.

Even if your function doesn't return an error, it's possible for it to receive the same event from Lambda multiple times because the queue itself is eventually consistent\. If the function can't keep up with incoming events, events might also be deleted from the queue without being sent to the function\. Ensure that your function code gracefully handles duplicate events, and that you have enough concurrency available to handle all invocations\.

When the queue is very long, new events might age out before Lambda has a chance to send them to your function\. When an event expires or fails all processing attempts, Lambda discards it\. You can [configure error handling](#invocation-async-errors) for a function to reduce the number of retries that Lambda performs, or to discard unprocessed events more quickly\.

You can also configure Lambda to send an invocation record to another service\. Lambda supports the following [destinations](#invocation-async-destinations) for asynchronous invocation\.
+ **Amazon SQS** – A standard SQS queue\.
+ **Amazon SNS** – An SNS topic\.
+ **AWS Lambda** – A Lambda function\.
+ **Amazon EventBridge** – An EventBridge event bus\.

The invocation record contains details about the request and response in JSON format\. You can configure separate destinations for events that are processed successfully, and events that fail all processing attempts\. Alternatively, you can configure an Amazon SQS queue or Amazon SNS topic as a [dead\-letter queue](#invocation-dlq) for discarded events\. For dead\-letter queues, Lambda only sends the content of the event, without details about the response\.

**Note**  
To prevent a function from triggering, you can set the function's reserved concurrency to zero\. When you set reserved concurrency to zero for an asynchronously\-invoked function, Lambda begins sending new events to the configured [dead\-letter queue](#invocation-dlq) or the on\-failure [event destination](#invocation-async-destinations), without any retries\. To process events that were sent while reserved concurrency was set to zero, you need to consume the events from the dead\-letter queue or the on\-failure event destination\.

## Configuring error handling for asynchronous invocation<a name="invocation-async-errors"></a>

Use the Lambda console to configure error handling settings on a function, a version, or an alias\.

**To configure error handling**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Asynchronous invocation**\.

1. Under **Asynchronous invocation**, choose **Edit**\.

1. Configure the following settings\.
   + **Maximum age of event** – The maximum amount of time Lambda retains an event in the asynchronous event queue, up to 6 hours\.
   + **Retry attempts** – The number of times Lambda retries when the function returns an error, between 0 and 2\.

1. Choose **Save**\.

When an invocation event exceeds the maximum age or fails all retry attempts, Lambda discards it\. To retain a copy of discarded events, configure a failed\-event destination\.

## Configuring destinations for asynchronous invocation<a name="invocation-async-destinations"></a>

To send records of asynchronous invocations to another service, add a destination to your function\. You can configure separate destinations for events that fail processing and events that are successfully processed\. Like error handling settings, you can configure destinations on a function, a version, or an alias\.

The following example shows a function that is processing asynchronous invocations\. When the function returns a success response or exits without throwing an error, Lambda sends a record of the invocation to an EventBridge event bus\. When an event fails all processing attempts, Lambda sends an invocation record to an Amazon SQS queue\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-destinations.png)

To send events to a destination, your function needs additional permissions\. Add a policy with the required permissions to your function's [execution role](lambda-intro-execution-role.md)\. Each destination service requires a different permission, as follows:
+ **Amazon SQS** – [sqs:SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) 
+ **Amazon SNS** – [sns:Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html) 
+ **Lambda** – [InvokeFunction](API_Invoke.md) 
+ **EventBridge** – [events:PutEvents](https://docs.aws.amazon.com/eventbridge/latest/APIReference/API_PutEvents.html)

Add destinations to your function in the Lambda console's function visualization\.

**To configure a destination for asynchronous invocation records**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Under **Function overview**, choose **Add destination**\.

1. For **Source**, choose **Asynchronous invocation**\.

1. For **Condition**, choose from the following options:
   + **On failure** – Send a record when the event fails all processing attempts or exceeds the maximum age\.
   + **On success** – Send a record when the function successfully processes an asynchronous invocation\.

1. For **Destination type**, choose the type of resource that receives the invocation record\.

1. For **Destination**, choose a resource\.

1. Choose **Save**\.

When an invocation matches the condition, Lambda sends a JSON document with details about the invocation to the destination\.

**Destination\-specific JSON format**
+ For Amazon SQS and Amazon SNS \(`SnsDestination` and `SqsDestination`\), the invocation record is passed as the `Message` to the destination\.
+ For Lambda \(`LambdaDestination`\), the invocation record is passed as the payload to the function\.
+ For EventBridge \(`EventBridgeDestination`\), the invocation record is passed as the `detail` in the [PutEvents](https://docs.aws.amazon.com/eventbridge/latest/APIReference/API_PutEvents.html) call\. The value for the `source` event field is `lambda`\. The value for the `detail-type` event field is either *Lambda Function Invocation Result – Success* or *Lambda Function Invocation Result – Failure*\. The `resource` event field contains the function and destination Amazon Resource Names \(ARNs\)\. For other event fields, see [Amazon EventBridge events](https://docs.aws.amazon.com/eventbridge/latest/userguide/aws-events.html)\.

The following example shows an invocation record for an event that failed three processing attempts due to a function error\.

**Example invocation record**  

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

## Asynchronous invocation configuration API<a name="invocation-async-api"></a>

To manage asynchronous invocation settings with the AWS CLI or AWS SDK, use the following API operations\.
+ [PutFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutFunctionEventInvokeConfig.html)
+ [GetFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionEventInvokeConfig.html)
+ [UpdateFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateFunctionEventInvokeConfig.html)
+ [ListFunctionEventInvokeConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListFunctionEventInvokeConfigs.html)
+ [DeleteFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteFunctionEventInvokeConfig.html)

To configure asynchronous invocation with the AWS CLI, use the `put-function-event-invoke-config` command\. The following example configures a function with a maximum event age of 1 hour and no retries\.

```
aws lambda put-function-event-invoke-config --function-name error \
--maximum-event-age-in-seconds 3600 --maximum-retry-attempts 0
```

You should see the following output:

```
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

The `put-function-event-invoke-config` command overwrites any existing configuration on the function, version, or alias\. To configure an option without resetting others, use `update-function-event-invoke-config`\. The following example configures Lambda to send a record to an SQS queue named `destination` when an event can't be processed\.

```
aws lambda update-function-event-invoke-config --function-name error \
--destination-config '{"OnFailure":{"Destination": "arn:aws:sqs:us-east-2:123456789012:destination"}}'
```

You should see the following output:

```
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

## Dead\-letter queues<a name="invocation-dlq"></a>

As an alternative to an [on\-failure destination](#invocation-async-destinations), you can configure your function with a dead\-letter queue to save discarded events for further processing\. A dead\-letter queue acts the same as an on\-failure destination in that it is used when an event fails all processing attempts or expires without being processed\. However, a dead\-letter queue is part of a function's version\-specific configuration, so it is locked in when you publish a version\. On\-failure destinations also support additional targets and include details about the function's response in the invocation record\.

To reprocess events in a dead\-letter queue, you can set it as an event source for your Lambda function\. Alternatively, you can manually retrieve the events\.

You can choose an Amazon SQS queue or Amazon SNS topic for your dead\-letter queue\. If you don't have a queue or topic, create one\. Choose the target type that matches your use case\.
+ [Amazon SQS queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-create-queue.html) – A queue holds failed events until they're retrieved\. Choose an Amazon SQS queue if you expect a single entity, such as a Lambda function or CloudWatch alarm, to process the failed event\. For more information, see [Using Lambda with Amazon SQS](with-sqs.md)\.

  Create a queue in the [Amazon SQS console](https://console.aws.amazon.com/sqs)\.
+ [Amazon SNS topic](https://docs.aws.amazon.com/sns/latest/gsg/CreateTopic.html) – A topic relays failed events to one or more destinations\. Choose an Amazon SNS topic if you expect multiple entities to act on a failed event\. For example, you can configure a topic to send events to an email address, a Lambda function, and/or an HTTP endpoint\. For more information, see [Using AWS Lambda with Amazon SNS](with-sns.md)\.

  Create a topic in the [Amazon SNS console](https://console.aws.amazon.com/sns/home)\.

To send events to a queue or topic, your function needs additional permissions\. Add a policy with the required permissions to your function's [execution role](lambda-intro-execution-role.md)\.
+ **Amazon SQS** – [sqs:SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) 
+ **Amazon SNS** – [sns:Publish](https://docs.aws.amazon.com/sns/latest/api/API_Publish.html) 

If the target queue or topic is encrypted with a customer managed key, the execution role must also be a user in the key's [resource\-based policy](https://docs.aws.amazon.com/kms/latest/developerguide/key-policies.html)\.

After creating the target and updating your function's execution role, add the dead\-letter queue to your function\. You can configure multiple functions to send events to the same target\.

**To configure a dead\-letter queue**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Asynchronous invocation**\.

1. Under **Asynchronous invocation**, choose **Edit**\.

1. Set **DLQ resource** to **Amazon SQS** or **Amazon SNS**\.

1. Choose the target queue or topic\.

1. Choose **Save**\.

To configure a dead\-letter queue with the AWS CLI, use the `update-function-configuration` command\.

```
aws lambda update-function-configuration --function-name my-function \
--dead-letter-config TargetArn=arn:aws:sns:us-east-2:123456789012:my-topic
```

Lambda sends the event to the dead\-letter queue as\-is, with additional information in attributes\. You can use this information to identify the error that the function returned, or to correlate the event with logs or an AWS X\-Ray trace\.

**Dead\-letter queue message attributes**
+ **RequestID** \(String\) – The ID of the invocation request\. Request IDs appear in function logs\. You can also use the X\-Ray SDK to record the request ID on an attribute in the trace\. You can then search for traces by request ID in the X\-Ray console\. For an example, see the [error processor sample](samples-errorprocessor.md)\.
+ **ErrorCode** \(Number\) – The HTTP status code\.
+ **ErrorMessage** \(String\) – The first 1 KB of the error message\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-dlq-attributes.png)

If Lambda can't send a message to the dead\-letter queue, it deletes the event and emits the [DeadLetterErrors](monitoring-metrics.md) metric\. This can happen because of lack of permissions, or if the total size of the message exceeds the limit for the target queue or topic\. For example, if an Amazon SNS notification with a body close to 256 KB triggers a function that results in an error, the additional event data added by Amazon SNS, combined with the attributes added by Lambda, can cause the message to exceed the maximum size allowed in the dead\-letter queue\. 

If you're using Amazon SQS as an event source, configure a dead\-letter queue on the Amazon SQS queue itself and not on the Lambda function\. For more information, see [Using Lambda with Amazon SQS](with-sqs.md)\.