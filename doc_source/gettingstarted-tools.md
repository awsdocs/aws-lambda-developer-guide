# Tools for working with AWS Lambda<a name="gettingstarted-tools"></a>

In addition to the Lambda console, you can use the following tools to manage and invoke Lambda resources\.

**Topics**
+ [AWS Command Line Interface](#gettingstarted-tools-awscli)
+ [AWS Serverless Application Model](#gettingstarted-tools-awssam)
+ [SAM CLI](#gettingstarted-tools-samcli)
+ [Code authoring tools](#lambda-app-author)

## AWS Command Line Interface<a name="gettingstarted-tools-awscli"></a>

Install the AWS Command Line Interface to manage and use Lambda functions from the command line\. Tutorials in this guide use the AWS CLI, which has commands for all Lambda API actions\. Some functionality is not available in the Lambda console and can only be accessed with the AWS CLI or the AWS SDK\.

To set up the AWS CLI, see the following topics in the *AWS Command Line Interface User Guide*\.
+ [Getting set up with the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-set-up.html)
+ [Configuring the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)

To verify that the AWS CLI is configured correctly, run the `list-functions` command to see a list of your Lambda functions in the current region\.

```
$ aws lambda list-functions
```

## AWS Serverless Application Model<a name="gettingstarted-tools-awssam"></a>

AWS SAM is an extension for the AWS CloudFormation template language that lets you define serverless applications at a higher level\. It abstracts away common tasks such as function role creation, which makes it easier to write templates\. AWS SAM is supported directly by AWS CloudFormation, and includes additional functionality through the AWS CLI and AWS SAM CLI\.

For more information about AWS SAM templates, see [ AWS SAM template basics](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-basics.html) in the *AWS Serverless Application Model Developer Guide*\.

## SAM CLI<a name="gettingstarted-tools-samcli"></a>

The AWS SAM CLI is a separate command line tool that you can use to manage and test AWS SAM applications\. In addition to commands for uploading artifacts and launching AWS CloudFormation stacks that are also available in the AWS CLI, the SAM CLI provides additional commands for validating templates and running applications locally in a Docker container\.

To set up the AWS SAM CLI, see [Installing the AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) in the *AWS Serverless Application Model Developer Guide*\.

## Code authoring tools<a name="lambda-app-author"></a>

You can author your Lambda function code in the languages that are supported by AWS Lambda\. For a list of supported languages, see [AWS Lambda runtimes](lambda-runtimes.md)\. There are tools for authoring code, such as the AWS Lambda console, Eclipse IDE, and Visual Studio IDE\. But the available tools and options depend on the following:
+ Language you choose to write your Lambda function code\. 
+ Libraries that you use in your code\. AWS Lambda runtime provides some of the libraries and you must upload any additional libraries that you use\. 

The following table lists languages, and the available tools and options that you can use\.


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