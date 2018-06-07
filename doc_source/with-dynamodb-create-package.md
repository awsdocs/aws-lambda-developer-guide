# Step 2\.1: Create a Lambda Function Deployment Package<a name="with-dynamodb-create-package"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-ddb-example-deployment-pkg-nodejs"></a>

1. Open a text editor, and then copy the following code\. 

   ```
   console.log('Loading function');
   
   exports.lambda_handler = function(event, context, callback) {
       console.log(JSON.stringify(event, null, 2));
       event.Records.forEach(function(record) {
           console.log(record.eventID);
           console.log(record.eventName);
           console.log('DynamoDB Record: %j', record.dynamodb);
       });
       callback(null, "message");
   };
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes v6\.10 or 8\.10\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as ` ProcessDynamoDBStream.js`\.

1. Zip the ` ProcessDynamoDBStream.js` file as ` ProcessDynamoDBStream.zip`\. 

### Next Step<a name="ddb-create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 

## Java<a name="with-ddb-example-deployment-pkg-java"></a>

In the following code, `handleRequest` is the handler that AWS Lambda invokes and provides event data\. The handler uses the predefined `DynamodbEvent` class, which is defined in the `aws-lambda-java-events` library\. 

```
package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

public class DDBEventProcessor implements
        RequestHandler<DynamodbEvent, String> {
    
    public String handleRequest(DynamodbEvent ddbEvent, Context context) {       
        for (DynamodbStreamRecord record : ddbEvent.getRecords()){
           System.out.println(record.getEventID());
           System.out.println(record.getEventName());
           System.out.println(record.getDynamodb().toString());
           
        }
        return "Successfully processed " + ddbEvent.getRecords().size() + " records.";
    }
}
```

If the handler returns normally without exceptions, Lambda considers the input batch of records as processed successfully and begins reading new records in the stream\. If the handler throws an exception, Lambda considers the input batch of records as not processed and invokes the function with the same batch of records again\. 

Using the preceding code \(in a file named `DDBEventProcessor.java`\), create a deployment package\. Make sure that you add the following dependencies: 
+ `aws-lambda-java-core`
+ `aws-lambda-java-events` 

For more information, see [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)\.

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="ddb-create-deployment-pkg-java-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 

## C\#<a name="with-ddb-example-deployment-pkg-dotnet"></a>

In the following code, `ProcessDynamoEvent` is the handler that AWS Lambda invokes and provides event data\. The handler uses the predefined `DynamoDbEvent` class, which is defined in the `Amazon.Lambda.DynamoDBEvents` library\. 

```
using System;
using System.IO;
using System.Text;
using Amazon.Lambda.Core;
using Amazon.Lambda.DynamoDBEvents;
 
using Amazon.Lambda.Serialization.Json;
 
namespace DynamoDBStreams
{
    public class DdbSample
    {
        private static readonly JsonSerializer _jsonSerializer = new JsonSerializer();
 
        public void ProcessDynamoEvent(DynamoDBEvent dynamoEvent)
        {
            Console.WriteLine($"Beginning to process {dynamoEvent.Records.Count} records...");
 
            foreach (var record in dynamoEvent.Records)
            {
                Console.WriteLine($"Event ID: {record.EventID}");
                Console.WriteLine($"Event Name: {record.EventName}");
 
                string streamRecordJson = SerializeObject(record.Dynamodb);
                Console.WriteLine($"DynamoDB Record:");
                Console.WriteLine(streamRecordJson);
            }
 
            Console.WriteLine("Stream processing complete.");
        }
 
        private string SerializeObject(object streamRecord)
        {
            using (var ms = new MemoryStream())
            {
                _jsonSerializer.Serialize(streamRecord, ms);
                return Encoding.UTF8.GetString(ms.ToArray());
            }
        }
    }
}
```

To create a deployment package, follow the steps outlined in [\.NET Core CLI](lambda-dotnet-coreclr-deployment-package.md)\. In doing so, note the following after you've created your \.NET project: 
+ Rename the default *Program\.cs file* with a file name of your choice, such as *ProcessingDynamoDBStreams\.cs*\. 
+ Replace the default contents of the renamed *Program\.cs* file with the code example above\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="create-deployment-pkg-dotnet-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 

## Python<a name="with-ddb-example-deployment-pkg-python"></a>

1. Open a text editor, and then copy the following code\. 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If you are using runtime version 3\.6, it is not necessary to include it\.

   ```
   from __future__ import print_function
   
   def lambda_handler(event, context):
       for record in event['Records']:
           print(record['eventID'])
           print(record['eventName'])       
       print('Successfully processed %s records.' % str(len(event['Records'])))
   ```

1. Save the file as ` ProcessDynamoDBStream.py`\.

1. Zip the ` ProcessDynamoDBStream.py` file as ` ProcessDynamoDBStream.zip`\. 

### Next Step<a name="ddb-create-deployment-pkg-python-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 

## Go<a name="with-ddb-example-deployment-pkg-go"></a>

1. Open a text editor, and then copy the following code\. 

   ```
   import (
       "strings"
   
       "github.com/aws/aws-lambda-go/events"
   )
   
   func handleRequest(ctx context.Context, e events.DynamoDBEvent) {
   
       for _, record := range e.Records {
           fmt.Printf("Processing request data for event ID %s, type %s.\n", record.EventID, record.EventName)
   
           // Print new values for attributes of type String
           for name, value := range record.Change.NewImage {
               if value.DataType() == events.DataTypeString {
                   fmt.Printf("Attribute name: %s, value: %s\n", name, value.String())
               }
           }
       }
   }
   ```

1. Save the file as ` ProcessDynamoDBStream.go`\.

1. Zip the ` ProcessDynamoDBStream.go` file as ` ProcessDynamoDBStream.zip`\. 

### Next Step<a name="ddb-create-deployment-pkg-go-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 
