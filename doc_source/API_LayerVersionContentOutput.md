# LayerVersionContentOutput<a name="API_LayerVersionContentOutput"></a>

Details about a version of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.

## Contents<a name="API_LayerVersionContentOutput_Contents"></a>

 ** CodeSha256 **   <a name="SSS-Type-LayerVersionContentOutput-CodeSha256"></a>
The SHA\-256 hash of the layer archive\.  
Type: String  
Required: No

 ** CodeSize **   <a name="SSS-Type-LayerVersionContentOutput-CodeSize"></a>
The size of the layer archive in bytes\.  
Type: Long  
Required: No

 ** Location **   <a name="SSS-Type-LayerVersionContentOutput-Location"></a>
A link to the layer archive in Amazon S3 that is valid for 10 minutes\.  
Type: String  
Required: No

 ** SigningJobArn **   <a name="SSS-Type-LayerVersionContentOutput-SigningJobArn"></a>
The Amazon Resource Name \(ARN\) of a signing job\.  
Type: String  
Required: No

 ** SigningProfileVersionArn **   <a name="SSS-Type-LayerVersionContentOutput-SigningProfileVersionArn"></a>
The Amazon Resource Name \(ARN\) for a signing profile version\.  
Type: String  
Required: No

## See Also<a name="API_LayerVersionContentOutput_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/LayerVersionContentOutput) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/LayerVersionContentOutput) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/LayerVersionContentOutput) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/LayerVersionContentOutput) 