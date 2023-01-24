# Deploying Lambda functions<a name="lambda-deploy-functions"></a>

You can deploy code to your Lambda function by uploading a zip file archive, or by creating and uploading a container image\.

## \.zip file archives<a name="deploying-zip-archives"></a>

A \.zip file archive includes your application code and its dependencies\. When you author functions using the Lambda console or a toolkit, Lambda automatically creates a \.zip file archive of your code\.

When you create functions with the Lambda API, command line tools, or the AWS SDKs, you must create a deployment package\. You also must create a deployment package if your function uses a compiled language, or to add dependencies to your function\. To deploy your function's code, you upload the deployment package from Amazon Simple Storage Service \(Amazon S3\) or your local machine\.

You can upload a \.zip file as your deployment package using the Lambda console, AWS Command Line Interface \(AWS CLI\), or to an Amazon Simple Storage Service \(Amazon S3\) bucket\.

## Container images<a name="deploying-containers"></a>

You can package your code and dependencies as a container image using tools such as the Docker command line interface \(CLI\)\. You can then upload the image to your container registry hosted on Amazon Elastic Container Registry \(Amazon ECR\)\.

AWS provides a set of open\-source base images that you can use to build the container image for your function code\. You can also use alternative base images from other container registries\. AWS also provides an open\-source runtime client that you add to your alternative base image to make it compatible with the Lambda service\.

Additionally, AWS provides a runtime interface emulator for you to test your functions locally using tools such as the Docker CLI\.

**Note**  
You create each container image to be compatible with one of the instruction set architectures that Lambda supports\. Lambda provides base images for each of the instruction set architectures and Lambda also provides base images that support both architectures\.   
The image that you build for your function must target only one of the architectures\.

There is no additional charge for packaging and deploying functions as container images\. When a function deployed as a container image is invoked, you pay for invocation requests and execution duration\. You do incur charges related to storing your container images in Amazon ECR\. For more information, see [Amazon ECR pricing](http://aws.amazon.com/ecr/pricing/)\. 

**Topics**
+ [\.zip file archives](#deploying-zip-archives)
+ [Container images](#deploying-containers)
+ [Deploying Lambda functions as \.zip file archives](configuration-function-zip.md)
+ [Deploying Lambda functions as container images](gettingstarted-images.md)