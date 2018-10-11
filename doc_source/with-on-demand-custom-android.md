# Using AWS Lambda as Mobile Application Backend \(Custom Event Source: Android\)<a name="with-on-demand-custom-android"></a>

You can use AWS Lambda to host backend logic for mobile applications\. That is, some of your mobile app code can be run as Lambda functions\. This allows you to put minimal logic in the mobile application itself making it easy to scale and update \(for example, you only apply code updates to the Lambda function, instead of having to deploy code updates in your app clients\)\. 

After you create the Lambda function, you can invoke it from your mobile app using AWS Mobile SDKs, such as the AWS SDK for Android\. For more information, see [Tools for Amazon Web Services](https://aws.amazon.com/tools/)\. 

**Note**  
You can also invoke your Lambda function over HTTP using Amazon API Gateway \(instead of using any of the AWS SDKs\)\. Amazon API Gateway adds an additional layer between your mobile users and your app logic that enable the following:  
Ability to throttle individual users or requests\. 
Protect against Distributed Denial of Service attacks\.
Provide a caching layer to cache response from your Lambda function\. 

Note the following about how the mobile application and AWS Lambda integration works: 
+ **Push\-event model** – This is a model \(see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\), where the app invokes the Lambda function by passing the event data as parameter\. 
+ **Synchronous or asynchronous invocation** – The app can invoke the Lambda function and get a response back in real time by specifying `RequestResponse` as the invocation type \(or use the `Event` invocation type for asynchronous invocation\)\. For information about invocation types, see [Manage Permissions: Using a Lambda Function Policy](intro-permission-model.md#intro-permission-model-access-policy)\. 
+ **Event structure** – The event your Lambda function receives is defined by your application, and your Lambda function is the custom code written to process the specific event type\. 

Note that there are two types of permissions policies that you work with in setting the end\-to\-end experience:
+ **Permissions for your Lambda function** – Regardless of what invokes a Lambda function, AWS Lambda executes the function by assuming the IAM role \(execution role\) that you specify at the time you create the Lambda function\. Using the permissions policy associated with this role, you grant your Lambda function the permissions that it needs\. For example, if your Lambda function needs to read an object, you grant permissions for the relevant Amazon S3 actions in the permissions policy\. For more information, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.
+ **Permissions for the mobile app to invoke your Lambda function** – The application must have valid security credentials and permissions to invoke a Lambda function\. For mobile applications, you can use the Amazon Cognito service to manage user identities, authentication, and permissions\. 

The following diagram illustrates the application flow \(the illustration assumes a mobile app using AWS Mobile SDK for Android to make the API calls\):

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-android.png)

1. The mobile application sends a request to Amazon Cognito with an identity pool ID in the request \(you create the identity pool as part the setup\)\.

1. Amazon Cognito returns temporary security credentials back to the application\. 

   Amazon Cognito assumes the role associated with the identity pool to generate temporary credentials\. What the application can do using the credentials is limited to the permissions defined in the permissions policy associated with the role Amazon Cognito used in obtaining the temporary credential\. 
**Note**  
The AWS SDK can cache the temporary credentials so that the application does not send a request to Amazon Cognito each time it needs to invoke a Lambda function\.

1. The mobile application invokes the Lambda function using temporary credentials \(Cognito Identity\)\.

1. AWS Lambda assumes the execution role to execute your Lambda function on your behalf\.

1. The Lambda function executes\.

1. AWS Lambda returns results to the mobile application, assuming the app invoked the Lambda function using the `RequestResponse` invocation type \(synchronous invocation\)\.

For a tutorial that walks you through an example setup, see [Tutorial: Using AWS Lambda as Mobile Application Backend](with-on-demand-custom-android-example.md)\.