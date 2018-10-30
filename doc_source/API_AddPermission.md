# AddPermission<a name="API_AddPermission"></a>

Adds a permission to the resource policy associated with the specified AWS Lambda function\. You use resource policies to grant permissions to event sources that use the *push* model\. In a *push* model, event sources \(such as Amazon S3 and custom applications\) invoke your Lambda function\. Each permission you add to the resource policy allows an event source permission to invoke the Lambda function\. 

Permissions apply to the Amazon Resource Name \(ARN\) used to invoke the function, which can be unqualified \(the unpublished version of the function\), or include a version or alias\. If a client uses a version or alias to invoke a function, use the `Qualifier` parameter to apply permissions to that ARN\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:AddPermission` action\.

## Request Syntax<a name="API_AddPermission_RequestSyntax"></a>

```
POST /2015-03-31/functions/FunctionName/policy?Qualifier=Qualifier HTTP/1.1
Content-type: application/json

{
   "[Action](#SSS-AddPermission-request-Action)": "string",
   "[EventSourceToken](#SSS-AddPermission-request-EventSourceToken)": "string",
   "[Principal](#SSS-AddPermission-request-Principal)": "string",
   "[RevisionId](#SSS-AddPermission-request-RevisionId)": "string",
   "[SourceAccount](#SSS-AddPermission-request-SourceAccount)": "string",
   "[SourceArn](#SSS-AddPermission-request-SourceArn)": "string",
   "[StatementId](#SSS-AddPermission-request-StatementId)": "string"
}
```

## URI Request Parameters<a name="API_AddPermission_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-FunctionName"></a>
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-Qualifier"></a>
Specify a version or alias to add permissions to a published version of the function\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_AddPermission_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Action](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-Action"></a>
The AWS Lambda action you want to allow in this statement\. Each Lambda action is a string starting with `lambda:` followed by the API name \(see [Actions](https://docs.aws.amazon.com/lambda/latest/dg/API_Operations.html)\) \. For example, `lambda:CreateFunction`\. You can use wildcard \(`lambda:*`\) to grant permission for all AWS Lambda actions\.   
Type: String  
Pattern: `(lambda:[*]|lambda:[a-zA-Z]+|[*])`   
Required: Yes

 ** [EventSourceToken](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-EventSourceToken"></a>
A unique token that must be supplied by the principal invoking the function\. This is currently only used for Alexa Smart Home functions\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Pattern: `[a-zA-Z0-9._\-]+`   
Required: No

 ** [Principal](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-Principal"></a>
The principal who is getting this permission\. The principal can be an AWS service \(e\.g\. `s3.amazonaws.com` or `sns.amazonaws.com`\) for service triggers, or an account ID for cross\-account access\. If you specify a service as a principal, use the `SourceArn` parameter to limit who can invoke the function through that service\.  
Type: String  
Pattern: `.*`   
Required: Yes

 ** [RevisionId](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-RevisionId"></a>
An optional value you can use to ensure you are updating the latest update of the function version or alias\. If the `RevisionID` you pass doesn't match the latest `RevisionId` of the function or alias, it will fail with an error message, advising you to retrieve the latest function version or alias `RevisionID` using either [GetFunction](API_GetFunction.md) or [GetAlias](API_GetAlias.md)   
Type: String  
Required: No

 ** [SourceAccount](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-SourceAccount"></a>
This parameter is used for S3 and SES\. The AWS account ID \(without a hyphen\) of the source owner\. For example, if the `SourceArn` identifies a bucket, then this is the bucket owner's account ID\. You can use this additional condition to ensure the bucket you specify is owned by a specific account \(it is possible the bucket owner deleted the bucket and some other AWS account created the bucket\)\. You can also use this condition to specify all sources \(that is, you don't specify the `SourceArn`\) owned by a specific account\.   
Type: String  
Pattern: `\d{12}`   
Required: No

 ** [SourceArn](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-SourceArn"></a>
The Amazon Resource Name of the invoker\.   
If you add a permission to a service principal without providing the source ARN, any AWS account that creates a mapping to your function ARN can invoke your Lambda function\.
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 ** [StatementId](#API_AddPermission_RequestSyntax) **   <a name="SSS-AddPermission-request-StatementId"></a>
A unique statement identifier\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 100\.  
Pattern: `([a-zA-Z0-9-_]+)`   
Required: Yes

## Response Syntax<a name="API_AddPermission_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "[Statement](#SSS-AddPermission-response-Statement)": "string"
}
```

## Response Elements<a name="API_AddPermission_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [Statement](#API_AddPermission_ResponseSyntax) **   <a name="SSS-AddPermission-response-Statement"></a>
The permission statement you specified in the request\. The response returns the same as a string using a backslash \("\\"\) as an escape character in the JSON\.  
Type: String

## Errors<a name="API_AddPermission_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **PolicyLengthExceededException**   
Lambda function access policy is limited to 20 KB\.  
HTTP Status Code: 400

 **PreconditionFailedException**   
The RevisionId provided does not match the latest RevisionId for the Lambda function or alias\. Call the `GetFunction` or the `GetAlias` API to retrieve the latest RevisionId for your resource\.  
HTTP Status Code: 412

 **ResourceConflictException**   
The resource already exists\.  
HTTP Status Code: 409

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
Request throughput limit exceeded  
HTTP Status Code: 429

## See Also<a name="API_AddPermission_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/AddPermission) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/AddPermission) 