# RemovePermission<a name="API_RemovePermission"></a>

Revokes function\-use permission from an AWS service or another account\. You can get the ID of the statement from the output of [GetPolicy](API_GetPolicy.md)\.

## Request Syntax<a name="API_RemovePermission_RequestSyntax"></a>

```
DELETE /2015-03-31/functions/FunctionName/policy/StatementId?Qualifier=Qualifier&RevisionId=RevisionId HTTP/1.1
```

## URI Request Parameters<a name="API_RemovePermission_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-FunctionName"></a>
The name of the Lambda function, version, or alias\.  

**Name formats**
+  **Function name** \- `my-function` \(name\-only\), `my-function:v1` \(with alias\)\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
You can append a version number or alias to any of the formats\. The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Qualifier](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-Qualifier"></a>
Specify a version or alias to remove permissions from a published version of the function\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

 ** [RevisionId](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-RevisionId"></a>
Only update the policy if the revision ID matches the ID that's specified\. Use this option to avoid modifying a policy that has changed since you last read it\.

 ** [StatementId](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-StatementId"></a>
Statement ID of the permission to remove\.  
Length Constraints: Minimum length of 1\. Maximum length of 100\.  
Pattern: `([a-zA-Z0-9-_.]+)`   
Required: Yes

## Request Body<a name="API_RemovePermission_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_RemovePermission_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_RemovePermission_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_RemovePermission_Errors"></a>

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

## See Also<a name="API_RemovePermission_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/RemovePermission) 