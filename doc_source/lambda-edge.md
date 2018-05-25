# AWS Lambda@Edge<a name="lambda-edge"></a>

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
+ Generate new HTTP responses to do things like redirect unauthenticated users to login pages, or create and deliver static webpages right from the edge\. For more information, see [Using Lambda Functions to Generate HTTP Responses to Viewer and Origin Requests](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/http-response-generation.html) in the *Amazon CloudFront Developer Guide*\.

The subsequent links pertain to content that has been moved to [Using CloudFront with Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-at-the-edge.html) in the *Amazon CloudFront Developer Guide*\. If you have bookmarked any of this content, be advised that you should update those bookmarks\. Each section following will provide you with an updated link\.

**Topics**
+ [How You Create and Use Lambda Functions for Lambda@Edge](#lambda-edge-how-it-works)
+ [How Replicas of Lambda Functions are Deleted](#lambda-edge-delete-replicas)
+ [Setting IAM Permissions and Roles for Lambda@Edge](#lambda-edge-permissions)
+ [Creating a Lambda@Edge Function](#lambda-edge-create-function)
+ [Adding Triggers for a Lambda@Edge Function \(AWS Lambda Console\)](#lambda-edge-add-triggers)
+ [Writing Functions for Lambda@Edge](#lambda-edge-authoring-functions)
+ [Editing a Lambda Function for Lambda@Edge](#lambda-edge-edit-function)
+ [Testing and Debugging](#lambda-edge-testing-debugging)
+ [Lambda@Edge Limits](#lambda-edge-limits)

## How You Create and Use Lambda Functions for Lambda@Edge<a name="lambda-edge-how-it-works"></a>

This content has been moved to [How You Create and Use Lambda Functions for Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-how-it-works.html) in the *Amazon CloudFront Developer Guide*\. 

## How Replicas of Lambda Functions are Deleted<a name="lambda-edge-delete-replicas"></a>

This content has been moved to [How Replicas of Lambda Functions are Deleted](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-delete-replicas.html) in the *Amazon CloudFront Developer Guide*\. 

## Setting IAM Permissions and Roles for Lambda@Edge<a name="lambda-edge-permissions"></a>

This content has been moved to [Setting IAM Permissions and Roles for Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-permissions.html) in the *Amazon CloudFront Developer Guide*\. 

## Creating a Lambda@Edge Function<a name="lambda-edge-create-function"></a>

This content has been moved to [Creating a Lambda@Edge Function](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-create-function.html) in the *Amazon CloudFront Developer Guide*\.

## Adding Triggers for a Lambda@Edge Function \(AWS Lambda Console\)<a name="lambda-edge-add-triggers"></a>

This content has been moved to [Adding Triggers for a Lambda@Edge Function \(AWS Lambda Console\)](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-add-triggers.html) in the *Amazon CloudFront Developer Guide*\. 

## Writing Functions for Lambda@Edge<a name="lambda-edge-authoring-functions"></a>

This content has been moved to [Writing Functions for Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-authoring-functions.html) in the *Amazon CloudFront Developer Guide*\. 

## Editing a Lambda Function for Lambda@Edge<a name="lambda-edge-edit-function"></a>

This content has been moved to [Editing a Lambda Function for Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-edit-function.html) in the *Amazon CloudFront Developer Guide*\. 

## Testing and Debugging<a name="lambda-edge-testing-debugging"></a>

This content has been moved to [Testing and Debugging](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-edge-testing-debugging.html) in the *Amazon CloudFront Developer Guide*\. 

## Lambda@Edge Limits<a name="lambda-edge-limits"></a>

This content has been moved to [Lambda@Edge Limits](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/cloudfront-limits.html#limits-lambda-at-edge) in the *Amazon CloudFront Developer Guide*\. 