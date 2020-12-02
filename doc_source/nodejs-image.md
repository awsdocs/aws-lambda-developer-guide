# Deploy Node\.js Lambda functions with container images<a name="nodejs-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Node\.js function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

## AWS base images for Node\.js<a name="nodejs-image-base"></a>

AWS provides the following base images for Node\.js:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
| 12 | NodeJS 12\.x | Amazon Linux 2 | [Dockerfile for Node\.js 12\.x on GitHub](https://github.com/aws/aws-lambda-base-images/blob/nodejs12.x/Dockerfile.nodejs12.x) | 
| 10 | NodeJS 10\.x | Amazon Linux 2 | [Dockerfile for Node\.js 10\.x on GitHub](https://github.com/aws/aws-lambda-base-images/blob/nodejs10.x/Dockerfile.nodejs10.x) | 

Docker Hub repository: amazon/aws\-lambda\-nodejs

Amazon ECR repository: public\.ecr\.aws/lambda/nodejs

## Node\.js runtime interface clients<a name="nodejs-image-clients"></a>

Install the runtime interface client for Node\.js using the npm package manager:

```
npm install aws-lambda-ric
```

For package details, see [Lambda RIC](http://npmjs.com/package/aws-lambda-ric) on the npm website\.

You can also download the [Node\.js runtime interface client](https://github.com/aws/aws-lambda-nodejs-runtime-interface-client) from GitHub\.