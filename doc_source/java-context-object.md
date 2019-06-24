# AWS Lambda Context Object in Java<a name="java-context-object"></a>

When Lambda runs your function, it passes a context object to the [handler](java-programming-model-handler-types.md)\. This object provides methods and properties that provide information about the invocation, function, and execution environment\.

**Context Methods**
+ `getRemainingTimeInMillis()` – Returns the number of milliseconds left before the execution times out\.
+ `getFunctionName()` – Returns the name of the Lambda function\.
+ `getFunctionVersion()` – Returns the [version](versioning-aliases.md) of the function\.
+ `getInvokedFunctionArn()` – Returns the Amazon Resource Name \(ARN\) that's used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `getMemoryLimitInMB()` – Returns the amount of memory that's allocated for the function\.
+ `getAwsRequestId()` – Returns the identifier of the invocation request\.
+ `getLogGroupName()` – Returns the log group for the function\.
+ `getLogStreamName()` – Returns the log stream for the function instance\.
+ `getIdentity()` – \(mobile apps\) Returns information about the Amazon Cognito identity that authorized the request\.
+ `getClientContext()` – \(mobile apps\) Returns the client context that's provided to Lambda by the client application\.
+ `getLogger()` – Returns the [logger object](java-logging.md) for the function\.