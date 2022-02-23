# DeleteProvisionedConcurrencyConfig<a name="API_DeleteProvisionedConcurrencyConfig"></a>

Deletes the provisioned concurrency configuration for a function\.

## Request Syntax<a name="API_DeleteProvisionedConcurrencyConfig_RequestSyntax"></a>

```
DELETE /2019-09-30/functions/FunctionName/provisioned-concurrency?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_DeleteProvisionedConcurrencyConfig_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_DeleteProvisionedConcurrencyConfig_RequestSyntax) **   <a name="SSS-DeleteProvisionedConcurrencyConfig-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Qualifier](#API_DeleteProvisionedConcurrencyConfig_RequestSyntax) **   <a name="SSS-DeleteProvisionedConcurrencyConfig-request-Qualifier"></a>
The version number or alias name\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)`   
Required: Yes

## Request Body<a name="API_DeleteProvisionedConcurrencyConfig_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_DeleteProvisionedConcurrencyConfig_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_DeleteProvisionedConcurrencyConfig_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_DeleteProvisionedConcurrencyConfig_Errors"></a>

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

## See Also<a name="API_DeleteProvisionedConcurrencyConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/DeleteProvisionedConcurrencyConfig) 