# AWS Lambda Context Object in Java<a name="java-context-object"></a>

When Lambda runs your function, it passes a context object to the [handler](java-programming-model-handler-types.md)\. This object provides methods and properties that provide information about the invocation, function, and execution environment\.

**Context Methods**
+ `getRemainingTimeInMillis()` – Returns the number of milliseconds left before the execution times out\.
+ `getFunctionName()` – Returns the name of the Lambda function\.
+ `getFunctionVersion()` – Returns the [version](versioning-aliases.md) of the function\.
+ `getInvokedFunctionArn()` – Returns the Amazon Resource Name \(ARN\) used to invoke the function\. Indicates if the invoker specified a version number or alias\.
+ `getMemoryLimitInMB()` – Returns the amount of memory configured on the function\.
+ `getAwsRequestId()` – Returns the identifier of the invocation request\.
+ `getLogGroupName()` – Returns the log group for the function\.
+ `getLogStreamName()` – Returns the log stream for the function instance\.
+ `getIdentity()` – \(mobile apps\) Returns information about the Amazon Cognito identity that authorized the request\.
+ `getClientContext()` – \(mobile apps\) Returns the client context provided to the Lambda invoker by the client application\.
+ `getLogger()` – Returns the [logger object](java-logging.md) for the function\.

The following example shows a handler function that logs context information\.

**Example ContextLogger\.java**  

```
package example;
import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.runtime.Context;

public class ContextLogger {
    public static void myHandler(InputStream inputStream, OutputStream outputStream, Context context) {
        int letter;
        try {
            while((letter = inputStream.read()) != -1)
            {
                outputStream.write(Character.toUpperCase(letter));
            }
            Thread.sleep(3000); // Intentional delay for testing the getRemainingTimeInMillis() result.
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Print info from the context object
        System.out.println("Function name: " + context.getFunctionName());
        System.out.println("Max mem allocated: " + context.getMemoryLimitInMB());
        System.out.println("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
        System.out.println("CloudWatch log stream name: " + context.getLogStreamName());
        System.out.println("CloudWatch log group name: " + context.getLogGroupName());
    }
}
```

**Dependencies**
+ `aws-lambda-java-core`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)\.