# AWS Lambda context object in Java<a name="java-context"></a>

When Lambda runs your function, it passes a context object to the [handler](java-handler.md)\. This object provides methods and properties that provide information about the invocation, function, and execution environment\.

**Context methods**
+ `getRemainingTimeInMillis()` – Returns the number of milliseconds left before the execution times out\.
+ `getFunctionName()` – Returns the name of the Lambda function\.
+ `getFunctionVersion()` – Returns the [version](configuration-versions.md) of the function\.
+ `getInvokedFunctionArn()` – Returns the Amazon Resource Name \(ARN\) that's used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `getMemoryLimitInMB()` – Returns the amount of memory that's allocated for the function\.
+ `getAwsRequestId()` – Returns the identifier of the invocation request\.
+ `getLogGroupName()` – Returns the log group for the function\.
+ `getLogStreamName()` – Returns the log stream for the function instance\.
+ `getIdentity()` – \(mobile apps\) Returns information about the Amazon Cognito identity that authorized the request\.
+ `getClientContext()` – \(mobile apps\) Returns the client context that's provided to Lambda by the client application\.
+ `getLogger()` – Returns the [logger object](java-logging.md) for the function\.

The following example shows a function that uses the context object to access the Lambda logger\.

**Example [Handler\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/main/java/example/Handler.java)**  

```
package example;
import [com\.amazonaws\.services\.lambda\.runtime\.Context](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/Context.java)
import [com\.amazonaws\.services\.lambda\.runtime\.RequestHandler](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/RequestHandler.java)
import [com\.amazonaws\.services\.lambda\.runtime\.LambdaLogger](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/LambdaLogger.java)
...

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(Map<String,String> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = new String("200 OK");
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return response;
  }
}
```

The function serializes the context object into JSON and records it in its log stream\.

**Example log output**  

```
START RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0 Version: $LATEST
...
CONTEXT: 
{
    "memoryLimit": 512,
    "awsRequestId": "6bc28136-xmpl-4365-b021-0ce6b2e64ab0",
    "functionName": "java-console",
    ...
}
...
END RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0
REPORT RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0	Duration: 198.50 ms	Billed Duration: 200 ms	Memory Size: 512 MB	Max Memory Used: 90 MB	Init Duration: 524.75 ms
```

The interface for the context object is available in the [aws\-lambda\-java\-core](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-core) library\. You can implement this interface to create a context class for testing\. The following example shows a context class that returns dummy values for most properties and a working test logger\.

**Example [src/test/java/example/TestContext\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/test/java/example/TestContext.java)**  

```
package example;

import [com\.amazonaws\.services\.lambda\.runtime\.Context](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/Context.java);
import [com\.amazonaws\.services\.lambda\.runtime\.CognitoIdentity](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/CognitoIdentity.java);
import [com\.amazonaws\.services\.lambda\.runtime\.ClientContext](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/ClientContext.java);
import [com\.amazonaws\.services\.lambda\.runtime\.LambdaLogger](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-core/src/main/java/com/amazonaws/services/lambda/runtime/LambdaLogger.java)

public class TestContext implements Context{
  public TestContext() {}
  public String getAwsRequestId(){
    return new String("495b12a8-xmpl-4eca-8168-160484189f99");
  }
  public String getLogGroupName(){
    return new String("/aws/lambda/my-function");
  }
  ...
  public LambdaLogger getLogger(){
    return new TestLogger();
  }

}
```

For more information on logging, see [AWS Lambda function logging in Java](java-logging.md)\.

## Context in sample applications<a name="java-context-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of the context object\. Each sample application includes scripts for easy deployment and cleanup, an AWS Serverless Application Model \(AWS SAM\) template, and supporting resources\.

**Sample Lambda applications in Java**
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A collection of minimal Java functions with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A collection of Java functions that contain skeleton code for how to handle events from various services such as Amazon API Gateway, Amazon SQS, and Amazon Kinesis\. These functions use the latest version of the [aws\-lambda\-java\-events](java-package.md) library \(3\.0\.0 and newer\)\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.
+ [Use API Gateway to invoke a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/example_cross_LambdaAPIGateway_section.html) – A Java function that scans a Amazon DynamoDB table that contains employee information\. It then uses Amazon Simple Notification Service to send a text message to employees celebrating their work anniversaries\. This example uses API Gateway to invoke the function\.

All of the sample applications have a test context class for unit tests\. The `java-basic` application shows you how to use the context object to get a logger\. It uses SLF4J and Log4J 2 to provide a logger that works for local unit tests\.