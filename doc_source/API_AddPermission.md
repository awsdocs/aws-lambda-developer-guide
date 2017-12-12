# AddPermission<a name="API_AddPermission"></a>

Adds a permission to the resource policy associated with the specified AWS Lambda function\. You use resource policies to grant permissions to event sources that use *push* model\. In a *push* model, event sources \(such as Amazon S3 and custom applications\) invoke your Lambda function\. Each permission you add to the resource policy allows an event source, permission to invoke the Lambda function\. 

For information about the push model, see [AWS Lambda: How it Works](http://docs.aws.amazon.com/lambda/latest/dg/lambda-introduction.html)\. 

If you are using versioning, the permissions you add are specific to the Lambda function version or alias you specify in the `AddPermission` request via the `Qualifier` parameter\. For more information about versioning, see [AWS Lambda Function Versioning and Aliases](http://docs.aws.amazon.com/lambda/latest/dg/versioning-aliases.html)\. 

This operation requires permission for the `lambda:AddPermission` action\.

## Request Syntax<a name="API_AddPermission_RequestSyntax"></a>

```
POST /2015-03-31/functions/FunctionName/policy?Qualifier=Qualifier HTTP/1.1
Content-type: application/json

{
   "Action": "string",
   "EventSourceToken": "string",
   "Principal": "string",
   "SourceAccount": "string",
   "SourceArn": "string",
   "StatementId": "string"
}
```

## URI Request Parameters<a name="API_AddPermission_RequestParameters"></a>

The request requires the following URI parameters\.

 ** FunctionName **   
Name of the Lambda function whose resource policy you are updating by adding a new permission\.  
 You can specify a function name \(for example, `Thumbnail`\) or you can specify Amazon Resource Name \(ARN\) of the function \(for example, `arn:aws:lambda:us-west-2:account-id:function:ThumbNail`\)\. AWS Lambda also allows you to specify partial ARN \(for example, `account-id:Thumbnail`\)\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.   
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** Qualifier **   
You can use this optional query parameter to describe a qualified ARN using a function version or an alias name\. The permission will then apply to the specific qualified ARN\. For example, if you specify function version 2 as the qualifier, then permission applies only when request is made using qualified function ARN:  
 `arn:aws:lambda:aws-region:acct-id:function:function-name:2`   
If you specify an alias name, for example `PROD`, then the permission is valid only for requests made using the alias ARN:  
 `arn:aws:lambda:aws-region:acct-id:function:function-name:PROD`   
If the qualifier is not specified, the permission is valid only when requests is made using unqualified function ARN\.  
 `arn:aws:lambda:aws-region:acct-id:function:function-name`   
Length Constraints: Minimum length of 1\. Maximum length of 128\.  
Pattern: `(|[a-zA-Z0-9$_-]+)` 

## Request Body<a name="API_AddPermission_RequestBody"></a>

The request accepts the following data in JSON format\.

 ** Action **   
The AWS Lambda action you want to allow in this statement\. Each Lambda action is a string starting with `lambda:` followed by the API name \(see [Actions](http://docs.aws.amazon.com/lambda/latest/dg/API_Operations.html)\) \. For example, `lambda:CreateFunction`\. You can use wildcard \(`lambda:*`\) to grant permission for all AWS Lambda actions\.   
Type: String  
Pattern: `(lambda:[*]|lambda:[a-zA-Z]+|[*])`   
Required: Yes

 ** EventSourceToken **   
A unique token that must be supplied by the principal invoking the function\. This is currently only used for Alexa Smart Home functions\.  
Type: String  
Length Constraints: Minimum length of 0\. Maximum length of 256\.  
Pattern: `[a-zA-Z0-9._\-]+`   
Required: No

 ** Principal **   
The principal who is getting this permission\. It can be Amazon S3 service Principal \(`s3.amazonaws.com`\) if you want Amazon S3 to invoke the function, an AWS account ID if you are granting cross\-account permission, or any valid AWS service principal such as `sns.amazonaws.com`\. For example, you might want to allow a custom application in another AWS account to push events to AWS Lambda by invoking your function\.   
Type: String  
Pattern: `.*`   
Required: Yes

 ** SourceAccount **   
This parameter is used for S3 and SES\. The AWS account ID \(without a hyphen\) of the source owner\. For example, if the `SourceArn` identifies a bucket, then this is the bucket owner's account ID\. You can use this additional condition to ensure the bucket you specify is owned by a specific account \(it is possible the bucket owner deleted the bucket and some other AWS account created the bucket\)\. You can also use this condition to specify all sources \(that is, you don't specify the `SourceArn`\) owned by a specific account\.   
Type: String  
Pattern: `\d{12}`   
Required: No

 ** SourceArn **   
This is optional; however, when granting permission to invoke your function, you should specify this field with the Amazon Resource Name \(ARN\) as its value\. This ensures that only events generated from the specified source can invoke the function\.  
If you add a permission without providing the source ARN, any AWS account that creates a mapping to your function ARN can send events to invoke your Lambda function\.
Type: String  
Pattern: `arn:aws:([a-zA-Z0-9\-])+:([a-z]{2}-[a-z]+-\d{1})?:(\d{12})?:(.*)`   
Required: No

 ** StatementId **   
A unique statement identifier\.  
Type: String  
Length Constraints: Minimum length of 1\. Maximum length of 100\.  
Pattern: `([a-zA-Z0-9-_]+)`   
Required: Yes

## Response Syntax<a name="API_AddPermission_ResponseSyntax"></a>

```
HTTP/1.1 201
Content-type: application/json

{
   "Statement": "string"
}
```

## Response Elements<a name="API_AddPermission_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 201 response\.

The following data is returned in JSON format by the service\.

 ** Statement **   
The permission statement you specified in the request\. The response returns the same as a string using a backslash \("\\"\) as an escape character in the JSON\.  
Type: String

## Errors<a name="API_AddPermission_Errors"></a>

 **InvalidParameterValueException**   
One of the parameters in the request is invalid\. For example, if you provided an IAM role for AWS Lambda to assume in the `CreateFunction` or the `UpdateFunctionConfiguration` API, that AWS Lambda is unable to assume you will get this exception\.  
HTTP Status Code: 400

 **PolicyLengthExceededException**   
Lambda function access policy is limited to 20 KB\.  
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

## See Also<a name="API_AddPermission_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/AddPermission) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/AddPermission) 