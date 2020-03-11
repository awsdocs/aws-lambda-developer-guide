# AccountLimit<a name="API_AccountLimit"></a>

Limits that are related to concurrency and storage\. All file and storage sizes are in bytes\.

## Contents<a name="API_AccountLimit_Contents"></a>

 **CodeSizeUnzipped**   <a name="SSS-Type-AccountLimit-CodeSizeUnzipped"></a>
The maximum size of a function's deployment package and layers when they're extracted\.  
Type: Long  
Required: No

 **CodeSizeZipped**   <a name="SSS-Type-AccountLimit-CodeSizeZipped"></a>
The maximum size of a deployment package when it's uploaded directly to AWS Lambda\. Use Amazon S3 for larger files\.  
Type: Long  
Required: No

 **ConcurrentExecutions**   <a name="SSS-Type-AccountLimit-ConcurrentExecutions"></a>
The maximum number of simultaneous function executions\.  
Type: Integer  
Required: No

 **TotalCodeSize**   <a name="SSS-Type-AccountLimit-TotalCodeSize"></a>
The amount of storage space that you can use for all deployment packages and layer archives\.  
Type: Long  
Required: No

 **UnreservedConcurrentExecutions**   <a name="SSS-Type-AccountLimit-UnreservedConcurrentExecutions"></a>
The maximum number of simultaneous function executions, minus the capacity that's reserved for individual functions with [PutFunctionConcurrency](API_PutFunctionConcurrency.md)\.  
Type: Integer  
Valid Range: Minimum value of 0\.  
Required: No

## See Also<a name="API_AccountLimit_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AccountLimit) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AccountLimit) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/AccountLimit) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/AccountLimit) 