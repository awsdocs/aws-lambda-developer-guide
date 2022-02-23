# CreateAlias<a name="API_CreateAlias"></a>

Creates an [alias](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html) for a Lambda function version\. Use aliases to provide clients with a function identifier that you can update to invoke a different version\.

You can also map an alias to split invocation requests between two versions\. Use the `RoutingConfig` parameter to specify a second version and the percentage of invocation requests that it receives\.

## Request Syntax<a name="API_CreateAlias_RequestSyntax"></a>

```
POST /2015-03-31/functions/FunctionName/aliases HTTP/1.1
Content-type: application/json

{
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

## URI Request Parameters<a name="API_CreateAlias_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

## Request Body<a name="API_CreateAlias_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Description](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-Description"></a>
A description of the alias\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [FunctionVersion](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-FunctionVersion"></a>
The function version that the alias invokes\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: Yes

 ** [Name](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-Name"></a>
The name of the alias\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)`   
Required: Yes

 ** [RoutingConfig](#API_CreateAlias_RequestSyntax) **   <a name="SSS-CreateAlias-request-RoutingConfig"></a>
The [routing configuration](https://docs.aws.amazon.com/lambda/latest/dg/configuration-aliases.html#configuring-alias-routing) of the alias\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object  
Required: No

## Response Syntax<a name="API_CreateAlias_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "AliasArn": "string",
   "Description": "string",
   "FunctionVersion": "string",
   "Name": "string",
   "RevisionId": "string",
   "RoutingConfig": { 
      "AdditionalVersionWeights": { 
         "string" : number 
      }
   }
}
```

## Response Elements<a name="API_CreateAlias_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [AliasArn](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-AliasArn"></a>
The Amazon Resource Name \(ARN\) of the alias\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Description](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-Description"></a>
A description of the alias\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [FunctionVersion](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-FunctionVersion"></a>
The function version that the alias invokes\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [Name](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-Name"></a>
The name of the alias\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)` 

 ** [RevisionId](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-RevisionId"></a>
A unique identifier that changes when you update the alias\.  
Type: String

 ** [RoutingConfig](#API_CreateAlias_ResponseSyntax) **   <a name="SSS-CreateAlias-response-RoutingConfig"></a>
The [routing configuration](https://docs.aws.amazon.com/lambda/latest/dg/lambda-traffic-shifting-using-aliases.html) of the alias\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object

## Errors<a name="API_CreateAlias_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceConflictException **   
The resource already exists, or another operation is in progress\.  
HTTP Status Code: 409

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_CreateAlias_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateAlias) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CreateAlias) 