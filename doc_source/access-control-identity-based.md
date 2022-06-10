# Identity\-based IAM policies for Lambda<a name="access-control-identity-based"></a>

You can use identity\-based policies in AWS Identity and Access Management \(IAM\) to grant users in your account access to Lambda\. Identity\-based policies can apply to users directly, or to groups and roles that are associated with a user\. You can also grant users in another account permission to assume a role in your account and access your Lambda resources\.

Lambda provides AWS managed policies that grant access to Lambda API actions and, in some cases, access to other AWS services used to develop and manage Lambda resources\. Lambda updates these managed policies as needed to ensure that your users have access to new features when they're released\.

**Note**  
The AWS managed policies **AWSLambdaFullAccess** and **AWSLambdaReadOnlyAccess** will be [deprecated](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-deprecated.html) on March 1, 2021\. After this date, you cannot attach these policies to new IAM users\. For more information, see the related [troubleshooting topic](security_iam_troubleshoot.md#security_iam_troubleshoot-admin-deprecation)\.
+ **AWSLambda\_FullAccess** – Grants full access to Lambda actions and other AWS services used to develop and maintain Lambda resources\. This policy was created by scoping down the previous policy **AWSLambdaFullAccess**\.
+ **AWSLambda\_ReadOnlyAccess** – Grants read\-only access to Lambda resources\. This policy was created by scoping down the previous policy **AWSLambdaReadOnlyAccess**\.
+ **AWSLambdaRole** – Grants permissions to invoke Lambda functions\.

AWS managed policies grant permission to API actions without restricting the Lambda functions or layers that a user can modify\. For finer\-grained control, you can create your own policies that limit the scope of a user's permissions\.

**Topics**
+ [Function development](#permissions-user-function)
+ [Layer development and use](#permissions-user-layer)
+ [Cross\-account roles](#permissions-user-xaccount)
+ [Condition keys for VPC settings](#permissions-condition-keys)

## Function development<a name="permissions-user-function"></a>

Use identity\-based policies to allow users to perform operations on Lambda functions\. 

**Note**  
For a function defined as a container image, the user permission to access the image MUST be configured in the Amazon Elastic Container Registry For an example, see [Amazon ECR permissions\.](gettingstarted-images.md#configuration-images-permissions)

The following shows an example of a permissions policy with limited scope\. It allows a user to create and manage Lambda functions named with a designated prefix \(`intern-`\), and configured with a designated execution role\.

**Example Function development policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ReadOnlyPermissions",
            "Effect": "Allow", 
            "Action": [
                "lambda:GetAccountSettings",
                "lambda:GetEventSourceMapping",
                "lambda:GetFunction",
                "lambda:GetFunctionConfiguration",           
                "lambda:GetFunctionCodeSigningConfig",
                "lambda:GetFunctionConcurrency",                
                "lambda:ListEventSourceMappings",
                "lambda:ListFunctions",      
                "lambda:ListTags",
                "iam:ListRoles"
            ],
            "Resource": "*"
        },
        {
            "Sid": "DevelopFunctions",
            "Effect": "Allow", 
            "NotAction": [
                "lambda:AddPermission",
                "lambda:PutFunctionConcurrency"
            ],
            "Resource": "arn:aws:lambda:*:*:function:intern-*"
        },
        {
            "Sid": "DevelopEventSourceMappings",
            "Effect": "Allow", 
            "Action": [
                "lambda:DeleteEventSourceMapping",
                "lambda:UpdateEventSourceMapping",
                "lambda:CreateEventSourceMapping"
            ],
            "Resource": "*",
            "Condition": {
                "StringLike": {
                    "lambda:FunctionArn": "arn:aws:lambda:*:*:function:intern-*"
                }
            }
        },
        {
            "Sid": "PassExecutionRole",
            "Effect": "Allow", 
            "Action": [
                "iam:ListRolePolicies",
                "iam:ListAttachedRolePolicies",
                "iam:GetRole",
                "iam:GetRolePolicy",
                "iam:PassRole",
                "iam:SimulatePrincipalPolicy"
            ],
            "Resource": "arn:aws:iam::*:role/intern-lambda-execution-role"
        },
        {
            "Sid": "ViewLogs",
            "Effect": "Allow", 
            "Action": [
                "logs:*"
            ],
            "Resource": "arn:aws:logs:*:*:log-group:/aws/lambda/intern-*"
        }
    ]
}
```

The permissions in the policy are organized into statements based on the [resources and conditions](lambda-api-permissions-ref.md) that they support\.
+ `ReadOnlyPermissions` – The Lambda console uses these permissions when you browse and view functions\. They don't support resource patterns or conditions\.

  ```
              "Action": [
                  "lambda:GetAccountSettings",
                  "lambda:GetEventSourceMapping",
                  "lambda:GetFunction",
                  "lambda:GetFunctionConfiguration",           
                  "lambda:GetFunctionCodeSigningConfig",
                  "lambda:GetFunctionConcurrency",                
                  "lambda:ListEventSourceMappings",
                  "lambda:ListFunctions",      
                  "lambda:ListTags",
                  "iam:ListRoles"
              ],
              "Resource": "*"
  ```
+ `DevelopFunctions` – Use any Lambda action that operates on functions prefixed with `intern-`, *except* `AddPermission` and `PutFunctionConcurrency`\. `AddPermission` modifies the [resource\-based policy](access-control-resource-based.md) on the function and can have security implications\. `PutFunctionConcurrency` reserves scaling capacity for a function and can take capacity away from other functions\.

  ```
              "NotAction": [
                  "lambda:AddPermission",
                  "lambda:PutFunctionConcurrency"
              ],
              "Resource": "arn:aws:lambda:*:*:function:intern-*"
  ```
+ `DevelopEventSourceMappings` – Manage event source mappings on functions that are prefixed with `intern-`\. These actions operate on event source mappings, but you can restrict them by function with a *condition*\.

  ```
              "Action": [
                  "lambda:DeleteEventSourceMapping",
                  "lambda:UpdateEventSourceMapping",
                  "lambda:CreateEventSourceMapping"
              ],
              "Resource": "*",
              "Condition": {
                  "StringLike": {
                      "lambda:FunctionArn": "arn:aws:lambda:*:*:function:intern-*"
                  }
              }
  ```
+ `PassExecutionRole` – View and pass only a role named `intern-lambda-execution-role`, which must be created and managed by a user with IAM permissions\. `PassRole` is used when you assign an execution role to a function\.

  ```
              "Action": [
                  "iam:ListRolePolicies",
                  "iam:ListAttachedRolePolicies",
                  "iam:GetRole",
                  "iam:GetRolePolicy",
                  "iam:PassRole",
                  "iam:SimulatePrincipalPolicy"
              ],
              "Resource": "arn:aws:iam::*:role/intern-lambda-execution-role"
  ```
+ `ViewLogs` – Use CloudWatch Logs to view logs for functions that are prefixed with `intern-`\.

  ```
              "Action": [
                  "logs:*"
              ],
              "Resource": "arn:aws:logs:*:*:log-group:/aws/lambda/intern-*"
  ```

This policy allows a user to get started with Lambda, without putting other users' resources at risk\. It doesn't allow a user to configure a function to be triggered by or call other AWS services, which requires broader IAM permissions\. It also doesn't include permission to services that don't support limited\-scope policies, like CloudWatch and X\-Ray\. Use the read\-only policies for these services to give the user access to metrics and trace data\.

When you configure triggers for your function, you need access to use the AWS service that invokes your function\. For example, to configure an Amazon S3 trigger, you need permission to use the Amazon S3 actions that manage bucket notifications\. Many of these permissions are included in the **AWSLambdaFullAccess** managed policy\. Example policies are available in this guide's [GitHub repository](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/iam-policies)\.

## Layer development and use<a name="permissions-user-layer"></a>

The following policy grants a user permission to create layers and use them with functions\. The resource patterns allow the user to work in any AWS Region and with any layer version, as long as the name of the layer starts with `test-`\.

**Example layer development policy**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublishLayers",
            "Effect": "Allow",
            "Action": [
                "lambda:PublishLayerVersion"
            ],
            "Resource": "arn:aws:lambda:*:*:layer:test-*"
        },
        {
            "Sid": "ManageLayerVersions",
            "Effect": "Allow",
            "Action": [
                "lambda:GetLayerVersion",
                "lambda:DeleteLayerVersion"
            ],
            "Resource": "arn:aws:lambda:*:*:layer:test-*:*"
        }
    ]
}
```

You can also enforce layer use during function creation and configuration with the `lambda:Layer` condition\. For example, you can prevent users from using layers published by other accounts\. The following policy adds a condition to the `CreateFunction` and `UpdateFunctionConfiguration` actions to require that any layers specified come from account `123456789012`\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ConfigureFunctions",
            "Effect": "Allow",
            "Action": [
                "lambda:CreateFunction",
                "lambda:UpdateFunctionConfiguration"
            ],
            "Resource": "*",
            "Condition": {
                "ForAllValues:StringLike": {
                    "lambda:Layer": [
                        "arn:aws:lambda:*:123456789012:layer:*:*"
                    ]
                }
            }
        }
    ]
}
```

To ensure that the condition applies, verify that no other statements grant the user permission to these actions\.

## Cross\-account roles<a name="permissions-user-xaccount"></a>

You can apply any of the preceding policies and statements to a role, which you can then share with another account to give it access to your Lambda resources\. Unlike an IAM user, a role doesn't have credentials for authentication\. Instead, it has a *trust policy* that specifies who can assume the role and use its permissions\.

You can use cross\-account roles to give accounts that you trust access to Lambda actions and resources\. If you just want to grant permission to invoke a function or use a layer, use [resource\-based policies](access-control-resource-based.md) instead\.

For more information, see [IAM roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\.

## Condition keys for VPC settings<a name="permissions-condition-keys"></a>

You can use condition keys for VPC settings to provide additional permission controls for your Lambda functions\. For example, you can enforce that all Lambda functions in your organization are connected to a VPC\. You can also specify the subnets and security groups that the functions are allowed to use, or are denied from using\.

For more information, see [Using IAM condition keys for VPC settings](configuration-vpc.md#vpc-conditions)\.