# AWS Lambda Runtimes<a name="lambda-runtimes"></a>

AWS Lambda supports multiple languages through the use of runtimes\. You choose a runtime when you create a function, and you can change runtimes by updating your function's configuration\. The underlying execution environment provides additional libraries and [environment variables](lambda-environment-variables.md) that you can access from your function code\.

**Amazon Linux**
+ AMI – [amzn\-ami\-hvm\-2018\.03\.0\.20181129\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2018.03.0.20181129-x86_64-gp2)
+ Linux kernel – 4\.14\.114\-93\.126\.amzn2\.x86\_64 or 4\.14\.114\-83\.126\.amzn1\.x86\_64

**Note**  
Lambda is upgrading to Amazon Linux 2018\.03\. See [Upcoming updates to the AWS Lambda and AWS Lambda@Edge execution environment](https://aws.amazon.com/blogs/compute/upcoming-updates-to-the-aws-lambda-execution-environment/) for details\.

**Amazon Linux 2**
+ AMI – [amzn2\-ami\-hvm\-2\.0\.20190313\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn2-ami-hvm-2.0.20190313-x86_64-gp2)
+ Linux kernel – 4\.14\.114\-93\.126\.amzn2\.x86\_64

When your function is invoked, Lambda attempts to re\-use the execution environment from a previous invocation if one is available\. This saves time preparing the execution environment, and allows you to save resources like database connections and temporary files in the [execution context](running-lambda-code.md) to avoid creating them every time your function runs\.

A runtime can support a single version of a language, multiple versions of a language, or multiple languages\. Runtimes specific to a language or framework version are [deprecated](runtime-support-policy.md) when the version reaches end of life\.


**Node\.js Runtimes**  

| Name | Identifier | Node\.js Version | AWS SDK for JavaScript | Operating System | 
| --- | --- | --- | --- | --- | 
|  Node\.js 10  |  `nodejs10.x`  |  10\.15  |  2\.437\.0  |  Amazon Linux 2  | 
|  Node\.js 8\.10  |  `nodejs8.10`  |  8\.10  |  2\.290\.0  |  Amazon Linux  | 


**Python Runtimes**  

| Name | Identifier | AWS SDK for Python | Operating System | 
| --- | --- | --- | --- | 
|  Python 3\.6  |  `python3.6`  |  boto3\-1\.7\.74 botocore\-1\.10\.74  |  Amazon Linux  | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.9\.42 botocore\-1\.12\.42  |  Amazon Linux  | 
|  Python 2\.7  |  `python2.7`  |  N/A  |  Amazon Linux  | 


**Ruby Runtimes**  

| Name | Identifier | AWS SDK for Ruby | Operating System | 
| --- | --- | --- | --- | 
|  Ruby 2\.5  |  `ruby2.5`  |  3\.0\.1  |  Amazon Linux  | 


**Java Runtimes**  

| Name | Identifier | JDK | Operating System | 
| --- | --- | --- | --- | 
|  Java 8  |  `java8`  |  java\-1\.8\.0\-openjdk  |  Amazon Linux  | 


**Go Runtimes**  

| Name | Identifier | Operating System | 
| --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  | 


**\.NET Runtimes**  

| Name | Identifier | Languages | Operating System | 
| --- | --- | --- | --- | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  C\# PowerShell Core 6\.0  |  Amazon Linux  | 
|  \.NET Core 1\.0  |  `dotnetcore1.0`  |  C\#  |  Amazon Linux  | 

To use other languages in Lambda, you can implement a [custom runtime](runtimes-custom.md)\. The Lambda execution environment provides a [runtime interface](runtimes-api.md) for getting invocation events and sending responses\. You can deploy a custom runtime alongside your function code, or in a [layer](configuration-layers.md)\.

**Topics**
+ [Environment Variables Available to Lambda Functions](lambda-environment-variables.md)
+ [AWS Lambda Execution Context](running-lambda-code.md)
+ [Runtime Support Policy](runtime-support-policy.md)
+ [Custom AWS Lambda Runtimes](runtimes-custom.md)
+ [AWS Lambda Runtime Interface](runtimes-api.md)
+ [Tutorial – Publishing a Custom Runtime](runtimes-walkthrough.md)