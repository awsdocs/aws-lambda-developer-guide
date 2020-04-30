# Blank function sample application for AWS Lambda<a name="samples-blank"></a>

The blank function sample application demonstrates common operations in Lambda with a function that calls the Lambda API\. It shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\. Explore this application to learn about building Lambda functions in your programming language, or use it as a starting point for your own projects\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-blank.png)

Variants of this sample application are available for the following languages:

**Variants**
+ Node\.js – [blank](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank)\.
+ Python – [blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-python)\.
+ Ruby – [blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-ruby)\.
+ Java – [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java)\.
+ Go – [blank\-go](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-go)\.
+ C\# – [blank\-csharp](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-csharp)\.
+ PowerShell – [blank\-powershell](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-python)\.

The examples in this topic highlight code from the Node\.js version, but the details are generally applicable to all variants\.

You can deploy the sample in a few minutes with the AWS CLI and AWS CloudFormation\. Follow the instructions in the [README](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank) to download, configure, and deploy it in your account\.

**Topics**
+ [Architecture and event structure](#samples-blank-architecture)
+ [Deployment automation with AWS CloudFormation and the AWS CLI](#samples-blank-automation)
+ [Instrumentation with the AWS X\-Ray](#samples-blank-instrumentation)
+ [Dependency management with layers](#samples-blank-dependencies)

## Architecture and event structure<a name="samples-blank-architecture"></a>

The sample application uses the following AWS services:
+ AWS Lambda – Runs function code, sends logs to CloudWatch Logs, and sends trace data to X\-Ray\. The function also calls the Lambda API to get details about the account's limits and usage in the current Region\.
+ [AWS X\-Ray](https://aws.amazon.com/xray) – Collects trace data, indexes traces for search, and generates a service map\.
+ [AWS CloudFormation](https://aws.amazon.com/cloudformation) – Creates application resources and deploys function code\.

Standard charges apply for each service\.

The function code shows a basic workflow for processing an event\. The handler takes an Amazon SQS event as input and iterates through the records that it contains, logging the contents of each message\. It logs the contents of the event, the context object, and environment variables\. Then it makes a call with the AWS SDK and passes the response back to the Lambda runtime\.

**Example [blank/function/index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank/function/index.js) – Handler code**  

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

The handler method is `async`, so it must return a promise back to the runtime\. The runtime waits for the promise to be resolved and returns the response to the invoker\. If the function code or AWS SDK client return an error, the runtime formats the error into a JSON document and returns that\.

## Deployment automation with AWS CloudFormation and the AWS CLI<a name="samples-blank-automation"></a>

The sample application's resources are defined in an AWS CloudFormation template and deployed with the AWS CLI\. The project includes simple shell scripts that automate the process of setting up, deploying, invoking, and tearing down the application\.

The application template uses an AWS Serverless Application Model \(AWS SAM\) resource type to define the model\. AWS SAM simplifies template authoring for serverless applications by automating the definition of execution roles, APIs, and other resources\.

**Example [blank/template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank/template.yml) – Serverless resource**  

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
        - AWSLambdaReadOnlyAccess
        - AWSXrayWriteOnlyAccess
      Tracing: Active
      Layers:
        - !Ref libs
  libs:
    Type: [AWS::Serverless::LayerVersion](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-layerversion.html)
    Properties:
      LayerName: blank-lib
      Description: Dependencies for the blank sample app.
      ContentUri: lib/.
      CompatibleRuntimes:
        - nodejs12.x
```

When you deploy the application, AWS CloudFormation applies the AWS SAM transform to the template to generate an AWS CloudFormation template with standard types such as `AWS::Lambda::Function` and `AWS::IAM::Role`\.

**Example Processed template**  

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

**Example [blank/3\-deploy\.sh](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank/3-deploy.sh) – Package and deploy**  

```
#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
aws cloudformation package --template-file template.yml --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name blank --capabilities CAPABILITY_NAMED_IAM
```

The first time you run this script, it creates a AWS CloudFormation stack named `blank`\. Run it again to update the stack with any local code or configuration changes\.

## Instrumentation with the AWS X\-Ray<a name="samples-blank-instrumentation"></a>

The sample function is configured for tracing with [AWS X\-Ray](https://console.aws.amazon.com/xray/home)\. With the tracing mode set to active, Lambda records timing information for a subset of invocations and sends it to X\-Ray\. X\-Ray processes the data to generate a *service map* that shows a client node and two service nodes:

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/blank-servicemap-basic.png)

The first service node \(`AWS::Lambda`\) represents the Lambda service, which validates the invocation request and sends it to the function\. The second node, `AWS::Lambda::Function`, represents the function itself\.

To record additional detail, the sample function uses the X\-Ray SDK\. With minimal changes to the function code, the X\-Ray SDK records details about calls made with the AWS SDK to AWS services\.

**Example [blank/function/index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank/function/index.js) – Instrumentation**  

```
const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))

// Create client outside of handler to reuse
const lambda = new AWS.Lambda()
```

Instrumenting the AWS SDK client adds an additional node to the service map and more detail in traces\. In this example, the service map shows the sample function calling the Lambda API to get details about storage and concurrency usage in the current Region\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/blank-servicemap.png)

The trace shows timing details for the invocation, with subsegments for function initialization, invocation, and overhead\. The invocation subsegment has a subsegment for the AWS SDK call to the `GetAccountSettings` API operation\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/blank-trace.png)

You can include the X\-Ray SDK and other libraries in your function's deployment package, or deploy them separately in a Lambda layer\. For Node\.js, Ruby, and Python, the Lambda runtime includes the AWS SDK in the execution environment\.

## Dependency management with layers<a name="samples-blank-dependencies"></a>

You can install libraries locally and include them in the deployment package that you upload to Lambda, but this has its drawbacks\. Larger file sizes cause increased deployment times and can prevent you from testing changes to your function code in the Lambda console\. To keep the deployment package small and avoid uploading dependencies that haven't changed, the sample app creates a [Lambda layer](configuration-layers.md) and associates it with the function\.

**Example [blank/template\.yml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank/template.yml) – Dependency layer**  

```
Resources:
  function:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      CodeUri: function/.
      Description: Call the AWS Lambda API
      Timeout: 10
      # Function's execution role
      Policies:
        - AWSLambdaBasicExecutionRole
        - AWSLambdaReadOnlyAccess
        - AWSXrayWriteOnlyAccess
      Tracing: Active
      Layers:
        - !Ref libs
  libs:
    Type: AWS::Serverless::LayerVersion
    Properties:
      LayerName: blank-lib
      Description: Dependencies for the blank sample app.
      ContentUri: lib/.
      CompatibleRuntimes:
        - nodejs12.x
```

The `2-build-layer.sh` script installs the function's dependencies with npm and places them in a folder with the [structure required by the Lambda runtime](configuration-layers.md#configuration-layers-path)\.

**Example [2\-build\-layer\.sh](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/blank/2-build-layer.sh) – Preparing the layer**  

```
#!/bin/bash
set -eo pipefail
mkdir -p lib/nodejs
rm -rf node_modules lib/nodejs/node_modules
npm install --production
mv node_modules lib/nodejs/
```

The first time that you deploy the sample application, the AWS CLI packages the layer separately from the function code and deploys both\. For subsequent deployments, the layer archive is only uploaded if the contents of the `lib` folder have changed\.