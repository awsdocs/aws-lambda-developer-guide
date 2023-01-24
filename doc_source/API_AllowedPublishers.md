# AllowedPublishers<a name="API_AllowedPublishers"></a>

List of signing profiles that can sign a code package\. 

## Contents<a name="API_AllowedPublishers_Contents"></a>

 ** SigningProfileVersionArns **   <a name="SSS-Type-AllowedPublishers-SigningProfileVersionArns"></a>
The Amazon Resource Name \(ARN\) for each of the signing profiles\. A signing profile defines a trusted user who can sign a code package\.   
Type: Array of strings  
Array Members: Minimum number of 1 item\. Maximum number of 20 items\.  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: Yes

## See Also<a name="API_AllowedPublishers_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AllowedPublishers) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AllowedPublishers) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/AllowedPublishers) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/AllowedPublishers) 