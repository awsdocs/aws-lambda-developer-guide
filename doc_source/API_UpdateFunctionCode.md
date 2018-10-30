# UpdateFunctionCode<a name="API_UpdateFunctionCode"></a>

Updates the code for the specified Lambda function\. This operation must only be used on an existing Lambda function and cannot be used to update the function configuration\.

If you are using the versioning feature, note this API will always update the $LATEST version of your Lambda function\. For information about the versioning feature, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:UpdateFunctionCode` action\.

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
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_UpdateFunctionCode_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [DryRun](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-DryRun"></a>
This boolean parameter can be used to test your request to AWS Lambda to update the Lambda function and publish a version as an atomic operation\. It will do all necessary computation and validation of your code but will not upload it or a publish a version\. Each time this operation is invoked, the `CodeSha256` hash value of the provided code will also be computed and returned in the response\.  
Type: Boolean  
Required: No

 ** [Publish](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-Publish"></a>
This boolean parameter can be used to request AWS Lambda to update the Lambda function and publish a version as an atomic operation\.  
Type: Boolean  
Required: No

 ** [RevisionId](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-RevisionId"></a>
An optional value you can use to ensure you are updating the latest update of the function version or alias\. If the `RevisionID` you pass doesn't match the latest `RevisionId` of the function or alias, it will fail with an error message, advising you to retrieve the latest function version or alias `RevisionID` using either using using either [GetFunction](API_GetFunction.md) or [GetAlias](API_GetAlias.md)\.  
Type: String  
Required: No

 ** [S3Bucket](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3Bucket"></a>
Amazon S3 bucket name where the \.zip file containing your deployment package is stored\. This bucket must reside in the same AWS Region where you are creating the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 ** [S3Key](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3Key"></a>
The Amazon S3 object \(the deployment package\) key name you want to upload\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** [S3ObjectVersion](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3ObjectVersion"></a>
The Amazon S3 object \(the deployment package\) version you want to upload\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** [ZipFile](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-ZipFile"></a>
The contents of your zip file containing your deployment package\. If you are using the web API directly, the contents of the zip file must be base64\-encoded\. If you are using the AWS SDKs or the AWS CLI, the SDKs or CLI will do the encoding for you\. For more information about creating a \.zip file, see [Execution Permissions](https://docs.aws.amazon.com/lambda/latest/dg/intro-permission-model.html#lambda-intro-execution-role.html)\.   
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
The size of the function's deployment package in bytes\.  
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
The function's Amazon Resource Name\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Handler"></a>
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-KMSKeyArn"></a>
The KMS key used to encrypt the function's environment variables\. Only returned if you've configured a customer managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ssTZD\)\.  
Type: String

 ** [MasterArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-MasterArn"></a>
The ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-MemorySize"></a>
The memory allocated to the function  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | nodejs4.3-edge | go1.x` 

 ** [Timeout](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Timeout"></a>
The amount of time that Lambda allows a function to run before terminating it\.  
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
You have exceeded your maximum total code size per account\. [Limits](https://docs.aws.amazon.com/lambda/latest/dg/limits.html)   
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
Request throughput limit exceeded  
HTTP Status Code: 429

## See Also<a name="API_UpdateFunctionCode_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UpdateFunctionCode) 