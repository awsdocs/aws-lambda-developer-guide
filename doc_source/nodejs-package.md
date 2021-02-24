# Deploy Node\.js Lambda functions with \.zip file archives<a name="nodejs-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip files\.

You can use a built\-in ZIP archive utility, or any other ZIP utility \(such as [7zip](https://www.7-zip.org/download.html)\) for your command line tool to create a deployment package\.
+ The \.zip file must contain your function's code, and any dependencies used to run your function's code \(if applicable\) on Lambda\. If your function depends only on standard libraries, or AWS SDK libraries, you do not need to include the libraries in your \.zip file\. These libraries are included with our supported [Lambda runtime](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html) environments\.
+ If your \.zip file is larger than 50 MB, we recommend uploading it to an Amazon S3 bucket\. For more information, see [Using other AWS services to build a deployment package](gettingstarted-package.md)\.
+ If your \.zip file contains C\-extension libraries, such as the Pillow \(PIL\) library, we recommend using the AWS SAM CLI to build a deployment package\. For more information, see [Lambda deployment packages](gettingstarted-package.md)\.

This page describes how to create a \.zip file as your deployment package, and then use the \.zip file to deploy your function code to Lambda using the AWS Command Line Interface \(AWS CLI\)\. To upload your \.zip file on the Lambda console, see [Deployment packages](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-package-zip)\.

**Topics**
+ [Prerequisites](#node-package-prereqs)
+ [Updating a function with no dependencies](#nodejs-package-codeonly)
+ [Updating a function with additional dependencies](#nodejs-package-dependencies)

## Prerequisites<a name="node-package-prereqs"></a>

The AWS Command Line Interface \(AWS CLI\) is an open source tool that enables you to interact with AWS services using commands in your command\-line shell\. To complete the steps in this section, you need the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## Updating a function with no dependencies<a name="nodejs-package-codeonly"></a>

To update a function by using the Lambda API, use the [UpdateFunctionCode](API_UpdateFunctionCode.md) operation\. Create an archive that contains your function code, and upload it using the AWS Command Line Interface \(AWS CLI\)\.

**To update a Node\.js function with no dependencies**

1. Create a \.zip file archive\.

   ```
   zip function.zip index.js
   ```

1. To upload the package, use the `update-function-code` command\.

   ```
   aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   ```

   You should see the following output:

   ```
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
       "Runtime": "nodejs12.x",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "index.handler",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```

## Updating a function with additional dependencies<a name="nodejs-package-dependencies"></a>

If your function depends on libraries other than the AWS SDK for JavaScript, use [npm](https://www.npmjs.com/) to include them in your deployment package\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\.

You can add the SDK for JavaScript to the deployment package if you need a newer version than the one [included on the runtime](lambda-nodejs.md), or to ensure that the version doesn't change in the future\.

If your deployment package contains native libraries, you can build the deployment package with AWS Serverless Application Model \(AWS SAM\)\. You can use the AWS SAM CLI `sam build` command with the `--use-container` to create your deployment package\. This option builds a deployment package inside a Docker image that is compatible with the Lambda execution environment\. 

For more information, see [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-build.html) in the *AWS Serverless Application Model Developer Guide*\.

As an alternative, you can create the deployment package using an Amazon EC2 instance that provides an Amazon Linux environment\. For instructions, see [Using Packages and Native nodejs Modules in AWS](http://aws.amazon.com/blogs/compute/nodejs-packages-in-lambda/) in the AWS compute blog\. 

**To update a Node\.js function with dependencies**

1. Open a command line terminal or shell\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\.

1. Create a folder for the deployment package\. The following steps assume that the folder is named `my-function>`\.

1. Install libraries in the node\_modules directory using the `npm install` command\.

   ```
   npm install aws-xray-sdk
   ```

   This creates a folder structure that's similar to the following:

   ```
   ~/my-function
   ├── index.js
   └── node_modules
       ├── async
       ├── async-listener
       ├── atomic-batcher
       ├── aws-sdk
       ├── aws-xray-sdk
       ├── aws-xray-sdk-core
   ```

1. Create a \.zip file that contains the contents of your project folder\. Use the `r` \(recursive\) option to ensure that zip compresses the subfolders\.

   ```
   zip -r function.zip .
   ```

1. Upload the package using the `update-function-code` command\.

   ```
   aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   ```

   You should see the following output:

   ```
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
       "Runtime": "nodejs12.x",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "index.handler",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```

In addition to code and libraries, your deployment package can also contain executable files and other resources\. For more information, see [Running Arbitrary Executables in AWS Lambda](http://aws.amazon.com/blogs/compute/running-executables-in-aws-lambda/) in the AWS Compute Blog\.