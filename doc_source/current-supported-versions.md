# Lambda Execution Environment and Available Libraries<a name="current-supported-versions"></a>

The underlying AWS Lambda execution environment includes the following software and libraries\.
+ Operating system – Amazon Linux
+ AMI – [amzn\-ami\-hvm\-2017\.03\.1\.20170812\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2017.03.1.20170812-x86_64-gp2)
+ Linux kernel – 4\.14\.72\-68\.55\.amzn1\.x86\_64
+ AWS SDK for JavaScript – 2\.290\.0
+ SDK for Python \(Boto 3\) – 3\-1\.7\.74 botocore\-1\.10\.74

Not all runtime languages are available on the Amazon Linux AMI or its yum repositories\. If you use Amazon Linux for development, you might need to download and install them manually from their respective public sites\.

## Environment Variables Available to Lambda Functions<a name="lambda-environment-variables"></a>

The following is a list of environment variables that are part of the AWS Lambda execution environment and made available to Lambda functions\. The table below indicates which ones are reserved by AWS Lambda and can't be changed, as well as which ones you can set when creating your Lambda function\. For more information on using environment variables with your Lambda function, see [AWS Lambda Environment Variables](env_variables.md)\. 


**Lambda Environment Variables**  

| Key | Reserved | Value | 
| --- | --- | --- | 
|  `_HANDLER`  |  Yes  |  The handler location configured on the function\.  | 
|   `AWS_REGION`   |  Yes  |  The AWS region where the Lambda function is executed\.  | 
|  `AWS_EXECUTION_ENV`  |  Yes  |  The [runtime identifier](lambda-runtimes.md), prefixed by `AWS_Lambda_`\. For example, `AWS_Lambda_java8`\.  | 
|  `AWS_LAMBDA_FUNCTION_NAME`  |  Yes  |  The name of the function\.  | 
|  `AWS_LAMBDA_FUNCTION_MEMORY_SIZE`  |  Yes  |  The amount of memory available to the function in MB\.  | 
|  `AWS_LAMBDA_FUNCTION_VERSION`  |  Yes  |  The version of the function being executed\.  | 
|  `AWS_LAMBDA_LOG_GROUP_NAME` `AWS_LAMBDA_LOG_STREAM_NAME`  |  Yes  |  The name of the Amazon CloudWatch Logs group and stream for the function\.  | 
|  `AWS_ACCESS_KEY_ID` `AWS_SECRET_ACCESS_KEY` `AWS_SESSION_TOKEN`  |  Yes  |  Access keys obtained from the function's [execution role](intro-permission-model.md#lambda-intro-execution-role)\.  | 
|  `LANG`  |  No  |  `en_US.UTF-8`\. This is the locale of the runtime\.  | 
|  `TZ`  |  Yes  |  The environment's timezone \(UTC\)\. The execution environment uses NTP to synchronize the system clock\.  | 
|  `LAMBDA_TASK_ROOT`  |  Yes  |  The path to your Lambda function code\.  | 
|  `LAMBDA_RUNTIME_DIR`  |  Yes  | The path to runtime libraries\. | 
|  `PATH`  |  No  |  `/usr/local/bin:/usr/bin/:/bin:/opt/bin`  | 
|  `LD_LIBRARY_PATH`  |  No  |  `/lib64:/usr/lib64:$LAMBDA_RUNTIME_DIR:$LAMBDA_RUNTIME_DIR/lib:$LAMBDA_TASK_ROOT:$LAMBDA_TASK_ROOT/lib:/opt/lib`  | 
|  `NODE_PATH`  |  No  |  \(Node\.js\) `/opt/nodejs/node8/node_modules/:/opt/nodejs/node_modules:$LAMBDA_RUNTIME_DIR/node_modules`  | 
|  `PYTHONPATH`  |  No  |  \(Python\) `$LAMBDA_RUNTIME_DIR`\.  | 
|  `AWS_LAMBDA_RUNTIME_API`  |  Yes  |  \(custom runtime\) The host and port of the [runtime API](runtimes-api.md)\.  | 