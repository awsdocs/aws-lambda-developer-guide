# AWS Lambda Runtimes<a name="lambda-runtimes"></a>

AWS Lambda supports multiple languages through the use of runtimes\. You choose a runtime when you create a function, and you can change runtimes by updating your function's configuration\. The underlying [execution environment](current-supported-versions.md), which is shared by all runtimes, provides additional libraries and environment variables that you can access from your function code\.

When your function is invoked, Lambda attempts to re\-use the execution environment from a previous invocation if one is available\. This saves time preparing the execution environment, and allows you to save resources like database connections and temporary files in the [execution context](running-lambda-code.md) to avoid creating them every time your function runs\.

A runtime can support a single version of a language, multiple versions of a language, or multiple languages\. Runtimes specific to a language or framework version are [deprecated](runtime-support-policy.md) when the version reaches end of life\.


**Node\.js Runtimes**  

| Name  | Identifier  | 
| --- | --- | 
|  Node\.js 8\.10  |  `nodejs8.10`  | 
|  Node\.js 6\.10  |  `nodejs6.10`  | 


**Java Runtimes**  

| Name | Identifier | JDK | 
| --- | --- | --- | 
|  Java 8  |  `java8`  |  java\-1\.8\.0\-openjdk  | 


**Python Runtimes**  

| Name | Identifier | 
| --- | --- | 
|  Python 3\.6  |  `python3.6`  | 
|  Python 3\.7  |  `python3.7`  | 
|  Python 2\.7  |  `python2.7`  | 


**Go Runtimes**  

| Name | Identifier | 
| --- | --- | 
|  Go 1\.x  |  `go1.x`  | 


**\.NET Runtimes**  

| Name | Identifier | Languages | 
| --- | --- | --- | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  C\# PowerShell Core 6\.0  | 
|  \.NET Core 2\.0  |  `dotnetcore2.0`  |  C\#  | 
|  \.NET Core 1\.0  |  `dotnetcore1.0`  |  C\#  | 

**Topics**
+ [Lambda Execution Environment and Available Libraries](current-supported-versions.md)
+ [AWS Lambda Execution Context](running-lambda-code.md)
+ [Runtime Support Policy](runtime-support-policy.md)
+ [Custom AWS Lambda Runtimes](runtimes-custom.md)
+ [AWS Lambda Runtime Interface](runtimes-api.md)
+ [Tutorial – Publishing a Custom Runtime](runtimes-walkthrough.md)