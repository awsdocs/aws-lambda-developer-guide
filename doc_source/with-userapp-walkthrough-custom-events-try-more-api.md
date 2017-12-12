# Step 4: Try More CLI Commands \(AWS CLI\)<a name="with-userapp-walkthrough-custom-events-try-more-api"></a>

## Step 4\.1: List the Lambda Functions in Your Account<a name="with-userapp-walkthrough-custom-events-list-functions"></a>

In this section, you try AWS Lambda list function operations\. Execute the following AWS CLI `list-functions` command to retrieve a list of functions that you uploaded\. 

```
$ aws lambda list-functions \
--max-items 10 \
--profile adminuser
```

To illustrate the use of pagination, the command specifies the optional `--max-items` parameter to limit the number of functions returned in the response\. For more information, see [ListFunctions](API_ListFunctions.md)\. The following is an example response\. 

```
{
    "Functions": [
        {
            "FunctionName": "helloworld",
            "MemorySize": 128,
            "CodeSize": 412,
            "FunctionArn": "arn:aws:lambda:us-east-1:account-id:function:ProcessKinesisRecords",
            "Handler": "ProcessKinesisRecords.handler",
            "Role": "arn:aws:iam::account-id:role/LambdaExecRole",
            "Timeout": 3,
            "LastModified": "2015-02-22T21:03:01.172+0000",
            "Runtime": "nodejs6.10",
            "Description": ""
        },
        {
            "FunctionName": "ProcessKinesisRecords",
            "MemorySize": 128,
            "CodeSize": 412,
            "FunctionArn": "arn:aws:lambda:us-east-1:account-id:function:ProcessKinesisRecords",
            "Handler": "ProcessKinesisRecords.handler",
            "Role": "arn:aws:iam::account-id:role/lambda-execute-test-kinesis",
            "Timeout": 3,
            "LastModified": "2015-02-22T21:03:01.172+0000",
            "Runtime": "nodejs6.10",
            "Description": ""
        },
        ...
      ],
       "NextMarker": null

}
```

In response, Lambda returns a list of up to 10 functions\. If there are more functions you can retrieve, `NextMarker` provides a marker you can use in the next `list-functions` request; otherwise, the value is null\. The following `list-functions` AWS CLI command is an example that shows the `--next-marker` parameter\.

```
$ aws lambda list-functions \
--max-items 10 \
--marker value-of-NextMarker-from-previous-response \
--profile adminuser
```

## Step 4\.2: Get Metadata and a URL to Download Previously Uploaded Lambda Function Deployment Packages<a name="with-userapp-walkthrough-custom-events-get-configuration"></a>

The Lambda CLI `get-function` command returns Lambda function metadata and a presigned URL that you can use to download the function's \.zip file \(deployment package\) that you uploaded to create the function\. For more information, see [GetFunction](API_GetFunction.md)\.

```
$ aws lambda get-function \
--function-name helloworld \
--region us-west-2 \
--profile adminuser
```

The following is an example response\.

```
{
    "Code": {
        "RepositoryType": "S3",
        "Location": "pre-signed-url"
    },
   "Configuration": {
        "FunctionName": "helloworld",
        "MemorySize": 128,
        "CodeSize": 287,
        "FunctionArn": "arn:aws:lambda:us-west-2:account-id:function:helloworld",
        "Handler": "helloworld.handler",
        "Role": "arn:aws:iam::account-id:role/LambdaExecRole",
        "Timeout": 3,
        "LastModified": "2015-04-07T22:02:58.854+0000",
        "Runtime": "nodejs6.10",
        "Description": ""
    }

}
```

If you want the function configuration information only \(not the presigned URL\), you can use the Lambda CLI `get-function-configuration` command\. 

```
$ aws lambda get-function-configuration \
 --function-name helloworld \
 --region us-west-2 \
--profile adminuser
```

### Next Step<a name="with-userapp-walkthrough-custom-events-get-configuration-next-step"></a>

[Step 5: Delete the Lambda Function and IAM Role \(AWS CLI\)](with-userapp-walkthrough-custom-events-delete-function.md)