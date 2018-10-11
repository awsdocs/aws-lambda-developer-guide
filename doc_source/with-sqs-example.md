# Tutorial: Using AWS Lambda with Amazon Simple Queue Service<a name="with-sqs-example"></a>

In this tutorial, you create a Lambda function to consume messages from an [Amazon SQS](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html) queue\.

## Prerequisites<a name="with-sqs-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Create the Execution Role<a name="with-sqs-create-execution-role"></a>

At the time you upload the deployment package, you need to specify an IAM execution role \([Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\. For example, AWS Lambda needs permissions for Amazon SQS actions so it can poll the queue and read messages\. The example Lambda function writes some of the event data to CloudWatch, so it needs permissions for necessary CloudWatch actions\. 

In order for AWS Lambda to poll, process and delete messages on the Amazon SQS queue you have configured, you need to set permissions for the following Amazon SQS actions:
+ [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html)
+ [DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html)
+ [GetQueueAttributes](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html)

You can do this in either of the following two ways:

**Note**  
If the Amazon SQS queue and Lambda function are associated with different AWS accounts, you must use a **resource\-based policy** to enable cross\-account access\.
+ **Identity\-based policy**: Add an inline policy to the execution role that grants the permissions for the required actions listed previously, as shown in the following example:

  ```
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Sid": sid,
              "Effect": "Allow",
              "Action": [
                  "sqs:DeleteMessage",
                  "sqs:ChangeMessageVisibility",
                  "sqs:ReceiveMessage",
                  "sqs:GetQueueAttributes"
              ],
              "Resource": “arn:aws:sqs:region:123456789012:test-queue“
          }
      ]
  }
  ```

  For more information, see [Overview of Managing Access Permissions to Your Amazon Simple Queue Service Resource](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-overview-of-managing-access.html)
+ **Resource\-based policy**: Alternatively, you can use an Amazon SQS resource\-based policy\.

  ```
  {
    "Version": "2012-10-17",
    "Id": "arn:aws:sqs:region:123456789012:test-queue/mypolicy",
    "Statement": [
      {
        "Sid": sid,
        "Effect": "Allow",
        "Principal": {
          "AWS": "arn:aws:iam::123456789012:role/function-execution-role"
        },
        "Action": [
          "SQS:GetQueueAttributes",
          "SQS:ChangeMessageVisibility",
          "SQS:DeleteMessage",
          "SQS:ReceiveMessage"	
        ],
        "Resource": "arn:aws:sqs:region:123456789012:test-queue"
      }
    ]
  }
  ```

## Create a Function<a name="with-sqs-create-test-function"></a>

The following is example code that receives an Amazon SQS event message as input and processes it\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\. 

**Example ProcessSQSRecords\.js**  

```
exports.handler = async function(event, context) {
  event.Records.forEach(record => {
    const { body } = record;
    console.log(body);
  });
  return {};
}
```

Create the Lambda function by uploading the deployment package, and then test it by invoking it manually using sample Amazon SQS event data\. You provide both the deployment package and the IAM role at the time of creating a Lambda function\. You can also specify other configuration information, such as the function name, memory size, runtime environment to use, and the handler\. For more information about these parameters, see [CreateFunction](API_CreateFunction.md)\. After creating the Lambda function, you invoke it using sample Amazon Simple Queue Service event data\. 

At the command prompt, run the following Lambda CLI `create-function` command\.

You need to update the command by providing the \.zip file path and the execution role ARN\. 

```
$ aws lambda create-function --function-name ProcessSQSRecord --zip-file fileb://ProcessSQSRecord.zip --role role-arn --handler ProcessSQSRecord.handler --runtime nodejs8.10
```

## Test the Function<a name="with-sqs-create-function"></a>

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
   $ aws lambda invoke --invocation-type RequestResponse --function-name ProcessSQSRecord --payload file://input.txt outputfile.txt
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
$ aws lambda create-event-source-mapping --function-name ProcessSQSRecord --event-source SQS-queue-arn --batch-size 1
```

You can get the list of event source mappings by running the following command\.

```
$ aws lambda list-event-source-mappings --function-name ProcessSQSRecord --event-source SQS-queue-arn
```

The list returns all of the event source mappings you created, and for each mapping it shows the `LastProcessingResult`, among other things\. This field is used to provide an informative message if there are any problems\. Values such as `No records processed` \(indicates that AWS Lambda has not started polling or that there are no records in the queue\) and `OK` \(indicates AWS Lambda successfully read records from the queue and invoked your Lambda function\) indicate that there no issues\. If there are issues, you receive an error message\.

## Test the Setup<a name="with-sqs-final-integration-test-no-iam"></a>

Now you can test the setup as follows:

1. In the Amazon SQS console, send messsages to the queue\. Amazon SQS writes records of these actions to the queue\.

1. AWS Lambda polls the queue and when it detects updates, it invokes your Lambda function by passing in the event data it finds in the queue\.

1. Your function executes and creates logs in Amazon CloudWatch\. The **adminuser** can also verify the logs reported in the Amazon CloudWatch console\.