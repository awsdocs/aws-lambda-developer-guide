# Sample Function Code<a name="with-kinesis-create-package"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js 8](#with-kinesis-example-deployment-pkg-nodejs)
+ [Java 8](#with-kinesis-example-deployment-pkg-java)
+ [C\#](#with-kinesis-example-deployment-pkg-dotnet)
+ [Python 3](#with-kinesis-example-deployment-pkg-python)
+ [Go](#with-kinesis-example-deployment-pkg-go)

## Node\.js 8<a name="with-kinesis-example-deployment-pkg-nodejs"></a>

The following example code receives a Kinesis event input and processes the messages that it contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

**Example index\.js**  

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

Zip up the sample code to create a deployment package\. For instructions, see [Creating a Deployment Package \(Node\.js\)](nodejs-create-deployment-pkg.md)\.

## Java 8<a name="with-kinesis-example-deployment-pkg-java"></a>

The following is example Java code that receives Kinesis event record data as a input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

In the code, `recordHandler` is the handler\. The handler uses the predefined `KinesisEvent` class that is defined in the `aws-lambda-java-events` library\.

**Example ProcessKinesisEvents\.java**  

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

**Dependencies**
+ `aws-lambda-java-core`
+ `aws-lambda-java-events`
+ `aws-java-sdk-s3`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [Creating a Deployment Package \(Java\)](lambda-java-how-to-create-deployment-package.md)\.

## C\#<a name="with-kinesis-example-deployment-pkg-dotnet"></a>

The following is example C\# code that receives Kinesis event record data as a input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `HandleKinesisRecord` is the handler\. The handler uses the predefined `KinesisEvent` class that is defined in the `Amazon.Lambda.KinesisEvents` library\. 

**Example ProcessingKinesisEvents\.cs**  

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
Replace the `Program.cs` in a \.NET Core project with the above sample\. For instructions, see [\.NET Core CLI](lambda-dotnet-coreclr-deployment-package.md)\.

## Python 3<a name="with-kinesis-example-deployment-pkg-python"></a>

 The following is example Python code that receives Kinesis event record data as input and processes it\. For illustration, the code writes to some of the incoming event data to CloudWatch Logs\.

**Example ProcessKinesisRecords\.py**  

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

Zip up the sample code to create a deployment package\. For instructions, see [Creating a Deployment Package \(Python\)](lambda-python-how-to-create-deployment-package.md)\.

## Go<a name="with-kinesis-example-deployment-pkg-go"></a>

 The following is example Go code that receives Kinesis event record data as input and processes it\. For illustration, the code writes to some of the incoming event data to CloudWatch Logs\. 

**Example ProcessKinesisRecords\.go**  

```
import (
    "strings"
    "github.com/aws/aws-lambda-go/events"
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

Build the executable with `go build` and create a deployment package\. For instructions, see [Creating a Deployment Package \(Go\)](lambda-go-how-to-create-deployment-package.md)\.