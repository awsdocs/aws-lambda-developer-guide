# C\#<a name="with-ddb-example-deployment-pkg-dotnet"></a>

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

## Next Step<a name="create-deployment-pkg-dotnet-next-step"></a>

 [Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 