# Configuring functions in the AWS Lambda console<a name="configuration-console"></a>

You can use the Lambda console to configure function settings, add triggers and destinations, and update and test your code\.

To manage a function, open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions) and choose a function\. The function designer is at the top of the configuration page\.

![\[The function designer in the AWS Lambda console.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-designer.png)

The designer shows an overview of your function and its upstream and downstream resources\. You can use it to configure triggers, layers, and destinations\.
+ **Triggers** – Triggers are services and resources that you have configured to invoke your function\. Choose **Add trigger** to create a Lambda [event source mapping](invocation-eventsourcemapping.md) or to configure a trigger in another service that the Lambda console integrates with\. For details about these services and others, see [Using AWS Lambda with other services](lambda-services.md)\.
+ **Layers** – Choose the **Layers** node to add [layers](configuration-layers.md) to your application\. A layer is a ZIP archive that contains libraries, a custom runtime, or other dependencies\.
+ **Destinations** – Add a destination to your function to send details about invocation results to another service\. You can send invocation records when your function is invoked [asynchronously](invocation-async.md), or by an [event source mapping](invocation-eventsourcemapping.md) that reads from a stream\.

With the function node selected in the designer, you can modify the following settings\.

**Function settings**
+ **Code** – The code and dependencies of your function\. For scripting languages, you can edit your function code in the embedded [editor](code-editor.md)\. To add libraries, or for languages that the editor doesn't support, upload a [deployment package](gettingstarted-features.md#gettingstarted-features-package)\. If your deployment package is larger than 50 MB, choose **Upload a file from Amazon S3**\.
+ **Runtime** – The [Lambda runtime](lambda-runtimes.md) that executes your function\.
+ **Handler** – The method that the runtime executes when your function is invoked, such as `index.handler`\. The first value is the name of the file or module\. The second value is the name of the method\.
+ **Environment variables** – Key\-value pairs that Lambda sets in the execution environment\. [ Use environment variables](configuration-envvars.md) to extend your function's configuration outside of code\.
+ **Tags** – Key\-value pairs that Lambda attaches to your function resource\. [Use tags](configuration-tags.md) to organize Lambda functions into groups for cost reporting and filtering in the Lambda console\.

  Tags apply to the entire function, including all versions and aliases\.
+ **Execution role** – The [IAM role](lambda-intro-execution-role.md) that AWS Lambda assumes when it executes your function\.
+ **Description** – A description of the function\.
+ **Memory**– The amount of memory available to the function during execution\. Choose an amount [between 128 MB and 3,008 MB](gettingstarted-limits.md) in 64\-MB increments\.

  Lambda allocates CPU power linearly in proportion to the amount of memory configured\. At 1,792 MB, a function has the equivalent of one full vCPU \(one vCPU\-second of credits per second\)\.
+ **Timeout** – The amount of time that Lambda allows a function to run before stopping it\. The default is 3 seconds\. The maximum allowed value is 900 seconds\.
+ **Virtual private cloud \(VPC\)** – If your function needs network access to resources that are not available over the internet, [configure it to connect to a VPC](configuration-vpc.md)\.
+ **Database proxies** – [Create a database proxy](configuration-database.md) for functions that use an Amazon RDS DB instance or cluster\.
+ **Active tracing** – Sample incoming requests and [trace sampled requests with AWS X\-Ray](services-xray.md)\.
+ **Concurrency** – [Reserve concurrency for a function](configuration-concurrency.md) to set the maximum number of simultaneous executions for a function\. Provision concurrency to ensure that a function can scale without fluctuations in latency\. 

  Reserved concurrency applies to the entire function, including all versions and aliases\.
+ **Asynchronous invocation** – [Configure error handling behavior](invocation-async.md) to reduce the number of retries that Lambda attempts, or the amount of time that unprocessed events stay queued before Lambda discards them\. [Configure a dead\-letter queue](invocation-async.md#dlq) to retain discarded events\.

  You can configure error handling settings on a function, version, or alias\.

Except as noted in the preceding list, you can only change function settings on the unpublished version of a function\. When you publish a version, code and most settings are locked to ensure a consistent experience for users of that version\. Use [aliases](configuration-aliases.md) to propagate configuration changes in a controlled manner\.

To configure functions with the Lambda API, use the following actions:
+ [UpdateFunctionCode](API_UpdateFunctionCode.md) – Update the function's code\.
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) – Update version\-specific settings\.
+ [TagResource](API_TagResource.md) – Tag a function\.
+ [AddPermission](API_AddPermission.md) – Modify the [resource\-based policy](access-control-resource-based.md) of a function, version, or alias\.
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md) – Configure a function's reserved concurrency\.
+ [PublishVersion](API_PublishVersion.md) – Create an immutable version with the current code and configuration\.
+ [CreateAlias](API_CreateAlias.md) – Create aliases for function versions\.
+ [PutFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutFunctionEventInvokeConfig.html) – Configure error handling for asynchronous invocation\.

For example, to update a function's memory setting with the AWS CLI, use the `update-function-configuration` command\.

```
$ aws lambda update-function-configuration --function-name my-function --memory-size 256
```

For function configuration best practices, see [Function configuration](best-practices.md#function-configuration)\.