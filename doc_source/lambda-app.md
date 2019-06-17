# Building Lambda Functions<a name="lambda-app"></a>

You upload your application code in the form of one or more *Lambda functions* to AWS Lambda, a compute service\. In turn, AWS Lambda executes the code on your behalf\. AWS Lambda takes care of provisioning and managing the servers to run the code upon invocation\.

Typically, the lifecycle for an AWS Lambda\-based application includes authoring code, deploying code to AWS Lambda, and then monitoring and troubleshooting\. The following are general questions that come up in each of these lifecycle phases:
+ **Authoring code for your Lambda function** – What languages are supported? Is there a programming model that I need to follow? How do I package my code and dependencies for uploading to AWS Lambda? What tools are available?
+ **Uploading code and creating Lambda functions** – How do I upload my code package to AWS Lambda? How do I tell AWS Lambda where to begin executing my code? How do I specify compute requirements like memory and timeout? 
+ **Monitoring and troubleshooting** – For my Lambda function that is in production, what metrics are available? If there are any failures, how do I get logs or troubleshoot issues?

The following sections provide introductory information and the Example section at the end provides working examples for you to explore\.

## Authoring Code for Your Lambda Function<a name="lambda-app-author"></a>

You can author your Lambda function code in the languages that are supported by AWS Lambda\. For a list of supported languages, see [AWS Lambda Runtimes](lambda-runtimes.md)\. There are tools for authoring code, such as the AWS Lambda console, Eclipse IDE, and Visual Studio IDE\. But the available tools and options depend on the following:
+ Language you choose to write your Lambda function code\. 
+ Libraries that you use in your code\. AWS Lambda runtime provides some of the libraries and you must upload any additional libraries that you use\. 

The following table lists languages, and the available tools and options that you can use\.


****  

| Language | Tools and Options for Authoring Code | 
| --- | --- | 
| Node\.js | [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html) | 
| Java | [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html) | 
| C\# | [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html) | 
| Python | [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html) | 
| Go | [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html) | 
| PowerShell | [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html) | 

In addition, regardless of the language you choose, there is a pattern to writing Lambda function code\. For example, how you write the handler method of your Lambda function \(that is, the method that AWS Lambda first calls when it begins executing the code\), how you pass events to the handler, what statements you can use in your code to generate logs in CloudWatch Logs, how to interact with AWS Lambda runtime and obtain information such as the time remaining before timeout, and how to handle exceptions\. The [Programming Model](programming-model-v2.md) section provides information for each of the supported languages\.

## Deploying Code and Creating a Lambda Function<a name="lambda-app-deploy"></a>

To create a Lambda function, you first package your code and dependencies in a deployment package\. Then, you upload the deployment package to AWS Lambda to create your Lambda function\. 

**Topics**
+ [Creating a Deployment Package](#lambda-app-structure-code)
+ [Uploading a Deployment Package](#lambda-app-upload-deployment-pkg)
+ [Testing a Lambda Function](#lambda-app-test-code)

### Creating a Deployment Package<a name="lambda-app-structure-code"></a>

You must first organize your code and dependencies in certain ways and create a *deployment package*\. Instructions to create a deployment package vary depending on the language you choose to author the code\. For example, you can use build plugins such as Jenkins \(for Node\.js and Python\), and Maven \(for Java\) to create the deployment packages\. For more information, see [Creating a Deployment Package](deployment-package-v2.md)\. 

When you create Lambda functions using the console, the console creates the deployment package for you, and then uploads it to create your Lambda function\.

### Uploading a Deployment Package<a name="lambda-app-upload-deployment-pkg"></a>

AWS Lambda provides the [CreateFunction](API_CreateFunction.md) operation, which is what you use to create a Lambda function\. You can use the AWS Lambda console, AWS CLI, and AWS SDKs to create a Lambda function\. Internally, all of these interfaces call the `CreateFunction` operation\. 

In addition to providing your deployment package, you can provide configuration information when you create your Lambda function including the compute requirements of your Lambda function, the name of the handler method in your Lambda function, and the runtime, which depends on the language you chose to author your code\. For more information, see [Working with Lambda Functions](lambda-introduction-function.md)\.

### Testing a Lambda Function<a name="lambda-app-test-code"></a>

If your Lambda function is designed to process events of a specific type, you can use sample event data to test your Lambda function using one of the following methods:
+ Test your Lambda function in the console\. 
+ Test your Lambda function using the AWS CLI\. You can use the `Invoke` method to invoke your Lambda function and pass in sample event data\.
+ Test your Lambda function locally using the [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-test-and-debug.html)\.

## Monitoring and Troubleshooting<a name="lambda-app-monitor"></a>

After your Lambda function is in production, AWS Lambda automatically monitors functions on your behalf, reporting metrics through Amazon CloudWatch\. For more information, see [Monitoring Functions in the AWS Lambda Console](monitoring-functions-access-metrics.md)\.

To help you troubleshoot failures in a function, Lambda logs all requests handled by your function and also automatically stores logs that your code generates in Amazon CloudWatch Logs\. For more information, see [Accessing Amazon CloudWatch Logs for AWS Lambda](monitoring-functions-logs.md)\.