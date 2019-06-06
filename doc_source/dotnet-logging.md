# AWS Lambda Function Logging in C\#<a name="dotnet-logging"></a>

Your Lambda function comes with a CloudWatch Logs log group, with a log stream for each instance of your function\. The runtime sends details about each invocation to the log stream, and relays logs and other output from your function's code\.

To output logs from your function code, you can use methods on [the Console class](https://docs.microsoft.com/en-us/dotnet/api/system.console), or any logging library that writes to `stdout` or `stderr`\. The following example logs the request ID for an invocation\.

```
public class ProductService
{
   public async Task<Product> DescribeProduct(DescribeProductRequest request)
    {
       Console.WriteLine("DescribeProduct invoked with Id " + request.Id);
       return await catalogService.DescribeProduct(request.Id);
    }
}
```

Lambda also provides a logger class in the `Amazon.Lambda.Core ` library\. Use the `Log` method on the `Amazon.Lambda.Core.LambdaLogger` class to write logs\.

```
using Amazon.Lambda.Core;
                            
public class ProductService
{
   public async Task<Product> DescribeProduct(DescribeProductRequest request)
   {
       LambdaLogger.Log("DescribeProduct invoked with Id " + request.Id);
       return await catalogService.DescribeProduct(request.Id);
   }
}
```

An instance of this class is also available on [the context object](dotnet-context-object.md)\.

```
public class ProductService
{
   public async Task<Product> DescribeProduct(DescribeProductRequest request, ILambdaContext context)
   {
       context.Logger.Log("DescribeProduct invoked with Id " + request.Id);
       return await catalogService.DescribeProduct(request.Id);
   }
}
```

The Lambda console shows log output when you test a function on the function configuration page\. To view logs for all invocations, use the CloudWatch Logs console\.

**To view your Lambda function's logs**

1. Open the [Logs page of the CloudWatch console](https://console.aws.amazon.com/cloudwatch/home?#logs:)\.

1. Choose the log group for your function \(**/aws/lambda/*function\-name***\)\.

1. Choose the first stream in the list\.

Each log stream corresponds to an [instance of your function](running-lambda-code.md)\. New streams appear when you update your function and when additional instances are created to handle multiple concurrent invocations\. To find logs for specific invocations, you can instrument your function with X\-Ray and record details about the request and log stream in the trace\. For a sample application that correlates logs and traces with X\-Ray, see [Error Processor Sample Application for AWS Lambda](sample-errorprocessor.md)\.

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
START RequestId: 8e827ab1-f155-11e8-b06d-018ab046158d Version: $LATEST
Processing event...
END RequestId: 8e827ab1-f155-11e8-b06d-018ab046158d
REPORT RequestId: 8e827ab1-f155-11e8-b06d-018ab046158d  Duration: 29.40 ms      Billed Duration: 100 ms         Memory Size: 128 MB     Max Memory Used: 19 MB
```

`base64` is available on Linux, macOS, and [Ubuntu on Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. For macOS, the command is `base64 -D`\.

Log groups are not deleted automatically when you delete a function\. To avoid storing logs indefinitely, delete the log group, or [configure a retention period](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html#SettingLogRetention) after which logs are deleted automatically\.