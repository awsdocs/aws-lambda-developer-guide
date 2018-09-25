# Using AWS Lambda with Scheduled Events<a name="with-scheduled-events"></a>

You can create a Lambda function and direct AWS Lambda to execute it on a regular schedule\. You can specify a fixed rate \(for example, execute a Lambda function every hour or 15 minutes\), or you can specify a Cron expression\. For more information on expressions schedules, see [Schedule Expressions Using Rate or Cron](tutorial-scheduled-events-schedule-expressions.md)\. 

This functionality is available when you create a Lambda function using the AWS Lambda console or the AWS CLI\. To configure it using the AWS CLI, see [Run an AWS Lambda Function on a Schedule Using the AWS CLI](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/RunLambdaSchedule.html)\. The console provides **CloudWatch Events** as an event source\. At the time of creating a Lambda function, you choose this event source and specify a time interval\. 

 If you have made any manual changes to the permissions on your function, you may need to reapply the scheduled event access to your function\. You can do that by using the following CLI command\. 

```
aws lambda add-permission \
    --statement-id 'statement_id' \
    --action 'lambda:InvokeFunction' \ 
    --principal events.amazonaws.com \ 
    --source-arn arn:aws:events:region:account-id:rule/rule_name \
    --function-name:function_name \
    --region region
```

**Note**  
Each AWS account can have up to 100 unique event sources of the **CloudWatch Events\- Schedule** source type\. Each of these can be the event source for up to five Lambda functions\. That is, you can have up to 500 Lambda functions that can be executing on a schedule in your AWS account\.

The console also provides a blueprint \(**lambda\-canary**\) that uses the **CloudWatch Events \- Schedule** source type\. Using this blueprint, you can create a sample Lambda function and test this feature\. The example code that the blueprint provides checks for the presence of a specific webpage and specific text string on the webpage\. If either the webpage or the text string is not found, the Lambda function throws an error\. 

For a tutorial that walks you through an example setup, see [Tutorial: Using AWS Lambda with Scheduled Events](with-scheduledevents-example.md)\.