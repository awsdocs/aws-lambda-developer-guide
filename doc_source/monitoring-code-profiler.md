# Using CodeGuru Profiler with your Lambda function<a name="monitoring-code-profiler"></a>

You can use Amazon CodeGuru Profiler to gain insights into runtime performance of your Lambda functions\. This page describes how to activate CodeGuru Profiler from the Lambda console\.

**Topics**
+ [Supported runtimes](#monitoring-code-profiler-runtimes)
+ [Activating CodeGuru Profiler from the Lambda console](#monitoring-code-profiler-activate-console)
+ [What happens when you activate CodeGuru Profiler from the Lambda console?](#monitoring-code-profiler-what-happens-activate)
+ [What's next?](#monitoring-code-profiler-next-up)

## Supported runtimes<a name="monitoring-code-profiler-runtimes"></a>

You can activate CodeGuru Profiler from the Lambda console if your function's runtime is Python3\.8, Python3\.9, Java 8 with Amazon Linux 2, or Java 11\. For additional runtime versions, you can activate CodeGuru Profiler manually\.
+ For Java runtimes, see [ Profiling your Java applications that run on AWS Lambda](https://docs.aws.amazon.com/codeguru/latest/profiler-ug/setting-up-lambda.html)\.
+ For Python runtimes, see [ Profiling your Python applications that run on AWS Lambda](https://docs.aws.amazon.com/codeguru/latest/profiler-ug/python-lambda.html)\.

## Activating CodeGuru Profiler from the Lambda console<a name="monitoring-code-profiler-activate-console"></a>

This section describes how to activate CodeGuru Profiler from the Lambda console\.

**To activate CodeGuru Profiler from the Lambda console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose your function\.

1. Choose the **Configuration** tab\. 

1. On the **Monitoring and operations tools** pane, choose **Edit**\.

1. Under **Amazon CodeGuru Profiler**, turn on **Code profiling**\.

1. Choose **Save**\.

After activation, CodeGuru automatically creates a profiler group with the name `aws-lambda-<your-function-name>`\. You can change the name from the CodeGuru console\.

## What happens when you activate CodeGuru Profiler from the Lambda console?<a name="monitoring-code-profiler-what-happens-activate"></a>

When you activate CodeGuru Profiler from the console, Lambda automatically does the following on your behalf:
+  Lambda adds a CodeGuru Profiler layer to your function\. For more details, see [ Use AWS Lambda layers](https://docs.aws.amazon.com/codeguru/latest/profiler-ug/python-lambda-layers.html) in the *Amazon CodeGuru Profiler User Guide*\. 
+  Lambda also adds environment variables to your function\. The exact value varies based on the runtime\.   
**Environment variables**    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/monitoring-code-profiler.html)
+  Lambda adds the `AmazonCodeGuruProfilerAgentAccess` policy to your function's execution role\. 

**Note**  
When you deactivate CodeGuru Profiler from the console, Lambda automatically removes the CodeGuru Profiler layer and environment variables from your function\. However, Lambda does not remove the `AmazonCodeGuruProfilerAgentAccess` policy from your execution role\.

## What's next?<a name="monitoring-code-profiler-next-up"></a>
+ Learn more about the data collected by your profiler group in [Working with visualizations](https://docs.aws.amazon.com/codeguru/latest/profiler-ug/working-with-visualizations.html) in the *Amazon CodeGuru Profiler User Guide*\.