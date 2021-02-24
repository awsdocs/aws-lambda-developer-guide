# Configuring Lambda function memory<a name="configuration-memory"></a>

Lambda allocates CPU power in proportion to the amount of memory configured\. *Memory* is the amount of memory available to your Lambda function at runtime\. You can increase or decrease the memory and CPU power allocated to your function using the **Memory \(MB\)** setting\. To configure the memory for your function, set a value between 128 MB and 10,240 MB in 1\-MB increments\. At 1,769 MB, a function has the equivalent of one vCPU \(one vCPU\-second of credits per second\)\.

This page describes how to update the memory allotted to your function in the Lambda console\.

## Configuring function memory in the Lambda console<a name="configuration-memory-console"></a>

You can configure the memory of your function in the Lambda console\.

**To update the memory of a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. On the [function configuration page](configuration-console.md), on the **Basic settings** pane, choose **Edit**\.

1. For **Memory \(MB\)**, set a value from 128 MB to 10,240 MB\.

1. Choose **Save**\.

## Accepting function memory recommendations on the Lambda console<a name="configuration-memory-optimization-accept"></a>

If you have administrator permissions in AWS Identity and Access Management \(IAM\), you can opt in to receive Lambda function memory setting recommendations from AWS Compute Optimizer\. For instructions on opting in to memory recommendations for your account or organization, see [Opting in your account](https://docs.aws.amazon.com/compute-optimizer/latest/ug/getting-started.html#account-opt-in) in the *AWS Compute Optimizer User Guide*\.

When you've opted in and your [Lambda function meets Compute Optimizer requirements](https://docs.aws.amazon.com/compute-optimizer/latest/ug/requirements.html#requirements-lambda-functions), you can view and accept function memory recommendations from Compute Optimizer in the Lambda console\.

**To accept a function memory recommendation**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose a function\.

1. On the [function configuration page](configuration-console.md), on the **Basic settings** pane, choose **Edit**\.

1. Under **Memory \(MB\)**, in the memory alert, choose **Update**\.

1. Choose **Save**\.