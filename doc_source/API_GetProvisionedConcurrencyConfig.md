# GetProvisionedConcurrencyConfig<a name="API_GetProvisionedConcurrencyConfig"></a>

Retrieves the provisioned concurrency configuration for a function's alias or version\.

## Request Syntax<a name="API_GetProvisionedConcurrencyConfig_RequestSyntax"></a>

```
GET /2019-09-30/functions/FunctionName/provisioned-concurrency?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetProvisionedConcurrencyConfig_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_GetProvisionedConcurrencyConfig_RequestSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Qualifier](#API_GetProvisionedConcurrencyConfig_RequestSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-request-Qualifier"></a>
The version number or alias name\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)`   
Required: Yes

## Request Body<a name="API_GetProvisionedConcurrencyConfig_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetProvisionedConcurrencyConfig_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "AllocatedProvisionedConcurrentExecutions": number,
   "AvailableProvisionedConcurrentExecutions": number,
   "LastModified": "string",
   "RequestedProvisionedConcurrentExecutions": number,
   "Status": "string",
   "StatusReason": "string"
}
```

## Response Elements<a name="API_GetProvisionedConcurrencyConfig_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [AllocatedProvisionedConcurrentExecutions](#API_GetProvisionedConcurrencyConfig_ResponseSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-response-AllocatedProvisionedConcurrentExecutions"></a>
The amount of provisioned concurrency allocated\. When a weighted alias is used during linear and canary deployments, this value fluctuates depending on the amount of concurrency that is provisioned for the function versions\.  
Type: Integer  
Valid Range: Minimum value of 0\.

 ** [AvailableProvisionedConcurrentExecutions](#API_GetProvisionedConcurrencyConfig_ResponseSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-response-AvailableProvisionedConcurrentExecutions"></a>
The amount of provisioned concurrency available\.  
Type: Integer  
Valid Range: Minimum value of 0\.

 ** [LastModified](#API_GetProvisionedConcurrencyConfig_ResponseSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-response-LastModified"></a>
The date and time that a user last updated the configuration, in [ISO 8601 format](https://www.iso.org/iso-8601-date-and-time-format.html)\.  
Type: String

 ** [RequestedProvisionedConcurrentExecutions](#API_GetProvisionedConcurrencyConfig_ResponseSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-response-RequestedProvisionedConcurrentExecutions"></a>
The amount of provisioned concurrency requested\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [Status](#API_GetProvisionedConcurrencyConfig_ResponseSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-response-Status"></a>
The status of the allocation process\.  
Type: String  
Valid Values:` IN_PROGRESS | READY | FAILED` 

 ** [StatusReason](#API_GetProvisionedConcurrencyConfig_ResponseSyntax) **   <a name="SSS-GetProvisionedConcurrencyConfig-response-StatusReason"></a>
For failed allocations, the reason that provisioned concurrency could not be allocated\.  
Type: String

## Errors<a name="API_GetProvisionedConcurrencyConfig_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ProvisionedConcurrencyConfigNotFoundException **   
The specified configuration does not exist\.  
HTTP Status Code: 404

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetProvisionedConcurrencyConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetProvisionedConcurrencyConfig) 