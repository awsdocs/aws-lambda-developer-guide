# PowerShell Lambda Invocation<a name="powershell-programming-model-handler-types"></a>

When a Lambda function is invoked, the Lambda handler invokes the PowerShell script\.

When the PowerShell script is invoked, the following variables are predefined:
+  *$LambdaInput* – A PSObject that contains the input to the handler, which can be event data \(published by an event source\) or custom input that you provide such as a string or any custom data object\. 
+  *$LambdaContext* – An Amazon\.Lambda\.Core\.ILambdaContext object you can use to access information about the current execution, such as the name of the current function, the memory limit, execution time remaining, and logging\. 

For example, consider the following PowerShell example code\.

```
#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.343.0'}
Write-Host 'Function Name:' $LambdaContext.FunctionName
```

This script returns the FunctionName property obtained from the $LambdaContext variable\.

**Note**  
You are required to use the `#Requires` statement within your PowerShell scripts to indicate the modules your scripts depend on\. This statement performs two important tasks: 1\) Communicates to other developers which modules the script uses, and 2\) Identifies the dependent modules that AWS PowerShell tools need to package up with the script as part of the deployment\. For more information about the `#Requires` statement in PowerShell see [ About Requires](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_requires?view=powershell-6)\. For more information about PowerShell deployment packages, see [Creating a Deployment Package \(PowerShell\)](lambda-powershell-how-to-create-deployment-package.md)\.  
When your PowerShell Lambda function leverages the AWS PowerShell cmdlets, be sure to set a `#Requires` statement referencing the `AWSPowerShell.NetCore` module which supports PowerShell Core, and not the `AWSPowerShell` module which only supports Windows PowerShell\. Also, be sure to use version 3\.3\.270\.0 or newer of `AWSPowerShell.NetCore` which optimized the cmdlet import process\. If you use an older versions you will experience longer cold starts\. For more information about AWS Tools for PowerShell see [ AWS Tools for PowerShell](https://aws.amazon.com/documentation/powershell/?id=docs_gateway)\.

## Returning Data<a name="powershell-programming-model-returning-data"></a>

Some Lambda invocations are meant to return data back to their caller\. For example if an invocation was in response to a web request coming from API Gateway then our Lambda function needs to return back the response\. For PowerShell Lambda the last object added to the PowerShell pipeline will be the return data from the Lambda invocation\. If the object is a string the data will be returned as is\. Otherwise the object will be converted to JSON using the `ConvertTo-Json` cmdlet\.

For example, consider the following PowerShell statement, which adds `$PSVersionTable` to the PowerShell pipeline:

```
$PSVersionTable
```

Once the PowerShell script is complete, the last object in the PowerShell pipeline will be the return data for the Lambda function\. `$PSVersionTable` is a PowerShell global variable that will also provide information about the running environment\.