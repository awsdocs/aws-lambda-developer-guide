# Building Lambda functions with PowerShell<a name="lambda-powershell"></a>

The following sections explain how common programming patterns and core concepts apply when you author Lambda function code in PowerShell\.


**\.NET**  

| Name | Identifier | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | 
|  \.NET Core 3\.1  |  `dotnetcore3.1`  |  Amazon Linux 2  |  x86\_64, arm64  |  Mar 31, 2023  | 
|  \.NET 6  |  `dotnet6`  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  \.NET 5  |  `dotnet5.0`  |  Amazon Linux 2  |  x86\_64  |    | 

Lambda provides the following sample applications for the PowerShell runtime:
+ [blank\-powershell](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-powershell) – A PowerShell function that shows the use of logging, environment variables, and the AWS SDK\.

Before you get started, you must first set up a PowerShell development environment\. For instructions on how to do this, see [Setting Up a PowerShell Development Environment](powershell-devenv.md)\.

To learn about how to use the AWSLambdaPSCore module to download sample PowerShell projects from templates, create PowerShell deployment packages, and deploy PowerShell functions to the AWS Cloud, see [Deploy PowerShell Lambda functions with \.zip file archives](powershell-package.md)\.

**Topics**
+ [Setting Up a PowerShell Development Environment](powershell-devenv.md)
+ [Deploy PowerShell Lambda functions with \.zip file archives](powershell-package.md)
+ [AWS Lambda function handler in PowerShell](powershell-handler.md)
+ [AWS Lambda context object in PowerShell](powershell-context.md)
+ [AWS Lambda function logging in PowerShell](powershell-logging.md)
+ [AWS Lambda function errors in PowerShell](powershell-exceptions.md)