# Deploy \.NET Lambda functions with container images<a name="csharp-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your \.NET function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

## AWS base images for \.NET<a name="csharp-image-base"></a>

AWS provides the following base images for \.NET:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
| 5\.0 | \.NET 5\.0 | Amazon Linux 2 | [Dockerfile for \.NET 5\.0 on GitHub](https://github.com/aws/aws-lambda-dotnet/blob/main/LambdaRuntimeDockerfiles/dotnet5/Dockerfile) | 
| core3\.1 | \.NET Core 3\.1 | Amazon Linux 2 | [Dockerfile for \.NET 3\.1 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnetcore3.1/Dockerfile.dotnetcore3.1) | 
| core2\.1 | \.NET Core 2\.1 | Amazon Linux 2018\.03 | [Dockerfile for \.NET 2\.1 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/dotnetcore2.1/Dockerfile.dotnetcore2.1) | 

Docker Hub repository: amazon/aws\-lambda\-dotnet

Amazon ECR repository: gallery\.ecr\.aws/lambda/dotnet

## \.NET runtime interface clients<a name="csharp-image-clients"></a>

Download the \.NET runtime interface client from the [AWS Lambda for \.NET Core](https://github.com/aws/aws-lambda-dotnet) repository on GitHub\.