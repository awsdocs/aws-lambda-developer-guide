# EventSourceMappingConfiguration<a name="API_EventSourceMappingConfiguration"></a>

A mapping between an AWS resource and an AWS Lambda function\. See [CreateEventSourceMapping](API_CreateEventSourceMapping.md) for details\.

## Contents<a name="API_EventSourceMappingConfiguration_Contents"></a>

 **BatchSize**   <a name="SSS-Type-EventSourceMappingConfiguration-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 **EventSourceArn**   <a name="SSS-Type-EventSourceMappingConfiguration-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 **FunctionArn**   <a name="SSS-Type-EventSourceMappingConfiguration-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **LastModified**   <a name="SSS-Type-EventSourceMappingConfiguration-LastModified"></a>
The date that the event source mapping was last updated, in Unix time seconds\.  
Type: Timestamp  
Required: No

 **LastProcessingResult**   <a name="SSS-Type-EventSourceMappingConfiguration-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String  
Required: No

 **State**   <a name="SSS-Type-EventSourceMappingConfiguration-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String  
Required: No

 **StateTransitionReason**   <a name="SSS-Type-EventSourceMappingConfiguration-StateTransitionReason"></a>
The cause of the last state change, either `User initiated` or `Lambda initiated`\.  
Type: String  
Required: No

 **UUID**   <a name="SSS-Type-EventSourceMappingConfiguration-UUID"></a>
The identifier of the event source mapping\.  
Type: String  
Required: No

## See Also<a name="API_EventSourceMappingConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/EventSourceMappingConfiguration) 