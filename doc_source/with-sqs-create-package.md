# Step 2\.1: Create a Lambda Function Deployment Package<a name="with-sqs-create-package"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-sqs-example-deployment-pkg-nodejs"></a>

The following is example code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

```
exports.handler = async function(event, context) {
  event.Records.forEach(record => {
    const { body } = record;
    console.log(body);
  });
  return {};
}
```

**Note**  
The previous code sample is compliant with Node\.js v8\.10\. 

To use Node\.js v6\.10, using the following code:

```
event.Records.forEach(function(record) {
    var body = record.body;
    console.log(body);
  });
  callback(null, "message");
};
```

For more information, see [Programming Model\(Node\.js\)](programming-model.md)

 Save the file as ProcessSQSRecords\.js and then zip `ProcessSQSRecords.js` file as ProcessSQSRecords\.zip\. 

### Next Step<a name="sqs-create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sqs-create-execution-role.md) 

## Java<a name="with-sqs-example-deployment-pkg-java"></a>

The following is example Java code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `handleRequest` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `aws-lambda-java-events` library\. 

```
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

        public class ProcessSQSEvents implements RequestHandler<SQSEvent, Void>{
            @Override
            public Void handleRequest(SQSEvent event, Context context)
            {
                for(SQSMessage msg : event.getRecords()){
                    System.out.println(new String(msg.getSQS().getBody()));
                }
                return null;
            }
        }
```

If the handler returns normally without exceptions, Lambda considers the message processed successfully and begins reading new messages in the queue\. Once a message is processed successfully, it is automatically deleted from the queue\. If the handler throws an exception, Lambda considers the input of messages as not processed and invokes the function with the same batch of messages\. 

Using the preceding code \(in a file named `ProcessSQSRecord.java`\), create a deployment package\. Make sure that you add the following dependencies: 
+ `aws-lambda-java-core`
+ `aws-lambda-java-events` 

For more information, see [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)\.

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="sqs-create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sqs-create-execution-role.md) 

## C\#<a name="with-sqs-example-deployment-pkg-dotnet"></a>

The following is example C\# code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to the console\. 

 In the code, `handleRequest` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `AWS.Lambda.SQSEvents` library\. 

```
[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace SQSLambdaFunction
{
    public class SQSLambdaFunction
    {
        public string HandleSQSEvent(SQSEvent sqsEvent, ILambdaContext context)
        {
            Console.WriteLine($"Beginning to process {sqsEvent.Records.Count} records...");

            foreach (var record in sqsEvent.Records)
            {
                Console.WriteLine($"Message ID: {record.MessageId}");
                Console.WriteLine($"Event Source: {record.EventSource}");

                Console.WriteLine($"Record Body:");
                Console.WriteLine(record.Body);
            }

            Console.WriteLine("Processing complete.");

            return $"Processed {sqsEvent.Records.Count} records.";
        }
    }
}
```

To create a deployment package, follow the steps outlined in [\.NET Core CLI](lambda-dotnet-coreclr-deployment-package.md)\. In doing so, note the following after you've created your \.NET project: 
+ Rename the default `Program.cs` file with a file name of your choice, such as `ProcessingSQSRecords.cs`\. 
+ Replace the default contents of the renamed *Program\.cs* file with the code example above\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="sqs-create-deployment-pkg-dotnet-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sqs-create-execution-role.md) 

## Go<a name="with-sqs-example-deployment-pkg-go"></a>

The following is example Go code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `handler` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `aws-lambda-go-events` library\. 

```
package main

import (
    "context"
    "fmt"

    "github.com/aws/aws-lambda-go/events"
    "github.com/aws/aws-lambda-go/lambda"
)

func handler(ctx context.Context, sqsEvent events.SQSEvent) error {
    for _, message := range sqsEvent.Records {
        fmt.Printf("The message %s for event source %s = %s \n", message.MessageId, message.EventSource, message.Body)
    }

    return nil
}

func main() {
    lambda.Start(handler)
}
```

If the handler returns normally without error, Lambda considers the message processed successfully and begins reading new messages in the queue\. Once a message is processed successfully, it is automatically deleted from the queue\. If the handler throws an exception, Lambda considers the input of messages as not processed and invokes the function with the same batch of messages\. 

Copy and save the preceding code into a file called ProcessSQSRecords\.go\. Your deployment package is a zip file comprised of a Go executable\. For instructions on how to create one, see [Creating a Deployment Package \(Go\)](lambda-go-how-to-create-deployment-package.md)\.

For more information, see [Programming Model for Authoring Lambda Functions in Go](go-programming-model.md)\.

### Next Step<a name="sqs-create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sqs-create-execution-role.md) 

## Python<a name="with-sqs-example-deployment-pkg-python"></a>

 The following is example Python code that accepts an Amazon SQS record as input and processes it\. For illustration, the code writes to some of the incoming event data to CloudWatch Logs\. 

Follow the instructions to create a AWS Lambda function deployment package\. 

1.  Open a text editor, and then copy the following code\. 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If you are using runtime version 3\.6, is not necessary to include it\.

   ```
   from __future__ import print_function
   
   def lambda_handler(event, context):
       for record in event['Records']:
          print ("test")
          payload=record["body"]
          print(str(payload))
   ```

1. Save the file as `ProcessSQSRecords.py`\. 

1.  Zip the `ProcessSQSRecords.py` file as `ProcessSQSRecords.zip`\. 

### Next Step<a name="create-deployment-pkg-python-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sqs-create-execution-role.md) 