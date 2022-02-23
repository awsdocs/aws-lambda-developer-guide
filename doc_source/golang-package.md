# Deploy Go Lambda functions with \.zip file archives<a name="golang-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

This page describes how to create a \.zip file as your deployment package for the Go runtime, and then use the \.zip file to deploy your function code to AWS Lambda using the AWS Command Line Interface \(AWS CLI\)\. 

**Topics**
+ [Prerequisites](#golang-package-prereqs)
+ [Tools and libraries](#golang-package-libraries)
+ [Sample applications](#golang-package-sample)
+ [Creating a \.zip file on macOS and Linux](#golang-package-mac-linux)
+ [Creating a \.zip file on Windows](#golang-package-windows)
+ [Build Go with the provided\.al2 runtime](#golang-package-al2)

## Prerequisites<a name="golang-package-prereqs"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## Tools and libraries<a name="golang-package-libraries"></a>

Lambda provides the following tools and libraries for the Go runtime:

**Tools and libraries for Go**
+ [AWS SDK for Go](https://github.com/aws/aws-sdk-go): the official AWS SDK for the Go programming language\.
+ [github\.com/aws/aws\-lambda\-go/lambda](https://github.com/aws/aws-lambda-go/tree/master/lambda): The implementation of the Lambda programming model for Go\. This package is used by AWS Lambda to invoke your [handler](golang-handler.md)\.
+ [github\.com/aws/aws\-lambda\-go/lambdacontext](https://github.com/aws/aws-lambda-go/tree/master/lambdacontext): Helpers for accessing context information from the [context object](golang-context.md)\.
+ [github\.com/aws/aws\-lambda\-go/events](https://github.com/aws/aws-lambda-go/tree/master/events): This library provides type definitions for common event source integrations\.
+ [github\.com/aws/aws\-lambda\-go/cmd/build\-lambda\-zip](https://github.com/aws/aws-lambda-go/tree/master/cmd/build-lambda-zip): This tool can be used to create a \.zip file archive on Windows\.

For more information, see [aws\-lambda\-go](https://github.com/aws/aws-lambda-go) on GitHub\.

## Sample applications<a name="golang-package-sample"></a>

Lambda provides the following sample applications for the Go runtime:

**Sample Lambda applications in Go**
+ [blank\-go](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-go) – A Go function that shows the use of Lambda's Go libraries, logging, environment variables, and the AWS SDK\.

## Creating a \.zip file on macOS and Linux<a name="golang-package-mac-linux"></a>

The following steps demonstrate how to download the [lambda](https://github.com/aws/aws-lambda-go/tree/master/lambda) library from GitHub with `go get`, and compile your executable with [go build](https://golang.org/cmd/go/)\.

1. Download the **lambda** library from GitHub\.

   ```
   go get github.com/aws/aws-lambda-go/lambda
   ```

1. Compile your executable\.

   ```
   GOOS=linux go build main.go
   ```

   Setting `GOOS` to `linux` ensures that the compiled executable is compatible with the [Go runtime](lambda-runtimes.md), even if you compile it in a non\-Linux environment\.

1. \(Optional\) If your `main` package consists of multiple files, use the following [go build](https://golang.org/cmd/go/) command to compile the package:

   ```
   GOOS=linux go build main
   ```

1. \(Optional\) You may need to compile packages with `CGO_ENABLED=0` set on Linux:

   ```
   GOOS=linux CGO_ENABLED=0 go build main.go
   ```

   This command creates a stable binary package for standard C library \(`libc`\) versions, which may be different on Lambda and other devices\.

1. Lambda uses POSIX file permissions, so you may need to [ set permissions for the deployment package folder](https://aws.amazon.com/premiumsupport/knowledge-center/lambda-deployment-package-errors/) before you create the \.zip file archive\.

1. Create a deployment package by packaging the executable in a \.zip file\.

   ```
   zip function.zip main
   ```

## Creating a \.zip file on Windows<a name="golang-package-windows"></a>

The following steps demonstrate how to download the [build\-lambda\-zip](https://github.com/aws/aws-lambda-go/tree/master/cmd/build-lambda-zip) tool for Windows from GitHub with `go get`, and compile your executable with [go build](https://golang.org/cmd/go/)\.

**Note**  
If you have not already done so, you must install [git](https://git-scm.com/) and then add the `git` executable to your Windows `%PATH%` environment variable\.

1. Download the **build\-lambda\-zip** tool from GitHub:

   ```
   go.exe get -u github.com/aws/aws-lambda-go/cmd/build-lambda-zip
   ```

1. Use the tool from your `GOPATH` to create a \.zip file\. If you have a default installation of Go, the tool is typically in `%USERPROFILE%\Go\bin`\. Otherwise, navigate to where you installed the Go runtime and do one of the following:

------
#### [ cmd\.exe ]

   In cmd\.exe, run the following:

   ```
   set GOOS=linux
   go build -o main main.go
   %USERPROFILE%\Go\bin\build-lambda-zip.exe -output main.zip main
   ```

------
#### [ PowerShell ]

   In PowerShell, run the following:

   ```
   $env:GOOS = "linux"
   $env:CGO_ENABLED = "0"
   $env:GOARCH = "amd64"
   go build -o main main.go
   ~\Go\Bin\build-lambda-zip.exe -output main.zip main
   ```

------

## Build Go with the provided\.al2 runtime<a name="golang-package-al2"></a>

Go is implemented differently than other native runtimes\. Lambda treats Go as a custom runtime, so you can create a Go function on the provided\.al2 runtime\. You can use the AWS SAM build command to build the \.zip file package\.

**Using AWS SAM to build Go for AL2 function**

1. Update the AWS SAM template to use the provided\.al2 runtime\. Also set the BuildMethod to makefile\.

   ```
   Resources:
     HelloWorldFunction:
       Type: AWS::Serverless::Function
       Properties:
         CodeUri: hello-world/
         Handler: my.bootstrap.file
         Runtime: provided.al2
         Architectures: [arm64]  
       Metadata:
         BuildMethod: makefile
   ```

   Remove the `Architectures` property to build the package for the x86\_64 instruction set architecture\.

1. Add file makefile to the project folder, with the following contents:

   ```
   GOOS=linux go build -o bootstrap
   	cp ./bootstrap $(ARTIFACTS_DIR)/.
   ```

For an example application, download [ Go on AL2](https://github.com/aws-samples/sessions-with-aws-sam/tree/master/go-al2)\. The readme file contains the instructions to build and run the application\. You can also view the blog post [ Migrating AWS Lambda functions to Amazon Linux 2](https://github.com/aws-samples/sessions-with-aws-sam/tree/master/go-al2)\.