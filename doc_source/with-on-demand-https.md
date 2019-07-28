# Using AWS Lambda with Amazon API Gateway<a name="with-on-demand-https"></a>

You can invoke AWS Lambda functions over HTTPS\. You can do this by defining a custom REST API and endpoint using [Amazon API Gateway](https://aws.amazon.com/api-gateway/), and then mapping individual methods, such as `GET` and `PUT`, to specific Lambda functions\. Alternatively, you could add a special method named ANY to map all supported methods \(`GET`, `POST`, `PATCH`, `DELETE`\) to your Lambda function\. When you send an HTTPS request to the API endpoint, the Amazon API Gateway service invokes the corresponding Lambda function\.

Amazon API Gateway invokes your function [synchronously](invocation-sync.md) with an event that contains details about the HTTP request that it received\.

**Example Amazon API Gateway Message Event**  

```
{
  "path": "/test/hello",
  "headers": {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, lzma, sdch, br",
    "Accept-Language": "en-US,en;q=0.8",
    "CloudFront-Forwarded-Proto": "https",
    "CloudFront-Is-Desktop-Viewer": "true",
    "CloudFront-Is-Mobile-Viewer": "false",
    "CloudFront-Is-SmartTV-Viewer": "false",
    "CloudFront-Is-Tablet-Viewer": "false",
    "CloudFront-Viewer-Country": "US",
    "Host": "wt6mne2s9k.execute-api.us-west-2.amazonaws.com",
    "Upgrade-Insecure-Requests": "1",
    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36 OPR/39.0.2256.48",
    "Via": "1.1 fb7cca60f0ecd82ce07790c9c5eef16c.cloudfront.net (CloudFront)",
    "X-Amz-Cf-Id": "nBsWBOrSHMgnaROZJK1wGCZ9PcRcSpq_oSXZNQwQ10OTZL4cimZo3g==",
    "X-Forwarded-For": "192.168.100.1, 192.168.1.1",
    "X-Forwarded-Port": "443",
    "X-Forwarded-Proto": "https"
  },
  "pathParameters": {
    "proxy": "hello"
  },
  "requestContext": {
    "accountId": "123456789012",
    "resourceId": "us4z18",
    "stage": "test",
    "requestId": "41b45ea3-70b5-11e6-b7bd-69b5aaebc7d9",
    "identity": {
      "cognitoIdentityPoolId": "",
      "accountId": "",
      "cognitoIdentityId": "",
      "caller": "",
      "apiKey": "",
      "sourceIp": "192.168.100.1",
      "cognitoAuthenticationType": "",
      "cognitoAuthenticationProvider": "",
      "userArn": "",
      "userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36 OPR/39.0.2256.48",
      "user": ""
    },
    "resourcePath": "/{proxy+}",
    "httpMethod": "GET",
    "apiId": "wt6mne2s9k"
  },
  "resource": "/{proxy+}",
  "httpMethod": "GET",
  "queryStringParameters": {
    "name": "me"
  },
  "stageVariables": {
    "stageVarName": "stageVarValue"
  }
}
```

Amazon API Gateway also adds a layer between your application users and your app logic that enables the following: 
+ Ability to throttle individual users or requests\. 
+ Protect against Distributed Denial of Service attacks\.
+ Provide a caching layer to cache response from your Lambda function\. 

Note that there are two types of permissions policies that you work with when you set up the end\-to\-end experience:
+ **Permissions for your Lambda function** – Regardless of what invokes a Lambda function, AWS Lambda executes the function by assuming the IAM role \(execution role\) that you specify at the time you create the Lambda function\. Using the permissions policy associated with this role, you grant your Lambda function the permissions that it needs\. For example, if your Lambda function needs to read an object, you grant permissions for the relevant Amazon S3 actions in the permissions policy\. For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.
+ **Permission for Amazon API Gateway to invoke your Lambda function** – Amazon API Gateway cannot invoke your Lambda function without your permission\. You grant this permission via the permission policy associated with the Lambda function\.