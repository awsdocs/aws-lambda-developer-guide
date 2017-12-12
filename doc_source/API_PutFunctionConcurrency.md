# PutFunctionConcurrency<a name="API_PutFunctionConcurrency"></a>

Sets a limit on the number of concurrent executions available to this function\. It is a subset of your account's total concurrent execution limit per region\. Note that Lambda automatically reserves a buffer of 100 concurrent executions for functions without any reserved concurrency limit\. This means if your account limit is 1000, you have a total of 900 available to allocate to individual functions\. For more information, see [Managing Concurrency](concurrent-executions.md)\.

## Request Syntax<a name="API_PutFunctionConcurrency_RequestSyntax"></a>

```
PUT /2017-10-31/functions/FunctionName/concurrency HTTP/1.1
Content-type: application/json

{
   "ReservedConcurrentExecutions": number
}
```

## URI Request Parameters<a name="API_PutFunctionConcurrency_RequestParameters"></a>

The request requires the following URI parameters\.

 ** FunctionName **   
The name of the function you are setting concurrent execution limits on\. For more information, see [Managing Concurrency](concurrent-executions.md)\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_PutFunctionConcurrency_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** ReservedConcurrentExecutions **   
The concurrent execution limit reserved for this function\. For more information, see [Managing Concurrency](concurrent-executions.md)\.  
Type: Integer  
Valid Range: Minimum value of 0\.  
Required: Yes

## Response Syntax<a name="API_PutFunctionConcurrency_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "ReservedConcurrentExecutions": number
}
```

## Response Elements<a name="API_PutFunctionConcurrency_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** ReservedConcurrentExecutions **   
The number of concurrent executions reserved for this function\. For more information, see [Managing Concurrency](concurrent-executions.md)\.  
Type: Integer  
Valid Range: Minimum value of 0\.

## Errors<a name="API_PutFunctionConcurrency_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **ResourceNotFoundException**   
The resource \(for example, a Lambda function or access policy statement\) specified in the request does not exist\.  
HTTP Status Code: 404

 **ServiceException**   
The AWS Lambda service encountered an internal error\.  
HTTP Status Code: 500

 **TooManyRequestsException**   
   
HTTP Status Code: 429

## See Also<a name="API_PutFunctionConcurrency_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/PutFunctionConcurrency) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/PutFunctionConcurrency) 