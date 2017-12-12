# Using the Earlier Node\.js Runtime v0\.10\.42<a name="nodejs-prog-model-using-old-runtime"></a>

As of May 2017, AWS Lambda supports Node\.js 6\.10 and Node\.js 4\.3\. For information about specifying this runtime when you create your Lambda function, see the `--runtime` parameter of [CreateFunction](API_CreateFunction.md)\.

Node v0\.10\.42 is currently marked as deprecated\. For more information, see [Runtime Support Policy](runtime-support-policy.md)\. You must migrate existing functions to the newer Node\.js runtime versions available on AWS Lambda \(nodejs4\.3 or nodejs6\.10\) as soon as possible\. Note that you will have to follow this procedure for each region that contains functions written in the Node v0\.10\.42 runtime\. The following section highlights AWS Lambda's runtime support policy, along with behavior unique to runtime v0\.10\.42 and how to migrate your existing functions to newer versions \(nodejs4\.3 or nodejs6\.10\) as soon as possible\. Failure to migrate or get an extension will result in any invocations of functions written in the Node v0\.10\.42 runtime returning an invalid parameter value error\. Note that you will have to follow this procedure for each region that contains functions written in the Node v0\.10\.42 runtime\. The following section describes behavior unique to runtime v0\.10\.42 and how to migrate your existing functions to newer versions\. 


+ [Transitioning Lambda Function Code to Newer Runtimes](#transition-to-new-nodejs-runtime)
+ [The Context Methods in Node\.js Runtime v0\.10\.42](#nodejs-prog-model-oldruntime-context-methods)

## Transitioning Lambda Function Code to Newer Runtimes<a name="transition-to-new-nodejs-runtime"></a>

Node v0\.10\.42 is currently marked as deprecated\. For more information, see [Runtime Support Policy](runtime-support-policy.md)\. You must migrate existing functions to the newer Node\.js runtime versions available on AWS Lambda \(nodejs4\.3 or nodejs6\.10\) as soon as possible\. Failure to migrate or get an extension will result in any invocations of functions written in the Node v0\.10\.42 runtime returning an invalid parameter error\. Note that you will have to follow this procedure for each region that contains functions written in the Node v0\.10\.42 runtime\. 

The following sections explain how to migrate your existing Lambda function code to newer runtimes: 

1. Review all your existing Lambda functions and plan your migration\. You can obtain your list of functions, along with their versions and aliases, in the following way:

   To list Lambda functions using a blueprint, see [Listing Lambda Functions and Updating to Newer Runtime Using the Runtime Update Blueprint](#transition-to-new-nodejs-runtime-blueprint)\.

   To list Lambda functions using the console:

   1. Sign in to the AWS Management Console and open the Lambda console\.

   1. Choose the **Runtime** column\. This will sort all the Lambda functions for that region by their runtime value\. 

   1. Open each Lambda function with a runtime value of Node\.js and then choose the **Configuration** tab\.

   1. Choose the **Qualifiers** dropdown list\.

   1. Select each version and view its runtime\.

   1. Select each alias to view the version it points to\.

   1. Repeat the preceeding steps for each region as necessary\.

1. For each function:

   1. Update the runtime first manually or by running the **nodejs\-upgrade\-functions** blueprint in **UPDATE** mode \(for more information see [Listing Lambda Functions and Updating to Newer Runtime Using the Runtime Update Blueprint](#transition-to-new-nodejs-runtime-blueprint)\. We strongly recommend updating any use of the context method and replacing it with the callback approach\. For more details, see [The Context Methods in Node\.js Runtime v0\.10\.42](#nodejs-prog-model-oldruntime-context-methods)\.

   1. Test and verify the Lambda function passes your internal validation for its behavior\. If it fails, you may need to update your Lambda code to work in the new runtime:

      + For a list of changes in Node\.js v6\.10, see [Breaking changes between v5 and v6](https://github.com/nodejs/node/wiki/Breaking-changes-between-v5-and-v6) on GitHub\. 

      +  For a list of changes in Node\.js v4\.3, see [API changes between v0\.10 and v4](https://github.com/nodejs/node/wiki/API-changes-between-v0.10-and-v4) on GitHub\.

   1. Once your function is invoked successfully, the transition is complete\.

1. Review your existing functions for versions and aliases\. You can obtain a list of versions for each function using either [Listing Lambda Functions and Updating to Newer Runtime Using the Lambda Console](#transition-to-new-nodejs-runtime-console) or [Listing Lambda Functions and Updating to Newer Runtime Using the Runtime Update Blueprint](#transition-to-new-nodejs-runtime-blueprint)\. For each such version:

   1. Copy the code to $LATEST\.

   1. Repeat the process from Step 2 above\.

   1. Republish the code when complete as a new version\.

   1. Update any alias that’s currently pointing to the old version to the newly published version\.

   1. Delete the old version\.

### Listing Lambda Functions and Updating the Runtime Using the CLI<a name="transition-to-new-nodejs-runtime-cli"></a>

 You can use the [ListFunctions](API_ListFunctions.md) command to return a list of all Lambda functions and from that list those created in the v0\.10 runtime\. The following code sample demonstrates how to do that: 

```
 #!/bin/bash

for REGION in $(aws ec2 describe-regions --output text --query 'Regions[].[RegionName]' | egrep -v 'ca-central-1|sa-east-1' | sort); do
        echo "...checking $REGION"
        echo " nodejs0.10 functions: "
    for i in $(aws lambda list-functions --output json --query 'Functions[*].[FunctionName, Runtime]' --region $REGION | grep -v nodejs4.3 | grep -v nodejs6.10 | grep -B1 nodejs | grep , | sort); do
        echo " -> $i"
    done
done

echo "This script only accounts for the \$LATEST versions of functions. You may need to take a closer look if you are using versioning."
```

For each Lambda function returned that was created using the v0\.10 runtime, use the [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) command and set the `--runtime` value to `nodejs4.3` or `nodejs6.10`\.

### Listing Lambda Functions and Updating to Newer Runtime Using the Lambda Console<a name="transition-to-new-nodejs-runtime-console"></a>

+ Sign in to the AWS Management Console and open the Lambda console\.

+ Choose the **Runtime** tab\. This will sort all the Lambda functions for that region by their runtime value\.

+ Open each Lambda function with a runtime value of **node\.js** and then choose the **Configuration tab**\.

+ Set the **Runtime** value to **Node\.js 4\.3** or **Node\.js 6\.10**\.

+ Repeat this process for each region, as necessary\.

### Listing Lambda Functions and Updating to Newer Runtime Using the Runtime Update Blueprint<a name="transition-to-new-nodejs-runtime-blueprint"></a>

+ Sign in to the AWS Management Console and open the Lambda console\.

+ Choose **Create a Lambda Function**\.

+ Choose the **nodejs\-upgrade\-functions** blueprint and create a function using it\.

+ Note that the function has the following environment variables available:

  + **MODE** = **List** or **Backup** or **Upgrade**

  + **TARGET\_RUNTIME** = **nodejs4\.3** or **nodejs6\.10**

  + **EXCLUDED** = a comma\-separated list of function names to exclude from processing \(do not include spaces in the list\)

+ To obtain a list of functions and versions, invoke the function from the console without any change to the variable values\.

+ To back up functions before upgrading, change the value of **MODE** to **Backup** and invoke the function from the console\. We strongly recommend you run this before upgrading your functions\.

+ To update the runtime value of functions, change the value of **MODE** to **Upgrade** and invoke the function from the console\.

+ Repeat this process for each region as necessary\.

+ Note that:

  + The blueprint will save your existing Node\.js v1\.0 function as a version and update $LATEST to nodejs4\.3 or nodejs6\.10, depending on which version you chose\. No other versions of the function can be upgraded\. You can use this version information to point any existing applications to that version\.

  + The blueprint does not modify aliases\. Any aliases pointing to that function will have to be remapped to the new version\. For more information, see [AWS Lambda Function Versioning and Aliases](versioning-aliases.md)\. 

## The Context Methods in Node\.js Runtime v0\.10\.42<a name="nodejs-prog-model-oldruntime-context-methods"></a>

Node\.js runtime v0\.10\.42 does not support the callback parameter for your Lambda function that runtimes v4\.3 and v6\.10 support\. When using runtime v0\.10\.42, you use the following context object methods to properly terminate your Lambda function\. The context object supports the `done()`, `succeed()`, and `fail()` methods that you can use to terminate your Lambda function\. These methods are also present in runtimes v4\.3 and v6\.10 for backward compatibility\. For information about transitioning your code to use runtime v4\.3 or v6\.10, see [Transitioning Lambda Function Code to Newer Runtimes](#transition-to-new-nodejs-runtime)\.

### context\.succeed\(\)<a name="nodejs-prog-model-context-methods-succeed"></a>

Indicates the Lambda function execution and all callbacks completed successfully\. Here's the general syntax:

```
context.succeed(Object result);
```

Where:

`result` – is an optional parameter and it can be used to provide the result of the function execution\. 

The `result` provided must be `JSON.stringify` compatible\. If AWS Lambda fails to stringify or encounters another error, an unhandled exception is thrown, with the `X-Amz-Function-Error` response header set to `Unhandled`\.

You can call this method without any parameters \(`succeed()`\) or pass a null value \(`succeed(null)`\)\. 

The behavior of this method depends on the invocation type specified in the Lambda function invocation\. For more information about invocation types, see [Invoke](API_Invoke.md)\. 

+ If the Lambda function is invoked using the `Event` invocation type \(asynchronous invocation\), the method will return `HTTP status 202, request accepted` response\. 

+ If the Lambda function is invoked using the `RequestResponse` invocation type \(synchronous invocation\), the method will return HTTP status 200 \(OK\) and set the response body to the string representation of the `result`\.

### context\.fail\(\)<a name="nodejs-prog-model-context-methods-fail"></a>

Indicates the Lambda function execution and all callbacks completed unsuccessfully, resulting in a handled exception\. The general syntax is shown following:

```
context.fail(Error error);
```

Where:

`error` – is an optional parameter that you can use to provide the result of the Lambda function execution\. 

If the `error` value is non\-null, the method will set the response body to the string representation of `error` and also write corresponding logs to CloudWatch\. If AWS Lambda fails to stringify or encounters another error, an unhandled error occurs with the `X-Amz-Function-Error` header set to `Unhandled`\. 

**Note**  
For the error from `context.fail(error)` and `context.done(error, null)`, Lambda logs the first 256 KB of the error object\. For larger error objects, AWS Lambda truncates the error and displays the text: ` Truncated by Lambda` next to the error object\.

You can call this method without any parameters \(`fail()`\) or pass a null value \(`fail(null)`\)\. 

### context\.done\(\)<a name="nodejs-prog-model-context-methods-done-v2"></a>

Causes the Lambda function execution to terminate\. 

**Note**  
This method complements the `succeed()` and `fail()` methods by allowing the use of the "error first" callback design pattern\.  It provides no additional functionality\. 

The general syntax is:

```
context.done(Error error, Object result);
```

Where:

+ `error` – is an optional parameter that you can use to provide results of the failed Lambda function execution\.

+  `result` – is an optional parameter that you can use to provide the result of a successful function execution\. The result provided must be `JSON.stringify` compatible\. If an error is provided, this parameter is ignored\. 

You can call this method without any parameters \(done\(\)\), or pass null \(done\(null\)\)\.

AWS Lambda treats any non\-null value for the `error` parameter as a handled exception\. 

The function behavior depends on the invocation type specified at the time of the Lambda invocation\. For more information about invocation types, see [Invoke](API_Invoke.md)\. 

+ Regardless of the invocation type, the method automatically logs the string representation of non\-null values of `error` to the Amazon CloudWatch Logs stream associated with the Lambda function\. 

+ If the Lambda function was invoked using the `RequestResponse` \(synchronous\) invocation type, the method returns response body as follows:

  + If `error` is null, set the response body to the JSON representation of `result`\. This is similar to `context.succeed()`\. 

  + If the `error` is not null or the function is called with a single argument of type `error`, the `error` value will be populated in the response body\.

**Note**  
For the error from both the `done(error, null)` and `fail(error)`, Lambda logs the first 256 KB of the error object, and for larger error object, AWS Lambda truncates the log and displays the text `Truncated by Lambda`" next to the error object\.

### Comparing the Context and Callback Methods<a name="context-and-callback"></a>

If you previously created Lambda functions using Node\.js runtime v0\.10\.42, you used one of the `context` object methods \(`done()`, `succeed()`, and `fail()`\) to terminate your Lambda function\. In Node\.js runtimes v4\.3 and v6\.10, these methods are supported primarily for backward compatibility\. We recommend you use the `callback` \(see [Using the Callback Parameter](nodejs-prog-model-handler.md#nodejs-prog-model-handler-callback)\)\. The following are `callback` examples equivalent to the `context` object methods:

+ The following example shows the `context.done()` method and corresponding equivalent `callback` supported in the newer runtime\.

  ```
  // Old way (Node.js runtime v0.10.42).
  context.done(null, 'Success message');  
  
  // New way (Node.js runtime v4.3 or v6.10).
  context.callbackWaitsForEmptyEventLoop = false; 
  callback(null, 'Success message');
  ```
**Important**  
For performance reasons, AWS Lambda may reuse the same Node\.js process for multiple executions of the Lambda function\. If this happens, AWS Lambda freezes the Node process between execution,retaining the state information it needs to continue execution\.   
When the `context` methods are called, AWS Lambda freezes the Node process immediately, without waiting for the event loop associated with the process to empty\. The process state and any events in the event loop are frozen\. When the function is invoked again, if AWS Lambda re\-uses the frozen process, the function execution continues with its same global state \(for example, events that remained in the event loop will begin to get processed\)\. However, when you use callback, AWS Lambda continues the Lambda function execution until the event loop is empty\. After all events in the event loop are processed, AWS Lambda then freezes the Node process, including any state variables in the Lambda function\. Therefore, if you want the same behavior as the context methods, you must set the `context` object property, `callbackWaitsForEmptyEventLoop`, to false\. 

+ The following example shows the `context.succeed()` method and corresponding equivalent `callback` supported in the newer runtime\.

  ```
  // Old way (Node.js runtime v0.10.42).
  context.succeed('Success message');  
  
  // New way (Node.js runtime v4.3 or v6.10).
  context.callbackWaitsForEmptyEventLoop = false; 
  callback(null, 'Success message');
  ```

+ The following example shows the `context.fail()` method and corresponding equivalent `callback` supported in the newer runtime\.

  ```
  // Old way (Node.js runtime v0.10.42).
  context.fail('Fail object');  
  
  // New way (Node.js runtime v4.3 or v6.10).
  context.callbackWaitsForEmptyEventLoop = false; 
  callback('Fail object', 'Failed result');
  ```