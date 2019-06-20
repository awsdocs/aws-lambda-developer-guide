# AWS Lambda Deployment Package in Python<a name="lambda-python-how-to-create-deployment-package"></a>

A deployment package is a ZIP archive that contains your function code and dependencies\. You need to create a deployment package if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

If you use the Lambda [console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

**Note**  
You can use the AWS SAM CLI `build` command to create a deployment package for your Python function code and dependencies\. See [Building Applications with Dependencies](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) in the AWS SAM Developer Guide for instructions\.

**Topics**
+ [Updating a Function with No Dependencies](#python-package-codeonly)
+ [Updating a Function with Additional Dependencies](#python-package-dependencies)
+ [With a Virtual Environment](#python-package-venv)

## Updating a Function with No Dependencies<a name="python-package-codeonly"></a>

To create or update a function with the Lambda API, create an archive that contains your function code and upload it with the AWS CLI\.

**To update a Python function with no dependencies**

1. Create a ZIP archive\.

   ```
   ~/my-function$ zip function.zip function.py
   ```

1. Use the `update-function-code` command to upload the package\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name python37 --zip-file fileb://function.zip
   {
       "FunctionName": "python37",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:python37",
       "Runtime": "python3.7",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 815,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-20T20:41:16.647+0000",
       "CodeSha256": "GcZ05oeHoJi61VpQj7vCLPs8DwCXmX5sE/fE2IHsizc=",
       "Version": "$LATEST",
       "VpcConfig": {
           "SubnetIds": [],
           "SecurityGroupIds": [],
           "VpcId": ""
       },
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "d1e983e3-ca8e-434b-8dc1-7add83d72ebd"
   }
   ```

## Updating a Function with Additional Dependencies<a name="python-package-dependencies"></a>

If your function depends on libraries other than the SDK for Python \(Boto 3\), install them to a local directory with [pip](https://pypi.org/project/pip/), and include them in your deployment package\.

**To update a Python function with dependencies**

1. Create a directory for dependencies\.

   ```
   ~/my-function$ mkdir package
   ```

1. Install libraries in the package directory with the `--target` option\.

   ```
   ~/my-function$ cd package
   ~/my-function/package$ pip install Pillow --target .
   Collecting Pillow
     Using cached https://files.pythonhosted.org/packages/62/8c/230204b8e968f6db00c765624f51cfd1ecb6aea57b25ba00b240ee3fb0bd/Pillow-5.3.0-cp37-cp37m-manylinux1_x86_64.whl
   Installing collected packages: Pillow
   Successfully installed Pillow-5.3.0
   ```

1. Create a ZIP archive\.

   ```
   package$ zip -r9 ../function.zip .
     adding: PIL/ (stored 0%)
     adding: PIL/.libs/ (stored 0%)
     adding: PIL/.libs/libfreetype-7ce95de6.so.6.16.1 (deflated 65%)
     adding: PIL/.libs/libjpeg-3fe7dfc0.so.9.3.0 (deflated 72%)
     adding: PIL/.libs/liblcms2-a6801db4.so.2.0.8 (deflated 67%)
   ...
   ```

1. Add your function code to the archive\.

   ```
   ~/my-function/package$ cd ../
   ~/my-function$ zip -g function.zip function.py
     adding: function.py (deflated 56%)
   ```

1. Update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name python37 --zip-file fileb://function.zip
   {
       "FunctionName": "python37",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:python37",
       "Runtime": "python3.7",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 2269409,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-20T20:51:35.871+0000",
       "CodeSha256": "GcZ05oeHoJi61VpQj7vCLPs8DwCXmX5sE/fE2IHsizc=",
       "Version": "$LATEST",
       "VpcConfig": {
           "SubnetIds": [],
           "SecurityGroupIds": [],
           "VpcId": ""
       },
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "a9c05ffd-8ad6-4d22-b6cd-d34a00c1702c"
   }
   ```

## With a Virtual Environment<a name="python-package-venv"></a>

In some cases, you may need to use a [virtual environment](https://virtualenv.pypa.io/en/latest/) to install dependencies for your function\. This can occur if your function or its dependencies have dependencies on native libraries, or if you used Homebrew to install Python\.

**To update a Python function with a virtual environment**

1. Create a virtual environment\.

   ```
   ~/my-function$ virtualenv v-env
   Using base prefix '~/.local/python-3.7.0'
   New python executable in v-env/bin/python3.7
   Also creating executable in v-env/bin/python
   Installing setuptools, pip, wheel...
   done.
   ```

1. Activate the environment\.

   ```
   ~/my-function$ source v-env/bin/activate
   (v-env) ~/my-function$
   ```

   For the Windows command line, the activation script is in the Scripts directory\.

   ```
   > v-env\Scripts\activate.bat
   ```

1. Install libraries with pip\.

   ```
   ~/my-function$ pip install Pillow
   Collecting Pillow
     Using cached https://files.pythonhosted.org/packages/62/8c/230204b8e968f6db00c765624f51cfd1ecb6aea57b25ba00b240ee3fb0bd/Pillow-5.3.0-cp37-cp37m-manylinux1_x86_64.whl
   Installing collected packages: Pillow
   Successfully installed Pillow-5.3.0
   ```

1. Deactivate the virtual environment\.

   ```
   (v-env)~/my-function$ deactivate
   ```

1. Create a ZIP archive with the contents of the library\.

   ```
   ~/my-function$ cd v-env/lib/python3.7/site-packages/  
   ~/my-function/v-env/lib/python3.7/site-packages$ zip -r9 ../../../../function.zip .
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
   ~/my-function/v-env/lib/python3.7/site-packages$ cd ../../../../
   ~/my-function$ zip -g function.zip function.py
     adding: function.py (deflated 56%)
   ```

1. Update the function code\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name python37 --zip-file fileb://function.zip
   {
       "FunctionName": "python37",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:python37",
       "Runtime": "python3.7",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "function.handler",
       "CodeSize": 5912988,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-20T21:08:26.326+0000",
       "CodeSha256": "A2P0NUWq1J+LtSbkuP8tm9uNYqs1TAa3M76ptmZCw5g=",
       "Version": "$LATEST",
       "VpcConfig": {
           "SubnetIds": [],
           "SecurityGroupIds": [],
           "VpcId": ""
       },
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "5afdc7dc-2fcb-4ca8-8f24-947939ca707f"
   }
   ```