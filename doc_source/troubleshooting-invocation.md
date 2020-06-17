# Troubleshoot invocation issues in AWS Lambda<a name="troubleshooting-invocation"></a>

When you invoke a Lambda function, Lambda validates the request and checks for scaling capacity before sending the event to your function or, for asynchronous invocation, to the event queue\. Invocation errors can be caused by issues with request parameters, event structure, function settings, user permissions, resource permissions, or limits\.

If you invoke your function directly, you see invocation errors in the response from Lambda\. If you invoke your function asynchronously with an event source mapping or through another service, you might find errors in logs, a dead\-letter queue, or a failed\-event destination\. Error handling options and retry behavior vary depending on how you invoke your function and on the type of error\.

For a list of error types that can be returned by the `Invoke` operation, see [Invoke](API_Invoke.md)\.

**Error:** *User: arn:aws:iam::123456789012:user/developer is not authorized to perform: lambda:InvokeFunction on resource: my\-function*

Your IAM user, or the role that you assume, needs permission to invoke a function\. This requirement also applies to Lambda functions and other compute resources that invoke functions\. Add the **AWSLambdaRole** managed policy, or a custom policy that allows the `lambda:InvokeFunction` action on the target function, to your IAM user\.

**Note**  
Unlike other API actions in Lambda, the name of the action in IAM \(`lambda:InvokeFunction`\) doesn't match the name of the API action \(`Invoke`\) for invoking a function\.

For more information, see [AWS Lambda permissions](lambda-permissions.md)\.

**Error:** *ResourceConflictException: The operation cannot be performed at this time\. The function is currently in the following state: Pending*

When you connect a function to a VPC at the time of creation, the function enters a `Pending` state while Lambda creates elastic network interfaces\. During this time, you can't invoke or modify your function\. If you connect your function to a VPC after creation, you can invoke it while the update is pending, but you can't modify its code or configuration\.

For more information, see [Monitoring the state of a function with the Lambda API](functions-states.md)\.

**Error:** *A function is stuck in the `Pending` state for several minutes\.*

If a function becomes stuck in the `Pending` state for more than six minutes, call one of the following API operations to unblock it\.
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [PublishVersion](API_PublishVersion.md)

Lambda cancels the pending operation and puts the function into the `Failed` state\. You can then delete the function and recreate it, or attempt another update\.

**Issue:** *One function is using all of the available concurrency, causing other functions to be throttled\.*

To divide the available concurrency in a Region into pools, use [reserved concurrency](configuration-concurrency.md)\. Reserved concurrency ensures that a function can always scale to its assigned concurrency, and also that it won't scale beyond its assigned concurrency\.

**Issue:** *You can invoke your function directly, but it doesn't run when another service or account invokes it\.*

You grant [other services](lambda-services.md) and accounts permission to invoke a function in the function's [resource\-based policy](access-control-resource-based.md)\. If the invoker is in another account, that user also needs [permission to invoke functions](access-control-identity-based.md)\.

**Issue:** *Function is invoked continuously in a loop\.*

This typically occurs when your function manages resources in the same AWS service that triggers it\. For example, it is possible to create a function that stores an object in an Amazon S3 bucket that is configured with a [notification that invokes the function again](with-s3.md)\. To stop the function from running, choose **Throttle** on the [function configuration page](configuration-console.md)\. Then identify the code path or configuration error that caused the recursive invocation\.

**Error:** *KMSDisabledException: Lambda was unable to decrypt the environment variables because the KMS key used is disabled\. Please check the function's KMS key settings\.*

This error can occur if your KMS key is disabled, or if the grant that allows Lambda to use the key is revoked\. If the grant is missing, configure the function to use a different key\. Then reassign the custom key to recreate the grant\.

**Error:** *EFSMountFailureException: The function could not mount the EFS file system with access point arn:aws:elasticfilesystem:us\-east\-2:123456789012:access\-point/fsap\-015cxmplb72b405fd\.*

The mount request to the function's [file system](configuration-filesystem.md) was rejected\. Check the function's permissions, and confirm that its file system and access point exist and are ready for use\.

**Error:** *EFSMountConnectivityException: The function couldn't connect to the Amazon EFS file system with access point arn:aws:elasticfilesystem:us\-east\-2:123456789012:access\-point/fsap\-015cxmplb72b405fd\. Check your network configuration and try again\.*

The function couldn't establish a connection to the function's [file system](configuration-filesystem.md) with the NFS protocol \(TCP port 2049\)\. Check the [security group and routing configuration](https://docs.aws.amazon.com/efs/latest/ug/network-access.html) for the VPC's subnets\.

**Error:** *EFSMountTimeoutException: The function could not mount the EFS file system with access point \{arn:aws:elasticfilesystem:us\-east\-2:123456789012:access\-point/fsap\-015cxmplb72b405fd\} due to mount time out*

The function was able to connect to the function's [file system](configuration-filesystem.md), but the mount operation timed out\. Try again after a short time and consider limiting the function's [concurrency](configuration-concurrency.md) to reduce load on the file system\.

**Error:** *EFSIOException: This function instance was stopped because Lambda detected an IO process that was taking too long\.*

A previous invocation timed out and Lambda was unable to terminate the function handler\. This issue can occur when an attached file system runs out of burst credits and the baseline throughput is insufficient\. To increase throughput, you can increase the size of the file system or use provisioned throughput\. For more information, see [Throughput](services-efs.md#services-efs-throughput)\.