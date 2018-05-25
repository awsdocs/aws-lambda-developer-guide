# GetFunction<a name="API_GetFunction"></a>

Returns the configuration information of the Lambda function and a presigned URL link to the \.zip file you uploaded with [CreateFunction](API_CreateFunction.md) so you can download the \.zip file\. Note that the URL is valid for up to 10 minutes\. The configuration information is the same information you provided as parameters when uploading the function\.

Using the optional `Qualifier` parameter, you can specify a specific function version for which you want this information\. If you don't specify this parameter, the API uses unqualified function ARN which return information about the `$LATEST` version of the Lambda function\. For more information, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\.

This operation requires permission for the `lambda:GetFunction` action\.

## Request Syntax<a name="API_GetFunction_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunction_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionName](#API_GetFunction_RequestSyntax) **   <a name="SSS-GetFunction-request-FunctionName"></a>
The Lambda function name\.  
 You can specify a function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. AWS Lambda also allows you to specify a partial ARN \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [Qualifier](#API_GetFunction_RequestSyntax) **   <a name="SSS-GetFunction-request-Qualifier"></a>
Use this optional parameter to specify a function version or an alias name\. If you specify function version, the API uses qualified function ARN for the request and returns information about the specific Lambda function version\. If you specify an alias name, the API uses the alias ARN and returns information about the function version to which the alias points\. If you don't provide this parameter, the API uses unqualified function ARN and returns information about the `$LATEST` version of the Lambda function\.   
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
The object for the Lambda function location\.  
Type: [FunctionCodeLocation](API_FunctionCodeLocation.md) object

 ** [Concurrency](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Concurrency"></a>
The concurrent execution limit set for this function\. For more information, see [Managing Concurrency](concurrent-executions.md)\.  
Type: [Concurrency](API_Concurrency.md) object

 ** [Configuration](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Configuration"></a>
A complex type that describes function metadata\.  
Type: [FunctionConfiguration](API_FunctionConfiguration.md) object

 ** [Tags](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Tags"></a>
Returns the list of tags associated with the function\. For more information, see [Tagging Lambda Functions](http://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\.  
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
   
HTTP Status Code: 429

## See Also<a name="API_GetFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/GetFunction) 