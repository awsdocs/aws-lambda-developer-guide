# PowerShell Lambda Invocation<a name="powershell-programming-model-handler-types"></a>

When a Lambda function is invoked, the Lambda handler invokes the PowerShell script\.

When the PowerShell script is invoked, the following variables are predefined:
+  *$LambdaInput* – A PSObject that contains the input to the handler\. This input can be event data \(published by an event source\) or custom input that you provide, such as a string or any custom data object\. 
+  *$LambdaContext* – An Amazon\.Lambda\.Core\.ILambdaContext object that you can use to access information about the current execution—such as the name of the current function, the memory limit, execution time remaining, and logging\. 

For example, consider the following PowerShell example code\.

```
#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.343.0'}
Write-Host 'Function Name:' $LambdaContext.FunctionName
```

This script returns the FunctionName property that's obtained from the $LambdaContext variable\.

**Note**  
You're required to use the `#Requires` statement within your PowerShell scripts to indicate the modules that your scripts depend on\. This statement performs two important tasks\. 1\) It communicates to other developers which modules the script uses, and 2\) it identifies the dependent modules that AWS PowerShell tools need to package with the script, as part of the deployment\. For more information about the `#Requires` statement in PowerShell, see [ About Requires](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_requires?view=powershell-6)\. For more information about PowerShell deployment packages, see [Creating a Deployment Package \(PowerShell\)](lambda-powershell-how-to-create-deployment-package.md)\.  
When your PowerShell Lambda function uses the AWS PowerShell cmdlets, be sure to set a `#Requires` statement that references the `AWSPowerShell.NetCore` module, which supports PowerShell Core—and not the `AWSPowerShell` module, which only supports Windows PowerShell\. Also, be sure to use version 3\.3\.270\.0 or newer of `AWSPowerShell.NetCore` which optimizes the cmdlet import process\. If you use an older version, you'll experience longer cold starts\. For more information, see [ AWS Tools for PowerShell](https://aws.amazon.com/documentation/powershell/?id=docs_gateway)\.

## Returning Data<a name="powershell-programming-model-returning-data"></a>

Some Lambda invocations are meant to return data back to their caller\. For example, if an invocation was in response to a web request coming from API Gateway, then our Lambda function needs to return back the response\. For PowerShell Lambda, the last object that's added to the PowerShell pipeline is the return data from the Lambda invocation\. If the object is a string, the data is returned as is\. Otherwise the object is converted to JSON by using the `ConvertTo-Json` cmdlet\.

For example, consider the following PowerShell statement, which adds `$PSVersionTable` to the PowerShell pipeline:

```
$PSVersionTable
```

After the PowerShell script is finished, the last object in the PowerShell pipeline is the return data for the Lambda function\. `$PSVersionTable` is a PowerShell global variable that also provides information about the running environment\.