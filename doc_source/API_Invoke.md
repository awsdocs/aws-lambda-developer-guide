# Invoke<a name="API_Invoke"></a>

Invokes a specific Lambda function\. For an example, see [Create the Lambda Function and Test It Manually](https://docs.aws.amazon.com/lambda/latest/dg/with-dynamodb-create-function.html#with-dbb-invoke-manually)\. 

If you are using the versioning feature, you can invoke the specific function version by providing function version or alias name that is pointing to the function version using the `Qualifier` parameter in the request\. If you don't provide the `Qualifier` parameter, the `$LATEST` version of the Lambda function is invoked\.

 If you use the `RequestResponse` \(synchronous\) invocation option, the function will be invoked only once\. If you use the `Event` \(asynchronous\) invocation option, the function will be invoked at least once in response to an event and the function must be idempotent to handle this\.

 For information about the versioning feature, see [AWS Lambda Function Versioning and Aliases](https://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:InvokeFunction` action\.

**Note**  
The `TooManyRequestsException` noted below will return the following: `ConcurrentInvocationLimitExceeded` will be returned if you have no functions with reserved concurrency and have exceeded your account concurrent limit or if a function without reserved concurrency exceeds the account's unreserved concurrency limit\. `ReservedFunctionConcurrentInvocationLimitExceeded` will be returned when a function with reserved concurrency exceeds its configured concurrency limit\. 

## Request Syntax<a name="API_Invoke_RequestSyntax"></a>

```
POST /2015-03-31/functions/FunctionName/invocations?Qualifier=Qualifier HTTP/1.1
X-Amz-Invocation-Type: InvocationType
X-Amz-Log-Type: LogType
X-Amz-Client-Context: ClientContext

Payload
```

## URI Request Parameters<a name="API_Invoke_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [ClientContext](#API_Invoke_RequestSyntax) **   <a name="SSS-Invoke-request-ClientContext"></a>
Using the `ClientContext` you can pass client\-specific information to the Lambda function you are invoking\. You can then process the client information in your Lambda function as you choose through the context variable\. For an example of a `ClientContext` JSON, see [PutEvents](https://docs.aws.amazon.com/mobileanalytics/latest/ug/PutEvents.html) in the *Amazon Mobile Analytics API Reference and User Guide*\.  
The ClientContext JSON must be base64\-encoded and has a maximum size of 3583 bytes\.  
 `ClientContext` information is returned only if you use the synchronous \(`RequestResponse`\) invocation type\.

 ** [FunctionName](#API_Invoke_RequestSyntax) **   <a name="SSS-Invoke-request-FunctionName"></a>
The Lambda function name\.  
 You can specify a function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. AWS Lambda also allows you to specify a partial ARN \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [InvocationType](#API_Invoke_RequestSyntax) **   <a name="SSS-Invoke-request-InvocationType"></a>
By default, this operation assumes a synchronous \(`RequestResponse`\) invocation type\. If the Lambda function you invoke is expected to have a long\-running execution time, your client may time out before execution completes\. To avoid this, update the client timeout\. If you are invoking the Lambda function via an SDK, please refer to the SDK documentation at the end of this section to learn more about configuring the timeout for your specific runtime\.  
 You can optionally request asynchronous execution by specifying `Event` as the `InvocationType`\. You can also use this parameter to request AWS Lambda to not execute the function but do some verification, such as if the caller is authorized to invoke the function and if the inputs are valid\. You request this by specifying `DryRun` as the `InvocationType`\. This is useful in a cross\-account scenario when you want to verify access to a function without running it\.   
Valid Values:` Event | RequestResponse | DryRun` 

 ** [LogType](#API_Invoke_RequestSyntax) **   <a name="SSS-Invoke-request-LogType"></a>
You can set this optional parameter to `Tail` in the request only if you specify the `InvocationType` parameter with value `RequestResponse`\. In this case, AWS Lambda returns the base64\-encoded last 4 KB of log data produced by your Lambda function in the `x-amz-log-result` header\.   
Valid Values:` None | Tail` 

 ** [Qualifier](#API_Invoke_RequestSyntax) **   <a name="SSS-Invoke-request-Qualifier"></a>
You can use this optional parameter to specify a Lambda function version or alias name\. If you specify a function version, the API uses the qualified function ARN to invoke a specific Lambda function\. If you specify an alias name, the API uses the alias ARN to invoke the Lambda function version to which the alias points\.  
If you don't provide this parameter, then the API uses unqualified function ARN which results in invocation of the `$LATEST` version\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_Invoke_RequestBody"></a>

The request accepts the following binary data\.

 ** [Payload](#API_Invoke_RequestSyntax) **   <a name="SSS-Invoke-request-Payload"></a>
JSON that you want to provide to your Lambda function as input\.

## Response Syntax<a name="API_Invoke_ResponseSyntax"></a>

```
HTTP/1.1 StatusCode
X-Amz-Function-Error: FunctionError
X-Amz-Log-Result: LogResult
X-Amz-Executed-Version: ExecutedVersion

Payload
```

## Response Elements<a name="API_Invoke_ResponseElements"></a>

If the action is successful, the service sends back the following HTTP response\.

 ** [StatusCode](#API_Invoke_ResponseSyntax) **   <a name="SSS-Invoke-response-StatusCode"></a>
The HTTP status code will be in the 200 range for successful request\. For the `RequestResponse` invocation type this status code will be 200\. For the `Event` invocation type this status code will be 202\. For the `DryRun` invocation type the status code will be 204\. 

The response returns the following HTTP headers\.

 ** [ExecutedVersion](#API_Invoke_ResponseSyntax) **   <a name="SSS-Invoke-response-ExecutedVersion"></a>
The function version that has been executed\. This value is returned only if the invocation type is `RequestResponse`\. For more information, see [Traffic Shifting Using Aliases](lambda-traffic-shifting-using-aliases.md)\.  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** [FunctionError](#API_Invoke_ResponseSyntax) **   <a name="SSS-Invoke-response-FunctionError"></a>
Indicates whether an error occurred while executing the Lambda function\. If an error occurred this field will have one of two values; `Handled` or `Unhandled`\. `Handled` errors are errors that are reported by the function while the `Unhandled` errors are those detected and reported by AWS Lambda\. Unhandled errors include out of memory errors and function timeouts\. For information about how to report an `Handled` error, see [Programming Model](https://docs.aws.amazon.com/lambda/latest/dg/programming-model.html)\. 

 ** [LogResult](#API_Invoke_ResponseSyntax) **   <a name="SSS-Invoke-response-LogResult"></a>
 It is the base64\-encoded logs for the Lambda function invocation\. This is present only if the invocation type is `RequestResponse` and the logs were requested\. 

The response returns the following as the HTTP body\.

 ** [Payload](#API_Invoke_ResponseSyntax) **   <a name="SSS-Invoke-response-Payload"></a>
 It is the JSON representation of the object returned by the Lambda function\. This is present only if the invocation type is `RequestResponse`\.   
In the event of a function error this field contains a message describing the error\. For the `Handled` errors the Lambda function will report this message\. For `Unhandled` errors AWS Lambda reports the message\. 

## Errors<a name="API_Invoke_Errors"></a>

 **EC2AccessDeniedException**   
  
HTTP Status Code: 502

 **EC2ThrottledException**   
AWS Lambda was throttled by Amazon EC2 during Lambda function initialization using the execution role provided for the Lambda function\.  
HTTP Status Code: 502

 **EC2UnexpectedException**   
AWS Lambda received an unexpected EC2 client exception while setting up for the Lambda function\.  
HTTP Status Code: 502

 **ENILimitReachedException**   
AWS Lambda was not able to create an Elastic Network Interface \(ENI\) in the VPC, specified as part of Lambda function configuration, because the limit for network interfaces has been reached\.  
HTTP Status Code: 502

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **InvalidRequestContentException**   
The request body could not be parsed as JSON\.  
HTTP Status Code: 400

 **InvalidRuntimeException**   
The runtime or runtime version specified is not supported\.  
HTTP Status Code: 502

 **InvalidSecurityGroupIDException**   
The Security Group ID provided in the Lambda function VPC configuration is invalid\.  
HTTP Status Code: 502

 **InvalidSubnetIDException**   
The Subnet ID provided in the Lambda function VPC configuration is invalid\.  
HTTP Status Code: 502

 **InvalidZipFileException**   
AWS Lambda could not unzip the function zip file\.  
HTTP Status Code: 502

 **KMSAccessDeniedException**   
Lambda was unable to decrypt the environment variables because KMS access was denied\. Check the Lambda function's KMS permissions\.  
HTTP Status Code: 502

 **KMSDisabledException**   
Lambda was unable to decrypt the environment variables because the KMS key used is disabled\. Check the Lambda function's KMS key settings\.  
HTTP Status Code: 502

 **KMSInvalidStateException**   
Lambda was unable to decrypt the environment variables because the KMS key used is in an invalid state for Decrypt\. Check the function's KMS key settings\.  
HTTP Status Code: 502

 **KMSNotFoundException**   
Lambda was unable to decrypt the environment variables because the KMS key was not found\. Check the function's KMS key settings\.   
HTTP Status Code: 502

 **RequestTooLargeException**   
The request payload exceeded the `Invoke` request body JSON input limit\. For more information, see [Limits](https://docs.aws.amazon.com/lambda/latest/dg/limits.html)\.   
HTTP Status Code: 413

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **SubnetIPAddressLimitReachedException**   
AWS Lambda was not able to set up VPC access for the Lambda function because one or more configured subnets has no available IP addresses\.  
HTTP Status Code: 502

 **TooManyRequestsException**   
   
HTTP Status Code: 429

 **UnsupportedMediaTypeException**   
The content type of the `Invoke` request body is not JSON\.  
HTTP Status Code: 415

## See Also<a name="API_Invoke_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/Invoke) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/Invoke) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/Invoke) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/Invoke) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/Invoke) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/Invoke) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/Invoke) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/Invoke) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/Invoke) 