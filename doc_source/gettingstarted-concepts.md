# AWS Lambda concepts<a name="gettingstarted-concepts"></a>

With AWS Lambda, you run functions to process events\. You can send events to your function by invoking it with the Lambda API, or by configuring an AWS service or resource to invoke it\.

**Topics**
+ [Function](#gettingstarted-concepts-function)
+ [Qualifier](#gettingstarted-concepts-qualifier)
+ [Runtime](#gettingstarted-concepts-runtimes)
+ [Event](#gettingstarted-concepts-event)
+ [Concurrency](#gettingstarted-concepts-concurrency)
+ [Trigger](#gettingstarted-concepts-trigger)

## Function<a name="gettingstarted-concepts-function"></a>

A function is a resource that you can invoke to run your code in AWS Lambda\. A function has code that processes events, and a runtime that passes requests and responses between Lambda and the function code\. You provide the code, and you can use the provided runtimes or create your own\.

For more information, see [Managing AWS Lambda functions](lambda-functions.md)\.

## Qualifier<a name="gettingstarted-concepts-qualifier"></a>

When you invoke or view a function, you can include a qualifier to specify a version or alias\. A version is an immutable snapshot of a function's code and configuration that has a numerical qualifier\. For example, `my-function:1`\. An alias is a pointer to a version that can be updated to map to a different version, or split traffic between two versions\. For example, `my-function:BLUE`\. You can use versions and aliases together to provide a stable interface for clients to invoke your function\.

For more information, see [Lambda function versions](configuration-versions.md)\.

## Runtime<a name="gettingstarted-concepts-runtimes"></a>

Lambda runtimes allow functions in different languages to run in the same base execution environment\. You configure your function to use a runtime that matches your programming language\. The runtime sits in between the Lambda service and your function code, relaying invocation events, context information, and responses between the two\. You can use runtimes provided by Lambda, or build your own\.

For more information, see [AWS Lambda runtimes](lambda-runtimes.md)\.

## Event<a name="gettingstarted-concepts-event"></a>

An event is a JSON formatted document that contains data for a function to process\. The Lambda runtime converts the event to an object and passes it to your function code\. When you invoke a function, you determine the structure and contents of the event\.

**Example custom event – Weather data**  

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

For details on events from AWS services, see [Using AWS Lambda with other services](lambda-services.md)\.

## Concurrency<a name="gettingstarted-concepts-concurrency"></a>

Concurrency is the number of requests that your function is serving at any given time\. When your function is invoked, Lambda provisions an instance of it to process the event\. When the function code finishes running, it can handle another request\. If the function is invoked again while a request is still being processed, another instance is provisioned, increasing the function's concurrency\.

Concurrency is subject to quotas at the region level\. You can also configure individual functions to limit their concurrency, or to ensure that they can reach a specific level of concurrency\. For more information, see [Managing concurrency for a Lambda function](configuration-concurrency.md)\.

## Trigger<a name="gettingstarted-concepts-trigger"></a>

A trigger is a resource or configuration that invokes a Lambda function\. This includes AWS services that can be configured to invoke a function, applications that you develop, and event source mappings\. An event source mapping is a resource in Lambda that reads items from a stream or queue and invokes a function\.

For more information, see [Invoking AWS Lambda functions](lambda-invocation.md) and [Using AWS Lambda with other services](lambda-services.md)\.