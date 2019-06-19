# GetFunctionConfiguration<a name="API_GetFunctionConfiguration"></a>

Returns the version\-specific settings of a Lambda function or version\. The output includes only options that can vary between versions of a function\. To modify these settings, use [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\.

To get all of a function's details, including function\-level settings, use [GetFunction](API_GetFunction.md)\.

## Request Syntax<a name="API_GetFunctionConfiguration_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/configuration?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunctionConfiguration_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_GetFunctionConfiguration_RequestSyntax) **   <a name="SSS-GetFunctionConfiguration-request-FunctionName"></a>
The name of the Lambda function, version, or alias\.  

**Name formats**
+  **Function name** \- `my-function` \(name\-only\), `my-function:v1` \(with alias\)\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
You can append a version number or alias to any of the formats\. The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_GetFunctionConfiguration_RequestSyntax) **   <a name="SSS-GetFunctionConfiguration-request-Qualifier"></a>
Specify a version or alias to get details about a published version of the function\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_GetFunctionConfiguration_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetFunctionConfiguration_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[CodeSha256](#SSS-GetFunctionConfiguration-response-CodeSha256)": "string",
   "[CodeSize](#SSS-GetFunctionConfiguration-response-CodeSize)": number,
   "[DeadLetterConfig](#SSS-GetFunctionConfiguration-response-DeadLetterConfig)": { 
      "[TargetArn](API_DeadLetterConfig.md#SSS-Type-DeadLetterConfig-TargetArn)": "string"
   },
   "[Description](#SSS-GetFunctionConfiguration-response-Description)": "string",
   "[Environment](#SSS-GetFunctionConfiguration-response-Environment)": { 
      "[Error](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Error)": { 
         "[ErrorCode](API_EnvironmentError.md#SSS-Type-EnvironmentError-ErrorCode)": "string",
         "[Message](API_EnvironmentError.md#SSS-Type-EnvironmentError-Message)": "string"
      },
      "[Variables](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Variables)": { 
         "string" : "string" 
      }
   },
   "[FunctionArn](#SSS-GetFunctionConfiguration-response-FunctionArn)": "string",
   "[FunctionName](#SSS-GetFunctionConfiguration-response-FunctionName)": "string",
   "[Handler](#SSS-GetFunctionConfiguration-response-Handler)": "string",
   "[KMSKeyArn](#SSS-GetFunctionConfiguration-response-KMSKeyArn)": "string",
   "[LastModified](#SSS-GetFunctionConfiguration-response-LastModified)": "string",
   "[Layers](#SSS-GetFunctionConfiguration-response-Layers)": [ 
      { 
         "[Arn](API_Layer.md#SSS-Type-Layer-Arn)": "string",
         "[CodeSize](API_Layer.md#SSS-Type-Layer-CodeSize)": number
      }
   ],
   "[MasterArn](#SSS-GetFunctionConfiguration-response-MasterArn)": "string",
   "[MemorySize](#SSS-GetFunctionConfiguration-response-MemorySize)": number,
   "[RevisionId](#SSS-GetFunctionConfiguration-response-RevisionId)": "string",
   "[Role](#SSS-GetFunctionConfiguration-response-Role)": "string",
   "[Runtime](#SSS-GetFunctionConfiguration-response-Runtime)": "string",
   "[Timeout](#SSS-GetFunctionConfiguration-response-Timeout)": number,
   "[TracingConfig](#SSS-GetFunctionConfiguration-response-TracingConfig)": { 
      "[Mode](API_TracingConfigResponse.md#SSS-Type-TracingConfigResponse-Mode)": "string"
   },
   "[Version](#SSS-GetFunctionConfiguration-response-Version)": "string",
   "[VpcConfig](#SSS-GetFunctionConfiguration-response-VpcConfig)": { 
      "[SecurityGroupIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SecurityGroupIds)": [ "string" ],
      "[SubnetIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SubnetIds)": [ "string" ],
      "[VpcId](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-VpcId)": "string"
   }
}
```

## Response Elements<a name="API_GetFunctionConfiguration_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSha256](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String

 ** [CodeSize](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-CodeSize"></a>
The size of the function's deployment package, in bytes\.  
Type: Long

 ** [DeadLetterConfig](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Environment"></a>
The function's environment variables\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [FunctionArn](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-FunctionArn"></a>
The function's Amazon Resource Name \(ARN\)\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Handler"></a>
The function that Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-KMSKeyArn"></a>
The KMS key that's used to encrypt the function's environment variables\. This key is only returned if you've configured a customer\-managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [Layers](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Layers"></a>
The function's [ layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.  
Type: Array of [Layer](API_Layer.md) objects

 ** [MasterArn](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-MasterArn"></a>
For Lambda@Edge functions, the ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-MemorySize"></a>
The memory that's allocated to the function\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-RevisionId"></a>
The latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs8.10 | nodejs10.x | java8 | python2.7 | python3.6 | python3.7 | dotnetcore1.0 | dotnetcore2.1 | go1.x | ruby2.5 | provided` 

 ** [Timeout](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Timeout"></a>
The amount of time that Lambda allows a function to run before stopping it\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [TracingConfig](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-TracingConfig"></a>
The function's AWS X\-Ray tracing configuration\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** [Version](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [VpcConfig](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-VpcConfig"></a>
The function's networking configuration\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object

## Errors<a name="API_GetFunctionConfiguration_Errors"></a>

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

## See Also<a name="API_GetFunctionConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetFunctionConfiguration) 