# ListFunctionsByCodeSigningConfig<a name="API_ListFunctionsByCodeSigningConfig"></a>

List the functions that use the specified code signing configuration\. You can use this method prior to deleting a code signing configuration, to verify that no functions are using it\.

## Request Syntax<a name="API_ListFunctionsByCodeSigningConfig_RequestSyntax"></a>

```
GET /2020-04-22/code-signing-configs/CodeSigningConfigArn/functions?Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListFunctionsByCodeSigningConfig_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [CodeSigningConfigArn](#API_ListFunctionsByCodeSigningConfig_RequestSyntax) **   <a name="SSS-ListFunctionsByCodeSigningConfig-request-CodeSigningConfigArn"></a>
The The Amazon Resource Name \(ARN\) of the code signing configuration\.  
Length Constraints: Maximum length of 200\.  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}((-gov)|(-iso(b?)))?-[a-z]+-\d{1}:\d{12}:code-signing-config:csc-[a-z0-9]{17}`   
Required: Yes

 ** [Marker](#API_ListFunctionsByCodeSigningConfig_RequestSyntax) **   <a name="SSS-ListFunctionsByCodeSigningConfig-request-Marker"></a>
Specify the pagination token that's returned by a previous request to retrieve the next page of results\.

 ** [MaxItems](#API_ListFunctionsByCodeSigningConfig_RequestSyntax) **   <a name="SSS-ListFunctionsByCodeSigningConfig-request-MaxItems"></a>
Maximum number of items to return\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListFunctionsByCodeSigningConfig_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListFunctionsByCodeSigningConfig_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "FunctionArns": [ "string" ],
   "NextMarker": "string"
}
```

## Response Elements<a name="API_ListFunctionsByCodeSigningConfig_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [FunctionArns](#API_ListFunctionsByCodeSigningConfig_ResponseSyntax) **   <a name="SSS-ListFunctionsByCodeSigningConfig-response-FunctionArns"></a>
The function ARNs\.   
Type: Array of strings  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [NextMarker](#API_ListFunctionsByCodeSigningConfig_ResponseSyntax) **   <a name="SSS-ListFunctionsByCodeSigningConfig-response-NextMarker"></a>
The pagination token that's included if more results are available\.  
Type: String

## Errors<a name="API_ListFunctionsByCodeSigningConfig_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

## See Also<a name="API_ListFunctionsByCodeSigningConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListFunctionsByCodeSigningConfig) 