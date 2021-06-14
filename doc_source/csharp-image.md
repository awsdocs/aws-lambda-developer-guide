# Deploy \.NET Lambda functions with container images<a name="csharp-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your \.NET function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

The workflow for a function defined as a container image includes these steps:

1. Build your container image using the resources listed in this topic\.

1. Upload the image to your Amazon ECR container registry\. See steps 7\-9 in [Create image](images-create.md#images-create-from-base)\.

1. [Create](configuration-images.md) the Lambda function and deploy the image\.

## AWS base images for \.NET<a name="csharp-image-base"></a>

AWS provides the following base images for \.NET:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
| 5\.0 | \.NET 5\.0 | Amazon Linux 2 | [Dockerfile for \.NET 5\.0 on GitHub](https://github.com/aws/aws-lambda-dotnet/blob/master/LambdaRuntimeDockerfiles/dotnet5/Dockerfile) | 
| core3\.1 | \.NET Core 3\.1 | Amazon Linux 2 | [Dockerfile for \.NET 3\.1 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnetcore3.1/Dockerfile.dotnetcore3.1) | 
| core2\.1 | \.NET Core 2\.1 | Amazon Linux 2018\.03 | [Dockerfile for \.NET 2\.1 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnetcore2.1/Dockerfile.dotnetcore2.1) | 

Docker Hub repository: amazon/aws\-lambda\-dotnet

Amazon ECR repository: gallery\.ecr\.aws/lambda/dotnet

## Using a \.NET base image<a name="csharp-image-instructions"></a>

For instructions on how to use a \.NET base image, choose the **usage** tab on [AWS Lambda base images for \.NET](https://gallery.ecr.aws/lambda/dotnet) in the *Amazon ECR repository*\. 

The instructions are also available on [Lambda base images for \.NET](https://hub.docker.com/r/amazon/aws-lambda-dotnet) in the *Docker Hub repository*\.

## \.NET runtime interface clients<a name="csharp-image-clients"></a>

Download the \.NET runtime interface client from the [AWS Lambda for \.NET Core](https://github.com/aws/aws-lambda-dotnet) repository on GitHub\.