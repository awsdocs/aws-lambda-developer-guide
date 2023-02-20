# Building Lambda functions with Node\.js<a name="lambda-nodejs"></a>

You can run JavaScript code with Node\.js in AWS Lambda\. Lambda provides [runtimes](lambda-runtimes.md) for Node\.js that run your code to process events\. Your code runs in an environment that includes the AWS SDK for JavaScript, with credentials from an AWS Identity and Access Management \(IAM\) role that you manage\.

Lambda supports the following Node\.js runtimes\.


**Node\.js**  

| Name | Identifier | SDK | Operating system | Architectures | Deprecation | 
| --- | --- | --- | --- | --- | --- | 
|  Node\.js 16  |  `nodejs16.x`  |  2\.1083\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Node\.js 14  |  `nodejs14.x`  |  2\.1055\.0  |  Amazon Linux 2  |  x86\_64, arm64  |    | 
|  Node\.js 12  |  `nodejs12.x`  |  2\.1055\.0  |  Amazon Linux 2  |  x86\_64, arm64  |  Mar 31, 2023  | 

Lambda functions use an [execution role](lambda-intro-execution-role.md) to get permission to write logs to Amazon CloudWatch Logs, and to access other services and resources\. If you don't already have an execution role for function development, create one\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **Lambda**\.
   + **Permissions** – **AWSLambdaBasicExecutionRole**\.
   + **Role name** – **lambda\-role**\.

   The **AWSLambdaBasicExecutionRole** policy has the permissions that the function needs to write logs to CloudWatch Logs\.

You can add permissions to the role later, or swap it out for a different role that's specific to a single function\.

**To create a Node\.js function**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Choose **Create function**\.

1. Configure the following settings:
   + **Name** – **my\-function**\.
   + **Runtime** – **Node\.js 16\.x**\.
   + **Role** – **Choose an existing role**\.
   + **Existing role** – **lambda\-role**\.

1. Choose **Create function**\.

1. To configure a test event, choose **Test**\.

1. For **Event name**, enter **test**\.

1. Choose **Save changes**\.

1. To invoke the function, choose **Test**\.

The console creates a Lambda function with a single source file named `index.js`\. You can edit this file and add more files in the built\-in [code editor](foundation-console.md#code-editor)\. To save your changes, choose **Save**\. Then, to run your code, choose **Test**\.

**Note**  
The Lambda console uses AWS Cloud9 to provide an integrated development environment in the browser\. You can also use AWS Cloud9 to develop Lambda functions in your own environment\. For more information, see [Working with Lambda Functions](https://docs.aws.amazon.com/cloud9/latest/user-guide/lambda-functions.html) in the AWS Cloud9 user guide\.

The `index.js` file exports a function named `handler` that takes an event object and a context object\. This is the [handler function](nodejs-handler.md) that Lambda calls when the function is invoked\. The Node\.js function runtime gets invocation events from Lambda and passes them to the handler\. In the function configuration, the handler value is `index.handler`\.

When you save your function code, the Lambda console creates a \.zip file archive deployment package\. When you develop your function code outside of the console \(using an IDE\) you need to [create a deployment package](nodejs-package.md) to upload your code to the Lambda function\.

**Note**  
To get started with application development in your local environment, deploy one of the sample applications available in this guide's GitHub repository\.  
[blank\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs) – A Node\.js function that shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\.
[nodejs\-apig](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig) – A function with a public API endpoint that processes an event from API Gateway and returns an HTTP response\.
[rds\-mysql](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql) – A function that relays queries to a MySQL for RDS Database\. This sample includes a private VPC and database instance configured with a password in AWS Secrets Manager\.
[efs\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/efs-nodejs) – A function that uses an Amazon EFS file system in a Amazon VPC\. This sample includes a VPC, file system, mount targets, and access point configured for use with Lambda\.
[list\-manager](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/list-manager) – A function processes events from an Amazon Kinesis data stream and update aggregate lists in Amazon DynamoDB\. The function stores a record of each event in a MySQL for RDS Database in a private VPC\. This sample includes a private VPC with a VPC endpoint for DynamoDB and a database instance\.
[error\-processor](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor) – A Node\.js function generates errors for a specified percentage of requests\. A CloudWatch Logs subscription invokes a second function when an error is recorded\. The processor function uses the AWS SDK to gather details about the request and stores them in an Amazon S3 bucket\.

The function runtime passes a context object to the handler, in addition to the invocation event\. The [context object](nodejs-context.md) contains additional information about the invocation, the function, and the execution environment\. More information is available from environment variables\.

Your Lambda function comes with a CloudWatch Logs log group\. The function runtime sends details about each invocation to CloudWatch Logs\. It relays any [logs that your function outputs](nodejs-logging.md) during invocation\. If your function [returns an error](nodejs-exceptions.md), Lambda formats the error and returns it to the invoker\.

## Node\.js initialization<a name="nodejs-initialization"></a>

Node\.js has a unique event loop model that causes its initialization behavior to be different from other runtimes\. Specifically, Node\.js uses a non\-blocking I/O model that supports asynchronous operations\. This model allows Node\.js to perform efficiently for most workloads\. For example, if a Node\.js function makes a network call, that request may be designated as an asynchronous operation and placed into a callback queue\. The function may continue to process other operations within the main call stack without getting blocked by waiting for the network call to return\. Once the network call is returned, its callback is executed and then removed from the callback queue\. 

 Some initialization tasks may run asynchronously\. These asynchronous tasks are not guaranteed to complete execution prior to an invocation\. For example, code that makes a network call to fetch a parameter from AWS Parameter Store may not be complete by the time Lambda executes the handler function\. As a result, the variable may be null during an invocation\. To avoid this, ensure that variables and other asynchronous code are fully initialized before continuing with the rest of the function’s core business logic\. 

### Designating a function handler as an ES module<a name="designate-es-module"></a>

Starting with Node 14, you can guarantee completion of asynchronous initialization code prior to handler invocations by designating your code as an ES module, and using top\-level await\. Packages are designated as CommonJS modules by default, meaning you must first designate your function code as an ES module to use top\-level await\. You can do this in two ways: specifying the `type` as `module` in the function's `package.json` file, or by using the \.mjs file name extension\.

In the first scenario, your function code treats all \.js files as ES modules, while in the second scenario, only the file you specify with \.mjs is an ES module\. You can mix ES modules and CommonJS modules by naming them \.mjs and \.cjs respectively, as \.mjs files are always ES modules and \.cjs files are always CommonJS modules\.

For more information and an example, see [Using Node\.js ES Modules and Top\-Level Await in AWS Lambda](https://aws.amazon.com/blogs/compute/using-node-js-es-modules-and-top-level-await-in-aws-lambda)\.

**Topics**
+ [Node\.js initialization](#nodejs-initialization)
+ [AWS Lambda function handler in Node\.js](nodejs-handler.md)
+ [Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md)
+ [Deploy Node\.js Lambda functions with container images](nodejs-image.md)
+ [AWS Lambda context object in Node\.js](nodejs-context.md)
+ [AWS Lambda function logging in Node\.js](nodejs-logging.md)
+ [AWS Lambda function errors in Node\.js](nodejs-exceptions.md)
+ [Instrumenting Node\.js code in AWS Lambda](nodejs-tracing.md)