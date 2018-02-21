# Lambda Function Handler \(Node\.js\)<a name="nodejs-prog-model-handler"></a>

 At the time you create a Lambda function you specify a *handler*, a function in your code, that AWS Lambda can invoke when the service executes your code\. Use the following general syntax when creating a handler function in Node\.js\. 

```
exports.myHandler = function(event, context) {
   ...
}
```

The callback parameter is optional, depending on whether you want to return information to the caller\.

```
exports.myHandler = function(event, context, callback) {
   ...
   
   // Use callback() and return information to the caller.  
}
```

In the syntax, note the following:

+ `event` – AWS Lambda uses this parameter to pass in event data to the handler\.

+ `context` – AWS Lambda uses this parameter to provide your handler the runtime information of the Lambda function that is executing\. For more information, see [The Context Object \(Node\.js\)](nodejs-prog-model-context.md)\.

+ `callback` – You can use the optional callback to return information to the caller, otherwise return value is null\. For more information, see [Using the Callback Parameter](#nodejs-prog-model-handler-callback)\.
**Note**  
The callback is supported only in the Node\.js runtimes v6\.10 and v4\.3\. If you are using runtime v0\.10\.42, you need to use the context methods \(done, succeed, and fail\) to properly terminate the Lambda function\. For information, see [Using the Earlier Node\.js Runtime v0\.10\.42](nodejs-prog-model-using-old-runtime.md)\.

+ `myHandler` – This is the name of the function AWS Lambda invokes\. You export this so it is visible to AWS Lambda\.  Suppose you save this code as `helloworld.js`\. Then, `helloworld.myHandler` is the handler\. For more information, see handler in [CreateFunction](API_CreateFunction.md)\.

  + If you used the `RequestResponse` invocation type \(synchronous execution\), AWS Lambda returns the result of the Node\.js function call to the client invoking the Lambda function \(in the HTTP response to the invocation request, serialized into JSON\)\. For example, AWS Lambda console uses the `RequestResponse` invocation type, so when you test invoke the function using the console, the console will display the return value\.

    If the handler does not return anything, AWS Lambda returns null\.

  + If you used the `Event` invocation type \(asynchronous execution\), the value is discarded\.

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

This example has one function, which is also the handler\. In the function, the `console.log()` statements log some of the incoming event data to CloudWatch Logs\. When the callback is called, the Lambda function exits only after the Node\.js event loop is empty \(the Node\.js event loop is not the same as the event that was passed as a parameter\)\. 

**Note**  
If you are using runtime v0\.10\.42, you need to use the context methods \(done, succeed, and fail\) to properly terminate the Lambda function\. For more information, see [Using the Earlier Node\.js Runtime v0\.10\.42](nodejs-prog-model-using-old-runtime.md)\.

**To upload and test this code as a Lambda function \(console\)**

1. In the console, create a Lambda function using the following information:

   + Use the hello\-world blueprint\. 

   + We recommend specifying ** nodejs6\.10** as the **runtime** but you can also select **nodejs4\.3**\. The code samples provided will work for either version\.

   For instructions to create a Lambda function using the console, see [Create a Simple Lambda Function](get-started-create-function.md)\.

1. Replace the template code with the code provided in this section and create the function\.

1. Test the Lambda function using the **Sample event template** called **Hello World** provided in the Lambda console\. 

## Using the Callback Parameter<a name="nodejs-prog-model-handler-callback"></a>

The Node\.js runtimes v4\.3 and v6\.10 support the optional `callback` parameter\. You can use it to explicitly return information back to the caller\. The general syntax is:

```
callback(Error error, Object result);
```

Where:

+ `error` – is an optional parameter that you can use to provide results of the failed Lambda function execution\. When a Lambda function succeeds, you can pass null as the first parameter\.

+  `result` – is an optional parameter that you can use to provide the result of a successful function execution\. The result provided must be `JSON.stringify` compatible\. If an error is provided, this parameter is ignored\. 

**Note**  
Using the `callback` parameter is optional\. If you don't use the optional `callback` parameter, the behavior is same as if you called the `callback()` without any parameters\. You can specify the `callback` in your code to return information to the caller\. 

If you don't use `callback` in your code, AWS Lambda will call it implicitly and the return value is `null`\.

When the callback is called \(explicitly or implicitly\), AWS Lambda continues the Lambda function invocation until the Node\.js event loop is empty\. 

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