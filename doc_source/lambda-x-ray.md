# Using AWS X\-Ray<a name="lambda-x-ray"></a>

You can use AWS X\-Ray to visualize the components of your application, identify performance bottlenecks, and troubleshoot requests that resulted in an error\. Your Lambda functions send trace data to X\-Ray, and X\-Ray processes the data to generate a service map and searchable trace summaries\.

If you've enabled X\-Ray tracing in a service that invokes your function, Lambda sends traces to X\-Ray automatically\. The upstream service, such as Amazon API Gateway, or an application hosted on Amazon EC2 that is instrumented with the X\-Ray SDK, samples incoming requests and adds a tracing header that tells Lambda to send traces or not\. For a full list of services that support active instrumentation, see [Supported AWS services](https://docs.aws.amazon.com/xray/latest/devguide/xray-usage.html#xray-usage-codechanges) in the AWS X\-Ray Developer Guide\.

To trace requests that don't have a tracing header, enable active tracing in your function's configuration\.

You can enable tracing in your function's configuration\.

**To enable active tracing**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **AWS X\-Ray**, choose **Active tracing**\.

1. Choose **Save**\.

Your function needs permission to upload trace data to X\-Ray\. When you enable active tracing in the Lambda console, Lambda adds the required permissions to your function's [execution role](lambda-intro-execution-role.md)\. Otherwise, add the [AWSXRayDaemonWriteAccess](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/AWSXRayDaemonWriteAccess) policy to the execution role\.

X\-Ray applies a sampling algorithm to ensure that tracing is efficient, while still providing a representative sample of the requests that your application serves\. The default sampling rule is 1 request per second and 5 percent of additional requests\.

## Using environment variables to communicate with AWS X\-Ray<a name="lambda-x-ray-env-variables"></a>

AWS Lambda uses environment variables to facilitate communication with the X\-Ray daemon and configure the X\-Ray SDK\.
+ **\_X\_AMZN\_TRACE\_ID:** Contains the tracing header, which includes the sampling decision, trace ID, and parent segment ID\. \(To learn more about these properties, see [Tracing header](https://docs.aws.amazon.com/xray/latest/devguide/xray-concepts.html#xray-concepts-tracingheader)\.\) If Lambda receives a tracing header when your function is invoked, that header will be used to populate the \_X\_AMZN\_TRACE\_ID environment variable\. If a tracing header was not received, Lambda will generate one for you\. 
+ **AWS\_XRAY\_CONTEXT\_MISSING:** The X\-Ray SDK uses this variable to determine its behavior in the event that your function tries to record X\-Ray data, but a tracing header is not available\. Lambda sets this value to `LOG_ERROR `by default\. 
+ **AWS\_XRAY\_DAEMON\_ADDRESS:** This environment variable exposes the X\-Ray daemon's address in the following format: *IP\_ADDRESS***:***PORT*\. You can use the X\-Ray daemon's address to send trace data to the X\-Ray daemon directly, without using the X\-Ray SDK\. 

## Lambda traces in the AWS X\-Ray console: Examples<a name="viewing-lambda-xray-results"></a>

The following shows Lambda traces for two different Lambda functions\. Each trace showcases a trace structure for a different invocation type: asynchronous and synchronous\.
+ **Async ** – The example following shows an asynchronous Lambda request with one successful invocation and one downstream call to DynamoDB\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Trace_Console_DDB.png)

  The Lambda service segment encapsulates the response time, which is the time it took to return a response \(for example, 202\) to the client\. It includes subsegments for the time spent in the Lambda service queue \(dwell time\) and each invocation attempt\. \(Only one invocation attempt appears in the example preceding\.\) Each attempt subsegment in the service segment will have a corresponding user function segment\. In this example, the user function segment contains two subsegments: the initialization subsegment representing the function's initialization code that is run before the handler, and a downstream call subsegment representing a `ListTables` call to DynamoDB\.

  Status codes and error messages are displayed for each Invocation subsegment and for each downstream call\. 
+ **Synchronous** – The example following shows a synchronous request with one downstream call to Amazon S3\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Trace_Console_S3.png)

  The Lambda service segment captures the entire time the request spends in the Lambda service\. The service segment will have a corresponding User function segment\. In this example, the User function segment contains a subsegment representing the function's initialization code \(code run before the handler\), and a subsegment representing the `PutObject` call to Amazon S3\. 

**Note**  
If you want to trace HTTP calls, you need to use an HTTP client\. For more information, see [Tracing calls to downstream HTTP web services with the X\-Ray SDK for Java](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java-httpclients.html) or [Tracing calls to downstream HTTP web services with the X\-Ray SDK for Node\.js ](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-nodejs-httpclients.html)\.