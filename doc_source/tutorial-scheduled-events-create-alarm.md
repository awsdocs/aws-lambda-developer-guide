# Step 4: Configure a CloudWatch Alarm<a name="tutorial-scheduled-events-create-alarm"></a>

To configure a CloudWatch alarm, follow the instructions at [Create Alarm](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/ConsoleAlarms.html)  in the  *Amazon CloudWatch User Guide*\. As you follow the steps, note the following:
+ In **Create Alarm** \(**1\. Select Metric** step\), choose **Lambda Metrics**, and then choose the **Errors** \(**Metric Name** is **Errors**\) for the Lambda function you created\. Also, on the statistics drop\-down, change the settings from **Average** to **Sum** statistics\.
+ In **Create Alarm** \(**2\. Define Metric** step\), set the alarm threshold to **Whenever: Errors is >= 1** and select your Amazon SNS topic from the **Send notification to:** list\.

## Next Step<a name="wt-scheduledevents4-next-step"></a>

[Step 5: Test the Lambda Function Again](tutorial-scheduled-events-test-function-again.md)