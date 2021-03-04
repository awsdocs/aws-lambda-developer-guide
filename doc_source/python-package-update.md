# Updating a Lambda function in Python 3\.8<a name="python-package-update"></a>

This tutorial guides you through the process of installing a Python library, creating a deployment package, and updating a Lambda function in Python 3\.8 using the AWS Command Line Interface \(AWS CLI\)\.

The following steps assume that you have created a Lambda function and are updating the \.zip file used as your deployment package\. If you haven't created a function yet, see [Tutorial: Creating a Lambda function in Python 3\.8](python-package-create.md)\.

**Topics**
+ [Prerequisites](#python-package-update-prereqs)
+ [Updating a function without runtime dependencies](#python-package-update-codeonly)
+ [Updating a function with runtime dependencies](#python-package-update-dependencies)
+ [Using a virtual environment](#python-package-update-venv)

## Prerequisites<a name="python-package-update-prereqs"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## Updating a function without runtime dependencies<a name="python-package-update-codeonly"></a>

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\. For more information, see [What is a runtime dependency?](python-package.md)\.

For more information, see [What is a runtime dependency?](python-package.md#python-package-dependencies)\.

The following steps show how to create a deployment package that contains only your function code, and upload it to Lambda using the AWS CLI\.

**To update a Python function without runtime dependencies**

1. Add function code files to the root of your deployment package\.

   ```
   ~/my-function$ zip my-deployment-package.zip lambda_function.py
   ```

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
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

The Lambda function in this step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [Naming](python-handler.md#naming) in **AWS Lambda function handler in Python**\.

## Updating a function with runtime dependencies<a name="python-package-update-dependencies"></a>

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\. For more information, see [What is a runtime dependency?](python-package.md)\.

For more information, see [What is a runtime dependency?](python-package.md#python-package-dependencies)\. 

 The following steps show how to install the [requests](https://pypi.org/project/requests/) library, create a deployment package, and upload it to Lambda using the AWS CLI\. The steps assume that you are not using a virtual environment\.  It also assumes that your function code uses Python 3\.8 and the [`python3.8` Lambda runtime](lambda-runtimes.md)\.

**Note**  
If you are creating a deployment package used in a layer, see [Include library dependencies in a layer](configuration-layers.md#configuration-layers-path)\.

**To update a Python function with dependencies**

1. Install libraries in a `package` directory with `pip`'s `--target` option\.

   ```
   ~/my-function$ pip install --target ./package requests
   ```
**Note**  
To prevent `distutils` errors on [Debian\-based systems](https://github.com/pypa/pip/issues/3826) such as Ubuntu, you may need to pass the `--system` option\.

1. Navigate to the `package` directory\.

   ```
   cd package
   ```

1. Create a deployment package with the installed libraries at the root\.

   ```
   ~/my-function$ zip -r ../my-deployment-package.zip .
   ```

   The last command saves the deployment package to the root of the `my-function` directory\.

1. Navigate back to the `my-function` directory\.

   ```
   cd ..
   ```

1. Add function code files to the root of your deployment package\.

   ```
   ~/my-function$ zip -g my-deployment-package.zip lambda_function.py
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
   ~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
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

The Lambda function in this step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [Naming](python-handler.md#naming) in **AWS Lambda function handler in Python**\.

## Using a virtual environment<a name="python-package-update-venv"></a>

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\. For more information, see [What is a runtime dependency?](python-package.md)\.

 For more information, see [What is a runtime dependency?](python-package.md#python-package-dependencies)\. 

 The following steps show how to install the [requests](https://pypi.org/project/requests/) library, create a deployment package, and upload it to Lambda using the AWS CLI\.  It also assumes that your function code uses Python 3\.8 and the [`python3.8` Lambda runtime](lambda-runtimes.md)\.

**Note**  
If you are creating a deployment package used in a layer, see [Include library dependencies in a layer](configuration-layers.md#configuration-layers-path)\.

**To update a Python function with a virtual environment**

1. Activate the virtual environment\. For example: 

   ```
   ~/my-function$ source myvenv/bin/activate
   ```

1. Install libraries with pip\.

   ```
   (myvenv) ~/my-function$ pip install requests
   ```

1. Deactivate the virtual environment\.

   ```
   (myvenv) ~/my-function$ deactivate
   ```

1. Create a deployment package with the installed libraries at the root\. 

   ```
   ~/my-function$ cd myvenv/lib/python3.8/site-packages
   ~/my-function/myvenv/lib/python3.8/site-packages$ cd myvenv/lib/python3.8/site-packages
   zip -r ../../../../my-deployment-package.zip .
   ```

   The last command saves the deployment package to the root of the `my-function` directory\.
**Tip**  
A library may appear in `site-packages` or `dist-packages` and the first folder `lib` or `lib64`\. You can use the `pip show` command to locate a specific package\.

1. Add function code files to the root of your deployment package\.

   ```
   ~/my-function/myvenv/lib/python3.8/site-packages$ cd ../../../../
   ~/my-function$ zip -g my-deployment-package.zip lambda_function.py
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

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
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

The Lambda function in this step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [Naming](python-handler.md#naming) in **AWS Lambda function handler in Python**\.