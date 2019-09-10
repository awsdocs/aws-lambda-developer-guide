# AWS Lambda Function Scaling<a name="scaling"></a>

The first time you invoke your function, AWS Lambda creates an instance of the function and runs its handler method to process the event\. When the function returns a response, it sticks around to process additional events\. If you invoke the function again while the first event is being processed, Lambda creates another instance\.

As more events come in, Lambda routes them to available instances and creates new instances as needed\. Your function's *concurrency* is the number of instances serving requests at a given time\. For an initial burst of traffic, your function's concurrency can reach an initial level of between 500 and 3000, which varies per region\.

**Initial Concurrency Burst Limits**
+ **3000** – US West \(Oregon\), US East \(N\. Virginia\), EU \(Ireland\)\.
+ **1000** – Asia Pacific \(Tokyo\), EU \(Frankfurt\)\.
+ **500** – Other regions\.

After the initial burst, your function's concurrency can scale by an additional 500 instances each minute\. This continues until there are enough instances to serve all requests, or a concurrency limit is reached\. When the number of requests decreases, Lambda stops unused instances to free up scaling capacity for other functions\.

The regional concurrency limit starts at 1,000 and can be increased by submitting a request in the [Support Center console](https://console.aws.amazon.com/support/v1#/case/create?issueType=service-limit-increase)\. To limit scaling and allocate capacity on a per\-function basis, you can configure functions with [reserved concurrency](per-function-concurrency.md)\.

When requests come in faster than your function can scale, or when your function is at maximum concurrency, additional requests fail with a throttling error \(429 status code\)\. When you invoke your function directly, you should treat this as a retryable error\.

When you invoke your function asynchronously, with an event source mapping, or with another AWS service, scaling behavior varies\. For example, event source mappings that read from a stream do not scale beyond the number of shards in the stream\. Scaling capacity unused by an event source is available for use by other clients and event sources\. For more information, see the following topics\.
+ [Asynchronous Invocation](invocation-async.md)
+ [AWS Lambda Event Source Mapping](invocation-eventsourcemapping.md)
+ [Error Handling and Automatic Retries in AWS Lambda](retries-on-errors.md)
+ [Using AWS Lambda with Other Services](lambda-services.md)

You can monitor concurrency levels in your account with the `ConcurrentExecutions` and `UnreservedConcurrentExecutions` metrics\. For more information, see [AWS Lambda Metrics](monitoring-functions-metrics.md)\.