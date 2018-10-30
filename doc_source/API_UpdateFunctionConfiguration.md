# UpdateFunctionConfiguration<a name="API_UpdateFunctionConfiguration"></a>

Updates the configuration parameters for the specified Lambda function by using the values provided in the request\. You provide only the parameters you want to change\. This operation must only be used on an existing Lambda function and cannot be used to update the function's code\.

If you are using the versioning feature, note this API will always update the $LATEST version of your Lambda function\. For information about the versioning feature, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:UpdateFunctionConfiguration` action\.

## Request Syntax<a name="API_UpdateFunctionConfiguration_RequestSyntax"></a>

```
PUT /2015-03-31/functions/FunctionName/configuration HTTP/1.1
Content-type: application/json

{
   "[DeadLetterConfig](#SSS-UpdateFunctionConfiguration-request-DeadLetterConfig)": { 
      "[TargetArn](API_DeadLetterConfig.md#SSS-Type-DeadLetterConfig-TargetArn)": "string"
   },
   "[Description](#SSS-UpdateFunctionConfiguration-request-Description)": "string",
   "[Environment](#SSS-UpdateFunctionConfiguration-request-Environment)": { 
      "[Variables](API_Environment.md#SSS-Type-Environment-Variables)": { 
         "string" : "string" 
      }
   },
   "[Handler](#SSS-UpdateFunctionConfiguration-request-Handler)": "string",
   "[KMSKeyArn](#SSS-UpdateFunctionConfiguration-request-KMSKeyArn)": "string",
   "[MemorySize](#SSS-UpdateFunctionConfiguration-request-MemorySize)": number,
   "[RevisionId](#SSS-UpdateFunctionConfiguration-request-RevisionId)": "string",
   "[Role](#SSS-UpdateFunctionConfiguration-request-Role)": "string",
   "[Runtime](#SSS-UpdateFunctionConfiguration-request-Runtime)": "string",
   "[Timeout](#SSS-UpdateFunctionConfiguration-request-Timeout)": number,
   "[TracingConfig](#SSS-UpdateFunctionConfiguration-request-TracingConfig)": { 
      "[Mode](API_TracingConfig.md#SSS-Type-TracingConfig-Mode)": "string"
   },
   "[VpcConfig](#SSS-UpdateFunctionConfiguration-request-VpcConfig)": { 
      "[SecurityGroupIds](API_VpcConfig.md#SSS-Type-VpcConfig-SecurityGroupIds)": [ "string" ],
      "[SubnetIds](API_VpcConfig.md#SSS-Type-VpcConfig-SubnetIds)": [ "string" ]
   }
}
```

## URI Request Parameters<a name="API_UpdateFunctionConfiguration_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-FunctionName"></a>
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_UpdateFunctionConfiguration_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [DeadLetterConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-DeadLetterConfig"></a>
A dead letter queue configuration that specifies the queue or topic where Lambda sends asynchronous events when they fail processing\. For more information, see [Dead Letter Queues](https://docs.aws.amazon.com/lambda/latest/dg/dlq.html)\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object  
Required: No

 ** [Description](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Description"></a>
A short user\-defined function description\. AWS Lambda does not use this value\. Assign a meaningful description as you see fit\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [Environment](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Environment"></a>
The parent object that contains your environment's configuration settings\.  
Type: [Environment](API_Environment.md) object  
Required: No

 ** [Handler](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Handler"></a>
The function that Lambda calls to begin executing your function\. For Node\.js, it is the `module-name.export` value in your function\.   
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+`   
Required: No

 ** [KMSKeyArn](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-KMSKeyArn"></a>
The Amazon Resource Name \(ARN\) of the KMS key used to encrypt your function's environment variables\. If you elect to use the AWS Lambda default service key, pass in an empty string \(""\) for this parameter\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()`   
Required: No

 ** [MemorySize](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-MemorySize"></a>
The amount of memory, in MB, your Lambda function is given\. AWS Lambda uses this memory size to infer the amount of CPU allocated to your function\. Your function use\-case determines your CPU and memory requirements\. For example, a database operation might need less memory compared to an image processing function\. The default value is 128 MB\. The value must be a multiple of 64 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.  
Required: No

 ** [RevisionId](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-RevisionId"></a>
An optional value you can use to ensure you are updating the latest update of the function version or alias\. If the `RevisionID` you pass doesn't match the latest `RevisionId` of the function or alias, it will fail with an error message, advising you to retrieve the latest function version or alias `RevisionID` using either [GetFunction](API_GetFunction.md) or [GetAlias](API_GetAlias.md)\.  
Type: String  
Required: No

 ** [Role](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Role"></a>
The Amazon Resource Name \(ARN\) of the IAM role that Lambda will assume when it executes your function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+`   
Required: No

 ** [Runtime](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Runtime"></a>
The runtime version for the function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | nodejs4.3-edge | go1.x`   
Required: No

 ** [Timeout](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-Timeout"></a>
The amount of time that Lambda allows a function to run before terminating it\. The default is 3 seconds\. The maximum allowed value is 900 seconds\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 ** [TracingConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-TracingConfig"></a>
Set `Mode` to `Active` to sample and trace a subset of incoming requests with AWS X\-Ray\.  
Type: [TracingConfig](API_TracingConfig.md) object  
Required: No

 ** [VpcConfig](#API_UpdateFunctionConfiguration_RequestSyntax) **   <a name="SSS-UpdateFunctionConfiguration-request-VpcConfig"></a>
Specify security groups and subnets in a VPC to which your Lambda function needs access\.  
Type: [VpcConfig](API_VpcConfig.md) object  
Required: No

## Response Syntax<a name="API_UpdateFunctionConfiguration_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[CodeSha256](#SSS-UpdateFunctionConfiguration-response-CodeSha256)": "string",
   "[CodeSize](#SSS-UpdateFunctionConfiguration-response-CodeSize)": number,
   "[DeadLetterConfig](#SSS-UpdateFunctionConfiguration-response-DeadLetterConfig)": { 
      "[TargetArn](API_DeadLetterConfig.md#SSS-Type-DeadLetterConfig-TargetArn)": "string"
   },
   "[Description](#SSS-UpdateFunctionConfiguration-response-Description)": "string",
   "[Environment](#SSS-UpdateFunctionConfiguration-response-Environment)": { 
      "[Error](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Error)": { 
         "[ErrorCode](API_EnvironmentError.md#SSS-Type-EnvironmentError-ErrorCode)": "string",
         "[Message](API_EnvironmentError.md#SSS-Type-EnvironmentError-Message)": "string"
      },
      "[Variables](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Variables)": { 
         "string" : "string" 
      }
   },
   "[FunctionArn](#SSS-UpdateFunctionConfiguration-response-FunctionArn)": "string",
   "[FunctionName](#SSS-UpdateFunctionConfiguration-response-FunctionName)": "string",
   "[Handler](#SSS-UpdateFunctionConfiguration-response-Handler)": "string",
   "[KMSKeyArn](#SSS-UpdateFunctionConfiguration-response-KMSKeyArn)": "string",
   "[LastModified](#SSS-UpdateFunctionConfiguration-response-LastModified)": "string",
   "[MasterArn](#SSS-UpdateFunctionConfiguration-response-MasterArn)": "string",
   "[MemorySize](#SSS-UpdateFunctionConfiguration-response-MemorySize)": number,
   "[RevisionId](#SSS-UpdateFunctionConfiguration-response-RevisionId)": "string",
   "[Role](#SSS-UpdateFunctionConfiguration-response-Role)": "string",
   "[Runtime](#SSS-UpdateFunctionConfiguration-response-Runtime)": "string",
   "[Timeout](#SSS-UpdateFunctionConfiguration-response-Timeout)": number,
   "[TracingConfig](#SSS-UpdateFunctionConfiguration-response-TracingConfig)": { 
      "[Mode](API_TracingConfigResponse.md#SSS-Type-TracingConfigResponse-Mode)": "string"
   },
   "[Version](#SSS-UpdateFunctionConfiguration-response-Version)": "string",
   "[VpcConfig](#SSS-UpdateFunctionConfiguration-response-VpcConfig)": { 
      "[SecurityGroupIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SecurityGroupIds)": [ "string" ],
      "[SubnetIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SubnetIds)": [ "string" ],
      "[VpcId](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-VpcId)": "string"
   }
}
```

## Response Elements<a name="API_UpdateFunctionConfiguration_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSha256](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String

 ** [CodeSize](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-CodeSize"></a>
The size of the function's deployment package in bytes\.  
Type: Long

 ** [DeadLetterConfig](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Environment"></a>
The function's environment variables\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [FunctionArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-FunctionArn"></a>
The function's Amazon Resource Name\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Handler"></a>
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-KMSKeyArn"></a>
The KMS key used to encrypt the function's environment variables\. Only returned if you've configured a customer managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ssTZD\)\.  
Type: String

 ** [MasterArn](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-MasterArn"></a>
The ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-MemorySize"></a>
The memory allocated to the function  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | nodejs4.3-edge | go1.x` 

 ** [Timeout](#API_UpdateFunctionConfiguration_ResponseSyntax) **   <a name="SSS-UpdateFunctionConfiguration-response-Timeout"></a>
The amount of time that Lambda allows a function to run before terminating it\.  
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

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **PreconditionFailedException**   
The RevisionId provided does not match the latest RevisionId for the Lambda function or alias\. Call the `GetFunction` or the `GetAlias` API to retrieve the latest RevisionId for your resource\.  
HTTP Status Code: 412

 **ResourceConflictException**   
The resource already exists\.  
HTTP Status Code: 409

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
Request throughput limit exceeded  
HTTP Status Code: 429

## See Also<a name="API_UpdateFunctionConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateFunctionConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UpdateFunctionConfiguration) 