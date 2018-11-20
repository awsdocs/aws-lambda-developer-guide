# AWS Lambda Function Errors in Node\.js<a name="nodejs-prog-mode-exceptions"></a>

 If your Lambda function notifies AWS Lambda that it failed to execute properly, Lambda will attempt to convert the error object to a String\. Consider the following example:

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
    // This example code only throws error. 
    var error = new Error("something is wrong");
    callback(error);
   
};
```

 When you invoke this Lambda function, it will notify AWS Lambda that function execution completed with an error and passes the error information to AWS Lambda\. AWS Lambda returns the error information back to the client: 

```
{
  "errorMessage": "something is wrong",
  "errorType": "Error",
  "stackTrace": [
    "exports.handler (/var/task/index.js:10:17)"
  ]
}
```

You would get the same result if you write the function using the async feature of Node\.js runtime version 8\.10\. For example:

```
exports.handler = async function(event, context) {                
    function AccountAlreadyExistsError(message) {
        this.name = "AccountAlreadyExistsError";
        this.message = message;
    }
    AccountAlreadyExistsError.prototype = new Error();
 
    const error = new AccountAlreadyExistsError("Account is in use!");
    throw error
};
```

Again, when this Lambda function is invoked, it will notify AWS Lambda that function execution completed with an error and passes the error information to AWS Lambda\. AWS Lambda returns the error information back to the client:

```
{
  "errorMessage": "Acccount is in use!",
  "errorType": "Error",
  "stackTrace": [
    "exports.handler (/var/task/index.js:10:17)"
  ]
}
```

 Note that the error information is returned as the `stackTrace` JSON array of stack trace elements\. 

How you get the error information back depends on the invocation type that the client specifies at the time of function invocation: 
+ If a client specifies the `RequestResponse` invocation type \(that is, synchronous execution\), it returns the result to the client that made the invoke call\.

  For example, the console always use the `RequestResponse` invocation type, so the console will display the error in the **Execution result** section as shown:  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/exception-shown-in-console-nodejs.png)

   The same information is also sent to CloudWatch and the  **Log output**  section shows the same logs\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/exception-shown-in-console20-nodejs.png)
+ If a client specifies the  `Event` invocation type \(that is, asynchronous execution\), AWS Lambda will not return anything\. Instead, it logs the error information to [CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html)\. You can also see the error metrics in [CloudWatch Metrics](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/viewing_metrics_with_cloudwatch.html)\. 

 Depending on the event source, AWS Lambda may retry the failed Lambda function\. For example, if Kinesis is the event source, AWS Lambda will retry the failed invocation until the Lambda function succeeds or the records in the stream expire\. For more information on retries, see [AWS Lambda Retry Behavior](retries-on-errors.md)\.

**To test the preceding Node\.js code \(console\)**

1. In the console, create a Lambda function using the hello\-world blueprint\. In **runtime**, choose **Node\.js**  and, in **Role**, choose **Basic execution role**\. For instructions on how to do this, see [Create a Simple Lambda Function](get-started-create-function.md)\. 

1. Replace the template code with the code provided in this section\.

1. Test the Lambda function using the **Sample event template** called **Hello World** provided in the Lambda console\. 

## Function Error Handling<a name="nodejs-prog-model-custom-exceptions"></a>

You can create custom error handling to raise an exception directly from your Lambda function and handle it directly \(Retry or Catch\) within an AWS Step Functions State Machine\. For more information, see [Handling Error Conditions Using a State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html)\. 

Consider a `CreateAccount` [state](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states.html) is a [task](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states-task.html) that writes a customer's details to a database using a Lambda function\.
+ If the task succeeds, an account is created and a welcome email is sent\.
+ If a user tries to create an account for a username that already exists, the Lambda function raises an error, causing the state machine to suggest a different username and to retry the account\-creation process\.

The following code samples demonstrate how to do this\. Note that custom errors in Node\.js must extend the error prototype\.

```
exports.handler = function(event, context, callback) {                
    function AccountAlreadyExistsError(message) {
        this.name = "AccountAlreadyExistsError";
        this.message = message;
    }
    AccountAlreadyExistsError.prototype = new Error();
 
    const error = new AccountAlreadyExistsError("Account is in use!");
    callback(error);
};
```

You can configure Step Functions to catch the error using a `Catch` rule:

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
               "ErrorEquals": ["AccountAlreadyExistsError"],
               "Next": "SuggestAccountName"
            }
         ]
      },
      …
   }
}
```

At runtime, AWS Step Functions catches the error, [transitioning](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-transitions.html) to the `SuggestAccountName` state as specified in the `Next` transition\.

**Note**  
The name property of the `Error` object must match the `ErrorEquals` value\.

Custom error handling makes it easier to create [serverless](https://aws.amazon.com/serverless) applications\. This feature integrates with all the languages supported by the Lambda [Programming Model](programming-model-v2.md), allowing you to design your application in the programming languages of your choice, mixing and matching as you go\.

To learn more about creating your own serverless applications using AWS Step Functions and AWS Lambda, see [AWS Step Functions](https://aws.amazon.com/step-functions/)\. 