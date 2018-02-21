# VpcConfig<a name="API_VpcConfig"></a>

If your Lambda function accesses resources in a VPC, you provide this parameter identifying the list of security group IDs and subnet IDs\. These must belong to the same VPC\. You must provide at least one security group and one subnet ID\.

## Contents<a name="API_VpcConfig_Contents"></a>

 **SecurityGroupIds**   <a name="SSS-Type-VpcConfig-SecurityGroupIds"></a>
A list of one or more security groups IDs in your VPC\.  
Type: Array of strings  
Array Members: Maximum number of 5 items\.  
Required: No

 **SubnetIds**   <a name="SSS-Type-VpcConfig-SubnetIds"></a>
A list of one or more subnet IDs in your VPC\.  
Type: Array of strings  
Array Members: Maximum number of 16 items\.  
Required: No

## See Also<a name="API_VpcConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/VpcConfig) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/VpcConfig) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/VpcConfig) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/VpcConfig) 