# Step 3: Add an Event Source \(Create a DynamoDB Stream and Associate It with Your Lambda Function\)<a name="with-ddb-configure-ddb"></a>

In this section, you do the following:
+ Create an Amazon DynamoDB table with a stream enabled\.
+ Create an event source mapping in AWS Lambda\. This event source mapping associates the DynamoDB stream with your Lambda function\. After you create this event source mapping, AWS Lambda starts polling the stream\.
+ Test the end\-to\-end experience\. As you perform table updates, DynamoDB writes event records to the stream\. As AWS Lambda polls the stream, it detects new records in the stream and executes your Lambda function on your behalf by passing events to the function\. 
**Note**  
The following example assumes you have a user \(`adminuser`\) with administrator privileges\. When you follow the procedure, create a user with name `adminuser`\.

**To create an IAM user for yourself and add the user to an Administrators group**

  1. Use your AWS account email address and password to sign in as the *[AWS account root user](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_root-user.html)* to the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.
**Note**  
We strongly recommend that you adhere to the best practice of using the **Administrator** IAM user below and securely lock away the root user credentials\. Sign in as the root user only to perform a few [account and service management tasks](http://docs.aws.amazon.com/general/latest/gr/aws_tasks-that-require-root.html)\.

  1. In the navigation pane of the console, choose **Users**, and then choose **Add user**\.

  1. For **User name**, type **Administrator**\.

  1. Select the check box next to **AWS Management Console access**, select **Custom password**, and then type the new user's password in the text box\. You can optionally select **Require password reset** to force the user to create a new password the next time the user signs in\.

  1. Choose **Next: Permissions**\.

  1. On the **Set permissions** page, choose **Add user to group**\.

  1. Choose **Create group**\.

  1. In the **Create group** dialog box, for **Group name** type **Administrators**\.

  1. For **Filter policies**, select the check box for **AWS managed \- job function**\.

  1. In the policy list, select the check box for **AdministratorAccess**\. Then choose **Create group**\.

  1. Back in the list of groups, select the check box for your new group\. Choose **Refresh** if necessary to see the group in the list\.

  1. Choose **Next: Review** to see the list of group memberships to be added to the new user\. When you are ready to proceed, choose **Create user**\.

  You can use this same process to create more groups and users, and to give your users access to your AWS account resources\. To learn about using policies to restrict users' permissions to specific AWS resources, go to [Access Management](http://docs.aws.amazon.com/IAM/latest/UserGuide/access.html) and [Example Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_examples.html)\.

## Step 3\.1: Create a DynamoDB Table with a Stream Enabled<a name="with-ddb-create-buckets"></a>

Follow the procedure to create a table with a stream:

1. Sign in to the AWS Management Console and open the DynamoDB console at [https://console\.aws\.amazon\.com/dynamodb/](https://console.aws.amazon.com/dynamodb/)\.

1. In the DynamoDB console, create a table with streams enabled\. For more information on enabling streams, see [Capturing Table Activity with DynamoDB Streams](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.html)\. 
**Important**  
You must create a DynamoDB table in the same region where you created the Lambda function\. This tutorial assumes the US East \(N\. Virginia\) region\. In addition, both the table and the Lambda functions must belong to the same AWS account\.

1. Write down the stream ARN\. You need this in the next step when you associate the stream with your Lambda function\.

## Step 3\.2: Add an Event Source in AWS Lambda<a name="with-ddb-attach-notification-configuration"></a>

Run the following AWS CLI `create-event-source-mapping` command\. After the command executes, note down the UUID\. You'll need this UUID to refer to the event source mapping in any commands, for example, when deleting the event source mapping\.

```
$ aws lambda create-event-source-mapping \
--region us-east-1 \
--function-name ProcessDynamoDBStream \
--event-source DynamoDB-stream-arn \
--batch-size 100 \
--starting-position TRIM_HORIZON \
--profile adminuser
```

**Note**  
 This creates a mapping between the specified DynamoDB stream and the Lambda function\. You can associate a DynamoDB stream with multiple Lambda functions, and associate the same Lambda function with multiple streams\. However, the Lambda functions will share the read throughput for the stream they share\. 

You can get the list of event source mappings by running the following command\.

```
$ aws lambda list-event-source-mappings \
--region us-east-1 \
--function-name ProcessDynamoDBStream \
--event-source DynamoDB-stream-arn \
--profile adminuser
```

The list returns all of the event source mappings you created, and for each mapping it shows the `LastProcessingResult`, among other things\. This field is used to provide an informative message if there are any problems\. Values such as `No records processed` \(indicates that AWS Lambda has not started polling or that there are no records in the stream\) and `OK` \(indicates AWS Lambda successfully read records from the stream and invoked your Lambda function\) indicate that there no issues\. If there are issues, you receive an error message\.

## Step 3\.3: Test the Setup<a name="with-ddb-final-integration-test-no-iam"></a>

You're all done\! Now **adminuser** can test the setup as follows:

1. In the DynamoDB console, add, update, delete items to the table\. DynamoDB writes records of these actions to the stream\.

1. AWS Lambda polls the stream and when it detects updates to the stream, it invokes your Lambda function by passing in the event data it finds in the stream\.

1. Your function executes and creates logs in Amazon CloudWatch\. The **adminuser** can also verify the logs reported in the Amazon CloudWatch console\.