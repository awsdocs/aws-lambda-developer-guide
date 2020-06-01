# AWS Lambda function errors in Java<a name="java-exceptions"></a>

When your function raises an error, Lambda returns details about the error to the invoker\. The body of the response that Lambda returns contains a JSON document with the error name, error type, and an array of stack frames\. The client or service that invoked the function can handle the error or pass it along to an end user\. You can use custom exceptions to return helpful information to users for client errors\.

**Example [src/main/java/example/HandlerDivide\.java](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/java-basic/src/main/java/example/HandlerDivide.java) – Runtime exception**  

```
import java.util.List;

// Handler value: example.HandlerDivide
public class HandlerDivide implements RequestHandler<List<Integer>, Integer>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public Integer handleRequest(List<Integer> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    // process event
    if ( event.size() != 2 )
    {
      throw new InputLengthException("Input must be an array that contains 2 numbers.");
    }
    int numerator = event.get(0);
    int denominator = event.get(1);
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return numerator/denominator;
  }
}
```

When the function throws `InputLengthException`, the Java runtime serializes it into the following document\.

**Example error document \(whitespace added\)**  

```
{
  "errorMessage":"Input must contain 2 numbers.",
  "errorType":"java.lang.InputLengthException",
  "stackTrace": [
    "example.HandlerDivide.handleRequest(HandlerDivide.java:23)",
    "example.HandlerDivide.handleRequest(HandlerDivide.java:14)"
  ]
}
```

In this example, [InputLengthException](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/java-basic/src/main/java/example/InputLengthException.java) is a `RuntimeException`\. The `RequestHandler` [interface](java-handler.md#java-handler-interfaces) does not allow checked exceptions\. The `RequestStreamHandler` interface supports throwing `IOException` errors\.

The return statement in the previous example can also throw a runtime exception\.

```
    return numerator/denominator;
```

This code can return an arithmetic error\.

```
{"errorMessage":"/ by zero","errorType":"java.lang.ArithmeticException","stackTrace":["example.HandlerDivide.handleRequest(HandlerDivide.java:28)","example.HandlerDivide.handleRequest(HandlerDivide.java:13)"]}
```

**Topics**
+ [Viewing error output](#java-exceptions-view)
+ [Understanding error types and sources](#java-exceptions-types)
+ [Error handling in clients](#java-exceptions-clients)
+ [Error handling in other AWS services](#java-exceptions-services)
+ [Error handling in sample applications](#java-exceptions-samples)

## Viewing error output<a name="java-exceptions-view"></a>

You can invoke your function with a test payload and view error output in the Lambda console, from the command line, or with the AWS SDK\. Error output is also captured in the function's execution logs and, when [tracing](java-tracing.md) is enabled, in AWS X\-Ray\.

To view error output in the Lambda console, invoke it with a test event\.

**To invoke a function in the Lambda console**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Choose **Configure test events** from the drop\-down menu next to the **Test** button\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-test-config.png)

1. Choose an **Event template** that matches the events that your function processes\.

1. Enter a name for the test event and modify the event structure as needed\.

1. Choose **Create**\.

1. Choose **Test**\.

The Lambda console invokes your function [synchronously](invocation-sync.md) and displays the result\. To see the response, logs, and other information, expand the **Details** section\.

When you invoke the function from the command line, the AWS CLI splits the response into two documents\. To indicate that a function error occurred, the response displayed in the terminal includes a `FunctionError` field\. The response or error returned by the function is written to the output file\.

```
$ aws lambda invoke --function-name my-function out.json
{
    "StatusCode": 200,
    "FunctionError": "Unhandled",
    "ExecutedVersion": "$LATEST"
}
```

View the output file to see the error document\.

```
$ cat out.json
{"errorMessage":"Input must contain 2 numbers.","errorType":"java.lang.InputLengthException","stackTrace": ["example.HandlerDivide.handleRequest(HandlerDivide.java:23)","example.HandlerDivide.handleRequest(HandlerDivide.java:14)"]}
```

Lambda also records up to 256 KB of the error object in the function's logs\. To view logs when you invoke the function from the command line, use the `--log-type` option and decode the base64 string in the response\.

```
$ aws lambda invoke --function-name my-function --payload "[100,0]" out.json --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: 081f7522-xmpl-48e2-8f67-96686904bb4f Version: $LATEST
EVENT: [
  100,
  0
]EVENT TYPE: class java.util.ArrayList/ by zero: java.lang.ArithmeticException
java.lang.ArithmeticException: / by zero
        at example.HandlerDivide.handleRequest(HandlerDivide.java:28)
        at example.HandlerDivide.handleRequest(HandlerDivide.java:13)

END RequestId: 081f7522-xmpl-48e2-8f67-96686904bb4f
REPORT RequestId: 081f7522-xmpl-48e2-8f67-96686904bb4f  Duration: 4.20 ms       Billed Duration: 100 ms Memory Size: 512 MB     Max Memory Used: 95 MB
XRAY TraceId: 1-5e73162b-1919xmpl2592f4549e1c39be       SegmentId: 3dadxmpl48126cb8     Sampled: true
```

For more information about logs, see [AWS Lambda function logging in Java](java-logging.md)\.

## Understanding error types and sources<a name="java-exceptions-types"></a>

When you invoke a function, multiple subsystems handle the request, event, output, and response\. Errors can come from the Lambda service \(invocation errors\), or from an instance of your function\. Function errors include exceptions returned by your handler code and exceptions returned by the Lambda runtime\.

The Lambda service receives the invocation request and validates it\. It checks permissions, verifies that the event document is a valid JSON document, and checks parameter values\. If the Lambda service encounters an error, it returns an exception type, message, and HTTP status code that indicate the cause of the error\.

**Note**  
For a full list of errors that the `Invoke` API operation can return, see [Invoke Errors](API_Invoke.md#API_Invoke_Errors) in the Lambda API reference\.

A `4xx` series error from the Lambda service indicates an error that the invoker can fix by modifying the request, requesting permission, or trying again\. A `5xx` series error indicates an issue with the Lambda service, or an issue with the function's configuration or resources\. These issues can't be addressed by the invoker, but the function's owner might be able to fix them\.

If a request passes validation, Lambda sends it to an instance of the function\. The runtime converts the event document into an object and passes it to your handler code\. Errors can occur during this process if, for example, the name of your handler method doesn't match the function's configuration, or if the invocation times out before your handler code returns a response\. Lambda runtime errors are formatted like errors that your code returns, but they are returned by the runtime\.

In the following example, the runtime fails to deserialize the event into an object\. The input is a valid JSON type, but it doesn't match the type expected by the handler method\.

**Example Lambda runtime error**  

```
{
  "errorMessage": "An error occurred during JSON parsing",
  "errorType": "java.lang.RuntimeException",
  "stackTrace": [],
  "cause": {
    "errorMessage": "com.fasterxml.jackson.databind.exc.InvalidFormatException: Can not construct instance of java.lang.Integer from String value '1000,10': not a valid Integer value\n at [Source: lambdainternal.util.NativeMemoryAsInputStream@35fc6dc4; line: 1, column: 1] (through reference chain: java.lang.Object[0])",
    "errorType": "java.io.UncheckedIOException",
    "stackTrace": [],
    "cause": {
      "errorMessage": "Can not construct instance of java.lang.Integer from String value '1000,10': not a valid Integer value\n at [Source: lambdainternal.util.NativeMemoryAsInputStream@35fc6dc4; line: 1, column: 1] (through reference chain: java.lang.Object[0])",
      "errorType": "com.fasterxml.jackson.databind.exc.InvalidFormatException",
      "stackTrace": [
        "com.fasterxml.jackson.databind.exc.InvalidFormatException.from(InvalidFormatException.java:55)",
        "com.fasterxml.jackson.databind.DeserializationContext.weirdStringException(DeserializationContext.java:907)",
        ...
      ]
    }
  }
}
```

For Lambda runtime errors and other function errors, the Lambda service does not return an error code\. A `2xx` series status code indicates that the Lambda service accepted the request\. Instead of an error code, Lambda indicates the error by including the `X-Amz-Function-Error` header in the response\.

For asynchronous invocation, events are queued before Lambda sends them to your function\. For valid requests, Lambda returns a success response immediately and adds the event to the queue\. Lambda then reads events from the queue and invokes the function\. If an error occurs, Lambda retries with behavior that varies depending on the type of error\. For more information, see [Asynchronous invocation](invocation-async.md)\.

## Error handling in clients<a name="java-exceptions-clients"></a>

Clients that invoke Lambda functions can choose to handle errors or pass them on to the end user\. The correct error handling behavior depends on the type of application, the audience, and the source of the error\. For example, if an invocation fails with an error code `429` \(too many requests\), the AWS CLI retries up to 4 times before showing an error to the user\.

```
$ aws lambda invoke --function-name my-function out.json
An error occurred (TooManyRequestsException) when calling the Invoke operation (reached max retries: 4): Rate Exceeded.
```

For other invocation errors, the correct behavior depends on the response code\. `5xx` series errors can indicate a temporary condition that can be resolved without any action by the user\. A retry might or might not succeed\. `4xx` series errors other than `429` generally indicate an error with the request\. A retry is not likely to succeed\.

For function errors, the client can process the error document and show the error message in a user\-friendly format\. A browser\-based application might show the error message and type, but omit the stack trace\. The AWS CLI saves the error object to the output file and displays a document generated from the response headers instead\.

```
$ aws lambda invoke --function-name my-function --payload '[1000]' out.json
{
    "StatusCode": 200,
    "FunctionError": "Unhandled",
    "ExecutedVersion": "$LATEST"
}
```

**Example out\.json**  

```
{"errorMessage":"Input must be an array that contains 2 numbers.","errorType":"example.InputLengthException","stackTrace":["example.HandlerDivide.handleRequest(HandlerDivide.java:22)","example.HandlerDivide.handleRequest(HandlerDivide.java:13)"]}
```

## Error handling in other AWS services<a name="java-exceptions-services"></a>

When an AWS service invokes your function, the service chooses the invocation type and retry behavior\. AWS services can invoke your function on a schedule, in response to a lifecycle event on a resource, or to serve a request from a user\. Some services invoke functions asynchronously and let Lambda handle errors, while others retry or pass errors back to the user\.

For example, API Gateway treats all invocation and function errors as internal errors\. If the Lambda API rejects the invocation request, API Gateway returns a 500 error code\. If the function runs but returns an error, or returns a response in the wrong format, API Gateway returns a 502 error code\. To customize the error response, you must catch errors in your code and format a response in the required format\.

To determine the source of an error and its cause, use AWS X\-Ray\. With X\-Ray, you can find out which component encountered an error and see details about exceptions\. The following example shows a function error that resulted in a 502 response from API Gateway\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/tracemap-apig-502.png)

Get started with X\-Ray by [enabling active tracing](java-tracing.md) on your functions\.

For details on how other services handler errors, see the topics in the [Using AWS Lambda with other services](lambda-services.md) chapter\.

## Error handling in sample applications<a name="java-exceptions-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of the errors\. Each sample application includes scripts for easy deployment and cleanup, an AWS Serverless Application Model \(AWS SAM\) template, and supporting resources\.

**Sample Lambda applications in Java**
+ [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java) – A Java function that shows the use of Lambda's Java libraries, logging, environment variables, layers, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-basic) – A minimal Java function with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events) – A minimal Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that don't require the AWS SDK as a dependency, such as Amazon API Gateway\.
+ [java\-events\-v1sdk](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events-v1sdk) – A Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that require the AWS SDK as a dependency \(Amazon Simple Storage Service, Amazon DynamoDB, and Amazon Kinesis\)\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.

The `java-basic` function includes a handler \(`HandlerDivide`\) that returns a custom runtime exception\. The `HandlerStream` handler implements the `RequestStreamHandler` and can throw an `IOException` checked exception\.