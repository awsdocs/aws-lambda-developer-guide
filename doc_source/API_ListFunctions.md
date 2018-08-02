# ListFunctions<a name="API_ListFunctions"></a>

Returns a list of your Lambda functions\. For each function, the response includes the function configuration information\. You must use [GetFunction](API_GetFunction.md) to retrieve the code for your function\.

This operation requires permission for the `lambda:ListFunctions` action\.

If you are using the versioning feature, you can list all of your functions or only `$LATEST` versions\. For information about the versioning feature, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

## Request Syntax<a name="API_ListFunctions_RequestSyntax"></a>

```
GET /2015-03-31/functions/?FunctionVersion=FunctionVersion&Marker=Marker&MasterRegion=MasterRegion&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListFunctions_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [FunctionVersion](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-FunctionVersion"></a>
Optional string\. If not specified, only the unqualified functions ARNs \(Amazon Resource Names\) will be returned\.  
Valid value:  
 `ALL`: Will return all versions, including `$LATEST` which will have fully qualified ARNs \(Amazon Resource Names\)\.  
Valid Values:` ALL` 

 ** [Marker](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-Marker"></a>
Optional string\. An opaque pagination token returned from a previous `ListFunctions` operation\. If present, indicates where to continue the listing\. 

 ** [MasterRegion](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-MasterRegion"></a>
Optional string\. If not specified, will return only regular function versions \(i\.e\., non\-replicated versions\)\.  
Valid values are:  
The region from which the functions are replicated\. For example, if you specify `us-east-1`, only functions replicated from that region will be returned\.  
 `ALL`: Will return all functions from any region\. If specified, you also must specify a valid FunctionVersion parameter\.  
Pattern: `ALL|[a-z]{2}(-gov)?-[a-z]+-\d{1}` 

 ** [MaxItems](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-MaxItems"></a>
Optional integer\. Specifies the maximum number of AWS Lambda functions to return in response\. This parameter value must be greater than 0\. The absolute maximum of AWS Lambda functions that can be returned is 50\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListFunctions_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListFunctions_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[Functions](#SSS-ListFunctions-response-Functions)": [ 
      { 
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
      }
   ],
   "[NextMarker](#SSS-ListFunctions-response-NextMarker)": "string"
}
```

## Response Elements<a name="API_ListFunctions_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Functions](#API_ListFunctions_ResponseSyntax) **   <a name="SSS-ListFunctions-response-Functions"></a>
A list of Lambda functions\.  
Type: Array of [FunctionConfiguration](API_FunctionConfiguration.md) objects

 ** [NextMarker](#API_ListFunctions_ResponseSyntax) **   <a name="SSS-ListFunctions-response-NextMarker"></a>
A string, present if there are more functions\.  
Type: String

## Errors<a name="API_ListFunctions_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
   
HTTP Status Code: 429

## See Also<a name="API_ListFunctions_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/ListFunctions) 