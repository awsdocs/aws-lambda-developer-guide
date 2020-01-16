# Managing Concurrency for a Lambda Function<a name="configuration-concurrency"></a>

Concurrency is the number of requests that your function is serving at any given time\. When your function is invoked, Lambda allocates an instance of it to process the event\. When the function code finishes running, it can handle another request\. If the function is invoked again while a request is still being processed, another instance is allocated, which increases the function's concurrency\.

Concurrency is subject to a Regional [limit](limits.md) that is shared by all functions in a Region\. To ensure that a function can always reach a certain level of concurrency, you can configure the function with [reserved concurrency](#configuration-concurrency-reserved)\. When a function has reserved concurrency, no other function can use that concurrency\. Reserved concurrency also limits the maximum concurrency for the function, and applies to the function as a whole, including versions and aliases\.

When Lambda allocates an instance of your function, the [runtime](lambda-runtimes.md) loads your function's code and runs initialization code that you define outside of the handler\. If your code and dependencies are large, or you create SDK clients during initialization, this process can take some time\. As your function [scales up](scaling.md), this causes the portion of requests that are served by new instances to have higher latency than the rest\.

To enable your function to scale without fluctuations in latency, use [provisioned concurrency](#configuration-concurrency-provisioned)\. By allocating provisioned concurrency before an increase in invocations, you can ensure that all requests are served by initialized instances with very low latency\. You can configure provisioned concurrency on a version of a function, or on an alias\.

Lambda also integrates with [Application Auto Scaling](https://docs.aws.amazon.com/autoscaling/application/userguide/)\. You can configure Application Auto Scaling to manage provisioned concurrency on a schedule or based on utilization\. Use scheduled scaling to increase provisioned concurrency in anticipation of peak traffic\. To increase provisioned concurrency automatically as needed, use [target tracking scaling](https://docs.aws.amazon.com/autoscaling/application/userguide/application-auto-scaling-target-tracking.html)\.

Provisioned concurrency counts towards a function's reserved concurrency and Regional limits\. If the amount of provisioned concurrency on a function's versions and aliases adds up to the function's reserved concurrency, all invocations run on provisioned concurrency\. This configuration also has the effect of throttling the unpublished version of the function \(`$LATEST`\), which prevents it from executing\.

**Topics**
+ [Configuring Reserved Concurrency](#configuration-concurrency-reserved)
+ [Configuring Provisioned Concurrency](#configuration-concurrency-provisioned)
+ [Configuring Concurrency with the Lambda API](#configuration-concurrency-api)

## Configuring Reserved Concurrency<a name="configuration-concurrency-reserved"></a>

To manage reserved concurrency settings for a function, use the Lambda console\.

**To reserve concurrency for a function**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Concurrency**, choose **Reserve concurrency**\.

1. Enter the amount of concurrency to reserve for the function\.

1. Choose **Save**\.

You can reserve up to the **Unreserved account concurrency** value that is shown, minus 100 for functions that don't have reserved concurrency\. To throttle a function, set the reserved concurrency to zero\. This stops any events from being processed until you remove the limit\.

Reserving concurrency has the following effects\.
+ **Other functions can't prevent your function from scaling** – All of your account's functions in the same Region without reserved concurrency share the pool of unreserved concurrency\. Without reserved concurrency, other functions can use up all of the available concurrency\. This prevents your function from scaling up when needed\.
+ **Your function can't scale out of control** – Reserved concurrency also limits your function from using concurrency from the unreserved pool, which caps its maximum concurrency\. You can reserve concurrency to prevent your function from using all the available concurrency in the Region, or from overloading downstream resources\.

Setting per\-function concurrency can impact the concurrency pool that is available to other functions\. To avoid issues, limit the number of users who can use the `PutFunctionConcurrency` and `DeleteFunctionConcurrency` API operations\.

## Configuring Provisioned Concurrency<a name="configuration-concurrency-provisioned"></a>

To manage provisioned concurrency settings for a version or alias, use the Lambda console\.

**Note:** Provisioned Concurrency is not supported with [Lambda@Edge](https://docs.aws.amazon.com/lambda/latest/dg/lambda-edge.html)

**To reserve concurrency for an alias**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Provisioned concurrency configurations**, choose **Add**\.

1. Choose an alias or version\.

1. Enter the amount of provisioned concurrency to allocate\.

1. Choose **Save**\.

You can manage provisioned concurrency for all aliases and versions from the function configuration page\. The list of provisioned concurrency configurations shows the allocation progress of each configuration\. Provisioned concurrency settings are also available on the configuration page for each version and alias\.

After a couple of minutes, Lambda allocates provisioned concurrency at the same rate that a function [scales under load](scaling.md)\. Provisioned concurrency runs continually and is billed in addition to standard invocation costs\. For details, see [AWS Lambda pricing](https://aws.amazon.com/lambda/pricing/)\.

Each version of a function can only have one provisioned concurrency configuration\. This can be directly on the version itself, or on an alias that points to the version\. Two aliases can't allocate provisioned concurrency for the same version\. Also, you can't allocate provisioned concurrency on an alias that points to the unpublished version \(`$LATEST`\)\.

When you change the version that an alias points to, provisioned concurrency is deallocated from the old version and then allocated to the new version\. You can add a routing configuration to an alias that has provisioned concurrency\. However, you can't manage provisioned concurrency settings on the alias while the routing configuration is in place\.

Lambda emits the following metrics for provisioned concurrency:

**Provisioned Concurrency Metrics**
+ `ProvisionedConcurrentExecutions`
+ `ProvisionedConcurrencyInvocations`
+ `ProvisionedConcurrencySpilloverInvocations`
+ `ProvisionedConcurrencyUtilization`

For details, see [AWS Lambda Metrics](monitoring-functions-metrics.md)\.

## Configuring Concurrency with the Lambda API<a name="configuration-concurrency-api"></a>

To manage concurrency settings with the AWS CLI or AWS SDK, use the following API operations\.
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md)
+ [GetFunctionConcurrency](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionConcurrency.html)
+ [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)
+ [PutProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutProvisionedConcurrencyConfig.html)
+ [GetProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetProvisionedConcurrencyConfig.html)
+ [ListProvisionedConcurrencyConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListProvisionedConcurrencyConfigs.html)
+ [DeleteProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteProvisionedConcurrencyConfig.html)
+ [GetAccountSettings](API_GetAccountSettings.md)

To configure reserved concurrency with the AWS CLI, use the `put-function-concurrency` command\. The following command reserves a concurrency of 100 for a function named `my-function`:

```
$ aws lambda put-function-concurrency --function-name my-function --reserved-concurrent-executions 100
{
    "ReservedConcurrentExecutions": 100
}
```

To allocate provisioned concurrency for a function, use `put-provisioned-concurrency-config`\. The following command allocates a concurrency of 100 for the `BLUE` alias of a function named `my-function`:

```
$ aws lambda put-provisioned-concurrency-config --function-name my-function \
--qualifier BLUE --provisioned-concurrent-executions 100
{
    "Requested ProvisionedConcurrentExecutions": 100,
    "Allocated ProvisionedConcurrentExecutions": 0,
    "Status": "IN_PROGRESS",
    "LastModified": "2019-11-21T19:32:12+0000"
}
```

To configure Application Auto Scaling to manage provisioned concurrency, first register an alias as a target\.

```
$ aws application-autoscaling register-scalable-target --service-namespace lambda \
      --resource-id function:my-function:BLUE --min-capacity 1 --max-capacity 100 \
      --scalable-dimension lambda:function:ProvisionedConcurrency
```

Next, apply a scaling policy to the target\. The following example configures Application Auto Scaling to adjust the provisioned concurrency configuration for an alias to keep utilization near 70 percent\.

```
$ aws application-autoscaling put-scaling-policy --service-namespace lambda 
--scalable-dimension lambda:function:ProvisionedConcurrency --resource-id function:my-function:BLUE \
--policy-name my-policy --policy-type TargetTrackingScaling \
--target-tracking-scaling-policy-configuration '{ "TargetValue": 0.7, "PredefinedMetricSpecification": { "PredefinedMetricType": "LambdaProvisionedConcurrencyUtilization" }}'
{
    "PolicyARN": "arn:aws:autoscaling:us-east-2:123456789012:scalingPolicy:12266dbb-1524-xmpl-a64e-9a0a34b996fa:resource/lambda/function:my-function:BLUE:policyName/my-policy",
    "Alarms": [
        {
            "AlarmName": "TargetTracking-function:my-function:BLUE-AlarmHigh-aed0e274-xmpl-40fe-8cba-2e78f000c0a7",
            "AlarmARN": "arn:aws:cloudwatch:us-east-2:123456789012:alarm:TargetTracking-function:my-function:BLUE-AlarmHigh-aed0e274-xmpl-40fe-8cba-2e78f000c0a7"
        },
        {
            "AlarmName": "TargetTracking-function:my-function:BLUE-AlarmLow-7e1a928e-xmpl-4d2b-8c01-782321bc6f66",
            "AlarmARN": "arn:aws:cloudwatch:us-east-2:123456789012:alarm:TargetTracking-function:my-function:BLUE-AlarmLow-7e1a928e-xmpl-4d2b-8c01-782321bc6f66"
        }
    ]
}
```

To view your account's concurrency limits in a Region, use `get-account-settings`\.

```
$ aws lambda get-account-settings
{
    "AccountLimit": {
        "TotalCodeSize": 80530636800,
        "CodeSizeUnzipped": 262144000,
        "CodeSizeZipped": 52428800,
        "ConcurrentExecutions": 1000,
        "UnreservedConcurrentExecutions": 900
    },
    "AccountUsage": {
        "TotalCodeSize": 174913095,
        "FunctionCount": 52
    }
}
```
