# AWS Lambda Function Errors in Java<a name="java-exceptions"></a>

If your Lambda function throws an exception, AWS Lambda recognizes the failure and serializes the exception information into JSON and returns it\. Following is an example error message:

```
{
  "errorMessage": "Name John Doe is invalid. Exception occurred...",
  "errorType": "java.lang.Exception",
  "stackTrace": [
    "example.Hello.handler(Hello.java:9)",
    "sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
    "sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)",
    "sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)",
    "java.lang.reflect.Method.invoke(Method.java:497)"
  ]
}
```

Note that the stack trace is returned as the `stackTrace` JSON array of stack trace elements\. 

The method in which you get the error information back depends on the invocation type that you specified at the time you invoked the function: 
+ `RequestResponse` invocation type \(that is, synchronous execution\): In this case, you get the error message back\. 

  For example, if you invoke a Lambda function using the Lambda console, the `RequestResponse` is always the invocation type and the console displays the error information returned by AWS Lambda in the **Execution result** section as shown in the following image\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/exception-shown-in-console.png)
+ `Event` invocation type \(that is, asynchronous execution\): In this case AWS Lambda does not return anything\. Instead, it logs the error information in CloudWatch Logs and CloudWatch metrics\.

Depending on the event source, AWS Lambda may retry the failed Lambda function\. For example, if Kinesis is the event source for the Lambda function, AWS Lambda retries the failed function until the Lambda function succeeds or the records in the stream expire\.

## Function Error Handling<a name="java-custom-errors"></a>

You can create custom error handling to raise an exception directly from your Lambda function and handle it directly \(Retry or Catch\) within an AWS Step Functions State Machine\. For more information, see [Handling Error Conditions Using a State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html)\. 

Consider a `CreateAccount` [state](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states.html) is a [task](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states-task.html) that writes a customer's details to a database using a Lambda function\.
+ If the task succeeds, an account is created and a welcome email is sent\.
+ If a user tries to create an account for a username that already exists, the Lambda function raises an error, causing the state machine to suggest a different username and to retry the account\-creation process\.

The following code samples demonstrate how to do this\. Note that custom errors in Java must extend the `Exception` class\.

```
package com.example; 

public static class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
} 

package com.example; 

import com.amazonaws.services.lambda.runtime.Context;  

public class Handler {
    public static void CreateAccount(String name, Context context) throws AccountAlreadyExistsException {
        throw new AccountAlreadyExistsException ("Account is in use!");
    }
}
```

You can configure Step Functions to catch the error using a `Catch` rule\. Lambda automatically sets the error name to the fully\-qualified class name of the exception at runtime:

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
               "ErrorEquals": ["com.example.AccountAlreadyExistsException"],
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