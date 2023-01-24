# GetAccountSettings<a name="API_GetAccountSettings"></a>

Retrieves details about your account's [limits](https://docs.aws.amazon.com/lambda/latest/dg/limits.html) and usage in an AWS Region\.

## Request Syntax<a name="API_GetAccountSettings_RequestSyntax"></a>

```
GET /2016-08-19/account-settings/ HTTP/1.1
```

## URI Request Parameters<a name="API_GetAccountSettings_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_GetAccountSettings_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetAccountSettings_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "AccountLimit": { 
      "CodeSizeUnzipped": number,
      "CodeSizeZipped": number,
      "ConcurrentExecutions": number,
      "TotalCodeSize": number,
      "UnreservedConcurrentExecutions": number
   },
   "AccountUsage": { 
      "FunctionCount": number,
      "TotalCodeSize": number
   }
}
```

## Response Elements<a name="API_GetAccountSettings_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [AccountLimit](#API_GetAccountSettings_ResponseSyntax) **   <a name="SSS-GetAccountSettings-response-AccountLimit"></a>
Limits that are related to concurrency and code storage\.  
Type: [AccountLimit](API_AccountLimit.md) object

 ** [AccountUsage](#API_GetAccountSettings_ResponseSyntax) **   <a name="SSS-GetAccountSettings-response-AccountUsage"></a>
The number of functions and amount of storage in use\.  
Type: [AccountUsage](API_AccountUsage.md) object

## Errors<a name="API_GetAccountSettings_Errors"></a>

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetAccountSettings_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetAccountSettings) 