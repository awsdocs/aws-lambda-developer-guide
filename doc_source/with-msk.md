# Using Lambda with Amazon MSK<a name="with-msk"></a>

[Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](https://docs.aws.amazon.com/msk/latest/developerguide/what-is-msk.html) is a fully managed service that you can use to build and run applications that use Apache Kafka to process streaming data\. Amazon MSK simplifies the setup, scaling, and management of clusters running Kafka\. Amazon MSK also makes it easier to configure your application for multiple Availability Zones and for security with AWS Identity and Access Management \(IAM\)\. Additionally, Amazon MSK supports multiple open\-source versions of Kafka\.

Amazon MSK as an event source operates similarly to using Amazon Simple Queue Service \(Amazon SQS\) or Amazon Kinesis\. Lambda internally polls for new messages from the event source and then synchronously invokes the target Lambda function\. Lambda reads the messages in batches and provides these to your function as an event payload\. The maximum batch size is configurable\. \(The default is 100 messages\.\)

For an example of how to configure Amazon MSK as an event source, see [Using Amazon MSK as an event source for AWS Lambda](http://aws.amazon.com/blogs/compute/using-amazon-msk-as-an-event-source-for-aws-lambda/) on the AWS Compute Blog\. Also, see [ Amazon MSK Lambda Integration](https://amazonmsk-labs.workshop.aws/en/msklambda.html) in the Amazon MSK Labs for a complete tutorial\.

Lambda reads the messages sequentially for each partition\. After Lambda processes each batch, it commits the offsets of the messages in that batch\. If your function times out or returns an error for any of the messages in a batch, Lambda retries the whole batch of messages until processing succeeds or the messages expire\.

For Amazon MSK invocations, Lambda allows the function to run for up to 14 minutes\. Set your function timeout value to 14 minutes or less \(the default timeout value is 3 seconds\)\.

Lambda sends the batch of messages in the event parameter when it invokes your function\. The event payload contains an array of messages\. Each array item contains details of the Amazon MSK topic and partition identifier, together with a timestamp and a base64\-encoded message\.

```
{
   "eventSource":"aws:kafka",
   "eventSourceArn":"arn:aws:kafka:sa-east-1:123456789012:cluster/vpc-2priv-2pub/751d2973-a626-431c-9d4e-d7975eb44dd7-2",
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
+ [Managing access and permissions](#msk-permissions)
+ [Network configuration](#services-msk-vpc-config)
+ [Adding Amazon MSK as an event source](#services-msk-topic-add)
+ [Auto scaling of the Amazon MSK event source](#services-msk-ops-scaling)
+ [Amazon MSK configuration parameters](#services-msk-parms)

## Managing access and permissions<a name="msk-permissions"></a>

You can use IAM access control to handle both authentication and authorization for your Amazon MSK cluster\. This eliminates the need to use one mechanism for authentication and a different mechanism for authorization\. For example, when a client tries to write to your cluster, Amazon MSK uses IAM to check whether that client is an authenticated identity and also whether it is authorized to produce to your cluster\. 

As an alternative, you can use SASL/SCRAM to authenticate clients and [Apache Kafka ACLs](https://docs.aws.amazon.com/msk/latest/developerguide/msk-acls.html) to control access\. 

### Required Lambda function permissions<a name="msk-api-actions"></a>

Your Lambda function's [execution role](lambda-intro-execution-role.md) must have permission to read records from your Amazon MSK cluster on your behalf\. You can either add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role, or create a custom policy with permission to perform the following actions:
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

### Additional function permissions for IAM authorization<a name="msk-permissions-iam-auth"></a>

If you plan to use IAM authorization, you need to add the following additional permissions:

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
                "arn:aws:kafka:<region>:<account>:cluster/<clusterName>/<clusterUUID>",
                "arn:aws:kafka:<region>:<account>:topic/<clusterName>/<clusterUUID>/<topicName>",
                "arn:aws:kafka:<region>:<account>:group/<clusterName>/<clusterUUID>/<eventSourceMappingUUID>"
            ]
        }
    ]
}
```

You can scope these permissions to a specific cluster, topic and group\. See [Amazon MSK Kafka actions](https://docs.aws.amazon.com/msk/latest/developerguide/iam-access-control.html#kafka-actions) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\. The group name that Lambda uses is equivalent to the event source mapping’s UUID\.

### Adding a policy to your execution role<a name="msk-permissions-add-policy"></a>

Follow these steps to add the AWS managed policy `AWSLambdaMSKExecutionRole` to your execution role using the IAM console\.

**To add an AWS managed policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the IAM console\.

1. In the search box, enter the policy name \(`AWSLambdaMSKExecutionRole`\)\.

1. Select the policy from the list, and then choose **Policy actions**, **Attach**\.

1. On the **Attach policy** page, select your execution role from the list, and then choose **Attach policy**\.

### Granting users access with an IAM policy<a name="msk-permissions-add-users"></a>

By default, IAM users and roles do not have permission to perform Amazon MSK API operations\. To grant access to users in your organization or account, you might need an identity\-based policy\. For more information, see [Amazon MSK Identity\-Based Policy Examples](https://docs.aws.amazon.com/msk/latest/developerguide/security_iam_id-based-policy-examples.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

### Using SASL/SCRAM authentication<a name="msk-permissions-add-secret"></a>

Amazon MSK supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication with TLS encryption\. You can control access to your Amazon MSK clusters by setting up user name and password authentication using an AWS Secrets Manager secret\. For more information, see [Username and password authentication with AWS Secrets Manager](https://docs.aws.amazon.com/msk/latest/developerguide/msk-password.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.

Note that Amazon MSK does not support SASL/PLAIN authentication\.

### Authentication and authorization Errors<a name="msk-permissions-errors"></a>

If any of the required permissions to consume data from the Amazon MSK cluster are missing, Lambda displays an error message in the event source mapping under **LastProcessingResult**\. 

The following error message results from authorization errors\.

**Example Cluster failed to authorize Lambda**  
For SASL/SCRAM, the provided user does not have all of the required Kafka ACL permissions:  
+ DescribeConfigs Cluster
+ Describe Group
+ Read Group
+ Describe Topic
+ Read Topic
For IAM access control, the execution role is missing one or more of the permissions required to access the group or topic\. To add the missing permissions to the role, see the example in [Additional function permissions for IAM authorization](#msk-permissions-iam-auth)  
When you create either Kafka ACLs or an IAM policy with the required kafka\-cluster permissions listed previously, you must specify the topic and group as resources\. The topic name must match the topic in the event source mapping and the group name must match the event source mapping’s UUID\.  
After you add the required permissions to the execution role, there might be a delay of several minutes before the changes take effect\.

The following error message results from authentication failures\.

**Example SASL authentication failed**  
For SASL/SCRAM, this failure indicates that the provided username and password are invalid\.  
For IAM access control, the execution role is missing `kafka-cluster:Connect` permissions for the cluster\. Add this permission to the role and specify the cluster ARN as a resource\.  
You might see this error intermittently if the cluster rejects connections because it reached the TCP connection limit set by [Amazon MSK](https://docs.aws.amazon.com/msk/latest/developerguide/limits.html)\. Lambda backs off and retries until a connection is successful\. The last processing result will eventually change to “OK” after Lambda successfully connects to and polls from the cluster\.

## Network configuration<a name="services-msk-vpc-config"></a>

Lambda must have access to the Amazon Virtual Private Cloud \(Amazon VPC\) resources associated with your Amazon MSK cluster\. We recommend that you deploy AWS PrivateLink [VPC endpoints](https://docs.aws.amazon.com/vpc/latest/privatelink/endpoint-services-overview.html) for Lambda and AWS Security Token Service \(AWS STS\)\. If authentication is required, also deploy a VPC endpoint for Secrets Manager\.

Alternatively, ensure that the VPC associated with your Amazon MSK cluster includes one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

You must configure your Amazon VPC security groups with the following rules \(at minimum\):
+ Inbound rules – Allow all traffic on the MSK broker port \(9092 for plaintext, 9094 for TLS, 9096 for SASL, 9098 for IAM\) for the security groups specified for your event source\.
+ Outbound rules – Allow all traffic on port 443 for all destinations\. Allow all traffic on the MSK broker port \(9092 for plaintext, 9094 for TLS, 9096 for SASL, 9098 for IAM\) for the security groups specified for your event source\.
+ if you are using VPC endpoints instead of NAT Gateway, the security groups associated with the VPC endpoints must allow all inbound traffic on port 443 from the event source's security groups\.

**Note**  
Your Amazon VPC configuration is discoverable through the [Amazon MSK API](https://docs.aws.amazon.com/msk/1.0/apireference/resources.html), and does not need to be configured during setup using the create\-event\-source\-mapping command\.

For more information about configuring the network, see [Setting up AWS Lambda with an Apache Kafka cluster within a VPC](http://aws.amazon.com/blogs/compute/setting-up-aws-lambda-with-an-apache-kafka-cluster-within-a-vpc/) on the AWS Compute Blog\.

## Adding Amazon MSK as an event source<a name="services-msk-topic-add"></a>

To create an [event source mapping](invocation-eventsourcemapping.md), add Amazon MSK as a Lambda function [trigger](gettingstarted-concepts.md#gettingstarted-concepts-trigger) using the Lambda console, an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), or the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)\.

This section describes how to create an event source mapping using the Lambda console and the AWS CLI\.

### Prerequisites<a name="services-msk-prereqs"></a>
+ An Amazon MSK cluster and a Kafka topic\. For more information, see [Getting Started Using Amazon MSK](https://docs.aws.amazon.com/msk/latest/developerguide/getting-started.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.
+ A [Lambda execution role](lambda-intro-execution-role.md) with permission to access the AWS resources that your Amazon MSK cluster uses\.

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

   1. For **Topic name**, enter the name of a Kafka topic\.

   1. \(Optional\) For **Starting position**, choose **Latest** to start reading the stream from the latest record\. Or, choose **Trim horizon** to start at the earliest available record\.

   1. \(Optional\) For **Secret key**, choose the secret key for SASL/SCRAM authentication of the brokers in your Amazon MSK cluster\. If you are using IAM access control, do not choose a secret key\.

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

When you initially create an Amazon MSK event source, Lambda allocates one consumer to process all of the partitions in the Kafka topic\. Lambda automatically scales up or down the number of consumers, based on workload\. To preserve message ordering in each partition, the maximum number of consumers is one consumer per partition in the topic\.

Every 15 minutes, Lambda evaluates the consumer offset lag of all the partitions in the topic\. If the lag is too high, the partition is receiving messages faster than Lambda can process them\. If necessary, Lambda adds or removes consumers from the topic\.

If your target Lambda function is overloaded, Lambda reduces the number of consumers\. This action reduces the workload on the function by reducing the number of messages that consumers can retrieve and send to the function\.

To monitor the throughput of your Kafka topic, you can view the [Amazon MSK consumer\-lag metrics](https://docs.aws.amazon.com/msk/latest/developerguide/consumer-lag.html)\. To help you find the metrics for this Lambda function, the value of the consumer group field in the logs is set to the event source UUID\.

To check how many function invocations occur in parallel, you can also monitor the [concurrency metrics](monitoring-metrics.md#monitoring-metrics-concurrency) for your function\.

## Amazon MSK configuration parameters<a name="services-msk-parms"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Amazon MSK\.


**Event source parameters that apply to Amazon MSK**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  BatchSize  |  N  |  100  |  Maximum: 10,000  | 
|  Enabled  |  N  |  Enabled  |     | 
|  EventSourceArn  |  Y  |  |  Can set only on Create  | 
|  FunctionName  |  Y  |     |     | 
|  SourceAccessConfigurations  |  N  |  No credentials  |  VPC information or SASL/SCRAM authentication credentials for your event source  | 
|  StartingPosition  |  Y  |     |  TRIM\_HORIZON or LATEST Can set only on Create  | 
|  Topics  |  Y  |     |  Kafka topic name Can set only on Create  | 