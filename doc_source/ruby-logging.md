# AWS Lambda Function Logging in Ruby<a name="ruby-logging"></a>

Your Lambda function comes with a CloudWatch Logs log group, with a log stream for each instance of your function\. The runtime sends details about each invocation to the log stream, and relays logs and other output from your function's code\.

To output logs from your function code, you can use `puts` statements, or any logging library that writes to `stdout` or `stderr`\. The following example logs the values of environment variables and the event object\.

**Example lambda\_function\.rb**  

```
# lambda_function.rb

def handler(event:, context:)
    puts "## ENVIRONMENT VARIABLES"
    puts ENV.to_a
    puts "## EVENT"
    puts event.to_a
end
```

The Lambda console shows log output when you test a function on the function configuration page\. To view logs for all invocations, use the CloudWatch Logs console\.

**To view your Lambda function's logs**

1. Open the [Logs page of the CloudWatch console](https://console.aws.amazon.com/cloudwatch/home?#logs:)\.

1. Choose the log group for your function \(**/aws/lambda/*function\-name***\)\.

1. Choose the first stream in the list\.

Each log stream corresponds to an [instance of your function](running-lambda-code.md)\. New streams appear when you update your function and when additional instances are created to handle multiple concurrent invocations\. To find logs for specific invocations, you can instrument your function with X\-Ray, and record details about the request and log stream in the trace\. For a sample application that correlates logs and traces with X\-Ray, see [Error Processor Sample Application for AWS Lambda](sample-errorprocessor.md)\.

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

Log groups aren't deleted automatically when you delete a function\. To avoid storing logs indefinitely, delete the log group, or [configure a retention period](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html#SettingLogRetention) after which logs are deleted automatically\.