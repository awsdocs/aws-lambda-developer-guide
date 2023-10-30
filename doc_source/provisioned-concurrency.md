# Managing Lambda provisioned concurrency<a name="provisioned-concurrency"></a>

There are two types of concurrency controls available:
+ Reserved concurrency – Reserved concurrency guarantees the maximum number of concurrent instances for the function\. When a function has reserved concurrency, no other function can use that concurrency\. There is no charge for configuring reserved concurrency for a function\.
+ Provisioned concurrency – Provisioned concurrency initializes a requested number of execution environments so that they are prepared to respond immediately to your function's invocations\. Note that configuring provisioned concurrency incurs charges to your AWS account\.

This topic details how to manage and configure provisioned concurrency\. If you want to configure reserved concurrency, see [Managing Lambda reserved concurrency](configuration-concurrency.md)\.

When Lambda allocates an instance of your function, the runtime loads your function's code and runs initialization code that you define outside of the handler\. If your code and dependencies are large, or you create SDK clients during initialization, this process can take some time\. When your function has not been used for some time, needs to scale up, or when you update a function, Lambda creates new execution environments\. This causes the portion of requests that are served by new instances to have higher latency than the rest, otherwise known as a cold start\.

By allocating provisioned concurrency before an increase in invocations, you can ensure that all requests are served by initialized instances with low latency\. Lambda functions configured with provisioned concurrency run with consistent start\-up latency, making them ideal for building interactive mobile or web backends, latency sensitive microservices, and synchronously invoked APIs\.

**Note**  
Provisioned concurrency counts towards a function's reserved concurrency and [Regional quotas](gettingstarted-limits.md)\. If the amount of provisioned concurrency on a function's versions and aliases adds up to the function's reserved concurrency, all invocations run on provisioned concurrency\. This configuration also has the effect of throttling the unpublished version of the function \($LATEST\), which prevents it from executing\. You can't allocate more provisioned concurrency than reserved concurrency for a function\.

Lambda also integrates with Application Auto Scaling, allowing you to manage provisioned concurrency on a schedule or based on utilization\.

**Topics**
+ [Configuring provisioned concurrency](#configuring-provisioned-concurrency)
+ [Optimizing latency with provisioned concurrency](#optimizing-latency)
+ [Managing provisioned concurrency with Application Auto Scaling](#managing-provisioned-concurency)

## Configuring provisioned concurrency<a name="configuring-provisioned-concurrency"></a>

To manage provisioned concurrency settings for a version or alias, use the Lambda console\. You can configure provisioned concurrency on a version of a function, or on an alias\.

 Each version of a function can only have one provisioned concurrency configuration\. This can be directly on the version itself, or on an alias that points to the version\. Two aliases can't allocate provisioned concurrency for the same version\. 

 If you change the version that an alias points to, Lambda deallocates the provisioned concurrency from the old version and allocates it to the new version\. You can add a routing configuration to an alias that has provisioned concurrency\. For more information, see [Lambda function aliases](configuration-aliases.md)\. Note that you can't manage provisioned concurrency settings on the alias while the routing configuration is in place\. 

**Note**  
 Provisioned Concurrency is not supported on the unpublished version of the function \($LATEST\)\. Ensure your client application is not pointing to $LATEST before configuring provisioned concurrency\. 

**To allocate provisioned concurrency for an alias or version**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Concurrency**\.

1. Under **Provisioned concurrency configurations**, choose **Add configuration**\.

1. Choose an alias or version\.

1. Enter the amount of provisioned concurrency to allocate\.

1. Choose **Save**\.

You can also configure provisioned concurrency using the Lambda API with the following operations:
+ [PutProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_PutProvisionedConcurrencyConfig.html)
+ [GetProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetProvisionedConcurrencyConfig.html)
+ [ListProvisionedConcurrencyConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListProvisionedConcurrencyConfigs.html)
+ [DeleteProvisionedConcurrencyConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteProvisionedConcurrencyConfig.html)

To allocate provisioned concurrency for a function, use `put-provisioned-concurrency-config`\. The following command allocates a concurrency of 100 for the `BLUE` alias of a function named `my-function`:

```
aws lambda put-provisioned-concurrency-config --function-name my-function \
--qualifier BLUE --provisioned-concurrent-executions 100
```

You should see the following output:

```
{
    "Requested ProvisionedConcurrentExecutions": 100,
    "Allocated ProvisionedConcurrentExecutions": 0,
    "Status": "IN_PROGRESS",
    "LastModified": "2019-11-21T19:32:12+0000"
}
```

To view your account's concurrency quotas in a Region, use `get-account-settings`\.

```
aws lambda get-account-settings
```

You should see the following output:

```
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

You can manage provisioned concurrency for all aliases and versions from the function configuration page\. The list of provisioned concurrency configurations shows the allocation progress of each configuration\. Provisioned concurrency settings are also available on the configuration page for each version and alias\.

Lambda emits the following metrics for provisioned concurrency:

**Provisioned concurrency metrics**
+ `ProvisionedConcurrentExecutions` – The number of function instances that are processing events on provisioned concurrency\. For each invocation of an alias or version with provisioned concurrency, Lambda emits the current count\.
+ `ProvisionedConcurrencyInvocations` – The number of times your function code is executed on provisioned concurrency\.
+ `ProvisionedConcurrencySpilloverInvocations` – The number of times your function code is executed on standard concurrency when all provisioned concurrency is in use\.
+ `ProvisionedConcurrencyUtilization` – For a version or alias, the value of `ProvisionedConcurrentExecutions` divided by the total amount of provisioned concurrency allocated\. For example, \.5 indicates that 50 percent of allocated provisioned concurrency is in use\.

For more details, see [Working with Lambda function metrics](monitoring-metrics.md)\.

## Optimizing latency with provisioned concurrency<a name="optimizing-latency"></a>

Provisioned concurrency does not come online immediately after you configure it\. Lambda starts allocating provisioned concurrency after a minute or two of preparation\. Similar to how functions [scale under load](invocation-scaling.md), up to 3000 instances of the function can be initialized at once, depending on the Region\. After the initial burst, instances are allocated at a steady rate of 500 per minute until the request is fulfilled\. When you request provisioned concurrency for multiple functions or versions of a function in the same Region, scaling quotas apply across all requests\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.png)

**Legend**
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.instances.png) Function instances
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.open.png) Open requests
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.provisioned.png) Provisioned concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.standard.png) Standard concurrency

To optimize latency, you can customize the initialization behavior for functions that use provisioned concurrency\. You can run initialization code for provisioned concurrency instances without impacting latency, because the initialization code runs at allocation time\. However, the initialization code for an on\-demand instance directly impacts the latency of the first invocation\. For an on\-demand instance, you may choose to defer initialization for a specific capability until the function needs that capability\.

To determine the type of initialization, check the value of AWS\_LAMBDA\_INITIALIZATION\_TYPE\. Lambda sets this environment variable to `provisioned-concurrency` or `on-demand`\. The value of AWS\_LAMBDA\_INITIALIZATION\_TYPE is immutable and does not change over the lifetime of the execution environment\.

If you use the \.NET 3\.1 runtime, you can configure the AWS\_LAMBDA\_DOTNET\_PREJIT environment variable to improve the latency for functions that use provisioned concurrency\. The \.NET runtime lazily compiles and initializes each library that your code calls for the first time\. As a result, the first invocation of a Lambda function can take longer than subsequent invocations\. When you set AWS\_LAMBDA\_DOTNET\_PREJIT to `ProvisionedConcurrency`, Lambda performs ahead\-of\-time JIT compilation for common system dependencies\. Lambda performs this initialization optimization for provisioned concurrency instances only, which results in faster performance for the first invocation\. If you set the environment variable to `Always`, Lambda performs ahead\-of\-time JIT compilation for every initialization\. If you set the environment variable to `Never`, ahead\-of\-time JIT compilation is disabled\. The default value for AWS\_LAMBDA\_DOTNET\_PREJIT is `ProvisionedConcurrency`\. 

For provisioned concurrency instances, your function's [initialization code](foundation-progmodel.md) runs during allocation and every few hours, as running instances of your function are recycled\. You can see the initialization time in logs and [traces](services-xray.md) after an instance processes a request\. However, initialization is billed even if the instance never processes a request\. Provisioned concurrency runs continually and is billed separately from initialization and invocation costs\. For details, see [AWS Lambda pricing](https://aws.amazon.com/lambda/pricing/)\.

For more information on optimizing functions using provisioned concurrency, see the [Lambda Operator Guide\.](https://docs.aws.amazon.com/lambda/latest/operatorguide/execution-environments.html)

## Managing provisioned concurrency with Application Auto Scaling<a name="managing-provisioned-concurency"></a>

Application Auto Scaling allows you to manage provisioned concurrency on a schedule or based on utilization\. Use a target tracking scaling policy if want your function to maintain a specified utilization percentage, and scheduled scaling to increase provisioned concurrency in anticipation of peak traffic\.

### Target tracking<a name="managing-provisioned-concurrency-targeting"></a>

 With target tracking, Application Auto Scaling creates and manages the CloudWatch alarms that trigger a scaling policy and calculates the scaling adjustment based on a metric and target value that you define\. This is ideal for applications that don’t have a scheduled time of increased traffic, but have certain traffic patterns\. 

To increase provisioned concurrency automatically as needed, use the `RegisterScalableTarget` and `PutScalingPolicy` Application Auto Scaling API operations to register a target and create a scaling policy:

1. Register a function's alias as a scaling target\. The following example registers the BLUE alias of a function named `my-function`:

   ```
   aws application-autoscaling register-scalable-target --service-namespace lambda \
                           --resource-id function:my-function:BLUE --min-capacity 1 --max-capacity 100 \
                           --scalable-dimension lambda:function:ProvisionedConcurrency
   ```

1. Apply a scaling policy to the target\. The following example configures Application Auto Scaling to adjust the provisioned concurrency configuration for an alias to keep utilization near 70 percent\.

   ```
   aws application-autoscaling put-scaling-policy --service-namespace lambda \
                           --scalable-dimension lambda:function:ProvisionedConcurrency --resource-id function:my-function:BLUE \
                           --policy-name my-policy --policy-type TargetTrackingScaling \
                           --target-tracking-scaling-policy-configuration '{ "TargetValue": 0.7, "PredefinedMetricSpecification": { "PredefinedMetricType": "LambdaProvisionedConcurrencyUtilization" }}'
   ```

   You should see the following output:

   ```
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

Application Auto Scaling creates two alarms in CloudWatch\. The first alarm triggers when the utilization of provisioned concurrency consistently exceeds 70 percent\. When this happens, Application Auto Scaling allocates more provisioned concurrency to reduce utilization\. The second alarm triggers when utilization is consistently less than 63 percent \(90 percent of the 70 percent target\)\. When this happens, Application Auto Scaling reduces the alias's provisioned concurrency\.

In the following example, a function scales between a minimum and maximum amount of provisioned concurrency based on utilization\. When the number of open requests increases, Application Auto Scaling increases provisioned concurrency in large steps until it reaches the configured maximum\. The function continues to scale on standard concurrency until utilization starts to drop\. When utilization is consistently low, Application Auto Scaling decreases provisioned concurrency in smaller periodic steps\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned-auto.png)

**Legend**
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.instances.png) Function instances
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.open.png) Open requests
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.provisioned.png) Provisioned concurrency
+ ![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned.standard.png) Standard concurrency

 Both of these alarms use the *average* statistic by default\. Functions that have traffic patterns of quick bursts may not trigger your provisioned concurrency to scale up\. For example, if your Lambda function executes quickly \(20–100 ms\) and your traffic pattern comes in quick bursts, this may cause incoming requests to exceed your allocated provisioned concurrency during the burst, but if the burst doesn’t last 3 minutes, auto scaling will not trigger\. Additionally, if CloudWatch doesn’t get three data points that hit the target average, the auto scaling policy will not trigger\. 

For more information on target tracking scaling policies, see [Target tracking scaling policies for Application Auto Scaling](https://docs.aws.amazon.com/autoscaling/application/userguide/application-auto-scaling-target-tracking.html)\.

### Scheduled scaling<a name="managing-provisioned-concurrency-scheduling"></a>

 Scaling based on a schedule allows you to set your own scaling schedule according to predictable load changes\. For more information and examples, see [Scheduling AWS Lambda Provisioned Concurrency for recurring peak usage](https://aws.amazon.com/blogs/compute/scheduling-aws-lambda-provisioned-concurrency-for-recurring-peak-usage/)\. 