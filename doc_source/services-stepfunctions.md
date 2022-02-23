# Orchestration examples with Step Functions<a name="services-stepfunctions"></a>

All work in your Step Functions state machine is done by [https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-task-state.html](https://docs.aws.amazon.com/step-functions/latest/dg/amazon-states-language-task-state.html)\. A `Task` performs work by using an activity, a Lambda function, or by passing parameters to the API actions of other [Supported AWS Service Integrations for Step Functions](https://docs.aws.amazon.com/step-functions/latest/dg/connect-supported-services.html)\.

**Topics**
+ [Configuring a Lambda function as a task](#services-stepfunctions-task)
+ [Configuring a state machine as an event source](#services-stepfunctions-setup)
+ [Handling function and service errors](#services-stepfunctions-exceptions)
+ [AWS CloudFormation and AWS SAM](#services-stepfunctions-cloudformation)

## Configuring a Lambda function as a task<a name="services-stepfunctions-task"></a>

Step Functions can invoke Lambda functions directly from a `Task` state in an [Amazon States Language](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-amazon-states-language.html) definition\.

```
...
        "MyStateName":{
          "Type":"Task",         
          "Resource":"arn:aws:lambda:us-west-2:01234567890:function:my_lambda_function",         
          "End":true 
      ...
```

You can create a `Task` state that invokes your Lambda function with the input to the state machine or any JSON document\.

**Example [event\.json](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/error-processor/event.json) – Input to [random\-error function](samples-errorprocessor.md)**  

```
{
  "max-depth": 10,
  "current-depth": 0,
  "error-rate": 0.05
}
```

## Configuring a state machine as an event source<a name="services-stepfunctions-setup"></a>

You can create a Step Functions state machine that invokes a Lambda function\. The following example shows a `Task` state that invokes version `1` of a function named `my-function` with an event payload that has three keys\. When the function returns a successful response, the state machine continues to the next task\.

**Example state machine**  

```
...
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
Your state machine needs permission to call the Lambda API to invoke a function\. To grant it permission, add the AWS managed policy [AWSLambdaRole](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/service-role/AWSLambdaRole) or a function\-scoped inline policy to its role\. For more information, see [How AWS Step Functions Works with IAM](https://docs.aws.amazon.com/step-functions/latest/dg/procedure-create-iam-role.html) in the *AWS Step Functions Developer Guide*\.

The `FunctionName` and `Payload` parameters map to parameters in the [Invoke](API_Invoke.md) API operation\. In addition to these, you can also specify the `InvocationType` and `ClientContext` parameters\. For example, to invoke the function asynchronously and continue to the next state without waiting for a result, you can set `InvocationType` to `Event`:

```
"InvocationType": "Event"
```

Instead of hard\-coding the event payload in the state machine definition, you can use the input from the state machine execution\. The following example uses the input specified when you run the state machine as the event payload:

```
"Payload.$": "$"
```

You can also invoke a function asynchronously and wait for it to make a callback with the AWS SDK\. To do this, set the state's resource to `arn:aws:states:::lambda:invoke.waitForTaskToken`\.

For more information, see [Invoke Lambda with Step Functions](https://docs.aws.amazon.com/step-functions/latest/dg/connect-lambda.html) in the *AWS Step Functions Developer Guide*\.

## Handling function and service errors<a name="services-stepfunctions-exceptions"></a>

When your function or the Lambda service returns an error, you can retry the invocation or continue to a different state based on the error type\.

The following example shows an invoke task that retries on `5XX` series Lambda API exceptions \(`ServiceException`\), throttles \(`TooManyRequestsException`\), runtime errors \(`Lambda.Unknown`\), and a function\-defined error named `function.MaxDepthError`\. It also catches an error named `function.DoublesRolledError` and continues to a state named `CaughtException` when it occurs\.

**Example catch and retry pattern**  

```
...
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

To catch or retry function errors, create a custom error type\. The name of the error type must match the `errorType` in the formatted error response that Lambda returns when you throw an error\.

For more information on error handling in Step Functions, see [Handling Error Conditions Using a Step Functions State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html) in the *AWS Step Functions Developer Guide*\.

## AWS CloudFormation and AWS SAM<a name="services-stepfunctions-cloudformation"></a>

You can define state machines using a AWS CloudFormation template with AWS Serverless Application Model \(AWS SAM\)\. Using AWS SAM, you can define the state machine inline in the template or in a separate file\. The following example shows a state machine that invokes a Lambda function that handles errors\. It refers to a function resource defined in the same template \(not shown\)\.

**Example branching pattern in template\.yml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: An AWS Lambda application that uses AWS Step Functions.
Resources:
  statemachine:
    Type: AWS::Serverless::StateMachine
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

For more information, see [AWS::Serverless::StateMachine](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-statemachine.html) in the *AWS Serverless Application Model Developer Guide*\.