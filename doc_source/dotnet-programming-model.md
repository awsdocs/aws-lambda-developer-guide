# Building Lambda Functions with C\#<a name="dotnet-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in C\#\.


**\.NET Runtimes**  

| Name | Identifier | Languages | Operating System | 
| --- | --- | --- | --- | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  C\# PowerShell Core 6\.0  |  Amazon Linux  | 
|  \.NET Core 1\.0  |  `dotnetcore1.0`  |  C\#  |  Amazon Linux  | 

**Topics**
+ [AWS Lambda Deployment Package in C\#](lambda-dotnet-how-to-create-deployment-package.md)
+ [AWS Lambda Function Handler in C\#](dotnet-programming-model-handler-types.md)
+ [AWS Lambda Context Object in C\#](dotnet-context-object.md)
+ [AWS Lambda Function Logging in C\#](dotnet-logging.md)
+ [AWS Lambda Function Errors in C\#](dotnet-exceptions.md)

Additionally, note that AWS Lambda provides the following:
+ **Amazon\.Lambda\.Core** – This library provides a static Lambda logger, serialization interfaces and a context object\. The `Context` object \([AWS Lambda Context Object in C\#](dotnet-context-object.md)\) provides runtime information about your Lambda function\. 
+ **Amazon\.Lambda\.Serialization\.Json ** – This an implementation of the serialization interface in **Amazon\.Lambda\.Core**\. 
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