# RemovePermission<a name="API_RemovePermission"></a>

Removes permissions from a function\. You can remove individual permissions from an resource policy associated with a Lambda function by providing a statement ID that you provided when you added the permission\. When you remove permissions, disable the event source mapping or trigger configuration first to avoid errors\.

Permissions apply to the Amazon Resource Name \(ARN\) used to invoke the function, which can be unqualified \(the unpublished version of the function\), or include a version or alias\. If a client uses a version or alias to invoke a function, use the `Qualifier` parameter to apply permissions to that ARN\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

You need permission for the `lambda:RemovePermission` action\.

## Request Syntax<a name="API_RemovePermission_RequestSyntax"></a>

```
DELETE /2015-03-31/functions/FunctionName/policy/StatementId?Qualifier=Qualifier&RevisionId=RevisionId HTTP/1.1
```

## URI Request Parameters<a name="API_RemovePermission_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-FunctionName"></a>
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-Qualifier"></a>
Specify a version or alias to remove permissions from a published version of the function\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

 ** [RevisionId](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-RevisionId"></a>
An optional value you can use to ensure you are updating the latest update of the function version or alias\. If the `RevisionID` you pass doesn't match the latest `RevisionId` of the function or alias, it will fail with an error message, advising you to retrieve the latest function version or alias `RevisionID` using either [GetFunction](API_GetFunction.md) or [GetAlias](API_GetAlias.md)\.

 ** [StatementId](#API_RemovePermission_RequestSyntax) **   <a name="SSS-RemovePermission-request-StatementId"></a>
Statement ID of the permission to remove\.  
Length Constraints: Minimum length of 1\. Maximum length of 100\.  
Pattern: `([a-zA-Z0-9-_.]+)` 

## Request Body<a name="API_RemovePermission_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_RemovePermission_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_RemovePermission_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_RemovePermission_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **PreconditionFailedException**   
The RevisionId provided does not match the latest RevisionId for the Lambda function or alias\. Call the `GetFunction` or the `GetAlias` API to retrieve the latest RevisionId for your resource\.  
HTTP Status Code: 412

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
Request throughput limit exceeded  
HTTP Status Code: 429

## See Also<a name="API_RemovePermission_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/RemovePermission) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/RemovePermission) 