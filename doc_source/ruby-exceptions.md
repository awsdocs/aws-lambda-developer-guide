# AWS Lambda Function Errors in Ruby<a name="ruby-exceptions"></a>

When your code raises an error, Lambda generates a JSON representation of the error\. This error document appears in the invocation log and, for a synchronous invocation, in the output\.

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

When you invoke the function from the command line, the status code doesn't change, but the response includes a `FunctionError` field, and the output includes the error document\.

```
$ aws lambda invoke --function-name ruby-function out
{
    "StatusCode": 200,
    "FunctionError": "Unhandled",
    "ExecutedVersion": "$LATEST"
}
$ cat out
{"errorMessage":"no implicit conversion of String into Integer","errorType":"Function<TypeError>","stackTrace":["/var/task/function.rb:3:in `first'","/var/task/function.rb:3:in `handler'"]}
```

To view the error in the error log, use the `--log-type` option and decode the base64 string in the response\.

```
$ aws lambda invoke --function-name ruby-function out --log-type Tail \
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

For more information, see [AWS Lambda Function Logging in Ruby](ruby-logging.md)\.