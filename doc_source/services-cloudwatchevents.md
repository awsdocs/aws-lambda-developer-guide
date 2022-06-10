# Using AWS Lambda with Amazon EventBridge \(CloudWatch Events\)<a name="services-cloudwatchevents"></a>

**Note**  
Amazon EventBridge is the preferred way to manage your events\. CloudWatch Events and EventBridge are the same underlying service and API, but EventBridge provides more features\. Changes you make in either CloudWatch Events or EventBridge will appear in each console\. For more information, see the [Amazon EventBridge documentation](https://docs.aws.amazon.com/eventbridge/index.html)\.

EventBridge \(CloudWatch Events\) helps you to respond to state changes in your AWS resources\. For more information about EventBridge, see [What is Amazon EventBridge?](https://docs.aws.amazon.com/eventbridge/latest/userguide/eb-what-is.html) in the *Amazon EventBridge User Guide*\.

When your resources change state, they automatically send events into an event stream\. With EventBridge \(CloudWatch Events\), you can create rules that match selected events in the stream and route them to your AWS Lambda function to take action\. For example, you can automatically invoke an AWS Lambda function to log the state of an [EC2 instance](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/LogEC2InstanceState.html) or [AutoScaling group](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/LogASGroupState.html)\.

EventBridge \(CloudWatch Events\) invokes your function asynchronously with an event document that wraps the event from its source\. The following example shows an event that originated from a database snapshot in Amazon Relational Database Service\.

**Example EventBridge \(CloudWatch Events\) event**  

```
{
    "version": "0",
    "id": "fe8d3c65-xmpl-c5c3-2c87-81584709a377",
    "detail-type": "RDS DB Instance Event",
    "source": "aws.rds",
    "account": "123456789012",
    "time": "2020-04-28T07:20:20Z",
    "region": "us-east-2",
    "resources": [
        "arn:aws:rds:us-east-2:123456789012:db:rdz6xmpliljlb1"
    ],
    "detail": {
        "EventCategories": [
            "backup"
        ],
        "SourceType": "DB_INSTANCE",
        "SourceArn": "arn:aws:rds:us-east-2:123456789012:db:rdz6xmpliljlb1",
        "Date": "2020-04-28T07:20:20.112Z",
        "Message": "Finished DB Instance backup",
        "SourceIdentifier": "rdz6xmpliljlb1"
    }
}
```

You can also create a Lambda function and direct AWS Lambda to invoke it on a regular schedule\. You can specify a fixed rate \(for example, invoke a Lambda function every hour or 15 minutes\), or you can specify a Cron expression\.

**Example EventBridge \(CloudWatch Events\) message event**  

```
{
  "version": "0",
  "account": "123456789012",
  "region": "us-east-2",
  "detail": {},
  "detail-type": "Scheduled Event",
  "source": "aws.events",
  "time": "2019-03-01T01:23:45Z",
  "id": "cdc73f9d-aea9-11e3-9d5a-835b769c0d9c",
  "resources": [
    "arn:aws:events:us-east-2:123456789012:rule/my-schedule"
  ]
}
```

**To configure EventBridge \(CloudWatch Events\) to invoke your function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function

1. Under **Function overview**, choose **Add trigger**\.

1. Set the trigger type to **EventBridge \(CloudWatch Events\)**\.

1. For **Rule**, choose **Create a new rule**\.

1. Configure the remaining options and choose **Add**\.

For more information on expressions schedules, see [Schedule expressions using rate or cron](services-cloudwatchevents-expressions.md)\.

Each AWS account can have up to 100 unique event sources of the **EventBridge \(CloudWatch Events\)\- Schedule** source type\. Each of these can be the event source for up to five Lambda functions\. That is, you can have up to 500 Lambda functions that can be executing on a schedule in your AWS account\.

**Topics**
+ [Tutorial: Using AWS Lambda with scheduled events](services-cloudwatchevents-tutorial.md)
+ [Schedule expressions using rate or cron](services-cloudwatchevents-expressions.md)