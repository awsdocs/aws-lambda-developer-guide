# Monitoring Lambda function URLs<a name="urls-monitoring"></a>

You can use AWS CloudTrail and Amazon CloudWatch to monitor your function URLs\.

**Topics**
+ [Monitoring function URLs with CloudTrail](#urls-cloudtrail)
+ [CloudWatch metrics for function URLs](#urls-cloudwatch)

## Monitoring function URLs with CloudTrail<a name="urls-cloudtrail"></a>

For function URLs, Lambda automatically supports logging the following API operations as events in CloudTrail log files:
+ [CreateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateFunctionUrlConfig.html)
+ [UpdateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateFunctionUrlConfig.html)
+ [DeleteFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteFunctionUrlConfig.html)
+ [GetFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionUrlConfig.html)
+ [ListFunctionUrlConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListFunctionUrlConfigs.html)

Each log entry contains information about the caller identity, when the request was made, and other details\. You can see all events within the last 90 days by viewing your CloudTrail **Event history**\. To retain records past 90 days, you can create a trail\. For more information, see [Using AWS Lambda with AWS CloudTrail](with-cloudtrail.md)\.

By default, CloudTrail doesn't log `InvokeFunctionUrl` requests, which are considered data events\. However, you can turn on data event logging in CloudTrail\. For more information, see [Logging data events for trails](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-data-events-with-cloudtrail.html) in the *AWS CloudTrail User Guide*\.

## CloudWatch metrics for function URLs<a name="urls-cloudwatch"></a>

Lambda sends aggregated metrics about function URL requests to CloudWatch\. With these metrics, you can monitor your function URLs, build dashboards, and configure alarms in the CloudWatch console\.

Function URLs support the following invocation metrics\. We recommend viewing these metrics with the `Sum` statistic\.
+ `UrlRequestCount` – The number of requests made to this function URL\.
+ `Url4xxError` – The number of requests that returned a 4XX HTTP status code\. 4XX series codes indicate client\-side errors, such as bad requests\.
+ `Url5xxError` – The number of requests that returned a 5XX HTTP status code\. 5XX series codes indicate server\-side errors, such as function errors and timeouts\.

Function URLs also support the following performance metric\. We recommend viewing this metric with the `Average` or `Max` statistics\.
+ `UrlRequestLatency` – The time between when the function URL receives a request and when the function URL returns a response\.

Each of these invocation and performance metrics supports the following dimensions:
+ `FunctionName` – View aggregate metrics for function URLs assigned to a function's `$LATEST` unpublished version, or to any of the function's aliases\. For example, `hello-world-function`\.
+ `Resource` – View metrics for a specific function URL\. This is defined by a function name, along with either the function's `$LATEST` unpublished version or one of the function's aliases\. For example, `hello-world-function:$LATEST`\.
+ `ExecutedVersion` – View metrics for a specific function URL based on the executed version\. You can use this dimension primarily to track the function URL assigned to the `$LATEST` unpublished version\.