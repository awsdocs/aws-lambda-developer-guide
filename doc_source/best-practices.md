# Best Practices for Working with AWS Lambda Functions<a name="best-practices"></a>

The following are recommended best practices for using AWS Lambda:

**Topics**
+ [Function Code](#function-code)
+ [Function Configuration](#function-configuration)
+ [Alarming and Metrics](#alarming-metrics)
+ [Stream Event Invokes](#stream-events)
+ [Async Invokes](#async-invoke)
+ [Lambda VPC](#lambda-vpc)

## Function Code<a name="function-code"></a>
+ **Separate the Lambda handler \(entry point\) from your core logic\.** This allows you to make a more unit\-testable function\. In Node\.js this may look like: 

  ```
  exports.myHandler = function(event, context, callback) {
  	var foo = event.foo;
  	var bar = event.bar;
  	var result = MyLambdaFunction (foo, bar);
   
  	callback(null, result);
  }
   
  function MyLambdaFunction (foo, bar) {
  	// MyLambdaFunction logic here
  }
  ```
+ **Take advantage of Execution Context reuse to improve the performance of your function\. ** Make sure any externalized configuration or dependencies that your code retrieves are stored and referenced locally after initial execution\. Limit the re\-initialization of variables/objects on every invocation\. Instead use static initialization/constructor, global/static variables and singletons\. Keep alive and reuse connections \(HTTP, database, etc\.\) that were established during a previous invocation\. 
+ **Use [Environment Variables](env_variables.md) to pass operational parameters to your function\.** For example, if you are writing to an Amazon S3 bucket, instead of hard\-coding the bucket name you are writing to, configure the bucket name as an environment variable\. 
+ **Control the dependencies in your function's deployment package\. ** The AWS Lambda execution environment contains a number of libraries such the AWS SDK for the Node\.js and Python runtimes \(a full list can be found here: [Lambda Execution Environment and Available Libraries](current-supported-versions.md)\)\. To enable the latest set of features and security updates, Lambda will periodically update these libraries\. These updates may introduce subtle changes to the behavior of your Lambda function\. To have full control of the dependencies your function uses, we recommend packaging all your dependencies with your deployment package\. 
+ **Minimize your deployment package size to its runtime necessities\. ** This will reduce the amount of time that it takes for your deployment package to be downloaded and unpacked ahead of invocation\. For functions authored in Java or \.NET Core, avoid uploading the entire AWS SDK library as part of your deployment package\. Instead, selectively depend on the modules which pick up components of the SDK you need \(e\.g\. DynamoDB, Amazon S3 SDK modules and [Lambda core libraries](https://github.com/aws/aws-lambda-java-libs)\)\. 
+ **Reduce the time it takes Lambda to unpack deployment packages** authored in Java by putting your dependency `.jar` files in a separate /lib directory\. This is faster than putting all your function’s code in a single jar with a large number of `.class` files\. 
+ **Minimize the complexity of your dependencies\.** Prefer simpler frameworks that load quickly on [Execution Context](http://docs.aws.amazon.com/lambda/latest/dg/running-lambda-code.html) startup\. For example, prefer simpler Java dependency injection \(IoC\) frameworks like [Dagger](http://square.github.io/dagger/) or [Guice](https://github.com/google/guice), over more complex ones like [Spring Framework](https://github.com/spring-projects/spring-framework)\. 
+ **Avoid using recursive code** in your Lambda function, wherein the function automatically calls itself until some arbitrary criteria is met\. This could lead to unintended volume of function invocations and escalated costs\. If you do accidentally do so, set the function concurrent execution limit to `0` immediately to throttle all invocations to the function, while you update the code\.

## Function Configuration<a name="function-configuration"></a>
+ **Performance testing your Lambda function** is a crucial part in ensuring you pick the optimum memory size configuration\. Any increase in memory size triggers an equivalent increase in CPU available to your function\. The memory usage for your function is determined per\-invoke and can be viewed in [AWS CloudWatch Logs](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/WhatIsCloudWatchLogs.html)\. On each invoke a `REPORT:` entry will be made, as shown below: 

  ```
  REPORT RequestId: 3604209a-e9a3-11e6-939a-754dd98c7be3	Duration: 12.34 ms	Billed Duration: 100 ms Memory Size: 128 MB	Max Memory Used: 18 MB
  ```

  By analyzing the `Max Memory Used:` field, you can determine if your function needs more memory or if you over\-provisioned your function's memory size\. 
+ **Load test your Lambda function** to determine an optimum timeout value\. It is important to analyze how long your function runs so that you can better determine any problems with a dependency service that may increase the concurrency of the function beyond what you expect\. This is especially important when your Lambda function makes network calls to resources that may not handle Lambda's scaling\. 
+ **Use most\-restrictive permissions when setting IAM policies\.** Understand the resources and operations your Lambda function needs, and limit the execution role to these permissions\. For more information, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)\. 
+ **Be familiar with [AWS Lambda Limits](limits.md)\.** Payload size, file descriptors and /tmp space are often overlooked when determining runtime resource limits\. 
+ **Delete Lambda functions that you are no longer using\.** By doing so, the unused functions won't needlessly count against your deployment package size limit\.
+ **If you are using Amazon Simple Queue Service** as an event source, make sure the value of the function's expected execution time does not exceed the [Visibility Timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html) value on the queue\. This applies both to [CreateFunction](API_CreateFunction.md) and [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\. 
  + In the case of **CreateFunction**, AWS Lambda will fail the function creation process\.
  + In the case of **UpdateFunctionConfiguration**, it could result in duplicate invocations of the function\.

## Alarming and Metrics<a name="alarming-metrics"></a>
+ **Use [AWS Lambda Metrics](monitoring-functions-metrics.md) and [ CloudWatch Alarms](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html)** instead of creating or updating a metric from within your Lambda function code\. It's a much more efficient way to track the health of your Lambda functions, allowing you to catch issues early in the development process\. For instance, you can configure an alarm based on the expected duration of your Lambda function execution time in order to address any bottlenecks or latencies attributable to your function code\.
+ **Leverage your logging library and [AWS Lambda Metrics and Dimensions](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/lam-metricscollected.html)** to catch app errors \(e\.g\. ERR, ERROR, WARNING, etc\.\) 

## Stream Event Invokes<a name="stream-events"></a>
+ **Test with different batch and record sizes **so that the polling frequency of each event source is tuned to how quickly your function is able to complete its task\. [BatchSize ](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateEventSourceMapping.html#SSS-CreateEventSourceMapping-request-BatchSize)controls the maximum number of records that can be sent to your function with each invoke\. A larger batch size can often more efficiently absorb the invoke overhead across a larger set of records, increasing your throughput\.
**Note**  
When there are not enough records to process, instead of waiting, the stream processing function will be invoked with a smaller number of records\.
+ **Increase Kinesis stream processing throughput by adding shards\.** A Kinesis stream is composed of one or more shards\. Lambda will poll each shard with at most one concurrent invocation\. For example, if your stream has 100 active shards, there will be at most 100 Lambda function invocations running concurrently\. Increasing the number of shards will directly increase the number of maximum concurrent Lambda function invocations and can increase your Kinesis stream processing throughput\. If you are increasing the number of shards in a Kinesis stream, make sure you have picked a good partition key \(see [Partition Keys](http://docs.aws.amazon.com/streams/latest/dev/key-concepts.html#partition-key)\) for your data, so that related records end up on the same shards and your data is well distributed\. 
+ **Use [Amazon CloudWatch](http://docs.aws.amazon.com/streams/latest/dev/monitoring-with-cloudwatch.html)** on IteratorAge to determine if your Kinesis stream is being processed\. For example, configure a CloudWatch alarm with a maximum setting to 30000 \(30 seconds\)\.

## Async Invokes<a name="async-invoke"></a>
+ **Create and use [Dead Letter Queues](dlq.md) **to address and replay async function errors\. 

## Lambda VPC<a name="lambda-vpc"></a>
+ The following diagram guides you through a decision tree as to whether you should use a VPC \(Virtual Private Cloud\):   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/VPC-flowchart4.png)
+ **Don't put your Lambda function in a VPC unless you have to\.** There is no benefit outside of using this to access resources you cannot expose publicly, like a private [Amazon Relational Database](http://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/) instance\. Services like Amazon Elasticsearch Service can be secured over IAM with access policies, so exposing the endpoint publicly is safe and wouldn't require you to run your function in the VPC to secure it\. 
+ **Lambda creates elastic network interfaces [\(ENIs\)](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_ElasticNetworkInterfaces.html)** in your VPC to access your internal resources\. Before requesting a concurrency increase, ensure you have enough ENI capacity \(the formula for this can be found here: [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)\) and IP address space\. If you do not have enough ENI capacity, you will need to request an increase\. If you do not have enough IP address space, you may need to create a larger subnet\. 
+ **Create dedicated Lambda subnets in your VPC: **
  + This will make it easier to apply a custom route table for NAT Gateway traffic without changing your other private/public subnets\. For more information, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)
  + This also allows you to dedicate an address space to Lambda without sharing it with other resources\. 