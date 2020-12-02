# Testing Lambda container images locally<a name="images-test"></a>

The Lambda Runtime Interface Emulator \(RIE\) is a proxy for the Lambda Runtime API that allows you to locally test your Lambda function packaged as a container image\. The emulator is a lightweight web server that converts HTTP requests into JSON events to pass to the Lambda function in the container image\. 

The AWS base images for Lambda include the RIE component\. If you use an alternate base image, you can test your image without adding RIE to the image\. You can also build the RIE component into your base image\. AWS provides an open\-sourced RIE component on the AWS GitHub repository\. 

You can use the emulator to test whether your function code is compatible with the Lambda environment\. Also use the emulator to test that your function runs to completion successfully and provides the expected output\. If you build extensions and agents into your container image, use the emulator to test that the extensions and agents work correctly with the Lambda Extensions API\.

**Topics**
+ [Guidelines for using the RIE](#images-test-limitations)
+ [Environment variables](#images-test-env)
+ [Test an image with RIE included in the image](#images-test-AWSbase)
+ [Build RIE into your base image](#images-test-alternative)
+ [Test an image without adding RIE to the image](#images-test-add)

## Guidelines for using the RIE<a name="images-test-limitations"></a>

Note the following guidelines when using the Runtime Interface Emulator:
+ The RIE does not emulate Lambda’s security and authentication configurations, or Lambda orchestration\. 
+ The emulator supports only Linux x84\-64 architectures\.
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
+ `AWS_LAMBDA_FUNCION_NAME`
+ `AWS_LAMBDA_MEMORY_SIZE`

## Test an image with RIE included in the image<a name="images-test-AWSbase"></a>

The AWS base images for Lambda include the runtime interface emulator\. You can also follow these steps if you built the RIE into your alternative base image\. 

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

1. Post an event to the following endpoint using a `curl` command:

   ```
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'.
   ```

   This command invokes the function running in the container image and returns a response\.

## Build RIE into your base image<a name="images-test-alternative"></a>

You can build RIE into a base image\. Download the RIE from GitHub to your local machine and update your Dockerfile to install RIE\. 

**To build the emulator into your image**

1. Create a script and save it in your project directory\. The following example shows a typical script for a Node\.js function\. The presence of the `AWS_LAMBDA_RUNTIME_API` environment variable indicates the presence of the runtime API\. If the runtime API is present, the script runs the [runtime interface client](runtimes-images.md#runtimes-api-client)\. Otherwise, the script runs the runtime interface emulator\.

   ```
   #!/bin/sh
   if [ -z "${AWS_LAMBDA_RUNTIME_API}" ]; then
     exec /usr/local/bin/aws-lambda-rie /usr/bin/npx aws-lambda-ric
   else
     exec /usr/bin/npx aws-lambda-ric
   fi
   ```

1. Download the [runtime interface emulator](https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie) from GitHub into your project directory\.

1. Install the emulator package and change `ENTRYPOINT` to run the new script by adding the following lines to your Dockerfile:

   ```
   ADD aws-lambda-rie /usr/local/bin/aws-lambda-rie
   ENTRYPOINT [ “/entry_script.sh” ]
   ```

1. Build your image locally using the `docker build` command\.

   ```
   docker build -t myfunction:latest .
   ```

## Test an image without adding RIE to the image<a name="images-test-add"></a>

You install the runtime interface emulator to your local machine\. When you run the image function, you set the entry point to be the emulator\. 

**To test an image without adding RIE to the image**

1. From your project directory, run the following command to download the RIE from GitHub and install it on your local machine\.

   ```
   mkdir -p ~/.aws-lambda-rie && curl -Lo ~/.aws-lambda-rie/aws-lambda-rie \
   https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie \
   && chmod +x ~/.aws-lambda-rie/aws-lambda-rie
   ```

1. Run your Lambda image function using the `docker run` command\. 

   ```
   docker run -d -v ~/.aws-lambda-rie:/aws-lambda -p 9000:8080 myfunction:latest 
       --entrypoint /aws-lambda/aws-lambda-rie  <image entrypoint> <(optional) image command>
   ```

   This runs the image as a container and starts up an endpoint locally at `localhost:9000/2015-03-31/functions/function/invocations`\.

1. Post an event to the following endpoint using a `curl` command:

   ```
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'.
   ```

   This command invokes the function running in the container image and returns a response\.