# EventSourceMappingConfiguration<a name="API_EventSourceMappingConfiguration"></a>

Describes mapping between an Amazon Kinesis stream and a Lambda function\.

## Contents<a name="API_EventSourceMappingConfiguration_Contents"></a>

 **BatchSize**   
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 **EventSourceArn**   
The Amazon Resource Name \(ARN\) of the Amazon Kinesis stream that is the source of events\.  
Type: String  
Pattern: `arn:aws:([a-zA-Z0-9\-])+:([a-z]{2}-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 **FunctionArn**   
The Lambda function to invoke when AWS Lambda detects an event on the stream\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **LastModified**   
The UTC time string indicating the last time the event mapping was updated\.  
Type: Timestamp  
Required: No

 **LastProcessingResult**   
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String  
Required: No

 **State**   
The state of the event source mapping\. It can be `Creating`, `Enabled`, `Disabled`, `Enabling`, `Disabling`, `Updating`, or `Deleting`\.  
Type: String  
Required: No

 **StateTransitionReason**   
The reason the event source mapping is in its current state\. It is either user\-requested or an AWS Lambda\-initiated state transition\.  
Type: String  
Required: No

 **UUID**   
The AWS Lambda assigned opaque identifier for the mapping\.  
Type: String  
Required: No

## See Also<a name="API_EventSourceMappingConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/EventSourceMappingConfiguration) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/EventSourceMappingConfiguration) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/EventSourceMappingConfiguration) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/EventSourceMappingConfiguration) 