# Step 2\.1: Create a Deployment Package<a name="with-on-demand-https-example-deployment-pkg"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-kinesis-example-deployment-pkg-nodejs1"></a>

Follow the instructions to create an AWS Lambda function deployment package\. 

1. Open a text editor, and then copy the following code\. 

   ```
   console.log('Loading function');
   
   var AWS = require('aws-sdk');
   var dynamo = new AWS.DynamoDB.DocumentClient();
   
   /**
    * Provide an event that contains the following keys:
    *
    *   - operation: one of the operations in the switch statement below
    *   - tableName: required for operations that interact with DynamoDB
    *   - payload: a parameter to pass to the operation being performed
    */
   exports.handler = function(event, context, callback) {
       //console.log('Received event:', JSON.stringify(event, null, 2));
   
       var operation = event.operation;
   
       if (event.tableName) {
           event.payload.TableName = event.tableName;
       }
   
       switch (operation) {
           case 'create':
               dynamo.put(event.payload, callback);
               break;
           case 'read':
               dynamo.get(event.payload, callback);
               break;
           case 'update':
               dynamo.update(event.payload, callback);
               break;
           case 'delete':
               dynamo.delete(event.payload, callback);
               break;
           case 'list':
               dynamo.scan(event.payload, callback);
               break;
           case 'echo':
               callback(null, "Success");
               break;
           case 'ping':
               callback(null, "pong");
               break;
           default:
               callback('Unknown operation: ${operation}');
       }
   };
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes v6\.10 or v4\.3\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as `LambdaFunctionOverHttps.js`\. 

1. Zip the `LambdaFunctionOverHttps.js` file as `LambdaFunctionOverHttps.zip`\. 

### Next Step<a name="create-deployment-pkg-nodejs-next-step1"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-on-demand-https-example-create-iam-role.md) 

## Python<a name="with-kinesis-example-deployment-pkg-python1"></a>

 Follow the instructions to create an AWS Lambda function deployment package\. 

1.  Open a text editor, and then copy the following code\. 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If are you using runtime version 3\.6, it is not necessary to include it\.

   ```
   from __future__ import print_function
   
   import boto3
   import json
   
   print('Loading function')
   
   
   def handler(event, context):
       '''Provide an event that contains the following keys:
   
         - operation: one of the operations in the operations dict below
         - tableName: required for operations that interact with DynamoDB
         - payload: a parameter to pass to the operation being performed
       '''
       #print("Received event: " + json.dumps(event, indent=2))
   
       operation = event['operation']
   
       if 'tableName' in event:
           dynamo = boto3.resource('dynamodb').Table(event['tableName'])
   
       operations = {
           'create': lambda x: dynamo.put_item(**x),
           'read': lambda x: dynamo.get_item(**x),
           'update': lambda x: dynamo.update_item(**x),
           'delete': lambda x: dynamo.delete_item(**x),
           'list': lambda x: dynamo.scan(**x),
           'echo': lambda x: x,
           'ping': lambda x: 'pong'
       }
   
       if operation in operations:
           return operations[operation](event.get('payload'))
       else:
           raise ValueError('Unrecognized operation "{}"'.format(operation))
   ```

1. Save the file as `LambdaFunctionOverHttps.py`\. 

1.  Zip the `LambdaFunctionOverHttps.py` file as `LambdaFunctionOverHttps.zip`\. 

### Next Step<a name="create-deployment-pkg-python-next-step1"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-on-demand-https-example-create-iam-role.md) 

## Go<a name="with-kinesis-example-deployment-pkg-go"></a>

 Follow the instructions to create an AWS Lambda function deployment package\. 

1.  Open a text editor, and then copy the following code\. 

   ```
   import (
       "strings"
       "github.com/aws/aws-lambda-go/events"
   )
   
   func handleRequest(ctx context.Context, request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
       fmt.Printf("Processing request data for request %s.\n", request.RequestContext.RequestId)
       fmt.Printf("Body size = %d.\n", len(request.Body))
   
       fmt.Println("Headers:")
       for key, value := range request.Headers {
           fmt.Printf("    %s: %s\n", key, value)
       }
   
       return events.APIGatewayProxyResponse { Body: request.Body, StatusCode: 200 }, nil
   }
   ```

1. Save the file as `LambdaFunctionOverHttps.go`\. 

1.  Your deployment package is a zip file comprised of a Go executable\. For instructions on how to create one, see [Creating a Deployment Package \(Go\)](lambda-go-how-to-create-deployment-package.md) 

### Next Step<a name="create-deployment-pkg-go-next-step1"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-on-demand-https-example-create-iam-role.md) 