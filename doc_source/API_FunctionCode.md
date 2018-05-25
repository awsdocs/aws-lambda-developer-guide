# FunctionCode<a name="API_FunctionCode"></a>

The code for the Lambda function\.

## Contents<a name="API_FunctionCode_Contents"></a>

 **S3Bucket**   <a name="SSS-Type-FunctionCode-S3Bucket"></a>
Amazon S3 bucket name where the \.zip file containing your deployment package is stored\. This bucket must reside in the same AWS region where you are creating the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 3\. Maximum length of 63\.  
Pattern: `^[0-9A-Za-z\.\-_]*(?<!\.)$`   
Required: No

 **S3Key**   <a name="SSS-Type-FunctionCode-S3Key"></a>
The Amazon S3 object \(the deployment package\) key name you want to upload\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 **S3ObjectVersion**   <a name="SSS-Type-FunctionCode-S3ObjectVersion"></a>
The Amazon S3 object \(the deployment package\) version you want to upload\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Required: No

 **ZipFile**   <a name="SSS-Type-FunctionCode-ZipFile"></a>
The contents of your zip file containing your deployment package\. If you are using the web API directly, the contents of the zip file must be base64\-encoded\. If you are using the AWS SDKs or the AWS CLI, the SDKs or CLI will do the encoding for you\. For more information about creating a \.zip file, see [Execution Permissions](http://docs.aws.amazon.com/lambda/latest/dg/intro-permission-model.html#lambda-intro-execution-role.html) in the **AWS Lambda Developer Guide**\.   
Type: Base64\-encoded binary data object  
Required: No

## See Also<a name="API_FunctionCode_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/FunctionCode) 
+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/FunctionCode) 