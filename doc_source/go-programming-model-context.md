# AWS Lambda Context Object in Go<a name="go-programming-model-context"></a>

When Lambda runs your function, it passes a context object to the [handler](go-programming-model-handler-types.md)\. This object provides methods and properties with information about the invocation, function, and execution environment\.

The Lambda context library provides the following global variables, methods, and properties\.

**Global Variables**
+ `FunctionName` – The name of the Lambda function\.
+ `FunctionVersion` – The [version](versioning-aliases.md) of the function\.
+ `MemoryLimitInMB` – The amount of memory configured on the function\.
+ `LogGroupName` – The log group for the function\.
+ `LogStreamName` – The log stream for the function instance\.

**Context Methods**
+ `Deadline` – Returns the date that the execution times out, in Unix time milliseconds\.

**Context Properties**
+ `InvokedFunctionArn` – The Amazon Resource Name \(ARN\) used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `AwsRequestID` – The identifier of the invocation request\.
+ `Identity` – \(mobile apps\) Information about the Amazon Cognito identity that authorized the request\.
+ `ClientContext` – \(mobile apps\) Client context provided to the Lambda invoker by the client application\.

## Accessing Invoke Context Information<a name="go-programming-model-context-access"></a>

Lambda functions have access to metadata about their environment and the invocation request\. This can be accessed at [Package context](https://golang.org/pkg/context/)\. Should your handler include `context.Context` as a parameter, Lambda will insert information about your function into the context's `Value` property\. Note that you need to import the `lambdacontext` library to access the contents of the `context.Context` object\.

```go
package main
 
import (
        "context"
        "log"
        "github.com/aws/aws-lambda-go/lambda"
        "github.com/aws/aws-lambda-go/lambdacontext"
)
 
func CognitoHandler(ctx context.Context) {
        lc, _ := lambdacontext.FromContext(ctx)
        log.Print(lc.Identity.CognitoPoolID)
}
 
func main() {
        lambda.Start(CognitoHandler)
}
```

In the example above, `lc` is the variable used to consume the information that the context object captured and `log.Print(lc.Identity.CognitoPoolID)` prints that information, in this case, the CognitoPoolID\.

### Monitoring Execution Time of a Function<a name="go-programming-model-monitoring-execution-time"></a>

The following example introduces how to use the context object to monitor how long it takes to execute your Lambda function\. This allows you to analyze performance expectations and adjust your function code accordingly, if needed\. 

```go
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
