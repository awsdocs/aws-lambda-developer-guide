# Accessing Amazon CloudWatch Metrics for AWS Lambda<a name="monitoring-functions-access-metrics"></a>

AWS Lambda automatically monitors functions on your behalf, reporting metrics through Amazon CloudWatch\. These metrics include total requests, latency, and error rates\. For more information about Lambda metrics, see [AWS Lambda Metrics](monitoring-functions-metrics.md)\. For more information about CloudWatch, see the [Amazon CloudWatch User Guide](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)\. 

You can monitor metrics for Lambda and view logs by using the Lambda console, the CloudWatch console, the AWS CLI, or the CloudWatch API\. The following procedures show you how to access metrics using these different methods\.

**To access metrics using the Lambda console**

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. On the **Functions ** page, choose the function name and then choose the **Monitoring** tab\.  
![\[Events\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)![\[Events\]](http://docs.aws.amazon.com/lambda/latest/dg/)![\[Events\]](http://docs.aws.amazon.com/lambda/latest/dg/)

   A graphical representation of the metrics for the Lambda function are shown\.

1. Choose **Jump to logs** to view the logs\.

**To access metrics using the CloudWatch console**

1. Open the CloudWatch console at [https://console\.aws\.amazon\.com/cloudwatch/](https://console.aws.amazon.com/cloudwatch/)\.

1. From the navigation bar, choose a region\.

1. In the navigation pane, choose **Metrics**\.

1. In the **CloudWatch Metrics by Category** pane, choose **Lambda Metrics**\.

1. \(Optional\) In the graph pane, choose a statistic and a time period, and then create a CloudWatch alarm using these settings\.

**To access metrics using the AWS CLI**  
Use the [http://docs.aws.amazon.com/cli/latest/reference/cloudwatch/list-metrics.html](http://docs.aws.amazon.com/cli/latest/reference/cloudwatch/list-metrics.html) and [http://docs.aws.amazon.com/cli/latest/reference/cloudwatch/get-metric-statistics.html](http://docs.aws.amazon.com/cli/latest/reference/cloudwatch/get-metric-statistics.html) commands\.

**To access metrics using the CloudWatch CLI**  
Use the [http://docs.aws.amazon.com/AmazonCloudWatch/latest/cli/cli-mon-list-metrics.html](http://docs.aws.amazon.com/AmazonCloudWatch/latest/cli/cli-mon-list-metrics.html) and [http://docs.aws.amazon.com/AmazonCloudWatch/latest/cli/cli-mon-get-stats.html](http://docs.aws.amazon.com/AmazonCloudWatch/latest/cli/cli-mon-get-stats.html) commands\.

**To access metrics using the CloudWatch API**  
Use the [http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_ListMetrics.html](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_ListMetrics.html) and [http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html) operations\.