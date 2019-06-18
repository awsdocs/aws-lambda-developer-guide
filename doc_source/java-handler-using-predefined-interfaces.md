# Leveraging Predefined Interfaces for Creating Handler \(Java\)<a name="java-handler-using-predefined-interfaces"></a>

You can use one of the predefined interfaces provided by the AWS Lambda Java core library \(`aws-lambda-java-core`\) to create your Lambda function handler, as an alternative to writing your own handler method with an arbitrary name and parameters\. For more information about handlers, see \(see [AWS Lambda Function Handler in Java](java-programming-model-handler-types.md)\)\.

You can implement one of the predefined interfaces, `RequestStreamHandler` or `RequestHandler` and provide implementation for the `handleRequest` method that the interfaces provide\. You implement one of these interfaces depending on whether you want to use standard Java types or custom POJO types for your handler input/output \(where AWS Lambda automatically serializes and deserializes the input and output to Match your data type\), or customize the serialization using the `Stream` type\.

**Note**  
These interfaces are available in the `aws-lambda-java-core` library\. 

When you implement standard interfaces, they help you validate your method signature at compile time\. 

If you implement one of the interfaces, you specify *package*\.*class* in your Java code as the handler when you create the Lambda function\. For example, the following is the modified `create-function` CLI command from the getting started\. Note that the `--handler` parameter specifies "example\.Hello" value:

```
aws lambda create-function \
--region region \
--function-name getting-started-lambda-function-in-java \
--zip-file fileb://deployment-package (zip or jar)
        path \
--role arn:aws:iam::account-id:role/lambda_basic_execution  \
--handler example.Hello \
--runtime java8 \
--timeout 15 \
--memory-size 512
```

The following sections provide examples of implementing these interfaces\. 

## Example 1: Creating Handler with Custom POJO Input/Output \(Leverage the RequestHandler Interface\)<a name="java-handler-using-predefined-interfaces-pojo-handler-class"></a>

The example `Hello` class in this section implements the `RequestHandler` interface\. The interface defines `handleRequest()` method that takes in event data as input parameter of the `Request` type and returns an POJO object of the `Response` type:

```
public Response handleRequest(Request request, Context context) {
   ...
}
```

The `Hello` class with sample implementation of the `handleRequest()` method is shown\. For this example, we assume event data consists of first name and last name\. 

```
package example;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context; 

public class Hello implements RequestHandler<Request, Response> {
    
    public Response handleRequest(Request request, Context context) {
        String greetingString = String.format("Hello %s %s.", request.firstName, request.lastName);
        return new Response(greetingString);
    }
}
```

For example, if the event data in the `Request` object is:

```
{
  "firstName":"value1",
  "lastName" : "value2"
}
```

The method returns a `Response` object as follows:

```
{
  "greetings": "Hello value1 value2."
}
```

Next, you need to implement the `Request` and `Response` classes\. You can use the following implementation for testing:

The Request class:

```
package example;

public class Request {
    String firstName;
    String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Request(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Request() {
    }
}
```

The Response class:

```
package example;

public class Response {
    String greetings;

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

    public Response(String greetings) {
        this.greetings = greetings;
    }

    public Response() {
    }
}
```

You can create a Lambda function from this code and test the end\-to\-end experience as follows:
+ Using the preceding code, create a deployment package\. For more information, see [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)
+ Upload the deployment package to AWS Lambda and create your Lambda function\.
+ Test the Lambda function using either the console or CLI\. You can specify any sample JSON data that conform to the getter and setter in your `Request` class, for example:

  ```
  {
    "firstName":"John",
    "lastName" : "Doe"
  }
  ```

  The Lambda function will return the following JSON in response\. 

  ```
  {
    "greetings": "Hello John, Doe."
  }
  ```

Follow instructions provided in the getting started \(see [Create a Lambda Function Authored in Java](get-started-step4-optional.md)\)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-core` library dependency\.
+ When you create the Lambda function specify `example.Hello` \(*package*\.*class*\) as the handler value\.

## Example 2: Creating Handler with Stream Input/Output \(Leverage the `RequestStreamHandler` Interface\)<a name="java-handler-using-predefined-interfaces-stream-handler-class"></a>

The `Hello` class in this example implements the `RequestStreamHandler` interface\. The interface defines `handleRequest` method as follows:

```
public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
        throws IOException {
      ...
}
```

The `Hello` class with sample implementation of the `handleRequest()` handler is shown\. The handler processes incoming event data \(for example, a string "hello"\) by simply converting it to uppercase and return it\.

```
package example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context; 

public class Hello implements RequestStreamHandler {
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        int letter;
        while((letter = inputStream.read()) != -1)
        {
            outputStream.write(Character.toUpperCase(letter));
        }
    }
}
```

You can create a Lambda function from this code and test the end\-to\-end experience as follows:
+ Use the preceding code to create deployment package\.
+ Upload the deployment package to AWS Lambda and create your Lambda function\.
+ Test the Lambda function using either the console or CLI\. You can specify any sample string data, for example:

  ```
  "test"
  ```

  The Lambda function will return `TEST` in response\. 

Follow instructions provided in the getting started \(see [Create a Lambda Function Authored in Java](get-started-step4-optional.md)\)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-core` library dependency\.
+ When you create the Lambda function specify `example.Hello` \(*package*\.*class*\) as the handler value\.