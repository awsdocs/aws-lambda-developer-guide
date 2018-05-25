# Test the Lambda Function \(Invoke Manually\)<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the function manually using sample Kinesis event data\. We recommend that you invoke the function using the console because the console UI provides a user\-friendly interface for reviewing the execution results, including the execution summary, logs written by your code, and the results returned by the function \(because the console always performs synchronous execution—invokes the Lambda function using the `RequestResponse` invocation type\)\. 

**To test the Lambda function \(console\)**

1. Follow the steps in the Getting Started to create and invoke the Lambda function at [Invoke the Lambda Function Manually and Verify Results, Logs, and Metrics](get-started-create-function.md#get-started-invoke-manually)\. For the sample event for testing, choose **Kinesis** in **Sample event template**\. 

1. Verify the results in the console\.

**To test the Lambda function \(AWS CLI\)**

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
       "Records": [
           {
               "kinesis": {
                   "partitionKey": "partitionKey-3",
                   "kinesisSchemaVersion": "1.0",
                   "data": "SGVsbG8sIHRoaXMgaXMgYSB0ZXN0IDEyMy4=",
                   "sequenceNumber": "49545115243490985018280067714973144582180062593244200961"
               },
               "eventSource": "aws:kinesis",
               "eventID": "shardId-000000000000:49545115243490985018280067714973144582180062593244200961",
               "invokeIdentityArn": "arn:aws:iam::account-id:role/testLEBRole",
               "eventVersion": "1.0",
               "eventName": "aws:kinesis:record",
               "eventSourceARN": "arn:aws:kinesis:us-west-2:35667example:stream/examplestream",
               "awsRegion": "us-west-2"
           }
       ]
   }
   ```

1. Execute the following invoke command:

   ```
   $ aws lambda  invoke \
   --invocation-type Event \
   --function-name ProcessKinesisRecords \
   --region region \
   --payload file://file-path/input.txt \
   --profile adminuser 
   outputfile.txt
   ```
**Note**  
In this tutorial example, the message is saved in the `outputfile.txt` file\. If you request synchronous execution \(`RequestResponse` as the invocation type\), the function returns the string message in the response body\.   
For Node\.js, it could be one of the following \(whatever one you specify in the code\):  
`context.succeed("message")`  
`context.fail("message")`  
`context.done(null, "message)`  
For Python or Java, it is the message in the return statement:  
`return "message"`