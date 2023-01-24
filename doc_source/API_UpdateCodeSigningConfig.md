# UpdateCodeSigningConfig<a name="API_UpdateCodeSigningConfig"></a>

Update the code signing configuration\. Changes to the code signing configuration take effect the next time a user tries to deploy a code package to the function\. 

## Request Syntax<a name="API_UpdateCodeSigningConfig_RequestSyntax"></a>

```
PUT /2020-04-22/code-signing-configs/CodeSigningConfigArn HTTP/1.1
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

## URI Request Parameters<a name="API_UpdateCodeSigningConfig_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [CodeSigningConfigArn](#API_UpdateCodeSigningConfig_RequestSyntax) **   <a name="SSS-UpdateCodeSigningConfig-request-CodeSigningConfigArn"></a>
The The Amazon Resource Name \(ARN\) of the code signing configuration\.  
Length Constraints: Maximum length of 200\.  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}((-gov)|(-iso(b?)))?-[a-z]+-\d{1}:\d{12}:code-signing-config:csc-[a-z0-9]{17}`   
Required: Yes

## Request Body<a name="API_UpdateCodeSigningConfig_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [AllowedPublishers](#API_UpdateCodeSigningConfig_RequestSyntax) **   <a name="SSS-UpdateCodeSigningConfig-request-AllowedPublishers"></a>
Signing profiles for this code signing configuration\.  
Type: [AllowedPublishers](API_AllowedPublishers.md) object  
Required: No

 ** [CodeSigningPolicies](#API_UpdateCodeSigningConfig_RequestSyntax) **   <a name="SSS-UpdateCodeSigningConfig-request-CodeSigningPolicies"></a>
The code signing policy\.  
Type: [CodeSigningPolicies](API_CodeSigningPolicies.md) object  
Required: No

 ** [Description](#API_UpdateCodeSigningConfig_RequestSyntax) **   <a name="SSS-UpdateCodeSigningConfig-request-Description"></a>
Descriptive name for this code signing configuration\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

## Response Syntax<a name="API_UpdateCodeSigningConfig_ResponseSyntax"></a>

```
HTTP/1.1 200
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

## Response Elements<a name="API_UpdateCodeSigningConfig_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [CodeSigningConfig](#API_UpdateCodeSigningConfig_ResponseSyntax) **   <a name="SSS-UpdateCodeSigningConfig-response-CodeSigningConfig"></a>
The code signing configuration  
Type: [CodeSigningConfig](API_CodeSigningConfig.md) object

## Errors<a name="API_UpdateCodeSigningConfig_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

## See Also<a name="API_UpdateCodeSigningConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateCodeSigningConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/UpdateCodeSigningConfig) 