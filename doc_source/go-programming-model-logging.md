# Logging \(Go\)<a name="go-programming-model-logging"></a>

Your Lambda function can contain logging statements\. AWS Lambda writes these logs to CloudWatch\. If you use the Lambda console to invoke your Lambda function, the console displays the same logs\. 

For example, consider the following example\. 

```
package main
 
import (
        "log"       
        "github.com/aws/aws-lambda-go/lambda"
)
 
func HandleRequest() {
        log.Print("Hello from Lambda")
}
 
func main() {
        lambda.Start(HandleRequest)
}
```

By importing the `log` module, Lambda will write additional logging information such as the time stamp\.

You can also analyze the logs in CloudWatch\. For more information, see [Accessing Amazon CloudWatch Logs for AWS Lambda](monitoring-functions-logs.md)\.

Instead of using the `log` module, you can use `print` statements in your code as shown below:

```
package main
 
import ( 
        "fmt"        
        "github.com/aws/aws-lambda-go/lambda"
)
 
func HandleRequest() {
        fmt.Print("Hello from Lambda")
}
 
func main() {
        lambda.Start(HandleRequest)
}
```

In this case only the text passed to the print method is sent to CloudWatch\. The log entries will not have additional information that the `log.Print` function returns\. In addition, any logger that writes to `stdout` or `stderror` will seamlessly integrate with a Go function and those logs will automatically be sent to CloudWatch logs\. 

The console uses the `RequestResponse` invocation type \(synchronous invocation\) when invoking the function\. And therefore it gets the return value \("Hello from Lambda\!"\) back from AWS Lambda\.

## Finding Logs<a name="go-logging-finding-logs"></a>

You can find the logs that your Lambda function writes, as follows:

+ **In the AWS Lambda console** – The ** Log output** section in the AWS Lambda console shows the logs\. 

+ **In the response header, when you invoke a Lambda function programmatically** – If you invoke a Lambda function programmatically, you can add the `LogType` parameter to retrieve the last 4 KB of log data that is written to CloudWatch Logs\. AWS Lambda returns this log information in the `x-amz-log-results` header in the response\. For more information, see [Invoke](API_Invoke.md)\.

  If you use AWS CLI to invoke the function, you can specify the` --log-type parameter` with value `Tail` to retrieve the same information\.

+ **In CloudWatch Logs** – To find your logs in CloudWatch you need to know the log group name and log stream name\. You can use the `context.logGroupName`, and `context.logStreamName` global variables in [The Context Object \(Go\) ](#go-programming-model-logging) library to get this information\. When you run your Lambda function, the resulting logs in the console or CLI will show you the log group name and log stream name\. 

## Next Step<a name="go-programming-model-next-step-errors"></a>

[Function Errors \(Go\) ](go-programming-model-errors.md)