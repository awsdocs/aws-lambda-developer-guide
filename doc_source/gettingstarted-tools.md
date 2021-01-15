# Tools for working with Lambda<a name="gettingstarted-tools"></a>

In addition to the Lambda console, you can use the following tools to manage and invoke Lambda resources\.

**Topics**
+ [AWS CLI](#gettingstarted-tools-awscli)
+ [AWS SAM](#gettingstarted-tools-awssam)
+ [AWS SAM CLI](#gettingstarted-tools-samcli)
+ [Tools for container images](#gettingstarted-tools-docker)
+ [Code authoring tools](#lambda-app-author)

## AWS CLI<a name="gettingstarted-tools-awscli"></a>

To manage and use Lambda functions from the command line, install the AWS Command Line Interface \(AWS CLI\)\. Tutorials in this guide use the AWS CLI, which has commands for all Lambda API operations\. Some functionality is not available in the Lambda console and can be accessed only with the AWS CLI or the AWS SDKs\.

To set up the AWS CLI, see the following topics in the *AWS Command Line Interface User Guide*\.
+ [Installing, updating, and uninstalling the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
+ [Configuring the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

To verify that the AWS CLI is configured correctly, run the `list-functions` command to see a list of your Lambda functions in the current AWS Region\.

```
$ aws lambda list-functions
```

## AWS SAM<a name="gettingstarted-tools-awssam"></a>

The AWS Serverless Application Model \(AWS SAM\) is an extension for the AWS CloudFormation template language that lets you define serverless applications at a higher level\. AWS SAM abstracts away common tasks such as function role creation, making it easier to write templates\. AWS SAM is supported directly by AWS CloudFormation, and includes additional functionality through the AWS CLI and AWS SAM CLI\.

For more information about AWS SAM templates, see the [AWS SAM specification](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification.html) in the *AWS Serverless Application Model Developer Guide*\.

## AWS SAM CLI<a name="gettingstarted-tools-samcli"></a>

The AWS SAM CLI is a separate command line tool that you can use to manage and test AWS SAM applications\. In addition to commands for uploading artifacts and launching AWS CloudFormation stacks that are also available in the AWS CLI, the AWS SAM CLI provides commands for validating templates and running applications locally in a Docker container\. You can use the AWS SAM CLI to build functions deployed as \.zip file archives or container images\.

To set up the AWS SAM CLI, see [Installing the AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) in the *AWS Serverless Application Model Developer Guide*\.

## Tools for container images<a name="gettingstarted-tools-docker"></a>

To create and test functions deployed as container images, you can use native container tools such as the Docker CLI\.

To set up the Docker CLI, see [Get Docker](https://docs.docker.com/get-docker) on the Docker Docs website\. For an introduction to using Docker with AWS, see [Getting started with Amazon ECR using the AWS CLI](https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html) in the *Amazon Elastic Container Registry User Guide*\.

## Code authoring tools<a name="lambda-app-author"></a>

You can author your Lambda function code in the languages that Lambda supports\. For a list of supported languages, see [Lambda runtimes](lambda-runtimes.md)\. There are tools for authoring code, such as the Lambda console, Eclipse integrated development environment \(IDE\), and Visual Studio IDE\. But the available tools and options depend on:
+ The language that you use to write your Lambda function code\.
+ The libraries that you use in your code\. The Lambda runtimes provide some of the libraries, and you must upload any additional libraries that you use\.

The following table lists the languages that Lambda supports, and the tools and options that you can use with them\.


****  

| Language | Tools and options for authoring code | 
| --- | --- | 
| Node\.js |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Java |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| C\# |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Python |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Ruby |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Go |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| PowerShell |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html) | 