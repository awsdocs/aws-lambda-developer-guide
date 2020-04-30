# Monitoring and troubleshooting Lambda applications<a name="lambda-monitoring"></a>

AWS Lambda automatically monitors Lambda functions on your behalf and reports metrics through Amazon CloudWatch\. To help you monitor your code as it executes, Lambda automatically tracks the number of requests, the execution duration per request, and the number of requests that result in an error\. It also publishes the associated CloudWatch metrics\. You can leverage these metrics to set CloudWatch custom alarms\. For more information about CloudWatch, see the *[Amazon CloudWatch User Guide](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)*\.

You can view request rates and error rates for each of your Lambda functions by using the AWS Lambda console, the CloudWatch console, and other AWS resources\. The following topics describe Lambda CloudWatch metrics and how to access them\.
+ [Monitoring functions in the AWS Lambda console](monitoring-functions-access-metrics.md)
+ [Working with AWS Lambda function metrics](monitoring-metrics.md)

You can insert logging statements into your code to help you validate that your code is working as expected\. Lambda automatically integrates with Amazon CloudWatch Logs\. It pushes all logs from your code to a CloudWatch Logs group that is associated with a Lambda function \(/aws/lambda/*<function name>*\)\. To learn more about log groups and accessing them through the CloudWatch console, see [Working with log groups and log streams](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Working-with-log-groups-and-streams.html) in the *Amazon CloudWatch Logs User Guide*\. For information about how to access CloudWatch log entries, see [Accessing Amazon CloudWatch logs for AWS Lambda](monitoring-cloudwatchlogs.md)\.

**Note**  
If your Lambda function code is executing, but you don't see any log data being generated after several minutes, this could mean that your execution role for the Lambda function didn't grant permissions to write log data to CloudWatch Logs\. For information about how to make sure that you have set up the execution role correctly to grant these permissions, see [AWS Lambda execution role](lambda-intro-execution-role.md)\.

Each of the monitoring services provides a free tier\. If your application exceeds the free tier limits, pricing is based on usage\. For more information, see [ CloudWatch pricing](https://aws.amazon.com/cloudwatch/pricing/) and [X\-Ray pricing](https://aws.amazon.com/xray/pricing/)\. Also note that standard storage rates apply for CloudWatch logs that are stored by the Lambda service\. 

The monitoring services follow these usage models:
+  AWS Lambda reports metrics and logs to CloudWatch every time your Lambda function executes\. 
+ CloudWatch Logs Insights runs when you view the Monitoring page of your Lambda function\.
+ X\-Ray starts to record traces when you enable X\-Ray for a function, or when an upstream service enables X\-Ray\. 