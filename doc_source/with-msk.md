# Using Lambda with Amazon MSK<a name="with-msk"></a>

[Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](https://docs.aws.amazon.com/msk/latest/developerguide/what-is-msk.html) is a fully managed service that enables you to build and run applications that use Apache Kafka to process streaming data\. Amazon MSK provides the control\-plane operations, such as those for creating, updating, and deleting clusters\. It supports multiple open\-source versions of Kafka\.

When you create an Amazon MSK cluster, you receive the required hosting and connection information of the cluster\. This information includes the Kafka cluster hostname, topic name, SASL/SCRAM user name and password, and bootstrap server host\-port pairs\.

To support your Kafka cluster on Amazon MSK, you might need to create Amazon Virtual Private Cloud \(Amazon VPC\) networking components\. For more information, see [Using Amazon MSK as an event source for AWS Lambda](http://aws.amazon.com/blogs/compute/using-amazon-msk-as-an-event-source-for-aws-lambda/) on the AWS Compute Blog\.

**Topics**
+ [Managing access and permissions for an Amazon MSK cluster](#msk-permissions)
+ [Adding an Amazon MSK cluster as an event source](#services-msk-topic-add)

## Managing access and permissions for an Amazon MSK cluster<a name="msk-permissions"></a>

Lambda polls your Apache Kafka topic partitions for new records and invokes your Lambda function [synchronously](invocation-sync.md)\. To update other AWS resources that your cluster uses, your Lambda function—as well as your AWS Identity and Access Management \(IAM\) users and roles—must have permission to perform these actions\.

This page describes how to grant permission to Lambda and other users of your Amazon MSK cluster\.

### Required Lambda function permissions<a name="msk-api-actions"></a>

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

### Adding a policy to your execution role<a name="msk-permissions-add-policy"></a>

Follow these steps to add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role using the IAM console\.

**To add an AWS managed policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the IAM console\.

1. In the search box, enter the policy name \(`AWSLambdaMSKExecutionRole`\)\.

1. Select the policy from the list, and then choose **Policy actions**, **Attach**\.

1. Select your execution role from the list, and then choose **Attach policy**\.

### Granting users access with an IAM policy<a name="msk-permissions-add-users"></a>

By default, IAM users and roles don't have permission to perform Amazon MSK API operations\. To grant access to users in your organization or account, you might need an identity\-based policy\. For more information, see [Amazon Managed Streaming for Apache Kafka Identity\-Based Policy Examples](https://docs.aws.amazon.com/msk/latest/developerguide/security_iam_id-based-policy-examples.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

### Using SASL/SCRAM authentication<a name="msk-permissions-add-secret"></a>

Amazon MSK supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication\. You can control access to your Amazon MSK clusters by setting up user name and password authentication using an AWS Secrets Manager secret\. For more information, see [Using Username and Password Authentication with AWS Secrets Manager](https://docs.aws.amazon.com/msk/latest/developerguide/msk-password.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

## Adding an Amazon MSK cluster as an event source<a name="services-msk-topic-add"></a>

You can use a Lambda function to process records from your Apache Kafka cluster when the cluster is configured as an [event](gettingstarted-concepts.md#gettingstarted-concepts-event) source\. To create an [event source mapping](invocation-eventsourcemapping.md), you can add your Kafka cluster as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to add your Kafka cluster and topic as a function trigger using the Lambda console or AWS CLI\.

### Prerequisites<a name="services-msk-prereqs"></a>
+ An Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster and a Kafka topic\. For more information, see [Getting Started Using Amazon MSK](https://docs.aws.amazon.com/msk/latest/developerguide/getting-started.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.
+ A [Lambda execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your MSK cluster uses\. For more information, see [Managing access and permissions for an Amazon MSK cluster](#msk-permissions)\.

### VPC configuration<a name="services-msk-vpc-config"></a>

To get Apache Kafka records from Amazon MSK brokers, Lambda must have access to the Amazon Virtual Private Cloud \(Amazon VPC\) resources associated with your MSK cluster\. To meet Amazon VPC access requirements, do one of the following:
+ We recommend that you deploy Amazon VPC Endpoints \(PrivateLink\) for Lambda and AWS STS services\. For more information, see [Configuring interface VPC endpoints for Lambda](configuration-vpc-endpoints.md)\.

  If authentication is required, also deploy an Amazon VPC Endpoint for the Secrets Manager\.
+ Alternatively, you can configure one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

Your Amazon VPC security groups must be configured with the following rules \(at minimum\):
+ Inbound rules – Allow all traffic on all ports for the security group specified as your event source\.
+ Outbound rules – Allow all traffic on all ports for all destinations\.

**Note**  
Your Amazon VPC configuration is discoverable through the [Amazon MSK API](https://docs.aws.amazon.com/msk/1.0/apireference/resources.html), and doesn't need to be configured in your `create-event-source-mapping` setup\.

### Adding an Amazon MSK cluster using the Lambda console<a name="services-msk-trigger"></a>

Follow these steps to add your Amazon MSK cluster and a Kafka topic as a trigger for your Lambda function\.

**To add an MSK trigger to your Lambda function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your Lambda function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Under **Trigger configuration**, choose the **MSK** trigger type\.

1. Configure the remaining options, and then choose **Add**\.

### Adding an Amazon MSK cluster using the AWS CLI<a name="services-msk-aws-cli"></a>

Use the following example AWS CLI commands to create and view an Amazon MSK trigger for your Lambda function\.

#### Creating a trigger using the AWS CLI<a name="services-msk-aws-cli-create"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\. The topic's starting position is set to `latest`\.

```
aws lambda create-event-source-mapping --event-source-arn arn:aws:kafka:us-west-2:arn:aws:kafka:us-west-2:111111111111:cluster/my-cluster/fc2f5bdf-fd1b-45ad-85dd-15b4a5a6247e-2 --topics AWSKafkaTopic --starting-position LATEST --function-name my-kafka-function
```

For more information, see the [CreateEventSourceMapping](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html) API reference documentation\.

#### Viewing the status using the AWS CLI<a name="services-msk-aws-cli-view"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping --uuid 6d9bce8e-836b-442c-8070-74e77903c815
```