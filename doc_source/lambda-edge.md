# Lambda@Edge<a name="lambda-edge"></a>

Lambda@Edge lets you run Lambda functions to customize content that CloudFront delivers, executing the functions in AWS locations closer to the viewer\. The functions run in response to CloudFront events, without provisioning or managing servers\. You can use Lambda functions to change CloudFront requests and responses at the following points:
+ After CloudFront receives a request from a viewer \(viewer request\)
+ Before CloudFront forwards the request to the origin \(origin request\)
+ After CloudFront receives the response from the origin \(origin response\)
+ Before CloudFront forwards the response to the viewer \(viewer response\)

![\[Conceptual graphic that shows how the CloudFront events that can trigger a Lambda function.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/cloudfront-events-that-trigger-lambda-functions.png)

You can also generate responses to viewers without ever sending the request to the origin\.

With Lambda@Edge, you can build a variety of solutions, for example:
+ Inspect cookies to rewrite URLs to different versions of a site for A/B testing\.
+ Send different objects to your users based on the `User-Agent` header, which contains information about the device that submitted the request\. For example, you can send images in different resolutions to users based on their devices\.
+ Inspect headers or authorized tokens, inserting a corresponding header and allowing access control before forwarding a request to the origin\.
+ Add, delete, and modify headers, and rewrite the URL path to direct users to different objects in the cache\.
+ Generate new HTTP responses to do things like redirect unauthenticated users to login pages, or create and deliver static webpages right from the edge\. For more information, see [Using Lambda Functions to Generate HTTP Responses to Viewer and Origin Requests](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/http-response-generation.html) in the *Amazon CloudFront Developer Guide*\.

For more information about using Lambda@Edge, see [Using CloudFront with Lambda@Edge](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-at-the-edge.html)\. 