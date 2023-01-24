# Using resource\-based policies for Lambda<a name="access-control-resource-based"></a>

Lambda supports resource\-based permissions policies for Lambda functions and layers\. Resource\-based policies let you grant usage permission to other AWS accounts or organizations on a per\-resource basis\. You also use a resource\-based policy to allow an AWS service to invoke your function on your behalf\.

For Lambda functions, you can [grant an account permission](#permissions-resource-xaccountinvoke) to invoke or manage a function\. You can also use a single resource\-based policy to grant permissions to an entire organization in AWS Organizations\. You can also use resource\-based policies to [grant invoke permission to an AWS service](#permissions-resource-serviceinvoke) that invokes a function in response to activity in your account\. 

**To view a function's resource\-based policy**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Permissions**\.

1. Scroll down to **Resource\-based policy** and then choose **View policy document**\. The resource\-based policy shows the permissions that are applied when another account or AWS service attempts to access the function\. The following example shows a statement that allows Amazon S3 to invoke a function named `my-function` for a bucket named `my-bucket` in account `123456789012`\.  
**Example Resource\-based policy**  

   ```
   {
       "Version": "2012-10-17",
       "Id": "default",
       "Statement": [
           {
               "Sid": "lambda-allow-s3-my-function",
               "Effect": "Allow",
               "Principal": {
                 "Service": "s3.amazonaws.com"
               },
               "Action": "lambda:InvokeFunction",
               "Resource":  "arn:aws:lambda:us-east-2:123456789012:function:my-function",
               "Condition": {
                 "StringEquals": {
                   "AWS:SourceAccount": "123456789012"
                 },
                 "ArnLike": {
                   "AWS:SourceArn": "arn:aws:s3:::my-bucket"
                 }
               }
           }
        ]
   }
   ```

For Lambda layers, you can only use a resource\-based policy on a specific layer version, instead of the entire layer\. In addition to policies that grant permission to a single account or multiple accounts, for layers, you can also grant permission to all accounts in an organization\.

**Note**  
You can only update resource\-based policies for Lambda resources within the scope of the [AddPermission](API_AddPermission.md) and [AddLayerVersionPermission](API_AddLayerVersionPermission.md) API actions\. Currently, you can't author policies for your Lambda resources in JSON, or use conditions that don't map to parameters for those actions\.

Resource\-based policies apply to a single function, version, alias, or layer version\. They grant permission to one or more services and accounts\. For trusted accounts that you want to have access to multiple resources, or to use API actions that resource\-based policies don't support, you can use [cross\-account roles](access-control-identity-based.md)\.

**Topics**
+ [Granting function access to AWS services](#permissions-resource-serviceinvoke)
+ [Granting function access to an organization](#permissions-resource-xorginvoke)
+ [Granting function access to other accounts](#permissions-resource-xaccountinvoke)
+ [Granting layer access to other accounts](#permissions-resource-xaccountlayer)
+ [Cleaning up resource\-based policies](#permissions-resource-cleanup)

## Granting function access to AWS services<a name="permissions-resource-serviceinvoke"></a>

When you [use an AWS service to invoke your function](lambda-services.md), you grant permission in a statement on a resource\-based policy\. You can apply the statement to the entire function to be invoked or managed, or limit the statement to a single version or alias\.

**Note**  
When you add a trigger to your function with the Lambda console, the console updates the function's resource\-based policy to allow the service to invoke it\. To grant permissions to other accounts or services that aren't available in the Lambda console, you can use the AWS CLI\.

Add a statement with the `add-permission` command\. The simplest resource\-based policy statement allows a service to invoke a function\. The following command grants Amazon SNS permission to invoke a function named `my-function`\.

```
aws lambda add-permission --function-name my-function --action lambda:InvokeFunction --statement-id sns \
--principal sns.amazonaws.com --output text
```

You should see the following output:

```
{"Sid":"sns","Effect":"Allow","Principal":{"Service":"sns.amazonaws.com"},"Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-2:123456789012:function:my-function"}
```

This lets Amazon SNS call the `lambda:Invoke` API for the function, but it doesn't restrict the Amazon SNS topic that triggers the invocation\. To ensure that your function is only invoked by a specific resource, specify the Amazon Resource Name \(ARN\) of the resource with the `source-arn` option\. The following command only allows Amazon SNS to invoke the function for subscriptions to a topic named `my-topic`\.

```
aws lambda add-permission --function-name my-function --action lambda:InvokeFunction --statement-id sns-my-topic \
--principal sns.amazonaws.com --source-arn arn:aws:sns:us-east-2:123456789012:my-topic
```

Some services can invoke functions in other accounts\. If you specify a source ARN that has your account ID in it, that isn't an issue\. For Amazon S3, however, the source is a bucket whose ARN doesn't have an account ID in it\. It's possible that you could delete the bucket and another account could create a bucket with the same name\. Use the `source-account` option with your account ID to ensure that only resources in your account can invoke the function\.

```
aws lambda add-permission --function-name my-function --action lambda:InvokeFunction --statement-id s3-account \
--principal s3.amazonaws.com --source-arn arn:aws:s3:::my-bucket-123456 --source-account 123456789012
```

## Granting function access to an organization<a name="permissions-resource-xorginvoke"></a>

To grant permissions to an organization in AWS Organizations, specify the organization ID as the `principal-org-id`\. The following [AddPermission](API_AddPermission.md) AWS CLI command grants invocation access to all users in organization `o-a1b2c3d4e5f`\.

```
aws lambda add-permission --function-name example \
--statement-id PrincipalOrgIDExample --action lambda:InvokeFunction \
--principal * --principal-org-id o-a1b2c3d4e5f
```

**Note**  
In this command, `Principal` is `*`\. This means that all users in the organization `o-a1b2c3d4e5f` get function invocation permissions\. If you specify an AWS account or role as the `Principal`, then only that principal gets function invocation permissions, but only if they are also part of the `o-a1b2c3d4e5f` organization\.

This command creates a resource\-based policy that looks like the following:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PrincipalOrgIDExample",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "lambda:InvokeFunction",
            "Resource": "arn:aws:lambda:us-west-2:123456789012:function:example",
            "Condition": {
                "StringEquals": {
                    "aws:PrincipalOrgID": "o-a1b2c3d4e5f"
                }
            }
        }
    ]
}
```

For more information, see [ aws:PrincipalOrgID](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-principalorgid) in the AWS Identity and Access Management user guide\.

## Granting function access to other accounts<a name="permissions-resource-xaccountinvoke"></a>

To grant permissions to another AWS account, specify the account ID as the `principal`\. The following example grants account `111122223333` permission to invoke `my-function` with the `prod` alias\.

```
aws lambda add-permission --function-name my-function:prod --statement-id xaccount --action lambda:InvokeFunction \
--principal 111122223333 --output text
```

You should see the following output:

```
{"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::111122223333:root"},"Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-2:123456789012:function:my-function"}
```

The resource\-based policy grants permission for the other account to access the function, but doesn't allow users in that account to exceed their permissions\. Users in the other account must have the corresponding [user permissions](access-control-identity-based.md) to use the Lambda API\.

To limit access to a user or role in another account, specify the full ARN of the identity as the principal\. For example, `arn:aws:iam::123456789012:user/developer`\.

The [alias](configuration-aliases.md) limits which version the other account can invoke\. It requires the other account to include the alias in the function ARN\.

```
aws lambda invoke --function-name arn:aws:lambda:us-west-2:123456789012:function:my-function:prod out
```

You should see the following output:

```
{
    "StatusCode": 200,
    "ExecutedVersion": "1"
}
```

The function owner can then update the alias to point to a new version without the caller needing to change the way they invoke your function\. This ensures that the other account doesn't need to change its code to use the new version, and it only has permission to invoke the version of the function associated with the alias\.

You can grant cross\-account access for most API actions that [operate on an existing function](lambda-api-permissions-ref.md#permissions-resources-function)\. For example, you could grant access to `lambda:ListAliases` to let an account get a list of aliases, or `lambda:GetFunction` to let them download your function code\. Add each permission separately, or use `lambda:*` to grant access to all actions for the specified function\.

**Cross\-account APIs**

Currently, Lambda doesn’t support cross\-account actions for all of its APIs via resource\-based policies\. The following APIs are supported:
+ [Invoke](API_Invoke.md)
+ [GetFunction](API_GetFunction.md)
+ [GetFunctionConfiguration](API_GetFunctionConfiguration.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [DeleteFunction](API_DeleteFunction.md)
+ [PublishVersion](API_PublishVersion.md)
+ [ListVersionsByFunction](API_ListVersionsByFunction.md)
+ [CreateAlias](API_CreateAlias.md)
+ [GetAlias](API_GetAlias.md)
+ [ListAliases](API_ListAliases.md)
+ [UpdateAlias](API_UpdateAlias.md)
+ [DeleteAlias](API_DeleteAlias.md)
+ [GetPolicy](API_GetPolicy.md)
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md)
+ [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)
+ [ListTags](API_ListTags.md)
+ [TagResource](API_TagResource.md)
+ [UntagResource](API_UntagResource.md)

To grant other accounts permission for multiple functions, or for actions that don't operate on a function, we recommend that you use [IAM roles](access-control-identity-based.md)\.

## Granting layer access to other accounts<a name="permissions-resource-xaccountlayer"></a>

To grant layer\-usage permission to another account, add a statement to the layer version's permissions policy using the add\-layer\-version\-permission command\. In each statement, you can grant permission to a single account, all accounts, or an organization\.

```
aws lambda add-layer-version-permission --layer-name xray-sdk-nodejs --statement-id xaccount \
--action lambda:GetLayerVersion  --principal 111122223333 --version-number 1 --output text
```

You should see output similar to the following:

```
e210ffdc-e901-43b0-824b-5fcd0dd26d16    {"Sid":"xaccount","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::111122223333:root"},"Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:xray-sdk-nodejs:1"}
```

Permissions apply only to a single layer version\. Repeat the process each time that you create a new layer version\.

To grant permission to all accounts in an organization, use the `organization-id` option\. The following example grants all accounts in an organization permission to use version 3 of a layer\.

```
aws lambda add-layer-version-permission --layer-name my-layer \
--statement-id engineering-org --version-number 3 --principal '*' \
--action lambda:GetLayerVersion --organization-id o-t194hfs8cz --output text
```

You should see the following output:

```
b0cd9796-d4eb-4564-939f-de7fe0b42236    {"Sid":"engineering-org","Effect":"Allow","Principal":"*","Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-east-2:123456789012:layer:my-layer:3","Condition":{"StringEquals":{"aws:PrincipalOrgID":"o-t194hfs8cz"}}}"
```

To grant permission to all AWS accounts, use `*` for the principal, and omit the organization ID\. For multiple accounts or organizations, you need to add multiple statements\.

## Cleaning up resource\-based policies<a name="permissions-resource-cleanup"></a>

To view a function's resource\-based policy, use the `get-policy` command\.

```
aws lambda get-policy --function-name my-function --output text
```

You should see the following output:

```
{"Version":"2012-10-17","Id":"default","Statement":[{"Sid":"sns","Effect":"Allow","Principal":{"Service":"s3.amazonaws.com"},"Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-2:123456789012:function:my-function","Condition":{"ArnLike":{"AWS:SourceArn":"arn:aws:sns:us-east-2:123456789012:lambda*"}}}]}      7c681fc9-b791-4e91-acdf-eb847fdaa0f0
```

For versions and aliases, append the version number or alias to the function name\.

```
aws lambda get-policy --function-name my-function:PROD
```

To remove permissions from your function, use `remove-permission`\.

```
aws lambda remove-permission --function-name example --statement-id sns
```

Use the `get-layer-version-policy` command to view the permissions on a layer\.

```
aws lambda get-layer-version-policy --layer-name my-layer --version-number 3 --output text
```

You should see the following output:

```
b0cd9796-d4eb-4564-939f-de7fe0b42236    {"Sid":"engineering-org","Effect":"Allow","Principal":"*","Action":"lambda:GetLayerVersion","Resource":"arn:aws:lambda:us-west-2:123456789012:layer:my-layer:3","Condition":{"StringEquals":{"aws:PrincipalOrgID":"o-t194hfs8cz"}}}"
```

Use `remove-layer-version-permission` to remove statements from the policy\.

```
aws lambda remove-layer-version-permission --layer-name my-layer --version-number 3 --statement-id engineering-org
```