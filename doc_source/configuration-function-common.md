# Configuring Lambda function options<a name="configuration-function-common"></a>

After you create a function, you can configure additional capabilities for the function, such as triggers, network access, and file system access\. You can also adjust resources associated with the function, such as memory and concurrency\. These configurations apply to functions defined as \.zip file archives and to functions defined as container images\.

You can also create and edit test events to test your function using the console\.

For function configuration best practices, see [Function configuration](best-practices.md#function-configuration)\.

**Topics**
+ [Function versions](#configuration-function-latest)
+ [Using the function overview](#configuration-functions-designer)
+ [Configuring functions \(console\)](#configuration-common-summary)
+ [Configuring functions \(API\)](#configuration-function-api)
+ [Configuring function memory \(console\)](#configuration-memory-console)
+ [Configuring function timeout \(console\)](#configuration-timeout-console)
+ [Configuring ephemeral storage \(console\)](#configuration-ephemeral-storage)
+ [Accepting function memory recommendations \(console\)](#configuration-memory-optimization-accept)
+ [Configuring triggers \(console\)](#configuration-common-triggers)
+ [Testing functions \(console\)](#configuration-common-test)

## Function versions<a name="configuration-function-latest"></a>

A function has an unpublished version, and can have published versions and aliases\. By default, the console displays configuration information for the unpublished version of the function\. You change the unpublished version when you update your function's code and configuration\. 

A published version is a snapshot of your function code and configuration that can't be changed \(except for a few configuration items relevant to a function version, such as provisioned concurrency\)\. 

## Using the function overview<a name="configuration-functions-designer"></a>

The **Function overview** shows a visualization of your function and its upstream and downstream resources\. You can use it to jump to trigger and destination configuration\. You can use it to jump to layer configuration for functions defined as \.zip file archives\.

![\[The Lambda function overview, with no configured triggers or destinations.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-designer.png)

## Configuring functions \(console\)<a name="configuration-common-summary"></a>

For the following function configurations, you can change the settings only for the unpublished version of a function\. In the console, the function **Configuration** tab provides the following sections:
+ **General configuration** – Configure [memory](#configuration-memory-console) or opt in to the [AWS Compute Optimizer](#configuration-memory-optimization-accept)\. You can also configure function [timeout](#configuration-timeout-console) and the execution role\.
+ **Permissions** – Configure the execution role and other [permissions](lambda-permissions.md)\.
+ **Environment variables** – Key\-value pairs that Lambda sets in the execution environment\. To extend your function's configuration outside of code, [use environment variables](configuration-envvars.md)\. 
+ **Tags** – Key\-value pairs that Lambda attaches to your function resource\. [Use tags](configuration-tags.md) to organize Lambda functions into groups for cost reporting and filtering in the Lambda console\.

  Tags apply to the entire function, including all versions and aliases\.
+ **Virtual private cloud \(VPC\)** – If your function needs network access to resources that are not available over the internet, [configure it to connect to a virtual private cloud \(VPC\)](configuration-vpc.md)\.
+ **Monitoring and operations tools** – configure CloudWatch and other [monitoring tools](lambda-monitoring.md)\.
+ **Concurrency** – [Reserve concurrency for a function](configuration-concurrency.md) to set the maximum number of simultaneous executions for a function\. Provision concurrency to ensure that a function can scale without fluctuations in latency\. Reserved concurrency applies to the entire function, including all versions and aliases\.
+ **Function URL** – Configure a [function URL](lambda-urls.md) to add a unique HTTP\(S\) endpoint to your Lambda function\. You can configure a function URL on the `$LATEST` unpublished function version, or on any function alias\.

You can configure the following options on a function, a function version, or an alias\.
+ **Triggers** – Configure [triggers](#configuration-common-triggers)\.
+ **Destinations** – Configure [destinations](invocation-async.md#invocation-async-destinations) for asynchronous invocations \.
+ **Asynchronous invocation** – [Configure error handling behavior](invocation-async.md) to reduce the number of retries that Lambda attempts, or the amount of time that unprocessed events stay queued before Lambda discards them\. [Configure a dead\-letter queue](invocation-async.md#invocation-dlq) to retain discarded events\.
+ **Code signing** – To use [Code signing](configuration-codesigning.md) with your function, configure the function to include a code\-signing configuration\. 
+ **Database proxies** – [Create a database proxy](configuration-database.md) for functions that use an Amazon RDS DB instance or cluster\.
+ **File systems** – Connect your function to a [file system](configuration-filesystem.md)\.
+ **State machines** – Use a state machine to orchestrate and apply error handling to your function\.

The console provides separate tabs to configure aliases and versions: 
+ **Aliases** – An alias is a named resource that maps to a function version\. You can change an alias to map to a different function version\.
+ **Versions** – Lambda assigns a new version number each time you publish your function\. For more information about managing versions, see [Lambda function versions](configuration-versions.md)\.

You can configure the following items for a published function version:
+ Triggers
+ Destinations
+ Provisioned concurrency
+ Asynchronous invocation
+ Database proxies

## Configuring functions \(API\)<a name="configuration-function-api"></a>

To configure functions with the Lambda API, use the following actions:
+ [UpdateFunctionCode](API_UpdateFunctionCode.md) – Update the function's code\.
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) – Update version\-specific settings\.
+ [TagResource](API_TagResource.md) – Tag a function\.
+ [AddPermission](API_AddPermission.md) – Modify the [resource\-based policy](access-control-resource-based.md) of a function, version, or alias\.
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md) – Configure a function's reserved concurrency\.
+ [PublishVersion](API_PublishVersion.md) – Create an immutable version with the current code and configuration\.
+ [CreateAlias](API_CreateAlias.md) – Create aliases for function versions\.
+ [PutFunctionEventInvokeConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutFunctionEventInvokeConfig.html) – Configure error handling for asynchronous invocation\.
+ [CreateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateFunctionUrlConfig.html) – Create a function URL configuration\.
+ [UpdateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateFunctionUrlConfig.html) – Update an existing function URL configuration\.

## Configuring function memory \(console\)<a name="configuration-memory-console"></a>

Lambda allocates CPU power in proportion to the amount of memory configured\. *Memory* is the amount of memory available to your Lambda function at runtime\. You can increase or decrease the memory and CPU power allocated to your function using the **Memory \(MB\)** setting\. To configure the memory for your function, set a value between 128 MB and 10,240 MB in 1\-MB increments\. At 1,769 MB, a function has the equivalent of one vCPU \(one vCPU\-second of credits per second\)\.

You can configure the memory of your function in the Lambda console\.

**To update the memory of a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. On the function configuration page, on the **General configuration** pane, choose **Edit**\.

1. For **Memory \(MB\)**, set a value from 128 MB to 10,240 MB\.

1. Choose **Save**\.

## Configuring function timeout \(console\)<a name="configuration-timeout-console"></a>

Lambda runs your code for a set amount of time before timing out\. *Timeout* is the maximum amount of time in seconds that a Lambda function can run\. The default value for this setting is 3 seconds, but you can adjust this in increments of 1 second up to a maximum value of 15 minutes\.

You can configure the timeout of your function in the Lambda console\.

**To change the timeout of a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. On the function configuration page, on the **General configuration** pane, choose **Edit**\.

1. For **Timeout**, set a value from 1 second to 15 minutes\.

1. Choose **Save**\.

## Configuring ephemeral storage \(console\)<a name="configuration-ephemeral-storage"></a>

 By default, Lambda allocates 512 MB for a function’s /tmp directory\. You can increase or decrease this amount using the **Ephemeral storage \(MB\)** setting\. To configure the size of a function’s /tmp directory, set a whole number value between 512 MB and 10,240 MB, in 1\-MB increments\. 

**Note**  
 Configuring ephemeral storage past the default 512 MB allocated incurs a cost\. For more information, see [Lambda pricing](https://aws.amazon.com/lambda/pricing)\. 

You can configure the size of a function’s /tmp directory in the Lambda console\.

**To update the size of a function’s /tmp directory**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. On the function configuration page, on the **General configuration** pane, choose **Edit**\.

1. For **Ephemeral storage \(MB\)**, set a value from 512 MB to 10,240 MB\.

1. Choose **Save**\.

## Accepting function memory recommendations \(console\)<a name="configuration-memory-optimization-accept"></a>

If you have administrator permissions in AWS Identity and Access Management \(IAM\), you can opt in to receive Lambda function memory setting recommendations from AWS Compute Optimizer\. For instructions on opting in to memory recommendations for your account or organization, see [Opting in your account](https://docs.aws.amazon.com/compute-optimizer/latest/ug/getting-started.html#account-opt-in) in the *AWS Compute Optimizer User Guide*\.

**Note**  
Compute Optimizer supports only functions that use x86\_64 architecture\.

When you've opted in and your [Lambda function meets Compute Optimizer requirements](https://docs.aws.amazon.com/compute-optimizer/latest/ug/requirements.html#requirements-lambda-functions), you can view and accept function memory recommendations from Compute Optimizer in the Lambda console\.

**To accept a function memory recommendation**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. On the function configuration page, on the **General configuration** pane, choose **Edit**\.

1. Under **Memory \(MB\)**, in the memory alert, choose **Update**\.

1. Choose **Save**\.

## Configuring triggers \(console\)<a name="configuration-common-triggers"></a>

You can configure other AWS services to trigger your function each time a specified event occurs\.

For details about how services trigger Lambda functions, see [Using AWS Lambda with other services](lambda-services.md)\.

**To add a trigger to your function\.**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function to update\.

1. Under **Function overview**, choose **Add trigger**\.

1. From the drop\-down list of triggers, choose a trigger\. The console displays additional configuration fields required for this trigger\.

1. Choose **Add**\.

## Testing functions \(console\)<a name="configuration-common-test"></a>

You can create test events for your function from the **Test** tab\. For more information, see [Testing Lambda functions in the console](https://docs.aws.amazon.com/lambda/latest/dg/testing-functions.html)\.

You can also invoke your function without saving your test event by choosing **Test** before saving\. This creates an unsaved test event that Lambda will preserve for the duration of the session\. You can access your unsaved test events from either the **Test** or **Code** tab\. 