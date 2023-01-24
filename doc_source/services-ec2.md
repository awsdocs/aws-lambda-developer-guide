# Using AWS Lambda with Amazon EC2<a name="services-ec2"></a>

You can use AWS Lambda to process lifecycle events from Amazon Elastic Compute Cloud and manage Amazon EC2 resources\. Amazon EC2 sends events to Amazon EventBridge \(CloudWatch Events\) for lifecycle events such as when an instance changes state, when an Amazon Elastic Block Store volume snapshot completes, or when a spot instance is scheduled to be terminated\. You configure EventBridge \(CloudWatch Events\) to forward those events to a Lambda function for processing\.

EventBridge \(CloudWatch Events\) invokes your Lambda function asynchronously with the event document from Amazon EC2\.

**Example instance lifecycle event**  

```
{
    "version": "0",
    "id": "b6ba298a-7732-2226-xmpl-976312c1a050",
    "detail-type": "EC2 Instance State-change Notification",
    "source": "aws.ec2",
    "account": "111122223333",
    "time": "2019-10-02T17:59:30Z",
    "region": "us-east-2",
    "resources": [
        "arn:aws:ec2:us-east-2:111122223333:instance/i-0c314xmplcd5b8173"
    ],
    "detail": {
        "instance-id": "i-0c314xmplcd5b8173",
        "state": "running"
    }
}
```

For details on configuring events in EventBridge \(CloudWatch Events\), see [Using AWS Lambda with Amazon EventBridge \(CloudWatch Events\)](services-cloudwatchevents.md)\. For an example function that processes Amazon EBS snapshot notifications, see [Amazon EventBridge \(CloudWatch Events\) for Amazon EBS](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ebs-cloud-watch-events.html) in the Amazon EC2 User Guide for Linux Instances\.

You can also use the AWS SDK to manage instances and other resources with the Amazon EC2 API\.  

## Permissions<a name="services-ec2-permissions"></a>

To process lifecycle events from Amazon EC2, EventBridge \(CloudWatch Events\) needs permission to invoke your function\. This permission comes from the function's [resource\-based policy](access-control-resource-based.md)\. If you use the EventBridge \(CloudWatch Events\) console to configure an event trigger, the console updates the resource\-based policy on your behalf\. Otherwise, add a statement like the following:

**Example resource\-based policy statement for Amazon EC2 lifecycle notifications**  

```
{
  "Sid": "ec2-events",
  "Effect": "Allow",
  "Principal": {
    "Service": "events.amazonaws.com"
  },
  "Action": "lambda:InvokeFunction",
  "Resource": "arn:aws:lambda:us-east-2:12456789012:function:my-function",
  "Condition": {
    "ArnLike": {
      "AWS:SourceArn": "arn:aws:events:us-east-2:12456789012:rule/*"
    }
  }
}
```

To add a statement, use the `add-permission` AWS CLI command\.

```
aws lambda add-permission --action lambda:InvokeFunction --statement-id ec2-events \
--principal events.amazonaws.com --function-name my-function --source-arn 'arn:aws:events:us-east-2:12456789012:rule/*'
```

If your function uses the AWS SDK to manage Amazon EC2 resources, add Amazon EC2 permissions to the function's [execution role](lambda-intro-execution-role.md)\.