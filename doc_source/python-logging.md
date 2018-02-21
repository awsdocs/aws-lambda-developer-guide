# Logging \(Python\)<a name="python-logging"></a>

Your Lambda function can contain logging statements\. AWS Lambda writes these logs to CloudWatch\. If you use the Lambda console to invoke your Lambda function, the console displays the same logs\. 

The following Python statements generate log entries:

+ `print` statements\.

+ `Logger` functions in the `logging` module \(for example, `logging.Logger.info` and `logging.Logger.error`\)\.

Both `print` and `logging.*` functions write logs to CloudWatch Logs but the `logging.*` functions write additional information to each log entry, such as time stamp and log level\.

For example, consider the following Python code example\. 

```
import logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)
def my_logging_handler(event, context):
    logger.info('got event{}'.format(event))
    logger.error('something went wrong')
    return 'Hello from Lambda!'
```

Because the code example uses the `logging` module to write message to the logs, you also get some additional information in the log such as the time stamp and the log levels\. The log level identifies the type of log, such as `[INFO]`, `[ERROR]`, and `[DEBUG]`\.

You can also find these logs in CloudWatch\. For more information, see [Accessing Amazon CloudWatch Logs for AWS Lambda](monitoring-functions-logs.md)\.

Instead of using the `logging` module, you can use the `print` statements in your code as shown in the following Python example:

```
from __future__ import print_function
def lambda_handler(event, context):
    print('this will also show up in cloud watch')
    return 'Hello World!'
```

In this case only the text passed to the print method is sent to CloudWatch\. The log entries will not have additional information that the `logging.*` function returns\. The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/python-logging-20.png)

The console uses the `RequestResponse` invocation type \(synchronous invocation\) when invoking the function\. And therefore it gets the return value \("Hello world\!"\) back from AWS Lambda which the console displays\.

**To test the preceding Python code \(console\)**

1. In the console, create a Lambda function using the hello\-world\-python blueprint\. In **runtime**, choose **Python 2\.7**\. In **Handler**, replace `lambda_function.lambda_handler` with `lambda_function.my_other_logging_handler` and in **Role**, choose **Basic execution role**\. You also replace the code provided by the blueprint by the code in this section\. For step\-by\-step instructions to create a Lambda function using the console, see  [Create a Simple Lambda Function](get-started-create-function.md)\. 

1. Replace the template code with the code provided in this section\.

1. Test the Lambda function using the **Sample event template** called **Hello World** provided in the Lambda console\. 

## Finding Logs<a name="python-logging-finding-logs"></a>

You can find the logs that your Lambda function writes, as follows:

+ **In the AWS Lambda console** – The ** Log output**  section in AWS Lambda console shows the logs\. 

+ **In the response header, when you invoke a Lambda function programmatically** – If you invoke a Lambda function programmatically, you can add the `LogType` parameter to retrieve the last 4 KB of log data that is written to CloudWatch Logs\. AWS Lambda returns this log information in the `x-amz-log-results` header in the response\. For more information, see [Invoke](API_Invoke.md)\.

  If you use AWS CLI to invoke the function, you can specify the` --log-type parameter` with value `Tail` to retrieve the same information\.

+ **In CloudWatch Logs** – To find your logs in CloudWatch you need to know the log group name and log stream name\. You can use the `context.logGroupName`, and `context.logStreamName` properties in your code to get this information\. When you run your Lambda function, the resulting logs in the console or CLI will show you the log group name and log stream name\. 