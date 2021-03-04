# Using Lambda with self\-managed Apache Kafka<a name="kafka-smaa"></a>

You can onboard a non\-AWS hosted Apache Kafka cluster—or an AWS hosted Apache Kafka cluster on another AWS service—as an [event source](invocation-eventsourcemapping.md) for a Lambda function\. This enables you to trigger your functions in response to records sent to your Kafka cluster\.

This section describes how to use a function with a self\-managed Kafka cluster to process records in an Kafka topic\.

**Topics**
+ [Managing access and permissions for a self\-managed Apache Kafka cluster](smaa-permissions.md)
+ [Adding a self\-managed Apache Kafka cluster as an event source](services-smaa-topic-add.md)