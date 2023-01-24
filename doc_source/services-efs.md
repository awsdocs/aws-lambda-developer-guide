# Using Amazon EFS with Lambda<a name="services-efs"></a>

Lambda integrates with Amazon Elastic File System \(Amazon EFS\) to support secure, shared file system access for Lambda applications\. You can configure functions to mount a file system during initialization with the NFS protocol over the local network within a VPC\. Lambda manages the connection and encrypts all traffic to and from the file system\. 

The file system and the Lambda function must be in the same region\. A Lambda function in one account can mount a file system in a different account\. For this scenario, you configure VPC peering between the function VPC and the file system VPC\. 

**Note**  
To configure a function to connect to a file system, see [Configuring file system access for Lambda functions](configuration-filesystem.md)\.

Amazon EFS supports [file locking](https://docs.aws.amazon.com/efs/latest/ug/how-it-works.html#consistency) to prevent corruption if multiple functions try to write to the same file system at the same time\. Locking in Amazon EFS follows the NFS v4\.1 protocol for advisory locking, and enables your applications to use both whole file and byte range locks\.

Amazon EFS provides options to customize your file system based on your application's need to maintain high performance at scale\. There are three primary factors to consider: the number of connections, throughput \(in MiB per second\), and IOPS\.

**Quotas**  
For detail on file system quotas and limits, see [Quotas for Amazon EFS file systems](https://docs.aws.amazon.com/efs/latest/ug/limits.html#limits-fs-specific) in the *Amazon Elastic File System User Guide*\.

To avoid issues with scaling, throughput, and IOPS, monitor the [metrics](https://docs.aws.amazon.com/efs/latest/ug/monitoring-cloudwatch.html) that Amazon EFS sends to Amazon CloudWatch\. For an overview of monitoring in Amazon EFS, see [Monitoring Amazon EFS](https://docs.aws.amazon.com/efs/latest/ug/monitoring_overview.html) in the *Amazon Elastic File System User Guide*\.

**Topics**
+ [Connections](#services-efs-connections)
+ [Throughput](#services-efs-throughput)
+ [IOPS](#services-efs-iops)

## Connections<a name="services-efs-connections"></a>

Amazon EFS supports up to 25,000 connections per file system\. During initialization, each instance of a function creates a single connection to its file system that persists across invocations\. This means that you can reach 25,000 concurrency across one or more functions connected to a file system\. To limit the number of connections a function creates, use [reserved concurrency](configuration-concurrency.md)\.

However, when you make changes to your function's code or configuration at scale, there is a temporary increase in the number of function instances beyond the current concurrency\. Lambda provisions new instances to handle new requests and there is some delay before old instances close their connections to the file system\. To avoid hitting the maximum connections limit during a deployment, use [rolling deployments](lambda-rolling-deployments.md)\. With rolling deployments, you gradually shift traffic to the new version each time you make a change\.

If you connect to the same file system from other services such as Amazon EC2, you should also be aware of the scaling behavior of connections in Amazon EFS\. A file system supports the creation of up to 3,000 connections in a burst, after which it supports 500 new connections per minute\. This matches [burst scaling](invocation-scaling.md) behavior in Lambda, which applies across all functions in a Region\. But if you are creating connections outside of Lambda, your functions may not be able to scale at full speed\.

To monitor and trigger an alarm on connections, use the `ClientConnections` metric\.

## Throughput<a name="services-efs-throughput"></a>

At scale, it is also possible to exceed the maximum *throughput* for a file system\. In *bursting mode* \(the default\), a file system has a low baseline throughput that scales linearly with its size\. To allow for bursts of activity, the file system is granted burst credits that allow it to use 100 MiB/s or more of throughput\. Credits accumulate continually and are expended with every read and write operation\. If the file system runs out of credits, it throttles read and write operations beyond the baseline throughput, which can cause invocations to time out\.

**Note**  
If you use [provisioned concurrency](configuration-concurrency.md), your function can consume burst credits even when idle\. With provisioned concurrency, Lambda initializes instances of your function before it is invoked, and recycles instances every few hours\. If you use files on an attached file system during initialization, this activity can use all of your burst credits\.

To monitor and trigger an alarm on throughput, use the `BurstCreditBalance` metric\. It should increase when your function's concurrency is low and decrease when it is high\. If it always decreases or does not accumulate enough during low activity to cover peak traffic, you may need to limit your function's concurrency or enable [provisioned throughput](https://docs.aws.amazon.com/efs/latest/ug/performance.html#throughput-modes)\.

## IOPS<a name="services-efs-iops"></a>

Input/output operations per second \(IOPS\) is a measurement of the number of read and write operations processed by the file system\. In general purpose mode, IOPS is limited in favor of lower latency, which is beneficial for most applications\.

To monitor and alarm on IOPS in general purpose mode, use the `PercentIOLimit` metric\. If this metric reaches 100%, your function can time out waiting for read and write operations to complete\.