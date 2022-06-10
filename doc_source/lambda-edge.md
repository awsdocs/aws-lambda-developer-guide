# Using AWS Lambda with CloudFront Lambda@Edge<a name="lambda-edge"></a>

Lambda@Edge lets you run Node\.js and Python Lambda functions to customize content that CloudFront delivers, executing the functions in AWS locations closer to the viewer\. The functions run in response to CloudFront events, without provisioning or managing servers\. You can use Lambda functions to change CloudFront requests and responses at the following points:
+ After CloudFront receives a request from a viewer \(viewer request\)
+ Before CloudFront forwards the request to the origin \(origin request\)
+ After CloudFront receives the response from the origin \(origin response\)
+ Before CloudFront forwards the response to the viewer \(viewer response\)

![\[Conceptual graphic that shows how the CloudFront events that can trigger a Lambda function.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/cloudfront-events-that-trigger-lambda-functions.png)

**Note**  
Lambda@Edge supports a limited set of runtimes and features\. For details, see [Requirements and restrictions on Lambda functions](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-requirements-limits.html) in the Amazon CloudFront developer guide\.

You can also generate responses to viewers without ever sending the request to the origin\.

**Example CloudFront message event**  

```
{
  "Records": [
    {
      "cf": {
        "config": {
          "distributionId": "EDFDVBD6EXAMPLE"
        },
        "request": {
          "clientIp": "2001:0db8:85a3:0:0:8a2e:0370:7334",
          "method": "GET",
          "uri": "/picture.jpg",
          "headers": {
            "host": [
              {
                "key": "Host",
                "value": "d111111abcdef8.cloudfront.net"
              }
            ],
            "user-agent": [
              {
                "key": "User-Agent",
                "value": "curl/7.51.0"
              }
            ]
          }
        }
      }
    }
  ]
}
```

With Lambda@Edge, you can build a variety of solutions, for example:
+ Inspect cookies to rewrite URLs to different versions of a site for A/B testing\.
+ Send different objects to your users based on the `User-Agent` header, which contains information about the device that submitted the request\. For example, you can send images in different resolutions to users based on their devices\.
+ Inspect headers or authorized tokens, inserting a corresponding header and allowing access control before forwarding a request to the origin\.
+ Add, delete, and modify headers, and rewrite the URL path to direct users to different objects in the cache\.
+ Generate new HTTP responses to do things like redirect unauthenticated users to login pages, or create and deliver static webpages right from the edge\. For more information, see [Using Lambda functions to generate HTTP responses to viewer and origin requests](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/http-response-generation.html) in the *Amazon CloudFront Developer Guide*\.

For more information about using Lambda@Edge, see [Using CloudFront with Lambda@Edge](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-at-the-edge.html)\. 