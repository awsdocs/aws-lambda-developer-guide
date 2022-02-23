# ImageConfig<a name="API_ImageConfig"></a>

Configuration values that override the container image Dockerfile settings\. See [Container settings](https://docs.aws.amazon.com/lambda/latest/dg/images-create.html#images-parms)\. 

## Contents<a name="API_ImageConfig_Contents"></a>

 ** Command **   <a name="SSS-Type-ImageConfig-Command"></a>
Specifies parameters that you want to pass in with ENTRYPOINT\.   
Type: Array of strings  
Array Members: Maximum number of 1500 items\.  
Required: No

 ** EntryPoint **   <a name="SSS-Type-ImageConfig-EntryPoint"></a>
Specifies the entry point to their application, which is typically the location of the runtime executable\.  
Type: Array of strings  
Array Members: Maximum number of 1500 items\.  
Required: No

 ** WorkingDirectory **   <a name="SSS-Type-ImageConfig-WorkingDirectory"></a>
Specifies the working directory\.  
Type: String  
Length Constraints: Maximum length of 1000\.  
Required: No

## See Also<a name="API_ImageConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ImageConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ImageConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ImageConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ImageConfig) 