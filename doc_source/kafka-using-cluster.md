# Using an Apache Kafka cluster as an event source for Lambda<a name="kafka-using-cluster"></a>



You can host an Apache Kafka cluster on AWS, or on any other cloud provider of your choice\. Lambda supports Kafka as an [event source](invocation-eventsourcemapping.md) regardless of where it is hosted, as long as Lambda can access the cluster\.

This page describes how to use your Kafka cluster as an event source for your Lambda function\.

## Prerequisites<a name="kafka-hosting-prereqs"></a>
+ A [Lambda function](getting-started-create-function.md) with function code in a [supported runtime](lambda-runtimes.md) to invoke your cluster
+ A [Lambda execution role](lambda-intro-execution-role.md)

## How it works<a name="kafka-hosting-how-it-works"></a>



When you add your Apache Kafka cluster as a trigger for your Lambda function, the cluster is used as an [event source](invocation-eventsourcemapping.md)\. When you add your Kafka cluster and topic as an event source, Lambda creates a consumer group with an event source `UUID`\.
+ If you use an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) cluster as your event source in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-EventSourceArn](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-EventSourceArn), Lambda reads event data using the Amazon MSK cluster and the Kafka topic that you specify\.
+ If you use a non\-AWS hosted Apache Kafka cluster—or an AWS hosted Apache Kafka cluster on another AWS service—as your event source in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SelfManagedEventSource](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SelfManagedEventSource), Lambda reads event data using the Kafka host, topic, and connection details that you specify\.
+ Lambda reads event data from the Kafka topics that you specify in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-Topics](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-Topics) based on the starting position that you specify in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-StartingPosition](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-StartingPosition)\. After successful processing, your Kafka topic is committed to your Kafka cluster\.
+ Lambda processes records from one or more Kafka topic partitions that you specify and sends a JSON payload to your Lambda function\. When more records are available, Lambda continues processing records in batches, based on the value that you specify in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize), until the function catches up with the topic\.
+ Lambda supports Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) authentication for your Kafka brokers\. Lambda uses the SASL/SCRAM user name and password that you specify in your AWS Secrets Manager secret in [https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SourceAccessConfigurations](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-SourceAccessConfigurations)\.

For Amazon MSK and self\-managed Apache Kafka, the maximum amount of time that Lambda allows a function to run before stopping it is 14 minutes\.

## Event source API operations<a name="kafka-hosting-api-operations"></a>

When you add your Kafka cluster as an [event source](invocation-eventsourcemapping.md) for your Lambda function using the Lambda console, an AWS SDK, or the AWS Command Line Interface \(AWS CLI\), Lambda uses APIs to process your request\.

To manage an event source with the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) or [AWS SDK](http://aws.amazon.com/getting-started/tools-sdks/), you can use the following API operations:
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)

## Event source mapping errors<a name="services-event-errors"></a>

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