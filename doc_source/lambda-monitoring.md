# Monitoring and troubleshooting Lambda applications<a name="lambda-monitoring"></a>

AWS Lambda integrates with other AWS services to help you monitor and troubleshoot your Lambda functions\. Lambda automatically monitors Lambda functions on your behalf and reports metrics through Amazon CloudWatch\. To help you monitor your code when it runs, Lambda automatically tracks the number of requests, the invocation duration per request, and the number of requests that result in an error\. 

You can use other AWS services to troubleshoot your Lambda functions\. This section describes how to use these AWS services to monitor, trace, debug, and troubleshoot your Lambda functions and applications\.

For more information about monitoring Lambda applications, see [Monitoring and observability](https://docs.aws.amazon.com/lambda/latest/operatorguide/monitoring-observability.html) in the *Lambda operator guide*\.

**Topics**
+ [Monitoring functions on the Lambda console](monitoring-functions-access-metrics.md)
+ [Using Lambda Insights in Amazon CloudWatch](monitoring-insights.md)
+ [Working with Lambda function metrics](monitoring-metrics.md)
+ [Accessing Amazon CloudWatch logs for AWS Lambda](monitoring-cloudwatchlogs.md)
+ [Using CodeGuru Profiler with your Lambda function](monitoring-code-profiler.md)
+ [Example workflows using other AWS services](monitoring-servicemap.md)