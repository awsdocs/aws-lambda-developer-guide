# Step 1: Create an ElastiCache Cluster<a name="vpc-ec-create-ec-cluster"></a>

In this step, you create an ElastiCache cluster in the default Amazon VPC in us\-east\-1 region in your account\.

1. Run the following AWS CLI command to create a Memcached cluster in the default VPC in the us\-east\-1 region in your account\. 

   ```
   aws elasticache create-cache-cluster \
       --cache-cluster-id ClusterForLambdaTest \
       --cache-node-type cache.m3.medium \
       --engine memcached \
       --security-group-ids your-default-vpc-security-group \
       --num-cache-nodes 1
   ```

   You can look up the default VPC security group in the VPC console under **Security Groups**\. Your example Lambda function will add and retrieve an item from this cluster\.

   You can also launch a cache cluster using the Amazon ElastiCache console\. For instructions, see [Getting Started with Amazon ElastiCache](https://docs.aws.amazon.com/AmazonElastiCache/latest/UserGuide/GettingStarted.html) in the *Amazon ElastiCache User Guide*\.

1. Write down the configuration endpoint for the cache cluster that you launched\. You can get this from the Amazon ElastiCache console\. You will specify this value in your Lambda function code in the next section\.

**Next Step**  
[Step 2: Create a Lambda Function ](vpc-ec-create-lambda-function.md)