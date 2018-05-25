# Step 2\.3: Create the Lambda Function and Test It Manually<a name="with-sqs-create-function"></a>

In this section, you do the following:
+ Create a Lambda function by uploading the deployment package\. 
+ Test the Lambda function by invoking it manually\. Instead of creating an event source, you use sample Amazon SQS message data\. 

In the next section, you create an Amazon SQS queue and test the end\-to\-end experience\.

## Step 2\.3\.1: Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-sqs-events-adminuser-create-test-function-upload-zip-test-upload"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. If you have not created this profile, see [Set Up an AWS Account](setup.md)

You need to update the command by providing the \.zip file path and the execution role ARN\. 

```
$ aws lambda create-function \
--region us-east-1 \
--function-name ProcessSQSMessage \
--zip-file fileb://file-path/ProcessSQSMessage.zip \
--role role-arn \
--handler ProcessSQSMessage.lambda_handler \
--runtime runtime-value \
--profile adminuser
```

For more information, see [CreateFunction](API_CreateFunction.md)\. AWS Lambda creates the function and returns function configuration information\. 

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="with-sqs-invoke-manually"></a>

In this step, you invoke your Lambda function manually using the `invoke` AWS Lambda CLI command and the following sample Amazon Simple Queue Service event\.

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
     "messages": [
       {
         "messageId" : "MessageID_1",
         "receiptHandle" : "MessageReceiptHandle",
         "body" : "Message Body",
         "md5OfBody" : "fce0ea8dd236ccb3ed9b37dae260836f",
         "md5OfMessageAttributes" : "582c92c5c5b6ac403040a4f3ab3115c9",
         "attributes" : {
           "ApproximateReceiveCount" : "2",
           "SentTimestamp" : "1520621625029",
           "SenderId" : "AROAIWPX5BD2BHG722MW4:sender",
           "ApproximateFirstReceiveTimestamp" : "1520621634884"
         },
         "messageAttributes" : {
           "Attribute3" : {
             "stringValue" : null,
             "binaryValue" : "1100",
             "stringListValues" : [ ],
             "binaryListValues" : [ ],
             "dataType" : "Binary"
           },
           "Attribute2" : {
             "stringValue" : "123",
             "binaryValue" : null,
             "stringListValues" : [ ],
             "binaryListValues" : [ ],
             "dataType" : "Number"
           },
           "Attribute1" : {
             "stringValue" : "AttributeValue1",
             "binaryValue" : null,
             "stringListValues" : [ ],
             "binaryListValues" : [ ],
             "dataType" : "String"
           }
         }
       },
       {
         "messageId" : "MessageID_2",
         "receiptHandle" : "MessageReceiptHandle",
         "body" : "Message Body",
         "md5OfBody" : "fce0ea8dd236ccb3ed9b37dae260836f",
         "md5OfMessageAttributes" : "582c92c5c5b6ac403040a4f3ab3115c9",
         "attributes" : {
           "ApproximateReceiveCount" : "5",
           "SentTimestamp" : "1520621625030",
           "SenderId" : "AROAIWPX5BD2BHG722MW4:sender",
           "ApproximateFirstReceiveTimestamp" : "1520621634900"
         },
         "messageAttributes" : {
           "Attribute2" : {
             "stringValue" : "123",
             "binaryValue" : null,
             "stringListValues" : [ ],
             "binaryListValues" : [ ],
             "dataType" : "Number"
           }
         }
       },
       {
         "messageId" : "MessageID_2",
         "receiptHandle" : "MessageReceiptHandle",
         "body" : "Message Body",
         "md5OfBody" : "fce0ea8dd236ccb3ed9b37dae260836f",
         "md5OfMessageAttributes" : "582c92c5c5b6ac403040a4f3ab3115c9",
         "attributes" : {
           "ApproximateReceiveCount" : "5",
           "SentTimestamp" : "1520621625030",
           "SenderId" : "AROAIWPX5BD2BHG722MW4:sender",
           "ApproximateFirstReceiveTimestamp" : "1520621634900"
         },
         "messageAttributes" : { }
       }
     ],
     "queueUrl": "https://sqs.us-east-1.amazonaws.com/123456789012/MyQueue"
   }
   ```

1. Execute the following `invoke` command\. 

   ```
   $ aws lambda invoke \
   --invocation-type RequestResponse \
   --function-name ProcessSQSMessage \
   --region us-east-1 \
   --payload file://file-path/input.txt \
   --profile adminuser \
   outputfile.txt
   ```

   Note that the `invoke` command specifies `RequestResponse` as the invocation type, which requests synchronous execution\. For more information, see [Invoke](API_Invoke.md)\. 

1. Verify the output in the `outputfile.txt` file\.

   You can monitor the activity of your Lambda function in the AWS Lambda console\. 
   + The AWS Lambda console shows a graphical representation of some of the CloudWatch metrics in the **Cloudwatch Metrics at a glance** section for your function\. Sign in to the AWS Management Console at [https://console\.aws\.amazon\.com/](https://console.aws.amazon.com/)\.
   +  For each graph you can also click the **logs** link to view the CloudWatch logs directly\.

### Next Step<a name="with-sqs-manual-invoke-next-step"></a>

[Step 3: Add an Event Source \(Create an Amazon SQS Queue and Associate It with Your Lambda Function\)](with-sqs-configure-sqs.md)