# The Context Object \(Node\.js\)<a name="nodejs-prog-model-context"></a>

While a Lambda function is executing, it can interact with AWS Lambda to get useful runtime information such as:
+ How much time is remaining before AWS Lambda terminates your Lambda function \(timeout is one of the Lambda function configuration properties\)\.
+ The CloudWatch log group and log stream associated with the Lambda function that is executing\.
+ The AWS request ID returned to the client that invoked the Lambda function\. You can use the request ID for any follow up inquiry with AWS support\.
+ If the Lambda function is invoked through AWS Mobile SDK, you can learn more about the mobile application calling the Lambda function\.

AWS Lambda provides this information via the `context` object that the service passes as the second parameter to your Lambda function handler\. For more information, see [Lambda Function Handler \(Node\.js\)](nodejs-prog-model-handler.md)\.

 The following sections provide an example Lambda function that uses the `context` object, and then lists all of the available methods and attributes\. 

## Example<a name="nodejs-prog-model-context-example"></a>

Consider the following Node\.js example\. The handler receives runtime information via a  `context`  parameter\. 

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
    //console.log('Received event:', JSON.stringify(event, null, 2));
    console.log('value1 =', event.key1);
    console.log('value2 =', event.key2);
    console.log('value3 =', event.key3);
    console.log('remaining time =', context.getRemainingTimeInMillis());
    console.log('functionName =', context.functionName);
    console.log('AWSrequestID =', context.awsRequestId);
    console.log('logGroupName =', context.logGroupName);
    console.log('logStreamName =', context.logStreamName);
    console.log('clientContext =', context.clientContext);
    if (typeof context.identity !== 'undefined') {
        console.log('Cognito
        identity ID =', context.identity.cognitoIdentityId);
    }    
    callback(null, event.key1); // Echo back the first key value
    // or
    // callback("some error type"); 
};
```

The handler code in this example logs some of the runtime information of the Lambda function to CloudWatch\. If you invoke the function using the Lambda console, the console displays the logs in the **Log output** section\. You can create a Lambda function using this code and test it using the console\.

**To test this code in the AWS Lambda console**

1. In the console, create a Lambda function using the hello\-world blueprint\. In **runtime**, choose **nodejs6\.10**\. For instructions on how to do this, see [Create a Simple Lambda Function](get-started-create-function.md)\. 

1. Test the function, and then you can also update the code to get more context information\.

## The Context Object Methods \(Node\.js\)<a name="nodejs-prog-model-context-methods"></a>

The context object provides the following methods\. 

### context\.getRemainingTimeInMillis\(\)<a name="nodejs-prog-model-context-methods-getRemainingTimeInMillis-nodejs"></a>

Returns the approximate remaining execution time \(before timeout occurs\) of the Lambda function that is currently executing\. The timeout is one of the Lambda function configuration\. When the timeout reaches, AWS Lambda terminates your Lambda function\. 

You can use this method to check the remaining time during your function execution and take appropriate corrective action at run time\.

The general syntax is:

```
context.getRemainingTimeInMillis();
```

## The Context Object Properties \(Node\.js\)<a name="nodejs-prog-model-context-properties"></a>

The `context` object provides the following property that you can update:

**callbackWaitsForEmptyEventLoop**  
The default value is true\. This property is useful only to modify the default behavior of the callback\. By default, the callback will wait until the event loop is empty before freezing the process and returning the results to the caller\. You can set this property to false to request AWS Lambda to freeze the process soon after the `callback` is called, even if there are events in the event loop\. AWS Lambda will freeze the process, any state data and the events in the event loop \(any remaining events in the event loop processed when the Lambda function is called next and if AWS Lambda chooses to use the frozen process\)\. For more information about callback, see [Using the Callback Parameter](nodejs-prog-model-handler.md#nodejs-prog-model-handler-callback)\.

In addition, the `context` object provides the following properties that you can use obtain runtime information:

**functionName**  
Name of the Lambda function that is executing\.

**functionVersion**  
The Lambda function version that is executing\. If an alias is used to invoke the function, then `function_version` will be the version the alias points to\.

**invokedFunctionArn**  
The ARN used to invoke this function\. It can be a function ARN or an alias ARN\. An unqualified ARN executes the `$LATEST` version and aliases execute the function version it is pointing to\. 

**memoryLimitInMB**  
Memory limit, in MB, you configured for the Lambda function\. You set the memory limit at the time you create a Lambda function and you can change it later\.

**awsRequestId**  
AWS request ID associated with the request\. This is the ID returned to the client that called the `invoke` method\.   
If AWS Lambda retries the invocation \(for example, in a situation where the Lambda function that is processing Kinesis records throws an exception\), the request ID remains the same\.

**logGroupName**  
The name of the CloudWatch log group where you can find logs written by your Lambda function\.

**logStreamName**  
 The name of the CloudWatch log group where you can find logs written by your Lambda function\. The log stream may or may not change for each invocation of the Lambda function\.  
The value is null if your Lambda function is unable to create a log stream, which can happen if the execution role that grants necessary permissions to the Lambda function does not include permissions for the CloudWatch actions\.

**identity**  
Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK\. It can be null\.  
+ **identity\.cognitoIdentityId**
+ **identity\.cognitoIdentityPoolId**
For more information about the exact values for a specific mobile platform, see [Identity Context](http://docs.aws.amazon.com/mobile/sdkforios/developerguide/lambda.html#identitycontext) in the *AWS Mobile SDK for iOS Developer Guide*, and [Identity Context](http://docs.aws.amazon.com/mobile/sdkforandroid/developerguide/lambda.html#identity-context) in the AWS Mobile SDK for Android Developer Guide\.

**clientContext**  
Information about the client application and device when invoked through the AWS Mobile SDK\. It can be null\. Using `clientContext`, you can get the following information:  
+ **clientContext\.client\.installation\_id**
+ **clientContext\.client\.app\_title**
+ **clientContext\.client\.app\_version\_name**
+ **clientContext\.client\.app\_version\_code**
+ **clientContext\.client\.app\_package\_name**
+ **clientContext\.Custom**

  Custom values set by the mobile client application\.
+ **clientContext\.env\.platform\_version**
+ **clientContext\.env\.platform**
+ **clientContext\.env\.make**
+ **clientContext\.env\.model**
+ **clientContext\.env\.locale**

For more information about the exact values for a specific mobile platform, see [Client Context](http://docs.aws.amazon.com/mobile/sdkforios/developerguide/lambda.html#clientcontext) in the *AWS Mobile SDK for iOS Developer Guide*, and [Client Context](http://docs.aws.amazon.com/mobile/sdkforandroid/developerguide/lambda.html#client-context) in the *AWS Mobile SDK for Android Developer Guide*\.