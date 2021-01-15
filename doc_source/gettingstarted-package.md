# Lambda deployment packages<a name="gettingstarted-package"></a>

Your Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

**Topics**
+ [Container images](#gettingstarted-package-images)
+ [\.zip file archives](#gettingstarted-package-zip)
+ [Layers](#gettingstarted-package-layers)

## Container images<a name="gettingstarted-package-images"></a>

A container image includes the base operating system, the runtime, Lambda extensions, your application code and its dependencies\. You can also add static data, such as machine learning models, into the image\. 

Lambda provides a set of open\-source base images that you can use to build your container image\. To create and test container images, you can use the AWS Serverless Application Model \(AWS SAM\) command line interface \(CLI\) or native container tools such as the Docker CLI\.

You upload your container images to Amazon Elastic Container Registry \(Amazon ECR\), a managed AWS container image registry service\. To deploy the image to your function, you specify the Amazon ECR image URL using the Lambda console, the Lambda API, command line tools, or the AWS SDKs\.

For more information about Lambda container images, see [Using container images with Lambda](lambda-images.md)\.

## \.zip file archives<a name="gettingstarted-package-zip"></a>

A \.zip file archive includes your application code and its dependencies\. When you author functions using the Lambda console or a toolkit, Lambda automatically creates a \.zip file archive of your code\.

When you create functions with the Lambda API, command line tools, or the AWS SDKs, you must create a deployment package\. You also must create a deployment package if your function uses a compiled language, or to add dependencies to your function\. To deploy your function's code, you upload the deployment package from Amazon Simple Storage Service \(Amazon S3\) or your local machine\.

For language\-specific instructions, see the following topics\.
+  [Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md) 
+  [Deploy Python Lambda functions with \.zip file archives](python-package.md) 
+  [Deploy Ruby Lambda functions with \.zip file archives](ruby-package.md) 
+  [Deploy Java Lambda functions with \.zip file archives](java-package.md) 
+  [Deploy Go Lambda functions with \.zip file archives](golang-package.md) 
+  [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md) 
+  [Deploy PowerShell Lambda functions with \.zip file archives](powershell-package.md) 

## Layers<a name="gettingstarted-package-layers"></a>

If you deploy your function code using a \.zip file archive, you can use Lambda layers as a distribution mechanism for libraries, custom runtimes, and other function dependencies\. Layers enable you to manage your in\-development function code independently from the unchanging code and resources that it uses\. You can configure your function to use layers that you create, layers that AWS provides, or layers from other AWS customers\.

You do not use layers with container images\. Instead, you package your preferred runtime, libraries, and other dependencies into the container image when you build the image\.

For more information about layers, see [AWS Lambda layers](configuration-layers.md)\.