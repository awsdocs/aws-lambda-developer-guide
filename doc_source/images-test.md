# Testing Lambda container images locally<a name="images-test"></a>

The AWS Lambda Runtime Interface Emulator \(RIE\) is a proxy for the Lambda Runtime API that allows you to locally test your Lambda function packaged as a container image\. The emulator is a lightweight web server that converts HTTP requests into JSON events to pass to the Lambda function in the container image\. 

The AWS base images for Lambda include the RIE component\. If you use an alternate base image, you can test your image without adding RIE to the image\. You can also build the RIE component into your base image\. AWS provides an open\-sourced RIE component on the AWS GitHub repository\. Note that there are separate RIE components for the x86\-64 architecture and the arm64 architecture\.

You can use the emulator to test whether your function code is compatible with the Lambda environment\. Also use the emulator to test that your Lambda function runs to completion successfully and provides the expected output\. If you build extensions and agents into your container image, you can use the emulator to test that the extensions and agents work correctly with the Lambda Extensions API\.

For examples of how to use the RIE, see [Container image support for Lambda](http://aws.amazon.com/blogs/aws/new-for-aws-lambda-container-image-support/) on the AWS Blog\.

**Topics**
+ [Guidelines for using the RIE](#images-test-limitations)
+ [Environment variables](#images-test-env)
+ [Test an image with RIE included in the image](#images-test-AWSbase)
+ [Build RIE into your base image](#images-test-alternative)
+ [Test an image without adding RIE to the image](#images-test-add)

## Guidelines for using the RIE<a name="images-test-limitations"></a>

Note the following guidelines when using the Runtime Interface Emulator:
+ The RIE does not emulate Lambda’s security and authentication configurations, or Lambda orchestration\. 
+ Lambda provides an emulator for each of the instruction set architectures\.
+ The emulator does not support AWS X\-Ray tracing or other Lambda integrations\.

## Environment variables<a name="images-test-env"></a>

The runtime interface emulator supports a subset of [environment variables](configuration-envvars.md) for the Lambda function in the local running image\.

If your function uses security credentials, you can configure the credentials by setting the following environment variables:
+ `AWS_ACCESS_KEY_ID`
+ `AWS_SECRET_ACCESS_KEY`
+ `AWS_SESSION_TOKEN`
+ `AWS_REGION`

To set the function timeout, configure `AWS_LAMBDA_FUNCTION_TIMEOUT`\. Enter the maximum number of seconds that you want to allow the function to run\.

The emulator does not populate the following Lambda environment variables\. However, you can set them to match the values that you expect when the function runs in the Lambda service:
+ `AWS_LAMBDA_FUNCTION_VERSION`
+ `AWS_LAMBDA_FUNCTION_NAME`
+ `AWS_LAMBDA_FUNCTION_MEMORY_SIZE`

## Test an image with RIE included in the image<a name="images-test-AWSbase"></a>

The AWS base images for Lambda include the runtime interface emulator\. You can also follow these steps after you build the RIE into your alternative base image\. 

**To test your Lambda function with the emulator**

1. Build your image locally using the `docker build` command\.

   ```
   docker build -t myfunction:latest .
   ```

1. Run your container image locally using the `docker run` command\.

   ```
   docker run -p 9000:8080  myfunction:latest 
   ```

   This command runs the image as a container and starts up an endpoint locally at `localhost:9000/2015-03-31/functions/function/invocations`\.

1. From a new terminal window, post an event to the following endpoint using a `curl` command:

   ```
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
   ```

   This command invokes the Lambda function running in the container image and returns a response\.

## Build RIE into your base image<a name="images-test-alternative"></a>

You can build RIE into a base image\. The following steps show how to download the RIE from GitHub to your local machine and update your Dockerfile to install RIE\. 

**To build the emulator into your image**

1. Create a script and save it in your project directory\. Set execution permissions for the script file\.

   The script checks for the presence of the `AWS_LAMBDA_RUNTIME_API` environment variable, which indicates the presence of the runtime API\. If the runtime API is present, the script runs the [runtime interface client](runtimes-images.md#runtimes-api-client)\. Otherwise, the script runs the runtime interface emulator\. 

   The following example shows a typical script for a Node\.js function\.

   ```
   #!/bin/sh
   if [ -z "${AWS_LAMBDA_RUNTIME_API}" ]; then
     exec /usr/local/bin/aws-lambda-rie /usr/local/bin/npx aws-lambda-ric $@
   else
     exec /usr/local/bin/npx aws-lambda-ric $@
   fi
   ```

   The following example shows a typical script for a Python function\.

   ```
   #!/bin/sh
   if [ -z "${AWS_LAMBDA_RUNTIME_API}" ]; then
     exec /usr/local/bin/aws-lambda-rie /usr/local/bin/python -m awslambdaric $@
   else
     exec /usr/local/bin/python -m awslambdaric $@
   fi
   ```

1. Download the runtime interface emulator for your target architecture from GitHub into your project directory\. Lambda provides an emulator for each of the instruction set architectures\.
   + x86\_64 – Download [aws\-lambda\-rie](https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie)
   + arm64 – Download [aws\-lambda\-rie\-arm64](https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie-arm64)

1. Copy the script, install the emulator package, and change `ENTRYPOINT` to run the new script by adding the following lines to your Dockerfile\. 

   To use the default x86\-64 architecture:

   ```
   COPY ./entry_script.sh /entry_script.sh
   ADD aws-lambda-rie-x86_64 /usr/local/bin/aws-lambda-rie
   ENTRYPOINT [ "/entry_script.sh" ]
   ```

   To use the arm64 architecture:

   ```
   COPY ./entry_script.sh /entry_script.sh
   ADD aws-lambda-rie-arm64 /usr/local/bin/aws-lambda-rie
   ENTRYPOINT [ "/entry_script.sh" ]
   ```

1. Build your image locally using the `docker build` command\.

   ```
   docker build -t myfunction:latest .
   ```

## Test an image without adding RIE to the image<a name="images-test-add"></a>

You install the runtime interface emulator to your local machine\. When you run the container image, you set the entry point to be the emulator\. 

**To test an image without adding RIE to the image**

1. From your project directory, run the following command to download the RIE \(x86\-64 architecture\) from GitHub and install it on your local machine\.

   ```
   mkdir -p ~/.aws-lambda-rie && curl -Lo ~/.aws-lambda-rie/aws-lambda-rie \
   https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie \
   && chmod +x ~/.aws-lambda-rie/aws-lambda-rie
   ```

   To download the RIE for arm64 architecture, use the previous command with a different GitHub download url\.

   ```
    https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie-arm64 \            
   ```

1. Run your Lambda function using the `docker run` command\. 

   ```
   docker run -d -v ~/.aws-lambda-rie:/aws-lambda -p 9000:8080 \
     --entrypoint /aws-lambda/aws-lambda-rie hello-world:latest <image entrypoint> \
         <(optional) image command>
   ```

   This runs the image as a container and starts up an endpoint locally at `localhost:9000/2015-03-31/functions/function/invocations`\.

1. Post an event to the following endpoint using a `curl` command:

   ```
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
   ```

   This command invokes the function running in the container image and returns a response\.