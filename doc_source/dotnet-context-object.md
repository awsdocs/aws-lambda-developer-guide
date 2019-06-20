# AWS Lambda Context Object in C\#<a name="dotnet-context-object"></a>

When Lambda runs your function, it passes a context object to the [handler](dotnet-programming-model-handler-types.md)\. This object provides properties with information about the invocation, function, and execution environment\.

**Context Properties**
+ `FunctionName` – The name of the Lambda function\.
+ `FunctionVersion` – The [version](versioning-aliases.md) of the function\.
+ `InvokedFunctionArn` – The Amazon Resource Name \(ARN\) that's used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `MemoryLimitInMB` – The amount of memory that's allocated for the function\.
+ `AwsRequestId` – The identifier of the invocation request\.
+ `LogGroupName` – The log group for the function\.
+ `LogStreamName` – The log stream for the function instance\.
+ `RemainingTime` \(`TimeSpan`\) – The number of milliseconds left before the execution times out\.
+ `Identity` – \(mobile apps\) Information about the Amazon Cognito identity that authorized the request\.
+ `ClientContext` – \(mobile apps\) Client context that's provided to Lambda by the client application\.
+ `Logger` The [logger object](dotnet-logging.md) for the function\.

 The following C\# code snippet shows a simple handler function that prints some of the context information\. 

```
public async Task Handler(ILambdaContext context)
{
    Console.WriteLine("Function name: " + context.FunctionName);
    Console.WriteLine("RemainingTime: " + context.RemainingTime);
    await Task.Delay(TimeSpan.FromSeconds(0.42));
    Console.WriteLine("RemainingTime after sleep: " + context.RemainingTime);
}
```