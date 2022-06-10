# FunctionEventInvokeConfig<a name="API_FunctionEventInvokeConfig"></a>

## Contents<a name="API_FunctionEventInvokeConfig_Contents"></a>

 ** DestinationConfig **   <a name="SSS-Type-FunctionEventInvokeConfig-DestinationConfig"></a>
A destination for events after they have been sent to a function for processing\.  

**Destinations**
+  **Function** \- The Amazon Resource Name \(ARN\) of a Lambda function\.
+  **Queue** \- The ARN of an SQS queue\.
+  **Topic** \- The ARN of an SNS topic\.
+  **Event Bus** \- The ARN of an Amazon EventBridge event bus\.
Type: [DestinationConfig](API_DestinationConfig.md) object  
Required: No

 ** FunctionArn **   <a name="SSS-Type-FunctionEventInvokeConfig-FunctionArn"></a>
The Amazon Resource Name \(ARN\) of the function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 ** LastModified **   <a name="SSS-Type-FunctionEventInvokeConfig-LastModified"></a>
The date and time that the configuration was last updated, in Unix time seconds\.  
Type: Timestamp  
Required: No

 ** MaximumEventAgeInSeconds **   <a name="SSS-Type-FunctionEventInvokeConfig-MaximumEventAgeInSeconds"></a>
The maximum age of a request that Lambda sends to a function for processing\.  
Type: Integer  
Valid Range: Minimum value of 60\. Maximum value of 21600\.  
Required: No

 ** MaximumRetryAttempts **   <a name="SSS-Type-FunctionEventInvokeConfig-MaximumRetryAttempts"></a>
The maximum number of times to retry when the function returns an error\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 2\.  
Required: No

## See Also<a name="API_FunctionEventInvokeConfig_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionEventInvokeConfig) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionEventInvokeConfig) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/FunctionEventInvokeConfig) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/FunctionEventInvokeConfig) 