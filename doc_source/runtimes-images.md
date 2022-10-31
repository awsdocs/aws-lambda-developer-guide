# Base images for Lambda<a name="runtimes-images"></a>

AWS provides a set of open\-source base images that you can use\. You can also use a preferred community or private base image\. Lambda provides client software that you add to your preferred base image to make it compatible with the Lambda service\. 

**Note**  
Each base image is compatible with one or more of the instruction set architectures that Lambda supports\. You need to build the function image for only one architecture\. Lambda does not support multi\-architecture images\.

**Topics**
+ [AWS base images for Lambda](#runtimes-images-lp)
+ [Base images for custom runtimes](#runtimes-images-custom)
+ [Runtime interface clients](#runtimes-api-client)
+ [Runtime interface emulator](#runtimes-test-emulator)

## AWS base images for Lambda<a name="runtimes-images-lp"></a>

You can use one of the AWS base images for Lambda to build the container image for your function code\. The base images are preloaded with a language runtime and other components required to run a container image on Lambda\. You add your function code and dependencies to the base image and then package it as a container image\.

AWS will maintain and regularly update these images\. In addition, AWS will release AWS base images when any new managed runtime becomes available\. 

Lambda provides base images for the following runtimes: 
+  [Node\.js](nodejs-image.md) 
+  [Python](python-image.md) 
+  [Java](java-image.md) 
+  [\.NET](csharp-image.md) 
+  [Go](go-image.md) 
+  [Ruby](ruby-image.md) 

## Base images for custom runtimes<a name="runtimes-images-custom"></a>

AWS provides base images that contain the required Lambda components and the Amazon Linux or Amazon Linux2 operating system\. You can add your preferred runtime, dependencies and code to these images\.


| Tags | Runtime | Operating system | 
| --- | --- | --- | 
| al2 | provided\.al2 | Amazon Linux 2 | 
| alami | provided | Amazon Linux  | 

Amazon ECR Public Gallery: [gallery\.ecr\.aws/lambda/provided](https://gallery.ecr.aws/lambda/provided)

## Runtime interface clients<a name="runtimes-api-client"></a>

The runtime interface client in your container image manages the interaction between Lambda and your function code\. The [Runtime API](runtimes-api.md), along with the [ Extensions API](runtimes-extensions-api.md), defines a simple HTTP interface for runtimes to receive invocation events from Lambda and respond with success or failure indications\. 

Each of the AWS base images for Lambda include a runtime interface client\. If you choose one of the base images for custom runtimes or an alternative base image, you need to add the appropriate runtime interface client\. 

For your convenience, Lambda provides an open source runtime interface client for each of the supported Lambda runtimes: 
+  [Node\.js](nodejs-image.md#nodejs-image-clients) 
+  [Python](python-image.md#python-image-clients) 
+  [Java](java-image.md#java-image-clients) 
+  [\.NET](csharp-image.md#csharp-image-clients) 
+  [Go](go-image.md#go-image-clients) 
+  [Ruby](ruby-image.md#ruby-image-clients) 

## Runtime interface emulator<a name="runtimes-test-emulator"></a>

Lambda provides a runtime interface emulator \(RIE\) for you to test your function locally\. The AWS base images for Lambda and base images for custom runtimes include the RIE\. For other base images, you can download the [Runtime interface emulator](https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie) from the AWS GitHub repository\. 
