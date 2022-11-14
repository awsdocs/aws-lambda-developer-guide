# UpdateFunctionCode<a name="API_UpdateFunctionCode"></a>

Updates a Lambda function's code\. If code signing is enabled for the function, the code package must be signed by a trusted publisher\. For more information, see [Configuring code signing](https://docs.aws.amazon.com/lambda/latest/dg/configuration-trustedcode.html)\.

If the function's package type is `Image`, you must specify the code package in `ImageUri` as the URI of a [container image](https://docs.aws.amazon.com/lambda/latest/dg/lambda-images.html) in the Amazon ECR registry\. 

If the function's package type is `Zip`, you must specify the deployment package as a [\.zip file archive](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-package.html#gettingstarted-package-zip)\. Enter the Amazon S3 bucket and key of the code \.zip file location\. You can also provide the function code inline using the `ZipFile` field\. 

The code in the deployment package must be compatible with the target instruction set architecture of the function \(`x86-64` or `arm64`\)\. 

The function's code is locked when you publish a version\. You can't modify the code of a published version, only the unpublished version\.

**Note**  
For a function defined as a container image, Lambda resolves the image tag to an image digest\. In Amazon ECR, if you update the image tag to a new image, Lambda does not automatically update the function\.

## Request Syntax<a name="API_UpdateFunctionCode_RequestSyntax"></a>

```
PUT /2015-03-31/functions/FunctionName/code HTTP/1.1
Content-type: application/json

{
   "Architectures": [ "string" ],
   "DryRun": boolean,
   "ImageUri": "string",
   "Publish": boolean,
   "RevisionId": "string",
   "S3Bucket": "string",
   "S3Key": "string",
   "S3ObjectVersion": "string",
   "ZipFile": blob
}
```

## URI Request Parameters<a name="API_UpdateFunctionCode_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

## Request Body<a name="API_UpdateFunctionCode_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Architectures](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-Architectures"></a>
The instruction set architecture that the function supports\. Enter a string array with one of the valid values \(arm64 or x86\_64\)\. The default value is `x86_64`\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Valid Values:` x86_64 | arm64`   
Required: No

 ** [DryRun](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-DryRun"></a>
Set to true to validate the request parameters and access permissions without modifying the function code\.  
Type: Boolean  
Required: No

 ** [ImageUri](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-ImageUri"></a>
URI of a container image in the Amazon ECR registry\. Do not use for a function defined with a \.zip file archive\.  
Type: String  
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
An Amazon S3 bucket in the same AWS Region as your function\. The bucket can be in a different AWS account\. Use only with a function defined with a \.zip file archive deployment package\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 ** [S3Key](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3Key"></a>
The Amazon S3 key of the deployment package\. Use only with a function defined with a \.zip file archive deployment package\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** [S3ObjectVersion](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-S3ObjectVersion"></a>
For versioned objects, the version of the deployment package object to use\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** [ZipFile](#API_UpdateFunctionCode_RequestSyntax) **   <a name="SSS-UpdateFunctionCode-request-ZipFile"></a>
The base64\-encoded contents of the deployment package\. AWS SDK and AWS CLI clients handle the encoding for you\. Use only with a function defined with a \.zip file archive deployment package\.  
Type: Base64\-encoded binary data object  
Required: No

## Response Syntax<a name="API_UpdateFunctionCode_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "Architectures": [ "string" ],
   "CodeSha256": "string",
   "CodeSize": number,
   "DeadLetterConfig": { 
      "TargetArn": "string"
   },
   "Description": "string",
   "Environment": { 
      "Error": { 
         "ErrorCode": "string",
         "Message": "string"
      },
      "Variables": { 
         "string" : "string" 
      }
   },
   "EphemeralStorage": { 
      "Size": number
   },
   "FileSystemConfigs": [ 
      { 
         "Arn": "string",
         "LocalMountPath": "string"
      }
   ],
   "FunctionArn": "string",
   "FunctionName": "string",
   "Handler": "string",
   "ImageConfigResponse": { 
      "Error": { 
         "ErrorCode": "string",
         "Message": "string"
      },
      "ImageConfig": { 
         "Command": [ "string" ],
         "EntryPoint": [ "string" ],
         "WorkingDirectory": "string"
      }
   },
   "KMSKeyArn": "string",
   "LastModified": "string",
   "LastUpdateStatus": "string",
   "LastUpdateStatusReason": "string",
   "LastUpdateStatusReasonCode": "string",
   "Layers": [ 
      { 
         "Arn": "string",
         "CodeSize": number,
         "SigningJobArn": "string",
         "SigningProfileVersionArn": "string"
      }
   ],
   "MasterArn": "string",
   "MemorySize": number,
   "PackageType": "string",
   "RevisionId": "string",
   "Role": "string",
   "Runtime": "string",
   "SigningJobArn": "string",
   "SigningProfileVersionArn": "string",
   "State": "string",
   "StateReason": "string",
   "StateReasonCode": "string",
   "Timeout": number,
   "TracingConfig": { 
      "Mode": "string"
   },
   "Version": "string",
   "VpcConfig": { 
      "SecurityGroupIds": [ "string" ],
      "SubnetIds": [ "string" ],
      "VpcId": "string"
   }
}
```

## Response Elements<a name="API_UpdateFunctionCode_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Architectures](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Architectures"></a>
The instruction set architecture that the function supports\. Architecture is a string array with one of the valid values\. The default architecture value is `x86_64`\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Valid Values:` x86_64 | arm64` 

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
The function's [environment variables](https://docs.aws.amazon.com/lambda/latest/dg/configuration-envvars.html)\. Omitted from AWS CloudTrail logs\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [EphemeralStorage](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-EphemeralStorage"></a>
The size of the function’s /tmp directory in MB\. The default value is 512, but can be any whole number between 512 and 10240 MB\.  
Type: [EphemeralStorage](API_EphemeralStorage.md) object

 ** [FileSystemConfigs](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-FileSystemConfigs"></a>
Connection settings for an [Amazon EFS file system](https://docs.aws.amazon.com/lambda/latest/dg/configuration-filesystem.html)\.  
Type: Array of [FileSystemConfig](API_FileSystemConfig.md) objects  
Array Members: Maximum number of 1 item\.

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

 ** [ImageConfigResponse](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-ImageConfigResponse"></a>
The function's image configuration values\.  
Type: [ImageConfigResponse](API_ImageConfigResponse.md) object

 ** [KMSKeyArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-KMSKeyArn"></a>
The AWS KMS key that's used to encrypt the function's environment variables\. This key is only returned if you've configured a customer managed key\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [LastUpdateStatus](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-LastUpdateStatus"></a>
The status of the last update that was performed on the function\. This is first set to `Successful` after function creation completes\.  
Type: String  
Valid Values:` Successful | Failed | InProgress` 

 ** [LastUpdateStatusReason](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-LastUpdateStatusReason"></a>
The reason for the last update that was performed on the function\.  
Type: String

 ** [LastUpdateStatusReasonCode](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-LastUpdateStatusReasonCode"></a>
The reason code for the last update that was performed on the function\.  
Type: String  
Valid Values:` EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup | ImageDeleted | ImageAccessDenied | InvalidImage` 

 ** [Layers](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Layers"></a>
The function's [ layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.  
Type: Array of [Layer](API_Layer.md) objects

 ** [MasterArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-MasterArn"></a>
For Lambda@Edge functions, the ARN of the main function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-MemorySize"></a>
The amount of memory available to the function at runtime\.   
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 10240\.

 ** [PackageType](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-PackageType"></a>
The type of deployment package\. Set to `Image` for container image and set `Zip` for \.zip file archive\.  
Type: String  
Valid Values:` Zip | Image` 

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
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | nodejs10.x | nodejs12.x | nodejs14.x | nodejs16.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | python3.9 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | dotnetcore3.1 | dotnet6 | nodejs4.3-edge | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2` 

 ** [SigningJobArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-SigningJobArn"></a>
The ARN of the signing job\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [SigningProfileVersionArn](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-SigningProfileVersionArn"></a>
The ARN of the signing profile version\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [State](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-State"></a>
The current state of the function\. When the state is `Inactive`, you can reactivate the function by invoking it\.  
Type: String  
Valid Values:` Pending | Active | Inactive | Failed` 

 ** [StateReason](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-StateReason"></a>
The reason for the function's current state\.  
Type: String

 ** [StateReasonCode](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-StateReasonCode"></a>
The reason code for the function's current state\. When the code is `Creating`, you can't invoke or modify the function\.  
Type: String  
Valid Values:` Idle | Creating | Restoring | EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup | ImageDeleted | ImageAccessDenied | InvalidImage` 

 ** [Timeout](#API_UpdateFunctionCode_ResponseSyntax) **   <a name="SSS-UpdateFunctionCode-response-Timeout"></a>
The amount of time in seconds that Lambda allows a function to run before stopping it\.  
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

 ** CodeSigningConfigNotFoundException **   
The specified code signing configuration does not exist\.  
HTTP Status Code: 404

 ** CodeStorageExceededException **   
You have exceeded your maximum total code size per account\. [Learn more](https://docs.aws.amazon.com/lambda/latest/dg/limits.html)   
HTTP Status Code: 400

 ** CodeVerificationFailedException **   
The code signature failed one or more of the validation checks for signature mismatch or expiry, and the code signing policy is set to ENFORCE\. Lambda blocks the deployment\.   
HTTP Status Code: 400

 ** InvalidCodeSignatureException **   
The code signature failed the integrity check\. Lambda always blocks deployment if the integrity check fails, even if code signing policy is set to WARN\.  
HTTP Status Code: 400

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** PreconditionFailedException **   
The RevisionId provided does not match the latest RevisionId for the Lambda function or alias\. Call the `GetFunction` or the `GetAlias` API to retrieve the latest RevisionId for your resource\.  
HTTP Status Code: 412

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

## See Also<a name="API_UpdateFunctionCode_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateFunctionCode) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/UpdateFunctionCode) 