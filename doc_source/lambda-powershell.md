# Building Lambda Functions with PowerShell<a name="lambda-powershell"></a>

The following sections explain how common programming patterns and core concepts apply when you author Lambda function code in PowerShell\.


**\.NET Runtimes**  

| Name | Identifier | Operating System | 
| --- | --- | --- | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |  Amazon Linux 2  | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  Amazon Linux  | 

Note that Lambda functions in PowerShell require PowerShell Core 6\.0\. Windows PowerShell isn't supported\.

Before you get started, you must first set up a PowerShell development environment\. For instructions on how to do this, see [Setting Up a PowerShell Development Environment](powershell-devenv.md)\.

To learn about how to use the AWSLambdaPSCore module to download sample PowerShell projects from templates, create PowerShell deployment packages, and deploy PowerShell functions to the AWS Cloud, see [AWS Lambda Deployment Package in PowerShell](powershell-package.md)\.

**Topics**
+ [Setting Up a PowerShell Development Environment](powershell-devenv.md)
+ [AWS Lambda Deployment Package in PowerShell](powershell-package.md)
+ [AWS Lambda Function Handler in PowerShell](powershell-handler.md)
+ [AWS Lambda Context Object in PowerShell](powershell-context.md)
+ [AWS Lambda Function Logging in PowerShell](powershell-logging.md)
+ [AWS Lambda Function Errors in PowerShell](powershell-exceptions.md)