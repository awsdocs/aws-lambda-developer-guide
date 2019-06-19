# PublishLayerVersion<a name="API_PublishLayerVersion"></a>

Creates an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html) from a ZIP archive\. Each time you call `PublishLayerVersion` with the same version name, a new version is created\.

Add layers to your function with [CreateFunction](API_CreateFunction.md) or [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\.

## Request Syntax<a name="API_PublishLayerVersion_RequestSyntax"></a>

```
POST /2018-10-31/layers/LayerName/versions HTTP/1.1
Content-type: application/json

{
   "[CompatibleRuntimes](#SSS-PublishLayerVersion-request-CompatibleRuntimes)": [ "string" ],
   "[Content](#SSS-PublishLayerVersion-request-Content)": { 
      "[S3Bucket](API_LayerVersionContentInput.md#SSS-Type-LayerVersionContentInput-S3Bucket)": "string",
      "[S3Key](API_LayerVersionContentInput.md#SSS-Type-LayerVersionContentInput-S3Key)": "string",
      "[S3ObjectVersion](API_LayerVersionContentInput.md#SSS-Type-LayerVersionContentInput-S3ObjectVersion)": "string",
      "[ZipFile](API_LayerVersionContentInput.md#SSS-Type-LayerVersionContentInput-ZipFile)": blob
   },
   "[Description](#SSS-PublishLayerVersion-request-Description)": "string",
   "[LicenseInfo](#SSS-PublishLayerVersion-request-LicenseInfo)": "string"
}
```

## URI Request Parameters<a name="API_PublishLayerVersion_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [LayerName](#API_PublishLayerVersion_RequestSyntax) **   <a name="SSS-PublishLayerVersion-request-LayerName"></a>
The name or Amazon Resource Name \(ARN\) of the layer\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+)|[a-zA-Z0-9-_]+` 

## Request Body<a name="API_PublishLayerVersion_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [CompatibleRuntimes](#API_PublishLayerVersion_RequestSyntax) **   <a name="SSS-PublishLayerVersion-request-CompatibleRuntimes"></a>
A list of compatible [function runtimes](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html)\. Used for filtering with [ListLayers](API_ListLayers.md) and [ListLayerVersions](API_ListLayerVersions.md)\.  
Type: Array of strings  
Array Members: Maximum number of 5 items\.  
Valid Values:` nodejs8.10 | nodejs10.x | java8 | python2.7 | python3.6 | python3.7 | dotnetcore1.0 | dotnetcore2.1 | go1.x | ruby2.5 | provided`   
Required: No

 ** [Content](#API_PublishLayerVersion_RequestSyntax) **   <a name="SSS-PublishLayerVersion-request-Content"></a>
The function layer archive\.  
Type: [LayerVersionContentInput](API_LayerVersionContentInput.md) object  
Required: Yes

 ** [Description](#API_PublishLayerVersion_RequestSyntax) **   <a name="SSS-PublishLayerVersion-request-Description"></a>
The description of the version\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** [LicenseInfo](#API_PublishLayerVersion_RequestSyntax) **   <a name="SSS-PublishLayerVersion-request-LicenseInfo"></a>
The layer's software license\. It can be any of the following:  
+ An [SPDX license identifier](https://spdx.org/licenses/)\. For example, `MIT`\.
+ The URL of a license hosted on the internet\. For example, `https://opensource.org/licenses/MIT`\.
+ The full text of the license\.
Type: String  
Length Constraints: Maximum length of 512\.  
Required: No

## Response Syntax<a name="API_PublishLayerVersion_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "[CompatibleRuntimes](#SSS-PublishLayerVersion-response-CompatibleRuntimes)": [ "string" ],
   "[Content](#SSS-PublishLayerVersion-response-Content)": { 
      "[CodeSha256](API_LayerVersionContentOutput.md#SSS-Type-LayerVersionContentOutput-CodeSha256)": "string",
      "[CodeSize](API_LayerVersionContentOutput.md#SSS-Type-LayerVersionContentOutput-CodeSize)": number,
      "[Location](API_LayerVersionContentOutput.md#SSS-Type-LayerVersionContentOutput-Location)": "string"
   },
   "[CreatedDate](#SSS-PublishLayerVersion-response-CreatedDate)": "string",
   "[Description](#SSS-PublishLayerVersion-response-Description)": "string",
   "[LayerArn](#SSS-PublishLayerVersion-response-LayerArn)": "string",
   "[LayerVersionArn](#SSS-PublishLayerVersion-response-LayerVersionArn)": "string",
   "[LicenseInfo](#SSS-PublishLayerVersion-response-LicenseInfo)": "string",
   "[Version](#SSS-PublishLayerVersion-response-Version)": number
}
```

## Response Elements<a name="API_PublishLayerVersion_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [CompatibleRuntimes](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-CompatibleRuntimes"></a>
The layer's compatible runtimes\.  
Type: Array of strings  
Array Members: Maximum number of 5 items\.  
Valid Values:` nodejs8.10 | nodejs10.x | java8 | python2.7 | python3.6 | python3.7 | dotnetcore1.0 | dotnetcore2.1 | go1.x | ruby2.5 | provided` 

 ** [Content](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-Content"></a>
Details about the layer version\.  
Type: [LayerVersionContentOutput](API_LayerVersionContentOutput.md) object

 ** [CreatedDate](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-CreatedDate"></a>
The date that the layer version was created, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ss\.sTZD\)\.  
Type: String

 ** [Description](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-Description"></a>
The description of the version\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** [LayerArn](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-LayerArn"></a>
The ARN of the layer\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+` 

 ** [LayerVersionArn](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-LayerVersionArn"></a>
The ARN of the layer version\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+:[0-9]+` 

 ** [LicenseInfo](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-LicenseInfo"></a>
The layer's software license\.  
Type: String  
Length Constraints: Maximum length of 512\.

 ** [Version](#API_PublishLayerVersion_ResponseSyntax) **   <a name="SSS-PublishLayerVersion-response-Version"></a>
The version number\.  
Type: Long

## Errors<a name="API_PublishLayerVersion_Errors"></a>

 **CodeStorageExceededException**   
You have exceeded your maximum total code size per account\. [Learn more](https://docs.aws.amazon.com/lambda/latest/dg/limits.html)   
HTTP Status Code: 400

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

## See Also<a name="API_PublishLayerVersion_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/PublishLayerVersion) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/PublishLayerVersion) 