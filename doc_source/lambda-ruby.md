# Building Lambda functions with Ruby<a name="lambda-ruby"></a>

You can run Ruby code in AWS Lambda\. Lambda provides [runtimes](lambda-runtimes.md) for Ruby that execute your code to process events\. Your code runs in an environment that includes the AWS SDK for Ruby, with credentials from an AWS Identity and Access Management \(IAM\) role that you manage\.

Lambda supports the following Ruby runtimes\.


**Ruby runtimes**  

| Name | Identifier | AWS SDK for Ruby | Operating system | 
| --- | --- | --- | --- | 
|  Ruby 2\.7  |  `ruby2.7`  |  3\.0\.1  |  Amazon Linux 2  | 
|  Ruby 2\.5  |  `ruby2.5`  |  3\.0\.1  |  Amazon Linux  | 

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

**To create a Ruby function**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Choose **Create function**\.

1. Configure the following settings:
   + **Name** – **my\-function**\.
   + **Runtime** – **Ruby 2\.7**\.
   + **Role** – **Choose an existing role**\.
   + **Existing role** – **lambda\-role**\.

1. Choose **Create function**\.

1. To configure a test event, choose **Test**\.

1. For **Event name**, enter **test**\.

1. Choose **Create**\.

1. To execute the function, choose **Test**\.

The console creates a Lambda function with a single source file named `lambda_function.rb`\. You can edit this file and add more files in the built\-in [code editor](code-editor.md)\. To save your changes, choose **Save**\. Then, to run your code, choose **Test**\.

**Note**  
The Lambda console uses AWS Cloud9 to provide an integrated development environment in the browser\. You can also use AWS Cloud9 to develop Lambda functions in your own environment\. For more information, see [Working with AWS Lambda Functions](https://docs.aws.amazon.com/cloud9/latest/user-guide/lambda-functions.html) in the AWS Cloud9 user guide\.

The `lambda_function.rb` file exports a function named `lambda_handler` that takes an event object and a context object\. This is the [handler function](ruby-handler.md) that Lambda calls when the function is invoked\. The Ruby function runtime gets invocation events from Lambda and passes them to the handler\. In the function configuration, the handler value is `lambda_function.lambda_handler`\.

Each time you save your function code, the Lambda console creates a deployment package, which is a ZIP archive that contains your function code\. As your function development progresses, you will want to store your function code in source control, add libraries, and automate deployments\. Start by [creating a deployment package](ruby-package.md) and updating your code at the command line\.

**Note**  
To get started with application development in your local environment, deploy one of the sample applications available in this guide's GitHub repository\.  
[blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-ruby) – A Ruby function that shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\.

The function runtime passes a context object to the handler, in addition to the invocation event\. The [context object](ruby-context.md) contains additional information about the invocation, the function, and the execution environment\. More information is available from environment variables\.

Your Lambda function comes with a CloudWatch Logs log group\. The function runtime sends details about each invocation to CloudWatch Logs\. It relays any [logs that your function outputs](ruby-logging.md) during invocation\. If your function [returns an error](ruby-exceptions.md), Lambda formats the error and returns it to the invoker\.

**Topics**
+ [AWS Lambda function handler in Ruby](ruby-handler.md)
+ [AWS Lambda deployment package in Ruby](ruby-package.md)
+ [AWS Lambda context object in Ruby](ruby-context.md)
+ [AWS Lambda function logging in Ruby](ruby-logging.md)
+ [AWS Lambda function errors in Ruby](ruby-exceptions.md)
+ [Instrumenting Ruby code in AWS Lambda](ruby-tracing.md)