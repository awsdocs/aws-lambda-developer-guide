# Invoking functions defined as container images<a name="invocation-images"></a>

For a Lambda function defined as a container image, function behavior during invocation is very similar to a function defined as a \.zip file archive\. The following sections highlight the similarities and differences\.

**Topics**
+ [Function lifecycle](#invocation-images-lifecycle)
+ [Invoking the function](#invocation-images-update)
+ [Image security](#invocation-images-security)

## Function lifecycle<a name="invocation-images-lifecycle"></a>

After you upload a new or updated container image, Lambda optimizes the image before the function can process invocations\. The optimization process can take a few seconds\. The function remains in the `Pending` state until the process completes\. The function then transitions to the `Active` state\. While the state is `Pending`, you can invoke the function, but other operations on the function fail\. Invocations that occur while an image update is in progress run the code from the previous image\.

If a function is not invoked for multiple weeks, Lambda reclaims its optimized version, and the function transitions to the `Inactive` state\. To reactivate the function, you must invoke it\. Lambda rejects the first invocation and the function enters the `Pending` state until Lambda re\-optimizes the image\. The function then returns to the `Active` state\.

Lambda periodically fetches the associated container image from the Amazon Elastic Container Registry \(Amazon ECR\) repository\. If the corresponding container image no longer exists on Amazon ECR or permissions are revoked, the function enters the `Failed` state, and Lambda returns a failure for any function invocations\.

You can use the Lambda API to get information about a function's state\. For more information, see [Lambda function states](functions-states.md)\.

## Invoking the function<a name="invocation-images-update"></a>

When you invoke the function, Lambda deploys the container image to an execution environment\. Lambda initializes any [extensions](extensions-configuration.md#invocation-extensions-images) and then runs the function’s initialization code \(the code outside the main handler\)\. Note that function initialization duration is included in billed execution time\. 

Lambda then runs the function by calling the code entry point specified in the function configuration \(the ENTRYPOINT and CMD [container image settings](images-create.md#images-parms)\)\. 

## Image security<a name="invocation-images-security"></a>

When Lambda first downloads the container image from its original source \(Amazon ECR\), the container image is optimized, encrypted, and stored using authenticated convergent encryption methods\. All keys that are required to decrypt customer data are protected using AWS KMS customer managed keys\. To track and audit Lambda's usage of customer managed keys, you can view the [AWS CloudTrail logs](logging-using-cloudtrail.md)\.