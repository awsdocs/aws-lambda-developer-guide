# AWS Lambda Extensions API<a name="runtimes-extensions-api"></a>

You use the Extensions API to create Lambda extensions\. Extensions provide a new way for monitoring, security and other tools to integrate with the Lambda [ execution environment](runtimes-context.md)\. For additional information, see [ Introducing AWS Lambda Extensions](https://aws.amazon.com/blogs/compute/introducing-aws-lambda-extensions-in-preview/)\.

The Extensions API builds on the existing [Runtime API](runtimes-api.md), which provides an HTTP API for custom runtimes to receive invocation events from Lambda\. As an extension author, you can use the Extensions API to register for function and execution environment lifecycle events\. In response to these events, you can start new processes or run logic\.

Lambda supports internal and external extensions\. *Internal extensions* allow you to configure the runtime environment and modify the startup of the runtime process\. Internal extensions use language\-specific environment variables and wrapper scripts, and start and shut down within the runtime process\. Internal extensions run as separate threads within the runtime process, which starts and stops them\.

An *external extension* runs as an independent process in the execution environment and continues to run after the function invoke is fully processed\. An external extension must register for the `Shutdown` event, which triggers the extension to shut down\. Because external extensions run as processes, they can be written in a different language than the function\.

The following [Lambda runtimes](lambda-runtimes.md) support external extensions:
+ \.NET Core 3\.1 \(C\#/PowerShell\) \(`dotnetcore3.1`\)
+ Custom runtime \(`provided`\)
+ Custom runtime on Amazon Linux 2 \(`provided.al2`\)
+ Java 11 \(Corretto\) \(`java11`\)
+ Java 8 \(Corretto\) \(`java8.al2`\)
+ Node\.js 12\.x \(`nodejs12.x`\)
+ Node\.js 10\.x \(`nodejs10.x`\)
+ Python 3\.8 \(`python3.8`\)
+ Python 3\.7 \(`python3.7`\)
+ Ruby 2\.7 \(`ruby2.7`\)
+ Ruby 2\.5 \(`ruby2.5`\)

We recommend that you implement external extensions using a compiled language\. In this case, the extension is a self\-contained binary that is compatible with all of the supported runtimes\. If you use a non\-compiled language, ensure that you include a compatible runtime in the extension\.

You share your extension as a [Lambda layer](configuration-layers.md) to make it available within your organization or to the entire community of Lambda developers\. Function developers can find, manage, and install extensions through layers\.

You can install and manage extensions using the Lambda console, the AWS Command Line Interface \(AWS CLI\), or infrastructure as code \(IaC\) services and tools such as AWS CloudFormation, AWS Serverless Application Model \(AWS SAM\), and Terraform\.

**Note**  
Example extensions and wrapper scripts are available in the [GitHub repository of example extensions](https://github.com/aws-samples/aws-lambda-extensions/tree/main/custom-runtime-extension-demo)\. 

## Lambda execution environment lifecycle<a name="runtimes-extensions-api-lifecycle"></a>

The lifecycle of the execution environment includes the following phases:
+ `Init`: In this phase, Lambda creates or unfreezes an execution environment with the configured resources, downloads the code for the function and all layers, initializes any extensions, initializes the runtime, and then runs the function’s initialization code \(i\.e\. the code outside the main handler\)\. The `Init` phase happens either during the first invocation, or in advance of function invokes if you have enabled [provisioned concurrency](configuration-concurrency.md#configuration-concurrency-provisioned)\.

  The `Init` phase is split into three sub\-phases: `Extension init`, `Runtime init` and `Function init`\. These sub\-phases ensure that all extensions and the runtime complete their setup tasks before the function code runs\.
+ `Invoke`: In this phase, Lambda invokes the function handler\. After the function runs to completion, Lambda prepares to handle another function invocation\.
+ `Shutdown`: This phase is triggered if the Lambda function does not receive any invokes for a period of time\. In the `Shutdown` phase, Lambda terminates the runtime, alerts the extensions to let them stop cleanly, and then removes the environment\. Lambda sends a `Shutdown` event to each extension, which tells the extension that the environment is about to be shut down\.

Each phase starts with an event from the Lambda service to the runtime and to all registered extensions\. The runtime and each extension signal completion by sending a `Next` API request\. Lambda freezes the execution environment when each process has completed and there are no pending events\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Overview-Full-Sequence.png)

**Topics**
+ [Init phase](#runtimes-extensions-api-reg)
+ [Invoke phase](#runtimes-lifecycle-invoke)
+ [Shutdown phase](#runtimes-lifecycle-shutdown)
+ [Permissions and configuration](#runtimes-extensions-registration-api-e)
+ [Failure handling](#runtimes-extensions-api-failure)
+ [Troubleshooting extensions](#runtimes-extensions-api-trbl)

### Init phase<a name="runtimes-extensions-api-reg"></a>

During the `Extension init` phase, each extension needs to register with Lambda to receive events\. Lambda uses the full file name of the extension to validate that the extension has completed the bootstrap sequence\. Therefore, each `Register` API call must include the `Lambda-Extension-Name` header with the full file name of the extension\.

You can register up to 10 extensions for a function\. This limit is enforced through the `Register` API call\.

After each extension registers with Lambda, Lambda starts the `Runtime init` phase\. The runtime process calls functionInit to start the Function init phase\. 

The `Init` phase completes after the runtime and each registered extension indicate completion by sending a `Next` API request\.

**Note**  
Extensions can complete their initialization at any point in the `Init` phase\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Init-Phase.png)

### Invoke phase<a name="runtimes-lifecycle-invoke"></a>

When a Lambda function is invoked in response to a `Next` API request, Lambda sends an `Invoke` event to the runtime and to each extension that is registered for the `Invoke` event\.

During the invocation, external extensions run in parallel with the function\. They also continue running after the function has completed\. This enables you to capture diagnostic information or send logs, metrics, and traces to a location of your choice\.

The `Invoke` phase ends after the runtime and all extensions signal that they are done by sending a `Next` API request\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Invoke-Phase.png)

**Event payload**: 

The event sent to each extension contains metadata that describes the event content\. This lifecycle event includes the type of the event, the time remaining, the `RequestId`, the invoked function ARN, and tracing headers\.

The event sent to the runtime \(and the Lambda function\) carries the entire request, headers \(such as `RequestId`\), and payload\. The event sent to each extension contains metadata that describes the event content\. This lifecycle event includes the type of the event, the time remaining, the `RequestId`, the invoked function ARN, and tracing headers\. 

Extensions that want to access the function event body can use an in\-runtime SDK that communicates with the extension\. Function developers use the in\-runtime SDK to send the payload to the extension when the function is invoked\.

Here is an example payload:

```
{
    "eventType": "INVOKE",
    "deadlineMs": 676051,
    "requestId": "3da1f2dc-3222-475e-9205-e2e6c6318895",
    "invokedFunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:ExtensionTest",
    "tracing": {
        "type": "X-Amzn-Trace-Id",
        "value": "Root=1-5f35ae12-0c0fec141ab77a00bc047aa2;Parent=2be948a625588e32;Sampled=1"
    }
 }
```

**Duration limit**: The function's [timeout setting](configuration-console.md) limits the duration of the entire `Invoke` phase\. For example, if you set the function timeout as 360 seconds, the function and all extensions need to complete within 360 seconds\. Note that there is no independent post\-invoke phase\. The duration is the sum of all invocation time \(runtime \+ extensions\) and is not calculated until the function and all extensions have finished executing\.

**Performance impact and extension overhead**: Extensions can impact function performance\. As an extension author, you have control over the performance impact of your extension\. For example, if your extension performs compute\-intensive operations, the function's duration increases because the extension and the function code share the same CPU resources\. In addition, if your extension performs extensive operations after the function invocation completes, the function duration increases because the `Invoke` phase continues until all extensions signal that they are completed\.

To help identify the overhead introduced by extensions on the `Invoke` phase, Lambda outputs the `PostRuntimeExecutionDuration` metric\. This metric measures the cumulative time spent between the runtime `Next` API request and the last extension `Next` API request\.

You can assess the performance impact of an extension by using the `PostRuntimeExecutionDuration` metric to measure the extra time an extension adds to the function execution\. To measure the increase in memory used, use the `MaxMemoryUsed` metric\. For more information about function metrics, see [Working with AWS Lambda function metrics](monitoring-metrics.md)\.

Function developers can run different versions of their functions side by side to understand the impact of a specific extension\. We recommend that extension authors publish expected resource consumption to make it easier for function developers to choose a suitable extension\.

### Shutdown phase<a name="runtimes-lifecycle-shutdown"></a>

When Lambda is about to terminate the runtime, it sends a `Shutdown` event to the runtime and then to each registered external extension\. Extensions can use this time for final cleanup tasks\. The `Shutdown` event is sent in response to a `Next` API request\.

**Duration limit**: The maximum duration of the `Shutdown` phase depends on the configuration of registered extensions:
+ 300 ms – a function with no registered extensions\.
+ 500 ms – a function with a registered internal extension\.
+ 2000 ms – a function with one or more registered external extensions\.

For a function with external extensions, Lambda reserves up to 300 ms \(500 ms for a runtime with an internal extension\) for the runtime process to perform a graceful shutdown\. Lambda allocates the remainder of the 2000 ms limit for external extensions to shut down\.

If the runtime or an extension does not respond to the `Shutdown` event within the limit, Lambda terminates the process using a `SIGKILL` signal\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Shutdown-Phase.png)

**Event payload**: The `Shutdown` event contains the reason for the shutdown and the time remaining in milliseconds\.

 The `shutdownReason` includes the following values:
+ SPINDOWN – Normal shutdown 
+ TIMEOUT – Duration limit timed out
+ FAILURE – Error condition, such as an `out-of-memory` event

```
{ 
  "eventType": "SHUTDOWN", 
  "shutdownReason": "reason for shutdown", 
  "deadlineMs": "time remaining in milliseconds" 
}
```

### Permissions and configuration<a name="runtimes-extensions-registration-api-e"></a>

Extensions run in the same execution environment as the Lambda function\. Extensions also share resources with the function, such as CPU, memory, and /tmp disk storage\. In addition, extensions use the same AWS Identity and Access Management \(IAM\) role and security context as the function\.

**File system and network access permissions**: Extensions run in the same file system and network name namespace as the function runtime\. This means that extensions need to be compatible with the associated operating system\. If an extension requires any additional network egress rules, you must apply these rules to the function configuration\.

**Note**  
Because the function code directory is read\-only, extensions cannot modify the function code\.

**Environment variables**: Extensions can access the function's [environment variables](configuration-envvars.md), except for the following variables that are specific to the runtime process:
+ `AWS_EXECUTION_ENV`
+ `AWS_LAMBDA_LOG_GROUP_NAME`
+ `AWS_LAMBDA_LOG_STREAM_NAME`
+ `AWS_XRAY_CONTEXT_MISSING`
+ `AWS_XRAY_DAEMON_ADDRESS`
+ `LAMBDA_RUNTIME_DIR`
+ `LAMBDA_TASK_ROOT`
+ `_AWS_XRAY_DAEMON_ADDRESS`
+ `_AWS_XRAY_DAEMON_PORT`
+ `_HANDLER`

### Failure handling<a name="runtimes-extensions-api-failure"></a>

**Initialization failures**: If an extension fails, Lambda restarts the execution environment to enforce consistent behavior and encourage fail fast for extensions\. Also, for some customers, the extensions must meet mission\-critical needs such as logging, security, governance, and telemetry collection\.

**Invoke failures** \(e\.g\. out of memory, function timeout\): Because extensions share resources with the runtime, they are affected by memory exhaustion\. When the runtime fails, all extensions and the runtime itself participate in the `Shutdown` phase\. In addition, the runtime is restarted—either automatically as part of the current invoke, or via a deferred re\-initialization mechanism\.

If there is a failure \(such as a function timeout or runtime error\) during `Invoke`, the Lambda service performs a reset\. The reset behaves like a `Shutdown` event\. First, Lambda shuts down the runtime, then it sends a `Shutdown` event to each registered external extension\. The event includes the reason for the shutdown\. If this environment is used for a new invocation, the extension and runtime are re\-initialized as part of the next invocation\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Overview-Invoke-with-Error.png)

**Note**  
Lambda does not automatically shut down extensions that repeatedly fail\. However, Lambda reports these errors through Amazon CloudWatch metrics and logs so that function developers and extension authors can understand and diagnose such failures\.

**Extension logs**: Lambda sends the log output of extensions to CloudWatch Logs\. Lambda also generates an additional log event for each extension during `Init`\. The log event records the name and registration preference \(event, config\) on success, or the failure reason on failure\.

### Troubleshooting extensions<a name="runtimes-extensions-api-trbl"></a>
+ If a `Register` request fails, make sure that the `Lambda-Extension-Name` header in the `Register` API call contains the full file name of the extension\.
+ If the `Register` request fails for an internal extension, make sure that the request does not register for the `Shutdown` event\.

## Extensions API reference<a name="runtimes-extensions-registration-api"></a>

The OpenAPI specification for the extensions API version **2020\-01\-01** is available here: [extensions\-api\.zip](samples/extensions-api.zip)

You can retrieve the value of the API endpoint from the `AWS_LAMBDA_RUNTIME_API` environment variable\. To send a `Register` request, use the prefix `2020-01-01/` before each API path\. For example:

```
http://${AWS_LAMBDA_RUNTIME_API}/2020-01-01/extension/register
```

**Topics**
+ [Register](#extensions-registration-api-a)
+ [Next](#extensions-api-next)
+ [Init error](#runtimes-extensions-init-error)
+ [Exit error](#runtimes-extensions-exit-error)

### Register<a name="extensions-registration-api-a"></a>

During `Extension init`, all extensions need to register with Lambda to receive events\. Lambda uses the full file name of the extension to validate that the extension has completed the bootstrap sequence\. Therefore, each `Register` API call must include the `Lambda-Extension-Name` header with the full file name of the extension\. 

Internal extensions are started and stopped by the runtime process, so they are not permitted to register for the `Shutdown` event\.

**Path** – `/extension/register`

**Method** – **POST**

**Headers **

`Lambda-Extension-Name` – The full file name of the extension\. Required: yes\. Type: string\.

**Body parameters **

`events` – Array of the events to register for\. Required: no\. Type: array of strings\. Valid strings: `INVOKE`, `SHUTDOWN`\.

**Response headers**
+ `Lambda-Extension-Identifier` – Generated unique agent identifier \(UUID string\) that is required for all subsequent requests\.

**Response codes**
+ 200 – Response body contains the function name, function version, and handler name\.
+ 400 – Bad Request 
+ 403 – Forbidden 
+ 500 – Container error\. Non\-recoverable state\. Extension should exit promptly\. 

**Example request body**  

```
{
    'events': [ 'INVOKE', 'SHUTDOWN']
}
```

**Example response body**  

```
{
    "functionName": "helloWorld",
    "functionVersion": "$LATEST",
    "handler": "lambda_function.lambda_handler"
}
```

### Next<a name="extensions-api-next"></a>

Extensions send a `Next` API request to receive the next event, which can be an `Invoke` event or a `Shutdown` event\. The response body contains the payload, which is a JSON document that contains event data\.

The extension sends a `Next` API request to signal that it is ready to receive new events\. This is a blocking call\.

Do not set a timeout on the GET call, as the extension can be suspended for a period of time until there is an event to return\.

**Path** – `/extension/event/next`

**Method** – **GET**

**Parameters**

`Lambda-Extension-Identifier` – Unique identifier for extension \(UUID string\)\. Required: yes\. Type: UUID string\.

**Response header**
+ `Lambda-Extension-Identifier` – Unique agent identifier \(UUID string\)\.

**Response codes**
+ 200 – Response contains information about the next event \(`EventInvoke` or `EventShutdown`\)\.
+ 403 – Forbidden
+ 500 – Container error\. Non\-recoverable state\. Extension should exit promptly\.

### Init error<a name="runtimes-extensions-init-error"></a>

The extension uses this method to report an initialization error to Lambda\. Call it when the extension fails to initialize after it has registered\. After Lambda receives the error, no further API calls succeed\. The extension should exit\.

**Path** – `/extension/init/error`

**Method** – **POST**

**Headers **

`Lambda-Extension-Identifier` – Unique identifier for extension\. Required: yes\. Type: UUID string\.

`Lambda-Extension-Function-Error-Type` – Error enum\. Required: yes\. Type: String error enum \(e\.g\. Fatal\.ConnectFailed\)\.

**Body parameters**

`ErrorRequest` – JSON object with the error type, error message, and stack trace\.

**Response body**
+ `Lambda-Extension-Identifier` – Unique agent identifier \(UUID string\)\.

**Response codes**
+ 202 – Accepted
+ 400 – Bad Request
+ 403 – Forbidden
+ 500 – Container error\. Non\-recoverable state\. Extension should exit promptly\.

### Exit error<a name="runtimes-extensions-exit-error"></a>

The extension uses this method to report an error to Lambda before exiting\. Call it when you encounter an unexpected failure\. After Lambda receives the error, no further API calls succeed\. The extension should exit\.

**Path** – `/extension/exit/error`

**Method** – **POST**

**Headers**

`Lambda-Extension-Identifier` – Unique identifier for extension\. Required: yes\. Type: UUID string\.

`Lambda-Extension-Function-Error-Type` – Error enum\. Required: yes\. Type: String error enum \(e\.g\. Fatal\.ConnectFailed\)\.

**Body parameters**

`ErrorRequest` – JSON object with the error type, error message, and stack trace\.

**Response body**
+ `Lambda-Extension-Identifier` – Unique agent identifier \(UUID string\)\.

**Response codes**
+ 202 – Accepted
+ 400 – Bad Request
+ 403 – Forbidden
+ 500 – Container error\. Non\-recoverable state\. Extension should exit promptly\.