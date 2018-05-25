# Step 2\.1: Create a Lambda Function Deployment Package<a name="with-sns-create-package"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-sns-example-deployment-pkg-nodejs"></a>

1. Open a text editor, and then copy the following code\. 

   ```
   console.log('Loading function');
    
   exports.handler = function(event, context, callback) {
   // console.log('Received event:', JSON.stringify(event, null, 4));
    
       var message = event.Records[0].Sns.Message;
       console.log('Message received from SNS:', message); 
       callback(null, "Success");
   };
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes v6\.10 or v8\.10\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as ` index.js`\.

1. Zip the ` index.js` file as ` LambdaWithSNS.zip`\. 

### Next Step<a name="sns-create-deployment-pkg-nodejs-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sns-example-create-iam-role.md) 

## Java<a name="with-sns-example-deployment-pkg-java"></a>

Open a text editor, and then copy the following code\. 

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

Using the preceding code \(in a file named `LambdaWithSNS.java`\), create a deployment package\. Make sure that you add the following dependencies: 
+ `aws-lambda-java-core`
+ `aws-lambda-java-events` 

For more information, see [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)\.

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="sns-create-deployment-pkg-java-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sns-example-create-iam-role.md) 

## Go<a name="with-sns-example-deployment-pkg-go"></a>

1. Open a text editor, and then copy the following code\. 

   ```
   import (
       "strings"
       "github.com/aws/aws-lambda-go/events”
   )
   
   func handler(ctx context.Context, snsEvent events.SNSEvent) {
       for _, record := range snsEvent.Records {
           snsRecord := record.SNS
   
           fmt.Printf("[%s %s] Message = %s \n", record.EventSource, snsRecord.Timestamp, snsRecord.Message) 
       }
   }
   ```

1. Save the file as ` lambda_handler.go`\.

1. Zip the ` lambda_handler.go` file as ` LambdaWithSNS.zip`\. 

### Next Step<a name="sns-create-deployment-pkg-python-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sns-example-create-iam-role.md) 

## Python<a name="with-sns-example-deployment-pkg-python"></a>

1. Open a text editor, and then copy the following code\. 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If you are using runtime version 3\.6, it is not necessary to include it\.

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

1. Save the file as ` lambda_handler.py`\.

1. Zip the ` lambda_handler.py` file as ` LambdaWithSNS.zip`\. 

### Next Step<a name="sns-create-deployment-pkg-python-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-sns-example-create-iam-role.md) 