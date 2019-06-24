# Building Lambda Functions with Python<a name="python-programming-model"></a>

You can run Python code in AWS Lambda\. Lambda provides [runtimes](lambda-runtimes.md) for Python that execute your code to process events\. Your code runs in an environment that includes the SDK for Python \(Boto 3\), with credentials from an AWS Identity and Access Management \(IAM\) role that you manage\.

Lambda supports the following Python runtimes\.


**Python Runtimes**  

| Name | Identifier | AWS SDK for Python | Operating System | 
| --- | --- | --- | --- | 
|  Python 3\.6  |  `python3.6`  |  boto3\-1\.7\.74 botocore\-1\.10\.74  |  Amazon Linux  | 
|  Python 3\.7  |  `python3.7`  |  boto3\-1\.9\.42 botocore\-1\.12\.42  |  Amazon Linux  | 
|  Python 2\.7  |  `python2.7`  |  N/A  |  Amazon Linux  | 

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

**To create a Python function**

1. Open the [Lambda console](https://console.aws.amazon.com/lambda)\.

1. Choose **Create function**\.

1. Configure the following settings:
   + **Name** – **my\-function**\.
   + **Runtime** – **Python 3\.7**\.
   + **Role** – **Choose an existing role**\.
   + **Existing role** – **lambda\-role**\.

1. Choose **Create function**\.

1. To configure a test event, choose **Test**\.

1. For **Event name**, enter **test**\.

1. Choose **Create**\.

1. To execute the function, choose **Test**\.

The console creates a Lambda function with a single source file named `lambda_function`\. You can edit this file and add more files in the built\-in [code editor](code-editor.md)\. To save your changes, choose **Save**\. Then, to run your code, choose **Test**\.

**Note**  
The Lambda console uses AWS Cloud9 to provide an integrated development environment in the browser\. You can also use AWS Cloud9 to develop Lambda functions in your own environment\. For more information, see [Working with AWS Lambda Functions](https://docs.aws.amazon.com/cloud9/latest/user-guide/lambda-functions.html) in the AWS Cloud9 user guide\.

The `lambda_function` file exports a function named `lambda_handler` that takes an event object and a context object\. This is the [handler function](python-programming-model-handler-types.md) that Lambda calls when the function is invoked\. The Python function runtime gets invocation events from Lambda and passes them to the handler\.

Each time you save your function code, the Lambda console creates a deployment package, which is a ZIP archive that contains your function code\. As your function development progresses, you will want to store your function code in source control, add libraries, and automate deployments\. Start by [creating a deployment package](lambda-python-how-to-create-deployment-package.md) and updating your code at the command line\.

The function runtime passes a context object to the handler, in addition to the invocation event\. The [context object](python-context-object.md) contains additional information about the invocation, the function, and the execution environment\. More information is available from environment variables\.

Your Lambda function comes with a CloudWatch Logs log group\. The function runtime sends details about each invocation to CloudWatch Logs\. It relays any [logs that your function outputs](python-logging.md) during invocation\. If your function [returns an error](python-exceptions.md), Lambda formats the error and returns it to the invoker\.

**Topics**
+ [AWS Lambda Function Handler in Python](python-programming-model-handler-types.md)
+ [AWS Lambda Deployment Package in Python](lambda-python-how-to-create-deployment-package.md)
+ [AWS Lambda Context Object in Python](python-context-object.md)
+ [AWS Lambda Function Logging in Python](python-logging.md)
+ [AWS Lambda Function Errors in Python](python-exceptions.md)
+ [Instrumenting Python Code in AWS Lambda](python-tracing.md)