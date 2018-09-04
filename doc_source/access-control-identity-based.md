# Using Identity\-Based Policies \(IAM Policies\) for AWS Lambda<a name="access-control-identity-based"></a>

This topic provides examples of identity\-based policies in which an account administrator can attach permissions policies to IAM identities \(that is, users, groups, and roles\)\. 

**Important**  
 We recommend that you first review the introductory topics that explain the basic concepts and options available for you to manage access to your AWS Lambda resources\. For more information, see [Overview of Managing Access Permissions to Your AWS Lambda Resources](access-control-overview.md)\.

The sections in this topic cover the following:
+ [Permissions Required to Use the AWS Lambda Console](#additional-console-required-permissions) 
+ [AWS Managed \(Predefined\) Policies for AWS Lambda](#access-policy-examples-aws-managed) 
+ [Customer Managed Policy Examples](#access-policy-examples-for-sdk-cli) 

The following shows an example of a permissions policy\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "CreateFunctionPermissions",
            "Effect": "Allow",
            "Action": [
                "lambda:CreateFunction"
            ],
            "Resource": "*"
        },
        {
            "Sid": "PermissionToPassAnyRole",
            "Effect": "Allow",
            "Action": [
                "iam:PassRole"
            ],
            "Resource": "arn:aws:iam::account-id:role/*"
        }
    ]
}
```

 The policy has two statements: 
+  The first statement grants permissions for the AWS Lambda action \(`lambda:CreateFunction`\) on a resource by using the Amazon Resource Name \(ARN\)  for the Lambda function\. Currently, AWS Lambda doesn't support permissions for this particular action at the resource\-level\. Therefore, the policy specifies a wildcard character \(\*\) as the `Resource` value\. 
+ The second statement grants permissions for the IAM action \(`iam:PassRole`\) on IAM roles\. The wildcard character \(\*\) at the end of the `Resource` value means that the statement allows permission for the `iam:PassRole` action on any IAM role\. To limit this permission to a specific role, replace the wildcard character \(\*\) in the resource ARN with the specific role name\. 

The policy doesn't specify the `Principal` element because in an identity\-based policy you don't specify the principal who gets the permission\. When you attach policy to a user, the user is the implicit principal\. When you attach a permission policy to an IAM role, the principal identified in the role's trust policy gets the permissions\.

 For a table showing all of the AWS Lambda API actions and the resources and conditions that they apply to, see [Lambda API Permissions: Actions, Resources, and Conditions Reference](lambda-api-permissions-ref.md)\. 

## Permissions Required to Use the AWS Lambda Console<a name="additional-console-required-permissions"></a>

The AWS Lambda console provides an integrated environment for you to create and manage Lambda functions\. The console provides many features and workflows that often require permissions to create a Lambda function in addition to the API\-specific permissions documented in the [Lambda API Permissions: Actions, Resources, and Conditions Reference](lambda-api-permissions-ref.md)\. For more information about these additional console permissions, see [Permissions Required to Use the AWS Lambda Console](console-specific-permissions.md)\.

## AWS Managed \(Predefined\) Policies for AWS Lambda<a name="access-policy-examples-aws-managed"></a>

AWS addresses many common use cases by providing standalone IAM policies that are created and administered by AWS\. Managed policies grant necessary permissions for common use cases so you can avoid having to investigate what permissions are needed\. For more information, see [AWS Managed Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-vs-inline.html#aws-managed-policies) in the *IAM User Guide*\.

The following AWS managed policies, which you can attach to users in your account, are specific to AWS Lambda and are grouped by use case scenario:
+ **AWSLambdaReadOnlyAccess** – Grants read\-only access to AWS Lambda resources\. Note that this policy doesn't grant permission for the `lambda:InvokeFunction` action\. If you want a user to invoke a Lambda function, you can also attach the `AWSLambdaRole` AWS managed policy\.
+ **AWSLambdaFullAccess** – Grants full access to AWS Lambda resources\.
+ **AWSLambdaRole** – Grants permissions to invoke any Lambda function\. 

**Note**  
You can review these permissions policies by signing in to the IAM console and searching for specific policies there\.

In addition, there are other AWS\-managed policies that are suitable for use with IAM role \(execution role\) you specify at the time of creating a Lambda function\. For more information, see [AWS Lambda Permissions Model](intro-permission-model.md)\.

You can also create your own custom IAM policies to allow permissions for AWS Lambda API actions and resources\. You can attach these custom policies to the IAM users or groups that require those permissions or to custom execution roles \(IAM roles\) that you create for your Lambda functions\. 

## Customer Managed Policy Examples<a name="access-policy-examples-for-sdk-cli"></a>

The examples in this section provide a group of sample policies that you can attach to a user\. If you are new to creating policies, we recommend that you first create an IAM user in your account and attach the policies to the user in sequence, as outlined in the steps in this section\.

You can use the console to verify the effects of each policy as you attach the policy to the user\. Initially, the user doesn't have permissions and the user won't be able to do anything in the console\. As you attach policies to the user, you can verify that the user can perform various actions in the console\. 

We recommend that you use two browser windows: one to create the user and grant permissions, and the other to sign in to the AWS Management Console using the user's credentials and verify permissions as you grant them to the user\.

 For examples that show how to create an IAM role that you can use as an execution role for your Lambda function, see [Creating IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create.html) in the *IAM User Guide*\.

**Topics**
+ [Step 1: Create an IAM User](#console-permissions-list-functions)
+ [Step 2: Allow a User to List Lambda Functions](#console-permissions-list-functions1)
+ [Step 3: Allow a User to View Details of a Lambda Function](#console-permissions-view-details)
+ [Step 4: Allow a User to Invoke a Lambda Function](#console-permissions-invoke)
+ [Step 5: Allow a User to Monitor a Lambda Function and View CloudWatch Logs](#console-permissions-cloudwatch-logs1)
+ [Step 6: Allow a User to Create a Lambda Function](#console-permissions-create)

### Step 1: Create an IAM User<a name="console-permissions-list-functions"></a>

First, you need to create an IAM user, add the user to an IAM group with administrative permissions, and then grant administrative permissions to the IAM user that you created\. You can then access AWS using a special URL and that IAM user's credentials\. 

For instructions, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. 

### Step 2: Allow a User to List Lambda Functions<a name="console-permissions-list-functions1"></a>

An IAM user in your account must have permissions for the `lambda:ListFunctions` action before the user can see anything in the console\. When you grant these permissions, the console can show the list of Lambda functions in the AWS account created in the specific AWS Region the user belongs to\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ListExistingFunctions",
            "Effect": "Allow",
            "Action": [
                "lambda:ListFunctions"
            ],
            "Resource": "*"
        }
    ]
}
```

### Step 3: Allow a User to View Details of a Lambda Function<a name="console-permissions-view-details"></a>

A user can select a Lambda function and view details of the function \(such as aliases, versions, and other configuration information\), provided that the user has permissions for the following AWS Lambda actions:

```
{
    "Version": "2012-10-17",
    "Statement": [
          {
              "Sid": "DisplayFunctionDetailsPermissions",
              "Effect": "Allow",
              "Action": [
                  "lambda:ListVersionsByFunction",
                  "lambda:ListAliases",
                  "lambda:GetFunction",
                  "lambda:GetFunctionConfiguration",
                  "lambda:ListEventSourceMappings",
                  "lambda:GetPolicy"
              ],
              "Resource": "*"
          }
    ]
}
```

### Step 4: Allow a User to Invoke a Lambda Function<a name="console-permissions-invoke"></a>

If you want to allow a user permissions to manually invoke a function, you need to grant permissions for the `lambda:InvokeFunction` action, as shown following:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "InvokePermission",
            "Effect": "Allow",
            "Action": [
                "lambda:InvokeFunction"
            ],
            "Resource": "*"
        }
    ]
}
```

### Step 5: Allow a User to Monitor a Lambda Function and View CloudWatch Logs<a name="console-permissions-cloudwatch-logs1"></a>

When a user invokes a Lambda function, AWS Lambda executes it and returns results\. The user needs additional permissions to monitor the Lambda function\. 

To enable the user to see the Lambda function's CloudWatch metrics on the console's **Monitoring** tab, or on the grid view on the console home page, you must grant the following permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "CloudWatchPermission",
            "Effect": "Allow",
            "Action": [
                "cloudwatch:GetMetricStatistics"
            ],
            "Resource": "*"
        }
    ]
}
```

To enable a user to click the links to CloudWatch Logs in the AWS Lambda console and view log output in CloudWatch Logs, you must grant the following permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "CloudWatchLogsPerms",
            "Effect": "Allow",
            "Action": [
                "cloudwatchlog:DescribeLogGroups",
                "cloudwatchlog:DescribeLogStreams",
                "cloudwatchlog:GetLogEvents"

              ],
            "Resource": "arn:aws:logs:region:account-id:log-group:/aws/lambda/*"
        }
    ]
}
```

### Step 6: Allow a User to Create a Lambda Function<a name="console-permissions-create"></a>

If you want a user to be able to create a Lambda function, you must grant the following permissions\. The permissions for IAM\-related actions are required because when a user creates a Lambda function, the user needs to select an IAM execution role, which AWS Lambda assumes to execute the Lambda function\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ListExistingRolesAndPolicies",
            "Effect": "Allow",
            "Action": [
                "iam:ListRolePolicies",
                "iam:ListRoles"
            ],
            "Resource": "*"
        },
        {
            "Sid": "CreateFunctionPermissions",
            "Effect": "Allow",
            "Action": [
                "lambda:CreateFunction"
            ],
            "Resource": "*"
        },
        {
            "Sid": "PermissionToPassAnyRole",
            "Effect": "Allow",
            "Action": [
                "iam:PassRole"
            ],
            "Resource": "arn:aws:iam::account-id:role/*"
        }
    ]
}
```

If you want a user to be able to create an IAM role when the user is creating a Lambda function, the user needs permissions to perform the `iam:PutRolePolicy` action, as shown following:

```
{
    "Sid": "CreateARole",
    "Effect": "Allow",
    "Action": [
        "iam:CreateRole",
        "iam:CreatePolicy",
        "iam:PutRolePolicy
        "iam:AttachRolePolicy"
    ],
    "Resource": "arn:aws:iam::account-id:role/*"
}
```

**Important**  
Each IAM role has a permissions policy attached to it, which grants specific permissions to the role\. Regardless of whether the user creates a new role or uses an existing role, the user must have permissions for all of the actions granted in the permissions policy associated with the role\. You must grant the user additional permissions accordingly\.