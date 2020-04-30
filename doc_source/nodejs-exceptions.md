# AWS Lambda function errors in Node\.js<a name="nodejs-exceptions"></a>

When your code raises an error, Lambda generates a JSON representation of the error\. This error document appears in the invocation log and, for synchronous invocations, in the output\.

**Example index\.js file – Reference error**  

```
exports.handler = async function() {
  return x + 10
}
```

This code results in a reference error\. Lambda catches the error and generates a JSON document with fields for the error message, the type, and the stack trace\.

```
{
  "errorType": "ReferenceError",
  "errorMessage": "x is not defined",
  "trace": [
    "ReferenceError: x is not defined",
    "    at Runtime.exports.handler (/var/task/index.js:2:3)",
    "    at Runtime.handleOnce (/var/runtime/Runtime.js:63:25)",
    "    at process._tickCallback (internal/process/next_tick.js:68:7)"
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
{"errorType":"ReferenceError","errorMessage":"x is not defined","trace":["ReferenceError: x is not defined"," at Runtime.exports.handler (/var/task/index.js:2:3)"," at Runtime.handleOnce (/var/runtime/Runtime.js:63:25)"," at process._tickCallback (internal/process/next_tick.js:68:7)"]}
```

**Note**  
The 200 \(success\) status code in the response from Lambda indicates that there wasn't an error with the request that you sent to Lambda\. For issues that result in an error status code, see [Errors](API_Invoke.md#API_Invoke_Errors)\.

Lambda also records up to 256 KB of the error object in the function's logs\. To view logs when you invoke the function from the command line, use the `--log-type` option and decode the base64 string in the response\.

```
$ aws lambda invoke --function-name my-function out.json --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: 8bbbfb91-a3ff-4502-b1b7-cb8f6658de64 Version: $LATEST
2019-06-05T22:11:27.082Z        8bbbfb91-a3ff-4502-b1b7-cb8f6658de64    ERROR   Invoke Error    {"errorType":"ReferenceError","errorMessage":"x is not defined","stack":["ReferenceError: x is not defined","    at Runtime.exports.handler (/var/task/index.js:2:3)","    at Runtime.handleOnce (/var/runtime/Runtime.js:63:25)","    at process._tickCallback (internal/process/next_tick.js:68:7)"]}
END RequestId: 8bbbfb91-a3ff-4502-b1b7-cb8f6658de64
REPORT RequestId: 8bbbfb91-a3ff-4502-b1b7-cb8f6658de64  Duration: 76.85 ms      Billed Duration: 100 ms         Memory Size: 128 MB     Max Memory Used: 74 MB
```

For more information about logs, see [AWS Lambda function logging in Node\.js](nodejs-logging.md)\.

Depending on the event source, AWS Lambda might retry the failed Lambda function\. For example, if Kinesis is the event source, AWS Lambda retries the failed invocation until the Lambda function succeeds or the records in the stream expire\. For more information on retries, see [Error handling and automatic retries in AWS Lambda](invocation-retries.md)\.