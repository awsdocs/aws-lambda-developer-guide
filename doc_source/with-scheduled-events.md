# Using AWS Lambda with Amazon CloudWatch Events<a name="with-scheduled-events"></a>

[Amazon CloudWatch Events](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/WhatIsCloudWatchEvents.html) help you to respond to state changes in your AWS resources\. When your resources change state, they automatically send events into an event stream\. You can create rules that match selected events in the stream and route them to your AWS Lambda function to take action\. For example, you can automatically invoke an AWS Lambda function to log the state of an [EC2 instance](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/LogEC2InstanceState.html) or [AutoScaling Group](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/LogASGroupState.html)\. 

You maintain event source mapping in Amazon CloudWatch Events by using a rule target definition\. For more information, see the [PutTargets](https://docs.aws.amazon.com/AmazonCloudWatchEvents/latest/APIReference/API_PutTargets.html) operation in the *Amazon CloudWatch Events API Reference*\.

You can also create a Lambda function and direct AWS Lambda to execute it on a regular schedule\. You can specify a fixed rate \(for example, execute a Lambda function every hour or 15 minutes\), or you can specify a Cron expression\. For more information on expressions schedules, see [Schedule Expressions Using Rate or Cron](tutorial-scheduled-events-schedule-expressions.md)\.

**Example CloudWatch Events Message Event**  

```
{
  "account": "123456789012",
  "region": "us-east-2",
  "detail": {},
  "detail-type": "Scheduled Event",
  "source": "aws.events",
  "time": "2019-03-01T01:23:45Z",
  "id": "cdc73f9d-aea9-11e3-9d5a-835b769c0d9c",
  "resources": [
    "arn:aws:events:us-east-1:123456789012:rule/my-schedule"
  ]
}
```

This functionality is available when you create a Lambda function using the AWS Lambda console or the AWS CLI\. To configure it using the AWS CLI, see [Run an AWS Lambda Function on a Schedule Using the AWS CLI](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/RunLambdaSchedule.html)\. The console provides **CloudWatch Events** as an event source\. At the time of creating a Lambda function, you choose this event source and specify a time interval\. 

 If you have made any manual changes to the permissions on your function, you may need to reapply the scheduled event access to your function\. You can do that by using the following CLI command\. 

```
$ aws lambda add-permission --function-name my-function\
    --action 'lambda:InvokeFunction' --principal events.amazonaws.com --statement-id events-access \
    --source-arn arn:aws:events:*:123456789012:rule/*
```

Each AWS account can have up to 100 unique event sources of the **CloudWatch Events\- Schedule** source type\. Each of these can be the event source for up to five Lambda functions\. That is, you can have up to 500 Lambda functions that can be executing on a schedule in your AWS account\.

The console also provides a blueprint \(**lambda\-canary**\) that uses the **CloudWatch Events \- Schedule** source type\. Using this blueprint, you can create a sample Lambda function and test this feature\. The example code that the blueprint provides checks for the presence of a specific webpage and specific text string on the webpage\. If either the webpage or the text string is not found, the Lambda function throws an error\. 