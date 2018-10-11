# Step 2\.3: Create the Lambda Function and Test It Manually<a name="with-kinesis-example-upload-deployment-pkg"></a>

In this section, you do the following:
+ Create a Lambda function by uploading the deployment package\. 
+ Test the Lambda function by invoking it manually\. Instead of creating an event source, you use sample Kinesis event data\. 

In the next section, you create an Kinesis stream and test the end\-to\-end experience\.

## Step 2\.3\.1: Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-upload1"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. For more information on setting this up, see [Configuring the AWS CLI](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)\.

You need to update the command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `python3.6`, `nodejs8.10`, `dotnetcore2.1`, `go1.x`, or `java8`, depending on the language you used to author your code\.

```
$ aws lambda create-function \
--region region \
--function-name ProcessKinesisRecords  \
--zip-file fileb://file-path/ProcessKinesisRecords.zip \
--role execution-role-arn  \
--handler handler \
--runtime runtime-value \
--profile adminuser
```

The `--handler` parameter value for Java should be `example.ProcessKinesisRecords::recordHandler`\. For Node\.js, it should be `ProcessKinesisRecords.handler` and for Python it should be `ProcessKinesisRecords.lambda_handler`\.

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

**Note**  
You can create the Lambda function using the AWS Lambda console, in which case note the value of the `create-function` AWS CLI command parameters\. You provide the same values in the console UI\.

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

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
   ```
**Note**  
In this tutorial example, the message is saved in the `outputfile.txt` file\. If you request synchronous execution \(`RequestResponse` as the invocation type\), the function returns the string message in the response body\.   
For Node\.js, it could be one of the following \(whatever one you specify in the code\):  
`context.succeed("message")`  
`context.fail("message")`  
`context.done(null, "message)`  
For Python or Java, it is the message in the return statement:  
`return "message"`

## Next Step<a name="with-kinesis-example-upload-deployment-pkg-next-step"></a>

 [Step 3: Add an Event Source \(Create a Kinesis Stream and Associate It with Your Lambda Function\)](with-kinesis-example-configure-event-source.md) 