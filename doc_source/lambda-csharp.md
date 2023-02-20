# Building Lambda functions with C\#<a name="lambda-csharp"></a>

The following sections explain how common programming patterns and core concepts apply when authoring Lambda function code in C\#\.

AWS Lambda provides the following libraries for C\# functions:
+ **Amazon\.Lambda\.Core** – This library provides a static Lambda logger, serialization interfaces and a context object\. The `Context` object \([AWS Lambda context object in C\#](csharp-context.md)\) provides runtime information about your Lambda function\.
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

These packages are available at [Nuget packages](https://www.nuget.org/packages/)\.


**\.NET**  

| Name | Identifier | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |  Amazon Linux 2  |  x86\_64, arm64  |  Mar 31, 2023  | 
|  \.NET 6  |  `dotnet6`  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  \.NET 5  |  `dotnet5.0`  |  Amazon Linux 2  |  x86\_64  |    | 

**Note**  
For end of support information about \.NET Core 2\.1, see [Runtime deprecation policy](lambda-runtimes.md#runtime-support-policy)\.

To get started with application development in your local environment, deploy one of the sample applications available in this guide's GitHub repository\.

**Sample Lambda applications in C\#**
+ [blank\-csharp](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-csharp) – A C\# function that shows the use of Lambda's \.NET libraries, logging, environment variables, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [ec2\-spot](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/ec2-spot) – A function that manages spot instance requests in Amazon EC2\.

**Topics**
+ [Lambda function handler in C\#](csharp-handler.md)
+ [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md)
+ [Deploy \.NET Lambda functions with container images](csharp-image.md)
+ [AWS Lambda context object in C\#](csharp-context.md)
+ [Lambda function logging in C\#](csharp-logging.md)
+ [AWS Lambda function errors in C\#](csharp-exceptions.md)
+ [Instrumenting C\# code in AWS Lambda](csharp-tracing.md)
+ [\.NET functions with native AOT compilation](dotnet-native-aot.md)