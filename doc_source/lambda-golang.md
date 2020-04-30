# Building Lambda functions with Go<a name="lambda-golang"></a>

The following sections explain how common programming patterns and core concepts apply when authoring Lambda function code in [Go](https://golang.org/)\.


**Go runtimes**  

| Name | Identifier | Operating system | 
| --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  | 

AWS Lambda provides the following libraries for Go:
+ **github\.com/aws/aws\-lambda\-go/lambda**: The implementation of the Lambda programming model for Go\. This package is used by AWS Lambda to invoke your [handler](golang-handler.md)\.
+ **github\.com/aws/aws\-lambda\-go/lambdacontext**: Helpers for accessing execution context information from the [context object](golang-context.md)\.
+ **github\.com/aws/aws\-lambda\-go/events**: This library provides type definitions for common event source integrations\.

**Note**  
To get started with application development in your local environment, deploy one of the sample applications available in this guide's GitHub repository\.  
[blank\-go](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-go) – A Go function that shows the use of Lambda's Go libraries, logging, environment variables, and the AWS SDK\.

**Topics**
+ [AWS Lambda deployment package in Go](golang-package.md)
+ [AWS Lambda function handler in Go](golang-handler.md)
+ [AWS Lambda context object in Go](golang-context.md)
+ [AWS Lambda function logging in Go](golang-logging.md)
+ [AWS Lambda function errors in Go](golang-exceptions.md)
+ [Instrumenting Go code in AWS Lambda](golang-tracing.md)
+ [Using environment variables](golang-envvars.md)