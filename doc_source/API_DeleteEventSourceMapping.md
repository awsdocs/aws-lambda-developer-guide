# DeleteEventSourceMapping<a name="API_DeleteEventSourceMapping"></a>

Deletes an [event source mapping](https://docs.aws.amazon.com/lambda/latest/dg/intro-invocation-modes.html)\. You can get the identifier of a mapping from the output of [ListEventSourceMappings](API_ListEventSourceMappings.md)\.

## Request Syntax<a name="API_DeleteEventSourceMapping_RequestSyntax"></a>

```
DELETE /2015-03-31/event-source-mappings/UUID HTTP/1.1
```

## URI Request Parameters<a name="API_DeleteEventSourceMapping_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [UUID](#API_DeleteEventSourceMapping_RequestSyntax) **   <a name="SSS-DeleteEventSourceMapping-request-UUID"></a>
The identifier of the event source mapping\.

## Request Body<a name="API_DeleteEventSourceMapping_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_DeleteEventSourceMapping_ResponseSyntax"></a>

```
HTTP/1.1 202
Content-type: application/json

{
   "[BatchSize](#SSS-DeleteEventSourceMapping-response-BatchSize)": number,
   "[EventSourceArn](#SSS-DeleteEventSourceMapping-response-EventSourceArn)": "string",
   "[FunctionArn](#SSS-DeleteEventSourceMapping-response-FunctionArn)": "string",
   "[LastModified](#SSS-DeleteEventSourceMapping-response-LastModified)": number,
   "[LastProcessingResult](#SSS-DeleteEventSourceMapping-response-LastProcessingResult)": "string",
   "[State](#SSS-DeleteEventSourceMapping-response-State)": "string",
   "[StateTransitionReason](#SSS-DeleteEventSourceMapping-response-StateTransitionReason)": "string",
   "[UUID](#SSS-DeleteEventSourceMapping-response-UUID)": "string"
}
```

## Response Elements<a name="API_DeleteEventSourceMapping_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 202 response\.

The following data is returned in JSON format by the service\.

 ** [BatchSize](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-BatchSize"></a>
The maximum number of items to retrieve in a single batch\.  
Type: Integer  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

 ** [EventSourceArn](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-EventSourceArn"></a>
The Amazon Resource Name \(ARN\) of the event source\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z0-9-]*):([a-zA-Z0-9\-])+:([a-z]{2}(-gov)?-[a-z]+-\d{1})?:(\d{12})?:(.*)` 

 ** [FunctionArn](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-FunctionArn"></a>
The ARN of the Lambda function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [LastModified](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-LastModified"></a>
The date that the event source mapping was last updated, in Unix time seconds\.  
Type: Timestamp

 ** [LastProcessingResult](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-LastProcessingResult"></a>
The result of the last AWS Lambda invocation of your Lambda function\.  
Type: String

 ** [State](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-State"></a>
The state of the event source mapping\. It can be one of the following: `Creating`, `Enabling`, `Enabled`, `Disabling`, `Disabled`, `Updating`, or `Deleting`\.  
Type: String

 ** [StateTransitionReason](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-StateTransitionReason"></a>
The cause of the last state change, either `User initiated` or `Lambda initiated`\.  
Type: String

 ** [UUID](#API_DeleteEventSourceMapping_ResponseSyntax) **   <a name="SSS-DeleteEventSourceMapping-response-UUID"></a>
The identifier of the event source mapping\.  
Type: String

## Errors<a name="API_DeleteEventSourceMapping_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

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

## See Also<a name="API_DeleteEventSourceMapping_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Go \- Pilot](https://docs.aws.amazon.com/goto/SdkForGoPilot/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/DeleteEventSourceMapping) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/DeleteEventSourceMapping) 