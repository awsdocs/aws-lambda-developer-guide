# CodeSigningPolicies<a name="API_CodeSigningPolicies"></a>

Code signing configuration [policies](https://docs.aws.amazon.com/lambda/latest/dg/configuration-codesigning.html#config-codesigning-policies) specify the validation failure action for signature mismatch or expiry\.

## Contents<a name="API_CodeSigningPolicies_Contents"></a>

 ** UntrustedArtifactOnDeployment **   <a name="SSS-Type-CodeSigningPolicies-UntrustedArtifactOnDeployment"></a>
Code signing configuration policy for deployment validation failure\. If you set the policy to `Enforce`, Lambda blocks the deployment request if signature validation checks fail\. If you set the policy to `Warn`, Lambda allows the deployment and creates a CloudWatch log\.   
Default value: `Warn`   
Type: String  
Valid Values:` Warn | Enforce`   
Required: No

## See Also<a name="API_CodeSigningPolicies_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CodeSigningPolicies) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CodeSigningPolicies) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/CodeSigningPolicies) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CodeSigningPolicies) 