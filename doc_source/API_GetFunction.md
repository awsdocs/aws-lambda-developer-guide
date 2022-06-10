# GetFunction<a name="API_GetFunction"></a>

Returns information about the function or function version, with a link to download the deployment package that's valid for 10 minutes\. If you specify a function version, only details that are specific to that version are returned\.

## Request Syntax<a name="API_GetFunction_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_GetFunction_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_GetFunction_RequestSyntax) **   <a name="SSS-GetFunction-request-FunctionName"></a>
The name of the Lambda function, version, or alias\.  

**Name formats**
+  **Function name** \- `my-function` \(name\-only\), `my-function:v1` \(with alias\)\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
You can append a version number or alias to any of the formats\. The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

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
   "Code": { 
      "ImageUri": "string",
      "Location": "string",
      "RepositoryType": "string",
      "ResolvedImageUri": "string"
   },
   "Concurrency": { 
      "ReservedConcurrentExecutions": number
   },
   "Configuration": { 
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
   },
   "Tags": { 
      "string" : "string" 
   }
}
```

## Response Elements<a name="API_GetFunction_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Code](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Code"></a>
The deployment package of the function or version\.  
Type: [FunctionCodeLocation](API_FunctionCodeLocation.md) object

 ** [Concurrency](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Concurrency"></a>
The function's [reserved concurrency](https://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html)\.  
Type: [Concurrency](API_Concurrency.md) object

 ** [Configuration](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Configuration"></a>
The configuration of the function or version\.  
Type: [FunctionConfiguration](API_FunctionConfiguration.md) object

 ** [Tags](#API_GetFunction_ResponseSyntax) **   <a name="SSS-GetFunction-response-Tags"></a>
The function's [tags](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html)\.  
Type: String to string map

## Errors<a name="API_GetFunction_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetFunction) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetFunction) 