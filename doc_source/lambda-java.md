# Building Lambda functions with Java<a name="lambda-java"></a>

You can run Java code in AWS Lambda\. Lambda provides [runtimes](lambda-runtimes.md) for Java that run your code to process events\. Your code runs in an Amazon Linux environment that includes AWS credentials from an AWS Identity and Access Management \(IAM\) role that you manage\.

Lambda supports the following Java runtimes\.


**Java**  

| Name | Identifier | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | 
|  Java 11  |  `java11`  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Java 8  |  `java8.al2`  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Java 8  |  `java8`  |  Amazon Linux  |  x86\_64  |    | 

Lambda provides the following libraries for Java functions:
+ [com\.amazonaws:aws\-lambda\-java\-core](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-core) \(required\) – Defines handler method interfaces and the context object that the runtime passes to the handler\. If you define your own input types, this is the only library that you need\.
+ [com\.amazonaws:aws\-lambda\-java\-events](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) – Input types for events from services that invoke Lambda functions\.
+ [com\.amazonaws:aws\-lambda\-java\-log4j2](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-log4j2) – An appender library for Apache Log4j 2 that you can use to add the request ID for the current invocation to your [function logs](java-logging.md)\.
+ [AWS SDK for Java 2\.0](https://github.com/aws/aws-sdk-java-v2) – The official AWS SDK for the Java programming language\.

Lambda functions use an [execution role](lambda-intro-execution-role.md) to get permission to write logs to Amazon CloudWatch Logs, and to access other services and resources\. If you don't already have an execution role for function development, create one\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **Lambda**\.
   + **Permissions** – **AWSLambdaBasicExecutionRole**\.
   + **Role name** – **lambda\-role**\.

   The **AWSLambdaBasicExecutionRole** policy has the permissions that the function needs to write logs to CloudWatch Logs\.

You can add permissions to the role later, or swap it out for a different role that's specific to a single function\.

**To create a Java function**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Choose **Create function**\.

1. Configure the following settings:
   + **Name** – **my\-function**\.
   + **Runtime** – **Java 11**\.
   + **Role** – **Choose an existing role**\.
   + **Existing role** – **lambda\-role**\.

1. Choose **Create function**\.

1. To configure a test event, choose **Test**\.

1. For **Event name**, enter **test**\.

1. Choose **Save changes**\.

1. To invoke the function, choose **Test**\.

The console creates a Lambda function with a handler class named `Hello`\. Since Java is a compiled language, you can't view or edit the source code in the Lambda console, but you can modify its configuration, invoke it, and configure triggers\.

**Note**  
To get started with application development in your local environment, deploy one of the [sample applications](java-samples.md) available in this guide's GitHub repository\.

The `Hello` class has a function named `handleRequest` that takes an event object and a context object\. This is the [handler function](java-handler.md) that Lambda calls when the function is invoked\. The Java function runtime gets invocation events from Lambda and passes them to the handler\. In the function configuration, the handler value is `example.Hello::handleRequest`\.

To update the function's code, you create a deployment package, which is a \.zip file archive that contains your function code\. As your function development progresses, you will want to store your function code in source control, add libraries, and automate deployments\. Start by [creating a deployment package](java-package.md) and updating your code at the command line\.

The function runtime passes a context object to the handler, in addition to the invocation event\. The [context object](java-context.md) contains additional information about the invocation, the function, and the execution environment\. More information is available from environment variables\.

Your Lambda function comes with a CloudWatch Logs log group\. The function runtime sends details about each invocation to CloudWatch Logs\. It relays any [logs that your function outputs](java-logging.md) during invocation\. If your function [returns an error](java-exceptions.md), Lambda formats the error and returns it to the invoker\.

**Topics**
+ [AWS Lambda function handler in Java](java-handler.md)
+ [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md)
+ [Deploy Java Lambda functions with container images](java-image.md)
+ [AWS Lambda context object in Java](java-context.md)
+ [AWS Lambda function logging in Java](java-logging.md)
+ [AWS Lambda function errors in Java](java-exceptions.md)
+ [Instrumenting Java code in AWS Lambda](java-tracing.md)
+ [Creating a deployment package using Eclipse](java-package-eclipse.md)
+ [Java sample applications for AWS Lambda](java-samples.md)