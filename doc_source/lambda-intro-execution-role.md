# AWS Lambda execution role<a name="lambda-intro-execution-role"></a>

A Lambda function's execution role is an AWS Identity and Access Management \(IAM\) role that grants the function permission to access AWS services and resources\. You provide this role when you create a function, and Lambda assumes the role when your function is invoked\. You can create an execution role for development that has permission to send logs to Amazon CloudWatch and to upload trace data to AWS X\-Ray\.

**To view a function's execution role**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Permissions**\.

1. Under **Resource summary**, view the services and resources that the function can access\. 

1. Choose a service from the dropdown list to see permissions related to that service\.

You can add or remove permissions from a function's execution role at any time, or configure your function to use a different role\. Add permissions for any services that your function calls with the AWS SDK, and for services that Lambda uses to enable optional features\.

When you add permissions to your function, make an update to its code or configuration as well\. This forces running instances of your function, which have out\-of\-date credentials, to stop and be replaced\.

**Topics**
+ [Creating an execution role in the IAM console](#permissions-executionrole-console)
+ [Grant least privilege access to your Lambda execution role](#permissions-executionrole-least-privilege)
+ [Managing roles with the IAM API](#permissions-executionrole-api)
+ [AWS managed policies for Lambda features](#permissions-executionrole-features)

## Creating an execution role in the IAM console<a name="permissions-executionrole-console"></a>

By default, Lambda creates an execution role with minimal permissions when you [create a function in the Lambda console](getting-started.md#getting-started-create-function)\. You can also create an execution role in the IAM console\.

**To create an execution role in the IAM console**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Under **Common use cases**, choose **Lambda**\.

1. Choose **Next: Permissions**\.

1. Under **Attach permissions policies**, choose the AWS managed policies **AWSLambdaBasicExecutionRole** and **AWSXRayDaemonWriteAccess**\.

1. Choose **Next: Tags**\.

1. Choose **Next: Review**\.

1. For **Role name**, enter **lambda\-role**\.

1. Choose **Create role**\.

For detailed instructions, see [Creating a role for an AWS service \(console\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html#roles-creatingrole-service-console) in the *IAM User Guide*\.

## Grant least privilege access to your Lambda execution role<a name="permissions-executionrole-least-privilege"></a>

When you first create an IAM role for your Lambda function during the development phase, you might sometimes grant permissions beyond what is required\. Before publishing your function in the production environment, best practice is to adjust the policy to include only the required permissions\. For more information, see [granting least privilege](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#grant-least-privilege)\. 

Use IAM Access Analyzer to help identify the required permissions for the IAM execution role policy\. IAM Access Analyzer reviews your AWS CloudTrail logs over the date range that you specify and generates a policy template with only the permissions that the function used during that time\. You can use the template to create a managed policy with fine\-grained permissions, and then attach it to the IAM role\. That way, you grant only the permissions that the role needs to interact with AWS resources for your specific use case\. 

To learn more, see [Generate policies based on access activity](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_generate-policy.html) in the *IAM User Guide*\.

## Managing roles with the IAM API<a name="permissions-executionrole-api"></a>

To create an execution role with the AWS Command Line Interface \(AWS CLI\), use the `create-role` command\.

In the following example, you specify the trust policy inline\. Requirements for escaping quotes in the JSON string vary depending on your shell\.

```
aws iam create-role --role-name lambda-ex --assume-role-policy-document '{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}'
```

You can also define the [trust policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html) for the role using a JSON file\. In the following example, `trust-policy.json` is a file in the current directory\. This trust policy allows Lambda to use the role's permissions by giving the service principal `lambda.amazonaws.com` permission to call the AWS Security Token Service `AssumeRole` action\.

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

To add permissions to the role, use the `attach-policy-to-role` command\. Start by adding the `AWSLambdaBasicExecutionRole` managed policy\.

```
aws iam attach-role-policy --role-name lambda-ex --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

## AWS managed policies for Lambda features<a name="permissions-executionrole-features"></a>

The following AWS managed policies provide permissions that are required to use Lambda features\.


| Change | Description | Date | 
| --- | --- | --- | 
|  **[ AWSLambdaBasicExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaBasicExecutionRole` grants permissions to upload logs to CloudWatch\.  |  February 14, 2022  | 
|  **[ AWSLambdaDynamoDBExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaDynamoDBExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaDynamoDBExecutionRole` grants permissions to read records from an Amazon DynamoDB stream and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaKinesisExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaKinesisExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaKinesisExecutionRole` grants permissions to read events from an Amazon Kinesis data stream and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaMSKExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaMSKExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaMSKExecutionRole` grants permissions to read and access records from an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster, manage elastic network interfaces \(ENIs\), and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaSQSQueueExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaSQSQueueExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaSQSQueueExecutionRole` grants permissions to read a message from an Amazon Simple Queue Service \(Amazon SQS\) queue and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSLambdaVPCAccessExecutionRole](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole)** – Lambda started tracking changes to this policy\.  |  `AWSLambdaVPCAccessExecutionRole` grants permissions to manage ENIs within an Amazon VPC and write to CloudWatch Logs\.  |  February 14, 2022  | 
|  **[ AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess)** – Lambda started tracking changes to this policy\.  |  `AWSXRayDaemonWriteAccess` grants permissions to upload trace data to X\-Ray\.  |  February 14, 2022  | 
|  **[ CloudWatchLambdaInsightsExecutionRolePolicy](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/CloudWatchLambdaInsightsExecutionRolePolicy)** – Lambda started tracking changes to this policy\.  |  `CloudWatchLambdaInsightsExecutionRolePolicy` grants permissions to write runtime metrics to CloudWatch Lambda Insights\.  |  February 14, 2022  | 
|  **[ AmazonS3ObjectLambdaExecutionRolePolicy](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/service-role/AmazonS3ObjectLambdaExecutionRolePolicy)** – Lambda started tracking changes to this policy\.  |  `AmazonS3ObjectLambdaExecutionRolePolicy` grants permissions to interact with Amazon S3 Object Lambda and write to CloudWatch Logs\.  |  February 14, 2022  | 

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