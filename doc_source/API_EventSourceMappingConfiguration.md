# EventSourceMappingConfiguration<a name="API_EventSourceMappingConfiguration"></a>

Describes mapping between an Amazon Kinesis or DynamoDB stream and a Lambda function\.

## Contents<a name="API_EventSourceMappingConfiguration_Contents"></a>

 **BatchSize**   <a name="SSS-Type-EventSourceMappingConfiguration-BatchSize"></a>
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 **EventSourceArn**   <a name="SSS-Type-EventSourceMappingConfiguration-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the Amazon Kinesis or DynamoDB stream that is the source of events\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 **FunctionArn**   <a name="SSS-Type-EventSourceMappingConfiguration-FunctionArn"></a>
The Lambda function to invoke when AWS Lambda detects an event on the poll\-based source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **LastModified**   <a name="SSS-Type-EventSourceMappingConfiguration-LastModified"></a>
The UTC time string indicating the last time the event mapping was updated\.  
Type: Timestamp  
Required: No

 **LastProcessingResult**   <a name="SSS-Type-EventSourceMappingConfiguration-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\. This value will be null if an SQS queue is the event source\.  
Type: String  
Required: No

 **State**   <a name="SSS-Type-EventSourceMappingConfiguration-State"></a>
The state of the event source mapping\. It can be `Creating`, `Enabled`, `Disabled`, `Enabling`, `Disabling`, `Updating`, or `Deleting`\.  
Type: String  
Required: No

 **StateTransitionReason**   <a name="SSS-Type-EventSourceMappingConfiguration-StateTransitionReason"></a>
The reason the event source mapping is in its current state\. It is either user\-requested or an AWS Lambda\-initiated state transition\.  
Type: String  
Required: No

 **UUID**   <a name="SSS-Type-EventSourceMappingConfiguration-UUID"></a>
The AWS Lambda assigned opaque identifier for the mapping\.  
Type: String  
Required: No

## See Also<a name="API_EventSourceMappingConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/EventSourceMappingConfiguration) 