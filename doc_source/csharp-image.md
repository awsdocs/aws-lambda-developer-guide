# Deploy \.NET Lambda functions with container images<a name="csharp-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. 

AWS provides the following resources to help you build a container image for your \.NET function:

AWS provides base images for the x86\_64 architecture for all supported \.NET runtimes, and for the arm64 architecture for the \.NET Core 3\.1 and \.NET 6\.0 runtimes\.
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients \(RIC\)

  If you use a community or private enterprise base image, you must add a [Runtime interface client](runtimes-images.md#runtimes-api-client) to the base image to make it compatible with Lambda\.
+ Open\-source runtime interface emulator \(RIE\)

   Lambda provides a runtime interface emulator for you to test your function locally\. The base images for Lambda and base images for custom runtimes include the RIE\. For other base images, you can download the RIE for [testing your image](images-test.md) locally\.

The workflow for a function defined as a container image includes these steps:

1. Build your container image using the resources listed in this topic\.

1. Upload the image to your [Amazon ECR container registry](images-create.md#images-upload)\.

1. [Create](gettingstarted-images.md#configuration-images-create) the Lambda function or [update the function code](gettingstarted-images.md#configuration-images-update) to deploy the image to an existing function\.

**Topics**
+ [AWS base images for \.NET](#csharp-image-base)
+ [Using a \.NET base image](#csharp-image-instructions)
+ [\.NET runtime interface clients](#csharp-image-clients)
+ [Deploy the container image](#csharp-image-deploy)

## AWS base images for \.NET<a name="csharp-image-base"></a>

AWS provides the following base images for \.NET:


| Tags | Runtime | Operating system | Dockerfile | Deprecation | 
| --- | --- | --- | --- | --- | 
| core3\.1 | \.NET Core 3\.1 | Amazon Linux 2 | [Dockerfile for \.NET Core 3\.1 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnetcore3.1/Dockerfile.dotnetcore3.1) |  Mar 31, 2023  | 
| 6 | \.NET 6 | Amazon Linux 2 | [Dockerfile for \.NET 6 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnet6/Dockerfile.dotnet6) |    | 
| 5\.0 | \.NET 5 | Amazon Linux 2 | [Dockerfile for \.NET 5 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnet5.0/Dockerfile.dotnet5.0) |    | 

Amazon ECR repository: [gallery\.ecr\.aws/lambda/dotnet](https://gallery.ecr.aws/lambda/dotnet)

## Using a \.NET base image<a name="csharp-image-instructions"></a>

For instructions on how to use a \.NET base image, choose the **usage** tab on [AWS Lambda base images for \.NET](https://gallery.ecr.aws/lambda/dotnet) in the *Amazon ECR repository*\. 

## \.NET runtime interface clients<a name="csharp-image-clients"></a>

Download the \.NET runtime interface client from the [AWS Lambda for \.NET Core](https://github.com/aws/aws-lambda-dotnet) repository on GitHub\.

## Deploy the container image<a name="csharp-image-deploy"></a>

For a new function, you deploy the container image when you [create the function](gettingstarted-images.md#configuration-images-create)\. For an existing function, if you rebuild the container image, you need to redeploy the image by [updating the function code](gettingstarted-images.md#configuration-images-update)\.