# Example workflows using other AWS services<a name="monitoring-servicemap"></a>

AWS Lambda integrates with other AWS services to help you monitor, trace, debug, and troubleshoot your Lambda functions\. This page shows workflows you can use with AWS X\-Ray, AWS Trusted Advisor and CloudWatch ServiceLens to trace and troubleshoot your Lambda functions\.

**Topics**
+ [Prerequisites](#monitoring-troubleshooting-prereqs)
+ [Pricing](#monitoring-troubleshooting-pricing)
+ [Example AWS X\-Ray workflow to view a service map](#monitoring-servicemap-example)
+ [Example AWS X\-Ray workflow to view trace details](#monitoring-tracing-example)
+ [Example AWS Trusted Advisor workflow to view recommendations](#monitoring-ta-example)
+ [What's next?](#monitoring-troubleshooting-next-up)

## Prerequisites<a name="monitoring-troubleshooting-prereqs"></a>

The following section describes the steps to using AWS X\-Ray and Trusted Advisor to troubleshoot your Lambda functions\.

### Using AWS X\-Ray<a name="monitoring-troubleshooting-prereqs-xray"></a>

AWS X\-Ray needs to be enabled on the Lambda console to complete the AWS X\-Ray workflows on this page\. If your execution role does not have the required permissions, the Lambda console will attempt to add them to your execution role\.

**To enable AWS X\-Ray on the Lambda console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose your function\.

1. Choose the **Configuration** tab\. 

1. On the **Monitoring tools** pane, choose **Edit**\.

1. Under **AWS X\-Ray**, turn on **Active tracing**\.

1. Choose **Save**\.

### Using AWS Trusted Advisor<a name="monitoring-troubleshooting-prereqs-trustedadvisor"></a>

AWS Trusted Advisor inspects your AWS environment and makes recommendations on ways you can save money, improve system availability and performance, and help close security gaps\. You can use Trusted Advisor checks to evaluate the Lambda functions and applications in your AWS account\. The checks provide recommended steps to take and resources for more information\.
+ For more information on AWS support plans for Trusted Advisor checks, see [Support plans](https://console.aws.amazon.com/support/plans/home?#/)\.
+ For more information about the checks for Lambda, see [AWS Trusted Advisor best practice checklist](http://aws.amazon.com/premiumsupport/technology/trusted-advisor/best-practice-checklist/)\.
+ For more information on how to use the Trusted Advisor console, see [Get started with AWS Trusted Advisor](https://docs.aws.amazon.com/awssupport/latest/user/get-started-with-aws-trusted-advisor.html)\.
+ For instructions on how to allow and deny console access to Trusted Advisor, see [IAM policy examples](https://docs.aws.amazon.com/awssupport/latest/user/security-trusted-advisor.html#iam-policy-examples-trusted-advisor)\.

## Pricing<a name="monitoring-troubleshooting-pricing"></a>
+ With AWS X\-Ray you pay only for what you use, based on the number of traces recorded, retrieved, and scanned\. For more information, see [AWS X\-Ray Pricing](http://aws.amazon.com/xray/pricing/)\.
+ Trusted Advisor cost optimization checks are included with AWS Business and Enterprise support subscriptions\. For more information, see [AWS Trusted Advisor Pricing](http://aws.amazon.com/premiumsupport/pricing/)\.

## Example AWS X\-Ray workflow to view a service map<a name="monitoring-servicemap-example"></a>

If you've enabled AWS X\-Ray, you can view a ServiceLens service map on the CloudWatch console\. A service map displays your service endpoints and resources as nodes and highlights the traffic, latency, and errors for each node and its connections\. 

You can choose a node to see detailed insights about the correlated metrics, logs, and traces associated with that part of the service\. This enables you to investigate problems and their effect on an application\.

**To view service map and traces using the CloudWatch console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

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

## Example AWS Trusted Advisor workflow to view recommendations<a name="monitoring-ta-example"></a>

Trusted Advisor checks Lambda functions in all AWS Regions to identify functions with the highest potential cost savings, and deliver actionable recommendations for optimization\. It analyzes your Lambda usage data such as function execution time, billed duration, memory used, memory configured, timeout configuration and errors\.

For example, the *Lambda Functions with High Error Rate* check recommends that you use AWS X\-Ray or CloudWatch to detect errors with your Lambda functions\.

**To check for functions with high error rates**

1. Open the [Trusted Advisor](https://console.aws.amazon.com/trustedadvisor) console\.

1. Choose the **Cost Optimization** category\.

1. Scroll down to **AWS Lambda Functions with High Error Rates**\. Expand the section to see the results and the recommended actions\.

## What's next?<a name="monitoring-troubleshooting-next-up"></a>
+ Learn more about how to integrate traces, metrics, logs, and alarms in [Using ServiceLens to Monitor the Health of Your Applications](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/ServiceLens.html)\.
+ Learn more about how to get a list of Trusted Advisor checks in [Using Trusted Advisor as a web service](https://docs.aws.amazon.com/awssupport/latest/user/trustedadvisor.html)\.