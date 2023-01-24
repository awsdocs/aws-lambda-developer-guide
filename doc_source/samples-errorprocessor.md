# Error processor sample application for AWS Lambda<a name="samples-errorprocessor"></a>

The Error Processor sample application demonstrates the use of AWS Lambda to handle events from an [Amazon CloudWatch Logs subscription](services-cloudwatchlogs.md)\. CloudWatch Logs lets you invoke a Lambda function when a log entry matches a pattern\. The subscription in this application monitors the log group of a function for entries that contain the word `ERROR`\. It invokes a processor Lambda function in response\. The processor function retrieves the full log stream and trace data for the request that caused the error, and stores them for later use\.

Function code is available in the following files:
+ Random error – [random\-error/index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor/random-error/index.js)
+ Processor – [processor/index\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor/processor/index.js)

You can deploy the sample in a few minutes with the AWS CLI and AWS CloudFormation\. To download, configure, and deploy it in your account, follow the instructions in the [README](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor)\.

**Topics**
+ [Architecture and event structure](#sample-errorprocessor-architecture)
+ [Instrumentation with AWS X\-Ray](#sample-errorprocessor-instrumentation)
+ [AWS CloudFormation template and additional resources](#sample-errorprocessor-template)

## Architecture and event structure<a name="sample-errorprocessor-architecture"></a>

The sample application uses the following AWS services\.
+ AWS Lambda – Runs function code, sends logs to CloudWatch Logs, and sends trace data to X\-Ray\.
+ Amazon CloudWatch Logs – Collects logs, and invokes a function when a log entry matches a filter pattern\.
+ AWS X\-Ray – Collects trace data, indexes traces for search, and generates a service map\.
+ Amazon Simple Storage Service \(Amazon S3\) – Stores deployment artifacts and application output\.

Standard charges apply for each service\.

A Lambda function in the application generates errors randomly\. When CloudWatch Logs detects the word `ERROR` in the function's logs, it sends an event to the processor function for processing\.

**Example CloudWatch Logs message event**  

```
{
    "awslogs": {
        "data": "H4sIAAAAAAAAAHWQT0/DMAzFv0vEkbLYcdJkt4qVXmCDteIAm1DbZKjS+kdpB0Jo350MhsQFyVLsZ+unl/fJWjeO5asrPgbH5..."
    }
}
```

When it's decoded, the data contains details about the log event\. The function uses these details to identify the log stream, and parses the log message to get the ID of the request that caused the error\.

**Example decoded CloudWatch Logs event data**  

```
{
    "messageType": "DATA_MESSAGE",
    "owner": "123456789012",
    "logGroup": "/aws/lambda/lambda-error-processor-randomerror-1GD4SSDNACNP4",
    "logStream": "2019/04/04/[$LATEST]63311769a9d742f19cedf8d2e38995b9",
    "subscriptionFilters": [
        "lambda-error-processor-subscription-15OPDVQ59CG07"
    ],
    "logEvents": [
        {
            "id": "34664632210239891980253245280462376874059932423703429141",
            "timestamp": 1554415868243,
            "message": "2019-04-04T22:11:08.243Z\t1d2c1444-efd1-43ec-b16e-8fb2d37508b8\tERROR\n"
        }
    ]
}
```

The processor function uses information from the CloudWatch Logs event to download the full log stream and X\-Ray trace for a request that caused an error\. It stores both in an Amazon S3 bucket\. To allow the log stream and trace time to finalize, the function waits for a short period of time before accessing the data\.

## Instrumentation with AWS X\-Ray<a name="sample-errorprocessor-instrumentation"></a>

The application uses [AWS X\-Ray](services-xray.md) to trace function invocations and the calls that functions make to AWS services\. X\-Ray uses the trace data that it receives from functions to create a service map that helps you identify errors\. 

The two Node\.js functions are configured for active tracing in the template, and are instrumented with the AWS X\-Ray SDK for Node\.js in code\. With active tracing, Lambda tags adds a tracing header to incoming requests and sends a trace with timing details to X\-Ray\. Additionally, the random error function uses the X\-Ray SDK to record the request ID and user information in annotations\. The annotations are attached to the trace, and you can use them to locate the trace for a specific request\. 

The processor function gets the request ID from the CloudWatch Logs event, and uses the AWS SDK for JavaScript to search X\-Ray for that request\. It uses AWS SDK clients, which are instrumented with the X\-Ray SDK, to download the trace and log stream\. Then it stores them in the output bucket\. The X\-Ray SDK records these calls, and they appear as subsegments in the trace\.

## AWS CloudFormation template and additional resources<a name="sample-errorprocessor-template"></a>

The application is implemented in two Node\.js modules and deployed with an AWS CloudFormation template and shell scripts\. The template creates the processor function, the random error function, and the following supporting resources\.
+ Execution role – An IAM role that grants the functions permission to access other AWS services\.
+ Primer function – An additional function that invokes the random error function to create a log group\.
+ Custom resource – An AWS CloudFormation custom resource that invokes the primer function during deployment to ensure that the log group exists\.
+ CloudWatch Logs subscription – A subscription for the log stream that triggers the processor function when the word ERROR is logged\.
+ Resource\-based policy – A permission statement on the processor function that allows CloudWatch Logs to invoke it\.
+ Amazon S3 bucket – A storage location for output from the processor function\.

View the [application template](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor/template.yml) on GitHub\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-errorprocessor-stack.png)

To work around a limitation of Lambda's integration with AWS CloudFormation, the template creates an additional function that runs during deployments\. All Lambda functions come with a CloudWatch Logs log group that stores output from function executions\. However, the log group isn't created until the function is invoked for the first time\.

To create the subscription, which depends on the existence of the log group, the application uses a third Lambda function to invoke the random error function\. The template includes the code for the primer function inline\. An AWS CloudFormation custom resource invokes it during deployment\. `DependsOn` properties ensure that the log stream and resource\-based policy are created prior to the subscription\.