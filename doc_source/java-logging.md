# Logging \(Java\)<a name="java-logging"></a>

 Your Lambda function can contain logging statements\. AWS Lambda writes these logs to CloudWatch\. We recommend you use one of the following to write logs\.

## Custom Appender for Log4j™ 2<a name="java-logging-log4j2"></a>

 AWS Lambda recommends Log4j 2 to provide a custom appender\. You can use the custom Log4j \(see [Apache log4j](https://logging.apache.org/log4j/2.x/)\) appender provided by Lambda for logging from your lambda functions\. Every call to Log4j methods, such as `log.debug()` or `log.error()`, will result in a CloudWatch Logs event\. The custom appender is called `LambdaAppender` and must be used in the `log4j2.xml` file\. You must include the `aws-lambda-java-log4j2` artifact \(`artifactId:aws-lambda-java-log4j2`\) in the deployment package \(\.jar file\)\. For an example, see [Example 1: Writing Logs Using Log4J v2\.8 ](#java-wt-logging-using-log4j2.8)\.

## LambdaLogger\.log\(\)<a name="java-logging-lambdalogger"></a>

 Each call to `LambdaLogger.log()` results in a CloudWatch Logs event, provided the event size is within the allowed limits\. For information about CloudWatch Logs limits, see [CloudWatch Logs Limits](http://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/cloudwatch_limits_cwl.html) in the *Amazon CloudWatch User Guide*\. For an example, see [Example 2: Writing Logs Using LambdaLogger \(Java\)](#java-wt-logging)\.

In addition, you can also use the following statements in your Lambda function code to generate log entries:
+ System\.out\(\)
+ System\.err\(\)

However, note that AWS Lambda treats each line returned by `System.out` and `System.err` as a separate event\. This works well when each output line corresponds to a single log entry\. When a log entry has multiple lines of output, AWS Lambda attempts to parse them using line breaks to identify separate events\. For example, the following logs the two words \("Hello" and "world"\) as two separate events:

```
System.out.println("Hello \n world"); 
```

## How to Find Logs<a name="how-to-find-logs-java"></a>

You can find the logs that your Lambda function writes, as follows:
+ Find logs in CloudWatch Logs\. The `context` object \(in the `aws-lambda-java-core` library\) provides the `getLogStreamName()` and the `getLogGroupName()` methods\. Using these methods, you can find the specific log stream where logs are written\.
+ If you invoke a Lambda function via the console, the invocation type is always `RequestResponse` \(that is, synchronous execution\) and the console displays the logs that the Lambda function writes using the `LambdaLogger` object\. AWS Lambda also returns logs from `System.out` and `System.err` methods\.
+ If you invoke a Lambda function programmatically, you can add the `LogType` parameter to retrieve the last 4 KB of log data that is written to CloudWatch Logs\. For more information, see [Invoke](API_Invoke.md)\. AWS Lambda returns this log information in the `x-amz-log-results` header in the response\. If you use the AWS Command Line Interface to invoke the function, you can specify the `--log-type` parameter with value `Tail`\. 

## Logging Examples \(Java\)<a name="logging-java-examples"></a>

This section provides examples of using Custom Appender for Log4j and the `LambdaLogger` objects for logging information\. 

### Example 1: Writing Logs Using Log4J v2\.8<a name="java-wt-logging-using-log4j2.8"></a>
+ The following shows how to build your artifact with Maven to correctly include the Log4j v2\.8 plugins: 
  + For Maven pom\.xml:

    ```
     <dependencies>
      ...
      <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-log4j2</artifactId>
        <version>1.0.0</version>
      </dependency>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.8.2</version>
      </dependency>
      <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.8.2</version>
      </dependency>
      ....
    </dependencies>
    ```
  + If using the Maven shade plugin, set the plugin configuration as follows:

    ```
    <plugins>
      ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.3</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <transformers>
                <transformer
                        implementation="com.github.edwgiz.mavenShadePlugin.log4j2CacheTransformer.PluginsCacheFileTransformer">
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
        <dependencies>
          <dependency>
            <groupId>com.github.edwgiz</groupId>
            <artifactId>maven-shade-plugin.log4j2-cachefile-transformer</artifactId>
            <version>2.8.1</version>
          </dependency>
        </dependencies>
      </plugin>
      ...
    </plugins>
    ```
  + The following Java code example shows how to use Log4j with Lambda:

    ```
    package example;
    
    import com.amazonaws.services.lambda.runtime.Context;
    
    import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;
    
    public class Hello {
        // Initialize the Log4j logger.
        static final Logger logger = LogManager.getLogger(Hello.class);
    
        public String myHandler(String name, Context context) {
            // System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
            System.out.println("log data from stdout \n this is continuation of system.out");
    
           // System.err: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
            System.err.println("log data from stderr. \n this is a continuation of system.err");
    
            // Use log4j to log the same thing as above and AWS Lambda will log only one event in CloudWatch.
            logger.debug("log data from log4j debug \n this is continuation of log4j debug");
    
            logger.error("log data from log4j err. \n this is a continuation of log4j.err");
    
            // Return will include the log stream name so you can look
            // up the log later.
            return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
        }
    }
    ```
  + The example preceding uses the following log4j2\.xml file to load properties

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
        <Root level="debug">
          <AppenderRef ref="Lambda" />
        </Root>
      </Loggers>
    </Configuration>
    ```

### Example 2: Writing Logs Using LambdaLogger \(Java\)<a name="java-wt-logging"></a>

The following Java code example writes logs using both the System methods and the `LambdaLogger` object to illustrate how they differ when AWS Lambda logs information to CloudWatch\. 

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Hello {
    public String myHandler(String name, Context context) {
        
         // System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.out.println("log data from stdout \n this is continuation of system.out");
        
        // System.err: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.err.println("log data from stderr \n this is continuation of system.err");

        LambdaLogger logger = context.getLogger();
        // Write log to CloudWatch using LambdaLogger.
        logger.log("log data from LambdaLogger \n this is continuation of logger.log");
        
        // Return will include the log stream name so you can look 
        // up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }
}
```

The following is sample of log entries in CloudWatch Logs\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/logging-java-lambda-logger-10.png)

Note:
+ AWS Lambda parses the log string in each of the `System.out.println()` and `System.err.println()` statements logs as two separate events \(note the two down arrows in the screenshot\) because of the line break\.
+ The `LambdaLogger.log()` produce one CloudWatch event\.

You can do the following to test the code:
+ Using the code, create a deployment package\. 
+ Upload the deployment package to AWS Lambda to create your Lambda function\. 
+ To test your Lambda function use a string \("this is a test"\) as sample event\. The handler code receives the sample event but does nothing with it\. It only shows how to write logs\.

Follow the instructions provided in the Getting Started\. For more information, see  [\(Optional\) Create a Lambda Function Authored in Java](get-started-step4-optional.md)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-core` library dependency\. 
+ When you create the Lambda function, specify `example.Hello::myHandler (package.class::method)` as the handler value\.
