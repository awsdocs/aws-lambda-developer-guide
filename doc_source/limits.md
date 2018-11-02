# AWS Lambda Limits<a name="limits"></a>

As explained in [Working with Lambda Functions](lambda-introduction-function.md), once you've packaged up your custom code, including any dependencies, and uploaded it to AWS Lambda, you have created a *Lambda function*\. But there are limits that AWS Lambda imposes that include, for example, the size of your deployment package or the amount of memory your Lambda function is allocated per invocation\. In addition:
+ IAM limits may impact your Lambda function\. For more information, see [Limitations on IAM Entities and Objects](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_iam-limits.html)\.
+ If you’re using Lambda@Edge, additional limits apply\. For more information, see [Limits on Lambda@Edge](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/cloudfront-limits.html#limits-lambda-at-edge) in the Amazon CloudFront Developer Guide\.

**To request a limit increase for concurrent executions:**

1. Open the [AWS Support Center](https://console.aws.amazon.com/support/home#/) page, sign in, if necessary, and then click **Create case**\.

1. Under **Regarding**, select **Service Limit Increase**\.

1. Under **Limit Type**, select **Lambda**, fill in the necessary fields in the form, and then click the button at the bottom of the page for your preferred method of contact\.

**Topics**
+ [AWS Lambda Limits](#limits-list)
+ [AWS Lambda Limit Errors](#limits-troubleshooting)

## AWS Lambda Limits<a name="limits-list"></a>


**Function Limits**  

| Resource | Limit | 
| --- | --- | 
| Memory allocation | 128 MB to 3008 MB, in 64 MB increments\. | 
| Ephemeral disk capacity \("/tmp" space\) | 512 MB | 
| Number of file descriptors  | 1,024 | 
| Number of processes and threads \(combined total\) | 1,024 | 
| Maximum execution duration per request | 900 seconds \(15 minutes\) | 
| [Invoke](API_Invoke.md) request body payload size \(RequestResponse/synchronous invocation\) NOTE: The response body payload also must adhere to this limit\. | 6 MB  | 
| [Invoke](API_Invoke.md) request body payload size \(Event/asynchronous invocation\) | 128 KB | 


**AWS Lambda Account Limits Per Region**  

| Resource | Default Limit | 
| --- | --- | 
| Concurrent executions \(see [Managing Concurrency](concurrent-executions.md)\)  | 1000 | 

For details on how Lambda scales your function concurrency in response to traffic, see [Understanding Scaling Behavior](scaling.md)\.

The following table lists service limits for deploying a Lambda function\.


**AWS Lambda Deployment Limits**  

| Item | Default Limit | 
| --- | --- | 
| Maximum deployment package size | 50 MB | 
| Maximum deployment package size for console code editor | 3 MB | 
| Total size of all the deployment packages that can be uploaded per region | 75 GB | 
| Size of code/dependencies that you can zip into a deployment package \(uncompressed \.zip/\.jar size\)\.  Each Lambda function receives an additional 512MB of non\-persistent disk space in its own `/tmp` directory\. The `/tmp` directory can be used for loading additional resources like dependency libraries or data sets during function initialization\.   | 250 MB | 
| Total size of environment variables set | 4 KB | 


**AWS Lambda Console Limits**

| Item | Default Limit | 
| --- | --- | 
| Size of Lambda function deployment package that can be edited inline | 3 MB | 
| Number of test events per function | 10 | 

## AWS Lambda Limit Errors<a name="limits-troubleshooting"></a>

Functions that exceed any of the limits listed in the previous limits tables will fail with an `exceeded limits` exception\. These limits are fixed and cannot be changed at this time\. For example, if you receive the exception `CodeStorageExceededException` or an error message similar to `Code storage limit exceeded` from AWS Lambda, you need to reduce the size of your code storage\.

**To reduce the size of your code storage**

1. Remove the functions that you no longer use\.

1. Reduce the code size of the functions that you do not want to remove\. You can find the code size of a Lambda function by using the AWS Lambda console, the AWS Command Line Interface, or AWS SDKs\.
