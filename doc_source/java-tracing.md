# Instrumenting Java code in AWS Lambda<a name="java-tracing"></a>

Lambda integrates with AWS X\-Ray to enable you to trace, debug, and optimize Lambda applications\. You can use X\-Ray to trace a request as it traverses resources in your application, from the frontend API to storage and database on the backend\. By simply adding the X\-Ray SDK library to your build configuration, you can record errors and latency for any call that your function makes to an AWS service\.

The X\-Ray *service map* shows the flow of requests through your application\. The following example from the [error processor](samples-errorprocessor.md) sample application shows an application with two functions\. The primary function processes events and sometimes returns errors\. The second function processes errors that appear in the first's log group and uses the AWS SDK to call X\-Ray, Amazon S3 and Amazon CloudWatch Logs\.

[images/sample-errorprocessor-servicemap-l.png](images/sample-errorprocessor-servicemap-l.png)

To trace requests that don't have a tracing header, enable active tracing in your function's configuration\.

**To enable active tracing**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **AWS X\-Ray**, choose **Active tracing**\.

1. Choose **Save**\.

**Pricing**  
X\-Ray has a perpetual free tier\. Beyond the free tier threshold, X\-Ray charges for trace storage and retrieval\. For details, see [AWS X\-Ray pricing](https://aws.amazon.com/xray/pricing/)\.

Your function needs permission to upload trace data to X\-Ray\. When you enable active tracing in the Lambda console, Lambda adds the required permissions to your function's [execution role](lambda-intro-execution-role.md)\. Otherwise, add the [AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess) policy to the execution role\.

X\-Ray applies a sampling algorithm to ensure that tracing is efficient, while still providing a representative sample of the requests that your application serves\. The default sampling rule is 1 request per second and 5 percent of additional requests\. This sampling rate cannot be configured for Lambda functions\.

When active tracing is enabled, Lambda records a trace for a subset of invocations\. Lambda records two *segments*, which creates two nodes on the service map\. The first node represents the Lambda service that receives the invocation request\. The second node is recorded by the function's [runtime](gettingstarted-concepts.md#gettingstarted-concepts-runtimes)\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/xray-servicemap-function.png)

To record detail about calls that your function makes to other resources and services, add the X\-Ray SDK for Java to your build configuration\. The following example shows a Gradle build configuration that includes the libraries that enable automatic instrumentation of AWS SDK for Java 2\.x clients\.

**Example [build\.gradle](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-java/build.gradle) – Tracing dependencies**  

```
dependencies {
    implementation platform('software.amazon.awssdk:bom:2.10.73')
    implementation platform('com.amazonaws:aws-xray-recorder-sdk-bom:2.4.0')
    implementation 'software.amazon.awssdk:lambda'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-core'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-core'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-v2'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-v2-instrumentor'
    ...
}
```

The following example shows a trace with 2 segments\. Both are named **my\-function**, but one is type `AWS::Lambda` and the other is `AWS::Lambda::Function`\. The function segment is expanded to show its subsegments\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/nodejs-xray-timeline.png)

The first segment represents the invocation request processed by the Lambda service\. The second segment records the work done by your function\. The function segment has 3 subsegments\.
+ **Initialization** – Represents time spent loading your function and running [initialization code](gettingstarted-features.md#gettingstarted-features-programmingmodel)\. This subsegment only appears for the first event processed by each instance of your function\.
+ **Invocation** – Represents the work done by your handler code\. By instrumenting your code, you can extend this subsegment with additional subsegments\.
+ **Overhead** – Represents the work done by the Lambda runtime to prepare to handle the next event\.

You can also instrument HTTP clients, record SQL queries, and create custom subsegments with annotations and metadata\. For more information, see [AWS X\-Ray SDK for Java ](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java.html) in the AWS X\-Ray Developer Guide\.

**Topics**
+ [Enabling active tracing with the Lambda API](#java-tracing-api)
+ [Enabling active tracing with AWS CloudFormation](#java-tracing-cloudformation)
+ [Storing runtime dependencies in a layer](#java-tracing-layers)
+ [Tracing in sample applications](#java-tracing-samples)

## Enabling active tracing with the Lambda API<a name="java-tracing-api"></a>

To manage tracing configuration with the AWS CLI or AWS SDK, use the following API operations:
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [GetFunctionConfiguration](API_GetFunctionConfiguration.md)
+ [CreateFunction](API_CreateFunction.md)

The following example AWS CLI command enables active tracing on a function named my\-function\.

```
$ aws lambda update-function-configuration --function-name my-function \
--tracing-config Mode=Active
```

Tracing mode is part of the version\-specific configuration that is locked when you publish a version of your function\. You can't change the tracing mode on a published version\.

## Enabling active tracing with AWS CloudFormation<a name="java-tracing-cloudformation"></a>

To enable active tracing on an `AWS::Lambda::Function` resource in an AWS CloudFormation template, use the `TracingConfig` property\.

**Example [function\-inline\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/templates/function-inline.yml) – Tracing configuration**  

```
Resources:
  function:
    Type: [AWS::Lambda::Function](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html)
    Properties:
      TracingConfig: 
        Mode: Active
      ...
```

For an AWS Serverless Application Model \(AWS SAM\) `AWS::Serverless::Function` resource, use the `Tracing` property\.

**Example [template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-nodejs/template.yml) – Tracing configuration**  

```
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Tracing: Active
      ...
```

## Storing runtime dependencies in a layer<a name="java-tracing-layers"></a>

If you use the X\-Ray SDK to instrument AWS SDK clients your function code, your deployment package can become quite large\. To avoid uploading runtime dependencies every time you update your functions code, package them in a [Lambda layer](configuration-layers.md)\.

The following example shows an `AWS::Serverless::LayerVersion` resource that stores the SDK for Java and X\-Ray SDK for Java\.

**Example [template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-java/template.yml) – Dependencies layer**  

```
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      CodeUri: build/distributions/blank-java.zip
      Tracing: Active
      Layers:
        - !Ref libs
      ...
  libs:
    Type: [AWS::Serverless::LayerVersion](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-layerversion.html)
    Properties:
      LayerName: blank-java-lib
      Description: Dependencies for the blank-java sample app.
      ContentUri: build/blank-java-lib.zip
      CompatibleRuntimes:
        - java8
```

With this configuration, you only update library layer if you change your runtime dependencies\. The function deployment package only contains your code\. When you update your function code, upload time is much faster than if you include dependencies in the deployment package\.

Creating a layer for dependencies requires build configuration changes to generate the layer archive prior to deployment\. For a working example, see the [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-basic) sample application\.

## Tracing in sample applications<a name="java-tracing-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of tracing\. Each sample application includes scripts for easy deployment and cleanup, an AWS SAM template, and supporting resources\.

**Sample Lambda applications in Java**
+ [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java) – A Java function that shows the use of Lambda's Java libraries, logging, environment variables, layers, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-basic) – A minimal Java function with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events) – A minimal Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that don't require the AWS SDK as a dependency, such as Amazon API Gateway\.
+ [java\-events\-v1sdk](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events-v1sdk) – A Java function that uses the [aws\-lambda\-java\-events](java-package.md) library with event types that require the AWS SDK as a dependency \(Amazon Simple Storage Service, Amazon DynamoDB, and Amazon Kinesis\)\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.

All of the sample applications have active tracing enabled for Lambda functions\. The `blank-java` application shows automatic instrumentation of AWS SDK for Java 2\.x clients, segment management for tests, custom subsegments, and the use of Lambda layers to store runtime dependencies\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/blank-java-servicemap.png)

This example from the `blank-java` sample application shows nodes for the Lambda service, a function, and the Lambda API\. The function calls the Lambda API to monitor storage use in Lambda\.