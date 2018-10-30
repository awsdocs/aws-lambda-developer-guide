# GetFunction<a name="API_GetFunction"></a>

Returns the configuration information of the Lambda function and a presigned URL link to the \.zip file you uploaded with [CreateFunction](API_CreateFunction.md) so you can download the \.zip file\. Note that the URL is valid for up to 10 minutes\. The configuration information is the same information you provided as parameters when uploading the function\.

Use the `Qualifier` parameter to retrieve a published version of the function\. Otherwise, returns the unpublished version \(`$LATEST`\)\. For more information, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\.

This operation requires permission for the `lambda:GetFunction` action\.

## Request Syntax<a name="API_GetFunction_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunction_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_GetFunction_RequestSyntax) **   <a name="SSS-GetFunction-request-FunctionName"></a>
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_GetFunction_RequestSyntax) **   <a name="SSS-GetFunction-request-Qualifier"></a>
Specify a version or alias to get details about a published version of the function\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_GetFunction_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetFunction_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[Code](#SSS-GetFunction-response-Code)": { 
      "[Location](API_FunctionCodeLocation.md#SSS-Type-FunctionCodeLocation-Location)": "string",
      "[RepositoryType](API_FunctionCodeLocation.md#SSS-Type-FunctionCodeLocation-RepositoryType)": "string"
   },
   "[Concurrency](#SSS-GetFunction-response-Concurrency)": { 
      "[ReservedConcurrentExecutions](API_Concurrency.md#SSS-Type-Concurrency-ReservedConcurrentExecutions)": number
   },
   "[Configuration](#SSS-GetFunction-response-Configuration)": { 
      "[CodeSha256](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-CodeSha256)": "string",
      "[CodeSize](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-CodeSize)": number,
      "[DeadLetterConfig](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-DeadLetterConfig)": { 
         "[TargetArn](API_DeadLetterConfig.md#SSS-Type-DeadLetterConfig-TargetArn)": "string"
      },
      "[Description](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Description)": "string",
      "[Environment](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Environment)": { 
         "[Error](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Error)": { 
            "[ErrorCode](API_EnvironmentError.md#SSS-Type-EnvironmentError-ErrorCode)": "string",
            "[Message](API_EnvironmentError.md#SSS-Type-EnvironmentError-Message)": "string"
         },
         "[Variables](API_EnvironmentResponse.md#SSS-Type-EnvironmentResponse-Variables)": { 
            "string" : "string" 
         }
      },
      "[FunctionArn](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-FunctionArn)": "string",
      "[FunctionName](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-FunctionName)": "string",
      "[Handler](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Handler)": "string",
      "[KMSKeyArn](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-KMSKeyArn)": "string",
      "[LastModified](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-LastModified)": "string",
      "[MasterArn](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-MasterArn)": "string",
      "[MemorySize](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-MemorySize)": number,
      "[RevisionId](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-RevisionId)": "string",
      "[Role](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Role)": "string",
      "[Runtime](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Runtime)": "string",
      "[Timeout](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Timeout)": number,
      "[TracingConfig](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-TracingConfig)": { 
         "[Mode](API_TracingConfigResponse.md#SSS-Type-TracingConfigResponse-Mode)": "string"
      },
      "[Version](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-Version)": "string",
      "[VpcConfig](API_FunctionConfiguration.md#SSS-Type-FunctionConfiguration-VpcConfig)": { 
         "[SecurityGroupIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SecurityGroupIds)": [ "string" ],
         "[SubnetIds](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-SubnetIds)": [ "string" ],
         "[VpcId](API_VpcConfigResponse.md#SSS-Type-VpcConfigResponse-VpcId)": "string"
      }
   },
   "[Tags](#SSS-GetFunction-response-Tags)": { 
      "string" : "string" 
   }
}
```

## Response Elements<a name="API_GetFunction_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Code](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Code"></a>
The function's code\.  
Type: [FunctionCodeLocation](API_FunctionCodeLocation.md) object

 ** [Concurrency](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Concurrency"></a>
The concurrent execution limit set for this function\. For more information, see [Managing Concurrency](https://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html)\.  
Type: [Concurrency](API_Concurrency.md) object

 ** [Configuration](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Configuration"></a>
The function's configuration\.  
Type: [FunctionConfiguration](API_FunctionConfiguration.md) object

 ** [Tags](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Tags"></a>
Returns the list of tags associated with the function\. For more information, see [Tagging Lambda Functions](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\.  
Type: String to string map

## Errors<a name="API_GetFunction_Errors"></a>

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
Request throughput limit exceeded  
HTTP Status Code: 429

## See Also<a name="API_GetFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetFunction) 