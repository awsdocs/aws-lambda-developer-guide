# Step 2: Test the Lambda Function \(Using a Sample Test Event\)<a name="tutorial-scheduled-events-test-function"></a>

1. Choose the function you created in the previous step and then choose **Test**\.

1. On the **Input sample event** page, choose **Scheduled Event** in the **Sample event** list\.

   Note the event time in the sample event\. This value will be different when AWS Lambda invokes the function at scheduled intervals\. The sample Lambda function code logs this time to CloudWatch Logs\. 

1. Choose **Save and test** and verify that the **Execution result** section shows success\.

## Next Step<a name="wt-scheduledevents2-next-step"></a>

[Step 3: Create an Amazon SNS Topic and Subscribe to It](tutorial-scheduled-events-subscribe-sns.md)