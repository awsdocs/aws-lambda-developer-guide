# Programming Model for Authoring Lambda Functions in PowerShell<a name="powershell-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when you author Lambda function code in PowerShell\.

Note that Lambda functions in PowerShell require PowerShell Core 6\.0\. Windows PowerShell isn't supported\.

Before you get started, you must first set up a PowerShell development environment\. For instructions on how to do this, see [Setting Up a PowerShell Development Environment](lambda-powershell-setup-dev-environment.md)\.

To learn about how to use the AWSLambdaPSCore module to download sample PowerShell projects from templates, create PowerShell deployment packages, and deploy PowerShell functions to the AWS Cloud, see [Using the AWSLambdaPSCore Module](lambda-powershell-how-to-create-deployment-package.md#lambda-powershell-using-lam-mod-deployment-package)\.

**Topics**
+ [PowerShell Lambda Invocation](powershell-programming-model-handler-types.md)
+ [The Context Object \(PowerShell\)](powershell-context-object.md)
+ [Logging \(PowerShell\)](powershell-logging.md)
+ [Function Errors \(PowerShell\)](powershell-exceptions.md)