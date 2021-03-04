# Adding an Amazon MSK cluster as an event source<a name="services-msk-topic-add"></a>

You can use a Lambda function to process records from your Apache Kafka cluster when the cluster is configured as an [event](gettingstarted-concepts.md#gettingstarted-concepts-event) source\. To create an [event source mapping](invocation-eventsourcemapping.md), you can add your Kafka cluster as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to add your Kafka cluster and topic as a function trigger using the Lambda console or AWS CLI\.

## Prerequisites<a name="services-msk-prereqs"></a>
+ An Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster and a Kafka topic\. For more information, see [Getting Started Using Amazon MSK](https://docs.aws.amazon.com/msk/latest/developerguide/getting-started.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.
+ A [Lambda execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your MSK cluster uses\. For more information, see [Managing access and permissions for an Amazon MSK cluster](msk-permissions.md)\.

## VPC configuration<a name="services-msk-vpc-config"></a>

To get Apache Kafka records from Amazon MSK brokers, Lambda must have access to the Amazon Virtual Private Cloud \(Amazon VPC\) resources associated with your MSK cluster\. To meet Amazon VPC access requirements, we recommend:
+ Configuring one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

Your Amazon VPC security groups must be configured with the following rules \(at minimum\):
+ Inbound rules – Allow all traffic on all ports for the security group specified as your event source\.
+ Outbound rules – Allow all traffic on all ports for all destinations\.

**Note**  
Your Amazon VPC configuration is discoverable through the [Amazon MSK API](https://docs.aws.amazon.com/msk/1.0/apireference/resources.html), and doesn't need to be configured in your `create-event-source-mapping` setup\.

## Adding an Amazon MSK cluster using the Lambda console<a name="services-msk-trigger"></a>

Follow these steps to add your Amazon MSK cluster and a Kafka topic as a trigger for your Lambda function\.

**To add an MSK trigger to your Lambda function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your Lambda function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Under **Trigger configuration**, choose the **MSK** trigger type\.

1. Configure the remaining options, and then choose **Add**\.

## Adding an Amazon MSK cluster using the AWS CLI<a name="services-msk-aws-cli"></a>

Use the following example AWS CLI commands to create and view an Amazon MSK trigger for your Lambda function\.

### Creating a trigger using the AWS CLI<a name="services-msk-aws-cli-create"></a>

The following example uses the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\. The topic's starting position is set to `latest`\.

```
aws lambda create-event-source-mapping --event-source-arn arn:aws:kafka:us-west-2:arn:aws:kafka:us-west-2:111111111111:cluster/my-cluster/fc2f5bdf-fd1b-45ad-85dd-15b4a5a6247e-2 --topics AWSKafkaTopic --starting-position LATEST --function-name my-kafka-function
```

For more information, see the [CreateEventSourceMapping](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html) API reference documentation\.

### Viewing the status using the AWS CLI<a name="services-msk-aws-cli-view"></a>

The following example uses the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping --uuid 6d9bce8e-836b-442c-8070-74e77903c815
```