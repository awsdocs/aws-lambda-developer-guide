# Building Lambda Functions with C\#<a name="lambda-csharp"></a>

The following sections explain how common programming patterns and core concepts apply when authoring Lambda function code in C\#\.

AWS Lambda provides the following libraries for C\# functions:
+ **Amazon\.Lambda\.Core** – This library provides a static Lambda logger, serialization interfaces and a context object\. The `Context` object \([AWS Lambda Context Object in C\#](csharp-context.md)\) provides runtime information about your Lambda function\.
+ **Amazon\.Lambda\.Serialization\.Json ** – This is an implementation of the serialization interface in **Amazon\.Lambda\.Core**\. 
+ **Amazon\.Lambda\.Logging\.AspNetCore ** – This provides a library for logging from ASP\.NET\. 
+ Event objects \(POCOs\) for several AWS services, including: 
  + **Amazon\.Lambda\.APIGatewayEvents **
  + **Amazon\.Lambda\.CognitoEvents **
  + **Amazon\.Lambda\.ConfigEvents **
  + **Amazon\.Lambda\.DynamoDBEvents **
  + **Amazon\.Lambda\.KinesisEvents **
  + **Amazon\.Lambda\.S3Events **
  + **Amazon\.Lambda\.SQSEvents **
  + **Amazon\.Lambda\.SNSEvents **

These packages are available at [Nuget Packages](https://www.nuget.org/packages/)\.


**\.NET Runtimes**  

| Name | Identifier | Operating System | 
| --- | --- | --- | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |  Amazon Linux 2  | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  Amazon Linux  | 

**Topics**
+ [AWS Lambda Deployment Package in C\#](csharp-package.md)
+ [AWS Lambda Function Handler in C\#](csharp-handler.md)
+ [AWS Lambda Context Object in C\#](csharp-context.md)
+ [AWS Lambda Function Logging in C\#](csharp-logging.md)
+ [AWS Lambda Function Errors in C\#](csharp-exceptions.md)