# AWS Lambda Metrics<a name="monitoring-functions-metrics"></a>

AWS Lambda monitors functions and sends metrics data to Amazon CloudWatch\. These metrics are used to create graphs that appear on the [monitoring pages](monitoring-functions-access-metrics.md) of the Lambda console\. You can use them to monitor and create alarms for concurrency, error rates, and performance\.

**To access metrics using the CloudWatch console**

1. Open the CloudWatch console at [https://console\.aws\.amazon\.com/cloudwatch/](https://console.aws.amazon.com/cloudwatch/)\.

1. In the navigation pane, choose **Metrics**\.

1. In the **CloudWatch Metrics by Category** pane, choose **Lambda Metrics**\.

For more information about CloudWatch, see the [https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)\.

## AWS Lambda CloudWatch Metrics<a name="lambda-cloudwatch-metrics"></a>

Lambda emits metrics in the `AWS/Lambda` namespace\. When an invocation completes, Lambda sends a set of metrics for that invocation\. For some metrics, like `Invocations`, Lambda increments the metric by one\. View these as a sum to get the total number emitted for a function or across all functions\. Other metrics reflect the current state of a resource, like `IteratorAge` or `ProvisionedConcurrentExecutions`\. These are better viewed as an average or max value for a time period\.

The timestamp on a metric reflects when the function was invoked\. Depending on the duration of the execution, this can be several minutes prior to when the metric is emitted\. If, for example, your function has a 10 minute timeout, look more than 10 minutes in the past for accurate metrics\.


| Metric | Description | 
| --- | --- | 
|  Invocations |  Measures the number of times a function is invoked in response to an event or invocation API call\. This includes successful and failed invocations, but does not include throttled attempts\. This equals the billed requests for the function\.  Units: Count \(sum\)  | 
| Errors |  Measures the number of invocations that failed due to errors in the function \(response code 4XX\)\. Failed invocations may trigger a retry attempt that succeeds\. This includes: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/monitoring-functions-metrics.html) This does not include invocations that fail due to invocation rates exceeding concurrency limits \(error code 429\) or service errors \(error code 500\)\. Units: Count \(sum\)  | 
| DeadLetterErrors |  Incremented when Lambda is unable to write the failed event payload to your function's dead\-letter queue\. This could be due to the following: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/monitoring-functions-metrics.html) Units: Count \(sum\)  | 
| Duration |  Measures the elapsed wall clock time from when the function code starts executing as a result of an invocation to when it stops executing\. The maximum data point value possible is the function timeout configuration\. The billed duration will be rounded up to the nearest 100 millisecond\. The Duration metric supports [Percentiles](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/cloudwatch_concepts.html#Percentiles)\.  Units: Milliseconds \(max\)  | 
|  Throttles |  Measures the number of Lambda function invocation attempts that were throttled due to invocation rates exceeding the customer’s concurrent limits \(error code 429\)\. Failed invocations may trigger a retry attempt that succeeds\. Units: Count \(sum\)  | 
|  IteratorAge |  Emitted for stream\-based invocations only \(functions triggered by an Amazon DynamoDB stream or Kinesis stream\)\. Measures the age of the last record for each batch of records processed\. Age is the difference between the time Lambda received the batch, and the time the last record in the batch was written to the stream\. Units: Milliseconds \(max\)  | 
| ConcurrentExecutions |  Emitted as an aggregate metric for all functions in the account, and for functions that have a custom concurrency limit specified\. Not applicable for versions or aliases\. Measures the sum of concurrent executions for a given function at a given point in time\. Units: Count \(max\)  | 
| UnreservedConcurrentExecutions |  Emitted as an aggregate metric for all functions in the account only\. Not applicable for functions, versions, or aliases\. Represents the sum of the concurrency of the functions that do not have a custom concurrency limit specified\. Must be viewed as an average metric if aggregated across a time period\.  Units: Count \(max\) | 
| ProvisionedConcurrentExecutions |  The number of events that are being processed on [provisioned concurrency](configuration-concurrency.md)\. For each invocation of an alias or version with provisioned concurrency, Lambda emits the current count\. Units: Count \(max\) | 
| ProvisionedConcurrencyInvocations |  The number of invocations that are run on provisioned concurrency\. Lambda increments the count once for each invocation that runs on provisioned concurrency\. Units: Count \(sum\)  | 
| ProvisionedConcurrencySpilloverInvocations |  The number of invocations that are run on standard concurrency, when all provisioned concurrency is in use\. For a version or alias that is configured to use provisioned concurrency, Lambda increments the count once for each invocation that runs on non\-provisioned concurrency\. Units: Count \(sum\)  | 
| ProvisionedConcurrencyUtilization |  The number of events that are being processed on provisioned concurrency, divided by the total amount of provisioned concurrency allocated\. For example, `.5` indicates that 50 percent of allocated provisioned concurrency is in use\. For each invocation of an alias or version with provisioned concurrency, Lambda emits the current count\. Units: Count \(max\) | 

**Errors/Invocations Ratio**  
When calculating the error rate on Lambda function invocations, it’s important to distinguish between an invocation request and an actual invocation\. It is possible for the error rate to exceed the number of billed Lambda function invocations\. Lambda reports an invocation metric only if the Lambda function code is executed\. If the invocation request yields a throttling or other initialization error that prevents the Lambda function code from being invoked, Lambda will report an error, but it does not log an invocation metric\.  
Lambda emits `Invocations=1` when the function is executed\. If the Lambda function is not executed, nothing is emitted\.
Lambda emits a data point for `Errors` for each invoke request\. `Errors=0` means that there is no function execution error\. `Errors=1` means that there is a function execution error\.
Lambda emits a data point for `Throttles` for each invoke request\. `Throttles=0` means there is no invocation throttle\. `Throttles=1` means there is an invocation throttle\.

## AWS Lambda CloudWatch Dimensions<a name="lambda-cloudwatch-dimensions"></a>

You can use the dimensions in the following table to refine the metrics returned for your Lambda functions\. 


| Dimension | Description | 
| --- | --- | 
| FunctionName |  Filters the metric data by Lambda function\.  | 
| Resource |  Filters the metric data by Lambda function resource, such as function version or alias\.  | 
| ExecutedVersion |  Filters the metric data by Lambda function versions\. This only applies to alias invocations\.  | 