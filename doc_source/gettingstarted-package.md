# Lambda deployment packages<a name="gettingstarted-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

**Topics**
+ [Container images](#gettingstarted-package-images)
+ [\.zip file archives](#gettingstarted-package-zip)
+ [Layers](#gettingstarted-package-layers)
+ [Using other AWS services to build a deployment package](#gettingstarted-package-awsother)

## Container images<a name="gettingstarted-package-images"></a>

A container image includes the base operating system, the runtime, Lambda extensions, your application code and its dependencies\. You can also add static data, such as machine learning models, into the image\. 

Lambda provides a set of open\-source base images that you can use to build your container image\. To create and test container images, you can use the AWS Serverless Application Model \(AWS SAM\) command line interface \(CLI\) or native container tools such as the Docker CLI\.

You upload your container images to Amazon Elastic Container Registry \(Amazon ECR\), a managed AWS container image registry service\. To deploy the image to your function, you specify the Amazon ECR image URL using the Lambda console, the Lambda API, command line tools, or the AWS SDKs\.

For more information about Lambda container images, see [Creating Lambda container images](images-create.md)\.

**Note**  
Container images aren't supported for Lambda functions in the Middle East \(UAE\) Region\.

## \.zip file archives<a name="gettingstarted-package-zip"></a>

A \.zip file archive includes your application code and its dependencies\. When you author functions using the Lambda console or a toolkit, Lambda automatically creates a \.zip file archive of your code\.

When you create functions with the Lambda API, command line tools, or the AWS SDKs, you must create a deployment package\. You also must create a deployment package if your function uses a compiled language, or to add dependencies to your function\. To deploy your function's code, you upload the deployment package from Amazon Simple Storage Service \(Amazon S3\) or your local machine\.

You can upload a \.zip file as your deployment package using the Lambda console, AWS Command Line Interface \(AWS CLI\), or to an Amazon Simple Storage Service \(Amazon S3\) bucket\.

### Using the Lambda console<a name="gettingstarted-package-zip-console"></a>

The following steps demonstrate how to upload a \.zip file as your deployment package using the Lambda console\.

**To upload a \.zip file on the Lambda console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Select a function\.

1. In the **Code Source** pane, choose **Upload from** and then **\.zip file**\.

1. Choose **Upload** to select your local \.zip file\.

1. Choose **Save**\.

### Using the AWS CLI<a name="gettingstarted-package-zip-cli"></a>

You can upload a \.zip file as your deployment package using the AWS Command Line Interface \(AWS CLI\)\. For language\-specific instructions, see the following topics\.

------
#### [ Node\.js ]

[Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md) 

------
#### [ Python ]

 [Deploy Python Lambda functions with \.zip file archives](python-package.md) 

------
#### [ Ruby ]

 [Deploy Ruby Lambda functions with \.zip file archives](ruby-package.md) 

------
#### [ Java ]

 [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md) 

------
#### [ Go ]

 [Deploy Go Lambda functions with \.zip file archives](golang-package.md) 

------
#### [ C\# ]

 [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md) 

------
#### [ PowerShell ]

 [Deploy PowerShell Lambda functions with \.zip file archives](powershell-package.md) 

------

### Using Amazon S3<a name="gettingstarted-package-zip-cli"></a>

You can upload a \.zip file as your deployment package using Amazon Simple Storage Service \(Amazon S3\)\. For more information, see [Using other AWS services to build a deployment package](#gettingstarted-package-awsother)\.

## Layers<a name="gettingstarted-package-layers"></a>

If you deploy your function code using a \.zip file archive, you can use Lambda layers as a distribution mechanism for libraries, custom runtimes, and other function dependencies\. Layers enable you to manage your in\-development function code independently from the unchanging code and resources that it uses\. You can configure your function to use layers that you create, layers that AWS provides, or layers from other AWS customers\.

You do not use layers with container images\. Instead, you package your preferred runtime, libraries, and other dependencies into the container image when you build the image\.

For more information about layers, see [Creating and sharing Lambda layers](configuration-layers.md)\.

## Using other AWS services to build a deployment package<a name="gettingstarted-package-awsother"></a>

The following section describes other AWS services you can use to package dependencies for your Lambda function\.

### Deployment packages with C or C\+\+ libraries<a name="gettingstarted-package-sam"></a>

If your deployment package contains native libraries, you can build the deployment package with AWS Serverless Application Model \(AWS SAM\)\. You can use the AWS SAM CLI `sam build` command with the `--use-container` to create your deployment package\. This option builds a deployment package inside a Docker image that is compatible with the Lambda execution environment\. 

For more information, see [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-build.html) in the *AWS Serverless Application Model Developer Guide*\.

### Deployment packages over 50 MB<a name="gettingstarted-package-s3"></a>

If your deployment package is larger than 50 MB, upload your function code and dependencies to an Amazon S3 bucket\.

You can create a deployment package and upload the \.zip file to your Amazon S3 bucket in the AWS Region where you want to create a Lambda function\. When you create your Lambda function, specify the S3 bucket name and object key name on the Lambda console, or using the AWS CLI\.

To create a bucket using the Amazon S3 console, see [How do I create an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.