# Deploy Go Lambda functions with \.zip file archives<a name="golang-package"></a>

To create a container image to deploy your function code, see [Using container images with Lambda](lambda-images.md)\.

A \.zip file archive is a deployment package for your Lambda function that consists of your code \(a Go executable\) and any dependencies\.

After you create the deployment package, you can upload it directly to an Amazon Simple Storage Service \(Amazon S3\) bucket in the AWS Region where you want to create the Lambda function\. Or, you can upload the \.zip file archive first and then specify the S3 bucket name and object key name when you create the function using the Lambda console or the AWS Command Line Interface \(AWS CLI\)\.

Download the Lambda library for Go with `go get`, and compile your executable\.

**Note**  
Use `aws-lambda-go` version 1\.18\.0 or later``\.

```
~/my-function$ go get github.com/aws/aws-lambda-go/lambda
~/my-function$ GOOS=linux go build main.go
```

Setting `GOOS` to `linux` ensures that the compiled executable is compatible with the [Go runtime](lambda-runtimes.md), even if you compile it in a non\-Linux environment\.

Create a deployment package by packaging the executable in a \.zip file, and use the AWS CLI to create a function\. The handler parameter must match the name of the executable containing your handler\.

```
~/my-function$ zip function.zip main
~/my-function$ aws lambda create-function --function-name my-function --runtime go1.x \
  --zip-file fileb://function.zip --handler main \
  --role arn:aws:iam::123456789012:role/execution_role
```

## Creating a deployment package on Windows<a name="golang-package-windows"></a>

To create a \.zip file archive that works on Lambda using Windows, we recommend installing the **build\-lambda\-zip** tool\.

**Note**  
If you have not already done so, you must install [git](https://git-scm.com/) and then add the `git` executable to your Windows `%PATH%` environment variable\.

To download the **build\-lambda\-zip** tool, run the following command:

```
go.exe get -u github.com/aws/aws-lambda-go/cmd/build-lambda-zip
```

Use the tool from your `GOPATH`\. If you have a default installation of Go, the tool is typically in `%USERPROFILE%\Go\bin`\. Otherwise, navigate to where you installed the Go runtime and do one of the following:

In cmd\.exe, run the following:

```
set GOOS=linux
go build -o main main.go
%USERPROFILE%\Go\bin\build-lambda-zip.exe -output main.zip main
```

In PowerShell, run the following:

```
$env:GOOS = "linux"
$env:CGO_ENABLED = "0"
$env:GOARCH = "amd64"
go build -o main main.go
~\Go\Bin\build-lambda-zip.exe -output main.zip main
```