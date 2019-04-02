# Accessing Amazon CloudWatch Metrics for AWS Lambda<a name="monitoring-functions-access-metrics"></a>

AWS Lambda automatically monitors functions on your behalf, reporting metrics through Amazon CloudWatch\. These metrics include total requests, duration, and error rates\. 

**To access metrics using the Lambda console**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose **Monitoring**\.  
![\[Graphs\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)![\[Graphs\]](http://docs.aws.amazon.com/lambda/latest/dg/)![\[Graphs\]](http://docs.aws.amazon.com/lambda/latest/dg/)

The console provides the following graphs\.

**Lambda Monitoring Graphs**
+ **Invocations** – The number of times the function was invoked in each 5 minute period\.
+ **Duration** – Average, minimum, and maximum execution times\.
+ **Error count and success rate \(%\)** – The number of errors, and the percentage of executions that completed without error\.
+ **Throttles** – The number of times execution failed due to concurrency limits\.
+ **IteratorAge** – For stream event sources, the age of the last item in the batch when Lambda receives it and invokes the function\.
+ **DeadLetterErrors** – The number of events that Lambda attempted to write to a dead letter queue, but failed\.

To see the definition of a graph in CloudWatch, choose **View in metrics** from the menu in the top right of the graph\. For more information about the metrics that Lambda records, see [AWS Lambda Metrics](monitoring-functions-metrics.md)\.