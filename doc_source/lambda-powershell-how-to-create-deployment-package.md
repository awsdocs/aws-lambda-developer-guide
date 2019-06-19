# AWS Lambda Deployment Package in PowerShell<a name="lambda-powershell-how-to-create-deployment-package"></a>

A PowerShell Lambda deployment package is a ZIP file that contains your PowerShell script, PowerShell modules that are required for your PowerShell script, and the assemblies needed to host PowerShell Core\.

**AWSLambdaPSCore** is a PowerShell module that you can install from the [ PowerShell Gallery](https://www.powershellgallery.com/packages/AWSLambdaPSCore)\. You use this module to create your PowerShell Lambda deployment package\.

You're required to use the `#Requires` statement within your PowerShell scripts to indicate the modules that your scripts depend on\. This statement performs two important tasks\. 1\) It communicates to other developers which modules the script uses, and 2\) it identifies the dependent modules that AWS PowerShell tools need to package with the script, as part of the deployment\. For more information about the `#Requires` statement in PowerShell, see [ About Requires](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_requires?view=powershell-6)\. For more information about PowerShell deployment packages, see [AWS Lambda Deployment Package in PowerShell](#lambda-powershell-how-to-create-deployment-package)\.

When your PowerShell Lambda function uses the AWS PowerShell cmdlets, be sure to set a `#Requires` statement that references the `AWSPowerShell.NetCore` module, which supports PowerShell Core—and not the `AWSPowerShell` module, which only supports Windows PowerShell\. Also, be sure to use version 3\.3\.270\.0 or newer of `AWSPowerShell.NetCore`, which optimizes the cmdlet import process\. If you use an older version, you'll experience longer cold starts\. For more information, see [ AWS Tools for PowerShell](https://aws.amazon.com/documentation/powershell/?id=docs_gateway)\.

Before you get started, you must first set up a PowerShell development environment\. For instructions on how to do this, see [Setting Up a PowerShell Development Environment](lambda-powershell-setup-dev-environment.md)\.

## Using the AWSLambdaPSCore Module<a name="lambda-powershell-using-lam-mod-deployment-package"></a>

The AWSLambdaPSCore module has the following new cmdlets to help author and publish PowerShell Lambda functions\.


****  

| Cmdlet Name | Description | 
| --- | --- | 
| Get‑AWSPowerShellLambdaTemplate | Returns a list of getting started templates\. | 
| New‑AWSPowerShellLambda | Creates an initial PowerShell script based on a template\. | 
| Publish‑AWSPowerShellLambda | Publishes a given PowerShell script to Lambda\. | 
| New‑AWSPowerShellLambdaPackage | Creates a Lambda deployment package that can be used in a CI/CD system for deployment\. | 

To help get started writing and invoking a PowerShell script with Lambda, you can use the `New-AWSPowerShellLambda` cmdlet to create a starter script based on a template\. You can use the `Publish-AWSPowerShellLambda` cmdlet to deploy your script to AWS Lambda\. Then you can test your script either through the command line or the console\.

To create a new PowerShell script, upload it, and test it, follow this procedure:

1. **View available templates\.**

   Run the following command to view the list of available templates:

   ```
   PS C:\> Get-AWSPowerShellLambdaTemplate
   
   Template               Description
   --------               -----------
   Basic                  Bare bones script
   CodeCommitTrigger      Script to process AWS CodeCommit Triggers
   DetectLabels           Use Amazon Rekognition service to tag image files in Amazon S3 with detected labels.
   KinesisStreamProcessor Script to process an Amazon Kinesis stream
   S3Event                Script to process S3 events
   SNSSubscription        Script to be subscribed to an Amazon SNS topic
   SQSQueueProcessor      Script to be subscribed to an Amazon SQS queue
   ```

   Note that new templates are being listed all the time, so this output is just an example\.

1. **Create a basic script\.**

   Run the following command to create a sample script based on the `Basic` template:

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
                   
   #Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.343.0'}
   
   # Uncomment to send the input to CloudWatch Logs
   # Write-Host (ConvertTo-Json -InputObject $LambdaInput -Compress -Depth 5)
   ```

1. **Edit the sample script\.**

   To see how log messages from your PowerShell script are sent to CloudWatch Logs, uncomment the `Write-Host` line of the sample script\.

   To demonstrate how you can return data back from your Lambda functions, add a new line at the end of the script with `$PSVersionTable`\. This adds the `$PSVersionTable` to the PowerShell pipeline\. After the PowerShell script is complete, the last object in the PowerShell pipeline is the return data for the Lambda function\. `$PSVersionTable` is a PowerShell global variable that also provides information about the running environment\.

   After making these changes, the last two lines of the sample script look like this:

   ```
   Write-Host (ConvertTo-Json -InputObject $LambdaInput -Compress -Depth 5)
   $PSVersionTable
   ```

1. **Publish to AWS Lambda\.**

   After editing the `MyFirstPSScript.ps1` file, change the directory to the script's location\. Then run the following command to publish the script to AWS Lambda:

   ```
   Publish-AWSPowerShellLambda -ScriptPath .\MyFirstPSScript.ps1 -Name  MyFirstPSScript -Region us-east-1
   ```

   Note that the `-Name` parameter specifies the Lambda function name, which appears in the Lambda console\. You can use this function to invoke your script manually\.

1. **Test the Lambda function\.**

   You can test the PowerShell Lambda function that you just published by using the **dotnet** CLI from a command prompt\. Use the `lambda invoke-function` command to invoke your function\.

   ```
   > dotnet lambda invoke-function MyFirstPSScript
   ```

   For more information about the dotnet CLI extension, see [\.NET Core CLI](lambda-dotnet-coreclr-deployment-package.md)\.