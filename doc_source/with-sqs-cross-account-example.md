# Tutorial: Using a cross\-account Amazon SQS queue as an event source<a name="with-sqs-cross-account-example"></a>

In this tutorial, you create a Lambda function that consumes messages from an Amazon Simple Queue Service \(Amazon SQS\) queue in a different AWS account\. This tutorial involves two AWS accounts: **Account A** refers to the account that contains your Lambda function, and **Account B** refers to the account that contains the Amazon SQS queue\.

## Prerequisites<a name="with-sqs-cross-account-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Create a Lambda function with the console](getting-started.md#getting-started-create-function) to create your first Lambda function\.

To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

```
aws --version
```

You should see the following output:

```
aws-cli/2.0.57 Python/3.7.4 Darwin/19.6.0 exe/x86_64
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\.

**Note**  
On Windows, some Bash CLI commands that you commonly use with Lambda \(such as `zip`\) are not supported by the operating system's built\-in terminals\. To get a Windows\-integrated version of Ubuntu and Bash, [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. 

## Create the execution role \(Account A\)<a name="with-sqs-cross-account-create-execution-role"></a>

In **Account A**, create an [execution role](lambda-intro-execution-role.md) that gives your function permission to access the required AWS resources\.

**To create an execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) in the AWS Identity and Access Management \(IAM\) console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**
   + **Permissions** – **AWSLambdaSQSQueueExecutionRole**
   + **Role name** – **cross\-account\-lambda\-sqs\-role**

The **AWSLambdaSQSQueueExecutionRole** policy has the permissions that the function needs to read items from Amazon SQS and to write logs to Amazon CloudWatch Logs\.

## Create the function \(Account A\)<a name="with-sqs-cross-account-create-function"></a>

In **Account A**, create a Lambda function that processes your Amazon SQS messages\. The following Node\.js 12 code example writes each message to a log in CloudWatch Logs\.

**Note**  
For code examples in other languages, see [Sample Amazon SQS function code](with-sqs-create-package.md)\.

**Example index\.js**  

```
exports.handler = async function(event, context) {
  event.Records.forEach(record => {
    const { body } = record;
    console.log(body);
  });
  return {};
}
```

**To create the function**
**Note**  
Following these steps creates a function in Node\.js 12\. For other languages, the steps are similar, but some details are different\.

1. Save the code example as a file named `index.js`\.

1. Create a deployment package\.

   ```
   zip function.zip index.js
   ```

1. Create the function using the `create-function` AWS Command Line Interface \(AWS CLI\) command\.

   ```
   aws lambda create-function --function-name CrossAccountSQSExample \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
   --role arn:aws:iam::<AccountA_ID>:role/cross-account-lambda-sqs-role
   ```

## Test the function \(Account A\)<a name="with-sqs-cross-account-create-test-function"></a>

In **Account A**, test your Lambda function manually using the `invoke` AWS CLI command and a sample Amazon SQS event\.

If the handler returns normally without exceptions, Lambda considers the message to be successfully processed and begins reading new messages in the queue\. After successfully processing a message, Lambda automatically deletes it from the queue\. If the handler throws an exception, Lambda considers the batch of messages not successfully processed, and Lambda invokes the function with the same batch of messages\.

1. Save the following JSON as a file named `input.txt`\.

   ```
   {
       "Records": [
           {
               "messageId": "059f36b4-87a3-44ab-83d2-661975830a7d",
               "receiptHandle": "AQEBwJnKyrHigUMZj6rYigCgxlaS3SLy0a...",
               "body": "test",
               "attributes": {
                   "ApproximateReceiveCount": "1",
                   "SentTimestamp": "1545082649183",
                   "SenderId": "AIDAIENQZJOLO23YVJ4VO",
                   "ApproximateFirstReceiveTimestamp": "1545082649185"
               },
               "messageAttributes": {},
               "md5OfBody": "098f6bcd4621d373cade4e832627b4f6",
               "eventSource": "aws:sqs",
               "eventSourceARN": "arn:aws:sqs:us-east-1:123456789012:example-queue",
               "awsRegion": "us-east-1"
           }
       ]
   }
   ```

   The preceding JSON simulates an event that Amazon SQS might send to your Lambda function, where `"body"` contains the actual message from the queue\.

1. Run the following `invoke` AWS CLI command\.

   ```
   aws lambda invoke --function-name CrossAccountSQSExample \
   --payload file://input.txt outputfile.txt
   ```

   The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.

1. Verify the output in the file `outputfile.txt`\.

## Create an Amazon SQS queue \(Account B\)<a name="with-sqs-cross-account-configure-sqs"></a>

In **Account B**, create an Amazon SQS queue that the Lambda function in **Account A** can use as an event source\.

**To create a queue**

1. Open the [Amazon SQS console](https://console.aws.amazon.com/sqs)\.

1. Choose **Create queue**\.

1. Create a queue with the following properties\.
   + **Type** – **Standard**
   + **Name** – **LambdaCrossAccountQueue**
   + **Configuration** – Keep the default settings\.
   + **Access policy** – Choose **Advanced**\. Paste in the following JSON policy:

     ```
     {
        "Version": "2012-10-17",
        "Id": "Queue1_Policy_UUID",
        "Statement": [{
           "Sid":"Queue1_AllActions",
           "Effect": "Allow",
           "Principal": {
              "AWS": [
                 "arn:aws:iam::<AccountA_ID>:role/cross-account-lambda-sqs-role"
              ]
           },
           "Action": "sqs:*",
           "Resource": "arn:aws:sqs:us-east-1:<AccountB_ID>:LambdaCrossAccountQueue"
         }
       ]
     }
     ```

     This policy grants the Lambda execution role in **Account A** permissions to consume messages from this Amazon SQS queue\.

1. After creating the queue, record its Amazon Resource Name \(ARN\)\. You need this in the next step when you associate the queue with your Lambda function\.

## Configure the event source \(Account A\)<a name="with-sqs-cross-account-event-source"></a>

In **Account A**, create an event source mapping between the Amazon SQS queue in **Account B** and your Lambda function by running the following `create-event-source-mapping` AWS CLI command\.

```
aws lambda create-event-source-mapping --function-name CrossAccountSQSExample --batch-size 10 \
--event-source-arn arn:aws:sqs:us-east-1:<AccountB_ID>:LambdaCrossAccountQueue
```

To get a list of your event source mappings, run the following command\.

```
aws lambda list-event-source-mappings --function-name CrossAccountSQSExample \
--event-source-arn arn:aws:sqs:us-east-1:<AccountB_ID>:LambdaCrossAccountQueue
```

## Test the setup<a name="with-sqs-final-integration-test-no-iam"></a>

You can now test the setup as follows:

1. In **Account B**, open the [Amazon SQS console](https://console.aws.amazon.com/sqs)\.

1. Choose **LambdaCrossAccountQueue**, which you created earlier\.

1. Choose **Send and receive messages**\.

1. Under **Message body**, enter a test message\.

1. Choose **Send message**\.

Your Lambda function in **Account A** should receive the message\. Lambda will continue to poll the queue for updates\. When there is a new message, Lambda invokes your function with this new event data from the queue\. Your function runs and creates logs in Amazon CloudWatch\. You can view the logs in the [CloudWatch console](https://console.aws.amazon.com/cloudwatch)\.

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

In **Account A**, clean up your execution role and Lambda function\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Select the execution role that you created\.

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, then choose **Delete**\.

1. Choose **Delete**\.

In **Account B**, clean up the Amazon SQS queue\.

**To delete the Amazon SQS queue**

1. Sign in to the AWS Management Console and open the Amazon SQS console at [https://console\.aws\.amazon\.com/sqs/](https://console.aws.amazon.com/sqs/)\.

1. Select the queue you created\.

1. Choose **Delete**\.

1. Enter **delete** in the text box\.

1. Choose **Delete**\.
