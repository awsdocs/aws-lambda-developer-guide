# The Context Object \(Java\)<a name="java-context-object"></a>

You interact with AWS Lambda execution environment via the context parameter\. The context object allows you to access useful information available within the Lambda execution environment\. For example, you can use the context parameter to determine the CloudWatch log stream associated with the function, or use the clientContext property of the context object to learn more about the application calling the Lambda function \(when invoked through the AWS Mobile SDK\)\.

The context object properties are:
+ `getMemoryLimitInMB()`: Memory limit, in MB, you configured for the Lambda function\.
+ `getFunctionName()`: Name of the Lambda function that is running\.
+ `getFunctionVersion()`: The Lambda function version that is executing\. If an alias is used to invoke the function, then `getFunctionVersion` will be the version the alias points to\.
+ `getInvokedFunctionArn()`: The ARN used to invoke this function\. It can be function ARN or alias ARN\. An unqualified ARN executes the `$LATEST` version and aliases execute the function version it is pointing to\. 
+  `getAwsRequestId()`: AWS request ID associated with the request\. This is the ID returned to the client that called invoke\(\)\. You can use the request ID for any follow up enquiry with AWS support\. Note that if AWS Lambda retries the function \(for example, in a situation where the Lambda function processing Kinesis records throw an exception\), the request ID remains the same\.
+ `getLogStreamName()`: The CloudWatch log stream name for the particular Lambda function execution\. It can be null if the IAM user provided does not have permission for CloudWatch actions\.
+ `getLogGroupName()`: The CloudWatch log group name associated with the Lambda function invoked\. It can be null if the IAM user provided does not have permission for CloudWatch actions\.
+ `getClientContext()`: Information about the client application and device when invoked through the AWS Mobile SDK\. It can be null\.  Client context provides client information such as client ID, application title, version name, version code, and the application package name\.
+  `getIdentity()`: Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK\. It can be null\.
+ `getRemainingTimeInMillis()`: Remaining execution time till the function will be terminated, in milliseconds\. At the time you create the Lambda function you set maximum time limit, at which time AWS Lambda will terminate the function execution\. Information about the remaining time of function execution can be used to specify function behavior when nearing the timeout\.
+ `getLogger()`: Returns the Lambda logger associated with the Context object\. For more information, see [Logging \(Java\)](java-logging.md)\.

 The following Java code snippet shows a handler function that prints some of the context information\. 

```
public static void handler(InputStream inputStream, OutputStream outputStream, Context context) {
        
  ...
        System.out.println("Function name: " + context.getFunctionName());
        System.out.println("Max mem allocated: " + context.getMemoryLimitInMB());
        System.out.println("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
        System.out.println("CloudWatch log stream name: " + context.getLogStreamName());
        System.out.println("CloudWatch log group name: " + context.getLogGroupName());        
}
```

## Example: Using Context Object \(Java\)<a name="java-wt-context-object"></a>

The following Java code example shows how to use the `Context` object to retrieve runtime information of your Lambda function, while it is running\. 

```
package example;
import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.runtime.Context; 

public class Hello {
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
       
        // For fun, let us get function info using the context object.
        System.out.println("Function name: " + context.getFunctionName());
        System.out.println("Max mem allocated: " + context.getMemoryLimitInMB());
        System.out.println("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
        System.out.println("CloudWatch log stream name: " + context.getLogStreamName());
        System.out.println("CloudWatch log group name: " + context.getLogGroupName());
    }
}
```

You can do the following to test the code:
+ Using the preceding code, create a deployment package\.
+ Upload the deployment package to AWS Lambda to create your Lambda function\. You can do this using the console or AWS CLI\.
+ To test your Lambda function use the "Hello World" **Sample event** that the Lambda console provides\. 

  You can type any string and the function will return the same string in uppercase\. In addition, you will also get the useful function information provided by the `context` object\.

Follow the instructions provided in the Getting Started\. For more information, see [Create a Lambda Function Authored in Java](get-started-step4-optional.md)\. Note the following differences:
+ When you create a deployment package, don't forget the `aws-lambda-java-core` library dependency\.
+ When you create the Lambda function, specify `example.Hello::myHandler (package.class::method)` as the handler value\.