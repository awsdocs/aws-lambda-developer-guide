# EnvironmentResponse<a name="API_EnvironmentResponse"></a>

The results of an operation to update or read environment variables\. If the operation is successful, the response contains the environment variables\. If it failed, the response contains details about the error\.

## Contents<a name="API_EnvironmentResponse_Contents"></a>

 ** Error **   <a name="SSS-Type-EnvironmentResponse-Error"></a>
Error messages for environment variables that couldn't be applied\.  
Type: [EnvironmentError](API_EnvironmentError.md) object  
Required: No

 ** Variables **   <a name="SSS-Type-EnvironmentResponse-Variables"></a>
Environment variable key\-value pairs\. Omitted from AWS CloudTrail logs\.  
Type: String to string map  
Key Pattern: `[a-zA-Z]([a-zA-Z0-9_])+`   
Required: No

## See Also<a name="API_EnvironmentResponse_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/EnvironmentResponse) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/EnvironmentResponse) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/EnvironmentResponse) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/EnvironmentResponse) 