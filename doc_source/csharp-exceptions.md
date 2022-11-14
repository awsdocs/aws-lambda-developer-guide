# AWS Lambda function errors in C\#<a name="csharp-exceptions"></a>

When your code raises an error, Lambda generates a JSON representation of the error\. This error document appears in the invocation log and, for synchronous invocations, in the output\.

This page describes how to view Lambda function invocation errors for the C\# runtime using the Lambda console and the AWS CLI\.

**Topics**
+ [Syntax](#csharp-exceptions-syntax)
+ [How it works](#csharp-exceptions-how)
+ [Using the Lambda console](#csharp-exceptions-console)
+ [Using the AWS Command Line Interface \(AWS CLI\)](#csharp-exceptions-cli)
+ [Error handling in other AWS services](#csharp-exceptions-other-services)
+ [What's next?](#csharp-exceptions-next-up)

## Syntax<a name="csharp-exceptions-syntax"></a>

In the initialization phase, exceptions can be thrown for invalid handler strings, a rule\-breaking type or method \(see [Lambda function handler restrictions](csharp-handler.md#csharp-handler-restrictions)\), or any other validation method \(such as forgetting the serializer attribute and having a POCO as your input or output type\)\. These exceptions are of type `LambdaException`\. For example: 

```
{
  "errorType": "LambdaException",
  "errorMessage": "Invalid lambda function handler: 'http://this.is.not.a.valid.handler/'. 
  The valid format is 'ASSEMBLY::TYPE::METHOD'."
}
```

If your constructor throws an exception, the error type is also of type `LambdaException`, but the exception thrown during construction is provided in the `cause` property, which is itself a modeled exception object:

```
{
  "errorType": "LambdaException",
  "errorMessage": "An exception was thrown when the constructor for type 'LambdaExceptionTestFunction.ThrowExceptionInConstructor'
   was invoked. Check inner exception for more details.",
  "cause":   {
    "errorType": "TargetInvocationException",
    "errorMessage": "Exception has been thrown by the target of an invocation.",
    "stackTrace": [
      "at System.RuntimeTypeHandle.CreateInstance(RuntimeType type, Boolean publicOnly, Boolean noCheck, Boolean&canBeCached, 
      RuntimeMethodHandleInternal&ctor, Boolean& bNeedSecurityCheck)",
      "at System.RuntimeType.CreateInstanceSlow(Boolean publicOnly, Boolean skipCheckThis, Boolean fillCache, StackCrawlMark& stackMark)",
      "at System.Activator.CreateInstance(Type type, Boolean nonPublic)",
      "at System.Activator.CreateInstance(Type type)"
    ],
    "cause":     {
      "errorType": "ArithmeticException",
      "errorMessage": "Sorry, 2 + 2 = 5",
      "stackTrace": [
        "at LambdaExceptionTestFunction.ThrowExceptionInConstructor..ctor()"
      ]
    }
  }
}
```

As the example shows, the inner exceptions are always preserved \(as the `cause` property\), and can be deeply nested\. 

Exceptions can also occur during invocation\. In this case, the exception type is preserved and the exception is returned directly as the payload and in the CloudWatch logs\. For example: 

```
{
  "errorType": "AggregateException",
  "errorMessage": "One or more errors occurred. (An unknown web exception occurred!)",
  "stackTrace": [
    "at System.Threading.Tasks.Task.ThrowIfExceptional(Boolean includeTaskCanceledExceptions)",
    "at System.Threading.Tasks.Task`1.GetResultCore(Boolean waitCompletionNotification)",
    "at lambda_method(Closure , Stream , Stream , ContextInfo )"
  ],
  "cause":   {
    "errorType": "UnknownWebException",
    "errorMessage": "An unknown web exception occurred!",
    "stackTrace": [
      "at LambdaDemo107.LambdaEntryPoint.<GetUriResponse>d__1.MoveNext()",
      "--- End of stack trace from previous location where exception was thrown ---",
      "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
      "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
      "at System.Runtime.CompilerServices.TaskAwaiter`1.GetResult()",
      "at LambdaDemo107.LambdaEntryPoint.<CheckWebsiteStatus>d__0.MoveNext()"
    ],
    "cause":     {
      "errorType": "WebException",
      "errorMessage": "An error occurred while sending the request. SSL peer certificate or SSH remote key was not OK",
      "stackTrace": [
        "at System.Net.HttpWebRequest.EndGetResponse(IAsyncResult asyncResult)",
        "at System.Threading.Tasks.TaskFactory`1.FromAsyncCoreLogic(IAsyncResult iar, Func`2 endFunction, Action`1 endAction, Task`1 promise, Boolean requiresSynchronization)",
        "--- End of stack trace from previous location where exception was thrown ---",
        "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
        "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
        "at System.Runtime.CompilerServices.TaskAwaiter`1.GetResult()",
        "at LambdaDemo107.LambdaEntryPoint.<GetUriResponse>d__1.MoveNext()"
      ],
      "cause":       {
        "errorType": "HttpRequestException",
        "errorMessage": "An error occurred while sending the request.",
        "stackTrace": [
          "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
          "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
          "at System.Net.Http.HttpClient.<FinishSendAsync>d__58.MoveNext()",
          "--- End of stack trace from previous location where exception was thrown ---",
          "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
          "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
          "at System.Net.HttpWebRequest.<SendRequest>d__63.MoveNext()",
          "--- End of stack trace from previous location where exception was thrown ---",
          "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
          "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
          "at System.Net.HttpWebRequest.EndGetResponse(IAsyncResult asyncResult)"
        ],
        "cause":         {
          "errorType": "CurlException",
          "errorMessage": "SSL peer certificate or SSH remote key was not OK",
          "stackTrace": [
            "at System.Net.Http.CurlHandler.ThrowIfCURLEError(CURLcode error)",
            "at System.Net.Http.CurlHandler.MultiAgent.FinishRequest(StrongToWeakReference`1 easyWrapper, CURLcode messageResult)"
          ]
        }
      }
    }
  }
}
```

The method in which error information is conveyed depends on the invocation type: 
+ `RequestResponse` invocation type \(that is, synchronous execution\): In this case, you get the error message back\. 

  For example, if you invoke a Lambda function using the Lambda console, the `RequestResponse` is always the invocation type and the console displays the error information returned by AWS Lambda in the **Execution result** section of the console\.
+ `Event` invocation type \(that is, asynchronous execution\): In this case AWS Lambda does not return anything\. Instead, it logs the error information in CloudWatch Logs and CloudWatch metrics\.

## How it works<a name="csharp-exceptions-how"></a>

When you invoke a Lambda function, Lambda receives the invocation request and validates the permissions in your execution role, verifies that the event document is a valid JSON document, and checks parameter values\.

If the request passes validation, Lambda sends the request to a function instance\. The [Lambda runtime](lambda-runtimes.md) environment converts the event document into an object, and passes it to your function handler\. 

If Lambda encounters an error, it returns an exception type, message, and HTTP status code that indicates the cause of the error\. The client or service that invoked the Lambda function can handle the error programmatically, or pass it along to an end user\. The correct error handling behavior depends on the type of application, the audience, and the source of the error\.

The following list describes the range of status codes you can receive from Lambda\.

**`2xx`**  
A `2xx` series error with a `X-Amz-Function-Error` header in the response indicates a Lambda runtime or function error\. A `2xx` series status code indicates that Lambda accepted the request, but instead of an error code, Lambda indicates the error by including the `X-Amz-Function-Error` header in the response\.

**`4xx`**  
A `4xx` series error indicates an error that the invoking client or service can fix by modifying the request, requesting permission, or by retrying the request\. `4xx` series errors other than `429` generally indicate an error with the request\. 

**`5xx`**  
A `5xx` series error indicates an issue with Lambda, or an issue with the function's configuration or resources\. `5xx` series errors can indicate a temporary condition that can be resolved without any action by the user\. These issues can't be addressed by the invoking client or service, but a Lambda function's owner may be able to fix the issue\.

For a complete list of invocation errors, see [InvokeFunction errors](API_Invoke.md#API_Invoke_Errors)\.

## Using the Lambda console<a name="csharp-exceptions-console"></a>

You can invoke your function on the Lambda console by configuring a test event and viewing the output\. The output is captured in the function's execution logs and, when [active tracing](services-xray.md) is enabled, in AWS X\-Ray\.

**To invoke a function on the Lambda console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function to test, and choose **Test**\.

1. Under **Test event**, select **New event**\.

1. Select a **Template**\.

1. For **Name**, enter a name for the test\. In the text entry box, enter the JSON test event\.

1. Choose **Save changes**\.

1. Choose **Test**\.

The Lambda console invokes your function [synchronously](invocation-sync.md) and displays the result\. To see the response, logs, and other information, expand the **Details** section\.

## Using the AWS Command Line Interface \(AWS CLI\)<a name="csharp-exceptions-cli"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

When you invoke a Lambda function in the AWS CLI, the AWS CLI splits the response into two documents\. The AWS CLI response is displayed in your command prompt\. If an error has occurred, the response contains a `FunctionError` field\. The invocation response or error returned by the function is written to an output file\. For example, `output.json` or `output.txt`\.

The following [invoke](https://docs.aws.amazon.com/cli/latest/reference/lambda/invoke.html) command example demonstrates how to invoke a function and write the invocation response to an `output.txt` file\.

```
aws lambda invoke   \
  --function-name my-function   \
      --cli-binary-format raw-in-base64-out  \
          --payload '{"key1": "value1", "key2": "value2", "key3": "value3"}' output.txt
```

The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.

You should see the AWS CLI response in your command prompt:

```
{
    "StatusCode": 200,
    "FunctionError": "Unhandled",
    "ExecutedVersion": "$LATEST"
}
```

You should see the function invocation response in the `output.txt` file\. In the same command prompt, you can also view the output in your command prompt using:

```
cat output.txt
```

You should see the invocation response in your command prompt\.

Lambda also records up to 256 KB of the error object in the function's logs\. For more information, see [Lambda function logging in C\#](csharp-logging.md)\.

## Error handling in other AWS services<a name="csharp-exceptions-other-services"></a>

When another AWS service invokes your function, the service chooses the invocation type and retry behavior\. AWS services can invoke your function on a schedule, in response to a lifecycle event on a resource, or to serve a request from a user\. Some services invoke functions asynchronously and let Lambda handle errors, while others retry or pass errors back to the user\.

For example, API Gateway treats all invocation and function errors as internal errors\. If the Lambda API rejects the invocation request, API Gateway returns a `500` error code\. If the function runs but returns an error, or returns a response in the wrong format, API Gateway returns a 502 error code\. To customize the error response, you must catch errors in your code and format a response in the required format\.

We recommend using AWS X\-Ray to determine the source of an error and its cause\. X\-Ray allows you to find out which component encountered an error, and see details about the errors\. The following example shows a function error that resulted in a `502` response from API Gateway\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/tracemap-apig-502.png)

For more information, see [Instrumenting C\# code in AWS Lambda](csharp-tracing.md)\.

## What's next?<a name="csharp-exceptions-next-up"></a>
+ Learn how to show logging events for your Lambda function on the [Lambda function logging in C\#](csharp-logging.md) page\.