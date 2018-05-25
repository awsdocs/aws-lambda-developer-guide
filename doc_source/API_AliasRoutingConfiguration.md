# AliasRoutingConfiguration<a name="API_AliasRoutingConfiguration"></a>

The parent object that implements what percentage of traffic will invoke each function version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.

## Contents<a name="API_AliasRoutingConfiguration_Contents"></a>

 **AdditionalVersionWeights**   <a name="SSS-Type-AliasRoutingConfiguration-AdditionalVersionWeights"></a>
Set this value to dictate what percentage of traffic will invoke the updated function version\. If set to an empty string, 100 percent of traffic will invoke `function-version`\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
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