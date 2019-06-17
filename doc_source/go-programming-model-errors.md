# AWS Lambda Function Errors in Go<a name="go-programming-model-errors"></a>

You can create custom error handling to raise an exception directly from your Lambda function and handle it directly\. 

The following code samples demonstrate how to do this\. Note that custom errors in Go must import the `errors` module\.

```
package main
 
import (
        "errors"
        "github.com/aws/aws-lambda-go/lambda"
)
 
func OnlyErrors() error {
        return errors.New("something went wrong!")
}
 
func main() {
        lambda.Start(OnlyErrors)
}
```

Which returns the following:

```
{
  "errorMessage": "something went wrong!",
  "errorType": "errorString"
}
```

## Function Error Handling<a name="python-custom-errors"></a>

You can create custom error handling to raise an exception directly from your Lambda function and handle it directly \(Retry or Catch\) within an AWS Step Functions State Machine\. For more information, see [Handling Error Conditions Using a State Machine](https://docs.aws.amazon.com/step-functions/latest/dg/tutorial-handling-error-conditions.html)\. 

Consider a `CreateAccount` [state](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states.html) is a [task](https://docs.aws.amazon.com/step-functions/latest/dg/awl-ref-states-task.html) that writes a customer's details to a database using a Lambda function\.
+ If the task succeeds, an account is created and a welcome email is sent\.
+ If a user tries to create an account for a username that already exists, the Lambda function raises an error, causing the state machine to suggest a different username and to retry the account\-creation process\.

The following code samples demonstrate how to do this\. 

```
package main

type CustomError struct {}

func (e *CustomError) Error() string {
	return "bad stuff happened..."
}

func MyHandler() (string, error) {
	return "", &CustomError{}
}
```

At runtime, AWS Step Functions catches the error, [transitioning](https://docs.aws.amazon.com/step-functions/latest/dg/concepts-transitions.html) to the `SuggestAccountName` state as specified in the `Next` transition\.

Custom error handling makes it easier to create [serverless](https://aws.amazon.com/serverless) applications\. This feature integrates with all the languages supported by the Lambda [Programming Model](programming-model-v2.md), allowing you to design your application in the programming languages of your choice, mixing and matching as you go\.

To learn more about creating your own serverless applications using AWS Step Functions and AWS Lambda, see [AWS Step Functions](https://aws.amazon.com/step-functions/)\. 

## Handling Unexpected Errors<a name="go-errors-panic"></a>

Lambda functions can fail for reasons beyond your control, such as network outages\. These are exceptional circumstances\. In Go, [panic](https://gobyexample.com/panic) addresses these issues\. If your code panics, Lambda will attempt to capture the error and serialize it into the standard error json format\. Lambda will also attempt to insert the value of the panic into the function's CloudWatch logs\. After returning the response, Lambda will re\-create the function automatically\. If you find it necessary, you can include the `panic` function in your code to customize the error response\.

```
package main

import (
	"errors"

	"github.com/aws/aws-lambda-go/lambda"
)

func handler(string) (string, error) {
	panic(errors.New("Something went wrong"))
}

func main() {
	lambda.Start(handler)
}
```

Which would return the following stack in json:

```
{
    "errorMessage": "Something went wrong",
       "errorType": "errorString",
    "stackTrace": [ 
            {
            "path": "github.com/aws/aws-lambda-go/lambda/function.go",
            "line": 27,
            "label": "(*Function).Invoke.function"
        },
 ...
    
    ]
}
```