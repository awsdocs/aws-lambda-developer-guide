# Example workflows using other AWS services<a name="monitoring-servicemap"></a>

AWS Lambda integrates with other AWS services to help you monitor, trace, debug, and troubleshoot your Lambda functions\. This page shows workflows you can use for AWS X\-Ray and CloudWatch ServiceLens to trace and troubleshoot your Lambda functions\.

**Topics**
+ [Prerequisites](#monitoring-troubleshooting-prereqs)
+ [Pricing](#monitoring-troubleshooting-pricing)
+ [Example AWS X\-Ray workflow to view a service map](#monitoring-servicemap-example)
+ [Example AWS X\-Ray workflow to view trace details](#monitoring-tracing-example)
+ [What's next?](#monitoring-troubleshooting-next-up)

## Prerequisites<a name="monitoring-troubleshooting-prereqs"></a>

### Using AWS X\-Ray<a name="monitoring-troubleshooting-prereqs-xray"></a>

AWS X\-Ray needs to be enabled on the Lambda console to complete the AWS X\-Ray workflows on this page\. If your execution role does not have the required permissions, the Lambda console will attempt to add them to your execution role\.

**To enable AWS X\-Ray on the Lambda console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose your function\.

1. Choose the **Configuration** tab\. 

1. On the **Monitoring tools** pane, choose **Edit**\.

1. Under **AWS X\-Ray**, turn on **Active tracing**\.

1. Choose **Save**\.

## Pricing<a name="monitoring-troubleshooting-pricing"></a>
+ With AWS X\-Ray you pay only for what you use, based on the number of traces recorded, retrieved, and scanned\. For more information, see [AWS X\-Ray Pricing](http://aws.amazon.com/xray/pricing/)\.

## Example AWS X\-Ray workflow to view a service map<a name="monitoring-servicemap-example"></a>

If you've enabled AWS X\-Ray, you can view a ServiceLens service map on the CloudWatch console\. A service map displays your service endpoints and resources as nodes and highlights the traffic, latency, and errors for each node and its connections\. 

You can choose a node to see detailed insights about the correlated metrics, logs, and traces associated with that part of the service\. This enables you to investigate problems and their effect on an application\.

**To view service map and traces using the CloudWatch console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Monitoring**\.

1. Choose **View traces in X\-Ray**\.

1. Choose **Service map**\.

1. Choose from the predefined time ranges, or choose a custom time range\.

1. To troubleshoot requests, choose a filter\.

## Example AWS X\-Ray workflow to view trace details<a name="monitoring-tracing-example"></a>

If you've enabled AWS X\-Ray, you can use the single\-function view on the CloudWatch Lambda Insights dashboard to show the distributed trace data of a function invocation error\. For example, if the application logs message shows an error, you can open the ServiceLens traces view to see the distributed trace data and the other services handling the transaction\.

**To view trace details of a function**

1. Open the [single\-function view](https://console.aws.amazon.com/cloudwatch/home#lambda-insights:functions) in the CloudWatch console\.

1. Choose the **Application logs** tab\.

1. Use the **Timestamp** or **Message** to identify the invocation request that you want to troubleshoot\.

1. To show the **Most recent 1000 invocations**, choose the **Invocations** tab\.  
![\[Sorting the most recent 1000 invocations by request ID.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambainsights-invocations-request-id.png)

1. Choose the **Request ID** column to sort entries in ascending alphabetical order\.

1. In the **Trace** column, choose **View**\.

   The **Trace details** page opens in the ServiceLens traces view\.  
![\[Function trace details in the ServiceLens traces view.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambdainsights-trace-details.png)

## What's next?<a name="monitoring-troubleshooting-next-up"></a>
+ Learn more about how to integrate traces, metrics, logs, and alarms in [Using ServiceLens to Monitor the Health of Your Applications](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/ServiceLens.html)\.