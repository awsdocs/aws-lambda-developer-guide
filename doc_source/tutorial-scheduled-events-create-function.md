# Step 1: Create a Lambda Function<a name="tutorial-scheduled-events-create-function"></a>

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. Choose **Create function**\.

1. Choose ** blueprints** and then choose the **lambda\-canary** blueprint\.

1. In Basic information, enter a **Name\*** for your function\.

1. In **Role\***, choose **Choose an existing role**\.

1. In **Existing role\***, choose **lambda\_basic\_execution**\.

1. In **cloudWatch Events**, choose the **Rule** list and then choose **Create a new rule**\.

   + In **Rule name**, type a name \(for example, **CheckWebsiteScheduledEvent**\)\.

   + In **Rule description**, type a description \(for example, **CheckWebsiteScheduledEvent trigger**\)\.

   + Choose **Schedule expression** and then specify **rate\(1 minute\)**\. Note that you can specify the value as a `rate` or in the `cron` expression format\. All schedules use the UTC time zone, and the minimum precision for schedules is one minute\.
**Note**  
When setting a rate expression, the first execution is immediate and subsequent executions occur based on the rate schedule\. In the preceding example, the subsequent execution rate would be every minute\.

     For more information on expressions schedules, see [Schedule Expressions Using Rate or Cron](tutorial-scheduled-events-schedule-expressions.md)\.

      

   + In **Enable trigger**, we recommend that you leave the trigger in a disabled state until you have tested it\. 

   + Note the **Lambda function code** section\. This is sample code that you can configure after you create the function\. In addition, the console will also allow you to select runtimes that Lambda supports and add your custom code\.
**Important**  
As mentioned previously, the code provided in the blueprint can be edited once you've created the function\. But also note that it uses `SITE` and `EXPECTED` variables as placeholders for environment variables that you can set, as explained below\.

   + The **Environment variables** section is where you configure settings that you apply to your Lambda function without having to update the function code\. In this case, you can supply a URL value for the **site** key and an expected value to be returned from that site in the **expected** key\. While we strongly recommend populating these values, should you choose not to use environment variables for this function, you will need to clear the **<enter value here>** fields for both **site** and **expected** fields prior to creating your function\. You will also need to update the sample function code to replace the `SITE` and `EXPECTED` variables with literal values of your choice\.

   + Choose **Create function**\. 
**Note**  
Once you have created your Lambda function, you can also add to or update the environment variables section to suit your function's requirements\. For more information, see [Environment Variables](env_variables.md)\.

## Next Step<a name="wt-scheduledevents1-next-step"></a>

[Step 2: Test the Lambda Function \(Using a Sample Test Event\)](tutorial-scheduled-events-test-function.md)