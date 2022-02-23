# Using AWS Lambda with AWS CloudFormation<a name="services-cloudformation"></a>

In an AWS CloudFormation template, you can specify a Lambda function as the target of a custom resource\. Use custom resources to process parameters, retrieve configuration values, or call other AWS services during stack lifecycle events\.

The following example invokes a function that's defined elsewhere in the template\.

**Example – Custom resource definition**  

```
Resources:
  primerinvoke:
    Type: [AWS::CloudFormation::CustomResource](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-cfn-customresource.html)
    Version: "1.0"
    Properties:
      ServiceToken: !GetAtt primer.Arn
      FunctionName: !Ref randomerror
```

The service token is the Amazon Resource Name \(ARN\) of the function that AWS CloudFormation invokes when you create, update, or delete the stack\. You can also include additional properties like `FunctionName`, which AWS CloudFormation passes to your function as is\.

AWS CloudFormation invokes your Lambda function [asynchronously](invocation-async.md) with an event that includes a callback URL\.

**Example – AWS CloudFormation message event**  

```
{
    "RequestType": "Create",
    "ServiceToken": "arn:aws:lambda:us-east-2:123456789012:function:lambda-error-processor-primer-14ROR2T3JKU66",
    "ResponseURL": "https://cloudformation-custom-resource-response-useast2.s3-us-east-2.amazonaws.com/arn%3Aaws%3Acloudformation%3Aus-east-2%3A123456789012%3Astack/lambda-error-processor/1134083a-2608-1e91-9897-022501a2c456%7Cprimerinvoke%7C5d478078-13e9-baf0-464a-7ef285ecc786?AWSAccessKeyId=AKIAIOSFODNN7EXAMPLE&Expires=1555451971&Signature=28UijZePE5I4dvukKQqM%2F9Rf1o4%3D",
    "StackId": "arn:aws:cloudformation:us-east-2:123456789012:stack/lambda-error-processor/1134083a-2608-1e91-9897-022501a2c456",
    "RequestId": "5d478078-13e9-baf0-464a-7ef285ecc786",
    "LogicalResourceId": "primerinvoke",
    "ResourceType": "AWS::CloudFormation::CustomResource",
    "ResourceProperties": {
        "ServiceToken": "arn:aws:lambda:us-east-2:123456789012:function:lambda-error-processor-primer-14ROR2T3JKU66",
        "FunctionName": "lambda-error-processor-randomerror-ZWUC391MQAJK"
    }
}
```

The function is responsible for returning a response to the callback URL that indicates success or failure\. For the full response syntax, see [Custom resource response objects](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/crpg-ref-responses.html)\.

**Example – AWS CloudFormation custom resource response**  

```
{
    "Status": "SUCCESS",
    "PhysicalResourceId": "2019/04/18/[$LATEST]b3d1bfc65f19ec610654e4d9b9de47a0",
    "StackId": "arn:aws:cloudformation:us-east-2:123456789012:stack/lambda-error-processor/1134083a-2608-1e91-9897-022501a2c456",
    "RequestId": "5d478078-13e9-baf0-464a-7ef285ecc786",
    "LogicalResourceId": "primerinvoke"
}
```

AWS CloudFormation provides a library called `cfn-response` that handles sending the response\. If you define your function within a template, you can require the library by name\. AWS CloudFormation then adds the library to the deployment package that it creates for the function\.

The following example function invokes a second function\. If the call succeeds, the function sends a success response to AWS CloudFormation, and the stack update continues\. The template uses the [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html) resource type provided by AWS Serverless Application Model\.

**Example [error\-processor/template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor/template.yml) – Custom resource function**  

```
Transform: 'AWS::Serverless-2016-10-31'
Resources:
  primer:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      InlineCode: |
        var aws = require('aws-sdk');
        var response = require('cfn-response');
        exports.handler = function(event, context) {
            // For Delete requests, immediately send a SUCCESS response.
            if (event.RequestType == "Delete") {
                response.send(event, context, "SUCCESS");
                return;
            }
            var responseStatus = "FAILED";
            var responseData = {};
            var functionName = event.ResourceProperties.FunctionName
            var lambda = new aws.Lambda();
            lambda.invoke({ FunctionName: functionName }, function(err, invokeResult) {
                if (err) {
                    responseData = {Error: "Invoke call failed"};
                    console.log(responseData.Error + ":\n", err);
                }
                else responseStatus = "SUCCESS";
                response.send(event, context, responseStatus, responseData);
            });
        };
      Description: Invoke a function to create a log stream.
      MemorySize: 128
      Timeout: 8
      Role: !GetAtt role.Arn
      Tracing: Active
```

If the function that the custom resource invokes isn't defined in a template, you can get the source code for `cfn-response` from [cfn\-response module](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-lambda-function-code-cfnresponsemodule.html) in the AWS CloudFormation User Guide\.

For a sample application that uses a custom resource to ensure that a function's log group is created before another resource that depends on it, see [Error processor sample application for AWS Lambda](samples-errorprocessor.md)\.

For more information about custom resources, see [Custom resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources.html) in the *AWS CloudFormation User Guide*\.