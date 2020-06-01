# AWS Lambda runtime interface<a name="runtimes-api"></a>

AWS Lambda provides an HTTP API for [custom runtimes](runtimes-custom.md) to receive invocation events from Lambda and send response data back within the Lambda [execution environment](lambda-runtimes.md)\.

The OpenAPI specification for the runtime API version **2018\-06\-01** is available here: [runtime\-api\.zip](samples/runtime-api.zip)

Runtimes get an endpoint from the `AWS_LAMBDA_RUNTIME_API` environment variable, add the API version, and use the following resource paths to interact with the API\.

**Example Request**  

```
curl "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/invocation/next"
```

**Topics**
+ [Next invocation](#runtimes-api-next)
+ [Invocation response](#runtimes-api-response)
+ [Invocation error](#runtimes-api-invokeerror)
+ [Initialization error](#runtimes-api-initerror)

## Next invocation<a name="runtimes-api-next"></a>

**Path** – `/runtime/invocation/next`

**Method** – **GET**

Retrieves an invocation event\. The response body contains the payload from the invocation, which is a JSON document that contains event data from the function trigger\. The response headers contain additional data about the invocation\.

**Response headers**
+ `Lambda-Runtime-Aws-Request-Id` – The request ID, which identifies the request that triggered the function invocation\.

  For example, `8476a536-e9f4-11e8-9739-2dfe598c3fcd`\.
+ `Lambda-Runtime-Deadline-Ms` – The date that the function times out in Unix time milliseconds\. 

  For example, `1542409706888`\.
+ `Lambda-Runtime-Invoked-Function-Arn` – The ARN of the Lambda function, version, or alias that's specified in the invocation\. 

  For example, `arn:aws:lambda:us-east-2:123456789012:function:custom-runtime`\.
+ `Lambda-Runtime-Trace-Id` – The [AWS X\-Ray tracing header](https://docs.aws.amazon.com/xray/latest/devguide/xray-concepts.html#xray-concepts-tracingheader)\. 

  For example, `Root=1-5bef4de7-ad49b0e87f6ef6c87fc2e700;Parent=9a9197af755a6419;Sampled=1`\.
+ `Lambda-Runtime-Client-Context` – For invocations from the AWS Mobile SDK, data about the client application and device\.
+ `Lambda-Runtime-Cognito-Identity` – For invocations from the AWS Mobile SDK, data about the Amazon Cognito identity provider\.

Call `/runtime/invocation/next` to get the invocation event, and pass it to the function handler for processing\. Do not set a timeout on the `GET` call\. Between when Lambda bootstraps the runtime and when the runtime has an event to return, the runtime process may be frozen for several seconds\.

The request ID tracks the invocation within Lambda\. Use it to specify the invocation when you send the response\.

The tracing header contains the trace ID, parent ID, and sampling decision\. If the request is sampled, the request was sampled by Lambda or an upstream service\. The runtime should set the `_X_AMZN_TRACE_ID` with the value of the header\. The X\-Ray SDK reads this to get the IDs and determine whether to trace the request\.

## Invocation response<a name="runtimes-api-response"></a>

**Path** – `/runtime/invocation/AwsRequestId/response`

**Method** – **POST**

Sends an invocation response to Lambda\. After the runtime invokes the function handler, it posts the response from the function to the invocation response path\. For synchronous invocations, Lambda then sends the response back to the client\.

**Example success request**  

```
REQUEST_ID=156cb537-e2d4-11e8-9b34-d36013741fb9
curl -X POST  "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/invocation/$REQUEST_ID/response"  -d "SUCCESS"
```

## Invocation error<a name="runtimes-api-invokeerror"></a>

**Path** – `/runtime/invocation/AwsRequestId/error`

**Method** – **POST**

If the function returns an error, the runtime formats the error into a JSON document, and posts it to the invocation error path\.

**Example request body**  

```
{
    "errorMessage" : "Error parsing event data.",
    "errorType" : "InvalidEventDataException"
}
```

**Example error request**  

```
REQUEST_ID=156cb537-e2d4-11e8-9b34-d36013741fb9
ERROR="{\"errorMessage\" : \"Error parsing event data.\", \"errorType\" : \"InvalidEventDataException\"}"
curl -X POST "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/invocation/$REQUEST_ID/error" -d "$ERROR" --header "Lambda-Runtime-Function-Error-Type: Unhandled"
```

## Initialization error<a name="runtimes-api-initerror"></a>

**Path** – `/runtime/init/error`

**Method** – **POST**

If the runtime encounters an error during initialization, it posts an error message to the initialization error path\.

**Example initialization error request**  

```
ERROR="{\"errorMessage\" : \"Failed to load function.\", \"errorType\" : \"InvalidFunctionException\"}"
curl -X POST "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/init/error" -d "$ERROR" --header "Lambda-Runtime-Function-Error-Type: Unhandled"
```