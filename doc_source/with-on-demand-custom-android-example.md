# Tutorial: Using AWS Lambda as Mobile Application Backend<a name="with-on-demand-custom-android-example"></a>

In this tutorial, you create a simple Android mobile application\. The primary purpose of this tutorial is to show you how to hook up various components to enable an Android mobile application to invoke a Lambda function and process response\. The app itself is simple, we will assume following:
+ The sample mobile application will generate event data consisting of a name \(first name and last name\) in this format:

  ```
  { firstName: 'value1', lastName: 'value2' }  
  ```
+ You use Lambda function to process the event\. That is, the app \(using the AWS Mobile SDK for Android\) invokes a Lambda function \(`ExampleAndroidEventProcessor`\) by passing the event data to it\. The Lambda function in this tutorial does the following:
  + Logs incoming event data to Amazon CloudWatch Logs\.
  + Upon successful execution, returns a simple string in the response body\. Your mobile app displays the message using the Android `Toast` class\. 

**Note**  
The way that the mobile application invokes a Lambda function as shown in this tutorial is an example of the AWS Lambda request\-response model in which an application invokes a Lambda function and then receives a response in real time\. For more information, see [Programming Model](programming-model-v2.md)\.

## Implementation Summary<a name="with-on-demand-custom-android-example-impl-summary"></a>

The tutorial is divided into two main sections:
+  First, you perform the necessary setup to create a Lambda function and test it by invoking it manually using sample event data \(you don't need mobile app to test your Lambda function\)\. 
+  Second, you create an Amazon Cognito identity pool to manage authentication and permissions, and create the example Android application\. Then, you run the application and it invokes the Lambda function\. You can then verify the end\-to\-end experience\. In this tutorial example:
  + You use the Amazon Cognito service to manage user identities, authentication, and permissions\. The mobile application must have valid security credentials and permissions to invoke a Lambda function\. As part of the application setup, you create an Amazon Cognito identity pool to store user identities and define permissions\. For more information, see [Amazon Cognito](https://aws.amazon.com/cognito/)
  + This mobile application does not require its users to log in\. A mobile application can require its users to log in using public identity providers such as Amazon and Facebook\. The scope of this tutorial is limited and assumes that the mobile application users are unauthenticated\. Therefore, when you configure Amazon Cognito identity pool you will do the following:
    + Enable access for unauthenticated identities\.

      Amazon Cognito provides a unique identifier and temporary AWS credentials for these users to invoke the Lambda function\.
    + In the access permissions policy associated with the IAM role for unauthenticated users, add permissions to invoke the Lambda function\. An identity pool has two associated IAM roles, one for authenticated and one for unauthenticated application users\. In this example, Amazon Cognito assumes the role for unauthenticated users to obtain temporary credentials\. When the app uses these temporary credentials to invoke your Lambda function, it can do so only if has necessary permissions \(that is, credentials may be valid, but you also need permissions\)\. You do this by updating the permissions policy that Amazon Cognito uses to obtain the temporary credentials\.

The following diagram illustrates the application flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-android.png)

Now you are ready to start the tutorial\. 

## Next Step<a name="with-on-demand-custom-android-example-impl-summary-next-step"></a>

[Step 1: Prepare](with-on-demand-custom-android-example-prepare.md)