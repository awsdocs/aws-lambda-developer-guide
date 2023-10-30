# Sample Amazon SQS function code<a name="with-sqs-create-package"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js](#with-sqs-example-deployment-pkg-nodejs)
+ [Java](#with-sqs-example-deployment-pkg-java)
+ [C\#](#with-sqs-example-deployment-pkg-dotnet)
+ [Go](#with-sqs-example-deployment-pkg-go)
+ [Python](#with-sqs-example-deployment-pkg-python)

## Node\.js<a name="with-sqs-example-deployment-pkg-nodejs"></a>

The following is example code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

**Example index\.js \(Node\.js 12\)**  

```
exports.handler = async function(event, context) {
  event.Records.forEach(record => {
    const { body } = record;
    console.log(body);
  });
  return {};
}
```

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md)\.

## Java<a name="with-sqs-example-deployment-pkg-java"></a>

The following is example Java code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

In the code, `handleRequest` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `aws-lambda-java-events` library\.

**Example Handler\.java**  

```
package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

public class Handler implements RequestHandler<SQSEvent, Void>{
    @Override
    public Void handleRequest(SQSEvent event, Context context)
    {
        for(SQSMessage msg : event.getRecords()){
            System.out.println(new String(msg.getBody()));
        }
        return null;
    }
}
```

**Dependencies**
+ `aws-lambda-java-core`
+ `aws-lambda-java-events`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md)\.

## C\#<a name="with-sqs-example-deployment-pkg-dotnet"></a>

The following is example C\# code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to the console\. 

In the code, `HandleSQSEvent` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `AWS.Lambda.SQSEvents` library\. 

**Example ProcessingSQSRecords\.cs**  

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

Replace the `Program.cs` in a \.NET Core project with the above sample\. For instructions, see [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md)\.

## Go<a name="with-sqs-example-deployment-pkg-go"></a>

The following is example Go code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

 In the code, `handler` is the handler\. The handler uses the predefined `SQSEvent` class that is defined in the `aws-lambda-go-events` library\. 

**Example ProcessSQSRecords\.go**  

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

Build the executable with `go build` and create a deployment package\. For instructions, see [Deploy Go Lambda functions with \.zip file archives](golang-package.md)\.

## Python<a name="with-sqs-example-deployment-pkg-python"></a>

The following is example Python code that accepts an Amazon SQS record as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

**Example ProcessSQSRecords\.py**  

```
from __future__ import print_function

def lambda_handler(event, context):
    for record in event['Records']:
        print("test")
        payload = record["body"]
        print(str(payload))
```

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Python Lambda functions with \.zip file archives](python-package.md)\.
