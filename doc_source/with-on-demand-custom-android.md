# Using AWS Lambda as an Android Mobile Application Backend<a name="with-on-demand-custom-android"></a>

You can use AWS Lambda to host backend logic for mobile applications\. That is, some of your mobile app code can be run as Lambda functions\. This allows you to put minimal logic in the mobile application itself making it easy to scale and update \(for example, you only apply code updates to the Lambda function, instead of having to deploy code updates in your app clients\)\. 

After you create the Lambda function, you can invoke it from your mobile app using AWS Mobile SDKs, such as the AWS SDK for Android\. For more information, see [Tools for Amazon Web Services](https://aws.amazon.com/tools/)\. 

**Note**  
You can also invoke your Lambda function over HTTP using Amazon API Gateway, instead of using the AWS SDK\. Amazon API Gateway adds an additional layer between your mobile users and your app logic that enable the following:  
Ability to throttle individual users or requests\. 
Protect against Distributed Denial of Service attacks\.
Provide a caching layer to cache response from your Lambda function\. 
For more information, see [Using AWS Lambda with Amazon API Gateway](with-on-demand-https.md)\.

Note the following about how the mobile application and AWS Lambda integration works: 
+ **Push\-event model** – This is a model \(see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\), where the app invokes the Lambda function by passing the event data as parameter\. 
+ **Synchronous or asynchronous invocation** – The app can invoke the Lambda function and get a response back in real time by specifying `RequestResponse` as the invocation type \(or use the `Event` invocation type for asynchronous invocation\)\. For information about invocation types, see [Invocation Types](invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is defined by your application, and your Lambda function is the custom code written to process the specific event type\. 

There are two types of permissions policies that you work with in setting the end\-to\-end experience:
+ **Permissions for your Lambda function** – Regardless of what invokes a Lambda function, AWS Lambda executes the function by assuming the IAM role \(execution role\) that you specify at the time you create the Lambda function\. Using the permissions policy associated with this role, you grant your Lambda function the permissions that it needs\. For example, if your Lambda function needs to read an object, you grant permissions for the relevant Amazon S3 actions in the permissions policy\. For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.
+ **Permissions for the mobile app to invoke your Lambda function** – The application must have valid security credentials and permissions to invoke a Lambda function\. For mobile applications, you can use the Amazon Cognito service to manage user identities, authentication, and permissions\. 

The following diagram illustrates the application flow \(the illustration assumes a mobile app using AWS Mobile SDK for Android to make the API calls\):

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-android.png)

1. The mobile application retrieves AWS credentials from an Amazon Cognito identity pool\. The role associated with the identitiy pool gives the application permission to use AWS Lambda\.

1. The mobile application invokes the Lambda function\.

1. AWS Lambda returns results to the mobile application\.

**Topics**
+ [Tutorial: Using AWS Lambda as Mobile Application Backend](with-android-example.md)
+ [Sample Function Code](with-android-create-package.md)