# Monitoring and Troubleshooting Lambda Applications<a name="troubleshooting"></a>

AWS Lambda automatically monitors Lambda functions on your behalf, reporting metrics through Amazon CloudWatch\. To help you monitor your code as it executes, Lambda automatically tracks the number of requests, the execution duration per request, and the number of requests resulting in an error and publishes the associated CloudWatch metrics\. You can leverage these metrics to set CloudWatch custom alarms\. For more information about CloudWatch, see the [Amazon CloudWatch User Guide](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)\.

You can view request rates and error rates for each of your Lambda functions by using the AWS Lambda console, the CloudWatch console, and other Amazon Web Services \(AWS\) resources\. The following topics describe Lambda CloudWatch metrics and how to access them\.
+ [Monitoring Functions in the AWS Lambda Console](monitoring-functions-access-metrics.md)
+ [AWS Lambda Metrics](monitoring-functions-metrics.md)

You can insert logging statements into your code to help you validate that your code is working as expected\. Lambda automatically integrates with Amazon CloudWatch Logs and pushes all logs from your code to a CloudWatch Logs group associated with a Lambda function \(/aws/lambda/*<function name>*\)\. To learn more about log groups and accessing them through the CloudWatch console, see the [Monitoring System, Application, and Custom Log Files](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/WhatIsCloudWatchLogs.html) in the *Amazon CloudWatch User Guide*\. For information about how to access CloudWatch log entries, see [Accessing Amazon CloudWatch Logs for AWS Lambda](monitoring-functions-logs.md)\.

**Note**  
If your Lambda function code is executing, but you don't see any log data being generated after several minutes, this could mean your execution role for the Lambda function did not grant permissions to write log data to CloudWatch Logs\. For information about how to make sure that you have set up the execution role correctly to grant these permissions, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.

**Topics**
+ [Monitoring Functions in the AWS Lambda Console](monitoring-functions-access-metrics.md)
+ [AWS Lambda Metrics](monitoring-functions-metrics.md)
+ [Accessing Amazon CloudWatch Logs for AWS Lambda](monitoring-functions-logs.md)
+ [Using AWS X\-Ray](lambda-x-ray.md)
+ [Logging AWS Lambda API Calls with AWS CloudTrail](logging-using-cloudtrail.md)
+ [AWS Lambda Troubleshooting Scenarios](monitoring-functions-troubleshooting.md)