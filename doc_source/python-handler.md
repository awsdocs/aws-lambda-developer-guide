# AWS Lambda function handler in Python<a name="python-handler"></a>

At the time you create a Lambda function, you specify a *handler*, which is a function in your code, that AWS Lambda can invoke when the service runs your code\. Use the following general syntax structure when creating a handler function in Python\. 

```
def handler_name(event, context): 
    ...
    return some_value
```

## Naming<a name="Naming"></a>

The Lambda function *handler* name specified at the time you create a Lambda function is derived from the following:
+ the name of the Python file in which the Lambda handler function is located
+ the name of the Python handler function

If your handler function name is `call_amazon`, located in a file named `handler.py`\. The handler name you would specify when you create your Lambda function is `handler.call_amazon`\.

## Syntax<a name="syntax"></a>

In the syntax, note the following:
+ `event` – AWS Lambda uses this parameter to pass in event data to the handler\. This parameter is usually of the Python `dict` type\. It can also be `list`, `str`, `int`, `float`, or `NoneType` type\.

  When you invoke your function, you determine the content and structure of the event\. When an AWS service invokes your function, the event structure varies by service\. For details, see [Using AWS Lambda with other services](lambda-services.md)\.
+ `context` – AWS Lambda uses this parameter to provide runtime information to your handler\. For details, see [AWS Lambda context object in Python](python-context.md)\.
+ Optionally, the handler can return a value\. What happens to the returned value depends on the invocation type you use when invoking the Lambda function:
  + If you use the `RequestResponse` invocation type \(synchronous execution\), AWS Lambda returns the result of the Python function call to the client invoking the Lambda function \(in the HTTP response to the invocation request, serialized into JSON\)\. For example, AWS Lambda console uses the `RequestResponse` invocation type, so when you invoke the function using the console, the console will display the returned value\.
  + If the handler returns objects that can't be serialized by `json.dumps`, the runtime returns an error\.
  + If the handler returns `None`, as Python functions without a `return` statement implicitly do, the runtime returns `null`\.
  + If you use the `Event` invocation type \(asynchronous execution\), the value is discarded\.

## Example<a name="example"></a>

For example, consider the following Python example code\. 

```
def my_handler(event, context):
      message = 'Hello {} {}!'.format(event['first_name'], 
                                      event['last_name'])  
     return { 
          'message' : message
      }
```

This example has one function called `my_handler`\. The function returns a message containing data from the event it received as input\. 

For more information, see the following Python code for the [Amazon Simple Queue Service](https://docs.aws.amazon.com/lambda/latest/dg/with-sqs-create-package.html#with-sqs-example-deployment-pkg-python)\.