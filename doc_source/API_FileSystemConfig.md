# FileSystemConfig<a name="API_FileSystemConfig"></a>

Details about the connection between a Lambda function and an [Amazon EFS file system](https://docs.aws.amazon.com/lambda/latest/dg/configuration-filesystem.html)\.

## Contents<a name="API_FileSystemConfig_Contents"></a>

 ** Arn **   <a name="SSS-Type-FileSystemConfig-Arn"></a>
The Amazon Resource Name \(ARN\) of the Amazon EFS access point that provides access to the file system\.  
Type: String  
Length Constraints: Maximum length of 200\.  
Pattern: `arn:aws[a-zA-Z-]*:elasticfilesystem:[a-z]{2}((-gov)|(-iso(b?)))?-[a-z]+-\d{1}:\d{12}:access-point/fsap-[a-f0-9]{17}`   
Required: Yes

 ** LocalMountPath **   <a name="SSS-Type-FileSystemConfig-LocalMountPath"></a>
The path where the function can access the file system, starting with `/mnt/`\.  
Type: String  
Length Constraints: Maximum length of 160\.  
Pattern: `^/mnt/[a-zA-Z0-9-_.]+$`   
Required: Yes

## See Also<a name="API_FileSystemConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FileSystemConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FileSystemConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/FileSystemConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/FileSystemConfig) 