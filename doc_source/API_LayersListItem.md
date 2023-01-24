# LayersListItem<a name="API_LayersListItem"></a>

Details about an [AWS Lambda layer](https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html)\.

## Contents<a name="API_LayersListItem_Contents"></a>

 ** LatestMatchingVersion **   <a name="SSS-Type-LayersListItem-LatestMatchingVersion"></a>
The newest version of the layer\.  
Type: [LayerVersionsListItem](API_LayerVersionsListItem.md) object  
Required: No

 ** LayerArn **   <a name="SSS-Type-LayersListItem-LayerArn"></a>
The Amazon Resource Name \(ARN\) of the function layer\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+`   
Required: No

 ** LayerName **   <a name="SSS-Type-LayersListItem-LayerName"></a>
The name of the layer\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:[a-zA-Z0-9-]+:lambda:[a-zA-Z0-9-]+:\d{12}:layer:[a-zA-Z0-9-_]+)|[a-zA-Z0-9-_]+`   
Required: No

## See Also<a name="API_LayersListItem_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/LayersListItem) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/LayersListItem) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/LayersListItem) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/LayersListItem) 