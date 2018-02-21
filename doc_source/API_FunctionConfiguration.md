# FunctionConfiguration<a name="API_FunctionConfiguration"></a>

A complex type that describes function metadata\.

## Contents<a name="API_FunctionConfiguration_Contents"></a>

 **CodeSha256**   <a name="SSS-Type-FunctionConfiguration-CodeSha256"></a>
It is the SHA256 hash of your function deployment package\.  
Type: String  
Required: No

 **CodeSize**   <a name="SSS-Type-FunctionConfiguration-CodeSize"></a>
The size, in bytes, of the function \.zip file you uploaded\.  
Type: Long  
Required: No

 **DeadLetterConfig**   <a name="SSS-Type-FunctionConfiguration-DeadLetterConfig"></a>
The parent object that contains the target ARN \(Amazon Resource Name\) of an Amazon SQS queue or Amazon SNS topic\. For more information, see [Dead Letter Queues](dlq.md)\.   
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object  
Required: No

 **Description**   <a name="SSS-Type-FunctionConfiguration-Description"></a>
The user\-provided description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 **Environment**   <a name="SSS-Type-FunctionConfiguration-Environment"></a>
The parent object that contains your environment's configuration settings\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object  
Required: No

 **FunctionArn**   <a name="SSS-Type-FunctionConfiguration-FunctionArn"></a>
The Amazon Resource Name \(ARN\) assigned to the function\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **FunctionName**   <a name="SSS-Type-FunctionConfiguration-FunctionName"></a>
The name of the function\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **Handler**   <a name="SSS-Type-FunctionConfiguration-Handler"></a>
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+`   
Required: No

 **KMSKeyArn**   <a name="SSS-Type-FunctionConfiguration-KMSKeyArn"></a>
The Amazon Resource Name \(ARN\) of the KMS key used to encrypt your function's environment variables\. If empty, it means you are using the AWS Lambda default service key\.  
Type: String  
Pattern: `(arn:aws:[a-z0-9-.]+:.*)|()`   
Required: No

 **LastModified**   <a name="SSS-Type-FunctionConfiguration-LastModified"></a>
The time stamp of the last time you updated the function\. The time stamp is conveyed as a string complying with ISO\-8601 in this way YYYY\-MM\-DDThh:mm:ssTZD \(e\.g\., 1997\-07\-16T19:20:30\+01:00\)\. For more information, see [Date and Time Formats](https://www.w3.org/TR/NOTE-datetime)\.  
Type: String  
Required: No

 **MasterArn**   <a name="SSS-Type-FunctionConfiguration-MasterArn"></a>
Returns the ARN \(Amazon Resource Name\) of the master function\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **MemorySize**   <a name="SSS-Type-FunctionConfiguration-MemorySize"></a>
The memory size, in MB, you configured for the function\. Must be a multiple of 64 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.  
Required: No

 **RevisionId**   <a name="SSS-Type-FunctionConfiguration-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String  
Required: No

 **Role**   <a name="SSS-Type-FunctionConfiguration-Role"></a>
The Amazon Resource Name \(ARN\) of the IAM role that Lambda assumes when it executes your function to access any other Amazon Web Services \(AWS\) resources\.  
Type: String  
Pattern: `arn:aws:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+`   
Required: No

 **Runtime**   <a name="SSS-Type-FunctionConfiguration-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | nodejs4.3-edge | go1.x`   
Required: No

 **Timeout**   <a name="SSS-Type-FunctionConfiguration-Timeout"></a>
The function execution time at which Lambda should terminate the function\. Because the execution time has cost implications, we recommend you set this value based on your expected execution time\. The default is 3 seconds\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 **TracingConfig**   <a name="SSS-Type-FunctionConfiguration-TracingConfig"></a>
The parent object that contains your function's tracing settings\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object  
Required: No

 **Version**   <a name="SSS-Type-FunctionConfiguration-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: No

 **VpcConfig**   <a name="SSS-Type-FunctionConfiguration-VpcConfig"></a>
VPC configuration associated with your Lambda function\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object  
Required: No

## See Also<a name="API_FunctionConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionConfiguration) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionConfiguration) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/FunctionConfiguration) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/FunctionConfiguration) 