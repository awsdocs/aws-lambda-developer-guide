# Hosting an Apache Kafka cluster<a name="kafka-hosting"></a>

You can host an Apache Kafka cluster on AWS, or on any other cloud provider of your choice\. Lambda supports Kafka as an [event source](invocation-eventsourcemapping.md) regardless of where it is hosted, as long as Lambda can access the cluster\.

This page describes the AWS and non\-AWS hosting options available for using your Kafka cluster as an for your Lambda function\.

## Using a non\-AWS provider<a name="kafka-hosting-cloud-using"></a>

To host your Apache Kafka cluster and topics, you can use any non\-AWS cloud provider, such as [CloudKarafka](https://www.cloudkarafka.com/)\.

When you create a Kafka cluster using a non\-AWS provider, you receive the connection information for your cluster\. This information includes the Kafka cluster hostname, topic name, Simple Authentication and Security Layer/Salted Challenge Response Authentication Mechanism \(SASL/SCRAM\) user name and password, and bootstrap server host\-port pairs\.

For more information about using a non\-AWS hosted Apache Kafka cluster, see [Using Lambda with self\-managed Apache Kafka](kafka-smaa.md)\.

## Using Amazon MSK<a name="kafka-hosting-msk-using"></a>

To host your Apache Kafka cluster and topics, you can use [Amazon Managed Streaming for Apache Kafka \(Amazon MSK\)](https://docs.aws.amazon.com/msk/latest/developerguide/what-is-msk.html)\.

When you create an Amazon MSK cluster, you receive the required hosting and connection information of the cluster\. This information includes the Kafka cluster hostname, topic name, SASL/SCRAM user name and password, and bootstrap server host\-port pairs\.

To support your Kafka cluster on Amazon MSK, you might need to create Amazon Virtual Private Cloud \(Amazon VPC\) networking components\. For more information, see [Using Amazon MSK as an event source for AWS Lambda](http://aws.amazon.com/blogs/compute/using-amazon-msk-as-an-event-source-for-aws-lambda/) on the AWS Compute Blog\.

For more information about using an MSK cluster, see [Using Lambda with Amazon MSK](with-msk.md)\.

## Using other AWS hosting options<a name="kafka-hosting-other-using"></a>

You can also use other AWS hosting options for your Apache Kafka cluster and topics\. For more information, see [Best Practices for Running Apache Kafka on AWS](http://aws.amazon.com/blogs/big-data/best-practices-for-running-apache-kafka-on-aws/) on the AWS Big Data Blog\.

For more information about using an AWS hosted Apache Kafka cluster on another AWS service, such as Amazon Elastic Compute Cloud \(Amazon EC2\), see [Using Lambda with self\-managed Apache Kafka](kafka-smaa.md)\.