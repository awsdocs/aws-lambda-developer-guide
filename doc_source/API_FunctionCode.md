# FunctionCode<a name="API_FunctionCode"></a>

The code for the Lambda function\. You can specify either an object in Amazon S3, upload a \.zip file archive deployment package directly, or specify the URI of a container image\.

## Contents<a name="API_FunctionCode_Contents"></a>

 ** ImageUri **   <a name="SSS-Type-FunctionCode-ImageUri"></a>
URI of a [container image](https://docs.aws.amazon.com/lambda/latest/dg/lambda-images.html) in the Amazon ECR registry\.  
Type: String  
Required: No

 ** S3Bucket **   <a name="SSS-Type-FunctionCode-S3Bucket"></a>
An Amazon S3 bucket in the same AWS Region as your function\. The bucket can be in a different AWS account\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 ** S3Key **   <a name="SSS-Type-FunctionCode-S3Key"></a>
The Amazon S3 key of the deployment package\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** S3ObjectVersion **   <a name="SSS-Type-FunctionCode-S3ObjectVersion"></a>
For versioned objects, the version of the deployment package object to use\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 ** ZipFile **   <a name="SSS-Type-FunctionCode-ZipFile"></a>
The base64\-encoded contents of the deployment package\. AWS SDK and AWS CLI clients handle the encoding for you\.  
Type: Base64\-encoded binary data object  
Required: No

## See Also<a name="API_FunctionCode_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/FunctionCode) 