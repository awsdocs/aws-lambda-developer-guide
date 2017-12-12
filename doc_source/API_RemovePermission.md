# RemovePermission<a name="API_RemovePermission"></a>

You can remove individual permissions from an resource policy associated with a Lambda function by providing a statement ID that you provided when you added the permission\.

If you are using versioning, the permissions you remove are specific to the Lambda function version or alias you specify in the `AddPermission` request via the `Qualifier` parameter\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

Note that removal of a permission will cause an active event source to lose permission to the function\.

You need permission for the `lambda:RemovePermission` action\.

## Request Syntax<a name="API_RemovePermission_RequestSyntax"></a>

```
DELETE /2015-03-31/functions/FunctionName/policy/StatementId?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_RemovePermission_RequestParameters"></a>

The request requires the following URI parameters\.

 ** FunctionName **   
Lambda function whose resource policy you want to remove a permission from\.  
 You can specify a function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. AWS Lambda also allows you to specify a partial ARN \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** Qualifier **   
You can specify this optional parameter to remove permission associated with a specific function version or function alias\. If you don't specify this parameter, the API removes permission associated with the unqualified function ARN\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

 ** StatementId **   
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

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
   
HTTP Status Code: 429

## See Also<a name="API_RemovePermission_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/RemovePermission) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/RemovePermission) 