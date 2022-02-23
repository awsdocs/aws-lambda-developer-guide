# AliasConfiguration<a name="API_AliasConfiguration"></a>

Provides configuration information about a Lambda function [alias](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\.

## Contents<a name="API_AliasConfiguration_Contents"></a>

 ** AliasArn **   <a name="SSS-Type-AliasConfiguration-AliasArn"></a>
The Amazon Resource Name \(ARN\) of the alias\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 ** Description **   <a name="SSS-Type-AliasConfiguration-Description"></a>
A description of the alias\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** FunctionVersion **   <a name="SSS-Type-AliasConfiguration-FunctionVersion"></a>
The function version that the alias invokes\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: No

 ** Name **   <a name="SSS-Type-AliasConfiguration-Name"></a>
The name of the alias\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)`   
Required: No

 ** RevisionId **   <a name="SSS-Type-AliasConfiguration-RevisionId"></a>
A unique identifier that changes when you update the alias\.  
Type: String  
Required: No

 ** RoutingConfig **   <a name="SSS-Type-AliasConfiguration-RoutingConfig"></a>
The [routing configuration](https://docs.aws.amazon.com/lambda/latest/dg/lambda-traffic-shifting-using-aliases.html) of the alias\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object  
Required: No

## See Also<a name="API_AliasConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AliasConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AliasConfiguration) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/AliasConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/AliasConfiguration) 