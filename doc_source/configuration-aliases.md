# Lambda function aliases<a name="configuration-aliases"></a>

You can create one or more aliases for your Lambda function\. A Lambda alias is like a pointer to a specific function version with the exception that it can point to two function versions during [traffic shifting](https://aws.amazon.com/about-aws/whats-new/2017/11/aws-lambda-supports-traffic-shifting-and-phased-deployments-with-aws-codedeploy/)\. Users can access the function version using the alias Amazon Resource Name \(ARN\)\.

**Topics**
+ [Creating a function alias \(Console\)](#configuration-aliases-config)
+ [Managing aliases with the Lambda API](#versioning-aliases-api)
+ [Using aliases](#using-aliases)
+ [Resource policies](#versioning-permissions-alias)
+ [Alias routing configuration](#configuring-alias-routing)

## Creating a function alias \(Console\)<a name="configuration-aliases-config"></a>

You can create a function alias using the Lambda console\.

**To create an alias**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Aliases** and then choose **Create alias**\.

1. On the **Create alias** page, do the following:

   1. Enter a **Name** for the alias\.

   1. \(Optional\) Enter a **Description** for the alias\.

   1. For **Version**, choose a function version that you want the alias to point to\.

   1. \(Optional\) To configure routing on the alias, expand **Weighted alias**\. For more information, see [Alias routing configuration](#configuring-alias-routing)\.

   1. Choose **Save**\.

## Managing aliases with the Lambda API<a name="versioning-aliases-api"></a>

To create an alias using the AWS Command Line Interface \(AWS CLI\), use the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-alias.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-alias.html) command\.

```
aws lambda create-alias --function-name my-function --name alias-name --function-version version-number --description " "
```

To change an alias to point a new version of the function, use the [https://docs.aws.amazon.com/cli/latest/reference/lambda/update-alias.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/update-alias.html) command\.

```
aws lambda update-alias --function-name my-function --name alias-name --function-version version-number 
```

To delete an alias, use the [https://docs.aws.amazon.com/cli/latest/reference/lambda/delete-alias.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/delete-alias.html) command\.

```
aws lambda delete-alias --function-name my-function --name alias-name 
```

 The AWS CLI commands in the preceding steps correspond to the following Lambda API operations:
+ [CreateAlias](API_CreateAlias.md)
+ [UpdateAlias](API_UpdateAlias.md)
+ [DeleteAlias](API_DeleteAlias.md)

## Using aliases<a name="using-aliases"></a>

Each alias has a unique ARN\. An alias can point only to a function version, not to another alias\. You can update an alias to point to a new version of the function\.

Event sources such as Amazon Simple Storage Service \(Amazon S3\) invoke your Lambda function\. These event sources maintain a mapping that identifies the function to invoke when events occur\. If you specify a Lambda function alias in the mapping configuration, you don't need to update the mapping when the function version changes\. For more information, see [Lambda event source mappings](invocation-eventsourcemapping.md)\.

In a resource policy, you can grant permissions for event sources to use your Lambda function\. If you specify an alias ARN in the policy, you don't need to update the policy when the function version changes\.

## Resource policies<a name="versioning-permissions-alias"></a>

You can use a [resource\-based policy](access-control-resource-based.md) to give a service, resource, or account access to your function\. The scope of that permission depends on whether you apply it to an alias, a version, or the entire function\. For example, if you use an alias name \(such as `helloworld:PROD`\), the permission allows you to invoke the `helloworld` function using the alias ARN \(`helloworld:PROD`\)\.

If you attempt to invoke the function without an alias or a specific version, then you get a permission error\. This permission error still occurs even if you attempt to directly invoke the function version associated with the alias\.

For example, the following AWS CLI command grants Amazon S3 permissions to invoke the PROD alias of the `helloworld` function when Amazon S3 is acting on behalf of `examplebucket`\.

```
aws lambda add-permission --function-name helloworld \
--qualifier PROD --statement-id 1 --principal s3.amazonaws.com --action lambda:InvokeFunction \
--source-arn arn:aws:s3:::examplebucket --source-account 123456789012
```

For more information about using resource names in policies, see [Resources and conditions for Lambda actions](lambda-api-permissions-ref.md)\.

## Alias routing configuration<a name="configuring-alias-routing"></a>

Use routing configuration on an alias to send a portion of traffic to a second function version\. For example, you can reduce the risk of deploying a new version by configuring the alias to send most of the traffic to the existing version, and only a small percentage of traffic to the new version\.

Note that Lambda uses a simple probabilistic model to distribute the traffic between the two function versions\. At low traffic levels, you might see a high variance between the configured and actual percentage of traffic on each version\. If your function uses provisioned concurrency, you can avoid [spillover invocations](monitoring-metrics.md#monitoring-metrics-invocation) by configuring a higher number of provisioned concurrency instances during the time that alias routing is active\. 

You can point an alias to a maximum of two Lambda function versions\. The versions must meet the following criteria:
+ Both versions must have the same [execution role](lambda-intro-execution-role.md)\.
+ Both versions must have the same [dead\-letter queue](invocation-async.md#invocation-dlq) configuration, or no dead\-letter queue configuration\.
+ Both versions must be published\. The alias cannot point to `$LATEST`\.

**To configure routing on an alias**
**Note**  
Verify that the function has at least two published versions\. To create additional versions, follow the instructions in [Lambda function versions](configuration-versions.md)\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Aliases** and then choose **Create alias**\.

1. On the **Create alias** page, do the following:

   1. Enter a **Name** for the alias\.

   1. \(Optional\) Enter a **Description** for the alias\.

   1. For **Version**, choose the first function version that you want the alias to point to\.

   1. Expand **Weighted alias**\.

   1. For **Additional version**, choose the second function version that you want the alias to point to\.

   1. For **Weight \(%\)**, enter a weight value for the function\. *Weight* is the percentage of traffic that is assigned to that version when the alias is invoked\. The first version receives the residual weight\. For example, if you specify 10 percent to **Additional version**, the first version is assigned 90 percent automatically\.

   1. Choose **Save**\.

### Configuring alias routing using CLI<a name="configuring-routing"></a>

Use the `create-alias` and `update-alias` AWS CLI commands to configure the traffic weights between two function versions\. When you create or update the alias, you specify the traffic weight in the `routing-config` parameter\.

The following example creates a Lambda function alias named **routing\-alias** that points to version 1 of the function\. Version 2 of the function receives 3 percent of the traffic\. The remaining 97 percent of traffic is routed to version 1\.

```
aws lambda create-alias --name routing-alias --function-name my-function --function-version 1  \
--routing-config AdditionalVersionWeights={"2"=0.03}
```

Use the `update-alias` command to increase the percentage of incoming traffic to version 2\. In the following example, you increase the traffic to 5 percent\.

```
aws lambda update-alias --name routing-alias --function-name my-function  \
--routing-config AdditionalVersionWeights={"2"=0.05}
```

To route all traffic to version 2, use the `update-alias` command to change the `function-version` property to point the alias to version 2\. The command also resets the routing configuration\.

```
aws lambda update-alias --name routing-alias --function-name my-function  \
--function-version 2 --routing-config AdditionalVersionWeights={}
```

 The AWS CLI commands in the preceding steps correspond to the following Lambda API operations:
+ [CreateAlias](API_CreateAlias.md)
+ [UpdateAlias](API_UpdateAlias.md)

### Determining which version has been invoked<a name="determining-routing-version"></a>

When you configure traffic weights between two function versions, there are two ways to determine the Lambda function version that has been invoked:
+ **CloudWatch Logs** – Lambda automatically emits a `START` log entry that contains the invoked version ID to Amazon CloudWatch Logs for every function invocation\. The following is an example:

  `19:44:37 START RequestId: request id Version: $version ` 

  For alias invocations, Lambda uses the `Executed Version` dimension to filter the metric data by the invoked version\. For more information, see [Working with Lambda function metrics](monitoring-metrics.md)\.
+ **Response payload \(synchronous invocations\)** – Responses to synchronous function invocations include an `x-amz-executed-version` header to indicate which function version has been invoked\.

 
