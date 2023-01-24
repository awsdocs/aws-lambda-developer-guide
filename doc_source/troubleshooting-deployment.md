# Troubleshoot deployment issues in Lambda<a name="troubleshooting-deployment"></a>

When you update your function, Lambda deploys the change by launching new instances of the function with the updated code or settings\. Deployment errors prevent the new version from being used and can arise from issues with your deployment package, code, permissions, or tools\.

When you deploy updates to your function directly with the Lambda API or with a client such as the AWS CLI, you can see errors from Lambda directly in the output\. If you use services like AWS CloudFormation, AWS CodeDeploy, or AWS CodePipeline, look for the response from Lambda in the logs or event stream for that service\.

## General: Permission is denied / Cannot load such file<a name="troubleshooting-deployment-denied"></a>

**Error:** *EACCES: permission denied, open '/var/task/index\.js'*

**Error:** *cannot load such file \-\- function*

**Error:** *\[Errno 13\] Permission denied: '/var/task/function\.py'*

The Lambda runtime needs permission to read the files in your deployment package\. You can use the `chmod` command to change the file mode\. The following example commands make all files and folders in the current directory readable by any user\.

```
chmod -R o+rX .
```

## General: Error occurs when calling the UpdateFunctionCode<a name="troubleshooting-deployment-updatefunctioncode"></a>

**Error:** *An error occurred \(RequestEntityTooLargeException\) when calling the UpdateFunctionCode operation*

When you upload a deployment package or layer archive directly to Lambda, the size of the ZIP file is limited to 50 MB\. To upload a larger file, store it in Amazon S3 and use the S3Bucket and S3Key parameters\.

**Note**  
When you upload a file directly with the AWS CLI, AWS SDK, or otherwise, the binary ZIP file is converted to base64, which increases its size by about 30%\. To allow for this, and the size of other parameters in the request, the actual request size limit that Lambda applies is larger\. Due to this, the 50 MB limit is approximate\.

## Amazon S3: Error Code PermanentRedirect\.<a name="troubleshooting-deployment-PermanentRedirect"></a>

**Error:** *Error occurred while GetObject\. S3 Error Code: PermanentRedirect\. S3 Error Message: The bucket is in this region: us\-east\-2\. Please use this region to retry the request*

When you upload a function's deployment package from an Amazon S3 bucket, the bucket must be in the same Region as the function\. This issue can occur when you specify an Amazon S3 object in a call to [UpdateFunctionCode](API_UpdateFunctionCode.md), or use the package and deploy commands in the AWS CLI or AWS SAM CLI\. Create a deployment artifact bucket for each Region where you develop applications\.

## General: Cannot find, cannot load, unable to import, class not found, no such file or directory<a name="troubleshooting-deployment-functionHandler1"></a>

**Error:** *Cannot find module 'function'*

**Error:** *cannot load such file \-\- function*

**Error:** *Unable to import module 'function'*

**Error:** *Class not found: function\.Handler*

**Error:** *fork/exec /var/task/function: no such file or directory*

**Error:** *Unable to load type 'Function\.Handler' from assembly 'Function'\.*

The name of the file or class in your function's handler configuration doesn't match your code\. See the following entry for more information\.

## General: Undefined method handler<a name="troubleshooting-deployment-functionHandler2"></a>

**Error:** *index\.handler is undefined or not exported*

**Error:** *Handler 'handler' missing on module 'function'*

**Error:** *undefined method `handler' for \#<LambdaHandler:0x000055b76ccebf98>*

**Error:** *No public method named handleRequest with appropriate method signature found on class function\.Handler*

**Error:** *Unable to find method 'handleRequest' in type 'Function\.Handler' from assembly 'Function'*

The name of the handler method in your function's handler configuration doesn't match your code\. Each runtime defines a naming convention for handlers, such as *filename*\.*methodname*\. The handler is the method in your function's code that the runtime runs when your function is invoked\.

For some languages, Lambda provides a library with an interface that expects a handler method to have a specific name\. For details about handler naming for each language, see the following topics\.
+ [Building Lambda functions with Node\.js](lambda-nodejs.md)
+ [Building Lambda functions with Python](lambda-python.md)
+ [Building Lambda functions with Ruby](lambda-ruby.md)
+ [Building Lambda functions with Java](lambda-java.md)
+ [Building Lambda functions with Go](lambda-golang.md)
+ [Building Lambda functions with C\#](lambda-csharp.md)
+ [Building Lambda functions with PowerShell](lambda-powershell.md)

## Lambda: InvalidParameterValueException or RequestEntityTooLargeException<a name="troubleshooting-deployment-InvalidParameterValueException1"></a>

**Error:** *InvalidParameterValueException: Lambda was unable to configure your environment variables because the environment variables you have provided exceeded the 4KB limit\. String measured: \{"A1":"uSFeY5cyPiPn7AtnX5BsM\.\.\.*

**Error:** *RequestEntityTooLargeException: Request must be smaller than 5120 bytes for the UpdateFunctionConfiguration operation*

The maximum size of the variables object that is stored in the function's configuration must not exceed 4096 bytes\. This includes key names, values, quotes, commas, and brackets\. The total size of the HTTP request body is also limited\.

```
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "nodejs16.x",
    "Role": "arn:aws:iam::123456789012:role/lambda-role",
    "Environment": {
        "Variables": {
            "BUCKET": "my-bucket",
            "KEY": "file.txt"
        }
    },
    ...
}
```

In this example, the object is 39 characters and takes up 39 bytes when it's stored \(without white space\) as the string `{"BUCKET":"my-bucket","KEY":"file.txt"}`\. Standard ASCII characters in environment variable values use one byte each\. Extended ASCII and Unicode characters can use between 2 bytes and 4 bytes per character\.

## Lambda: InvalidParameterValueException<a name="troubleshooting-deployment-InvalidParameterValueException2"></a>

**Error:** *InvalidParameterValueException: Lambda was unable to configure your environment variables because the environment variables you have provided contains reserved keys that are currently not supported for modification\.*

Lambda reserves some environment variable keys for internal use\. For example, `AWS_REGION` is used by the runtime to determine the current Region and cannot be overridden\. Other variables, like `PATH`, are used by the runtime but can be extended in your function configuration\. For a full list, see [Defined runtime environment variables](configuration-envvars.md#configuration-envvars-runtime)\.

## Lambda: Concurrency and memory quotas<a name="troubleshooting-deployment-quotas"></a>

**Error:*** Specified ConcurrentExecutions for function decreases account's UnreservedConcurrentExecution below its minimum value*

**Error:*** 'MemorySize' value failed to satisfy constraint: Member must have value less than or equal to 3008*

These errors occur when you exceed the concurrency or memory [quotas](gettingstarted-limits.md) for your account\. New AWS accounts have reduced concurrency and memory quotas\. AWS raises these quotas automatically based on your usage\. To resolve these errors, you can [request a quota increase](https://docs.aws.amazon.com/servicequotas/latest/userguide/request-quota-increase.html)\.
+ **Concurrency:** You might get an error if you try to create a function using reserved or provisioned concurrency, or if your per\-function concurrency request \([PutFunctionConcurrency](API_PutFunctionConcurrency.md)\) exceeds your account's concurrency quota\.
+ **Memory:** Errors occur if the amount of memory allocated to the function exceeds your account's memory quota\.