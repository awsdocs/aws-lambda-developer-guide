# Using Lambda with self\-managed Apache Kafka<a name="with-kafka"></a>

Lambda supports [Apache Kafka](https://kafka.apache.org/) as an [event source](invocation-eventsourcemapping.md)\. Apache Kafka is a an open\-source event streaming platform that supports workloads such as data pipelines and streaming analytics\.

You can use the AWS managed Kafka service Amazon Managed Streaming for Apache Kafka \(Amazon MSK\), or a self\-managed Kafka cluster\. For details about using Lambda with Amazon MSK, see [Using Lambda with Amazon MSK](with-msk.md)\.

This topic describes how to use Lambda with a self\-managed Kafka cluster\. In AWS terminology, a self\-managed cluster includes non\-AWS hosted Kafka clusters\. For example, you can host your Kafka cluster with a cloud provider such as [CloudKarafka](https://www.cloudkarafka.com/)\. You can also use other AWS hosting options for your cluster\. For more information, see [Best Practices for Running Apache Kafka on AWS](http://aws.amazon.com/blogs/big-data/best-practices-for-running-apache-kafka-on-aws/) on the AWS Big Data Blog\.

Apache Kafka as an event source operates similarly to using Amazon Simple Queue Service \(Amazon SQS\) or Amazon Kinesis\. Lambda internally polls for new messages from the event source and then synchronously invokes the target Lambda function\. Lambda reads the messages in batches and provides these to your function as an event payload\. The maximum batch size is configurable\. \(The default is 100 messages\.\)

For Kafka\-based event sources, Lambda supports processing control parameters, such as batching windows and batch size\. For more information, see [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)\.

For an example of how to use self\-managed Kafka as an event source, see [Using self\-hosted Apache Kafka as an event source for AWS Lambda](http://aws.amazon.com/blogs/compute/using-self-hosted-apache-kafka-as-an-event-source-for-aws-lambda/) on the AWS Compute Blog\.

Lambda sends the batch of messages in the event parameter when it invokes your Lambda function\. The event payload contains an array of messages\. Each array item contains details of the Kafka topic and Kafka partition identifier, together with a timestamp and a base64\-encoded message\.

```
{
   "eventSource": "SelfManagedKafka",
   "bootstrapServers":"b-2.demo-cluster-1.a1bcde.c1.kafka.us-east-1.amazonaws.com:9092,b-1.demo-cluster-1.a1bcde.c1.kafka.us-east-1.amazonaws.com:9092",
   "records":{
      "mytopic-0":[
         {
            "topic":"mytopic",
            "partition":0,
            "offset":15,
            "timestamp":1545084650987,
            "timestampType":"CREATE_TIME",
            "key":"abcDEFghiJKLmnoPQRstuVWXyz1234==",
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
+ [Kafka cluster authentication](#smaa-authentication)
+ [Managing API access and permissions](#smaa-permissions)
+ [Authentication and authorization errors](#kafka-permissions-errors)
+ [Network configuration](#services-kafka-vpc-config)
+ [Adding a Kafka cluster as an event source](#services-smaa-topic-add)
+ [Using a Kafka cluster as an event source](#kafka-using-cluster)
+ [Auto scaling of the Kafka event source](#services-kafka-scaling)
+ [Event source API operations](#kafka-hosting-api-operations)
+ [Event source mapping errors](#services-event-errors)
+ [Amazon CloudWatch metrics](#services-kafka-metrics)
+ [Self\-managed Apache Kafka configuration parameters](#services-kafka-parms)

## Kafka cluster authentication<a name="smaa-authentication"></a>

Lambda supports several methods to authenticate with your self\-managed Apache Kafka cluster\. Make sure that you configure the Kafka cluster to use one of these supported authentication methods\. For more information about Kafka security, see the [Security](http://kafka.apache.org/documentation.html#security) section of the Kafka documentation\.

### VPC access<a name="smaa-auth-vpc"></a>

If only Kafka users within your VPC access your Kafka brokers, you must configure the Kafka event source for Amazon Virtual Private Cloud \(Amazon VPC\) access\.

### SASL/SCRAM authentication<a name="smaa-auth-sasl"></a>

Lambda supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication with Transport Layer Security \(TLS\) encryption\. Lambda sends the encrypted credentials to authenticate with the cluster\. For more information about SASL/SCRAM authentication, see [RFC 5802](https://tools.ietf.org/html/rfc5802)\.

Lambda supports SASL/PLAIN authentication with TLS encryption\. With SASL/PLAIN authentication, Lambda sends credentials as clear text \(unencrypted\) to the server\.

For SASL authentication, you store the user name and password as a secret in AWS Secrets Manager\. For more information about using Secrets Manager, see [Tutorial: Create and retrieve a secret](https://docs.aws.amazon.com/secretsmanager/latest/userguide/tutorials_basic.html) in the *AWS Secrets Manager User Guide*\.

### Mutual TLS authentication<a name="smaa-auth-mtls"></a>

Mutual TLS \(mTLS\) provides two\-way authentication between the client and server\. The client sends a certificate to the server for the server to verify the client, and the server sends a certificate to the client for the client to verify the server\. 

In self\-managed Apache Kafka, Lambda acts as the client\. You configure a client certificate \(as a secret in Secrets Manager\) to authenticate Lambda with your Kafka brokers\. The client certificate must be signed by a CA in the server's trust store\.

The Kafka cluster sends a server certificate to Lambda to authenticate the Kafka brokers with Lambda\. The server certificate can be a public CA certificate or a private CA/self\-signed certificate\. The public CA certificate must be signed by a certificate authority \(CA\) that's in the Lambda trust store\. For a private CA/self\-signed certificate, you configure the server root CA certificate \(as a secret in Secrets Manager\)\. Lambda uses the root certificate to verify the Kafka brokers\.

For more information about mTLS, see [ Introducing mutual TLS authentication for Amazon MSK as an event source](http://aws.amazon.com/blogs/compute/introducing-mutual-tls-authentication-for-amazon-msk-as-an-event-source)\.

### Configuring the client certificate secret<a name="smaa-auth-secret"></a>

The CLIENT\_CERTIFICATE\_TLS\_AUTH secret requires a certificate field and a private key field\. For an encrypted private key, the secret requires a private key password\. Both the certificate and private key must be in PEM format\.

**Note**  
Lambda supports the [PBES1](https://datatracker.ietf.org/doc/html/rfc2898/#section-6.1) \(but not PBES2\) private key encryption algorithms\.

The certificate field must contain a list of certificates, beginning with the client certificate, followed by any intermediate certificates, and ending with the root certificate\. Each certificate must start on a new line with the following structure:

```
-----BEGIN CERTIFICATE-----  
        <certificate contents>
-----END CERTIFICATE-----
```

Secrets Manager supports secrets up to 65,536 bytes, which is enough space for long certificate chains\.

The private key must be in [PKCS \#8](https://datatracker.ietf.org/doc/html/rfc5208) format, with the following structure:

```
-----BEGIN PRIVATE KEY-----  
         <private key contents>
-----END PRIVATE KEY-----
```

For an encrypted private key, use the following structure:

```
-----BEGIN ENCRYPTED PRIVATE KEY-----  
          <private key contents>
-----END ENCRYPTED PRIVATE KEY-----
```

The following example shows the contents of a secret for mTLS authentication using an encrypted private key\. For an encrypted private key, include the private key password in the secret\.

```
{
 "privateKeyPassword": "testpassword",
 "certificate": "-----BEGIN CERTIFICATE-----
MIIE5DCCAsygAwIBAgIRAPJdwaFaNRrytHBto0j5BA0wDQYJKoZIhvcNAQELBQAw
...
j0Lh4/+1HfgyE2KlmII36dg4IMzNjAFEBZiCRoPimO40s1cRqtFHXoal0QQbIlxk
cmUuiAii9R0=
-----END CERTIFICATE-----
-----BEGIN CERTIFICATE-----
MIIFgjCCA2qgAwIBAgIQdjNZd6uFf9hbNC5RdfmHrzANBgkqhkiG9w0BAQsFADBb
...
rQoiowbbk5wXCheYSANQIfTZ6weQTgiCHCCbuuMKNVS95FkXm0vqVD/YpXKwA/no
c8PH3PSoAaRwMMgOSA2ALJvbRz8mpg==
-----END CERTIFICATE-----",
 "privateKey": "-----BEGIN ENCRYPTED PRIVATE KEY-----
MIIFKzBVBgkqhkiG9w0BBQ0wSDAnBgkqhkiG9w0BBQwwGgQUiAFcK5hT/X7Kjmgp
...
QrSekqF+kWzmB6nAfSzgO9IaoAaytLvNgGTckWeUkWn/V0Ck+LdGUXzAC4RxZnoQ
zp2mwJn2NYB7AZ7+imp0azDZb+8YG2aUCiyqb6PnnA==
-----END ENCRYPTED PRIVATE KEY-----"
}
```

### Configuring the server root CA certificate secret<a name="smaa-auth-ca-cert"></a>

You create this secret if your Kafka brokers use TLS encryption with certificates signed by a private CA\. You can use TLS encryption for VPC, SASL/SCRAM, SASL/PLAIN, or mTLS authentication\.

The server root CA certificate secret requires a field that contains the Kafka broker's root CA certificate in PEM format\. The following example shows the structure of the secret\.

```
{
     "certificate": "-----BEGIN CERTIFICATE-----       
  MIID7zCCAtegAwIBAgIBADANBgkqhkiG9w0BAQsFADCBmDELMAkGA1UEBhMCVVMx
  EDAOBgNVBAgTB0FyaXpvbmExEzARBgNVBAcTClNjb3R0c2RhbGUxJTAjBgNVBAoT
  HFN0YXJmaWVsZCBUZWNobm9sb2dpZXMsIEluYy4xOzA5BgNVBAMTMlN0YXJmaWVs
  ZCBTZXJ2aWNlcyBSb290IENlcnRpZmljYXRlIEF1dG...
  -----END CERTIFICATE-----"
```

## Managing API access and permissions<a name="smaa-permissions"></a>

In addition to accessing your self\-managed Kafka cluster, your Lambda function needs permissions to perform various API actions\. You add these permissions to the function's [execution role](lambda-intro-execution-role.md)\. If your users need access to any API actions, add the required permissions to the identity policy for the AWS Identity and Access Management \(IAM\) user or role\.

### Required Lambda function permissions<a name="smaa-api-actions-required"></a>

To create and store logs in a log group in Amazon CloudWatch Logs, your Lambda function must have the following permissions in its execution role:
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

### Optional Lambda function permissions<a name="smaa-api-actions-optional"></a>

Your Lambda function might also need permissions to:
+ Describe your Secrets Manager secret\.
+ Access your AWS Key Management Service \(AWS KMS\) customer managed key\.
+ Access your Amazon VPC\.

#### Secrets Manager and AWS KMS permissions<a name="smaa-api-actions-vpc"></a>

Depending on the type of access control that you're configuring for your Kafka brokers, your Lambda function might need permission to access your Secrets Manager secret or to decrypt your AWS KMS customer managed key\. To access these resources, your function's execution role must have the following permissions:
+ [secretsmanager:GetSecretValue](https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html)
+ [kms:Decrypt](https://docs.aws.amazon.com/kms/latest/APIReference/API_Decrypt.html)

#### VPC permissions<a name="smaa-api-actions-vpc"></a>

If only users within a VPC can access your self\-managed Apache Kafka cluster, your Lambda function must have permission to access your Amazon VPC resources\. These resources include your VPC, subnets, security groups, and network interfaces\. To access these resources, your function's execution role must have the following permissions:
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)

### Adding permissions to your execution role<a name="smaa-permissions-add-policy"></a>

To access other AWS services that your self\-managed Apache Kafka cluster uses, Lambda uses the permissions policies that you define in your Lambda function's [execution role](lambda-intro-execution-role.md)\.

By default, Lambda is not permitted to perform the required or optional actions for a self\-managed Apache Kafka cluster\. You must create and define these actions in an [IAM trust policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html#term_trust-policy), and then attach the policy to your execution role\. This example shows how you might create a policy that allows Lambda to access your Amazon VPC resources\.

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
              "Resource":"*"
           }
        ]
     }
```

For information about creating a JSON policy document in the IAM console, see [Creating policies on the JSON tab](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_create-console.html#access_policies_create-json-editor) in the *IAM User Guide*\.

### Granting users access with an IAM policy<a name="smaa-permissions-add-users"></a>

By default, IAM users and roles don't have permission to perform [event source API operations](#kafka-hosting-api-operations)\. To grant access to users in your organization or account, you create or update an identity\-based policy\. For more information, see [Controlling access to AWS resources using policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_controlling.html) in the *IAM User Guide*\.

## Authentication and authorization errors<a name="kafka-permissions-errors"></a>

If any of the permissions required to consume data from the Kafka cluster are missing, Lambda displays one of the following error messages in the event source mapping under **LastProcessingResult**\.

**Topics**
+ [Cluster failed to authorize Lambda](#kafka-authorize-errors)
+ [SASL authentication failed](#kafka-sasl-errors)
+ [Server failed to authenticate Lambda](#kafka-mtls-errors-server)
+ [Lambda failed to authenticate server](#kafka-mtls-errors-lambda)
+ [Provided certificate or private key is invalid](#kafka-key-errors)

### Cluster failed to authorize Lambda<a name="kafka-authorize-errors"></a>

For SASL/SCRAM or mTLS, this error indicates that the provided user doesn't have all of the following required Kafka access control list \(ACL\) permissions:
+ DescribeConfigs Cluster
+ Describe Group
+ Read Group
+ Describe Topic
+ Read Topic

When you create Kafka ACLs with the required `kafka-cluster` permissions, specify the topic and group as resources\. The topic name must match the topic in the event source mapping\. The group name must match the event source mapping's UUID\.

After you add the required permissions to the execution role, it might take several minutes for the changes to take effect\.

### SASL authentication failed<a name="kafka-sasl-errors"></a>

For SASL/SCRAM or SASL/PLAIN, this error indicates that the provided user name and password aren't valid\.

### Server failed to authenticate Lambda<a name="kafka-mtls-errors-server"></a>

This error indicates that the Kafka broker failed to authenticate Lambda\. This can occur for any of the following reasons:
+ You didn't provide a client certificate for mTLS authentication\.
+ You provided a client certificate, but the Kafka brokers aren't configured to use mTLS authentication\.
+ A client certificate isn't trusted by the Kafka brokers\.

### Lambda failed to authenticate server<a name="kafka-mtls-errors-lambda"></a>

This error indicates that Lambda failed to authenticate the Kafka broker\. This can occur for any of the following reasons:
+ The Kafka brokers use self\-signed certificates or a private CA, but didn't provide the server root CA certificate\.
+ The server root CA certificate doesn't match the root CA that signed the broker's certificate\.
+ Hostname validation failed because the broker's certificate doesn't contain the broker's DNS name or IP address as a subject alternative name\.

### Provided certificate or private key is invalid<a name="kafka-key-errors"></a>

This error indicates that the Kafka consumer couldn't use the provided certificate or private key\. Make sure that the certificate and key use PEM format, and that the private key encryption uses a PBES1 algorithm\.

## Network configuration<a name="services-kafka-vpc-config"></a>

If you configure Amazon VPC access to your Kafka brokers, Lambda must have access to the Amazon VPC resources associated with your Kafka cluster\. We recommend that you deploy AWS PrivateLink [VPC endpoints](https://docs.aws.amazon.com/vpc/latest/privatelink/endpoint-services-overview.html) for Lambda and AWS Security Token Service \(AWS STS\)\. If the broker uses authentication, also deploy a VPC endpoint for Secrets Manager\.

Alternatively, ensure that the VPC associated with your Kafka cluster includes one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

Configure your Amazon VPC security groups with the following rules \(at minimum\):
+ Inbound rules – Allow all traffic on the Kafka broker port for the security groups specified for your event source\. Kafka uses port 9092 by default\.
+ Outbound rules – Allow all traffic on port 443 for all destinations\. Allow all traffic on the Kafka broker port for the security groups specified for your event source\. Kafka uses port 9092 by default\.
+ If you are using VPC endpoints instead of a NAT gateway, the security groups associated with the VPC endpoints must allow all inbound traffic on port 443 from the event source's security groups\.

For more information about configuring the network, see [Setting up AWS Lambda with an Apache Kafka cluster within a VPC](http://aws.amazon.com/blogs/compute/setting-up-aws-lambda-with-an-apache-kafka-cluster-within-a-vpc/) on the AWS Compute Blog\.

## Adding a Kafka cluster as an event source<a name="services-smaa-topic-add"></a>

To create an [event source mapping](invocation-eventsourcemapping.md), add your Kafka cluster as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to create an event source mapping using the Lambda console and the AWS CLI\.

### Prerequisites<a name="services-smaa-prereqs"></a>
+ A self\-managed Apache Kafka cluster\. Lambda supports Apache Kafka version 0\.10\.0\.0 and later\.
+ An [execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your self\-managed Kafka cluster uses\.

### Customizable consumer group ID<a name="services-smaa-consumer-group-id"></a>

When setting up Kafka as an event source, you can specify a consumer group ID\. This consumer group ID is an existing identifier for the Kafka consumer group that you want your Lambda function to join\. You can use this feature to seamlessly migrate any ongoing Kafka record processing setups from other consumers to Lambda\.

If you specify a consumer group ID and there are other active pollers within that consumer group, Kafka distributes messages across all consumers\. In other words, Lambda doesn't receive all message for the Kafka topic\. If you want Lambda to handle all messages in the topic, turn off any other pollers in that consumer group\.

Additionally, if you specify a consumer group ID, and Kafka finds a valid existing consumer group with the same ID, Lambda ignores the `StartingPosition` parameter for your event source mapping\. Instead, Lambda begins processing records according to the committed offset of the consumer group\. If you specify a consumer group ID, and Kafka cannot find an existing consumer group, then Lambda configures your event source with the specified `StartingPosition`\.

The consumer group ID that you specify must be unique among all your Kafka event sources\. After creating a Kafka event source mapping with the consumer group ID specified, you cannot update this value\.

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

   1. For **Batch window**, enter the maximum amount of seconds that Lambda spends gathering records before invoking the function\.

   1. \(Optional\) For **Consumer group ID**, enter the ID of a Kafka consumer group to join\.

   1. \(Optional\) For **Starting position**, choose **Latest** to start reading the stream from the latest record\. Or, choose **Trim horizon** to start at the earliest available record\.

   1. \(Optional\) For **VPC**, choose the Amazon VPC for your Kafka cluster\. Then, choose the **VPC subnets** and **VPC security groups**\.

      This setting is required if only users within your VPC access your brokers\.

      

   1. \(Optional\) For **Authentication**, choose **Add**, and then do the following:

      1. Choose the access or authentication protocol of the Kafka brokers in your cluster\.
         + If your Kafka broker uses SASL plaintext authentication, choose **BASIC\_AUTH**\.
         + If your broker uses SASL/SCRAM authentication, choose one of the **SASL\_SCRAM** protocols\.
         + If you're configuring mTLS authentication, choose the **CLIENT\_CERTIFICATE\_TLS\_AUTH** protocol\.

      1. For SASL/SCRAM or mTLS authentication, choose the Secrets Manager secret key that contains the credentials for your Kafka cluster\.

   1. \(Optional\) For **Encryption**, choose the Secrets Manager secret containing the root CA certificate that your Kafka brokers use for TLS encryption, if your Kafka brokers use certificates signed by a private CA\.

      This setting applies to TLS encryption for SASL/SCRAM or SASL/PLAIN, and to mTLS authentication\.

   1. To create the trigger in a disabled state for testing \(recommended\), clear **Enable trigger**\. Or, to enable the trigger immediately, select **Enable trigger**\.

1. To create the trigger, choose **Add**\.

### Adding a self\-managed Kafka cluster \(AWS CLI\)<a name="services-kafka-aws-cli"></a>

Use the following example AWS CLI commands to create and view a self\-managed Apache Kafka trigger for your Lambda function\.

#### Using SASL/SCRAM<a name="services-kafka-aws-cli-create"></a>

If Kafka users access your Kafka brokers over the internet, specify the Secrets Manager secret that you created for SASL/SCRAM authentication\. The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\.

```
aws lambda create-event-source-mapping --topics AWSKafkaTopic
          --source-access-configuration Type=SASL_SCRAM_512_AUTH,URI=arn:aws:secretsmanager:us-east-1:01234567890:secret:MyBrokerSecretName
          --function-name arn:aws:lambda:us-east-1:01234567890:function:my-kafka-function
          --self-managed-event-source '{"Endpoints":{"KAFKA_BOOTSTRAP_SERVERS":["abc3.xyz.com:9092", "abc2.xyz.com:9092"]}}'
```

For more information, see the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) API reference documentation\.

#### Using a VPC<a name="services-kafka-aws-cli-create-vpc"></a>

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

#### Viewing the status using the AWS CLI<a name="services-kafka-aws-cli-view"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping
          --uuid dh38738e-992b-343a-1077-3478934hjkfd7
```

## Using a Kafka cluster as an event source<a name="kafka-using-cluster"></a>

When you add your Apache Kafka cluster as a trigger for your Lambda function, the cluster is used as an [event source](invocation-eventsourcemapping.md)\.

Lambda reads event data from the Kafka topics that you specify as `Topics` in a [CreateEventSourceMapping](API_CreateEventSourceMapping.md) request, based on the `StartingPosition` that you specify\. After successful processing, your Kafka topic is committed to your Kafka cluster\.

If you specify the `StartingPosition` as `LATEST`, Lambda starts reading from the latest message in each partition belonging to the topic\. Because there can be some delay after trigger configuration before Lambda starts reading the messages, Lambda doesn't read any messages produced during this window\.

Lambda processes records from one or more Kafka topic partitions that you specify and sends a JSON payload to your function\. When more records are available, Lambda continues processing records in batches, based on the `BatchSize` value that you specify in a [CreateEventSourceMapping](API_CreateEventSourceMapping.md) request, until your function catches up with the topic\.

If your function returns an error for any of the messages in a batch, Lambda retries the whole batch of messages until processing succeeds or the messages expire\.

**Note**  
While Lambda functions typically have a maximum timeout limit of 15 minutes, event source mappings for Amazon MSK, self\-managed Apache Kafka, and Amazon MQ for ActiveMQ and RabbitMQ only support functions with maximum timeout limits of 14 minutes\. This constraint ensures that the event source mapping can properly handle function errors and retries\.

## Auto scaling of the Kafka event source<a name="services-kafka-scaling"></a>

When you initially create an an Apache Kafka [event source](invocation-eventsourcemapping.md), Lambda allocates one consumer to process all partitions in the Kafka topic\. Each consumer has multiple processors running in parallel to handle increased workloads\. Additionally, Lambda automatically scales up or down the number of consumers, based on workload\. To preserve message ordering in each partition, the maximum number of consumers is one consumer per partition in the topic\.

In one\-minute intervals, Lambda evaluates the consumer offset lag of all the partitions in the topic\. If the lag is too high, the partition is receiving messages faster than Lambda can process them\. If necessary, Lambda adds or removes consumers from the topic\. The scaling process of adding or removing consumers occurs within three minutes of evaluation\.

If your target Lambda function is overloaded, Lambda reduces the number of consumers\. This action reduces the workload on the function by reducing the number of messages that consumers can retrieve and send to the function\.

To monitor the throughput of your Kafka topic, you can view the Apache Kafka consumer metrics, such as `consumer_lag` and `consumer_offset`\. To check how many function invocations occur in parallel, you can also monitor the [concurrency metrics](monitoring-metrics.md#monitoring-metrics-concurrency) for your function\.

## Event source API operations<a name="kafka-hosting-api-operations"></a>

When you add your Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function using the Lambda console, an AWS SDK, or the AWS CLI, Lambda uses APIs to process your request\.

To manage an event source with the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+  [CreateEventSourceMapping](API_CreateEventSourceMapping.md) 
+  [ListEventSourceMappings](API_ListEventSourceMappings.md) 
+  [GetEventSourceMapping](API_GetEventSourceMapping.md) 
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) 
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md) 

## Event source mapping errors<a name="services-event-errors"></a>

When you add your Apache Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function, if your function encounters an error, your Kafka consumer stops processing records\. Consumers of a topic partition are those that subscribe to, read, and process your records\. Your other Kafka consumers can continue processing records, provided they don't encounter the same error\.

To determine the cause of a stopped consumer, check the `StateTransitionReason` field in the response of `EventSourceMapping`\. The following list describes the event source errors that you can receive:

**`ESM_CONFIG_NOT_VALID`**  
The event source mapping configuration isn't valid\.

**`EVENT_SOURCE_AUTHN_ERROR`**  
Lambda couldn't authenticate the event source\.

**`EVENT_SOURCE_AUTHZ_ERROR`**  
Lambda doesn't have the required permissions to access the event source\.

**`FUNCTION_CONFIG_NOT_VALID`**  
The function configuration isn't valid\.

**Note**  
If your Lambda event records exceed the allowed size limit of 6 MB, they can go unprocessed\.

## Amazon CloudWatch metrics<a name="services-kafka-metrics"></a>

Lambda emits the `OffsetLag` metric while your function processes records\. The value of this metric is the difference in offset between the last record written to the Kafka event source topic and the last record that your function's consumer group processed\. You can use `OffsetLag` to estimate the latency between when a record is added and when your consumer group processes it\.

An increasing trend in `OffsetLag` can indicate issues with pollers in your function's consumer group\. For more information, see [Working with Lambda function metrics](monitoring-metrics.md)\.

## Self\-managed Apache Kafka configuration parameters<a name="services-kafka-parms"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Apache Kafka\.


**Event source parameters that apply to self\-managed Apache Kafka**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  BatchSize  |  N  |  100  |  Maximum: 10,000  | 
|  Enabled  |  N  |  Enabled  |     | 
|  FunctionName  |  Y  |     |     | 
|  FilterCriteria  |  N  |     |  [Lambda event filtering](invocation-eventfiltering.md)  | 
|  MaximumBatchingWindowInSeconds  |  N  |  500 ms  |  [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)  | 
|  SelfManagedEventSource  |  Y  |  |  List of Kafka Brokers\. Can set only on Create  | 
|  SelfManagedKafkaEventSourceConfig  |  N  |  Contains the ConsumerGroupId field which defaults to a unique value\.  |  Can set only on Create  | 
|  SourceAccessConfigurations  |  N  |  No credentials  |  VPC information or authentication credentials for the cluster   For SASL\_PLAIN, set to BASIC\_AUTH  | 
|  StartingPosition  |  Y  |     |  TRIM\_HORIZON or LATEST Can set only on Create  | 
|  Topics  |  Y  |     |  Topic name Can set only on Create  | 