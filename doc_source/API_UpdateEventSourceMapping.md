# UpdateEventSourceMapping<a name="API_UpdateEventSourceMapping"></a>

You can update an event source mapping\. This is useful if you want to change the parameters of the existing mapping without losing your position in the stream\. You can change which function will receive the stream records, but to change the stream itself, you must create a new mapping\.

If you are using the versioning feature, you can update the event source mapping to map to a specific Lambda function version or alias as described in the `FunctionName` parameter\. For information about the versioning feature, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

If you disable the event source mapping, AWS Lambda stops polling\. If you enable again, it will resume polling from the time it had stopped polling, so you don't lose processing of any records\. However, if you delete event source mapping and create it again, it will reset\.

This operation requires permission for the `lambda:UpdateEventSourceMapping` action\.

## Request Syntax<a name="API_UpdateEventSourceMapping_RequestSyntax"></a>

```
PUT /2015-03-31/event-source-mappings/UUID HTTP/1.1
Content-type: application/json

{
   "[BatchSize](#SSS-UpdateEventSourceMapping-request-BatchSize)": number,
   "[Enabled](#SSS-UpdateEventSourceMapping-request-Enabled)": boolean,
   "[FunctionName](#SSS-UpdateEventSourceMapping-request-FunctionName)": "string"
}
```

## URI Request Parameters<a name="API_UpdateEventSourceMapping_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [UUID](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-UUID"></a>
The event source mapping identifier\.

## Request Body<a name="API_UpdateEventSourceMapping_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [BatchSize](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-BatchSize"></a>
The maximum number of stream records that can be sent to your Lambda function for a single invocation\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** [Enabled](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-Enabled"></a>
Specifies whether AWS Lambda should actively poll the stream or not\. If disabled, AWS Lambda will not poll the stream\.  
Type: Boolean  
Required: No

 ** [FunctionName](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-FunctionName"></a>
The Lambda function to which you want the stream records sent\.  
 You can specify a function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. AWS Lambda also allows you to specify a partial ARN \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
If you are using versioning, you can also provide a qualified function ARN \(ARN that is qualified with function version or alias name as suffix\)\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)   
Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 character in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

## Response Syntax<a name="API_UpdateEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 202
Content-type: application/json

{
   "[BatchSize](#SSS-UpdateEventSourceMapping-response-BatchSize)": number,
   "[EventSourceArn](#SSS-UpdateEventSourceMapping-response-EventSourceArn)": "string",
   "[FunctionArn](#SSS-UpdateEventSourceMapping-response-FunctionArn)": "string",
   "[LastModified](#SSS-UpdateEventSourceMapping-response-LastModified)": number,
   "[LastProcessingResult](#SSS-UpdateEventSourceMapping-response-LastProcessingResult)": "string",
   "[State](#SSS-UpdateEventSourceMapping-response-State)": "string",
   "[StateTransitionReason](#SSS-UpdateEventSourceMapping-response-StateTransitionReason)": "string",
   "[UUID](#SSS-UpdateEventSourceMapping-response-UUID)": "string"
}
```

## Response Elements<a name="API_UpdateEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** [BatchSize](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-BatchSize"></a>
The largest number of records that AWS Lambda will retrieve from your event source at the time of invoking your function\. Your function receives an event with all the retrieved records\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [EventSourceArn](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the Amazon Kinesis or DynamoDB stream that is the source of events\.  
Type: String  
Pattern: `arn:aws:([a-zA-Z0-9\-])+:([a-z]{2}-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionArn](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-FunctionArn"></a>
The Lambda function to invoke when AWS Lambda detects an event on the poll\-based source\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [LastModified](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-LastModified"></a>
The UTC time string indicating the last time the event mapping was updated\.  
Type: Timestamp

 ** [LastProcessingResult](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\. This value will be null if an SQS queue is the event source\.  
Type: String

 ** [State](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be `Creating`, `Enabled`, `Disabled`, `Enabling`, `Disabling`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-StateTransitionReason"></a>
The reason the event source mapping is in its current state\. It is either user\-requested or an AWS Lambda\-initiated state transition\.  
Type: String

 ** [UUID](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-UUID"></a>
The AWS Lambda assigned opaque identifier for the mapping\.  
Type: String

## Errors<a name="API_UpdateEventSourceMapping_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **ResourceConflictException**   
The resource already exists\.  
HTTP Status Code: 409

 **ResourceInUseException**   
The operation conflicts with the resource's availability\. For example, you attempted to update an EventSoure Mapping in CREATING, or tried to delete a EventSoure mapping currently in the UPDATING state\.   
HTTP Status Code: 400

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
   
HTTP Status Code: 429

## See Also<a name="API_UpdateEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UpdateEventSourceMapping) 