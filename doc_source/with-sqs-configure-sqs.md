# Step 3: Add an Event Source \(Create an Amazon SQS Queue and Associate It with Your Lambda Function\)<a name="with-sqs-configure-sqs"></a>

In this section, you do the following:
+ Create an Amazon SQS queue\.
+ Create an event source mapping in AWS Lambda\. This event source mapping associates the Amazon SQS queue with your Lambda function\. After you create this event source mapping, AWS Lambda starts polling the queue\.
+ Test the end\-to\-end experience\. As you perform queue updates, Amazon Simple Queue Service writes messages to the queue\. AWS Lambda polls the queue, detects new records and executes your Lambda function on your behalf by passing events, in this case Amazon SQS messages, to the function\. 
**Note**  
The following example uses an \(`adminuser`\) role with administrator privileges\. If you have not set this up, see [Step 1: Prepare](with-sqs-prepare.md)\. 

## Step 3\.1: Create an Amazon SQS Queue<a name="with-sqs-create-queue"></a>

Follow the procedure to create an Amazon SQS queue:

1. Sign in to the AWS Management Console and open the Amazon SQS console at [https://console\.aws\.amazon\.com/sqs/](https://console.aws.amazon.com/sqs/)\.

1. In the Amazon SQS console, create a queue\. 

1. Write down or otherwise record the identifying queue ARN \(Amazon Resource Name\)\. You need this in the next step when you associate the queue with your Lambda function\.

## Step 3\.2: Add an Event Source in AWS Lambda<a name="with-sqs-attach-notification-configuration"></a>

To create a mapping between the specified Amazon SQS queue and the Lambda function, run the following AWS CLI `create-event-source-mapping` command\. After the command executes, write down or otherwise record the UUID\. You'll need this UUID to refer to the event source mapping in any other commands, for example, if you choose to delete the event source mapping\.

```
$ aws lambda create-event-source-mapping \
--region us-east-1 \
--function-name ProcessSQSMessage \
--event-source SQS-queue-arn \
--batch-size 1 \
--profile adminuser
```

You can get the list of event source mappings by running the following command\.

```
$ aws lambda list-event-source-mappings \
--region us-east-1 \
--function-name ProcessSQSMessage \
--event-source SQS-queue-arn \
--profile adminuser
```

The list returns all of the event source mappings you created, and for each mapping it shows the `LastProcessingResult`, among other things\. This field is used to provide an informative message if there are any problems\. Values such as `No records processed` \(indicates that AWS Lambda has not started polling or that there are no records in the queue\) and `OK` \(indicates AWS Lambda successfully read records from the queue and invoked your Lambda function\) indicate that there no issues\. If there are issues, you receive an error message\.

## Step 3\.3: Test the Setup<a name="with-sqs-final-integration-test-no-iam"></a>

You're all done\! Now **adminuser** can test the setup as follows:

1. In the Amazon SQS console, send messsages to the queue\. Amazon SQS writes records of these actions to the queue\.

1. AWS Lambda polls the queue and when it detects updates, it invokes your Lambda function by passing in the event data it finds in the queue\.

1. Your function executes and creates logs in Amazon CloudWatch\. The **adminuser** can also verify the logs reported in the Amazon CloudWatch console\.