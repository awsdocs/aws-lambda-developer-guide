# Monitoring and troubleshooting Lambda applications<a name="lambda-monitoring"></a>

Lambda automatically monitors Lambda functions on your behalf and reports metrics through Amazon CloudWatch\. To help you monitor your code when it runs, Lambda automatically tracks the number of requests, the invocation duration per request, and the number of requests that result in an error\. Lambda also publishes the associated CloudWatch metrics\. You can leverage these metrics to set CloudWatch custom alarms\.

The Lambda console provides a built\-in [monitoring dashboard](monitoring-functions-access-metrics.md) for each of your functions and applications\.

**To monitor a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Monitoring**\.

**Pricing**  
CloudWatch has a perpetual free tier\. Beyond the free tier threshold, CloudWatch charges for metrics, dashboards, alarms, logs, and insights\. For more information, see [Amazon CloudWatch pricing](http://aws.amazon.com/cloudwatch/pricing/)\.

Each time your function is invoked, Lambda records [metrics](monitoring-metrics.md) for the request, the function's response, and the overall state of the function\. You can use metrics to set alarms that are triggered when function performance degrades, or when you are close to hitting concurrency limits in the current AWS Region\.

To debug and validate that your code is working as expected, you can output logs with the standard logging functionality for your programming language\. The Lambda runtime uploads your function's log output to CloudWatch Logs\. You can [view logs](monitoring-cloudwatchlogs.md) in the CloudWatch Logs console, in the Lambda console, or from the command line\.

In addition to monitoring logs and metrics in CloudWatch, you can use AWS X\-Ray to trace and debug requests served by your application\. For more information, see [Using AWS Lambda with AWS X\-Ray](services-xray.md)\.

**Topics**
+ [Monitoring functions in the AWS Lambda console](monitoring-functions-access-metrics.md)
+ [Using Lambda Insights in Amazon CloudWatch](monitoring-insights.md)
+ [Working with AWS Lambda function metrics](monitoring-metrics.md)
+ [Accessing Amazon CloudWatch logs for AWS Lambda](monitoring-cloudwatchlogs.md)