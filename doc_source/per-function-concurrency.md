# Reserving Concurrency for a Lambda Function<a name="per-function-concurrency"></a>

You can configure a function with reserved concurrency to guarantee that it can always reach a certain level of [concurrency](scaling.md)\. Reserving concurrency also limits the maximum concurrency for the function\.

**To reserve concurrency for a function**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Concurrency**, choose **Reserve concurrency**\.

1. Enter the amount of concurrency to reserve for the function\.

1. Choose **Save**\.

You can reserve up to the **Unreserved account concurrency** value shown, less 100 concurrency for functions that don't have reserved concurrency\. Set reserved concurrency to 0 to throttle all invocations\.

Reserving concurrency has the following effects\.
+ **Other functions cannot prevent your function from scaling** – All of your account's functions in the same region without reserved concurrency share the pool of unreserved concurrency\. Without reserved concurrency, other functions can use up all of the available concurrency, preventing your function from scaling up when needed\.
+ **Your function can't scale out of control** – Reserved concurrency also limits your function from using concurrency from the unreserved pool, capping it's maximum concurrency\. Reserve concurrency to prevent your function from using all the available concurrency in the region, or from overloading downstream resources\.
+ **Conserve networking resources** – If your function connects to a VPC, you must make sure your subnets have adequate address capacity to support the ENI scaling requirements of your function\. For more information, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)\.

Concurrency limits can only be set at the function level, not for individual versions\. All invocations to all versions and aliases of a given function will accrue towards the function limit\.

## Configuring Concurrency with the Lambda API<a name="per-function-concurrency-cli"></a>

To configure reserved concurrency on a function, use the `put-function-concurrency` command\.

```
$ aws lambda put-function-concurrency --function-name my-function --reserved-concurrent-executions 100
```

To remove reserved concurrency, use `delete-function-concurrency`\.

```
$ aws lambda delete-function-concurrency --function-name my-function  
```

To view a function's reserved concurrency, use `get-function`\.

```
$ aws lambda get-function --function-name my-function
```

To view your account's concurrency limits in a region, use `get-account-settings`\.

```
$ aws lambda get-account-settings
{
    "AccountLimit": {
        "TotalCodeSize": 80530636800,
        "CodeSizeUnzipped": 262144000,
        "CodeSizeZipped": 52428800,
        "ConcurrentExecutions": 1000,
        "UnreservedConcurrentExecutions": 984
    },
    "AccountUsage": {
        "TotalCodeSize": 174913095,
        "FunctionCount": 52
    }
}
```

The preceding commands correspond to the following API actions\.
+ [PutFunctionConcurrency](API_PutFunctionConcurrency.md)
+ [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md)
+ [GetFunction](API_GetFunction.md)
+ [GetAccountSettings](API_GetAccountSettings.md)

Setting per\-function concurrency can impact the concurrency pool available to other functions\. We recommend restricting the permissions to PutFunctionConcurrency and DeleteFunctionConcurrency to administrative users so that the number of users who can make these changes is limited\.