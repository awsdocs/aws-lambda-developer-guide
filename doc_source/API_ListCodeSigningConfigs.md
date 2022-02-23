# ListCodeSigningConfigs<a name="API_ListCodeSigningConfigs"></a>

Returns a list of [code signing configurations](https://docs.aws.amazon.com/lambda/latest/dg/configuring-codesigning.html)\. A request returns up to 10,000 configurations per call\. You can use the `MaxItems` parameter to return fewer configurations per call\. 

## Request Syntax<a name="API_ListCodeSigningConfigs_RequestSyntax"></a>

```
GET /2020-04-22/code-signing-configs/?Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListCodeSigningConfigs_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [Marker](#API_ListCodeSigningConfigs_RequestSyntax) **   <a name="SSS-ListCodeSigningConfigs-request-Marker"></a>
Specify the pagination token that's returned by a previous request to retrieve the next page of results\.

 ** [MaxItems](#API_ListCodeSigningConfigs_RequestSyntax) **   <a name="SSS-ListCodeSigningConfigs-request-MaxItems"></a>
Maximum number of items to return\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListCodeSigningConfigs_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListCodeSigningConfigs_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "CodeSigningConfigs": [ 
      { 
         "AllowedPublishers": { 
            "SigningProfileVersionArns": [ "string" ]
         },
         "CodeSigningConfigArn": "string",
         "CodeSigningConfigId": "string",
         "CodeSigningPolicies": { 
            "UntrustedArtifactOnDeployment": "string"
         },
         "Description": "string",
         "LastModified": "string"
      }
   ],
   "NextMarker": "string"
}
```

## Response Elements<a name="API_ListCodeSigningConfigs_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSigningConfigs](#API_ListCodeSigningConfigs_ResponseSyntax) **   <a name="SSS-ListCodeSigningConfigs-response-CodeSigningConfigs"></a>
The code signing configurations  
Type: Array of [CodeSigningConfig](API_CodeSigningConfig.md) objects

 ** [NextMarker](#API_ListCodeSigningConfigs_ResponseSyntax) **   <a name="SSS-ListCodeSigningConfigs-response-NextMarker"></a>
The pagination token that's included if more results are available\.  
Type: String

## Errors<a name="API_ListCodeSigningConfigs_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

## See Also<a name="API_ListCodeSigningConfigs_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListCodeSigningConfigs) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ListCodeSigningConfigs) 