# PublishVersion<a name="API_PublishVersion"></a>

Publishes a version of your function from the current snapshot of $LATEST\. That is, AWS Lambda takes a snapshot of the function code and configuration information from $LATEST and publishes a new version\. The code and configuration cannot be modified after publication\. For information about the versioning feature, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

## Request Syntax<a name="API_PublishVersion_RequestSyntax"></a>

```
POST /2015-03-31/functions/FunctionName/versions HTTP/1.1
Content-type: application/json

{
   "[CodeSha256](#SSS-PublishVersion-request-CodeSha256)": "string",
   "[Description](#SSS-PublishVersion-request-Description)": "string",
   "[RevisionId](#SSS-PublishVersion-request-RevisionId)": "string"
}
```

## URI Request Parameters<a name="API_PublishVersion_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_PublishVersion_RequestSyntax) **   <a name="SSS-PublishVersion-request-FunctionName"></a>
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_PublishVersion_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [CodeSha256](#API_PublishVersion_RequestSyntax) **   <a name="SSS-PublishVersion-request-CodeSha256"></a>
The SHA256 hash of the deployment package you want to publish\. This provides validation on the code you are publishing\. If you provide this parameter, the value must match the SHA256 of the $LATEST version for the publication to succeed\. You can use the **DryRun** parameter of [UpdateFunctionCode](API_UpdateFunctionCode.md) to verify the hash value that will be returned before publishing your new version\.  
Type: String  
Required: No

 ** [Description](#API_PublishVersion_RequestSyntax) **   <a name="SSS-PublishVersion-request-Description"></a>
The description for the version you are publishing\. If not provided, AWS Lambda copies the description from the $LATEST version\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [RevisionId](#API_PublishVersion_RequestSyntax) **   <a name="SSS-PublishVersion-request-RevisionId"></a>
An optional value you can use to ensure you are updating the latest update of the function version or alias\. If the `RevisionID` you pass doesn't match the latest `RevisionId` of the function or alias, it will fail with an error message, advising you retrieve the latest function version or alias `RevisionID` using either [GetFunction](API_GetFunction.md) or [GetAlias](API_GetAlias.md)\.  
Type: String  
Required: No

## Response Syntax<a name="API_PublishVersion_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "[CodeSha256](#SSS-PublishVersion-response-CodeSha256)": "string",
   "[CodeSize](#SSS-PublishVersion-response-CodeSize)": number,
   "[DeadLetterConfig](#SSS-PublishVersion-response-DeadLetterConfig)": { 
      "[TargetArn](API_DeadLetterConfig.md#SSS-Type-DeadLetterConfig-TargetArn)": "string"
   },
   "[Description](#SSS-PublishVersion-response-Description)": "string",
   "[Environment](#SSS-PublishVersion-response-Environment)": { 
      "[Error](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Error)": { 
         "[ErrorCode](API_EnvironmentError.md#SSS-Type-EnvironmentError-ErrorCode)": "string",
         "[Message](API_EnvironmentError.md#SSS-Type-EnvironmentError-Message)": "string"
      },
      "[Variables](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Variables)": { 
         "string" : "string" 
      }
   },
   "[FunctionArn](#SSS-PublishVersion-response-FunctionArn)": "string",
   "[FunctionName](#SSS-PublishVersion-response-FunctionName)": "string",
   "[Handler](#SSS-PublishVersion-response-Handler)": "string",
   "[KMSKeyArn](#SSS-PublishVersion-response-KMSKeyArn)": "string",
   "[LastModified](#SSS-PublishVersion-response-LastModified)": "string",
   "[MasterArn](#SSS-PublishVersion-response-MasterArn)": "string",
   "[MemorySize](#SSS-PublishVersion-response-MemorySize)": number,
   "[RevisionId](#SSS-PublishVersion-response-RevisionId)": "string",
   "[Role](#SSS-PublishVersion-response-Role)": "string",
   "[Runtime](#SSS-PublishVersion-response-Runtime)": "string",
   "[Timeout](#SSS-PublishVersion-response-Timeout)": number,
   "[TracingConfig](#SSS-PublishVersion-response-TracingConfig)": { 
      "[Mode](API_TracingConfigResponse.md#SSS-Type-TracingConfigResponse-Mode)": "string"
   },
   "[Version](#SSS-PublishVersion-response-Version)": "string",
   "[VpcConfig](#SSS-PublishVersion-response-VpcConfig)": { 
      "[SecurityGroupIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SecurityGroupIds)": [ "string" ],
      "[SubnetIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SubnetIds)": [ "string" ],
      "[VpcId](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-VpcId)": "string"
   }
}
```

## Response Elements<a name="API_PublishVersion_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSha256](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String

 ** [CodeSize](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-CodeSize"></a>
The size of the function's deployment package in bytes\.  
Type: Long

 ** [DeadLetterConfig](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Environment"></a>
The function's environment variables\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [FunctionArn](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-FunctionArn"></a>
The function's Amazon Resource Name\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Handler"></a>
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-KMSKeyArn"></a>
The KMS key used to encrypt the function's environment variables\. Only returned if you've configured a customer managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ssTZD\)\.  
Type: String

 ** [MasterArn](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-MasterArn"></a>
The ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-MemorySize"></a>
The memory allocated to the function  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | nodejs4.3-edge | go1.x` 

 ** [Timeout](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Timeout"></a>
The amount of time that Lambda allows a function to run before terminating it\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [TracingConfig](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-TracingConfig"></a>
The function's AWS X\-Ray tracing configuration\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** [Version](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [VpcConfig](#API_PublishVersion_ResponseSyntax) **   <a name="SSS-PublishVersion-response-VpcConfig"></a>
The function's networking configuration\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object

## Errors<a name="API_PublishVersion_Errors"></a>

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

## See Also<a name="API_PublishVersion_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/PublishVersion) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/PublishVersion) 