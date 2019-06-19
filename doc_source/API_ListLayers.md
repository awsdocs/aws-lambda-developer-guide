# ListLayers<a name="API_ListLayers"></a>

Lists [AWS Lambda layers](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html) and shows information about the latest version of each\. Specify a [runtime identifier](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html) to list only layers that indicate that they're compatible with that runtime\.

## Request Syntax<a name="API_ListLayers_RequestSyntax"></a>

```
GET /2018-10-31/layers?CompatibleRuntime=CompatibleRuntime&Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListLayers_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [CompatibleRuntime](#API_ListLayers_RequestSyntax) **   <a name="SSS-ListLayers-request-CompatibleRuntime"></a>
A runtime identifier\. For example, `go1.x`\.  
Valid Values:` nodejs8.10 | nodejs10.x | java8 | python2.7 | python3.6 | python3.7 | dotnetcore1.0 | dotnetcore2.1 | go1.x | ruby2.5 | provided` 

 ** [Marker](#API_ListLayers_RequestSyntax) **   <a name="SSS-ListLayers-request-Marker"></a>
A pagination token returned by a previous call\.

 ** [MaxItems](#API_ListLayers_RequestSyntax) **   <a name="SSS-ListLayers-request-MaxItems"></a>
The maximum number of layers to return\.  
Valid Range: Minimum value of 1\. Maximum value of 50\.

## Request Body<a name="API_ListLayers_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListLayers_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "[Layers](#SSS-ListLayers-response-Layers)": [ 
      { 
         "[LatestMatchingVersion](API_LayersListItem.md#SSS-Type-LayersListItem-LatestMatchingVersion)": { 
            "[CompatibleRuntimes](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-CompatibleRuntimes)": [ "string" ],
            "[CreatedDate](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-CreatedDate)": "string",
            "[Description](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-Description)": "string",
            "[LayerVersionArn](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-LayerVersionArn)": "string",
            "[LicenseInfo](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-LicenseInfo)": "string",
            "[Version](API_LayerVersionsListItem.md#SSS-Type-LayerVersionsListItem-Version)": number
         },
         "[LayerArn](API_LayersListItem.md#SSS-Type-LayersListItem-LayerArn)": "string",
         "[LayerName](API_LayersListItem.md#SSS-Type-LayersListItem-LayerName)": "string"
      }
   ],
   "[NextMarker](#SSS-ListLayers-response-NextMarker)": "string"
}
```

## Response Elements<a name="API_ListLayers_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [Layers](#API_ListLayers_ResponseSyntax) **   <a name="SSS-ListLayers-response-Layers"></a>
A list of function layers\.  
Type: Array of [LayersListItem](API_LayersListItem.md) objects

 ** [NextMarker](#API_ListLayers_ResponseSyntax) **   <a name="SSS-ListLayers-response-NextMarker"></a>
A pagination token returned when the response doesn't contain all layers\.  
Type: String

## Errors<a name="API_ListLayers_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
Request throughput limit exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_ListLayers_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListLayers) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/ListLayers) 