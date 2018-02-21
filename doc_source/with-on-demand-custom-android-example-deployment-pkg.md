# Step 2\.1: Create a Deployment Package<a name="with-on-demand-custom-android-example-deployment-pkg"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-on-demand-custom-android-example-deployment-pkg-nodejs"></a>

Follow the instructions to create an AWS Lambda function deployment package\. 

1. Open a text editor, and then copy the following code\. 

   ```
   exports.handler = function(event, context, callback) {
      console.log("Received event: ", event);
      var data = {
          "greetings": "Hello, " + event.firstName + " " + event.lastName + "."
      };
      callback(null, data);
   }
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes v6\.10 or v4\.3\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as `AndroidBackendLambdaFunction.js`\. 

1. Zip the `AndroidBackendLambdaFunction.js` file as `AndroidBackendLambdaFunction.zip`\. 

### Next Step<a name="with-on-demand-custom-android-example-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-on-demand-custom-android-example-create-iam-role.md) 

## Java<a name="with-on-demand-custom-android-example-deployment-pkg-java"></a>

Use the following Java code to create your Lambda function \(`AndroidBackendLambdaFunction`\)\. The code receives Android app event data as the first parameter to the handler\. Then, the code processes event data \(for illustration this code writes some of the event data to CloudWatch Logs and returns a string in response\)\.

In the code, the `handler` \(`myHandler`\) uses the `RequestClass` and `ResponseClass` types for the input and output\. The code provides implementation for these types\.

**Important**  
You use the same classes \(POJOs\) to handle the input and output data when you create the sample mobile application in the next section\. 

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

Save the preceding code in a file \(`HelloPojo.java`\)\. Your can now create a deployment package\. You need to include the following dependency: 

+ `aws-lambda-java-core`

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package \(`lambda-java-example-1.0-SNAPSHOT.jar`\) is created, go to the next section to create an IAM role \(execution role\)\. You specify the role when you create your Lambda function\. 

### Next Step<a name="with-on-demand-custom-android-example-deployment-pkg-java-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-on-demand-custom-android-example-create-iam-role.md) 