# AWS Lambda Function Handler in Go<a name="go-programming-model-handler-types"></a>

A Lambda function written in [Go](https://golang.org/) is authored as a Go executable\. In your Lambda function code, you need to include the [github\.com/aws/aws\-lambda\-go/lambda](https://github.com/aws/aws-lambda-go/tree/master/lambda) package, which implements the Lambda programming model for Go\. In addition, you need to implement handler function code and a `main()` function\. 

```
package main

import (
        "fmt"
        "context"
        "github.com/aws/aws-lambda-go/lambda"
)

type MyEvent struct {
        Name string `json:"name"`
}

func HandleRequest(ctx context.Context, name MyEvent) (string, error) {
        return fmt.Sprintf("Hello %s!", name.Name ), nil
}

func main() {
        lambda.Start(HandleRequest)
}
```

Note the following:
+ **package main**: In Go, the package containing `func main()` must always be named `main`\.
+ **import**: Use this to include the libraries your Lambda function requires\. In this instance, it includes:
  + **context: **[AWS Lambda Context Object in Go](go-programming-model-context.md)\.
  + **fmt:** The Go [Formatting](https://golang.org/pkg/fmt/) object used to format the return value of your function\.
  + **github\.com/aws/aws\-lambda\-go/lambda:** As mentioned previously, implements the Lambda programming model for Go\.
+ **func HandleRequest\(ctx context\.Context, name MyEvent\) \(string, error\)**: This is your Lambda handler signature and includes the code which will be executed\. In addition, the parameters included denote the following: 
  + **ctx context\.Context**: Provides runtime information for your Lambda function invocation\. `ctx` is the variable you declare to leverage the information available via [AWS Lambda Context Object in Go](go-programming-model-context.md)\.
  + **name MyEvent**: An input type with a variable name of `name` whose value will be returned in the `return` statement\.
  + **string, error**: Returns standard [error](https://golang.org/pkg/builtin/#error) information\. For more information on custom error handling, see [AWS Lambda Function Errors in Go](go-programming-model-errors.md)\.
  + **return fmt\.Sprintf\("Hello %s\!", name\), nil**: Simply returns a formatted "Hello" greeting with the name you supplied in the handler signature\. `nil` indicates there were no errors and the function executed successfully\.
+ **func main\(\)**: The entry point that executes your Lambda function code\. This is required\.

  By adding `lambda.Start(HandleRequest)` between `func main(){}` code brackets, your Lambda function will be executed\.
**Note**  
Per Go language standards, the opening bracket, `{` must be placed directly at end the of the `main` function signature\.

## Lambda Function Handler Using Structured Types<a name="go-programming-model-handler-types-structured"></a>

In the example above, the input type was a simple string\. But you can also pass in structured events to your function handler:

```
package main
 
import (
        "fmt"
        "github.com/aws/aws-lambda-go/lambda"
)

type MyEvent struct {
        Name string `json:"What is your name?"`
        Age int     `json:"How old are you?"`
}
 
type MyResponse struct {
        Message string `json:"Answer:"`
}
 
func HandleLambdaEvent(event MyEvent) (MyResponse, error) {
        return MyResponse{Message: fmt.Sprintf("%s is %d years old!", event.Name, event.Age)}, nil
}
 
func main() {
        lambda.Start(HandleLambdaEvent)
}
```

Your request would then look like this:

```
# request
{
    "What is your name?": "Jim",
    "How old are you?": 33
}
```

And the response would look like this:

```
# response
{
    "Answer": "Jim is 33 years old!"
}
```

For more information on handling events from AWS event sources, see [aws\-lambda\-go/events](https://github.com/aws/aws-lambda-go/tree/master/events)\.

### Valid Handler Signatures<a name="go-programming-model-handler-types-signatures"></a>

You have several options when building a Lambda function handler in Go, but you must adhere to the following rules:
+ The handler must be a function\.
+ The handler may take between 0 and 2 arguments\. If there are two arguments, the first argument must implement `context.Context`\.
+ The handler may return between 0 and 2 arguments\. If there is a single return value, it must implement `error`\. If there are two return values, the second value must implement `error`\. For more information on implementing error\-handling information, see [AWS Lambda Function Errors in Go](go-programming-model-errors.md)\.

The following lists valid handler signatures\. `TIn` and `TOut` represent types compatible with the *encoding/json* standard library\. For more information, see [func Unmarshal](https://golang.org/pkg/encoding/json/#Unmarshal) to learn how these types are deserialized\.
+ 

  ```
  func ()
  ```
+ 

  ```
  func () error
  ```
+ 

  ```
  func (TIn), error
  ```
+ 

  ```
  func () (TOut, error)
  ```
+ 

  ```
  func (context.Context) error
  ```
+ 

  ```
  func (context.Context, TIn) error
  ```
+ 

  ```
  func (context.Context) (TOut, error)
  ```
+ 

  ```
  func (context.Context, TIn) (TOut, error)
  ```

## Using Global State<a name="go-programming-model-handler-execution-environment-reuse"></a>

You can declare and modify global variables that are independent of your Lambda function's handler code\. In addition, your handler may declare an `init` function that is executed when your handler is loaded\. This behaves the same in AWS Lambda as it does in standard Go programs\. A single instance of your Lambda function will never handle multiple events simultaneously\. This means, for example, that you may safely change global state, assured that those changes will require a new Execution Context and will not introduce locking or unstable behavior from function invocations directed at the previous Execution Context\. For more information, see the following:
+ [AWS Lambda Execution Context](running-lambda-code.md)
+ [Best Practices for Working with AWS Lambda Functions](best-practices.md)

```
package main
 
import (
        "log"
        "github.com/aws/aws-lambda-go/lambda"
        "github.com/aws/aws-sdk-go/aws/session"
        "github.com/aws/aws-sdk-go/service/s3"
        "github.com/aws/aws-sdk-go/aws"
)
 
var invokeCount = 0
var myObjects []*s3.Object
func init() {
        svc := s3.New(session.New())
        input := &s3.ListObjectsV2Input{
                Bucket: aws.String("examplebucket"),
        }
        result, _ := svc.ListObjectsV2(input)
        myObjects = result.Contents
}
 
func LambdaHandler() (int, error) {
        invokeCount = invokeCount + 1
        log.Print(myObjects)
        return invokeCount, nil
}
 
func main() {
        lambda.Start(LambdaHandler)
}
```