# Deploy Java Lambda functions with container images<a name="java-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Java function:
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
+ [AWS base images for Java](#java-image-base)
+ [Using a Java base image](#java-image-instructions)
+ [Java runtime interface clients](#java-image-clients)
+ [Deploy the container image](#java-image-deploy)

## AWS base images for Java<a name="java-image-base"></a>

AWS provides the following base images for Java:


| Tags | Runtime | Operating system | Dockerfile | Deprecation | 
| --- | --- | --- | --- | --- | 
| 11 | Java 11 | Amazon Linux 2 | [Dockerfile for Java 11 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/java11/Dockerfile.java11) |    | 
| 8\.al2 | Java 8 | Amazon Linux 2 | [Dockerfile for Java 8 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/java8.al2/Dockerfile.java8.al2) |    | 
| 8 | Java 8 | Amazon Linux | [Dockerfile for Java 8 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/java8/Dockerfile.java8) |    | 

Amazon ECR repository: [gallery\.ecr\.aws/lambda/java](https://gallery.ecr.aws/lambda/java)

## Using a Java base image<a name="java-image-instructions"></a>

For instructions on how to use a Java base image, choose the **usage** tab on [Lambda base images for Java](https://gallery.ecr.aws/lambda/java) in the *Amazon ECR repository*\. 

## Java runtime interface clients<a name="java-image-clients"></a>

Install the runtime interface client for Java using the Apache Maven package manager\. Add the following to your `pom.xml` file:

```
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-lambda-java-runtime-interface-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

For package details, see [Lambda RIC](https://search.maven.org/artifact/com.amazonaws/aws-lambda-java-runtime-interface-client) in Maven Central Repository\.

You can also view the Java client source code in the [AWS Lambda Java Support Libraries](https://github.com/aws/aws-lambda-java-libs) repository on GitHub\.

After your container image resides in the Amazon ECR container registry, you can [create and run](gettingstarted-images.md) the Lambda function\.

## Deploy the container image<a name="java-image-deploy"></a>

For a new function, you deploy the Java image when you [create the function](gettingstarted-images.md#configuration-images-create)\. For an existing function, if you rebuild the container image, you need to redeploy the image by [updating the function code](gettingstarted-images.md#configuration-images-update)\.