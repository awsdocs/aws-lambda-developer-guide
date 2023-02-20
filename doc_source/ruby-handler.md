# AWS Lambda function handler in Ruby<a name="ruby-handler"></a>

The Lambda function *handler* is the method in your function code that processes events\. When your function is invoked, Lambda runs the handler method\. When the handler exits or returns a response, it becomes available to handle another event\.

In the following example, the file `function.rb` defines a handler method named `handler`\. The handler function takes two objects as input and returns a JSON document\.

**Example function\.rb**  

```
require 'json'

def handler(event:, context:)
    { event: JSON.generate(event), context: JSON.generate(context.inspect) }
end
```

In your function configuration, the `handler` setting tells Lambda where to find the handler\. For the preceding example, the correct value for this setting is **function\.handler**\. It includes two names separated by a dot: the name of the file and the name of the handler method\.

You can also define your handler method in a class\. The following example defines a handler method named `process` on a class named `Handler` in a module named `LambdaFunctions`\.

**Example source\.rb**  

```
module LambdaFunctions
  class Handler
    def self.process(event:,context:)
      "Hello!"
    end
  end
end
```

In this case, the handler setting is **source\.LambdaFunctions::Handler\.process**\.

The two objects that the handler accepts are the invocation event and context\. The event is a Ruby object that contains the payload that's provided by the invoker\. If the payload is a JSON document, the event object is a Ruby hash\. Otherwise, it's a string\. The [context object](ruby-context.md) has methods and properties that provide information about the invocation, the function, and the execution environment\.

The function handler is executed every time your Lambda function is invoked\. Static code outside of the handler is executed once per instance of the function\. If your handler uses resources like SDK clients and database connections, you can create them outside of the handler method to reuse them for multiple invocations\.

Each instance of your function can process multiple invocation events, but it only processes one event at a time\. The number of instances processing an event at any given time is your function's *concurrency*\. For more information about the Lambda execution environment, see [Lambda execution environment](lambda-runtime-environment.md)\.