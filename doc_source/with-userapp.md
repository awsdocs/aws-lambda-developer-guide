# Using AWS Lambda with the AWS Command Line Interface<a name="with-userapp"></a>

You can use the AWS Command Line Interface to manage functions and other AWS Lambda resources\. The AWS CLI uses the AWS SDK for Python \(Boto\) to interact with the Lambda API\. You can use it to learn about the API, and apply that knowledge in building applications that use Lambda with the AWS SDK\.

In this tutorial, you manage and invoke Lambda functions with the AWS CLI\.

## Prerequisites<a name="with-userapp-walkthrough-custom-events-deploy"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Create the Execution Role<a name="with-userapp-walkthrough-custom-events-create-iam-role"></a>

Create the [execution role](intro-permission-model.md#lambda-intro-execution-role) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaBasicExecutionRole**\.
   + **Role name** – **lambda\-cli\-role**\.

The **AWSLambdaBasicExecutionRole** policy has the permissions that the function needs to write logs to CloudWatch Logs\.

## Create the Function<a name="with-userapp-walkthrough-custom-events-upload"></a>

The following example code receives an event as input and logs some of the incoming event data to CloudWatch Logs\.

**Example index\.js**  

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
    console.log('value1 =', event.key1);
    console.log('value2 =', event.key2);
    console.log('value3 =', event.key3);
    callback(null, "Success");
};
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   $ zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   $ aws lambda create-function --function-name helloworld \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs8.10 \
   --role role-arn
   {
       "FunctionName": "helloworld",
       "CodeSize": 351,
       "MemorySize": 128,
       "FunctionArn": "function-arn",
       "Handler": "index.handler",
       "Role": "arn:aws:iam::account-id:role/LambdaExecRole",
       "Timeout": 3,
       "LastModified": "2015-04-07T22:02:58.854+0000",
       "Runtime": "nodejs8.10",
       "Description": ""
   }
   ```

Invoke your Lambda function using the invoke command\. 

```
$ aws lambda invoke --function-name helloworld --log-type Tail \
--payload '{"key1":"value1", "key2":"value2", "key3":"value3"}' \
outputfile.txt
{
     "LogResult": "base64-encoded-log",
     "StatusCode": 200 
}
```

By specifying the `--log-type` parameter, the command also requests the tail end of the log produced by the function\. The log data in the response is base64\-encoded\. Use the base64 program to decode the log\.

```
$ echo base64-encoded-log | base64 --decode
START RequestId: 16d25499-d89f-11e4-9e64-5d70fce44801
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    value1 = value1
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    value2 = value2
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    value3 = value3
2015-04-01T18:44:12.323Z    16d25499-d89f-11e4-9e64-5d70fce44801    result: "value1"
END RequestId: 16d25499-d89f-11e4-9e64-5d70fce44801
REPORT RequestId: 16d25499-d89f-11e4-9e64-5d70fce44801       
Duration: 13.35 ms      Billed Duration: 100 ms   Memory Size: 128 MB  
Max Memory Used: 9 MB
```

Because you invoked the function using the default invocation type \(`RequestResponse`\), the connection stays open until execution completes\. Lambda writes the response to the output file\.

## List the Lambda Functions in Your Account<a name="with-userapp-walkthrough-custom-events-list-functions"></a>

Execute the following AWS CLI `list-functions` command to retrieve a list of functions that you have created\. 

```
$ aws lambda list-functions --max-items 10
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

In response, Lambda returns a list of up to 10 functions\. If there are more functions you can retrieve, `NextMarker` provides a marker you can use in the next `list-functions` request; otherwise, the value is null\. The following `list-functions` AWS CLI command is an example that shows the `--marker` parameter\.

```
$ aws lambda list-functions --max-items 10 \
--marker value-of-NextMarker-from-previous-response
```

### Retrieve a Lambda Function<a name="with-userapp-walkthrough-custom-events-get-configuration"></a>

The Lambda CLI `get-function` command returns Lambda function metadata and a presigned URL that you can use to download the function's deployment packagen\.

```
$ aws lambda get-function --function-name helloworld
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
        "Handler": "index.handler",
        "Role": "arn:aws:iam::account-id:role/LambdaExecRole",
        "Timeout": 3,
        "LastModified": "2015-04-07T22:02:58.854+0000",
        "Runtime": "nodejs8.10",
        "Description": ""
    }
}
```

For more information, see [GetFunction](API_GetFunction.md)\.

## Clean Up<a name="with-userapp-walkthrough-custom-events-delete-function"></a>

Execute the following `delete-function` command to delete the `helloworld` function\.

```
$ aws lambda delete-function --function-name helloworld
```

Delete the IAM role you created in the IAM console\. For information about deleting a role, see [Deleting Roles or Instance Profiles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_manage_delete.html) in the *IAM User Guide*\. 