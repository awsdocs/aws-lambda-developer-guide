# CreateFunction<a name="API_CreateFunction"></a>

Creates a Lambda function\. To create a function, you need a [deployment package](https://docs.aws.amazon.com/lambda/latest/dg/deployment-package-v2.html) and an [execution role](https://docs.aws.amazon.com/lambda/latest/dg/intro-permission-model.html#lambda-intro-execution-role)\. The deployment package contains your function code\. The execution role grants the function permission to use AWS services, such as Amazon CloudWatch Logs for log streaming and AWS X\-Ray for request tracing\.

When you create a function, Lambda provisions an instance of the function and its supporting resources\. If your function connects to a VPC, this process can take a minute or so\. During this time, you can't invoke or modify the function\. The `State`, `StateReason`, and `StateReasonCode` fields in the response from [GetFunctionConfiguration](API_GetFunctionConfiguration.md) indicate when the function is ready to invoke\. For more information, see [Function States](https://docs.aws.amazon.com/lambda/latest/dg/functions-states.html)\.

A function has an unpublished version, and can have published versions and aliases\. The unpublished version changes when you update your function's code and configuration\. A published version is a snapshot of your function code and configuration that can't be changed\. An alias is a named resource that maps to a version, and can be changed to map to a different version\. Use the `Publish` parameter to create version `1` of your function from its initial configuration\.

The other parameters let you configure version\-specific and function\-level settings\. You can modify version\-specific settings later with [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\. Function\-level settings apply to both the unpublished and published versions of the function, and include tags \([TagResource](API_TagResource.md)\) and per\-function concurrency limits \([PutFunctionConcurrency](API_PutFunctionConcurrency.md)\)\.

If another account or an AWS service invokes your function, use [AddPermission](API_AddPermission.md) to grant permission by creating a resource\-based IAM policy\. You can grant permissions at the function level, on a version, or on an alias\.

To invoke your function directly, use [Invoke](API_Invoke.md)\. To invoke your function in response to events in other AWS services, create an event source mapping \([CreateEventSourceMapping](API_CreateEventSourceMapping.md)\), or configure a function trigger in the other service\. For more information, see [Invoking Functions](https://docs.aws.amazon.com/lambda/latest/dg/lambda-invocation.html)\.

## Request Syntax<a name="API_CreateFunction_RequestSyntax"></a>

```
POST /2015-03-31/functions HTTP/1.1
Content-type: application/json

{
   "Code": { 
      "S3Bucket": "string",
      "S3Key": "string",
      "S3ObjectVersion": "string",
      "ZipFile": blob
   },
   "DeadLetterConfig": { 
      "TargetArn": "string"
   },
   "Description": "string",
   "Environment": { 
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
   "FunctionName": "string",
   "Handler": "string",
   "KMSKeyArn": "string",
   "Layers": [ "string" ],
   "MemorySize": number,
   "Publish": boolean,
   "Role": "string",
   "Runtime": "string",
   "Tags": { 
      "string" : "string" 
   },
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

## URI Request Parameters<a name="API_CreateFunction_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateFunction_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Code](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Code"></a>
The code for the function\.  
Type: [FunctionCode](API_FunctionCode.md) object  
Required: Yes

 ** [DeadLetterConfig](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-DeadLetterConfig"></a>
A dead letter queue configuration that specifies the queue or topic where Lambda sends asynchronous events when they fail processing\. For more information, see [Dead Letter Queues](https://docs.aws.amazon.com/lambda/latest/dg/invocation-async.html#dlq)\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object  
Required: No

 ** [Description](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Description"></a>
A description of the function\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [Environment](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Environment"></a>
Environment variables that are accessible from function code during execution\.  
Type: [Environment](API_Environment.md) object  
Required: No

 ** [FileSystemConfigs](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-FileSystemConfigs"></a>
Connection settings for an Amazon EFS file system\.  
Type: Array of [FileSystemConfig](API_FileSystemConfig.md) objects  
Array Members: Maximum number of 1 item\.  
Required: No

 ** [FunctionName](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Handler](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Handler"></a>
The name of the method within your code that Lambda calls to execute your function\. The format includes the file name\. It can also include namespaces and other qualifiers, depending on the runtime\. For more information, see [Programming Model](https://docs.aws.amazon.com/lambda/latest/dg/programming-model-v2.html)\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+`   
Required: Yes

 ** [KMSKeyArn](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-KMSKeyArn"></a>
The ARN of the AWS Key Management Service \(AWS KMS\) key that's used to encrypt your function's environment variables\. If it's not provided, AWS Lambda uses a default service key\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()`   
Required: No

 ** [Layers](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Layers"></a>
A list of [function layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html) to add to the function's execution environment\. Specify each layer by its ARN, including the version\.  
Type: Array of strings  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+:[0-9]+`   
Required: No

 ** [MemorySize](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-MemorySize"></a>
The amount of memory that your function has access to\. Increasing the function's memory also increases its CPU allocation\. The default value is 128 MB\. The value must be a multiple of 64 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.  
Required: No

 ** [Publish](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Publish"></a>
Set to true to publish the first version of the function during creation\.  
Type: Boolean  
Required: No

 ** [Role](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Role"></a>
The Amazon Resource Name \(ARN\) of the function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+`   
Required: Yes

 ** [Runtime](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Runtime"></a>
The identifier of the function's [runtime](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html)\.  
Type: String  
Valid Values:` nodejs10.x | nodejs12.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | dotnetcore2.1 | dotnetcore3.1 | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2`   
Required: Yes

 ** [Tags](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Tags"></a>
A list of [tags](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html) to apply to the function\.  
Type: String to string map  
Required: No

 ** [Timeout](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-Timeout"></a>
The amount of time that Lambda allows a function to run before stopping it\. The default is 3 seconds\. The maximum allowed value is 900 seconds\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 ** [TracingConfig](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-TracingConfig"></a>
Set `Mode` to `Active` to sample and trace a subset of incoming requests with AWS X\-Ray\.  
Type: [TracingConfig](API_TracingConfig.md) object  
Required: No

 ** [VpcConfig](#API_CreateFunction_RequestSyntax) **   <a name="SSS-CreateFunction-request-VpcConfig"></a>
For network connectivity to AWS resources in a VPC, specify a list of security groups and subnets in the VPC\. When you connect a function to a VPC, it can only access resources and the internet through that VPC\. For more information, see [VPC Settings](https://docs.aws.amazon.com/lambda/latest/dg/configuration-vpc.html)\.  
Type: [VpcConfig](API_VpcConfig.md) object  
Required: No

## Response Syntax<a name="API_CreateFunction_ResponseSyntax"></a>

```
HTTP/1.1 201
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

## Response Elements<a name="API_CreateFunction_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSha256](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String

 ** [CodeSize](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-CodeSize"></a>
The size of the function's deployment package, in bytes\.  
Type: Long

 ** [DeadLetterConfig](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Environment"></a>
The function's environment variables\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [FileSystemConfigs](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-FileSystemConfigs"></a>
Connection settings for an Amazon EFS file system\.  
Type: Array of [FileSystemConfig](API_FileSystemConfig.md) objects  
Array Members: Maximum number of 1 item\.

 ** [FunctionArn](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-FunctionArn"></a>
The function's Amazon Resource Name \(ARN\)\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Handler"></a>
The function that Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-KMSKeyArn"></a>
The KMS key that's used to encrypt the function's environment variables\. This key is only returned if you've configured a customer managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [LastUpdateStatus](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-LastUpdateStatus"></a>
The status of the last update that was performed on the function\. This is first set to `Successful` after function creation completes\.  
Type: String  
Valid Values:` Successful | Failed | InProgress` 

 ** [LastUpdateStatusReason](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-LastUpdateStatusReason"></a>
The reason for the last update that was performed on the function\.  
Type: String

 ** [LastUpdateStatusReasonCode](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-LastUpdateStatusReasonCode"></a>
The reason code for the last update that was performed on the function\.  
Type: String  
Valid Values:` EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup` 

 ** [Layers](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Layers"></a>
The function's [ layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.  
Type: Array of [Layer](API_Layer.md) objects

 ** [MasterArn](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-MasterArn"></a>
For Lambda@Edge functions, the ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-MemorySize"></a>
The memory that's allocated to the function\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-RevisionId"></a>
The latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs10.x | nodejs12.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | dotnetcore2.1 | dotnetcore3.1 | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2` 

 ** [State](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-State"></a>
The current state of the function\. When the state is `Inactive`, you can reactivate the function by invoking it\.  
Type: String  
Valid Values:` Pending | Active | Inactive | Failed` 

 ** [StateReason](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-StateReason"></a>
The reason for the function's current state\.  
Type: String

 ** [StateReasonCode](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-StateReasonCode"></a>
The reason code for the function's current state\. When the code is `Creating`, you can't invoke or modify the function\.  
Type: String  
Valid Values:` Idle | Creating | Restoring | EniLimitExceeded | InsufficientRolePermissions | InvalidConfiguration | InternalError | SubnetOutOfIPAddresses | InvalidSubnet | InvalidSecurityGroup` 

 ** [Timeout](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Timeout"></a>
The amount of time in seconds that Lambda allows a function to run before stopping it\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [TracingConfig](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-TracingConfig"></a>
The function's AWS X\-Ray tracing configuration\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** [Version](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [VpcConfig](#API_CreateFunction_ResponseSyntax) **   <a name="SSS-CreateFunction-response-VpcConfig"></a>
The function's networking configuration\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object

## Errors<a name="API_CreateFunction_Errors"></a>

 **CodeStorageExceededException**   
You have exceeded your maximum total code size per account\. [Learn more](https://docs.aws.amazon.com/lambda/latest/dg/limits.html)   
HTTP Status Code: 400

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 **ResourceConflictException**   
The resource already exists, or another operation is in progress\.  
HTTP Status Code: 409

 **ResourceNotFoundException**   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_CreateFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateFunction) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CreateFunction) 