# ListLayerVersions<a name="API_ListLayerVersions"></a>

Lists the versions of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\. Versions that have been deleted aren't listed\. Specify a [runtime identifier](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html) to list only versions that indicate that they're compatible with that runtime\.

## Request Syntax<a name="API_ListLayerVersions_RequestSyntax"></a>

```
GET /2018-10-31/layers/LayerName/versions?CompatibleRuntime=CompatibleRuntime&Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListLayerVersions_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [CompatibleRuntime](#API_ListLayerVersions_RequestSyntax) **   <a name="SSS-ListLayerVersions-request-CompatibleRuntime"></a>
A runtime identifier\. For example, `go1.x`\.  
Valid Values:` nodejs8.10 | nodejs10.x | java8 | python2.7 | python3.6 | python3.7 | dotnetcore1.0 | dotnetcore2.1 | go1.x | ruby2.5 | provided` 

 ** [LayerName](#API_ListLayerVersions_RequestSyntax) **   <a name="SSS-ListLayerVersions-request-LayerName"></a>
The name or Amazon Resource Name \(ARN\) of the layer\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+)|[a-zA-Z0-9-_]+` 

 ** [Marker](#API_ListLayerVersions_RequestSyntax) **   <a name="SSS-ListLayerVersions-request-Marker"></a>
A pagination token returned by a previous call\.

 ** [MaxItems](#API_ListLayerVersions_RequestSyntax) **   <a name="SSS-ListLayerVersions-request-MaxItems"></a>
The maximum number of versions to return\.  
Valid Range: Minimum value of 1\. Maximum value of 50\.

## Request Body<a name="API_ListLayerVersions_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListLayerVersions_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[LayerVersions](#SSS-ListLayerVersions-response-LayerVersions)": [ 
      { 
         "[CompatibleRuntimes](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-CompatibleRuntimes)": [ "string" ],
         "[CreatedDate](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-CreatedDate)": "string",
         "[Description](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-Description)": "string",
         "[LayerVersionArn](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-LayerVersionArn)": "string",
         "[LicenseInfo](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-LicenseInfo)": "string",
         "[Version](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-Version)": number
      }
   ],
   "[NextMarker](#SSS-ListLayerVersions-response-NextMarker)": "string"
}
```

## Response Elements<a name="API_ListLayerVersions_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [LayerVersions](#API_ListLayerVersions_ResponseSyntax) **   <a name="SSS-ListLayerVersions-response-LayerVersions"></a>
A list of versions\.  
Type: Array of [LayerVersionsListItem](API_LayerVersionsListItem.md) objects

 ** [NextMarker](#API_ListLayerVersions_ResponseSyntax) **   <a name="SSS-ListLayerVersions-response-NextMarker"></a>
A pagination token returned when the response doesn't contain all versions\.  
Type: String

## Errors<a name="API_ListLayerVersions_Errors"></a>

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
Request throughput limit exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_ListLayerVersions_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListLayerVersions) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/ListLayerVersions) 