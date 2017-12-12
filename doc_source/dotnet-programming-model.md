# Programming Model for Authoring Lambda Functions in C\#<a name="dotnet-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](http://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in C\#\.


+ [Lambda Function Handler \(C\#\)](dotnet-programming-model-handler-types.md)
+ [The Context Object \(C\#\)](dotnet-context-object.md)
+ [Logging \(C\#\)](dotnet-logging.md)
+ [Function Errors \(C\#\)](dotnet-exceptions.md)

Additionally, note that AWS Lambda provides the following:

+ **Amazon\.Lambda\.Core** – This library provides a static Lambda logger, serialization interfaces and a context object\. The `Context` object \([The Context Object \(C\#\)](dotnet-context-object.md)\) provides runtime information about your Lambda function\. 

+ **Amazon\.Lambda\.Serialization\.Json ** – This an implementation of the serialization interface in **Amazon\.Lambda\.Core**\. 

+ **Amazon\.Lambda\.Logging\.AspNetCore ** – This provides a library for logging from ASP\.NET\. 

+ Event objects \(POCOs\) for several AWS services, including: 

  + **Amazon\.Lambda\.APIGatewayEvents **

  + **Amazon\.Lambda\.CognitoEvents **

  + **Amazon\.Lambda\.ConfigEvents **

  + **Amazon\.Lambda\.DynamoDBEvents **

  + **Amazon\.Lambda\.KinesisEvents **

  + **Amazon\.Lambda\.S3Events **

  + **Amazon\.Lambda\.SNSEvents **

These packages are available at [Nuget Packages](https://www.nuget.org/packages/)\.