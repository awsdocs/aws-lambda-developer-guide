# Security and auth model for Lambda function URLs<a name="urls-auth"></a>

You can control access to your Lambda function URLs using the `AuthType` parameter combined with [resource\-based policies](access-control-resource-based.md) attached to your specific function\. The configuration of these two components determines who can invoke or perform other administrative actions on your function URL\.

The `AuthType` parameter determines how Lambda authenticates or authorizes requests to your function URL\. When you configure your function URL, you must specify one of the following `AuthType` options:
+ `AWS_IAM` – Lambda uses AWS Identity and Access Management \(IAM\) to authenticate and authorize requests based on the IAM principal's identity policy and the function's resource\-based policy\. Choose this option if you want only authenticated IAM users and roles to invoke your function via the function URL\.
+ `NONE` – Lambda doesn't perform any authentication before invoking your function\. However, your function's resource\-based policy is always in effect and must grant public access before your function URL can receive requests\. Choose this option to allow public, unauthenticated access to your function URL\.

In addition to `AuthType`, you can also use resource\-based policies to grant permissions to other AWS accounts to invoke your function\. For more information, see [Using resource\-based policies for Lambda](access-control-resource-based.md)\.

For additional insights into security, you can use AWS Identity and Access Management Access Analyzer to get a comprehensive analysis of external access to your function URL\. IAM Access Analyzer also monitors for new or updated permissions on your Lambda functions to help you identify permissions that grant public and cross\-account access\. IAM Access Analyzer is free to use for any AWS customer\. To get started with IAM Access Analyzer, see [Using AWS IAM Access Analyzer](https://docs.aws.amazon.com/IAM/latest/UserGuide/what-is-access-analyzer.html)\.

This page contains examples of resource\-based policies for both auth types, and also how to create these policies using the [AddPermission](API_AddPermission.md) API operation or the Lambda console\. For information on how to invoke your function URL after you've set up permissions, see [Invoking Lambda function URLs](urls-invocation.md)\.

**Topics**
+ [Using the `AWS_IAM` auth type](#urls-auth-iam)
+ [Using the `NONE` auth type](#urls-auth-none)
+ [Governance and access control](#urls-governance)

## Using the `AWS_IAM` auth type<a name="urls-auth-iam"></a>

If you choose the `AWS_IAM` auth type, users who need to invoke your Lambda function URL must have the `lambda:InvokeFunctionUrl` permission\. Depending on who makes the invocation request, you may have to grant this permission using a resource\-based policy\.

If the principal making the request is in the same AWS account as the function URL, then the principal must **either** have `lambda:InvokeFunctionUrl` permissions in their [identity\-based policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_identity-vs-resource.html), **or** have permissions granted to them in the function's resource\-based policy\. In other words, a resource\-based policy is optional if the user already has `lambda:InvokeFunctionUrl` permissions in their identity\-based policy\. Policy evaluation follows the rules outlined in [Determining whether a request is allowed or denied within an account](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_evaluation-logic.html#policy-eval-denyallow)\.

If the principal making the request is in a different account, then the principal must have **both** an identity\-based policy that gives them `lambda:InvokeFunctionUrl` permissions **and** permissions granted to them in a resource\-based policy on the function that they are trying to invoke\. In these cross\-account cases, policy evaluation follows the rules outlined in [Determining whether a cross\-account request is allowed](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_evaluation-logic-cross-account.html#policy-eval-cross-account)\.

For an example cross\-account interaction, the following resource\-based policy allows the `example` role in AWS account `444455556666` to invoke the function URL associated with function `my-function`:

**Example function URL cross\-account invoke policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::444455556666:role/example"
            },
            "Action": "lambda:InvokeFunctionUrl",
            "Resource": "arn:aws:lambda:us-east-1:123456789012:function:my-function",
            "Condition": {
                "StringEquals": {
                    "lambda:FunctionUrlAuthType": "AWS_IAM"
                }
            }
        }
    ]
}
```

You can create this policy statement through the console by following these steps:

**To grant URL invocation permissions to another account \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of the function that you want to grant URL invocation permissions for\.

1. Choose the **Configuration** tab, and then choose **Permissions**\.

1. Under **Resource\-based policy**, choose **Add permissions**\.

1. Choose **Function URL**\.

1. For **Auth type**, choose **AWS\_IAM**\.

1. \(Optional\) For **Statement ID**, enter a statement ID for your policy statement\.

1. For **Principal**, enter the Amazon Resource Name \(ARN\) of the IAM user or role that you want to grant permissions to\. For example: **arn:aws:iam::444455556666:role/example**\.

1. Choose **Save**\.

Alternatively, you can create this policy statement using the following [add\-permission](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/add-permission.html) AWS Command Line Interface \(AWS CLI\) command:

```
aws lambda add-permission --function-name my-function \
--statement-id example0-cross-account-statement \
--action lambda:InvokeFunctionUrl \
--principal arn:aws:iam::444455556666:role/example \
--function-url-auth-type AWS_IAM
```

In the previous example, the `lambda:FunctionUrlAuthType` condition key value is `AWS_IAM`\. This policy only allows access when your function URL's auth type is also `AWS_IAM`\.

## Using the `NONE` auth type<a name="urls-auth-none"></a>

**Important**  
When your function URL auth type is `NONE` and you have a resource\-based policy that grants public access, any unauthenticated user with your function URL can invoke your function\.

In some cases, you may want your function URL to be public\. For example, you might want to serve requests made directly from a web browser\. To allow public access to your function URL, choose the `NONE` auth type\.

If you choose the `NONE` auth type, Lambda doesn't use IAM to authenticate requests to your function URL\. However, users must still have `lambda:InvokeFunctionUrl` permissions in order to successfully invoke your function URL\. You can grant `lambda:InvokeFunctionUrl` permissions using the following resource\-based policy:

**Example function URL invoke policy for all unauthenticated principals**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": "*",
            "Action": "lambda:InvokeFunctionUrl",
            "Resource": "arn:aws:lambda:us-east-1:123456789012:function:my-function",
            "Condition": {
                "StringEquals": {
                    "lambda:FunctionUrlAuthType": "NONE"
                }
            }
        }
    ]
}
```

**Note**  
When you create a function URL with auth type `NONE` via the console or AWS Serverless Application Model \(AWS SAM\), Lambda automatically creates the preceding resource\-based policy statement for you\. If the policy already exists, or the user or role creating the application doesn't have the appropriate permissions, then Lambda won't create it for you\. If you're using the AWS CLI, AWS CloudFormation, or the Lambda API directly, you must add `lambda:InvokeFunctionUrl` permissions yourself\. This makes your function public\.  
In addition, if you delete your function URL with auth type `NONE`, Lambda doesn't automatically delete the associated resource\-based policy\. If you want to delete this policy, you must manually do so\.

In this statement, the `lambda:FunctionUrlAuthType` condition key value is `NONE`\. This policy statement allows access only when your function URL's auth type is also `NONE`\.

If a function's resource\-based policy doesn't grant `lambda:invokeFunctionUrl` permissions, then users will get a 403 Forbidden error code when they try to invoke your function URL, even if the function URL uses the `NONE` auth type\.

## Governance and access control<a name="urls-governance"></a>

In addition to function URL invocation permissions, you can also control access on actions used to configure function URLs\. Lambda supports the following IAM policy actions for function URLs:
+ `lambda:InvokeFunctionUrl` – Invoke a Lambda function using the function URL\.
+ `lambda:CreateFunctionUrlConfig` – Create a function URL and set its `AuthType`\.
+ `lambda:UpdateFunctionUrlConfig` – Update a function URL configuration and its `AuthType`\.
+ `lambda:GetFunctionUrlConfig` – View the details of a function URL\.
+ `lambda:ListFunctionUrlConfigs` – List function URL configurations\.
+ `lambda:DeleteFunctionUrlConfig` – Delete a function URL\.

**Note**  
The Lambda console supports adding permissions only for `lambda:InvokeFunctionUrl`\. For all other actions, you must add permissions using the Lambda API or AWS CLI\.

To allow or deny function URL access to other AWS entities, include these actions in IAM policies\. For example, the following policy grants the `example` role in AWS account `444455556666` permissions to update the function URL for function **my\-function** in account `123456789012`\.

**Example cross\-account function URL policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": { 
                "AWS": "arn:aws:iam::444455556666:role/example"
            },
            "Action": "lambda:UpdateFunctionUrlConfig",
            "Resource": "arn:aws:lambda:us-east-2:123456789012:function:my-function"
        }
    ]
}
```

### Condition keys<a name="urls-condition-keys"></a>

For fine\-grained access control over your function URLs, use a condition key\. Lambda supports one additional condition key for function URLs: `FunctionUrlAuthType`\. The `FunctionUrlAuthType` key defines an enum value describing the auth type that your function URL uses\. The value can be either `AWS_IAM` or `NONE`\.

You can use this condition key in policies associated with your function\. For example, you might want to restrict who can make configuration changes to your function URLs\. To deny all `UpdateFunctionUrlConfig` requests to any function with URL auth type `NONE`, you can define the following policy:

**Example function URL policy with explicit deny**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Deny",
            "Principal": "*",
            "Action":[
                "lambda:UpdateFunctionUrlConfig"
            ],
            "Resource": "arn:aws:lambda:us-east-1:123456789012:function:*",
            "Condition": {
                "StringEquals": {
                    "lambda:FunctionUrlAuthType": "NONE"
                }
            }
        }
    ]
}
```

To grant the `example` role in AWS account `444455556666` permissions to make `CreateFunctionUrlConfig` and `UpdateFunctionUrlConfig` requests on functions with URL auth type `AWS_IAM`, you can define the following policy:

**Example function URL policy with explicit allow**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": { 
                "AWS": "arn:aws:iam::444455556666:role/example"
            },
            "Action":[
                "lambda:CreateFunctionUrlConfig",
                "lambda:UpdateFunctionUrlConfig"
            ],
            "Resource": "arn:aws:lambda:us-east-1:123456789012:function:*",
            "Condition": {
                "StringEquals": {
                    "lambda:FunctionUrlAuthType": "AWS_IAM"
                }
            }
        }
    ]
}
```

You can also use this condition key in a [service control policy](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_manage_policies_scps.html) \(SCP\)\. Use SCPs to manage permissions across an entire organization in AWS Organizations\. For example, to deny users from creating or updating function URLs that use anything other than the `AWS_IAM` auth type, use the following service control policy:

**Example function URL SCP with explicit deny**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Deny",
            "Action":[
                "lambda:CreateFunctionUrlConfig",
                "lambda:UpdateFunctionUrlConfig"
            ],
            "Resource": "arn:aws:lambda:*:123456789012:function:*",
            "Condition": {
                "StringNotEquals": {
                    "lambda:FunctionUrlAuthType": "AWS_IAM"
                }
            }
        }
    ]
}
```