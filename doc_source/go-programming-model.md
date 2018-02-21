# Programming Model for Authoring Lambda Functions in Go<a name="go-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](http://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in [Go](https://golang.org/)\.


+ [Lambda Function Handler \(Go\)](go-programming-model-handler-types.md)
+ [The Context Object \(Go\)](go-programming-model-context.md)
+ [Logging \(Go\)](go-programming-model-logging.md)
+ [Function Errors \(Go\)](go-programming-model-errors.md)
+ [Using Environment Variables \(Go\)](go-programming-model-env-variables.md)

Additionally, note that AWS Lambda provides the following:

+ **github\.com/aws/aws\-lambda\-go/lambda**: The implementation of the Lambda programming model for Go\. This package is used by AWS Lambda to invoke your [Lambda Function Handler \(Go\)](go-programming-model-handler-types.md)\.

+ **github\.com/aws/aws\-lambda\-go/lambdacontext**: Helpers for accessing execution context information from the [The Context Object \(Go\) ](go-programming-model-context.md)\.

+ **github\.com/aws/aws\-lambda\-go/events**: This library provides type definitions for common event source integrations\.