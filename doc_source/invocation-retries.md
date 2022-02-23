# Error handling and automatic retries in AWS Lambda<a name="invocation-retries"></a>

When you invoke a function, two types of error can occur\. Invocation errors occur when the invocation request is rejected before your function receives it\. Function errors occur when your function's code or [runtime](lambda-runtimes.md) returns an error\. Depending on the type of error, the type of invocation, and the client or service that invokes the function, the retry behavior and the strategy for managing errors varies\.

Issues with the request, caller, or account can cause invocation errors\. Invocation errors include an error type and status code in the response that indicate the cause of the error\.

**Common invocation errors**
+ **Request** – The request event is too large or isn't valid JSON, the function doesn't exist, or a parameter value is the wrong type\.
+ **Caller** – The user or service doesn't have permission to invoke the function\.
+ **Account** – The maximum number of function instances are already running, or requests are being made too quickly\.

Clients such as the AWS CLI and the AWS SDK retry on client timeouts, throttling errors \(429\), and other errors that aren't caused by a bad request\. For a full list of invocation errors, see [Invoke](API_Invoke.md)\.

Function errors occur when your function code or the runtime that it uses return an error\.

**Common function errors**
+ **Function** – Your function's code throws an exception or returns an error object\.
+ **Runtime** – The runtime terminated your function because it ran out of time, detected a syntax error, or failed to marshal the response object into JSON\. The function exited with an error code\.

Unlike invocation errors, function errors don't cause Lambda to return a 400\-series or 500\-series status code\. If the function returns an error, Lambda indicates this by including a header named `X-Amz-Function-Error`, and a JSON\-formatted response with the error message and other details\. For examples of function errors in each language, see the following topics\.
+  [AWS Lambda function errors in Node\.js](nodejs-exceptions.md) 
+  [AWS Lambda function errors in Python](python-exceptions.md) 
+  [AWS Lambda function errors in Ruby](ruby-exceptions.md) 
+  [AWS Lambda function errors in Java](java-exceptions.md) 
+  [AWS Lambda function errors in Go](golang-exceptions.md) 
+  [AWS Lambda function errors in C\#](csharp-exceptions.md) 
+  [AWS Lambda function errors in PowerShell](powershell-exceptions.md) 

When you invoke a function directly, you determine the strategy for handling errors\. You can retry, send the event to a queue for debugging, or ignore the error\. Your function's code might have run completely, partially, or not at all\. If you retry, ensure that your function's code can handle the same event multiple times without causing duplicate transactions or other unwanted side effects\.

When you invoke a function indirectly, you need to be aware of the retry behavior of the invoker and any service that the request encounters along the way\. This includes the following scenarios\.
+ **Asynchronous invocation** – Lambda retries function errors twice\. If the function doesn't have enough capacity to handle all incoming requests, events might wait in the queue for hours or days to be sent to the function\. You can configure a dead\-letter queue on the function to capture events that weren't successfully processed\. For more information, see [Asynchronous invocation](invocation-async.md)\.
+ **Event source mappings** – Event source mappings that read from streams retry the entire batch of items\. Repeated errors block processing of the affected shard until the error is resolved or the items expire\. To detect stalled shards, you can monitor the [Iterator Age](monitoring-metrics.md) metric\.

  For event source mappings that read from a queue, you determine the length of time between retries and destination for failed events by configuring the visibility timeout and redrive policy on the source queue\. For more information, see [Lambda event source mappings](invocation-eventsourcemapping.md) and the service\-specific topics under [Using AWS Lambda with other services](lambda-services.md)\.
+ **AWS services** – AWS services can invoke your function [synchronously](invocation-sync.md) or asynchronously\. For synchronous invocation, the service decides whether to retry\. For example, Amazon S3 batch operations retries the operation if the Lambda function returns a `TemporaryFailure` response code\. Services that proxy requests from an upstream user or client may have a retry strategy or may relay the error response back to the requestor\. For example, API Gateway always relays the error response back to the requestor\. 

  For asynchronous invocation, the behavior is the same as when you invoke the function synchronously\. For more information, see the service\-specific topics under [Using AWS Lambda with other services](lambda-services.md) and the invoking service's documentation\.
+ **Other accounts and clients** – When you grant access to other accounts, you can use [resource\-based policies](access-control-resource-based.md) to restrict the services or resources they can configure to invoke your function\. To protect your function from being overloaded, consider putting an API layer in front of your function with [Amazon API Gateway](services-apigateway.md)\.

To help you deal with errors in Lambda applications, Lambda integrates with services like Amazon CloudWatch and AWS X\-Ray\. You can use a combination of logs, metrics, alarms, and tracing to quickly detect and identify issues in your function code, API, or other resources that support your application\. For more information, see [Monitoring and troubleshooting Lambda applications](lambda-monitoring.md)\.

For a sample application that uses a CloudWatch Logs subscription, X\-Ray tracing, and a Lambda function to detect and process errors, see [Error processor sample application for AWS Lambda](samples-errorprocessor.md)\.