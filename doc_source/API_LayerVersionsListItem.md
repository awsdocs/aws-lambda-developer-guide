# LayerVersionsListItem<a name="API_LayerVersionsListItem"></a>

Details about a version of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.

## Contents<a name="API_LayerVersionsListItem_Contents"></a>

 ** CompatibleArchitectures **   <a name="SSS-Type-LayerVersionsListItem-CompatibleArchitectures"></a>
A list of compatible [instruction set architectures](https://docs.aws.amazon.com/lambda/latest/dg/foundation-arch.html)\.  
Type: Array of strings  
Array Members: Maximum number of 2 items\.  
Valid Values:` x86_64 | arm64`   
Required: No

 ** CompatibleRuntimes **   <a name="SSS-Type-LayerVersionsListItem-CompatibleRuntimes"></a>
The layer's compatible runtimes\.  
Type: Array of strings  
Array Members: Maximum number of 15 items\.  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | nodejs10.x | nodejs12.x | nodejs14.x | nodejs16.x | java8 | java8.al2 | java11 | python2.7 | python3.6 | python3.7 | python3.8 | python3.9 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | dotnetcore3.1 | dotnet6 | nodejs4.3-edge | go1.x | ruby2.5 | ruby2.7 | provided | provided.al2`   
Required: No

 ** CreatedDate **   <a name="SSS-Type-LayerVersionsListItem-CreatedDate"></a>
The date that the version was created, in ISO 8601 format\. For example, `2018-11-27T15:10:45.123+0000`\.  
Type: String  
Required: No

 ** Description **   <a name="SSS-Type-LayerVersionsListItem-Description"></a>
The description of the version\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** LayerVersionArn **   <a name="SSS-Type-LayerVersionsListItem-LayerVersionArn"></a>
The ARN of the layer version\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+:[0-9]+`   
Required: No

 ** LicenseInfo **   <a name="SSS-Type-LayerVersionsListItem-LicenseInfo"></a>
The layer's open\-source license\.  
Type: String  
Length Constraints: Maximum length of 512\.  
Required: No

 ** Version **   <a name="SSS-Type-LayerVersionsListItem-Version"></a>
The version number\.  
Type: Long  
Required: No

## See Also<a name="API_LayerVersionsListItem_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/LayerVersionsListItem) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/LayerVersionsListItem) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/LayerVersionsListItem) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/LayerVersionsListItem) 