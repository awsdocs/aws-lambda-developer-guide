# DeleteLayerVersion<a name="API_DeleteLayerVersion"></a>

Deletes a version of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\. Deleted versions can no longer be viewed or added to functions\. To avoid breaking functions, a copy of the version remains in Lambda until no functions refer to it\.

## Request Syntax<a name="API_DeleteLayerVersion_RequestSyntax"></a>

```
DELETE /2018-10-31/layers/LayerName/versions/VersionNumber HTTP/1.1
```

## URI Request Parameters<a name="API_DeleteLayerVersion_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [LayerName](#API_DeleteLayerVersion_RequestSyntax) **   <a name="SSS-DeleteLayerVersion-request-LayerName"></a>
The name or Amazon Resource Name \(ARN\) of the layer\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+)|[a-zA-Z0-9-_]+`   
Required: Yes

 ** [VersionNumber](#API_DeleteLayerVersion_RequestSyntax) **   <a name="SSS-DeleteLayerVersion-request-VersionNumber"></a>
The version number\.  
Required: Yes

## Request Body<a name="API_DeleteLayerVersion_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_DeleteLayerVersion_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_DeleteLayerVersion_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_DeleteLayerVersion_Errors"></a>

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_DeleteLayerVersion_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/DeleteLayerVersion) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/DeleteLayerVersion) 