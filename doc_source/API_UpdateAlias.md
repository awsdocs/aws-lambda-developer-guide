# UpdateAlias<a name="API_UpdateAlias"></a>

Using this API you can update the function version to which the alias points and the alias description\. For more information, see [Introduction to AWS Lambda Aliases](https://docs.aws.amazon.com/lambda/latest/dg/aliases-intro.html)\.

This requires permission for the lambda:UpdateAlias action\.

## Request Syntax<a name="API_UpdateAlias_RequestSyntax"></a>

```
PUT /2015-03-31/functions/FunctionName/aliases/Name HTTP/1.1
Content-type: application/json

{
   "[Description](#SSS-UpdateAlias-request-Description)": "string",
   "[FunctionVersion](#SSS-UpdateAlias-request-FunctionVersion)": "string",
   "[RevisionId](#SSS-UpdateAlias-request-RevisionId)": "string",
   "[RoutingConfig](#SSS-UpdateAlias-request-RoutingConfig)": { 
      "[AdditionalVersionWeights](API_AliasRoutingConfiguration.md#SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights)": { 
         "string" : number 
      }
   }
}
```

## URI Request Parameters<a name="API_UpdateAlias_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_UpdateAlias_RequestSyntax) **   <a name="SSS-UpdateAlias-request-FunctionName"></a>
The function name for which the alias is created\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Name](#API_UpdateAlias_RequestSyntax) **   <a name="SSS-UpdateAlias-request-Name"></a>
The alias name\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

## Request Body<a name="API_UpdateAlias_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Description](#API_UpdateAlias_RequestSyntax) **   <a name="SSS-UpdateAlias-request-Description"></a>
You can change the description of the alias using this parameter\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [FunctionVersion](#API_UpdateAlias_RequestSyntax) **   <a name="SSS-UpdateAlias-request-FunctionVersion"></a>
Using this parameter you can change the Lambda function version to which the alias points\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: No

 ** [RevisionId](#API_UpdateAlias_RequestSyntax) **   <a name="SSS-UpdateAlias-request-RevisionId"></a>
An optional value you can use to ensure you are updating the latest update of the function version or alias\. If the `RevisionID` you pass doesn't match the latest `RevisionId` of the function or alias, it will fail with an error message, advising you retrieve the latest function version or alias `RevisionID` using either [GetFunction](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunction.html) or [GetAlias](https://docs.aws.amazon.com/lambda/latest/dg/API_GetAlias.html) operations\.  
Type: String  
Required: No

 ** [RoutingConfig](#API_UpdateAlias_RequestSyntax) **   <a name="SSS-UpdateAlias-request-RoutingConfig"></a>
Specifies an additional version your alias can point to, allowing you to dictate what percentage of traffic will invoke each version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object  
Required: No

## Response Syntax<a name="API_UpdateAlias_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[AliasArn](#SSS-UpdateAlias-response-AliasArn)": "string",
   "[Description](#SSS-UpdateAlias-response-Description)": "string",
   "[FunctionVersion](#SSS-UpdateAlias-response-FunctionVersion)": "string",
   "[Name](#SSS-UpdateAlias-response-Name)": "string",
   "[RevisionId](#SSS-UpdateAlias-response-RevisionId)": "string",
   "[RoutingConfig](#SSS-UpdateAlias-response-RoutingConfig)": { 
      "[AdditionalVersionWeights](API_AliasRoutingConfiguration.md#SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights)": { 
         "string" : number 
      }
   }
}
```

## Response Elements<a name="API_UpdateAlias_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [AliasArn](#API_UpdateAlias_ResponseSyntax) **   <a name="SSS-UpdateAlias-response-AliasArn"></a>
Lambda function ARN that is qualified using the alias name as the suffix\. For example, if you create an alias called `BETA` that points to a helloworld function version, the ARN is `arn:aws:lambda:aws-regions:acct-id:function:helloworld:BETA`\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Description](#API_UpdateAlias_ResponseSyntax) **   <a name="SSS-UpdateAlias-response-Description"></a>
Alias description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [FunctionVersion](#API_UpdateAlias_ResponseSyntax) **   <a name="SSS-UpdateAlias-response-FunctionVersion"></a>
Function version to which the alias points\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [Name](#API_UpdateAlias_ResponseSyntax) **   <a name="SSS-UpdateAlias-response-Name"></a>
Alias name\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

 ** [RevisionId](#API_UpdateAlias_ResponseSyntax) **   <a name="SSS-UpdateAlias-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

 ** [RoutingConfig](#API_UpdateAlias_ResponseSyntax) **   <a name="SSS-UpdateAlias-response-RoutingConfig"></a>
Specifies an additional function versions the alias points to, allowing you to dictate what percentage of traffic will invoke each version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object

## Errors<a name="API_UpdateAlias_Errors"></a>

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
   
HTTP Status Code: 429

## See Also<a name="API_UpdateAlias_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateAlias) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UpdateAlias) 