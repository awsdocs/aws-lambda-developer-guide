# AWS Lambda function errors in Ruby<a name="ruby-exceptions"></a>

When your code raises an error, Lambda generates a JSON representation of the error\. This error document appears in the invocation log and, for synchronous invocations, in the output\.

**Example function\.rb**  

```
def handler(event:, context:)
    puts "Processing event..."
    [1, 2, 3].first("two")
    "Success"
end
```

This code results in a type error\. Lambda catches the error and generates a JSON document with fields for the error message, the type, and the stack trace\.

```
{
  "errorMessage": "no implicit conversion of String into Integer",
  "errorType": "Function<TypeError>",
  "stackTrace": [
    "/var/task/function.rb:3:in `first'",
    "/var/task/function.rb:3:in `handler'"
  ]
}
```

When you invoke the function from the command line, the AWS CLI splits the response into two documents\. To indicate that a function error occurred, the response displayed in the terminal includes a `FunctionError` field\. The response or error returned by the function is written to the output file\.

```
$ aws lambda invoke --function-name my-function out.json
{
    "StatusCode": 200,
    "FunctionError": "Unhandled",
    "ExecutedVersion": "$LATEST"
}
```

View the output file to see the error document\.

```
$ cat out.json
{"errorMessage":"no implicit conversion of String into Integer","errorType":"Function<TypeError>","stackTrace":["/var/task/function.rb:3:in `first'","/var/task/function.rb:3:in `handler'"]}
```

**Note**  
The 200 \(success\) status code in the response from Lambda indicates that there wasn't an error with the request that you sent to Lambda\. For issues that result in an error status code, see [Errors](API_Invoke.md#API_Invoke_Errors)\.

Lambda also records up to 256 KB of the error object in the function's logs\. To view logs when you invoke the function from the command line, use the `--log-type` option and decode the base64 string in the response\.

```
$ aws lambda invoke --function-name my-function out.json --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: 5ce6a15a-f156-11e8-b8aa-25371a5ca2a3 Version: $LATEST
Processing event...
Error raised from handler method
{
  "errorMessage": "no implicit conversion of String into Integer",
  "errorType": "Function<TypeError>",
  "stackTrace": [
    "/var/task/function.rb:3:in `first'",
    "/var/task/function.rb:3:in `handler'"
  ]
}
END RequestId: 5ce6a15a-f156-11e8-b8aa-25371a5ca2a3
REPORT RequestId: 5ce6a15a-f156-11e8-b8aa-25371a5ca2a3  Duration: 22.74 ms      Billed Duration: 100 ms         Memory Size: 128 MB     Max Memory Used: 18 MB
```

For more information about logs, see [AWS Lambda function logging in Ruby](ruby-logging.md)\.

Depending on the event source, AWS Lambda might retry the failed Lambda function\. For example, if Kinesis is the event source, AWS Lambda retries the failed invocation until the Lambda function succeeds or the records in the stream expire\. For more information on retries, see [Error handling and automatic retries in AWS Lambda](invocation-retries.md)\.