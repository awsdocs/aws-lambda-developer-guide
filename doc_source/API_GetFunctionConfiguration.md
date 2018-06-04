# GetFunctionConfiguration<a name="API_GetFunctionConfiguration"></a>

Returns the configuration information of the Lambda function\. This the same information you provided as parameters when uploading the function by using [CreateFunction](API_CreateFunction.md)\.

If you are using the versioning feature, you can retrieve this information for a specific function version by using the optional `Qualifier` parameter and specifying the function version or alias that points to it\. If you don't provide it, the API returns information about the $LATEST version of the function\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\.

This operation requires permission for the `lambda:GetFunctionConfiguration` operation\.

## Request Syntax<a name="API_GetFunctionConfiguration_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/configuration?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunctionConfiguration_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_GetFunctionConfiguration_RequestSyntax) **   <a name="SSS-GetFunctionConfiguration-request-FunctionName"></a>
The name of the Lambda function for which you want to retrieve the configuration information\.  
 You can specify a function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. AWS Lambda also allows you to specify a partial ARN \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_GetFunctionConfiguration_RequestSyntax) **   <a name="SSS-GetFunctionConfiguration-request-Qualifier"></a>
Using this optional parameter you can specify a function version or an alias name\. If you specify function version, the API uses qualified function ARN and returns information about the specific function version\. If you specify an alias name, the API uses the alias ARN and returns information about the function version to which the alias points\.  
If you don't specify this parameter, the API uses unqualified function ARN, and returns information about the `$LATEST` function version\.  
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
It is the SHA256 hash of your function deployment package\.  
Type: String

 ** [CodeSize](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-CodeSize"></a>
The size, in bytes, of the function \.zip file you uploaded\.  
Type: Long

 ** [DeadLetterConfig](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-DeadLetterConfig"></a>
The parent object that contains the target ARN \(Amazon Resource Name\) of an Amazon SQS queue or Amazon SNS topic\. For more information, see [Dead Letter Queues](dlq.md)\.   
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** [Description](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Description"></a>
The user\-provided description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [Environment](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Environment"></a>
The parent object that contains your environment's configuration settings\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** [FunctionArn](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-FunctionArn"></a>
The Amazon Resource Name \(ARN\) assigned to the function\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionName](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-FunctionName"></a>
The name of the function\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Handler](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Handler"></a>
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** [KMSKeyArn](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-KMSKeyArn"></a>
The Amazon Resource Name \(ARN\) of the KMS key used to encrypt your function's environment variables\. If empty, it means you are using the AWS Lambda default service key\.  
Type: String  
Pattern: `(arn:aws:[a-z0-9-.]+:.*)|()` 

 ** [LastModified](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-LastModified"></a>
The time stamp of the last time you updated the function\. The time stamp is conveyed as a string complying with ISO\-8601 in this way YYYY\-MM\-DDThh:mm:ssTZD \(e\.g\., 1997\-07\-16T19:20:30\+01:00\)\. For more information, see [Date and Time Formats](https://www.w3.org/TR/NOTE-datetime)\.  
Type: String

 ** [MasterArn](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-MasterArn"></a>
Returns the ARN \(Amazon Resource Name\) of the master function\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [MemorySize](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-MemorySize"></a>
The memory size, in MB, you configured for the function\. Must be a multiple of 64 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** [RevisionId](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String

 ** [Role](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Role"></a>
The Amazon Resource Name \(ARN\) of the IAM role that Lambda assumes when it executes your function to access any other Amazon Web Services \(AWS\) resources\.  
Type: String  
Pattern: `arn:aws:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** [Runtime](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | go1.x` 

 ** [Timeout](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Timeout"></a>
The function execution time at which Lambda should terminate the function\. Because the execution time has cost implications, we recommend you set this value based on your expected execution time\. The default is 3 seconds\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** [TracingConfig](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-TracingConfig"></a>
The parent object that contains your function's tracing settings\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** [Version](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [VpcConfig](#API_GetFunctionConfiguration_ResponseSyntax) **   <a name="SSS-GetFunctionConfiguration-response-VpcConfig"></a>
VPC configuration associated with your Lambda function\.  
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
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetFunctionConfiguration) 