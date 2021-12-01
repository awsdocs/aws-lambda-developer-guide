# Using Lambda with self\-managed Apache Kafka<a name="with-kafka"></a>

Lambda supports [Apache Kafka](https://kafka.apache.org/) as an [event source](invocation-eventsourcemapping.md)\. Apache Kafka is a an open\-source event streaming platform that supports workloads such as data pipelines and streaming analytics\.

You can use the AWS managed Kafka service Amazon Managed Streaming for Apache Kafka \(Amazon MSK\), or a self\-managed Kafka cluster\. For details about using Lambda with Amazon MSK, see [Using Lambda with Amazon MSK](with-msk.md)\.

This topic describes how to use Lambda with a self\-managed Kafka cluster\. In AWS terminology, a self\-managed cluster includes non\-AWS hosted Kafka clusters\. For example, you can host your Kafka cluster with a cloud provider such as [CloudKarafka](https://www.cloudkarafka.com/)\. You can also use other AWS hosting options for your cluster\. For more information, see [Best Practices for Running Apache Kafka on AWS](http://aws.amazon.com/blogs/big-data/best-practices-for-running-apache-kafka-on-aws/) on the AWS Big Data Blog\.

Apache Kafka as an event source operates similarly to using Amazon Simple Queue Service \(Amazon SQS\) or Amazon Kinesis\. Lambda internally polls for new messages from the event source and then synchronously invokes the target Lambda function\. Lambda reads the messages in batches and provides these to your function as an event payload\. The maximum batch size is configurable\. \(The default is 100 messages\.\)

For an example of how to use self\-managed Kafka as an event source, see [Using self\-hosted Apache Kafka as an event source for AWS Lambda](http://aws.amazon.com/blogs/compute/using-self-hosted-apache-kafka-as-an-event-source-for-aws-lambda/) on the AWS Compute Blog\.



Lambda sends the batch of messages in the event parameter when it invokes your Lambda function\. The event payload contains an array of messages\. Each array item contains details of the Kafka topic and Kafka partition identifier, together with a timestamp and a base64\-encoded message\.

```
{
   "eventSource":"aws:SelfManagedKafka",
   "bootstrapServers":"b-2.demo-cluster-1.a1bcde.c1.kafka.us-east-1.amazonaws.com:9092,b-1.demo-cluster-1.a1bcde.c1.kafka.us-east-1.amazonaws.com:9092",
   "records":{
      "mytopic-0":[
         {
            "topic":"mytopic",
            "partition":"0",
            "offset":15,
            "timestamp":1545084650987,
            "timestampType":"CREATE_TIME",
            "value":"SGVsbG8sIHRoaXMgaXMgYSB0ZXN0Lg==",
            "headers":[
               {
                  "headerKey":[
                     104,
                     101,
                     97,
                     100,
                     101,
                     114,
                     86,
                     97,
                     108,
                     117,
                     101
                  ]
               }
            ]
         }
      ]
   }
}
```

**Topics**
+ [Managing access and permissions](#smaa-permissions)
+ [Kafka authentication](#smaa-authentication)
+ [Network configuration](#services-msk-vpc-config)
+ [Adding a Kafka cluster as an event source](#services-smaa-topic-add)
+ [Using a Kafka cluster as an event source](#kafka-using-cluster)
+ [Auto scaling of the Kafka event source](#services-kafka-scaling)
+ [Event source API operations](#kafka-hosting-api-operations)
+ [Event source mapping errors](#services-event-errors)
+ [Self\-managed Apache Kafka configuration parameters](#services-kafka-parms)

## Managing access and permissions<a name="smaa-permissions"></a>

For Lambda to poll your Apache Kafka topic and update other cluster resources, your Lambda function—as well as your AWS Identity and Access Management \(IAM\) users and roles—must have the following permissions\.

### Required Lambda function permissions<a name="smaa-api-actions-required"></a>

To create and store logs to a log group in Amazon CloudWatch Logs, your Lambda function must have the following permissions in its [execution role](lambda-intro-execution-role.md):
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

### Optional Lambda function permissions<a name="smaa-api-actions-optional"></a>

Your Lambda function might need these permissions:
+ Describe your AWS Secrets Manager secret
+ Access your AWS Key Management Service \(AWS KMS\) customer managed key
+ Access your Amazon Virtual Private Cloud \(Amazon VPC\)

#### Secrets Manager and AWS KMS permissions<a name="smaa-api-actions-secret"></a>

If your Apache Kafka users access your Kafka brokers over the internet, you must specify a Secrets Manager secret\. Your Lambda function might need permission to describe your Secrets Manager secret or to decrypt your AWS KMS customer managed key\. To access these resources, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:
+ [secretsmanager:GetSecretValue](https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html)
+ [kms:Decrypt](https://docs.aws.amazon.com/kms/latest/APIReference/API_Decrypt.html)

#### VPC permissions<a name="smaa-api-actions-vpc"></a>

If only users within a VPC can access your self\-managed Apache Kafka cluster, your Lambda function must have permission to access your Amazon Virtual Private Cloud \(Amazon VPC\) resources\. These resources include your VPC, subnets, security groups, and network interfaces\. To access these resources, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)

### Adding permissions to your execution role<a name="smaa-permissions-add-policy"></a>

To access other AWS services that your self\-managed Apache Kafka cluster uses, Lambda uses the permissions policies that you define in your Lambda function's execution role\.

By default, Lambda is not permitted to perform the required or optional actions for a self\-managed Apache Kafka cluster\. You must create and define these actions in an IAM trust policy, and then attach the policy to your execution role\. For more information, see [AWS Lambda execution role](lambda-intro-execution-role.md)

### Adding users to an IAM policy<a name="smaa-permissions-add-users"></a>

By default, IAM users and roles do not have permission to perform [event source API operations](#kafka-hosting-api-operations)\. To grant access to users in your organization or account, you might need to create an identity\-based policy\. For more information, see [Controlling access to AWS resources using policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_controlling.html) in the *IAM User Guide*\.

## Kafka authentication<a name="smaa-authentication"></a>

Lambda supports several methods to authenticate with your self\-managed Apache Kafka cluster\. Make sure that you configure the Kafka cluster to use one of the following authentication methods that Lambda supports:
+ VPC

  If only Kafka users within your VPC access your Kafka brokers, you must configure the event source with VPC access\.
+ SASL/SCRAM

  Lambda supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication with TLS encryption\. Lambda sends the encrypted credentials to authenticate with the cluster\. Because the credentials are encrypted, the connection to the cluster does not need to be encrypted\. For more information about SASL/SCRAM authentication, see [RFC 5802](https://tools.ietf.org/html/rfc5802)\.
+ SASL/PLAIN

  Lambda supports SASL/PLAIN authentication with TLS encryption\. With SASL/PLAIN authentication, credentials are sent as clear text \(unencrypted\) to the server\. Because the credentials are clear text, the connection to the server must use TLS encryption\.

For SASL authentication, you must store the user name and password as a secret in Secrets Manager\. For more information, see [Tutorial: Creating and retrieving a secret](https://docs.aws.amazon.com/secretsmanager/latest/userguide/tutorials_basic.html) in the *AWS Secrets Manager User Guide*\.

## Network configuration<a name="services-msk-vpc-config"></a>

If you configure Amazon VPC access to your Kafka brokers, Lambda must have access to the Amazon VPC resources\. 

Lambda must have access to the Amazon Virtual Private Cloud \(Amazon VPC\) resources associated with your Kafka cluster\. We recommend that you deploy AWS PrivateLink [VPC endpoints](https://docs.aws.amazon.com/vpc/latest/privatelink/endpoint-services-overview.html) for Lambda and AWS Security Token Service \(AWS STS\)\. If authentication is required, also deploy a VPC endpoint for Secrets Manager\.

Alternatively, ensure that the VPC associated with your Kafka cluster includes one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

You must configure your Amazon VPC security groups with the following rules \(at minimum\):
+ Inbound rules – Allow all traffic on all ports for the security group specified as your event source\.
+ Outbound rules – Allow all traffic on all ports for all destinations\.

For more information about configuring the network, see [Setting up AWS Lambda with an Apache Kafka cluster within a VPC](http://aws.amazon.com/blogs/compute/setting-up-aws-lambda-with-an-apache-kafka-cluster-within-a-vpc/) on the AWS Compute Blog\.

## Adding a Kafka cluster as an event source<a name="services-smaa-topic-add"></a>

To create an [event source mapping](invocation-eventsourcemapping.md), add your Kafka cluster as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to create an event source mapping using the Lambda console and the AWS CLI\.

### Prerequisites<a name="services-smaa-prereqs"></a>
+ A self\-managed Apache Kafka cluster\. Lambda supports Apache Kafka version 0\.10\.0\.0 and later\.
+ A Lambda execution role with permission to access the AWS resources that your self\-managed Kafka cluster uses\.

### Adding a self\-managed Kafka cluster \(console\)<a name="services-smaa-trigger"></a>

Follow these steps to add your self\-managed Apache Kafka cluster and a Kafka topic as a trigger for your Lambda function\.

**To add an Apache Kafka trigger to your Lambda function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your Lambda function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Under **Trigger configuration**, do the following:

   1. Choose the **Apache Kafka** trigger type\.

   1. For **Bootstrap servers**, enter the host and port pair address of a Kafka broker in your cluster, and then choose **Add**\. Repeat for each Kafka broker in the cluster\.

   1. For **Topic name**, enter the name of the Kafka topic used to store records in the cluster\.

   1. \(Optional\) For **Batch size**, enter the maximum number of records to receive in a single batch\.

   1. \(Optional\) For **Starting position**, choose **Latest** to start reading the stream from the latest record\. Or, choose **Trim horizon** to start at the earliest available record\.

1. Under **Authentication method**, choose the access or authentication protocol of the Kafka brokers in your cluster\. If only users within your VPC access your Kafka brokers, you must configure VPC access\. If users access your Kafka brokers over the internet, you must configure SASL authentication\.
   + To configure VPC access, choose the **VPC** for your Kafka cluster, then choose **VPC subnets** and **VPC security groups**\.
   + To configure SASL authentication, under **Secret key**, choose **Add**, then do the following:

     1. Choose the key type\. If your Kafka broker uses SASL plaintext, choose **BASIC\_AUTH**\. Otherwise, choose one of the **SASL\_SCRAM** options\.

     1. Choose the name of the Secrets Manager secret key that contains the credentials for your Kafka cluster\.

1. To create the trigger, choose **Add**\.

### Adding a self\-managed Kafka cluster \(AWS CLI\)<a name="services-smak-aws-cli"></a>

Use the following example AWS CLI commands to create and view a self\-managed Apache Kafka trigger for your Lambda function\.

#### Using SASL/SCRAM<a name="services-smak-aws-cli-create"></a>

If Kafka users access your Kafka brokers over the internet, you must specify the Secrets Manager secret that you created for SASL/SCRAM authentication\. The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping --topics AWSKafkaTopic
          --source-access-configuration Type=SASL_SCRAM_512_AUTH,URI=arn:aws:secretsmanager:us-east-1:01234567890:secret:MyBrokerSecretName
          --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function
          --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092", "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) API reference documentation\.

#### Using a VPC<a name="services-smak-aws-cli-create-vpc"></a>

If only Kafka users within your VPC access your Kafka brokers, you must specify your VPC, subnets, and VPC security group\. The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping
          --topics AWSKafkaTopic
          --source-access-configuration '[{"Type": "VPC_SUBNET", "URI": "subnet:subnet-0011001100"},
          {"Type": "VPC_SUBNET", "URI": "subnet:subnet-0022002200"},
          {"Type": "VPC_SECURITY_GROUP", "URI": "security_group:sg-0123456789"}]'
          --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function
          --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092",
          "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) API reference documentation\.

#### Viewing the status using the AWS CLI<a name="services-smak-aws-cli-view"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping
          --uuid dh38738e-992b-343a-1077-3478934hjkfd7
```

## Using a Kafka cluster as an event source<a name="kafka-using-cluster"></a>

When you add your Apache Kafka cluster as a trigger for your Lambda function, the cluster is used as an [event source](invocation-eventsourcemapping.md)\.

Lambda reads event data from the Kafka topics that you specify in [CreateEventSourceMapping](API_CreateEventSourceMapping.md) `Topics` based on the starting position that you specify in [CreateEventSourceMapping](API_CreateEventSourceMapping.md) `StartingPosition`\. After successful processing, your Kafka topic is committed to your Kafka cluster\.

If you specify `LATEST` as the starting position, Lambda starts reading from the latest message in each partition belonging to the topic\. Because there can be some delay after trigger configuration before Lambda starts reading the messages, Lambda does not read any messages produced during this window\.

Lambda processes records from one or more Kafka topic partitions that you specify and sends a JSON payload to your Lambda function\. When more records are available, Lambda continues processing records in batches, based on the value that you specify in [CreateEventSourceMapping](API_CreateEventSourceMapping.md) >`BatchSize`, until the function catches up with the topic\.

If your Lambda function returns an error for any of the messages in a batch, Lambda retries the whole batch of messages until processing succeeds or the messages expire\.

The maximum amount of time that Lambda lets a function run before stopping it is 14 minutes\.

## Auto scaling of the Kafka event source<a name="services-kafka-scaling"></a>

When you initially create an Apache Kafka [event source](invocation-eventsourcemapping.md), Lambda allocates one consumer to process all of the partitions in the Kafka topic\. Lambda automatically scales up or down the number of consumers, based on workload\. To preserve message ordering in each partition, the maximum number of consumers is one consumer per partition in the topic\.

Every 15 minutes, Lambda evaluates the consumer offset lag of all the partitions in the topic\. If the lag is too high, the partition is receiving messages faster than Lambda can process them\. If necessary, Lambda adds or removes consumers from the topic\.

If your target Lambda function is overloaded, Lambda reduces the number of consumers\. This action reduces the workload on the function by reducing the number of messages that consumers can retrieve and send to the function\.

To monitor the throughput of your Kafka topic, you can view the Apache Kafka consumer metrics, such as `consumer_lag` and `consumer_offset`\. To check how many function invocations occur in parallel, you can also monitor the [concurrency metrics](monitoring-metrics.md#monitoring-metrics-concurrency) for your function\.

## Event source API operations<a name="kafka-hosting-api-operations"></a>

When you add your Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function using the Lambda console, an AWS SDK, or the AWS CLI, Lambda uses APIs to process your request\.

To manage an event source with the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+  [CreateEventSourceMapping](API_CreateEventSourceMapping.md) 
+  [ListEventSourceMappings](API_ListEventSourceMappings.md) 
+  [GetEventSourceMapping](API_GetEventSourceMapping.md) 
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) 
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md) 

## Event source mapping errors<a name="services-event-errors"></a>

When you add your Apache Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function, if your function encounters an error, your Kafka consumer stops processing records\. Consumers of a topic partition are those that subscribe to, read, and process your records\. Your other Kafka consumers can continue processing records, provided they don't encounter the same error\.

To determine the cause of a stopped consumer, check the `StateTransitionReason` field in the response of `EventSourceMapping`\. The following list describes the event source errors that you can receive:

**`ESM_CONFIG_NOT_VALID`**  
The event source mapping configuration is not valid\.

**`EVENT_SOURCE_AUTHN_ERROR`**  
Lambda could not authenticate the event source\.

**`EVENT_SOURCE_AUTHZ_ERROR`**  
Lambda does not have the required permissions to access the event source\.

**`FUNCTION_CONFIG_NOT_VALID`**  
The function configuration is not valid\.

**Note**  
If your Lambda event records exceed the allowed size limit of 6 MB, they can go unprocessed\.

## Self\-managed Apache Kafka configuration parameters<a name="services-kafka-parms"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Apache Kafka\.


**Event source parameters that apply to self\-managed Apache Kafka**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  BatchSize  |  N  |  100  |  Maximum: 10,000  | 
|  Enabled  |  N  |  Enabled  |     | 
|  FunctionName  |  Y  |     |     | 
|  SelfManagedEventSource   |  Y  |  |  List of Kafka Brokers\. Can set only on Create  | 
|  SourceAccessConfigurations  |  N  |  No credentials  |  VPC information or authentication credentials for the cluster   For SASL\_PLAIN, set to BASIC\_AUTH  | 
|  StartingPosition  |  Y  |     |  TRIM\_HORIZON or LATEST Can set only on Create  | 
|  Topics  |  Y  |     |  Topic name Can set only on Create  | 