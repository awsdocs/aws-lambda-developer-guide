# Step 2\.3: Create the Lambda Function and Test It Manually<a name="with-s3-example-upload-deployment-pkg"></a>

In this section, you do the following:

+ Create a Lambda function by uploading the deployment package\. 

+ Test the Lambda function by invoking it manually and passing sample Amazon S3 event data as a parameter\. 

## Step 2\.3\.1: Create the Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-s3-events-adminuser-create-test-function-upload-zip-test-upload"></a>

In this step, you upload the deployment package using the AWS CLI\.

1. At the command prompt, run the following Lambda AWS CLI `create-function` command using the `adminuser` as the `--profile`\. You need to update the command by providing the \.zip file path and the execution role ARN\. For the runtime parameter, choose between `nodejs6.10`, `nodejs4.3`, `python3.6`, `python2.7` or `java8`, depending on the code sample you when you created your deployment package\.

   ```
   $ aws lambda create-function \
   --region us-west-2 \
   --function-name CreateThumbnail \
   --zip-file fileb://file-path/CreateThumbnail.zip \
   --role role-arn \
   --handler CreateThumbnail.handler \
   --runtime runtime \
   --profile adminuser \
   --timeout 10 \
   --memory-size 1024
   ```

   Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

   ```
   --code S3Bucket=bucket-name,S3Key=zip-file-object-key
   ```

1. Write down the function ARN\. You will need this in the next section when you add notification configuration to your Amazon S3 bucket\. 

1. \(Optional\) The preceding command specifies a 10\-second timeout value as the function configuration\. Depending on the size of objects you upload, you might need to increase the timeout value using the following AWS CLI command\.

   ```
   $ aws lambda update-function-configuration \
      --function-name CreateThumbnail  \
      --region us-west-2 \
      --timeout timeout-in-seconds \
      --profile adminuser
   ```

**Note**  
You can create the Lambda function using the AWS Lambda console, in which case note the value of the `create-function` AWS CLI command parameters\. You provide the same values in the console UI\.

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="walkthrough-s3-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

In this step, you invoke the Lambda function manually using sample Amazon S3 event data\. You can test the function using the AWS Management Console or the AWS CLI\.

**To test the Lambda function \(console\)**

1. Follow the steps in the Getting Started to create and invoke the Lambda function at [Invoke the Lambda Function Manually and Verify Results, Logs, and Metrics](get-started-create-function.md#get-started-invoke-manually)\. For the sample event for testing, choose **S3 Put** in **Sample event template**\. 

1. Verify that the thumbnail was created in the target bucket and monitor the activity of your Lambda function in the AWS Lambda console as follows:

   + The AWS Lambda console shows a graphical representation of some of the CloudWatch metrics in the **Cloudwatch Metrics at a glance** section for your function\.

   +  For each graph, you can also click the **logs** link to view the CloudWatch Logs directly\.

**To test the Lambda function \(AWS CLI\)**

1. Save the following Amazon S3 sample event data in a file and save it as `input.txt`\. You need to update the JSON by providing your *sourcebucket* name and a \.jpg object key\.

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
                  "name":"sourcebucket",
                  "ownerIdentity":{  
                     "principalId":"A3NL1KOZZKExample"
                  },
                  "arn":"arn:aws:s3:::sourcebucket"
               },
               "object":{  
                  "key":"HappyFace.jpg",
                  "size":1024,
                  "eTag":"d41d8cd98f00b204e9800998ecf8427e",
                  "versionId":"096fKKXTRTtl3on89fVO.nfljtsv6qko"
               }
            }
         }
      ]
   }
   ```

1. Run the following Lambda CLI `invoke` command to invoke the function\. Note that the command requests asynchronous execution\. You can optionally invoke it synchronously by specifying `RequestResponse` as the `invocation-type` parameter value\.

   ```
   $ aws lambda invoke \
   --invocation-type Event \
   --function-name CreateThumbnail \
   --region us-west-2 \
   --payload file://file-path/inputfile.txt \
   --profile adminuser \
   outputfile.txt
   ```
**Note**  
You are able to invoke this function because you are using your own credentials to invoke your own function\. In the next section, you configure Amazon S3 to invoke this function on your behalf, which requires you to add permissions to the access policy associated with your Lambda function to grant Amazon S3 permissions to invoke your function\.

1. Verify that the thumbnail was created in the target bucket and monitor the activity of your Lambda function in the AWS Lambda console as follows:

   + The AWS Lambda console shows a graphical representation of some of the CloudWatch metrics in the **Cloudwatch Metrics at a glance** section for your function\.

   +  For each graph, you can also click the **logs** link to view the CloudWatch Logs directly\.

## Next Step<a name="with-s3-example-upload-deployment-pkg-next-step"></a>

 [Step 3: Add an Event Source \(Configure Amazon S3 to Publish Events\)](with-s3-example-configure-event-source.md) 