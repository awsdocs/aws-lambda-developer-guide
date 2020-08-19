# AWS Lambda execution role<a name="lambda-intro-execution-role"></a>

An AWS Lambda function's execution role grants it permission to access AWS services and resources\. You provide this role when you create a function, and Lambda assumes the role when your function is invoked\. You can create an execution role for development that has permission to send logs to Amazon CloudWatch and upload trace data to AWS X\-Ray\.

**To view a function's execution role**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Choose **Permissions**\.

1. The resource summary shows the services and resources that the function has access to\. The following example shows the CloudWatch Logs permissions that Lambda adds to an execution role when you create it in the Lambda console\.  
![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/permissions-executionrole.png)

1. Choose a service from the drop\-down menu to see permissions related to that service\.

You can add or remove permissions from a function's execution role at any time, or configure your function to use a different role\. Add permissions for any services that your function calls with the AWS SDK, and for services that Lambda uses to enable optional features\.

When you add permissions to your function, make an update to its code or configuration as well\. This forces running instances of your function, which have out\-of\-date credentials, to stop and be replaced\.

## Creating an execution role in the IAM console<a name="permissions-executionrole-console"></a>

By default, Lambda creates an execution role with minimal permissions when you [create a function](getting-started-create-function.md) in the Lambda console\. You can also create an execution role in the IAM console\.

**To create an execution role in the IAM console**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Under **Common use cases**, choose **Lambda**\.

1. Choose **Next: Permissions**\.

1. Under **Attach permissions policies**, choose the **AWSLambdaBasicExecutionRole** and **AWSXRayDaemonWriteAccess** managed policies\.

1. Choose **Next: Tags**\.

1. Choose **Next: Review**\.

1. For **Role name**, enter **lambda\-role**\.

1. Choose **Create role**\.

For detailed instructions, see [Creating a role](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html#roles-creatingrole-service-console) in the IAM User Guide\.

## Managing roles with the IAM API<a name="permissions-executionrole-api"></a>

An execution role is an IAM role that Lambda has permission to assume when you invoke a function\. To create an execution role with the AWS CLI, use the `create-role` command\.

```
$ aws iam create-role --role-name lambda-ex --assume-role-policy-document file://trust-policy.json
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

The `trust-policy.json` file is a JSON file in the current directory that defines the [trust policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html) for the role\. This trust policy allows Lambda to use the role's permissions by giving the service principal `lambda.amazonaws.com` permission to call the AWS Security Token Service `AssumeRole` action\.

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

You can also specify the trust policy inline\. Requirements for escaping quotes in the JSON string vary depending on your shell\.

```
$ aws iam create-role --role-name lambda-ex --assume-role-policy-document '{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}'
```

To add permissions to the role, use the `attach-policy-to-role` command\. Start by adding the `AWSLambdaBasicExecutionRole` managed policy\.

```
$ aws iam attach-role-policy --role-name lambda-ex --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

## Managed policies for Lambda features<a name="permissions-executionrole-features"></a>

The following managed policies provide permissions that are required to use Lambda features:
+ **AWSLambdaBasicExecutionRole** – Permission to upload logs to CloudWatch\.
+ **AWSLambdaDynamoDBExecutionRole** – Permission to read records from an Amazon DynamoDB stream\.
+ **AWSLambdaKinesisExecutionRole** – Permission to read events from an Amazon Kinesis data stream or consumer\.
+ **AWSLambdaMSKExecutionRole** – Permission to read records from an Amazon MSK cluster\.
+ **AWSLambdaSQSQueueExecutionRole** – Permission to read a message from an Amazon Simple Queue Service \(Amazon SQS\) queue\.
+ **AWSLambdaVPCAccessExecutionRole** – Permission to manage elastic network interfaces to connect your function to a VPC\.
+ **AWSXRayDaemonWriteAccess** – Permission to upload trace data to X\-Ray\.

For some features, the Lambda console attempts to add missing permissions to your execution role in a customer managed policy\. These policies can become numerous\. Add the relevant managed policies to your execution role before enabling features to avoid creating extra policies\.

When you use an [event source mapping](invocation-eventsourcemapping.md) to invoke your function, Lambda uses the execution role to read event data\. For example, an event source mapping for Amazon Kinesis reads events from a data stream and sends them to your function in batches\. You can use event source mappings with the following services:

**Services that Lambda reads events from**
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon Managed Streaming for Apache Kafka](with-msk.md)
+ [Amazon Simple Queue Service](with-sqs.md)

In addition to the managed policies, the Lambda console provides templates for creating a custom policy that has the permissions related to additional use cases\. When you create a function in the Lambda console, you can choose to create a new execution role with permissions from one or more templates\. These templates are also applied automatically when you create a function from a blueprint, or when you configure options that require access to other services\. Example templates are available in this guide's [GitHub repository](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/iam-policies)\.