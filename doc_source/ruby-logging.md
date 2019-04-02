# AWS Lambda Function Logging in Ruby<a name="ruby-logging"></a>

Your Lambda function comes with a CloudWatch Logs log group, with a log stream for each instance of your function\. The runtime sends details about each invocation to the log stream, and relays logs and other output from your function's code\.

To write to your function's log, use `puts`\. The following example logs the values of environment variables and the event object\.

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

**To view your Lambda function's logs**

1. Open the [Logs page of the CloudWatch console](https://console.aws.amazon.com/cloudwatch/home?#logs:)\.

1. Choose the log group for your function \(**/aws/lambda/*function\-name***\)\.

1. Choose the first stream in the list\.

To output logs from your function code, you can use `puts` statements, or any logging library that writes to `stdout` or `stderr`\.

To get logs for an invocation from the command line, use the `--log-type` option\. The response includes a `LogResult` field that contains up to 4 KB of base64\-encoded logs from the invocation\.

```
$ aws lambda invoke --function-name ruby-function out --log-type Tail
{
    "StatusCode": 200,
    "LogResult": "U1RBUlQgUmVxdWVzdElkOiA4N2QwNDRiOC1mMTU0LTExZTgtOGNkYS0yOTc0YzVlNGZiMjEgVmVyc2lvb...",
    "ExecutedVersion": "$LATEST"
}
```

You can use the `base64` utility to decode the logs\.

```
$ aws lambda invoke --function-name ruby-function out --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: 8e827ab1-f155-11e8-b06d-018ab046158d Version: $LATEST
Processing event...
END RequestId: 8e827ab1-f155-11e8-b06d-018ab046158d
REPORT RequestId: 8e827ab1-f155-11e8-b06d-018ab046158d  Duration: 29.40 ms      Billed Duration: 100 ms         Memory Size: 128 MB     Max Memory Used: 19 MB
```

`base64` is available on Linux, macOS, and [Ubuntu on Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. For macOS, the command is `base64 -D`\.