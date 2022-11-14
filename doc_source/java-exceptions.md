# AWS Lambda function errors in Java<a name="java-exceptions"></a>

When your code raises an error, Lambda generates a JSON representation of the error\. This error document appears in the invocation log and, for synchronous invocations, in the output\.

This page describes how to view Lambda function invocation errors for the Java runtime using the Lambda console and the AWS CLI\.

**Topics**
+ [Syntax](#java-exceptions-syntax)
+ [How it works](#java-exceptions-how)
+ [Creating a function that returns exceptions](#java-exceptions-createfunction)
+ [Using the Lambda console](#java-exceptions-console)
+ [Using the AWS Command Line Interface \(AWS CLI\)](#java-exceptions-cli)
+ [Error handling in other AWS services](#java-exceptions-other-services)
+ [Sample applications](#java-exceptions-samples)
+ [What's next?](#java-exceptions-next-up)

## Syntax<a name="java-exceptions-syntax"></a>

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

## How it works<a name="java-exceptions-how"></a>

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

## Creating a function that returns exceptions<a name="java-exceptions-createfunction"></a>

You can create a Lambda function that displays human\-readable error messages to users\.

**Note**  
To test this code, you need to include [InputLengthException\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/InputLengthException.java) in your project src folder\.

**Example [src/main/java/example/HandlerDivide\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/HandlerDivide.java) – Runtime exception**  

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
        throw new InputLengthException("Input must be a list that contains 2 numbers.");
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
    "errorMessage":"Input must be a list that contains 2 numbers.",
    "errorType":"java.lang.InputLengthException",
    "stackTrace": [
      "example.HandlerDivide.handleRequest(HandlerDivide.java:23)",
      "example.HandlerDivide.handleRequest(HandlerDivide.java:14)"
    ]
  }
```

In this example, [InputLengthException](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/InputLengthException.java) is a `RuntimeException`\. The `RequestHandler` [interface](java-handler.md#java-handler-interfaces) does not allow checked exceptions\. The `RequestStreamHandler` interface supports throwing `IOException` errors\.

The return statement in the previous example can also throw a runtime exception\.

```
    return numerator/denominator;
```

This code can return an arithmetic error\.

```
{"errorMessage":"/ by zero","errorType":"java.lang.ArithmeticException","stackTrace":["example.HandlerDivide.handleRequest(HandlerDivide.java:28)","example.HandlerDivide.handleRequest(HandlerDivide.java:13)"]}
```

## Using the Lambda console<a name="java-exceptions-console"></a>

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

## Using the AWS Command Line Interface \(AWS CLI\)<a name="java-exceptions-cli"></a>

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

```
{"errorMessage":"Input must contain 2 numbers.","errorType":"java.lang.InputLengthException","stackTrace": ["example.HandlerDivide.handleRequest(HandlerDivide.java:23)","example.HandlerDivide.handleRequest(HandlerDivide.java:14)"]}
```

Lambda also records up to 256 KB of the error object in the function's logs\. For more information, see [AWS Lambda function logging in Java](java-logging.md)\.

## Error handling in other AWS services<a name="java-exceptions-other-services"></a>

When another AWS service invokes your function, the service chooses the invocation type and retry behavior\. AWS services can invoke your function on a schedule, in response to a lifecycle event on a resource, or to serve a request from a user\. Some services invoke functions asynchronously and let Lambda handle errors, while others retry or pass errors back to the user\.

For example, API Gateway treats all invocation and function errors as internal errors\. If the Lambda API rejects the invocation request, API Gateway returns a `500` error code\. If the function runs but returns an error, or returns a response in the wrong format, API Gateway returns a 502 error code\. To customize the error response, you must catch errors in your code and format a response in the required format\.

We recommend using AWS X\-Ray to determine the source of an error and its cause\. X\-Ray allows you to find out which component encountered an error, and see details about the errors\. The following example shows a function error that resulted in a `502` response from API Gateway\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/tracemap-apig-502.png)

For more information, see [Instrumenting Java code in AWS Lambda](java-tracing.md)\.

## Sample applications<a name="java-exceptions-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of the errors\. Each sample application includes scripts for easy deployment and cleanup, an AWS Serverless Application Model \(AWS SAM\) template, and supporting resources\.

**Sample Lambda applications in Java**
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A collection of minimal Java functions with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A collection of Java functions that contain skeleton code for how to handle events from various services such as Amazon API Gateway, Amazon SQS, and Amazon Kinesis\. These functions use the latest version of the [aws\-lambda\-java\-events](java-package.md) library \(3\.0\.0 and newer\)\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.
+ [Use API Gateway to invoke a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/example_cross_LambdaAPIGateway_section.html) – A Java function that scans a Amazon DynamoDB table that contains employee information\. It then uses Amazon Simple Notification Service to send a text message to employees celebrating their work anniversaries\. This example uses API Gateway to invoke the function\.

The `java-basic` function includes a handler \(`HandlerDivide`\) that returns a custom runtime exception\. The `HandlerStream` handler implements the `RequestStreamHandler` and can throw an `IOException` checked exception\.

## What's next?<a name="java-exceptions-next-up"></a>
+ Learn how to show logging events for your Lambda function on the [AWS Lambda function logging in Java](java-logging.md) page\.