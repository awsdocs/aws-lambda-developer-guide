# Example: Using POJOs for Handler Input/Output \(Java\)<a name="java-handler-io-type-pojo"></a>

Suppose your application events generate data that includes first name and last name as shown:

```
{ "firstName": "John", "lastName": "Doe" }  
```

For this example, the handler receives this JSON and returns the string `"Hello John Doe"`\. 

```
public static ResponseClass handleRequest(RequestClass request, Context context){
        String greetingString = String.format("Hello %s, %s.", request.firstName, request.lastName);
        return new ResponseClass(greetingString);
}
```

To create a Lambda function with this handler, you must provide implementation of the input and output types as shown in the following Java example\. The `HelloPojo` class defines the `handler` method\. 

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

In order to implement the input type, add the following code to a separate file and name it *RequestClass\.java*\. Place it next to the *HelloPojo\.java *class in your directory structure:

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

In order to implement the output type, add the following code to a separate file and name it *ResponseClass\.java*\. Place it next to the *HelloPojo\.java *class in your directory structure:

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

**Note**  
 The `get` and `set` methods are required in order for the POJOs to work with AWS Lambda's built in JSON serializer\. The constructors that take no arguments are usually not required, however in this example we provided other constructors and therefore we need to explicitly provide the zero argument constructors\.

You can upload this code as your Lambda function and test as follows:
+ Using the preceding code files, create a deployment package\.
+ Upload the deployment package to AWS Lambda and create your Lambda function\. You can do this using the console or AWS CLI\.
+ Invoke the Lambda function manually using the console or the CLI\. You can use provide sample JSON event data when you manually invoke your Lambda function\. For example: 

  ```
  { "firstName":"John", "lastName":"Doe" }
  ```

For more information, see [Create a Lambda Function Authored in Java](get-started-step4-optional.md)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-core` library dependency\.
+ When you create the Lambda function, specify `example.HelloPojo::handleRequest` \(*package*\.*class*::*method*\) as the handler value\.