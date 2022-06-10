# AWS Lambda runtime API<a name="runtimes-api"></a>

AWS Lambda provides an HTTP API for [custom runtimes](runtimes-custom.md) to receive invocation events from Lambda and send response data back within the Lambda [execution environment](lambda-runtimes.md)\.

![\[Architecture diagram of the execution environment.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/logs-api-concept-diagram.png)

The OpenAPI specification for the runtime API version **2018\-06\-01** is available in [runtime\-api\.zip](samples/runtime-api.zip)

To create an API request URL, runtimes get the API endpoint from the `AWS_LAMBDA_RUNTIME_API` environment variable, add the API version, and add the desired resource path\.

**Example Request**  

```
curl "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/invocation/next"
```

**Topics**
+ [Next invocation](#runtimes-api-next)
+ [Invocation response](#runtimes-api-response)
+ [Initialization error](#runtimes-api-initerror)
+ [Invocation error](#runtimes-api-invokeerror)

## Next invocation<a name="runtimes-api-next"></a>

**Path** – `/runtime/invocation/next`

**Method** – **GET**

The runtime sends this message to Lambda to request an invocation event\. The response body contains the payload from the invocation, which is a JSON document that contains event data from the function trigger\. The response headers contain additional data about the invocation\.

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

Do not set a timeout on the `GET` request as the response may be delayed\. Between when Lambda bootstraps the runtime and when the runtime has an event to return, the runtime process may be frozen for several seconds\.

The request ID tracks the invocation within Lambda\. Use it to specify the invocation when you send the response\.

The tracing header contains the trace ID, parent ID, and sampling decision\. If the request is sampled, the request was sampled by Lambda or an upstream service\. The runtime should set the `_X_AMZN_TRACE_ID` with the value of the header\. The X\-Ray SDK reads this to get the IDs and determine whether to trace the request\.

## Invocation response<a name="runtimes-api-response"></a>

**Path** – `/runtime/invocation/AwsRequestId/response`

**Method** – **POST**

After the function has run to completion, the runtime sends an invocation response to Lambda\. For synchronous invocations, Lambda sends the response to the client\.

**Example success request**  

```
REQUEST_ID=156cb537-e2d4-11e8-9b34-d36013741fb9
curl -X POST  "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/invocation/$REQUEST_ID/response"  -d "SUCCESS"
```

## Initialization error<a name="runtimes-api-initerror"></a>

If the function returns an error or the runtime encounters an error during initialization, the runtime uses this method to report the error to Lambda\.

**Path** – `/runtime/init/error`

**Method** – **POST**

**Headers**

`Lambda-Runtime-Function-Error-Type` – Error type that the runtime encountered\. Required: no\. 

This header consists of a string value\. Lambda accepts any string, but we recommend a format of <category\.reason>\. For example:
+ Runtime\.NoSuchHandler
+ Runtime\.APIKeyNotFound
+ Runtime\.ConfigInvalid
+ Runtime\.UnknownReason

**Body parameters**

`ErrorRequest` – Information about the error\. Required: no\. 

This field is a JSON object with the following structure:

```
{
      errorMessage: string (text description of the error),
      errorType: string,
      stackTrace: array of strings
}
```

Note that Lambda accepts any value for `errorType`\.

The following example shows a Lambda function error message in which the function could not parse the event data provided in the invocation\.

**Example Function error**  

```
{
      "errorMessage" : "Error parsing event data.",
      "errorType" : "InvalidEventDataException",
      "stackTrace": [ ]
}
```

**Response body parameters**
+ `StatusResponse` – String\. Status information, sent with 202 response codes\. 
+ `ErrorResponse` – Additional error information, sent with the error response codes\. ErrorResponse contains an error type and an error message\.

**Response codes**
+ 202 – Accepted
+ 403 – Forbidden
+ 500 – Container error\. Non\-recoverable state\. Runtime should exit promptly\.

**Example initialization error request**  

```
ERROR="{\"errorMessage\" : \"Failed to load function.\", \"errorType\" : \"InvalidFunctionException\"}"
curl -X POST "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/init/error" -d "$ERROR" --header "Lambda-Runtime-Function-Error-Type: Unhandled"
```

## Invocation error<a name="runtimes-api-invokeerror"></a>

If the function returns an error or the runtime encounters an error, the runtime uses this method to report the error to Lambda\.

**Path** – `/runtime/invocation/AwsRequestId/error`

**Method** – **POST**

**Headers**

`Lambda-Runtime-Function-Error-Type` – Error type that the runtime encountered\. Required: no\. 

This header consists of a string value\. Lambda accepts any string, but we recommend a format of <category\.reason>\. For example:
+ Runtime\.NoSuchHandler
+ Runtime\.APIKeyNotFound
+ Runtime\.ConfigInvalid
+ Runtime\.UnknownReason

**Body parameters**

`ErrorRequest` – Information about the error\. Required: no\. 

This field is a JSON object with the following structure:

```
{
      errorMessage: string (text description of the error),
      errorType: string,
      stackTrace: array of strings
}
```

Note that Lambda accepts any value for `errorType`\.

The following example shows a Lambda function error message in which the function could not parse the event data provided in the invocation\.

**Example Function error**  

```
{
      "errorMessage" : "Error parsing event data.",
      "errorType" : "InvalidEventDataException",
      "stackTrace": [ ]
}
```

**Response body parameters**
+ `StatusResponse` – String\. Status information, sent with 202 response codes\. 
+ `ErrorResponse` – Additional error information, sent with the error response codes\. ErrorResponse contains an error type and an error message\.

**Response codes**
+ 202 – Accepted
+ 400 – Bad Request
+ 403 – Forbidden
+ 500 – Container error\. Non\-recoverable state\. Runtime should exit promptly\.

**Example error request**  

```
REQUEST_ID=156cb537-e2d4-11e8-9b34-d36013741fb9
ERROR="{\"errorMessage\" : \"Error parsing event data.\", \"errorType\" : \"InvalidEventDataException\"}"
curl -X POST "http://${AWS_LAMBDA_RUNTIME_API}/2018-06-01/runtime/invocation/$REQUEST_ID/error" -d "$ERROR" --header "Lambda-Runtime-Function-Error-Type: Unhandled"
```