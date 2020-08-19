# Create a Lambda function with the console<a name="getting-started-create-function"></a>

In this Getting Started exercise you create a Lambda function using the AWS Lambda console\. Next, you manually invoke the Lambda function using sample event data\. AWS Lambda executes the Lambda function and returns results\. You then verify execution results, including the logs that your Lambda function created and various CloudWatch metrics\. 

**To create a Lambda function**

1. Open the [AWS Lambda console](https://console.aws.amazon.com/lambda/home)\.

1. Choose **Create a function**\.

1. For **Function name**, enter **my\-function**\.

1. Choose **Create function**\.

Lambda creates a Node\.js function and an execution role that grants the function permission to upload logs\. Lambda assumes the execution role when you invoke your function, and uses it to create credentials for the AWS SDK and to read data from event sources\.

## Use the designer<a name="get-started-designer"></a>

The **Designer** shows an overview of your function and its upstream and downstream resources\. You can use it to configure triggers, layers, and destinations\.

![\[A Lambda function with an Amazon S3 trigger and Amazon EventBridge destination.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/console-designer.png)

Choose **my\-function** in the designer to return to the function's code and configuration\. For scripting languages, Lambda includes sample code that returns a success response\. You can edit your function code with the embedded [AWS Cloud9](https://docs.aws.amazon.com/cloud9/latest/user-guide/) editor as long as your source code doesn't exceed the 3 MB limit\.

## Invoke the Lambda function<a name="get-started-invoke-manually"></a>

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

1. From the tabs near the top of the page, choose **Monitoring**\. This page shows graphs for the metrics that Lambda sends to CloudWatch\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

   For more information on these graphs, see [Monitoring functions in the AWS Lambda console](monitoring-functions-access-metrics.md)\.

## Clean up<a name="gettingstarted-cleanup"></a>

If you are done working with the example function, delete it\. You can also delete the execution role that the console created, and the log group that stores the function's logs\.

**To delete a Lambda function**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Choose **Actions**, and then choose **Delete function**\.

1. Choose **Delete**\.

**To delete the log group**

1. Open the [Log groups page](https://console.aws.amazon.com/cloudwatch/home#logs:) of the Amazon CloudWatch console\.

1. Choose the function's log group \(`/aws/lambda/my-function`\)\.

1. Choose **Actions**, and then choose **Delete log group**\.

1. Choose **Yes, Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home?#/roles) of the AWS Identity and Access Management console\.

1. Choose the function's role \(`my-function-role-31exxmpl`\)

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

You can automate the creation and cleanup of functions, roles, and log groups with AWS CloudFormation and the AWS CLI\. For fully functional sample applications, see [Lambda sample applications](lambda-samples.md)\.