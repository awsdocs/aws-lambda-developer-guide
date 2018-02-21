# TagResource<a name="API_TagResource"></a>

Creates a list of tags \(key\-value pairs\) on the Lambda function\. Requires the Lambda function ARN \(Amazon Resource Name\)\. If a key is specified without a value, Lambda creates a tag with the specified key and a value of null\. For more information, see [Tagging Lambda Functions](http://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\. 

## Request Syntax<a name="API_TagResource_RequestSyntax"></a>

```
POST /2017-03-31/tags/ARN HTTP/1.1
Content-type: application/json

{
   "[Tags](#SSS-TagResource-request-Tags)": { 
      "string" : "string" 
   }
}
```

## URI Request Parameters<a name="API_TagResource_RequestParameters"></a>

The request requires the following URI parameters\.

 ** [Resource](#API_TagResource_RequestSyntax) **   <a name="SSS-TagResource-request-Resource"></a>
The ARN \(Amazon Resource Name\) of the Lambda function\. For more information, see [Tagging Lambda Functions](http://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\.  
Pattern: `arn:aws:lambda:[a-z]{2}-[a-z]+-\d{1}:\d{12}:function:[a-zA-Z0-9-_]+(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

## Request Body<a name="API_TagResource_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** [Tags](#API_TagResource_RequestSyntax) **   <a name="SSS-TagResource-request-Tags"></a>
The list of tags \(key\-value pairs\) you are assigning to the Lambda function\. For more information, see [Tagging Lambda Functions](http://docs.aws.amazon.com/lambda/latest/dg/tagging.html) in the **AWS Lambda Developer Guide**\.  
Type: String to string map  
Required: Yes

## Response Syntax<a name="API_TagResource_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_TagResource_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_TagResource_Errors"></a>

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

## See Also<a name="API_TagResource_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/TagResource) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/TagResource) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/TagResource) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/TagResource) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/TagResource) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/TagResource) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/TagResource) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/TagResource) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/TagResource) 