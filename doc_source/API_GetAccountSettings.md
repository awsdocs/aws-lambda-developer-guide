# GetAccountSettings<a name="API_GetAccountSettings"></a>

Returns a customer's account settings\.

You can use this operation to retrieve Lambda limits information, such as code size and concurrency limits\. For more information about limits, see [AWS Lambda Limits](http://docs.aws.amazon.com/lambda/latest/dg/limits.html)\. You can also retrieve resource usage statistics, such as code storage usage and function count\.

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
   "[AccountLimit](#SSS-GetAccountSettings-response-AccountLimit)": { 
      "[CodeSizeUnzipped](API_AccountLimit.md#SSS-Type-AccountLimit-CodeSizeUnzipped)": number,
      "[CodeSizeZipped](API_AccountLimit.md#SSS-Type-AccountLimit-CodeSizeZipped)": number,
      "[ConcurrentExecutions](API_AccountLimit.md#SSS-Type-AccountLimit-ConcurrentExecutions)": number,
      "[TotalCodeSize](API_AccountLimit.md#SSS-Type-AccountLimit-TotalCodeSize)": number,
      "[UnreservedConcurrentExecutions](API_AccountLimit.md#SSS-Type-AccountLimit-UnreservedConcurrentExecutions)": number
   },
   "[AccountUsage](#SSS-GetAccountSettings-response-AccountUsage)": { 
      "[FunctionCount](API_AccountUsage.md#SSS-Type-AccountUsage-FunctionCount)": number,
      "[TotalCodeSize](API_AccountUsage.md#SSS-Type-AccountUsage-TotalCodeSize)": number
   }
}
```

## Response Elements<a name="API_GetAccountSettings_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [AccountLimit](#API_GetAccountSettings_ResponseSyntax) **   <a name="SSS-GetAccountSettings-response-AccountLimit"></a>
Provides limits of code size and concurrency associated with the current account and region\.  
Type: [AccountLimit](API_AccountLimit.md) object

 ** [AccountUsage](#API_GetAccountSettings_ResponseSyntax) **   <a name="SSS-GetAccountSettings-response-AccountUsage"></a>
Provides code size usage and function count associated with the current account and region\.  
Type: [AccountUsage](API_AccountUsage.md) object

## Errors<a name="API_GetAccountSettings_Errors"></a>

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
   
HTTP Status Code: 429

## See Also<a name="API_GetAccountSettings_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetAccountSettings) 
+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetAccountSettings) 