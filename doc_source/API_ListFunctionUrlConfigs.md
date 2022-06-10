# ListFunctionUrlConfigs<a name="API_ListFunctionUrlConfigs"></a>

Returns a list of Lambda function URLs for the specified function\.

## Request Syntax<a name="API_ListFunctionUrlConfigs_RequestSyntax"></a>

```
GET /2021-10-31/functions/FunctionName/urls?Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListFunctionUrlConfigs_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [FunctionName](#API_ListFunctionUrlConfigs_RequestSyntax) **   <a name="SSS-ListFunctionUrlConfigs-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `my-function`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:my-function`\.
+  **Partial ARN** \- `123456789012:function:my-function`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [Marker](#API_ListFunctionUrlConfigs_RequestSyntax) **   <a name="SSS-ListFunctionUrlConfigs-request-Marker"></a>
Specify the pagination token that's returned by a previous request to retrieve the next page of results\.

 ** [MaxItems](#API_ListFunctionUrlConfigs_RequestSyntax) **   <a name="SSS-ListFunctionUrlConfigs-request-MaxItems"></a>
The maximum number of function URLs to return in the response\. Note that `ListFunctionUrlConfigs` returns a maximum of 50 items in each response, even if you set the number higher\.  
Valid Range: Minimum value of 1\. Maximum value of 50\.

## Request Body<a name="API_ListFunctionUrlConfigs_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListFunctionUrlConfigs_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "FunctionUrlConfigs": [ 
      { 
         "AuthType": "string",
         "Cors": { 
            "AllowCredentials": boolean,
            "AllowHeaders": [ "string" ],
            "AllowMethods": [ "string" ],
            "AllowOrigins": [ "string" ],
            "ExposeHeaders": [ "string" ],
            "MaxAge": number
         },
         "CreationTime": "string",
         "FunctionArn": "string",
         "FunctionUrl": "string",
         "LastModifiedTime": "string"
      }
   ],
   "NextMarker": "string"
}
```

## Response Elements<a name="API_ListFunctionUrlConfigs_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [FunctionUrlConfigs](#API_ListFunctionUrlConfigs_ResponseSyntax) **   <a name="SSS-ListFunctionUrlConfigs-response-FunctionUrlConfigs"></a>
A list of function URL configurations\.  
Type: Array of [FunctionUrlConfig](API_FunctionUrlConfig.md) objects

 ** [NextMarker](#API_ListFunctionUrlConfigs_ResponseSyntax) **   <a name="SSS-ListFunctionUrlConfigs-response-NextMarker"></a>
The pagination token that's included if more results are available\.  
Type: String

## Errors<a name="API_ListFunctionUrlConfigs_Errors"></a>

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

## See Also<a name="API_ListFunctionUrlConfigs_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListFunctionUrlConfigs) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListFunctionUrlConfigs) 