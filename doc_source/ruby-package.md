# Deploy Ruby Lambda functions with \.zip file archives<a name="ruby-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

To create the deployment package for a \.zip file archive, you can use a built\-in \.zip file archive utility or any other \.zip file utility \(such as [7zip](https://www.7-zip.org/download.html)\) for your command line tool\. Note the following requirements for using a \.zip file as your deployment package:
+ The \.zip file contains your function's code and any dependencies used to run your function's code \(if applicable\) on Lambda\. If your function depends only on standard libraries, or AWS SDK libraries, you don't need to include these libraries in your \.zip file\. These libraries are included with the supported [Lambda runtime](lambda-runtimes.md) environments\.
+ If the \.zip file is larger than 50 MB, we recommend uploading it to your function from an Amazon Simple Storage Service \(Amazon S3\) bucket\.
+ If your deployment package contains native libraries, you can build the deployment package with AWS Serverless Application Model \(AWS SAM\)\. You can use the AWS SAM CLI `sam build` command with the `--use-container` to create your deployment package\. This option builds a deployment package inside a Docker image that is compatible with the Lambda execution environment\. 

  For more information, see [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-build.html) in the *AWS Serverless Application Model Developer Guide*\.
+ You need to build the deployment package to be compatible with this [instruction set architecture](foundation-arch.md) of the function\.
+ Lambda uses POSIX file permissions, so you may need to [ set permissions for the deployment package folder](http://aws.amazon.com/premiumsupport/knowledge-center/lambda-deployment-package-errors/) before you create the \.zip file archive\.

**Topics**
+ [Prerequisites](#ruby-package-prereqs)
+ [Tools and libraries](#ruby-package-libraries)
+ [Updating a function with no dependencies](#ruby-package-codeonly)
+ [Updating a function with additional dependencies](#ruby-package-dependencies)

## Prerequisites<a name="ruby-package-prereqs"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## Tools and libraries<a name="ruby-package-libraries"></a>

Lambda provides the following tools and libraries for the Ruby runtime:

**Tools and libraries for Ruby**
+ [AWS SDK for Ruby](https://github.com/aws/aws-sdk-ruby): the official AWS SDK for the Ruby programming language\.

## Updating a function with no dependencies<a name="ruby-package-codeonly"></a>

To update a function by using the Lambda API, use the [UpdateFunctionCode](API_UpdateFunctionCode.md) operation\. Create an archive that contains your function code, and upload it using the AWS Command Line Interface \(AWS CLI\)\.

**To update a Ruby function with no dependencies**

1. Create a \.zip file archive\.

   ```
   zip function.zip function.rb
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
       "Runtime": "ruby2.5",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```

## Updating a function with additional dependencies<a name="ruby-package-dependencies"></a>

If your function depends on libraries other than the AWS SDK for Ruby, install them to a local directory with [Bundler](https://bundler.io/), and include them in your deployment package\.

**To update a Ruby function with dependencies**

1. Install libraries in the vendor directory using the `bundle` command\.

   ```
   bundle config set --local path 'vendor/bundle' \ 
   bundle install
   ```

   You should see the following output:

   ```
   Fetching gem metadata from https://rubygems.org/..............
   Resolving dependencies...
   Fetching aws-eventstream 1.0.1
   Installing aws-eventstream 1.0.1
   ...
   ```

   This installs the gems in the project directory instead of the system location, and sets `vendor/bundle` as the default path for future installations\. To later install gems globally, use `bundle config set --local system 'true'`\.

1. Create a \.zip file archive\.

   ```
   zip -r function.zip function.rb vendor
   ```

   You should see the following output:

   ```
   adding: function.rb (deflated 37%)
     adding: vendor/ (stored 0%)
     adding: vendor/bundle/ (stored 0%)
     adding: vendor/bundle/ruby/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/build_info/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/cache/ (stored 0%)
     adding: vendor/bundle/ruby/2.7.0/cache/aws-eventstream-1.0.1.gem (deflated 36%)
   ...
   ```

1. Update the function code\.

   ```
   aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   ```

   You should see the following output:

   ```
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
       "Runtime": "ruby2.5",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 300,
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```