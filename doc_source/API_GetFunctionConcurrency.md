# GetFunctionConcurrency<a name="API_GetFunctionConcurrency"></a>

Returns details about the reserved concurrency configuration for a function\. To set a concurrency limit for a function, use [PutFunctionConcurrency](API_PutFunctionConcurrency.md)\.

## Request Syntax<a name="API_GetFunctionConcurrency_RequestSyntax"></a>

```
GET /2019-09-30/functions/FunctionName/concurrency HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunctionConcurrency_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_GetFunctionConcurrency_RequestSyntax) **   <a name="SSS-GetFunctionConcurrency-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

## Request Body<a name="API_GetFunctionConcurrency_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetFunctionConcurrency_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "ReservedConcurrentExecutions": number
}
```

## Response Elements<a name="API_GetFunctionConcurrency_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [ReservedConcurrentExecutions](#API_GetFunctionConcurrency_ResponseSyntax) **   <a name="SSS-GetFunctionConcurrency-response-ReservedConcurrentExecutions"></a>
The number of simultaneous executions that are reserved for the function\.  
Type: Integer  
Valid Range: Minimum value of 0\.

## Errors<a name="API_GetFunctionConcurrency_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetFunctionConcurrency_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetFunctionConcurrency) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetFunctionConcurrency) 