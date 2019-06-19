# AWS Lambda Function Errors in PowerShell<a name="powershell-exceptions"></a>

If your Lambda function has a terminating error, AWS Lambda recognizes the failure, serializes the error information into JSON, and returns it\.

Consider the following PowerShell script example statement:

```
throw 'The Account is not found'
```

When you invoke this Lambda function, it throws a terminating error, and AWS Lambda returns the following error message:

```
{
  "errorMessage": "The Account is not found",
  "errorType": "RuntimeException"
}
```

Note the `errorType` is `RuntimeException`, which is the default exception thrown by PowerShell\. You can use custom error types by throwing the error like this:

```
throw @{'Exception'='AccountNotFound';'Message'='The Account is not found'}
```

The error message is serialized with `errorType` set to `AccountNotFound`:

```
{
  "errorMessage": "The Account is not found",
  "errorType": "AccountNotFound"
}
```

If you don't need an error message, you can throw a string in the format of an error code\. The error code format requires that the string starts with a character and only contain letters and digits afterwards, with no spaces or symbols\.

For example, if your Lambda function contains the following:

```
throw 'AccountNotFound'
```

The error is serialized like this:

```
{
  "errorMessage": "AccountNotFound",
  "errorType": "AccountNotFound"
}
```

## Function Error Handling<a name="powershell-custom-errors"></a>

You can use a custom errorType in your Lambda function and handle function errors directly \(Retry or Catch\) within an AWS Step Functions State Machine\. For more information, see [Handling Error Conditions Using a State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html)\. 

Custom error handling makes it easier to create [serverless](https://aws.amazon.com/serverless) applications\. This feature integrates with all the languages that are supported by the Lambda [Programming Model](programming-model-v2.md)\. This allows you to design your application in the programming languages of your choice, mixing and matching as you go\.

To learn more about creating your own serverless applications using AWS Step Functions and AWS Lambda, see [AWS Step Functions](https://aws.amazon.com/step-functions/)\.