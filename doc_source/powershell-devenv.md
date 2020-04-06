# Setting Up a PowerShell Development Environment<a name="powershell-devenv"></a>

To set up your development environment for writing PowerShell scripts, do the following:

1. **Install the correct version of PowerShell** – Lambda's support for PowerShell is based on the cross\-platform PowerShell Core 6\.0 release\. This means that you can develop your PowerShell Lambda functions on Windows, Linux, or Mac\. If you don’t have this version of PowerShell installed, you can find instructions in [Installing PowerShell Core](https://docs.microsoft.com/en-us/powershell/scripting/install/installing-powershell#powershell-core)\.

1. **Install the \.NET Core 3\.1 SDK** – Because PowerShell Core is built on top of \.NET Core, the Lambda support for PowerShell uses the same \.NET Core 3\.1 Lambda runtime for both \.NET Core and PowerShell Lambda functions\. The \.NET Core 3\.1 SDK is used by the new Lambda PowerShell publishing cmdlets to create the Lambda deployment package\. The \.NET Core 3\.1 SDK is available at [\.NET downloads](https://www.microsoft.com/net/download) on the Microsoft website\. Be sure to install the SDK and not the runtime installation\.

1. **Install the AWSLambdaPSCore module** – You can install this either from the [ PowerShell Gallery](https://www.powershellgallery.com/packages/AWSLambdaPSCore), or you can install it by using the following PowerShell Core shell command:

   ```
   Install-Module AWSLambdaPSCore -Scope CurrentUser
   ```

1. **\(Optional\) Install AWS Tools for PowerShell** – You can install either the modularized [AWS\.Tools](https://docs.aws.amazon.com/powershell/latest/userguide/pstools-welcome.html#pwsh_structure_pstools) or single\-module [AWSPowerShell\.NetCore](https://docs.aws.amazon.com/powershell/latest/userguide/pstools-welcome.html#pwsh_structure_pscore) version in PowerShell Core 6\.0 to use the Lambda API within your PowerShell environment\\\. For instructions, see [Installing the AWS Tools for PowerShell](https://docs.aws.amazon.com/powershell/latest/userguide/pstools-getting-set-up.html)