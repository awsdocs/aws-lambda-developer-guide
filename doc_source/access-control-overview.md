# Overview of Managing Access Permissions to Your AWS Lambda Resources<a name="access-control-overview"></a>

Every AWS resource is owned by an AWS account, and permissions to create or access a resource are governed by permissions policies\. An account administrator can attach permissions policies to IAM identities \(that is, users, groups, and roles\), and some services \(such as AWS Lambda\) also support attaching permissions policies to resources\.

**Note**  
An *account administrator* \(or administrator user\) is a user with administrator privileges\. For more information, see [IAM Best Practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html) in the *IAM User Guide*\.

When granting permissions, you decide who is getting the permissions, the resources they get permissions for, and the specific actions that you want to allow on those resources\.

**Topics**
+ [AWS Lambda Resources and Operations](#access-control-resources)
+ [Understanding Resource Ownership](#access-control-resource-ownership)
+ [Managing Access to Resources](#access-control-manage-access-intro)
+ [Specifying Policy Elements: Actions, Effects, Resources, and Principals](#access-control-specify-lambda-actions)
+ [Specifying Conditions in a Policy](#specifying-conditions)

## AWS Lambda Resources and Operations<a name="access-control-resources"></a>

In AWS Lambda, the primary resources are a Lambda *function* and an *event source mapping*\. You create an event source mapping in the AWS Lambda pull model to associate a Lambda function with an event source\. For more information, see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\.

AWS Lambda also supports additional resource types, *alias* and *version*\. However, you can create aliases and versions only in the context of an existing Lambda function\. These are referred to as *subresources*\. 

These resources and subresources have unique Amazon Resource Names \(ARNs\) associated with them as shown in the following table\. 


****  

| Resource Type | ARN Format  | 
| --- | --- | 
| Function |  arn:aws:lambda:*region*:*account\-id*:function:*function\-name*  | 
| Function alias |   arn:aws:lambda:*region*:*account\-id*:function:*function\-name*:*alias\-name*   | 
| Function version |   arn:aws:lambda:*region*:*account\-id*:function:*function\-name*:*version*  | 
| Event source mapping |   arn:aws:lambda:*region*:*account\-id*:event\-source\-mapping:*event\-source\-mapping\-id*  | 

AWS Lambda provides a set of operations to work with the Lambda resources\. For a list of available operations, see [Actions](API_Operations.md)\.

## Understanding Resource Ownership<a name="access-control-resource-ownership"></a>

A *resource owner* is the AWS account that created the resource\. That is, the resource owner is the AWS account of the *principal entity* \(the root account, an IAM user, or an IAM role\) that authenticates the request that creates the resource\. The following examples illustrate how this works:
+ If you use the root account credentials of your AWS account to create a Lambda function, your AWS account is the owner of the resource \(in Lambda, the resource is the Lambda function\)\.
+ If you create an IAM user in your AWS account and grant permissions to create a Lambda function to that user, the user can create a Lambda function\. However, your AWS account, to which the user belongs, owns the Lambda function resource\.
+ If you create an IAM role in your AWS account with permissions to create a Lambda function, anyone who can assume the role can create a Lambda function\. Your AWS account, to which the role belongs, owns the Lambda function resource\. 

## Managing Access to Resources<a name="access-control-manage-access-intro"></a>

A *permissions policy* describes who has access to what\. The following section explains the available options for creating permissions policies\.

**Note**  
This section discusses using IAM in the context of AWS Lambda\. It doesn't provide detailed information about the IAM service\. For complete IAM documentation, see [What Is IAM?](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html) in the *IAM User Guide*\. For information about IAM policy syntax and descriptions, see [AWS IAM Policy Reference](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies.html) in the *IAM User Guide*\.

Policies attached to an IAM identity are referred to as *identity\-based* policies \(IAM polices\) and policies attached to a resource are referred to as *resource\-based* policies\. AWS Lambda supports both identity\-based \(IAM policies\) and resource\-based policies\.

**Topics**
+ [Identity\-Based Policies \(IAM Policies\)](#access-control-manage-access-identity-based)
+ [Resource\-Based Policies \(Lambda Function Policies\)](#access-control-manage-access-resource-based)

### Identity\-Based Policies \(IAM Policies\)<a name="access-control-manage-access-identity-based"></a>

You can attach policies to IAM identities\. For example, you can do the following: 
+ **Attach a permissions policy to a user or a group in your account** – An account administrator can use a permissions policy that is associated with a particular user to grant permissions for that user to create a Lambda function\. 
+ **Attach a permissions policy to a role \(grant cross\-account permissions\)** – You can attach an identity\-based permissions policy to an IAM role to grant cross\-account permissions\. For example, the administrator in Account A can create a role to grant cross\-account permissions to another AWS account \(for example, Account B\) or an AWS service as follows:

  1. Account A administrator creates an IAM role and attaches a permissions policy to the role that grants permissions on resources in Account A\.

  1. Account A administrator attaches a trust policy to the role identifying Account B as the principal who can assume the role\. 

  1. Account B administrator can then delegate permissions to assume the role to any users in Account B\. Doing this allows users in Account B to create or access resources in Account A\. The principal in the trust policy can also be an AWS service principal if you want to grant an AWS service permissions to assume the role\.

   For more information about using IAM to delegate permissions, see [Access Management](https://docs.aws.amazon.com/IAM/latest/UserGuide/access.html) in the *IAM User Guide*\. 

The following is an example policy that grants permissions for the `lambda:ListFunctions` action on all resources\. In the current implementation, Lambda doesn't support identifying specific resources using the resource ARNs \(also referred to as resource\-level permissions\) for some of the API actions, so you must specify a wildcard character \(\*\)\.

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

For more information about using identity\-based policies with Lambda, see [Using Identity\-Based Policies \(IAM Policies\) for AWS Lambda](access-control-identity-based.md)\. For more information about users, groups, roles, and permissions, see [Identities \(Users, Groups, and Roles\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/id.html) in the *IAM User Guide*\. 

### Resource\-Based Policies \(Lambda Function Policies\)<a name="access-control-manage-access-resource-based"></a>

Each Lambda function can have resource\-based permissions policies associated with it\. For Lambda, a Lambda function is the primary resource and these policies are referred to as *Lambda function policies*\. You can use a Lambda function policy to grant cross\-account permissions as an alternative to using identity\-based policies with IAM roles\. For example, you can grant Amazon S3 permissions to invoke your Lambda function by simply adding permissions to the Lambda function policy instead of creating an IAM role\.

**Important**  
Lambda function policies are primarily used when you are setting up an event source in AWS Lambda to grant a service or an event source permissions to invoke your Lambda function \(see [Invoke](API_Invoke.md)\)\. An exception to this is when an event source \(for example, Amazon DynamoDB or Kinesis\) uses the pull model, where permissions are managed in the Lambda function execution role instead\. For more information, see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\.

The following is an example Lambda function policy that has one statement\. The statement allows the Amazon S3 service principal permission for the `lambda:InvokeFunction` action on a Lambda function called HelloWorld\. The condition ensures that the bucket where the event occurred is owned by the same account that owns the Lambda function\.

```
{
   "Policy":{
      "Version":"2012-10-17",
      "Statement":[
         {
            "Effect":"Allow",
            "Principal":{
               "Service":"s3.amazonaws.com"
            },
            "Action":"lambda:InvokeFunction",
            "Resource":"arn:aws:lambda:region:account-id:function:HelloWorld",
            "Sid":"65bafc90-6a1f-42a8-a7ab-8aa9bc877985",
            "Condition":{
               "StringEquals":{
                  "AWS:SourceAccount":"account-id"
               },
               "ArnLike":{
                  "AWS:SourceArn":"arn:aws:s3:::ExampleBucket"
               }
            }
         }
      ]
   }
}
```

For more information about using resource\-based policies with Lambda, see [Using Resource\-Based Policies for AWS Lambda \(Lambda Function Policies\)](access-control-resource-based.md)\. For additional information about using IAM roles \(identity\-based policies\) as opposed to resource\-based policies, see [How IAM Roles Differ from Resource\-based Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_compare-resource-policies.html) in the *IAM User Guide*\.

## Specifying Policy Elements: Actions, Effects, Resources, and Principals<a name="access-control-specify-lambda-actions"></a>

For each AWS Lambda resource \(see [AWS Lambda Resources and Operations](#access-control-resources)\), the service defines a set of API operations \(see [Actions](API_Operations.md)\)\. To grant permissions for these API operations, Lambda defines a set of actions that you can specify in a policy\. Note that, performing an API operation can require permissions for more than one action\. When granting permissions for specific actions, you also identify the resource on which the actions are allowed or denied\.

The following are the most basic policy elements:
+ **Resource** – In a policy, you use an Amazon Resource Name \(ARN\) to identify the resource to which the policy applies\. For more information, see [AWS Lambda Resources and Operations](#access-control-resources)\. 
+ **Action** – You use action keywords to identify resource operations that you want to allow or deny\. For example, the `lambda:InvokeFunction` permission allows the user permissions to perform the AWS Lambda `Invoke` operation\. 
+ **Effect** – You specify the effect when the user requests the specific action—this can be either allow or deny\. If you don't explicitly grant access to \(allow\) a resource, access is implicitly denied\. You can also explicitly deny access to a resource, which you might do to make sure that a user cannot access it, even if a different policy grants access\.
+ **Principal** – In identity\-based policies \(IAM policies\), the user that the policy is attached to is the implicit principal\. For resource\-based policies, you specify the user, account, service, or other entity that you want to receive permissions \(applies to resource\-based policies only\)\. 

To learn more about IAM policy syntax and descriptions, see [AWS IAM Policy Reference](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies.html) in the *IAM User Guide*\.

For a table showing all of the AWS Lambda API actions and the resources that they apply to, see [Lambda API Permissions: Actions, Resources, and Conditions Reference](lambda-api-permissions-ref.md)\. 

## Specifying Conditions in a Policy<a name="specifying-conditions"></a>

When you grant permissions, you can use the IAM policy language to specify the conditions when a policy should take effect\. For example, you might want a policy to be applied only after a specific date\. For more information about specifying conditions in a policy language, see [Condition](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Condition) in the *IAM User Guide*\.

To express conditions, you use predefined condition keys\. There are no condition keys specific to Lambda\. However, there are AWS\-wide condition keys that you can use as appropriate\. For a complete list of AWS\-wide keys, see [Available Keys for Conditions](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#AvailableKeys) in the *IAM User Guide*\.