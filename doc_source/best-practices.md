# Best practices for working with AWS Lambda functions<a name="best-practices"></a>

The following are recommended best practices for using AWS Lambda:

**Topics**
+ [Function code](#function-code)
+ [Function configuration](#function-configuration)
+ [Metrics and alarms](#alarming-metrics)
+ [Working with streams](#stream-events)

For more information about best practices for Lambda applications, see [Application design](https://docs.aws.amazon.com/lambda/latest/operatorguide/application-design.html) in the *Lambda operator guide*\.

## Function code<a name="function-code"></a>
+ **Separate the Lambda handler from your core logic\.** This allows you to make a more unit\-testable function\. In Node\.js this may look like: 

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
+ **Take advantage of execution environment reuse to improve the performance of your function\.** Initialize SDK clients and database connections outside of the function handler, and cache static assets locally in the `/tmp` directory\. Subsequent invocations processed by the same instance of your function can reuse these resources\. This saves cost by reducing function run time\.

  To avoid potential data leaks across invocations, don’t use the execution environment to store user data, events, or other information with security implications\. If your function relies on a mutable state that can’t be stored in memory within the handler, consider creating a separate function or separate versions of a function for each user\.
+ **Use a keep\-alive directive to maintain persistent connections\.** Lambda purges idle connections over time\. Attempting to reuse an idle connection when invoking a function will result in a connection error\. To maintain your persistent connection, use the keep\-alive directive associated with your runtime\. For an example, see [Reusing Connections with Keep\-Alive in Node\.js](https://docs.aws.amazon.com//sdk-for-javascript/v2/developer-guide/node-reusing-connections.html)\.
+ **Use [environment variables](configuration-envvars.md) to pass operational parameters to your function\.** For example, if you are writing to an Amazon S3 bucket, instead of hard\-coding the bucket name you are writing to, configure the bucket name as an environment variable\.
+ **Control the dependencies in your function's deployment package\. ** The AWS Lambda execution environment contains a number of libraries such as the AWS SDK for the Node\.js and Python runtimes \(a full list can be found here: [Lambda runtimes](lambda-runtimes.md)\)\. To enable the latest set of features and security updates, Lambda will periodically update these libraries\. These updates may introduce subtle changes to the behavior of your Lambda function\. To have full control of the dependencies your function uses, package all of your dependencies with your deployment package\. 
+ **Minimize your deployment package size to its runtime necessities\. ** This will reduce the amount of time that it takes for your deployment package to be downloaded and unpacked ahead of invocation\. For functions authored in Java or \.NET Core, avoid uploading the entire AWS SDK library as part of your deployment package\. Instead, selectively depend on the modules which pick up components of the SDK you need \(e\.g\. DynamoDB, Amazon S3 SDK modules and [Lambda core libraries](https://github.com/aws/aws-lambda-java-libs)\)\. 
+ **Reduce the time it takes Lambda to unpack deployment packages** authored in Java by putting your dependency `.jar` files in a separate /lib directory\. This is faster than putting all your function’s code in a single jar with a large number of `.class` files\. See [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md) for instructions\.
+ **Minimize the complexity of your dependencies\.** Prefer simpler frameworks that load quickly on [execution environment](lambda-runtime-environment.md) startup\. For example, prefer simpler Java dependency injection \(IoC\) frameworks like [Dagger](https://google.github.io/dagger/) or [Guice](https://github.com/google/guice), over more complex ones like [Spring Framework](https://github.com/spring-projects/spring-framework)\. 
+ **Avoid using recursive code** in your Lambda function, wherein the function automatically calls itself until some arbitrary criteria is met\. This could lead to unintended volume of function invocations and escalated costs\. If you do accidentally do so, set the function reserved concurrency to `0` immediately to throttle all invocations to the function, while you update the code\.
+ **Do not use non\-documented, non\-public APIs** in your Lambda function code\. For AWS Lambda managed runtimes, Lambda periodically applies security and functional updates to Lambda's internal APIs\. These internal API updates may be backwards\-incompatible, leading to unintended consequences such as invocation failures if your function has a dependency on these non\-public APIs\. See [the API reference](API_Operations.md) for a list of publicly available APIs\.
+ **Write idempotent code\.** Writing idempotent code for your functions ensures that duplicate events are handled the same way\. Your code should properly validate events and gracefully handle duplicate events\. For more information, see [How do I make my Lambda function idempotent?](https://aws.amazon.com/premiumsupport/knowledge-center/lambda-function-idempotent/)\.

## Function configuration<a name="function-configuration"></a>
+ **Performance testing your Lambda function** is a crucial part in ensuring you pick the optimum memory size configuration\. Any increase in memory size triggers an equivalent increase in CPU available to your function\. The memory usage for your function is determined per\-invoke and can be viewed in [Amazon CloudWatch](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/WhatIsCloudWatchLogs.html)\. On each invoke a `REPORT:` entry will be made, as shown below: 

  ```
  REPORT RequestId: 3604209a-e9a3-11e6-939a-754dd98c7be3	Duration: 12.34 ms	Billed Duration: 100 ms Memory Size: 128 MB	Max Memory Used: 18 MB
  ```

  By analyzing the `Max Memory Used:` field, you can determine if your function needs more memory or if you over\-provisioned your function's memory size\. 

  **To find the right memory configuration** for your functions, we recommend using the open source AWS Lambda Power Tuning project\. For more information, see [AWS Lambda Power Tuning](https://github.com/alexcasalboni/aws-lambda-power-tuning) on GitHub\. 

  To optimize function performance, we also recommend deploying libraries that can leverage [Advanced Vector Extensions 2 \(AVX2\)](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-avx2.html)\. This allows you to process demanding workloads, including machine learning inferencing, media processing, high performance computing \(HPC\), scientific simulations, and financial modeling\. For more information, see [ Creating faster AWS Lambda functions with AVX2](http://aws.amazon.com/blogs/compute/creating-faster-aws-lambda-functions-with-avx2/)\.
+ **Load test your Lambda function** to determine an optimum timeout value\. It is important to analyze how long your function runs so that you can better determine any problems with a dependency service that may increase the concurrency of the function beyond what you expect\. This is especially important when your Lambda function makes network calls to resources that may not handle Lambda's scaling\. 
+ **Use most\-restrictive permissions when setting IAM policies\.** Understand the resources and operations your Lambda function needs, and limit the execution role to these permissions\. For more information, see [Lambda permissions](lambda-permissions.md)\. 
+ **Be familiar with [Lambda quotas](gettingstarted-limits.md)\.** Payload size, file descriptors and /tmp space are often overlooked when determining runtime resource limits\. 
+ **Delete Lambda functions that you are no longer using\.** By doing so, the unused functions won't needlessly count against your deployment package size limit\.
+ **If you are using Amazon Simple Queue Service** as an event source, make sure the value of the function's expected invocation time does not exceed the [Visibility Timeout](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html) value on the queue\. This applies both to [CreateFunction](API_CreateFunction.md) and [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\.
  + In the case of **CreateFunction**, AWS Lambda will fail the function creation process\.
  + In the case of **UpdateFunctionConfiguration**, it could result in duplicate invocations of the function\.

## Metrics and alarms<a name="alarming-metrics"></a>
+ **Use [Working with Lambda function metrics](monitoring-metrics.md) and [ CloudWatch Alarms](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html)** instead of creating or updating a metric from within your Lambda function code\. It's a much more efficient way to track the health of your Lambda functions, allowing you to catch issues early in the development process\. For instance, you can configure an alarm based on the expected duration of your Lambda function invocation in order to address any bottlenecks or latencies attributable to your function code\.
+ **Leverage your logging library and [AWS Lambda Metrics and Dimensions](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/lam-metricscollected.html)** to catch app errors \(e\.g\. ERR, ERROR, WARNING, etc\.\) 
+ **Use [AWS Cost Anomaly Detection](https://docs.aws.amazon.com/cost-management/latest/userguide/manage-ad.html)** to detect unusual activity on your account\. Cost Anomaly Detection uses machine learning to continuously monitor your cost and usage while minimizing false positive alerts\. Cost Anomaly Detection uses data from AWS Cost Explorer, which has a delay of up to 24 hours\. As a result, it can take up to 24 hours to detect an anomaly after usage occurs\. To get started with Cost Anomaly Detection, you must first [ sign up for Cost Explorer](https://docs.aws.amazon.com/cost-management/latest/userguide/ce-enable.html)\. Then, [access Cost Anomaly Detection](https://docs.aws.amazon.com/cost-management/latest/userguide/settingup-ad.html#access-ad)\.

## Working with streams<a name="stream-events"></a>
+ **Test with different batch and record sizes **so that the polling frequency of each event source is tuned to how quickly your function is able to complete its task\. The [CreateEventSourceMapping](API_CreateEventSourceMapping.md) BatchSize parameter controls the maximum number of records that can be sent to your function with each invoke\. A larger batch size can often more efficiently absorb the invoke overhead across a larger set of records, increasing your throughput\.

  By default, Lambda invokes your function as soon as records are available\. If the batch that Lambda reads from the event source has only one record in it, Lambda sends only one record to the function\. To avoid invoking the function with a small number of records, you can tell the event source to buffer records for up to 5 minutes by configuring a *batching window*\. Before invoking the function, Lambda continues to read records from the event source until it has gathered a full batch, the batching window expires, or the batch reaches the payload limit of 6 MB\. For more information, see [Batching behavior](invocation-eventsourcemapping.md#invocation-eventsourcemapping-batching)\.
+ **Increase Kinesis stream processing throughput by adding shards\.** A Kinesis stream is composed of one or more shards\. Lambda will poll each shard with at most one concurrent invocation\. For example, if your stream has 100 active shards, there will be at most 100 Lambda function invocations running concurrently\. Increasing the number of shards will directly increase the number of maximum concurrent Lambda function invocations and can increase your Kinesis stream processing throughput\. If you are increasing the number of shards in a Kinesis stream, make sure you have picked a good partition key \(see [Partition Keys](https://docs.aws.amazon.com/streams/latest/dev/key-concepts.html#partition-key)\) for your data, so that related records end up on the same shards and your data is well distributed\. 
+ **Use [Amazon CloudWatch](https://docs.aws.amazon.com/streams/latest/dev/monitoring-with-cloudwatch.html)** on IteratorAge to determine if your Kinesis stream is being processed\. For example, configure a CloudWatch alarm with a maximum setting to 30000 \(30 seconds\)\.