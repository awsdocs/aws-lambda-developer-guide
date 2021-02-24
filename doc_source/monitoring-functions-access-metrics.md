# Monitoring functions in the AWS Lambda console<a name="monitoring-functions-access-metrics"></a>

AWS Lambda monitors functions on your behalf and sends metrics to Amazon CloudWatch\. The Lambda console creates monitoring graphs for these metrics and shows them on the **Monitoring** page for each Lambda function\. 

This page describes the basics of using the Lambda console to view function metrics, including total requests, duration, and error rates\.

## Pricing<a name="monitoring-console-metrics-pricing"></a>

CloudWatch has a perpetual free tier\. Beyond the free tier threshold, CloudWatch charges for metrics, dashboards, alarms, logs, and insights\. For more information, see [Amazon CloudWatch pricing](http://aws.amazon.com/cloudwatch/pricing/)\.

## Using the Lambda console<a name="monitoring-console-metrics"></a>

You can use the Lambda console's monitoring dashboard to monitor your Lambda functions and applications\.

**To monitor a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Monitoring**\.

## Types of monitoring graphs<a name="monitoring-console-graph-types"></a>

The following section describes the monitoring graphs on the Lambda console\.

**Lambda monitoring graphs**
+ **Invocations** – The number of times that the function was invoked in each 5\-minute period\.
+ **Duration** – The average, minimum, and maximum execution times\.
+ **Error count and success rate \(%\)** – The number of errors and the percentage of executions that completed without error\.
+ **Throttles** – The number of times that execution failed due to concurrency limits\.
+ **IteratorAge** – For stream event sources, the age of the last item in the batch when Lambda received it and invoked the function\.
+ **Async delivery failures** – The number of errors that occurred when Lambda attempted to write to a destination or dead\-letter queue\.
+ **Concurrent executions** – The number of function instances that are processing events\.

## Viewing graphs on the Lambda console<a name="monitoring-console-graph-types-console"></a>

The following section describes how to view CloudWatch monitoring graphs on the Lambda console, and open the CloudWatch metrics dashboard\.

**To view monitoring graphs for a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Monitoring**\.

1. Choose from the predefined time ranges, or choose a custom time range\.

1. To see the definition of a graph in CloudWatch, choose the three vertical dots, and then choose **View in metrics** to open the **Metrics** dashboard in CloudWatch\.

![\[An example monitoring definition on the Lambda console.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-monitoring-definition.png)

## Viewing queries in the CloudWatch Logs console<a name="monitoring-console-queries"></a>

The following section describes how to view and add reports from CloudWatch Logs Insights to a custom dashboard on the CloudWatch Logs console\. 

**To view reports for a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Monitoring**\.

1. Choose **View logs in CloudWatch**\.

1. Choose **View in Logs Insights**\.

1. Choose from the predefined time ranges, or choose a custom time range\.

1. Choose **Run query**\.

1. \(Optional\) Choose **Save**\.

![\[The AWS CloudWatch Logs Insights reports on the CloudWatch dashboard.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-monitoring-insights.png)

## What's next?<a name="monitoring-console-next-up"></a>
+ Learn about the metrics that Lambda records and sends to CloudWatch in [Working with AWS Lambda function metrics](monitoring-metrics.md)\.
+ Learn how to use Lambda Insights to collect and aggregate Lambda function runtime performance metrics and logs in [Using Lambda Insights in Amazon CloudWatch](monitoring-insights.md)\.