# UpdateFunctionCode<a name="API_UpdateFunctionCode"></a>

Updates a Lambda function's code\.

The function's code is locked when you publish a version\. You can't modify the code of a published version, only the unpublished version\.

## Request Syntax<a name="API_UpdateFunctionCode_RequestSyntax"></a>

```
PUT /2015-03-31/functions/FunctionName/code HTTP/1.1
Content-type: application/json

{
   "[DryRun](#SSS-UpdateFunctionCode-request-DryRun)": boolean,
   "[Publish](#SSS-UpdateFunctionCode-request-Publish)": boolean,
   "[RevisionId](#SSS-UpdateFunctionCode-request-RevisionId)": "string",
   "[S3Bucket](#SSS-UpdateFunctionCode-request-S3Bucket)": "string",
   "[S3Key](#SSS-UpdateFunctionCode-request-S3Key)": "string",
   "[S3ObjectVersion](#SSS-UpdateFunctionCode-request-S3ObjectVersion)": "string",
   "[ZipFile](#SSS-UpdateFunctionCode-request-ZipFile)": blob
}
```

## URI Request Parameters<a name="API_UpdateFunctionCode_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_UpdateFunctionCode_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [DryRun](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-DryRun"></a>
Set to true to validate the request parameters and access permissions without modifying the function code\.  
Type: Boolean  
Required: No

 ** [Publish](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-Publish"></a>
Set to true to publish a new version of the function after updating the code\. This has the same effect as calling [PublishVersion](API_PublishVersion.md) separately\.  
Type: Boolean  
Required: No

 ** [RevisionId](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-RevisionId"></a>
Only update the function if the revision ID matches the ID that's specified\. Use this option to avoid modifying a function that has changed since you last read it\.  
Type: String  
Required: No

 ** [S3Bucket](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3Bucket"></a>
An Amazon S3 bucket in the same AWS Region as your function\. The bucket can be in a different AWS account\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 ** [S3Key](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3Key"></a>
The Amazon S3 key of the deployment package\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** [S3ObjectVersion](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3ObjectVersion"></a>
For versioned objects, the version of the deployment package object to use\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** [ZipFile](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-ZipFile"></a>
The base64\-encoded contents of the deployment package\. AWS SDK and AWS CLI clients handle the encoding for you\.  
Type: Base64\-encoded binary data object  
Required: No

## Response Syntax<a name="API_UpdateFunctionCode_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[CodeSha256](#SSS-UpdateFunctionCode-response-CodeSha256)": "string",
   "[CodeSize](#SSS-UpdateFunctionCode-response-CodeSize)": number,
   "[DeadLetterConfig](#SSS-UpdateFunctionCode-response-DeadLetterConfig)": { 
      "[TargetArn](API_DeadLetterConfig.md#SSS-Type-DeadLetterConfig-TargetArn)": "string"
   },
   "[Description](#SSS-UpdateFunctionCode-response-Description)": "string",
   "[Environment](#SSS-UpdateFunctionCode-response-Environment)": { 
      "[Error](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Error)": { 
         "[ErrorCode](API_EnvironmentError.md#SSS-Type-EnvironmentError-ErrorCode)": "string",
         "[Message](API_EnvironmentError.md#SSS-Type-EnvironmentError-Message)": "string"
      },
      "[Variables](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Variables)": { 
         "string" : "string" 
      }
   },
   "[FunctionArn](#SSS-UpdateFunctionCode-response-FunctionArn)": "string",
   "[FunctionName](#SSS-UpdateFunctionCode-response-FunctionName)": "string",
   "[Handler](#SSS-UpdateFunctionCode-response-Handler)": "string",
   "[KMSKeyArn](#SSS-UpdateFunctionCode-response-KMSKeyArn)": "string",
   "[LastModified](#SSS-UpdateFunctionCode-response-LastModified)": "string",
   "[Layers](#SSS-UpdateFunctionCode-response-Layers)": [ 
      { 
         "[Arn](API_Layer.md#SSS-Type-Layer-Arn)": "string",
         "[CodeSize](API_Layer.md#SSS-Type-Layer-CodeSize)": number
      }
   ],
   "[MasterArn](#SSS-UpdateFunctionCode-response-MasterArn)": "string",
   "[MemorySize](#SSS-UpdateFunctionCode-response-MemorySize)": number,
   "[RevisionId](#SSS-UpdateFunctionCode-response-RevisionId)": "string",
   "[Role](#SSS-UpdateFunctionCode-response-Role)": "string",
   "[Runtime](#SSS-UpdateFunctionCode-response-Runtime)": "string",
   "[Timeout](#SSS-UpdateFunctionCode-response-Timeout)": number,
   "[TracingConfig](#SSS-UpdateFunctionCode-response-TracingConfig)": { 
      "[Mode](API_TracingConfigResponse.md#SSS-Type-TracingConfigResponse-Mode)": "string"
   },
   "[Version](#SSS-UpdateFunctionCode-response-Version)": "string",
   "[VpcConfig](#SSS-UpdateFunctionCode-response-VpcConfig)": { 
      "[SecurityGroupIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SecurityGroupIds)": [ "string" ],
      "[SubnetIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SubnetIds)": [ "string" ],
      "[VpcId](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-VpcId)": "string"
   }
}
```

## Response Elements<a name="API_UpdateFunctionCode_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSha256](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String

 ** [CodeSize](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-CodeSize"></a>
The size of the function's deployment package, in bytes\.  
Type: Long

 ** [DeadLetterConfig](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Environment"></a>
The function's environment variables\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [FunctionArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-FunctionArn"></a>
The function's Amazon Resource Name \(ARN\)\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Handler"></a>
The function that Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-KMSKeyArn"></a>
The KMS key that's used to encrypt the function's environment variables\. This key is only returned if you've configured a customer\-managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [Layers](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Layers"></a>
The function's [ layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.  
Type: Array of [Layer](API_Layer.md) objects

 ** [MasterArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-MasterArn"></a>
For Lambda@Edge functions, the ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-MemorySize"></a>
The memory that's allocated to the function\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-RevisionId"></a>
The latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs8.10 | nodejs10.x | java8 | python2.7 | python3.6 | python3.7 | dotnetcore1.0 | dotnetcore2.1 | go1.x | ruby2.5 | provided` 

 ** [Timeout](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Timeout"></a>
The amount of time that Lambda allows a function to run before stopping it\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [TracingConfig](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-TracingConfig"></a>
The function's AWS X\-Ray tracing configuration\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** [Version](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [VpcConfig](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-VpcConfig"></a>
The function's networking configuration\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object

## Errors<a name="API_UpdateFunctionCode_Errors"></a>

 **CodeStorageExceededException**   
You have exceeded your maximum total code size per account\. [Learn more](https://docs.aws.amazon.com/lambda/latest/dg/limits.html)   
HTTP Status Code: 400

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
Request throughput limit exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_UpdateFunctionCode_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UpdateFunctionCode) 