# TracingConfig<a name="API_TracingConfig"></a>

The parent object that contains your function's tracing settings\.

## Contents<a name="API_TracingConfig_Contents"></a>

 **Mode**   
Can be either PassThrough or Active\. If PassThrough, Lambda will only trace the request from an upstream service if it contains a tracing header with "sampled=1"\. If Active, Lambda will respect any tracing header it receives from an upstream service\. If no tracing header is received, Lambda will call X\-Ray for a tracing decision\.  
Type: String  
Valid Values:` Active | PassThrough`   
Required: No

## See Also<a name="API_TracingConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/TracingConfig) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/TracingConfig) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/TracingConfig) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/TracingConfig) 