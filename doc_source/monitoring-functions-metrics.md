# AWS Lambda Metrics<a name="monitoring-functions-metrics"></a>

This topic describes the AWS Lambda namespace, metrics, and dimensions\. AWS Lambda automatically monitors functions on your behalf, reporting metrics through Amazon CloudWatch\. These metrics include total invocations, errors, duration, throttles, DLQ errors and Iterator age for stream\-based invocations\. 

CloudWatch is basically a metrics repository\. A metric is the fundamental concept in CloudWatch and represents a time\-ordered set of data points\. You \(or AWS services\) publish metrics data points into CloudWatch and you retrieve statistics about those data points as an ordered set of time\-series data\.

Metrics are uniquely defined by a name, a namespace, and one or more dimensions\. Each data point has a time stamp, and, optionally, a unit of measure\. When you request statistics, the returned data stream is identified by namespace, metric name, and dimension\. For more information about CloudWatch, see the [https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)\. 

## AWS Lambda CloudWatch Metrics<a name="lambda-cloudwatch-metrics"></a>

The `AWS/Lambda` namespace includes the following metrics\.


| Metric | Description | 
| --- | --- | 
|  Invocations |  Measures the number of times a function is invoked in response to an event or invocation API call\. This replaces the deprecated RequestCount metric\. This includes successful and failed invocations, but does not include throttled attempts\. This equals the billed requests for the function\. Note that AWS Lambda only sends these metrics to CloudWatch if they have a nonzero value\. Units: Count  | 
| Errors |  Measures the number of invocations that failed due to errors in the function \(response code 4XX\)\. This replaces the deprecated ErrorCount metric\. Failed invocations may trigger a retry attempt that succeeds\. This includes: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/monitoring-functions-metrics.html) This does not include invocations that fail due to invocation rates exceeding default concurrent limits \(error code 429\) or failures due to internal service errors \(error code 500\)\. Units: Count  | 
| DeadLetterErrors |  Incremented when Lambda is unable to write the failed event payload to your configured Dead Letter Queues\. This could be due to the following: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/monitoring-functions-metrics.html) Units: Count  | 
| Duration |  Measures the elapsed wall clock time from when the function code starts executing as a result of an invocation to when it stops executing\. The maximum data point value possible is the function timeout configuration\. The billed duration will be rounded up to the nearest 100 millisecond\. Note that AWS Lambda only sends these metrics to CloudWatch if they have a nonzero value\. Units: Milliseconds  | 
|  Throttles  |  Measures the number of Lambda function invocation attempts that were throttled due to invocation rates exceeding the customer’s concurrent limits \(error code 429\)\. Failed invocations may trigger a retry attempt that succeeds\.  Units: Count  | 
|  IteratorAge |  Emitted for stream\-based invocations only \(functions triggered by an Amazon DynamoDB stream or Kinesis stream\)\. Measures the age of the last record for each batch of records processed\. Age is the difference between the time Lambda received the batch, and the time the last record in the batch was written to the stream\. Units: Milliseconds  | 
| ConcurrentExecutions  |  Emitted as an aggregate metric for all functions in the account, and for functions that have a custom concurrency limit specified\. Not applicable for versions or aliases\. Measures the sum of concurrent executions for a given function at a given point in time\. Must be viewed as an average metric if aggregated across a time period\.  Units: Count  | 
| UnreservedConcurrentExecutions |  Emitted as an aggregate metric for all functions in the account only\. Not applicable for functions, versions, or aliases\. Represents the sum of the concurrency of the functions that do not have a custom concurrency limit specified\. Must be viewed as an average metric if aggregated across a time period\.  Units: Count | 

**To access metrics using the CloudWatch console**

1. Open the CloudWatch console at [https://console\.aws\.amazon\.com/cloudwatch/](https://console.aws.amazon.com/cloudwatch/)\.

1. In the navigation pane, choose **Metrics**\.

1. In the **CloudWatch Metrics by Category** pane, choose **Lambda Metrics**\.

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