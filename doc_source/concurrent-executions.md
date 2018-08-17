# Managing Concurrency<a name="concurrent-executions"></a>

 The unit of scale for AWS Lambda is a concurrent execution \(see [Understanding Scaling Behavior](scaling.md) for more details\)\. However, scaling indefinitely is not desirable in all scenarios\. For example, you may want to control your concurrency for cost reasons, or to regulate how long it takes you to process a batch of events, or to simply match it with a downstream resource\. To assist with this, Lambda provides a concurrent execution limit control at both the account level and the function level\.

## Account Level Concurrent Execution Limit<a name="concurrent-execution-safety-limit"></a>

By default, AWS Lambda limits the total concurrent executions across all functions within a given region to 1000\. You can view the account level setting by using the [GetAccountSettings](API_GetAccountSettings.md) API and viewing the `AccountLimit` object\. This limit can be raised as described below:<a name="increase-concurrent-executions-limit"></a>

**To request a limit increase for concurrent executions**

1. Open the [AWS Support Center](https://console.aws.amazon.com/support/home#/) page, sign in if necessary, and then choose **Create case**\.

1. For **Regarding**, select **Service Limit Increase**\.

1. For **Limit Type**, choose **Lambda**, fill in the necessary fields in the form, and then choose the button at the bottom of the page for your preferred method of contact\.

## Function Level Concurrent Execution Limit<a name="per-function-concurrency"></a>

By default, the concurrent execution limit is enforced against the sum of the concurrent executions of all functions\. The shared concurrent execution pool is referred to as the unreserved concurrency allocation\. If you haven’t set up any function\-level concurrency limit, then the unreserved concurrency limit is the same as the account level concurrency limit\. Any increases to the account level limit have a corresponding increase in the unreserved concurrency limit\. You can view the unreserved concurrency allocation for a function by using the [GetAccountSettings](API_GetAccountSettings.md) API or using the AWS Lambda console\. Functions that are being accounted against the shared concurrent execution pool will not show any concurrency value when queried using the [GetFunctionConfiguration](API_GetFunctionConfiguration.md) API\.

You can optionally set the concurrent execution limit for a function\. You may choose to do this for a few reasons:
+ The default behavior means a surge of concurrent executions in one function prevents the function you have isolated with an execution limit from getting throttled\. By setting a concurrent execution limit on a function, you are reserving the specified concurrent execution value for that function\.
+ Functions scale automatically based on incoming request rate, but not all resources in your architecture may be able to do so\. For example, relational databases have limits on how many concurrent connections they can handle\. You can set the concurrent execution limit for a function to align with the values of its downstream resources support\.
+ If your function connects to VPC based resources, you must make sure your subnets have adequate address capacity to support the ENI scaling requirements of your function\. You can estimate the approximate ENI capacity with the following formula:

  Where:
  + **Concurrent execution** – This is the projected concurrency of your workload\. Use the information in [Understanding Scaling Behavior](scaling.md) to determine this value\.
  + **Memory in GB** – The amount of memory you configured for your Lambda function\.

You can set the concurrent execution limit for a function to match the subnet size limits you have\.

**Note**  
If you need a function to stop processing any invocations, you can choose to set the concurrency to 0 and throttle all incoming executions\.

By setting a concurrency limit on a function, Lambda guarantees that allocation will be applied specifically to that function, regardless of the amount of traffic processing remaining functions\. If that limit is exceeded, the function will be throttled\. How that function behaves when throttled will depend on the event source\. For more information, see [Throttling Behavior](#throttling-behavior)\.

**Note**  
Concurrency limits can only be set at the function level, not for individual versions\. All invocations to all versions and aliases of a given function will accrue towards the function limit\.

### Reserved vs\. Unreserved Concurrency Limits<a name="reserved-vs-unreserved-concurrency-limits"></a>

If you set the concurrent execution limit for a function, the value is deducted from the unreserved concurrency pool\. For example, if your account's concurrent execution limit is 1000 and you have 10 functions, you can specify a limit on one function at 200 and another function at 100\. The remaining 700 will be shared among the other 8 functions\. 

**Note**  
AWS Lambda will keep the unreserved concurrency pool at a minimum of 100 concurrent executions, so that functions that do not have specific limits set can still process requests\. So, in practice, if your total account limit is 1000, you are limited to allocating 900 to individual functions\. 

### Setting Concurrency Limits Per Function \(Console\)<a name="per-function-concurrency-console"></a>

To set a concurrency limit for your Lambda function using the Lambda console, do the following:

1. Sign in to the AWS Management Console and open the AWS Lambda console\.

1. Whether you are creating a new Lambda function or updating an existing function, the process of setting a concurrency limit is the same\. If you are new to Lambda and are unfamiliar with creating a function, see [Create a Simple Lambda Function](get-started-create-function.md)\.

1. Under the **Configuration** tab, choose **Concurrency**\. In **Reserve concurrency**, set the value to the maximum of concurrent executions you want reserved for the function\. Note that when you set this value, the **Unreserved account concurrency** value will automatically be updated to display the remaining number of concurrent executions available for all other functions in the account\. Also note that if you want to block invocation of this function, set the value to 0\. To remove a dedicated allotment value for this account, choose **Use unreserved account concurrency**\.

### Setting Concurrency Limits Per Function \(CLI\)<a name="per-function-concurrency-cli"></a>

To set a concurrency limit for your Lambda function using the AWS CLI, do the following:
+ Use the [PutFunctionConcurrency](API_PutFunctionConcurrency.md) operation and pass in the function name and concurrency limit you want allocated to this function:

  ```
  aws lambda put-function-concurrency --function-name function-name
                      --reserved-concurrent-executions limit value
  ```

To remove a concurrency limit for your Lambda function using the AWS CLI, do the following:
+ Use the [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md) operation and pass in the function name:

  ```
  aws lambda delete-function-concurrency --function-name function-name  
  ```

To view a concurrency limit for your Lambda function using the AWS CLI, do the following:
+ Use the [GetFunction](API_GetFunction.md) operation and pass in the function name:

  ```
  aws lambda get-function --function-name function-name  
  ```

**Note**  
Setting the per function concurrency can impact the concurrency pool available to other functions\. We recommend restricting the permissions to the [PutFunctionConcurrency](API_PutFunctionConcurrency.md) API and [DeleteFunctionConcurrency](API_DeleteFunctionConcurrency.md) API to administrative users so that the number of users who can make these changes is limited\.

## Throttling Behavior<a name="throttling-behavior"></a>

On reaching the concurrency limit associated with a function, any further invocation requests to that function are throttled, i\.e\. the invocation doesn't execute your function\. Each throttled invocation increases the Amazon CloudWatch `Throttles` metric for the function\. AWS Lambda handles throttled invocation requests differently, depending on their source: 
+ **Event sources that aren't stream\-based: **Some of these event sources invoke a Lambda function synchronously, and others invoke it asynchronously\. Handling is different for each: 
  + **Synchronous invocation:** If the function is invoked synchronously and is throttled, Lambda returns a 429 error and the invoking service is responsible for retries\. The `ThrottledReason` error code explains whether you ran into a function level throttle \(if specified\) or an account level throttle \(see note below\)\. Each service may have its own retry policy\. For a list of event sources and their invocation type, see [Supported Event Sources](invoking-lambda-function.md)\. 
**Note**  
If you invoke the function directly through the AWS SDKs using the `RequestResponse` invocation mode, your client receives the 429 error and you can retry the invocation\. 
  + **Asynchronous invocation:** If your Lambda function is invoked asynchronously and is throttled, AWS Lambda automatically retries the throttled event for up to six hours, with delays between retries\. For example, CloudWatch Logs retries the failed batch up to five times with delays between retries\. Remember, asynchronous events are queued before they are used to invoke the Lambda function\. You can configure a Dead Letter Queue \(DLQ\) to investigate why your function was throttled\. For more information, see [Dead Letter Queues](dlq.md)\.
+ **Poll\-based event sources that are also stream\-based:** such as [Amazon Kinesis](http://docs.aws.amazon.com/kinesis/latest/dev/), [Amazon DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/), AWS Lambda polls your stream and invokes your Lambda function\. When your Lambda function is throttled, Lambda attempts to process the throttled batch of records until the time the data expires\. This time period can be up to seven days for Amazon Kinesis\. The throttled request is treated as blocking per shard, and Lambda doesn't read any new records from the shard until the throttled batch of records either expires or succeeds\. If there is more than one shard in the stream, Lambda continues invoking on the non\-throttled shards until one gets through\. 
+ **Poll\-based event sources that are not stream\-based:** such as [Amazon Simple Queue Service](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-getting-started.html), AWS Lambda polls your queue and invokes your Lambda function\. When your Lambda function is throttled, Lambda attempts to process the throttled batch of records until it is succesfully invoked \(in which case the message is automatically deleted from the queue\) or until the MessageRetentionPeriod set for the queue expires\. 

## Monitoring Your Concurrency Usage<a name="monitoring-concurrent-usage"></a>

To understand your concurrent execution usage, AWS Lambda provides the following metrics:
+ **ConcurrentExecutions:** This shows you the concurrent executions at an account level, and for any function with a custom concurrency limit\.
+ **UnreservedConcurrentExecutions:** This shows you the total concurrent executions for functions assigned to the default “unreserved” concurrency pool\.

To learn about these metrics and how to access them, see [Using Amazon CloudWatch](monitoring-functions.md)\. 