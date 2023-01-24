# Lambda concepts<a name="gettingstarted-concepts"></a>

Lambda runs instances of your function to process events\. You can invoke your function directly using the Lambda API, or you can configure an AWS service or resource to invoke your function\.

**Topics**
+ [Function](#gettingstarted-concepts-function)
+ [Trigger](#gettingstarted-concepts-trigger)
+ [Event](#gettingstarted-concepts-event)
+ [Execution environment](#gettingstarted-concepts-ee)
+ [Instruction set architecture](#gettingstarted-concepts-arch)
+ [Deployment package](#gettingstarted-concepts-dp)
+ [Runtime](#gettingstarted-concepts-runtime)
+ [Layer](#gettingstarted-concepts-layer)
+ [Extension](#gettingstarted-concepts-extensions)
+ [Concurrency](#gettingstarted-concepts-concurrency)
+ [Qualifier](#gettingstarted-concepts-qualifier)
+ [Destination](#gettingstarted-concepts-destinations)

## Function<a name="gettingstarted-concepts-function"></a>

A *function* is a resource that you can invoke to run your code in Lambda\. A function has code to process the [events](#gettingstarted-concepts-event) that you pass into the function or that other AWS services send to the function\.

For more information, see [Configuring AWS Lambda functions](lambda-functions.md)\.

## Trigger<a name="gettingstarted-concepts-trigger"></a>

A *trigger* is a resource or configuration that invokes a Lambda function\. Triggers include AWS services that you can configure to invoke a function and [event source mappings](invocation-eventsourcemapping.md)\. An event source mapping is a resource in Lambda that reads items from a stream or queue and invokes a function\. For more information, see [Invoking Lambda functions](lambda-invocation.md) and [Using AWS Lambda with other services](lambda-services.md)\.

## Event<a name="gettingstarted-concepts-event"></a>

An *event* is a JSON\-formatted document that contains data for a Lambda function to process\. The runtime converts the event to an object and passes it to your function code\. When you invoke a function, you determine the structure and contents of the event\.

**Example custom event – weather data**  

```
{
  "TemperatureK": 281,
  "WindKmh": -3,
  "HumidityPct": 0.55,
  "PressureHPa": 1020
}
```

When an AWS service invokes your function, the service defines the shape of the event\.

**Example service event – Amazon SNS notification**  

```
{
  "Records": [
    {
      "Sns": {
        "Timestamp": "2019-01-02T12:45:07.000Z",
        "Signature": "tcc6faL2yUC6dgZdmrwh1Y4cGa/ebXEkAi6RibDsvpi+tE/1+82j...65r==",
        "MessageId": "95df01b4-ee98-5cb9-9903-4c221d41eb5e",
        "Message": "Hello from SNS!",
        ...
```

For more information about events from AWS services, see [Using AWS Lambda with other services](lambda-services.md)\.

## Execution environment<a name="gettingstarted-concepts-ee"></a>

An *execution environment* provides a secure and isolated runtime environment for your Lambda function\. An execution environment manages the processes and resources that are required to run the function\. The execution environment provides lifecycle support for the function and for any [extensions](#gettingstarted-concepts-extensions) associated with your function\.

For more information, see [Lambda execution environment](lambda-runtime-environment.md)\.

## Instruction set architecture<a name="gettingstarted-concepts-arch"></a>

 The *instruction set architecture* determines the type of computer processor that Lambda uses to run the function\. Lambda provides a choice of instruction set architectures:
+ arm64 – 64\-bit ARM architecture, for the AWS Graviton2 processor\.
+ x86\_64 – 64\-bit x86 architecture, for x86\-based processors\.

For more information, see [Lambda instruction set architectures](foundation-arch.md)\.

## Deployment package<a name="gettingstarted-concepts-dp"></a>

You deploy your Lambda function code using a *deployment package*\. Lambda supports two types of deployment packages:
+ A \.zip file archive that contains your function code and its dependencies\. Lambda provides the operating system and runtime for your function\.
+ A container image that is compatible with the [Open Container Initiative \(OCI\)](https://opencontainers.org/) specification\. You add your function code and dependencies to the image\. You must also include the operating system and a Lambda runtime\.

For more information, see [Lambda deployment packages](gettingstarted-package.md)\.

## Runtime<a name="gettingstarted-concepts-runtime"></a>

The *runtime* provides a language\-specific environment that runs in an execution environment\. The runtime relays invocation events, context information, and responses between Lambda and the function\. You can use runtimes that Lambda provides, or build your own\. If you package your code as a \.zip file archive, you must configure your function to use a runtime that matches your programming language\. For a container image, you include the runtime when you build the image\.

For more information, see [Lambda runtimes](lambda-runtimes.md)\.

## Layer<a name="gettingstarted-concepts-layer"></a>

A Lambda *layer* is a \.zip file archive that can contain additional code or other content\. A layer can contain libraries, a [custom runtime](runtimes-custom.md), data, or configuration files\.

Layers provide a convenient way to package libraries and other dependencies that you can use with your Lambda functions\. Using layers reduces the size of uploaded deployment archives and makes it faster to deploy your code\. Layers also promote code sharing and separation of responsibilities so that you can iterate faster on writing business logic\.

You can include up to five layers per function\. Layers count towards the standard Lambda [deployment size quotas](gettingstarted-limits.md)\. When you include a layer in a function, the contents are extracted to the `/opt` directory in the execution environment\.

By default, the layers that you create are private to your AWS account\. You can choose to share a layer with other accounts or to make the layer public\. If your functions consume a layer that a different account published, your functions can continue to use the layer version after it has been deleted, or after your permission to access the layer is revoked\. However, you cannot create a new function or update functions using a deleted layer version\.

Functions deployed as a container image do not use layers\. Instead, you package your preferred runtime, libraries, and other dependencies into the container image when you build the image\.

For more information, see [Creating and sharing Lambda layers](configuration-layers.md)\.

## Extension<a name="gettingstarted-concepts-extensions"></a>

Lambda *extensions* enable you to augment your functions\. For example, you can use extensions to integrate your functions with your preferred monitoring, observability, security, and governance tools\. You can choose from a broad set of tools that [AWS Lambda Partners](http://aws.amazon.com/lambda/partners/) provides, or you can [create your own Lambda extensions](runtimes-extensions-api.md)\.

An internal extension runs in the runtime process and shares the same lifecycle as the runtime\. An external extension runs as a separate process in the execution environment\. The external extension is initialized before the function is invoked, runs in parallel with the function's runtime, and continues to run after the function invocation is complete\.

For more information, see [Lambda extensions](lambda-extensions.md)\.

## Concurrency<a name="gettingstarted-concepts-concurrency"></a>

*Concurrency* is the number of requests that your function is serving at any given time\. When your function is invoked, Lambda provisions an instance of it to process the event\. When the function code finishes running, it can handle another request\. If the function is invoked again while a request is still being processed, another instance is provisioned, increasing the function's concurrency\.

Concurrency is subject to [quotas](gettingstarted-limits.md) at the AWS Region level\. You can configure individual functions to limit their concurrency, or to enable them to reach a specific level of concurrency\. For more information, see [Managing Lambda reserved concurrency](configuration-concurrency.md)\.

## Qualifier<a name="gettingstarted-concepts-qualifier"></a>

When you invoke or view a function, you can include a *qualifier* to specify a version or alias\. A *version* is an immutable snapshot of a function's code and configuration that has a numerical qualifier\. For example, `my-function:1`\. An *alias* is a pointer to a version that you can update to map to a different version, or split traffic between two versions\. For example, `my-function:BLUE`\. You can use versions and aliases together to provide a stable interface for clients to invoke your function\.

For more information, see [Lambda function versions](configuration-versions.md)\.

## Destination<a name="gettingstarted-concepts-destinations"></a>

A *destination* is an AWS resource where Lambda can send events from an asynchronous invocation\. You can configure a destination for events that fail processing\. Some services also support a destination for events that are successfully processed\.

For more information, see [Configuring destinations for asynchronous invocation](invocation-async.md#invocation-async-destinations)\.