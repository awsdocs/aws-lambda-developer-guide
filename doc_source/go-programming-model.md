# Building Lambda Functions with Go<a name="go-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in [Go](https://golang.org/)\.


**Go Runtimes**  

| Name | Identifier | Operating System | 
| --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  | 

**Topics**
+ [AWS Lambda Deployment Package in Go](lambda-go-how-to-create-deployment-package.md)
+ [AWS Lambda Function Handler in Go](go-programming-model-handler-types.md)
+ [AWS Lambda Context Object in Go](go-programming-model-context.md)
+ [AWS Lambda Function Logging in Go](go-programming-model-logging.md)
+ [AWS Lambda Function Errors in Go](go-programming-model-errors.md)
+ [Instrumenting Go Code in AWS Lambda](go-tracing.md)
+ [Using Environment Variables](go-programming-model-env-variables.md)

Additionally, note that AWS Lambda provides the following:
+ **github\.com/aws/aws\-lambda\-go/lambda**: The implementation of the Lambda programming model for Go\. This package is used by AWS Lambda to invoke your [AWS Lambda Function Handler in Go](go-programming-model-handler-types.md)\.
+ **github\.com/aws/aws\-lambda\-go/lambdacontext**: Helpers for accessing execution context information from the [AWS Lambda Context Object in Go](go-programming-model-context.md)\.
+ **github\.com/aws/aws\-lambda\-go/events**: This library provides type definitions for common event source integrations\.