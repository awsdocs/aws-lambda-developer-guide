# Step 3: Add an Event Source \(Create a Kinesis Stream and Associate It with Your Lambda Function\)<a name="with-kinesis-example-configure-event-source"></a>

 In this section, you create a Kinesis stream, and then you add an event source in AWS Lambda to associate the Kinesis stream with your Lambda function\. 

After you create an event source, AWS Lambda starts polling the stream\. You then test the setup by adding events to the stream and verify that AWS Lambda executed your Lambda function on your behalf:

## Step 3\.1: Create a Kinesis Stream<a name="with-kinesis-example-configure-event-source-create"></a>

Use the following Kinesis `create-stream ` CLI command to create a stream\.

```
$ aws kinesis create-stream \
--stream-name examplestream \
--shard-count 1 \
--region region \
--profile adminuser
```

Run the following Kinesis `describe-stream` AWS CLI command to get the stream ARN\. 

```
$ aws kinesis describe-stream \
--stream-name examplestream \
--region region \
--profile adminuser
```

You need the stream ARN in the next step to associate the stream with your Lambda function\. The stream is of the form:

```
arn:aws:kinesis:aws-region:account-id:stream/stream-name
```

## Step 3\.2: Add an Event Source in AWS Lambda<a name="with-kinesis-example-configure-event-source-add-event-source"></a>

Run the following AWS CLI `add-event-source` command\. After the command executes, note down the UUID\. You'll need this UUID to refer to the event source in any commands \(for example, when deleting the event source\)\.

```
$ aws lambda create-event-source-mapping \
--region region \
--function-name ProcessKinesisRecords \
--event-source  kinesis-stream-arn \
--batch-size 100 \
--starting-position TRIM_HORIZON \
--profile adminuser
```

You can get a list of event source mappings by running the following command\.

```
$ aws lambda list-event-source-mappings \
--region region \
--function-name ProcessKinesisRecords \
--event-source kinesis-stream-arn \
--profile adminuser \
```

In the response, you can verify the status value is `enabled`\. 

**Note**  
If you disable the event source mapping, AWS Lambda stops polling the Kinesis stream\. If you re\-enable event source mapping, it will resume polling from the sequence number where it stopped, so each record is processed either before you disabled the mapping or after you enabled it\. If the sequence number falls behind `TRIM_HORIZON`, when you re\-enable it polling will start from `TRIM_HORIZON`\. However, if you create a new event source mapping, polling will always start from `TRIM_HORIZON`, `LATEST` or `AT_TIMESTAMP`, depending on the starting position you specify\. This applies even if you delete an event source mapping and create a new one with the same configuration as the deleted one\. 

## Step 3\.3: Test the Setup<a name="with-kinesis-example-configure-event-source-test-end-to-end"></a>

You're all done\! Now *adminuser* can test the setup as follows:

1. Using the following AWS CLI command, add event records to your Kinesis stream\. The `--data` value is a base64\-encoded value of the `"Hello, this is a test."` string\. You can run the same command more than once to add multiple records to the stream\. 

   ```
   $ aws kinesis put-record \
   --stream-name examplestream \
   --data "This is a test. final" \
   --partition-key shardId-000000000000 \
   --region region \
   --profile adminuser
   ```

1. AWS Lambda polls the stream and, when it detects updates to the stream, it invokes your Lambda function by passing in the event data from the stream\.

   AWS Lambda assumes the execution role to poll the stream\. You have granted the role permissions for the necessary Kinesis actions so that AWS Lambda can poll the stream and read events from the stream\.

1. Your function executes and adds logs to the log group that corresponds to the Lambda function in Amazon CloudWatch\. 

   The **adminuser** can also verify the logs reported in the Amazon CloudWatch console\. Make sure you are checking for logs in the same AWS region where you created the Lambda function\. 