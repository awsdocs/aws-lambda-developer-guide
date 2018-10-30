# AliasRoutingConfiguration<a name="API_AliasRoutingConfiguration"></a>

The alias's [traffic shifting](https://docs.aws.amazon.com/lambda/latest/dg/lambda-traffic-shifting-using-aliases.html) configuration\.

## Contents<a name="API_AliasRoutingConfiguration_Contents"></a>

 **AdditionalVersionWeights**   <a name="SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights"></a>
The name of the second alias, and the percentage of traffic that is routed to it\.  
Type: String to double map  
Key Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Key Pattern: `[0-9]+`   
Valid Range: Minimum value of 0\.0\. Maximum value of 1\.0\.  
Required: No

## See Also<a name="API_AliasRoutingConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AliasRoutingConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AliasRoutingConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/AliasRoutingConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/AliasRoutingConfiguration) 