# Configuring Lambda function memory<a name="configuration-memory"></a>

AWS Lambda allocates CPU power in proportion to the amount of memory configured\. *Memory* is the amount of memory available to your function at runtime\. You can increase or decrease the memory and CPU power allocated to your Lambda function using the Memory \(MB\) setting\. To set the memory for your function, enter a value between 128 MB and 10,240 MB in 1\-MB increments\. At 1,769 MB, a function has the equivalent of one vCPU \(one vCPU\-second of credits per second\)\. This page describes how to update the memory allotted to your function on the Lambda console\. 

## Configuring function memory on the Lambda console<a name="configuration-memory-console"></a>

You can configure the memory of your function in a text field on the Lambda console\.

**To update the memory for an existing function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. Choose **Edit** on the **Basic settings** pane\.

1. Enter a value in the text box\.

1. Choose **Save**\.