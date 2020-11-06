# AWS Lambda concepts<a name="gettingstarted-concepts"></a>

With Lambda, you run functions to process events\. To send events to your function, you can invoke it using the Lambda API, or you can configure an AWS service or resource to invoke it\.

**Topics**
+ [Function](#gettingstarted-concepts-function)
+ [Qualifier](#gettingstarted-concepts-qualifier)
+ [Execution environment](#gettingstarted-concepts-ee)
+ [Runtime](#gettingstarted-concepts-runtimes)
+ [Extension](#gettingstarted-concepts-extensions)
+ [Event](#gettingstarted-concepts-event)
+ [Concurrency](#gettingstarted-concepts-concurrency)
+ [Trigger](#gettingstarted-concepts-trigger)

## Function<a name="gettingstarted-concepts-function"></a>

A *function* is a resource that you can invoke to run your code in Lambda\. A function has code that processes events, and a [runtime](#gettingstarted-concepts-runtimes) that passes requests and responses between Lambda and the function code\. You provide the code, and you can use the provided runtimes or create your own\.

For more information, see [Managing AWS Lambda functions](lambda-functions.md)\.

## Qualifier<a name="gettingstarted-concepts-qualifier"></a>

When you invoke or view a function, you can include a *qualifier* to specify a version or alias\. A *version* is an immutable snapshot of a function's code and configuration that has a numerical qualifier\. For example, `my-function:1`\. An *alias* is a pointer to a version that can be updated to map to a different version, or split traffic between two versions\. For example, `my-function:BLUE`\. You can use versions and aliases together to provide a stable interface for clients to invoke your function\.

For more information, see [Lambda function versions](configuration-versions.md)\.

## Execution environment<a name="gettingstarted-concepts-ee"></a>

An *execution environment* provides a secure and isolated runtime environment where Lambda invokes your function\. An execution environment manages the runtime and other resources that are required to run your function\. The execution environment provides lifecycle support for the function's runtime and for any [extensions](#gettingstarted-concepts-extensions) associated with your function\.

For more information, see [Execution environment](runtimes-context.md)\.

## Runtime<a name="gettingstarted-concepts-runtimes"></a>

The Lambda *runtime* provides a language\-specific environment that runs in the execution environment\. You configure your function to use a runtime that matches your programming language\. The runtime relays invocation events, context information, and responses between Lambda and the function\. You can use runtimes provided by Lambda, or build your own\.

For more information, see [AWS Lambda runtimes](lambda-runtimes.md)\.

## Extension<a name="gettingstarted-concepts-extensions"></a>

Lambda *extensions* enable you to augment your functions\. For example, you can use extensions to integrate your functions with your preferred monitoring, observability, security, and governance tools\. You can choose from a broad set of tools provided by [AWS Lambda Partners](http://aws.amazon.com/lambda/partners/), or you can [create your own Lambda extensions](runtimes-extensions-api.md)\.

An internal extension runs in the runtime process and shares the same lifecycle as the runtime\. An external extension runs as a separate process in the execution environment\. The external extension is initialized before the function is invoked, runs in parallel with the function's runtime, and continues to run after the function invocation is complete\.

For more information, see [Using AWS Lambda extensions](using-extensions.md)\.

## Event<a name="gettingstarted-concepts-event"></a>

An *event* is a JSON\-formatted document that contains data for a function to process\. The Lambda runtime converts the event to an object and passes it to your function code\. When you invoke a function, you determine the structure and contents of the event\.

**Example Custom event – Weather data**  

```
{
  "TemperatureK": 281,
  "WindKmh": -3,
  "HumidityPct": 0.55,
  "PressureHPa": 1020
}
```

When an AWS service invokes your function, the service defines the shape of the event\.

**Example Service event – Amazon SNS notification**  

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

## Concurrency<a name="gettingstarted-concepts-concurrency"></a>

*Concurrency* is the number of requests that your function is serving at any given time\. When your function is invoked, Lambda provisions an instance of it to process the event\. When the function code finishes running, it can handle another request\. If the function is invoked again while a request is still being processed, another instance is provisioned, increasing the function's concurrency\.

Concurrency is subject to [quotas](gettingstarted-limits.md) at the AWS Region level\. You can configure individual functions to limit their concurrency, or to enable them to reach a specific level of concurrency\. For more information, see [Managing concurrency for a Lambda function](configuration-concurrency.md)\.

## Trigger<a name="gettingstarted-concepts-trigger"></a>

A *trigger* is a resource or configuration that invokes a Lambda function\. This includes AWS services that can be configured to invoke a function, applications that you develop, and [event source mappings](invocation-eventsourcemapping.md)\. An event source mapping is a resource in Lambda that reads items from a stream or queue and invokes a function\.

For more information, see [Invoking AWS Lambda functions](lambda-invocation.md) and [Using AWS Lambda with other services](lambda-services.md)\.