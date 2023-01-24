# Deploy Python Lambda functions with \.zip file archives<a name="python-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

To create the deployment package for a \.zip file archive, you can use a built\-in \.zip file archive utility or any other \.zip file utility \(such as [7zip](https://www.7-zip.org/download.html)\) for your command line tool\. Note the following requirements for using a \.zip file as your deployment package:
+ The \.zip file contains your function's code and any dependencies used to run your function's code \(if applicable\) on Lambda\. If your function depends only on standard libraries, or AWS SDK libraries, you don't need to include these libraries in your \.zip file\. These libraries are included with the supported [Lambda runtime](lambda-runtimes.md) environments\.
+ If the \.zip file is larger than 50 MB, we recommend uploading it to your function from an Amazon Simple Storage Service \(Amazon S3\) bucket\.
+ If your deployment package contains native libraries, you can build the deployment package with AWS Serverless Application Model \(AWS SAM\)\. You can use the AWS SAM CLI `sam build` command with the `--use-container` to create your deployment package\. This option builds a deployment package inside a Docker image that is compatible with the Lambda execution environment\. 

  For more information, see [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-build.html) in the *AWS Serverless Application Model Developer Guide*\.
+ You need to build the deployment package to be compatible with this [instruction set architecture](foundation-arch.md) of the function\.
+ Lambda uses POSIX file permissions, so you may need to [ set permissions for the deployment package folder](http://aws.amazon.com/premiumsupport/knowledge-center/lambda-deployment-package-errors/) before you create the \.zip file archive\.

**Note**  
A python package may contain initialization code in the \_\_init\_\_\.py file\. Prior to Python 3\.9, Lambda did not run the \_\_init\_\_\.py code for packages in the function handler’s directory or parent directories\. In Python 3\.9 and later releases, Lambda runs the init code for packages in these directories during initialization\.   
Note that Lambda runs the init code only when the execution environment is first initialized, not for each function invocation in that initialized environment\.

**Topics**
+ [Prerequisites](#python-package-prereqs)
+ [What is a runtime dependency?](#python-package-dependencies)
+ [Deployment package with no dependencies](#python-package-create-package-no-dependency)
+ [Deployment package with dependencies](#python-package-create-package-with-dependency)
+ [Using a virtual environment](#python-package-update-venv)
+ [Deploy your \.zip file to the function](#python-package-upload-code)

## Prerequisites<a name="python-package-prereqs"></a>

You need the AWS Command Line Interface \(AWS CLI\) to call service API operations\. To install the AWS CLI, see [Installing the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) in the AWS Command Line Interface User Guide\.

## What is a runtime dependency?<a name="python-package-dependencies"></a>

A [deployment package](gettingstarted-package.md) is required to create or update a Lambda function with or without runtime dependencies\. The deployment package acts as the source bundle to run your function's code and dependencies \(if applicable\) on Lambda\.

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\.

The following describes a Lambda function without runtime dependencies:
+ If your function's code is in Python 3\.8 or later, and it depends only on standard Python math and logging libraries, you don't need to include the libraries in your \.zip file\. These libraries are included with the Python runtime\.
+ If your function's code depends on the [AWS SDK for Python \(Boto3\)](http://aws.amazon.com/sdk-for-python/), you don't need to include the boto3 library in your \.zip file\. These libraries are included with Python3\.8 and later runtimes\. 

Note: Lambda periodically updates the Boto3 libraries to enable the latest set of features and security updates\. To have full control of the dependencies your function uses, package all of your dependencies with your deployment package\. 

## Deployment package with no dependencies<a name="python-package-create-package-no-dependency"></a>

Create the \.zip file for your deployment package\.

**To create the deployment package**

1. Open a command prompt and create a `my-math-function` project directory\. For example, on macOS:

   ```
   mkdir my-math-function
   ```

1. Navigate to the `my-math-function` project directory\.

   ```
   cd my-math-function
   ```

1. Copy the contents of the [sample Python code from GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/python/example_code/lambda/lambda_handler_basic.py) and save it in a new file named `lambda_function.py`\. Your directory structure should look like this:

   ```
   my-math-function$
   | lambda_function.py
   ```

1. Add the `lambda_function.py` file to the root of the \.zip file\.

   ```
   zip my-deployment-package.zip lambda_function.py
   ```

   This generates a `my-deployment-package.zip` file in your project directory\. The command produces the following output:

   ```
   adding: lambda_function.py (deflated 50%)
   ```

## Deployment package with dependencies<a name="python-package-create-package-with-dependency"></a>

Create the \.zip file for your deployment package\.

**To create the deployment package**

1. Open a command prompt and create a `my-sourcecode-function` project directory\. For example, on macOS:

   ```
   mkdir my-sourcecode-function
   ```

1. Navigate to the `my-sourcecode-function` project directory\.

   ```
   cd my-sourcecode-function
   ```

1. Copy the contents of the following sample Python code and save it in a new file named `lambda_function.py`:

   ```
   import requests
   def lambda_handler(event, context):   
       response = requests.get("https://www.example.com/")
       print(response.text)
       return response.text
   ```

   Your directory structure should look like this:

   ```
   my-sourcecode-function$
   | lambda_function.py
   ```

1. Install the requests library to a new `package` directory\.

   ```
   pip install --target ./package requests
   ```

1. Create a deployment package with the installed library at the root\.

   ```
   cd package
   zip -r ../my-deployment-package.zip .
   ```

   This generates a `my-deployment-package.zip` file in your project directory\. The command produces the following output:

   ```
   adding: chardet/ (stored 0%)
   adding: chardet/enums.py (deflated 58%)
   ...
   ```

1. Add the `lambda_function.py` file to the root of the zip file\.

   ```
   cd ..
   zip my-deployment-package.zip lambda_function.py
   ```

## Using a virtual environment<a name="python-package-update-venv"></a>

**To update a Python function using a virtual environment**

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
   ~/my-function$cd myvenv/lib/python3.8/site-packages
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

## Deploy your \.zip file to the function<a name="python-package-upload-code"></a>

To deploy the new code to your function, you upload the new \.zip file deployment package\. You can use the [Lambda console](configuration-function-zip.md#configuration-function-update) to upload a \.zip file to the function, or you can use the [UpdateFunctionCode](API_UpdateFunctionCode.md) CLI command\.

The following example uploads a file named my\-deployment\-package\.zip\. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) file prefix to upload the binary \.zip file to Lambda\. 

```
~/my-function$ aws lambda update-function-code --function-name MyLambdaFunction --zip-file fileb://my-deployment-package.zip
  {
  "FunctionName": "mylambdafunction",
  "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:mylambdafunction",
  "Runtime": "python3.9",
  "Role": "arn:aws:iam::123456789012:role/lambda-role",
  "Handler": "lambda_function.lambda_handler",
  "CodeSize": 5912988,
  "CodeSha256": "A2P0NUWq1J+LtSbkuP8tm9uNYqs1TAa3M76ptmZCw5g=",
  "Version": "$LATEST",
  "RevisionId": "5afdc7dc-2fcb-4ca8-8f24-947939ca707f",
  ...
  }
```