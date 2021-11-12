# Lambda runtimes<a name="lambda-runtimes"></a>

Lambda supports multiple languages through the use of [runtimes](gettingstarted-concepts.md#gettingstarted-concepts-runtime)\. For a [function defined as a container image](configuration-images.md), you choose a runtime and the Linux distribution when you [create the container image](images-create.md)\. To change the runtime, you create a new container image\.

When you use a \.zip file archive for the deployment package, you choose a runtime when you create the function\. To change the runtime, you can [update your function's configuration](configuration-function-zip.md)\. The runtime is paired with one of the Amazon Linux distributions\. The underlying execution environment provides additional libraries and [environment variables](configuration-envvars.md) that you can access from your function code\.

**Amazon Linux**
+ Image – [amzn\-ami\-hvm\-2018\.03\.0\.20181129\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2018.03.0.20181129-x86_64-gp2)
+ Linux kernel – 4\.14\.171\-105\.231\.amzn1\.x86\_64

**Amazon Linux 2**
+ Image – Custom
+ Linux kernel – 4\.14\.165\-102\.205\.amzn2\.x86\_64

Lambda invokes your function in an [execution environment](runtimes-context.md)\. The execution environment provides a secure and isolated runtime environment that manages the resources required to run your function\. Lambda re\-uses the execution environment from a previous invocation if one is available, or it can create a new execution environment\. 

A runtime can support a single version of a language, multiple versions of a language, or multiple languages\. Runtimes specific to a language or framework version are [deprecated](runtime-support-policy.md) when the version reaches end of life\.


**Node\.js runtimes**  

| Name | Identifier | SDK for JavaScript | Operating system | Architectures | 
| --- | --- | --- | --- | --- | 
|  Node\.js 14  |  `nodejs14.x`  |  2\.1001\.0  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Node\.js 12  |  `nodejs12.x`  |  2\.1001\.0  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Node\.js 10  |  `nodejs10.x`  |  2\.1001\.0  |  Amazon Linux 2  |  x86\_64  | 

**Note**  
For end of support information about Node\.js 10, see [Runtime support policy](runtime-support-policy.md)\.


**Python runtimes**  

| Name | Identifier | AWS SDK for Python | Operating system | Architectures | 
| --- | --- | --- | --- | --- | 
|  Python 3\.9  |  `python3.9`  |  boto3\-1\.18\.55 botocore\-1\.21\.55  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Python 3\.8  |  `python3.8`  |  boto3\-1\.18\.55 botocore\-1\.21\.55  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.18\.55 botocore\-1\.21\.55  |  Amazon Linux  |  x86\_64  | 
|  Python 3\.6  |  `python3.6`  |  boto3\-1\.18\.55 botocore\-1\.21\.55  |  Amazon Linux  |  x86\_64  | 
|  Python 2\.7  |  `python2.7`  |  boto3\-1\.17\.100 botocore\-1\.20\.100  |  Amazon Linux  |  x86\_64  | 

**Important**  
Python 2\.7 reached end of life on January 1, 2020\. End of support \(phase 1\) for the Python 2\.7 runtime started on July 15, 2021\. For more information, see [Announcing end of support for Python 2\.7 in AWS Lambda](http://aws.amazon.com/blogs/compute/announcing-end-of-support-for-python-2-7-in-aws-lambda/) on the AWS Compute Blog\.


**Ruby runtimes**  

| Name | Identifier | SDK for Ruby | Operating system | Architectures | 
| --- | --- | --- | --- | --- | 
|  Ruby 2\.7  |  `ruby2.7`  |  3\.0\.1  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Ruby 2\.5  |  `ruby2.5`  |  3\.0\.1  |  Amazon Linux  |  x86\_64  | 

**Note**  
For end of support information about Ruby 2\.5, see [Runtime support policy](runtime-support-policy.md)\.


**Java runtimes**  

| Name | Identifier | JDK | Operating system | Architectures | 
| --- | --- | --- | --- | --- | 
|  Java 11  |  `java11`  |  amazon\-corretto\-11  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Java 8  |  `java8.al2`  |  amazon\-corretto\-8  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Java 8  |  `java8`  |  amazon\-corretto\-8  |  Amazon Linux  |  x86\_64  | 


**Go runtimes**  

| Name | Identifier | Operating system | Architectures | 
| --- | --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  |  x86\_64  | 

**Note**  
Runtimes that use the Amazon Linux operating system, such as Go 1\.x, do not support the arm64 architecture\. To use arm64 architecture, you can run Go with the provided\.al2 runtime\.


**\.NET runtimes**  

| Name | Identifier | Operating system | Architectures | 
| --- | --- | --- | --- | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  Amazon Linux  |  x86\_64  | 

**Note**  
For end of support information about \.NET Core 2\.1, see [Runtime support policy](runtime-support-policy.md)\.

To use other languages in Lambda, you can implement a [custom runtime](runtimes-custom.md)\. The Lambda execution environment provides a [runtime interface](runtimes-api.md) for getting invocation events and sending responses\. You can deploy a custom runtime alongside your function code, or in a [layer](configuration-layers.md)\.


**Custom runtime**  

| Name | Identifier | Operating system | Architectures | 
| --- | --- | --- | --- | 
|  Custom Runtime  |  `provided.al2`  |  Amazon Linux 2  |  x86\_64, arm64  | 
|  Custom Runtime  |  `provided`  |  Amazon Linux  |  x86\_64  | 

**Topics**
+ [Runtime support policy](runtime-support-policy.md)
+ [AWS Lambda execution environment](runtimes-context.md)
+ [Runtime support for Lambda container images](runtimes-images.md)
+ [AWS Lambda runtime API](runtimes-api.md)
+ [Lambda Extensions API](runtimes-extensions-api.md)
+ [Lambda Logs API](runtimes-logs-api.md)
+ [Modifying the runtime environment](runtimes-modify.md)
+ [Custom AWS Lambda runtimes](runtimes-custom.md)
+ [Tutorial – Publishing a custom runtime](runtimes-walkthrough.md)
+ [Using AVX2 vectorization in Lambda](runtimes-avx2.md)