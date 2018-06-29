# Using Earlier Custom Appender for Log4j™ 1\.2 \(Not Recommended\)<a name="java-logging-log4j1"></a>

**Note**  
Support for the Log4j v1\.2 custom appender is marked for End\-Of\-Life\. It will not receive ongoing updates and is not recommended for use\. For more information, see [Log4j 1\.2](https://logging.apache.org/log4j/1.2/)

 AWS Lambda supports Log4j 1\.2 by providing a custom appender\. You can use the custom Log4j \(see [Apache log4j 1\.2](https://logging.apache.org/log4j/1.2/)\) appender provided by Lambda for logging from your lambda functions\. Every call to Log4j methods, such as `log.info()` or `log.error()`, will result in a CloudWatch Logs event\. The custom appender is called `LambdaAppender` and must be used in the `log4j.properties` file\. You must include the `aws-lambda-java-log4j` artifact \(`artifactId:aws-lambda-java-log4j`\) in the deployment package \(\.jar file\)\. For an example, see [Example: Writing Logs Using Log4J v1\.2 \(Not Recommended\) ](#java-wt-logging-using-log4j)\.

## Example: Writing Logs Using Log4J v1\.2 \(Not Recommended\)<a name="java-wt-logging-using-log4j"></a>

**Note**  
Versions 1\.x of Log4j have been marked as end of life\. For more information, see [Log4j 1\.2](https://logging.apache.org/log4j/1.2/)

The following Java code example writes logs using both the System methods and Log4j to illustrate how they differ when AWS Lambda logs information to CloudWatch\.

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 

import org.apache.logging.log4j.Logger;

public class Hello {
    // Initialize the Log4j logger.
    static final Logger log = Logger.getLogger(Hello.class);

    public String myHandler(String name, Context context) {
        // System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch). 
        System.out.println("log data from stdout \n this is continuation of system.out");
        
       // System.err: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.err.println("log data from stderr. \n this is a continuation of system.err");

       

        log.error("log data from log4j err. \n this is a continuation of log4j.err");
        
        // Return will include the log stream name so you can look 
        // up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }
}
```

The example uses the following log4j\.properties file \(*project\-dir*/src/main/resources/ directory\)\.

```
log = .
log4j.rootLogger = INFO, LAMBDA

#Define the LAMBDA appender
log4j.appender.LAMBDA=com.amazonaws.services.lambda.runtime.log4j.LambdaAppender
log4j.appender.LAMBDA.layout=org.apache.log4j.PatternLayout
log4j.appender.LAMBDA.layout.conversionPattern=%d{yyyy-MM-dd HH:mm:ss} <%X{AWSRequestId}> %-5p %c{1}:%m%n
```

You can do the following to test the code:
+ Using the code, create a deployment package\. In your project, don't forget to add the `log4j.properties` file in the *project\-dir*/src/main/resources/ directory\.
+ Upload the deployment package to AWS Lambda to create your Lambda function\. 
+ To test your Lambda function use a string \("this is a test"\) as sample event\. The handler code receives the sample event but does nothing with it\. It only shows how to write logs\.

Follow the instructions provided in the Getting Started\. For more information, see  [\(Optional\) Create a Lambda Function Authored in Java](get-started-step4-optional.md)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-log4j` dependency for Log4j 1\.2 dependency\. 
+ When you create the Lambda function, specify `example.Hello::myHandler (package.class::method)` as the handler value\.