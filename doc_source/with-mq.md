# Using Lambda with Amazon MQ<a name="with-mq"></a>

Amazon MQ is a managed message broker service for [Apache ActiveMQ](https://activemq.apache.org/) and [RabbitMQ](https://www.rabbitmq.com)\. A *message broker* enables software applications and components to communicate using various programming languages, operating systems, and formal messaging protocols through either topic or queue event destinations\.

Amazon MQ can also manage Amazon Elastic Compute Cloud \(Amazon EC2\) instances on your behalf by installing ActiveMQ or RabbitMQ brokers and by providing different network topologies and other infrastructure needs\.

You can use a Lambda function to process records from your Amazon MQ message broker\. Lambda invokes your function through an [event source mapping](invocation-eventsourcemapping.md), a Lambda resource that reads messages from your broker and invokes the function [synchronously](invocation-sync.md)\.

The Amazon MQ event source mapping has the following configuration restrictions:
+ Cross account – Lambda does not support cross\-account processing\. You cannot use Lambda to process records from an Amazon MQ message broker that is in a different AWSaccount\.
+ Authentication – For ActiveMQ, only the ActiveMQ [SimpleAuthenticationPlugin](https://activemq.apache.org/security#simple-authentication-plugin) is supported\. For RabbitMQ, only the [PLAIN](https://www.rabbitmq.com/access-control.html#mechanisms) authentication mechanism is supported\. Users must use AWS Secrets Manager to manage their credentials\. For more information about ActiveMQ authentication, see [Integrating ActiveMQ brokers with LDAP](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/security-authentication-authorization.html) in the *Amazon MQ Developer Guide*\.
+ Connection quota – Brokers have a maximum number of allowed connections per wire\-level protocol\. This quota is based on the broker instance type\. For more information, see the [Brokers](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-limits.html#broker-limits) section of **Quotas in Amazon MQ** in the *Amazon MQ Developer Guide*\.
+ Connectivity – You can create brokers in a public or private virtual private cloud \(VPC\)\. For private VPCs, your Lambda function needs access to the VPC to receive messages\. For more information, see [Event source mapping API](#services-mq-api) later in this topic\.
+ Event destinations – Only queue destinations are supported\. However, you can use a virtual topic, which behaves as a topic internally while interacting with Lambda as a queue\. For more information, see [Virtual Destinations](https://activemq.apache.org/virtual-destinations) on the Apache ActiveMQ website, and [Virtual Hosts](https://www.rabbitmq.com/vhosts.html) on the RabbitMQ website\.
+ Network topology – For ActiveMQ, only one single\-instance or standby broker is supported per event source mapping\. For RabbitMQ, only one single\-instance broker or cluster deployment is supported per event source mapping\. Single\-instance brokers require a failover endpoint\. For more information about these broker deployment modes, see [Active MQ Broker Architecture](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-broker-architecture.html) and [Rabbit MQ Broker Architecture](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/rabbitmq-broker-architecture.html)in the *Amazon MQ Developer Guide*\.
+ Protocols – Supported protocols depend on the type of Amazon MQ integration\.
  + For ActiveMQ integrations, Lambda consumes messages using the OpenWire/Java Message Service \(JMS\) protocol\. No other protocols are supported for consuming messages\. Within the JMS protocol, only [https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQTextMessage.html](https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQTextMessage.html) and [https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQBytesMessage.html](https://activemq.apache.org/maven/apidocs/org/apache/activemq/command/ActiveMQBytesMessage.html) are supported\. For more information about the OpenWire protocol, see [OpenWire](https://activemq.apache.org/openwire.html) on the Apache ActiveMQ website\.
  + For RabbitMQ integrations, Lambda consumes messages using the AMQP 0\-9\-1 protocol\. No other protocols are supported for consuming messages\. For more information about RabbitMQ's implementation of the AMQP 0\-9\-1 protocol, see [AMQP 0\-9\-1 Complete Reference Guide](https://www.rabbitmq.com/amqp-0-9-1-reference.html) on the RabbitMQ website\.

Lambda automatically supports the latest versions of ActiveMQ and RabbitMQ that Amazon MQ supports\. For the latest supported versions, see [Amazon MQ release notes](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/amazon-mq-release-notes.html) in the *Amazon MQ Developer Guide*\.

**Note**  
By default, Amazon MQ has a weekly maintenance window for brokers\. During that window of time, brokers are unavailable\. For brokers without standby, Lambda cannot process any messages during that window\.

**Topics**
+ [Lambda consumer group](#services-mq-configure)
+ [Execution role permissions](#events-mq-permissions)
+ [Configuring a broker as an event source](#services-mq-eventsourcemapping)
+ [Event source mapping API](#services-mq-api)
+ [Event source mapping errors](#services-mq-errors)
+ [Amazon MQ and RabbitMQ configuration parameters](#services-mq-params)

## Lambda consumer group<a name="services-mq-configure"></a>

To interact with Amazon MQ, Lambda creates a consumer group which can read from your Amazon MQ brokers\. The consumer group is created with the same ID as the event source mapping UUID\.

For Amazon MQ event sources, Lambda batches records together and sends them to your function in a single payload\. To control behavior, you can configure the batching window and batch size\. Lambda pulls messages until it processes the payload size maximum of 6 MB, the batching window expires, or the number of records reaches the full batch size\. For more information, see [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)\.

The consumer group retrieves the messages as a BLOB of bytes, base64\-encodes them into a single JSON payload, and then invokes your function\. If your function returns an error for any of the messages in a batch, Lambda retries the whole batch of messages until processing succeeds or the messages expire\.

**Note**  
While Lambda functions typically have a maximum timeout limit of 15 minutes, event source mappings for Amazon MSK, self\-managed Apache Kafka, and Amazon MQ for ActiveMQ and RabbitMQ only support functions with maximum timeout limits of 14 minutes\. This constraint ensures that the event source mapping can properly handle function errors and retries\.

You can monitor a given function's concurrency usage using the `ConcurrentExecutions` metric in Amazon CloudWatch\. For more information about concurrency, see [Managing Lambda reserved concurrency](configuration-concurrency.md)\.

**Example Amazon MQ record events**  

```
{
   "eventSource": "aws:mq",
   "eventSourceArn": "arn:aws:mq:us-west-2:111122223333:broker:test:b-9bcfa592-423a-4942-879d-eb284b418fc8",
   "messages": [
      { 
        "messageID": "ID:b-9bcfa592-423a-4942-879d-eb284b418fc8-1.mq.us-west-2.amazonaws.com-37557-1234520418293-4:1:1:1:1", 
        "messageType": "jms/text-message",
        "deliveryMode": 1,
        "replyTo": null,
        "type": null,
        "expiration": "60000",
        "priority": 1,
        "correlationId": "myJMSCoID",
        "redelivered": false,
        "destination": { 
          "physicalname": "testQueue" 
        },
        "data":"QUJDOkFBQUE=",
        "timestamp": 1598827811958,
        "brokerInTime": 1598827811958, 
        "brokerOutTime": 1598827811959 
      },
      { 
        "messageID": "ID:b-9bcfa592-423a-4942-879d-eb284b418fc8-1.mq.us-west-2.amazonaws.com-37557-1234520418293-4:1:1:1:1",
        "messageType": "jms/bytes-message",
        "deliveryMode": 1,
        "replyTo": null,
        "type": null,
        "expiration": "60000",
        "priority": 2,
        "correlationId": "myJMSCoID1",
        "redelivered": false,
        "destination": { 
          "physicalname": "testQueue" 
        },
        "data":"LQaGQ82S48k=",
        "timestamp": 1598827811958,
        "brokerInTime": 1598827811958, 
        "brokerOutTime": 1598827811959 
      }
   ]
}
```

```
{
  "eventSource": "aws:rmq",
  "eventSourceArn": "arn:aws:mq:us-west-2:111122223333:broker:pizzaBroker:b-9bcfa592-423a-4942-879d-eb284b418fc8",
  "rmqMessagesByQueue": {
    "pizzaQueue::/": [
      {
        "basicProperties": {
          "contentType": "text/plain",
          "contentEncoding": null,
          "headers": {
            "header1": {
              "bytes": [
                118,
                97,
                108,
                117,
                101,
                49
              ]
            },
            "header2": {
              "bytes": [
                118,
                97,
                108,
                117,
                101,
                50
              ]
            },
            "numberInHeader": 10
          },
          "deliveryMode": 1,
          "priority": 34,
          "correlationId": null,
          "replyTo": null,
          "expiration": "60000",
          "messageId": null,
          "timestamp": "Jan 1, 1970, 12:33:41 AM",
          "type": null,
          "userId": "AIDACKCEVSQ6C2EXAMPLE",
          "appId": null,
          "clusterId": null,
          "bodySize": 80
        },
        "redelivered": false,
        "data": "eyJ0aW1lb3V0IjowLCJkYXRhIjoiQ1pybWYwR3c4T3Y0YnFMUXhENEUifQ=="
      }
    ]
  }
}
```
In the RabbitMQ example, `pizzaQueue` is the name of the RabbitMQ queue, and `/` is the name of the virtual host\. When receiving messages, the event source lists messages under `pizzaQueue::/`\.

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
+ [logs:CreateLogGroup](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogGroup.html)
+ [logs:CreateLogStream](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_CreateLogStream.html)
+ [logs:PutLogEvents](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_PutLogEvents.html)

**Note**  
When using an encrypted customer managed key, add the `[kms:Decrypt](https://docs.aws.amazon.com/msk/1.0/apireference/clusters-clusterarn-bootstrap-brokers.html#clusters-clusterarn-bootstrap-brokersget)` permission as well\.

## Configuring a broker as an event source<a name="services-mq-eventsourcemapping"></a>

Create an [event source mapping](invocation-eventsourcemapping.md) to tell Lambda to send records from an Amazon MQ broker to a Lambda function\. You can create multiple event source mappings to process the same data with multiple functions, or to process items from multiple sources with a single function\.

To configure your function to read from Amazon MQ, create an **MQ** trigger in the Lambda console\.

**To create a trigger**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of a function\.

1. Under **Function overview**, choose **Add trigger**\.

1. Choose a trigger type\.

1. Configure the required options, and then choose **Add**\.

Lambda supports the following options for Amazon MQ event sources:
+ **MQ broker** – Select an Amazon MQ broker\.
+ **Batch size** – Set the maximum number of messages to retrieve in a single batch\.
+ **Queue name** – Enter the Amazon MQ queue to consume\.
+ **Source access configuration** – Enter virtual host information and the Secrets Manager secret that stores your broker credentials\.
+ **Enable trigger** – Disable the trigger to stop processing records\.

To enable or disable the trigger \(or delete it\), choose the **MQ** trigger in the designer\. To reconfigure the trigger, use the event source mapping API operations\.

## Event source mapping API<a name="services-mq-api"></a>

To manage an event source with the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or an [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+  [CreateEventSourceMapping](API_CreateEventSourceMapping.md) 
+  [ListEventSourceMappings](API_ListEventSourceMappings.md) 
+  [GetEventSourceMapping](API_GetEventSourceMapping.md) 
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) 
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md) 

To create the event source mapping with the AWS Command Line Interface \(AWS CLI\), use the [https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/create-event-source-mapping.html) command\.

By default, Amazon MQ brokers are created with the `PubliclyAccessible` flag set to false\. It is only when `PubliclyAccessible` is set to true that the broker receives a public IP address\.

For full access with your event source mapping, your broker must either use a public endpoint or provide access to the VPC\. Note that when you add Amazon MQ as a trigger, Lambda assumes the VPC settings of the Amazon MQ broker, not the Lambda function's VPC settings\. To meet the Amazon Virtual Private Cloud \(Amazon VPC\) access requirements, you can do one of the following:
+ Configure one NAT gateway per public subnet\. For more information, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.
+ Create a connection between your Amazon VPC and Lambda\. Your Amazon VPC must also connect to AWS Security Token Service \(AWS STS\) and Secrets Manager endpoints\. For more information, see [Configuring interface VPC endpoints for Lambda](configuration-vpc-endpoints.md)\.

The Amazon VPC security group rules that you configure should have the following settings at minimum:
+ Inbound rules – For a broker without public accessibility, allow all traffic on all ports for the security group that's specified as your source\. For a broker with public accessibility, allow all traffic on all ports for all destinations\.
+ Outbound rules – Allow all traffic on all ports for all destinations\.

The Amazon VPC configuration is discoverable through the [Amazon MQ API](https://docs.aws.amazon.com/amazon-mq/latest/api-reference/resources.html) and does not need to be configured in the `create-event-source-mapping` setup\.

The following example AWS CLI command creates an event source which maps a Lambda function named `MQ-Example-Function` to an Amazon MQ RabbitMQ\-based broker named `ExampleMQBroker`\. The command also provides the virtual host name and a Secrets Manager secret ARN that stores the broker credentials\.

```
aws lambda create-event-source-mapping \
--event-source-arn arn:aws:mq:us-east-1:123456789012:broker:ExampleMQBroker:b-24cacbb4-b295-49b7-8543-7ce7ce9dfb98 \
--function-name arn:aws:lambda:us-east-1:123456789012:function:MQ-Example-Function \
--queues ExampleQueue \
--source-access-configuration Type=VIRTUAL_HOST,URI="/" Type=BASIC_AUTH,URI=arn:aws:secretsmanager:us-east-1:123456789012:secret:ExampleMQBrokerUserPassword-xPBMTt \
```

You should see the following output:

```
{
    "UUID": "91eaeb7e-c976-1234-9451-8709db01f137",
    "BatchSize": 100,
    "EventSourceArn": "arn:aws:mq:us-east-1:123456789012:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca",
    "FunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:MQ-Example-Function",
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
            "URI": "arn:aws:secretsmanager:us-east-1:123456789012:secret:ExampleMQBrokerUserPassword-xPBMTt"
        }
    ]
}
```

Using the `[update\-event\-source\-mapping](https://docs.aws.amazon.com/cli/latest/reference/lambda/update-event-source-mapping.html)` command, you can configure additional options such as how Lambda processes batches and to specify when to discard records that cannot be processed\. The following example command updates an event source mapping to have a batch size of 2\.

```
aws lambda update-event-source-mapping \
--uuid 91eaeb7e-c976-1234-9451-8709db01f137 \
--batch-size 2
```

You should see the following output:

```
{
    "UUID": "91eaeb7e-c976-1234-9451-8709db01f137",
    "BatchSize": 2,
    "EventSourceArn": "arn:aws:mq:us-east-1:123456789012:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca",
    "FunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:MQ-Example-Function",
    "LastModified": 1601928393.531,
    "LastProcessingResult": "No records processed",
    "State": "Updating",
    "StateTransitionReason": "USER_INITIATED"
}
```

Lambda updates these settings asynchronously\. The output will not reflect changes until this process completes\. To view the current status of your resource, use the [https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html](https://docs.aws.amazon.com/cli/latest/reference/lambda/get-event-source-mapping.html) command\.

```
aws lambda get-event-source-mapping \
--uuid 91eaeb7e-c976-4939-9451-8709db01f137
```

You should see the following output:

```
{
    "UUID": "91eaeb7e-c976-4939-9451-8709db01f137",
    "BatchSize": 2,
    "EventSourceArn": "arn:aws:mq:us-east-1:123456789012:broker:ExampleMQBroker:b-b4d492ef-bdc3-45e3-a781-cd1a3102ecca",
    "FunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:MQ-Example-Function",
    "LastModified": 1601928393.531,
    "LastProcessingResult": "No records processed",
    "State": "Enabled",
    "StateTransitionReason": "USER_INITIATED"
}
```

## Event source mapping errors<a name="services-mq-errors"></a>

When a Lambda function encounters an unrecoverable error, your Amazon MQ consumer stops processing records\. Any other consumers can continue processing, provided that they do not encounter the same error\. To determine the potential cause of a stopped consumer, check the `StateTransitionReason` field in the return details of your `EventSourceMapping` for one of the following codes:

**`ESM_CONFIG_NOT_VALID`**  
The event source mapping configuration is not valid\.

**`EVENT_SOURCE_AUTHN_ERROR`**  
Lambda failed to authenticate the event source\.

**`EVENT_SOURCE_AUTHZ_ERROR`**  
Lambda does not have the required permissions to access the event source\.

**`FUNCTION_CONFIG_NOT_VALID`**  
The function's configuration is not valid\.

Records also go unprocessed if Lambda drops them due to their size\. The size limit for Lambda records is 6 MB\. To redeliver messages upon function error, you can use a dead\-letter queue \(DLQ\)\. For more information, see [Message Redelivery and DLQ Handling](https://activemq.apache.org/message-redelivery-and-dlq-handling) on the Apache ActiveMQ website and [Reliability Guide](https://www.rabbitmq.com/reliability.html) on the RabbitMQ website\.

**Note**  
Lambda does not support custom redelivery policies\. Instead, Lambda uses a policy with the default values from the [Redelivery Policy](https://activemq.apache.org/redelivery-policy) page on the Apache ActiveMQ website, with `maximumRedeliveries` set to 5\.

## Amazon MQ and RabbitMQ configuration parameters<a name="services-mq-params"></a>

All Lambda event source types share the same [CreateEventSourceMapping](API_CreateEventSourceMapping.md) and [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md) API operations\. However, only some of the parameters apply to Amazon MQ and RabbitMQ\.


**Event source parameters that apply to Amazon MQ and RabbitMQ**  

| Parameter | Required | Default | Notes | 
| --- | --- | --- | --- | 
|  BatchSize  |  N  |  100  |  Maximum: 10,000  | 
|  Enabled  |  N  |  true  |   | 
|  FunctionName  |  Y  |   |   | 
|  FilterCriteria  |  N  |     |  [Lambda event filtering](invocation-eventfiltering.md)  | 
|  MaximumBatchingWindowInSeconds  |  N  |  500 ms  |  [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)  | 
|  Queues  |  N  |   |  The name of the Amazon MQ broker destination queue to consume\.  | 
|  SourceAccessConfigurations  |  N  |   |  For ActiveMQ, BASIC\_AUTH credentials\. For RabbitMQ, can contain both BASIC\_AUTH credentials and VIRTUAL\_HOST information\.  | 