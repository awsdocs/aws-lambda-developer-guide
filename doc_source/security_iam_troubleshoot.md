# Troubleshooting AWS Lambda identity and access<a name="security_iam_troubleshoot"></a>

Use the following information to help you diagnose and fix common issues that you might encounter when working with Lambda and IAM\.

**Topics**
+ [I am not authorized to perform an action in Lambda](#security_iam_troubleshoot-no-permissions)
+ [I am not authorized to perform iam:PassRole](#security_iam_troubleshoot-passrole)
+ [I want to view my access keys](#security_iam_troubleshoot-access-keys)
+ [I'm an administrator and want to allow others to access Lambda](#security_iam_troubleshoot-admin-delegate)
+ [I'm an administrator and want to migrate from AWS managed policies for Lambda that will be deprecated](#security_iam_troubleshoot-admin-deprecation)
+ [I want to allow people outside of my AWS account to access my Lambda resources](#security_iam_troubleshoot-cross-account-access)

## I am not authorized to perform an action in Lambda<a name="security_iam_troubleshoot-no-permissions"></a>

If the AWS Management Console tells you that you're not authorized to perform an action, then you must contact your administrator for assistance\. Your administrator is the person that provided you with your user name and password\.

The following example error occurs when the `mateojackson` IAM user tries to use the console to view details about a function but does not have `lambda:GetFunction` permissions\.

```
User: arn:aws:iam::123456789012:user/mateojackson is not authorized to perform: lambda:GetFunction on resource: my-function
```

In this case, Mateo asks his administrator to update his policies to allow him to access the `my-function` resource using the `lambda:GetFunction` action\.

## I am not authorized to perform iam:PassRole<a name="security_iam_troubleshoot-passrole"></a>

If you receive an error that you're not authorized to perform the `iam:PassRole` action, your policies must be updated to allow you to pass a role to Lambda\.

Some AWS services allow you to pass an existing role to that service instead of creating a new service role or service\-linked role\. To do this, you must have permissions to pass the role to the service\.

The following example error occurs when an IAM user named `marymajor` tries to use the console to perform an action in Lambda\. However, the action requires the service to have permissions that are granted by a service role\. Mary does not have permissions to pass the role to the service\.

```
User: arn:aws:iam::123456789012:user/marymajor is not authorized to perform: iam:PassRole
```

In this case, Mary's policies must be updated to allow her to perform the `iam:PassRole` action\.

If you need help, contact your AWS administrator\. Your administrator is the person who provided you with your sign\-in credentials\.

## I want to view my access keys<a name="security_iam_troubleshoot-access-keys"></a>

After you create your IAM user access keys, you can view your access key ID at any time\. However, you can't view your secret access key again\. If you lose your secret key, you must create a new access key pair\. 

Access keys consist of two parts: an access key ID \(for example, `AKIAIOSFODNN7EXAMPLE`\) and a secret access key \(for example, `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`\)\. Like a user name and password, you must use both the access key ID and secret access key together to authenticate your requests\. Manage your access keys as securely as you do your user name and password\.

**Important**  
 Do not provide your access keys to a third party, even to help [find your canonical user ID](https://docs.aws.amazon.com/general/latest/gr/acct-identifiers.html#FindingCanonicalId)\. By doing this, you might give someone permanent access to your account\. 

When you create an access key pair, you are prompted to save the access key ID and secret access key in a secure location\. The secret access key is available only at the time you create it\. If you lose your secret access key, you must add new access keys to your IAM user\. You can have a maximum of two access keys\. If you already have two, you must delete one key pair before creating a new one\. To view instructions, see [Managing access keys](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html#Using_CreateAccessKey) in the *IAM User Guide*\.

## I'm an administrator and want to allow others to access Lambda<a name="security_iam_troubleshoot-admin-delegate"></a>

To allow others to access Lambda, you must create an IAM entity \(user or role\) for the person or application that needs access\. They will use the credentials for that entity to access AWS\. You must then attach a policy to the entity that grants them the correct permissions in Lambda\.

To get started right away, see [Creating your first IAM delegated user and group](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-delegated-user.html) in the *IAM User Guide*\.

## I'm an administrator and want to migrate from AWS managed policies for Lambda that will be deprecated<a name="security_iam_troubleshoot-admin-deprecation"></a>

After March 1, 2021, the AWS managed policies **AWSLambdaReadOnlyAccess** and **AWSLambdaFullAccess** will be deprecated and can no longer be attached to new IAM users\. For more information about policy deprecations, see [Deprecated AWS managed policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-deprecated.html) in the *IAM User Guide*\.

Lambda has introduced two new AWS managed policies:
+ The **AWSLambda\_ReadOnlyAccess** policy grants read\-only access to Lambda, Lambda console features, and other related AWS services\. This policy was created by scoping down the previous policy **AWSLambdaReadOnlyAccess**\.
+ The **AWSLambda\_FullAccess** policy grants full access to Lambda, Lambda console features, and other related AWS services\. This policy was created by scoping down the previous policy **AWSLambdaFullAccess**\.



### Using the AWS managed policies<a name="security_iam_troubleshoot-admin-deprecation-aws"></a>

We recommend using the newly launched managed policies to grant users, groups, and roles access to Lambda; however, review the permissions granted in the policies to ensure they meet your requirements\.
+ To review the permissions of the **AWSLambda\_ReadOnlyAccess** policy, see the [AWSLambda\_ReadOnlyAccess](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/AWSLambda_ReadOnlyAccess$jsonEditor) policy page in the IAM console\.
+ To review the permissions of the **AWSLambda\_FullAccess** policy, see the [AWSLambda\_FullAccess](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/AWSLambda_FullAccess$jsonEditor) policy page in the IAM console\.

After reviewing the permissions, you can attach the policies to an IAM identity \(groups, users, or roles\)\. For instructions about attaching an AWS managed policy, see [Adding and removing IAM identity permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-attach-detach.html) in the *IAM User Guide*\.

### Using customer managed policies<a name="security_iam_troubleshoot-admin-deprecation-customer"></a>

If you need more fine\-grained access control or would like to add permissions, you can create your own [customer managed policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-vs-inline.html#customer-managed-policies)\. For more information, see [Creating policies on the JSON tab](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_create-console.html#access_policies_create-json-editor) in the *IAM User Guide*\.

## I want to allow people outside of my AWS account to access my Lambda resources<a name="security_iam_troubleshoot-cross-account-access"></a>

You can create a role that users in other accounts or people outside of your organization can use to access your resources\. You can specify who is trusted to assume the role\. For services that support resource\-based policies or access control lists \(ACLs\), you can use those policies to grant people access to your resources\.

To learn more, consult the following:
+ To learn whether Lambda supports these features, see [How AWS Lambda works with IAM](security_iam_service-with-iam.md)\.
+ To learn how to provide access to your resources across AWS accounts that you own, see [Providing access to an IAM user in another AWS account that you own](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_common-scenarios_aws-accounts.html) in the *IAM User Guide*\.
+ To learn how to provide access to your resources to third\-party AWS accounts, see [Providing access to AWS accounts owned by third parties](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_common-scenarios_third-party.html) in the *IAM User Guide*\.
+ To learn how to provide access through identity federation, see [Providing access to externally authenticated users \(identity federation\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_common-scenarios_federated-users.html) in the *IAM User Guide*\.
+ To learn the difference between using roles and resource\-based policies for cross\-account access, see [How IAM roles differ from resource\-based policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_compare-resource-policies.html) in the *IAM User Guide*\.