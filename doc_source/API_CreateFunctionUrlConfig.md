# CreateFunctionUrlConfig<a name="API_CreateFunctionUrlConfig"></a>

Creates a Lambda function URL with the specified configuration parameters\. A function URL is a dedicated HTTP\(S\) endpoint that you can use to invoke your function\.

## Request Syntax<a name="API_CreateFunctionUrlConfig_RequestSyntax"></a>

```
POST /2021-10-31/functions/FunctionName/url?Qualifier=Qualifier HTTP/1.1
Content-type: application/json

{
   "AuthType": "string",
   "Cors": { 
      "AllowCredentials": boolean,
      "AllowHeaders": [ "string" ],
      "AllowMethods": [ "string" ],
      "AllowOrigins": [ "string" ],
      "ExposeHeaders": [ "string" ],
      "MaxAge": number
   }
}
```

## URI Request Parameters<a name="API_CreateFunctionUrlConfig_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_CreateFunctionUrlConfig_RequestSyntax) **   <a name="SSS-CreateFunctionUrlConfig-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Qualifier](#API_CreateFunctionUrlConfig_RequestSyntax) **   <a name="SSS-CreateFunctionUrlConfig-request-Qualifier"></a>
The alias name\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(^\$LATEST$)|((?!^[0-9]+$)([a-zA-Z0-9-_]+))` 

## Request Body<a name="API_CreateFunctionUrlConfig_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [AuthType](#API_CreateFunctionUrlConfig_RequestSyntax) **   <a name="SSS-CreateFunctionUrlConfig-request-AuthType"></a>
The type of authentication that your function URL uses\. Set to `AWS_IAM` if you want to restrict access to authenticated `IAM` users only\. Set to `NONE` if you want to bypass IAM authentication to create a public endpoint\. For more information, see [ Security and auth model for Lambda function URLs](https://docs.aws.amazon.com/lambda/latest/dg/urls-auth.html)\.  
Type: String  
Valid Values:` NONE | AWS_IAM`   
Required: Yes

 ** [Cors](#API_CreateFunctionUrlConfig_RequestSyntax) **   <a name="SSS-CreateFunctionUrlConfig-request-Cors"></a>
The [cross\-origin resource sharing \(CORS\)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) settings for your function URL\.  
Type: [Cors](API_Cors.md) object  
Required: No

## Response Syntax<a name="API_CreateFunctionUrlConfig_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "AuthType": "string",
   "Cors": { 
      "AllowCredentials": boolean,
      "AllowHeaders": [ "string" ],
      "AllowMethods": [ "string" ],
      "AllowOrigins": [ "string" ],
      "ExposeHeaders": [ "string" ],
      "MaxAge": number
   },
   "CreationTime": "string",
   "FunctionArn": "string",
   "FunctionUrl": "string"
}
```

## Response Elements<a name="API_CreateFunctionUrlConfig_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [AuthType](#API_CreateFunctionUrlConfig_ResponseSyntax) **   <a name="SSS-CreateFunctionUrlConfig-response-AuthType"></a>
The type of authentication that your function URL uses\. Set to `AWS_IAM` if you want to restrict access to authenticated `IAM` users only\. Set to `NONE` if you want to bypass IAM authentication to create a public endpoint\. For more information, see [ Security and auth model for Lambda function URLs](https://docs.aws.amazon.com/lambda/latest/dg/urls-auth.html)\.  
Type: String  
Valid Values:` NONE | AWS_IAM` 

 ** [Cors](#API_CreateFunctionUrlConfig_ResponseSyntax) **   <a name="SSS-CreateFunctionUrlConfig-response-Cors"></a>
The [cross\-origin resource sharing \(CORS\)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) settings for your function URL\.  
Type: [Cors](API_Cors.md) object

 ** [CreationTime](#API_CreateFunctionUrlConfig_ResponseSyntax) **   <a name="SSS-CreateFunctionUrlConfig-response-CreationTime"></a>
When the function URL was created, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [FunctionArn](#API_CreateFunctionUrlConfig_ResponseSyntax) **   <a name="SSS-CreateFunctionUrlConfig-response-FunctionArn"></a>
The Amazon Resource Name \(ARN\) of your function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionUrl](#API_CreateFunctionUrlConfig_ResponseSyntax) **   <a name="SSS-CreateFunctionUrlConfig-response-FunctionUrl"></a>
The HTTP URL endpoint for your function\.  
Type: String  
Length Constraints: Minimum length of 40\. Maximum length of 100\.

## Errors<a name="API_CreateFunctionUrlConfig_Errors"></a>

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

## See Also<a name="API_CreateFunctionUrlConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateFunctionUrlConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CreateFunctionUrlConfig) 