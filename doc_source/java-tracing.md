# Instrumenting Java code in Lambda<a name="java-tracing"></a>

Lambda integrates with AWS X\-Ray to help you trace, debug, and optimize Lambda applications\. You can use X\-Ray to trace a request as it traverses resources in your application, which may include Lambda functions and other AWS services\.

To send tracing data to X\-Ray, you can use one of two SDK libraries:
+ [AWS Distro for OpenTelemetry \(ADOT\)](http://aws.amazon.com/otel) – A secure, production\-ready, AWS\-supported distribution of the OpenTelemetry \(OTel\) SDK\.
+ [AWS X\-Ray SDK for Java](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java.html) – A collection of libraries for generating and sending trace data to X\-Ray\.

Both ADOT and the X\-Ray SDK offer ways to send your telemetry data to the X\-Ray service\. You can then use X\-Ray to view, filter, and gain insights into your application's performance metrics to identify issues and opportunities for optimization\.

**Important**  
**ADOT is the preferred method for instrumenting your Lambda functions**\. We recommend using ADOT for all new applications\.

**Topics**
+ [Using ADOT to instrument your Java functions](#java-adot)
+ [Using the X\-Ray SDK to instrument your Java functions](#java-xray-sdk)
+ [Activating tracing with the Lambda API](#java-tracing-api)
+ [Activating tracing with AWS CloudFormation](#java-tracing-cloudformation)
+ [Storing runtime dependencies in a layer \(X\-Ray SDK\)](#java-tracing-layers)
+ [X\-Ray tracing in sample applications \(X\-Ray SDK\)](#java-tracing-samples)

## Using ADOT to instrument your Java functions<a name="java-adot"></a>

ADOT provides fully managed Lambda [layers](gettingstarted-concepts.md#gettingstarted-concepts-layer) that package everything you need to collect telemetry data using the OTel SDK\. By consuming this layer, you can instrument your Lambda functions without having to modify any function code\. You can also configure your layer to do custom initialization of OTel\. For more information, see [Custom configuration for the ADOT Collector on Lambda](https://aws-otel.github.io/docs/getting-started/lambda#custom-configuration-for-the-adot-collector-on-lambda) in the ADOT documentation\.

For Java runtimes, you can choose between two layers to consume:
+ **AWS managed Lambda layer for ADOT Java \(Auto\-instrumentation Agent\)** – This layer automatically transforms your function code at startup to collect tracing data\. For detailed instructions on how to consume this layer together with the ADOT Java agent, see [AWS Distro for OpenTelemetry Lambda Support for Java \(Auto\-instrumentation Agent\)](https://aws-otel.github.io/docs/getting-started/lambda/lambda-java-auto-instr) in the ADOT documentation\.
+ **AWS managed Lambda layer for ADOT Java** – This layer also provides built\-in instrumentation for Lambda functions, but it requires a few manual code changes to initialize the OTel SDK\. For detailed instructions on how to consume this layer, see [AWS Distro for OpenTelemetry Lambda Support for Java](https://aws-otel.github.io/docs/getting-started/lambda/lambda-java) in the ADOT documentation\.

## Using the X\-Ray SDK to instrument your Java functions<a name="java-xray-sdk"></a>

To record data about calls that your function makes to other resources and services in your application, you can add the X\-Ray SDK for Java to your build configuration\. The following example shows a Gradle build configuration that includes the libraries that activate automatic instrumentation of AWS SDK for Java 2\.x clients\.

**Example [build\.gradle](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-java/build.gradle) – Tracing dependencies**  

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

After you add the correct dependencies, activate tracing in your function's configuration:

**To turn on active tracing**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration**, and then choose **Monitoring and operations tools**\.

1. Choose **Edit**\.

1. Under **AWS X\-Ray**, turn on **Active tracing**\.

1. Choose **Save**\.

**Pricing**  
You can use X\-Ray tracing for free each month up to a certain limit as part of the AWS Free Tier\. Beyond that threshold, X\-Ray charges for trace storage and retrieval\. For more information, see [AWS X\-Ray pricing](http://aws.amazon.com/xray/pricing/)\.

Your function needs permissions to upload trace data to X\-Ray\. When you turn on active tracing in the Lambda console, Lambda adds the required permissions to your function's [execution role](lambda-intro-execution-role.md)\. You can also manually add the AWS Identity and Access Management \(IAM\) policy [AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home#policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess) to your execution role\.

After you've configured active tracing, you can observe specific requests through your application\. The [X\-Ray service graph](https://docs.aws.amazon.com/xray/latest/devguide/xray-concepts.html#xray-concepts-servicegraph) shows information about your application and all its components\. The following example from the [error processor sample application](samples-errorprocessor.md) shows an application with two Lambda functions\. The primary function processes events and sometimes returns errors\. The second function at the top processes errors that appear in the first function's log group and uses the AWS SDK to call X\-Ray, Amazon Simple Storage Service \(Amazon S3\), and Amazon CloudWatch Logs\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-errorprocessor-servicemap.png)

X\-Ray may not trace all requests to your application\. X\-Ray applies a sampling algorithm to ensure that tracing is efficient, while still providing a representative sample of all requests\. The default sample rule is one request per second and five percent of additional requests\. You cannot configure this sampling rate for your functions\.

For each trace, Lambda records two segments, which creates two nodes on the service graph\. The following image highlights the primary function from the error processor example\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/xray-servicemap-function.png)

The first node on the left represents the Lambda service, which receives the invocation request\. The second node on the right records the work of your function\. The following example shows a trace with these two segments\. Both are named **my\-function**, but one is type `AWS::Lambda` and the other is `AWS::Lambda::Function`\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/nodejs-xray-timeline.png)

This example expands the function segment to show its three subsegments:
+ **Initialization** – Represents time spent loading your function and running [initialization code](foundation-progmodel.md)\. This subsegment appears only for the first event that each instance of your function processes\.
+ **Invocation** – Represents the work that your handler code does\.
+ **Overhead** – Represents the work that the Lambda runtime does to prepare to handle the next event\.

You can also instrument HTTP clients, record SQL queries, and create custom subsegments with annotations and metadata\. For more information, see [AWS X\-Ray SDK for Java](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java.html) in the *AWS X\-Ray Developer Guide*\.

## Activating tracing with the Lambda API<a name="java-tracing-api"></a>

To manage tracing configuration with the AWS CLI or AWS SDK, use the following API operations:
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [GetFunctionConfiguration](API_GetFunctionConfiguration.md)
+ [CreateFunction](API_CreateFunction.md)

The following example AWS CLI command enables active tracing on a function named **my\-function**\.

```
aws lambda update-function-configuration --function-name my-function \
--tracing-config Mode=Active
```

Tracing mode is part of the version\-specific configuration that is locked when you publish a version of your function\. You can't change the tracing mode on a published version\.

## Activating tracing with AWS CloudFormation<a name="java-tracing-cloudformation"></a>

To activate tracing on an `AWS::Lambda::Function` resource in an AWS CloudFormation template, use the `TracingConfig` property\.

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

**Example [template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/template.yml) – Tracing configuration**  

```
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Tracing: Active
      ...
```

## Storing runtime dependencies in a layer \(X\-Ray SDK\)<a name="java-tracing-layers"></a>

If you use the X\-Ray SDK to instrument AWS SDK clients in your function code, your deployment package can become quite large\. To avoid uploading runtime dependencies every time that you update your function code, package them in a Lambda layer\.

The following example shows an `AWS::Serverless::LayerVersion` resource that stores the AWS SDK for Java and X\-Ray SDK for Java\.

**Example [template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-java/template.yml) – Dependencies layer**  

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

With this configuration, you update the library layer only if you change your runtime dependencies\. The function deployment package contains only your code\. When you update your function code, upload time is much faster than if you include dependencies in the deployment package\.

Creating a layer for dependencies requires build configuration changes to generate the layer archive prior to deployment\. For a working example, see the [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) sample application on GitHub\.

## X\-Ray tracing in sample applications \(X\-Ray SDK\)<a name="java-tracing-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of X\-Ray tracing\. Each sample application includes scripts for easy deployment and cleanup, an AWS SAM template, and supporting resources\.

**Sample Lambda applications in Java**
+ [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-java) – A Java function that shows the use of Lambda's Java libraries, logging, environment variables, layers, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A minimal Java function with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A minimal Java function that uses the latest version \(3\.0\.0 and newer\) of the [aws\-lambda\-java\-events](java-package.md) library\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.

All of the sample applications have active tracing enabled for Lambda functions\. The `blank-java` application shows automatic instrumentation of AWS SDK for Java 2\.x clients, segment management for tests, custom subsegments, and the use of Lambda layers to store runtime dependencies\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/blank-java-servicemap.png)

This example from the `blank-java` sample application shows nodes for the Lambda service, a function, and the Lambda API\. The function calls the Lambda API to monitor storage usage in Lambda\.