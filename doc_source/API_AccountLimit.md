# AccountLimit<a name="API_AccountLimit"></a>

Provides limits of code size and concurrency associated with the current account and region\.

## Contents<a name="API_AccountLimit_Contents"></a>

 **CodeSizeUnzipped**   <a name="SSS-Type-AccountLimit-CodeSizeUnzipped"></a>
Size, in bytes, of code/dependencies that you can zip into a deployment package \(uncompressed zip/jar size\) for uploading\. The default limit is 250 MB\.  
Type: Long  
Required: No

 **CodeSizeZipped**   <a name="SSS-Type-AccountLimit-CodeSizeZipped"></a>
Size, in bytes, of a single zipped code/dependencies package you can upload for your Lambda function\(\.zip/\.jar file\)\. Try using Amazon S3 for uploading larger files\. Default limit is 50 MB\.  
Type: Long  
Required: No

 **ConcurrentExecutions**   <a name="SSS-Type-AccountLimit-ConcurrentExecutions"></a>
Number of simultaneous executions of your function per region\. For more information or to request a limit increase for concurrent executions, see [Lambda Function Concurrent Executions](http://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html)\. The default limit is 1000\.  
Type: Integer  
Required: No

 **TotalCodeSize**   <a name="SSS-Type-AccountLimit-TotalCodeSize"></a>
Maximum size, in bytes, of a code package you can upload per region\. The default size is 75 GB\.   
Type: Long  
Required: No

 **UnreservedConcurrentExecutions**   <a name="SSS-Type-AccountLimit-UnreservedConcurrentExecutions"></a>
The number of concurrent executions available to functions that do not have concurrency limits set\. For more information, see [Managing Concurrency](concurrent-executions.md)\.  
Type: Integer  
Valid Range: Minimum value of 0\.  
Required: No

## See Also<a name="API_AccountLimit_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AccountLimit) 
+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AccountLimit) 
+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/AccountLimit) 
+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/AccountLimit) 