# AWS Lambda Context Object in Node\.js<a name="nodejs-prog-model-context"></a>

When Lambda runs your function, it passes a context object to the [handler](nodejs-prog-model-handler.md)\. This object provides methods and properties that provide information about the invocation, function, and execution environment\.

**Context Methods**
+ `getRemainingTimeInMillis()` – Returns the number of milliseconds left before the execution times out\.

**Context Properties**
+ `functionName` – The name of the Lambda function\.
+ `functionVersion` – The [version](versioning-aliases.md) of the function\.
+ `invokedFunctionArn` – The Amazon Resource Name \(ARN\) used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `memoryLimitInMB` – The amount of memory configured on the function\.
+ `awsRequestId` – The identifier of the invocation request\.
+ `logGroupName` – The log group for the function\.
+ `logStreamName` – The log stream for the function instance\.
+ `identity` – \(mobile apps\) Information about the Amazon Cognito identity that authorized the request\.
  + `cognitoIdentityId` – The authenticated Amazon Cognito identity\.
  + `cognitoIdentityPoolId` – The Amazon Cognito identity pool that authorized the invocation\.
+ `clientContext` – \(mobile apps\) Client context provided to the Lambda invoker by the client application\.
  + `client.installation_id`
  + `client.app_title`
  + `client.app_version_name`
  + `client.app_version_code`
  + `client.app_package_name`
  + `env.platform_version`
  + `env.platform`
  + `env.make`
  + `env.model`
  + `env.locale`
  + `Custom` – Custom values set by the mobile application\.
+ `callbackWaitsForEmptyEventLoop` – Set to false to send the response right away when the [callback](nodejs-prog-model-handler.md#nodejs-prog-model-handler-callback) executes, instead of waiting for the Node\.js event loop to be empty\. If false, any outstanding events will continue to run during the next invocation\.

The following example shows a handler function that logs context information\.

**Example index\.js**  

```
exports.handler = function(event, context, callback) {
    console.log('remaining time =', context.getRemainingTimeInMillis());
    console.log('functionName =', context.functionName);
    console.log('AWSrequestID =', context.awsRequestId);
    callback(null, context.functionName);
};
```