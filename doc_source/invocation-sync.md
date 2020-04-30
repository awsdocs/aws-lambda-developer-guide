# Synchronous invocation<a name="invocation-sync"></a>

When you invoke a function synchronously, Lambda runs the function and waits for a response\. When the function execution ends, Lambda returns the response from the function's code with additional data, such as the version of the function that was executed\. To invoke a function synchronously with the AWS CLI, use the `invoke` command\.

```
$ aws lambda invoke --function-name my-function --payload '{ "key": "value" }' response.json
{
    "ExecutedVersion": "$LATEST",
    "StatusCode": 200
}
```

The following diagram shows clients invoking a Lambda function synchronously\. Lambda sends the events directly to the function and sends the function's response back to the invoker\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/invocation-sync.png)

The `payload` is a string that contains an event in JSON format\. The name of the file where the AWS CLI writes the response from the function is `response.json`\. If the function returns an object or error, the response is the object or error in JSON format\. If the function exits without error, the response is `null`\.

The output from the command, which is displayed in the terminal, includes information from headers in the response from Lambda\. This includes the version that processed the event \(useful when you use [aliases](configuration-aliases.md)\), and the status code returned by Lambda\. If Lambda was able to run the function, the status code is 200, even if the function returned an error\.

**Note**  
For functions with a long timeout, your client might be disconnected during synchronous invocation while it waits for a response\. Configure your HTTP client, SDK, firewall, proxy, or operating system to allow for long connections with timeout or keep\-alive settings\.

If Lambda isn't able to run the function, the error is displayed in the output\.

```
$ aws lambda invoke --function-name my-function --payload value response.json
An error occurred (InvalidRequestContentException) when calling the Invoke operation: Could not parse request body into json: Unrecognized token 'value': was expecting ('true', 'false' or 'null')
 at [Source: (byte[])"value"; line: 1, column: 11]
```

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

For more information about the `Invoke` API, including a full list of parameters, headers, and errors, see [Invoke](API_Invoke.md)\.

When you invoke a function directly, you can check the response for errors and retry\. The AWS CLI and AWS SDK also automatically retry on client timeouts, throttling, and service errors\. For more information, see [Error handling and automatic retries in AWS Lambda](invocation-retries.md)\.