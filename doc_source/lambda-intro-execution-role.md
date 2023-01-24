# Lambda execution role<a name="lambda-intro-execution-role"></a>

A Lambda function's execution role is an AWS Identity and Access Management \(IAM\) role that grants the function permission to access AWS services and resources\. For example, you might create an execution role that has permission to send logs to Amazon CloudWatch and upload trace data to AWS X\-Ray\.

You provide an execution role when you create a function\. **When you invoke your function, Lambda automatically provides your function with temporary credentials by assuming this role\.** You don't have to call `sts:AssumeRole` in your function code\.

**To view a function's execution role**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of a function\.

1. Choose **Configuration**, and then choose **Permissions**\.

1. Under **Resource summary**, review the services and resources that the function can access\.

1. Choose a service from the dropdown list to see permissions related to that service\.

You can add or remove permissions from a function's execution role at any time, or configure your function to use a different role\. Add permissions for any services that your function calls with the AWS SDK, and for services that Lambda uses to enable optional features\.

When you add permissions to your function, update its code or configuration as well\. This forces running instances of your function, which have outdated credentials, to stop and be replaced\.

**Topics**
+ [Creating an execution role in the IAM console](#permissions-executionrole-console)
+ [Grant least privilege access to your Lambda execution role](#permissions-executionrole-least-privilege)
+ [Managing roles with the IAM API](#permissions-executionrole-api)
+ [Session duration for temporary security credentials](#permissions-executionrole-session)
+ [AWS managed policies for Lambda features](#permissions-executionrole-features)
+ [Working with Lambda execution environment credentials](#permissions-executionrole-source-function-arn)

## Creating an execution role in the IAM console<a name="permissions-executionrole-console"></a>

By default, Lambda creates an execution role with minimal permissions when you [create a function in the Lambda console](getting-started.md#getting-started-create-function)\. You can also create an execution role in the IAM console\.

**To create an execution role in the IAM console**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Under **Use case**, choose **Lambda**\.

1. Choose **Next**\.

1. Select the AWS managed policies **AWSLambdaBasicExecutionRole** and **AWSXRayDaemonWriteAccess**\.

1. Choose **Next**\.

1. Enter a **Role name** and then choose **Create role**\.

For detailed instructions, see [Creating a role for an AWS service \(console\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html#roles-creatingrole-service-console) in the *IAM User Guide*\.

## Grant least privilege access to your Lambda execution role<a name="permissions-executionrole-least-privilege"></a>

When you first create an IAM role for your Lambda function during the development phase, you might sometimes grant permissions beyond what is required\. Before publishing your function in the production environment, as a best practice, adjust the policy to include only the required permissions\. For more information, see [Apply least\-privilege permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#grant-least-privilege) in the *IAM User Guide*\.

Use IAM Access Analyzer to help identify the required permissions for the IAM execution role policy\. IAM Access Analyzer reviews your AWS CloudTrail logs over the date range that you specify and generates a policy template with only the permissions that the function used during that time\. You can use the template to create a managed policy with fine\-grained permissions, and then attach it to the IAM role\. That way, you grant only the permissions that the role needs to interact with AWS resources for your specific use case\.

For more information, see [Generate policies based on access activity](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_generate-policy.html) in the *IAM User Guide*\.

## Managing roles with the IAM API<a name="permissions-executionrole-api"></a>

To create an execution role with the AWS Command Line Interface \(AWS CLI\), use the create\-role command\. When using this command, you can specify the [ trust policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html#delegation) inline\. A role's trust policy gives the specified principals permission to assume the role\. In the following example, you grant the Lambda service principal permission to assume your role\. Note that requirements for escaping quotes in the JSON string may vary depending on your shell\.

```
aws iam create-role --role-name lambda-ex --assume-role-policy-document '{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}'
```

You can also define the trust policy for the role using a separate JSON file\. In the following example, `trust-policy.json` is a file in the current directory\.

**Example trust\-policy\.json**  

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

```
aws iam create-role --role-name lambda-ex --assume-role-policy-document file://trust-policy.json
```

You should see the following output:

```
{
    "Role": {
        "Path": "/",
        "RoleName": "lambda-ex",
        "RoleId": "AROAQFOXMPL6TZ6ITKWND",
        "Arn": "arn:aws:iam::123456789012:role/lambda-ex",
        "CreateDate": "2020-01-17T23:19:12Z",
        "AssumeRolePolicyDocument": {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Effect": "Allow",
                    "Principal": {
                        "Service": "lambda.amazonaws.com"
                    },
                    "Action": "sts:AssumeRole"
                }
            ]
        }
    }
}
```

**Note**  
Lambda automatically assumes your execution role when you invoke your function\. You should avoid calling `sts:AssumeRole` manually in your function code\. If your use case requires that the role assumes itself, you must include the role itself as a trusted principal in your role's trust policy\. For more information on how to modify a role trust policy, see [ Modifying a role trust policy \(console\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/roles-managingrole-editing-console.html#roles-managingrole_edit-trust-policy) in the IAM User Guide\.

To add permissions to the role, use the attach\-policy\-to\-role command\. Start by adding the `AWSLambdaBasicExecutionRole` managed policy\.

```
aws iam attach-role-policy --role-name lambda-ex --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

## Session duration for temporary security credentials<a name="permissions-executionrole-session"></a>

Lambda assumes the execution role associated with your function to fetch temporary security credentials which are then available as environment variables during a function's invocation\. If you use these temporary credentials outside of Lambda, such as to create a presigned Amazon S3 URL, you can't control the session duration\. The IAM maximum session duration setting doesn't apply to sessions that are assumed by AWS services such as Lambda\. Use the [sts:AssumeRole](https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRole.html) action if you need control over session duration\.

## AWS managed policies for Lambda features<a name="permissions-executionrole-features"></a>

The following AWS managed policies provide permissions that are required to use Lambda features\.


| Change | Description | Date | 
| --- | --- | --- | 
|  **[ AWSLambdaMSKExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaMSKExecutionRole)** – Lambda added the [kafka:DescribeClusterV2](https://docs.aws.amazon.com/MSK/2.0/APIReference/v2-clusters-clusterarn.html#v2-clusters-clusterarnget) permission to this policy\.  |  `AWSLambdaMSKExecutionRole` grants permissions to read and access records from an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster, manage elastic network interfaces \(ENIs\), and write to CloudWatch Logs\.  |  June 17, 2022  | 
|  **[ AWSLambdaBasicExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaBasicExecutionRole` grants permissions to upload logs to CloudWatch\.  |  February 14, 2022  | 
|  **[ AWSLambdaDynamoDBExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaDynamoDBExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaDynamoDBExecutionRole` grants permissions to read records from an Amazon DynamoDB stream and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaKinesisExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaKinesisExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaKinesisExecutionRole` grants permissions to read events from an Amazon Kinesis data stream and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaMSKExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaMSKExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaMSKExecutionRole` grants permissions to read and access records from an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster, manage elastic network interfaces \(ENIs\), and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaSQSQueueExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaSQSQueueExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaSQSQueueExecutionRole` grants permissions to read a message from an Amazon Simple Queue Service \(Amazon SQS\) queue and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaVPCAccessExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaVPCAccessExecutionRole` grants permissions to manage ENIs within an Amazon VPC and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess)** – Lambda started tracking changes to this policy\.  |  `AWSXRayDaemonWriteAccess` grants permissions to upload trace data to X\-Ray\.  |  February 14, 2022  | 
|  **[ CloudWatchLambdaInsightsExecutionRolePolicy](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/CloudWatchLambdaInsightsExecutionRolePolicy)** – Lambda started tracking changes to this policy\.  |  `CloudWatchLambdaInsightsExecutionRolePolicy` grants permissions to write runtime metrics to CloudWatch Lambda Insights\.  |  February 14, 2022  | 
|  **[ AmazonS3ObjectLambdaExecutionRolePolicy](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AmazonS3ObjectLambdaExecutionRolePolicy)** – Lambda started tracking changes to this policy\.  |  `AmazonS3ObjectLambdaExecutionRolePolicy` grants permissions to interact with Amazon Simple Storage Service \(Amazon S3\) object Lambda and to write to CloudWatch Logs\.  |  February 14, 2022  | 

For some features, the Lambda console attempts to add missing permissions to your execution role in a customer managed policy\. These policies can become numerous\. To avoid creating extra policies, add the relevant AWS managed policies to your execution role before enabling features\.

When you use an [event source mapping](invocation-eventsourcemapping.md) to invoke your function, Lambda uses the execution role to read event data\. For example, an event source mapping for Kinesis reads events from a data stream and sends them to your function in batches\. You can use event source mappings with the following services:

**Services that Lambda reads events from**
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon MQ](with-mq.md)
+ [Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](with-msk.md)
+ [Self\-managed Apache Kafka](with-kafka.md)
+ [Amazon Simple Queue Service \(Amazon SQS\)](with-sqs.md)

In addition to the AWS managed policies, the Lambda console provides templates for creating a custom policy with permissions for additional use cases\. When you create a function in the Lambda console, you can choose to create a new execution role with permissions from one or more templates\. These templates are also applied automatically when you create a function from a blueprint, or when you configure options that require access to other services\. Example templates are available in this guide's [GitHub repository](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/iam-policies)\.

## Working with Lambda execution environment credentials<a name="permissions-executionrole-source-function-arn"></a>

It's common for your Lambda function code to make API requests to other AWS services\. To make these requests, Lambda generates an ephemeral set of credentials by assuming your function's execution role\. Lambda also automatically injects the source function Amazon Resource Name \(ARN\) into the credentials context used to make the API request from your function code\.

Lambda injects the source function ARN into the credentials context only if the request is an AWS API request that comes from within your execution environment\. AWS API calls that Lambda makes outside of your execution environment on your behalf using the same execution role don't contain the source function ARN\. Examples of such API calls outside the execution environment include:
+ Calls to AWS Key Management Service \(AWS KMS\) to automatically encrypt and decrypt your environment variables\.
+ Calls to CloudWatch for logging\.

With the source function ARN in the credentials context, you can verify whether a call to your resource came from a specific Lambda function's code\. To verify this, use the `lambda:SourceFunctionArn` condition key in an IAM identity\-based policy or service control policy \(SCP\)\.

**Note**  
You cannot use the `lambda:SourceFunctionArn` condition key in resource\-based policies\.

With this condition key in your identity\-based policies or SCPs, you can implement security controls for the API actions that your function code makes to other AWS services\. This has a few key security applications, such as helping you identify the source of a credential leak\.

**Note**  
The `lambda:SourceFunctionArn` condition key is different from the `lambda:FunctionArn` and `aws:SourceArn` condition keys\. The `lambda:FunctionArn` condition key applies only to [event source mappings](invocation-eventsourcemapping.md) and helps define which functions your event source can invoke\. The `aws:SourceArn` condition key applies only to policies where your Lambda function is the target resource, and helps define which other AWS services and resources can invoke that function\. The `lambda:SourceFunctionArn` condition key can apply to any identity\-based policy or SCP to define the specific Lambda functions that have permissions to make specific AWS API calls to other resources\.

To use `lambda:SourceFunctionArn` in your policy, include it as a condition with any of the [ARN condition operators](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_condition_operators.html#Conditions_ARN)\. The value of the key must be a valid ARN\.

For example, suppose your Lambda function code makes an `s3:PutObject` call that targets a specific Amazon S3 bucket\. You might want to allow only one specific Lambda function to have `s3:PutObject` access that bucket\. In this case, your function's execution role should have a policy attached that looks like this:

**Example policy granting a specific Lambda function access to an Amazon S3 resource**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ExampleSourceFunctionArn",
            "Effect": "Allow",
            "Action": "s3:PutObject",
            "Resource": "arn:aws:s3:::lambda_bucket/*",
            "Condition": {
                "ArnEquals": {
                    "lambda:SourceFunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:source_lambda"
                }
            }
        }
    ]
}
```

This policy allows only `s3:PutObject` access if the source is the Lambda function with ARN `arn:aws:lambda:us-east-1:123456789012:function:source_lambda`\. This policy doesn't allow `s3:PutObject` access to any other calling identity\. This is true even if a different function or entity makes an `s3:PutObject` call with the same execution role\.

You can also use `lambda:SourceFunctionArn` in [service control policies](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_manage_policies_scps.html)\. For example, suppose you want to restrict access to your bucket to either a single Lambda function's code or to calls from a specific Amazon Virtual Private Cloud \(VPC\)\. The following SCP illustrates this\.

**Example policy denying access to Amazon S3 under specific conditions**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "s3:*"
            ],
            "Resource": "arn:aws:s3:::lambda_bucket/*",
            "Effect": "Deny",
            "Condition": {
                "StringNotEqualsIfExists": {
                    "aws:SourceVpc": [
                        "vpc-12345678"
                    ]
                },
                "ArnNotEqualsIfExists": {
                    "lambda:SourceFunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:source_lambda"
                }
            }
        }
    ]
}
```

This policy denies all S3 actions unless they come from a specific Lambda function with ARN `arn:aws:lambda:*:123456789012:function:source_lambda`, or unless they come from the specified VPC\. The `StringNotEqualsIfExists` operator tells IAM to process this condition only if the `aws:SourceVpc` key is present in the request\. Similarly, IAM considers the `ArnNotEqualsIfExists` operator only if the `lambda:SourceFunctionArn` exists\.