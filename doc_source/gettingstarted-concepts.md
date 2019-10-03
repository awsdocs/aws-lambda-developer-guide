# AWS Lambda Concepts<a name="gettingstarted-concepts"></a>

With AWS Lambda, you run functions to process events\. You can send events to your function by invoking it with the Lambda API, or by configuring an AWS service or resource to invoke it\.

**Topics**
+ [Function](#gettingstarted-concepts-function)
+ [Runtime](#gettingstarted-concepts-runtimes)
+ [Event](#gettingstarted-concepts-event)
+ [Concurrency](#gettingstarted-concepts-concurrency)
+ [Trigger](#gettingstarted-concepts-trigger)

## Function<a name="gettingstarted-concepts-function"></a>

A function is a resource that you can invoke to run your code in AWS Lambda\. A function has code that processes events, and a runtime that passes requests and responses between Lambda and the function code\. You provide the code, and you can use the provided runtimes or create your own\.

For more information, see [AWS Lambda Runtimes](lambda-runtimes.md)\.

## Runtime<a name="gettingstarted-concepts-runtimes"></a>

Lambda runtimes allow functions in different languages to run in the same base execution environment\. You configure your function to use a runtime that matches your programming language\. The runtime sits in between the Lambda service and your function code, relaying invocation events, context information, and responses between the two\. You can use runtimes provided by Lambda, or build your own\.

For more information, see [AWS Lambda Runtimes](lambda-runtimes.md)\.

## Event<a name="gettingstarted-concepts-event"></a>

An event is a JSON formatted document that contains data for a function to process\. The Lambda runtime converts the function to an object and passes it to your function code\. When you invoke a function, you determine the structure and contents of the event\. When an AWS service invokes your function, the service defines the event\.

For details on events from AWS services, see [Using AWS Lambda with Other Services](lambda-services.md)\.

## Concurrency<a name="gettingstarted-concepts-concurrency"></a>

Concurrency is the number of requests that your function is serving at any given time\. When your function is invoked, Lambda provisions an instance of it to process the event\. When the function code finishes running, it can handle another request\. If the function is invoked again while a request is still being processed, another instance is provisioned, increasing the function's concurrency\.

Concurrency is subject to limits at the region level\. You can also configure individual functions to limit their concurrency, or to ensure that they can reach a specific level of concurrency\. For more information, see [Reserving Concurrency for a Lambda Function](per-function-concurrency.md)\.

## Trigger<a name="gettingstarted-concepts-trigger"></a>

A trigger is a resource or configuration that invokes a Lambda function\. This includes AWS services that can be configured to invoke a function, applications that you develop, and event source mappings\. An event source mapping is a resource in Lambda that reads items from a stream or queue and invokes a function\.

For more information, see [Invoking AWS Lambda Functions](lambda-invocation.md) and [Using AWS Lambda with Other Services](lambda-services.md)\.