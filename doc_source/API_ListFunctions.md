# ListFunctions<a name="API_ListFunctions"></a>

Returns a list of Lambda functions, with the version\-specific configuration of each\. Lambda returns up to 50 functions per call\.

Set `FunctionVersion` to `ALL` to include all published versions of each function in addition to the unpublished version\. 

**Note**  
The `ListFunctions` action returns a subset of the [FunctionConfiguration](API_FunctionConfiguration.md) fields\. To get the additional fields \(State, StateReasonCode, StateReason, LastUpdateStatus, LastUpdateStatusReason, LastUpdateStatusReasonCode\) for a function or version, use [GetFunction](API_GetFunction.md)\.

## Request Syntax<a name="API_ListFunctions_RequestSyntax"></a>

```
GET /2015-03-31/functions/?FunctionVersion=FunctionVersion&Marker=Marker&MasterRegion=MasterRegion&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListFunctions_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionVersion](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-FunctionVersion"></a>
Set to `ALL` to include entries for all published versions of each function\.  
Valid Values:` ALL` 

 ** [Marker](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-Marker"></a>
Specify the pagination token that's returned by a previous request to retrieve the next page of results\.

 ** [MasterRegion](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-MasterRegion"></a>
For Lambda@Edge functions, the AWS Region of the master function\. For example, `us-east-1` filters the list of functions to only include Lambda@Edge functions replicated from a master function in US East \(N\. Virginia\)\. If specified, you must set `FunctionVersion` to `ALL`\.  
Pattern: `ALL|[a-z]{2}(-gov)?-[a-z]+-\d{1}` 

 ** [MaxItems](#API_ListFunctions_RequestSyntax) **   <a name="SSS-ListFunctions-request-MaxItems"></a>
The maximum number of functions to return in the response\. Note that `ListFunctions` returns a maximum of 50 items in each response, even if you set the number higher\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListFunctions_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListFunctions_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "Functions": [ 
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
   ],
   "NextMarker": "string"
}
```

## Response Elements<a name="API_ListFunctions_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Functions](#API_ListFunctions_ResponseSyntax) **   <a name="SSS-ListFunctions-response-Functions"></a>
A list of Lambda functions\.  
Type: Array of [FunctionConfiguration](API_FunctionConfiguration.md) objects

 ** [NextMarker](#API_ListFunctions_ResponseSyntax) **   <a name="SSS-ListFunctions-response-NextMarker"></a>
The pagination token that's included if more results are available\.  
Type: String

## Errors<a name="API_ListFunctions_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_ListFunctions_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListFunctions) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListFunctions) 