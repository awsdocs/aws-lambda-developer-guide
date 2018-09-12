# Programming Model for Authoring Lambda Functions in PowerShell<a name="powershell-programming-model"></a>

The following sections explain how [common programming patterns and core concepts](http://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html) apply when authoring Lambda function code in PowerShell\.

Note that Lambda functions in PowerShell requires PowerShell Core 6\.0\. Windows PowerShell is not supported\.

Before you get started, you must first set up a PowerShell development environment\. For instructions on how to do this see [Setting Up a PowerShell Development Environment](lambda-powershell-setup-dev-environment.md)\.

To learn about using the AWSLambdaPSCore module to download sample PowerShell projects from templates, creating PowerShell deployment packages, and deploying PowerShell functions to the AWS Cloud, see [Using the AWSLambdaPSCore Module](lambda-powershell-how-to-create-deployment-package.md#lambda-powershell-using-lam-mod-deployment-package)\.

**Topics**
+ [PowerShell Lambda Invocation](powershell-programming-model-handler-types.md)
+ [The Context Object \(PowerShell\)](powershell-context-object.md)
+ [Logging \(PowerShell\)](powershell-logging.md)
+ [Function Errors \(PowerShell\)](powershell-exceptions.md)