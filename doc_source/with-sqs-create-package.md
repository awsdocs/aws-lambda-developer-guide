# Step 2\.1: Create a Lambda Function Deployment Package<a name="with-sqs-create-package"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Java<a name="with-sqs-example-deployment-pkg-java"></a>

The following is example Java code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `handleRequest` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `aws-lambda-java-events` library\. 

```
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSEventRecord;

        public class ProcessSQSEvents implements RequestHandler<SQSEvent, Void>{
            @Override
            public Void handleRequest(SQSEvent event, Context context)
            {
                for(SQSEventRecord rec : event.getRecords()) {
                    System.out.println(new String(rec.getSQS().getBody());
                }
                return null;
            }
        }
```

If the handler returns normally without exceptions, Lambda considers the message processed successfully and begins reading new messages in the queue\. Once a message is processed successfully, it is automatically deleted from the queue\. If the handler throws an exception, Lambda considers the input of messages as not processed and invokes the function with the same batch of messages\. 

Using the preceding code \(in a file named `ProcessSQSMessage.java`\), create a deployment package\. Make sure that you add the following dependencies: 
+ `aws-lambda-java-core`
+ `aws-lambda-java-events` 

For more information, see [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)\.

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

NOTE TO DOC BASH REVIEWERS: OTHER SUPPORTED RUNTIME SAMPLES ARE FORTHCOMING

### Next Step<a name="sqs-create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sqs-create-execution-role.md) 