# AWS Lambda function errors in Python<a name="python-exceptions"></a>

When your code raises an error, Lambda generates a JSON representation of the error\. This error document appears in the invocation log and, for synchronous invocations, in the output\.

**Example lambda\_function\.py file – Exception**  

```
def lambda_handler(event, context):
    return x + 10
```

This code results in a name error\. Lambda catches the error and generates a JSON document with fields for the error message, the type, and the stack trace\.

```
{
  "errorMessage": "name 'x' is not defined",
  "errorType": "NameError",
  "stackTrace": [
    "  File \"/var/task/error_function.py\", line 2, in lambda_handler\n    return x + 10\n"
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
{"errorMessage": "name 'x' is not defined", "errorType": "NameError", "stackTrace": ["  File \"/var/task/error_function.py\", line 2, in lambda_handler\n    return x + 10\n"]}
```

**Note**  
The 200 \(success\) status code in the response from Lambda indicates that there wasn't an error with the request that you sent to Lambda\. For issues that result in an error status code, see [Errors](API_Invoke.md#API_Invoke_Errors)\.

Lambda also records up to 256 KB of the error object in the function's logs\. To view logs when you invoke the function from the command line, use the `--log-type` option and decode the base64 string in the response\.

```
$ aws lambda invoke --function-name my-function out.json --log-type Tail \
--query 'LogResult' --output text |  base64 -d
START RequestId: fc4f8810-88ff-4800-974c-12cec018a4b9 Version: $LATEST
      return x + 10/lambda_function.py", line 2, in lambda_handler
END RequestId: fc4f8810-88ff-4800-974c-12cec018a4b9
REPORT RequestId: fc4f8810-88ff-4800-974c-12cec018a4b9	Duration: 12.33 ms	Billed Duration: 100 ms	Memory Size: 128 MB	Max Memory Used: 56 MB
```

For more information about logs, see [AWS Lambda function logging in Python](python-logging.md)\.