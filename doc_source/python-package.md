# Deploy Python Lambda functions with \.zip file archives<a name="python-package"></a>

To create a container image to deploy your function code, see [Using container images with Lambda](lambda-images.md)\.

A \.zip file archive is a deployment package that contains your function code and dependencies\. You must create a \.zip file archive if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon Simple Storage Service \(Amazon S3\) bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

If you use the [Lambda console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

You can also use the AWS Serverless Application Model \(AWS SAM\) CLI `build` command to create a deployment package for your Python function code and dependencies\. The AWS SAM CLI also provides an option to build your deployment package inside a Docker image that is compatible with the Lambda execution environment\. For more information, see [Building applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) in the *AWS Serverless Application Model Developer Guide*\.

**Note**  
We recommend using the AWS SAM CLI [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) command to create deployment packages that contain libraries written in C or C\+\+, such as the [Pillow](https://pypi.org/project/Pillow/) library\.

**Topics**
+ [Prerequisites](#python-package-prereqs)
+ [Updating a function with no dependencies](#python-package-codeonly)
+ [Updating a function with additional dependencies](#python-package-dependencies)
+ [With a virtual environment](#python-package-venv)

## Prerequisites<a name="python-package-prereqs"></a>

These instructions assume that you have already created a Lambda function and are updating the deployment package for your function\. If you haven't created a function yet, see [Building Lambda functions with Python](lambda-python.md)\.

To complete the following steps, you need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Updating a function with no dependencies<a name="python-package-codeonly"></a>

To create or update a function with the Lambda API, create a deployment package that contains your function code and upload it with the AWS Command Line Interface \(AWS CLI\)\.

**To update a Python function with no dependencies**

1. Add function code files to the root of your deployment package\.

   ```
   ~/my-function$ zip my-deployment-package.zip lambda_function.py
   ```

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file deployment package to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
   {
       "FunctionName": "mylambdafunction",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
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

## Updating a function with additional dependencies<a name="python-package-dependencies"></a>

If your Lambda function depends on libraries other than the AWS SDK for Python \(Boto3\), install them to a local directory with [pip](https://pypi.org/project/pip/), and include them in your deployment package\.

The following example shows how to create a deployment package that contains the [requests](https://pypi.org/project/requests/) library and upload it to Lambda using the AWS CLI\. The steps assume that you are not using a virtual environment\.

**Note**  
If you are creating a deployment package used in a layer, see [Including library dependencies in a layer](configuration-layers.md#configuration-layers-path)\.

**To update a Python function with dependencies**

1. Install libraries in a `package` directory with `pip`'s `--target` option\.

   ```
   ~/my-function$ pip install --target ./package requests
   ```
**Note**  
To prevent `distutils` errors on [Debian\-based systems](https://github.com/pypa/pip/issues/3826) such as Ubuntu, you may need to pass the `--system` option\.

1. Create a deployment package with the installed libraries at the root\.

   ```
   ~/my-function$ zip -r my-deployment-package.zip ./package/
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

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file deployment package to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
   {
       "FunctionName": "mylambdafunction",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
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

## With a virtual environment<a name="python-package-venv"></a>

In some cases, you may need to use a [virtual environment](https://virtualenv.pypa.io/en/latest) to install dependencies for your function\. This can occur if your function or its dependencies have dependencies on native libraries, or if you used Homebrew to install Python\.

The following example shows how to create a deployment package that contains the [requests](https://pypi.org/project/requests/) library and upload it to Lambda using the AWS CLI\.

**Note**  
If you are creating a deployment package used in a layer, see [Including library dependencies in a layer](configuration-layers.md#configuration-layers-path)\.

**To update a Python function with a virtual environment**

1. Create a virtual environment\.

   ```
   ~/my-function$ virtualenv myvenv
   ```

1. Activate the environment\.

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
   ~/my-function/myvenv/lib/python3.8/site-packages$ zip -r my-deployment-package.zip .
   ```
**Note**  
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

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary \.zip file deployment package to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
   {
       "FunctionName": "mylambdafunction",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
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