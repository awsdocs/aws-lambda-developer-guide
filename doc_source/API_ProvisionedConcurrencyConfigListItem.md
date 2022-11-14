# ProvisionedConcurrencyConfigListItem<a name="API_ProvisionedConcurrencyConfigListItem"></a>

Details about the provisioned concurrency configuration for a function alias or version\.

## Contents<a name="API_ProvisionedConcurrencyConfigListItem_Contents"></a>

 ** AllocatedProvisionedConcurrentExecutions **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-AllocatedProvisionedConcurrentExecutions"></a>
The amount of provisioned concurrency allocated\. When a weighted alias is used during linear and canary deployments, this value fluctuates depending on the amount of concurrency that is provisioned for the function versions\.  
Type: Integer  
Valid Range: Minimum value of 0\.  
Required: No

 ** AvailableProvisionedConcurrentExecutions **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-AvailableProvisionedConcurrentExecutions"></a>
The amount of provisioned concurrency available\.  
Type: Integer  
Valid Range: Minimum value of 0\.  
Required: No

 ** FunctionArn **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-FunctionArn"></a>
The Amazon Resource Name \(ARN\) of the alias or version\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 ** LastModified **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-LastModified"></a>
The date and time that a user last updated the configuration, in [ISO 8601 format](https://www.iso.org/iso-8601-date-and-time-format.html)\.  
Type: String  
Required: No

 ** RequestedProvisionedConcurrentExecutions **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-RequestedProvisionedConcurrentExecutions"></a>
The amount of provisioned concurrency requested\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 ** Status **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-Status"></a>
The status of the allocation process\.  
Type: String  
Valid Values:` IN_PROGRESS | READY | FAILED`   
Required: No

 ** StatusReason **   <a name="SSS-Type-ProvisionedConcurrencyConfigListItem-StatusReason"></a>
For failed allocations, the reason that provisioned concurrency could not be allocated\.  
Type: String  
Required: No

## See Also<a name="API_ProvisionedConcurrencyConfigListItem_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ProvisionedConcurrencyConfigListItem) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ProvisionedConcurrencyConfigListItem) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/ProvisionedConcurrencyConfigListItem) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/ProvisionedConcurrencyConfigListItem) 