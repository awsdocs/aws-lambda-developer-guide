# AWS Lambda context object in Python<a name="python-context"></a>

When Lambda runs your function, it passes a context object to the [handler](python-handler.md)\. This object provides methods and properties that provide information about the invocation, function, and execution environment\.

**Context methods**
+ `get_remaining_time_in_millis` – Returns the number of milliseconds left before the execution times out\.

**Context properties**
+ `function_name` – The name of the Lambda function\.
+ `function_version` – The [version](configuration-versions.md) of the function\.
+ `invoked_function_arn` – The Amazon Resource Name \(ARN\) that's used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `memory_limit_in_mb` – The amount of memory that's allocated for the function\.
+ `aws_request_id` – The identifier of the invocation request\.
+ `log_group_name` – The log group for the function\.
+ `log_stream_name` – The log stream for the function instance\.
+ `identity` – \(mobile apps\) Information about the Amazon Cognito identity that authorized the request\.
  + `cognito_identity_id` – The authenticated Amazon Cognito identity\.
  + `cognito_identity_pool_id` – The Amazon Cognito identity pool that authorized the invocation\.
+ `client_context` – \(mobile apps\) Client context that's provided to Lambda by the client application\.
  + `client.installation_id`
  + `client.app_title`
  + `client.app_version_name`
  + `client.app_version_code`
  + `client.app_package_name`
  + `custom` – A `dict` of custom values set by the mobile client application\.
  + `env` – A `dict` of environment information provided by the AWS SDK\.

The following example shows a handler function that logs context information\.

**Example handler\.py**  

```
import time
def get_my_log_stream(event, context):       
    print("Log stream name:", context.log_stream_name)
    print("Log group name:",  context.log_group_name)
    print("Request ID:",context.aws_request_id)
    print("Mem. limits(MB):", context.memory_limit_in_mb)
    # Code will execute quickly, so we add a 1 second intentional delay so you can see that in time remaining value.
    time.sleep(1) 
    print("Time remaining (MS):", context.get_remaining_time_in_millis())
```

In addition to the options listed above, you can also use the AWS X\-Ray SDK for [Instrumenting Python code in AWS Lambda](python-tracing.md) to identify critical code paths, trace their performance and capture the data for analysis\. 