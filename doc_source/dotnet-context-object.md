# The Context Object \(C\#\)<a name="dotnet-context-object"></a>

You can gain useful information on how your Lambda function is interacting with the AWS Lambda runtime by adding the `ILambdaContext` parameter to your method\. In return, AWS Lambda provides runtime details such as the CloudWatch log stream associated with the function or the id of the client that called your functions, which you access via the properties provided by the context object\.

To do this, create a method with the following signature:

```
public void Handler(string Input, ILambdaContext context)
```

The context object properties are:
+ `MemoryLimitInMB`: Memory limit, in MB, you configured for the Lambda function\.
+ `FunctionName`: Name of the Lambda function that is running\.
+ `FunctionVersion`: The Lambda function version that is executing\. If an alias is used to invoke the function, then `FunctionVersion` will be the version the alias points to\.
+ `InvokedFunctionArn`: The ARN used to invoke this function\. It can be function ARN or alias ARN\. An unqualified ARN executes the `$LATEST` version and aliases execute the function version it is pointing to\. 
+  `AwsRequestId`: AWS request ID associated with the request\. This is the ID returned to the client that invoked this Lambda function\. You can use the request ID for any follow up enquiry with AWS support\. Note that if AWS Lambda retries the function \(for example, in a situation where the Lambda function processing Kinesis records throw an exception\), the request ID remains the same\.
+ `LogStreamName`: The CloudWatch log stream name for the particular Lambda function execution\. It can be null if the IAM user provided does not have permission for CloudWatch actions\.
+ `LogGroupName`: The CloudWatch log group name associated with the Lambda function invoked\. It can be null if the IAM user provided does not have permission for CloudWatch actions\.
+ `ClientContext`: Information about the client application and device when invoked through the AWS Mobile SDK\. It can be null\.  Client context provides client information such as client ID, application title, version name, version code, and the application package name\.
+  `Identity`: Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK\. It can be null\.
+ `RemainingTime`: Remaining execution time till the function will be terminated\. At the time you create the Lambda function you set maximum time limit, at which time AWS Lambda will terminate the function execution\. Information about the remaining time of function execution can be used to specify function behavior when nearing the timeout\. This is a `TimeSpan` field\.
+ `Logger`: The Lambda logger associated with the ILambdaContext object\. For more information, see [Logging \(C\#\)](dotnet-logging.md)\.

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