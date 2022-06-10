# InvokeAsync<a name="API_InvokeAsync"></a>

 *This action has been deprecated\.* 

**Important**  
For asynchronous function invocation, use [Invoke](API_Invoke.md)\.

Invokes a function asynchronously\.

## Request Syntax<a name="API_InvokeAsync_RequestSyntax"></a>

```
POST /2014-11-13/functions/FunctionName/invoke-async/ HTTP/1.1

InvokeArgs
```

## URI Request Parameters<a name="API_InvokeAsync_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_InvokeAsync_RequestSyntax) **   <a name="SSS-InvokeAsync-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

## Request Body<a name="API_InvokeAsync_RequestBody"></a>

The request accepts the following binary data\.

 ** [InvokeArgs](#API_InvokeAsync_RequestSyntax) **   <a name="SSS-InvokeAsync-request-InvokeArgs"></a>
The JSON that you want to provide to your Lambda function as input\.  
Required: Yes

## Response Syntax<a name="API_InvokeAsync_ResponseSyntax"></a>

```
HTTP/1.1 Status
```

## Response Elements<a name="API_InvokeAsync_ResponseElements"></a>

If the action is successful, the service sends back the following HTTP response\.

 ** [Status](#API_InvokeAsync_ResponseSyntax) **   <a name="SSS-InvokeAsync-response-Status"></a>
The status code\.

## Errors<a name="API_InvokeAsync_Errors"></a>

 ** InvalidRequestContentException **   
The request body could not be parsed as JSON\.  
HTTP Status Code: 400

 ** InvalidRuntimeException **   
The runtime or runtime version specified is not supported\.  
HTTP Status Code: 502

 ** ResourceConflictException **   
The resource already exists, or another operation is in progress\.  
HTTP Status Code: 409

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

## See Also<a name="API_InvokeAsync_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/InvokeAsync) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/InvokeAsync) 