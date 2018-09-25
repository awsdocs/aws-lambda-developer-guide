# Tutorial: Configuring a Lambda Function to Access Amazon ElastiCache in an Amazon VPC<a name="vpc-ec"></a>

In this tutorial, you do the following:
+ Create an Amazon ElastiCache cluster in your default Amazon Virtual Private Cloud \(Amazon VPC\) in the us\-east\-1 region\. For more information about Amazon ElastiCache, see [Amazon ElastiCache](https://aws.amazon.com/elasticache/)\.
+ Create a Lambda function to access the ElastiCache cluster\. When you create the Lambda function, you provide subnet IDs in your Amazon VPC and a VPC security group to allow the Lambda function to access resources in your VPC\. For illustration in this tutorial, the Lambda function generates a UUID, writes it to the cache, and retrieves it from the cache\.
+ Invoke the Lambda function manually and verify that it accessed the ElastiCache cluster in your VPC\.

**Important**  
This tutorial uses the default Amazon VPC in the us\-east\-1 region in your account\. For more information about Amazon VPC, see [How to Get Started with Amazon VPC](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_Introduction.html#howto) in the *Amazon VPC User Guide*\. 

**Next Step**  
[Step 1: Create an ElastiCache Cluster](vpc-ec-create-ec-cluster.md)