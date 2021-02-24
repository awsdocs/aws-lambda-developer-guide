# SourceAccessConfiguration<a name="API_SourceAccessConfiguration"></a>

 \(MQ\) The Secrets Manager secret that stores your broker credentials\. To store your secret, use the following format: ` { "username": "your username", "password": "your password" }` 

## Contents<a name="API_SourceAccessConfiguration_Contents"></a>

 **Type**   <a name="SSS-Type-SourceAccessConfiguration-Type"></a>
To reference the secret, use the following format: `[ { "Type": "BASIC_AUTH", "URI": "secretARN" } ]`   
The value of `Type` is always `BASIC_AUTH`\. To encrypt the secret, you can use customer or service managed keys\. When using a customer managed KMS key, the Lambda execution role requires `kms:Decrypt` permissions\.  
Type: String  
Valid Values:` BASIC_AUTH`   
Required: No

 **URI**   <a name="SSS-Type-SourceAccessConfiguration-URI"></a>
To reference the secret, use the following format: `[ { "Type": "BASIC_AUTH", "URI": "secretARN" } ]`   
The value of `Type` is always `BASIC_AUTH`\. To encrypt the secret, you can use customer or service managed keys\. When using a customer managed KMS key, the Lambda execution role requires `kms:Decrypt` permissions\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

## See Also<a name="API_SourceAccessConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/SourceAccessConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/SourceAccessConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/SourceAccessConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/SourceAccessConfiguration) 