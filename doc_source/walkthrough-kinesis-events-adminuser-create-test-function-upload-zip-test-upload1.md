# Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-upload1"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. For more information on setting this up, see [Configuring the AWS CLI](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)\.

You need to update the command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `python3.6`, `python2.7`, `nodejs8.10`, `nodejs6.10`, `nodejs4.3`, or `java8`, depending on the language you used to author your code\.

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