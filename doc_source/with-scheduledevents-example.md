# Tutorial: Using AWS Lambda with Scheduled Events<a name="with-scheduledevents-example"></a>

In this tutorial, you do the following:
+ Create a Lambda function using the **lambda\-canary** blueprint\. You configure the Lambda function to run every minute\. Note that if the function returns an error, AWS Lambda logs error metrics to CloudWatch\. 
+ Configure a CloudWatch alarm on the `Errors` metric of your Lambda function to post a message to your Amazon SNS topic when AWS Lambda emits error metrics to CloudWatch\. You subscribe to the Amazon SNS topics to get email notification\. In this tutorial, you do the following to set this up:
  + Create an Amazon SNS topic\.
  + Subscribe to the topic so you can get email notifications when a new message is posted to the topic\.
  + In Amazon CloudWatch, set an alarm on the `Errors` metric of your Lambda function to publish a message to your SNS topic when errors occur\.

## Prerequisites<a name="with-scheduled-events-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started with AWS Lambda](getting-started.md) to create your first Lambda function\.

## Create a Lambda Function<a name="tutorial-scheduled-events-create-function"></a>

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. Choose **Create function**\.

1. Choose **Blueprints**\.

1. Enter **canary** in the search bar\. Choose the **lambda\-canary** blueprint and then choose **Configure**\.

1. Configure the following settings\.
   + **Name** – **lambda\-canary**\.
   + **Role** – **Create a new role from one or more templates**\.
   + **Role name** – **lambda\-apigateway\-role**\.
   + **Policy templates** – **Simple microservice permissions**\.
   + **Rule** – **Create a new rule**\.
   + **Rule name** – **CheckWebsiteScheduledEvent**\.
   + **Rule description** – **CheckWebsiteScheduledEvent trigger**\.
   + **Schedule expression** – **rate\(1 minute\)**\.
   + **Enabled** – True \(checked\)\.
   + **Environment variables**
     + **site** – **https://docs\.aws\.amazon\.com/lambda/latest/dg/welcome\.html**
     + **expected** – **What Is AWS Lambda?**

1. Choose **Create function**\.

CloudWatch Events emits an event every minute, based on the schedule expression\. The event triggers the Lambda function, which verifies that the expected string appears in the specified page\. For more information on expressions schedules, see [Schedule Expressions Using Rate or Cron](tutorial-scheduled-events-schedule-expressions.md)\.

## Test the Lambda Function<a name="tutorial-scheduled-events-test-function"></a>

Test the function with a sample event provided by the Lambda console\.

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose **lambda\-canary**\.

1. Next to the **Test** button at the top of the page, choose **Configure test events** from the drop\-down menu\.

1. Create a new event using the **CloudWatch Events** event template\.

1. Choose **Create**\.

1. Choose **Test**\.

The output from the function execution is shown at the top of the page\.

## Create an Amazon SNS Topic and Subscribe to It<a name="tutorial-scheduled-events-subscribe-sns"></a>

Create an Amazon Simple Notification Service topic to receive notifications when the canary function returns an error\.

**To create a topic**

1. Open the [Amazon SNS console](https://console.aws.amazon.com/sns)\.

1. Choose **Create topic**\.

1. Create a topic with the following settings\.
   + **Name** – **lambda\-canary\-notifications**\.
   + **Display name** – **Canary**\.

1. Choose **Create subscription**\.

1. Create a subscription with the following settings\.
   + **Protocol** – **Email**\.
   + **Endpoint** – Your email address\.

Amazon SNS sends an email from `Canary <no-reply@sns.amazonaws.com>`, reflecting the friendly name of the topic\. Use the link in the email to confirm your address\.

## Configure an Alarm<a name="tutorial-scheduled-events-create-alarm"></a>

Configure an alarm in Amazon CloudWatch that monitors the Lambda function and sends a notification when it fails\.

**To create an alarm**

1. Open the [CloudWatch console](https://console.aws.amazon.com/cloudwatch)\.

1. Choose **Alarms**\.

1. Choose **Create alarm**\.

1. Choose **Alarms**\.

1. Create an alarm with the following settings\.
   + **Metrics** – **lambda\-canary Errors**\.

     Search for **lambda canary errors** to find the metric\.
   + Statistic – **Sum**\.

     Choose the statistic from the drop down above the preview graph\.
   + **Name** – **lambda\-canary\-alarm**\.
   + **Description** – **Lambda canary alarm**\.
   + Threshold – **Whenever Errors is >=****1**\.
   + **Send notification to** – **lambda\-canary\-notifications**

## Test the Alarm<a name="tutorial-scheduled-events-test-function-again"></a>

Update the function configuration to cause the function to return an error, triggering the alarm\.

**To trigger an alarm**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose **lambda\-canary**\.

1. Under **Environment variables**, set **expected** to **404**\.

1. Choose **Save**

Wait a minute, and then check your email for a message from Amazon SNS