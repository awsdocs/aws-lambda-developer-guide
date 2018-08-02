# AWS Lambda Limits<a name="limits"></a>

As explained in [Lambda Functions](lambda-introduction-function.md), once you've packaged up your custom code, including any dependencies, and uploaded it to AWS Lambda, you have created a *Lambda function*\. But there are limits that AWS Lambda imposes that include, for example, the size of your deployment package or the amount of memory your Lambda function is allocated per invocation\. In addition: 
+ IAM limits may impact your Lambda function\. For more information, see [Limitations on IAM Entities and Objects](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_iam-limits.html)\.
+ If you’re using Lambda@Edge, additional limits apply\. For more information, see [Limits on Lambda@Edge](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/cloudfront-limits.html#limits-lambda-at-edge) in the Amazon CloudFront Developer Guide\.

 This section discusses those AWS Lambda limits\.

**Topics**
+ [AWS Lambda Limits](#limits-list)
+ [AWS Lambda Limit Errors](#limits-troubleshooting)

## AWS Lambda Limits<a name="limits-list"></a>


**AWS Lambda Resource Limits per Invocation**  

| Resource | Limits | 
| --- | --- | 
| Memory allocation range  | Minimum = 128 MB / Maximum = 3008 MB \(with 64 MB increments\)\. If the maximum memory use is exceeded, function invocation will be terminated\. | 
| Ephemeral disk capacity \("/tmp" space\) | 512 MB | 
| Number of file descriptors  | 1,024 | 
| Number of processes and threads \(combined total\) | 1,024 | 
| Maximum execution duration per request | 300 seconds \(5 minutes\) | 
| [Invoke](API_Invoke.md) request body payload size \(RequestResponse/synchronous invocation\) NOTE: The response body payload also must adhere to this limit\. | 6 MB  | 
| [Invoke](API_Invoke.md) request body payload size \(Event/asynchronous invocation\) | 128 KB | 


**AWS Lambda Account Limits Per Region**  

| Resource | Default Limit | 
| --- | --- | 
| Concurrent executions \(see [Managing Concurrency](concurrent-executions.md)\)  | 1000 | 

AWS Lambda will dynamically scale capacity in response to increased traffic, subject to the concurrent executions limit noted previously\. To handle any burst in traffic, AWS Lambda will immediately increase your concurrently executing functions by a predetermined amount, dependent on which region it's executed, as noted below:


****  

| Region | Immediate Concurrency Increase \(function executions\) | 
| --- | --- | 
| Asia Pacific \(Tokyo\) | 1000 | 
| Asia Pacific \(Seoul\) | 500 | 
| Asia Pacific \(Mumbai\) | 500 | 
| Asia Pacific \(Singapore\) | 500 | 
| Asia Pacific \(Sydney\) | 500 | 
| China \(Beijing\) | 500 | 
| China \(Ningxia\) | 500 | 
| Canada \(Central\) | 500 | 
| EU \(Frankfurt\) | 1000 | 
| EU \(London\) | 500 | 
| EU \(Ireland\) | 3000 | 
| EU \(Paris\) | 500 | 
| AWS GovCloud \(US\) | 500 | 
| US East \(Ohio\) | 500 | 
| US West \(N\. California\) | 500 | 
| US West \(Oregon\) | 3000 | 
| US East \(N\. Virginia\) | 3000 | 
| South America \(São Paulo\) | 500 | 

**Note**  
If the default **Immediate Concurrency Increase** value is not sufficient to accommodate the traffic surge, AWS Lambda will continue to increase the number of concurrent function executions by **500 per minute** until your account safety limit has been reached or the number of concurrently executing functions is sufficient to successfully process the increased load\. For more information, see [Understanding Scaling Behavior](scaling.md)\.

**To request a limit increase for concurrent executions:**

1. Open the [AWS Support Center](https://console.aws.amazon.com/support/home#/) page, sign in, if necessary, and then click **Create case**\.

1. Under **Regarding**, select **Service Limit Increase**\.

1. Under **Limit Type**, select **Lambda**, fill in the necessary fields in the form, and then click the button at the bottom of the page for your preferred method of contact\.

**Note**  
AWS may automatically raise the concurrent execution limit on your behalf to enable your function to match the incoming event rate, as in the case of triggering the function from an Amazon S3 bucket\.

The following table lists service limits for deploying a Lambda function\.


**AWS Lambda Deployment Limits**  

| Item | Default Limit | 
| --- | --- | 
| Lambda function deployment package size \(compressed \.zip/\.jar file\) | 50 MB | 
| Total size of all the deployment packages that can be uploaded per region | 75 GB | 
| Size of code/dependencies that you can zip into a deployment package \(uncompressed \.zip/\.jar size\)\. Each Lambda function receives an additional 512MB of non\-persistent disk space in its own `/tmp` directory\. The `/tmp` directory can be used for loading additional resources like dependency libraries or data sets during function initialization\.  | 250 MB | 
| Total size of environment variables set | 4 KB | 

**Note**  
If the size of your Lambda function's zipped deployment packages exceeds 3MB, you will not be able to use the inline code editing feature in the Lambda console\. You can still use the console to invoke your Lambda function\.

## AWS Lambda Limit Errors<a name="limits-troubleshooting"></a>

Functions that exceed any of the limits listed in the previous limits tables will fail with an `exceeded limits` exception\. These limits are fixed and cannot be changed at this time\. For example, if you receive the exception `CodeStorageExceededException` or an error message similar to `"Code storage limit exceeded"` from AWS Lambda, you need to reduce the size of your code storage\.

**To reduce the size of your code storage**

1. Remove the functions that you no longer use\.

1. Reduce the code size of the functions that you do not want to remove\. You can find the code size of a Lambda function by using the AWS Lambda console, the AWS Command Line Interface, or AWS SDKs\.