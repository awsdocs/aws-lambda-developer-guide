# CreateCodeSigningConfig<a name="API_CreateCodeSigningConfig"></a>

Creates a code signing configuration\. A [code signing configuration](https://docs.aws.amazon.com/lambda/latest/dg/configuration-codesigning.html) defines a list of allowed signing profiles and defines the code\-signing validation policy \(action to be taken if deployment validation checks fail\)\. 

## Request Syntax<a name="API_CreateCodeSigningConfig_RequestSyntax"></a>

```
POST /2020-04-22/code-signing-configs/ HTTP/1.1
Content-type: application/json

{
   "AllowedPublishers": { 
      "SigningProfileVersionArns": [ "string" ]
   },
   "CodeSigningPolicies": { 
      "UntrustedArtifactOnDeployment": "string"
   },
   "Description": "string"
}
```

## URI Request Parameters<a name="API_CreateCodeSigningConfig_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateCodeSigningConfig_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [AllowedPublishers](#API_CreateCodeSigningConfig_RequestSyntax) **   <a name="SSS-CreateCodeSigningConfig-request-AllowedPublishers"></a>
Signing profiles for this code signing configuration\.  
Type: [AllowedPublishers](API_AllowedPublishers.md) object  
Required: Yes

 ** [CodeSigningPolicies](#API_CreateCodeSigningConfig_RequestSyntax) **   <a name="SSS-CreateCodeSigningConfig-request-CodeSigningPolicies"></a>
The code signing policies define the actions to take if the validation checks fail\.   
Type: [CodeSigningPolicies](API_CodeSigningPolicies.md) object  
Required: No

 ** [Description](#API_CreateCodeSigningConfig_RequestSyntax) **   <a name="SSS-CreateCodeSigningConfig-request-Description"></a>
Descriptive name for this code signing configuration\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

## Response Syntax<a name="API_CreateCodeSigningConfig_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "CodeSigningConfig": { 
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
}
```

## Response Elements<a name="API_CreateCodeSigningConfig_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSigningConfig](#API_CreateCodeSigningConfig_ResponseSyntax) **   <a name="SSS-CreateCodeSigningConfig-response-CodeSigningConfig"></a>
The code signing configuration\.  
Type: [CodeSigningConfig](API_CodeSigningConfig.md) object

## Errors<a name="API_CreateCodeSigningConfig_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

## See Also<a name="API_CreateCodeSigningConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateCodeSigningConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CreateCodeSigningConfig) 