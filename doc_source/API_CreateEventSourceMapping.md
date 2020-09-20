# CreateEventSourceMapping<a name="API_CreateEventSourceMapping"></a>

Creates a mapping between an event source and an AWS Lambda function\. Lambda reads items from the event source and triggers the function\.

For details about each event source type, see the following topics\.
+  [Using AWS Lambda with Amazon DynamoDB](https://docs.aws.amazon.com/lambda/latest/dg/with-ddb.html) 
+  [Using AWS Lambda with Amazon Kinesis](https://docs.aws.amazon.com/lambda/latest/dg/with-kinesis.html) 
+  [Using AWS Lambda with Amazon SQS](https://docs.aws.amazon.com/lambda/latest/dg/with-sqs.html) 
+  [Using AWS Lambda with Amazon MSK](https://docs.aws.amazon.com/lambda/latest/dg/with-msk.html) 

The following error handling options are only available for stream sources \(DynamoDB and Kinesis\):
+  `BisectBatchOnFunctionError` \- If the function returns an error, split the batch in two and retry\.
+  `DestinationConfig` \- Send discarded records to an Amazon SQS queue or Amazon SNS topic\.
+  `MaximumRecordAgeInSeconds` \- Discard records older than the specified age\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires
+  `MaximumRetryAttempts` \- Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.
+  `ParallelizationFactor` \- Process multiple batches from each shard concurrently\.

## Request Syntax<a name="API_CreateEventSourceMapping_RequestSyntax"></a>

```
POST /2015-03-31/event-source-mappings/ HTTP/1.1
Content-type: application/json

{
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
   "FunctionName": "string",
   "MaximumBatchingWindowInSeconds": number,
   "MaximumRecordAgeInSeconds": number,
   "MaximumRetryAttempts": number,
   "ParallelizationFactor": number,
   "StartingPosition": "string",
   "StartingPositionTimestamp": number,
   "Topics": [ "string" ]
}
```

## URI Request Parameters<a name="API_CreateEventSourceMapping_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateEventSourceMapping_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [BatchSize](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
+  **Amazon Kinesis** \- Default 100\. Max 10,000\.
+  **Amazon DynamoDB Streams** \- Default 100\. Max 1,000\.
+  **Amazon Simple Queue Service** \- Default 10\. Max 10\.
+  **Amazon Managed Streaming for Apache Kafka** \- Default 100\. Max 10,000\.
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** [BisectBatchOnFunctionError](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-BisectBatchOnFunctionError"></a>
\(Streams\) If the function returns an error, split the batch in two and retry\.  
Type: Boolean  
Required: No

 ** [DestinationConfig](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-DestinationConfig"></a>
\(Streams\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object  
Required: No

 ** [Enabled](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-Enabled"></a>
If true, the event source mapping is active\. Set to false to pause polling and invocation\.  
Type: Boolean  
Required: No

 ** [EventSourceArn](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
+  **Amazon Kinesis** \- The ARN of the data stream or a stream consumer\.
+  **Amazon DynamoDB Streams** \- The ARN of the stream\.
+  **Amazon Simple Queue Service** \- The ARN of the queue\.
+  **Amazon Managed Streaming for Apache Kafka** \- The ARN of the cluster\.
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: Yes

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

 ** [MaximumBatchingWindowInSeconds](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-MaximumBatchingWindowInSeconds"></a>
\(Streams\) The maximum amount of time to gather records before invoking the function, in seconds\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.  
Required: No

 ** [MaximumRecordAgeInSeconds](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-MaximumRecordAgeInSeconds"></a>
\(Streams\) Discard records older than the specified age\. The default value is infinite \(\-1\)\.  
Type: Integer  
Valid Range: Minimum value of 60\. Maximum value of 604800\.  
Required: No

 ** [MaximumRetryAttempts](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-MaximumRetryAttempts"></a>
\(Streams\) Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records will be retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 10000\.  
Required: No

 ** [ParallelizationFactor](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-ParallelizationFactor"></a>
\(Streams\) The number of batches to process from each shard concurrently\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.  
Required: No

 ** [StartingPosition](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-StartingPosition"></a>
The position in a stream from which to start reading\. Required for Amazon Kinesis, Amazon DynamoDB, and Amazon MSK Streams sources\. `AT_TIMESTAMP` is only supported for Amazon Kinesis streams\.  
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP`   
Required: No

 ** [StartingPositionTimestamp](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-StartingPositionTimestamp"></a>
With `StartingPosition` set to `AT_TIMESTAMP`, the time from which to start reading, in Unix time seconds\.  
Type: Timestamp  
Required: No

 ** [Topics](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-Topics"></a>
 \(MSK\) The name of the Kafka topic\.   
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)`   
Required: No

## Response Syntax<a name="API_CreateEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 202
Content-type: application/json

{
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
   "FunctionArn": "string",
   "LastModified": number,
   "LastProcessingResult": "string",
   "MaximumBatchingWindowInSeconds": number,
   "MaximumRecordAgeInSeconds": number,
   "MaximumRetryAttempts": number,
   "ParallelizationFactor": number,
   "State": "string",
   "StateTransitionReason": "string",
   "Topics": [ "string" ],
   "UUID": "string"
}
```

## Response Elements<a name="API_CreateEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** [BatchSize](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [BisectBatchOnFunctionError](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-BisectBatchOnFunctionError"></a>
\(Streams\) If the function returns an error, split the batch in two and retry\. The default value is false\.  
Type: Boolean

 ** [DestinationConfig](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-DestinationConfig"></a>
\(Streams\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object

 ** [EventSourceArn](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionArn](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [LastModified](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-LastModified"></a>
The date that the event source mapping was last updated, or its state changed, in Unix time seconds\.  
Type: Timestamp

 ** [LastProcessingResult](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String

 ** [MaximumBatchingWindowInSeconds](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-MaximumBatchingWindowInSeconds"></a>
\(Streams\) The maximum amount of time to gather records before invoking the function, in seconds\. The default value is zero\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.

 ** [MaximumRecordAgeInSeconds](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-MaximumRecordAgeInSeconds"></a>
\(Streams\) Discard records older than the specified age\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of 60\. Maximum value of 604800\.

 ** [MaximumRetryAttempts](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-MaximumRetryAttempts"></a>
\(Streams\) Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 10000\.

 ** [ParallelizationFactor](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-ParallelizationFactor"></a>
\(Streams\) The number of batches to process from each shard concurrently\. The default value is 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.

 ** [State](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-StateTransitionReason"></a>
Indicates whether the last change to the event source mapping was made by a user, or by the Lambda service\.  
Type: String

 ** [Topics](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-Topics"></a>
 \(MSK\) The name of the Kafka topic\.   
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)` 

 ** [UUID](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-UUID"></a>
The identifier of the event source mapping\.  
Type: String

## Errors<a name="API_CreateEventSourceMapping_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 **ResourceConflictException**   
The resource already exists, or another operation is in progress\.  
HTTP Status Code: 409

 **ResourceNotFoundException**   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_CreateEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateEventSourceMapping) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/CreateEventSourceMapping) 