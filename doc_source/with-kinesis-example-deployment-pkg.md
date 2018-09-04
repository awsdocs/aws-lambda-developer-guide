# Step 2\.1: Create a Deployment Package<a name="with-kinesis-example-deployment-pkg"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-kinesis-example-deployment-pkg-nodejs"></a>

The following is example Node\.js code that receives Kinesis event records as input and processes them\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

Follow the instructions to create an AWS Lambda function deployment package\. 

1. Open a text editor, and then copy the following code\. 

   ```
   console.log('Loading function');
   
   exports.handler = function(event, context) {
       //console.log(JSON.stringify(event, null, 2));
       event.Records.forEach(function(record) {
           // Kinesis data is base64 encoded so decode here
           var payload = new Buffer(record.kinesis.data, 'base64').toString('ascii');
           console.log('Decoded payload:', payload);
       });
       
   };
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes v8\.10 or v6\.10 For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as `ProcessKinesisRecords.js`\. 

1. Zip the `ProcessKinesisRecords.js` file as `ProcessKinesisRecords.zip`\. 

### Next Step<a name="create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-kinesis-example-create-iam-role.md) 

## Java<a name="with-kinesis-example-deployment-pkg-java"></a>

The following is example Java code that receives Kinesis event record data as a input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `recordHandler` is the handler\. The handler uses the predefined `KinesisEvent` class that is defined in the `aws-lambda-java-events` library\. 

```
package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;

public class ProcessKinesisRecords implements RequestHandler<KinesisEvent, Void>{
@Override
public Void recordHandler(KinesisEvent event, Context context)
{
for(KinesisEventRecord rec : event.getRecords()) {
System.out.println(new String(rec.getKinesis().getData().array()));
}
return null;
}
}
```

If the handler returns normally without exceptions, Lambda considers the input batch of records as processed successfully and begins reading new records in the stream\. If the handler throws an exception, Lambda considers the input batch of records as not processed and invokes the function with the same batch of records again\. 

Using the preceding code \(in a file named `ProcessKinesisEvents.java`\), create a deployment package\. Make sure that you add the following dependencies: 
+ `aws-lambda-java-core`
+ `aws-lambda-java-events` 

For more information, see [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)\.

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="create-deployment-pkg-java-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-kinesis-example-create-iam-role.md) 

## C\#<a name="with-kinesis-example-deployment-pkg-dotnet"></a>

The following is example C\# code that receives Kinesis event record data as a input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `HandleKinesisRecord` is the handler\. The handler uses the predefined `KinesisEvent` class that is defined in the `Amazon.Lambda.KinesisEvents` library\. 

```
using System;
using System.IO;
using System.Text;
 
using Amazon.Lambda.Core;
using Amazon.Lambda.KinesisEvents;
 
namespace KinesisStreams
{
    public class KinesisSample
    {
    	[LambdaSerializer(typeof(JsonSerializer))]
        public void HandleKinesisRecord(KinesisEvent kinesisEvent)
        {
            Console.WriteLine($"Beginning to process {kinesisEvent.Records.Count} records...");
 
            foreach (var record in kinesisEvent.Records)
            {
                Console.WriteLine($"Event ID: {record.EventId}");
                Console.WriteLine($"Event Name: {record.EventName}");
 
                string recordData = GetRecordContents(record.Kinesis);
                Console.WriteLine($"Record Data:");
                Console.WriteLine(recordData);
            }
 
            Console.WriteLine("Stream processing complete.");
        }
 
        private string GetRecordContents(KinesisEvent.Record streamRecord)
        {
            using (var reader = new StreamReader(streamRecord.Data, Encoding.ASCII))
            {
                return reader.ReadToEnd();
            }
        }
    }
}
```

To create a deployment package, follow the steps outlined in [\.NET Core CLI](lambda-dotnet-coreclr-deployment-package.md)\. In doing so, note the following after you've created your \.NET project: 
+ Rename the default *Program\.cs file* with a file name of your choice, such as *ProcessingKinesisEvents\.cs*\. 
+ Replace the default contents of the renamed *Program\.cs* file with the code example above\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="create-deployment-pkg-dotnet-next-step1"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-kinesis-example-create-iam-role.md) 

## Python<a name="with-kinesis-example-deployment-pkg-python"></a>

 The following is example Python code that receives Kinesis event record data as input and processes it\. For illustration, the code writes to some of the incoming event data to CloudWatch Logs\. 

Follow the instructions to create a AWS Lambda function deployment package\. 

1.  Open a text editor, and then copy the following code\. 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If you are using runtime version 3\.6, is not necessary to include it\.

   ```
   from __future__ import print_function
   #import json
   import base64
   def lambda_handler(event, context):
       for record in event['Records']:
          #Kinesis data is base64 encoded so decode here
          payload=base64.b64decode(record["kinesis"]["data"])
          print("Decoded payload: " + str(payload))
   ```

1. Save the file as `ProcessKinesisRecords.py`\. 

1.  Zip the `ProcessKinesisRecords.py` file as `ProcessKinesisRecords.zip`\. 

### Next Step<a name="create-deployment-pkg-python-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-kinesis-example-create-iam-role.md) 

## Go<a name="with-kinesis-example-deployment-pkg-go"></a>

 The following is example Go code that receives Kinesis event record data as input and processes it\. For illustration, the code writes to some of the incoming event data to CloudWatch Logs\. 

Follow the instructions to create a AWS Lambda function deployment package\. 

1.  Open a text editor, and then copy the following code\. 

   ```
   import (
       "strings"
       "github.com/aws/aws-lambda-go/events”
   )
   
   func handler(ctx context.Context, kinesisEvent events.KinesisEvent) {
       for _, record := range kinesisEvent.Records {
           kinesisRecord := record.Kinesis
           dataBytes := kinesisRecord.Data
           dataText := string(dataBytes)
   
           fmt.Printf("%s Data = %s \n", record.EventName, dataText) 
       }
   }
   ```

1. Save the file as `ProcessKinesisRecords.go`\. 

1. Using the preceding code, create a deployment package\. For instructions, see [Creating a Deployment Package \(Go\)](lambda-go-how-to-create-deployment-package.md)\.

### Next Step<a name="create-deployment-pkg-go-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-kinesis-example-create-iam-role.md) 