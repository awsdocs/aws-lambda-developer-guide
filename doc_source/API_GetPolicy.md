# GetPolicy<a name="API_GetPolicy"></a>

Returns the resource policy associated with the specified Lambda function\.

 If you are using the versioning feature, you can get the resource policy associated with the specific Lambda function version or alias by specifying the version or alias name using the `Qualifier` parameter\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

You need permission for the `lambda:GetPolicy action.` 

## Request Syntax<a name="API_GetPolicy_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/policy?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetPolicy_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_GetPolicy_RequestSyntax) **   <a name="SSS-GetPolicy-request-FunctionName"></a>
Function name whose resource policy you want to retrieve\.  
 You can specify the function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. If you are using versioning, you can also provide a qualified function ARN \(ARN that is qualified with function version or alias name as suffix\)\. AWS Lambda also allows you to specify only the function name with the account ID qualifier \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_GetPolicy_RequestSyntax) **   <a name="SSS-GetPolicy-request-Qualifier"></a>
You can specify this optional query parameter to specify a function version or an alias name in which case this API will return all permissions associated with the specific qualified ARN\. If you don't provide this parameter, the API will return permissions that apply to the unqualified function ARN\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_GetPolicy_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetPolicy_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[Policy](#SSS-GetPolicy-response-Policy)": "string",
   "[RevisionId](#SSS-GetPolicy-response-RevisionId)": "string"
}
```

## Response Elements<a name="API_GetPolicy_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Policy](#API_GetPolicy_ResponseSyntax) **   <a name="SSS-GetPolicy-response-Policy"></a>
The resource policy associated with the specified function\. The response returns the same as a string using a backslash \("\\"\) as an escape character in the JSON\.  
Type: String

 ** [RevisionId](#API_GetPolicy_ResponseSyntax) **   <a name="SSS-GetPolicy-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

## Errors<a name="API_GetPolicy_Errors"></a>

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

## See Also<a name="API_GetPolicy_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetPolicy) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetPolicy) 