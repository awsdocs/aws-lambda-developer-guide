# LayerVersionContentInput<a name="API_LayerVersionContentInput"></a>

A ZIP archive that contains the contents of an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\. You can specify either an Amazon S3 location, or upload a layer archive directly\.

## Contents<a name="API_LayerVersionContentInput_Contents"></a>

 ** S3Bucket **   <a name="SSS-Type-LayerVersionContentInput-S3Bucket"></a>
The Amazon S3 bucket of the layer archive\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 ** S3Key **   <a name="SSS-Type-LayerVersionContentInput-S3Key"></a>
The Amazon S3 key of the layer archive\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** S3ObjectVersion **   <a name="SSS-Type-LayerVersionContentInput-S3ObjectVersion"></a>
For versioned objects, the version of the layer archive object to use\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** ZipFile **   <a name="SSS-Type-LayerVersionContentInput-ZipFile"></a>
The base64\-encoded contents of the layer archive\. AWS SDK and AWS CLI clients handle the encoding for you\.  
Type: Base64\-encoded binary data object  
Required: No

## See Also<a name="API_LayerVersionContentInput_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/LayerVersionContentInput) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/LayerVersionContentInput) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/LayerVersionContentInput) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/LayerVersionContentInput) 