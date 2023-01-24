# UpdateFunctionConfiguration<a name="API_UpdateFunctionConfiguration"></a>

Modify the version\-specific settings of a Lambda function\.

When you update a function, Lambda provisions an instance of the function and its supporting resources\. If your function connects to a VPC, this process can take a minute\. During this time, you can't modify the function, but you can still invoke it\. The `LastUpdateStatus`, `LastUpdateStatusReason`, and `LastUpdateStatusReasonCode` fields in the response from [GetFunctionConfiguration](API_GetFunctionConfiguration.md) indicate when the update is complete and the function is processing events with the new configuration\. For more information, see [Function States](https://docs.aws.amazon.com/lambda/latest/dg/functions-states.html)\.

These settings can vary between versions of a function and are locked when you publish a version\. You can't modify the configuration of a published version, only the unpublished version\.

To configure function concurrency, use [PutFunctionConcurrency](API_PutFunctionConcurrency.md)\. To grant invoke permissions to an account or AWS service, use [AddPermission](API_AddPermission.md)\.

## Request Syntax<a name="API_UpdateFunctionConfiguration_RequestSyntax"></a>

```
PUT /2015-03-31/functions/FunctionName/configuration HTTP/1.1
Content-type: application/json

{
   "DeadLetterConfig": { 
      "TargetArn": "string"
   },
   "Description": "string",
   "Environment": { 
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
   "Handler": "string",
   "ImageConfig": { 
      "Command": [ "string" ],
      "EntryPoint": [ "string" ],
      "WorkingDirectory": "string"
   },
   "KMSKeyArn": "string",
   "Layers": [ "string" ],
   "MemorySize": number,
   "RevisionId": "string",
   "Role": "string",
   "Runtime": "string",
   "Timeout": number,
   "TracingConfig": { 
      "Mode": "string"
   },
   "VpcConfig": { 
      "SecurityGroupIds": [ "string" ],
      "SubnetIds": [ "string" ]
   }
}
```

## URI Request Parameters<a name="API_UpdateFunctionConfiguration_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

## Request Body<a name="API_UpdateFunctionConfiguration_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [DeadLetterConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-DeadLetterConfig"></a>
A dead letter queue configuration that specifies the queue or topic where Lambda sends asynchronous events when they fail processing\. For more information, see [Dead Letter Queues](https://docs.aws.amazon.com/lambda/latest/dg/invocation-async.html#dlq)\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object  
Required: No

 ** [Description](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Description"></a>
A description of the function\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [Environment](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Environment"></a>
Environment variables that are accessible from function code during execution\.  
Type: [Environment](API_Environment.md) object  
Required: No

 ** [EphemeralStorage](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-EphemeralStorage"></a>
The size of the function’s /tmp directory in MB\. The default value is 512, but can be any whole number between 512 and 10240 MB\.  
Type: [EphemeralStorage](API_EphemeralStorage.md) object  
Required: No

 ** [FileSystemConfigs](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-FileSystemConfigs"></a>
Connection settings for an Amazon EFS file system\.  
Type: Array of [FileSystemConfig](API_FileSystemConfig.md) objects  
Array Members: Maximum number of 1 item\.  
Required: No

 ** [Handler](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Handler"></a>
The name of the method within your code that Lambda calls to execute your function\. Handler is required if the deployment package is a \.zip file archive\. The format includes the file name\. It can also include namespaces and other qualifiers, depending on the runtime\. For more information, see [Programming Model](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html)\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+`   
Required: No

 ** [ImageConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-ImageConfig"></a>
 [Container image configuration values](https://docs.aws.amazon.com/lambda/latest/dg/images-parms.html) that override the values in the container image Docker file\.  
Type: [ImageConfig](API_ImageConfig.md) object  
Required: No

 ** [KMSKeyArn](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-KMSKeyArn"></a>
The ARN of the AWS Key Management Service \(AWS KMS\) key that's used to encrypt your function's environment variables\. If it's not provided, AWS Lambda uses a default service key\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()`   
Required: No

 ** [Layers](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Layers"></a>
A list of [function layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html) to add to the function's execution environment\. Specify each layer by its ARN, including the version\.  
Type: Array of strings  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+:[0-9]+`   
Required: No

 ** [MemorySize](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-MemorySize"></a>
The amount of [memory available to the function](https://docs.aws.amazon.com/lambda/latest/dg/configuration-memory.html) at runtime\. Increasing the function memory also increases its CPU allocation\. The default value is 128 MB\. The value can be any multiple of 1 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 10240\.  
Required: No

 ** [RevisionId](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-RevisionId"></a>
Only update the function if the revision ID matches the ID that's specified\. Use this option to avoid modifying a function that has changed since you last read it\.  
Type: String  
Required: No

 ** [Role](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Role"></a>
The Amazon Resource Name \(ARN\) of the function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+`   
Required: No

 ** [Runtime](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Runtime"></a>
The identifier of the function's [runtime](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html)\. Runtime is required if the deployment package is a \.zip file archive\.   
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | nodejs10.x | nodejs12.x | nodejs14.x | nodejs16.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | python3.9 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | dotnetcore3.1 | dotnet6 | nodejs4.3-edge | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2`   
Required: No

 ** [Timeout](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Timeout"></a>
The amount of time \(in seconds\) that Lambda allows a function to run before stopping it\. The default is 3 seconds\. The maximum allowed value is 900 seconds\. For additional information, see [Lambda execution environment](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-context.html)\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 ** [TracingConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-TracingConfig"></a>
Set `Mode` to `Active` to sample and trace a subset of incoming requests with [X\-Ray](https://docs.aws.amazon.com/lambda/latest/dg/services-xray.html)\.  
Type: [TracingConfig](API_TracingConfig.md) object  
Required: No

 ** [VpcConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-VpcConfig"></a>
For network connectivity to AWS resources in a VPC, specify a list of security groups and subnets in the VPC\. When you connect a function to a VPC, it can only access resources and the internet through that VPC\. For more information, see [VPC Settings](https://docs.aws.amazon.com/lambda/latest/dg/configuration-vpc.html)\.  
Type: [VpcConfig](API_VpcConfig.md) object  
Required: No

## Response Syntax<a name="API_UpdateFunctionConfiguration_ResponseSyntax"></a>

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

## Response Elements<a name="API_UpdateFunctionConfiguration_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Architectures](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Architectures"></a>
The instruction set architecture that the function supports\. Architecture is a string array with one of the valid values\. The default architecture value is `x86_64`\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Valid Values:` x86_64 | arm64` 

 ** [CodeSha256](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String

 ** [CodeSize](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-CodeSize"></a>
The size of the function's deployment package, in bytes\.  
Type: Long

 ** [DeadLetterConfig](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Environment"></a>
The function's [environment variables](https://docs.aws.amazon.com/lambda/latest/dg/configuration-envvars.html)\. Omitted from AWS CloudTrail logs\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [EphemeralStorage](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-EphemeralStorage"></a>
The size of the function’s /tmp directory in MB\. The default value is 512, but can be any whole number between 512 and 10240 MB\.  
Type: [EphemeralStorage](API_EphemeralStorage.md) object

 ** [FileSystemConfigs](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-FileSystemConfigs"></a>
Connection settings for an [Amazon EFS file system](https://docs.aws.amazon.com/lambda/latest/dg/configuration-filesystem.html)\.  
Type: Array of [FileSystemConfig](API_FileSystemConfig.md) objects  
Array Members: Maximum number of 1 item\.

 ** [FunctionArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-FunctionArn"></a>
The function's Amazon Resource Name \(ARN\)\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Handler"></a>
The function that Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [ImageConfigResponse](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-ImageConfigResponse"></a>
The function's image configuration values\.  
Type: [ImageConfigResponse](API_ImageConfigResponse.md) object

 ** [KMSKeyArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-KMSKeyArn"></a>
The AWS KMS key that's used to encrypt the function's environment variables\. This key is only returned if you've configured a customer managed key\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [LastUpdateStatus](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-LastUpdateStatus"></a>
The status of the last update that was performed on the function\. This is first set to `Successful` after function creation completes\.  
Type: String  
Valid Values:` Successful | Failed | InProgress` 

 ** [LastUpdateStatusReason](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-LastUpdateStatusReason"></a>
The reason for the last update that was performed on the function\.  
Type: String

 ** [LastUpdateStatusReasonCode](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-LastUpdateStatusReasonCode"></a>
The reason code for the last update that was performed on the function\.  
Type: String  
Valid Values:` EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup | ImageDeleted | ImageAccessDenied | InvalidImage` 

 ** [Layers](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Layers"></a>
The function's [ layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.  
Type: Array of [Layer](API_Layer.md) objects

 ** [MasterArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-MasterArn"></a>
For Lambda@Edge functions, the ARN of the main function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-MemorySize"></a>
The amount of memory available to the function at runtime\.   
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 10240\.

 ** [PackageType](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-PackageType"></a>
The type of deployment package\. Set to `Image` for container image and set `Zip` for \.zip file archive\.  
Type: String  
Valid Values:` Zip | Image` 

 ** [RevisionId](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-RevisionId"></a>
The latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | nodejs10.x | nodejs12.x | nodejs14.x | nodejs16.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | python3.9 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | dotnetcore3.1 | dotnet6 | nodejs4.3-edge | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2` 

 ** [SigningJobArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-SigningJobArn"></a>
The ARN of the signing job\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [SigningProfileVersionArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-SigningProfileVersionArn"></a>
The ARN of the signing profile version\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [State](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-State"></a>
The current state of the function\. When the state is `Inactive`, you can reactivate the function by invoking it\.  
Type: String  
Valid Values:` Pending | Active | Inactive | Failed` 

 ** [StateReason](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-StateReason"></a>
The reason for the function's current state\.  
Type: String

 ** [StateReasonCode](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-StateReasonCode"></a>
The reason code for the function's current state\. When the code is `Creating`, you can't invoke or modify the function\.  
Type: String  
Valid Values:` Idle | Creating | Restoring | EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup | ImageDeleted | ImageAccessDenied | InvalidImage` 

 ** [Timeout](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Timeout"></a>
The amount of time in seconds that Lambda allows a function to run before stopping it\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [TracingConfig](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-TracingConfig"></a>
The function's AWS X\-Ray tracing configuration\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** [Version](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [VpcConfig](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-VpcConfig"></a>
The function's networking configuration\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object

## Errors<a name="API_UpdateFunctionConfiguration_Errors"></a>

 ** CodeSigningConfigNotFoundException **   
The specified code signing configuration does not exist\.  
HTTP Status Code: 404

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

## See Also<a name="API_UpdateFunctionConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/UpdateFunctionConfiguration) 