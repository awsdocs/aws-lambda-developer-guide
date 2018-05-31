# AliasConfiguration<a name="API_AliasConfiguration"></a>

Provides configuration information about a Lambda function version alias\.

## Contents<a name="API_AliasConfiguration_Contents"></a>

 **AliasArn**   <a name="SSS-Type-AliasConfiguration-AliasArn"></a>
Lambda function ARN that is qualified using the alias name as the suffix\. For example, if you create an alias called `BETA` that points to a helloworld function version, the ARN is `arn:aws:lambda:aws-regions:acct-id:function:helloworld:BETA`\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **Description**   <a name="SSS-Type-AliasConfiguration-Description"></a>
Alias description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 **FunctionVersion**   <a name="SSS-Type-AliasConfiguration-FunctionVersion"></a>
Function version to which the alias points\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: No

 **Name**   <a name="SSS-Type-AliasConfiguration-Name"></a>
Alias name\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(?!^[0-9]+$)([a-zA-Z0-9-_]+)`   
Required: No

 **RevisionId**   <a name="SSS-Type-AliasConfiguration-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String  
Required: No

 **RoutingConfig**   <a name="SSS-Type-AliasConfiguration-RoutingConfig"></a>
Specifies an additional function versions the alias points to, allowing you to dictate what percentage of traffic will invoke each version\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Type: [AliasRoutingConfiguration](API_AliasRoutingConfiguration.md) object  
Required: No

## See Also<a name="API_AliasConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AliasConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AliasConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/AliasConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/AliasConfiguration) 