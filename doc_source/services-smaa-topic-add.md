# Adding a self\-managed Apache Kafka cluster as an event source<a name="services-smaa-topic-add"></a>

You can use a Lambda function to process records from your Apache Kafka cluster when the cluster is configured as an [event](gettingstarted-concepts.md#gettingstarted-concepts-event) source\. To create an [event source mapping](invocation-eventsourcemapping.md), you can add your Kafka cluster as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to add your Kafka cluster and topic as a function trigger using the Lambda console or AWS CLI\.

## Prerequisites<a name="services-smaa-prereqs"></a>
+ A non\-AWS hosted Apache Kafka cluster, or an AWS hosted Apache Kafka cluster on another AWS service\. For more information, see [Hosting an Apache Kafka cluster](kafka-hosting.md)\.
+ A [Lambda execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your self\-managed Kafka cluster uses\. For more information, see [Managing access and permissions for a self\-managed Apache Kafka cluster](smaa-permissions.md)\.

## Adding a self\-managed Apache Kafka cluster using the Lambda console<a name="services-smaa-trigger"></a>

Follow these steps to add your self\-managed Apache Kafka cluster and a Kafka topic as a trigger for your Lambda function\.

**To add an Apache Kafka trigger to your Lambda function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your Lambda function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Under **Trigger configuration**, choose the **Apache Kafka** trigger type\.

1. Configure the remaining options, and then choose **Add**\.

## Adding a self\-managed Apache Kafka cluster using the AWS CLI<a name="services-smak-aws-cli"></a>

Use the following example AWS CLI commands to create and view a self\-managed Apache Kafka trigger for your Lambda function\.

### Using SASL/SCRAM<a name="services-smak-aws-cli-create"></a>

If Kafka users access your Kafka brokers over the internet, you must specify your AWS Secrets Manager secret that you created for SASL/SCRAM authentication\. The following example uses the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping --topics AWSKafkaTopic --source-access-configuration Type=SASL_SCRAM_512_AUTH,URI=arn:aws:secretsmanager:us-east-1:01234567890:secret:MyBrokerSecretName --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092", "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html) API reference documentation\.

### Using a VPC<a name="services-smak-aws-cli-create-vpc"></a>

If only Kafka users within your virtual private cloud \(VPC\) access your Kafka brokers, you must specify your VPC, subnets, and VPC security group\. The following example uses the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping --topics AWSKafkaTopic --source-access-configuration '[{"Type": "VPC_SUBNET", "URI": "subnet:subnet-0011001100"},{"Type": "VPC_SUBNET", "URI": "subnet:subnet-0022002200"},{"Type": "VPC_SECURITY_GROUP", "URI": "security_group:sg-0123456789"}]' --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092", "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html) API reference documentation\.

### Viewing the status<a name="services-smak-aws-cli-view"></a>

The following example uses the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping --uuid dh38738e-992b-343a-1077-3478934hjkfd7
```