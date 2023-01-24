# RemoveLayerVersionPermission<a name="API_RemoveLayerVersionPermission"></a>

Removes a statement from the permissions policy for a version of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\. For more information, see [AddLayerVersionPermission](API_AddLayerVersionPermission.md)\.

## Request Syntax<a name="API_RemoveLayerVersionPermission_RequestSyntax"></a>

```
DELETE /2018-10-31/layers/LayerName/versions/VersionNumber/policy/StatementId?RevisionId=RevisionId HTTP/1.1
```

## URI Request Parameters<a name="API_RemoveLayerVersionPermission_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [LayerName](#API_RemoveLayerVersionPermission_RequestSyntax) **   <a name="SSS-RemoveLayerVersionPermission-request-LayerName"></a>
The name or Amazon Resource Name \(ARN\) of the layer\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+)|[a-zA-Z0-9-_]+`   
Required: Yes

 ** [RevisionId](#API_RemoveLayerVersionPermission_RequestSyntax) **   <a name="SSS-RemoveLayerVersionPermission-request-RevisionId"></a>
Only update the policy if the revision ID matches the ID specified\. Use this option to avoid modifying a policy that has changed since you last read it\.

 ** [StatementId](#API_RemoveLayerVersionPermission_RequestSyntax) **   <a name="SSS-RemoveLayerVersionPermission-request-StatementId"></a>
The identifier that was specified when the statement was added\.  
Length Constraints: Minimum length of 1\. Maximum length of 100\.  
Pattern: `([a-zA-Z0-9-_]+)`   
Required: Yes

 ** [VersionNumber](#API_RemoveLayerVersionPermission_RequestSyntax) **   <a name="SSS-RemoveLayerVersionPermission-request-VersionNumber"></a>
The version number\.  
Required: Yes

## Request Body<a name="API_RemoveLayerVersionPermission_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_RemoveLayerVersionPermission_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_RemoveLayerVersionPermission_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_RemoveLayerVersionPermission_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** PreconditionFailedException **   
The RevisionId provided does not match the latest RevisionId for the Lambda function or alias\. Call the `GetFunction` or the `GetAlias` API to retrieve the latest RevisionId for your resource\.  
HTTP Status Code: 412

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_RemoveLayerVersionPermission_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/RemoveLayerVersionPermission) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/RemoveLayerVersionPermission) 