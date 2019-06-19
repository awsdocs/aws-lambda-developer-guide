# AWS Lambda Function Configuration<a name="resource-model"></a>

A Lambda function consists of code and any associated dependencies\. In addition, a Lambda function also has configuration information associated with it\. Initially, you specify the configuration information when you create a Lambda function\.

**To configure function settings**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Choose a function\.

1. Configure any of the available options and then choose **Save**\.

**Function Settings**
+ **Code** – The code and dependencies of your function\. For scripting languages, you can edit your function code in the embedded [editor](code-editor.md)\. To add libraries, or for languages that the editor doesn't support, upload a [deployment package](deployment-package-v2.md)\.
+ **Runtime** – The [Lambda runtime](lambda-runtimes.md) that executes your function\.
+ **Handler** – The method that the runtime executes when your function is invoked\. The format for this value varies per language\. See [Programming Model](programming-model-v2.md) for more information\.
+ **Environment variables** – Key\-value pairs that Lambda sets in the execution environment\. [Use environment variables](env_variables.md) to extend your function's configuration outside of code\.
+ **Tags** – Key\-value pairs that Lambda attaches to your function resource\. [Use tags](tagging.md) to organize Lambda functions into groups for cost reporting and filtering in the Lambda console\.

  Tags apply to the entire function, including all versions and aliases\.
+ **Execution role** – The [IAM role](lambda-intro-execution-role.md) that AWS Lambda assumes when it executes your function\.
+ **Description** – A description of the function\.
+ **Memory** – The amount of memory available to the function during execution\. Choose an amount [between 128 MB and 3,008 MB](limits.md) in 64 MB increments\.

  Lambda allocates CPU power linearly in proportion to the amount of memory configured\. At 1,792 MB, a function has the equivalent of 1 full vCPU \(one vCPU\-second of credits per second\)\.
+ **Timeout** – The amount of time that Lambda allows a function to run before stopping it\. The default is 3 seconds\. The maximum allowed value is 900 seconds\.
+ **Virtual private cloud \(VPC\)** – If your function needs network access to resources that are not available over the internet, [configure it to connect to a VPC](vpc.md)\.
+ **Dead letter queue \(DLQ\)** – If your function is invoked asynchronously, [choose a queue or topic](dlq.md) to receive failed invocations\.
+ **Enable active tracing** – Sample incoming requests and [trace sampled requests with AWS X\-Ray](lambda-x-ray.md)\.
+ **Concurrency** – [Reserve concurrency for a function](concurrent-executions.md) to set the maximum number of simultaneous executions for a function, and reserves capacity for that concurrency level\.

  Reserved concurrency applies to the entire function, including all versions and aliases\.

Function settings can only be changed on the unpublished version of a function\. When you publish a version, code and most settings are locked to ensure a consistent experience for users of that version\. Use [aliases](versioning-aliases.md) to propagate configuration changes in a controlled manner\.

To configure functions with the Lambda API, use the following actions\.
+ [UpdateFunctionCode](API_UpdateFunctionCode.md) – Update the function's code\.
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) – Update version\-specific settings\.
+ [TagResource](API_TagResource.md) – Tag a function\.
+ [AddPermission](API_AddPermission.md) – Modify the [resource\-based policy](access-control-resource-based.md) of a function, version, or alias\.
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md) – Configure a function's reserved concurrency\.
+ [PublishVersion](API_PublishVersion.md) – Create an immutable version with the current code and configuration\.
+ [CreateAlias](API_CreateAlias.md) – Create aliases for function versions\.

For example, to update a function's memory setting with the AWS CLI, use the `update-function-configuration` command\.

```
$ aws lambda update-function-configuration --function-name my-function --memory-size 256
```

For function configuration best practices, see [Function Configuration](best-practices.md#function-configuration)\.