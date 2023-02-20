# Lambda function URLs<a name="lambda-urls"></a>

A function URL is a dedicated HTTP\(S\) endpoint for your Lambda function\. You can create and configure a function URL through the Lambda console or the Lambda API\. When you create a function URL, Lambda automatically generates a unique URL endpoint for you\. Once you create a function URL, its URL endpoint never changes\. Function URL endpoints have the following format:

```
https://<url-id>.lambda-url.<region>.on.aws
```

Function URLs are dual stack\-enabled, supporting IPv4 and IPv6\. After you configure a function URL for your function, you can invoke your function through its HTTP\(S\) endpoint via a web browser, curl, Postman, or any HTTP client\.

**Note**  
You can access your function URL through the public Internet only\. While Lambda functions do support AWS PrivateLink, function URLs do not\.

Lambda function URLs use [resource\-based policies](access-control-resource-based.md) for security and access control\. Function URLs also support cross\-origin resource sharing \(CORS\) configuration options\.

You can apply function URLs to any function alias, or to the `$LATEST` unpublished function version\. You can't add a function URL to any other function version\.

**Topics**
+ [Creating and managing Lambda function URLs](urls-configuration.md)
+ [Security and auth model for Lambda function URLs](urls-auth.md)
+ [Invoking Lambda function URLs](urls-invocation.md)
+ [Monitoring Lambda function URLs](urls-monitoring.md)
+ [Tutorial: Creating a Lambda function with a function URL](urls-tutorial.md)