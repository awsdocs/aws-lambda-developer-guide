# Building Lambda functions with Python<a name="lambda-python"></a>

You can run Python code in AWS Lambda\. Lambda provides [runtimes](lambda-runtimes.md) for Python that run your code to process events\. Your code runs in an environment that includes the SDK for Python \(Boto3\), with credentials from an AWS Identity and Access Management \(IAM\) role that you manage\.

Lambda supports the following Python runtimes\.


**Python**  

| Name | Identifier | SDK | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | --- | 
|  Python 3\.9  |  `python3.9`  |  boto3\-1\.20\.32 botocore\-1\.23\.32  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Python 3\.8  |  `python3.8`  |  boto3\-1\.20\.32 botocore\-1\.23\.32  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.20\.32 botocore\-1\.23\.32  |  Amazon Linux  |  x86\_64  |    | 

The runtime information in this table undergoes continuous updates\. For more information on using AWS SDKs in Lambda, see [Managing AWS SDKs in Lambda functions](https://docs.aws.amazon.com/lambda/latest/operatorguide/sdks-functions.html)\.

**To create a Python function**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Choose **Create function**\.

1. Configure the following settings:
   + **Name** – **my\-function**\.
   + **Runtime** – **Python 3\.9**\.
   + **Role** – **Choose an existing role**\.
   + **Existing role** – **lambda\-role**\.

1. Choose **Create function**\.

1. To configure a test event, choose **Test**\.

1. For **Event name**, enter **test**\.

1. Choose **Save changes**\.

1. To invoke the function, choose **Test**\.

The console creates a Lambda function with a single source file named `lambda_function`\. You can edit this file and add more files in the built\-in [code editor](foundation-console.md#code-editor)\. To save your changes, choose **Save**\. Then, to run your code, choose **Test**\.

**Note**  
The Lambda console uses AWS Cloud9 to provide an integrated development environment in the browser\. You can also use AWS Cloud9 to develop Lambda functions in your own environment\. For more information, see [Working with Lambda Functions](https://docs.aws.amazon.com/cloud9/latest/user-guide/lambda-functions.html) in the AWS Cloud9 user guide\.

**Note**  
To get started with application development in your local environment, deploy one of the sample applications available in this guide's GitHub repository\.  
[blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-python) – A Python function that shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\.

Your Lambda function comes with a CloudWatch Logs log group\. The function runtime sends details about each invocation to CloudWatch Logs\. It relays any [logs that your function outputs](python-logging.md) during invocation\. If your function [returns an error](python-exceptions.md), Lambda formats the error and returns it to the invoker\.

**Topics**
+ [Lambda function handler in Python](python-handler.md)
+ [Deploy Python Lambda functions with \.zip file archives](python-package.md)
+ [Deploy Python Lambda functions with container images](python-image.md)
+ [AWS Lambda context object in Python](python-context.md)
+ [AWS Lambda function logging in Python](python-logging.md)
+ [AWS Lambda function errors in Python](python-exceptions.md)
+ [Instrumenting Python code in AWS Lambda](python-tracing.md)