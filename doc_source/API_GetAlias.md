# GetAlias<a name="API_GetAlias"></a>

Returns details about a Lambda function [alias](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\.

## Request Syntax<a name="API_GetAlias_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/aliases/Name HTTP/1.1
```

## URI Request Parameters<a name="API_GetAlias_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_GetAlias_RequestSyntax) **   <a name="SSS-GetAlias-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Name](#API_GetAlias_RequestSyntax) **   <a name="SSS-GetAlias-request-Name"></a>
The name of the alias\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

## Request Body<a name="API_GetAlias_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetAlias_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[AliasArn](#SSS-GetAlias-response-AliasArn)": "string",
   "[Description](#SSS-GetAlias-response-Description)": "string",
   "[FunctionVersion](#SSS-GetAlias-response-FunctionVersion)": "string",
   "[Name](#SSS-GetAlias-response-Name)": "string",
   "[RevisionId](#SSS-GetAlias-response-RevisionId)": "string",
   "[RoutingConfig](#SSS-GetAlias-response-RoutingConfig)": { 
      "[AdditionalVersionWeights](API_AliasRoutingConfiguration.md#SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights)": { 
         "string" : number 
      }
   }
}
```

## Response Elements<a name="API_GetAlias_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [AliasArn](#API_GetAlias_ResponseSyntax) **   <a name="SSS-GetAlias-response-AliasArn"></a>
The Amazon Resource Name \(ARN\) of the alias\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Description](#API_GetAlias_ResponseSyntax) **   <a name="SSS-GetAlias-response-Description"></a>
A description of the alias\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [FunctionVersion](#API_GetAlias_ResponseSyntax) **   <a name="SSS-GetAlias-response-FunctionVersion"></a>
The function version that the alias invokes\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [Name](#API_GetAlias_ResponseSyntax) **   <a name="SSS-GetAlias-response-Name"></a>
The name of the alias\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

 ** [RevisionId](#API_GetAlias_ResponseSyntax) **   <a name="SSS-GetAlias-response-RevisionId"></a>
A unique identifier that changes when you update the alias\.  
Type: String

 ** [RoutingConfig](#API_GetAlias_ResponseSyntax) **   <a name="SSS-GetAlias-response-RoutingConfig"></a>
The [routing configuration](https://docs.aws.amazon.com/lambda/latest/dg/lambda-traffic-shifting-using-aliases.html) of the alias\.  
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
Request throughput limit exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetAlias_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetAlias) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetAlias) 