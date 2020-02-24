# Building Lambda Functions with Go<a name="lambda-golang"></a>

The following sections explain how common programming patterns and core concepts apply when authoring Lambda function code in [Go](https://golang.org/)\.


**Go Runtimes**  

| Name | Identifier | Operating System | 
| --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  | 

AWS Lambda provides the following libraries for Go:
+ **github\.com/aws/aws\-lambda\-go/lambda**: The implementation of the Lambda programming model for Go\. This package is used by AWS Lambda to invoke your [handler](golang-handler.md)\.
+ **github\.com/aws/aws\-lambda\-go/lambdacontext**: Helpers for accessing execution context information from the [context object](golang-context.md)\.
+ **github\.com/aws/aws\-lambda\-go/events**: This library provides type definitions for common event source integrations\.

**Topics**
+ [AWS Lambda Deployment Package in Go](golang-package.md)
+ [AWS Lambda Function Handler in Go](golang-handler.md)
+ [AWS Lambda Context Object in Go](golang-context.md)
+ [AWS Lambda Function Logging in Go](golang-logging.md)
+ [AWS Lambda Function Errors in Go](golang-exceptions.md)
+ [Instrumenting Go Code in AWS Lambda](golang-tracing.md)
+ [Using Environment Variables](golang-envvars.md)