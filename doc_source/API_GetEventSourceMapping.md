# GetEventSourceMapping<a name="API_GetEventSourceMapping"></a>

Returns details about an event source mapping\. You can get the identifier of a mapping from the output of [ListEventSourceMappings](API_ListEventSourceMappings.md)\.

## Request Syntax<a name="API_GetEventSourceMapping_RequestSyntax"></a>

```
GET /2015-03-31/event-source-mappings/UUID HTTP/1.1
```

## URI Request Parameters<a name="API_GetEventSourceMapping_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [UUID](#API_GetEventSourceMapping_RequestSyntax) **   <a name="SSS-GetEventSourceMapping-request-UUID"></a>
The identifier of the event source mapping\.  
Required: Yes

## Request Body<a name="API_GetEventSourceMapping_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_GetEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 200
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

## Response Elements<a name="API_GetEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** [AmazonManagedKafkaEventSourceConfig](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-AmazonManagedKafkaEventSourceConfig"></a>
Specific configuration settings for an Amazon Managed Streaming for Apache Kafka \(Amazon MSK\) event source\.  
Type: [AmazonManagedKafkaEventSourceConfig](API_AmazonManagedKafkaEventSourceConfig.md) object

 ** [BatchSize](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-BatchSize"></a>
The maximum number of records in each batch that Lambda pulls from your stream or queue and sends to your function\. Lambda passes all of the records in the batch to the function in a single call, up to the payload limit for synchronous invocation \(6 MB\)\.  
Default value: Varies by service\. For Amazon SQS, the default is 10\. For all other services, the default is 100\.  
Related setting: When you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [BisectBatchOnFunctionError](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-BisectBatchOnFunctionError"></a>
\(Streams only\) If the function returns an error, split the batch in two and retry\. The default value is false\.  
Type: Boolean

 ** [DestinationConfig](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-DestinationConfig"></a>
\(Streams only\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object

 ** [EventSourceArn](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FilterCriteria](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-FilterCriteria"></a>
An object that defines the filter criteria that determine whether Lambda should process an event\. For more information, see [Lambda event filtering](https://docs.aws.amazon.com/lambda/latest/dg/invocation-eventfiltering.html)\.  
Type: [FilterCriteria](API_FilterCriteria.md) object

 ** [FunctionArn](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [FunctionResponseTypes](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-FunctionResponseTypes"></a>
\(Streams and Amazon SQS\) A list of current response type enums applied to the event source mapping\.  
Type: Array of strings  
Array Members: Minimum number of 0 items\. Maximum number of 1 item\.  
Valid Values:` ReportBatchItemFailures` 

 ** [LastModified](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-LastModified"></a>
The date that the event source mapping was last updated or that its state changed, in Unix time seconds\.  
Type: Timestamp

 ** [LastProcessingResult](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-LastProcessingResult"></a>
The result of the last Lambda invocation of your function\.  
Type: String

 ** [MaximumBatchingWindowInSeconds](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-MaximumBatchingWindowInSeconds"></a>
The maximum amount of time, in seconds, that Lambda spends gathering records before invoking the function\. You can configure `MaximumBatchingWindowInSeconds` to any value from 0 seconds to 300 seconds in increments of seconds\.  
For streams and Amazon SQS event sources, the default batching window is 0 seconds\. For Amazon MSK, Self\-managed Apache Kafka, and Amazon MQ event sources, the default batching window is 500 ms\. Note that because you can only change `MaximumBatchingWindowInSeconds` in increments of seconds, you cannot revert back to the 500 ms default batching window after you have changed it\. To restore the default batching window, you must create a new event source mapping\.  
Related setting: For streams and Amazon SQS event sources, when you set `BatchSize` to a value greater than 10, you must set `MaximumBatchingWindowInSeconds` to at least 1\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.

 ** [MaximumRecordAgeInSeconds](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-MaximumRecordAgeInSeconds"></a>
\(Streams only\) Discard records older than the specified age\. The default value is \-1, which sets the maximum age to infinite\. When the value is set to infinite, Lambda never discards old records\.   
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 604800\.

 ** [MaximumRetryAttempts](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-MaximumRetryAttempts"></a>
\(Streams only\) Discard records after the specified number of retries\. The default value is \-1, which sets the maximum number of retries to infinite\. When MaximumRetryAttempts is infinite, Lambda retries failed records until the record expires in the event source\.  
Type: Integer  
Valid Range: Minimum value of \-1\. Maximum value of 10000\.

 ** [ParallelizationFactor](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-ParallelizationFactor"></a>
\(Streams only\) The number of batches to process concurrently from each shard\. The default value is 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.

 ** [Queues](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-Queues"></a>
 \(Amazon MQ\) The name of the Amazon MQ broker destination queue to consume\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 1000\.  
Pattern: `[\s\S]*` 

 ** [SelfManagedEventSource](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-SelfManagedEventSource"></a>
The self\-managed Apache Kafka cluster for your event source\.  
Type: [SelfManagedEventSource](API_SelfManagedEventSource.md) object

 ** [SelfManagedKafkaEventSourceConfig](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-SelfManagedKafkaEventSourceConfig"></a>
Specific configuration settings for a self\-managed Apache Kafka event source\.  
Type: [SelfManagedKafkaEventSourceConfig](API_SelfManagedKafkaEventSourceConfig.md) object

 ** [SourceAccessConfigurations](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-SourceAccessConfigurations"></a>
An array of the authentication protocol, VPC components, or virtual host to secure and define your event source\.  
Type: Array of [SourceAccessConfiguration](API_SourceAccessConfiguration.md) objects  
Array Members: Minimum number of 0 items\. Maximum number of 22 items\.

 ** [StartingPosition](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-StartingPosition"></a>
The position in a stream from which to start reading\. Required for Amazon Kinesis, Amazon DynamoDB, and Amazon MSK stream sources\. `AT_TIMESTAMP` is supported only for Amazon Kinesis streams\.  
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP` 

 ** [StartingPositionTimestamp](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-StartingPositionTimestamp"></a>
With `StartingPosition` set to `AT_TIMESTAMP`, the time from which to start reading, in Unix time seconds\.  
Type: Timestamp

 ** [State](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-StateTransitionReason"></a>
Indicates whether a user or Lambda made the last change to the event source mapping\.  
Type: String

 ** [Topics](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-Topics"></a>
The name of the Kafka topic\.  
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)` 

 ** [TumblingWindowInSeconds](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-TumblingWindowInSeconds"></a>
\(Streams only\) The duration in seconds of a processing window\. The range is 1–900 seconds\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 900\.

 ** [UUID](#API_GetEventSourceMapping_ResponseSyntax) **   <a name="SSS-GetEventSourceMapping-response-UUID"></a>
The identifier of the event source mapping\.  
Type: String

## Errors<a name="API_GetEventSourceMapping_Errors"></a>

 ** InvalidParameterValueException **   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 ** ResourceNotFoundException **   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 ** ServiceException **   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 ** TooManyRequestsException **   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_GetEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for Java V2](https://docs.aws.amazon.com/goto/SdkForJavaV2/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/GetEventSourceMapping) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/GetEventSourceMapping) 