# Logging \(PowerShell\)<a name="powershell-logging"></a>

Your Lambda function can contain logging statements and, in turn, AWS Lambda writes these logs to CloudWatch Logs\. 

In the PowerShell Lambda programming model, the `Write` cmdlets like `Write-Host`, `Write-Output`, and `Write-Information` are all written to CloudWatch Logs\.

For example, the following example code writes a message to CloudWatch Logs:

```
Write-Host 'Hello World. This string is written to CloudWatch logs.'
```

## How to Find Logs<a name="how-to-find-logs-powershell"></a>

You can find the logs that your Lambda function writes, as follows:
+ Find the logs in CloudWatch Logs\. The `$LambdaContext` variable provides the `LogGroupName` and the `LogStreamName` properties\. Using these properties, you can find the specific log stream where the logs are written\.
+ If you invoke a Lambda function programmatically, you can add the `LogType` parameter to retrieve the last 4 KB of log data that's written to CloudWatch Logs\. For more information, see [Invoke](API_Invoke.md)\. AWS Lambda returns this log information in the `x-amz-log-results` header in the response\. If you use the AWS Command Line Interface \(AWS CLI\) to invoke the function, you can specify the `--log-type` parameter with the value `Tail`\. 