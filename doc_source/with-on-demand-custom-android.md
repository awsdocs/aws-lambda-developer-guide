# Invoking Lambda functions with the AWS Mobile SDK for Android<a name="with-on-demand-custom-android"></a>

You can call a Lambda function from a mobile application\. Put business logic in functions to separate its development lifecycle from that of front\-end clients, making mobile applications less complex to develop and maintain\. With the Mobile SDK for Android, you [use Amazon Cognito to authenticate users and authorize requests](with-android-example.md)\.

When you invoke a function from a mobile application, you choose the event structure, [invocation type](lambda-invocation.md), and permission model\. You can use [aliases](configuration-aliases.md) to enable seamless updates to your function code, but otherwise the function and application are tightly coupled\. As you add more functions, you can create an API layer to decouple your function code from your front\-end clients and improve performance\.

To create a fully\-featured web API for your mobile and web applications, use Amazon API Gateway\. With API Gateway, you can add custom authorizers, throttle requests, and cache results for all of your functions\. For more information, see [Using AWS Lambda with Amazon API Gateway](services-apigateway.md)\.

**Topics**
+ [Tutorial: Using AWS Lambda with the Mobile SDK for Android](with-android-example.md)
+ [Sample function code](with-android-create-package.md)