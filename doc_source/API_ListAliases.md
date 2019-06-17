# ListAliases<a name="API_ListAliases"></a>

Returns a list of [aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html) for a Lambda function\.

## Request Syntax<a name="API_ListAliases_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/aliases?FunctionVersion=FunctionVersion&Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListAliases_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_ListAliases_RequestSyntax) **   <a name="SSS-ListAliases-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionVersion](#API_ListAliases_RequestSyntax) **   <a name="SSS-ListAliases-request-FunctionVersion"></a>
Specify a function version to only list aliases that invoke that version\.  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [Marker](#API_ListAliases_RequestSyntax) **   <a name="SSS-ListAliases-request-Marker"></a>
Specify the pagination token that's returned by a previous request to retrieve the next page of results\.

 ** [MaxItems](#API_ListAliases_RequestSyntax) **   <a name="SSS-ListAliases-request-MaxItems"></a>
Limit the number of aliases returned\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListAliases_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListAliases_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[Aliases](#SSS-ListAliases-response-Aliases)": [ 
      { 
         "[AliasArn](API_AliasConfiguration.md#SSS-Type-AliasConfiguration-AliasArn)": "string",
         "[Description](API_AliasConfiguration.md#SSS-Type-AliasConfiguration-Description)": "string",
         "[FunctionVersion](API_AliasConfiguration.md#SSS-Type-AliasConfiguration-FunctionVersion)": "string",
         "[Name](API_AliasConfiguration.md#SSS-Type-AliasConfiguration-Name)": "string",
         "[RevisionId](API_AliasConfiguration.md#SSS-Type-AliasConfiguration-RevisionId)": "string",
         "[RoutingConfig](API_AliasConfiguration.md#SSS-Type-AliasConfiguration-RoutingConfig)": { 
            "[AdditionalVersionWeights](API_AliasRoutingConfiguration.md#SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights)": { 
               "string" : number 
            }
         }
      }
   ],
   "[NextMarker](#SSS-ListAliases-response-NextMarker)": "string"
}
```

## Response Elements<a name="API_ListAliases_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Aliases](#API_ListAliases_ResponseSyntax) **   <a name="SSS-ListAliases-response-Aliases"></a>
A list of aliases\.  
Type: Array of [AliasConfiguration](API_AliasConfiguration.md) objects

 ** [NextMarker](#API_ListAliases_ResponseSyntax) **   <a name="SSS-ListAliases-response-NextMarker"></a>
The pagination token that's included if more results are available\.  
Type: String

## Errors<a name="API_ListAliases_Errors"></a>

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

## See Also<a name="API_ListAliases_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListAliases) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/ListAliases) 