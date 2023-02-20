# Using Lambda with Amazon MSK<a name="with-msk"></a>

[Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](https://docs.aws.amazon.com/msk/latest/developerguide/what-is-msk.html) is a fully managed service that you can use to build and run applications that use Apache Kafka to process streaming data\. Amazon MSK simplifies the setup, scaling, and management of clusters running Kafka\. Amazon MSK also makes it easier to configure your application for multiple Availability Zones and for security with AWS Identity and Access Management \(IAM\)\. Amazon MSK supports multiple open\-source versions of Kafka\.

Amazon MSK as an event source operates similarly to using Amazon Simple Queue Service \(Amazon SQS\) or Amazon Kinesis\. Lambda internally polls for new messages from the event source and then synchronously invokes the target Lambda function\. Lambda reads the messages in batches and provides these to your function as an event payload\. The maximum batch size is configurable\. \(The default is 100 messages\.\)

For an example of how to configure Amazon MSK as an event source, see [Using Amazon MSK as an event source for AWS Lambda](http://aws.amazon.com/blogs/compute/using-amazon-msk-as-an-event-source-for-aws-lambda/) on the AWS Compute Blog\. For a complete tutorial, see [ Amazon MSK Lambda Integration](https://amazonmsk-labs.workshop.aws/en/msklambda.html) in the Amazon MSK Labs\.

For Kafka\-based event sources, Lambda supports processing control parameters, such as batching windows and batch size\. For more information, see [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)\.

Lambda reads the messages sequentially for each partition\. After Lambda processes each batch, it commits the offsets of the messages in that batch\. If your function returns an error for any of the messages in a batch, Lambda retries the whole batch of messages until processing succeeds or the messages expire\.

**Note**  
While Lambda functions typically have a maximum timeout limit of 15 minutes, event source mappings for Amazon MSK, self\-managed Apache Kafka, and Amazon MQ for ActiveMQ and RabbitMQ only support functions with maximum timeout limits of 14 minutes\. This constraint ensures that the event source mapping can properly handle function errors and retries\.

Lambda sends the batch of messages in the event parameter when it invokes your function\. The event payload contains an array of messages\. Each array item contains details of the Amazon MSK topic and partition identifier, together with a timestamp and a base64\-encoded message\.

```
{
   "eventSource":"aws:kafka",
   "eventSourceArn":"arn:aws:kafka:sa-east-1:123456789012:cluster/vpc-2priv-2pub/751d2973-a626-431c-9d4e-d7975eb44dd7-2",
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
+ [MSK cluster authentication](#msk-cluster-permissions)
+ [Managing API access and permissions](#msk-permissions)
+ [Authentication and authorization errors](#msk-permissions-errors)
+ [Network configuration](#services-msk-vpc-config)
+ [Adding Amazon MSK as an event source](#services-msk-topic-add)
+ [Auto scaling of the Amazon MSK event source](#services-msk-ops-scaling)
+ [Amazon CloudWatch metrics](#services-msk-metrics)
+ [Amazon MSK configuration parameters](#services-msk-parms)

## MSK cluster authentication<a name="msk-cluster-permissions"></a>

Lambda needs permission to access the Amazon MSK cluster, retrieve records, and perform other tasks\. Amazon MSK supports several options for controlling client access to the MSK cluster\.

**Topics**
+ [Unauthenticated access](#msk-permissions-none)
+ [SASL/SCRAM authentication](#msk-permissions-add-secret)
+ [IAM role\-based authentication](#msk-permissions-iam-policy)
+ [Mutual TLS authentication](#msk-permissions-mTLS)
+ [Configuring the mTLS secret](#smaa-auth-secret)
+ [How Lambda chooses a bootstrap broker](#msk-bootstrap-brokers)

### Unauthenticated access<a name="msk-permissions-none"></a>

If no clients access the cluster over the internet, you can use unauthenticated access\.

### SASL/SCRAM authentication<a name="msk-permissions-add-secret"></a>

Amazon MSK supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication with Transport Layer Security \(TLS\) encryption\. For Lambda to connect to the cluster, you store the authentication credentials \(user name and password\) in an AWS Secrets Manager secret\.

For more information about using Secrets Manager, see [User name and password authentication with AWS Secrets Manager](https://docs.aws.amazon.com/msk/latest/developerguide/msk-password.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

Amazon MSK doesn't support SASL/PLAIN authentication\.

### IAM role\-based authentication<a name="msk-permissions-iam-policy"></a>

You can use IAM to authenticate the identity of clients that connect to the MSK cluster\. If IAM auth is active on your MSK cluster, and you don't provide a secret for auth, Lambda automatically defaults to using IAM auth\. To create and deploy IAM user or role\-based policies, use the IAM console or API\. For more information, see [IAM access control](https://docs.aws.amazon.com/msk/latest/developerguide/iam-access-control.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

To allow Lambda to connect to the MSK cluster, read records, and perform other required actions, add the following permissions to your function's [execution role](lambda-intro-execution-role.md)\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "kafka-cluster:Connect",
                "kafka-cluster:DescribeGroup",
                "kafka-cluster:AlterGroup",
                "kafka-cluster:DescribeTopic",
                "kafka-cluster:ReadData",
                "kafka-cluster:DescribeClusterDynamicConfiguration"
            ],
            "Resource": [
                "arn:aws:kafka:region:account-id:cluster/cluster-name/cluster-uuid",
                "arn:aws:kafka:region:account-id:topic/cluster-name/cluster-uuid/topic-name",
                "arn:aws:kafka:region:account-id:group/cluster-name/cluster-uuid/consumer-group-id"
            ]
        }
    ]
}
```

You can scope these permissions to a specific cluster, topic, and group\. For more information, see the [Amazon MSK Kafka actions](https://docs.aws.amazon.com/msk/latest/developerguide/iam-access-control.html#kafka-actions) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

### Mutual TLS authentication<a name="msk-permissions-mTLS"></a>

Mutual TLS \(mTLS\) provides two\-way authentication between the client and server\. The client sends a certificate to the server for the server to verify the client, and the server sends a certificate to the client for the client to verify the server\. 

For Amazon MSK, Lambda acts as the client\. You configure a client certificate \(as a secret in Secrets Manager\) to authenticate Lambda with the brokers in your MSK cluster\. The client certificate must be signed by a CA in the server's trust store\. The MSK cluster sends a server certificate to Lambda to authenticate the brokers with Lambda\. The server certificate must be signed by a certificate authority \(CA\) that's in the AWS trust store\. 

For instructions on how to generate a client certificate, see [ Introducing mutual TLS authentication for Amazon MSK as an event source](http://aws.amazon.com/blogs/compute/introducing-mutual-tls-authentication-for-amazon-msk-as-an-event-source)\.

Amazon MSK doesn't support self\-signed server certificates, because all brokers in Amazon MSK use [public certificates](https://docs.aws.amazon.com/msk/latest/developerguide/msk-encryption.html) signed by [Amazon Trust Services CAs](https://www.amazontrust.com/repository/), which Lambda trusts by default\.



For more information about mTLS for Amazon MSK, see [Mutual TLS Authentication](https://docs.aws.amazon.com/msk/latest/developerguide/msk-authentication.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

### Configuring the mTLS secret<a name="smaa-auth-secret"></a>

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

The following example shows the contents of a secret for mTLS authentication using an encrypted private key\. For an encrypted private key, you include the private key password in the secret\.

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

### How Lambda chooses a bootstrap broker<a name="msk-bootstrap-brokers"></a>

Lambda chooses a [ bootstrap broker](https://docs.aws.amazon.com/msk/latest/developerguide/msk-get-bootstrap-brokers.html) based on the authentication methods available on your cluster, and whether you provide a secret for authentication\. If you provide a secret for mTLS or SASL/SCRAM, Lambda automatically chooses that auth method\. If you don't provide a secret, Lambda selects the strongest auth method that's active on your cluster\. The following is the order of priority in which Lambda selects a broker, from strongest to weakest auth:
+ mTLS \(secret provided for mTLS\)
+ SASL/SCRAM \(secret provided for SASL/SCRAM\)
+ SASL IAM \(no secret provided, and IAM auth active\)
+ Unauthenticated TLS \(no secret provided, and IAM auth not active\)
+ Plaintext \(no secret provided, and both IAM auth and unauthenticated TLS are not active\)

**Note**  
If Lambda can't connect to the most secure broker type, Lambda doesn't attempt to connect to a different \(weaker\) broker type\. If you want Lambda to choose a weaker broker type, deactivate all stronger auth methods on your cluster\.

## Managing API access and permissions<a name="msk-permissions"></a>

In addition to accessing the Amazon MSK cluster, your function needs permissions to perform various Amazon MSK API actions\. You add these permissions to the function's execution role\. If your users need access to any of the Amazon MSK API actions, add the required permissions to the identity policy for the IAM user or role\.

### Required Lambda function execution role permissions<a name="msk-api-actions"></a>

Your Lambda function's [execution role](lambda-intro-execution-role.md) must have the following permissions to access the MSK cluster on your behalf\. You can either add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role, or create a custom policy with permission to perform the following actions:
+ [kafka:DescribeCluster](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn.html#clusters-clusterarnget)
+ [kafka:DescribeClusterV2](https://docs.aws.amazon.com/MSK/2.0/APIReference/v2-clusters-clusterarn.html#v2-clusters-clusterarnget)
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

**Note**  
Lambda eventually plans to remove the `kafka:DescribeCluster` permission from this policy\. You should migrate any applications using `kafka:DescribeCluster` to use `kafka:DescribeClusterV2` instead\.

### Adding permissions to your execution role<a name="msk-permissions-add-policy"></a>

Follow these steps to add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role using the IAM console\.

**To add an AWS managed policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the IAM console\.

1. In the search box, enter the policy name \(`AWSLambdaMSKExecutionRole`\)\.

1. Select the policy from the list, and then choose **Policy actions**, **Attach**\.

1. On the **Attach policy** page, select your execution role from the list, and then choose **Attach policy**\.

### Granting users access with an IAM policy<a name="msk-permissions-identity-policy"></a>

By default, IAM users and roles don't have permission to perform Amazon MSK API operations\. To grant access to users in your organization or account, you can add or update an identity\-based policy\. For more information, see [Amazon MSK Identity\-Based Policy Examples](https://docs.aws.amazon.com/msk/latest/developerguide/security_iam_id-based-policy-examples.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

### Using SASL/SCRAM authentication<a name="msk-permissions-add-secret"></a>

Amazon MSK supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication with TLS encryption\. You can control access to your Amazon MSK clusters by setting up user name and password authentication using an AWS Secrets Manager secret\. For more information, see [Username and password authentication with AWS Secrets Manager](https://docs.aws.amazon.com/msk/latest/developerguide/msk-password.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

Note that Amazon MSK does not support SASL/PLAIN authentication\.

## Authentication and authorization errors<a name="msk-permissions-errors"></a>

If any of the permissions required to consume data from the Amazon MSK cluster are missing, Lambda displays one of the following error messages in the event source mapping under **LastProcessingResult**\.

**Topics**
+ [Cluster failed to authorize Lambda](#msk-authorize-errors)
+ [SASL authentication failed](#msk-sasl-errors)
+ [Server failed to authenticate Lambda](#msk-mtls-errors)
+ [Provided certificate or private key is invalid](#msk-key-errors)

### Cluster failed to authorize Lambda<a name="msk-authorize-errors"></a>

For SASL/SCRAM or mTLS, this error indicates that the provided user doesn't have all of the following required Kafka access control list \(ACL\) permissions:
+ DescribeConfigs Cluster
+ Describe Group
+ Read Group
+ Describe Topic
+ Read Topic

For IAM access control, your function's execution role is missing one or more of the permissions required to access the group or topic\. Review the list of required permissions in [ IAM role\-based authentication](#msk-permissions-iam-policy)\.

When you create either Kafka ACLs or an IAM policy with the required Kafka cluster permissions, specify the topic and group as resources\. The topic name must match the topic in the event source mapping\. The group name must match the event source mapping's UUID\.

After you add the required permissions to the execution role, it might take several minutes for the changes to take effect\.

### SASL authentication failed<a name="msk-sasl-errors"></a>

For SASL/SCRAM, this error indicates that the provided user name and password aren't valid\.

For IAM access control, the execution role is missing the `kafka-cluster:Connect` permission for the MSK cluster\. Add this permission to the role and specify the cluster's Amazon Resource Name \(ARN\) as a resource\.

You might see this error occurring intermittently\. The cluster rejects connections after the number of TCP connections exceeds the [Amazon MSK service quota](https://docs.aws.amazon.com/msk/latest/developerguide/limits.html)\. Lambda backs off and retries until a connection is successful\. After Lambda connects to the cluster and polls for records, the last processing result changes to `OK`\.

### Server failed to authenticate Lambda<a name="msk-mtls-errors"></a>

This error indicates that the Amazon MSK Kafka brokers failed to authenticate with Lambda\. This can occur for any of the following reasons:
+ You didn't provide a client certificate for mTLS authentication\.
+ You provided a client certificate, but the brokers aren't configured to use mTLS\.
+ A client certificate isn't trusted by the brokers\.

### Provided certificate or private key is invalid<a name="msk-key-errors"></a>

This error indicates that the Amazon MSK consumer couldn't use the provided certificate or private key\. Make sure that the certificate and key use PEM format, and that the private key encryption uses a PBES1 algorithm\.

## Network configuration<a name="services-msk-vpc-config"></a>

Lambda must have access to the Amazon Virtual Private Cloud \(Amazon VPC\) resources associated with your Amazon MSK cluster\. We recommend that you deploy [AWS PrivateLink VPC endpoints](https://docs.aws.amazon.com/vpc/latest/privatelink/aws-services-privatelink-support.html) for Lambda and AWS Security Token Service \(AWS STS\)\. The poller needs access to AWS STS to assume the execution role associated with the Lambda function\. Lambda must have access to the Lambda VPC endpoint to invoke the function\. If you configured a secret in Secrets Manager to authenticate Lambda with the brokers, also [deploy a VPC endpoint for Secrets Manager](https://docs.aws.amazon.com/secretsmanager/latest/userguide/vpc-endpoint-overview.html)\.

Alternatively, ensure that the VPC associated with your MSK cluster includes one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

Configure your Amazon VPC security groups with the following rules \(at minimum\):
+ Inbound rules – Allow all traffic on the Amazon MSK broker port \(9092 for plaintext, 9094 for TLS, 9096 for SASL, 9098 for IAM\) for the security groups specified for your event source\.
+ Outbound rules – Allow all traffic on port 443 for all destinations\. Allow all traffic on the Amazon MSK broker port \(9092 for plaintext, 9094 for TLS, 9096 for SASL, 9098 for IAM\) for the security groups specified for your event source\.
+ If you are using VPC endpoints instead of a NAT gateway, the security groups associated with the VPC endpoints must allow all inbound traffic on port 443 from the event source's security groups\.

**Note**  
Your Amazon VPC configuration is discoverable through the [Amazon MSK API](https://docs.aws.amazon.com/msk/1.0/apireference/resources.html)\. You don't need to configure it during setup using the create\-event\-source\-mapping command\.

For more information about configuring the network, see [Setting up AWS Lambda with an Apache Kafka cluster within a VPC](http://aws.amazon.com/blogs/compute/setting-up-aws-lambda-with-an-apache-kafka-cluster-within-a-vpc/) on the AWS Compute Blog\.

## Adding Amazon MSK as an event source<a name="services-msk-topic-add"></a>

To create an [event source mapping](invocation-eventsourcemapping.md), add Amazon MSK as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\. Note that when you add Amazon MSK as a trigger, Lambda assumes the VPC settings of the Amazon MSK cluster, not the Lambda function's VPC settings\.

This section describes how to create an event source mapping using the Lambda console and the AWS CLI\.

### Prerequisites<a name="services-msk-prereqs"></a>
+ An Amazon MSK cluster and a Kafka topic\. For more information, see [Getting Started Using Amazon MSK](https://docs.aws.amazon.com/msk/latest/developerguide/getting-started.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.
+ An [execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your MSK cluster uses\.

### Customizable consumer group ID<a name="services-msk-consumer-group-id"></a>

When setting up Kafka as an event source, you can specify a consumer group ID\. This consumer group ID is an existing identifier for the Kafka consumer group that you want your Lambda function to join\. You can use this feature to seamlessly migrate any ongoing Kafka record processing setups from other consumers to Lambda\.

If you specify a consumer group ID and there are other active pollers within that consumer group, Kafka distributes messages across all consumers\. In other words, Lambda doesn't receive all message for the Kafka topic\. If you want Lambda to handle all messages in the topic, turn off any other pollers in that consumer group\.

Additionally, if you specify a consumer group ID, and Kafka finds a valid existing consumer group with the same ID, Lambda ignores the `StartingPosition` parameter for your event source mapping\. Instead, Lambda begins processing records according to the committed offset of the consumer group\. If you specify a consumer group ID, and Kafka cannot find an existing consumer group, then Lambda configures your event source with the specified `StartingPosition`\.

The consumer group ID that you specify must be unique among all your Kafka event sources\. After creating a Kafka event source mapping with the consumer group ID specified, you cannot update this value\.

### Adding an Amazon MSK trigger \(console\)<a name="services-msk-trigger"></a>

Follow these steps to add your Amazon MSK cluster and a Kafka topic as a trigger for your Lambda function\.

**To add an Amazon MSK trigger to your Lambda function \(console\)**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your Lambda function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Under **Trigger configuration**, do the following:

   1. Choose the **MSK** trigger type\.

   1. For **MSK cluster**, select your cluster\.

   1. For **Batch size**, enter the maximum number of messages to receive in a single batch\.

   1. For **Batch window**, enter the maximum amount of seconds that Lambda spends gathering records before invoking the function\.

   1. For **Topic name**, enter the name of a Kafka topic\.

   1. \(Optional\) For **Consumer group ID**, enter the ID of a Kafka consumer group to join\.

   1. \(Optional\) For **Starting position**, choose **Latest** to start reading the stream from the latest record\. Or, choose **Trim horizon** to start at the earliest available record\.

   1. \(Optional\) For **Authentication**, choose the secret key for authenticating with the brokers in your MSK cluster\.

   1. To create the trigger in a disabled state for testing \(recommended\), clear **Enable trigger**\. Or, to enable the trigger immediately, select **Enable trigger**\.

1. To create the trigger, choose **Add**\.

### Adding an Amazon MSK trigger \(AWS CLI\)<a name="services-msk-aws-cli"></a>

Use the following example AWS CLI commands to create and view an Amazon MSK trigger for your Lambda function\.

#### Creating a trigger using the AWS CLI<a name="services-msk-aws-cli-create"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) AWS CLI command to map a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`\. The topic's starting position is set to `LATEST`\.

```
aws lambda create-event-source-mapping \
  --event-source-arn arn:aws:kafka:us-west-2:arn:aws:kafka:us-west-2:111111111111:cluster/my-cluster/fc2f5bdf-fd1b-45ad-85dd-15b4a5a6247e-2 \
  --topics AWSKafkaTopic \
  --starting-position LATEST \
  --function-name my-kafka-function
```

For more information, see the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) API reference documentation\.

#### Viewing the status using the AWS CLI<a name="services-msk-aws-cli-view"></a>

The following example uses the [https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html) AWS CLI command to describe the status of the event source mapping that you created\.

```
aws lambda get-event-source-mapping \
  --uuid 6d9bce8e-836b-442c-8070-74e77903c815
```

## Auto scaling of the Amazon MSK event source<a name="services-msk-ops-scaling"></a>

When you initially create an Amazon MSK event source, Lambda allocates one consumer to process all partitions in the Kafka topic\. Each consumer has multiple processors running in parallel to handle increased workloads\. Additionally, Lambda automatically scales up or down the number of consumers, based on workload\. To preserve message ordering in each partition, the maximum number of consumers is one consumer per partition in the topic\.

In one\-minute intervals, Lambda evaluates the consumer offset lag of all the partitions in the topic\. If the lag is too high, the partition is receiving messages faster than Lambda can process them\. If necessary, Lambda adds or removes consumers from the topic\. The scaling process of adding or removing consumers occurs within three minutes of evaluation\.

If your target Lambda function is overloaded, Lambda reduces the number of consumers\. This action reduces the workload on the function by reducing the number of messages that consumers can retrieve and send to the function\.

To monitor the throughput of your Kafka topic, view the [Offset lag metric](#services-msk-metrics) Lambda emits while your function processes records\.

To check how many function invocations occur in parallel, you can also monitor the [concurrency metrics](monitoring-metrics.md#monitoring-metrics-concurrency) for your function\.

## Amazon CloudWatch metrics<a name="services-msk-metrics"></a>

Lambda emits the `OffsetLag` metric while your function processes records\. The value of this metric is the difference in offset between the last record written to the Kafka event source topic and the last record that your function's consumer group processed\. You can use `OffsetLag` to estimate the latency between when a record is added and when your consumer group processes it\.

An increasing trend in `OffsetLag` can indicate issues with pollers in your function's consumer group\. For more information, see [Working with Lambda function metrics](monitoring-metrics.md)\.

## Amazon MSK configuration parameters<a name="services-msk-parms"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Amazon MSK\.


**Event source parameters that apply to Amazon MSK**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  AmazonManagedKafkaEventSourceConfig  |  N  |  Contains the ConsumerGroupId field, which defaults to a unique value\.  |  Can set only on Create  | 
|  BatchSize  |  N  |  100  |  Maximum: 10,000  | 
|  Enabled  |  N  |  Enabled  |     | 
|  EventSourceArn  |  Y  |  |  Can set only on Create  | 
|  FunctionName  |  Y  |     |     | 
|  FilterCriteria  |  N  |     |  [Lambda event filtering](invocation-eventfiltering.md)  | 
|  MaximumBatchingWindowInSeconds  |  N  |  500 ms  |  [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)  | 
|  SourceAccessConfigurations  |  N  |  No credentials  |  SASL/SCRAM or CLIENT\_CERTIFICATE\_TLS\_AUTH \(MutualTLS\) authentication credentials for your event source  | 
|  StartingPosition  |  Y  |     |  TRIM\_HORIZON or LATEST Can set only on Create  | 
|  Topics  |  Y  |     |  Kafka topic name Can set only on Create  | 