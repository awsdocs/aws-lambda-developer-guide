# Step 2\.3: Create the Lambda Function \(Upload the Deployment Package\)<a name="vpc-rds-upload-deployment-pkg"></a>

In this step, you create the Lambda function \(`ReadMySqlTable`\) using the `create-function` AWS CLI command\.

At the command prompt, run the following Lambda CLI `create-function` command using the **adminuser** profile\. 

You need to update the following `create-function` command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `python2.7`, `nodejs`, or `java8`, depending on the language you used to author your code\. 

```
$ aws lambda create-function \
--region us-east-1 \
--function-name   CreateTableAddRecordsAndRead  \
--zip-file fileb://file-path/app.zip \
--role execution-role-arn \
--handler app.handler \
--runtime python3.6 \
--vpc-config SubnetIds=comma-separated-subnet-ids,SecurityGroupIds=default-vpc-security-group-id \
--profile adminuser
```

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

**Note**  
You can also create the Lambda function using the AWS Lambda console \(use the parameter values shown in the preceding CLI command\)\.

**Next Step**  
[Step 3: Test the Lambda Function \(Invoke Manually\)](vpc-rds-invoke-lambda-function.md)