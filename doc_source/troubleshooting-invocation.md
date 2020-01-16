# Troubleshoot Invocation Issues in AWS Lambda<a name="troubleshooting-invocation"></a>

When you invoke a Lambda function, the event might be processed on an instance of the function that's been processing events for some time, or it might require a new instance to be initialized\. Errors can occur when you make the request, during function initialization, when your handler code processes the event, or when your function returns \(or fails to return\) a response\.

Invocation errors can be caused by issues with function code, settings, downstream resources, permissions, or limits\. If you invoke your function directly, you see invocation errors in the response from Lambda\. If you invoke your function asynchronously, with an event source mapping, or through another service, you might find errors in logs, a dead\-letter queue, or an on\-failure destination\. Error handling options and retry behavior vary depending on how you invoke your function and on the type of error\.

**Issue:** *Function is invoked continuously in a loop\.*

This typically occurs when your function manages resources in the same AWS service that triggers it\. For example, it is possible to create a function that stores an object in an Amazon S3 bucket that is configured with a [notification that invokes the function again](with-s3.md)\. To stop the function from running, choose **Throttle** on the [function configuration page](resource-model.md)\. Then identify the code path or configuration error that caused the recursive invocation\.

**Error:** *User: arn:aws:iam::123456789012:user/developer is not authorized to perform: lambda:InvokeFunction on resource: my\-function*

Your IAM user, or the role that you assume, needs permission to invoke a function\. This requirement also applies to Lambda functions and other compute resources that invoke functions\. Add the **AWSLambdaRole** managed policy, or a custom policy that allows the `lambda:InvokeFunction` action on the target function, to your IAM user\.

**Note**  
Unlike other API actions in Lambda, the name of the action in IAM \(`lambda:InvokeFunction`\) does not match the name of the API action \(`Invoke`\) for invoking a function\.

For more information, see [AWS Lambda Permissions](lambda-permissions.md)\.

**Issue:** *Function execution takes too long\.*

If your code takes much longer to run in Lambda than on your local machine, it may be constrained by the memory or processing power available to the function\. [Configure the function with additional memory](resource-model.md) to increase both memory and CPU\.

**Issue:** *Logs don't appear in CloudWatch Logs\.*

**Issue:** *Traces don't appear in AWS X\-Ray\.*

Your function needs permission to call CloudWatch Logs and X\-Ray\. Update its [execution role](lambda-intro-execution-role.md) to grant it permission\.

**Issue:** *You can invoke your function directly, but it doesn't run when another service or account invokes it\.*

You grant [other services](lambda-services.md) and accounts permission to invoke a function in the function's [resource\-based policy](access-control-resource-based.md)\. If the invoker is in another account, that user also needs [permission to invoke functions](access-control-identity-based.md)\.