# DeleteFunction<a name="API_DeleteFunction"></a>

Deletes the specified Lambda function code and configuration\.

If you are using the versioning feature and you don't specify a function version in your `DeleteFunction` request, AWS Lambda will delete the function, including all its versions, and any aliases pointing to the function versions\. To delete a specific function version, you must provide the function version via the `Qualifier` parameter\. For information about function versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

When you delete a function the associated resource policy is also deleted\. You will need to delete the event source mappings explicitly\.

This operation requires permission for the `lambda:DeleteFunction` action\.

## Request Syntax<a name="API_DeleteFunction_RequestSyntax"></a>

```
DELETE /2015-03-31/functions/FunctionName?Qualifier=Qualifier HTTP/1.1
```

## URI Request Parameters<a name="API_DeleteFunction_RequestParameters"></a>

The request requires the following URI parameters\.

 ** FunctionName **   
The Lambda function to delete\.  
 You can specify the function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. If you are using versioning, you can also provide a qualified function ARN \(ARN that is qualified with function version or alias name as suffix\)\. AWS Lambda also allows you to specify only the function name with the account ID qualifier \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** Qualifier **   
Using this optional parameter you can specify a function version \(but not the `$LATEST` version\) to direct AWS Lambda to delete a specific function version\. If the function version has one or more aliases pointing to it, you will get an error because you cannot have aliases pointing to it\. You can delete any function version but not the `$LATEST`, that is, you cannot specify `$LATEST` as the value of this parameter\. The `$LATEST` version can be deleted only when you want to delete all the function versions and aliases\.  
You can only specify a function version, not an alias name, using this parameter\. You cannot delete a function version using its alias\.  
If you don't specify this parameter, AWS Lambda will delete the function, including all of its versions and aliases\.  
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_DeleteFunction_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_DeleteFunction_ResponseSyntax"></a>

```
HTTP/1.1 204
```

## Response Elements<a name="API_DeleteFunction_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 204 response with an empty HTTP body\.

## Errors<a name="API_DeleteFunction_Errors"></a>

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

## See Also<a name="API_DeleteFunction_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/DeleteFunction) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/DeleteFunction) 