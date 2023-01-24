# EventSourceMappingConfiguration<a name="API_EventSourceMappingConfiguration"></a>

A mapping between an AWS resource and a Lambda function\. For details, see [CreateEventSourceMapping](API_CreateEventSourceMapping.md)\.

## Contents<a name="API_EventSourceMappingConfiguration_Contents"></a>

 ** AmazonManagedKafkaEventSourceConfig **   <a name="SSS-Type-EventSourceMappingConfiguration-AmazonManagedKafkaEventSourceConfig"></a>
Specific configuration settings for an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) event source\.  
Type: [AmazonManagedKafkaEventSourceConfig](API_AmazonManagedKafkaEventSourceConfig.md) object  
Required: No

 ** BatchSize **   <a name="SSS-Type-EventSourceMappingConfiguration-BatchSize"></a>
The maximum number of records in each batch that Lambda pulls from your stream or queue and sends to your function\. Lambda passes all of the records in the batch to the function in a single call, up to the payload limit for synchronous invocation \(6 MB\)\.  
Default value: Varies by service\. For Amazon SQS, the default is 10\. For all other services, the default is 100\.  
Related setting: When you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** BisectBatchOnFunctionError **   <a name="SSS-Type-EventSourceMappingConfiguration-BisectBatchOnFunctionError"></a>
\(Streams only\) If the function returns an error, split the batch in two and retry\. The default value is false\.  
Type: Boolean  
Required: No

 ** DestinationConfig **   <a name="SSS-Type-EventSourceMappingConfiguration-DestinationConfig"></a>
\(Streams only\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object  
Required: No

 ** EventSourceArn **   <a name="SSS-Type-EventSourceMappingConfiguration-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 ** FilterCriteria **   <a name="SSS-Type-EventSourceMappingConfiguration-FilterCriteria"></a>
An object that defines the filter criteria that determine whether Lambda should process an event\. For more information, see [Lambda event filtering](https://docs.aws.amazon.com/lambda/latest/dg/invocation-eventfiltering.html)\.  
Type: [FilterCriteria](API_FilterCriteria.md) object  
Required: No

 ** FunctionArn **   <a name="SSS-Type-EventSourceMappingConfiguration-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 ** FunctionResponseTypes **   <a name="SSS-Type-EventSourceMappingConfiguration-FunctionResponseTypes"></a>
\(Streams and Amazon SQS\) A list of current response type enums applied to the event source mapping\.  
Type: Array of strings  
Array Members: Minimum number of 0 items\. Maximum number of 1 item\.  
Valid Values:` ReportBatchItemFailures`   
Required: No

 ** LastModified **   <a name="SSS-Type-EventSourceMappingConfiguration-LastModified"></a>
The date that the event source mapping was last updated or that its state changed, in Unix time seconds\.  
Type: Timestamp  
Required: No

 ** LastProcessingResult **   <a name="SSS-Type-EventSourceMappingConfiguration-LastProcessingResult"></a>
The result of the last Lambda invocation of your function\.  
Type: String  
Required: No

 ** MaximumBatchingWindowInSeconds **   <a name="SSS-Type-EventSourceMappingConfiguration-MaximumBatchingWindowInSeconds"></a>
The maximum amount of time, in seconds, that Lambda spends gathering records before invoking the function\. You can configure `MaximumBatchingWindowInSeconds` to any value from 0 seconds to 300 seconds in increments of seconds\.  
For streams and Amazon SQS event sources, the default batching window is 0 seconds\. For Amazon MSK, Self\-managed Apache Kafka, and Amazon MQ event sources, the default batching window is 500 ms\. Note that because you can only change `MaximumBatchingWindowInSeconds` in increments of seconds, you cannot revert back to the 500 ms default batching window after you have changed it\. To restore the default batching window, you must create a new event source mapping\.  
Related setting: For streams and Amazon SQS event sources, when you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.  
Required: No

 ** MaximumRecordAgeInSeconds **   <a name="SSS-Type-EventSourceMappingConfiguration-MaximumRecordAgeInSeconds"></a>
\(Streams only\) Discard records older than the specified age\. The default value is \-1, which sets the maximum age to infinite\. When the value is set to infinite, Lambda never discards old records\.   
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 604800\.  
Required: No

 ** MaximumRetryAttempts **   <a name="SSS-Type-EventSourceMappingConfiguration-MaximumRetryAttempts"></a>
\(Streams only\) Discard records after the specified number of retries\. The default value is \-1, which sets the maximum number of retries to infinite\. When MaximumRetryAttempts is infinite, Lambda retries failed records until the record expires in the event source\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 10000\.  
Required: No

 ** ParallelizationFactor **   <a name="SSS-Type-EventSourceMappingConfiguration-ParallelizationFactor"></a>
\(Streams only\) The number of batches to process concurrently from each shard\. The default value is 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.  
Required: No

 ** Queues **   <a name="SSS-Type-EventSourceMappingConfiguration-Queues"></a>
 \(Amazon MQ\) The name of the Amazon MQ broker destination queue to consume\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 1000\.  
Pattern: `[\s\S]*`   
Required: No

 ** SelfManagedEventSource **   <a name="SSS-Type-EventSourceMappingConfiguration-SelfManagedEventSource"></a>
The self\-managed Apache Kafka cluster for your event source\.  
Type: [SelfManagedEventSource](API_SelfManagedEventSource.md) object  
Required: No

 ** SelfManagedKafkaEventSourceConfig **   <a name="SSS-Type-EventSourceMappingConfiguration-SelfManagedKafkaEventSourceConfig"></a>
Specific configuration settings for a self\-managed Apache Kafka event source\.  
Type: [SelfManagedKafkaEventSourceConfig](API_SelfManagedKafkaEventSourceConfig.md) object  
Required: No

 ** SourceAccessConfigurations **   <a name="SSS-Type-EventSourceMappingConfiguration-SourceAccessConfigurations"></a>
An array of the authentication protocol, VPC components, or virtual host to secure and define your event source\.  
Type: Array of [SourceAccessConfiguration](API_SourceAccessConfiguration.md) objects  
Array Members: Minimum number of 0 items\. Maximum number of 22 items\.  
Required: No

 ** StartingPosition **   <a name="SSS-Type-EventSourceMappingConfiguration-StartingPosition"></a>
The position in a stream from which to start reading\. Required for Amazon Kinesis, Amazon DynamoDB, and Amazon MSK stream sources\. `AT_TIMESTAMP` is supported only for Amazon Kinesis streams\.  
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP`   
Required: No

 ** StartingPositionTimestamp **   <a name="SSS-Type-EventSourceMappingConfiguration-StartingPositionTimestamp"></a>
With `StartingPosition` set to `AT_TIMESTAMP`, the time from which to start reading, in Unix time seconds\.  
Type: Timestamp  
Required: No

 ** State **   <a name="SSS-Type-EventSourceMappingConfiguration-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String  
Required: No

 ** StateTransitionReason **   <a name="SSS-Type-EventSourceMappingConfiguration-StateTransitionReason"></a>
Indicates whether a user or Lambda made the last change to the event source mapping\.  
Type: String  
Required: No

 ** Topics **   <a name="SSS-Type-EventSourceMappingConfiguration-Topics"></a>
The name of the Kafka topic\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)`   
Required: No

 ** TumblingWindowInSeconds **   <a name="SSS-Type-EventSourceMappingConfiguration-TumblingWindowInSeconds"></a>
\(Streams only\) The duration in seconds of a processing window\. The range is 1–900 seconds\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 900\.  
Required: No

 ** UUID **   <a name="SSS-Type-EventSourceMappingConfiguration-UUID"></a>
The identifier of the event source mapping\.  
Type: String  
Required: No

## See Also<a name="API_EventSourceMappingConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/EventSourceMappingConfiguration) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/EventSourceMappingConfiguration) 