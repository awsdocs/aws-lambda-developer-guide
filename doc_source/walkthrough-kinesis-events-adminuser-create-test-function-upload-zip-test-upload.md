# Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-kinesis-events-adminuser-create-test-function-upload-zip-test-upload"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. If you have already created this profile, see [Set Up an AWS Account](setup.md)

You need to update the command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `python3.6`, `python2.7`, `nodejs8.10` or `nodejs6.10`, or `java8`, depending on the language you used to author your code\.

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