# Go<a name="with-ddb-example-deployment-pkg-go"></a>

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
           for name, value := range record.DynamoDB.NewImage {
               if value.DataType() == events.DataTypeString {
                   fmt.Printf("Attribute name: %s, value: %s\n", name, value.String())
               }
           }
       }
   }
   ```

1. Save the file as ` ProcessDynamoDBStream.go`\.

1. Zip the ` ProcessDynamoDBStream.go` file as ` ProcessDynamoDBStream.zip`\. 

## Next Step<a name="ddb-create-deployment-pkg-go-next-step"></a>

 [Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 