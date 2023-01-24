# Deploy Node\.js Lambda functions with container images<a name="nodejs-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Node\.js function:
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
+ [AWS base images for Node\.js](#nodejs-image-base)
+ [Using a Node\.js base image](#nodejs-image-instructions)
+ [Node\.js runtime interface clients](#nodejs-image-clients)
+ [Deploy the container image](#nodejs-image-deploy)

## AWS base images for Node\.js<a name="nodejs-image-base"></a>

AWS provides the following base images for Node\.js:


| Tags | Runtime | Operating system | Dockerfile | Deprecation | 
| --- | --- | --- | --- | --- | 
| 16 | Node\.js 16 | Amazon Linux 2 | [Dockerfile for Node\.js 16 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/nodejs16.x/Dockerfile.nodejs16.x) |    | 
| 14 | Node\.js 14 | Amazon Linux 2 | [Dockerfile for Node\.js 14 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/nodejs14.x/Dockerfile.nodejs14.x) |    | 
| 12 | Node\.js 12 | Amazon Linux 2 | [Dockerfile for Node\.js 12 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/nodejs12.x/Dockerfile.nodejs12.x) |  Mar 31, 2023  | 

Amazon ECR repository: [gallery\.ecr\.aws/lambda/nodejs](https://gallery.ecr.aws/lambda/nodejs)

## Using a Node\.js base image<a name="nodejs-image-instructions"></a>

For instructions on how to use a Node\.js base image, choose the **usage** tab on [AWS Lambda base images for Node\.js](https://gallery.ecr.aws/lambda/nodejs) in the *Amazon ECR repository*\. 

## Node\.js runtime interface clients<a name="nodejs-image-clients"></a>

Install the runtime interface client for Node\.js using the npm package manager:

```
npm install aws-lambda-ric
```

For package details, see [Lambda RIC](http://npmjs.com/package/aws-lambda-ric) on the npm website\.

You can also download the [Node\.js runtime interface client](https://github.com/aws/aws-lambda-nodejs-runtime-interface-client) from GitHub\.

## Deploy the container image<a name="nodejs-image-deploy"></a>

For a new function, you deploy the Node\.js image when you [create the function](gettingstarted-images.md#configuration-images-create)\. For an existing function, if you rebuild the container image, you need to redeploy the image by [updating the function code](gettingstarted-images.md#configuration-images-update)\.