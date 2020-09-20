# AWS Lambda function logging in Java<a name="java-logging"></a>

Your Lambda function comes with a CloudWatch Logs log group, with a log stream for each instance of your function\. The runtime sends details about each invocation to the log stream, and relays logs and other output from your function's code\.

To output logs from your function code, you can use methods on [java\.lang\.System](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html), or any logging module that writes to `stdout` or `stderr`\. The [aws\-lambda\-java\-core](java-package.md) library provides a logger class named `LambdaLogger` that you can access from the context object\. The logger class supports multiline logs\.

The following example uses the `LambdaLogger` logger provided by the context object\.

**Example Handler\.java**  

```
// Handler value: example.Handler
public class Handler implements RequestHandler<Object, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(Object event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = new String("SUCCESS");
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    return response;
  }
}
```

**Example log format**  

```
START RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0 Version: $LATEST
ENVIRONMENT VARIABLES: 
{
    "_HANDLER": "example.Handler",
    "AWS_EXECUTION_ENV": "AWS_Lambda_java8",
    "AWS_LAMBDA_FUNCTION_MEMORY_SIZE": "512",
    ...
}
CONTEXT: 
{
    "memoryLimit": 512,
    "awsRequestId": "6bc28136-xmpl-4365-b021-0ce6b2e64ab0",
    "functionName": "java-console",
    ...
}
EVENT:
{
  "records": [
    {
      "messageId": "19dd0b57-xmpl-4ac1-bd88-01bbb068cb78",
      "receiptHandle": "MessageReceiptHandle",
      "body": "Hello from SQS!",
       ...
    }
  ]
}
END RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0
REPORT RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0	Duration: 198.50 ms	Billed Duration: 200 ms	Memory Size: 512 MB	Max Memory Used: 90 MB	Init Duration: 524.75 ms
```

The Java runtime logs the `START`, `END`, and `REPORT` lines for each invocation\. The report line provides the following details:

**Report Log**
+ **RequestId** – The unique request ID for the invocation\.
+ **Duration** – The amount of time that your function's handler method spent processing the event\.
+ **Billed Duration** – The amount of time billed for the invocation\.
+ **Memory Size** – The amount of memory allocated to the function\.
+ **Max Memory Used** – The amount of memory used by the function\.
+ **Init Duration** – For the first request served, the amount of time it took the runtime to load the function and run code outside of the handler method\.
+ **XRAY TraceId** – For traced requests, the [AWS X\-Ray trace ID](services-xray.md)\.
+ **SegmentId** – For traced requests, the X\-Ray segment ID\.
+ **Sampled** – For traced requests, the sampling result\.

You can view logs in the Lambda console, in the CloudWatch Logs console, or from the command line\.

**Topics**
+ [Viewing logs in the AWS Management Console](#java-logging-console)
+ [Using the AWS CLI](#java-logging-cli)
+ [Deleting logs](#java-logging-delete)
+ [Advanced logging with Log4j 2 and SLF4J](#java-logging-log4j2)
+ [Sample logging code](#java-logging-samples)

## Viewing logs in the AWS Management Console<a name="java-logging-console"></a>

The Lambda console shows log output when you test a function on the function configuration page\. To view logs for all invocations, use the CloudWatch Logs console\.

**To view your Lambda function's logs**

1. Open the [Logs page of the CloudWatch console](https://console.aws.amazon.com/cloudwatch/home?#logs:)\.

1. Choose the log group for your function \(**/aws/lambda/*function\-name***\)\.

1. Choose the first stream in the list\.

Each log stream corresponds to an [instance of your function](runtimes-context.md)\. New streams appear when you update your function and when additional instances are created to handle multiple concurrent invocations\. To find logs for specific invocations, you can instrument your function with X\-Ray, and record details about the request and log stream in the trace\. For a sample application that correlates logs and traces with X\-Ray, see [Error processor sample application for AWS Lambda](samples-errorprocessor.md)\.

## Using the AWS CLI<a name="java-logging-cli"></a>

To get logs for an invocation from the command line, use the `--log-type` option\. The response includes a `LogResult` field that contains up to 4 KB of base64\-encoded logs from the invocation\.

```
$ aws lambda invoke --function-name my-function out --log-type Tail
{
    "StatusCode": 200,
    "LogResult": "U1RBUlQgUmVxdWVzdElkOiA4N2QwNDRiOC1mMTU0LTExZTgtOGNkYS0yOTc0YzVlNGZiMjEgVmVyc2lvb...",
    "ExecutedVersion": "$LATEST"
}
```

You can use the `base64` utility to decode the logs\.

```
$ aws lambda invoke --function-name my-function out --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8 Version: $LATEST
  "AWS_SESSION_TOKEN": "AgoJb3JpZ2luX2VjELj...", "_X_AMZN_TRACE_ID": "Root=1-5d02e5ca-f5792818b6fe8368e5b51d50;Parent=191db58857df8395;Sampled=0"",ask/lib:/opt/lib",
END RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8
REPORT RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8  Duration: 79.67 ms      Billed Duration: 100 ms         Memory Size: 128 MB     Max Memory Used: 73 MB
```

The `base64` utility is available on Linux, macOS, and [Ubuntu on Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. For macOS, the command is `base64 -D`\.

To get full log events from the command line, you can include the log stream name in the output of your function, as shown in the preceding example\. The following example script invokes a function named `my-function` and downloads the last five log events\.

**Example get\-logs\.sh Script**  
This example requires that `my-function` returns a log stream ID\.  

```
#!/bin/bash
aws lambda invoke --function-name my-function --payload '{"key": "value"}' out
sed -i'' -e 's/"//g' out
sleep 15
aws logs get-log-events --log-group-name /aws/lambda/my-function --log-stream-name $(cat out) --limit 5
```

The script uses `sed` to remove quotes from the output file, and sleeps for 15 seconds to allow time for the logs to be available\. The output includes the response from Lambda and the output from the `get-log-events` command\.

```
$ ./get-logs.sh
{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
{
    "events": [
        {
            "timestamp": 1559763003171,
            "message": "START RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf Version: $LATEST\n",
            "ingestionTime": 1559763003309
        },
        {
            "timestamp": 1559763003173,
            "message": "2019-06-05T19:30:03.173Z\t4ce9340a-b765-490f-ad8a-02ab3415e2bf\tINFO\tENVIRONMENT VARIABLES\r{\r  \"AWS_LAMBDA_FUNCTION_VERSION\": \"$LATEST\",\r ...",
            "ingestionTime": 1559763018353
        },
        {
            "timestamp": 1559763003173,
            "message": "2019-06-05T19:30:03.173Z\t4ce9340a-b765-490f-ad8a-02ab3415e2bf\tINFO\tEVENT\r{\r  \"key\": \"value\"\r}\n",
            "ingestionTime": 1559763018353
        },
        {
            "timestamp": 1559763003218,
            "message": "END RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf\n",
            "ingestionTime": 1559763018353
        },
        {
            "timestamp": 1559763003218,
            "message": "REPORT RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf\tDuration: 26.73 ms\tBilled Duration: 100 ms \tMemory Size: 128 MB\tMax Memory Used: 75 MB\t\n",
            "ingestionTime": 1559763018353
        }
    ],
    "nextForwardToken": "f/34783877304859518393868359594929986069206639495374241795",
    "nextBackwardToken": "b/34783877303811383369537420289090800615709599058929582080"
}
```

## Deleting logs<a name="java-logging-delete"></a>

Log groups aren't deleted automatically when you delete a function\. To avoid storing logs indefinitely, delete the log group, or [configure a retention period](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html#SettingLogRetention) after which logs are deleted automatically\.

## Advanced logging with Log4j 2 and SLF4J<a name="java-logging-log4j2"></a>

To customize log output, support logging during unit tests, and log AWS SDK calls, use Apache Log4j 2 with SLF4J\. Log4j is a logging library for Java programs that enables you to configure log levels and use appender libraries\. SLF4J is a facade library that lets you change which library you use without changing your function code\.

To add the request ID to your function's logs, use the appender in the [aws\-lambda\-java\-log4j2](java-package.md) library\. The following example shows a Log4j 2 configuration file that adds a timestamp and request ID to all logs\.

**Example [src/main/resources/log4j2\.xml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-java/src/main/resources/log4j2.xml) – Appender configuration**  

```
<Configuration status="WARN">
  <Appenders>
    <Lambda name="Lambda">
      <PatternLayout>
          <pattern>%d{yyyy-MM-dd HH:mm:ss} %X{AWSRequestId} %-5p %c{1} - %m%n</pattern>
      </PatternLayout>
    </Lambda>
  </Appenders>
  <Loggers>
    <Root level="INFO">
      <AppenderRef ref="Lambda"/>
    </Root>
    <Logger name="software.amazon.awssdk" level="WARN" />
    <Logger name="software.amazon.awssdk.request" level="DEBUG" />
  </Loggers>
</Configuration>
```

With this configuration, each line is prepended with the date, time, request ID, log level, and class name\.

**Example log format with appender**  

```
START RequestId: 6bc28136-xmpl-4365-b021-0ce6b2e64ab0 Version: $LATEST
2020-03-18 08:52:43 6bc28136-xmpl-4365-b021-0ce6b2e64ab0 INFO  Handler - ENVIRONMENT VARIABLES:
{
    "_HANDLER": "example.Handler",
    "AWS_EXECUTION_ENV": "AWS_Lambda_java8",
    "AWS_LAMBDA_FUNCTION_MEMORY_SIZE": "512",
    ...
}
2020-03-18 08:52:43 6bc28136-xmpl-4365-b021-0ce6b2e64ab0 INFO  Handler - CONTEXT:
{
    "memoryLimit": 512,
    "awsRequestId": "6bc28136-xmpl-4365-b021-0ce6b2e64ab0",
    "functionName": "java-console",
    ...
}
```

SLF4J is a facade library for logging in Java code\. In your function code, you use the SLF4J logger factory to retrieve a logger with methods for log levels like `info()` and `warn()`\. In your build configuration, you include the logging library and SLF4J adapter in the classpath\. By changing the libraries in the build configuration, you can change the logger type without changing your function code\. SLF4J is required to capture logs from the SDK for Java\.

In the following example, the handler class uses SLF4J to retrieve a logger\.

**Example [src/main/java/example/Handler\.java](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-java/src/main/java/example/Handler.java) – Logging with SLF4J**  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.Handler
public class Handler implements RequestHandler<SQSEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(Handler.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  LambdaAsyncClient lambdaClient = LambdaAsyncClient.create();
  @Override
  public String handleRequest(SQSEvent event, Context context)
  {
    String response = new String();
    // call Lambda API
    logger.info("Getting account settings");
    CompletableFuture<GetAccountSettingsResponse> accountSettings = 
        lambdaClient.getAccountSettings(GetAccountSettingsRequest.builder().build());
    // log execution details
    logger.info("ENVIRONMENT VARIABLES: {}", gson.toJson(System.getenv()));
...
```

The build configuration takes runtime dependencies on the Lambda appender and SLF4J adapter, and implementation dependencies on Log4J 2\.

**Example [build\.gradle](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-java/build.gradle) – Logging dependencies**  

```
dependencies {
    implementation platform('software.amazon.awssdk:bom:2.10.73')
    implementation platform('com.amazonaws:aws-xray-recorder-sdk-bom:2.4.0')
    implementation 'software.amazon.awssdk:lambda'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-core'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-core'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-v2'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-v2-instrumentor'
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.1'
    implementation 'com.amazonaws:aws-lambda-java-events:3.1.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'org.apache.logging.log4j:log4j-api:2.13.0'
    implementation 'org.apache.logging.log4j:log4j-core:2.13.0'
    runtimeOnly 'org.apache.logging.log4j:log4j-slf4j18-impl:2.13.0'
    runtimeOnly 'com.amazonaws:aws-lambda-java-log4j2:1.2.0'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.6.0'
}
```

When you run your code locally for tests, the context object with the Lambda logger is not available, and there's no request ID for the Lambda appender to use\. For example test configurations, see the sample applications in the next section\. 

## Sample logging code<a name="java-logging-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of various logging configurations\. Each sample application includes scripts for easy deployment and cleanup, an AWS SAM template, and supporting resources\.

**Sample Lambda applications in Java**
+ [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java) – A Java function that shows the use of Lambda's Java libraries, logging, environment variables, layers, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-basic) – A minimal Java function with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events) – A minimal Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that don't require the AWS SDK as a dependency, such as Amazon API Gateway\.
+ [java\-events\-v1sdk](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events-v1sdk) – A Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that require the AWS SDK as a dependency \(Amazon Simple Storage Service, Amazon DynamoDB, and Amazon Kinesis\)\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.

The `java-basic` sample application shows a minimal logging configuration that supports logging tests\. The handler code uses the `LambdaLogger` logger provided by the context object\. For tests, the application uses a custom `TestLogger` class that implements the `LambdaLogger` interface with a Log4j 2 logger\. It uses SLF4J as a facade for compatibility with the AWS SDK\. Logging libraries are excluded from build output to keep the deployment package small\.

The `blank-java` sample application builds on the basic configuration with AWS SDK logging and the Lambda Log4j 2 appender\. It uses Log4j 2 in Lambda with custom appender that adds the invocation request ID to each line\.