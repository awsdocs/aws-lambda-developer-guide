# AWS Lambda function errors in Go<a name="golang-exceptions"></a>

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