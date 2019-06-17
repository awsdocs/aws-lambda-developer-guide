# Environment Variables Available to Lambda Functions<a name="lambda-environment-variables"></a>

The following is a list of environment variables that are part of the AWS Lambda execution environment and made available to Lambda functions\. The table below indicates which ones are reserved by AWS Lambda and can't be changed, as well as which ones you can set when creating your Lambda function\. For more information on using environment variables with your Lambda function, see [AWS Lambda Environment Variables](env_variables.md)\. 


**Lambda Environment Variables**  

| Key | Reserved | Value | 
| --- | --- | --- | 
|  `_HANDLER`  |  Yes  |  The handler location configured on the function\.  | 
|  `AWS_REGION`  |  Yes  |  The AWS region where the Lambda function is executed\.  | 
|  `AWS_EXECUTION_ENV`  |  Yes  |  The [runtime identifier](lambda-runtimes.md), prefixed by `AWS_Lambda_`\. For example, `AWS_Lambda_java8`\.  | 
|  `AWS_LAMBDA_FUNCTION_NAME`  |  Yes  |  The name of the function\.  | 
|  `AWS_LAMBDA_FUNCTION_MEMORY_SIZE`  |  Yes  |  The amount of memory available to the function in MB\.  | 
|  `AWS_LAMBDA_FUNCTION_VERSION`  |  Yes  |  The version of the function being executed\.  | 
|  `AWS_LAMBDA_LOG_GROUP_NAME` `AWS_LAMBDA_LOG_STREAM_NAME`  |  Yes  |  The name of the Amazon CloudWatch Logs group and stream for the function\.  | 
|  `AWS_ACCESS_KEY_ID` `AWS_SECRET_ACCESS_KEY` `AWS_SESSION_TOKEN`  |  Yes  |  Access keys obtained from the function's [execution role](lambda-intro-execution-role.md)\.  | 
|  `LANG`  |  No  |  `en_US.UTF-8`\. This is the locale of the runtime\.  | 
|  `TZ`  |  Yes  |  The environment's timezone \(UTC\)\. The execution environment uses NTP to synchronize the system clock\.  | 
|  `LAMBDA_TASK_ROOT`  |  Yes  |  The path to your Lambda function code\.  | 
|  `LAMBDA_RUNTIME_DIR`  |  Yes  | The path to runtime libraries\. | 
|  `PATH`  |  No  |  `/usr/local/bin:/usr/bin/:/bin:/opt/bin`  | 
|  `LD_LIBRARY_PATH`  |  No  |  `/lib64:/usr/lib64:$LAMBDA_RUNTIME_DIR:$LAMBDA_RUNTIME_DIR/lib:$LAMBDA_TASK_ROOT:$LAMBDA_TASK_ROOT/lib:/opt/lib`  | 
|  `NODE_PATH`  |  No  |  \(Node\.js\) `/opt/nodejs/node8/node_modules/:/opt/nodejs/node_modules:$LAMBDA_RUNTIME_DIR/node_modules`  | 
|  `PYTHONPATH`  |  No  |  \(Python\) `$LAMBDA_RUNTIME_DIR`\.  | 
|  `GEM_PATH`  |  No  |  \(Ruby\) `$LAMBDA_TASK_ROOT/vendor/bundle/ruby/2.5.0:/opt/ruby/gems/2.5.0`\.  | 
|  `AWS_LAMBDA_RUNTIME_API`  |  Yes  |  \(custom runtime\) The host and port of the [runtime API](runtimes-api.md)\.  | 