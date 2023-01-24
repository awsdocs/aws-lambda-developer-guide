# AWS Lambda function handler in Node\.js<a name="nodejs-handler"></a>

The Lambda function *handler* is the method in your function code that processes events\. When your function is invoked, Lambda runs the handler method\. When the handler exits or returns a response, it becomes available to handle another event\.

The following example function logs the contents of the event object and returns the location of the logs\.

**Example index\.js**  

```
exports.handler =  async function(event, context) {
  console.log("EVENT: \n" + JSON.stringify(event, null, 2))
  return context.logStreamName
}
```

When you configure a function, the value of the handler setting is the file name and the name of the exported handler method, separated by a dot\. The default in the console and for examples in this guide is `index.handler`\. This indicates the `handler` method that's exported from the `index.js` file\.

The runtime passes three arguments to the handler method\. The first argument is the `event` object, which contains information from the invoker\. The invoker passes this information as a JSON\-formatted string when it calls [Invoke](API_Invoke.md), and the runtime converts it to an object\. When an AWS service invokes your function, the event structure [varies by service](lambda-services.md)\.

The second argument is the [context object](nodejs-context.md), which contains information about the invocation, function, and execution environment\. In the preceding example, the function gets the name of the [log stream](nodejs-logging.md) from the context object and returns it to the invoker\.

The third argument, `callback`, is a function that you can call in [non\-async handlers](#nodejs-handler-sync) to send a response\. The callback function takes two arguments: an `Error` and a response\. When you call it, Lambda waits for the event loop to be empty and then returns the response or error to the invoker\. The response object must be compatible with `JSON.stringify`\.

For asynchronous function handlers, you return a response, error, or promise to the runtime instead of using `callback`\.

If your function has additional dependencies, [use npm to include them in your deployment package](nodejs-package.md#nodejs-package-dependencies)\.

## Async handlers<a name="nodejs-handler-async"></a>

For async handlers, you can use `return` and `throw` to send a response or error, respectively\. Functions must use the `async` keyword to use these methods to return a response or error\.

If your code performs an asynchronous task, return a promise to make sure that it finishes running\. When you resolve or reject the promise, Lambda sends the response or error to the invoker\.

**Example index\.js file – HTTP request with async handler and promises**  

```
const https = require('https')
let url = "https://docs.aws.amazon.com/lambda/latest/dg/welcome.html"   

exports.handler = async function(event) {
  const promise = new Promise(function(resolve, reject) {
    https.get(url, (res) => {
        resolve(res.statusCode)
      }).on('error', (e) => {
        reject(Error(e))
      })
    })
  return promise
}
```

For libraries that return a promise, you can return that promise directly to the runtime\.

**Example index\.js file – AWS SDK with async handler and promises**  

```
const AWS = require('aws-sdk')
const s3 = new AWS.S3()

exports.handler = async function(event) {
  return s3.listBuckets().promise()
}
```

### Using Node\.js modules and top\-level await<a name="top-level-await"></a>

You can designate your function code as an ES module, allowing you to use `await` at the top level of the file, outside the scope of your function handler\. This guarantees completion of asynchronous initialization code prior to handler invocations, maximizing the effectiveness of [provisioned concurrency](provisioned-concurrency.md) in reducing cold start latency\. You can do this in two ways: specifying the `type` as `module` in the function's `package.json` file, or by using the \.mjs file name extension\.

In the first scenario, your function code treats all \.js files as ES modules, while in the second scenario, only the file you specify with \.mjs is an ES module\. You can mix ES modules and CommonJS modules by naming them \.mjs and \.cjs respectively, as \.mjs files are always ES modules and \.cjs files are always CommonJS modules\.

For more information and an example, see [Using Node\.js ES Modules and Top\-Level Await in AWS Lambda](https://aws.amazon.com/blogs/compute/using-node-js-es-modules-and-top-level-await-in-aws-lambda)\.

## Non\-async handlers<a name="nodejs-handler-sync"></a>

The following example function checks a URL and returns the status code to the invoker\.

**Example index\.js file – HTTP request with callback**  

```
const https = require('https')
let url = "https://docs.aws.amazon.com/lambda/latest/dg/welcome.html"

exports.handler =  function(event, context, callback) {
  https.get(url, (res) => {
    callback(null, res.statusCode)
  }).on('error', (e) => {
    callback(Error(e))
  })
}
```

For non\-async handlers, function execution continues until the [event loop](https://nodejs.org/en/docs/guides/event-loop-timers-and-nexttick/) is empty or the function times out\. The response isn't sent to the invoker until all event loop tasks are finished\. If the function times out, an error is returned instead\. You can configure the runtime to send the response immediately by setting [context\.callbackWaitsForEmptyEventLoop](nodejs-context.md) to false\.

In the following example, the response from Amazon S3 is returned to the invoker as soon as it's available\. The timeout running on the event loop is frozen, and it continues running the next time the function is invoked\.

**Example index\.js file – callbackWaitsForEmptyEventLoop**  

```
const AWS = require('aws-sdk')
const s3 = new AWS.S3()

exports.handler = function(event, context, callback) {
  context.callbackWaitsForEmptyEventLoop = false
  s3.listBuckets(null, callback)
  setTimeout(function () {
    console.log('Timeout complete.')
  }, 5000)
}
```