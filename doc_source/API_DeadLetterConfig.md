# DeadLetterConfig<a name="API_DeadLetterConfig"></a>

The [dead\-letter queue](https://docs.aws.amazon.com/lambda/latest/dg/invocation-async.html#dlq) for failed asynchronous invocations\.

## Contents<a name="API_DeadLetterConfig_Contents"></a>

 ** TargetArn **   <a name="SSS-Type-DeadLetterConfig-TargetArn"></a>
The Amazon Resource Name \(ARN\) of an Amazon SQS queue or Amazon SNS topic\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()`   
Required: No

## See Also<a name="API_DeadLetterConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/DeadLetterConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/DeadLetterConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/DeadLetterConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/DeadLetterConfig) 