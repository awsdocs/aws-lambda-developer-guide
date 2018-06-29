# Creating a Deployment Package \(Go\)<a name="lambda-go-how-to-create-deployment-package"></a>

To create a Lambda function you first create a Lambda function deployment package, a \.zip file consisting of your code and any dependencies\. 

After you create a deployment package, you may either upload it directly or upload the \.zip file first to an Amazon S3 bucket in the same AWS region where you want to create the Lambda function, and then specify the bucket name and object key name when you create the Lambda function using the console or the AWS CLI\.

 For Lambda functions written in Go, download the Lambda library for Go by navigating to the Go runtime directory and enter the following command:  `go get github.com/aws/aws-lambda-go/lambda` 

Then use following command to build, package and deploy a Go Lambda function via the CLI\. Note that your *function\-name *must match the name of your *Lambda handler* name\. 

```
GOOS=linux go build lambda_handler.go
zip handler.zip ./lambda_handler
# --handler is the path to the executable inside the .zip
aws lambda create-function \
  --region region \
  --function-name lambda-handler \
  --memory 128 \
  --role arn:aws:iam::account-id:role/execution_role \
  --runtime go1.x \
  --zip-file fileb://path-to-your-zip-file/handler.zip \
  --handler lambda-handler
```

**Note**  
If you are using a non\-Linux environment, such as Windows or macOS, ensure that your handler function is compatible with the Lambda [Execution Context](http://docs.aws.amazon.com/lambda/latest/dg/running-lambda-code.html) by setting the `GOOS `\(Go Operating System\) environment variable to 'linux' when compiling your handler function code\.

## Creating a Deployment Package on Windows<a name="lambda-go-how-to-create-deployment-package-windows"></a>

To create a \.zip that will work on AWS Lambda using Windows, we recommend installing the **build\-lambda\-zip** tool\.

**Note**  
If you have not already done so, you will need to install [git](https://git-scm.com/) and then add the `git` executable to your Windows `%PATH%` environment variable\.

To download the tool, run the following command:

```
go.exe get -u github.com/aws/aws-lambda-go/cmd/build-lambda-zip
```

Use the tool from your `GOPATH`\. If you have a default installation of Go, the tool will typically be in `%USERPROFILE%\Go\bin`\. Otherwise, navigate to where you installed the Go runtime and do the following:

In cmd\.exe, run the following:

```
set GOOS=linux
go build -o main main.go
%USERPROFILE%\Go\bin\build-lambda-zip.exe -o main.zip main
```

In Powershell, run the following:

```
$env:GOOS = "linux"
go build -o main main.go
~\Go\Bin\build-lambda-zip.exe -o main.zip main
```