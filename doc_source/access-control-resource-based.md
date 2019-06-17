# Using Resource\-based Policies for AWS Lambda<a name="access-control-resource-based"></a>

AWS Lambda supports resource\-based permissions policies for Lambda functions and layers\. Resource\-based policies let you grant usage permission to other accounts on a per\-resource basis\. You also use a resource\-based policy to allow an AWS service to invoke your function\.

For Lambda functions, you can [grant an account permission](#permissions-resource-xaccountinvoke) to invoke or manage a function\. You can add multiple statements to grant access to multiple accounts, or let any account invoke your function\. For functions that another AWS service invokes in response to activity in your account, you use the policy to [grant invoke permission to the service](#permissions-resource-serviceinvoke)\.

For Lambda layers, you use a resource\-based policy on a version of the layer to let other accounts use it\. In addition to policies that grant permission to a single account or all accounts, for layers, you can also grant permission to all accounts in an organization\.

**Note**  
You can only update resource\-based policies for Lambda resources within the scope of the [AddPermission](API_AddPermission.md) and [AddLayerVersionPermission](API_AddLayerVersionPermission.md) API actions\. You can't author policies for your Lambda resources in JSON, or use conditions that don't map to parameters for those actions\.

Resource\-based policies apply to a single function, version, alias, or layer version\. They grant permission to one or more services and accounts\. For trusted accounts that you want to have access to multiple resources, or to use API actions that resource\-based policies don't support, you can use [cross\-account roles](access-control-identity-based.md)\.

**Topics**
+ [Granting Function Access to AWS Services](#permissions-resource-serviceinvoke)
+ [Granting Function Access to Other Accounts](#permissions-resource-xaccountinvoke)
+ [Granting Layer Access to Other Accounts](#permissions-resource-xaccountlayer)
+ [Cleaning up Resource\-based Policies](#permissions-resource-cleanup)

## Granting Function Access to AWS Services<a name="permissions-resource-serviceinvoke"></a>

When you [use an AWS service to invoke your function](intro-invocation-modes.md#non-streaming-event-source-mapping), you grant permission in a statement on a resource\-based policy\. You can apply the statement to the function, or limit it to a single version or alias\.

**Note**  
When you add a trigger to your function with the Lambda console, the console updates the function's resource\-based policy to allow the service to invoke it\. To grant permissions to other accounts or services that aren't available in the Lambda console, use the AWS CLI\.

Add a statement with the `add-permission` command\. The simplest resource\-based policy statement allows a service to invoke a function\. The following command grants Amazon SNS permission to invoke a function named `my-function`\.

```
$ aws lambda add-permission --function-name my-function --action lambda:InvokeFunction --statement-id sns \
--principal sns.amazonaws.com --output text
{"Sid":"sns","Effect":"Allow","Principal":{"Service":"sns.amazonaws.com"},"Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-2:123456789012:function:my-function"}
```

This lets Amazon SNS invoke the function, but it doesn't restrict the Amazon SNS topic that triggers the invocation\. To ensure that your function is only invoked by a specific resource, specify the Amazon Resource Name \(ARN\) of the resource with the `source-arn` option\. The following command only allows Amazon SNS to invoke the function for subscriptions to a topic named `my-topic`\.

```
$ aws lambda add-permission --function-name my-function --action lambda:InvokeFunction --statement-id sns-my-topic \
--principal sns.amazonaws.com --source-arn arn:aws:sns:us-east-2:123456789012:my-topic
```

Some services can invoke functions in other accounts\. If you specify a source ARN that has your account ID in it, that isn't an issue\. For Amazon S3, however, the source is a bucket whose ARN doesn't have an account ID in it\. It's possible that you could delete the bucket and another account could create a bucket with the same name\. Use the `account-id` option to ensure that only resources in your account can invoke the function\.

```
$ aws lambda add-permission --function-name my-function --action lambda:InvokeFunction --statement-id s3-account \
--principal s3.amazonaws.com --source-arn arn:aws:s3:::my-bucket-123456 --source-account 123456789012
```

## Granting Function Access to Other Accounts<a name="permissions-resource-xaccountinvoke"></a>

To grant permissions to another AWS account, specify the account ID as the `principal`\. The following example grants account `210987654321` permission to invoke `my-function` with the `prod` alias\.

```
$ aws lambda add-permission --function-name my-function:prod --statement-id xaccount --action lambda:InvokeFunction \
--principal 210987654321 --output text
{"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::210987654321:root"},"Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-2:123456789012:function:my-function"}
```

The [alias](aliases-intro.md) limits which version the other account can invoke\. It requires the other account to include the alias in the function ARN\.

```
$ aws lambda invoke --function-name arn:aws:lambda:us-west-2:123456789012:function:my-function:prod out
{
    "StatusCode": 200,
    "ExecutedVersion": "1"
}
```

You can then update the alias to point to new versions as needed\. When you update the alias, the other account doesn't need to change its code to use the new version, and it only has permission to invoke the version that you choose\.

You can grant cross\-account access for any API action that [operates on an existing function](lambda-api-permissions-ref.md#permissions-resources-function)\. For example, you could grant access to `lambda:ListAliases` to let an account get a list of aliases, or `lambda:GetFunction` to let them download your function code\. Add each permission separately, or use `lambda:*` to grant access to all actions for the specified function\.

To grant other accounts permission for multiple functions, or for actions that don't operate on a function, use [roles](access-control-identity-based.md)\.

## Granting Layer Access to Other Accounts<a name="permissions-resource-xaccountlayer"></a>

To grant layer\-usage permission to another account, add a statement to the layer version's permissions policy with the `add-layer-version-permission` command\. In each statement, you can grant permission to a single account, all accounts, or an organization\.

```
$ aws lambda add-layer-version-permission --layer-name xray-sdk-nodejs --statement-id xaccount \
--action lambda:GetLayerVersion  --principal 210987654321 --version-number 1 --output text
e210ffdc-e901-43b0-824b-5fcd0dd26d16    {"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::210987654321:root"},"Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:xray-sdk-nodejs:1"}
```

Permissions only apply to a single version of a layer\. Repeat the procedure each time you create a new layer version\.

To grant permission to all accounts in an organization, use the `organization-id` option\. The following example grants all accounts in an organization permission to use version 3 of a layer\.

```
$ aws lambda add-layer-version-permission --layer-name my-layer \
--statement-id engineering-org --version-number 3 --principal '*' \
--action lambda:GetLayerVersion --organization-id o-t194hfs8cz --output text 
b0cd9796-d4eb-4564-939f-de7fe0b42236    {"Sid":"engineering-org","Effect":"Allow","Principal":"*","Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3","Condition":{"StringEquals":{"aws:PrincipalOrgID":"o-t194hfs8cz"}}}"
```

To grant permission to all AWS accounts, use `*` for the principal, and omit the organization ID\. For multiple accounts or organizations, add multiple statements\.

## Cleaning up Resource\-based Policies<a name="permissions-resource-cleanup"></a>

To view a function's resource\-based policy, use the `get-policy` command\.

```
$ aws lambda get-policy --function-name my-function --output text
{"Version":"2012-10-17","Id":"default","Statement":[{"Sid":"sns","Effect":"Allow","Principal":{"Service":"s3.amazonaws.com"},"Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-2:123456789012:function:my-function","Condition":{"ArnLike":{"AWS:SourceArn":"arn:aws:sns:us-east-2:123456789012:lambda*"}}}]}      7c681fc9-b791-4e91-acdf-eb847fdaa0f0
```

For versions and aliases, append the version number or alias to the function name\.

```
$ aws lambda get-policy --function-name my-function:PROD
```

To remove permissions from your function, use `remove-permission`\.

```
$ aws lambda remove-permission --function-name example --statement-id sns
```

Use the `get-layer-version-policy` command to view the permissions on a layer, and `remove-layer-version-permission` to remove statements from the policy\.

```
$ aws lambda get-layer-version-policy --layer-name my-layer --version-number 3 --output text
b0cd9796-d4eb-4564-939f-de7fe0b42236    {"Sid":"engineering-org","Effect":"Allow","Principal":"*","Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-west-2:123456789012:layer:my-layer:3","Condition":{"StringEquals":{"aws:PrincipalOrgID":"o-t194hfs8cz"}}}"

$ aws lambda remove-layer-version-permission --layer-name my-layer --version-number 3 --statement-id engineering-org
```