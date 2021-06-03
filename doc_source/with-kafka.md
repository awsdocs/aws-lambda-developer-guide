# Using Lambda with an Apache Kafka cluster<a name="with-kafka"></a>

[Apache Kafka](https://kafka.apache.org/) is a distributed data store optimized for ingesting and processing streaming data in real time\.

Kafka is primarily used to build streaming data pipelines and applications that adapt to the data streams\. It combines messaging, storage, and stream processing to allow storage and analysis of both historical and real\-time data\.

AWS provides a managed Kafka service, or you can use a non\-AWS Kafka cluster with Lambda\.

**Topics**
+ [Hosting an Apache Kafka cluster](#kafka-hosting)
+ [Using an Apache Kafka cluster as an event source for Lambda](#kafka-using-cluster)
+ [Using Lambda with self\-managed Apache Kafka](#kafka-smaa)

## Hosting an Apache Kafka cluster<a name="kafka-hosting"></a>

You can host an Apache Kafka cluster on AWS, or on any other cloud provider of your choice\. Lambda supports Kafka as an [event source](invocation-eventsourcemapping.md) regardless of where it is hosted, as long as Lambda can access the cluster\.

### Using Amazon MSK<a name="kafka-hosting-msk-using"></a>

To host your Apache Kafka cluster and topics, you can use [Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](https://docs.aws.amazon.com/msk/latest/developerguide/what-is-msk.html)\.

For more information about using an MSK cluster, see [Using Lambda with Amazon MSK](with-msk.md)\.

### Using a self\-managed Kafka provider<a name="kafka-hosting-cloud-using"></a>

To host your Apache Kafka cluster and topics, you can use any non\-AWS cloud provider, such as [CloudKarafka](https://www.cloudkarafka.com/)\.

You can also use other AWS hosting options for your Apache Kafka cluster and topics\. For more information, see [Best Practices for Running Apache Kafka on AWS](http://aws.amazon.com/blogs/big-data/best-practices-for-running-apache-kafka-on-aws/) on the AWS Big Data Blog\.

For more information about using a self\-managed Kafka cluster, see [Using Lambda with self\-managed Apache Kafka](#kafka-smaa)\.

## Using an Apache Kafka cluster as an event source for Lambda<a name="kafka-using-cluster"></a>



You can host an Apache Kafka cluster on AWS, or on any other cloud provider of your choice\. Lambda supports Kafka as an [event source](invocation-eventsourcemapping.md) regardless of where it is hosted, as long as Lambda can access the cluster\.

### Prerequisites<a name="kafka-hosting-prereqs"></a>
+ A [Lambda function](getting-started-create-function.md) with function code in a [supported runtime](lambda-runtimes.md) to invoke your cluster
+ A [Lambda execution role](lambda-intro-execution-role.md)

### How it works<a name="kafka-hosting-how-it-works"></a>



When you add your Apache Kafka cluster as a trigger for your Lambda function, the cluster is used as an [event source](invocation-eventsourcemapping.md)\. When you add your Kafka cluster and topic as an event source, Lambda creates a consumer group with an event source `UUID`\.
+ If you use an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster as your event source in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-EventSourceArn](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-EventSourceArn), Lambda reads event data using the Amazon MSK cluster and the Kafka topic that you specify\.
+ If you use a non\-AWS hosted Apache Kafka cluster—or an AWS hosted Apache Kafka cluster on another AWS service—as your event source in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SelfManagedEventSource](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SelfManagedEventSource), Lambda reads event data using the Kafka host, topic, and connection details that you specify\.
+ Lambda reads event data from the Kafka topics that you specify in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-Topics](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-Topics) based on the starting position that you specify in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-StartingPosition](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-StartingPosition)\. After successful processing, your Kafka topic is committed to your Kafka cluster\.
+ Lambda processes records from one or more Kafka topic partitions that you specify and sends a JSON payload to your Lambda function\. When more records are available, Lambda continues processing records in batches, based on the value that you specify in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize), until the function catches up with the topic\.
+ Lambda supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication for your Kafka brokers\. Lambda uses the SASL/SCRAM user name and password that you specify in your AWS Secrets Manager secret in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SourceAccessConfigurations](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SourceAccessConfigurations)\.
+ 

For Amazon MSK and self\-managed Apache Kafka, the maximum amount of time that Lambda allows a function to run before stopping it is 14 minutes\.

### Event source API operations<a name="kafka-hosting-api-operations"></a>

When you add your Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function using the Lambda console, an AWS SDK, or the AWS Command Line Interface \(AWS CLI\), Lambda uses APIs to process your request\.

To manage an event source with the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

### Event source mapping errors<a name="services-event-errors"></a>

When you add your Apache Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function, if your function encounters an error, your Kafka consumer stops processing records\. Consumers of a topic partition are those that subscribe to, read, and process your records\. Your other Kafka consumers can continue processing records, provided they don't encounter the same error\.

To determine the cause of a stopped consumer, check the `StateTransitionReason` field in the response of `EventSourceMapping`\. The following list describes the event source errors that you can receive:

**`ESM_CONFIG_NOT_VALID`**  
The event source mapping configuration is not valid\.

**`EVENT_SOURCE_AUTHN_ERROR`**  
Lambda couldn't authenticate the event source\.

**`EVENT_SOURCE_AUTHZ_ERROR`**  
Lambda doesn't have the required permissions to access the event source\.

**`FUNCTION_CONFIG_NOT_VALID`**  
The Lambda function configuration is not valid\.

**Note**  
If your Lambda event records exceed the allowed size limit of 6 MB, they can go unprocessed\.

## Using Lambda with self\-managed Apache Kafka<a name="kafka-smaa"></a>

You can onboard a non\-AWS hosted Apache Kafka cluster—or an AWS hosted Apache Kafka cluster on another AWS service—as an [event source](invocation-eventsourcemapping.md) for a Lambda function\. This enables you to trigger your functions in response to records sent to your Kafka cluster\.

**Topics**
+ [Managing access and permissions for a self\-managed Apache Kafka cluster](#smaa-permissions)
+ [Adding a self\-managed Apache Kafka cluster as an event source](#services-smaa-topic-add)

### Managing access and permissions for a self\-managed Apache Kafka cluster<a name="smaa-permissions"></a>

Lambda polls your Apache Kafka topic partitions for new records and invokes your Lambda function [synchronously](invocation-sync.md)\. To update other AWS resources that your cluster uses, your Lambda function—as well as your AWS Identity and Access Management \(IAM\) users and roles—must have permission to perform these actions\.

This page describes how to grant permission to Lambda and other users of your self\-managed Kafka cluster\.

#### Required Lambda function permissions<a name="smaa-api-actions-required"></a>

To create and store logs to a log group in Amazon CloudWatch Logs, your Lambda function must have the following permissions in its [execution role](lambda-intro-execution-role.md):
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

#### Optional Lambda function permissions<a name="smaa-api-actions-optional"></a>

Your Lambda function might need permission to describe your AWS Secrets Manager secret or your AWS Key Management Service \(AWS KMS\) [customer managed CMK](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#customer-cmk), or to access your virtual private cloud \(VPC\)\.

##### Secrets Manager and AWS KMS permissions<a name="smaa-api-actions-vpc"></a>

If your Kafka users access your Apache Kafka brokers over the internet, you must specify a Secrets Manager secret\. For more information, see [Using SASL/SCRAM authentication](#smaa-permissions-add-secret)\.

Your Lambda function might need permission to describe your Secrets Manager secret or decrypt your AWS KMS customer managed CMK\. To access these resources, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:
+ [secretsmanager:GetSecretValue](https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html)
+ [kms:Decrypt](https://docs.aws.amazon.com/kms/latest/APIReference/API_Decrypt.html)

##### VPC permissions<a name="smaa-api-actions-vpc"></a>

If only users within your VPC access your self\-managed Apache Kafka cluster, your Lambda function needs permission to access your Amazon Virtual Private Cloud \(Amazon VPC\) resources, including your VPC, subnets, security groups, and network interfaces\. To access these resources, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)

#### Adding permissions to your execution role<a name="smaa-permissions-add-policy"></a>

To access other AWS services that your self\-managed Apache Kafka cluster uses, Lambda uses the permission policies that you define in your function's [execution role](lambda-intro-execution-role.md)\.

By default, Lambda isn't permitted to perform the required or optional actions for a self\-managed Apache Kafka cluster\. You must create and define these actions in an IAM trust policy, and then attach the policy to your execution role\. This example shows how you might create a policy that allows Lambda to access your Amazon VPC resources\.

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

#### Adding users to an IAM policy<a name="smaa-permissions-add-users"></a>

By default, IAM users and roles don't have permission to perform [event source API operations](#kafka-hosting-api-operations)\. To grant access to users in your organization or account, you might need to create an identity\-based policy\. For more information, see [Controlling access to AWS resources using policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_controlling.html) in the *IAM User Guide*\.

#### Using SASL/SCRAM authentication<a name="smaa-permissions-add-secret"></a>

**Important**  
Plaintext brokers are not supported if you are is using SASL/SCRAM based authentication\. You must use TLS encryption for your brokers\.

User name and password authentication for a self\-managed Apache Kafka cluster uses Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\)\. SCRAM uses secured hashing algorithms and doesn't transmit plaintext passwords between the client and server\. For more information about SASL/SCRAM authentication, see [RFC 5802](https://tools.ietf.org/html/rfc5802)\.

To set up user name and password authentication for your self\-managed Kafka cluster, create a secret in AWS Secrets Manager\. Your non\-AWS cloud provider must provide your user name and password in SASL/SCRAM format\. For example:

```
{
        "username": "ab1c23de",
        "password": "qxbbaLRG7JXYN4NpNMVccP4gY9WZyDbp"
}
```

For more information, see [Tutorial: Creating and retrieving a secret](https://docs.aws.amazon.com/secretsmanager/latest/userguide/tutorials_basic.html) in the *AWS Secrets Manager User Guide*\.

### Adding a self\-managed Apache Kafka cluster as an event source<a name="services-smaa-topic-add"></a>

You can use a Lambda function to process records from your Apache Kafka cluster when the cluster is configured as an [event](gettingstarted-concepts.md#gettingstarted-concepts-event) source\. To create an [event source mapping](invocation-eventsourcemapping.md), you can add your Kafka cluster as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to add your Kafka cluster and topic as a function trigger using the Lambda console or AWS CLI\.

#### Prerequisites<a name="services-smaa-prereqs"></a>
+ A non\-AWS hosted Apache Kafka cluster, or an AWS hosted Apache Kafka cluster on another AWS service\. For more information, see [Hosting an Apache Kafka cluster](#kafka-hosting)\.
+ A [Lambda execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your self\-managed Kafka cluster uses\. For more information, see [Managing access and permissions for a self\-managed Apache Kafka cluster](#smaa-permissions)\.

#### Adding a self\-managed Apache Kafka cluster using the Lambda console<a name="services-smaa-trigger"></a>

Follow these steps to add your self\-managed Apache Kafka cluster and a Kafka topic as a trigger for your Lambda function\.

**To add an Apache Kafka trigger to your Lambda function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your Lambda function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Under **Trigger configuration**, choose the **Apache Kafka** trigger type\.

1. Configure the remaining options, and then choose **Add**\.

#### Adding a self\-managed Apache Kafka cluster using the AWS CLI<a name="services-smak-aws-cli"></a>

Use the following example AWS CLI commands to create and view a self\-managed Apache Kafka trigger for your Lambda function\.

##### Using SASL/SCRAM<a name="services-smak-aws-cli-create"></a>

If Kafka users access your Kafka brokers over the internet, you must specify your AWS Secrets Manager secret that you created for SASL/SCRAM authentication\. The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping --topics AWSKafkaTopic --source-access-configuration Type=SASL_SCRAM_512_AUTH,URI=arn:aws:secretsmanager:us-east-1:01234567890:secret:MyBrokerSecretName --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092", "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html) API reference documentation\.

##### Using a VPC<a name="services-smak-aws-cli-create-vpc"></a>

If only Kafka users within your virtual private cloud \(VPC\) access your Kafka brokers, you must specify your VPC, subnets, and VPC security group\. The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping --topics AWSKafkaTopic --source-access-configuration '[{"Type": "VPC_SUBNET", "URI": "subnet:subnet-0011001100"},{"Type": "VPC_SUBNET", "URI": "subnet:subnet-0022002200"},{"Type": "VPC_SECURITY_GROUP", "URI": "security_group:sg-0123456789"}]' --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092", "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html) API reference documentation\.

##### Viewing the status<a name="services-smak-aws-cli-view"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping --uuid dh38738e-992b-343a-1077-3478934hjkfd7
```