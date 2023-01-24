# ListTags<a name="API_ListTags"></a>

Returns a function's [tags](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html)\. You can also view tags with [GetFunction](API_GetFunction.md)\.

## Request Syntax<a name="API_ListTags_RequestSyntax"></a>

```
GET /2017-03-31/tags/ARN HTTP/1.1
```

## URI Request Parameters<a name="API_ListTags_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [ARN](#API_ListTags_RequestSyntax) **   <a name="SSS-ListTags-request-Resource"></a>
The function's Amazon Resource Name \(ARN\)\. Note: Lambda does not support adding tags to aliases or versions\.  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

## Request Body<a name="API_ListTags_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListTags_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "Tags": { 
      "string" : "string" 
   }
}
```

## Response Elements<a name="API_ListTags_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Tags](#API_ListTags_ResponseSyntax) **   <a name="SSS-ListTags-response-Tags"></a>
The function's tags\.  
Type: String to string map

## Errors<a name="API_ListTags_Errors"></a>

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

## See Also<a name="API_ListTags_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListTags) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListTags) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListTags) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListTags) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListTags) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListTags) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListTags) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListTags) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListTags) 