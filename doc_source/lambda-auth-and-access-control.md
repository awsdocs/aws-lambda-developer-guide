# Authentication and Access Control for AWS Lambda<a name="lambda-auth-and-access-control"></a>

Access to AWS Lambda requires credentials that AWS can use to authenticate your requests\. Those credentials must have permissions to access AWS resources, such as an AWS Lambda function or an Amazon S3 bucket\. The following sections provide details on how you can use [AWS Identity and Access Management \(IAM\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html) and Lambda to help secure your resources by controlling who can access them:

+ [Authentication](#authentication)

+ [Access Control](#access-control)

## Authentication<a name="authentication"></a>

You can access AWS as any of the following types of identities:

+ **AWS account root user** – When you first create an AWS account, you begin with a single sign\-in identity that has complete access to all AWS services and resources in the account\. This identity is called the AWS account *root user* and is accessed by signing in with the email address and password that you used to create the account\. We strongly recommend that you do not use the root user for your everyday tasks, even the administrative ones\. Instead, adhere to the [best practice of using the root user only to create your first IAM user](http://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#create-iam-users)\. Then securely lock away the root user credentials and use them to perform only a few account and service management tasks\.

+ **IAM user** – An [IAM user](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users.html) is an identity within your AWS account that has specific custom permissions \(for example, permissions to create a function in Lambda\)\. You can use an IAM user name and password to sign in to secure AWS webpages like the [AWS Management Console](https://console.aws.amazon.com/), [AWS Discussion Forums](https://forums.aws.amazon.com/), or the [AWS Support Center](https://console.aws.amazon.com/support/home#/)\.

   

  In addition to a user name and password, you can also generate [access keys](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html) for each user\. You can use these keys when you access AWS services programmatically, either through [one of the several SDKs](https://aws.amazon.com/tools/) or by using the [AWS Command Line Interface \(CLI\)](https://aws.amazon.com/cli/)\. The SDK and CLI tools use the access keys to cryptographically sign your request\. If you don’t use AWS tools, you must sign the request yourself\. Lambda supports *Signature Version 4*, a protocol for authenticating inbound API requests\. For more information about authenticating requests, see [Signature Version 4 Signing Process](http://docs.aws.amazon.com/general/latest/gr/signature-version-4.html) in the *AWS General Reference*\.

   

+ **IAM role** – An [IAM role](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) is an IAM identity that you can create in your account that has specific permissions\. It is similar to an *IAM user*, but it is not associated with a specific person\. An IAM role enables you to obtain temporary access keys that can be used to access AWS services and resources\. IAM roles with temporary credentials are useful in the following situations:

   

  + **Federated user access** – Instead of creating an IAM user, you can use existing user identities from AWS Directory Service, your enterprise user directory, or a web identity provider\. These are known as *federated users*\. AWS assigns a role to a federated user when access is requested through an [identity provider](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_providers.html)\. For more information about federated users, see [Federated Users and Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/introduction_access-management.html#intro-access-roles) in the *IAM User Guide*\. 

     

  + **AWS service access** – You can use an IAM role in your account to grant an AWS service permissions to access your account’s resources\. For example, you can create a role that allows Amazon Redshift to access an Amazon S3 bucket on your behalf and then load data from that bucket into an Amazon Redshift cluster\. For more information, see [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide*\. 

      

  + **Applications running on Amazon EC2** – You can use an IAM role to manage temporary credentials for applications that are running on an EC2 instance and making AWS API requests\. This is preferable to storing access keys within the EC2 instance\. To assign an AWS role to an EC2 instance and make it available to all of its applications, you create an instance profile that is attached to the instance\. An instance profile contains the role and enables programs that are running on the EC2 instance to get temporary credentials\. For more information, see [Using Roles for Applications on Amazon EC2](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2.html) in the *IAM User Guide*\. 

## Access Control<a name="access-control"></a>

You can have valid credentials to authenticate your requests, but unless you have permissions you cannot create or access AWS Lambda resources\. For example, you must have permissions to create a Lambda function, add an event source, and publish a version of your Lambda function\.

The following sections describe how to manage permissions for AWS Lambda\. We recommend that you read the overview first\.

+ [Overview of Managing Access Permissions to Your AWS Lambda Resources](access-control-overview.md)

+  [Using Identity\-Based Policies \(IAM Policies\) for AWS Lambda](access-control-identity-based.md) 

+  [Using Resource\-Based Policies for AWS Lambda \(Lambda Function Policies\)](access-control-resource-based.md) 