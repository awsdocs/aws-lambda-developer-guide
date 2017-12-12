# ListAliases<a name="API_ListAliases"></a>

Returns list of aliases created for a Lambda function\. For each alias, the response includes information such as the alias ARN, description, alias name, and the function version to which it points\. For more information, see [Introduction to AWS Lambda Aliases](http://docs.aws.amazon.com/lambda/latest/dg/aliases-intro.html)\.

This requires permission for the lambda:ListAliases action\.

## Request Syntax<a name="API_ListAliases_RequestSyntax"></a>

```
GET /2015-03-31/functions/FunctionName/aliases?FunctionVersion=FunctionVersion&Marker=Marker&MaxItems=MaxItems HTTP/1.1
```

## URI Request Parameters<a name="API_ListAliases_RequestParameters"></a>

The request requires the following URI parameters\.

 ** FunctionName **   
Lambda function name for which the alias is created\. Note that the length constraint applies only to the ARN\. If you specify only the function name, it is limited to 64 characters in length\.  
Length Constraints: Minimum length of 1\. Maximum length of 140\.  
Pattern: `(arn:aws:lambda:)?([a-z]{2}-[a-z]+-\d{1}:)?(\d{12}:)?(function:)?([a-zA-Z0-9-_]+)(:(\$LATEST|[a-zA-Z0-9-_]+))?` 

 ** FunctionVersion **   
If you specify this optional parameter, the API returns only the aliases that are pointing to the specific Lambda function version, otherwise the API returns all of the aliases created for the Lambda function\.  
Length Constraints: Minimum length of 1\. Maximum length of 1024\.  
Pattern: `(\$LATEST|[0-9]+)` 

 ** Marker **   
Optional string\. An opaque pagination token returned from a previous `ListAliases` operation\. If present, indicates where to continue the listing\.

 ** MaxItems **   
Optional integer\. Specifies the maximum number of aliases to return in response\. This parameter value must be greater than 0\.  
Valid Range: Minimum value of 1\. Maximum value of 10000\.

## Request Body<a name="API_ListAliases_RequestBody"></a>

The request does not have a request body\.

## Response Syntax<a name="API_ListAliases_ResponseSyntax"></a>

```
HTTP/1.1 200
Content-type: application/json

{
   "Aliases": [ 
      { 
         "AliasArn": "string",
         "Description": "string",
         "FunctionVersion": "string",
         "Name": "string",
         "RoutingConfig": { 
            "AdditionalVersionWeights": { 
               "string" : number 
            }
         }
      }
   ],
   "NextMarker": "string"
}
```

## Response Elements<a name="API_ListAliases_ResponseElements"></a>

If the action is successful, the service sends back an HTTP 200 response\.

The following data is returned in JSON format by the service\.

 ** Aliases **   
A list of aliases\.  
Type: Array of [AliasConfiguration](API_AliasConfiguration.md) objects

 ** NextMarker **   
A string, present if there are more aliases\.  
Type: String

## Errors<a name="API_ListAliases_Errors"></a>

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

## See Also<a name="API_ListAliases_SeeAlso"></a>

For more information about using this API in one of the language\-specific AWS SDKs, see the following:

+  [AWS Command Line Interface](http://docs.aws.amazon.com/goto/aws-cli/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for \.NET](http://docs.aws.amazon.com/goto/DotNetSDKV3/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for C\+\+](http://docs.aws.amazon.com/goto/SdkForCpp/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for Go](http://docs.aws.amazon.com/goto/SdkForGoV1/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for Java](http://docs.aws.amazon.com/goto/SdkForJava/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for JavaScript](http://docs.aws.amazon.com/goto/AWSJavaScriptSDK/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for PHP V3](http://docs.aws.amazon.com/goto/SdkForPHPV3/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for Python](http://docs.aws.amazon.com/goto/boto3/lambda-2015-03-31/ListAliases) 

+  [AWS SDK for Ruby V2](http://docs.aws.amazon.com/goto/SdkForRubyV2/lambda-2015-03-31/ListAliases) 