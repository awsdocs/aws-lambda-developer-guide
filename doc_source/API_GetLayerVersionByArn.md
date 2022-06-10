# GetLayerVersionByArn<a name="API_GetLayerVersionByArn"></a>

Returns information about a version of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html), with a link to download the layer archive that's valid for 10 minutes\.

## Request Syntax<a name="API_GetLayerVersionByArn_RequestSyntax"></a>

```
GET /2018-10-31/layers?find=LayerVersion&Arn=Arn HTTP/1.1
```

## URI Request Parameters<a name="API_GetLayerVersionByArn_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [Arn](#API_GetLayerVersionByArn_RequestSyntax) **   <a name="SSS-GetLayerVersionByArn-request-Arn"></a>
The ARN of the layer version\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+:[0-9]+`   
Required: Yes

## Request Body<a name="API_GetLayerVersionByArn_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetLayerVersionByArn_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "CompatibleArchitectures": [ "string" ],
   "CompatibleRuntimes": [ "string" ],
   "Content": { 
      "CodeSha256": "string",
      "CodeSize": number,
      "Location": "string",
      "SigningJobArn": "string",
      "SigningProfileVersionArn": "string"
   },
   "CreatedDate": "string",
   "Description": "string",
   "LayerArn": "string",
   "LayerVersionArn": "string",
   "LicenseInfo": "string",
   "Version": number
}
```

## Response Elements<a name="API_GetLayerVersionByArn_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [CompatibleArchitectures](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-CompatibleArchitectures"></a>
A list of compatible [instruction set architectures](https://docs.aws.amazon.com/lambda/latest/dg/foundation-arch.html)\.  
Type: Array of strings  
Array Members: Maximum number of 2 items\.  
Valid Values:` x86_64 | arm64` 

 ** [CompatibleRuntimes](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-CompatibleRuntimes"></a>
The layer's compatible runtimes\.  
Type: Array of strings  
Array Members: Maximum number of 15 items\.  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | nodejs10.x | nodejs12.x | nodejs14.x | nodejs16.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | python3.9 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | dotnetcore3.1 | dotnet6 | nodejs4.3-edge | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2` 

 ** [Content](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-Content"></a>
Details about the layer version\.  
Type: [LayerVersionContentOutput](API_LayerVersionContentOutput.md) object

 ** [CreatedDate](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-CreatedDate"></a>
The date that the layer version was created, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [Description](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-Description"></a>
The description of the version\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [LayerArn](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-LayerArn"></a>
The ARN of the layer\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+` 

 ** [LayerVersionArn](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-LayerVersionArn"></a>
The ARN of the layer version\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+:[0-9]+` 

 ** [LicenseInfo](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-LicenseInfo"></a>
The layer's software license\.  
Type: String  
Length Constraints: Maximum length of 512\.

 ** [Version](#API_GetLayerVersionByArn_ResponseSyntax) **   <a name="SSS-GetLayerVersionByArn-response-Version"></a>
The version number\.  
Type: Long

## Errors<a name="API_GetLayerVersionByArn_Errors"></a>

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

## See Also<a name="API_GetLayerVersionByArn_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetLayerVersionByArn) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetLayerVersionByArn) 