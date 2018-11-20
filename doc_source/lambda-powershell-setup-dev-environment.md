# Setting Up a PowerShell Development Environment<a name="lambda-powershell-setup-dev-environment"></a>

To set up your development environment for writing PowerShell scripts, do the following:

1. **Install the correct version of PowerShell**\. Lambda's support for PowerShell is based on the cross\-platform PowerShell Core 6\.0 release\. This means that you can develop your PowerShell Lambda functions on Windows, Linux, or Mac\. If you don’t have this version of PowerShell installed, you can find instructions in [Installing PowerShell Core](https://docs.microsoft.com/en-us/powershell/scripting/setup/installing-powershell)\.

1. **Install the \.NET Core 2\.1 SDK**\. Because PowerShell Core is built on top of \.NET Core, the Lambda support for PowerShell uses the same \.NET Core 2\.1 Lambda runtime for both \.NET Core and PowerShell Lambda functions\. The \.NET Core 2\.1 SDK is used by the new Lambda PowerShell publishing cmdlets to create the Lambda deployment package\. The \.NET Core 2\.1 SDK is available at [\.NET downloads](https://www.microsoft.com/net/download) on the Microsoft website\. Be sure to install the SDK and not the runtime installation\.

1. **Install the AWSLambdaPSCore module**\. You can install this either from the [ PowerShell Gallery](https://www.powershellgallery.com/packages/AWSLambdaPSCore), or you can install it by using the following PowerShell Core shell command:

   ```
   Install-Module AWSLambdaPSCore -Scope CurrentUser
   ```

## Next Steps<a name="lambda-powershell-setup-dev-environment-next-steps"></a>
+ To learn about writing Lambda functions in PowerShell, see [Building Lambda Functions with PowerShell](powershell-programming-model.md)\.
+ To learn about using the AWSLambdaPSCore module to download sample PowerShell projects from templates, creating PowerShell deployment packages, and deploying PowerShell functions to the AWS Cloud, see [Using the AWSLambdaPSCore Module](lambda-powershell-how-to-create-deployment-package.md#lambda-powershell-using-lam-mod-deployment-package)\.