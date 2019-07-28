# Tools for Working With AWS Lambda<a name="gettingstarted-tools"></a>

Install the AWS Command Line Interface to manage and use Lambda functions from the command line\. Tutorials in this guide use the AWS CLI, which has commands for all Lambda API actions\. Some functionality is not available in the Lambda console and can only be accessed with the AWS CLI or the AWS SDK\.

**To set up the AWS CLI**

1. Download and configure the AWS CLI\. For instructions, see the following topics in the *AWS Command Line Interface User Guide*\. 
   + [Getting Set Up with the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-set-up.html)
   + [Configuring the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)

1. Verify the setup by entering the following commands at the command prompt\. 
   + Try the help command to verify that the AWS CLI is installed on your computer:

     ```
     $ aws help
     ```
   + Try a Lambda command to verify the user can reach AWS Lambda\. This command lists Lambda functions in the account, if any\. The AWS CLI uses the `adminuser` credentials to authenticate the request\.

     ```
     $ aws lambda list-functions
     ```

The AWS SAM CLI is a separate command line tool that you can use to manage and test AWS SAM applications\. In addition to commands for uploading artifacts and launching AWS CloudFormation stacks that are also available in the AWS CLI, the SAM CLI provides additional commands for validating templates and running applications locally in a Docker container\.

For more details about installing the AWS SAM CLI, see [ Installing the AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) in the *AWS Serverless Application Model Developer Guide*\.

## Code Authoring Tools<a name="lambda-app-author"></a>

You can author your Lambda function code in the languages that are supported by AWS Lambda\. For a list of supported languages, see [AWS Lambda Runtimes](lambda-runtimes.md)\. There are tools for authoring code, such as the AWS Lambda console, Eclipse IDE, and Visual Studio IDE\. But the available tools and options depend on the following:
+ Language you choose to write your Lambda function code\. 
+ Libraries that you use in your code\. AWS Lambda runtime provides some of the libraries and you must upload any additional libraries that you use\. 

The following table lists languages, and the available tools and options that you can use\.


****  

| Language | Tools and Options for Authoring Code | 
| --- | --- | 
| Node\.js |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Java |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| C\# |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Python |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Ruby |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| Go |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html)  | 
| PowerShell |  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-tools.html) | 

In addition, regardless of the language you choose, there is a [programming model](programming-model-v2.md) for writing Lambda function code\. For example, how you write the handler method of your Lambda function \(that is, the method that AWS Lambda first calls when it begins executing the code\), how you pass events to the handler, what statements you can use in your code to generate logs in CloudWatch Logs, how to interact with AWS Lambda runtime and obtain information such as the time remaining before timeout, and how to handle exceptions\.