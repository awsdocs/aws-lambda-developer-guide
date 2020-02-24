# Building Lambda Functions with Java<a name="lambda-java"></a>

The following sections explain how common programming patterns and core concepts apply when authoring Lambda function code in Java\.


**Java Runtimes**  

| Name | Identifier | JDK | Operating System | 
| --- | --- | --- | --- | 
|  Java 11  |  `java11`  |  amazon\-corretto\-11  |  Amazon Linux 2  | 
|  Java 8  |  `java8`  |  java\-1\.8\.0\-openjdk  |  Amazon Linux  | 

AWS Lambda provides the following libraries for Java functions:
+ **aws\-lambda\-java\-core** – This library provides the Context object, `RequestStreamHandler`, and the `RequestHandler` interfaces\. The `Context` object \([AWS Lambda Context Object in Java](java-context.md)\) provides runtime information about your Lambda function\. The predefined interfaces provide one way of defining your Lambda function handler\. For more information, see [Using Provided Interfaces for Java Function Handlers in AWS Lambda](java-handler-interfaces.md)\.
+ **aws\-lambda\-java\-events** – This library provides predefined types that you can use when writing Lambda functions to process events published by Amazon S3, Kinesis, Amazon SNS, and Amazon Cognito\. These classes help you process the event without having to write your own custom serialization logic\.
+ **Custom Appender for Log4j2\.8** – You can use the custom Log4j \(see [Apache Log4j 2](http://logging.apache.org/log4j/2.x)\) appender provided by AWS Lambda for logging from your lambda functions\. Every call to Log4j methods, such as log\.info\(\) or log\.error\(\), will result in a CloudWatch Logs event\. The custom appender is called LambdaAppender and must be used in the log4j2\.xml file\. You must include the aws\-lambda\-java\-log4j2 artifact \(artifactId:aws\-lambda\-java\-log4j2\) in the deployment package \(\.jar file\)\.For more information, see [AWS Lambda Function Logging in Java](java-logging.md)\.

 These libraries are available through the [Maven Central Repository](https://search.maven.org/search?q=g:com.amazonaws) and can also be found on [GitHub](https://github.com/aws/aws-lambda-java-libs)\.

**Topics**
+ [AWS Lambda Deployment Package in Java](java-package.md)
+ [AWS Lambda Function Handler in Java](java-handler.md)
+ [AWS Lambda Context Object in Java](java-context.md)
+ [AWS Lambda Function Logging in Java](java-logging.md)
+ [AWS Lambda Function Errors in Java](java-exceptions.md)
+ [Instrumenting Java Code in AWS Lambda](java-tracing.md)