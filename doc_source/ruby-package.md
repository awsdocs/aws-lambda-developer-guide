# Deploy Ruby Lambda functions with \.zip file archives<a name="ruby-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip files\.

To create a deployment package, you can use a built\-in \.zip file archive utility or any other \.zip file utility \(such as [7zip](https://www.7-zip.org/download.html)\) for your command line tool\. Note the following requirements for using a \.zip file as your deployment package:
+ The \.zip file must contain your function's code and any dependencies used to run your function's code \(if applicable\) on Lambda\. If your function depends only on standard libraries, or AWS SDK libraries, you don't need to include these libraries in your \.zip file\. These libraries are included with the supported [Lambda runtime](lambda-runtimes.md) environments\.
+ The \.zip file must be less than 50 MB\. If it's larger than 50 MB, we recommend uploading it to an Amazon Simple Storage Service \(Amazon S3\) bucket\.
+ The \.zip file can't contain libraries written in C or C\+\+\. If your \.zip file contains C\-extension libraries, such as the Pillow \(PIL\) or numpy libraries, we recommend using the AWS Serverless Application Model \(AWS SAM\) command line interface \(CLI\) to build a deployment package\.

This section describes how to create a \.zip file as your deployment package, and then use the \.zip file to deploy your function code to Lambda using the AWS Command Line Interface \(AWS CLI\)\.

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
   bundle install --path vendor/bundle
   ```

   You should see the following output:

   ```
   Fetching gem metadata from https://rubygems.org/..............
   Resolving dependencies...
   Fetching aws-eventstream 1.0.1
   Installing aws-eventstream 1.0.1
   ...
   ```

   The `--path` installs the gems in the project directory instead of the system location, and sets this as the default path for future installations\. To later install gems globally, use the `--system` option\.

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