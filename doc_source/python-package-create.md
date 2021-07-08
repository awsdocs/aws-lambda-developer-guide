# Tutorial: Creating a Lambda function in Python 3\.8<a name="python-package-create"></a>

This tutorial guides you through building the code and assets to create a Lambda function in Python 3\.8 using the AWS Command Line Interface \(AWS CLI\)\.

**Topics**
+ [Prerequisites](#python-package-create-prereqs)
+ [Creating a function without runtime dependencies](#python-package-create-no-dependency)
+ [Creating a function with runtime dependencies](#python-package-create-with-dependency)

## Prerequisites<a name="python-package-create-prereqs"></a>

This section describes the tools and resources required to complete the steps in the tutorial\.

### Install the AWS CLI<a name="python-package-create-prereqs-aws-cli"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

### Create an execution role<a name="python-package-create-executionrole"></a>

Your Lambda function's [execution role](lambda-intro-execution-role.md) is an AWS Identity and Access Management \(IAM\) role that grants your function permission to access AWS services and resources\. Create an execution role in IAM with [Invoke](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html) permission\.

**To create the execution role**

1. Open a command prompt and use the [create\-role](https://docs.aws.amazon.com/cli/latest/reference/iam/create-role.html) command to create an execution role named `lambda-ex`\.

------
#### [ macOS/Linux ]

   ```
   aws iam create-role --role-name lambda-ex --assume-role-policy-document '{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}'
   ```

------
#### [ Windows ]

   ```
   aws iam create-role --role-name lambda-ex --assume-role-policy-document "{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}"
   ```

------

   This command produces the following output\. Save the value returned in `Arn`\.

   ```
   {
           "Role": {
               "Path": "/",
               "RoleName": "lambda-ex",
               "RoleId": "AROAWNZPPVHULXRJXQJD5",
               "Arn": "arn:aws:iam::your-account-id:role/lambda-ex",
               "CreateDate": "2021-01-05T18:00:30Z",
               "AssumeRolePolicyDocument": {
                   "Version": "2012-10-17",
                   "Statement": [
                       {
                           "Effect": "Allow",
                           "Principal": {
                               "Service": "lambda.amazonaws.com"
                           },
                           "Action": "sts:AssumeRole"
                       }
                   ]
               }
           }
       }
   ```

1. Use the [attach\-role\-policy](https://docs.aws.amazon.com/cli/latest/reference/iam/attach-role-policy.html) command to add `AWSLambdaBasicExecutionRole` permissions to the role\.

   ```
   $ aws iam attach-role-policy --role-name lambda-ex --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
   ```

   This command produces no output\.

## Creating a function without runtime dependencies<a name="python-package-create-no-dependency"></a>

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\.

 For more information, see [What is a runtime dependency?](python-package.md#python-package-dependencies)

This section describes how to create a Lambda function without runtime dependencies\.

**Topics**
+ [Overview](#python-package-create-about-no-dependency)
+ [Create the deployment package](#python-package-create-package-no-dependency)
+ [Create the Lambda function](#python-package-create-createfunction-no-dependency)
+ [Invoke the Lambda function](#python-package-create-invokefunction-no-dependency)
+ [What's next?](#python-package-create-next-no-dependency)
+ [Clean up your resources](#python-package-create-cleanup-no-dependency)

### Overview<a name="python-package-create-about-no-dependency"></a>

In this tutorial, you use the [sample code from the AWS SDK for Python \(Boto3\) project on GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/python/example_code/lambda/boto_client_examples/lambda_handler_basic.py) to create a Lambda function using the AWS CLI\. You'll learn how to:
+ Set up the directory structure of a deployment package \(\.zip file\)\.
+ Create a deployment package for a Lambda function without any runtime dependencies\.
+ Use the AWS CLI to upload the deployment package and create the Lambda function\.
+ Invoke the Lambda function to return a mathematical calculation\.

The sample code contains standard math and logging Python libraries, which are used to return a calculation based on user input\. Standard Python libraries are included with the `python3.8` [runtime](lambda-runtimes.md)\. Although the function's code doesn't depend on any other Python libraries and has no additional application dependencies, Lambda still requires a deployment package to create a function\.

### Create the deployment package<a name="python-package-create-package-no-dependency"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

Create the \.zip file that Lambda uses as your deployment package\.

**To create the deployment package**

1. Open a command prompt and create a `my-math-function` project directory\. For example, on macOS:

   ```
   mkdir my-math-function
   ```

1. Navigate to the `my-math-function` project directory\.

   ```
   cd my-math-function
   ```

1. Copy the contents of the [sample Python code from GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/python/example_code/lambda/boto_client_examples/lambda_handler_basic.py) and save it in a new file named `lambda_function.py`\. Your directory structure should look like this:

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

### Create the Lambda function<a name="python-package-create-createfunction-no-dependency"></a>

Lambda needs to know the [runtime](lambda-runtimes.md) environment to use for your function's code, the [handler](python-handler.md) in your function code, and the [execution role](lambda-intro-execution-role.md) that it can use to invoke your function\.

Create the Lambda function using the execution role and deployment package that you created in the previous steps\.

**To create the function**

1. Navigate to the `my-math-function` project directory\.

   ```
   cd my-math-function
   ```

1. Create a function named `my-math-function`\. Substitute the value for `role` with the `Arn` you copied in previous steps\.

   ```
   aws lambda create-function --function-name my-math-function --zip-file fileb://my-deployment-package.zip --handler lambda_function.lambda_handler --runtime python3.8 --role arn:aws:iam::your-account-id:role/lambda-ex
   ```

   This command produces the following output:

   ```
   {
       "FunctionName": "my-math-function",
       "FunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:my-math-function",
       "Runtime": "python3.8",
       "Role": "arn:aws:iam::123456789012:role/lambda-ex",
       "Handler": "lambda_function.lambda_handler",
       "CodeSize": 753,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2021-01-05T18:39:44.847+0000",
       "CodeSha256": "82RtIE7p1ET5Od6bk4xSleJbUybUnZX52m92x/fEH84=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "PassThrough"
       },
       "RevisionId": "589e5115-f3c0-446c-bc62-4e05cf0a3c85",
       "State": "Active",
       "LastUpdateStatus": "Successful"
   }
   ```

   The Lambda function in this step uses a function handler of `lambda_function.lambda_handler`\. For more information about function handler naming conventions, see [Naming](python-handler.md#naming) in **AWS Lambda function handler in Python**\.

### Invoke the Lambda function<a name="python-package-create-invokefunction-no-dependency"></a>

Invoke the Lambda function [synchronously](invocation-sync.md) using the event input defined for the sample code\. For more information, see [How it works](python-handler.md#python-handler-how) in **AWS Lambda function handler in Python**\.

**To invoke the function**
+ Use the [invoke](https://docs.aws.amazon.com/cli/latest/reference/lambda/invoke.html) command\.

  ```
  aws lambda invoke \
    --function-name my-math-function \
        --cli-binary-format raw-in-base64-out \
            --payload '{"action": "square","number": 3}' output.txt
  ```

  The cli\-binary\-format option is required if you are using AWS CLI version 2\. You can also configure this option in your [ AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cliv2-migration.html#cliv2-migration-binaryparam)

  This command produces the following output:

  ```
  {
      "StatusCode": 200,
      "ExecutedVersion": "$LATEST"
  }
  ```

  For the `RequestResponse` invocation type, the status code is `200`\. For more information, see the [Invoke](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html#API_Invoke_ResponseSyntax) API reference\.

  You should see the following mathematical calculation in `output.txt`:

  ```
  {"result": 9}
  ```

### What's next?<a name="python-package-create-next-no-dependency"></a>
+ Learn how to update your Lambda function, see [Updating a Lambda function in Python 3\.8](python-package-update.md)\.
+ Learn how to show logging events for your Lambda function, see [AWS Lambda function logging in Python](python-logging.md)\.
+ Explore other [AWS SDK examples in Python on GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/master/python/example_code/lambda)\.

### Clean up your resources<a name="python-package-create-cleanup-no-dependency"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**
+ Use the [delete\-function](https://docs.aws.amazon.com/cli/latest/reference/lambda/delete-function.html) command\.

  ```
  aws lambda delete-function --function-name my-function
  ```

  This command produces no output\.

**To delete the execution role policy**
+ Use the [delete\-policy](https://docs.aws.amazon.com/cli/latest/reference/iam/delete-policy.html) command\.

  ```
  aws iam delete-role-policy --role-name lambda-ex --policy-name AWSLambdaBasicExecutionRole
  ```

**To delete the execution role**
+ Use the [delete\-role](https://docs.aws.amazon.com/cli/latest/reference/iam/delete-role.html) command\.

  ```
  aws iam delete-role --role-name lambda-ex
  ```

## Creating a function with runtime dependencies<a name="python-package-create-with-dependency"></a>

A dependency can be any package, module or other assembly dependency that is not included with the [Lambda runtime](lambda-runtimes.md) environment for your function's code\.

For more information, see [What is a runtime dependency?](python-package.md#python-package-dependencies)

This section describes how to create a Lambda function with runtime dependencies\.

**Topics**
+ [Overview](#python-package-create-about)
+ [Create the deployment package](#python-package-create-package-with-dependency)
+ [Create the Lambda function](#python-package-create-createfunction-with-dependency)
+ [Invoke the Lambda function](#python-package-create-invokefunction-with-dependency)
+ [What's next?](#python-package-create-next-with-dependency)
+ [Clean up your resources](#python-package-create-cleanup-with-dependency)

### Overview<a name="python-package-create-about"></a>

In this tutorial, you use sample code to create a Lambda function using the AWS CLI\. The sample code uses the requests library to get the source code for [https://www\.test\.com/](https://www.test.com/)\. The requests library is not included with the `python3.8` [runtime](lambda-runtimes.md), so you install it to a `package` directory\.

You'll learn how to:
+ Set up the directory structure of a deployment package \(\.zip file\)\.
+ Create a deployment package for a Lambda function with runtime dependencies\.
+ Use the AWS CLI to upload the deployment package and create the Lambda function\.
+ Invoke the Lambda function to return the source code\.

### Create the deployment package<a name="python-package-create-package-with-dependency"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

Create the \.zip file that Lambda uses as your deployment package\.

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
   def main(event, context):   
       response = requests.get("https://www.test.com/")
       print(response.text)
       return response.text
   if __name__ == "__main__":   
       main('', '')
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
   zip -g my-deployment-package.zip lambda_function.py
   ```

### Create the Lambda function<a name="python-package-create-createfunction-with-dependency"></a>

Lambda needs to know the [runtime](lambda-runtimes.md) environment to use for your function's code, the [handler](python-handler.md) in your function code, and the [execution role](lambda-intro-execution-role.md) that it can use to invoke your function\.

Create the Lambda function using the execution role and deployment package that you created in the previous steps\.

**To create the function**

1. Navigate to the `my-sourcecode-function` project directory\.

   ```
   cd my-sourcecode-function
   ```

1. Create a function named `my-sourcecode-function`\. Substitute the value for `role` with the `Arn` you copied in previous steps\.

   ```
   aws lambda create-function --function-name my-sourcecode-function --zip-file fileb://my-deployment-package.zip --handler lambda_function.main --runtime python3.8 --role arn:aws:iam::your-account-id:role/lambda-ex
   ```

   This command produces the following output:

   ```
   {
       "FunctionName": "my-sourcecode-function",
       "FunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:my-sourcecode-function",
       "Runtime": "python3.8",
       "Role": "arn:aws:iam::123456789012:role/lambda-ex",
       "Handler": "lambda_function.main",
       "CodeSize": 753,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2021-01-11T18:39:44.847+0000",
       "CodeSha256": "82RtIE7p1ET5Od6bk4xSleJbUybUnZX52m92x/fEH84=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "PassThrough"
       },
       "RevisionId": "589e5115-f3c0-446c-bc62-4e05cf0a3c85",
       "State": "Active",
       "LastUpdateStatus": "Successful"
   }
   ```

   The Lambda function in this step uses a function handler of `lambda_function.main`\. For more information about function handler naming conventions, see [Naming](python-handler.md#naming) in **AWS Lambda function handler in Python**\.

### Invoke the Lambda function<a name="python-package-create-invokefunction-with-dependency"></a>

Invoke the Lambda function [synchronously](invocation-sync.md) using the event input defined for the sample code\. For more information, see [How it works](python-handler.md#python-handler-how) in **AWS Lambda function handler in Python**\.

**To invoke the function**
+ Use the [invoke](https://docs.aws.amazon.com/cli/latest/reference/lambda/invoke.html) command\.

  ```
  aws lambda invoke \
    --function-name my-sourcecode-function \
        --cli-binary-format raw-in-base64-out \
            --payload '{"key1": "value1", "key2": "value2", "key3": "value3"}' output.txt
  ```

  The cli\-binary\-format option is required if you are using AWS CLI version 2\. You can also configure this option in your [ AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cliv2-migration.html#cliv2-migration-binaryparam)

  This command produces the following output:

  ```
  {
      "StatusCode": 200,
      "ExecutedVersion": "$LATEST"
  }
  ```

  For the `RequestResponse` invocation type, the status code is `200`\. For more information, see the [Invoke](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html#API_Invoke_ResponseSyntax) API reference\.

  You should see the source code in `output.txt`\.

### What's next?<a name="python-package-create-next-with-dependency"></a>
+ Learn how to update your Lambda function, see [Updating a Lambda function in Python 3\.8](python-package-update.md)\.
+ Learn how to show logging events for your Lambda function, see [AWS Lambda function logging in Python](python-logging.md)\.
+ Explore other [AWS SDK examples in Python on GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/master/python/example_code/lambda)\.

### Clean up your resources<a name="python-package-create-cleanup-with-dependency"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**
+ Use the [delete\-function](https://docs.aws.amazon.com/cli/latest/reference/lambda/delete-function.html) command\.

  ```
  aws lambda delete-function --function-name my-function
  ```

  This command produces no output\.

**To delete the execution role policy**
+ Use the [delete\-policy](https://docs.aws.amazon.com/cli/latest/reference/iam/delete-policy.html) command\.

  ```
  aws iam delete-role-policy --role-name lambda-ex --policy-name AWSLambdaBasicExecutionRole
  ```

**To delete the execution role**
+ Use the [delete\-role](https://docs.aws.amazon.com/cli/latest/reference/iam/delete-role.html) command\.

  ```
  aws iam delete-role --role-name lambda-ex
  ```