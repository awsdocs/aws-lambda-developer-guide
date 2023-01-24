# Lambda function scaling<a name="invocation-scaling"></a>

The first time you invoke your function, AWS Lambda creates an instance of the function and runs its handler method to process the event\. When the function returns a response, it stays active and waits to process additional events\. If you invoke the function again while the first event is being processed, Lambda initializes another instance, and the function processes the two events concurrently\. As more events come in, Lambda routes them to available instances and creates new instances as needed\. When the number of requests decreases, Lambda stops unused instances to free up scaling capacity for other functions\.

The default regional concurrency quota starts at 1,000 instances\. For more information, or to request an increase on this quota, see [Lambda quotas](gettingstarted-limits.md)\. To allocate capacity on a per\-function basis, you can configure functions with [reserved concurrency](configuration-concurrency.md)\.

Your functions' *concurrency* is the number of instances that serve requests at a given time\. For an initial burst of traffic, your functions' cumulative concurrency in a Region can reach an initial level of between 500 and 3000, which varies per Region\. Note that the burst concurrency quota is not per\-function; it applies to all your functions in the Region\.

**Burst concurrency quotas**
+ **3000** – US West \(Oregon\), US East \(N\. Virginia\), Europe \(Ireland\)
+ **1000** – Asia Pacific \(Tokyo\), Europe \(Frankfurt\), US East \(Ohio\)
+ **500** – Other Regions

After the initial burst, your functions' concurrency can scale by an additional 500 instances each minute\. This continues until there are enough instances to serve all requests, or until a concurrency limit is reached\. When requests come in faster than your function can scale, or when your function is at maximum concurrency, additional requests fail with a throttling error \(429 status code\)\.

The following example shows a function processing a spike in traffic\. As invocations increase exponentially, the function scales up\. It initializes a new instance for any request that can't be routed to an available instance\. When the burst concurrency limit is reached, the function starts to scale linearly\. If this isn't enough concurrency to serve all requests, additional requests are throttled and should be retried\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling.png)

**Legend**
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.instances.png) Function instances
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.open.png) Open requests
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling.throttling.png) Throttling possible

The function continues to scale until the account's concurrency limit for the function's Region is reached\. The function catches up to demand, requests subside, and unused instances of the function are stopped after being idle for some time\. Unused instances are frozen while they're waiting for requests and don't incur any charges\.

When your function scales up, the first request served by each instance is impacted by the time it takes to load and initialize your code\. If your [initialization code](foundation-progmodel.md) takes a long time, the impact on average and percentile latency can be significant\. To enable your function to scale without fluctuations in latency, use [provisioned concurrency](provisioned-concurrency.md)\. The following example shows a function with provisioned concurrency processing a spike in traffic\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.png)

**Legend**
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.instances.png) Function instances
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.open.png) Open requests
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.provisioned.png) Provisioned concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.standard.png) Standard concurrency

When you configure a number for provisioned concurrency, Lambda initializes that number of execution environments\. Your function is ready to serve a burst of incoming requests with very low latency\. Note that configuring [provisioned concurrency](provisioned-concurrency.md) incurs charges to your AWS account\. 

 When all provisioned concurrency is in use, the function scales up normally to handle any additional requests\. 

Application Auto Scaling takes this a step further by providing autoscaling for provisioned concurrency\. With Application Auto Scaling, you can create a target tracking scaling policy that adjusts provisioned concurrency levels automatically, based on the utilization metric that Lambda emits\. [Use the Application Auto Scaling API](configuration-concurrency.md#configuration-concurrency-api) to register an alias as a scalable target and create a scaling policy\.

In the following example, a function scales between a minimum and maximum amount of provisioned concurrency based on utilization\. When the number of open requests increases, Application Auto Scaling increases provisioned concurrency in large steps until it reaches the configured maximum\. The function continues to scale on standard concurrency until utilization starts to drop\. When utilization is consistently low, Application Auto Scaling decreases provisioned concurrency in smaller periodic steps\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned-auto.png)

**Legend**
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.instances.png) Function instances
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.open.png) Open requests
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.provisioned.png) Provisioned concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.standard.png) Standard concurrency

When you invoke your function asynchronously, by using an event source mapping or another AWS service, scaling behavior varies\. For example, event source mappings that read from a stream are limited by the number of shards in the stream\. Scaling capacity that is unused by an event source is available for use by other clients and event sources\. For more information, see the following topics\.
+ [Asynchronous invocation](invocation-async.md)
+ [Lambda event source mappings](invocation-eventsourcemapping.md)
+ [Error handling and automatic retries in AWS Lambda](invocation-retries.md)
+ [Using AWS Lambda with other services](lambda-services.md)

You can monitor concurrency levels in your account by using the following metrics:

**Concurrency metrics**
+ `ConcurrentExecutions`
+ `UnreservedConcurrentExecutions`
+ `ProvisionedConcurrentExecutions`
+ `ProvisionedConcurrencyInvocations`
+ `ProvisionedConcurrencySpilloverInvocations`
+ `ProvisionedConcurrencyUtilization`

For more information, see [Working with Lambda function metrics](monitoring-metrics.md)\.