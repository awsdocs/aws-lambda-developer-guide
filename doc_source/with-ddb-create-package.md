# Sample function code<a name="with-ddb-create-package"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js](#with-ddb-example-deployment-pkg-nodejs)
+ [Java 11](#with-ddb-example-deployment-pkg-java)
+ [C\#](#with-ddb-example-deployment-pkg-dotnet)
+ [Python 3](#with-ddb-example-deployment-pkg-python)
+ [Go](#with-ddb-example-deployment-pkg-go)

## Node\.js<a name="with-ddb-example-deployment-pkg-nodejs"></a>

The following example processes messages from DynamoDB, and logs their contents\.

**Example ProcessDynamoDBStream\.js**  

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

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md)\.

## Java 11<a name="with-ddb-example-deployment-pkg-java"></a>

The following example processes messages from DynamoDB, and logs their contents\. `handleRequest` is the handler that AWS Lambda invokes and provides event data\. The handler uses the predefined `DynamodbEvent` class, which is defined in the `aws-lambda-java-events` library\.

**Example DDBEventProcessor\.java**  

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

**Dependencies**
+ `aws-lambda-java-core`
+ `aws-lambda-java-events`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md)\.

## C\#<a name="with-ddb-example-deployment-pkg-dotnet"></a>

The following example processes messages from DynamoDB, and logs their contents\. `ProcessDynamoEvent` is the handler that AWS Lambda invokes and provides event data\. The handler uses the predefined `DynamoDbEvent` class, which is defined in the `Amazon.Lambda.DynamoDBEvents` library\.

**Example ProcessingDynamoDBStreams\.cs**  

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

Replace the `Program.cs` in a \.NET Core project with the above sample\. For instructions, see [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md)\.

## Python 3<a name="with-ddb-example-deployment-pkg-python"></a>

The following example processes messages from DynamoDB, and logs their contents\.

**Example ProcessDynamoDBStream\.py**  

```
from __future__ import print_function

def lambda_handler(event, context):
    for record in event['Records']:
        print(record['eventID'])
        print(record['eventName'])
    print('Successfully processed %s records.' % str(len(event['Records'])))
```

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Python Lambda functions with \.zip file archives](python-package.md)\.

## Go<a name="with-ddb-example-deployment-pkg-go"></a>

The following example processes messages from DynamoDB, and logs their contents\.

**Example**  

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

Build the executable with `go build` and create a deployment package\. For instructions, see [Deploy Go Lambda functions with \.zip file archives](golang-package.md)\.