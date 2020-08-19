# Using AWS Lambda with AWS X\-Ray<a name="services-xray"></a>

You can use AWS X\-Ray to visualize the components of your application, identify performance bottlenecks, and troubleshoot requests that resulted in an error\. Your Lambda functions send trace data to X\-Ray, and X\-Ray processes the data to generate a service map and searchable trace summaries\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-errorprocessor-servicemap.png)

If you've enabled X\-Ray tracing in a service that invokes your function, Lambda sends traces to X\-Ray automatically\. The upstream service, such as Amazon API Gateway, or an application hosted on Amazon EC2 that is instrumented with the X\-Ray SDK, samples incoming requests and adds a tracing header that tells Lambda to send traces or not\.

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

In X\-Ray, a *trace* records information about a request that is processed by one or more *services*\. Services record *segments* that contain layers of *subsegments*\. Lambda records a segment for the Lambda service that handles the invocation request, and one for the work done by the function\. The function segment comes with subsegments for `Initialization`, `Invocation` and `Overhead`\.

The following example shows a trace with 2 segments\. Both are named **my\-function**, but one is type `AWS::Lambda` and the other is `AWS::Lambda::Function`\. The function segment is expanded to show its subsegments\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/nodejs-xray-timeline.png)

**Important**  
In Lambda, you can use the X\-Ray SDK to extend the `Invocation` subsegment with additional subsegments for downstream calls, annotations, and metadata\. You can't access the function segment directly or record work done outside of the handler invocation scope\.

See the following topics for a language\-specific introduction to tracing in Lambda:
+ [Instrumenting Node\.js code in AWS Lambda](nodejs-tracing.md)
+ [Instrumenting Python code in AWS Lambda](python-tracing.md)
+ [Instrumenting Ruby code in AWS Lambda](ruby-tracing.md)
+ [Instrumenting Java code in AWS Lambda](java-tracing.md)
+ [Instrumenting Go code in AWS Lambda](golang-tracing.md)
+ [Instrumenting C\# code in AWS Lambda](csharp-tracing.md)

For a full list of services that support active instrumentation, see [Supported AWS services](https://docs.aws.amazon.com/xray/latest/devguide/xray-usage.html#xray-usage-codechanges) in the AWS X\-Ray Developer Guide\.

**Topics**
+ [Execution role permissions](#services-xray-permissions)
+ [The AWS X\-Ray daemon](#services-xray-daemon)
+ [Enabling active tracing with the Lambda API](#services-xray-api)
+ [Enabling active tracing with AWS CloudFormation](#services-xray-cloudformation)

## Execution role permissions<a name="services-xray-permissions"></a>

Lambda needs the following permissions to send trace data to X\-Ray\. Add them to your function's [execution role](lambda-intro-execution-role.md)\.
+ [xray:PutTraceSegments](https://docs.aws.amazon.com/xray/latest/api/API_PutTraceSegments.html)
+ [xray:PutTelemetryRecords](https://docs.aws.amazon.com/xray/latest/api/API_PutTelemetryRecords.html)

These permissions are included in the [AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home?#/policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess) managed policy\.

## The AWS X\-Ray daemon<a name="services-xray-daemon"></a>

Instead of sending trace data directly to the X\-Ray API, the X\-Ray SDK uses a daemon process\. The AWS X\-Ray daemon is an application that runs in the Lambda environment and listens for UDP traffic that contains segments and subsegments\. It buffers incoming data and writes it to X\-Ray in batches, reducing the processing and memory overhead required to trace invocations\.

The Lambda runtime allows the daemon to up to 3 percent of your function's configured memory or 16 MB, whichever is greater\. If your function runs out of memory during invocation, the runtime terminates the daemon process first to free up memory\.

The daemon process is fully managed by Lambda and cannot be configured by the user\. All segments generated by function invocations are recorded in the same account as the Lambda function\. The daemon cannot be configured to redirect them to any other account\.

For more information, see [The X\-Ray daemon](https://docs.aws.amazon.com/xray/latest/devguide/xray-daemon.html) in the X\-Ray Developer Guide\.

## Enabling active tracing with the Lambda API<a name="services-xray-api"></a>

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

## Enabling active tracing with AWS CloudFormation<a name="services-xray-cloudformation"></a>

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