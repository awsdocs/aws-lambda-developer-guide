# Deploy Go Lambda functions with container images<a name="go-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Go function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

**Topics**
+ [AWS base images for Go](#go-image-base)
+ [Go runtime interface clients](#go-image-clients)
+ [Deploying Go with the `provided.al2` base image](#go-image-al2)
+ [Deploying Go with an alternative base image](#go-image-other)

## AWS base images for Go<a name="go-image-base"></a>

AWS provides the following base image for Go:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
|  1  | Go 1\.x | Amazon Linux 2018\.03 | [Dockerfile for Go 1\.x on GitHub](https://github.com/aws/aws-lambda-base-images/blob/go1.x/Dockerfile.go1.x) | 

Docker Hub repository: amazon/aws\-lambda\-go

Amazon ECR repository: gallery\.ecr\.aws/lambda/go

## Go runtime interface clients<a name="go-image-clients"></a>

AWS does not provide a separate runtime interface client for Go\. The `aws-lambda-go/lambda` package includes an implementation of the runtime interface\. 

## Deploying Go with the `provided.al2` base image<a name="go-image-al2"></a>

To build a container image for Go that runs on Amazon Linux 2, use the `provided.al2` base image\. For more information about this base image, see [provided](https://gallery.ecr.aws/lambda/provided) in the Amazon ECR public gallery\. 

You include the `aws-lambda-go/lambda` package with your Go handler\. This package implements the programming model for Go, including the runtime interface client\. The `provided.al2` base image also includes the runtime interface emulator\. 

**To build and deploy a Go function with the `provided.al2` base image\.**

Note that the first three steps are identical whether you deploy your function as a \.zip file archive or as a container image\.

1. On your local machine, create a project directory for your new function\.

1. From your project folder, run the following command to install the required Lambda Go libraries\. 

   ```
   go get github.com/aws/aws-lambda-go
   ```

   For a description of the Lambda Go libraries libraries, see [Building Lambda functions with Go](lambda-golang.md)\.

1. Create your [Go handler code](golang-handler.md) and include the `aws-lambda-go/lambda` package\.

1. Use a text editor to create a Dockerfile in your project directory\. The following example Dockerfile uses the AWS `provided.al2` base image\.

   ```
   FROM public.ecr.aws/lambda/provided:al2 as build
   # install compiler
   RUN yum install -y golang
   RUN go env -w GOPROXY=direct
   # cache dependencies
   ADD go.mod go.sum ./
   RUN go mod download
   # build
   ADD . .
   RUN go build -o /main
   # copy artifacts to a clean image
   FROM public.ecr.aws/lambda/provided:al2
   COPY --from=build /main /main
   ENTRYPOINT [ "/main" ]
   ```

1. Build your Docker image with the `docker build` command\. Enter a name for the image\. The following example names the image `hello-world`\.

   ```
   docker build -t hello-world . 
   ```

1. Authenticate the Docker CLI to your Amazon ECR registry\.

   ```
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com    
   ```

1. Tag your image to match your repository name, and deploy the image to Amazon ECR using the `docker push` command\. 

   ```
   docker tag  hello-world:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
   ```

## Deploying Go with an alternative base image<a name="go-image-other"></a>

You can build a container image for Go from an alternative base image\. The following example Dockerfile uses [alpine](https://gallery.ecr.aws/h1a5s9h8/alpine) as the base image\. 

```
FROM alpine as build
# install build tools
RUN apk add go git
# cache dependencies
ADD go.mod go.sum ./
RUN go mod download GOPROXY=direct
# build
ADD . .
RUN go build -o /main
# copy artifacts to a clean image
FROM alpine
COPY --from=build /main /main
ENTRYPOINT [ "/main" ]
```

The steps are the same as described for a `provided.al2` base image, with one additional consideration: if you want to add the RIE to your image, you need to follow these additional steps before you run the `docker build` command\.

**To add RIE to the image**

1. In your Dockerfile, replace the ENTRYPOINT instruction with the following content:

   ```
   # (Optional) Add Lambda Runtime Interface Emulator and use a script in the ENTRYPOINT for simpler local runs
   ADD https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie /usr/bin/aws-lambda-rie
   RUN chmod 755 /usr/bin/aws-lambda-rie
   COPY entry.sh /
   RUN chmod 755 /entry.sh
   ENTRYPOINT [ "/entry.sh" ]
   ```

1. Use a text editor to create file `entry.sh` in your project directory, containing the following content:

   ```
   #!/bin/sh
   if [ -z "${AWS_LAMBDA_RUNTIME_API}" ]; then
     exec /usr/bin/aws-lambda-rie "$@"
   else
     exec "$@"
   fi
   ```

If you do not want to add the RIE to your image, you can test your image locally without adding RIE to the image\. 

**To test locally without adding RIE to the image**

1. From your project directory, run the following command to download the RIE from GitHub and install it on your local machine\.

   ```
   mkdir -p ~/.aws-lambda-rie && curl -Lo ~/.aws-lambda-rie/aws-lambda-rie \
   https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie \
   && chmod +x ~/.aws-lambda-rie/aws-lambda-rie
   ```

1. Run your Lambda image function using the `docker run` command\. In the following example, `/main` is the path to the function entry point\.

   ```
   docker run -d -v ~/.aws-lambda-rie:/aws-lambda --entrypoint /aws-lambda/aws-lambda-rie  -p 9000:8080 myfunction:latest /main      
   ```

   This runs the image as a container and starts up an endpoint locally at `localhost:9000/2015-03-31/functions/function/invocations`\.

1. Post an event to the following endpoint using a `curl` command:

   ```
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
   ```

   This command invokes the function running in the container image and returns a response\.

For more information about testing your image locally with the RIE, see [Testing Lambda container images locally](images-test.md)\. 