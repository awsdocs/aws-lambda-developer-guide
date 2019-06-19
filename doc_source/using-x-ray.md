# Tracing Lambda\-Based Applications with AWS X\-Ray<a name="using-x-ray"></a>

AWS X\-Ray is an AWS service that allows you to detect, analyze, and optimize performance issues with your AWS Lambda applications\. X\-Ray collects metadata from the Lambda service and any upstream or downstream services that make up your application\. X\-Ray uses this metadata to generate a detailed service graph that illustrates performance bottlenecks, latency spikes, and other issues that impact the performance of your Lambda application\. 

After using the [Lambda on the AWS X\-Ray Service Map ](#lambda-service-map)to identify a problematic resource or component, you can zoom in and view a visual representation of the request\. This visual representation covers the time from when an event source triggers a Lambda function until the function execution has completed\. X\-Ray provides you with a breakdown of your function's operations, such as information regarding downstream calls your Lambda function made to other services\. In addition, X\-Ray integration with Lambda provides you with visibility into the AWS Lambda service overhead\. It does so by displaying specifics such as your request's dwell time and number of invocations\. 

**Note**  
Only services that currently integrate with X\-Ray show as standalone traces, outside of your Lambda trace\. For a list of services that currently support X\-Ray, see [Integrating AWS X\-Ray with Other AWS Services](https://docs.aws.amazon.com/xray/latest/devguide/xray-services.html)\.

## Lambda on the AWS X\-Ray Service Map<a name="lambda-service-map"></a>

X\-Ray displays three types of nodes on the service map for requests served by Lambda:
+ **Lambda service \(AWS::Lambda\)** – This type of node represents the time the request spent in the Lambda service\. Timing starts when Lambda first receives the request and ends when the request leaves the Lambda service\.
+ **Lambda function \(AWS::Lambda::Function\)** – This type of node represents the Lambda function's execution time\.
+ **Downstream service calls** – In this type, each downstream service call from within the Lambda function is represented by a separate node\. 

In the diagram following, the nodes represent \(from left to right\): The Lambda service, the user function, and a downstream call to Amazon S3:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Tracing-S3.png)

For more information, see [Viewing the Service Map](https://docs.aws.amazon.com/xray/latest/devguide/xray-console.html)\.

## Lambda as an AWS X\-Ray Trace<a name="lambda-request"></a>

From the service map, you can zoom in to see a trace view of your Lambda function\. The trace will display in\-depth information regarding your function invocations, represented as segments and subsegments:
+ **Lambda service segment** – This segment represents different information depending on the event source used to invoke the function:
  + **Synchronous and stream event sources** – The service segment measures the time from when the Lambda service receives the request/event and ends when the request leaves the Lambda service \(after the final invocation for the request is completed\)\.
  + **Asynchronous** – The service segment represents the response time, that is, the time it took the Lambda service to return a 202 response to the client\.

  The Lambda service segment can include two types of subsegments:
  + **Dwell time \(asynchronous invocations only\)** – Represents the time the function spends in the Lambda service before being invoked\. This subsegment starts when the Lambda service receives the request/event and ends when the Lambda function is invoked for the first time\.
  + **Attempt** – Represents a single invocation attempt, including any overhead introduced by the Lambda service\. Examples of overhead are time spent initializing the function's code and function execution time\.
+ **Lambda function segment** – Represents execution time for the function for a given invocation attempt\. It starts when the function handler starts executing and ends when the function terminates\. This segment can include three types of subsegments:
  + **Initialization** – The time spent running the `initialization` code of the function, defined as the code outside the Lambda function handler or static initializers\.
  + **Downstream calls** – Calls made to other AWS services from the Lambda function's code\.
  + **Custom subsegments** – Custom subsegments or user annotations that you can add to the Lambda function segment by using the X\-Ray SDK\. 

**Note**  
For each traced invocation, Lambda emits the Lambda service segment and all of its subsegments\. These segments are emitted regardless of the runtime and require you to use the X\-Ray SDK for AWS API calls\. 