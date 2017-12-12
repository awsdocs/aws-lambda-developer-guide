# CreateEventSourceMapping<a name="API_CreateEventSourceMapping"></a>

Identifies a stream as an event source for a Lambda function\. It can be either an Amazon Kinesis stream or an Amazon DynamoDB stream\. AWS Lambda invokes the specified function when records are posted to the stream\.

This association between a stream source and a Lambda function is called the event source mapping\.

**Important**  
This event source mapping is relevant only in the AWS Lambda pull model, where AWS Lambda invokes the function\. For more information, see [AWS Lambda: How it Works](http://docs.aws.amazon.com/lambda/latest/dg/lambda-introduction.html) in the *AWS Lambda Developer Guide*\.

You provide mapping information \(for example, which stream to read from and which Lambda function to invoke\) in the request body\.

Each event source, such as an Amazon Kinesis or a DynamoDB stream, can be associated with multiple AWS Lambda function\. A given Lambda function can be associated with multiple AWS event sources\.

If you are using versioning, you can specify a specific function version or an alias via the function name parameter\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:CreateEventSourceMapping` action\.

## Request Syntax<a name="API_CreateEventSourceMapping_RequestSyntax"></a>

```
POST /2015-03-31/event-source-mappings/ HTTP/1.1
Content-type: application/json

{
   "BatchSize": number,
   "Enabled": boolean,
   "EventSourceArn": "string",
   "FunctionName": "string",
   "StartingPosition": "string",
   "StartingPositionTimestamp": number
}
```

## URI Request Parameters<a name="API_CreateEventSourceMapping_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateEventSourceMapping_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** BatchSize **   
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\. The default is 100 records\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** Enabled **   
Indicates whether AWS Lambda should begin polling the event source\. By default, `Enabled` is true\.   
Type: Boolean  
Required: No

 ** EventSourceArn **   
The Amazon Resource Name \(ARN\) of the Amazon Kinesis or the Amazon DynamoDB stream that is the event source\. Any record added to this stream could cause AWS Lambda to invoke your Lambda function, it depends on the `BatchSize`\. AWS Lambda POSTs the Amazon Kinesis event, containing records, to your Lambda function as JSON\.  
Type: String  
Pattern: `arn:aws:([a-zA-Z0-9\-])+:([a-z]{2}-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: Yes

 ** FunctionName **   
The Lambda function to invoke when AWS Lambda detects an event on the stream\.  
 You can specify the function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\.   
 If you are using versioning, you can also provide a qualified function ARN \(ARN that is qualified with function version or alias name as suffix\)\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)   
AWS Lambda also allows you to specify only the function name with the account ID qualifier \(for example, `account-id:Thumbnail`\)\.   
Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** StartingPosition **   
The position in the stream where AWS Lambda should start reading\. Valid only for Kinesis streams\. For more information, see [ShardIteratorType](http://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetShardIterator.html#Kinesis-GetShardIterator-request-ShardIteratorType) in the *Amazon Kinesis API Reference*\.   
Type: String  
Valid Values:` TRIM_HORIZON | LATEST | AT_TIMESTAMP`   
Required: Yes

 ** StartingPositionTimestamp **   
The timestamp of the data record from which to start reading\. Used with [shard iterator type](http://docs.aws.amazon.com/kinesis/latest/APIReference/API_GetShardIterator.html#Kinesis-GetShardIterator-request-ShardIteratorType) AT\_TIMESTAMP\. If a record with this exact timestamp does not exist, the iterator returned is for the next \(later\) record\. If the timestamp is older than the current trim horizon, the iterator returned is for the oldest untrimmed data record \(TRIM\_HORIZON\)\. Valid only for Kinesis streams\.   
Type: Timestamp  
Required: No

## Response Syntax<a name="API_CreateEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 202
Content-type: application/json

{
   "BatchSize": number,
   "EventSourceArn": "string",
   "FunctionArn": "string",
   "LastModified": number,
   "LastProcessingResult": "string",
   "State": "string",
   "StateTransitionReason": "string",
   "UUID": "string"
}
```

## Response Elements<a name="API_CreateEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** BatchSize **   
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** EventSourceArn **   
The Amazon Resource Name \(ARN\) of the Amazon Kinesis stream that is the source of events\.  
Type: String  
Pattern: `arn:aws:([a-zA-Z0-9\-])+:([a-z]{2}-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** FunctionArn **   
The Lambda function to invoke when AWS Lambda detects an event on the stream\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** LastModified **   
The UTC time string indicating the last time the event mapping was updated\.  
Type: Timestamp

 ** LastProcessingResult **   
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String

 ** State **   
The state of the event source mapping\. It can be `Creating`, `Enabled`, `Disabled`, `Enabling`, `Disabling`, `Updating`, or `Deleting`\.  
Type: String

 ** StateTransitionReason **   
The reason the event source mapping is in its current state\. It is either user\-requested or an AWS Lambda\-initiated state transition\.  
Type: String

 ** UUID **   
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
   
HTTP Status Code: 429

## See Also<a name="API_CreateEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateEventSourceMapping) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/CreateEventSourceMapping) 