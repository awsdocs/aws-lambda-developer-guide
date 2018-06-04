# CreateAlias<a name="API_CreateAlias"></a>

Creates an alias that points to the specified Lambda function version\. For more information, see [Introduction to AWS Lambda Aliases](https://docs.aws.amazon.com/lambda/latest/dg/aliases-intro.html)\.

Alias names are unique for a given function\. This requires permission for the lambda:CreateAlias action\.

## Request Syntax<a name="API_CreateAlias_RequestSyntax"></a>

```
POST /2015-03-31/functions/FunctionName/aliases HTTP/1.1
Content-type: application/json

{
   "[Description](#SSS-CreateAlias-request-Description)": "string",
   "[FunctionVersion](#SSS-CreateAlias-request-FunctionVersion)": "string",
   "[Name](#SSS-CreateAlias-request-Name)": "string",
   "[RoutingConfig](#SSS-CreateAlias-request-RoutingConfig)": { 
      "[AdditionalVersionWeights](API_AliasRoutingConfiguration.md#SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights)": { 
         "string" : number 
      }
   }
}
```

## URI Request Parameters<a name="API_CreateAlias_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-FunctionName"></a>
Name of the Lambda function for which you want to create an alias\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_CreateAlias_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Description](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-Description"></a>
Description of the alias\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [FunctionVersion](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-FunctionVersion"></a>
Lambda function version for which you are creating the alias\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: Yes

 ** [Name](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-Name"></a>
Name for the alias you are creating\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)`   
Required: Yes

 ** [RoutingConfig](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-RoutingConfig"></a>
Specifies an additional version your alias can point to, allowing you to dictate what percentage of traffic will invoke each version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object  
Required: No

## Response Syntax<a name="API_CreateAlias_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "[AliasArn](#SSS-CreateAlias-response-AliasArn)": "string",
   "[Description](#SSS-CreateAlias-response-Description)": "string",
   "[FunctionVersion](#SSS-CreateAlias-response-FunctionVersion)": "string",
   "[Name](#SSS-CreateAlias-response-Name)": "string",
   "[RevisionId](#SSS-CreateAlias-response-RevisionId)": "string",
   "[RoutingConfig](#SSS-CreateAlias-response-RoutingConfig)": { 
      "[AdditionalVersionWeights](API_AliasRoutingConfiguration.md#SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights)": { 
         "string" : number 
      }
   }
}
```

## Response Elements<a name="API_CreateAlias_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [AliasArn](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-AliasArn"></a>
Lambda function ARN that is qualified using the alias name as the suffix\. For example, if you create an alias called `BETA` that points to a helloworld function version, the ARN is `arn:aws:lambda:aws-regions:acct-id:function:helloworld:BETA`\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Description](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-Description"></a>
Alias description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [FunctionVersion](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-FunctionVersion"></a>
Function version to which the alias points\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [Name](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-Name"></a>
Alias name\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

 ** [RevisionId](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

 ** [RoutingConfig](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-RoutingConfig"></a>
Specifies an additional function versions the alias points to, allowing you to dictate what percentage of traffic will invoke each version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object

## Errors<a name="API_CreateAlias_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

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
   
HTTP Status Code: 429

## See Also<a name="API_CreateAlias_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/CreateAlias) 