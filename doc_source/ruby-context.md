# AWS Lambda context object in Ruby<a name="ruby-context"></a>

When Lambda runs your function, it passes a context object to the [handler](ruby-handler.md)\. This object provides methods and properties that provide information about the invocation, function, and execution environment\.

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
+ `deadline_ms`– The date that the execution times out, in Unix time milliseconds\.
+ `identity` – \(mobile apps\) Information about the Amazon Cognito identity that authorized the request\.
+ `client_context`– \(mobile apps\) Client context that's provided to Lambda by the client application\.