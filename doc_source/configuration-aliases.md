# AWS Lambda Function Aliases<a name="configuration-aliases"></a>

You can create one or more aliases for your AWS Lambda function\. A Lambda alias is like a pointer to a specific Lambda function version\. Users can access the function version using the alias ARN\. 

**To create an alias**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. In **Actions**, choose **Create alias**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/version-actions.png)

1. In the **Create a new alias** form, enter a name for the alias and an optional description\. Choose the function version for this alias\.

To view the aliases that are currently defined for a function, choose **Qualifiers**, and choose the **Aliases** tab\.

## Managing Aliases with the Lambda API<a name="versioning-aliases-api"></a>

To create an alias, use the `create-alias` command\.

```
$ aws lambda create-alias   --function-name my-function --name alias-name --function-version version-number  --description " "
```

To change an alias to point a new version of the function, use the `update-alias` command\. 

```
$ aws lambda update-alias  --function-name my-function --name alias-name --function-version version-number 
```

 The AWS CLI commands in the preceding steps correspond to the following AWS Lambda APIs: 
+ [CreateAlias](API_CreateAlias.md)
+ [UpdateAlias](API_UpdateAlias.md)

## Using Aliases<a name="using-aliases"></a>

Each alias has a unique ARN\. An alias can only point to a function version, not to another alias\. You can update an alias to point to a new version of the function\.

Event sources such as Amazon S3 invoke your Lambda function\. These event sources maintain a mapping that identifies the function to invoke when events occur\. If you specify a Lambda function alias in the mapping configuration, you don't need to update the mapping when the function version changes\. 

In a resource policy, you can grant permissions for event sources to use your Lambda function\. If you specify an alias ARN in the policy, you don't need to update the policy when the function version changes\. 

## Resource Policies<a name="versioning-permissions-alias"></a>

When you use a [resource\-based policy](access-control-resource-based.md) to give a service, resource, or account access to your function, the scope of that permission depends on whether you applied it to an alias, to a version, or to the function\. If you use an alias name \(such as `helloworld:PROD`\), the permission is valid only for invoking the `helloworld` function using the alias ARN\. You get a permission error if you use a version ARN or the function ARN\. This includes the version ARN that the alias points to\.

For example, the following AWS CLI command grants Amazon S3 permissions to invoke the PROD alias of the `helloworld` Lambda function\. Note that the `--qualifier` parameter specifies the alias name\. 

```
$ aws lambda add-permission --function-name helloworld \
--qualifier PROD --statement-id 1 --principal s3.amazonaws.com --action lambda:InvokeFunction \
--source-arn arn:aws:s3:::examplebucket --source-account 123456789012
```

In this case, Amazon S3 is now able to invoke the PROD alias\. Lambda can then execute the `helloworld` Lambda function version that the PROD alias references\. For this to work correctly, you must use the PROD alias ARN in the S3 bucket's notification configuration\.

## Alias Routing Configuration<a name="configuring-alias-routing"></a>

Use routing configuration on an alias to send a portion of traffic to a second function version\. For example, you can reduce the risk of deploying a new version by configuring the alias to send most of the traffic to the existing version, and only a small percentage of traffic to the new version\. 

You can point an alias to a maximum of two Lambda function versions\. The versions must meet the following criteria:
+ Both versions must have the same IAM execution role\.
+ Both versions must have the same [dead\-letter queue](https://docs.aws.amazon.com/lambda/latest/dg/invocation-async.html#dlq) configuration, or no dead\-letter queue configuration\.
+ Both versions must be published\. The alias cannot point to `$LATEST`\.

**To configure routing on an alias**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Verify that the function has at least two published versions\. To do this, choose **Qualifiers** and then choose **Versions** to display the list of versions\. If you need to create additional versions, follow the instructions in [AWS Lambda Function Versions](configuration-versions.md)\.

1. On the **Actions** menu, choose **Create alias**\.

1. In the **Create a new alias** window, enter a value for **Name**, optionally enter a value for **Description**, and choose the **Version** of the Lambda function that the alias references\.

1. Under **Additional version**, specify the following items:

   1. Choose the second Lambda function version\.

   1. Enter a weight value for the function\. *Weight* is the percentage of traffic that is assigned to that version when the alias is invoked\. The first version receives the residual weight\. For example, if you specify 10 percent to **Additional version**, the first version is assigned 90 percent automatically\.

1. Choose **Create**\.

### Configuring Alias Routing<a name="configuring-routing"></a>

Use the `create-alias` and `update-alias` commands to configure the traffic weights between two function versions\. When you create or update the alias, you specify the traffic weight in the `routing-config` parameter\. 

The following example creates an alias \(named **routing\-alias**\) for a Lambda function\. The alias points to version 1 of the function\. Version 2 of the function receives 3 percent of the traffic\. The remaining 97 percent of traffic is routed to version 1\. 

```
$ aws lambda create-alias --name  routing-alias  --function-name  my-function  --function-version  1  \
--routing-config  AdditionalVersionWeights={"2"=0.03}
```

Use the `update-alias` command to increase the percentage of incoming traffic to version 2\. In the following example, you increase the traffic to 5 percent\.

```
$ aws lambda update-alias --name  routing-alias  --function-name  my-function  \
--routing-config  AdditionalVersionWeights={"2"=0.05}
```

To route all traffic to version 2, use the `UpdateAlias` command to change the `function-version` property to point the alias to version 2\. The command also resets the routing configuration\.

```
$ aws lambda update-alias --name  routing-alias  --function-name  my-function  \ 
--function-version  2   --routing-config  AdditionalVersionWeights={}
```

 The CLI commands in the preceding steps correspond to the following AWS Lambda API operations: 
+ [CreateAlias](API_CreateAlias.md)
+ [UpdateAlias](API_UpdateAlias.md)

### Determining Which Version Has Been Invoked<a name="determining-routing-version"></a>

When you configure traffic weights between two function versions, there are two ways to determine the Lambda function version that has been invoked:
+ **CloudWatch Logs** – Lambda automatically emits a `START` log entry that contains the invoked version ID to CloudWatch Logs for every function invocation\. The following is an example:

  `19:44:37 START RequestId: request id Version: $version ` 

  For alias invocations, Lambda uses the `Executed Version` dimension to filter the metric data by the executed version\. For more information, see [Working with AWS Lambda Function Metrics](monitoring-metrics.md)\.
+ **Response payload \(synchronous invocations\)** – Responses to synchronous function invocations include an `x-amz-executed-version` header to indicate which function version has been invoked\.