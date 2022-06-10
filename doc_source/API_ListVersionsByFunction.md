# ListVersionsByFunction<a name="API_ListVersionsByFunction"></a>

Returns a list of [versions](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html), with the version\-specific configuration of each\. Lambda returns up to 50 versions per call\.

## Request Syntax<a name="API_ListVersionsByFunction_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/versions?Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListVersionsByFunction_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_ListVersionsByFunction_RequestSyntax) **   <a name="SSS-ListVersionsByFunction-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Marker](#API_ListVersionsByFunction_RequestSyntax) **   <a name="SSS-ListVersionsByFunction-request-Marker"></a>
Specify the pagination token that's returned by a previous request to retrieve the next page of results\.

 ** [MaxItems](#API_ListVersionsByFunction_RequestSyntax) **   <a name="SSS-ListVersionsByFunction-request-MaxItems"></a>
The maximum number of versions to return\. Note that `ListVersionsByFunction` returns a maximum of 50 items in each response, even if you set the number higher\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListVersionsByFunction_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListVersionsByFunction_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "NextMarker": "string",
   "Versions": [ 
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
   ]
}
```

## Response Elements<a name="API_ListVersionsByFunction_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [NextMarker](#API_ListVersionsByFunction_ResponseSyntax) **   <a name="SSS-ListVersionsByFunction-response-NextMarker"></a>
The pagination token that's included if more results are available\.  
Type: String

 ** [Versions](#API_ListVersionsByFunction_ResponseSyntax) **   <a name="SSS-ListVersionsByFunction-response-Versions"></a>
A list of Lambda function versions\.  
Type: Array of [FunctionConfiguration](API_FunctionConfiguration.md) objects

## Errors<a name="API_ListVersionsByFunction_Errors"></a>

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

## See Also<a name="API_ListVersionsByFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListVersionsByFunction) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListVersionsByFunction) 