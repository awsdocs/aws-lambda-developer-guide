# AWS Lambda Function Errors in Python<a name="python-exceptions"></a>

If your Lambda function raises an exception, AWS Lambda recognizes the failure and serializes the exception information into JSON and returns it\. Consider the following example:

```
def always_failed_handler(event, context):
    raise Exception('I failed!')
```

When you invoke this Lambda function, it will raise an exception and AWS Lambda returns the following error message:

```
{
  "errorMessage": "I failed!",
  "stackTrace": [
    [
      "/var/task/lambda_function.py",
      3,
      "my_always_fails_handler",
      "raise Exception('I failed!')"
    ]
  ],
  "errorType": "Exception"
}
```

 Note that the stack trace is returned as the `stackTrace` JSON array of stack trace elements\. 

How you get the error information back depends on the invocation type that the client specifies at the time of function invocation: 
+ If a client specifies the `RequestResponse` invocation type \(that is, synchronous execution\), it returns the result to the client that made the invoke call\. 

  For example, the console always use the `RequestResponse` invocation type, so the console will display the error in the **Execution result** section as shown:  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/exception-shown-in-console.png)

  The same information is also sent to CloudWatch and the **Log output** section shows the same logs\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/exception-shown-in-console20.png)
+ If a client specifies the `Event` invocation type \(that is, asynchronous execution\), AWS Lambda will not return anything\. Instead, it logs the error information to CloudWatch Logs\. You can also see the error metrics in CloudWatch Metrics\.

Depending on the event source, AWS Lambda may retry the failed Lambda function\. For example, if Kinesis is the event source, AWS Lambda will retry the failed invocation until the Lambda function succeeds or the records in the stream expire\. 

**To test the preceding Python code \(console\)**

1. In the console, create a Lambda function using the hello\-world blueprint\. In **runtime**, choose Python 3\.7\. In **Handler**, replace `lambda_function.lambda_handler` with `lambda_function.always_failed_handler`\. For instructions on how to do this, see [Create a Lambda Function with the Console](getting-started-create-function.md)\. 

1. Replace the template code with the code provided in this section\.

1. Test the Lambda function using the **Sample event template** called **Hello World** provided in the Lambda console\.

## Function Error Handling<a name="python-custom-errors"></a>

You can create custom error handling to raise an exception directly from your Lambda function and handle it directly \(Retry or Catch\) within an AWS Step Functions State Machine\. For more information, see [Handling Error Conditions Using a State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html)\. 

Consider a `CreateAccount` [state](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states.html) is a [task](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states-task.html) that writes a customer's details to a database using a Lambda function\.
+ If the task succeeds, an account is created and a welcome email is sent\.
+ If a user tries to create an account for a username that already exists, the Lambda function raises an error, causing the state machine to suggest a different username and to retry the account\-creation process\.

The following code samples demonstrate how to do this\. Note that custom errors in Python must extend the `Exception` class\.

```
class AccountAlreadyExistsException(Exception): pass

def create_account(event, context):
    raise AccountAlreadyExistsException('Account is in use!')
```

You can configure Step Functions to catch the error using a `Catch` rule\. Lambda automatically sets the error name to the simple class name of the exception at runtime:

```
{
   "StartAt": "CreateAccount",
   "States": {
      "CreateAccount": {
         "Type": "Task",
         "Resource": "arn:aws:lambda:us-east-1:123456789012:function:CreateAccount",
         "Next": "SendWelcomeEmail",
         "Catch": [
            {
               "ErrorEquals": ["AccountAlreadyExistsException"],
               "Next": "SuggestAccountName"
            }
         ]
      },
      …
   }
}
```

At runtime, AWS Step Functions catches the error, [transitioning](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-transitions.html) to the `SuggestAccountName` state as specified in the `Next` transition\.

Custom error handling makes it easier to create [serverless](https://aws.amazon.com/serverless) applications\. This feature integrates with all the languages supported by the Lambda [Programming Model](programming-model-v2.md), allowing you to design your application in the programming languages of your choice, mixing and matching as you go\.

To learn more about creating your own serverless applications using AWS Step Functions and AWS Lambda, see [AWS Step Functions](https://aws.amazon.com/step-functions/)\. 