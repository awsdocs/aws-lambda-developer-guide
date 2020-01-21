# AWS Lambda Runtimes<a name="lambda-runtimes"></a>

AWS Lambda supports multiple languages through the use of runtimes\. You choose a runtime when you create a function, and you can change runtimes by updating your function's configuration\. The underlying execution environment provides additional libraries and [environment variables](configuration-envvars.md) that you can access from your function code\.

**Amazon Linux**
+ AMI – [amzn\-ami\-hvm\-2018\.03\.0\.20181129\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2018.03.0.20181129-x86_64-gp2)
+ Linux kernel – 4\.14\.154\-99\.181\.amzn1\.x86\_64

**Amazon Linux 2**
+ AMI – [amzn2\-ami\-hvm\-2\.0\.20190313\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn2-ami-hvm-2.0.20190313-x86_64-gp2)
+ Linux kernel – 4\.14\.138\-99\.102\.amzn2\.x86\_64

If you are providing a custom runtime and would like to use Amazon Linux 2 rather than Amazon Linux add `
arn:aws:lambda:::awslayer:AmazonLinux2` to your function's layers.  By default all custom runtimes will use Amazon Linux.

When your function is invoked, Lambda attempts to re\-use the execution environment from a previous invocation if one is available\. This saves time preparing the execution environment, and allows you to save resources like database connections and temporary files in the [execution context](running-lambda-code.md) to avoid creating them every time your function runs\.

A runtime can support a single version of a language, multiple versions of a language, or multiple languages\. Runtimes specific to a language or framework version are [deprecated](runtime-support-policy.md) when the version reaches end of life\.


**Node\.js Runtimes**  

| Name | Identifier | AWS SDK for JavaScript | Operating System | 
| --- | --- | --- | --- | 
|  Node\.js 12  |  `nodejs12.x`  |  2\.536\.0  |  Amazon Linux 2  | 
|  Node\.js 10  |  `nodejs10.x`  |  2\.488\.0  |  Amazon Linux 2  | 


**Python Runtimes**  

| Name | Identifier | AWS SDK for Python | Operating System | 
| --- | --- | --- | --- | 
|  Python 3\.8  |  `python3.8`  |  boto3\-1\.10\.2 botocore\-1\.13\.2  |  Amazon Linux 2  | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.9\.221 botocore\-1\.12\.221  |  Amazon Linux  | 
|  Python 3\.6  |  `python3.6`  |  boto3\-1\.9\.221 botocore\-1\.12\.221  |  Amazon Linux  | 
|  Python 2\.7  |  `python2.7`  |  boto3\-1\.9\.221 botocore\-1\.12\.221  |  Amazon Linux  | 


**Ruby Runtimes**  

| Name | Identifier | AWS SDK for Ruby | Operating System | 
| --- | --- | --- | --- | 
|  Ruby 2\.5  |  `ruby2.5`  |  3\.0\.1  |  Amazon Linux  | 


**Java Runtimes**  

| Name | Identifier | JDK | Operating System | 
| --- | --- | --- | --- | 
|  Java 11  |  `java11`  |  amazon\-corretto\-11  |  Amazon Linux 2  | 
|  Java 8  |  `java8`  |  java\-1\.8\.0\-openjdk  |  Amazon Linux  | 


**Go Runtimes**  

| Name | Identifier | Operating System | 
| --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  | 


**\.NET Runtimes**  

| Name | Identifier | Languages | Operating System | 
| --- | --- | --- | --- | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  C\# PowerShell Core 6\.0  |  Amazon Linux  | 

To use other languages in Lambda, you can implement a [custom runtime](runtimes-custom.md)\. The Lambda execution environment provides a [runtime interface](runtimes-api.md) for getting invocation events and sending responses\. You can deploy a custom runtime alongside your function code, or in a [layer](configuration-layers.md)\.

**Topics**
+ [AWS Lambda Execution Context](running-lambda-code.md)
+ [Runtime Support Policy](runtime-support-policy.md)
+ [Custom AWS Lambda Runtimes](runtimes-custom.md)
+ [AWS Lambda Runtime Interface](runtimes-api.md)
+ [Tutorial – Publishing a Custom Runtime](runtimes-walkthrough.md)
