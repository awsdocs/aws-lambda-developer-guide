# FunctionCodeLocation<a name="API_FunctionCodeLocation"></a>

Details about a function's deployment package\.

## Contents<a name="API_FunctionCodeLocation_Contents"></a>

 ** ImageUri **   <a name="SSS-Type-FunctionCodeLocation-ImageUri"></a>
URI of a container image in the Amazon ECR registry\.  
Type: String  
Required: No

 ** Location **   <a name="SSS-Type-FunctionCodeLocation-Location"></a>
A presigned URL that you can use to download the deployment package\.  
Type: String  
Required: No

 ** RepositoryType **   <a name="SSS-Type-FunctionCodeLocation-RepositoryType"></a>
The service that's hosting the file\.  
Type: String  
Required: No

 ** ResolvedImageUri **   <a name="SSS-Type-FunctionCodeLocation-ResolvedImageUri"></a>
The resolved URI for the image\.  
Type: String  
Required: No

## See Also<a name="API_FunctionCodeLocation_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionCodeLocation) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionCodeLocation) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/FunctionCodeLocation) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/FunctionCodeLocation) 