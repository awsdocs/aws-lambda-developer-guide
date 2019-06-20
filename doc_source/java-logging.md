# AWS Lambda Function Logging in Java<a name="java-logging"></a>

Your Lambda function comes with a CloudWatch Logs log group, with a log stream for each instance of your function\. The runtime sends details about each invocation to the log stream, and relays logs and other output from your function's code\.

To output logs from your function code, you can use methods on [java\.lang\.System ](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html), or any logging module that writes to `stdout` or `stderr`\. The following example uses `System.out.println`\.

**Example Hello\.java**  

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Hello {
    public String myHandler(String name, Context context) {
        System.out.println("Event received.");
        return String.format("Hello %s.", name);
    }
}
```

The Lambda console shows log output when you test a function on the function configuration page\. To view logs for all invocations, use the CloudWatch Logs console\.

**To view your Lambda function's logs**

1. Open the [Logs page of the CloudWatch console](https://console.aws.amazon.com/cloudwatch/home?#logs:)\.

1. Choose the log group for your function \(**/aws/lambda/*function\-name***\)\.

1. Choose the first stream in the list\.

Each log stream corresponds to an [instance of your function](running-lambda-code.md)\. New streams appear when you update your function and when additional instances are created to handle multiple concurrent invocations\. To find logs for specific invocations, you can instrument your function with X\-Ray, and record details about the request and log stream in the trace\. For a sample application that correlates logs and traces with X\-Ray, see [Error Processor Sample Application for AWS Lambda](sample-errorprocessor.md)\.

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

Log groups aren't deleted automatically when you delete a function\. To avoid storing logs indefinitely, delete the log group, or [configure a retention period](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html#SettingLogRetention) after which logs are deleted automatically\.

## LambdaLogger<a name="java-logging-lambdalogger"></a>

Lambda provides a logger object that you can retrieve from the context object\. `LambdaLogger` supports multi\-line logs\. If you log a string that includes line breaks with `System.out.println`, each line break results in a separate entry in CloudWatch Logs\. If you use `LambdaLogger`, you get one entry with multiple lines\.

The following example function logs context information\.

**Example ContextLogger\.java**  

```
package example;
import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class ContextLogger {
    public static void myHandler(InputStream inputStream, OutputStream outputStream, Context context) {
        LambdaLogger logger = context.getLogger();
        int letter;
        try {
            while((letter = inputStream.read()) != -1)
            {
                outputStream.write(Character.toUpperCase(letter));
            }
            Thread.sleep(3000); // Intentional delay for testing the getRemainingTimeInMillis() result.
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        logger.log("Log data from LambdaLogger \n with multiple lines");
        // Print info from the context object
        logger.log("Function name: " + context.getFunctionName());
        logger.log("Max mem allocated: " + context.getMemoryLimitInMB());
        logger.log("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());

        // Return the log stream name so you can look up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }
}
```

**Dependencies**
+ `aws-lambda-java-core`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)\.

## Custom Appender for Log4j 2<a name="java-logging-log4j2"></a>

 AWS Lambda recommends Log4j 2 to provide a custom appender\. You can use the custom [Apache log4j](https://logging.apache.org/log4j/2.x/) appender provided by Lambda for logging from your functions\. The custom appender is called `LambdaAppender` and must be used in the `log4j2.xml` file\. You must include the `aws-lambda-java-log4j2` artifact \(`artifactId:aws-lambda-java-log4j2`\) in the deployment package\.

**Example Hello\.java**  

```
package example;

import com.amazonaws.services.lambda.runtime.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hello {
    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(Hello.class);

    public String myHandler(String name, Context context) {
        logger.error("log data from log4j err.");

        // Return will include the log stream name so you can look
        // up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }
}
```

The example preceding uses the following log4j2\.xml file to load properties

**Example log4j2\.xml**  

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration packages="com.amazonaws.services.lambda.runtime.log4j2">
  <Appenders>
    <Lambda name="Lambda">
      <PatternLayout>
          <pattern>%d{yyyy-MM-dd HH:mm:ss} %X{AWSRequestId} %-5p %c{1}:%L - %m%n</pattern>
      </PatternLayout>
    </Lambda>
  </Appenders>
  <Loggers>
    <Root level="info">
      <AppenderRef ref="Lambda" />
    </Root>
  </Loggers>
</Configuration>
```

**Dependencies**
+ `aws-lambda-java-log4j2`
+ `log4j-core`
+ `log4j-api`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)\.