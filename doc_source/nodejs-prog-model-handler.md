# AWS Lambda Function Handler in Node\.js<a name="nodejs-prog-model-handler"></a>

AWS Lambda invokes your Lambda function via a `handler` object\. A `handler` represents the name of your Lambda function and serves as the entry point that AWS Lambda uses to execute your function code\. For example: 

```
exports.myHandler = function(event, context, callback) {   
    ... function code   
    callback(null, "some success message");
   // or 
   // callback("some error type"); 
}
```
+ `myHandler` – This is the name of the function AWS Lambda invokes\. Suppose you save this code as `helloworld.js`\. Then, `myHandler` is the function that contains your Lambda function code and `helloworld` is the name of the file that represents your deployment package\. For more information, see [AWS Lambda Deployment Package in Node\.js](nodejs-create-deployment-pkg.md)\.
+ `context` – AWS Lambda uses this parameter to provide details of your Lambda function's execution\. For more information, see [AWS Lambda Context Object in Node\.js](nodejs-prog-model-context.md)\.
+ `callback` \(optional\) – See [Using the Callback Parameter](#nodejs-prog-model-handler-callback)\.

## Using the Callback Parameter<a name="nodejs-prog-model-handler-callback"></a>

Node\.js runtimes support the optional `callback` parameter\. You can use it to explicitly return information back to the caller\.

```
callback(Error error, Object result);
```

Both parameters are optional\. `error` is an optional parameter that you can use to provide results of the failed Lambda function execution\. When a Lambda function succeeds, you can pass null as the first parameter\.

`result` is an optional parameter that you can use to provide the result of a successful function execution\. The result provided must be `JSON.stringify` compatible\. If an error is provided, this parameter is ignored\. 

If you don't use `callback` in your code, AWS Lambda will call it implicitly and the return value is `null`\. When the callback is called, AWS Lambda continues the Lambda function invocation until the event loop is empty\.

The following are example callbacks:

```
callback();     // Indicates success but no information returned to the caller.
callback(null); // Indicates success but no information returned to the caller.
callback(null, "success");  // Indicates success with information returned to the caller.
callback(error);    //  Indicates error with error information returned to the caller.
```

AWS Lambda treats any non\-null value for the `error` parameter as a handled exception\. 

The callback method automatically logs the string representation of non\-null values of `error` to the Amazon CloudWatch Logs stream associated with the Lambda function\. 

If the Lambda function was invoked synchronously, the callback returns a response body\. If `error` is null, the response body is set to the string representation of `result`\. If the `error` is not null, the `error` value will be populated in the response body\. 

When the `callback(error, null)` \(and `callback(error)`\) is called, Lambda will log the first 256 KB of the error object\. For a larger error object, AWS Lambda truncates the log and displays the text `Truncated by Lambda` next to the error object\.

If you are using runtime version 8\.10, you can include the `async` keyword:

```
exports.myHandler = async function(event, context) {
            ...
            
            // return information to the caller.  
}
```

## Example<a name="nodejs-prog-model-handler-example"></a>

Consider the following example code\.

```
exports.myHandler = function(event, context, callback) {
   console.log("value1 = " + event.key1);
   console.log("value2 = " + event.key2);  
   callback(null, "some success message");
   // or 
   // callback("some error type"); 
}
```

This example has one function, *myHandler*\.

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