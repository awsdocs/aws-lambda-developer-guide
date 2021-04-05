# Deploy Python Lambda functions with \.zip file archives<a name="python-package"></a>

**Note**  
End of support for the Python 2\.7 runtime starts on July 15, 2021\. For more information, see [Runtime support policy](runtime-support-policy.md)\.

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip files\.

To create a deployment package, you can use a built\-in \.zip file archive utility or any other \.zip file utility \(such as [7zip](https://www.7-zip.org/download.html)\) for your command line tool\. Note the following requirements for using a \.zip file as your deployment package:
+ The \.zip file must contain your function's code and any dependencies used to run your function's code \(if applicable\) on Lambda\. If your function depends only on standard libraries, or AWS SDK libraries, you don't need to include these libraries in your \.zip file\. These libraries are included with the supported [Lambda runtime](lambda-runtimes.md) environments\.
+ The \.zip file must be less than 50 MB\. If it's larger than 50 MB, we recommend uploading it to an Amazon Simple Storage Service \(Amazon S3\) bucket\.
+ The \.zip file can't contain libraries written in C or C\+\+\. If your \.zip file contains C\-extension libraries, such as the Pillow \(PIL\) or numpy libraries, we recommend using the AWS Serverless Application Model \(AWS SAM\) command line interface \(CLI\) to build a deployment package\.

This section describes how to create a \.zip file as your deployment package, and then use the \.zip file to deploy your function code to Lambda using the AWS Command Line Interface \(AWS CLI\)\.

**Topics**
+ [Prerequisites](#python-package-prereqs)
+ [What is a runtime dependency?](#python-package-dependencies)
+ [Tutorial: Creating a Lambda function in Python 3\.8](python-package-create.md)
+ [Updating a Lambda function in Python 3\.8](python-package-update.md)

## Prerequisites<a name="python-package-prereqs"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## What is a runtime dependency?<a name="python-package-dependencies"></a>

A [deployment package](gettingstarted-package.md) is required to create or update a Lambda function with or without runtime dependencies\. The deployment package acts as the source bundle to run your function's code and dependencies \(if applicable\) on Lambda\.

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\. For more information, see [What is a runtime dependency?](#python-package)\.

The following example describes a Lambda function without runtime dependencies:
+ If your function's code is in Python 3\.8, and it depends only on standard Python math and logging libraries, you don't need to include the libraries in your \.zip file\. These libraries are included with the `python3.8` runtime\.
+ If your function's code depends only on the [AWS SDK for Python \(Boto3\)](http://aws.amazon.com/sdk-for-python/), you don't need to include the boto3 library in your \.zip file\. These libraries are included with the `python3.8` runtime\.

For a complete list of AWS SDKs, see [Tools to Build on AWS](http://aws.amazon.com/tools/)\.