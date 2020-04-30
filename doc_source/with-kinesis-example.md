# Tutorial: Using AWS Lambda with Amazon Kinesis<a name="with-kinesis-example"></a>

In this tutorial, you create a Lambda function to consume events from a Kinesis stream\. The following diagram illustrates the application flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/kinesis-pull-10.png)

1. Custom app writes records to the stream\.

1. AWS Lambda polls the stream and, when it detects new records in the stream, invokes your Lambda function\.

1. AWS Lambda executes the Lambda function by assuming the execution role you specified at the time you created the Lambda function\.

## Prerequisites<a name="with-kinesis-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting started with AWS Lambda](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Create the execution role<a name="with-kinesis-example-create-iam-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaKinesisExecutionRole**\.
   + **Role name** – **lambda\-kinesis\-role**\.

The **AWSLambdaKinesisExecutionRole** policy has the permissions that the function needs to read items from Kinesis and write logs to CloudWatch Logs\.

## Create the function<a name="with-kinesis-example-create-function"></a>

The following example code receives a Kinesis event input and processes the messages that it contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

**Note**  
For sample code in other languages, see [Sample function code](with-kinesis-create-package.md)\.

**Example index\.js**  

```
console.log('Loading function');

exports.handler = function(event, context) {
    //console.log(JSON.stringify(event, null, 2));
    event.Records.forEach(function(record) {
        // Kinesis data is base64 encoded so decode here
        var payload = Buffer.from(record.kinesis.data, 'base64').toString('ascii');
        console.log('Decoded payload:', payload);
    });
};
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   $ zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   $ aws lambda create-function --function-name ProcessKinesisRecords \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
   --role arn:aws:iam::123456789012:role/lambda-kinesis-role
   ```

## Test the Lambda function<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke your Lambda function manually using the `invoke` AWS Lambda CLI command and a sample Kinesis event\.

**To test the Lambda function**

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
       "Records": [
           {
               "kinesis": {
                   "kinesisSchemaVersion": "1.0",
                   "partitionKey": "1",
                   "sequenceNumber": "49590338271490256608559692538361571095921575989136588898",
                   "data": "SGVsbG8sIHRoaXMgaXMgYSB0ZXN0Lg==",
                   "approximateArrivalTimestamp": 1545084650.987
               },
               "eventSource": "aws:kinesis",
               "eventVersion": "1.0",
               "eventID": "shardId-000000000006:49590338271490256608559692538361571095921575989136588898",
               "eventName": "aws:kinesis:record",
               "invokeIdentityArn": "arn:aws:iam::123456789012:role/lambda-kinesis-role",
               "awsRegion": "us-east-2",
               "eventSourceARN": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"
           }
       ]
   }
   ```

1. Use the `invoke` command to send the event to the function\.

   ```
   $ aws lambda invoke --function-name ProcessKinesisRecords --payload file://input.txt out.txt
   ```

   The response is saved to `out.txt`\.

## Create a Kinesis stream<a name="with-kinesis-example-configure-event-source-create"></a>

Use the `create-stream ` command to create a stream\.

```
$ aws kinesis create-stream --stream-name lambda-stream --shard-count 1
```

Run the following `describe-stream` command to get the stream ARN\.

```
$ aws kinesis describe-stream --stream-name lambda-stream
{
    "StreamDescription": {
        "Shards": [
            {
                "ShardId": "shardId-000000000000",
                "HashKeyRange": {
                    "StartingHashKey": "0",
                    "EndingHashKey": "340282366920746074317682119384634633455"
                },
                "SequenceNumberRange": {
                    "StartingSequenceNumber": "49591073947768692513481539594623130411957558361251844610"
                }
            }
        ],
        "StreamARN": "arn:aws:kinesis:us-west-2:123456789012:stream/lambda-stream",
        "StreamName": "lambda-stream",
        "StreamStatus": "ACTIVE",
        "RetentionPeriodHours": 24,
        "EnhancedMonitoring": [
            {
                "ShardLevelMetrics": []
            }
        ],
        "EncryptionType": "NONE",
        "KeyId": null,
        "StreamCreationTimestamp": 1544828156.0
    }
}
```

You use the stream ARN in the next step to associate the stream with your Lambda function\.

## Add an event source in AWS Lambda<a name="with-kinesis-example-configure-event-source-add-event-source"></a>

Run the following AWS CLI `add-event-source` command\.

```
$ aws lambda create-event-source-mapping --function-name ProcessKinesisRecords \
--event-source  arn:aws:kinesis:us-west-2:123456789012:stream/lambda-stream \
--batch-size 100 --starting-position LATEST
```

Note the mapping ID for later use\. You can get a list of event source mappings by running the `list-event-source-mappings` command\.

```
$ aws lambda list-event-source-mappings --function-name ProcessKinesisRecords \
--event-source arn:aws:kinesis:us-west-2:123456789012:stream/lambda-stream
```

In the response, you can verify the status value is `enabled`\. Event source mappings can be disabled to pause polling temporarily without losing any records\.

## Test the setup<a name="with-kinesis-example-configure-event-source-test-end-to-end"></a>

To test the event source mapping, add event records to your Kinesis stream\. The `--data` value is a string that the CLI encodes to base64 prior to sending it to Kinesis\. You can run the same command more than once to add multiple records to the stream\.

```
$ aws kinesis put-record --stream-name lambda-stream --partition-key 1 \
--data "Hello, this is a test."
```

Lambda uses the execution role to read records from the stream\. Then it invokes your Lambda function, passing in batches of records\. The function decodes data from each record and logs it, sending the output to CloudWatch Logs\. View the logs in the [CloudWatch console](https://console.aws.amazon.com/cloudwatch)\.