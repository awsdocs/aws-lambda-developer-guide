# AWS Lambda function logging in PowerShell<a name="powershell-logging"></a>

AWS Lambda automatically monitors Lambda functions on your behalf and sends function metrics to Amazon CloudWatch\. Your Lambda function comes with a CloudWatch Logs log group and a log stream for each instance of your function\. The Lambda runtime environment sends details about each invocation to the log stream, and relays logs and other output from your function's code\. 

This page describes how to produce log output from your Lambda function's code, or access logs using the AWS Command Line Interface, the Lambda console, or the CloudWatch console\.

**Topics**
+ [Creating a function that returns logs](#powershell-logging-output)
+ [Using the Lambda console](#powershell-logging-console)
+ [Using the CloudWatch console](#powershell-logging-cwconsole)
+ [Using the AWS Command Line Interface \(AWS CLI\)](#powershell-logging-cli)
+ [Deleting logs](#powershell-logging-delete)

## Creating a function that returns logs<a name="powershell-logging-output"></a>

 To output logs from your function code, you can use cmdlets on [Microsoft\.PowerShell\.Utility ](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.utility), or any logging module that writes to `stdout` or `stderr`\. The following example uses `Write-Host`\.

**Example [function/Handler\.ps1](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-powershell/function/Handler.ps1) – Logging**  

```
#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.618.0'}
Write-Host `## Environment variables
Write-Host AWS_LAMBDA_FUNCTION_VERSION=$Env:AWS_LAMBDA_FUNCTION_VERSION
Write-Host AWS_LAMBDA_LOG_GROUP_NAME=$Env:AWS_LAMBDA_LOG_GROUP_NAME
Write-Host AWS_LAMBDA_LOG_STREAM_NAME=$Env:AWS_LAMBDA_LOG_STREAM_NAME
Write-Host AWS_EXECUTION_ENV=$Env:AWS_EXECUTION_ENV
Write-Host AWS_LAMBDA_FUNCTION_NAME=$Env:AWS_LAMBDA_FUNCTION_NAME
Write-Host PATH=$Env:PATH
Write-Host `## Event
Write-Host (ConvertTo-Json -InputObject $LambdaInput -Compress -Depth 3)
```

**Example log format**  

```
START RequestId: 56639408-xmpl-435f-9041-ac47ae25ceed Version: $LATEST
Importing module ./Modules/AWSPowerShell.NetCore/3.3.618.0/AWSPowerShell.NetCore.psd1
[Information] - ## Environment variables
[Information] - AWS_LAMBDA_FUNCTION_VERSION=$LATEST
[Information] - AWS_LAMBDA_LOG_GROUP_NAME=/aws/lambda/blank-powershell-function-18CIXMPLHFAJJ
[Information] - AWS_LAMBDA_LOG_STREAM_NAME=2020/04/01/[$LATEST]53c5xmpl52d64ed3a744724d9c201089
[Information] - AWS_EXECUTION_ENV=AWS_Lambda_dotnet6_powershell_1.0.0
[Information] - AWS_LAMBDA_FUNCTION_NAME=blank-powershell-function-18CIXMPLHFAJJ
[Information] - PATH=/var/lang/bin:/usr/local/bin:/usr/bin/:/bin:/opt/bin
[Information] - ## Event
[Information] - 
{
    "Records": [
        {
            "messageId": "19dd0b57-b21e-4ac1-bd88-01bbb068cb78",
            "receiptHandle": "MessageReceiptHandle",
            "body": "Hello from SQS!",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1523232000000",
                "SenderId": "123456789012",
                "ApproximateFirstReceiveTimestamp": "1523232000001"
            },
            ...
END RequestId: 56639408-xmpl-435f-9041-ac47ae25ceed
REPORT RequestId: 56639408-xmpl-435f-9041-ac47ae25ceed	Duration: 3906.38 ms	Billed Duration: 4000 ms	Memory Size: 512 MB	Max Memory Used: 367 MB	Init Duration: 5960.19 ms	
XRAY TraceId: 1-5e843da6-733cxmple7d0c3c020510040	SegmentId: 3913xmpl20999446	Sampled: true
```

The \.NET runtime logs the `START`, `END`, and `REPORT` lines for each invocation\. The report line provides the following details\.

**Report Log**
+ **RequestId** – The unique request ID for the invocation\.
+ **Duration** – The amount of time that your function's handler method spent processing the event\.
+ **Billed Duration** – The amount of time billed for the invocation\.
+ **Memory Size** – The amount of memory allocated to the function\.
+ **Max Memory Used** – The amount of memory used by the function\.
+ **Init Duration** – For the first request served, the amount of time it took the runtime to load the function and run code outside of the handler method\.
+ **XRAY TraceId** – For traced requests, the [AWS X\-Ray trace ID](services-xray.md)\.
+ **SegmentId** – For traced requests, the X\-Ray segment ID\.
+ **Sampled** – For traced requests, the sampling result\.

## Using the Lambda console<a name="powershell-logging-console"></a>

You can use the Lambda console to view log output after you invoke a Lambda function\. For more information, see [Accessing Amazon CloudWatch logs for AWS Lambda](monitoring-cloudwatchlogs.md)\.

## Using the CloudWatch console<a name="powershell-logging-cwconsole"></a>

You can use the Amazon CloudWatch console to view logs for all Lambda function invocations\.

**To view logs on the CloudWatch console**

1. Open the [Log groups page](https://console.aws.amazon.com/cloudwatch/home?#logs:) on the CloudWatch console\.

1. Choose the log group for your function \(**/aws/lambda/*your\-function\-name***\)\.

1. Choose a log stream\.

Each log stream corresponds to an [instance of your function](lambda-runtime-environment.md)\. A log stream appears when you update your Lambda function, and when additional instances are created to handle multiple concurrent invocations\. To find logs for a specific invocation, we recommend instrumenting your function with AWS X\-Ray\. X\-Ray records details about the request and the log stream in the trace\.

To use a sample application that correlates logs and traces with X\-Ray, see [Error processor sample application for AWS Lambda](samples-errorprocessor.md)\.

## Using the AWS Command Line Interface \(AWS CLI\)<a name="powershell-logging-cli"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

You can use the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-welcome.html) to retrieve logs for an invocation using the `--log-type` command option\. The response contains a `LogResult` field that contains up to 4 KB of base64\-encoded logs from the invocation\.

**Example retrieve a log ID**  
The following example shows how to retrieve a *log ID* from the `LogResult` field for a function named `my-function`\.  

```
aws lambda invoke --function-name my-function out --log-type Tail
```
You should see the following output:  

```
{
    "StatusCode": 200,
    "LogResult": "U1RBUlQgUmVxdWVzdElkOiA4N2QwNDRiOC1mMTU0LTExZTgtOGNkYS0yOTc0YzVlNGZiMjEgVmVyc2lvb...",
    "ExecutedVersion": "$LATEST"
}
```

**Example decode the logs**  
In the same command prompt, use the `base64` utility to decode the logs\. The following example shows how to retrieve base64\-encoded logs for `my-function`\.  

```
aws lambda invoke --function-name my-function out --log-type Tail \
--query 'LogResult' --output text |  base64 -d
```
You should see the following output:  

```
START RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8 Version: $LATEST
"AWS_SESSION_TOKEN": "AgoJb3JpZ2luX2VjELj...", "_X_AMZN_TRACE_ID": "Root=1-5d02e5ca-f5792818b6fe8368e5b51d50;Parent=191db58857df8395;Sampled=0"",ask/lib:/opt/lib",
END RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8
REPORT RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8  Duration: 79.67 ms      Billed Duration: 80 ms         Memory Size: 128 MB     Max Memory Used: 73 MB
```
The `base64` utility is available on Linux, macOS, and [Ubuntu on Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. macOS users may need to use `base64 -D`\.

**Example get\-logs\.sh script**  
In the same command prompt, use the following script to download the last five log events\. The script uses `sed` to remove quotes from the output file, and sleeps for 15 seconds to allow time for the logs to become available\. The output includes the response from Lambda and the output from the `get-log-events` command\.   
Copy the contents of the following code sample and save in your Lambda project directory as `get-logs.sh`\.  
The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.  

```
#!/bin/bash
aws lambda invoke --function-name my-function --cli-binary-format raw-in-base64-out --payload '{"key": "value"}' out
sed -i'' -e 's/"//g' out
sleep 15
aws logs get-log-events --log-group-name /aws/lambda/my-function --log-stream-name stream1 --limit 5
```

**Example macOS and Linux \(only\)**  
In the same command prompt, macOS and Linux users may need to run the following command to ensure the script is executable\.  

```
chmod -R 755 get-logs.sh
```

**Example retrieve the last five log events**  
In the same command prompt, run the following script to get the last five log events\.  

```
./get-logs.sh
```
You should see the following output:  

```
{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
{
    "events": [
        {
            "timestamp": 1559763003171,
            "message": "START RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf Version: $LATEST\n",
            "ingestionTime": 1559763003309
        },
        {
            "timestamp": 1559763003173,
            "message": "2019-06-05T19:30:03.173Z\t4ce9340a-b765-490f-ad8a-02ab3415e2bf\tINFO\tENVIRONMENT VARIABLES\r{\r  \"AWS_LAMBDA_FUNCTION_VERSION\": \"$LATEST\",\r ...",
            "ingestionTime": 1559763018353
        },
        {
            "timestamp": 1559763003173,
            "message": "2019-06-05T19:30:03.173Z\t4ce9340a-b765-490f-ad8a-02ab3415e2bf\tINFO\tEVENT\r{\r  \"key\": \"value\"\r}\n",
            "ingestionTime": 1559763018353
        },
        {
            "timestamp": 1559763003218,
            "message": "END RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf\n",
            "ingestionTime": 1559763018353
        },
        {
            "timestamp": 1559763003218,
            "message": "REPORT RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf\tDuration: 26.73 ms\tBilled Duration: 27 ms \tMemory Size: 128 MB\tMax Memory Used: 75 MB\t\n",
            "ingestionTime": 1559763018353
        }
    ],
    "nextForwardToken": "f/34783877304859518393868359594929986069206639495374241795",
    "nextBackwardToken": "b/34783877303811383369537420289090800615709599058929582080"
}
```

## Deleting logs<a name="powershell-logging-delete"></a>

Log groups aren't deleted automatically when you delete a function\. To avoid storing logs indefinitely, delete the log group, or [configure a retention period](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html#SettingLogRetention) after which logs are deleted automatically\.