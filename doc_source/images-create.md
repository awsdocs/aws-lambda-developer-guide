# Creating Lambda container images<a name="images-create"></a>

You can package your Lambda function code and dependencies as a container image, using tools such as the Docker CLI\. You can then upload the image to your container registry hosted on Amazon Elastic Container Registry \(Amazon ECR\)\. Note that you must create the Lambda function from the same account as the container registry in Amazon ECR\.

AWS provides a set of open\-source [base images](runtimes-images.md#runtimes-images-lp) that you can use to create your container image\. These base images include a [runtime interface client](runtimes-images.md#runtimes-api-client) to manage the interaction between Lambda and your function code\.

You can also use an alternative base image from another container registry\. Lambda provides open\-source runtime interface clients that you add to an alternative base image to make it compatible with Lambda\.

For example applications, including a Node\.js example and a Python example, see [Container image support for Lambda](http://aws.amazon.com/blogs/aws/new-for-aws-lambda-container-image-support/) on the AWS Blog\.

After you create a container image in the Amazon ECR container registry, you can [create and run](configuration-images.md) the Lambda function\.

**Topics**
+ [Image types](#images-types)
+ [Container tools](#images-tools)
+ [Lambda requirements for container images](#images-reqs)
+ [Container image settings](#images-parms)
+ [Create an image from an AWS base image for Lambda](#images-create-1)
+ [Create an image from an alternative base image](#images-create-2)
+ [Create an image using the AWS SAM toolkit](#images-create-sam)

## Image types<a name="images-types"></a>

You can use an AWS provided base image or an alternative base image, such as Alpine or Debian\. Lambda supports any image that conforms to one of the following image manifest formats:
+ Docker image manifest V2, schema 2 \(used with Docker version 1\.10 and newer\)
+ Open Container Initiative \(OCI\) Specifications \(v1\.0\.0 and up\)

Lambda supports images up to 10 GB in size\.

## Container tools<a name="images-tools"></a>

To create your container image, you can use any development tool that supports one of the following container image manifest formats:
+ Docker image manifest V2, schema 2 \(used with Docker version 1\.10 and newer\)
+ OCI Specifications \(v1\.0\.0 and up\)

For example, you can use the Docker CLI to build, test, and deploy your container images\.

## Lambda requirements for container images<a name="images-reqs"></a>

To deploy a container image to Lambda, note the following requirements:

1. The container image must implement the Lambda [Runtime API](runtimes-api.md)\. The AWS open\-source [runtime interface clients](runtimes-images.md#runtimes-api-client) implement the API\. You can add a runtime interface client to your preferred base image to make it compatible with Lambda\.

1. The container image must be able to run on a read\-only file system\. Your function code can access a writable `/tmp` directory with 512 MB of storage\. If you are using an image that requires a writable directory outside of `/tmp`, configure it to write to a directory under the `/tmp` directory\.

1. The default Lambda user must be able to read all the files required to run your function code\. Lambda follows security best practices by defining a default Linux user with least\-privileged permissions\. Verify that your application code does not rely on files that other Linux users are restricted from running\.

1. Lambda supports only Linux\-based container images\.

## Container image settings<a name="images-parms"></a>

Lambda supports the following container image settings in the Dockerfile:
+ ENTRYPOINT – Specifies the absolute path to the entry point of the application\.
+ CMD – Specifies parameters that you want to pass in with ENTRYPOINT\.
+ WORKDIR – Specifies the absolute path to the working directory\.
+ ENV – Specifies an environment variable for the Lambda function\.

**Note**  
Lambda ignores the values of any unsupported container image settings in the Dockerfile\.

For more information about how Docker uses the container image settings, see [ENTRYPOINT](https://docs.docker.com/engine/reference/builder/#entrypoint) in the Dockerfile reference on the Docker Docs website\. For more information about using ENTRYPOINT and CMD, see [Demystifying ENTRYPOINT and CMD in Docker](http://aws.amazon.com/blogs/opensource/demystifying-entrypoint-cmd-docker/) on the AWS Open Source Blog\.

You can specify the container image settings in the Dockerfile when you build your image\. You can also override these configurations using the Lambda console or Lambda API\. This allows you to deploy multiple functions that deploy the same container image but with different runtime configurations\.

**Warning**  
When you specify ENTRYPOINT or CMD in the Dockerfile or as an override, make sure that you enter the absolute path\. Also, do not use symlinks as the entry point to the container\.

## Create an image from an AWS base image for Lambda<a name="images-create-1"></a>

To build a container image for a new Lambda function, you can start with an AWS base image for Lambda\.

**Note**  
AWS periodically provides updates to the AWS base images for Lambda\. If your Dockerfile includes the image name in the FROM property, your Docker client pulls the latest version of the image from Docker Hub\. To use the updated base image, you must rebuild your container image and [update the function code](configuration-images.md#configuration-images-update)\.

**Prerequisites**
+ The AWS Command Line Interface \(AWS CLI\)

  The following instructions use the AWS CLI to call AWS service API operations\. To install the AWS CLI, see [Installing, updating, and uninstalling the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) in the *AWS Command Line Interface User Guide*\.
+ Docker Desktop

  The following instructions use Docker CLI commands to create the container image\. To install the Docker CLI, see [Get Docker](https://docs.docker.com/get-docker) on the Docker Docs website\.
+ Your function code

**To create an image from an AWS base image for Lambda**

1. On your local machine, create a project directory for your new function\.

1. Create a directory named **app** in in the project directory, and then add your function handler code to the app directory\.

1. Use a text editor to create a new Dockerfile\.

   The AWS base images provide the following environment variables:
   + LAMBDA\_TASK\_ROOT=/var/task
   + LAMBDA\_RUNTIME\_DIR=/var/runtime

   The following shows an example Dockerfile for Node\.js version 14\. :

   ```
   FROM public.ecr.aws/lambda/nodejs:14
   # Alternatively, you can pull the base image from Docker Hub: amazon/aws-lambda-nodejs:12
   
   # Assumes your function is named "app.js", and there is a package.json file in the app directory.
   COPY app.js package.json  /var/task/
   
   # Install NPM dependencies for function
   RUN npm install
   
   # Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
   CMD [ "app.handler" ]
   ```

1. Build your Docker image with the `docker build` command\. Enter a name for the image\. The following example names the image `hello-world`\.

   ```
   docker build -t hello-world .   
   ```

1. Start the Docker image with the `docker run` command\. For this example, enter `hello-world` as the image name\.

   ```
   docker run -p 9000:8080 hello-world 
   ```

1. \(Optional\) Test your application locally using the [runtime interface emulator](images-test.md)\. From a new terminal window, post an event to the following endpoint using a `curl` command:

   ```
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
   ```

   This command invokes the function running in the container image and returns a response\.

1. Authenticate the Docker CLI to your Amazon ECR registry\.

   ```
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com    
   ```

1. Create a repository in Amazon ECR using the `create-repository` command\.

   ```
   aws ecr create-repository --repository-name hello-world --image-scanning-configuration scanOnPush=true --image-tag-mutability MUTABLE
   ```

1. Tag your image to match your repository name, and deploy the image to Amazon ECR using the `docker push` command\. 

   ```
   docker tag  hello-world:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   ```

Now that your container image resides in the Amazon ECR container registry, you can [create and run](configuration-images.md) the Lambda function\.

## Create an image from an alternative base image<a name="images-create-2"></a>

**Prerequisites**
+ The AWS CLI
+ Docker Desktop
+ Your function code

**To create an image using an alternative base image**

1. Choose a base image\. Lambda supports all Linux distributions, such as Alpine, Debian, and Ubuntu\.

1. On your local machine, create a project directory for your new function\.

1. Create a directory named **app** in the project directory, and then add your function handler code to the app directory\.

1. Use a text editor to create a new Dockerfile with the following configuration:
   + Set the `FROM` property to the URI of the base image\.
   + Add instructions to install the runtime interface client\.
   + Set the `ENTRYPOINT` property to invoke the runtime interface client\.
   + Set the `CMD` argument to specify the Lambda function handler\.

   The following example shows a Dockerfile for Python:

   ```
   # Define function directory
   ARG FUNCTION_DIR="/function"
   
   FROM python:buster as build-image
   
   # Install aws-lambda-cpp build dependencies
   RUN apt-get update && \
     apt-get install -y \
     g++ \
     make \
     cmake \
     unzip \
     libcurl4-openssl-dev
   
   # Include global arg in this stage of the build
   ARG FUNCTION_DIR
   # Create function directory
   RUN mkdir -p ${FUNCTION_DIR}
   
   # Copy function code
   COPY app/* ${FUNCTION_DIR}
   
   # Install the runtime interface client
   RUN pip install \
           --target ${FUNCTION_DIR} \
           awslambdaric
   
   # Multi-stage build: grab a fresh copy of the base image
   FROM python:buster
   
   # Include global arg in this stage of the build
   ARG FUNCTION_DIR
   # Set working directory to function root directory
   WORKDIR ${FUNCTION_DIR}
   
   # Copy in the build image dependencies
   COPY --from=build-image ${FUNCTION_DIR} ${FUNCTION_DIR}
   
   ENTRYPOINT [ "/usr/local/bin/python", "-m", "awslambdaric" ]
   CMD [ "app.handler" ]
   ```

1. Build your Docker image with the `docker build` command\. Enter a name for the image\. The following example names the image `hello-world`\.

   ```
   docker build -t hello-world .    
   ```

1. \(Optional\) Test your application locally using the [Runtime interface emulator](images-test.md)\.

1. Authenticate the Docker CLI to your Amazon ECR registry\.

   ```
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com    
   ```

1. Create a repository in Amazon ECR using the `create-repository` command\.

   ```
   aws ecr create-repository --repository-name hello-world --image-scanning-configuration scanOnPush=true --image-tag-mutability MUTABLE
   ```

1. Tag your image to match your repository name, and deploy the image to Amazon ECR using the `docker push` command\. 

   ```
   docker tag  hello-world:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   ```

Now that your container image resides in the Amazon ECR container registry, you can [create and run](configuration-images.md) the Lambda function\.

## Create an image using the AWS SAM toolkit<a name="images-create-sam"></a>

You can use the AWS Serverless Application Model \(AWS SAM\) toolkit to create and deploy a function defined as a container image\. For a new project, you can use the AWS SAM CLI `init` command to set up the scaffolding for your project in your preferred runtime\.

In your AWS SAM template, you set the `Runtime` type to `Image` and provide the URI of the base image\.

For more information, see [Building applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) in the *AWS Serverless Application Model Developer Guide*\.