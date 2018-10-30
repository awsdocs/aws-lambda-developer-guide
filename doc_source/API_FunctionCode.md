# FunctionCode<a name="API_FunctionCode"></a>

The code for the Lambda function\. You can specify either an S3 location, or upload a deployment package directly\.

## Contents<a name="API_FunctionCode_Contents"></a>

 **S3Bucket**   <a name="SSS-Type-FunctionCode-S3Bucket"></a>
An Amazon S3 bucket in the same region as your function\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 **S3Key**   <a name="SSS-Type-FunctionCode-S3Key"></a>
The Amazon S3 key of the deployment package\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 **S3ObjectVersion**   <a name="SSS-Type-FunctionCode-S3ObjectVersion"></a>
For versioned objects, the version of the deployment package object to use\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 **ZipFile**   <a name="SSS-Type-FunctionCode-ZipFile"></a>
The base64\-encoded contents of your zip file containing your deployment package\. AWS SDK and AWS CLI clients handle the encoding for you\.  
Type: Base64\-encoded binary data object  
Required: No

## See Also<a name="API_FunctionCode_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/FunctionCode) 