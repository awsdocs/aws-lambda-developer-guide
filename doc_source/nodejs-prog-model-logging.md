# Logging \(Node\.js\)<a name="nodejs-prog-model-logging"></a>

 Your Lambda function can contain logging statements\. AWS Lambda writes these logs to CloudWatch\. If you use the Lambda console to invoke your Lambda function, the console displays the same logs\.

 The following Node\.js statements generate log entries: 

+ `console.log()`

+ `console.error()`

+ `console.warn()`

+ `console.info()`

 For example, consider the following Node\.js code example\. 

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
    //console.log('Received event:', JSON.stringify(event, null, 2));
    console.log('value1 =', event.key1);
    console.log('value2 =', event.key2);
    console.log('value3 =', event.key3);
    callback(null, event.key1); // Echo back the first key value
    
};
```

 The screenshot shows an example  **Log output**  section in Lambda console, you can also find these logs in CloudWatch\. For more information, see [Accessing Amazon CloudWatch Logs for AWS Lambda](monitoring-functions-logs.md)\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/logging-nodejs-console-output.png)

 The console uses the  `RequestResponse`  invocation type \(synchronous invocation\) when invoking the function, therefore it gets the return value \(`value1`\) back from AWS Lambda which the console displays\. 

**To test the preceding Node\.js code in AWS Lambda console**

1. In the console, create a Lambda function using the hello\-world blueprint\. Make sure to select the Node\.js as the **runtime**\. For instructions on how to do this, see  [Create a Simple Lambda Function](get-started-create-function.md)\.

1. Test the Lambda function using the **Sample event template** called **Hello World** provided in the Lambda console\. You can also update the code and try other logging methods and properties discussed in this section\.

For step\-by\-step instructions, see [Getting Started](getting-started.md)\.

## Finding Logs<a name="nodejs-prog-model-logging-finding-logs"></a>

You can find the logs that your Lambda function writes, as follows:

+ **In the AWS Lambda console** – The ** Log output**  section in the AWS Lambda console shows the logs\. 

+ **In the response header, when you invoke a Lambda function programmatically** – If you invoke a Lambda function programmatically, you can add the `LogType` parameter to retrieve the last 4 KB of log data that is written to CloudWatch Logs\. AWS Lambda returns this log information in the `x-amz-log-results` header in the response\. For more information, see [Invoke](http://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html)\.

  If you use AWS CLI to invoke the function, you can specify the` --log-type parameter` with value `Tail` to retrieve the same information\.

+ **In CloudWatch Logs** – To find your logs in CloudWatch you need to know the log group name and log stream name\. You can get that information by adding the `context.logGroupName`, and `context.logStreamName` methods in your code\. When you run your Lambda function, the resulting logs in the console or CLI will show you the log group name and log stream name\. 