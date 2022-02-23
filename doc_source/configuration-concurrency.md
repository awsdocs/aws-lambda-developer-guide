# Managing Lambda reserved concurrency<a name="configuration-concurrency"></a>

There are two types of concurrency controls available:
+ Reserved concurrency – Reserved concurrency guarantees the maximum number of concurrent instances for the function\. When a function has reserved concurrency, no other function can use that concurrency\. There is no charge for configuring reserved concurrency for a function\.
+ Provisioned concurrency – Provisioned concurrency initializes a requested number of execution environments so that they are prepared to respond immediately to your function's invocations\. Note that configuring provisioned concurrency incurs charges to your AWS account\.

This topic details how to manage and configure reserved concurrency\. If you want to decrease latency for your functions, use [provisioned concurrency](provisioned-concurrency.md)\.

Concurrency is the number of requests that your function is serving at any given time\. When your function is invoked, Lambda allocates an instance of it to process the event\. When the function code finishes running, it can handle another request\. If the function is invoked again while a request is still being processed, another instance is allocated, which increases the function's concurrency\. The total concurrency for all of the functions in your account is subject to a per\-region quota\.

To learn about how concurrency interacts with scaling, [see Lambda function scaling](https://docs.aws.amazon.com/lambda/latest/dg/invocation-scaling.html)\.

**Topics**
+ [Configuring reserved concurrency](#configuration-concurrency-reserved)
+ [Configuring concurrency with the Lambda API](#configuration-concurrency-api)

## Configuring reserved concurrency<a name="configuration-concurrency-reserved"></a>

To manage reserved concurrency settings for a function, use the Lambda console\.

**To reserve concurrency for a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Concurrency**\. 

1. Under **Concurrency**, choose **Edit**\. 

1. Choose **Reserve concurrency**\. Enter the amount of concurrency to reserve for the function\.

1. Choose **Save**\.

You can reserve up to the **Unreserved account concurrency** value that is shown, minus 100 for functions that don't have reserved concurrency\. To throttle a function, set the reserved concurrency to zero\. This stops any events from being processed until you remove the limit\.

The following example shows two functions with pools of reserved concurrency, and the unreserved concurrency pool used by other functions\. Throttling errors occur when all of the concurrency in a pool is in use\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-concurrency-reserved.png)

**Legend**
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-concurrency.concurrency.png) Function concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-concurrency.reserved.png) Reserved concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-concurrency.unreserved.png) Unreserved concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-concurrency.throttling.png) Throttling

Reserving concurrency has the following effects\.
+ **Other functions can't prevent your function from scaling** – All of your account's functions in the same Region without reserved concurrency share the pool of unreserved concurrency\. Without reserved concurrency, other functions can use up all of the available concurrency\. This prevents your function from scaling up when needed\.
+ **Your function can't scale out of control** – Reserved concurrency also limits your function from using concurrency from the unreserved pool, which caps its maximum concurrency\. You can reserve concurrency to prevent your function from using all the available concurrency in the Region, or from overloading downstream resources\.

Setting per\-function concurrency can impact the concurrency pool that is available to other functions\. To avoid issues, limit the number of users who can use the `PutFunctionConcurrency` and `DeleteFunctionConcurrency` API operations\.

## Configuring concurrency with the Lambda API<a name="configuration-concurrency-api"></a>

To manage concurrency settings the AWS CLI or AWS SDK, use the following API operations\.
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md)
+ [GetFunctionConcurrency](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionConcurrency.html)
+ [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)
+ [GetAccountSettings](API_GetAccountSettings.md)

To configure reserved concurrency with the AWS CLI, use the `put-function-concurrency` command\. The following command reserves a concurrency of 100 for a function named `my-function`:

```
aws lambda put-function-concurrency --function-name my-function --reserved-concurrent-executions 100
```

You should see the following output:

```
{
    "ReservedConcurrentExecutions": 100
}
```