# Create a Lambda Function with the Console<a name="getting-started-create-function"></a>

In this Getting Started exercise you create a Lambda function using the AWS Lambda console\. Next, you manually invoke the Lambda function using sample event data\. AWS Lambda executes the Lambda function and returns results\. You then verify execution results, including the logs that your Lambda function created and various CloudWatch metrics\. 

**To create a Lambda function**

1. Open the [AWS Lambda console](https://console.aws.amazon.com/lambda/home)\.

1. Choose **Create a function**\.

1. For **Function name**, enter **my\-function**\.

1. Choose **Create function**\.

Lambda creates a Node\.js function and an execution role that grants the function permission to upload logs\. Lambda assumes the execution role when you invoke your function, and uses it to create credentials for the AWS SDK and to read data from event sources\.

## Use the Designer<a name="get-started-designer"></a>

The **Designer** lets you configure triggers and view permissions\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-designer.png)

Choose **Amazon CloudWatch Logs** to view the log\-related permissions that the execution role grants the function\. When you add a trigger or configure features that require additional permissions, Lambda modifies to the function's execution role or resource\-based policy to grant the minimum required access\. To view these policies, choose the key icon\.

Choose **my\-function** in the designer to return to the function's code and configuration\. For scripting languages, Lambda includes sample code that returns a success response\. You can edit your function code with the embedded [AWS Cloud9](https://docs.aws.amazon.com/cloud9/latest/user-guide/) editor as long as your source code doesn't exceed the 3 MB limit\.

## Invoke the Lambda Function<a name="get-started-invoke-manually"></a>

Invoke your Lambda function using the sample event data provided in the console\.

**To invoke a function**

1. In the upper right corner, choose **Test**\.

1. In the **Configure test event** page, choose **Create new test event** and in **Event template**, leave the default **Hello World** option\. Enter an **Event name** and note the following sample event template:

   ```
   {
     "key3": "value3",
     "key2": "value2",
     "key1": "value1"
   }
   ```

   You can change key and values in the sample JSON, but don't change the event structure\. If you do change any keys and values, you must update the sample code accordingly\.

1. Choose **Create** and then choose **Test**\. Each user can create up to 10 test events per function\. Those test events are not available to other users\.

1. AWS Lambda executes your function on your behalf\. The `handler` in your Lambda function receives and then processes the sample event\. 

1. Upon successful execution, view results in the console\. 
   + The **Execution result** section shows the execution status as **succeeded** and also shows the function execution results, returned by the `return` statement\.
   + The **Summary** section shows the key information reported in the **Log output** section \(the *REPORT* line in the execution log\)\.
   + The **Log output** section shows the log AWS Lambda generates for each execution\. These are the logs written to CloudWatch by the Lambda function\. The AWS Lambda console shows these logs for your convenience\.

   Note that the **Click here** link shows logs in the CloudWatch console\. The function then adds logs to Amazon CloudWatch in the log group that corresponds to the Lambda function\.

1. Run the Lambda function a few times to gather some metrics that you can view in the next step\.

1. Choose **Monitoring**\. This page shows graphs for the metrics that Lambda sends to CloudWatch\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

   For more information on these graphs, see [Monitoring Functions in the AWS Lambda Console](monitoring-functions-access-metrics.md)\.