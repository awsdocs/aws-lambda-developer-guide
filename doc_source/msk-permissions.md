# Managing access and permissions for an Amazon MSK cluster<a name="msk-permissions"></a>

Lambda polls your Apache Kafka topic partitions for new records and invokes your Lambda function [synchronously](invocation-sync.md)\. To update other AWS resources that your cluster uses, your Lambda function—as well as your AWS Identity and Access Management \(IAM\) users and roles—must have permission to perform these actions\.

This page describes how to grant permission to Lambda and other users of your Amazon MSK cluster\.

## Required Lambda function permissions<a name="msk-api-actions"></a>

To read records from your Amazon MSK cluster on your behalf, your Lambda function's [execution role](lambda-intro-execution-role.md) must have permission\. You can either add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role, or create a custom policy with permission to perform the following actions:
+ [kafka:DescribeCluster](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn.html#clusters-clusterarnget)
+ [kafka:GetBootstrapBrokers](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn-bootstrap-brokers.html#clusters-clusterarn-bootstrap-brokersget)
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

## Adding a policy to your execution role<a name="msk-permissions-add-policy"></a>

Follow these steps to add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role using the IAM console\.

**To add an AWS managed policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the IAM console\.

1. In the search box, enter the policy name \(`AWSLambdaMSKExecutionRole`\)\.

1. Select the policy from the list, and then choose **Policy actions**, **Attach**\.

1. Select your execution role from the list, and then choose **Attach policy**\.

## Granting users access with an IAM policy<a name="msk-permissions-add-users"></a>

By default, IAM users and roles don't have permission to perform Amazon MSK API operations\. To grant access to users in your organization or account, you might need an identity\-based policy\. For more information, see [Amazon Managed Streaming for Apache Kafka Identity\-Based Policy Examples](https://docs.aws.amazon.com/msk/latest/developerguide/security_iam_id-based-policy-examples.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

## Using SASL/SCRAM authentication<a name="msk-permissions-add-secret"></a>

Amazon MSK supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication\. You can control access to your Amazon MSK clusters by setting up user name and password authentication using an AWS Secrets Manager secret\. For more information, see [Using Username and Password Authentication with AWS Secrets Manager](https://docs.aws.amazon.com/msk/latest/developerguide/msk-password.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.