# AWS Lambda function logging in Java<a name="java-logging"></a>

AWS Lambda automatically monitors Lambda functions on your behalf and sends function metrics to Amazon CloudWatch\. Your Lambda function comes with a CloudWatch Logs log group and a log stream for each instance of your function\. The Lambda runtime environment sends details about each invocation to the log stream, and relays logs and other output from your function's code\. 

This page describes how to produce log output from your Lambda function's code, or access logs using the AWS Command Line Interface, the Lambda console, or the CloudWatch console\.

**Topics**
+ [Creating a function that returns logs](#java-logging-output)
+ [Using the Lambda console](#java-logging-console)
+ [Using the CloudWatch console](#java-logging-cwconsole)
+ [Using the AWS Command Line Interface \(AWS CLI\)](#java-logging-cli)
+ [Deleting logs](#java-logging-delete)
+ [Advanced logging with Log4j 2 and SLF4J](#java-logging-log4j2)
+ [Sample logging code](#java-logging-samples)

## Creating a function that returns logs<a name="java-logging-output"></a>

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

## Using the Lambda console<a name="java-logging-console"></a>

You can use the Lambda console to view log output after you invoke a Lambda function\. For more information, see [Accessing Amazon CloudWatch logs for AWS Lambda](monitoring-cloudwatchlogs.md)\.

## Using the CloudWatch console<a name="java-logging-cwconsole"></a>

You can use the Amazon CloudWatch console to view logs for all Lambda function invocations\.

**To view logs on the CloudWatch console**

1. Open the [Log groups page](https://console.aws.amazon.com/cloudwatch/home?#logs:) on the CloudWatch console\.

1. Choose the log group for your function \(**/aws/lambda/*your\-function\-name***\)\.

1. Choose a log stream\.

Each log stream corresponds to an [instance of your function](lambda-runtime-environment.md)\. A log stream appears when you update your Lambda function, and when additional instances are created to handle multiple concurrent invocations\. To find logs for a specific invocation, we recommend instrumenting your function with AWS X\-Ray\. X\-Ray records details about the request and the log stream in the trace\.

To use a sample application that correlates logs and traces with X\-Ray, see [Error processor sample application for AWS Lambda](samples-errorprocessor.md)\.

## Using the AWS Command Line Interface \(AWS CLI\)<a name="java-logging-cli"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

You can use the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-welcome.html) to retrieve logs for an invocation using the `--log-type` command option\. The response contains a `LogResult` field that contains up to 4 KB of base64\-encoded logs from the invocation\.

**Example retrieve a log ID**  
The following example shows how to retrieve a *log ID* from the `LogResult` field for a function named `my-function`\.  

```
aws lambda invoke --function-name my-function out --log-type Tail
```
You should see the following output:  

```
{
    "StatusCode": 200,
    "LogResult": "U1RBUlQgUmVxdWVzdElkOiA4N2QwNDRiOC1mMTU0LTExZTgtOGNkYS0yOTc0YzVlNGZiMjEgVmVyc2lvb...",
    "ExecutedVersion": "$LATEST"
}
```

**Example decode the logs**  
In the same command prompt, use the `base64` utility to decode the logs\. The following example shows how to retrieve base64\-encoded logs for `my-function`\.  

```
aws lambda invoke --function-name my-function out --log-type Tail \
--query 'LogResult' --output text |  base64 -d
```
You should see the following output:  

```
START RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8 Version: $LATEST
"AWS_SESSION_TOKEN": "AgoJb3JpZ2luX2VjELj...", "_X_AMZN_TRACE_ID": "Root=1-5d02e5ca-f5792818b6fe8368e5b51d50;Parent=191db58857df8395;Sampled=0"",ask/lib:/opt/lib",
END RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8
REPORT RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8  Duration: 79.67 ms      Billed Duration: 80 ms         Memory Size: 128 MB     Max Memory Used: 73 MB
```
The `base64` utility is available on Linux, macOS, and [Ubuntu on Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. macOS users may need to use `base64 -D`\.

**Example get\-logs\.sh script**  
In the same command prompt, use the following script to download the last five log events\. The script uses `sed` to remove quotes from the output file, and sleeps for 15 seconds to allow time for the logs to become available\. The output includes the response from Lambda and the output from the `get-log-events` command\.   
Copy the contents of the following code sample and save in your Lambda project directory as `get-logs.sh`\.  
The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.  

```
#!/bin/bash
aws lambda invoke --function-name my-function --cli-binary-format raw-in-base64-out --payload '{"key": "value"}' out
sed -i'' -e 's/"//g' out
sleep 15
aws logs get-log-events --log-group-name /aws/lambda/my-function --log-stream-name stream1 --limit 5
```

**Example macOS and Linux \(only\)**  
In the same command prompt, macOS and Linux users may need to run the following command to ensure the script is executable\.  

```
chmod -R 755 get-logs.sh
```

**Example retrieve the last five log events**  
In the same command prompt, run the following script to get the last five log events\.  

```
./get-logs.sh
```
You should see the following output:  

```
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
            "message": "REPORT RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf\tDuration: 26.73 ms\tBilled Duration: 27 ms \tMemory Size: 128 MB\tMax Memory Used: 75 MB\t\n",
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

**Note**  
 AWS Lambda does not include Log4j2 in its managed runtimes or base container images\. These are therefore not affected by the issues described in CVE\-2021\-44228, CVE\-2021\-45046, and CVE\-2021\-45105\.   
 For cases where a customer function includes an impacted Log4j2 version, we have applied a change to the Lambda Java [managed runtimes](lambda-runtimes.md) and [base container images](java-image.md) that helps to mitigate the issues in CVE\-2021\-44228, CVE\-2021\-45046, and CVE\-2021\-45105\. As a result of this change, customers using Log4J2 may see an additional log entry, similar to "`Transforming org/apache/logging/log4j/core/lookup/JndiLookup (java.net.URLClassLoader@...)`"\. Any log strings that reference the jndi mapper in the Log4J2 output will be replaced with "`Patched JndiLookup::lookup()`"\.   
 Independent of this change, we strongly encourage all customers whose functions include Log4j2 to update to the latest version\. Specifically, customers using the aws\-lambda\-java\-log4j2 library in their functions should update to version 1\.5\.0 \(or later\), and redeploy their functions\. This version updates the underlying Log4j2 utility dependencies to version 2\.17\.0 \(or later\)\. The updated aws\-lambda\-java\-log4j2 binary is available at the [Maven repository](https://repo1.maven.org/maven2/com/amazonaws/aws-lambda-java-log4j2/) and its source code is available in [Github](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-log4j2)\. 

To customize log output, support logging during unit tests, and log AWS SDK calls, use Apache Log4j 2 with SLF4J\. Log4j is a logging library for Java programs that enables you to configure log levels and use appender libraries\. SLF4J is a facade library that lets you change which library you use without changing your function code\.

To add the request ID to your function's logs, use the appender in the [aws\-lambda\-java\-log4j2](java-package.md) library\. The following example shows a Log4j 2 configuration file that adds a timestamp and request ID to all logs\.

**Example [src/main/resources/log4j2\.xml](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java/src/main/resources/log4j2.xml) – Appender configuration**  

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

**Example [src/main/java/example/HandlerS3\.java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events/src/main/java/example/HandlerS3.java) – Logging with SLF4J**  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: example.Handler
public class HandlerS3 implements RequestHandler<S3Event, String>{
    private static final Logger logger = LoggerFactory.getLogger(HandlerS3.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String handleRequest(S3Event event, Context context)
    {
        ...
        logger.info("RECORD: " + record);
        logger.info("SOURCE BUCKET: " + srcBucket);
        logger.info("SOURCE KEY: " + srcKey);
        ...
    }
}
```

The build configuration takes runtime dependencies on the Lambda appender and SLF4J adapter, and implementation dependencies on Log4J 2\.

**Example [build\.gradle](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events/build.gradle) – Logging dependencies**  

```
dependencies {
    ...
    implementation 'org.apache.logging.log4j:log4j-api:[2.17.1,)'
    implementation 'org.apache.logging.log4j:log4j-core:[2.17.1,)'
    implementation 'org.apache.logging.log4j:log4j-slf4j18-impl:[2.17.1,)'
    ...
}
```

When you run your code locally for tests, the context object with the Lambda logger is not available, and there's no request ID for the Lambda appender to use\. For example test configurations, see the sample applications in the next section\. 

## Sample logging code<a name="java-logging-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of various logging configurations\. Each sample application includes scripts for easy deployment and cleanup, an AWS SAM template, and supporting resources\.

**Sample Lambda applications in Java**
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A collection of minimal Java functions with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A collection of Java functions that contain skeleton code for how to handle events from various services such as Amazon API Gateway, Amazon SQS, and Amazon Kinesis\. These functions use the latest version of the [aws\-lambda\-java\-events](java-package.md) library \(3\.0\.0 and newer\)\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.
+ [Use API Gateway to invoke a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/example_cross_LambdaAPIGateway_section.html) – A Java function that scans a Amazon DynamoDB table that contains employee information\. It then uses Amazon Simple Notification Service to send a text message to employees celebrating their work anniversaries\. This example uses API Gateway to invoke the function\.

The `java-basic` sample application shows a minimal logging configuration that supports logging tests\. The handler code uses the `LambdaLogger` logger provided by the context object\. For tests, the application uses a custom `TestLogger` class that implements the `LambdaLogger` interface with a Log4j 2 logger\. It uses SLF4J as a facade for compatibility with the AWS SDK\. Logging libraries are excluded from build output to keep the deployment package small\.