# Step 2\.3: Create the Lambda Function and Test It Manually<a name="with-dynamodb-create-function"></a>

In this section, you do the following:

+ Create a Lambda function by uploading the deployment package\. 

+ Test the Lambda function by invoking it manually\. Instead of creating an event source, you use sample DynamoDB event data\. 

In the next section, you create an DynamoDB stream and test the end\-to\-end experience\.

## Step 2\.3\.1: Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-upload"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. If you have already created this profile, see [Set Up an AWS Account](setup.md)

You need to update the command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `python3.6`, `python2.7`, `nodejs6.10` `nodejs4.3`, or `java8`, depending on the language you used to author your code\.

```
$ aws lambda create-function \
--region us-east-1 \
--function-name ProcessDynamoDBStream \
--zip-file fileb://file-path/ProcessDynamoDBStream.zip \
--role role-arn \
--handler ProcessDynamoDBStream.lambda_handler \
--runtime runtime-value \
--profile adminuser
```

**Note**  
If you choose `Java 8` as the runtime, the handler value must be `packageName::methodName`\.

For more information, see [CreateFunction](API_CreateFunction.md)\. AWS Lambda creates the function and returns function configuration information\. 

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="with-dbb-invoke-manually"></a>

In this step, you invoke your Lambda function manually using the `invoke` AWS Lambda CLI command and the following sample DynamoDB event\.

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
      "Records":[
         {
            "eventID":"1",
            "eventName":"INSERT",
            "eventVersion":"1.0",
            "eventSource":"aws:dynamodb",
            "awsRegion":"us-east-1",
            "dynamodb":{
               "Keys":{
                  "Id":{
                     "N":"101"
                  }
               },
               "NewImage":{
                  "Message":{
                     "S":"New item!"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "SequenceNumber":"111",
               "SizeBytes":26,
               "StreamViewType":"NEW_AND_OLD_IMAGES"
            },
            "eventSourceARN":"stream-ARN"
         },
         {
            "eventID":"2",
            "eventName":"MODIFY",
            "eventVersion":"1.0",
            "eventSource":"aws:dynamodb",
            "awsRegion":"us-east-1",
            "dynamodb":{
               "Keys":{
                  "Id":{
                     "N":"101"
                  }
               },
               "NewImage":{
                  "Message":{
                     "S":"This item has changed"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "OldImage":{
                  "Message":{
                     "S":"New item!"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "SequenceNumber":"222",
               "SizeBytes":59,
               "StreamViewType":"NEW_AND_OLD_IMAGES"
            },
            "eventSourceARN":"stream-ARN"
         },
         {
            "eventID":"3",
            "eventName":"REMOVE",
            "eventVersion":"1.0",
            "eventSource":"aws:dynamodb",
            "awsRegion":"us-east-1",
            "dynamodb":{
               "Keys":{
                  "Id":{
                     "N":"101"
                  }
               },
               "OldImage":{
                  "Message":{
                     "S":"This item has changed"
                  },
                  "Id":{
                     "N":"101"
                  }
               },
               "SequenceNumber":"333",
               "SizeBytes":38,
               "StreamViewType":"NEW_AND_OLD_IMAGES"
            },
            "eventSourceARN":"stream-ARN"
         }
      ]
   }
   ```

1. Execute the following `invoke` command\. 

   ```
   $ aws lambda invoke \
   --invocation-type RequestResponse \
   --function-name ProcessDynamoDBStream \
   --region us-east-1 \
   --payload file://file-path/input.txt \
   --profile adminuser \
   outputfile.txt
   ```

   Note that the `invoke` command specifies the `RequestResponse` as the invocation type, which requests synchronous execution\. For more information, see [Invoke](API_Invoke.md)\. The function returns the string message \(message in the `context.succeed()` in the code\) in the response body\. 

1. Verify the output in the `outputfile.txt` file\.

   You can monitor the activity of your Lambda function in the AWS Lambda console\. 

   + The AWS Lambda console shows a graphical representation of some of the CloudWatch metrics in the **Cloudwatch Metrics at a glance** section for your function\. Sign in to the AWS Management Console at [https://console\.aws\.amazon\.com/](https://console.aws.amazon.com/)\.

   +  For each graph you can also click the **logs** link to view the CloudWatch logs directly\.

### Next Step<a name="with-ddb-manual-invoke-next-step"></a>

[Step 3: Add an Event Source \(Create a DynamoDB Stream and Associate It with Your Lambda Function\)](with-ddb-configure-ddb.md)