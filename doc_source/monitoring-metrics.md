# Working with Lambda function metrics<a name="monitoring-metrics"></a>

When your AWS Lambda function finishes processing an event, Lambda sends metrics about the invocation to Amazon CloudWatch\. There is no charge for these metrics\.

On the CloudWatch console, you can build graphs and dashboards with these metrics\. You can set alarms to respond to changes in utilization, performance, or error rates\. Lambda sends metric data to CloudWatch in 1\-minute intervals\. If you want more immediate insight into your Lambda function, you can create high\-resolution [custom metrics](https://docs.aws.amazon.com/lambda/latest/operatorguide/custom-metrics.html)\. Charges apply for custom metrics and CloudWatch Alarms\. For more information, see [CloudWatch pricing\.](https://aws.amazon.com/cloudwatch/pricing/)\.

This page describes the Lambda function invocation, performance, and concurrency metrics available on the CloudWatch console\.

**Topics**
+ [Viewing metrics on the CloudWatch console](#monitoring-metrics-console)
+ [Types of metrics](#monitoring-metrics-types)

## Viewing metrics on the CloudWatch console<a name="monitoring-metrics-console"></a>

You can use the CloudWatch console to filter and sort function metrics by function name, alias, or version\.

**To view metrics on the CloudWatch console**

1. Open the [Metrics page](https://console.aws.amazon.com/cloudwatch/home?region=us-east-1#metricsV2:graph=~();namespace=~'AWS*2fLambda) \(`AWS/Lambda` namespace\) of the CloudWatch console\.

1. Choose a dimension\.
   + **By Function Name** \(`FunctionName`\) – View aggregate metrics for all versions and aliases of a function\.
   + **By Resource** \(`Resource`\) – View metrics for a version or alias of a function\.
   + **By Executed Version** \(`ExecutedVersion`\) – View metrics for a combination of alias and version\. Use the `ExecutedVersion` dimension to compare error rates for two versions of a function that are both targets of a [weighted alias](configuration-aliases.md)\.
   + **Across All Functions** \(none\) – View aggregate metrics for all functions in the current AWS Region\.

1. Choose metrics to add them to the graph\.

By default, graphs use the `Sum` statistic for all metrics\. To choose a different statistic and customize the graph, use the options on the **Graphed metrics** tab\.

**Note**  
The timestamp on a metric reflects when the function was invoked\. Depending on the duration of the invocation, this can be several minutes before the metric is emitted\. For example, if your function has a 10\-minute timeout, look more than 10 minutes in the past for accurate metrics\.

For more information about CloudWatch, see the [https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)\.

## Types of metrics<a name="monitoring-metrics-types"></a>

The following section describes the types of metrics available on the CloudWatch console\.

### Using invocation metrics<a name="monitoring-metrics-invocation"></a>

Invocation metrics are binary indicators of the outcome of an invocation\. For example, if the function returns an error, Lambda sends the `Errors` metric with a value of 1\. To get a count of the number of function errors that occurred each minute, view the `Sum` of the `Errors` metric with a period of 1 minute\.

View the following metrics with the `Sum` statistic\.

**Invocation metrics**
+ `Invocations` – The number of times that your function code is invoked, including successful invocations and invocations that result in a function error\. Invocations aren't recorded if the invocation request is throttled or otherwise results in an invocation error\. This equals the number of requests billed\.
+ `Errors` – The number of invocations that result in a function error\. Function errors include exceptions that your code throws and exceptions that the Lambda runtime throws\. The runtime returns errors for issues such as timeouts and configuration errors\. To calculate the error rate, divide the value of `Errors` by the value of `Invocations`\. Note that the timestamp on an error metric reflects when the function was invoked, not when the error occurred\.
+ `DeadLetterErrors` – For [asynchronous invocation](invocation-async.md), the number of times that Lambda attempts to send an event to a dead\-letter queue but fails\. Dead\-letter errors can occur due to permissions errors, misconfigured resources, or size limits\.
+ `DestinationDeliveryFailures` – For asynchronous invocation, the number of times that Lambda attempts to send an event to a [destination](gettingstarted-features.md#gettingstarted-features-destinations) but fails\. Delivery errors can occur due to permissions errors, misconfigured resources, or size limits\.
+ `Throttles` – The number of invocation requests that are throttled\. When all function instances are processing requests and no concurrency is available to scale up, Lambda rejects additional requests with a `TooManyRequestsException` error\. Throttled requests and other invocation errors don't count as `Invocations` or `Errors`\.
+ `ProvisionedConcurrencyInvocations` – The number of times that your function code is invoked on [provisioned concurrency](configuration-concurrency.md)\.
+ `ProvisionedConcurrencySpilloverInvocations` – The number of times that your function code is invoked on standard concurrency when all provisioned concurrency is in use\.

### Using performance metrics<a name="monitoring-metrics-performance"></a>

Performance metrics provide performance details about a single invocation\. For example, the `Duration` metric indicates the amount of time in milliseconds that your function spends processing an event\. To get a sense of how fast your function processes events, view these metrics with the `Average` or `Max` statistic\.

**Performance metrics**
+ `Duration` – The amount of time that your function code spends processing an event\. The billed duration for an invocation is the value of `Duration` rounded up to the nearest millisecond\.
+ `PostRuntimeExtensionsDuration` – The cumulative amount of time that the runtime spends running code for extensions after the function code has completed\.
+ `IteratorAge` – For [event source mappings](invocation-eventsourcemapping.md) that read from streams, the age of the last record in the event\. The age is the amount of time between when a stream receives the record and when the event source mapping sends the event to the function\.
+ `OffsetLag` – For self\-managed Apache Kafka and Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) event sources, the difference in offset between the last record written to a topic and the last record that your Lambda function's consumer group processed\. Though a Kafka topic can have multiple partitions, this metric measures the offset lag at the topic level\.

`Duration` also supports [percentile statistics](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/cloudwatch_concepts.html#Percentiles)\. Use percentiles to exclude outlier values that skew average and maximum statistics\. For example, the p95 statistic shows the maximum duration of 95 percent of invocations, excluding the slowest 5 percent\.

### Using concurrency metrics<a name="monitoring-metrics-concurrency"></a>

Lambda reports concurrency metrics as an aggregate count of the number of instances processing events across a function, version, alias, or AWS Region\. To see how close you are to hitting concurrency limits, view these metrics with the `Max` statistic\.

**Concurrency metrics**
+ `ConcurrentExecutions` – The number of function instances that are processing events\. If this number reaches your [concurrent executions quota](gettingstarted-limits.md) for the Region, or the [reserved concurrency limit](configuration-concurrency.md) that you configured on the function, Lambda throttles additional invocation requests\.
+ `ProvisionedConcurrentExecutions` – The number of function instances that are processing events on [provisioned concurrency](configuration-concurrency.md)\. For each invocation of an alias or version with provisioned concurrency, Lambda emits the current count\.
+ `ProvisionedConcurrencyUtilization` – For a version or alias, the value of `ProvisionedConcurrentExecutions` divided by the total amount of provisioned concurrency allocated\. For example, `.5` indicates that 50 percent of allocated provisioned concurrency is in use\.
+ `UnreservedConcurrentExecutions` – For a Region, the number of events that functions without reserved concurrency are processing\.