# Deploy Python Lambda functions with \.zip file archives<a name="python-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip files\.

You can use a built\-in ZIP archive utility, or any other ZIP utility \(such as [7zip](https://www.7-zip.org/download.html)\) for your command line tool to create a deployment package\.
+ The \.zip file must contain your function's code, and any dependencies used to run your function's code \(if applicable\) on Lambda\. If your function depends only on standard libraries, or AWS SDK libraries, you do not need to include the libraries in your \.zip file\. These libraries are included with our supported [Lambda runtime](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html) environments\.
+ If your \.zip file is larger than 50 MB, we recommend uploading it to an Amazon S3 bucket\. For more information, see [Using other AWS services to build a deployment package](gettingstarted-package.md)\.
+ If your \.zip file contains C\-extension libraries, such as the Pillow \(PIL\) library, we recommend using the AWS SAM CLI to build a deployment package\. For more information, see [Lambda deployment packages](gettingstarted-package.md)\.

This page describes how to create a \.zip file as your deployment package, and then use the \.zip file to deploy your function code to Lambda using the AWS Command Line Interface \(AWS CLI\)\. To upload your \.zip file on the Lambda console, see [Deployment packages](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-package-zip)\.

**Topics**
+ [Prerequisites](#python-package-prereqs)
+ [Updating a function with no dependencies](#python-package-codeonly)
+ [Updating a function with additional dependencies](#python-package-dependencies)
+ [Using a virtual environment](#python-package-venv)

## Prerequisites<a name="python-package-prereqs"></a>

The following steps assume that you have created a Lambda function, and are updating the deployment package for your function\. If you haven't created a function yet, see [Building Lambda functions with Python](lambda-python.md)\.

The AWS Command Line Interface \(AWS CLI\) is an open source tool that enables you to interact with AWS services using commands in your command\-line shell\. To complete the steps in this section, you need the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## Updating a function with no dependencies<a name="python-package-codeonly"></a>

The following steps show how to create a deployment package that contains only your function code, and upload it to Lambda using the AWS CLI\.

**To update a Python function with no dependencies**

1. Add function code files to the root of your deployment package\.

   ```
   zip my-deployment-package.zip lambda_function.py
   ```

1. Use the [update\-function\-code](https://docs.aws.amazon.com/cli/latest/reference/lambda/update-function-code.html) command with the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file to Lambda and update the function code\.

   ```
   aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
   ```

   You should see the following output:

   ```
   {
       "FunctionName": "mylambdafunction",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:mylambdafunction",
       "Runtime": "python3.8",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "lambda_function.lambda_handler",
       "CodeSize": 815,
       "CodeSha256": "GcZ05oeHoJi61VpQj7vCLPs8DwCXmX5sE/fE2IHsizc=",
       "Version": "$LATEST",
       "RevisionId": "d1e983e3-ca8e-434b-8dc1-7add83d72ebd",
       ...
   }
   ```

The Lambda function in the last step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [AWS Lambda function handler in Python](python-handler.md)\.

## Updating a function with additional dependencies<a name="python-package-dependencies"></a>

If your Lambda function depends on libraries other than the [AWS SDK for Python \(Boto3\)](http://aws.amazon.com/sdk-for-python/), install the libraries to a local directory with [pip](https://pypi.org/project/pip/), and include them in your deployment package \(\.zip file\)\. 

**Note**  
Make sure that the files and directories in the deployment package have their permissions set to be globally readable, so that Lambda can import your Python modules at runtime\.

 The following steps show how to install the [requests](https://pypi.org/project/requests/) library, create a deployment package, and upload it to Lambda using the AWS CLI\. The steps assume that you are not using a virtual environment\.  It also assumes that your function code uses Python 3\.8 and the [`python3.8` Lambda runtime](lambda-runtimes.md)\.

**Note**  
If you are creating a deployment package used in a layer, see [Include library dependencies in a layer](configuration-layers.md#configuration-layers-path)\.

**To update a Python function with dependencies**

1. Install libraries in a `package` directory with `pip`'s `--target` option\.

   ```
   pip install --target ./package requests
   ```
**Note**  
To prevent `distutils` errors on [Debian\-based systems](https://github.com/pypa/pip/issues/3826) such as Ubuntu, you may need to pass the `--system` option\.

1. Navigate to the `package` directory\.

   ```
   cd package
   ```

1. Create a deployment package with the installed libraries at the root\.

   ```
   zip -r ../my-deployment-package.zip .
   ```

   The last command saves the deployment package to the root of the `my-function` directory\.

1. Navigate back to the `my-function` directory\.

   ```
   cd.. 
   ```

1. Add function code files to the root of your deployment package\.

   ```
   zip -g my-deployment-package.zip lambda_function.py
   ```

   After you complete this step, you should have the following directory structure:

   ```
   my-deployment-package.zip$
     │ lambda_function.py
     │ __pycache__
     │ certifi/ 
     │ certifi-2020.6.20.dist-info/ 
     │ chardet/ 
     │ chardet-3.0.4.dist-info/ 
     ...
   ```

1. Use the [update\-function\-code](https://docs.aws.amazon.com/cli/latest/reference/lambda/update-function-code.html) command with the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file to Lambda and update the function code\.

   ```
   aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
   ```

   You should see the following output:

   ```
   {
       "FunctionName": "mylambdafunction",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:mylambdafunction",
       "Runtime": "python3.8",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "lambda_function.lambda_handler",
       "CodeSize": 2269409,
       "CodeSha256": "GcZ05oeHoJi61VpQj7vCLPs8DwCXmX5sE/fE2IHsizc=",
       "Version": "$LATEST",
       "RevisionId": "a9c05ffd-8ad6-4d22-b6cd-d34a00c1702c",
       ...
   }
   ```

The Lambda function in the last step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [AWS Lambda function handler in Python](python-handler.md)\.

## Using a virtual environment<a name="python-package-venv"></a>

If your Lambda function depends on libraries other than the [AWS SDK for Python \(Boto3\)](http://aws.amazon.com/sdk-for-python/), install the libraries to a local directory with [pip](https://pypi.org/project/pip/), and include them in your deployment package \(\.zip file\)\. 

 The following steps show how to install the [requests](https://pypi.org/project/requests/) library, create a deployment package, and upload it to Lambda using the AWS CLI\. The steps assume that you are using the [virtualenv](https://docs.python.org/3/library/venv.html) module for a virtual environment\.  It also assumes that your function code uses Python 3\.8 and the [`python3.8` Lambda runtime](lambda-runtimes.md)\.

**Note**  
If you are creating a deployment package used in a layer, see [Include library dependencies in a layer](configuration-layers.md#configuration-layers-path)\.

**To update a Python function with a virtual environment**

1. Create a virtual environment\.

   ```
   virtualenv myvenv
   ```
**Note**  
The [virtualenv](https://docs.python.org/3/library/venv.html) module uses Python 2\.7 by default\. You may need to add a local export path to your command line profile, such as `export VIRTUALENV_PYTHON=/usr/bin/python3.8` when using the *virtualenv* module with Python 3 and pip 3\. 

1. Activate the environment\.

   ```
   source myvenv/bin/activate
   ```

1. Install libraries with pip\.

   ```
   pip install requests
   ```

1. Deactivate the virtual environment\.

   ```
   deactivate
   ```

1. Create a deployment package with the installed libraries at the root\. 

   ```
   cd myvenv/lib/python3.8/site-packages
   zip -r ../../../../my-deployment-package.zip .
   ```

   The last command saves the deployment package to the root of the `my-function` directory\.
**Tip**  
A library may appear in `site-packages` or `dist-packages` and the first folder `lib` or `lib64`\. You can use the `pip show` command to locate a specific package\.

1. Add function code files to the root of your deployment package\.

   ```
   cd ../../../../
   zip -g my-deployment-package.zip lambda_function.py
   ```

   After you complete this step, you should have the following directory structure:

   ```
   my-deployment-package.zip$
     │ lambda_function.py
     │ __pycache__
     │ certifi/ 
     │ certifi-2020.6.20.dist-info/ 
     │ chardet/ 
     │ chardet-3.0.4.dist-info/ 
     ...
   ```

1. Use the [update\-function\-code](https://docs.aws.amazon.com/cli/latest/reference/lambda/update-function-code.html) command with the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file to Lambda and update the function code\.

   ```
   aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
   ```

   You should see the following output:

   ```
   {
       "FunctionName": "mylambdafunction",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:mylambdafunction",
       "Runtime": "python3.8",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "lambda_function.lambda_handler",
       "CodeSize": 5912988,
       "CodeSha256": "A2P0NUWq1J+LtSbkuP8tm9uNYqs1TAa3M76ptmZCw5g=",
       "Version": "$LATEST",
       "RevisionId": "5afdc7dc-2fcb-4ca8-8f24-947939ca707f",
       ...
   }
   ```

The Lambda function in the last step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [AWS Lambda function handler in Python](python-handler.md)\.