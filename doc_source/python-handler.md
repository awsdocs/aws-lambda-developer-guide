# AWS Lambda function handler in Python<a name="python-handler"></a>

The AWS Lambda function handler is the method in your function code that processes events\. When your function is invoked, Lambda runs the handler method\. When the handler exits or returns a response, it becomes available to handle another event\.

You can use the following general syntax when creating a function handler in Python:

```
def handler_name(event, context): 
    ...
    return some_value
```

## Naming<a name="Naming"></a>

The Lambda function *handler* name specified at the time you create a Lambda function is derived from the following:
+ the name of the file in which the Lambda handler function is located
+ the name of the Python handler function

A function handler can be any name; however, the default on the Lambda console is `lambda_function.lambda_handler`\. This name reflects the function name as `lambda_handler`, and the file where the handler code is stored in `lambda_function.py`\.

If you choose a different name for your function handler on the Lambda console, you must update the name on the **Runtime settings** pane\. The following example shows the Lambda function handler on the Lambda console:

![\[The following image shows the function handler on the Lambda console.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/python-console-handler.png)

## How it works<a name="python-handler-how"></a>

When your function handler is invoked by Lambda, the [Lambda runtime](lambda-runtimes.md) passes two arguments to the function handler:
+ The first argument is the [event object](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-concepts.html#gettingstarted-concepts-event)\. An event is a JSON\-formatted document that contains data for a Lambda function to process\. The [Lambda runtime](lambda-runtimes.md) converts the event to an object and passes it to your function code\. It is usually of the Python `dict` type\. It can also be `list`, `str`, `int`, `float`, or the `NoneType` type\.

  The event object contains information from the invoking service\. When you invoke a function, you determine the structure and contents of the event\. When an AWS service invokes your function, the service defines the event structure\. For more information about events from AWS services, see [Using AWS Lambda with other services](lambda-services.md)\.
+ The second argument is the [context object](python-context.md)\. A context object is passed to your function by Lambda at runtime\. This object provides methods and properties that provide information about the invocation, function, and runtime environment\.

## Returning a value<a name="python-handler-return"></a>

Optionally, a handler can return a value\. What happens to the returned value depends on the [invocation type](lambda-invocation.md) and the [Using AWS Lambda with other services](lambda-services.md)\. For example:
+ If you use the `RequestResponse` invocation type, such as [Synchronous invocation](invocation-sync.md), AWS Lambda returns the result of the Python function call to the client invoking the Lambda function \(in the HTTP response to the invocation request, serialized into JSON\)\. For example, AWS Lambda console uses the `RequestResponse` invocation type, so when you invoke the function on the console, the console will display the returned value\.
+ If the handler returns objects that can't be serialized by `json.dumps`, the runtime returns an error\.
+ If the handler returns `None`, as Python functions without a `return` statement implicitly do, the runtime returns `null`\.
+ If you use an `Event` an [Asynchronous invocation](invocation-async.md) invocation type, the value is discarded\.

## Examples<a name="python-example"></a>

The following section shows examples of Python functions you can use with Lambda\. If you use the Lambda console to author your function, you do not need to attach a [\.zip archive file](python-package.md) to run the functions in this section\. These functions use standard Python libraries which are included with the Lambda runtime you selected\. For more information, see [Lambda deployment packages](gettingstarted-package.md)\.

### Returning a message<a name="python-example-message"></a>

The following example shows a function called `lambda_handler` that uses the `python3.8` [Lambda runtime](lambda-runtimes.md)\. The function accepts user input of a first and last name, and returns a message that contains data from the event it received as input\.

```
def lambda_handler(event, context):
    message = 'Hello {} {}!'.format(event['first_name'], event['last_name'])  
    return { 
        'message' : message
    }
```

You can use the following event data to [invoke the function](https://docs.aws.amazon.com/lambda/latest/dg/getting-started-create-function.html#get-started-invoke-manually):

```
{
    "first_name": "John",
    "last_name": "Smith"
}
```

The response shows the event data passed as input:

```
{
    "message": "Hello John Smith!"
}
```

### Parsing a response<a name="python-example-parse"></a>

The following example shows a function called `lambda_handler` that uses the `python3.8` [Lambda runtime](lambda-runtimes.md)\. The function uses event data passed by Lambda at runtime\. It parses the [environment variable](configuration-envvars.md) in `AWS_REGION` returned in the JSON response\.

```
import os
import json
        
def lambda_handler(event, context):
    json_region = os.environ['AWS_REGION']
    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps({
            "Region ": json_region
        })
    }
```

You can use any event data to [invoke the function](https://docs.aws.amazon.com/lambda/latest/dg/getting-started-create-function.html#get-started-invoke-manually):

```
{
  "key1": "value1",
  "key2": "value2",
  "key3": "value3"
}
```

Lambda runtimes set several environment variables during initialization\. For more information on the environment variables returned in the response at runtime, see [Using AWS Lambda environment variables](configuration-envvars.md)\.

The function in this example depends on a successful response \(in `200`\) from the Invoke API\. For more information on the Invoke API status, see the [Invoke API Response Syntax](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html#API_Invoke_ResponseSyntax)\.

### Returning a calculation<a name="python-example-userinputcalc"></a>

The following example [Lambda Python function code on GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/python/example_code/lambda/boto_client_examples/lambda_handler_basic.py) shows a function called `lambda_handler` that uses the `python3.6` [Lambda runtime](lambda-runtimes.md)\. The function accepts user input and returns a calculation to the user\.

You can use the following event data to [invoke the function](https://docs.aws.amazon.com/lambda/latest/dg/getting-started-create-function.html#get-started-invoke-manually):

```
{
    "action": "increment",
    "number": 3
}
```