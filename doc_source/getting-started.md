# Getting Started<a name="getting-started"></a>

In this section, we introduce you to the fundamental concepts of a typical Lambda\-based application and the options available to create and test your applications\. In addition, you will be provided with instructions on installing the necessary tools to complete the tutorials included in this guide and create your first Lambda function\. 

## Building Blocks of a Lambda\-based Application<a name="lambda-application-fundamentals"></a>
+ **Lambda function: **The foundation, it is comprised of your custom code and any dependent libraries\. For more information, see [Lambda Functions](lambda-introduction-function.md)\.
+ **Event source: ** An AWS service, such as Amazon SNS, or a custom service, that triggers your function and executes its logic\. For more information, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\.
+ **Downstream resources: **An AWS service, such as DynamoDB tables or Amazon S3 buckets, that your Lambda function calls once it is triggered\. 
+ **Log streams: **While Lambda automatically monitors your function invocations and reports metrics to CloudWatch, you can annotate your function code with custom logging statements that allow you to analyze the execution flow and performance of your Lambda function to ensure it's working properly\.
+ **AWS SAM: **A model to define [serverless applications](https://aws.amazon.com/serverless)\. AWS SAM is natively supported by AWS CloudFormation and defines simplified syntax for expressing serverless resources\. For more information, see [Using the AWS Serverless Application Model \(AWS SAM\)](serverless_app.md)

## Tools to Create and Test Lambda\-based Applications<a name="lambda-application-tools"></a>

There are three key tools that you use to interact with the AWS Lambda service, described below\. We will cover tools for building AWS Lambda\-based applications in further sections\.
+ **Lambda Console: ** Provides a way for you to graphically design your Lambda\-based application, author or update your Lambda function code, and configure event, downstream resources and IAM permissions that your function requires\. It also includes advanced configuration options, outlined in [Advanced Topics](advanced.md)\.
+ **AWS CLI: **A command\-line interface you can use to leverage Lambda's API operations, such as creating functions and mapping event sources\. For a full list of Lambda's API operations, see [Actions](API_Operations.md)\.
+ **SAM CLI: **A command\-line interface you can use to develop, test, and analyze your serverless applications locally before uploading them to the Lambda runtime\. For more information, see [Test Your Serverless Applications Locally Using SAM CLI \(Public Beta\)](test-sam-cli.md)\.

## Before you begin<a name="lambda-application-fundamentals-before-you-begin"></a>

In order to use the tutorials offered at the end of this section, make sure you have done the following:
+ [Set Up an AWS Account](setup.md) 
+ [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)
+ Followed the steps to use SAM CLI, including [Docker](https://www.docker.com), outlined here: [Install SAM CLI](sam-cli-requirements.md)\. 

### Next Step<a name="setting-up-next-step-account"></a>
[Set Up an AWS Account](setup.md)