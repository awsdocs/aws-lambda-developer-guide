# CreateEventSourceMapping<a name="API_CreateEventSourceMapping"></a>

Identifies a poll\-based event source for a Lambda function\. It can be either an Amazon Kinesis or DynamoDB stream\. AWS Lambda invokes the specified function when records are posted to the event source\.

This association between a poll\-based source and a Lambda function is called the event source mapping\.

You provide mapping information \(for example, which stream or SQS queue to read from and which Lambda function to invoke\) in the request body\.

Amazon Kinesis or DynamoDB stream event sources can be associated with multiple AWS Lambda functions and a given Lambda function can be associated with multiple AWS event sources\. For Amazon SQS, you can configure multiple queues as event sources for a single Lambda function, but an SQS queue can be mapped only to a single Lambda function\.

You can configure an SQS queue in an account separate from your Lambda function's account\. Also the queue needs to reside in the same AWS region as your function\. 

If you are using versioning, you can specify a specific function version or an alias via the function name parameter\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:CreateEventSourceMapping` action\.

## Request Syntax<a name="API_CreateEventSourceMapping_RequestSyntax"></a>

```
POST /2015-03-31/event-source-mappings/ HTTP/1.1
Content-type: application/json

{
   "[BatchSize](#SSS-CreateEventSourceMapping-request-BatchSize)": number,
   "[Enabled](#SSS-CreateEventSourceMapping-request-Enabled)": boolean,
   "[EventSourceArn](#SSS-CreateEventSourceMapping-request-EventSourceArn)": "string",
   "[FunctionName](#SSS-CreateEventSourceMapping-request-FunctionName)": "string",
   "[StartingPosition](#SSS-CreateEventSourceMapping-request-StartingPosition)": "string",
   "[StartingPositionTimestamp](#SSS-CreateEventSourceMapping-request-StartingPositionTimestamp)": number
}
```

## URI Request Parameters<a name="API_CreateEventSourceMapping_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateEventSourceMapping_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [BatchSize](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-BatchSize"></a>
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\. The default for Amazon Kinesis and Amazon DynamoDB is 100 records\. Both the default and maximum for Amazon SQS are 10 messages\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** [Enabled](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-Enabled"></a>
Set to false to disable the event source upon creation\.   
Type: Boolean  
Required: No

 ** [EventSourceArn](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: Yes

 ** [FunctionName](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-FunctionName"></a>
The name of the lambda function\.  

**Name formats**
+  **Function name** \- `MyFunction`\.
+  **Function ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction`\.
+  **Version or Alias ARN** \- `arn:aws:lambda:us-west-2:123456789012:function:MyFunction:PROD`\.
+  **Partial ARN** \- `123456789012:function:MyFunction`\.
The length constraint applies only to the full ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** [StartingPosition](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-StartingPosition"></a>
The position in the DynamoDB or Kinesis stream where AWS Lambda should start reading\. For more information, see [GetShardIterator](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetShardIterator.html#Kinesis-GetShardIterator-request-ShardIteratorType) in the *Amazon Kinesis API Reference Guide* or [GetShardIterator](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_streams_GetShardIterator.html) in the *Amazon DynamoDB API Reference Guide*\. The `AT_TIMESTAMP` value is supported only for [Kinesis streams](https://docs.aws.amazon.com/streams/latest/dev/amazon-kinesis-streams.html)\.   
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP`   
Required: No

 ** [StartingPositionTimestamp](#API_CreateEventSourceMapping_RequestSyntax) **   <a name="SSS-CreateEventSourceMapping-request-StartingPositionTimestamp"></a>
The timestamp of the data record from which to start reading\. Used with [shard iterator type](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetShardIterator.html#Kinesis-GetShardIterator-request-ShardIteratorType) AT\_TIMESTAMP\. If a record with this exact timestamp does not exist, the iterator returned is for the next \(later\) record\. If the timestamp is older than the current trim horizon, the iterator returned is for the oldest untrimmed data record \(TRIM\_HORIZON\)\. Valid only for [Kinesis streams](https://docs.aws.amazon.com/streams/latest/dev/amazon-kinesis-streams.html)\.   
Type: Timestamp  
Required: No

## Response Syntax<a name="API_CreateEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 202
Content-type: application/json

{
   "[BatchSize](#SSS-CreateEventSourceMapping-response-BatchSize)": number,
   "[EventSourceArn](#SSS-CreateEventSourceMapping-response-EventSourceArn)": "string",
   "[FunctionArn](#SSS-CreateEventSourceMapping-response-FunctionArn)": "string",
   "[LastModified](#SSS-CreateEventSourceMapping-response-LastModified)": number,
   "[LastProcessingResult](#SSS-CreateEventSourceMapping-response-LastProcessingResult)": "string",
   "[State](#SSS-CreateEventSourceMapping-response-State)": "string",
   "[StateTransitionReason](#SSS-CreateEventSourceMapping-response-StateTransitionReason)": "string",
   "[UUID](#SSS-CreateEventSourceMapping-response-UUID)": "string"
}
```

## Response Elements<a name="API_CreateEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** [BatchSize](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-BatchSize"></a>
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [EventSourceArn](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the Amazon Kinesis or DynamoDB stream that is the source of events\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionArn](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-FunctionArn"></a>
The Lambda function to invoke when AWS Lambda detects an event on the poll\-based source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [LastModified](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-LastModified"></a>
The UTC time string indicating the last time the event mapping was updated\.  
Type: Timestamp

 ** [LastProcessingResult](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\. This value will be null if an SQS queue is the event source\.  
Type: String

 ** [State](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be `Creating`, `Enabled`, `Disabled`, `Enabling`, `Disabling`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-StateTransitionReason"></a>
The reason the event source mapping is in its current state\. It is either user\-requested or an AWS Lambda\-initiated state transition\.  
Type: String

 ** [UUID](#API_CreateEventSourceMapping_ResponseSyntax) **   <a name="SSS-CreateEventSourceMapping-response-UUID"></a>
The AWS Lambda assigned opaque identifier for the mapping\.  
Type: String

## Errors<a name="API_CreateEventSourceMapping_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **ResourceConflictException**   
The resource already exists\.  
HTTP Status Code: 409

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
Request throughput limit exceeded  
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
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/CreateEventSourceMapping) 