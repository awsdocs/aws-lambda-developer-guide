# Sample function code<a name="with-android-create-package"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js](#with-android-example-deployment-pkg-nodejs)
+ [Java](#with-on-demand-custom-android-example-deployment-pkg-java)

## Node\.js<a name="with-android-example-deployment-pkg-nodejs"></a>

The following example uses data to generate a string response\.

**Example index\.js**  

```
exports.handler = function(event, context, callback) {
   console.log("Received event: ", event);
   var data = {
       "greetings": "Hello, " + event.firstName + " " + event.lastName + "."
   };
   callback(null, data);
}
```

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md)\.

## Java<a name="with-on-demand-custom-android-example-deployment-pkg-java"></a>

The following example uses data to generate a string response\.

In the code, the `handler` \(`myHandler`\) uses the `RequestClass` and `ResponseClass` types for the input and output\. The code provides implementation for these types\.

**Example HelloPojo\.java**  

```
package example;

import com.amazonaws.services.lambda.runtime.Context; 

public class HelloPojo {

    // Define two classes/POJOs for use with Lambda function.
    public static class RequestClass {
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

    public static class ResponseClass {
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

    public static ResponseClass myHandler(RequestClass request, Context context){
        String greetingString = String.format("Hello %s, %s.", request.firstName, request.lastName);
        context.getLogger().log(greetingString);
        return new ResponseClass(greetingString);
    }
}
```

**Dependencies**
+ `aws-lambda-java-core`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md)\.