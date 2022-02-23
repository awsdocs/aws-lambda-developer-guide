# AWS Lambda context object in Go<a name="golang-context"></a>

When Lambda runs your function, it passes a context object to the [handler](golang-handler.md)\. This object provides methods and properties with information about the invocation, function, and execution environment\.

The Lambda context library provides the following global variables, methods, and properties\.

**Global variables**
+ `FunctionName` – The name of the Lambda function\.
+ `FunctionVersion` – The [version](configuration-versions.md) of the function\.
+ `MemoryLimitInMB` – The amount of memory that's allocated for the function\.
+ `LogGroupName` – The log group for the function\.
+ `LogStreamName` – The log stream for the function instance\.

**Context methods**
+ `Deadline` – Returns the date that the execution times out, in Unix time milliseconds\.

**Context properties**
+ `InvokedFunctionArn` – The Amazon Resource Name \(ARN\) that's used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `AwsRequestID` – The identifier of the invocation request\.
+ `Identity` – \(mobile apps\) Information about the Amazon Cognito identity that authorized the request\.
+ `ClientContext` – \(mobile apps\) Client context that's provided to Lambda by the client application\.

## Accessing invoke context information<a name="golang-context-access"></a>

Lambda functions have access to metadata about their environment and the invocation request\. This can be accessed at [Package context](https://golang.org/pkg/context/)\. Should your handler include `context.Context` as a parameter, Lambda will insert information about your function into the context's `Value` property\. Note that you need to import the `lambdacontext` library to access the contents of the `context.Context` object\.

```
package main
 
import (
        "context"
        "log"
        "github.com/aws/aws-lambda-go/lambda"
        "github.com/aws/aws-lambda-go/lambdacontext"
)
 
func CognitoHandler(ctx context.Context) {
        lc, _ := lambdacontext.FromContext(ctx)
        log.Print(lc.Identity.CognitoIdentityPoolID)
}
 
func main() {
        lambda.Start(CognitoHandler)
}
```

In the example above, `lc` is the variable used to consume the information that the context object captured and `log.Print(lc.Identity.CognitoIdentityPoolID)` prints that information, in this case, the CognitoIdentityPoolID\.

The following example introduces how to use the context object to monitor how long your Lambda function takes to complete\. This allows you to analyze performance expectations and adjust your function code accordingly, if needed\. 

```
package main

import (
        "context"
        "log"
        "time"
        "github.com/aws/aws-lambda-go/lambda"
)

func LongRunningHandler(ctx context.Context) (string, error) {

        deadline, _ := ctx.Deadline()
        deadline = deadline.Add(-100 * time.Millisecond)
        timeoutChannel := time.After(time.Until(deadline))

        for {

                select {

                case <- timeoutChannel:
                        return "Finished before timing out.", nil

                default:
                        log.Print("hello!")
                        time.Sleep(50 * time.Millisecond)
                }
        }
}

func main() {
        lambda.Start(LongRunningHandler)
}
```