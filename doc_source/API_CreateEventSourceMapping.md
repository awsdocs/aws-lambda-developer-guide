# CreateEventSourceMapping<a name="API_CreateEventSourceMapping"></a>

Creates a mapping between an event source and an AWS Lambda function\. Lambda reads items from the event source and invokes the function\.

For details about how to configure different event sources, see the following topics\. 
+  [ Amazon DynamoDB Streams](https://docs.aws.amazon.com/lambda/latest/dg/with-ddb.html#services-dynamodb-eventsourcemapping) 
+  [ Amazon Kinesis](https://docs.aws.amazon.com/lambda/latest/dg/with-kinesis.html#services-kinesis-eventsourcemapping) 
+  [ Amazon SQS](https://docs.aws.amazon.com/lambda/latest/dg/with-sqs.html#events-sqs-eventsource) 
+  [ Amazon MQ and RabbitMQ](https://docs.aws.amazon.com/lambda/latest/dg/with-mq.html#services-mq-eventsourcemapping) 
+  [ Amazon MSK](https://docs.aws.amazon.com/lambda/latest/dg/with-msk.html) 
+  [ Apache Kafka](https://docs.aws.amazon.com/lambda/latest/dg/kafka-smaa.html) 

The following error handling options are available only for stream sources \(DynamoDB and Kinesis\):
+  `BisectBatchOnFunctionError` \- If the function returns an error, split the batch in two and retry\.
+  `DestinationConfig` \- Send discarded records to an Amazon SQS queue or Amazon SNS topic\.
+  `MaximumRecordAgeInSeconds` \- Discard records older than the specified age\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires
+  `MaximumRetryAttempts` \- Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.
+  `ParallelizationFactor` \- Process multiple batches from each shard concurrently\.

For information about which configuration parameters apply to each event source, see the following topics\.
+  [ Amazon DynamoDB Streams](https://docs.aws.amazon.com/lambda/latest/dg/with-ddb.html#services-ddb-params) 
+  [ Amazon Kinesis](https://docs.aws.amazon.com/lambda/latest/dg/with-kinesis.html#services-kinesis-params) 
+  [ Amazon SQS](https://docs.aws.amazon.com/lambda/latest/dg/with-sqs.html#services-sqs-params) 
+  [ Amazon MQ and RabbitMQ](https://docs.aws.amazon.com/lambda/latest/dg/with-mq.html#services-mq-params) 
+  [ Amazon MSK](https://docs.aws.amazon.com/lambda/latest/dg/with-msk.html#services-msk-parms) 
+  [ Apache Kafka](https://docs.aws.amazon.com/lambda/latest/dg/with-kafka.html#services-kafka-parms) 

## Request Syntax<a name="API_CreateEventSourceMapping_RequestSyntax"></a>

```
POST /2015-03-31/event-source-mappings/ HTTP/1.1
Content-type: application/json

{
   "AmazonManagedKafkaEventSourceConfig": { 
      "ConsumerGroupId": "string"
   },
   "BatchSize": number,
   "BisectBatchOnFunctionError": boolean,
   "DestinationConfig": { 
      "OnFailure": { 
         "Destination": "string"
      },
      "OnSuccess": { 
         "Destination": "string"
      }
   },
   "Enabled": boolean,
   "EventSourceArn": "string",
   "FilterCriteria": { 
      "Filters": [ 
         { 
            "Pattern": "string"
         }
      ]
   },
   "FunctionName": "string",
   "FunctionResponseTypes": [ "string" ],
   "MaximumBatchingWindowInSeconds": number,
   "MaximumRecordAgeInSeconds": number,
   "MaximumRetryAttempts": number,
   "ParallelizationFactor": number,
   "Queues": [ "string" ],
   "SelfManagedEventSource": { 
      "Endpoints": { 
         "string" : [ "string" ]
      }
   },
   "SelfManagedKafkaEventSourceConfig": { 
      "ConsumerGroupId": "string"
   },
   "SourceAccessConfigurations": [ 
      { 
         "Type": "string",
         "URI": "string"
      }
   ],
   "StartingPosition": "string",
   "StartingPositionTimestamp": number,
   "Topics": [ "string" ],
   "TumblingWindowInSeconds": number
}
```

## URI Request Parameters<a name="API_CreateEventSourceMapping_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateEventSourceMapping_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [AmazonManagedKafkaEventSourceConfig](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-AmazonManagedKafkaEventSourceConfig"></a>
Specific configuration settings for an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) event source\.  
Type: [AmazonManagedKafkaEventSourceConfig](API_AmazonManagedKafkaEventSourceConfig.md) object  
Required: No

 ** [BatchSize](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-BatchSize"></a>
The maximum number of records in each batch that Lambda pulls from your stream or queue and sends to your function\. Lambda passes all of the records in the batch to the function in a single call, up to the payload limit for synchronous invocation \(6 MB\)\.  
+  **Amazon Kinesis** \- Default 100\. Max 10,000\.
+  **Amazon DynamoDB Streams** \- Default 100\. Max 10,000\.
+  **Amazon Simple Queue Service** \- Default 10\. For standard queues the max is 10,000\. For FIFO queues the max is 10\.
+  **Amazon Managed Streaming for Apache Kafka** \- Default 100\. Max 10,000\.
+  **Self\-managed Apache Kafka** \- Default 100\. Max 10,000\.
+  **Amazon MQ \(ActiveMQ and RabbitMQ\)** \- Default 100\. Max 10,000\.
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** [BisectBatchOnFunctionError](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-BisectBatchOnFunctionError"></a>
\(Streams only\) If the function returns an error, split the batch in two and retry\.  
Type: Boolean  
Required: No

 ** [DestinationConfig](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-DestinationConfig"></a>
\(Streams only\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object  
Required: No

 ** [Enabled](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-Enabled"></a>
When true, the event source mapping is active\. When false, Lambda pauses polling and invocation\.  
Default: True  
Type: Boolean  
Required: No

 ** [EventSourceArn](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
+  **Amazon Kinesis** \- The ARN of the data stream or a stream consumer\.
+  **Amazon DynamoDB Streams** \- The ARN of the stream\.
+  **Amazon Simple Queue Service** \- The ARN of the queue\.
+  **Amazon Managed Streaming for Apache Kafka** \- The ARN of the cluster\.
+  **Amazon MQ** \- The ARN of the broker\.
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 ** [FilterCriteria](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-FilterCriteria"></a>
An object that defines the filter criteria that determine whether Lambda should process an event\. For more information, see [Lambda event filtering](https://docs.aws.amazon.com/lambda/latest/dg/invocation-eventfiltering.html)\.  
Type: [FilterCriteria](API_FilterCriteria.md) object  
Required: No

 ** [FunctionName](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-FunctionName"></a>
The name of the Lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Version or Alias ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction:PROD`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it's limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [FunctionResponseTypes](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-FunctionResponseTypes"></a>
\(Streams and Amazon SQS\) A list of current response type enums applied to the event source mapping\.  
Type: Array of strings  
Array Members: Minimum number of 0 items\. Maximum number of 1 item\.  
Valid Values:` ReportBatchItemFailures`   
Required: No

 ** [MaximumBatchingWindowInSeconds](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-MaximumBatchingWindowInSeconds"></a>
The maximum amount of time, in seconds, that Lambda spends gathering records before invoking the function\. You can configure `MaximumBatchingWindowInSeconds` to any value from 0 seconds to 300 seconds in increments of seconds\.  
For streams and Amazon SQS event sources, the default batching window is 0 seconds\. For Amazon MSK, Self\-managed Apache Kafka, and Amazon MQ event sources, the default batching window is 500 ms\. Note that because you can only change `MaximumBatchingWindowInSeconds` in increments of seconds, you cannot revert back to the 500 ms default batching window after you have changed it\. To restore the default batching window, you must create a new event source mapping\.  
Related setting: For streams and Amazon SQS event sources, when you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.  
Required: No

 ** [MaximumRecordAgeInSeconds](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-MaximumRecordAgeInSeconds"></a>
\(Streams only\) Discard records older than the specified age\. The default value is infinite \(\-1\)\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 604800\.  
Required: No

 ** [MaximumRetryAttempts](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-MaximumRetryAttempts"></a>
\(Streams only\) Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 10000\.  
Required: No

 ** [ParallelizationFactor](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-ParallelizationFactor"></a>
\(Streams only\) The number of batches to process from each shard concurrently\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.  
Required: No

 ** [Queues](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-Queues"></a>
 \(MQ\) The name of the Amazon MQ broker destination queue to consume\.   
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 1000\.  
Pattern: `[\s\S]*`   
Required: No

 ** [SelfManagedEventSource](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-SelfManagedEventSource"></a>
The self\-managed Apache Kafka cluster to receive records from\.  
Type: [SelfManagedEventSource](API_SelfManagedEventSource.md) object  
Required: No

 ** [SelfManagedKafkaEventSourceConfig](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-SelfManagedKafkaEventSourceConfig"></a>
Specific configuration settings for a self\-managed Apache Kafka event source\.  
Type: [SelfManagedKafkaEventSourceConfig](API_SelfManagedKafkaEventSourceConfig.md) object  
Required: No

 ** [SourceAccessConfigurations](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-SourceAccessConfigurations"></a>
An array of authentication protocols or VPC components required to secure your event source\.  
Type: Array of [SourceAccessConfiguration](API_SourceAccessConfiguration.md) objects  
Array Members: Minimum number of 0 items\. Maximum number of 22 items\.  
Required: No

 ** [StartingPosition](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-StartingPosition"></a>
The position in a stream from which to start reading\. Required for Amazon Kinesis, Amazon DynamoDB, and Amazon MSK Streams sources\. `AT_TIMESTAMP` is supported only for Amazon Kinesis streams\.  
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP`   
Required: No

 ** [StartingPositionTimestamp](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-StartingPositionTimestamp"></a>
With `StartingPosition` set to `AT_TIMESTAMP`, the time from which to start reading, in Unix time seconds\.  
Type: Timestamp  
Required: No

 ** [Topics](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-Topics"></a>
The name of the Kafka topic\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)`   
Required: No

 ** [TumblingWindowInSeconds](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-TumblingWindowInSeconds"></a>
\(Streams only\) The duration in seconds of a processing window\. The range is between 1 second and 900 seconds\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 900\.  
Required: No

## Response Syntax<a name="API_CreateEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 202
Content-type: application/json

{
   "AmazonManagedKafkaEventSourceConfig": { 
      "ConsumerGroupId": "string"
   },
   "BatchSize": number,
   "BisectBatchOnFunctionError": boolean,
   "DestinationConfig": { 
      "OnFailure": { 
         "Destination": "string"
      },
      "OnSuccess": { 
         "Destination": "string"
      }
   },
   "EventSourceArn": "string",
   "FilterCriteria": { 
      "Filters": [ 
         { 
            "Pattern": "string"
         }
      ]
   },
   "FunctionArn": "string",
   "FunctionResponseTypes": [ "string" ],
   "LastModified": number,
   "LastProcessingResult": "string",
   "MaximumBatchingWindowInSeconds": number,
   "MaximumRecordAgeInSeconds": number,
   "MaximumRetryAttempts": number,
   "ParallelizationFactor": number,
   "Queues": [ "string" ],
   "SelfManagedEventSource": { 
      "Endpoints": { 
         "string" : [ "string" ]
      }
   },
   "SelfManagedKafkaEventSourceConfig": { 
      "ConsumerGroupId": "string"
   },
   "SourceAccessConfigurations": [ 
      { 
         "Type": "string",
         "URI": "string"
      }
   ],
   "StartingPosition": "string",
   "StartingPositionTimestamp": number,
   "State": "string",
   "StateTransitionReason": "string",
   "Topics": [ "string" ],
   "TumblingWindowInSeconds": number,
   "UUID": "string"
}
```

## Response Elements<a name="API_CreateEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** [AmazonManagedKafkaEventSourceConfig](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-AmazonManagedKafkaEventSourceConfig"></a>
Specific configuration settings for an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) event source\.  
Type: [AmazonManagedKafkaEventSourceConfig](API_AmazonManagedKafkaEventSourceConfig.md) object

 ** [BatchSize](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-BatchSize"></a>
The maximum number of records in each batch that Lambda pulls from your stream or queue and sends to your function\. Lambda passes all of the records in the batch to the function in a single call, up to the payload limit for synchronous invocation \(6 MB\)\.  
Default value: Varies by service\. For Amazon SQS, the default is 10\. For all other services, the default is 100\.  
Related setting: When you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [BisectBatchOnFunctionError](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-BisectBatchOnFunctionError"></a>
\(Streams only\) If the function returns an error, split the batch in two and retry\. The default value is false\.  
Type: Boolean

 ** [DestinationConfig](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-DestinationConfig"></a>
\(Streams only\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object

 ** [EventSourceArn](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FilterCriteria](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-FilterCriteria"></a>
An object that defines the filter criteria that determine whether Lambda should process an event\. For more information, see [Lambda event filtering](https://docs.aws.amazon.com/lambda/latest/dg/invocation-eventfiltering.html)\.  
Type: [FilterCriteria](API_FilterCriteria.md) object

 ** [FunctionArn](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionResponseTypes](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-FunctionResponseTypes"></a>
\(Streams and Amazon SQS\) A list of current response type enums applied to the event source mapping\.  
Type: Array of strings  
Array Members: Minimum number of 0 items\. Maximum number of 1 item\.  
Valid Values:` ReportBatchItemFailures` 

 ** [LastModified](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-LastModified"></a>
The date that the event source mapping was last updated or that its state changed, in Unix time seconds\.  
Type: Timestamp

 ** [LastProcessingResult](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-LastProcessingResult"></a>
The result of the last Lambda invocation of your function\.  
Type: String

 ** [MaximumBatchingWindowInSeconds](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-MaximumBatchingWindowInSeconds"></a>
The maximum amount of time, in seconds, that Lambda spends gathering records before invoking the function\. You can configure `MaximumBatchingWindowInSeconds` to any value from 0 seconds to 300 seconds in increments of seconds\.  
For streams and Amazon SQS event sources, the default batching window is 0 seconds\. For Amazon MSK, Self\-managed Apache Kafka, and Amazon MQ event sources, the default batching window is 500 ms\. Note that because you can only change `MaximumBatchingWindowInSeconds` in increments of seconds, you cannot revert back to the 500 ms default batching window after you have changed it\. To restore the default batching window, you must create a new event source mapping\.  
Related setting: For streams and Amazon SQS event sources, when you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.

 ** [MaximumRecordAgeInSeconds](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-MaximumRecordAgeInSeconds"></a>
\(Streams only\) Discard records older than the specified age\. The default value is \-1, which sets the maximum age to infinite\. When the value is set to infinite, Lambda never discards old records\.   
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 604800\.

 ** [MaximumRetryAttempts](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-MaximumRetryAttempts"></a>
\(Streams only\) Discard records after the specified number of retries\. The default value is \-1, which sets the maximum number of retries to infinite\. When MaximumRetryAttempts is infinite, Lambda retries failed records until the record expires in the event source\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 10000\.

 ** [ParallelizationFactor](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-ParallelizationFactor"></a>
\(Streams only\) The number of batches to process concurrently from each shard\. The default value is 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.

 ** [Queues](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-Queues"></a>
 \(Amazon MQ\) The name of the Amazon MQ broker destination queue to consume\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 1000\.  
Pattern: `[\s\S]*` 

 ** [SelfManagedEventSource](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-SelfManagedEventSource"></a>
The self\-managed Apache Kafka cluster for your event source\.  
Type: [SelfManagedEventSource](API_SelfManagedEventSource.md) object

 ** [SelfManagedKafkaEventSourceConfig](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-SelfManagedKafkaEventSourceConfig"></a>
Specific configuration settings for a self\-managed Apache Kafka event source\.  
Type: [SelfManagedKafkaEventSourceConfig](API_SelfManagedKafkaEventSourceConfig.md) object

 ** [SourceAccessConfigurations](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-SourceAccessConfigurations"></a>
An array of the authentication protocol, VPC components, or virtual host to secure and define your event source\.  
Type: Array of [SourceAccessConfiguration](API_SourceAccessConfiguration.md) objects  
Array Members: Minimum number of 0 items\. Maximum number of 22 items\.

 ** [StartingPosition](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-StartingPosition"></a>
The position in a stream from which to start reading\. Required for Amazon Kinesis, Amazon DynamoDB, and Amazon MSK stream sources\. `AT_TIMESTAMP` is supported only for Amazon Kinesis streams\.  
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP` 

 ** [StartingPositionTimestamp](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-StartingPositionTimestamp"></a>
With `StartingPosition` set to `AT_TIMESTAMP`, the time from which to start reading, in Unix time seconds\.  
Type: Timestamp

 ** [State](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-StateTransitionReason"></a>
Indicates whether a user or Lambda made the last change to the event source mapping\.  
Type: String

 ** [Topics](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-Topics"></a>
The name of the Kafka topic\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)` 

 ** [TumblingWindowInSeconds](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-TumblingWindowInSeconds"></a>
\(Streams only\) The duration in seconds of a processing window\. The range is 1–900 seconds\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 900\.

 ** [UUID](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-UUID"></a>
The identifier of the event source mapping\.  
Type: String

## Errors<a name="API_CreateEventSourceMapping_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceConflictException **   
The resource already exists, or another operation is in progress\.  
HTTP Status Code: 409

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_CreateEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CreateEventSourceMapping) 