# Using POJO Input and Output Types for Java Functions in AWS Lambda<a name="java-handler-pojo"></a>

Suppose your application events generate data that includes first name and last name as shown:

```
{ "firstName": "John", "lastName": "Doe" }
```

For this example, the handler receives this JSON and returns the string `"Hello John Doe"`\. 

**Example HelloPojo\.java**  

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HelloPojo implements RequestHandler<RequestClass, ResponseClass>{   

    public ResponseClass handleRequest(RequestClass request, Context context){
        String greetingString = String.format("Hello %s, %s.", request.firstName, request.lastName);
        return new ResponseClass(greetingString);
    }
}
```

**Dependencies**
+ `aws-lambda-java-core`
+ **Handler** – `example.HelloPojo::handleRequest`

To create a Lambda function with this handler, you must provide implementation of the input and output types as shown in the following Java example\. The `HelloPojo` class defines the function handler\. Define the input type in a separate class named `RequestClass`\.

**Example RequestClass\.java**  

```
package example;
        
     public class RequestClass {
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

        public RequestClass(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public RequestClass() {
        }
    }
```

Define the response type in its own class as well\.

**Example ResponseClass\.java**  

```
package example;
        
     public class ResponseClass {
        String greetings;

        public String getGreetings() {
            return greetings;
        }

        public void setGreetings(String greetings) {
            this.greetings = greetings;
        }

        public ResponseClass(String greetings) {
            this.greetings = greetings;
        }

        public ResponseClass() {
        }

    }
```

AWS Lambda serializes input and output based on standard bean naming conventions \(see [The Java EE 6 Tutorial](https://docs.oracle.com/javaee/6/tutorial/doc/gipks.html)\)\. Use mutable POJOs with public getters and setters\. Don't rely on any other features of serialization frameworks such as annotations\. If you need to customize the serialization behavior, use the stream input type and provide your own serialization\.

The `get` and `set` methods are required in order for the POJOs to work with AWS Lambda's built in JSON serializer\. The constructors that take no arguments are usually not required, however in this example we provided other constructors and therefore we need to explicitly provide the zero argument constructors\.