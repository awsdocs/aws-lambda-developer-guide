# Using AWS X\-Ray<a name="lambda-x-ray"></a>

A typical Lambda\-based application consists of one or more functions triggered by events such as object uploads to Amazon S3, Amazon SNS notifications, and API actions\. Once triggered, those functions usually call downstream resources such as DynamoDB tables or Amazon S3 buckets, or make other API calls\. AWS Lambda leverages Amazon CloudWatch to automatically emit metrics and logs for all invocations of your function\. However, this mechanism might not be convenient for tracing the event source that invoked your Lambda function, or for tracing downstream calls that your function made\. For a complete overview of how tracing works, see [AWS X\-Ray](https://docs.aws.amazon.com/xray/latest/devguide/)\. 

## Using Environment Variables to Communicate with AWS X\-Ray<a name="lambda-x-ray-env-variables"></a>

AWS Lambda uses environment variables to facilitate communication with the X\-Ray daemon and configure the X\-Ray SDK\.
+ **\_X\_AMZN\_TRACE\_ID:** Contains the tracing header, which includes the sampling decision, trace ID, and parent segment ID\. \(To learn more about these properties, see [Tracing Header](https://docs.aws.amazon.com/xray/latest/devguide/xray-concepts.html#xray-concepts-tracingheader)\.\) If Lambda receives a tracing header when your function is invoked, that header will be used to populate the \_X\_AMZN\_TRACE\_ID environment variable\. If a tracing header was not received, Lambda will generate one for you\. 
+ **AWS\_XRAY\_CONTEXT\_MISSING:** The X\-Ray SDK uses this variable to determine its behavior in the event that your function tries to record X\-Ray data, but a tracing header is not available\. Lambda sets this value to `LOG_ERROR `by default\. 
+ **AWS\_XRAY\_DAEMON\_ADDRESS:** This environment variable exposes the X\-Ray daemon's address in the following format: *IP\_ADDRESS***:***PORT*\. You can use the X\-Ray daemon's address to send trace data to the X\-Ray daemon directly, without using the X\-Ray SDK\. 

## Lambda Traces in the AWS X\-Ray Console: Examples<a name="viewing-lambda-xray-results"></a>

The following shows Lambda traces for two different Lambda functions\. Each trace showcases a trace structure for a different invocation type: asynchronous and synchronous\.
+ **Async ** – The example following shows an asynchronous Lambda request with one successful invocation and one downstream call to DynamoDB\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Trace_Console_DDB.png)

  The Lambda service segment encapsulates the response time, which is the time it took to return a response \(for example, 202\) to the client\. It includes subsegments for the time spent in the Lambda service queue \(dwell time\) and each invocation attempt\. \(Only one invocation attempt appears in the example preceding\.\) Each attempt subsegment in the service segment will have a corresponding user function segment\. In this example, the user function segment contains two subsegments: the initialization subsegment representing the function's initialization code that is run before the handler, and a downstream call subsegment representing a `ListTables` call to DynamoDB\.

  Status codes and error messages are displayed for each Invocation subsegment and for each downstream call\. 
+ **Synchronous** – The example following shows a synchronous request with one downstream call to Amazon S3\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Trace_Console_S3.png)

  The Lambda service segment captures the entire time the request spends in the Lambda service\. The service segment will have a corresponding User function segment\. In this example, the User function segment contains a subsegment representing the function's initialization code \(code run before the handler\), and a subsegment representing the `PutObject` call to Amazon S3\. 

**Note**  
If you want to trace HTTP calls, you need to use an HTTP client\. For more information, see [Tracing Calls to Downstream HTTP Web Services with the X\-Ray SDK for Java](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java-httpclients.html) or [Tracing Calls to Downstream HTTP Web Services with the X\-Ray SDK for Node\.js ](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-nodejs-httpclients.html)\.