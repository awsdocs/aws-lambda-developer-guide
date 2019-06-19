# Building Lambda Functions with PowerShell<a name="powershell-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when you author Lambda function code in PowerShell\.


**\.NET Runtimes**  

| Name | Identifier | Languages | Operating System | 
| --- | --- | --- | --- | 
|  \.NET Core 2\.1  |  `dotnetcore2.1`  |  C\# PowerShell Core 6\.0  |  Amazon Linux  | 
|  \.NET Core 1\.0  |  `dotnetcore1.0`  |  C\#  |  Amazon Linux  | 

Note that Lambda functions in PowerShell require PowerShell Core 6\.0\. Windows PowerShell isn't supported\.

Before you get started, you must first set up a PowerShell development environment\. For instructions on how to do this, see [Setting Up a PowerShell Development Environment](lambda-powershell-setup-dev-environment.md)\.

To learn about how to use the AWSLambdaPSCore module to download sample PowerShell projects from templates, create PowerShell deployment packages, and deploy PowerShell functions to the AWS Cloud, see [Using the AWSLambdaPSCore Module](lambda-powershell-how-to-create-deployment-package.md#lambda-powershell-using-lam-mod-deployment-package)\.

**Topics**
+ [AWS Lambda Deployment Package in PowerShell](lambda-powershell-how-to-create-deployment-package.md)
+ [AWS Lambda Function Handler in PowerShell](powershell-programming-model-handler-types.md)
+ [AWS Lambda Context Object in PowerShell](powershell-context-object.md)
+ [AWS Lambda Function Logging in PowerShell](powershell-logging.md)
+ [AWS Lambda Function Errors in PowerShell](powershell-exceptions.md)