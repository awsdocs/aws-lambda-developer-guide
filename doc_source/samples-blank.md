# Blank function sample application for AWS Lambda<a name="samples-blank"></a>

The blank function sample application is a starter application that demonstrates common operations in Lambda with a function that calls the Lambda API\. It shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\. Explore this application to learn about building Lambda functions in your programming language, or use it as a starting point for your own projects\.

Variants of this sample application are available for the following languages:

**Variants**
+ Node\.js – [blank\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs)\.
+ Python – [blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-python)\.
+ Ruby – [blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-ruby)\.
+ Java – [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-java)\.
+ Go – [blank\-go](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-go)\.
+ C\# – [blank\-csharp](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-csharp)\.
+ PowerShell – [blank\-powershell](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-powershell)\.

The examples in this topic highlight code from the Node\.js version, but the details are generally applicable to all variants\.

You can deploy the sample in a few minutes with the AWS CLI and AWS CloudFormation\. Follow the instructions in the [README](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs) to download, configure, and deploy it in your account\.

**Topics**
+ [Architecture and handler code](#samples-blank-architecture)
+ [Deployment automation with AWS CloudFormation and the AWS CLI](#samples-blank-automation)
+ [Instrumentation with the AWS X\-Ray](#samples-blank-instrumentation)
+ [Dependency management with layers](#samples-blank-dependencies)

## Architecture and handler code<a name="samples-blank-architecture"></a>

The sample application consists of function code, an AWS CloudFormation template, and supporting resources\. When you deploy the sample, you use the following AWS services:
+ AWS Lambda – Runs function code, sends logs to CloudWatch Logs, and sends trace data to X\-Ray\. The function also calls the Lambda API to get details about the account's quotas and usage in the current Region\.
+ [AWS X\-Ray](https://aws.amazon.com/xray) – Collects trace data, indexes traces for search, and generates a service map\.
+ [Amazon CloudWatch](https://aws.amazon.com/cloudwatch) – Stores logs and metrics\.
+ [AWS Identity and Access Management \(IAM\)](https://aws.amazon.com/iam) – Grants permission\.
+ [Amazon Simple Storage Service \(Amazon S3\)](https://aws.amazon.com/s3) – Stores the function's deployment package during deployment\.
+ [AWS CloudFormation](https://aws.amazon.com/cloudformation) – Creates application resources and deploys function code\.

Standard charges apply for each service\. For more information, see [AWS Pricing](https://aws.amazon.com/pricing)\.

The function code shows a basic workflow for processing an event\. The handler takes an Amazon Simple Queue Service \(Amazon SQS\) event as input and iterates through the records that it contains, logging the contents of each message\. It logs the contents of the event, the context object, and environment variables\. Then it makes a call with the AWS SDK and passes the response back to the Lambda runtime\.

**Example [blank\-nodejs/function/index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/function/index.js) – Handler code**  

```
// Handler
exports.handler = async function(event, context) {
  event.Records.forEach(record => {
    console.log(record.body)
  })
  console.log('## ENVIRONMENT VARIABLES: ' + serialize(process.env))
  console.log('## CONTEXT: ' + serialize(context))
  console.log('## EVENT: ' + serialize(event))
  
  return getAccountSettings()
}

// Use SDK client
var getAccountSettings = function(){
  return lambda.getAccountSettings().promise()
}

var serialize = function(object) {
  return JSON.stringify(object, null, 2)
}
```

The input/output types for the handler and support for asynchronous programming vary per runtime\. In this example, the handler method is `async`, so in Node\.js this means that it must return a promise back to the runtime\. The Lambda runtime waits for the promise to be resolved and returns the response to the invoker\. If the function code or AWS SDK client return an error, the runtime formats the error into a JSON document and returns that\.

The sample application doesn't include an Amazon SQS queue to send events, but uses an event from Amazon SQS \([event\.json](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/event.json)\) to illustrate how events are processed\. To add an Amazon SQS queue to your application, see [Using Lambda with Amazon SQS](with-sqs.md)\.

## Deployment automation with AWS CloudFormation and the AWS CLI<a name="samples-blank-automation"></a>

The sample application's resources are defined in an AWS CloudFormation template and deployed with the AWS CLI\. The project includes simple shell scripts that automate the process of setting up, deploying, invoking, and tearing down the application\.

The application template uses an AWS Serverless Application Model \(AWS SAM\) resource type to define the model\. AWS SAM simplifies template authoring for serverless applications by automating the definition of execution roles, APIs, and other resources\.

The template defines the resources in the application *stack*\. This includes the function, its execution role, and a Lambda layer that provides the function's library dependencies\. The stack does not include the bucket that the AWS CLI uses during deployment or the CloudWatch Logs log group\.

**Example [blank\-nodejs/template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank-nodejs/template.yml) – Serverless resources**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: An AWS Lambda application that calls the Lambda API.
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      CodeUri: function/.
      Description: Call the AWS Lambda API
      Timeout: 10
      # Function's execution role
      Policies:
        - AWSLambdaBasicExecutionRole
        - AWSLambda_ReadOnlyAccess
        - AWSXrayWriteOnlyAccess
      Tracing: Active
      Layers:
        - !Ref libs
  libs:
    Type: [AWS::Serverless::LayerVersion](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-layerversion.html)
    Properties:
      LayerName: blank-nodejs-lib
      Description: Dependencies for the blank sample app.
      ContentUri: lib/.
      CompatibleRuntimes:
        - nodejs12.x
```

When you deploy the application, AWS CloudFormation applies the AWS SAM transform to the template to generate an AWS CloudFormation template with standard types such as `AWS::Lambda::Function` and `AWS::IAM::Role`\.

**Example processed template**  

```
{
  "AWSTemplateFormatVersion": "2010-09-09",
  "Description": "An AWS Lambda application that calls the Lambda API.",
  "Resources": {
    "function": {
      "Type": "[AWS::Lambda::Function](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html)",
      "Properties": {
        "Layers": [
          {
            "Ref": "libs32xmpl61b2"
          }
        ],
        "TracingConfig": {
          "Mode": "Active"
        },
        "Code": {
          "S3Bucket": "lambda-artifacts-6b000xmpl1e9bf2a",
          "S3Key": "3d3axmpl473d249d039d2d7a37512db3"
        },
        "Description": "Call the AWS Lambda API",
        "Tags": [
          {
            "Value": "SAM",
            "Key": "lambda:createdBy"
          }
        ],
```

In this example, the `Code` property specifies an object in an Amazon S3 bucket\. This corresponds to the local path in the `CodeUri` property in the project template:

```
      CodeUri: function/.
```

To upload the project files to Amazon S3, the deployment script uses commands in the AWS CLI\. The `cloudformation package` command preprocesses the template, uploads artifacts, and replaces local paths with Amazon S3 object locations\. The `cloudformation deploy` command deploys the processed template with a AWS CloudFormation change set\.

**Example [blank\-nodejs/3\-deploy\.sh](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/3-deploy.sh) – Package and deploy**  

```
#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
aws cloudformation package --template-file template.yml --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name blank-nodejs --capabilities CAPABILITY_NAMED_IAM
```

The first time you run this script, it creates a AWS CloudFormation stack named `blank-nodejs`\. If you make changes to the function code or template, you can run it again to update the stack\.

The cleanup script \([blank\-nodejs/5\-cleanup\.sh](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/5-cleanup.sh)\) deletes the stack and optionally deletes the deployment bucket and function logs\.

## Instrumentation with the AWS X\-Ray<a name="samples-blank-instrumentation"></a>

The sample function is configured for tracing with [AWS X\-Ray](https://console.aws.amazon.com/xray/home)\. With the tracing mode set to active, Lambda records timing information for a subset of invocations and sends it to X\-Ray\. X\-Ray processes the data to generate a *service map* that shows a client node and two service nodes\.

The first service node \(`AWS::Lambda`\) represents the Lambda service, which validates the invocation request and sends it to the function\. The second node, `AWS::Lambda::Function`, represents the function itself\.

To record additional detail, the sample function uses the X\-Ray SDK\. With minimal changes to the function code, the X\-Ray SDK records details about calls made with the AWS SDK to AWS services\.

**Example [blank\-nodejs/function/index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/function/index.js) – Instrumentation**  

```
const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))

// Create client outside of handler to reuse
const lambda = new AWS.Lambda()
```

Instrumenting the AWS SDK client adds an additional node to the service map and more detail in traces\. In this example, the service map shows the sample function calling the Lambda API to get details about storage and concurrency usage in the current Region\.

The trace shows timing details for the invocation, with subsegments for function initialization, invocation, and overhead\. The invocation subsegment has a subsegment for the AWS SDK call to the `GetAccountSettings` API operation\.

You can include the X\-Ray SDK and other libraries in your function's deployment package, or deploy them separately in a Lambda layer\. For Node\.js, Ruby, and Python, the Lambda runtime includes the AWS SDK in the execution environment\.

## Dependency management with layers<a name="samples-blank-dependencies"></a>

You can install libraries locally and include them in the deployment package that you upload to Lambda, but this has its drawbacks\. Larger file sizes cause increased deployment times and can prevent you from testing changes to your function code in the Lambda console\. To keep the deployment package small and avoid uploading dependencies that haven't changed, the sample app creates a [Lambda layer](configuration-layers.md) and associates it with the function\.

**Example [blank\-nodejs/template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/template.yml) – Dependency layer**  

```
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      CodeUri: function/.
      Description: Call the AWS Lambda API
      Timeout: 10
      # Function's execution role
      Policies:
        - AWSLambdaBasicExecutionRole
        - AWSLambda_ReadOnlyAccess
        - AWSXrayWriteOnlyAccess
      Tracing: Active
      Layers:
        - !Ref libs
  libs:
    Type: [AWS::Serverless::LayerVersion](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-layerversion.html)
    Properties:
      LayerName: blank-nodejs-lib
      Description: Dependencies for the blank sample app.
      ContentUri: lib/.
      CompatibleRuntimes:
        - nodejs12.x
```

The `2-build-layer.sh` script installs the function's dependencies with npm and places them in a folder with the [structure required by the Lambda runtime](configuration-layers.md#configuration-layers-path)\.

**Example [2\-build\-layer\.sh](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs/2-build-layer.sh) – Preparing the layer**  

```
#!/bin/bash
set -eo pipefail
mkdir -p lib/nodejs
rm -rf node_modules lib/nodejs/node_modules
npm install --production
mv node_modules lib/nodejs/
```

The first time that you deploy the sample application, the AWS CLI packages the layer separately from the function code and deploys both\. For subsequent deployments, the layer archive is only uploaded if the contents of the `lib` folder have changed\.