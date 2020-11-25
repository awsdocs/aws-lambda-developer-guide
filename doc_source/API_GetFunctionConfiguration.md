# GetFunctionConfiguration<a name="API_GetFunctionConfiguration"></a>

Returns the version\-specific settings of a Lambda function or version\. The output includes only options that can vary between versions of a function\. To modify these settings, use [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\.

To get all of a function's details, including function\-level settings, use [GetFunction](API_GetFunction.md)\.

## Request Syntax<a name="API_GetFunctionConfiguration_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/configuration?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunctionConfiguration_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_GetFunctionConfiguration_RequestSyntax) **   <a name="SSS-GetFunctionConfiguration-request-FunctionName"></a>
The name of the Lambda function, version, or alias\.  

**Name formats**
+  **Function name** \- `my-function` \(name\-only\), `my-function:v1` \(with alias\)\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
You can append a version number or alias to any of the formats\. The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

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
   "FileSystemConfigs": [ 
      { 
         "Arn": "string",
         "LocalMountPath": "string"
      }
   ],
   "FunctionArn": "string",
   "FunctionName": "string",
   "Handler": "string",
   "KMSKeyArn": "string",
   "LastModified": "string",
   "LastUpdateStatus": "string",
   "LastUpdateStatusReason": "string",
   "LastUpdateStatusReasonCode": "string",
   "Layers": [ 
      { 
         "Arn": "string",
         "CodeSize": number
      }
   ],
   "MasterArn": "string",
   "MemorySize": number,
   "RevisionId": "string",
   "Role": "string",
   "Runtime": "string",
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

 ** [FileSystemConfigs](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-FileSystemConfigs"></a>
Connection settings for an Amazon EFS file system\.  
Type: Array of [FileSystemConfig](API_FileSystemConfig.md) objects  
Array Members: Maximum number of 1 item\.

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
The KMS key that's used to encrypt the function's environment variables\. This key is only returned if you've configured a customer managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [LastUpdateStatus](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-LastUpdateStatus"></a>
The status of the last update that was performed on the function\. This is first set to `Successful` after function creation completes\.  
Type: String  
Valid Values:` Successful | Failed | InProgress` 

 ** [LastUpdateStatusReason](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-LastUpdateStatusReason"></a>
The reason for the last update that was performed on the function\.  
Type: String

 ** [LastUpdateStatusReasonCode](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-LastUpdateStatusReasonCode"></a>
The reason code for the last update that was performed on the function\.  
Type: String  
Valid Values:` EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup` 

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
Valid Values:` nodejs10.x | nodejs12.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | dotnetcore2.1 | dotnetcore3.1 | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2` 

 ** [State](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-State"></a>
The current state of the function\. When the state is `Inactive`, you can reactivate the function by invoking it\.  
Type: String  
Valid Values:` Pending | Active | Inactive | Failed` 

 ** [StateReason](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-StateReason"></a>
The reason for the function's current state\.  
Type: String

 ** [StateReasonCode](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-StateReasonCode"></a>
The reason code for the function's current state\. When the code is `Creating`, you can't invoke or modify the function\.  
Type: String  
Valid Values:` Idle | Creating | Restoring | EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup` 

 ** [Timeout](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Timeout"></a>
The amount of time in seconds that Lambda allows a function to run before stopping it\.  
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
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 **ResourceNotFoundException**   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetFunctionConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetFunctionConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetFunctionConfiguration) 