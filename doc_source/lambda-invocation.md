# Invoking AWS Lambda functions<a name="lambda-invocation"></a>

You can invoke Lambda functions directly [with the Lambda console](getting-started-create-function.md#get-started-invoke-manually), the Lambda API, the AWS SDK, the AWS CLI, and AWS toolkits\. You can also configure other AWS services to invoke your function, or you can configure Lambda to read from a stream or queue and invoke your function\.

When you invoke a function, you can choose to invoke it synchronously or asynchronously\. With [synchronous invocation](invocation-sync.md), you wait for the function to process the event and return a response\. With [asynchronous](invocation-async.md) invocation, Lambda queues the event for processing and returns a response immediately\. For asynchronous invocation, Lambda handles retries and can send invocation records to a [destination](invocation-async.md#invocation-async-destinations)\.

To use your function to process data automatically, add one or more triggers\. A trigger is a Lambda resource or a resource in another service that you configure to invoke your function in response to lifecycle events, external requests, or on a schedule\. Your function can have multiple triggers\. Each trigger acts as an client invoking your function independently\. Each event that Lambda passes to your function only has data from one client or trigger\.

To process items from a stream or queue, you can create an [event source mapping](invocation-eventsourcemapping.md)\. An event source mapping is a resource in Lambda that reads items from an Amazon SQS queue, an Amazon Kinesis stream, or an Amazon DynamoDB stream, and sends them to your function in batches\. Each event that your function processes can contain hundreds or thousands of items\.

Other AWS services and resources invoke your function directly\. For example, you can configure CloudWatch Events to invoke your function on a timer, or you can configure Amazon S3 to invoke your function when an object is created\. Each service varies in the method it uses to invoke your function, the structure of the event, and how you configure it\. For more information, see [Using AWS Lambda with other services](lambda-services.md)\.

Depending on who invokes your function and how it's invoked, scaling behavior and the types of errors that occur can vary\. When you invoke a function synchronously, you receive errors in the response and can retry\. When you invoke asynchronously, use an event source mapping, or configure another service to invoke your function, the retry requirements and the way that your function scales to handle large numbers of events can vary\. For details, see [AWS Lambda function scaling](invocation-scaling.md) and [Error handling and automatic retries in AWS Lambda](invocation-retries.md)\.

**Topics**
+ [Synchronous invocation](invocation-sync.md)
+ [Asynchronous invocation](invocation-async.md)
+ [AWS Lambda event source mappings](invocation-eventsourcemapping.md)
+ [Monitoring the state of a function with the Lambda API](functions-states.md)
+ [AWS Lambda function scaling](invocation-scaling.md)
+ [Error handling and automatic retries in AWS Lambda](invocation-retries.md)
+ [Invoking Lambda functions with the AWS Mobile SDK for Android](with-on-demand-custom-android.md)