# Lambda execution environment<a name="lambda-runtime-environment"></a>

 Lambda invokes your function in an execution environment, which provides a secure and isolated runtime environment\. The execution environment manages the resources required to run your function\. The execution environment also provides lifecycle support for the function's runtime and any [external extensions](lambda-extensions.md) associated with your function\. 

The function's runtime communicates with Lambda using the [Runtime API](runtimes-api.md)\. Extensions communicate with Lambda using the [Extensions API](runtimes-extensions-api.md)\. Extensions can also receive log messages and other telemetry from the function by using the [Telemetry API](telemetry-api.md)\. 



![\[Architecture diagram of the execution environment.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/logs-api-concept-diagram.png)

When you create your Lambda function, you specify configuration information, such as the amount of memory available and the maximum execution time allowed for your function\. Lambda uses this information to set up the execution environment\.

The function's runtime and each external extension are processes that run within the execution environment\. Permissions, resources, credentials, and environment variables are shared between the function and the extensions\.

**Topics**
+ [Lambda execution environment lifecycle](#runtimes-lifecycle)

## Lambda execution environment lifecycle<a name="runtimes-lifecycle"></a>

The lifecycle of the execution environment includes the following phases:
+ `Init`: In this phase, Lambda creates or unfreezes an execution environment with the configured resources, downloads the code for the function and all layers, initializes any extensions, initializes the runtime, and then runs the function’s initialization code \(the code outside the main handler\)\. The `Init` phase happens either during the first invocation, or in advance of function invocations if you have enabled [provisioned concurrency](provisioned-concurrency.md)\.

  The `Init` phase is split into three sub\-phases: `Extension init`, `Runtime init`, and `Function init`\. These sub\-phases ensure that all extensions and the runtime complete their setup tasks before the function code runs\.
+ `Invoke`: In this phase, Lambda invokes the function handler\. After the function runs to completion, Lambda prepares to handle another function invocation\.
+ `Shutdown`: This phase is triggered if the Lambda function does not receive any invocations for a period of time\. In the `Shutdown` phase, Lambda shuts down the runtime, alerts the extensions to let them stop cleanly, and then removes the environment\. Lambda sends a `Shutdown` event to each extension, which tells the extension that the environment is about to be shut down\.

![\[The Init phase is followed by one or more function invocations. When there are no invocation requests, Lambda initiates the SHutdown phase.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Overview-Successful-Invokes.png)

Each phase starts with an event that Lambda sends to the runtime and to all registered extensions\. The runtime and each extension indicate completion by sending a `Next` API request\. Lambda freezes the execution environment when the runtime and each extension have completed and there are no pending events\.

**Topics**
+ [Init phase](#runtimes-lifecycle-ib)
+ [Invoke phase](#runtimes-lifecycle-invoke)
+ [Shutdown phase](#runtimes-lifecycle-shutdown)

### Init phase<a name="runtimes-lifecycle-ib"></a>

In the `Init` phase, Lambda performs three tasks:
+ Start all extensions \(`Extension init`\)
+ Bootstrap the runtime \(`Runtime init`\)
+ Run the function's static code \(`Function init`\)

The `Init` phase ends when the runtime and all extensions signal that they are ready by sending a `Next` API request\. The `Init` phase is limited to 10 seconds\. If all three tasks do not complete within 10 seconds, Lambda retries the `Init` phase at the time of the first function invocation\.

### Invoke phase<a name="runtimes-lifecycle-invoke"></a>

When a Lambda function is invoked in response to a `Next` API request, Lambda sends an `Invoke` event to the runtime and to each extension\.

The function's timeout setting limits the duration of the entire `Invoke` phase\. For example, if you set the function timeout as 360 seconds, the function and all extensions need to complete within 360 seconds\. Note that there is no independent post\-invoke phase\. The duration is the sum of all invocation time \(runtime \+ extensions\) and is not calculated until the function and all extensions have finished executing\.

The invoke phase ends after the runtime and all extensions signal that they are done by sending a `Next` API request\.

If the Lambda function crashes or times out during the `Invoke` phase, Lambda resets the execution environment\. The reset behaves like a `Shutdown` event\. First, Lambda shuts down the runtime\. Then Lambda sends a `Shutdown` event to each registered external extension\. The event includes the reason for the shutdown\. If another `Invoke` event results in this execution environment being reused, Lambda initializes the runtime and extensions as part of the next invocation\.

**Note**  
The Lambda reset does not clear the `/tmp` directory content prior to the next init phase\. This behavior is consistent with the regular shutdown phase\.

![\[This is my image.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Overview-Invoke-with-Error.png)

### Shutdown phase<a name="runtimes-lifecycle-shutdown"></a>

When Lambda is about to shut down the runtime, it sends a `Shutdown` event to each registered external extension\. Extensions can use this time for final cleanup tasks\. The `Shutdown` event is a response to a `Next` API request\.

**Duration**: The entire `Shutdown` phase is capped at 2 seconds\. If the runtime or any extension does not respond, Lambda terminates it via a signal \(`SIGKILL`\)\. 

After the function and all extensions have completed, Lambda maintains the execution environment for some time in anticipation of another function invocation\. In effect, Lambda freezes the execution environment\. When the function is invoked again, Lambda thaws the environment for reuse\. Reusing the execution environment has the following implications: 
+ Objects declared outside of the function's handler method remain initialized, providing additional optimization when the function is invoked again\. For example, if your Lambda function establishes a database connection, instead of reestablishing the connection, the original connection is used in subsequent invocations\. We recommend adding logic in your code to check if a connection exists before creating a new one\.
+ Each execution environment provides between 512 MB and 10,240 MB, in 1\-MB increments, of disk space in the `/tmp` directory\. The directory content remains when the execution environment is frozen, providing a transient cache that can be used for multiple invocations\. You can add extra code to check if the cache has the data that you stored\. For more information on deployment size limits, see [Lambda quotas](gettingstarted-limits.md)\.
+ Background processes or callbacks that were initiated by your Lambda function and did not complete when the function ended resume if Lambda reuses the execution environment\. Make sure that any background processes or callbacks in your code are complete before the code exits\.

When you write your function code, do not assume that Lambda automatically reuses the execution environment for subsequent function invocations\. Other factors may dictate a need for Lambda to create a new execution environment, which can lead to unexpected results, such as database connection failures\.