# AWS Lambda Function Errors in Java<a name="java-exceptions"></a>

If your Lambda function throws an exception, AWS Lambda recognizes the failure and serializes the exception information into JSON and returns it\. Following is an example error message:

```
{
  "errorMessage": "Name John Doe is invalid. Exception occurred...",
  "errorType": "java.lang.Exception",
  "stackTrace": [
    "example.Hello.handler(Hello.java:9)",
    "sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
    "sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)",
    "sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)",
    "java.lang.reflect.Method.invoke(Method.java:497)"
  ]
}
```

Note that the stack trace is returned as the `stackTrace` JSON array of stack trace elements\. 

The method in which you get the error information back depends on the invocation type that you specified at the time you invoked the function: 
+ `RequestResponse` invocation type \(that is, synchronous execution\): In this case, you get the error message back\. 

  For example, if you invoke a Lambda function using the Lambda console, the `RequestResponse` is always the invocation type and the console displays the error information returned by AWS Lambda in the **Execution result** section as shown in the following image\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/exception-shown-in-console.png)
+ `Event` invocation type \(that is, asynchronous execution\): In this case AWS Lambda does not return anything\. Instead, it logs the error information in CloudWatch Logs and CloudWatch metrics\.

Depending on the event source, AWS Lambda may retry the failed Lambda function\. For example, if Kinesis is the event source for the Lambda function, AWS Lambda retries the failed function until the Lambda function succeeds or the records in the stream expire\.