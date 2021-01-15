# EventSourceMappingConfiguration<a name="API_EventSourceMappingConfiguration"></a>

A mapping between an AWS resource and an AWS Lambda function\. See [CreateEventSourceMapping](API_CreateEventSourceMapping.md) for details\.

## Contents<a name="API_EventSourceMappingConfiguration_Contents"></a>

 **BatchSize**   <a name="SSS-Type-EventSourceMappingConfiguration-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 **BisectBatchOnFunctionError**   <a name="SSS-Type-EventSourceMappingConfiguration-BisectBatchOnFunctionError"></a>
\(Streams\) If the function returns an error, split the batch in two and retry\. The default value is false\.  
Type: Boolean  
Required: No

 **DestinationConfig**   <a name="SSS-Type-EventSourceMappingConfiguration-DestinationConfig"></a>
\(Streams\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object  
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

 **FunctionResponseTypes**   <a name="SSS-Type-EventSourceMappingConfiguration-FunctionResponseTypes"></a>
\(Streams\) A list of current response type enums applied to the event source mapping\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Valid Values:` ReportBatchItemFailures`   
Required: No

 **LastModified**   <a name="SSS-Type-EventSourceMappingConfiguration-LastModified"></a>
The date that the event source mapping was last updated, or its state changed, in Unix time seconds\.  
Type: Timestamp  
Required: No

 **LastProcessingResult**   <a name="SSS-Type-EventSourceMappingConfiguration-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String  
Required: No

 **MaximumBatchingWindowInSeconds**   <a name="SSS-Type-EventSourceMappingConfiguration-MaximumBatchingWindowInSeconds"></a>
\(Streams and SQS standard queues\) The maximum amount of time to gather records before invoking the function, in seconds\. The default value is zero\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.  
Required: No

 **MaximumRecordAgeInSeconds**   <a name="SSS-Type-EventSourceMappingConfiguration-MaximumRecordAgeInSeconds"></a>
\(Streams\) Discard records older than the specified age\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 604800\.  
Required: No

 **MaximumRetryAttempts**   <a name="SSS-Type-EventSourceMappingConfiguration-MaximumRetryAttempts"></a>
\(Streams\) Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 10000\.  
Required: No

 **ParallelizationFactor**   <a name="SSS-Type-EventSourceMappingConfiguration-ParallelizationFactor"></a>
\(Streams\) The number of batches to process from each shard concurrently\. The default value is 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.  
Required: No

 **Queues**   <a name="SSS-Type-EventSourceMappingConfiguration-Queues"></a>
 \(MQ\) The name of the Amazon MQ broker destination queue to consume\.   
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 1000\.  
Pattern: `[\s\S]*`   
Required: No

 **SelfManagedEventSource**   <a name="SSS-Type-EventSourceMappingConfiguration-SelfManagedEventSource"></a>
The Self\-Managed Apache Kafka cluster for your event source\.  
Type: [SelfManagedEventSource](API_SelfManagedEventSource.md) object  
Required: No

 **SourceAccessConfigurations**   <a name="SSS-Type-EventSourceMappingConfiguration-SourceAccessConfigurations"></a>
An array of the authentication protocol, or the VPC components to secure your event source\.  
Type: Array of [SourceAccessConfiguration](API_SourceAccessConfiguration.md) objects  
Array Members: Minimum number of 1 item\. Maximum number of 22 items\.  
Required: No

 **StartingPosition**   <a name="SSS-Type-EventSourceMappingConfiguration-StartingPosition"></a>
The position in a stream from which to start reading\. Required for Amazon Kinesis, Amazon DynamoDB, and Amazon MSK Streams sources\. `AT_TIMESTAMP` is only supported for Amazon Kinesis streams\.  
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP`   
Required: No

 **StartingPositionTimestamp**   <a name="SSS-Type-EventSourceMappingConfiguration-StartingPositionTimestamp"></a>
With `StartingPosition` set to `AT_TIMESTAMP`, the time from which to start reading, in Unix time seconds\.  
Type: Timestamp  
Required: No

 **State**   <a name="SSS-Type-EventSourceMappingConfiguration-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String  
Required: No

 **StateTransitionReason**   <a name="SSS-Type-EventSourceMappingConfiguration-StateTransitionReason"></a>
Indicates whether the last change to the event source mapping was made by a user, or by the Lambda service\.  
Type: String  
Required: No

 **Topics**   <a name="SSS-Type-EventSourceMappingConfiguration-Topics"></a>
The name of the Kafka topic\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)`   
Required: No

 **TumblingWindowInSeconds**   <a name="SSS-Type-EventSourceMappingConfiguration-TumblingWindowInSeconds"></a>
\(Streams\) The duration of a processing window in seconds\. The range is between 1 second up to 15 minutes\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 900\.  
Required: No

 **UUID**   <a name="SSS-Type-EventSourceMappingConfiguration-UUID"></a>
The identifier of the event source mapping\.  
Type: String  
Required: No

## See Also<a name="API_EventSourceMappingConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/EventSourceMappingConfiguration) 