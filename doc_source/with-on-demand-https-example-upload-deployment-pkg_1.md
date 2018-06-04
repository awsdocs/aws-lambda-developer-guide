# Step 2\.3: Create the Lambda Function and Test It Manually<a name="with-on-demand-https-example-upload-deployment-pkg_1"></a>

In this section, you do the following:
+ Create a Lambda function by uploading the deployment package\. 
+ Test the Lambda function by invoking it manually and passing sample event data\. 

## Step 2\.3\.1: Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-on-demand-https-events-adminuser-create-test-function-upload-zip-test-upload_1"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. 

You need to update the command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `python3.6`, `python2.7`, `nodejs8.10` or `nodejs6.10`, or `java8`, depending on the language you used to author your code\.

```
$ aws lambda create-function \
--region region \
--function-name LambdaFunctionOverHttps  \
--zip-file fileb://file-path/LambdaFunctionOverHttps.zip \
--role execution-role-arn  \
--handler LambdaFunctionOverHttps.handler \
--runtime runtime-value \
--profile adminuser
```

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

**Note**  
You can create the Lambda function using the AWS Lambda console, in which case note the value of the `create-function` AWS CLI command parameters\. You provide the same values in the console UI\.

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="walkthrough-on-demand-https-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the function manually using the sample event data\. We recommend that you invoke the function using the console because the console UI provides a user\-friendly interface for reviewing the execution results, including the execution summary, logs written by your code, and the results returned by the function \(because the console always performs synchronous execution—invokes the Lambda function using the `RequestResponse` invocation type\)\.

**To test the Lambda function \(AWS Management Console\)**

1. Follow the steps in the Getting Started exercise to create and invoke the Lambda function at [Invoke the Lambda Function Manually and Verify Results, Logs, and Metrics](get-started-create-function.md#get-started-invoke-manually)\. For the sample event for testing, choose **Hello World** in **Sample event template**, and then replace the data using the following: 

   ```
   {
       "operation": "echo",
       "payload": {
           "somekey1": "somevalue1",
           "somekey2": "somevalue2"
       }
   }
   ```

1. To test one of the `dynamo` operations, such as `read`, change the input data to the following:

   ```
   {
       "operation": "read",
       "tableName": "the name of your stream table",
       "payload": {
           "Key": {
             "the primary key of the table": "the value of the key"
           }
       }
   }
   ```

1. Verify the results in the console\.

**To test the Lambda function \(AWS CLI\)**

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
       "operation": "echo",
       "payload": {
           "somekey1": "somevalue1",
           "somekey2": "somevalue2"
       }
   }
   ```

1.  Execute the following `invoke` command:

   ```
   $ aws lambda  invoke \
   --invocation-type Event \
   --function-name LambdaFunctionOverHttps \
   --region region \
   --payload fileb://file-path/input.txt \
   --profile adminuser \
   outputfile.txt
   ```
**Note**  
In this tutorial example, the message is saved in the `outputfile.txt` file if you request synchronous execution \(`RequestResponse` as the invocation type\)\. The function returns the string message in the response body\. If you use the `Event` invocation type, no message is returned to the output file\. In either case, the *outputfile\.txt* parameter is required\. 

## Next Step<a name="with-on-demand-https-example-upload-deployment-pkg-next-step"></a>

 [Step 3: Create an API Using Amazon API Gateway and Test It](with-on-demand-https-example-configure-event-source.md) 