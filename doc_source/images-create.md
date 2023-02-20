# Creating Lambda container images<a name="images-create"></a>

AWS provides a set of open\-source [base images](runtimes-images.md#runtimes-images-lp) that you can use to create your container image\. These base images include a [runtime interface client](runtimes-images.md#runtimes-api-client) to manage the interaction between Lambda and your function code\.

For example applications, including a Node\.js example and a Python example, see [Container image support for Lambda](http://aws.amazon.com/blogs/aws/new-for-aws-lambda-container-image-support/) on the AWS Blog\.

**Note**  
Container images aren't supported for Lambda functions in the Middle East \(UAE\) Region\.

**Topics**
+ [Base images for Lambda](runtimes-images.md)
+ [Testing Lambda container images locally](images-test.md)
+ [Prerequisites](#images-reqs)
+ [Image types](#images-types)
+ [Container tools](#images-tools)
+ [Container image settings](#images-parms)
+ [Creating images from AWS base images](#images-create-from-base)
+ [Creating images from alternative base images](#images-create-from-alt)
+ [Upload the image to the Amazon ECR repository](#images-upload)
+ [Create an image using the AWS SAM toolkit](#images-create-sam)

## Prerequisites<a name="images-reqs"></a>

To deploy a container image to Lambda, you need the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) and [Docker CLI](https://docs.docker.com/get-docker)\. Additionally, note the following requirements:
+ The container image must implement the Lambda [Runtime API](runtimes-api.md)\. The AWS open\-source [runtime interface clients](runtimes-images.md#runtimes-api-client) implement the API\. You can add a runtime interface client to your preferred base image to make it compatible with Lambda\.
+ The container image must be able to run on a read\-only file system\. Your function code can access a writable `/tmp` directory with between 512 MB and 10,240 MB, in 1\-MB increments, of storage\. 
+ The default Lambda user must be able to read all the files required to run your function code\. Lambda follows security best practices by defining a default Linux user with least\-privileged permissions\. Verify that your application code does not rely on files that other Linux users are restricted from running\.
+ Lambda supports only Linux\-based container images\.
+ Lambda provides multi\-architecture base images\. However, the image you build for your function must target only one of the architectures\. Lambda does not support functions that use multi\-architecture container images\.

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

## Creating images from AWS base images<a name="images-create-from-base"></a>

To build a container image for a new Lambda function, you can start with an AWS base image for Lambda\. Lambda provides two types of base images:
+ Multi\-architecture base image

  Specify one of the main image tags \(such as `python:3.9` or `java:11`\) to choose this type of image\.
+ Architecture\-specific base image

  Specify an image tag with an architecture suffix\. For example, specify `3.9-arm64` to choose the arm64 base image for Python 3\.9\.

You can also use an [alternative base image from another container registry](#images-create-from-alt)\. Lambda provides open\-source runtime interface clients that you add to an alternative base image to make it compatible with Lambda\.

**Note**  
AWS periodically provides updates to the AWS base images for Lambda\. If your Dockerfile includes the image name in the FROM property, your Docker client pulls the latest version of the image from the Amazon ECR repository\. To use the updated base image, you must rebuild your container image and [update the function code](gettingstarted-images.md#configuration-images-update)\.

**To create an image from an AWS base image for Lambda**

1. On your local machine, create a project directory for your new function\.

1. Create a directory named **app** in the project directory, and then add your function handler code to the app directory\.

1. Use a text editor to create a new Dockerfile\.

   The AWS base images provide the following environment variables:
   + LAMBDA\_TASK\_ROOT=/var/task
   + LAMBDA\_RUNTIME\_DIR=/var/runtime

   Install any dependencies under the $\{LAMBDA\_TASK\_ROOT\} directory alongside the function handler to ensure that the Lambda runtime can locate them when the function is invoked\.

   The following shows an example Dockerfile for Node\.js, Python, and Ruby:

------
#### [ Node\.js 14 ]

   ```
   FROM public.ecr.aws/lambda/nodejs:14
   
   # Assumes your function is named "app.js", and there is a package.json file in the app directory 
   COPY app.js package.json  ${LAMBDA_TASK_ROOT}
   
   # Install NPM dependencies for function
   RUN npm install
   
   # Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
   CMD [ "app.handler" ]
   ```

------
#### [ Python 3\.8 ]

   ```
   FROM public.ecr.aws/lambda/python:3.8
   
   # Install the function's dependencies using file requirements.txt
   # from your project folder.
   
   COPY requirements.txt  .
   RUN  pip3 install -r requirements.txt --target "${LAMBDA_TASK_ROOT}"
   
   # Copy function code
   COPY app.py ${LAMBDA_TASK_ROOT}
   
   # Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
   CMD [ "app.handler" ]
   ```

------
#### [ Ruby 2\.7 ]

   ```
   FROM public.ecr.aws/lambda/ruby:2.7
   
   # Copy function code
   COPY app.rb ${LAMBDA_TASK_ROOT}
   
   # Copy dependency management file
   COPY Gemfile ${LAMBDA_TASK_ROOT}
   
   # Install dependencies under LAMBDA_TASK_ROOT
   ENV GEM_HOME=${LAMBDA_TASK_ROOT}
   RUN bundle install
   
   # Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
   CMD [ "app.LambdaFunction::Handler.process" ]
   ```

------

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

## Creating images from alternative base images<a name="images-create-from-alt"></a>

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

## Upload the image to the Amazon ECR repository<a name="images-upload"></a>

In the following commands, replace `123456789012` with your AWS account ID and set the region value to the region where you want to create the Amazon ECR repository\.

**Note**  
In Amazon ECR, if you reassign the image tag to another image, Lambda does not update the image version\.

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

Now that your container image resides in the Amazon ECR container registry, you can [create and run](gettingstarted-images.md) the Lambda function\.

## Create an image using the AWS SAM toolkit<a name="images-create-sam"></a>

You can use the AWS Serverless Application Model \(AWS SAM\) toolkit to create and deploy a function defined as a container image\. For a new project, you can use the AWS SAM CLI `init` command to set up the scaffolding for your project in your preferred runtime\.

In your AWS SAM template, you set the `Runtime` type to `Image` and provide the URI of the base image\.

For more information, see [Building applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) in the *AWS Serverless Application Model Developer Guide*\.