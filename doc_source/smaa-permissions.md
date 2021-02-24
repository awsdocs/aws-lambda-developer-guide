# Managing access and permissions for a Self\-managed Apache Kafka cluster<a name="smaa-permissions"></a>

Lambda polls your Apache Kafka topic partitions for new records and invokes your Lambda function [synchronously](invocation-sync.md)\. To update other AWS resources that your cluster uses, your Lambda function—as well as your AWS Identity and Access Management \(IAM\) users and roles—must have permission to perform these actions\.

This page describes how to grant permission to Lambda and other users of your self\-managed Kafka cluster\.

## Required Lambda function permissions<a name="smaa-api-actions-required"></a>

To create and store logs to a log group in Amazon CloudWatch Logs, your Lambda function must have the following permissions in its [execution role](lambda-intro-execution-role.md):
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

## Optional Lambda function permissions<a name="smaa-api-actions-optional"></a>

Your Lambda function might need permission to describe your AWS Secrets Manager secret or your AWS Key Management Service \(AWS KMS\) [customer managed CMK](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#customer-cmk), or to access your virtual private cloud \(VPC\)\.

### Secrets Manager and AWS KMS permissions<a name="smaa-api-actions-vpc"></a>

If your Kafka users access your Apache Kafka brokers over the internet, you must specify a Secrets Manager secret\. For more information, see [Using SASL/SCRAM authentication](#smaa-permissions-add-secret)\.

Your Lambda function might need permission to describe your Secrets Manager secret or decrypt your AWS KMS customer managed CMK\. To access these resources, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:
+ [secretsmanager:GetSecretValue](https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html)
+ [kms:Decrypt](https://docs.aws.amazon.com/kms/latest/APIReference/API_Decrypt.html)

### VPC permissions<a name="smaa-api-actions-vpc"></a>

If only users within your VPC access your Self\-managed Apache Kafka cluster, your Lambda function needs permission to access your Amazon Virtual Private Cloud \(Amazon VPC\) resources, including your VPC, subnets, security groups, and network interfaces\. To access these resources, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)

## Adding permissions to your execution role<a name="smaa-permissions-add-policy"></a>

To access other AWS services that your Self\-managed Apache Kafka cluster uses, Lambda uses the permission policies that you define in your function's [execution role](lambda-intro-execution-role.md)\.

By default, Lambda isn't permitted to perform the required or optional actions for a Self\-managed Apache Kafka cluster\. You must create and define these actions in an IAM trust policy, and then attach the policy to your execution role\. This example shows how you might create a policy that allows Lambda to access your Amazon VPC resources\.

```
{
          "Version":"2012-10-17",
          "Statement":[
             {
                "Effect":"Allow",
                "Action":[
                   "ec2:CreateNetworkInterface",
                   "ec2:DescribeNetworkInterfaces",
                   "ec2:DescribeVpcs",
                   "ec2:DeleteNetworkInterface",
                   "ec2:DescribeSubnets",
                   "ec2:DescribeSecurityGroups"
                ],
                "Resource":"arn:aws:ec2:us-east-1:01234567890:instance/my-instance-name"
             }
          ]
       }
```

For information about creating a JSON policy document on the IAM console, see [Creating policies on the JSON tab](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_create-console.html#access_policies_create-json-editor) in the *IAM User Guide*\.

## Adding users to an IAM policy<a name="smaa-permissions-add-users"></a>

By default, IAM users and roles don't have permission to perform [event source API operations](kafka-using-cluster.md#kafka-hosting-api-operations)\. To grant access to users in your organization or account, you might need to create an identity\-based policy\. For more information, see [Controlling access to AWS resources using policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_controlling.html) in the *IAM User Guide*\.

## Using SASL/SCRAM authentication<a name="smaa-permissions-add-secret"></a>

User name and password authentication for a Self\-managed Apache Kafka cluster uses Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\)\. SCRAM uses secured hashing algorithms and doesn't transmit plaintext passwords between the client and server\. For more information about SASL/SCRAM authentication, see [RFC 5802](https://tools.ietf.org/html/rfc5802)\.

To set up user name and password authentication for your self\-managed Kafka cluster, create a secret in AWS Secrets Manager\. Your non\-AWS cloud provider must provide your user name and password in SASL/SCRAM format\. For example:

```
{
        "username": "ab1c23de",
        "password": "qxbbaLRG7JXYN4NpNMVccP4gY9WZyDbp"
}
```

For more information, see [Tutorial: Creating and retrieving a secret](https://docs.aws.amazon.com/secretsmanager/latest/userguide/tutorials_basic.html) in the *AWS Secrets Manager User Guide*\.