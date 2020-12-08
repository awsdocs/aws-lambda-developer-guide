# Deploy Go Lambda functions with container images<a name="go-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Go function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

## AWS base images for Go<a name="go-image-base"></a>

AWS provides the following base image for Go:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
|  1  | Go 1\.x | Amazon Linux 2018\.03 | [Dockerfile for Go 1\.x on GitHub](https://github.com/aws/aws-lambda-base-images/blob/go1.x/Dockerfile.go1.x) | 

Docker Hub repository: amazon/aws\-lambda\-go

Amazon ECR repository: gallery\.ecr\.aws/lambda/go

## Go runtime interface clients<a name="go-image-clients"></a>

Download the runtime interface client for Go from the [AWS Lambda for Go](https://github.com/aws/aws-lambda-go) repository on GitHub\.