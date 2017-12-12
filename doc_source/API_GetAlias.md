# GetAlias<a name="API_GetAlias"></a>

Returns the specified alias information such as the alias ARN, description, and function version it is pointing to\. For more information, see [Introduction to AWS Lambda Aliases](http://docs.aws.amazon.com/lambda/latest/dg/aliases-intro.html)\.

This requires permission for the `lambda:GetAlias` action\.

## Request Syntax<a name="API_GetAlias_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/aliases/Name HTTP/1.1
```

## URI Request Parameters<a name="API_GetAlias_RequestParameters"></a>

The request requires the following URI parameters\.

 ** FunctionName **   
Function name for which the alias is created\. An alias is a subresource that exists only in the context of an existing Lambda function so you must specify the function name\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** Name **   
Name of the alias for which you want to retrieve information\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

## Request Body<a name="API_GetAlias_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetAlias_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "AliasArn": "string",
   "Description": "string",
   "FunctionVersion": "string",
   "Name": "string",
   "RoutingConfig": { 
      "AdditionalVersionWeights": { 
         "string" : number 
      }
   }
}
```

## Response Elements<a name="API_GetAlias_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** AliasArn **   
Lambda function ARN that is qualified using the alias name as the suffix\. For example, if you create an alias called `BETA` that points to a helloworld function version, the ARN is `arn:aws:lambda:aws-regions:acct-id:function:helloworld:BETA`\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** Description **   
Alias description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** FunctionVersion **   
Function version to which the alias points\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** Name **   
Alias name\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

 ** RoutingConfig **   
Specifies an additional function versions the alias points to, allowing you to dictate what percentage of traffic will invoke each version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object

## Errors<a name="API_GetAlias_Errors"></a>

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

## See Also<a name="API_GetAlias_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetAlias) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetAlias) 