# AWS Lambda deployment package in Python<a name="python-package"></a>

A deployment package is a ZIP archive that contains your function code and dependencies\. You need to create a deployment package if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

If you use the Lambda [console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

**Note**  
You can use the AWS SAM CLI `build` command to create a deployment package for your Python function code and dependencies\. The AWS SAM CLI also provides an option to build your deployment package inside a Docker image that is compatible with the Lambda execution environment\. See [Building applications with dependencies](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) in the AWS SAM Developer Guide for instructions\.

**Topics**
+ [Prerequisites](#python-package-prereqs)
+ [Updating a function with no dependencies](#python-package-codeonly)
+ [Updating a function with additional dependencies](#python-package-dependencies)
+ [With a virtual environment](#python-package-venv)

## Prerequisites<a name="python-package-prereqs"></a>

These instructions assume that you already have a function\. If you haven't created a function yet, see [Building Lambda functions with Python](lambda-python.md)\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Updating a function with no dependencies<a name="python-package-codeonly"></a>

To create or update a function with the Lambda API, create an archive that contains your function code and upload it with the AWS CLI\.

**To update a Python function with no dependencies**

1. Create a ZIP archive\.

   ```
   ~/my-function$ zip function.zip lambda_function.py
     adding: lambda_function.py (deflated 17%)
   ```

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary ZIP deployment package to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
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

If your function depends on libraries other than the SDK for Python \(Boto3\), install them to a local directory with [pip](https://pypi.org/project/pip/), and include them in your deployment package\.

**Note**  
For libraries that use extension modules written in C or C\+\+, build your deployment package in an Amazon Linux environment\. You can use the [SAM CLI build command](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html), which uses Docker, or build your deployment package on Amazon EC2 or AWS CodeBuild\.

The following example shows how to create a deployment package that includes a common graphics library named Pillow\.

**To update a Python function with dependencies**

1. Install libraries in a new, project\-local `package` directory with `pip`'s `--target` option\.

   ```
   ~/my-function$ pip install --target ./package Pillow
   Collecting Pillow
     Using cached https://files.pythonhosted.org/packages/62/8c/230204b8e968f6db00c765624f51cfd1ecb6aea57b25ba00b240ee3fb0bd/Pillow-5.3.0-cp37-cp37m-manylinux1_x86_64.whl
   Installing collected packages: Pillow
   Successfully installed Pillow-5.3.0
   ```
**Note**  
In order for `--target` to work on [Debian\-based systems](https://github.com/pypa/pip/issues/3826) like Ubuntu, you may also need to pass the `--system` flag to prevent `distutils` errors\.

1. Create a ZIP archive of the dependencies\.

   ```
   ~/my-function$ cd package
   ~/my-function/package$ zip -r9 ${OLDPWD}/function.zip .
     adding: PIL/ (stored 0%)
     adding: PIL/.libs/ (stored 0%)
     adding: PIL/.libs/libfreetype-7ce95de6.so.6.16.1 (deflated 65%)
     adding: PIL/.libs/libjpeg-3fe7dfc0.so.9.3.0 (deflated 72%)
     adding: PIL/.libs/liblcms2-a6801db4.so.2.0.8 (deflated 67%)
   ...
   ```

1. Add your function code to the archive\.

   ```
   ~/my-function/package$ cd $OLDPWD
   ~/my-function$ zip -g function.zip lambda_function.py
     adding: lambda_function.py (deflated 56%)
   ```

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary ZIP deployment package to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
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

In some cases, you may need to use a [virtual environment](https://virtualenv.pypa.io/en/latest/) to install dependencies for your function\. This can occur if your function or its dependencies have dependencies on native libraries, or if you used Homebrew to install Python\.

**To update a Python function with a virtual environment**

1. Create a virtual environment\.

   ```
   ~/my-function$ virtualenv v-env
   Using base prefix '~/.local/python-3.7.0'
   New python executable in v-env/bin/python3.8
   Also creating executable in v-env/bin/python
   Installing setuptools, pip, wheel...
   done.
   ```
**Note**  
For Python 3\.3 and newer, you can use the built\-in [venv module](https://docs.python.org/3/library/venv.html) to create a virtual environment, instead of installing `virtualenv`\.  

   ```
   ~/my-function$ python3 -m venv v-env
   ```

1. Activate the environment\.

   ```
   ~/my-function$ source v-env/bin/activate
   (v-env) ~/my-function$
   ```

1. Install libraries with pip\.

   ```
   (v-env) ~/my-function$ pip install Pillow
   Collecting Pillow
     Using cached https://files.pythonhosted.org/packages/62/8c/230204b8e968f6db00c765624f51cfd1ecb6aea57b25ba00b240ee3fb0bd/Pillow-5.3.0-cp37-cp37m-manylinux1_x86_64.whl
   Installing collected packages: Pillow
   Successfully installed Pillow-5.3.0
   ```

1. Deactivate the virtual environment\.

   ```
   (v-env) ~/my-function$ deactivate
   ```

1. Create a ZIP archive with the contents of the library\.

   ```
   ~/my-function$ cd v-env/lib/python3.8/site-packages
   ~/my-function/v-env/lib/python3.8/site-packages$ zip -r9 ${OLDPWD}/function.zip .
     adding: easy_install.py (deflated 17%)
     adding: PIL/ (stored 0%)
     adding: PIL/.libs/ (stored 0%)
     adding: PIL/.libs/libfreetype-7ce95de6.so.6.16.1 (deflated 65%)
     adding: PIL/.libs/libjpeg-3fe7dfc0.so.9.3.0 (deflated 72%)
   ...
   ```

   Depending on the library, dependencies may appear in either `site-packages` or `dist-packages`, and the first folder in the virtual environment may be `lib` or `lib64`\. You can use the `pip show` command to locate a specific package\.

1. Add your function code to the archive\.

   ```
   ~/my-function/v-env/lib/python3.8/site-packages$ cd $OLDPWD
   ~/my-function$ zip -g function.zip lambda_function.py
     adding: lambda_function.py (deflated 56%)
   ```

1. Use the [fileb://](https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-parameters-file.html#cli-usage-parameters-file-binary) prefix to upload the binary ZIP deployment package to Lambda and update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
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