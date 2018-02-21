# The Context Object \(Go\)<a name="go-programming-model-context"></a>

While a Lambda function is executing, it can interact with AWS Lambda to get useful runtime information such as:

+ How much time is remaining before AWS Lambda terminates your Lambda function \(timeout is one of the Lambda function configuration properties\)\.

+ The [CloudWatch](https://aws.amazon.com/documentation/cloudwatch/) log group and log stream associated with the Lambda function that is executing\.

+ The AWS request ID returned to the client that invoked the Lambda function\. You can use the request ID for any follow up inquiry with AWS support\.

+ If the Lambda function is invoked through AWS Mobile SDK, you can learn more about the mobile application calling the Lambda function\.

+ In addition to the options listed below, you can also use the AWS X\-Ray SDK for [Go](go-tracing.md) to identify critical code paths, trace their performance and capture the data for analysis\. 

AWS Lambda provides this information via the `context.Context` object that the service passes as a parameter to your Lambda function handler\. For more information, see [Valid Handler Signatures ](go-programming-model-handler-types.md#go-programming-model-handler-types-signatures)\.

 The following sections provide an example Lambda function that uses the `context` object, and then lists all of the available methods and attributes\. 

## Accessing Invoke Context Information<a name="go-programming-model-context-access"></a>

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
        log.Print(lc.Identity.CognitoPoolID)
}
 
func main() {
        lambda.Start(CognitoHandler)
}
```

In the example above, `lc` is the variable used to consume the information that the context object captured and `log.Print(lc.Identity.CognitoPoolID)` prints that information, in this case, the CognitoPoolID\.

### Monitoring Execution Time of a Function<a name="go-programming-model-monitoring-execution-time"></a>

The following example introduces how to use the context object to monitor how long it takes to execute your Lambda function\. This allows you to analyze performance expectations and adjust your function code accordingly, if needed\. 

```
package main
 
import (
        "context"
        "log"
        "time"
        "github.com/aws/aws-lambda-go/lambda"
)
 
func LongRunningHandler(ctx context.Context) string {
        deadline, _ := ctx.Deadline()
        for {
                select {
                case <- time.Until(deadline).Truncate(100 * time.Millisecond):
                        return "Finished before timing out."
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

The Lambda context library provides the following global variables:

+ `MemoryLimitInMB`: Memory limit, in MB, you configured for the Lambda function\.

+ `FunctionName`: Name of the Lambda function that is running\.

+ `FunctionVersion`: The Lambda function version that is executing\. If an alias is used to invoke the function, then `FunctionVersion` will be the version the alias points to\.

+ `LogStreamName`: The CloudWatch log stream name for the particular Lambda function execution\. It can be null if the IAM user provided does not have permission for CloudWatch actions\.

+ `LogGroupName`: The CloudWatch log group name associated with the Lambda function invoked\. It can be null if the IAM user provided does not have permission for CloudWatch actions\.

The Lambda context object also includes the following properties:

+  `AwsRequestID`: AWS request ID associated with the request\. This is the ID returned to the client that invoked this Lambda function\. You can use the request ID for any follow up inquiry with AWS support\. Note that if AWS Lambda retries the function \(for example, in a situation where the Lambda function processing Kinesis records throw an exception\), the request ID remains the same\.

+ `ClientContext`: Information about the client application and device when invoked through the AWS Mobile SDK\. It can be null\.  Client context provides client information such as client ID, application title, version name, version code, and the application package name\.

+  `Identity`: Noted in the preceding example\. Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK\. It can be null\.

+ `InvokedFunctionArn`: The ARN used to invoke this function\. It can be function ARN or alias ARN\. An unqualified ARN executes the `$LATEST` version and aliases execute the function version it is pointing to\. 

## Next Step<a name="go-programming-model-next-step-logging"></a>

[Logging \(Go\) ](go-programming-model-logging.md)