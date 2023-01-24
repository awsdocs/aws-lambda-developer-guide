# Custom AWS Lambda runtimes<a name="runtimes-custom"></a>

You can implement an AWS Lambda runtime in any programming language\. A runtime is a program that runs a Lambda function's handler method when the function is invoked\. You can include a runtime in your function's deployment package in the form of an executable file named `bootstrap`\.

A runtime is responsible for running the function's setup code, reading the handler name from an environment variable, and reading invocation events from the Lambda runtime API\. The runtime passes the event data to the function handler, and posts the response from the handler back to Lambda\.

Your custom runtime runs in the standard Lambda [execution environment](lambda-runtimes.md)\. It can be a shell script, a script in a language that's included in Amazon Linux, or a binary executable file that's compiled in Amazon Linux\.

To get started with custom runtimes, see [Tutorial – Publishing a custom runtime](runtimes-walkthrough.md)\. You can also explore a custom runtime implemented in C\+\+ at [awslabs/aws\-lambda\-cpp](https://github.com/awslabs/aws-lambda-cpp) on GitHub\.

**Topics**
+ [Using a custom runtime](#runtimes-custom-use)
+ [Building a custom runtime](#runtimes-custom-build)

## Using a custom runtime<a name="runtimes-custom-use"></a>

To use a custom runtime, set your function's runtime to `provided`\. The runtime can be included in your function's deployment package, or in a [layer](configuration-layers.md)\.

**Example function\.zip**  

```
.
├── bootstrap
├── function.sh
```

If there's a file named `bootstrap` in your deployment package, Lambda runs that file\. If not, Lambda looks for a runtime in the function's layers\. If the bootstrap file isn't found or isn't executable, your function returns an error upon invocation\.

## Building a custom runtime<a name="runtimes-custom-build"></a>

A custom runtime's entry point is an executable file named `bootstrap`\. The bootstrap file can be the runtime, or it can invoke another file that creates the runtime\. The following example uses a bundled version of Node\.js to run a JavaScript runtime in a separate file named `runtime.js`\.

**Example bootstrap**  

```
#!/bin/sh
cd $LAMBDA_TASK_ROOT
./node-v11.1.0-linux-x64/bin/node runtime.js
```

Your runtime code is responsible for completing some initialization tasks\. Then it processes invocation events in a loop until it's terminated\. The initialization tasks run once [per instance of the function](lambda-runtime-environment.md) to prepare the environment to handle invocations\.

**Initialization tasks**
+ **Retrieve settings** – Read environment variables to get details about the function and environment\.
  + `_HANDLER` – The location to the handler, from the function's configuration\. The standard format is `file.method`, where `file` is the name of the file without an extension, and `method` is the name of a method or function that's defined in the file\.
  + `LAMBDA_TASK_ROOT` – The directory that contains the function code\.
  + `AWS_LAMBDA_RUNTIME_API` – The host and port of the runtime API\.

  See [Defined runtime environment variables](configuration-envvars.md#configuration-envvars-runtime) for a full list of available variables\.
+ **Initialize the function** – Load the handler file and run any global or static code that it contains\. Functions should create static resources like SDK clients and database connections once, and reuse them for multiple invocations\.
+ **Handle errors** – If an error occurs, call the [initialization error](runtimes-api.md#runtimes-api-initerror) API and exit immediately\.

Initialization counts towards billed execution time and timeout\. When an execution triggers the initialization of a new instance of your function, you can see the initialization time in the logs and [AWS X\-Ray trace](services-xray.md)\.

**Example log**  

```
REPORT RequestId: f8ac1208... Init Duration: 48.26 ms   Duration: 237.17 ms   Billed Duration: 300 ms   Memory Size: 128 MB   Max Memory Used: 26 MB
```

While it runs, a runtime uses the [Lambda runtime interface](runtimes-api.md) to manage incoming events and report errors\. After completing initialization tasks, the runtime processes incoming events in a loop\. In your runtime code, perform the following steps in order\.

**Processing tasks**
+ **Get an event** – Call the [next invocation](runtimes-api.md#runtimes-api-next) API to get the next event\. The response body contains the event data\. Response headers contain the request ID and other information\.
+ **Propagate the tracing header** – Get the X\-Ray tracing header from the `Lambda-Runtime-Trace-Id` header in the API response\. Set the `_X_AMZN_TRACE_ID` environment variable locally with the same value\. The X\-Ray SDK uses this value to connect trace data between services\.
+ **Create a context object** – Create an object with context information from environment variables and headers in the API response\.
+ **Invoke the function handler** – Pass the event and context object to the handler\.
+ **Handle the response** – Call the [invocation response](runtimes-api.md#runtimes-api-response) API to post the response from the handler\.
+ **Handle errors** – If an error occurs, call the [invocation error](runtimes-api.md#runtimes-api-invokeerror) API\.
+ **Cleanup** – Release unused resources, send data to other services, or perform additional tasks before getting the next event\.

You can include the runtime in your function's deployment package, or distribute the runtime separately in a function layer\. For an example walkthrough, see [Tutorial – Publishing a custom runtime](runtimes-walkthrough.md)\.