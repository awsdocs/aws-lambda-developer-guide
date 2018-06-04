# Creating a Deployment Package \(Python\)<a name="lambda-python-how-to-create-deployment-package"></a>

To create a Lambda function you first create a Lambda function deployment package, a \.zip file consisting of your code and any dependencies\. You will then need to set the appropriate security permissions for the zip package\. For more information, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md) policies\.

You can create a deployment package yourself or write your code directly in the Lambda console, in which case the console creates the deployment package for you and uploads it, creating your Lambda function\. Note the following to determine if you can use the console to create your Lambda function:
+ **Simple scenario** – If your custom code requires only the AWS SDK library, then you can use the inline editor in the AWS Lambda console\. Using the console, you can edit and upload your code to AWS Lambda\. The console will zip up your code with the relevant configuration information into a deployment package that the Lambda service can run\. 

  You can also test your code in the console by manually invoking it using sample event data\. 
**Note**  
The Lambda service has preinstalled the AWS SDK for Python\.
+ **Advanced scenario** – If you are writing code that uses other resources, such as a graphics library for image processing, or you want to use the AWS CLI instead of the console, you need to first create the Lambda function deployment package, and then use the console or the CLI to upload the package\.

**Note**  
After you create a deployment package, you may either upload it directly or upload the \.zip file first to an Amazon S3 bucket in the same AWS region where you want to create the Lambda function, and then specify the bucket name and object key name when you create the Lambda function using the console or the AWS CLI\.

The following is an example procedure to create a deployment package \(outside the console\)\. 

**Note**  
This should work for most standard installations of Python and pip when using pure Python modules in your Lambda function\. If you are including modules that have native dependencies or have Python installed with Homebrew on OS X, you should see the next section which provides instructions to create a deployment package when using Virtualenv\. For more information, see [Create Deployment Package Using a Python Environment Created with Virtualenv](#deployment-pkg-for-virtualenv) and the [Virtualenv](http://virtualenv.readthedocs.io/en/latest/) website\.

You will use `pip` to install dependencies/libraries\. For information to install `pip`, go to [Installation](https://pip.pypa.io/en/stable/installing/)\. 

1. You create a directory, for example `project-dir`\. 

1. Save all of your Python source files \(the \.py files\) at the root level of this directory\.

1. Install any libraries using **pip**\. Again, you install these libraries at the root level of the directory\.

   ```
   pip install module-name -t /path/to/project-dir
   ```

   For example, the following command installs the `requests` HTTP library in the `project-dir` directory\.

   ```
   pip install requests -t /path/to/project-dir
   ```

   If using Mac OS X and you have Python installed using Homebrew \(see [Homebrew](http://brew.sh/)\), the preceding command will not work\. A simple workaround is to add a `setup.cfg` file in your `/path/to/project-dir` with the following content\.

   ```
   [install]
   prefix=
   ```

1. Zip the content of the `project-dir` directory, which is your deployment package\. 
**Important**  
Zip the directory *content* contained within the directory, not the directory itself\. The contents of the Zip file are available as the current working directory of the Lambda function\. For example: */project\-dir/codefile\.py/lib/yourlibraries*\. In this case, you zip the content contained within */project\-dir*\.

**Note**  
AWS Lambda includes the AWS SDK for Python \(Boto 3\), so you don't need to include it in your deployment package\. However, if you want to use a version of Boto3 other than the one included by default, you can include it in your deployment package\.

## Create Deployment Package Using a Python Environment Created with Virtualenv<a name="deployment-pkg-for-virtualenv"></a>

This section explains how to create a deployment package if you are using a Python environment that you created with the Virtualenv tool\. Consider the following example: 
+ Created the following isolated Python environment using the Virtualenv tool and activated the environment:

  ```
  virtualenv path/to/my/virtual-env
  ```

  You can activate the environment on Windows, OS X, and Linux as follows:
  + On Windows, you activate using the `activate.bat`:

    ```
    path\to\my\virtual-env\Scripts\activate.bat  
    ```
  + On OS X and Linux, you source the `activate` script:

    ```
    source path/to/my/virtual-env/bin/activate
    ```
+ Also, to install the **requests** package in the activated environment, do the following: :

  ```
  pip install requests  
  ```

Now, to create a deployment package you do the following:

1. First, create \.zip file with your Python code you want to upload to AWS Lambda\. 

1. Add the libraries from preceding activated virtual environment to the \.zip file\. That is, you add the content of the following directory to the \.zip file \(note again that you add the content of the directory and not the directory itself\)\.

   For Windows the directory is:

   ```
   %VIRTUAL_ENV%\Lib\site-packages 
   ```

   For OS X, Linux, the directory is:

   ```
   $VIRTUAL_ENV/lib/python3.6/site-packages
   ```
**Note**  
If you don't find the packages in the `site-packages` directory in your virtual environment, you might find it in the `dist-packages` directory\.

For an example of creating a Python deployment package, see [Python](with-s3-example-deployment-pkg.md#with-s3-example-deployment-pkg-python)\. 