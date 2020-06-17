# Orchestrate Lambda functions with AWS Step Functions<a name="services-stepfunctions"></a>

You can use AWS Step Functions to create state machines that orchestrate Lambda functions to create an application\. Step Functions manages the state of your application and provides a visual interface for defining workflows that involve Lambda functions, AWS Batch jobs, Amazon SNS topics, Amazon SQS queues and other common AWS resources\. Instead of defining your application logic in a program, you assemble components visually or with a JSON\-based, structured language called [Amazon States Language](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-amazon-states-language.html)\. Lambda functions in Step Functions are self\-contained, reusable services with a well\-defined interface that you can share with less technical users\.

Step Functions invokes your function with an event document that you define\. You create a *task* state that invokes your function with the input to the state machine or any JSON document\.

**Example [event\.json](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/error-processor/event.json) – Input to [random\-error function](samples-errorprocessor.md)**  

```
{
  "max-depth": 10,
  "current-depth": 0,
  "error-rate": 0.05
}
```

Step Functions makes it easy to [retry failed executions](#services-stepfunctions-exceptions) or branch your application logic based on the result of an invocation\.

**Topics**
+ [Configuring a state machine as an event source](#services-stepfunctions-setup)
+ [Handling function and service errors](#services-stepfunctions-exceptions)
+ [AWS CloudFormation and AWS SAM](#services-stepfunctions-cloudformation)

## Configuring a state machine as an event source<a name="services-stepfunctions-setup"></a>

You can use the [Step Functions console](https://console.aws.amazon.com/states/home) to generate a state that invokes a Lambda function, or define states directly in JSON\. The following example shows a task state that invokes version 1 of a function named `my-function` with an event payload that has three keys\. When the function returns a successful response, the state machine continues to the next task\.

**Example state machine task**  

```
"Invoke": {
  "Type": "Task",
  "Resource": "arn:aws:states:::lambda:invoke",
  "Parameters": {
    "FunctionName": "arn:aws:lambda:us-east-2:123456789012:function:my-function:1",
    "Payload": {
      "max-depth": 10,
      "current-depth": 0,
      "error-rate": 0.05
    }
  },
  "Next": "NEXT_STATE",
  "TimeoutSeconds": 25
}
```

**Permissions**  
Your state machine needs permission to call the Lambda API to invoke a function\. To grant it permission, add the [AWSLambdaRole](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/service-role/AWSLambdaRole) managed policy or a function\-scoped inline policy to its role\. For more information, see [How AWS Step Functions Works with IAM](https://docs.aws.amazon.com/step-functions/latest/dg/procedure-create-iam-role.html) in the *AWS Step Functions Developer Guide*\.

The `FunctionName` and `Payload` parameters map to parameters in the [Invoke](API_Invoke.md) API operation\. In addition to these, you can also specify the `InvocationType` and `ClientContext` parameters\. For example, to invoke the function asynchronously and continue to the next state without waiting for a result, you can set `InvocationType` to `Event` as shown following:

```
    "InvocationType": "Event"
```

Instead of hard coding the event payload in the state machine definition, you can use the input from the state machine execution\. The following example uses the input specified when you run the state machine as the event payload:

```
    "Payload.$": "$"
```

You can also invoke a function asynchronously and wait for it to make a callback with the AWS SDK\. To do this, you set the state's resource to `arn:aws:states:::lambda:invoke.waitForTaskToken`\. For more information, see [Invoke Lambda with Step Functions ](https://docs.aws.amazon.com/step-functions/latest/dg/connect-lambda.html) in the *AWS Step Functions Developer Guide*\.

## Handling function and service errors<a name="services-stepfunctions-exceptions"></a>

When your function or the Lambda service returns an error, you can retry the invocation or continue to a different state based on the error type\.

The following example shows an invoke task that retries on `5XX` series Lambda API exceptions \(`ServiceException`\), throttles \(`TooManyRequestsException`\), runtime errors \(`Lambda.Unknown`\), and a function\-defined error named `function.MaxDepthError`\. It also catches an error named `function.DoublesRolledError` and continues to a state named `CaughtException` when it occurs\.

**Example invoke task – Retry and catch**  

```
  "Invoke": {
    "Type": "Task",
    "Resource": "arn:aws:states:::lambda:invoke",
    "Retry": [
      {
        "ErrorEquals": [ 
            "function.MaxDepthError",
            "Lambda.TooManyRequestsException",
            "Lambda.ServiceException",
            "Lambda.Unknown"
          ],
        "MaxAttempts": 5
      }
    ],
    "Catch": [
      {
        "ErrorEquals": [ "function.DoublesRolledError" ],
        "Next": "CaughtException"
      }
    ],
    "Parameters": {
      "FunctionName": "arn:aws:lambda:us-east-2:123456789012:function:my-function:1",
      ...
```

To catch or retry on function errors, create a custom error type\. The name of the error type must match the `errorType` in the formatted error response that Lambda returns when you throw an error\. For details on throwing errors in each support language, see the following topics:
+  [AWS Lambda function errors in Node\.js](nodejs-exceptions.md) 
+  [AWS Lambda function errors in Python](python-exceptions.md) 
+  [AWS Lambda function errors in Ruby](ruby-exceptions.md) 
+  [AWS Lambda function errors in Java](java-exceptions.md) 
+  [AWS Lambda function errors in Go](golang-exceptions.md) 
+  [AWS Lambda function errors in C\#](csharp-exceptions.md) 
+  [AWS Lambda function errors in PowerShell](powershell-exceptions.md) 

For more information on error handling in Step Functions, see [Handling error conditions using a state machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html) in the *AWS Step Functions Developer Guide*\.

## AWS CloudFormation and AWS SAM<a name="services-stepfunctions-cloudformation"></a>

You can create a serverless application that includes a Step Functions state machine in a AWS CloudFormation template with the AWS Serverless Application Model \(AWS SAM\)\. With AWS SAM, you can define the state machine inline in the template or in a separate file\. The following example shows a state machine that demonstrates invoking a Lambda function and handling errors\. It refers to a function resource defined in the same template \(not shown\)\.

**Example template\.yml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: An AWS Lambda application that uses AWS Step Functions.
Resources:
  statemachine:
    Type: [AWS::Serverless::StateMachine](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-statemachine.html)
    Properties:
      DefinitionSubstitutions:
        FunctionArn: !GetAtt function.Arn
        Payload: |
          {
            "max-depth": 5,
            "current-depth": 0,
            "error-rate": 0.2
          }
      Definition:
        StartAt: Invoke
        States:
          Invoke:
            Type: Task
            Resource: arn:aws:states:::lambda:invoke
            Parameters:
              FunctionName: "${FunctionArn}"
              Payload: "${Payload}"
              InvocationType: Event
            Retry:
            - ErrorEquals:
              - function.MaxDepthError
              - function.MaxDepthError
              - Lambda.TooManyRequestsException
              - Lambda.ServiceException
              - Lambda.Unknown
              IntervalSeconds: 1
              MaxAttempts: 5
            Catch:
            - ErrorEquals:
              - function.DoublesRolledError
              Next: CaughtException
            - ErrorEquals:
              - States.ALL
              Next: UncaughtException
            Next: Success
          CaughtException:
            Type: Pass
            Result: The function returned an error.
            End: true
          UncaughtException:
            Type: Pass
            Result: Invocation failed.
            End: true
          Success:
            Type: Pass
            Result: Invocation succeeded!
            End: true
      Events:
        scheduled:
          Type: Schedule
          Properties:
            Description: Run every minute
            Schedule: rate(1 minute)
      Type: STANDARD
      Policies:
        - AWSLambdaRole
      ...
```

This creates a state machine with the following structure:

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/services-stepfunctions-statemachine.png)