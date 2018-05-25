# Node\.js<a name="with-ddb-example-deployment-pkg-nodejs"></a>

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

## Next Step<a name="ddb-create-deployment-pkg-nodejs-next-step"></a>

 [Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 