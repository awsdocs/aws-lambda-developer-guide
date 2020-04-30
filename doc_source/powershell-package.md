# AWS Lambda deployment package in PowerShell<a name="powershell-package"></a>

A PowerShell Lambda deployment package is a ZIP file that contains your PowerShell script, PowerShell modules that are required for your PowerShell script, and the assemblies needed to host PowerShell Core\.

The AWSLambdaPSCore module has the following new cmdlets to help author and publish PowerShell Lambda functions\.

**AWSLambdaPSCore cmdlets**
+ **Get\-AWSPowerShellLambdaTemplate** – Returns a list of getting started templates\.
+ **New\-AWSPowerShellLambda** – Creates an initial PowerShell script based on a template\.
+ **Publish\-AWSPowerShellLambda** – Publishes a given PowerShell script to Lambda\.
+ **New\-AWSPowerShellLambdaPackage** – Creates a Lambda deployment package that can be used in a CI/CD system for deployment\.

To help get started writing and invoking a PowerShell script with Lambda, you can use the `New-AWSPowerShellLambda` cmdlet to create a starter script based on a template\. You can use the `Publish-AWSPowerShellLambda` cmdlet to deploy your script to AWS Lambda\. Then you can test your script either through the command line or the console\.

To create a new PowerShell script, upload it, and test it, follow this procedure:

1. Run the following command to view the list of available templates:

   ```
   PS C:\> Get-AWSPowerShellLambdaTemplate
   
   Template               Description
   --------               -----------
   Basic                  Bare bones script
   CodeCommitTrigger      Script to process AWS CodeCommit Triggers
   ...
   ```

1. Run the following command to create a sample script based on the `Basic` template:

   ```
   New-AWSPowerShellLambda -ScriptName MyFirstPSScript -Template Basic
   ```

   A new file named `MyFirstPSScript.ps1` is created in a new subdirectory of the current directory\. The name of the directory is based on the `-ScriptName` parameter\. You can use the `-Directory` parameter to choose an alternative directory\.

   You can see that the new file has the following contents:

   ```
   # PowerShell script file to be executed as a AWS Lambda function. 
   # 
   # When executing in Lambda the following variables will be predefined.
   #   $LambdaInput - A PSObject that contains the Lambda function input data.
   #   $LambdaContext - An Amazon.Lambda.Core.ILambdaContext object that contains information about the currently running Lambda environment.
   #
   # The last item in the PowerShell pipeline will be returned as the result of the Lambda function.
   #
   # To include PowerShell modules with your Lambda function, like the AWSPowerShell.NetCore module, add a "#Requires" statement 
   # indicating the module and version.
                   
   #Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.618.0'}
   
   # Uncomment to send the input to CloudWatch Logs
   # Write-Host (ConvertTo-Json -InputObject $LambdaInput -Compress -Depth 5)
   ```

1. To see how log messages from your PowerShell script are sent to CloudWatch Logs, uncomment the `Write-Host` line of the sample script\.

   To demonstrate how you can return data back from your Lambda functions, add a new line at the end of the script with `$PSVersionTable`\. This adds the `$PSVersionTable` to the PowerShell pipeline\. After the PowerShell script is complete, the last object in the PowerShell pipeline is the return data for the Lambda function\. `$PSVersionTable` is a PowerShell global variable that also provides information about the running environment\.

   After making these changes, the last two lines of the sample script look like this:

   ```
   Write-Host (ConvertTo-Json -InputObject $LambdaInput -Compress -Depth 5)
   $PSVersionTable
   ```

1. After editing the `MyFirstPSScript.ps1` file, change the directory to the script's location\. Then run the following command to publish the script to AWS Lambda:

   ```
   Publish-AWSPowerShellLambda -ScriptPath .\MyFirstPSScript.ps1 -Name  MyFirstPSScript -Region us-east-2
   ```

   Note that the `-Name` parameter specifies the Lambda function name, which appears in the Lambda console\. You can use this function to invoke your script manually\.

1. Invoke your function with the AWS CLI `invoke` command\.

   ```
   > aws lambda invoke --function-name MyFirstPSScript out
   ```