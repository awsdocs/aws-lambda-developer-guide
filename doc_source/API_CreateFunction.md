# CreateFunction<a name="API_CreateFunction"></a>

Creates a new Lambda function\. The function metadata is created from the request parameters, and the code for the function is provided by a \.zip file in the request body\. If the function name already exists, the operation will fail\. Note that the function name is case\-sensitive\.

 If you are using versioning, you can also publish a version of the Lambda function you are creating using the `Publish` parameter\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:CreateFunction` action\.

## Request Syntax<a name="API_CreateFunction_RequestSyntax"></a>

```
POST /2015-03-31/functions HTTP/1.1
Content-type: application/json

{
   "Code": { 
      "S3Bucket": "string",
      "S3Key": "string",
      "S3ObjectVersion": "string",
      "ZipFile": blob
   },
   "DeadLetterConfig": { 
      "TargetArn": "string"
   },
   "Description": "string",
   "Environment": { 
      "Variables": { 
         "string" : "string" 
      }
   },
   "FunctionName": "string",
   "Handler": "string",
   "KMSKeyArn": "string",
   "MemorySize": number,
   "Publish": boolean,
   "Role": "string",
   "Runtime": "string",
   "Tags": { 
      "string" : "string" 
   },
   "Timeout": number,
   "TracingConfig": { 
      "Mode": "string"
   },
   "VpcConfig": { 
      "SecurityGroupIds": [ "string" ],
      "SubnetIds": [ "string" ]
   }
}
```

## URI Request Parameters<a name="API_CreateFunction_RequestParameters"></a>

The request does not use any URI parameters\.

## Request Body<a name="API_CreateFunction_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** Code **   
The code for the Lambda function\.  
Type: [FunctionCode](API_FunctionCode.md) object  
Required: Yes

 ** DeadLetterConfig **   
The parent object that contains the target ARN \(Amazon Resource Name\) of an Amazon SQS queue or Amazon SNS topic\.   
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object  
Required: No

 ** Description **   
A short, user\-defined function description\. Lambda does not use this value\. Assign a meaningful description as you see fit\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Required: No

 ** Environment **   
The parent object that contains your environment's configuration settings\.  
Type: [Environment](API_Environment.md) object  
Required: No

 ** FunctionName **   
The name you want to assign to the function you are uploading\. The function names appear in the console and are returned in the [ListFunctions](API_ListFunctions.md) API\. Function names are used to specify functions to other AWS Lambda API operations, such as [Invoke](API_Invoke.md)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?`   
Required: Yes

 ** Handler **   
The function within your code that Lambda calls to begin execution\. For Node\.js, it is the *module\-name*\.*export* value in your function\. For Java, it can be `package.class-name::handler` or `package.class-name`\. For more information, see [Lambda Function Handler \(Java\)](http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-handler-types.html)\.   
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+`   
Required: Yes

 ** KMSKeyArn **   
The Amazon Resource Name \(ARN\) of the KMS key used to encrypt your function's environment variables\. If not provided, AWS Lambda will use a default service key\.  
Type: String  
Pattern: `(arn:aws:[a-z0-9-.]+:.*)|()`   
Required: No

 ** MemorySize **   
The amount of memory, in MB, your Lambda function is given\. Lambda uses this memory size to infer the amount of CPU and memory allocated to your function\. Your function use\-case determines your CPU and memory requirements\. For example, a database operation might need less memory compared to an image processing function\. The default value is 128 MB\. The value must be a multiple of 64 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.  
Required: No

 ** Publish **   
This boolean parameter can be used to request AWS Lambda to create the Lambda function and publish a version as an atomic operation\.  
Type: Boolean  
Required: No

 ** Role **   
The Amazon Resource Name \(ARN\) of the IAM role that Lambda assumes when it executes your function to access any other Amazon Web Services \(AWS\) resources\. For more information, see [AWS Lambda: How it Works](http://docs.aws.amazon.com/lambda/latest/dg/lambda-introduction.html)\.   
Type: String  
Pattern: `arn:aws:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+`   
Required: Yes

 ** Runtime **   
The runtime environment for the Lambda function you are uploading\.  
To use the Python runtime v3\.6, set the value to "python3\.6"\. To use the Python runtime v2\.7, set the value to "python2\.7"\. To use the Node\.js runtime v6\.10, set the value to "nodejs6\.10"\. To use the Node\.js runtime v4\.3, set the value to "nodejs4\.3"\.  
Node v0\.10\.42 is currently marked as deprecated\. You must migrate existing functions to the newer Node\.js runtime versions available on AWS Lambda \(nodejs4\.3 or nodejs6\.10\) as soon as possible\. Failure to do so will result in an invalid parmaeter error being returned\. Note that you will have to follow this procedure for each region that contains functions written in the Node v0\.10\.42 runtime\.
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | nodejs4.3-edge`   
Required: Yes

 ** Tags **   
The list of tags \(key\-value pairs\) assigned to the new function\.  
Type: String to string map  
Required: No

 ** Timeout **   
The function execution time at which Lambda should terminate the function\. Because the execution time has cost implications, we recommend you set this value based on your expected execution time\. The default is 3 seconds\.  
Type: Integer  
Valid Range: Minimum value of 1\.  
Required: No

 ** TracingConfig **   
The parent object that contains your function's tracing settings\.  
Type: [TracingConfig](API_TracingConfig.md) object  
Required: No

 ** VpcConfig **   
If your Lambda function accesses resources in a VPC, you provide this parameter identifying the list of security group IDs and subnet IDs\. These must belong to the same VPC\. You must provide at least one security group and one subnet ID\.  
Type: [VpcConfig](API_VpcConfig.md) object  
Required: No

## Response Syntax<a name="API_CreateFunction_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "CodeSha256": "string",
   "CodeSize": number,
   "DeadLetterConfig": { 
      "TargetArn": "string"
   },
   "Description": "string",
   "Environment": { 
      "Error": { 
         "ErrorCode": "string",
         "Message": "string"
      },
      "Variables": { 
         "string" : "string" 
      }
   },
   "FunctionArn": "string",
   "FunctionName": "string",
   "Handler": "string",
   "KMSKeyArn": "string",
   "LastModified": "string",
   "MasterArn": "string",
   "MemorySize": number,
   "Role": "string",
   "Runtime": "string",
   "Timeout": number,
   "TracingConfig": { 
      "Mode": "string"
   },
   "Version": "string",
   "VpcConfig": { 
      "SecurityGroupIds": [ "string" ],
      "SubnetIds": [ "string" ],
      "VpcId": "string"
   }
}
```

## Response Elements<a name="API_CreateFunction_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** CodeSha256 **   
It is the SHA256 hash of your function deployment package\.  
Type: String

 ** CodeSize **   
The size, in bytes, of the function \.zip file you uploaded\.  
Type: Long

 ** DeadLetterConfig **   
The parent object that contains the target ARN \(Amazon Resource Name\) of an Amazon SQS queue or Amazon SNS topic\.  
Type: [DeadLetterConfig](API_DeadLetterConfig.md) object

 ** Description **   
The user\-provided description\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.

 ** Environment **   
The parent object that contains your environment's configuration settings\.  
Type: [EnvironmentResponse](API_EnvironmentResponse.md) object

 ** FunctionArn **   
The Amazon Resource Name \(ARN\) assigned to the function\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_\.]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** FunctionName **   
The name of the function\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 170\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_\.]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** Handler **   
The function Lambda calls to begin executing your function\.  
Type: String  
Length Constraints: Maximum length of 128\.  
Pattern: `[^\s]+` 

 ** KMSKeyArn **   
The Amazon Resource Name \(ARN\) of the KMS key used to encrypt your function's environment variables\. If empty, it means you are using the AWS Lambda default service key\.  
Type: String  
Pattern: `(arn:aws:[a-z0-9-.]+:.*)|()` 

 ** LastModified **   
The time stamp of the last time you updated the function\. The time stamp is conveyed as a string complying with ISO\-8601 in this way YYYY\-MM\-DDThh:mm:ssTZD \(e\.g\., 1997\-07\-16T19:20:30\+01:00\)\. For more information, see [Date and Time Formats](https://www.w3.org/TR/NOTE-datetime)\.  
Type: String

 ** MasterArn **   
Returns the ARN \(Amazon Resource Name\) of the master function\.  
Type: String  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** MemorySize **   
The memory size, in MB, you configured for the function\. Must be a multiple of 64 MB\.  
Type: Integer  
Valid Range: Minimum value of 128\. Maximum value of 3008\.

 ** Role **   
The Amazon Resource Name \(ARN\) of the IAM role that Lambda assumes when it executes your function to access any other Amazon Web Services \(AWS\) resources\.  
Type: String  
Pattern: `arn:aws:iam::\d{12}:role/?[a-zA-Z_0-9+=,.@\-_/]+` 

 ** Runtime **   
The runtime environment for the Lambda function\.  
Type: String  
Valid Values:` nodejs | nodejs4.3 | nodejs6.10 | java8 | python2.7 | python3.6 | dotnetcore1.0 | nodejs4.3-edge` 

 ** Timeout **   
The function execution time at which Lambda should terminate the function\. Because the execution time has cost implications, we recommend you set this value based on your expected execution time\. The default is 3 seconds\.  
Type: Integer  
Valid Range: Minimum value of 1\.

 ** TracingConfig **   
The parent object that contains your function's tracing settings\.  
Type: [TracingConfigResponse](API_TracingConfigResponse.md) object

 ** Version **   
The version of the Lambda function\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** VpcConfig **   
VPC configuration associated with your Lambda function\.  
Type: [VpcConfigResponse](API_VpcConfigResponse.md) object

## Errors<a name="API_CreateFunction_Errors"></a>

 **CodeStorageExceededException**   
You have exceeded your maximum total code size per account\. [Limits](http://docs.aws.amazon.com/lambda/latest/dg/limits.html)   
HTTP Status Code: 400

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

## See Also<a name="API_CreateFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/CreateFunction) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/CreateFunction) 