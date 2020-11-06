# Using Lambda with Amazon MQ<a name="with-mq"></a>

Amazon MQ is a managed message broker service for [Apache ActiveMQ](https://activemq.apache.org/) and [RabbitMQ](https://www.rabbitmq.com)\. Lambda only supports Apache ActiveMQ\. A *message broker* allows software applications and components to communicate using various programming languages, operating systems, and formal messaging protocols through either topic or queue event destinations\.

Amazon MQ can also manage Amazon Elastic Compute Cloud \(Amazon EC2\) instances on your behalf by installing ActiveMQ brokers and by providing different network topologies and other infrastructure needs\.

You can use a Lambda function to process records from your Amazon MQ message broker\. Your function is triggered through an [event source mapping](invocation-eventsourcemapping.md), a Lambda resource that reads messages from your broker and invokes the function [synchronously](invocation-sync.md)\.

The Amazon MQ event source mapping has the following configuration restrictions:
+ Authentication – Only the ActiveMQ [SimpleAuthenticationPlugin](https://activemq.apache.org/security#simple-authentication-plugin) is supported\. User credentials associated with the broker are the only method of connection\. For more information about authentication, see [Messaging Authentication and Authorization for ActiveMQ](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/security-authentication-authorization.html) in the *Amazon MQ Developer Guide*\.
+ Connection quota – Brokers have a maximum number of allowed connections per wire\-level protocol\. This quota is based on the broker instance type\. For more information, see the [Brokers](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-limits.html#broker-limits) section of **Quotas in Amazon MQ** in the *Amazon MQ Developer Guide*\.
+ Connectivity – You can create brokers in a public or private virtual private cloud \(VPC\)\. For private VPCs, your Lambda function needs access to the VPC to interact with the records\. For more information, see [Event source mapping API](#services-mq-api) later in this topic\.
+ Event destinations – Only queue destinations are supported\. However, you can use a virtual topic, which behaves as a topic internally while interacting with Lambda as a queue\. For more information, see [Virtual Destinations](https://activemq.apache.org/virtual-destinations) on the Apache ActiveMQ website\.
+ Network topology – Only one single\-instance or standby broker is supported per event source mapping\. Single\-instance brokers require a failover endpoint\. For more information about these broker deployment modes, see [Amazon MQ Broker Architecture](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-broker-architecture.html) in the *Amazon MQ Developer Guide*\.
+ Protocols – Lambda consumes messages using the OpenWire/Java Message Service \(JMS\) protocol\. No other protocols are supported\. Within the JMS protocol, only [https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQTextMessage.html](https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQTextMessage.html) and [https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQBytesMessage.html](https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQBytesMessage.html) are supported\. For more information about the OpenWire protocol, see [OpenWire](https://activemq.apache.org/openwire.html) on the Apache ActiveMQ website\.

Lambda automatically supports the latest versions of ActiveMQ that Amazon MQ supports\. For the latest supported versions, see [Amazon MQ Release Notes](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-release-notes.html) in the *Amazon MQ Developer Guide*\.

**Note**  
By default, Amazon MQ has a weekly maintenance window for brokers\. During that window of time, brokers are unavailable\. For brokers without standby, Lambda cannot process any messages during that window\.

**Topics**
+ [Lambda consumer group](#services-mq-configure)
+ [Execution role permissions](#events-mq-permissions)
+ [Configuring a broker as an event source](#services-mq-eventsourcemapping)
+ [Event source mapping API](#services-mq-api)
+ [Event source mapping errors](#services-mq-errors)

## Lambda consumer group<a name="services-mq-configure"></a>

To interact with Amazon MQ, Lambda creates a consumer group which can read from your Amazon MQ brokers\. The consumer group is created with the same ID as the event source mapping UUID\.

Lambda will pull messages until it has processed a maximum of 6 MB, until timeout, or until the batch size is fulfilled\. When configured, batch size determines the maximum number of items to retrieve in a single batch\. Your batch is converted into a Lambda payload, and your target function is invoked\. Messages are neither persisted nor deserialized\. Instead, they are retrieved by the consumer group as a BLOB of bytes and are base64\-encoded for a JSON payload\.

**Note**  
The maximum function invocation time is 14 minutes\. 

Lambda processes all incoming batches concurrently and automatically scales the concurrency to meet demands\. You can monitor a given function's concurrency usage using the `ConcurrentExecutions` metric in Amazon CloudWatch\. For more information about concurrency, see [Managing concurrency for a Lambda function](configuration-concurrency.md)\.

**Example Amazon MQ record event**  

```
{
    "eventSource": "aws:amq",
    "eventSourceArn": "arn:aws:mq:us-west-2:112556298976:broker:test:b-9bcfa592-423a-4942-879d-eb284b418fc8",
    "messages": { [
            {
                "messageID": "ID:b-9bcfa592-423a-4942-879d-eb284b418fc8-1.mq.us-west-2.amazonaws.com-37557-1234520418293-4:1:1:1:1",
                "messageType": "jms/text-message",
                "data": "QUJDOkFBQUE=",
                "connectionId": "myJMSCoID",
                "redelivered": false,
                "destination": {
                   "physicalname": "testQueue" 
                }, 
                "timestamp": 1598827811958,
                "brokerInTime": 1598827811958,
                "brokerOutTime": 1598827811959
            },
            {
                "messageID": "ID:b-9bcfa592-423a-4942-879d-eb284b418fc8-1.mq.us-west-2.amazonaws.com-37557-1234520418293-4:1:1:1:1",
                "messageType":"jms/bytes-message",
                "data": "3DTOOW7crj51prgVLQaGQ82S48k=",
                "connectionId": "myJMSCoID1",
                "persistent": false,
                "destination": {
                   "physicalname": "testQueue" 
                }, 
                "timestamp": 1598827811958,
                "brokerInTime": 1598827811958,
                "brokerOutTime": 1598827811959
            }
        ]
    }
}
```

## Execution role permissions<a name="events-mq-permissions"></a>

To read records from an Amazon MQ broker, your Lambda function needs the following permissions added to its [execution role](lambda-intro-execution-role.md):
+ [mq:DescribeBroker](https://docs.aws.amazon.com/amazon-mq/latest/api-reference/brokers-broker-id.html#brokers-broker-id-http-methods)
+ [secretsmanager:GetSecretValue](https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html)
+ [ec2:CreateNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateNetworkInterface.html)
+ [ec2:DeleteNetworkInterface](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteNetworkInterface.html)
+ [ec2:DescribeNetworkInterfaces](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html)
+ [ec2:DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)
+ [ec2:DescribeSubnets](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html)
+ [ec2:DescribeVpcs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcs.html)

The AWS managed policy `AWSLambdaMQExecutionRole` includes these permissions\. For more information, see [AWS managed policies for Lambda features](lambda-intro-execution-role.md#permissions-executionrole-features)\.

**Note**  
When using an encrypted customer managed key, add the `[kms:Decrypt](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn-bootstrap-brokers.html#clusters-clusterarn-bootstrap-brokersget)` permission as well\.

## Configuring a broker as an event source<a name="services-mq-eventsourcemapping"></a>

Create an [event source mapping](invocation-eventsourcemapping.md) to tell Lambda to send records from an Amazon MQ broker to a Lambda function\. You can create multiple event source mappings to process the same data with multiple functions, or to process items from multiple sources with a single function\.

To configure your function to read from Amazon MQ, create an **MQ** trigger in the Lambda console\.

**To create a trigger**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Under **Designer**, choose **Add trigger**\.

1. Choose a trigger type\.

1. Configure the required options and then choose **Add**\.

Lambda supports the following options for Amazon MQ event sources:
+ **MQ broker** – Select an Amazon MQ broker\.
+ **Batch size** – Set the maximum number of messages to retrieve in a single batch\.
+ **Queue name** – Enter the Amazon MQ queue to consume\.
+ **Source access configuration** – Select the AWS Secrets Manager secret that stores your broker credentials\.
+ **Enable trigger** – Disable the trigger to stop processing records\.

To enable or disable the trigger \(or delete it\), choose the **MQ** trigger in the [designer](getting-started-create-function.md#get-started-designer)\. To reconfigure the trigger, use the event source mapping API operations\.

## Event source mapping API<a name="services-mq-api"></a>

To manage event source mappings with the AWS CLI or AWS SDK, use the following API operations:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

To create the event source mapping with the AWS Command Line Interface \(AWS CLI\), use the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/create-event-source-mapping.html) command\.

By default, Amazon MQ brokers are created with the `PubliclyAccessible` flag set to false\. It is only when `PubliclyAccessible` is set to true that the broker is given a public IP address\. 

For full access with your event source mapping, your broker must either use a public endpoint or provide access to the VPC\. To meet the Amazon VPC access requirements, you can do one of the following:
+ Configure one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.
+ Create a connection between your Amazon VPC and Lambda\. Your Amazon VPC must also connect to AWS STS and Secrets Manager endpoints\. For more information, see [Configuring interface VPC endpoints for Lambda](configuration-vpc-endpoints.md)\.

The Amazon Virtual Private Cloud \(Amazon VPC\) security group rules that you configure should have the following settings at minimum:
+ Inbound rules – For a broker without public accessibility, allow all traffic on all ports for the security group that's specified as your source\. For a broker with public accessibility, allow all traffic on all ports for all destinations\.
+ Outbound rules – Allow all traffic on all ports for all destinations\.

The Amazon VPC configuration is discoverable through the [Amazon MQ API](https://docs.aws.amazon.com/amazon-mq/latest/api-reference/resources.html) and does not need to be configured in the `create-event-source-mapping` setup\.

The following example AWS CLI command creates an event source which maps a Lambda function named `MQ-Example-Function` to an Amazon MQ broker named `ExampleMQBroker`\. The command also provides a Secrets Manager secret named `ExampleMQBrokerUserPassword` that stores the broker credentials\.

```
$ aws lambda create-event-source-mapping \
--event-source-arn arn:aws:mq:us-east-1:12345678901:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca \
--function-name MQ-Example-Function \
--source-access-configuration Type=BASIC_AUTH,URI=arn:aws:secretsmanager:us-east-1:12345678901:secret:ExampleMQBrokerUserPassword-xPBMTt \
--queues ExampleQueue 
{
    "UUID": "91eaeb7e-c976-1234-9451-8709db01f137",
    "BatchSize": 100,
    "EventSourceArn": "arn:aws:mq:us-east-1:12345678901:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca",
    "FunctionArn": "arn:aws:lambda:us-east-1:12345678901:function:MQ-Example-Function",
    "LastModified": 1601927898.741,
    "LastProcessingResult": "No records processed",
    "State": "Creating",
    "StateTransitionReason": "USER_INITIATED",
    "Queues": [
        "ExampleQueue"
    ],
    "SourceAccessConfigurations": [
        {
            "Type": "BASIC_AUTH",
            "URI": "arn:aws:secretsmanager:us-east-1:12345678901:secret:ExampleMQBrokerUserPassword-xPBMTt"
        }
    ]
}
```

Using the `[update\-event\-source\-mapping](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/update-event-source-mapping.html)` command, you can configure additional options such as how batches are processed and to specify when to discard records that can't be processed\. The following example command updates an event source mapping to have a batch size of 2\.

```
$ aws lambda update-event-source-mapping \
--uuid 91eaeb7e-c976-1234-9451-8709db01f137 \
--batch-size 2
{
    "UUID": "91eaeb7e-c976-1234-9451-8709db01f137",
    "BatchSize": 2,
    "EventSourceArn": "arn:aws:mq:us-east-1:12345678901:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca",
    "FunctionArn": "arn:aws:lambda:us-east-1:12345678901:function:MQ-Example-Function",
    "LastModified": 1601928393.531,
    "LastProcessingResult": "No records processed",
    "State": "Updating",
    "StateTransitionReason": "USER_INITIATED"
}
```

Updated settings are applied asynchronously and aren't reflected in the output until the process completes\. To view the current status of your resource, use the [https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/lambda/get-event-source-mapping.html) command\.

```
$ aws lambda get-event-source-mapping \
--uuid 91eaeb7e-c976-4939-9451-8709db01f137
{
    "UUID": "91eaeb7e-c976-4939-9451-8709db01f137",
    "BatchSize": 2,
    "EventSourceArn": "arn:aws:mq:us-east-1:12345678901:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca",
    "FunctionArn": "arn:aws:lambda:us-east-1:12345678901:function:MQ-Example-Function",
    "LastModified": 1601928393.531,
    "LastProcessingResult": "No records processed",
    "State": "Enabled",
    "StateTransitionReason": "USER_INITIATED"
}
```

## Event source mapping errors<a name="services-mq-errors"></a>

When a Lambda function encounters an unrecoverable error, your Amazon MQ consumer stops processing records\. Any other consumers can continue processing, provided they don't encounter the same error\. To determine the potential cause of a stopped consumer, check the `StateTransitionReason` field in the return details of your `EventSourceMapping` for one of the following codes:

**`ESM_CONFIG_NOT_VALID`**  
The event source mapping configuration is not valid\.

**`EVENT_SOURCE_AUTHN_ERROR`**  
Lambda failed to authenticate the event source\.

**`EVENT_SOURCE_AUTHZ_ERROR`**  
Lambda does not have the required permissions to access the event source\.

**`FUNCTION_CONFIG_NOT_VALID`**  
The function's configuration is not valid\.

Records also go unprocessed if they are dropped due to their size\. The size limit for Lambda records is 6 MB\. To redeliver messages upon function error, you can use a redelivery policy and dead\-letter queue \(DLQ\) handling with Amazon MQ\. For more information, see [Message Redelivery and DLQ Handling](https://activemq.apache.org/message-redelivery-and-dlq-handling) on the Apache ActiveMQ website\.