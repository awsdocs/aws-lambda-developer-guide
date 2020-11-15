# Configuring functions in the AWS Lambda console \(preview\)<a name="configuration-preview"></a>


**Public preview**  

|  | 
| --- |
| Public preview The updated AWS Lambda console is in preview release and is subject to change\. The preview is available to AWS accounts on a region by region basis\.  The changes include a new function\-level configuration page, increased visibility into versions and aliases, and a dedicated section for your code\. To provide feedback, visit the updated console \([https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\) and select **Tell us what you think**\. | 

 You can use the Lambda console to configure function settings, add triggers and destinations, and edit and update your code\. The following sections will cover how to perform those workflows in the updated Lambda console preview\.

The text and workflows are subject to change throughout the preview\.

**Topics**
+ [Manage preview settings](#configuration-preview-manage)
+ [Configure function settings](#configuration-preview-settings)
+ [Add and edit triggers and destinations](#configuration-preview-triggers)
+ [Edit and update code](#configuration-preview-code)

## Manage preview settings<a name="configuration-preview-manage"></a>

![\[The toggle to update your console experience.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sidebar.png)

To manage your active console experience, activate the **Updated console \(preview\)** toggle in the console's left\-hand navigation\.

To protect unsaved changes the console experience can only be updated on Lambda console pages where you are not creating or editing a resource\.

The feedback option is available after activating the updated console\. Select **Tell us what you think** at any time to write feedback\. The feedback form will also be provided when you deactivate the updated console preview\. 

## Configure function settings<a name="configuration-preview-settings"></a>

To manage a function, open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions) and choose a function\. From the list of aliases associated with your function, choose the alias that points to the version you want to configure\. This will take you to the function visualization\.

Choose **Latest configuration** to view and manage your version's current configuration\. 

![\[The Latest configuration section of the updated console.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/latest-config.png)

Areas of configuration are broken into five sections, which encapsulate the following:

**General**  
Basic settings  
Asynchronous invocation  
Monitoring tools  
VPC  
Database proxies  
File system  
Concurrency

**Runtime**  
Environment variables  
Layers

**Triggers**  
A list of existing triggers and options to create, enable, disable, fix, and delete triggers\.

**Permissions**  
Execution role   
Resource summary  
Resource\-based policy  
Auditing and compliance

**Destinations**  
A list of existing destinations and options to create, remove, and edit destinations\. 

## Add and edit triggers and destinations<a name="configuration-preview-triggers"></a>

Triggers and destinations now exist as subsets of your function's greater configuration settings\. Both resources are still visible in the function visualization, but are no longer accessed through interacting with it\.

To see your lists of existing triggers or destinations, choose **Latest configuration** when viewing a version of your function and select either **Triggers** or **Destinations** from the left\-hand sections\. 

**To add a new trigger**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions) and choose a function\.

1. From the list of aliases associated with your function, choose the alias that points to the version you want to configure\. 

1. Choose **Latest configuration** and then **Triggers**\.

1. Choose **Add trigger** and select the type of trigger from the list of available options\.

1. The fields associated with that trigger will load in below\. Fill them in and choose **Add**\. 

1. To verify your trigger has been added, choose **Overview** and note the new trigger in the **Function visualization**\.

To enable, disable, fix, or delete a trigger, choose the item in the list of triggers provided when you visit the **Triggers** section of **Latest configuration**\. To reconfigure the trigger, use the event source mapping API commands\.

**To add a new destination**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions) and choose a function\.

1. From the list of aliases associated with your function, choose the alias that points to the version you want to configure\. 

1. Choose **Latest configuration** and then **Destinations**\.

1. Choose **Add destination**\.

1. Fill in the required fields and then choose **Save**\. 

1. To verify your destination has been added, choose **Overview** and note the new destination in the **Function visualization**\.

To edit or remove the destination, choose the item in the list of destinations provided when you choose the **Destinations** section of **Latest configuration**\.

## Edit and update code<a name="configuration-preview-code"></a>

Function code now has its own primary section within your function's details\. To access your function code, open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions) and choose a function\. From the list of aliases associated with your function, choose the alias that points to the version you want to configure\. This will take you to the function visualization\.

Choose **Code** to view and manage your version's current code\. From this view you can edit inline, where possible, or choose **Upload from** to upload from a source\. 

Functionality has not changed for the development environment and test events\.