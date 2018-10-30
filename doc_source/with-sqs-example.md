# Tutorial: Using AWS Lambda with Amazon Simple Queue Service<a name="with-sqs-example"></a>

In this tutorial, you create a Lambda function to consume messages from an [Amazon SQS](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html) queue\.

## Prerequisites<a name="with-sqs-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Create the Execution Role<a name="with-sqs-create-execution-role"></a>

Create the [execution role](intro-permission-model.md#lambda-intro-execution-role) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaSQSQueueExecutionRole**\.
   + **Role name** – **lambda\-sqs\-role**\.

The **AWSLambdaSQSQueueExecutionRole** policy has the permissions that the function needs to read items from Amazon SQS and write logs to CloudWatch Logs\.

## Create the Function<a name="with-sqs-create-function"></a>

The following example code receives an Amazon SQS event input and processes the messages that it contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

**Note**  
For sample code in other languages, see [Sample Amazon SQS Function Code](with-sqs-create-package.md)\.

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

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   $ zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   $ aws lambda create-function --function-name ProcessSQSRecord \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs8.10 \
   --role role-arn
   ```

## Test the Function<a name="with-sqs-create-test-function"></a>

Invoke your Lambda function manually using the `invoke` AWS Lambda CLI command and a sample Amazon Simple Queue Service event\.

If the handler returns normally without exceptions, Lambda considers the message processed successfully and begins reading new messages in the queue\. Once a message is processed successfully, it is automatically deleted from the queue\. If the handler throws an exception, Lambda considers the input of messages as not processed and invokes the function with the same batch of messages\.

1. Copy the following JSON into a file and save it as `input.txt`\. 

   ```
   {
   	"Records": [
           {
               "messageId": "c80e8021-a70a-42c7-a470-796e1186f753",
               "receiptHandle": "AQEBJQ+/u6NsnT5t8Q/VbVxgdUl4TMKZ5FqhksRdIQvLBhwNvADoBxYSOVeCBXdnS9P+erlTtwEALHsnBXynkfPLH3BOUqmgzP25U8kl8eHzq6RAlzrSOfTO8ox9dcp6GLmW33YjO3zkq5VRYyQlJgLCiAZUpY2D4UQcE5D1Vm8RoKfbE+xtVaOctYeINjaQJ1u3mWx9T7tork3uAlOe1uyFjCWU5aPX/1OHhWCGi2EPPZj6vchNqDOJC/Y2k1gkivqCjz1CZl6FlZ7UVPOx3AMoszPuOYZ+Nuqpx2uCE2MHTtMHD8PVjlsWirt56oUr6JPp9aRGo6bitPIOmi4dX0FmuMKD6u/JnuZCp+AXtJVTmSHS8IXt/twsKU7A+fiMK01NtD5msNgVPoe9JbFtlGwvTQ==",
               "body": "{\"foo\":\"bar\"}",
               "attributes": {
                   "ApproximateReceiveCount": "3",
                   "SentTimestamp": "1529104986221",
                   "SenderId": "594035263019",
                   "ApproximateFirstReceiveTimestamp": "1529104986230"
               },
               "messageAttributes": {},
               "md5OfBody": "9bb58f26192e4ba00f01e2e7b136bbd8",
               "eventSource": "aws:sqs",
               "eventSourceARN": "arn:aws:sqs:us-west-2:594035263019:NOTFIFOQUEUE",
               "awsRegion": "us-west-2"
           }
       ]
   }
   ```

1. Execute the following `invoke` command\. 

   ```
   $ aws lambda invoke --invocation-type RequestResponse --function-name ProcessSQSRecord \
   --payload file://input.txt outputfile.txt
   ```

   The `invoke` command specifies `RequestResponse` as the invocation type, which requests synchronous execution\. For more information, see [Invocation Types](invocation-options.md)\. 

1. Verify the output in the `outputfile.txt` file\.

## Create an Amazon SQS Queue<a name="with-sqs-configure-sqs"></a>

Create an Amazon SQS queue that the Lambda function can use as an event source\.

**To create a queue**

1. Sign in to the AWS Management Console and open the Amazon SQS console at [https://console\.aws\.amazon\.com/sqs/](https://console.aws.amazon.com/sqs/)\.

1. In the Amazon SQS console, create a queue\.

1. Write down or otherwise record the identifying queue ARN \(Amazon Resource Name\)\. You need this in the next step when you associate the queue with your Lambda function\.

Create an event source mapping in AWS Lambda\. This event source mapping associates the Amazon SQS queue with your Lambda function\. After you create this event source mapping, AWS Lambda starts polling the queue\.

Test the end\-to\-end experience\. As you perform queue updates, Amazon Simple Queue Service writes messages to the queue\. AWS Lambda polls the queue, detects new records and executes your Lambda function on your behalf by passing events, in this case Amazon SQS messages, to the function\. 

## Configure the Event Source<a name="with-sqs-attach-notification-configuration"></a>

To create a mapping between the specified Amazon SQS queue and the Lambda function, run the following AWS CLI `create-event-source-mapping` command\. After the command executes, write down or otherwise record the UUID\. You'll need this UUID to refer to the event source mapping in any other commands, for example, if you choose to delete the event source mapping\.

```
$ aws lambda create-event-source-mapping --function-name ProcessSQSRecord \
--event-source SQS-queue-arn --batch-size 1
```

You can get the list of event source mappings by running the following command\.

```
$ aws lambda list-event-source-mappings --function-name ProcessSQSRecord \
--event-source SQS-queue-arn
```

The list returns all of the event source mappings you created, and for each mapping it shows the `LastProcessingResult`, among other things\. This field is used to provide an informative message if there are any problems\. Values such as `No records processed` \(indicates that AWS Lambda has not started polling or that there are no records in the queue\) and `OK` \(indicates AWS Lambda successfully read records from the queue and invoked your Lambda function\) indicate that there no issues\. If there are issues, you receive an error message\.

## Test the Setup<a name="with-sqs-final-integration-test-no-iam"></a>

Now you can test the setup as follows:

1. In the Amazon SQS console, send messsages to the queue\. Amazon SQS writes records of these actions to the queue\.

1. AWS Lambda polls the queue and when it detects updates, it invokes your Lambda function by passing in the event data it finds in the queue\.

1. Your function executes and creates logs in Amazon CloudWatch\. You can verify the logs reported in the Amazon CloudWatch console\.