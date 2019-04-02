# UpdateEventSourceMapping<a name="API_UpdateEventSourceMapping"></a>

Updates an event source mapping\. You can change the function that AWS Lambda invokes, or pause invocation and resume later from the same location\.

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
The identifier of the event source mapping\.

## Request Body<a name="API_UpdateEventSourceMapping_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [BatchSize](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
+  **Amazon Kinesis** \- Default 100\. Max 10,000\.
+  **Amazon DynamoDB Streams** \- Default 100\. Max 1,000\.
+  **Amazon Simple Queue Service** \- Default 10\. Max 10\.
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.  
Required: No

 ** [Enabled](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-Enabled"></a>
Disables the event source mapping to pause polling and invocation\.  
Type: Boolean  
Required: No

 ** [FunctionName](#API_UpdateEventSourceMapping_RequestSyntax) **   <a name="SSS-UpdateEventSourceMapping-request-FunctionName"></a>
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
The maximum number of items to retrieve in a single batch\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [EventSourceArn](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionArn](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [LastModified](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-LastModified"></a>
The date that the event source mapping was last updated, in Unix time seconds\.  
Type: Timestamp

 ** [LastProcessingResult](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String

 ** [State](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-StateTransitionReason"></a>
The cause of the last state change, either `User initiated` or `Lambda initiated`\.  
Type: String

 ** [UUID](#API_UpdateEventSourceMapping_ResponseSyntax) **   <a name="SSS-UpdateEventSourceMapping-response-UUID"></a>
The identifier of the event source mapping\.  
Type: String

## Errors<a name="API_UpdateEventSourceMapping_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **ResourceConflictException**   
The resource already exists\.  
HTTP Status Code: 409

 **ResourceInUseException**   
The operation conflicts with the resource's availability\. For example, you attempted to update an EventSource Mapping in CREATING, or tried to delete a EventSource mapping currently in the UPDATING state\.   
HTTP Status Code: 400

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
Request throughput limit exceeded\.  
HTTP Status Code: 429

## See Also<a name="API_UpdateEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UpdateEventSourceMapping) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UpdateEventSourceMapping) 