# Attribute\-based access control for Lambda<a name="attribute-based-access-control"></a>

With [attribute\-based access control \(ABAC\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction_attribute-based-access-control.html), you can use tags to control access to your Lambda functions\. You can attach tags to a Lambda function, pass them in certain API requests, or attach them to the AWS Identity and Access Management \(IAM\) principal making the request\. For more information about how AWS grants attribute\-based access, see [Controlling access to AWS resources using tags](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_tags.html) in the *IAM User Guide*\.

You can use ABAC to [grant least privilege](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#grant-least-privilege) without specifying an Amazon Resource Name \(ARN\) or ARN pattern in the IAM policy\. Instead, you can specify a tag in the [condition element](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_condition.html) of an IAM policy to control access\. Scaling is easier with ABAC because you don't have to update your IAM policies when you create new functions\. Instead, add tags to the new functions to control access\.

In Lambda, tags work at the function level\. Tags aren't supported for layers, code signing configurations, or event source mappings\. When you tag a function, those tags apply to all versions and aliases associated with the function\. For information about how to tag functions, see [Using tags on Lambda functions](configuration-tags.md)\.

You can use the following condition keys to control function actions:
+ [aws:ResourceTag/tag\-key](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-resourcetag): Control access based on the tags that are attached to Lambda functions\.
+ [aws:RequestTag/tag\-key](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-requesttag): Require tags to be present in a request, such as when creating a new function\.
+ [aws:PrincipalTag/tag\-key](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-principaltag): Control what the IAM principal \(the person making the request\) is allowed to do based on the tags that are attached to their IAM [user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_tags_users.html) or [role](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_tags_roles.html)\.
+ [aws:TagKeys](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-tagkeys): Control whether specific tag keys can be used in a request\.

For a complete list of Lambda actions that support ABAC, see [Function actions](lambda-api-permissions-ref.md#permissions-resources-function) and check the **Condition** column in the table\.

The following steps demonstrate one way to set up permissions using ABAC\. In this example scenario, you'll create four IAM permissions policies\. Then, you'll attach these policies to a new IAM role\. Finally, you'll create an IAM user and give that user permission to assume the new role\.

## Prerequisites<a name="abac-prerequisites"></a>

Make sure that you have a [Lambda execution role](lambda-intro-execution-role.md)\. You'll use this role when you grant IAM permissions and when you create a Lambda function\.

## Step 1: Require tags on new functions<a name="require-tag-on-create"></a>

When using ABAC with Lambda, it's a best practice to require that all functions have tags\. This helps ensure that your ABAC permissions policies work as expected\.

[Create an IAM policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_create-console.html#access_policies_create-json-editor) similar to the following example\. This policy uses the [aws:RequestTag/tag\-key](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-requesttag) and [aws:TagKeys](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-tagkeys) condition keys to require that new functions and the IAM principal creating the functions both have the `project` tag\. The `ForAllValues` modifier ensures that `project` is the only allowed tag\. If you don't include the `ForAllValues` modifier, users can add other tags to the function as long as they also pass `project`\.

**Example – Require tags on new functions**  

```
{
  "Version": "2012-10-17",
  "Statement": {
    "Effect": "Allow",
    "Action": [
      "lambda:CreateFunction",
      "lambda:TagResource"
    ],
    "Resource": "arn:aws:lambda:*:*:function:*",
    "Condition": {
      "StringEquals": {
        "aws:RequestTag/project": "${aws:PrincipalTag/project}"
      },
      "ForAllValues:StringEquals": {
        "aws:TagKeys": "project"
      }
    }
  }
}
```

## Step 2: Allow actions based on tags attached to a Lambda function and IAM principal<a name="restrict-actions-function-tags"></a>

Create a second IAM policy using the [aws:ResourceTag/tag\-key](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_condition-keys.html#condition-keys-resourcetag) condition key to require the principal's tag to match the tag that's attached to the function\. The following example policy allows principals with the `project` tag to invoke functions with the `project` tag\. If a function has any other tags, the action is denied\.

**Example – Require matching tags on function and IAM principal**  

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "lambda:InvokeFunction",
        "lambda:GetFunction"
      ],
      "Resource": "arn:aws:lambda:*:*:function:*",
      "Condition": {
        "StringEquals": {
          "aws:ResourceTag/project": "${aws:PrincipalTag/project}"
        }
      }
    }
  ]
}
```

## Step 3: Grant list permissions<a name="abac-list-permissions"></a>

Create a policy that allows the principal to list Lambda functions and IAM roles\. This allows the principal to see all Lambda functions and IAM roles on the console and when calling the API actions\.

**Example – Grant Lambda and IAM list permissions**  

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllResourcesLambdaNoTags",
      "Effect": "Allow",
      "Action": [
        "lambda:GetAccountSettings",
        "lambda:ListFunctions",
        "iam:ListRoles"
      ],
      "Resource": "*"
    }
  ]
}
```

## Step 4: Grant IAM permissions<a name="abac-iam-permissions"></a>

Create a policy that allows **iam:PassRole**\. This permission is required when you assign an execution role to a function\. In the following example policy, replace the example ARN with the ARN of your Lambda execution role\.

**Note**  
Do not use the `ResourceTag` condition key in a policy with the `iam:PassRole` action\. You cannot use the tag on an IAM role to control access to who can pass that role\. For more information about permissions required to pass a role to a service, see [Granting a user permissions to pass a role to an AWS service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html)\.

**Example – Grant permission to pass the execution role**  

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "VisualEditor0",
      "Effect": "Allow",
      "Action": [
        "iam:PassRole"
      ],
      "Resource": "arn:aws:iam::111122223333:role/lambda-ex"
    }
  ]
}
```

## Step 5: Create the IAM role<a name="abac-create-role"></a>

It's a best practice to [use roles to delegate permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#delegate-using-roles)\. [Create an IAM role](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-user.html#roles-creatingrole-user-console) called `abac-project-role`:
+ On **Step 1: Select trusted entity**: Choose **AWS account** and then choose **This account**\.
+ On **Step 2: Add permissions**: Attach the four IAM policies that you created in the previous steps\.
+ On **Step 3: Name, review, and create**: Choose **Add tag**\. For **Key**, enter `project`\. Don't enter a **Value**\.

## Step 6: Create the IAM user<a name="abac-create-user"></a>

[Create an IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console) called `abac-test-user`\. In the **Set permissions** section, choose **Attach existing policies directly** and then choose **Create policy**\. Enter the following policy definition\. Replace *111122223333* with your [AWS account ID](https://docs.aws.amazon.com/general/latest/gr/acct-identifiers.html#FindingYourAccountIdentifiers)\. This policy allows `abac-test-user` to assume `abac-project-role`\.

**Example – Allow IAM user to assume ABAC role**  

```
{
  "Version": "2012-10-17",
  "Statement": {
    "Effect": "Allow",
    "Action": "sts:AssumeRole",
    "Resource": "arn:aws:iam::111122223333:role/abac-project-role"
  }
}
```

## Step 7: Test the permissions<a name="abac-test"></a>

1. Sign in to the AWS console as `abac-test-user`\. For more information, see [Sign in as an IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/console.html#user-sign-in-page)\.

1. Switch to the `abac-project-role` role\. For more information, see [Switching to a role \(console\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-console.html)\.

1. [Create a Lambda function](configuration-tags.md#using-tags-with-the-console):
   + Under **Permissions**, choose **Change default execution role**, and then for **Execution role**, choose **Use an existing role**\. Choose the same execution role that you used in [Step 4: Grant IAM permissions](#abac-iam-permissions)\.
   + Under **Advanced settings**, choose **Enable tags** and then choose **Add new tag**\. For **Key**, enter `project`\. Don't enter a **Value**\.

1. [Test the function](testing-functions.md)\.

1. Create a second Lambda function and add a different tag, such as `environment`\. This operation should fail because the ABAC policy that you created in [Step 1: Require tags on new functions](#require-tag-on-create) only allows the principal to create functions with the `project` tag\.

1. Create a third function without tags\. This operation should fail because the ABAC policy that you created in [Step 1: Require tags on new functions](#require-tag-on-create) doesn't allow the principal to create functions without tags\.

This authorization strategy allows you to control access without creating new policies for each new user\. To grant access to new users, simply give them permission to assume the role that corresponds to their assigned project\.