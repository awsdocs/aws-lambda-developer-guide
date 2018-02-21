# AWS Lambda Permissions Model<a name="intro-permission-model"></a>

For the end\-to\-end AWS Lambda\-based applications to work, you have to manage various permissions\. For example:

+ For event sources, except for the stream\-based services \(Amazon Kinesis Data Streams and DynamoDB streams\), you must grant the event source permissions to invoke your AWS Lambda function\. 

   

+ For stream\-based event sources \(Amazon Kinesis Data Streams and DynamoDB streams\), AWS Lambda polls the streams on your behalf and reads new records on the stream, so you need to grant AWS Lambda permissions for the relevant stream actions\.

   

+ When your Lambda function executes, it can access AWS resources in your account \(for example, read an object from your S3 bucket\)\. AWS Lambda executes your Lambda function on your behalf by assuming the role you provided at the time of creating the Lambda function\. Therefore, you need to grant the role the necessary permissions that your Lambda function needs, such as permissions for Amazon S3 actions to read an object\.

The following sections describe permissions management\.


+ [Manage Permissions: Using an IAM Role \(Execution Role\)](#lambda-intro-execution-role)
+ [Manage Permissions: Using a Lambda Function Policy](#intro-permission-model-access-policy)
+ [Suggested Reading](#w3ab1c67c11c20c21)

## Manage Permissions: Using an IAM Role \(Execution Role\)<a name="lambda-intro-execution-role"></a>

Each Lambda function has an IAM role \(execution role\) associated with it\. You specify the IAM role when you create your Lambda function\. Permissions you grant to this role determine what AWS Lambda can do when it assumes the role\. There are two types of permissions that you grant to the IAM role:

+ If your Lambda function code accesses other AWS resources, such as to read an object from an S3 bucket or write logs to CloudWatch Logs, you need to grant permissions for relevant Amazon S3 and CloudWatch actions to the role\.

   

+ If the event source is stream\-based \(Amazon Kinesis Data Streams and DynamoDB streams\), AWS Lambda polls these streams on your behalf\. AWS Lambda needs permissions to poll the stream and read new records on the stream so you need to grant the relevant permissions to this role\. 

For more information about IAM roles, see [Roles \(Delegation and Federation\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. 

**Important**  
The user that creates the IAM role is, in effect, passing permissions to AWS Lambda to assume this role, which requires the user to have permissions for the `iam:PassRole` action\. If an administrator user is creating this role, you don't need to do anything extra to set up permissions for the `iam:PassRole` action because the administrator user has full permissions, including the `iam:PassRole` action\.

To simplify the process for creating an execution role, AWS Lambda provides the following AWS managed \(predefined\) permissions policies that you can use\. These policies include common permissions for specific scenarios:

+ **AWSLambdaBasicExecutionRole** – Grants permissions only for the Amazon CloudWatch Logs actions to write logs\. You can use this policy if your Lambda function does not access any other AWS resources except writing logs\. 

   

+ **AWSLambdaKinesisExecutionRole** – Grants permissions for Amazon Kinesis Data Streams actions, and CloudWatch Logs actions\. If you are writing a Lambda function to process Kinesis stream events you can attach this permissions policy\.

   

+ **AWSLambdaDynamoDBExecutionRole** – Grants permissions for DynamoDB streams actions and CloudWatch Logs actions\. If you are writing a Lambda function to process DynamoDB stream events you can attach this permissions policy\.

   

+ **AWSLambdaVPCAccessExecutionRole** – Grants permissions for Amazon Elastic Compute Cloud \(Amazon EC2\) actions to manage elastic network interfaces \(ENIs\)\. If you are writing a Lambda function to access resources in a VPC in the Amazon Virtual Private Cloud \(Amazon VPC\) service, you can attach this permissions policy\. The policy also grants permissions for CloudWatch Logs actions to write logs\.

  You can find these AWS managed permissions policies in the IAM console\. Search for these policies and you can see the permissions each of these policies grant\.

## Manage Permissions: Using a Lambda Function Policy<a name="intro-permission-model-access-policy"></a>

All supported event sources, except the stream\-based services \(Kinesis and DynamoDB streams\), invoke your Lambda function \(the *push model*\), provided that you grant the necessary permissions\. For example, if you want Amazon S3 to invoke your Lambda function when objects are created in a bucket, Amazon S3 needs permissions to invoke your Lambda function\. 

You can grant these permissions via the function policies\. AWS Lambda provides APIs for you to manage permission in a function policy\. For example, see [AddPermission](API_AddPermission.md)\.

You can also grant cross\-account permissions using the function policy\. For example, if a user\-defined application and the Lambda function it invokes belong to the same AWS account, you don't need to grant explicit permissions\. Otherwise, the AWS account that owns the Lambda function must allow cross\-account permissions in the permissions policy associated with the Lambda function\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-user-cross-account-app-example-10.png)

**Note**  
Instead of using a Lambda function policy, you can create another IAM role that grants the event sources \(for example, Amazon S3 or DynamoDB\) permissions to invoke your Lambda function\. However, you might find that resource policies are easier to set up and they make it easier for you to track which event sources have permissions to invoke your Lambda function\.

For more information about Lambda function policies, see [Using Resource\-Based Policies for AWS Lambda \(Lambda Function Policies\)](access-control-resource-based.md)\. For more information about Lambda permissions, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)\.

## Suggested Reading<a name="w3ab1c67c11c20c21"></a>

If you are new to AWS Lambda, we suggest you read through all of the topics in the How It Works section to familiarize yourself with Lambda\. The next topic is [Lambda Execution Environment and Available Libraries](current-supported-versions.md)\.

After you read all of the topics in the How it Works section, we recommend that you review [Building Lambda Functions](lambda-app.md), try the [Getting Started](getting-started.md) exercise, and then explore the [Use Cases](use-cases.md)\. Each use case provides step\-by\-step instructions for you to set up the end\-to\-end experience\.