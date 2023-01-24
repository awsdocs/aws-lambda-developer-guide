# Troubleshoot invocation issues in Lambda<a name="troubleshooting-invocation"></a>

When you invoke a Lambda function, Lambda validates the request and checks for scaling capacity before sending the event to your function or, for asynchronous invocation, to the event queue\. Invocation errors can be caused by issues with request parameters, event structure, function settings, user permissions, resource permissions, or limits\.

If you invoke your function directly, you see any invocation errors in the response from Lambda\. If you invoke your function asynchronously with an event source mapping or through another service, you might find errors in logs, a dead\-letter queue, or a failed\-event destination\. Error handling options and retry behavior vary depending on how you invoke your function and on the type of error\.

For a list of error types that the `Invoke` operation can return, see [Invoke](API_Invoke.md)\.

## IAM: lambda:InvokeFunction not authorized<a name="troubleshooting-invocation-noauth"></a>

**Error:** *User: arn:aws:iam::123456789012:user/developer is not authorized to perform: lambda:InvokeFunction on resource: my\-function*

Your AWS Identity and Access Management \(IAM\) user, or the role that you assume, must have permission to invoke a function\. This requirement also applies to Lambda functions and other compute resources that invoke functions\. Add the AWS managed policy **AWSLambdaRole** to your IAM user, or add a custom policy that allows the `lambda:InvokeFunction` action on the target function\.

**Note**  
Unlike other Lambda API operations, the name of the IAM action \(`lambda:InvokeFunction`\) doesn't match the name of the API operation \(`Invoke`\) for invoking a function\.

For more information, see [Lambda permissions](lambda-permissions.md)\.

## Lambda: Operation cannot be performed ResourceConflictException<a name="troubleshooting-invocation-ResourceConflictException"></a>

**Error:** *ResourceConflictException: The operation cannot be performed at this time\. The function is currently in the following state: Pending*

When you connect a function to a virtual private cloud \(VPC\) at the time of creation, the function enters a `Pending` state while Lambda creates elastic network interfaces\. During this time, you can't invoke or modify your function\. If you connect your function to a VPC after creation, you can invoke it while the update is pending, but you can't modify its code or configuration\.

For more information, see [Lambda function states](functions-states.md)\.

## Lambda: Function is stuck in Pending<a name="troubleshooting-invocation-pending"></a>

**Error:** *A function is stuck in the `Pending` state for several minutes\.*

If a function is stuck in the `Pending` state for more than six minutes, call one of the following API operations to unblock it:
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [PublishVersion](API_PublishVersion.md)

Lambda cancels the pending operation and puts the function into the `Failed` state\. You can then delete the function and recreate it, or attempt another update\.

## Lambda: One function is using all concurrency<a name="troubleshooting-invocation-allconcurrency"></a>

**Issue:** *One function is using all of the available concurrency, causing other functions to be throttled\.*

To divide your AWS account's available concurrency in an AWS Region into pools, use [reserved concurrency](configuration-concurrency.md)\. Reserved concurrency ensures that a function can always scale to its assigned concurrency, and that it doesn't scale beyond its assigned concurrency\.

## General: Cannot invoke function with other accounts or services<a name="troubleshooting-invocation-cannotinvoke"></a>

**Issue:** *You can invoke your function directly, but it doesn't run when another service or account invokes it\.*

You grant [other services](lambda-services.md) and accounts permission to invoke a function in the function's [resource\-based policy](access-control-resource-based.md)\. If the invoker is in another account, that user must also have [permission to invoke functions](access-control-identity-based.md)\.

## General: Function invocation is looping<a name="troubleshooting-invocation-loop"></a>

**Issue:** *Function is invoked continuously in a loop\.*

This typically occurs when your function manages resources in the same AWS service that triggers it\. For example, it's possible to create a function that stores an object in an Amazon Simple Storage Service \(Amazon S3\) bucket that's configured with a [notification that invokes the function again](with-s3.md)\. To stop the function from running, on the [function configuration page](configuration-function-common.md), choose **Throttle**\. Then, identify the code path or configuration error that caused the recursive invocation\.

## Lambda: Alias routing with provisioned concurrency<a name="troubleshooting-invocation-alias"></a>

**Issue:** *Provisioned concurrency spillover invocations during alias routing\.*

Lambda uses a simple probabilistic model to distribute the traffic between the two function versions\. At low traffic levels, you might see a high variance between the configured and actual percentage of traffic on each version\. If your function uses provisioned concurrency, you can avoid [spillover invocations](monitoring-metrics.md#monitoring-metrics-invocation) by configuring a higher number of provisioned concurrency instances during the time that alias routing is active\. 

## Lambda: Cold starts with provisioned concurrency<a name="troubleshooting-invocation-coldstart"></a>

**Issue:** *You see cold starts after enabling provisioned concurrency\.*

When the number of concurrent executions on a function is less than or equal to the [configured level of provisioned concurrency](provisioned-concurrency.md), there shouldn't be any cold starts\. To help you confirm if provisioned concurrency is operating normally, do the following:
+ [Check that provisioned concurrency is enabled](provisioned-concurrency.md) on the function version or alias\.
**Note**  
Provisioned concurrency is not configurable on the [$LATEST version](gettingstarted-images.md#configuration-images-latest)\.
+ Ensure that your triggers invoke the correct function version or alias\. For example, if you're using Amazon API Gateway, check that API Gateway invokes the function version or alias with provisioned concurrency, not $LATEST\. To confirm that provisioned concurrency is being used, you can check the [ProvisionedConcurrencyInvocations Amazon CloudWatch metric](monitoring-metrics.md#monitoring-metrics-invocation)\. A non\-zero value indicates that the function is processing invocations on initialized execution environments\.
+ Determine whether your function concurrency exceeds the configured level of provisioned concurrency by checking the [ProvisionedConcurrencySpilloverInvocations CloudWatch metric](monitoring-metrics.md#monitoring-metrics-invocation)\. A non\-zero value indicates that all provisioned concurrency is in use and some invocation occurred with a cold start\.
+ Check your [invocation frequency](gettingstarted-limits.md) \(requests per second\)\. Functions with provisioned concurrency have a maximum rate of 10 requests per second per provisioned concurrency\. For example, a function configured with 100 provisioned concurrency can handle 1,000 requests per second\. If the invocation rate exceeds 1,000 requests per second, some cold starts can occur\.

**Note**  
There is a known issue in which the first invocation on an initialized execution environment reports a non\-zero **Init Duration** metric in CloudWatch Logs, even though no cold start has occurred\. We're developing a fix to correct the reporting to CloudWatch Logs\.

## Lambda: Latency variability with provisioned concurrency<a name="troubleshooting-invocation-latency"></a>

**Issue:** *You see latency variability on the first invocation after enabling provisioned concurrency\.*

Depending on your function's runtime and memory configuration, it's possible to see some latency variability on the first invocation on an initialized execution environment\. For example, \.NET and other JIT runtimes can lazily load resources on the first invocation, leading to some latency variability \(typically tens of milliseconds\)\. This variability is more apparent on 128\-MiB functions\. You mitigate this by increasing the function's configured memory\.

## Lambda: Cold starts with new versions<a name="troubleshooting-invocation-newversion"></a>

**Issue:** *You see cold starts while deploying new versions of your function\.*

When you update a function alias, Lambda automatically shifts provisioned concurrency to the new version based on the weights configured on the alias\.

**Error:** *KMSDisabledException: Lambda was unable to decrypt the environment variables because the KMS key used is disabled\. Please check the function's KMS key settings\.*

This error can occur if your AWS Key Management Service \(AWS KMS\) key is disabled, or if the grant that allows Lambda to use the key is revoked\. If the grant is missing, configure the function to use a different key\. Then, reassign the custom key to recreate the grant\.

## EFS: Function could not mount the EFS file system<a name="troubleshooting-invocation-efsmount"></a>

**Error:** *EFSMountFailureException: The function could not mount the EFS file system with access point arn:aws:elasticfilesystem:us\-east\-2:123456789012:access\-point/fsap\-015cxmplb72b405fd\.*

The mount request to the function's [file system](configuration-filesystem.md) was rejected\. Check the function's permissions, and confirm that its file system and access point exist and are ready for use\.

## EFS: Function could not connect to the EFS file system<a name="troubleshooting-invocation-efsconnect"></a>

**Error:** *EFSMountConnectivityException: The function couldn't connect to the Amazon EFS file system with access point arn:aws:elasticfilesystem:us\-east\-2:123456789012:access\-point/fsap\-015cxmplb72b405fd\. Check your network configuration and try again\.*

The function couldn't establish a connection to the function's [file system](configuration-filesystem.md) with the NFS protocol \(TCP port 2049\)\. Check the [security group and routing configuration](https://docs.aws.amazon.com/efs/latest/ug/network-access.html) for the VPC's subnets\.

## EFS: Function could not mount the EFS file system due to timeout<a name="troubleshooting-invocation-efstimeout"></a>

**Error:** *EFSMountTimeoutException: The function could not mount the EFS file system with access point \{arn:aws:elasticfilesystem:us\-east\-2:123456789012:access\-point/fsap\-015cxmplb72b405fd\} due to mount time out\.*

The function could connect to the function's [file system](configuration-filesystem.md), but the mount operation timed out\. Try again after a short time and consider limiting the function's [concurrency](configuration-concurrency.md) to reduce load on the file system\.

## Lambda: Lambda detected an IO process that was taking too long<a name="troubleshooting-invocation-ioprocess"></a>

*EFSIOException: This function instance was stopped because Lambda detected an IO process that was taking too long\.*

A previous invocation timed out and Lambda couldn't terminate the function handler\. This issue can occur when an attached file system runs out of burst credits and the baseline throughput is insufficient\. To increase throughput, you can increase the size of the file system or use provisioned throughput\. For more information, see [Throughput](services-efs.md#services-efs-throughput)\.