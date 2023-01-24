# Setting up Amazon CodeWhisperer with Lambda<a name="codewhisperer-setup"></a>

This document describes how to request access to and activate Amazon CodeWhisperer for the Lambda console\. Once activated, CodeWhisperer can make code recommendations on\-demand in the Lambda code editor as you develop your function\.

**Note**  
In the Lambda console, CodeWhisperer only supports functions using the Python and Node\.js runtimes\.

**Topics**
+ [Requesting CodeWhisperer access \(experimental feature\)](#codewhisperer-request-access)
+ [Activating Amazon CodeWhisperer \(experimental feature\)](#codewhisperer-activate)

## Requesting CodeWhisperer access \(experimental feature\)<a name="codewhisperer-request-access"></a>

To use Amazon CodeWhisperer in the Lambda console, you must first get allow listed\. Request allow listing by completing these steps\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console, and choose the function that you want to edit\.

1. In the code editor under **Code source**, choose **Tools** in the top menu bar\.

1. Choose **Request CodeWhisperer access**\. This opens up a link to a [request form](https://pages.awscloud.com/codewhisperer-sign-up-form.html) in a new tab\.

1. Fill out the form\. For Lambda users, you must fill out the **AWS Account ID** field\.

You will receive an email when the CodeWhisperer team approves your request\.

## Activating Amazon CodeWhisperer \(experimental feature\)<a name="codewhisperer-activate"></a>

**Note**  
You must be allow listed to use CodeWhisperer in the Lambda console\. If you aren't allow listed, see [Requesting CodeWhisperer access \(experimental feature\)](#codewhisperer-request-access)\.

To activate CodeWhisperer in the Lambda console code editor, complete these steps\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console, and choose the function that you want to edit\.

1. In the code editor under **Code source**, choose **Tools** in the top menu bar\.

1. Choose **CodeWhisperer code suggestions**\. This immediately activates the CodeWhisperer service, and a check mark appears next to this option\. To deactivate, choose this option again\.

The first time you activate CodeWhisperer, you'll see a pop\-up containing terms and conditions for using CodeWhisperer\. Read the terms and conditions and choose **Accept** to continue\.