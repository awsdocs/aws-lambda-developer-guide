# Deploy Java Lambda functions with container images<a name="java-image"></a>

You can deploy your Lambda function code as a [container image](images-create.md)\. AWS provides the following resources to help you build a container image for your Java function:
+ AWS base images for Lambda

  These base images are preloaded with a language runtime and other components that are required to run the image on Lambda\. AWS provides a Dockerfile for each of the base images to help with building your container image\.
+ Open\-source runtime interface clients

  If you use a community or private enterprise base image, add a runtime interface client to the base image to make it compatible with Lambda\.

**Topics**
+ [AWS base images for Java](#java-image-base)
+ [Using a Java base image](#java-image-instructions)
+ [Java runtime interface clients](#java-image-clients)

## AWS base images for Java<a name="java-image-base"></a>

AWS provides the following base images for Java:


| Tags | Runtime | Operating system | Dockerfile | 
| --- | --- | --- | --- | 
|  11  | Java 11 \(Corretto\) | Amazon Linux 2 | [Dockerfile for Java 11 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/java11/Dockerfile.java11) | 
|  8\.al2  | Java 8 \(Corretto\) | Amazon Linux 2 | [Dockerfile for Java 8\.al2 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/java8.al2/Dockerfile.java8.al2) | 
|  8  | Java 8 \(OpenJDK\) | Amazon Linux 2018\.03 | [Dockerfile for Java 8 on GitHub](https://github.com/aws/aws-lambda-base-images/blob/java8/Dockerfile.java8) | 

Docker Hub repository: amazon/aws\-lambda\-java

Amazon ECR repository: gallery\.ecr\.aws/lambda/java

## Using a Java base image<a name="java-image-instructions"></a>

For instructions on how to use a Java base image, choose the **usage** tab on [Lambda base images for Java](https://gallery.ecr.aws/lambda/java) in the *Amazon ECR repository*\. 

The instructions are also available on [Lambda base images for Java](https://hub.docker.com/r/amazon/aws-lambda-java) in the *Docker Hub repository*\.

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