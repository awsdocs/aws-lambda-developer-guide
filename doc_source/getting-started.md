# Getting started with Lambda<a name="getting-started"></a>

To get started with Lambda, use the Lambda console to create a function\. In a few minutes, you can create a function, invoke it, and then view logs, metrics, and trace data\.

**Note**  
To use Lambda and other AWS services, you need an AWS account\. If you don't have an account, visit [aws\.amazon\.com](https://aws.amazon.com/) and choose **Create an AWS Account**\. For instructions, see [How do I create and activate a new AWS account?](http://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)  
As a best practice, create an AWS Identity and Access Management \(IAM\) user with administrator permissions, and then use that IAM user for all work that does not require root credentials\. Create a password for console access, and create access keys to use command line tools\. For instructions, see [Creating your first IAM admin user and group](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\.

You can author functions in the Lambda console, or with an IDE toolkit, command line tools, or the AWS SDKs\. The Lambda console provides a [code editor](code-editor.md) for non\-compiled languages that lets you modify and test code quickly\. The [AWS Command Line Interface \(AWS CLI\)](gettingstarted-awscli.md) gives you direct access to the Lambda API for advanced configuration and automation use cases\.

You deploy your function code to Lambda using a deployment package\. Lambda supports two types of deployment packages:
+ A \.zip file archive that contains your function code and its dependencies\. For an example tutorial, see [Using AWS Lambda with the AWS Command Line Interface](gettingstarted-awscli.md)\.
+ A container image that is compatible with the [Open Container Initiative \(OCI\)](https://opencontainers.org/) specification\. For an example tutorial, see [Create a function defined as a container image](getting-started-create-function.md#gettingstarted-images)\.

**Topics**
+ [Create a Lambda function with the console](getting-started-create-function.md)
+ [Creating functions using the AWS Lambda console editor](code-editor.md)
+ [Using AWS Lambda with the AWS Command Line Interface](gettingstarted-awscli.md)
+ [Lambda concepts](gettingstarted-concepts.md)
+ [Lambda features](gettingstarted-features.md)
+ [Lambda deployment packages](gettingstarted-package.md)
+ [Tools for working with Lambda](gettingstarted-tools.md)
+ [Lambda quotas](gettingstarted-limits.md)