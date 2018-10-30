# UntagResource<a name="API_UntagResource"></a>

Removes tags from a Lambda function\. Requires the function ARN \(Amazon Resource Name\)\. For more information, see [Tagging Lambda Functions](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\. 

## Request Syntax<a name="API_UntagResource_RequestSyntax"></a>

```
DELETE /2017-03-31/tags/ARN?tagKeys=TagKeys HTTP/1.1
```

## URI Request Parameters<a name="API_UntagResource_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [Resource](#API_UntagResource_RequestSyntax) **   <a name="SSS-UntagResource-request-Resource"></a>
The ARN \(Amazon Resource Name\) of the function\. For more information, see [Tagging Lambda Functions](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\.  
Pattern: `arn:(aws[a-zA-Z-]*)?:lambda:[a-z]{2}(-gov)?-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** [TagKeys](#API_UntagResource_RequestSyntax) **   <a name="SSS-UntagResource-request-TagKeys"></a>
The list of tag keys to be deleted from the function\. For more information, see [Tagging Lambda Functions](https://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\.

## Request Body<a name="API_UntagResource_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_UntagResource_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_UntagResource_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_UntagResource_Errors"></a>

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
Request throughput limit exceeded  
HTTP Status Code: 429

## See Also<a name="API_UntagResource_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:
+  [AWS Command Line Interface](https://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for \.NET](https://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for C\+\+](https://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for Go](https://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for Java](https://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for JavaScript](https://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for PHP V3](https://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for Python](https://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/UntagResource) 
+  [AWS SDK for Ruby V2](https://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/UntagResource) 