# Using Provided Interfaces for Java Function Handlers in AWS Lambda<a name="java-handler-interfaces"></a>

You can use one of the predefined interfaces provided by the AWS Lambda Java core library \(`aws-lambda-java-core`\) to create your Lambda function handler\. Using the provided interfaces simplifies handler definition and enables you to validate your handler method signature at compile time\.

The library defines 2 interfaces, `RequestStreamHandler` and `RequestHandler`\. You implement one of these interfaces depending on whether you want to use standard Java types or custom POJO types for your handler input/output \(where AWS Lambda automatically serializes and deserializes the input and output to Match your data type\), or customize the serialization using the `Stream` type\.

If you implement one of the interfaces, you specify *package*\.*class* in your Java code as the handler when you create the Lambda function\. For example, the following is the modified `create-function` CLI command from the getting started\. Note that the `--handler` parameter specifies "example\.Hello" value:

```
aws lambda create-function --function-name my-function \
--zip-file fileb://function.zip \
--role arn:aws:iam::123456789012:role/lambdarole  \
--handler example.Hello --runtime java11
```

The following sections provide examples of implementing these interfaces\. 

## Using Custom Types with the RequestHandler Interface<a name="java-handler-interfaces-pojo"></a>

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

The following example code shows the `Request` and `Response` classes\.

**Example Request\.java**  

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

**Example Response\.java**  

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

**Example Event**  

```
{
  "firstName":"John",
  "lastName" : "Doe"
}
```

**Example Output**  

```
{
  "greetings": "Hello John, Doe."
}
```

**Dependencies**
+ `aws-lambda-java-core`

When you create the Lambda function specify `example.Hello` \(*package*\.*class*\) as the handler value\.

## Using Streams with the RequestStreamHandler Interface<a name="java-handler-interfaces-stream"></a>

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

**Dependencies**
+ `aws-lambda-java-core`

When you create the Lambda function specify `example.Hello` \(*package*\.*class*\) as the handler value\.