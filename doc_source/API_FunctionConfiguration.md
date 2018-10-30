# FunctionConfiguration<a name="API_FunctionConfiguration"></a>

A Lambda function's configuration settings\.

## Contents<a name="API_FunctionConfiguration_Contents"></a>

 **CodeSha256**   <a name="SSS-Type-FunctionConfiguration-CodeSha256"></a>
The SHA256 hash of the function's deployment package\.  
Type: String  
Required: No

 **CodeSize**   <a name="SSS-Type-FunctionConfiguration-CodeSize"></a>
The size of the function's deployment package in bytes\.  
Type: Long  
Required: No

 **DeadLetterConfig**   <a name="SSS-Type-FunctionConfiguration-DeadLetterConfig"></a>
The function's dead letter queue\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object  
Required: No

 **Description**   <a name="SSS-Type-FunctionConfiguration-Description"></a>
The function's description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 **Environment**   <a name="SSS-Type-FunctionConfiguration-Environment"></a>
The function's environment variables\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object  
Required: No

 **FunctionArn**   <a name="SSS-Type-FunctionConfiguration-FunctionArn"></a>
The function's Amazon Resource Name\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **FunctionName**   <a name="SSS-Type-FunctionConfiguration-FunctionName"></a>
The name of the function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:(aws[a-zA-Z-]*)?:lambda:)?([a-z]{2}(-gov)?-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **Handler**   <a name="SSS-Type-FunctionConfiguration-Handler"></a>
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+`   
Required: No

 **KMSKeyArn**   <a name="SSS-Type-FunctionConfiguration-KMSKeyArn"></a>
The KMS key used to encrypt the function's environment variables\. Only returned if you've configured a customer managed CMK\.  
Type: String  
Pattern: `(arn:(aws[a-zA-Z-]*)?:[a-z0-9-.]+:.*)|()`   
Required: No

 **LastModified**   <a name="SSS-Type-FunctionConfiguration-LastModified"></a>
The date and time that the function was last updated, in [ISO\-8601 format](https://www.w3.org/TR/NOTE-datetime) \(YYYY\-MM\-DDThh:mm:ssTZD\)\.  
Type: String  
Required: No

 **MasterArn**   <a name="SSS-Type-FunctionConfiguration-MasterArn"></a>
The ARN of the master function\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: No

 **MemorySize**   <a name="SSS-Type-FunctionConfiguration-MemorySize"></a>
The memory allocated to the function  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.  
Required: No

 **RevisionId**   <a name="SSS-Type-FunctionConfiguration-RevisionId"></a>
Represents the latest updated revision of the function or alias\.  
Type: String  
Required: No

 **Role**   <a name="SSS-Type-FunctionConfiguration-Role"></a>
The function's execution role\.  
Type: String  
Pattern: `arn:(aws[a-zA-Z-]*)?:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+`   
Required: No

 **Runtime**   <a name="SSS-Type-FunctionConfiguration-Runtime"></a>
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | nodejs8.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | dotnetcore2.0 | dotnetcore2.1 | nodejs4.3-edge | go1.x`   
Required: No

 **Timeout**   <a name="SSS-Type-FunctionConfiguration-Timeout"></a>
The amount of time that Lambda allows a function to run before terminating it\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 **TracingConfig**   <a name="SSS-Type-FunctionConfiguration-TracingConfig"></a>
The function's AWS X\-Ray tracing configuration\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object  
Required: No

 **Version**   <a name="SSS-Type-FunctionConfiguration-Version"></a>
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)`   
Required: No

 **VpcConfig**   <a name="SSS-Type-FunctionConfiguration-VpcConfig"></a>
The function's networking configuration\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object  
Required: No

## See Also<a name="API_FunctionConfiguration_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/FunctionConfiguration) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/FunctionConfiguration) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/FunctionConfiguration) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/FunctionConfiguration) 