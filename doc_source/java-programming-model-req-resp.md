# Handler Input/Output Types \(Java\)<a name="java-programming-model-req-resp"></a>

When AWS Lambda executes the Lambda function, it invokes the handler\. The first parameter is the input to the handler which can be event data \(published by an event source\) or custom input you provide such as a string or any custom data object\. 

AWS Lambda supports the following input/output types for a handler:
+ Simple Java types \(AWS Lambda supports the String, Integer, Boolean, Map, and List types\)
+ POJO \(Plain Old Java Object\) type
+ Stream type \(If you do not want to use POJOs or if Lambda's serialization approach does not meet your needs, you can use the byte stream implementation\. For more information, see [Example: Using Stream for Handler Input/Output \(Java\)](java-handler-io-type-stream.md)\.\)

## Handler Input/Output: String Type<a name="java-programming-model-req-resp-string"></a>

The following Java class shows a handler called `myHandler` that uses String type for input and output\.

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 

public class Hello {
    public String myHandler(String name, Context context) {
        return String.format("Hello %s.", name);
    }
}
```

You can have similar handler functions for other simple Java types\. 

**Note**  
When you invoke a Lambda function asynchronously, any return value by your Lambda function will be ignored\. Therefore you might want to set the return type to void to make this clear in your code\. For more information, see [Invoke](API_Invoke.md)\.

To test an end\-to\-end example, see [Create a Lambda Function Authored in Java](get-started-step4-optional.md)\.

## Handler Input/Output: POJO Type<a name="java-programming-model-req-resp-pojo"></a>

The following Java class shows a handler called `myHandler` that uses POJOs for input and output\.

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 

public class HelloPojo {

    // Define two classes/POJOs for use with Lambda function.
    public static class RequestClass {
      ...
    }

    public static class ResponseClass {
      ...
    }

    public static ResponseClass myHandler(RequestClass request, Context context) {
        String greetingString = String.format("Hello %s, %s.", request.getFirstName(), request.getLastName());
        return new ResponseClass(greetingString);
    }
}
```

AWS Lambda serializes based on standard bean naming conventions \(see [The Java EE 6 Tutorial](https://docs.oracle.com/javaee/6/tutorial/doc/gipks.html)\)\. You should use mutable POJOs with public getters and setters\. 

**Note**  
You shouldn't rely on any other features of serialization frameworks such as annotations\. If you need to customize the serialization behavior, you can use the raw byte stream to use your own serialization\.

If you use POJOs for input and output, you need to provide implementation of the `RequestClass` and `ResponseClass` types\. For an example, see [Example: Using POJOs for Handler Input/Output \(Java\)](java-handler-io-type-pojo.md)\.