# AWS Lambda Function Logging in PowerShell<a name="powershell-logging"></a>

Your Lambda function comes with a CloudWatch Logs log group, with a log stream for each instance of your function\. The runtime sends details about each invocation to the log stream, and relays logs and other output from your function's code\.

To output logs from your function code, you can use cmdlets on [Microsoft\.PowerShell\.Utility ](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.utility), or any logging module that writes to `stdout` or `stderr`\. The following example uses `Write-Host` to log environment variables\.

**Example function\.ps1**  

```
#Requires -Modules @{ModuleName='AWSPowerShell.NetCore';ModuleVersion='3.3.365.0'}
$var = (Get-ChildItem env:*).GetEnumerator() | Sort-Object Name
$out = "`n"
Foreach ($v in $var) {$out = $out + "{0} : {1}`n" -f $v.Name, $v.Value}
Write-Host ( $out )
```

**Example Log Format**  

```
START RequestId: d50fc4d4-xmpl-4b55-8225-af0c43bb27d2 Version: $LATEST
Importing module ./Modules/AWSPowerShell.NetCore/3.3.618.0/AWSPowerShell.NetCore.psd1
[Information] - Region: us-east-2
[Information] - Function name: my-function
END RequestId: d50fc4d4-xmpl-4b55-8225-af0c43bb27d2
REPORT RequestId: d50fc4d4-xmpl-4b55-8225-af0c43bb27d2  Duration: 601.73 ms Billed Duration: 700 ms Memory Size: 512 MB Max Memory Used: 365 MB Init Duration: 5994.31 ms   
XRAY TraceId: 1-5e34a46a-591fxmpl7a412745094b2b7a   SegmentId: 4988xmpl190a003a Sampled: true
```

The \.NET runtime logs the `START`, `END`, and `REPORT` lines for each invocation\. The report line provides the following details\.

**Report Log**
+ **RequestId** – The unique request ID for the invocation\.
+ **Duration** – The amount of time that your function's handler method spent processing the event\.
+ **Billed Duration** – The amount of time billed for the invocation\.
+ **Memory Size** – The amount of memory allocated to the function\.
+ **Max Memory Used** – The amount of memory used by the function\.
+ **Init Duration** – For the first request served, the amount of time it took the runtime to load the function and run code outside of the handler method\.
+ **XRAY TraceId** – For traced requests, the [AWS X\-Ray trace ID](lambda-x-ray.md)\.
+ **SegmentId** – For traced requests, the X\-Ray segment ID\.
+ **Sampled** – For traced requests, the sampling result\.

You can view logs in the Lambda console, in the CloudWatch Logs console, or from the command line\.

**Topics**
+ [Viewing Logs in the AWS Management Console](#powershell-logging-console)
+ [Using the AWS CLI](#powershell-logging-cli)
+ [Deleting Logs](#powershell-logging-delete)

## Viewing Logs in the AWS Management Console<a name="powershell-logging-console"></a>

The Lambda console shows log output when you test a function on the function configuration page\. To view logs for all invocations, use the CloudWatch Logs console\.

**To view your Lambda function's logs**

1. Open the [Logs page of the CloudWatch console](https://console.aws.amazon.com/cloudwatch/home?#logs:)\.

1. Choose the log group for your function \(**/aws/lambda/*function\-name***\)\.

1. Choose the first stream in the list\.

Each log stream corresponds to an [instance of your function](running-lambda-code.md)\. New streams appear when you update your function and when additional instances are created to handle multiple concurrent invocations\. To find logs for specific invocations, you can instrument your function with X\-Ray, and record details about the request and log stream in the trace\. For a sample application that correlates logs and traces with X\-Ray, see [Error Processor Sample Application for AWS Lambda](sample-errorprocessor.md)\.

## Using the AWS CLI<a name="powershell-logging-cli"></a>

To get logs for an invocation from the command line, use the `--log-type` option\. The response includes a `LogResult` field that contains up to 4 KB of base64\-encoded logs from the invocation\.

```
$ aws lambda invoke --function-name my-function out --log-type Tail
{
    "StatusCode": 200,
    "LogResult": "U1RBUlQgUmVxdWVzdElkOiA4N2QwNDRiOC1mMTU0LTExZTgtOGNkYS0yOTc0YzVlNGZiMjEgVmVyc2lvb...",
    "ExecutedVersion": "$LATEST"
}
```

You can use the `base64` utility to decode the logs\.

```
$ aws lambda invoke --function-name my-function out --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8 Version: $LATEST
  "AWS_SESSION_TOKEN": "AgoJb3JpZ2luX2VjELj...", "_X_AMZN_TRACE_ID": "Root=1-5d02e5ca-f5792818b6fe8368e5b51d50;Parent=191db58857df8395;Sampled=0"",ask/lib:/opt/lib",
END RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8
REPORT RequestId: 57f231fb-1730-4395-85cb-4f71bd2b87b8  Duration: 79.67 ms      Billed Duration: 100 ms         Memory Size: 128 MB     Max Memory Used: 73 MB
```

The `base64` utility is available on Linux, macOS, and [Ubuntu on Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. For macOS, the command is `base64 -D`\.

To get full log events from the command line, you can include the log stream name in the output of your function, as shown in the preceding example\. The following example script invokes a function named `my-function` and downloads the last five log events\.

**Example get\-logs\.sh Script**  
This example requires that `my-function` returns a log stream ID\.  

```
aws lambda invoke --function-name my-function --payload '{"key": "value"}' out
sed -i'' -e 's/"//g' out
sleep 15
aws logs get-log-events --log-group-name /aws/lambda/my-function --log-stream-name $(cat out) --limit 5
```

The script uses `sed` to remove quotes from the output file, and sleeps for 15 seconds to allow time for the logs to be available\. The output includes the response from Lambda and the output from the `get-log-events` command\.

```
$ ./get-logs.sh
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
            "message": "REPORT RequestId: 4ce9340a-b765-490f-ad8a-02ab3415e2bf\tDuration: 26.73 ms\tBilled Duration: 100 ms \tMemory Size: 128 MB\tMax Memory Used: 75 MB\t\n",
            "ingestionTime": 1559763018353
        }
    ],
    "nextForwardToken": "f/34783877304859518393868359594929986069206639495374241795",
    "nextBackwardToken": "b/34783877303811383369537420289090800615709599058929582080"
}
```

## Deleting Logs<a name="powershell-logging-delete"></a>

Log groups aren't deleted automatically when you delete a function\. To avoid storing logs indefinitely, delete the log group, or [configure a retention period](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html#SettingLogRetention) after which logs are deleted automatically\.