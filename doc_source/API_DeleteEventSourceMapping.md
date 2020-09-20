# DeleteEventSourceMapping<a name="API_DeleteEventSourceMapping"></a>

Deletes an [event source mapping](https://docs.aws.amazon.com/lambda/latest/dg/intro-invocation-modes.html)\. You can get the identifier of a mapping from the output of [ListEventSourceMappings](API_ListEventSourceMappings.md)\.

When you delete an event source mapping, it enters a `Deleting` state and might not be completely deleted for several seconds\.

## Request Syntax<a name="API_DeleteEventSourceMapping_RequestSyntax"></a>

```
DELETE /2015-03-31/event-source-mappings/UUID HTTP/1.1
```

## URI Request Parameters<a name="API_DeleteEventSourceMapping_RequestParameters"></a>

The request uses the following URI parameters\.

 ** [UUID](#API_DeleteEventSourceMapping_RequestSyntax) **   <a name="SSS-DeleteEventSourceMapping-request-UUID"></a>
The identifier of the event source mapping\.  
Required: Yes

## Request Body<a name="API_DeleteEventSourceMapping_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_DeleteEventSourceMapping_ResponseSyntax"></a>

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

## Response Elements<a name="API_DeleteEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** [BatchSize](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [BisectBatchOnFunctionError](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-BisectBatchOnFunctionError"></a>
\(Streams\) If the function returns an error, split the batch in two and retry\. The default value is false\.  
Type: Boolean

 ** [DestinationConfig](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-DestinationConfig"></a>
\(Streams\) An Amazon SQS queue or Amazon SNS topic destination for discarded records\.  
Type: [DestinationConfig](API_DestinationConfig.md) object

 ** [EventSourceArn](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionArn](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [LastModified](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-LastModified"></a>
The date that the event source mapping was last updated, or its state changed, in Unix time seconds\.  
Type: Timestamp

 ** [LastProcessingResult](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String

 ** [MaximumBatchingWindowInSeconds](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-MaximumBatchingWindowInSeconds"></a>
\(Streams\) The maximum amount of time to gather records before invoking the function, in seconds\. The default value is zero\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 300\.

 ** [MaximumRecordAgeInSeconds](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-MaximumRecordAgeInSeconds"></a>
\(Streams\) Discard records older than the specified age\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of 60\. Maximum value of 604800\.

 ** [MaximumRetryAttempts](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-MaximumRetryAttempts"></a>
\(Streams\) Discard records after the specified number of retries\. The default value is infinite \(\-1\)\. When set to infinite \(\-1\), failed records are retried until the record expires\.  
Type: Integer  
Valid Range: Minimum value of 0\. Maximum value of 10000\.

 ** [ParallelizationFactor](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-ParallelizationFactor"></a>
\(Streams\) The number of batches to process from each shard concurrently\. The default value is 1\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10\.

 ** [State](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-StateTransitionReason"></a>
Indicates whether the last change to the event source mapping was made by a user, or by the Lambda service\.  
Type: String

 ** [Topics](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-Topics"></a>
 \(MSK\) The name of the Kafka topic\.   
Type: Array of strings  
Array Members: Fixed number of 1 item\.  
Length Constraints: Minimum length of 1\. Maximum length of 249\.  
Pattern: `^[^.]([a-zA-Z0-9\-_.]+)` 

 ** [UUID](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-UUID"></a>
The identifier of the event source mapping\.  
Type: String

## Errors<a name="API_DeleteEventSourceMapping_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\.  
HTTP Status Code: 400

 **ResourceInUseException**   
The operation conflicts with the resource's availability\. For example, you attempted to update an EventSource Mapping in CREATING, or tried to delete a EventSource mapping currently in the UPDATING state\.  
HTTP Status Code: 400

 **ResourceNotFoundException**   
The resource specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
The request throughput limit was exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_DeleteEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Ruby V3](https://docs.aws.amazon.com/goto/SdkForRubyV3/lambda-2015-03-31/DeleteEventSourceMapping) 