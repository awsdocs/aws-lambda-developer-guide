# Programming Model for Authoring Lambda Functions in Java<a name="java-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in Java\.

**Topics**
+ [Lambda Function Handler \(Java\)](java-programming-model-handler-types.md)
+ [The Context Object \(Java\)](java-context-object.md)
+ [Logging \(Java\)](java-logging.md)
+ [Function Errors \(Java\)](java-exceptions.md)
+ [Create a Lambda Function Authored in Java](get-started-step4-optional.md)

Additionally, note that AWS Lambda provides the following libraries:
+ **aws\-lambda\-java\-core** – This library provides the Context object, `RequestStreamHandler`, and the `RequestHandler` interfaces\. The `Context` object \([The Context Object \(Java\)](java-context-object.md)\) provides runtime information about your Lambda function\. The predefined interfaces provide one way of defining your Lambda function handler\. For more information, see [Leveraging Predefined Interfaces for Creating Handler \(Java\)](java-handler-using-predefined-interfaces.md)\.
+ **aws\-lambda\-java\-events** – This library provides predefined types that you can use when writing Lambda functions to process events published by Amazon S3, Kinesis, Amazon SNS, and Amazon Cognito\. These classes help you process the event without having to write your own custom serialization logic\.
+ **Custom Appender for Log4j2\.8** – You can use the custom Log4j \(see [Apache Log4j 2](http://logging.apache.org/log4j/2.x)\) appender provided by AWS Lambda for logging from your lambda functions\. Every call to Log4j methods, such as log\.info\(\) or log\.error\(\), will result in a CloudWatch Logs event\. The custom appender is called LambdaAppender and must be used in the log4j2\.xml file\. You must include the aws\-lambda\-java\-log4j2 artifact \(artifactId:aws\-lambda\-java\-log4j2\) in the deployment package \(\.jar file\)\.For more information, see [Logging \(Java\)](java-logging.md)\.
+ **Custom Appender for Log4j1\.2 ** – You can use the custom Log4j \(see [Apache Log4j 1\.2](http://logging.apache.org/log4j/1.2)\) appender provided by AWS Lambda for logging from your lambda functions\. For more information, see [Logging \(Java\)](java-logging.md)\.
**Note**  
Support for the Log4j v1\.2 custom appender is marked for End\-Of\-Life\. It will not receive ongoing updates and is not recommended for use\.

 These libraries are available through the [Maven Central Repository](https://maven.apache.org/) and can also be found on [GitHub](https://github.com/)\.