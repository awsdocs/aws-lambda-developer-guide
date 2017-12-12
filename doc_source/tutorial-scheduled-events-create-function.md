# Step 1: Create a Lambda Function<a name="tutorial-scheduled-events-create-function"></a>

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. Choose **Create a Lambda function**\.

1. In **Select blueprint**, choose the **lambda\-canary** blueprint\.

1. In **Configure triggers**:

   + Choose **CloudWatch Events \- Schedule**\.

   + In **Rule name**, type a name \(for example, **CheckWebsiteScheduledEvent**\)\.

   + In **Rule description**, type a description \(for example, **CheckWebsiteScheduledEvent trigger**\)\.

   + In **Schedule expression**, specify **rate\(1 minute\)**\. Note that you can specify the value as a `rate` or in the `cron` expression format\. All schedules use the UTC time zone, and the minimum precision for schedules is one minute\.
**Note**  
When setting a rate expression, the first execution is immediate and subsequent executions occur based on the rate schedule\. In the preceding example, the subsequent execution rate would be every minute\.

     For more information on expressions schedules, see [Schedule Expressions Using Rate or Cron](tutorial-scheduled-events-schedule-expressions.md)\.

      

   + In **Enable trigger**, we recommend that you leave the trigger in a disabled state until you have tested it\. 

   + Choose **Next**\. 

1. In **Configure function**, do the following:

   + Specify your Lambda function name \(for example, **CheckWebsitePeriodically**\)\.

   + In **Runtime**, specify **Python3\.6**, **Python 2\.7**, **Node\.js 6\.10** or **Node\.js 4\.3**, depending on your preferred language\. 

   + Review the code provided by the template\. Later in this tutorial, you will update the function code so that the function will return an error\. You can either specify a non\-existing URL or replace search text to a string that is not on the page\. 

   + In **Role\***, choose **Create new role from template\(s\)**\.

   + In **Role name**, type a name for the role\.

   + In **Policy templates**, Lambda provides a list of optional, additional templates that extend the basic Lambda permissions\. For the purpose of this tutorial, you can leave this field blank because your Lambda function already has the basic execution permission it needs\.

   + In **Advanced settings**, leave the default configurations and choose **Next**\.

1. In **Review**, review the configuration and then choose **Create Function**\.

## Next Step<a name="wt-scheduledevents1-next-step"></a>

[Step 2: Test the Lambda Function \(Using a Sample Test Event\)](tutorial-scheduled-events-test-function.md)