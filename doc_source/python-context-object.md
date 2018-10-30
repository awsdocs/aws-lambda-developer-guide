# The Context Object \(Python\)<a name="python-context-object"></a>

**Topics**
+ [Example](#python-context-object-example)
+ [The Context Object Methods \(Python\)](#python-context-object-methods)
+ [The Context Object Attributes \(Python\)](#python-context-object-props)

While a Lambda function is executing, it can interact with the AWS Lambda service to get useful runtime information such as:
+ How much time is remaining before AWS Lambda terminates your Lambda function \(timeout is one of the Lambda function configuration properties\)\.
+ The CloudWatch log group and log stream associated with the Lambda function that is executing\.
+ The AWS request ID returned to the client that invoked the Lambda function\. You can use the request ID for any follow up inquiry with AWS support\. 
+  If the Lambda function is invoked through AWS Mobile SDK, you can learn more about the mobile application calling the Lambda function\. 

AWS Lambda provides this information via the `context` object that the service passes as the second parameter to your Lambda function handler\. For more information, see [Lambda Function Handler \(Python\)](python-programming-model-handler-types.md)\. 

The following sections provide an example Lambda function that uses the `context` object, and then lists all of the available methods and attributes\.

## Example<a name="python-context-object-example"></a>

Consider the following Python example\. It has one function that is also the handler\. The handler receives runtime information via the `context` object passed as parameter\. 

```
from __future__ import print_function

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

The handler code in this example simply prints some of the runtime information\. Each print statement creates a log entry in CloudWatch\. If you invoke the function using the Lambda console, the console displays the logs\. The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\.

**To test this code in the AWS Lambda console**

1. In the console, create a Lambda function using the hello\-world blueprint\. In **runtime**, choose **Python 2\.7**\. In **Handler**, replace `lambda_function.lambda_handler` with `lambda_function.get_my_log_stream`\. For instructions on how to do this, see  [Create a Simple Lambda Function](get-started-create-function.md)\.

1. Test the function, and then you can also update the code to get more context information\.

The following sections provide a list of available `context` object methods and attributes that you can use to get runtime information of your Lambda function\.

## The Context Object Methods \(Python\)<a name="python-context-object-methods"></a>

The context object provides the following methods:

**get\_remaining\_time\_in\_millis\(\)**  
 Returns the remaining execution time, in milliseconds, until AWS Lambda terminates the function\. 

## The Context Object Attributes \(Python\)<a name="python-context-object-props"></a>

The context object provides the following attributes:

**function\_name**  
Name of the Lambda function that is executing\.

**function\_version**  
The Lambda function version that is executing\. If an alias is used to invoke the function, then `function_version` will be the version the alias points to\.

**invoked\_function\_arn**  
The ARN used to invoke this function\. It can be function ARN or alias ARN\. An unqualified ARN executes the `$LATEST` version and aliases execute the function version it is pointing to\. 

**memory\_limit\_in\_mb**  
Memory limit, in MB, you configured for the Lambda function\. You set the memory limit at the time you create a Lambda function and you can change it later\.

**aws\_request\_id**  
AWS request ID associated with the request\. This is the ID returned to the client that called the `invoke` method\.   
If AWS Lambda retries the invocation \(for example, in a situation where the Lambda function that is processing Kinesis records throws an exception\), the request ID remains the same\.

**log\_group\_name**  
The name of the CloudWatch log group where you can find logs written by your Lambda function\.

**log\_stream\_name**  
 The name of the CloudWatch log stream where you can find logs written by your Lambda function\. The log stream may or may not change for each invocation of the Lambda function\.  
The value is none if your Lambda function is unable to create a log stream, which can happen if the execution role that grants necessary permissions to the Lambda function does not include permissions for the CloudWatch Logs actions\.

**identity**  
Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK\. It can be none\.  
+ **identity\.cognito\_identity\_id**
+ **identity\.cognito\_identity\_pool\_id**

**client\_context**  
Information about the client application and device when invoked through the AWS Mobile SDK\. It can be none\.  
+ **client\_context\.client\.installation\_id**
+ **client\_context\.client\.app\_title**
+ **client\_context\.client\.app\_version\_name**
+ **client\_context\.client\.app\_version\_code**
+ **client\_context\.client\.app\_package\_name**
+ **client\_context\.custom**

  A `dict` of custom values set by the mobile client application\.
+ **client\_context\.env**

  A `dict` of environment information provided by the AWS Mobile SDK\.

In addition to the options listed above, you can also use the AWS X\-Ray SDK for [Python](python-tracing.md) to identify critical code paths, trace their performance and capture the data for analysis\. 