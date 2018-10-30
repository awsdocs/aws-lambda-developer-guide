# The Context Object \(PowerShell\)<a name="powershell-context-object"></a>

You can gain useful information on how your Lambda function is interacting with the AWS Lambda runtime by using the predefined `$LambdaContext` variable\. This variable provides runtime details, such as the CloudWatch log stream that's associated with the function or the ID of the client that called your functions\.

The context object properties are:
+ `MemoryLimitInMB`: The memory limit, in MB, that you configured for the Lambda function\.
+ `FunctionName`: The name of the Lambda function that's running\.
+ `FunctionVersion`: The Lambda function version that's executing\. If an alias is used to invoke the function, then `FunctionVersion` is the version the alias points to\.
+ `InvokedFunctionArn`: The Amazon Resource Name \(ARN\) that's used to invoke this function\. It can be a function ARN or an alias ARN\. An unqualified ARN executes the `$LATEST` version, and aliases execute the function version that it's pointing to\. 
+  `AwsRequestId`: The AWS request ID that's associated with the request\. This is the ID returned to the client that invoked this Lambda function\. You can use the request ID for any follow\-up inquiry with AWS Support\. Note that if AWS Lambda retries the function \(for example, in a situation where the Lambda function that's processing Kinesis records throws an exception\), the request ID remains the same\.
+ `LogStreamName`: The CloudWatch log stream name for the particular Lambda function execution\. It can be null if the IAM user that's provided doesn't have permission for CloudWatch actions\.
+ `LogGroupName`: The CloudWatch log group name associated with the Lambda function that's invoked\. It can be null if the IAM user provided doesn't have permission for CloudWatch actions\.
+ `ClientContext`: Information about the client application and device when invoked through the AWS Mobile SDK\. It can be null\. Client context provides client information—such as the client ID, application title, version name, version code, and application package name\.
+  `Identity`: Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK\. It can be null\.
+ `RemainingTime`: The remaining execution time until the function is terminated\. When you create the Lambda function, you set the maximum time limit, which specifies when AWS Lambda terminates the function execution\. You can use information about the remaining time of function execution to specify function behavior when nearing the timeout\. This is a `TimeSpan` field\.
+ `Logger`: The Lambda logger that's associated with the ILambdaContext object\. For more information, see [Logging \(PowerShell\)](powershell-logging.md)\.

The following PowerShell code snippet shows a simple handler function that prints some of the context information\. 

```
#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.343.0'}
Write-Host 'Function name:' $LambdaContext.FunctionName
Write-Host 'Remaining milliseconds:' $LambdaContext.RemainingTime.TotalMilliseconds
Write-Host 'Log group name:' $LambdaContext.LogGroupName
Write-Host 'Log stream name:' $LambdaContext.LogStreamName
```