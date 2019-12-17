# Troubleshooting AWS Lambda<a name="lambda-troubleshooting"></a>

This topic lists common errors and issues that you might encounter when using the Lambda API, console, or tools\. If you find an issue that is not listed here, you can use the **Feedback** button on this page to report it\.

For more troubleshooting advice and answers to common support questions, visit the [AWS Knowledge Center](https://aws.amazon.com/premiumsupport/knowledge-center/#AWS_Lambda)\.

**Topics**
+ [Deployments](#troubleshooting-deployment)
+ [Invocation](#troubleshooting-invocation)

## Deployments<a name="troubleshooting-deployment"></a>

**Error:** *EACCES: permission denied, open '/var/task/index\.js'*

**Error:** *cannot load such file \-\- function*

**Error:** *\[Errno 13\] Permission denied: '/var/task/function\.py'*

The Lambda runtime needs permission to read the files in your deployment package\. You can use the `chmod` command to change the file mode\. The following example commands make all files and folders in the current directory readable by any user\.

```
my-function$ chmod 644 $(find . -type f)
my-function$ chmod 755 $(find . -type d)
```

**Error:** *An error occurred \(RequestEntityTooLargeException\) when calling the UpdateFunctionCode operation*

When you upload a deployment package or layer archive directly to Lambda, the size of the ZIP file is limited to 50 MB\. To upload a larger file, store it in Amazon S3 and use the [S3Bucket and S3Key](API_UpdateFunctionCode.md#SSS-UpdateFunctionCode-request-S3Bucket) parameters\.

**Note**  
When you upload a file directly with the AWS CLI, AWS SDK, or otherwise, the binary ZIP file is converted to Base64, increasing its size by about 30%\. To allow for this, and the size of other parameters in the request, the actual request size limit that Lambda applies is larger\. Due to this, the 50 MB limit is approximate\.

## Invocation<a name="troubleshooting-invocation"></a>

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