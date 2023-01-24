# Sample function code<a name="with-sns-create-package"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js 12\.x](#with-sns-example-deployment-pkg-nodejs)
+ [Java 11](#with-sns-example-deployment-pkg-java)
+ [Go](#with-sns-example-deployment-pkg-go)
+ [Python 3](#with-sns-example-deployment-pkg-python)

## Node\.js 12\.x<a name="with-sns-example-deployment-pkg-nodejs"></a>

The following example processes messages from Amazon SNS, and logs their contents\.

**Example index\.js**  

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
// console.log('Received event:', JSON.stringify(event, null, 4));

    var message = event.Records[0].Sns.Message;
    console.log('Message received from SNS:', message);
    callback(null, "Success");
};
```

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md)\.

## Java 11<a name="with-sns-example-deployment-pkg-java"></a>

The following example processes messages from Amazon SNS, and logs their contents\.

**Example LogEvent\.java**  

```
package example;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

public class LogEvent implements RequestHandler<SNSEvent, Object> {
    public Object handleRequest(SNSEvent request, Context context){
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    context.getLogger().log("Invocation started: " + timeStamp);
    context.getLogger().log(request.getRecords().get(0).getSNS().getMessage());

    timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    context.getLogger().log("Invocation completed: " + timeStamp);
           return null;
     }
}
```

**Dependencies**
+ `aws-lambda-java-core`
+ `aws-lambda-java-events`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md)\.

## Go<a name="with-sns-example-deployment-pkg-go"></a>

The following example processes messages from Amazon SNS, and logs their contents\.

**Example lambda\_handler\.go**  

```
package main

import (
    "context"
    "fmt"
    "github.com/aws/aws-lambda-go/lambda"
    "github.com/aws/aws-lambda-go/events"
)

func handler(ctx context.Context, snsEvent events.SNSEvent) {
    for _, record := range snsEvent.Records {
        snsRecord := record.SNS
        fmt.Printf("[%s %s] Message = %s \n", record.EventSource, snsRecord.Timestamp, snsRecord.Message)
    }
}

func main() {
    lambda.Start(handler)
}
```

Build the executable with `go build` and create a deployment package\. For instructions, see [Deploy Go Lambda functions with \.zip file archives](golang-package.md)\.

## Python 3<a name="with-sns-example-deployment-pkg-python"></a>

The following example processes messages from Amazon SNS, and logs their contents\.

**Example lambda\_handler\.py**  

```
from __future__ import print_function
import json
print('Loading function')

def lambda_handler(event, context):
    #print("Received event: " + json.dumps(event, indent=2))
    message = event['Records'][0]['Sns']['Message']
    print("From SNS: " + message)
    return message
```

Zip up the sample code to create a deployment package\. For instructions, see [Deploy Python Lambda functions with \.zip file archives](python-package.md)\.