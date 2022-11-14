# Instrumenting Go code in AWS Lambda<a name="golang-tracing"></a>

Lambda integrates with AWS X\-Ray to help you trace, debug, and optimize Lambda applications\. You can use X\-Ray to trace a request as it traverses resources in your application, which may include Lambda functions and other AWS services\.

To send tracing data to X\-Ray, you can use one of two SDK libraries:
+ [AWS Distro for OpenTelemetry \(ADOT\)](http://aws.amazon.com/otel) – A secure, production\-ready, AWS\-supported distribution of the OpenTelemetry \(OTel\) SDK\.
+ [AWS X\-Ray SDK for Go](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-go.html) – An SDK for generating and sending trace data to X\-Ray\.

Both ADOT and the X\-Ray SDK offer ways to send your telemetry data to the X\-Ray service\. You can then use X\-Ray to view, filter, and gain insights into your application's performance metrics to identify issues and opportunities for optimization\.

**Important**  
**ADOT is the preferred method for instrumenting your Lambda functions**\. We recommend using ADOT for all new applications\. However, due to the flexibility OpenTelemetry offers, your Lambda function invocations may experience cold start latency increases\. If you're optimizing for low\-latency and also do not require OpenTelemetry's advanced capabilities such as telemetry correlation and dynamically configurable backend destinations, you may want to use the AWS X\-Ray SDK over ADOT\.

**Topics**
+ [Using ADOT to instrument your Go functions](#golang-adot)
+ [Using the X\-Ray SDK to instrument your Go functions](#golang-xray-sdk)
+ [Activating tracing with the Lambda console](#golang-tracing-console)
+ [Activating tracing with the Lambda API](#golang-tracing-api)
+ [Activating tracing with AWS CloudFormation](#golang-tracing-cloudformation)
+ [Interpreting an X\-Ray trace](#golang-tracing-interpretation)

## Using ADOT to instrument your Go functions<a name="golang-adot"></a>

ADOT provides fully managed Lambda [layers](gettingstarted-concepts.md#gettingstarted-concepts-layer) that package everything you need to collect telemetry data using the OTel SDK\. By consuming this layer, you can instrument your Lambda functions without having to modify any function code\. You can also configure your layer to do custom initialization of OTel\. For more information, see [Custom configuration for the ADOT Collector on Lambda](https://aws-otel.github.io/docs/getting-started/lambda#custom-configuration-for-the-adot-collector-on-lambda) in the ADOT documentation\.

For Go runtimes, you can add the **AWS managed Lambda layer for ADOT Go** to automatically instrument your functions\. For detailed instructions on how to add this layer, see [AWS Distro for OpenTelemetry Lambda Support for Go](https://aws-otel.github.io/docs/getting-started/lambda/lambda-go) in the ADOT documentation\.

## Using the X\-Ray SDK to instrument your Go functions<a name="golang-xray-sdk"></a>

To record details about calls that your Lambda function makes to other resources in your application, you can also use the AWS X\-Ray SDK for Go\. To get the SDK, download the SDK from its [GitHub repository](https://github.com/aws/aws-xray-sdk-go) with `go get`:

```
go get github.com/aws/aws-xray-sdk-go
```

To instrument AWS SDK clients, pass the client to the `xray.AWS()` method\. You can then trace calls by using the `WithContext` version of the method\.

```
svc := s3.New(session.New())
xray.AWS(svc.Client)
...
svc.ListBucketsWithContext(ctx aws.Context, input *ListBucketsInput)
```

After you add the correct dependencies and make the necessary code changes, activate tracing in your function's configuration via the Lambda console or the API\.

## Activating tracing with the Lambda console<a name="golang-tracing-console"></a>

To toggle active tracing on your Lambda function with the console, follow these steps:

**To turn on active tracing**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Monitoring and operations tools**\.

1. Choose **Edit**\.

1. Under **X\-Ray**, toggle on **Active tracing**\.

1. Choose **Save**\.

## Activating tracing with the Lambda API<a name="golang-tracing-api"></a>

Configure tracing on your Lambda function with the AWS CLI or AWS SDK, use the following API operations:
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)
+ [GetFunctionConfiguration](API_GetFunctionConfiguration.md)
+ [CreateFunction](API_CreateFunction.md)

The following example AWS CLI command enables active tracing on a function named **my\-function**\.

```
aws lambda update-function-configuration --function-name my-function \
--tracing-config Mode=Active
```

Tracing mode is part of the version\-specific configuration when you publish a version of your function\. You can't change the tracing mode on a published version\.

## Activating tracing with AWS CloudFormation<a name="golang-tracing-cloudformation"></a>

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

## Interpreting an X\-Ray trace<a name="golang-tracing-interpretation"></a>

Your function needs permission to upload trace data to X\-Ray\. When you activate tracing in the Lambda console, Lambda adds the required permissions to your function's [execution role](lambda-intro-execution-role.md)\. Otherwise, add the [AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess) policy to the execution role\.

After you've configured active tracing, you can observe specific requests through your application\. The [ X\-Ray service graph](https://docs.aws.amazon.com/xray/latest/devguide/xray-concepts.html#xray-concepts-servicegraph) shows information about your application and all its components\. The following example from the [error processor](samples-errorprocessor.md) sample application shows an application with two functions\. The primary function processes events and sometimes returns errors\. The second function at the top processes errors that appear in the first's log group and uses the AWS SDK to call X\-Ray, Amazon Simple Storage Service \(Amazon S3\), and Amazon CloudWatch Logs\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-errorprocessor-servicemap.png)

X\-Ray doesn't trace all requests to your application\. X\-Ray applies a sampling algorithm to ensure that tracing is efficient, while still providing a representative sample of all requests\. The sampling rate is 1 request per second and 5 percent of additional requests\.

**Note**  
You cannot configure the X\-Ray sampling rate for your functions\.

When using active tracing, Lambda records 2 segments per trace, which creates two nodes on the service graph\. The following image highlights these two nodes for the primary function from the error processor example above\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/xray-servicemap-function.png)

The first node on the left represents the Lambda service, which receives the invocation request\. The second node represents your specific Lambda function\. The following example shows a trace with these two segments\. Both are named **my\-function**, but one has an origin of `AWS::Lambda` and the other has origin `AWS::Lambda::Function`\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/nodejs-xray-timeline.png)

This example expands the function segment to show its three subsegments:
+ **Initialization** – Represents time spent loading your function and running [initialization code](foundation-progmodel.md)\. This subsegment only appears for the first event that each instance of your function processes\.
+ **Invocation** – Represents the time spent running your handler code\.
+ **Overhead** – Represents the time the Lambda runtime spends preparing to handle the next event\.

You can also instrument HTTP clients, record SQL queries, and create custom subsegments with annotations and metadata\. For more information, see the [AWS X\-Ray SDK for Go](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-python.html) in the *AWS X\-Ray Developer Guide*\.

**Pricing**  
You can use X\-Ray tracing for free each month up to a certain limit as part of the AWS Free Tier\. Beyond that threshold, X\-Ray charges for trace storage and retrieval\. For more information, see [AWS X\-Ray pricing](http://aws.amazon.com/xray/pricing/)\.