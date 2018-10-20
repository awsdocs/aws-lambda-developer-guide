# Lambda Function Handler \(Node\.js\)<a name="nodejs-prog-model-handler"></a>

AWS Lambda invokes your Lambda function via a `handler` object\. A `handler` represents the name of your Lambda function \(and serves as the entry point that AWS Lambda uses to execute your function code\. For example:

Using Node\.js v6\.10
```
exports.myHandler = function(event, context, callback) {
    ... function code
    callback(null, "some success message");
    // or
    // callback("some error type");
}
```

Using Node\.js v8\.10 supporting `async`/`await`
```
exports.myHandler = async function(event, context) {
    ... function code
    return "some success message";
    // or
    // throw new Error("some error type");
}
```

+ `myHandler` – This is the name of the function AWS Lambda invokes\. Suppose you save this code as `helloworld.js`\. Then, `myHandler` is the function that contains your Lambda function code and `helloworld` is the name of the file that represents your deployment package\. For more information, see [Creating a Deployment Package \(Node\.js\)](nodejs-create-deployment-pkg.md)\.

  AWS Lambda supports two invocation types:
  + **RequestResponse**, or *synchronous execution*: AWS Lambda returns the result of the function call to the client invoking the Lambda function\. If the handler code of your Lambda function does not specify a return value, AWS Lambda will automatically return `null` for that value\. For a simple sample, see [Example](#nodejs-prog-model-handler-example)\.
  + **Event**, or *asynchronous execution*: AWS Lambda will discard any results of the function call\. 
**Note**  
If you discover that your Lambda function does not process the event using asynchronous invocation, you can investigate the failure using [Dead Letter Queues](dlq.md)\.

     Event sources can range from a supported AWS service or custom applications that invoke your Lambda function\. For examples, see [Sample Events Published by Event Sources](eventsources.md)\. For a simple sample, see [Example](#nodejs-prog-model-handler-example)\. 
+ `context` – AWS Lambda uses this parameter to provide details of your Lambda function's execution\. For more information, see [The Context Object \(Node\.js\)](nodejs-prog-model-context.md)\.
+ `callback` \(optional\) – See [Using the Callback Parameter](#nodejs-prog-model-handler-callback)\.

## Using the Callback Parameter<a name="nodejs-prog-model-handler-callback"></a>

The Node\.js runtimes v6\.10 and v8\.10 support the optional `callback` parameter\. You can use it to explicitly return information back to the caller\. The general syntax is:

```
callback(error, result);
```

Where:
+ `error` – is an optional parameter that you can use to provide results of the failed Lambda function execution\. When a Lambda function succeeds, you can pass null as the first parameter\.
+  `result` – is an optional parameter that you can use to provide the result of a successful function execution\. The result provided must be `JSON.stringify` compatible\. If an error is provided, this parameter is ignored\. 

If you don't use `callback` in your code, AWS Lambda will call it implicitly and the return value is `null`\.

When the callback is called \(explicitly or implicitly\), AWS Lambda continues the Lambda function invocation until the event loop is empty\.

The following are example callbacks:

```
callback();     // Indicates success but no information returned to the caller.
callback(null); // Indicates success but no information returned to the caller.
callback(null, "success");  // Indicates success with information returned to the caller.
callback(error);    //  Indicates error with error information returned to the caller.
```

AWS Lambda treats any non\-null value for the `error` parameter as a handled exception\. 

Note the following:
+ Regardless of the invocation type specified at the time of the Lambda function invocation \(see [Invoke](API_Invoke.md)\), the callback method automatically logs the string representation of non\-null values of `error` to the Amazon CloudWatch Logs stream associated with the Lambda function\. 
+ If the Lambda function was invoked synchronously \(using the `RequestResponse` invocation type\), the callback returns a response body as follows:
  + If `error` is null, the response body is set to the string representation of `result`\. 
  + If the `error` is not null, the `error` value will be populated in the response body\. 

**Note**  
When the `callback(error, null)` \(and `callback(error)`\) is called, Lambda will log the first 256 KB of the error object\. For a larger error object, AWS Lambda truncates the log and displays the text `Truncated by Lambda` next to the error object\.

## Example<a name="nodejs-prog-model-handler-example"></a>

Consider the following Node\.js example code\. 

```
exports.myHandler = function(event, context, callback) {
    console.log("value1 = " + event.key1);
    console.log("value2 = " + event.key2);  
    callback(null, "some success message");
    // or 
    // callback("some error type"); 
}
```

This example has one function, *myHandler*

In the function, the `console.log()` statements log some of the incoming event data to CloudWatch Logs\. When the `callback` parameter is called, the Lambda function exits only after the event loop passed is empty\.

If you want to use the `async` feature provided by the v8\.10 runtime, consider the following code sample:

```
exports.myHandler = async function(event, context) {
    console.log("value1 = " + event.key1);
    console.log("value2 = " + event.key2);  
    return "some success message";
    // or 
    // throw new Error("some error type"); 
}
```

**To upload and test this code as a Lambda function \(console\)**

1. In the console, create a Lambda function using the following information:
   + Use the hello\-world blueprint\. 
   + The sample uses **nodejs6\.10** as the **runtime** but you can also select **nodejs8\.10**\. The code samples provided will work for any version\.

   For instructions to create a Lambda function using the console, see [Create a Simple Lambda Function](get-started-create-function.md)\.

1. Replace the template code with the code provided in this section and create the function\.

1. Test the Lambda function using the **Sample event template** called **Hello World** provided in the Lambda console\. 
