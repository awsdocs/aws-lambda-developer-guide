# Step 3: Create an Amazon SNS Topic and Subscribe to It<a name="tutorial-scheduled-events-subscribe-sns"></a>

1.  Create an SNS topic using the Amazon SNS console\. For instructions, see  [Create a Topic](https://docs.aws.amazon.com/sns/latest/dg/CreateTopic.html) in the *Amazon Simple Notification Service Developer Guide*\. 

1. Subscribe to the topic\. For this exercise, use email as the communication protocol\. For instructions, see  [Subscribe to a Topic](https://docs.aws.amazon.com/sns/latest/dg/SubscribeTopic.html) in the *Amazon Simple Notification Service Developer Guide*\. 

 You use this Amazon SNS topic in the next step when you configure a CloudWatch alarm so that when AWS Lambda emits an error the alarm will publish a notification to this topic\.

## Next Step<a name="wt-scheduledevents3-next-step"></a>

[Step 4: Configure a CloudWatch Alarm](tutorial-scheduled-events-create-alarm.md)