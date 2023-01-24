# Building Lambda functions with Go<a name="lambda-golang"></a>

The following sections explain how common programming patterns and core concepts apply when authoring Lambda function code in [Go](https://golang.org/)\.


**Go**  

| Name | Identifier | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  |  x86\_64  |    | 

**Note**  
Runtimes that use the Amazon Linux operating system, such as Go 1\.x, do not support the arm64 architecture\. To use arm64 architecture, you can run Go with the provided\.al2 runtime\.

Lambda provides the following tools and libraries for the Go runtime:

**Tools and libraries for Go**
+ [AWS SDK for Go](https://github.com/aws/aws-sdk-go): the official AWS SDK for the Go programming language\.
+ [github\.com/aws/aws\-lambda\-go/lambda](https://github.com/aws/aws-lambda-go/tree/master/lambda): The implementation of the Lambda programming model for Go\. This package is used by AWS Lambda to invoke your [handler](golang-handler.md)\.
+ [github\.com/aws/aws\-lambda\-go/lambdacontext](https://github.com/aws/aws-lambda-go/tree/master/lambdacontext): Helpers for accessing context information from the [context object](golang-context.md)\.
+ [github\.com/aws/aws\-lambda\-go/events](https://github.com/aws/aws-lambda-go/tree/master/events): This library provides type definitions for common event source integrations\.
+ [github\.com/aws/aws\-lambda\-go/cmd/build\-lambda\-zip](https://github.com/aws/aws-lambda-go/tree/master/cmd/build-lambda-zip): This tool can be used to create a \.zip file archive on Windows\.

For more information, see [aws\-lambda\-go](https://github.com/aws/aws-lambda-go) on GitHub\.

Lambda provides the following sample applications for the Go runtime:

**Sample Lambda applications in Go**
+ [blank\-go](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-go) – A Go function that shows the use of Lambda's Go libraries, logging, environment variables, and the AWS SDK\.

**Topics**
+ [AWS Lambda function handler in Go](golang-handler.md)
+ [AWS Lambda context object in Go](golang-context.md)
+ [Deploy Go Lambda functions with \.zip file archives](golang-package.md)
+ [Deploy Go Lambda functions with container images](go-image.md)
+ [AWS Lambda function logging in Go](golang-logging.md)
+ [AWS Lambda function errors in Go](golang-exceptions.md)
+ [Instrumenting Go code in AWS Lambda](golang-tracing.md)
+ [Using environment variables](golang-envvars.md)