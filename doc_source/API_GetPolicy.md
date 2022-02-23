# GetPolicy<a name="API_GetPolicy"></a>

Returns the [resource\-based IAM policy](https://docs.aws.amazon.com/lambda/latest/dg/access-control-resource-based.html) for a function, version, or alias\.

## Request Syntax<a name="API_GetPolicy_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/policy?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetPolicy_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_GetPolicy_RequestSyntax) **   <a name="SSS-GetPolicy-request-FunctionName"></a>
The name of the Lambda function, version, or alias\.  

**Name formats**
+  **Function name** \- `my-function` \(name\-only\), `my-function:v1` \(with alias\)\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
You can append a version number or alias to any of the formats\. The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Qualifier](#API_GetPolicy_RequestSyntax) **   <a name="SSS-GetPolicy-request-Qualifier"></a>
Specify a version or alias to get the policy for that resource\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_GetPolicy_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetPolicy_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "Policy": "string",
   "RevisionId": "string"
}
```

## Response Elements<a name="API_GetPolicy_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Policy](#API_GetPolicy_ResponseSyntax) **   <a name="SSS-GetPolicy-response-Policy"></a>
The resource\-based policy\.  
Type: String

 ** [RevisionId](#API_GetPolicy_ResponseSyntax) **   <a name="SSS-GetPolicy-response-RevisionId"></a>
A unique identifier for the current revision of the policy\.  
Type: String

## Errors<a name="API_GetPolicy_Errors"></a>

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

## See Also<a name="API_GetPolicy_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetPolicy) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetPolicy) 