# Using Lambda with Amazon MSK<a name="with-msk"></a>

Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) is a managed service that enables you to build and run applications that use [Apache Kafka](http://aws.amazon.com/msk/what-is-kafka/) to process streaming data\. Apache Kafka is a distributed streaming platform that is conceptually similar to [Amazon Kinesis](http://aws.amazon.com/kinesis)\. With Amazon MSK, you can collect data from many sources and process them with multiple consumers\.

You can use a Lambda function to process records in a Kafka topic\. Your function is triggered through an [event source mapping](invocation-eventsourcemapping.md), a Lambda resource that reads items from a topic and invokes the function\. Lambda polls across multiple partitions for new records and invokes your target function [synchronously](invocation-sync.md)\.

The Amazon MSK event source mapping supports the following features:
+ Full compatibility with all Kafka versions that Amazon MSK supports\. For more information, see [Supported Apache Kafka versions](https://docs.aws.amazon.com/msk/latest/developerguide/supported-kafka-versions.html) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.
+ Both plaintext and TLS encrypted brokers\. TLS brokers are not supported with a private certificate authority\. For more information, see the **Encryption in Transit** section of [Amazon MSK Encryption](https://docs.aws.amazon.com/msk/latest/developerguide/msk-encryption.html#msk-encryption-in-transit) in the *Amazon Managed Streaming for Apache Kafka Developer Guide*\.
+ Configurable starting positions and batch sizes\. The configurable starting positions supported are `TRIM_HORIZON` and `LATEST`\. They are not timestamp\-based\.

The following Kafka features are not supported:
+ Authentication – SSL and SASL authentication are not supported\.
+ Schema registry – You can host your own schema registry, but the Lambda API doesn't support this functionality\. For more information, see [Schema Management](https://docs.confluent.io/current/schema-registry/index.html) on the Confluent website\.

**Topics**
+ [Lambda consumer group](#services-msk-configure)
+ [Execution role permissions](#events-kinesis-permissions)
+ [Configuring a topic as an event source](#services-msk-eventsourcemapping)
+ [Event source mapping API](#services-msk-api)
+ [Event source mapping errors](#services-msk-errors)

## Lambda consumer group<a name="services-msk-configure"></a>

To interact with Amazon MSK, Lambda creates a consumer group which can read from multiple Kafka topics\. The consumer group is created with the same ID as an event source mapping UUID\. The Lambda created consumer group is also used for checkpointing\. The group's position in each topic partition is committed to Kafka after successful processing\.

Lambda processes records from one or more partitions and then sends the payload to the target function\. When more records are available, Lambda continues processing batches until the function catches up with the topic\. The maximum supported function execution time is 14 minutes\.

**Example Amazon MSK record event**  

```
Received event:{
  "eventSource": "aws:kafka",
  "eventSourceArn": "arn:aws:kafka:us-west-2:012345678901:cluster/ExampleMSKCluster/e9f754c6-d29a-4430-a7db-958a19fd2c54-4",
  "records": {
    "AWSKafkaTopic-0": [
      {
        "topic": "AWSKafkaTopic",
        "partition": 0,
        "offset": 0,
        "timestamp": 1595035749700,
        "timestampType": "CREATE_TIME",
        "key": "OGQ1NTk2YjQtMTgxMy00MjM4LWIyNGItNmRhZDhlM2QxYzBj",
        "value": "OGQ1NTk2YjQtMTgxMy00MjM4LWIyNGItNmRhZDhlM2QxYzBj"
      }
    ]
  }
}
```

**Note**  
The key\-value set of an `aws:kafka` resource is base64\-encoded\.

## Execution role permissions<a name="events-kinesis-permissions"></a>

Your Lambda function's [execution role](lambda-intro-execution-role.md) needs the following permissions to read records from an Amazon MSK cluster:
+ [kafka:DescribeCluster](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn.html#clusters-clusterarnget)
+ [kafka:GetBootstrapBrokers](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn-bootstrap-brokers.html#clusters-clusterarn-bootstrap-brokersget)
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

The AWS managed policy `AWSLambdaMSKExecutionRole` includes these permissions\. For more information, see [Managed policies for Lambda features](lambda-intro-execution-role.md#permissions-executionrole-features)\.

## Configuring a topic as an event source<a name="services-msk-eventsourcemapping"></a>

Create an [event source mapping](invocation-eventsourcemapping.md) to tell Lambda to send records from a Kafka topic to a Lambda function\. You can create multiple event source mappings to process the same data with multiple functions, or to process items from multiple topics with a single function\.

To configure your function to read from Amazon MSK, create an **MSK** trigger in the Lambda console\.

**To create a trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Designer**, choose **Add trigger**\.

1. Choose a trigger type\.

1. Configure the required options and then choose **Add**\.

Lambda supports the following options for Amazon MSK event sources:
+ **MSK cluster** – Select the MSK cluster\.
+ **Topic name** – Enter the Kafka topic to consume\.
+ **Starting position** \(optional\) – Enter the position in the stream to begin reading records\.
  + **Latest** – Read from the latest position in all the topic's partitions\.
  + **Trim Horizon** – Read from the oldest position in all the topic partitions\.

  After processing any existing records, the function is caught up and continues to process new records\.
+ **Enable trigger** – Disable the trigger to stop processing records\. 

To enable or disable the trigger \(or delete it\), choose the **MSK** trigger in the [designer](getting-started-create-function.md#get-started-designer)\. To reconfigure the trigger, use the event source mapping API commands\.

## Event source mapping API<a name="services-msk-api"></a>

To manage event source mappings with the AWS CLI or AWS SDK, use the following API actions:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

To create the event source mapping with the AWS Command Line Interface \(AWS CLI\), use the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html) command\. Fetching records from Amazon MSK brokers requires access to an Amazon Virtual Private Cloud \(Amazon VPC\) associated with your MSK cluster\. To meet the Amazon VPC access requirements, you can configure one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

The Amazon VPC security group rules you configure should have the following settings at minimum:
+ Inbound rules – Allow all traffic on all ports for the security group specified as your source\.
+ Outbound rules – Allow all traffic on all ports for all destinations\.

The Amazon VPC configuration is discoverable through the [Amazon MSK API](https://docs.aws.amazon.com/msk/1.0/apireference/resources.html) and doesn't need to be configured in the `create-event-source-mapping` setup\.

The following AWS CLI example maps a Lambda function named `my-kafka-function` to a Kafka topic named `AWSKafkaTopic`, with the starting position set to `latest`:

```
$ aws lambda create-event-source-mapping --event-source-arn arn:aws:kafka:us-west-2:111111111111:cluster/my-cluster/fc2f5bdf-fd1b-45ad-85dd-15b4a5a6247e-2 --topics AWSKafkaTopic --starting-position LATEST --function-name my-kafka-function 
{
    "UUID": "6d9bce8e-836b-442c-8070-74e77903c815",
    "BatchSize": 100,
    "EventSourceArn": "arn:aws:kafka:us-west-2:111111111111:cluster/my-cluster/fc2f5bdf-fd1b-45ad-85dd-15b4a5a6247e-2",
    "FunctionArn": "arn:aws:lambda:us-west-2:111111111111:function:my-kafka-function",
    "LastModified": 1580331394.363,
    "State": "Creating",
    "StateTransitionReason": "USER_INITIATED",
    "LastProcessingResult": "OK",
    "Topics": [ "AWSKafkaTopic" ]
}
```

Use the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html) command to view the current status of your resource\.

```
$ aws lambda get-event-source-mapping --uuid 6d9bce8e-836b-442c-8070-74e77903c815
{
   "UUID": "6d9bce8e-836b-442c-8070-74e77903c815"
   "BatchSize": 100,
   "EventSourceArn": "arn:aws:kafka:us-west-2:111111111111:cluster/my-cluster/fc2f5bdf-fd1b-45ad-85dd-15b4a5a6247e-2",
   "FunctionArn": "arn:aws:lambda:us-west-2:111111111111:function:my-kafka-function",
   "LastModified": 1580331394.363,
   "State": "Enabled",
   "StateTransitionReason": "User action",
   "LastProcessingResult": "OK",
   "Topics": [ "AWSKafkaTopic" ],
}
```

## Event source mapping errors<a name="services-msk-errors"></a>

When a Lambda function encounters an unrecoverable error, your Kafka topic consumer stops processing records\. Any other consumers may continue processing, provided they don't encounter the same error\. To determine the potential cause of a stopped consumer, check the `StateTransitionReason` field in the return details of your `EventSourceMapping` for one of the following codes:

**`ESM_CONFIG_NOT_VALID`**  
The event source mapping configuration is not valid\.

**`EVENT_SOURCE_AUTHN_ERROR`**  
Lambda failed to authenticate the event source\.

**`EVENT_SOURCE_AUTHZ_ERROR`**  
Lambda does not have the required permissions to access the event source\.

**`FUNCTION_CONFIG_NOT_VALID`**  
The function's configuration is not valid\.

Records will also go unprocessed if they are dropped due to their size\. The size limit for Lambda records is 6 MB\.