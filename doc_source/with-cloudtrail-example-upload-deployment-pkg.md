# Step 2\.3: Create the Lambda Function and Test It Manually<a name="with-cloudtrail-example-upload-deployment-pkg"></a>

In this section, you do the following:
+ Create a Lambda function by uploading the deployment package\. 
+ Test the Lambda function by invoking it manually\. 

  In this step, you use a sample S3 event that identifies your bucket name and the sample object \(that is, an example CloudTrail log\)\. In the next section you configure your S3 bucket notification to publish object\-created events and test the end\-to\-end experience\.

## Step 2\.3\.1: Create the Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-cloudtrail-events-adminuser-create-test-function-upload-zip-test-upload"></a>

In this step, you upload the deployment package using the AWS CLI and provide configuration information when you create the Lambda function using the `adminuser` `profile`\. For more information on setting up the `admin` profile and using the AWS CLI, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)\.

**Note**  
You need to update the command by providing the \.zip file path \(*//file\-path/CloudTrailEventProcessing\.zip \\*\) and the execution role ARN \(*execution\-role\-arn*\)\. If you used the sample code provided earlier in this tutorial, set the `--runtime` parameter value to `nodejs8.10` or `nodejs6.10`\. The sample following uses `nodejs6.10`\.   
You can author your Lambda functions in Java or Python too\. If you use another language, change the `--runtime` parameter value to `java8`, `python3.6` or `python2.7` as needed\.

```
$ aws lambda create-function \
--region region \
--function-name CloudTrailEventProcessing  \
--zip-file fileb://file-path/CloudTrailEventProcessing.zip \
--role execution-role-arn \
--handler CloudTrailEventProcessing.handler \
--runtime nodejs6.10 \
--profile adminuser \
--timeout 10 \
--memory-size 1024
```

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter as shown:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

**Note**  
You can create the Lambda function using the AWS Lambda console, in which case note the value of the `create-function` AWS CLI command parameters\. You provide the same values in the console\.

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="walkthrough-cloudtrail-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

In this section, you invoke the Lambda function manually using sample Amazon S3 event data\. When the Lambda function executes, it reads the S3 object \(a sample CloudTrail log\) from the bucket identified in the S3 event data, and then it publishes an event to your SNS topic if the sample CloudTrail log reports use a specific API\. For this tutorial, the API is the SNS API used to create a topic\. That is, the CloudTrail log reports a record identifying `sns.amazonaws.com` as the `eventSource`, and `CreateTopic` as the `eventName`\.

1. Save the following sample CloudTrail log to a file \(`ExampleCloudTrailLog.json`\)\. 
**Note**  
Note that one of events in this log has `sns.amazonaws.com` as the `eventSource` and `CreateTopic` as the `eventName`\. Your Lambda function reads the logs and if it finds an event of this type, it publishes the event to the Amazon SNS topic that you created and then you receive one email when you invoke the Lambda function manually\.

   ```
   {  
      "Records":[  
         {  
            "eventVersion":"1.02",
            "userIdentity":{  
               "type":"Root",
               "principalId":"account-id",
               "arn":"arn:aws:iam::account-id:root",
               "accountId":"account-id",
               "accessKeyId":"access-key-id",
               "sessionContext":{  
                  "attributes":{  
                     "mfaAuthenticated":"false",
                     "creationDate":"2015-01-24T22:41:54Z"
                  }
               }
            },
            "eventTime":"2015-01-24T23:26:50Z",
            "eventSource":"sns.amazonaws.com",
            "eventName":"CreateTopic",
            "awsRegion":"us-west-2",
            "sourceIPAddress":"205.251.233.176",
            "userAgent":"console.amazonaws.com",
            "requestParameters":{  
               "name":"dropmeplease"
            },
            "responseElements":{  
               "topicArn":"arn:aws:sns:us-west-2:account-id:exampletopic"
            },
            "requestID":"3fdb7834-9079-557e-8ef2-350abc03536b",
            "eventID":"17b46459-dada-4278-b8e2-5a4ca9ff1a9c",
            "eventType":"AwsApiCall",
            "recipientAccountId":"account-id"
         },
         {  
            "eventVersion":"1.02",
            "userIdentity":{  
               "type":"Root",
               "principalId":"account-id",
               "arn":"arn:aws:iam::account-id:root",
               "accountId":"account-id",
               "accessKeyId": "access key id",
               "sessionContext":{  
                  "attributes":{  
                     "mfaAuthenticated":"false",
                     "creationDate":"2015-01-24T22:41:54Z"
                  }
               }
            },
            "eventTime":"2015-01-24T23:27:02Z",
            "eventSource":"sns.amazonaws.com",
            "eventName":"GetTopicAttributes",
            "awsRegion":"us-west-2",
            "sourceIPAddress":"205.251.233.176",
            "userAgent":"console.amazonaws.com",
            "requestParameters":{  
               "topicArn":"arn:aws:sns:us-west-2:account-id:exampletopic"
            },
            "responseElements":null,
            "requestID":"4a0388f7-a0af-5df9-9587-c5c98c29cbec",
            "eventID":"ec5bb073-8fa1-4d45-b03c-f07b9fc9ea18",
            "eventType":"AwsApiCall",
            "recipientAccountId":"account-id"
         }
      ]
   }
   ```

1. Run the `gzip` command to create a \.gz file from the preceding source file\.

   ```
   $ gzip ExampleCloudTrailLog.json
   ```

   This creates `ExampleCloudTrailLog.json.gz` file\.

1. Upload the `ExampleCloudTrailLog.json.gz` file to the *examplebucket* that you specified in the CloudTrail configuration\.

   This object is specified in the sample Amazon S3 event data that we use to manually invoke the Lambda function\. 

1. Save the following JSON \(an example S3 event\) in a file, `input.txt`\. Note the bucket name and the object key name values\.

   You provide this sample event when you invoke your Lambda function\. For more information about the S3 event structure, see [Event Message Structure](https://docs.aws.amazon.com/AmazonS3/latest/dev/notification-content-structure.html) in the *Amazon Simple Storage Service Developer Guide*\. 

   ```
   {
       "Records":[
           {
               "eventVersion":"2.0",
               "eventSource":"aws:s3",
               "awsRegion":"us-west-2",
               "eventTime":"1970-01-01T00:00:00.000Z",
               "eventName":"ObjectCreated:Put",
               "userIdentity":{
                   "principalId":"AIDAJDPLRKLG7UEXAMPLE"
               },
               "requestParameters":{
                   "sourceIPAddress":"127.0.0.1"
               },
               "responseElements":{
                   "x-amz-request-id":"C3D13FE58DE4C810",
                   "x-amz-id-2":"FMyUVURIY8/IgAtTv8xRjskZQpcIZ9KG4V5Wp6S7S/JRWeUWerMUE5JgHvANOjpD"
               },
               "s3":{
                   "s3SchemaVersion":"1.0",
                   "configurationId":"testConfigRule",
                   "bucket":{
                       "name":"your bucket name",
                       "ownerIdentity":{
                           "principalId":"A3NL1KOZZKExample"
                       },
                       "arn":"arn:aws:s3:::mybucket"
                   },
                   "object":{
                       "key":"ExampleCloudTrailLog.json.gz",
                       "size":1024,
                       "eTag":"d41d8cd98f00b204e9800998ecf8427e",
                       "versionId":"096fKKXTRTtl3on89fVO.nfljtsv6qko"
                   }
               }
           }
       ]
   }
   ```

1. In the AWS Management Console, invoke the function manually using sample Amazon S3 event data\. In the console, use the following sample Amazon S3 event data\.
**Note**  
We recommend that you invoke the function using the console because the console UI provides a user\-friendly interface for reviewing the execution results, including the execution summary, logs written by your code, and the results returned by the function \(because the console always performs synchronous execution—invokes the Lambda function using the `RequestResponse` invocation type\)\. 

   ```
   {
       "Records":[
           {
               "eventVersion":"2.0",
               "eventSource":"aws:s3",
               "awsRegion":"us-west-2",
               "eventTime":"1970-01-01T00:00:00.000Z",
               "eventName":"ObjectCreated:Put",
               "userIdentity":{
                   "principalId":"AIDAJDPLRKLG7UEXAMPLE"
               },
               "requestParameters":{
                   "sourceIPAddress":"127.0.0.1"
               },
               "responseElements":{
                   "x-amz-request-id":"C3D13FE58DE4C810",
                   "x-amz-id-2":"FMyUVURIY8/IgAtTv8xRjskZQpcIZ9KG4V5Wp6S7S/JRWeUWerMUE5JgHvANOjpD"
               },
               "s3":{
                   "s3SchemaVersion":"1.0",
                   "configurationId":"testConfigRule",
                   "bucket":{
                       "name":"your bucket name",
                       "ownerIdentity":{
                           "principalId":"A3NL1KOZZKExample"
                       },
                       "arn":"arn:aws:s3:::mybucket"
                   },
                   "object":{
                       "key":"ExampleCloudTrailLog.json.gz",
                       "size":1024,
                       "eTag":"d41d8cd98f00b204e9800998ecf8427e",
                       "versionId":"096fKKXTRTtl3on89fVO.nfljtsv6qko"
                   }
               }
           }
       ]
   }
   ```

1. Execute the following AWS CLI command to invoke the function manually using the `adminuser` profile\.
**Note**  
 If you have not already created this profile, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)\.

   ```
   $ aws lambda invoke-async \
    --function-name CloudTrailEventProcessing \
    --region region \
    --invoke-args /filepath/input.txt \
    --debug \
   --profile adminuser
   ```

   Because your example log object has an event record showing the SNS API to call to create a topic, the Lambda function posts that event to your SNS topic, and you should get an email notification\. 

   You can monitor the activity of your Lambda function by using CloudWatch metrics and logs\. For more information about CloudWatch monitoring, see [Using Amazon CloudWatch](monitoring-functions.md)\. 

1. \(Optional\) Manually invoke the Lambda function using AWS CLI as follows:

   1. Save the JSON from Step 2 earlier in this procedure to a file called `input.txt`\.

   1. Execute the following invoke command:

      ```
      $ aws lambda  invoke \
      --invocation-type Event \
      --function-name CloudTrailEventProcessing \
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

## Next Step<a name="with-cloudtrail-example-upload-deployment-pkg-next-step"></a>

 [Step 3: Add Event Source \(Configure Amazon S3 to Publish Events\)](with-cloudtrail-example-configure-event-source.md) 