# AWS Lambda runtimes<a name="lambda-runtimes"></a>

AWS Lambda supports multiple languages through the use of runtimes\. You choose a runtime when you create a function, and you can change runtimes by updating your function's configuration\. The underlying execution environment provides additional libraries and [environment variables](configuration-envvars.md) that you can access from your function code\.

**Amazon Linux**
+ Image – [amzn\-ami\-hvm\-2018\.03\.0\.20181129\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2018.03.0.20181129-x86_64-gp2)
+ Linux kernel – 4\.14\.171\-105\.231\.amzn1\.x86\_64

**Amazon Linux 2**
+ Image – Custom
+ Linux kernel – 4\.14\.165\-102\.205\.amzn2\.x86\_64

When your function is invoked, Lambda attempts to re\-use the execution environment from a previous invocation if one is available\. This saves time preparing the execution environment, and allows you to save resources like database connections and temporary files in the [execution context](runtimes-context.md) to avoid creating them every time your function runs\.

A runtime can support a single version of a language, multiple versions of a language, or multiple languages\. Runtimes specific to a language or framework version are [deprecated](runtime-support-policy.md) when the version reaches end of life\.


**Node\.js runtimes**  

| Name | Identifier | AWS SDK for JavaScript | Operating system | 
| --- | --- | --- | --- | 
|  Node\.js 12  |  `nodejs12.x`  |  2\.712\.0  |  Amazon Linux 2  | 
|  Node\.js 10  |  `nodejs10.x`  |  2\.712\.0  |  Amazon Linux 2  | 


**Python runtimes**  

| Name | Identifier | AWS SDK for Python | Operating system | 
| --- | --- | --- | --- | 
|  Python 3\.8  |  `python3.8`  |  boto3\-1\.14\.17 botocore\-1\.17\.17  |  Amazon Linux 2  | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.14\.17 botocore\-1\.17\.17  |  Amazon Linux  | 
|  Python 3\.6  |  `python3.6`  |  boto3\-1\.14\.17 botocore\-1\.17\.17  |  Amazon Linux  | 
|  Python 2\.7  |  `python2.7`  |  boto3\-1\.14\.17 botocore\-1\.17\.17  |  Amazon Linux  | 


**Ruby runtimes**  

| Name | Identifier | AWS SDK for Ruby | Operating system | 
| --- | --- | --- | --- | 
|  Ruby 2\.7  |  `ruby2.7`  |  3\.0\.1  |  Amazon Linux 2  | 
|  Ruby 2\.5  |  `ruby2.5`  |  3\.0\.1  |  Amazon Linux  | 


**Java runtimes**  

| Name | Identifier | JDK | Operating system | 
| --- | --- | --- | --- | 
|  Java 11  |  `java11`  |  amazon\-corretto\-11  |  Amazon Linux 2  | 
|  Java 8  |  `java8.al2`  |  amazon\-corretto\-8  |  Amazon Linux 2  | 
|  Java 8  |  `java8`  |  java\-1\.8\.0\-openjdk  |  Amazon Linux  | 


**Go runtimes**  

| Name | Identifier | Operating system | 
| --- | --- | --- | 
|  Go 1\.x  |  `go1.x`  |  Amazon Linux  | 


**\.NET runtimes**  

| Name | Identifier | Operating system | 
| --- | --- | --- | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |  Amazon Linux 2  | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  Amazon Linux  | 

To use other languages in Lambda, you can implement a [custom runtime](runtimes-custom.md)\. The Lambda execution environment provides a [runtime interface](runtimes-api.md) for getting invocation events and sending responses\. You can deploy a custom runtime alongside your function code, or in a [layer](configuration-layers.md)\.


**Custom runtime**  

| Name | Identifier | Operating system | 
| --- | --- | --- | 
|  Custom Runtime  |  `provided.al2`  |  Amazon Linux 2  | 
|  Custom Runtime  |  `provided`  |  Amazon Linux  | 

**Topics**
+ [AWS Lambda execution context](runtimes-context.md)
+ [Runtime support policy](runtime-support-policy.md)
+ [Custom AWS Lambda runtimes](runtimes-custom.md)
+ [AWS Lambda runtime interface](runtimes-api.md)
+ [Tutorial – Publishing a custom runtime](runtimes-walkthrough.md)