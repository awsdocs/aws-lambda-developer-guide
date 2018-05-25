# Tutorial: Using AWS Lambda with Scheduled Events<a name="with-scheduledevents-example"></a>

In this tutorial, you do the following:
+ Create a Lambda function using the **lambda\-canary** blueprint\. You configure the Lambda function to run every minute\. Note that if the function returns an error, AWS Lambda logs error metrics to CloudWatch\. 
+ Configure a CloudWatch alarm on the `Errors` metric of your Lambda function to post a message to your Amazon SNS topic when AWS Lambda emits error metrics to CloudWatch\. You subscribe to the Amazon SNS topics to get email notification\. In this tutorial, you do the following to set this up:
  + Create an Amazon SNS topic\.
  + Subscribe to the topic so you can get email notifications when a new message is posted to the topic\.
  + In Amazon CloudWatch, set an alarm on the `Errors` metric of your Lambda function to publish a message to your SNS topic when errors occur\.

## Next Step<a name="wt-scheduledevents-next-step"></a>

[Step 1: Create a Lambda Function](tutorial-scheduled-events-create-function.md)