# Monitoring functions in the AWS Lambda console<a name="monitoring-functions-access-metrics"></a>

AWS Lambda monitors functions on your behalf and sends metrics to Amazon CloudWatch\. The metrics include total requests, duration, and error rates\. The Lambda console creates graphs for these metrics and shows them on the **Monitoring** page for each function\.

**To access the monitoring console**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose **Monitoring**\.  
![\[Graphs of various CloudWatch metrics.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

The console provides the following graphs\.

**Lambda monitoring graphs**
+ **Invocations** – The number of times that the function was invoked in each 5\-minute period\.
+ **Duration** – The average, minimum, and maximum execution times\.
+ **Error count and success rate \(%\)** – The number of errors and the percentage of executions that completed without error\.
+ **Throttles** – The number of times that execution failed due to concurrency limits\.
+ **IteratorAge** – For stream event sources, the age of the last item in the batch when Lambda received it and invoked the function\.
+ **Async delivery failures** – The number of errors that occurred when Lambda attempted to write to a destination or dead\-letter queue\.
+ **Concurrent executions** – The number of function instances that are processing events\.

To see the definition of a graph in CloudWatch, choose **View in metrics** from the menu in the top right of the graph\. For more information about the metrics that Lambda records, see [Working with AWS Lambda function metrics](monitoring-metrics.md)\.

The console also shows reports from CloudWatch Logs Insights that are compiled from information in your function's logs\. You can add these reports to a custom dashboard in the CloudWatch Logs console\. Use the queries as a starting point for your own reports\.

![\[AWS CloudWatch Logs Insights reports\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-monitoring-insights.png)

To view a query, choose **View in CloudWatch Logs Insights** from the menu in the top right of the report\.