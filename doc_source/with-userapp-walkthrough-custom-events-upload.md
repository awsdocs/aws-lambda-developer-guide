# Step 2\.3: Create a Lambda Function<a name="with-userapp-walkthrough-custom-events-upload"></a>

Execute the following Lambda CLI `create-function` command to create a Lambda function\. You provide the deployment package and IAM role ARN as parameters\. Note that the `Runtime` parameter uses `nodejs6.10` but you can also specify `nodejs8.10`\. 

```
$ aws lambda create-function \
--region region \
--function-name helloworld \
--zip-file fileb://file-path/helloworld.zip \
--role role-arn \
--handler helloworld.handler \
--runtime nodejs6.10 \
--profile adminuser
```

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

For more information, see [CreateFunction](API_CreateFunction.md)\. AWS Lambda creates the function and returns function configuration information as shown in the following example:

```
{
    "FunctionName": "helloworld",
    "CodeSize": 351,
    "MemorySize": 128,
    "FunctionArn": "function-arn",
    "Handler": "helloworld.handler",
    "Role": "arn:aws:iam::account-id:role/LambdaExecRole",
    "Timeout": 3,
    "LastModified": "2015-04-07T22:02:58.854+0000",
    "Runtime": "nodejs6.10",
    "Description": ""
}
```