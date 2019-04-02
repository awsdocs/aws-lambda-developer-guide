# AWS Lambda Function Errors in C\#<a name="dotnet-exceptions"></a>

When an exception occurs in your Lambda function, Lambda will report the exception information back to you\. Exceptions can occur in two different places: 
+ Initialization \(Lambda loading your code, validating the handler string, and creating an instance of your class if it is non\-static\)\.
+ The Lambda function invocation\.

The serialized exception information is returned as the payload as a modeled JSON object and outputted to CloudWatch logs\.

In the initialization phase, exceptions can be thrown for invalid handler strings, a rule\-breaking type or method \(see [Lambda Function Handler Restrictions ](dotnet-programming-model-handler-types.md#dotnet-handler-restrictions)\), or any other validation method \(such as forgetting the serializer attribute and having a POCO as your input or output type\)\. These exceptions are of type `LambdaException`\. For example: 

```
{
  "errorType": "LambdaException",
  "errorMessage": "Invalid lambda function handler: 'http://this.is.not.a.valid.handler/'. 
  The valid format is 'ASSEMBLY::TYPE::METHOD'."
}
```

If your constructor throws an exception, the error type is also of type `LambdaException`, but the exception thrown during construction is provided in the `cause` property, which is itself a modeled exception object:

```
{
  "errorType": "LambdaException",
  "errorMessage": "An exception was thrown when the constructor for type 'LambdaExceptionTestFunction.ThrowExceptionInConstructor'
   was invoked. Check inner exception for more details.",
  "cause":   {
    "errorType": "TargetInvocationException",
    "errorMessage": "Exception has been thrown by the target of an invocation.",
    "stackTrace": [
      "at System.RuntimeTypeHandle.CreateInstance(RuntimeType type, Boolean publicOnly, Boolean noCheck, Boolean&canBeCached, 
      RuntimeMethodHandleInternal&ctor, Boolean& bNeedSecurityCheck)",
      "at System.RuntimeType.CreateInstanceSlow(Boolean publicOnly, Boolean skipCheckThis, Boolean fillCache, StackCrawlMark& stackMark)",
      "at System.Activator.CreateInstance(Type type, Boolean nonPublic)",
      "at System.Activator.CreateInstance(Type type)"
    ],
    "cause":     {
      "errorType": "ArithmeticException",
      "errorMessage": "Sorry, 2 + 2 = 5",
      "stackTrace": [
        "at LambdaExceptionTestFunction.ThrowExceptionInConstructor..ctor()"
      ]
    }
  }
}
```

As the example shows, the inner exceptions are always preserved \(as the `cause` property\), and can be deeply nested\. 

Exceptions can also occur during invocation\. In this case, the exception type is preserved and the exception is returned directly as the payload and in the CloudWatch logs\. For example: 

```
{
  "errorType": "AggregateException",
  "errorMessage": "One or more errors occurred. (An unknown web exception occurred!)",
  "stackTrace": [
    "at System.Threading.Tasks.Task.ThrowIfExceptional(Boolean includeTaskCanceledExceptions)",
    "at System.Threading.Tasks.Task`1.GetResultCore(Boolean waitCompletionNotification)",
    "at lambda_method(Closure , Stream , Stream , ContextInfo )"
  ],
  "cause":   {
    "errorType": "UnknownWebException",
    "errorMessage": "An unknown web exception occurred!",
    "stackTrace": [
      "at LambdaDemo107.LambdaEntryPoint.<GetUriResponse>d__1.MoveNext()",
      "--- End of stack trace from previous location where exception was thrown ---",
      "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
      "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
      "at System.Runtime.CompilerServices.TaskAwaiter`1.GetResult()",
      "at LambdaDemo107.LambdaEntryPoint.<CheckWebsiteStatus>d__0.MoveNext()"
    ],
    "cause":     {
      "errorType": "WebException",
      "errorMessage": "An error occurred while sending the request. SSL peer certificate or SSH remote key was not OK",
      "stackTrace": [
        "at System.Net.HttpWebRequest.EndGetResponse(IAsyncResult asyncResult)",
        "at System.Threading.Tasks.TaskFactory`1.FromAsyncCoreLogic(IAsyncResult iar, Func`2 endFunction, Action`1 endAction, Task`1 promise, Boolean requiresSynchronization)",
        "--- End of stack trace from previous location where exception was thrown ---",
        "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
        "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
        "at System.Runtime.CompilerServices.TaskAwaiter`1.GetResult()",
        "at LambdaDemo107.LambdaEntryPoint.<GetUriResponse>d__1.MoveNext()"
      ],
      "cause":       {
        "errorType": "HttpRequestException",
        "errorMessage": "An error occurred while sending the request.",
        "stackTrace": [
          "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
          "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
          "at System.Net.Http.HttpClient.<FinishSendAsync>d__58.MoveNext()",
          "--- End of stack trace from previous location where exception was thrown ---",
          "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
          "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
          "at System.Net.HttpWebRequest.<SendRequest>d__63.MoveNext()",
          "--- End of stack trace from previous location where exception was thrown ---",
          "at System.Runtime.CompilerServices.TaskAwaiter.ThrowForNonSuccess(Task task)",
          "at System.Runtime.CompilerServices.TaskAwaiter.HandleNonSuccessAndDebuggerNotification(Task task)",
          "at System.Net.HttpWebRequest.EndGetResponse(IAsyncResult asyncResult)"
        ],
        "cause":         {
          "errorType": "CurlException",
          "errorMessage": "SSL peer certificate or SSH remote key was not OK",
          "stackTrace": [
            "at System.Net.Http.CurlHandler.ThrowIfCURLEError(CURLcode error)",
            "at System.Net.Http.CurlHandler.MultiAgent.FinishRequest(StrongToWeakReference`1 easyWrapper, CURLcode messageResult)"
          ]
        }
      }
    }
  }
}
```

The method in which error information is conveyed depends on the invocation type: 
+ `RequestResponse` invocation type \(that is, synchronous execution\): In this case, you get the error message back\. 

  For example, if you invoke a Lambda function using the Lambda console, the `RequestResponse` is always the invocation type and the console displays the error information returned by AWS Lambda in the **Execution result** section of the console\.
+ `Event` invocation type \(that is, asynchronous execution\): In this case AWS Lambda does not return anything\. Instead, it logs the error information in CloudWatch Logs and CloudWatch metrics\.

Depending on the event source, AWS Lambda may retry the failed Lambda function\. For more information, see [AWS Lambda Retry Behavior](retries-on-errors.md)\. 

## Function Error Handling<a name="dotnet-custom-errors"></a>

You can create custom error handling to raise an exception directly from your Lambda function and handle it directly \(Retry or Catch\) within an AWS Step Functions State Machine\. For more information, see [Handling Error Conditions Using a State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html)\. 

Consider a `CreateAccount` [state](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states.html) is a [task](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states-task.html) that writes a customer's details to a database using a Lambda function\.
+ If the task succeeds, an account is created and a welcome email is sent\.
+ If a user tries to create an account for a username that already exists, the Lambda function raises an error, causing the state machine to suggest a different username and to retry the account\-creation process\.

The following code samples demonstrate how to do this\. Note that custom errors in C\# must extend the `Exception` class\.

```
namespace Example {            
   public class AccountAlreadyExistsException : Exception {
      public AccountAlreadyExistsException(String message) :
         base(message) {
      }
   }
} 

namespace Example {
   public class Handler {
     public static void CreateAccount() {
       throw new AccountAlreadyExistsException("Account is in use!");
     }
   }
}
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