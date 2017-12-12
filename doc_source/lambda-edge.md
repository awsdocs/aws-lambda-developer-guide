# AWS Lambda@Edge<a name="lambda-edge"></a>

Lambda@Edge lets you run Lambda functions at AWS Regions and Amazon CloudFront edge locations in response to CloudFront events, without provisioning or managing servers\. You can use Lambda functions to change CloudFront requests and responses at the following points:

+ After CloudFront receives a request from a viewer \(viewer request\)

+ Before CloudFront forwards the request to the origin \(origin request\)

+ After CloudFront receives the response from the origin \(origin response\)

+ Before CloudFront forwards the response to the viewer \(viewer response\)

![\[Conceptual graphic that shows how the CloudFront events that can trigger a Lambda function.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/cloudfront-events-that-trigger-lambda-functions.png)

You can also generate responses to viewers without ever sending the request to the origin\.

You write Lambda functions for CloudFront in Node\.js 6\.10\. With Lambda@Edge, you can build a variety of solutions, for example:

+ Inspect cookies to rewrite URLs to different versions of a site for A/B testing\.

+ Send different objects to your users based on the `User-Agent` header, which contains information about the device that submitted the request\. For example, you can send images in different resolutions to users based on their devices\.

+ Inspect headers or authorized tokens, inserting a corresponding header and allowing access control before forwarding a request to the origin\.

+ Add, delete, and modify headers, and rewrite the URL path to direct users to different objects in the cache\.

+ Generate new HTTP responses to do things like redirect unauthenticated users to login pages, or create and deliver static webpages right from the edge\. For more information, see [Using Lambda Functions to Generate HTTP Responses to Viewer and Origin Requests](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/http-response-generation.html) in the *Amazon CloudFront Developer Guide*\.

For more information about setting up CloudFront with Lambda@Edge, including sample code, see [Using CloudFront with Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-at-the-edge.html) in the *Amazon CloudFront Developer Guide*\. 


+ [How You Create Lambda Functions for Lambda@Edge](#lambda-edge-how-it-works)
+ [Setting IAM Permissions and Roles for Lambda@Edge](#lambda-edge-permissions)
+ [Creating a Lambda@Edge Function](#lambda-edge-create-function)
+ [Adding Triggers for a Lambda@Edge Function \(AWS Lambda Console\)](#lambda-edge-add-triggers)
+ [Writing Functions for Lambda@Edge](#lambda-edge-authoring-functions)
+ [Editing a Lambda Function for Lambda@Edge](#lambda-edge-edit-function)
+ [Testing and Debugging](#lambda-edge-testing-debugging)
+ [Lambda@Edge Limits](#lambda-edge-limits)

## How You Create Lambda Functions for Lambda@Edge<a name="lambda-edge-how-it-works"></a>

Here's an overview of how you create Lambda functions for Lambda@Edge:

1. You use Node\.js 6\.10 to write the code for your Lambda function\.

1. Using the AWS Lambda console, you create a Lambda function in the US East \(N\. Virginia\) Region\. \(You can also create the function programmatically, for example, by using one of the AWS SDKs\.\) When you create the function, you specify the following values:

   + The CloudFront distribution that you want the function to apply to

   + A cache behavior in the distribution

1. You publish a numbered version of the function\.

   If you want to edit the function, you edit it in the US East \(N\. Virginia\) Region\. You then publish a new numbered version\.

1. You specify one or more CloudFront events, known as *triggers*, that cause the function to execute\. For example, you can create a trigger that causes the function to execute when CloudFront receives a request from a viewer\.

   When you create a trigger, Lambda replicates the function to AWS Regions and CloudFront edge locations around the globe\.

![\[Conceptual graphic that shows how you create Lambda functions that integrate with CloudFront.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-creation-workflow.png)

## Setting IAM Permissions and Roles for Lambda@Edge<a name="lambda-edge-permissions"></a>

To configure Lambda@Edge, you need IAM permissions and an IAM execution role:

**IAM Permissions Required to Associate Lambda Functions with CloudFront Distributions**  
In addition to the IAM permissions that you need to use AWS Lambda, you need the following IAM permissions to associate Lambda functions with CloudFront distributions:  

+ `lambda:GetFunction`

  For the resource, specify the ARN of the function version that you want to execute when a CloudFront event occurs, as shown in the following example:

  `arn:aws:lambda:us-east-1:123456789012:function:TestFunction:2`

+ `lambda:EnableReplication*`

  For the resource, specify the ARN of the function version that you want to execute when a CloudFront event occurs, as shown in the following example:

  `arn:aws:lambda:us-east-1:123456789012:function:TestFunction:2`

+ `iam:CreateServiceLinkedRole`

+ `cloudfront:UpdateDistribution` or `cloudfront:CreateDistribution`

  Choose `cloudfront:UpdateDistribution` to update a distribution or `cloudfront:CreateDistribution` to create a distribution\.
For more information, see the following documentation:  

+ [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md) in this guide

+ [Authentication and Access Control for CloudFront](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/auth-and-access-control.html) in the *Amazon CloudFront Developer Guide*

**Execution Role**  
You must create an IAM role that can be assumed by the service principals `lambda.amazonaws.com` and `edgelambda.amazonaws.com`\. This role is assumed by the service principals when they execute your function\. For more information, see [Creating the Roles and Attaching the Policies \(Console\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_job-functions.html#access_policies_job-functions_create-policies) in the topic "AWS Managed Policies for Job Functions" in the *IAM User Guide*\.  
Here's an example role trust policy:  

```
{
   "Version": "2012-10-17",
   "Statement": [
      {
         "Effect": "Allow",
         "Principal": {
            "Service": [
               "lambda.amazonaws.com",
               "edgelambda.amazonaws.com"
            ]
         },
         "Action": "sts:AssumeRole"
      }
   ]
}
```
For information about the permissions that you need to grant to the execution role, see [Manage Permissions: Using an IAM Role \(Execution Role\)](http://docs.aws.amazon.com/lambda/latest/dg/intro-permission-model.html#lambda-intro-execution-role) in the *AWS Lambda Developer Guide*\. Note the following:  

+ By default, whenever a CloudFront event triggers a Lambda function, data is written to CloudWatch Logs\. If you want to use these logs, the execution role needs permission to write data to CloudWatch Logs\. You can use the predefined AWSLambdaBasicExecutionRole to grant permission to the execution role\.

  For more information about CloudWatch Logs, see [CloudWatch Metrics and CloudWatch Logs for Lambda Functions](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-cloudwatch-metrics-logging.html) in the Amazon CloudFront Developer Guide\.

+ If your Lambda function code accesses other AWS resources, such as reading an object from an S3 bucket, the execution role needs permission to perform that operation\. 

**AWSServiceRoleForLambdaReplicator Role**  
When you first create a trigger, a role named AWSServiceRoleForLambdaReplicator is automatically created to allow Lambda to replicate Lambda@Edge functions to AWS Regions\. This role is required to use Lambda@Edge\. The ARN for the AWSServiceRoleForLambdaReplicator role looks like this:  
`arn:aws:iam::123456789012:role/aws-service-role/replicator.lambda.amazonaws.com/AWSServiceRoleForLambdaReplicator`

## Creating a Lambda@Edge Function<a name="lambda-edge-create-function"></a>

To set up AWS Lambda to run Lambda functions that are based on CloudFront events, follow this procedure\.

**To create a Lambda@Edge function**

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. If you already have one or more Lambda functions, choose **Create function**\.

   If you've don't have any functions, choose **Get Started Now**\.

1. In the region list at the top of the page, choose **US East \(N\. Virginia\)**\.

1. Create a function using your own code or create a function starting with a CloudFront blueprint\.

   + To create a function using your own code, choose **Author from scratch**\. 

   + To display a list of blueprints for CloudFront, type **cloudfront** in the filter field, and then press **Enter**\.

     If you find a blueprint that you want to use, choose the name of the blueprint\.

1. In the **Basic information** section, specify the following values:  
**Name**  
Type a name for your function\.  
**Role**  
Choose **Create new role from template\(s\)**\.  
Choosing this value will get your started quickly\. Or you can choose **Choose an existing role** or **Create a custom role**\. If you choose one of these, follow the prompts to complete the information for this section\.  
**Role name**  
Type a name for the role\.  
**Policy templates**  
Choose **Basic Edge Lambda permissions**\.

1. If you chose **Author from scratch** in step 4, skip to step 7\.

   If you chose a blueprint in step 4, the **cloudfront** section lets you create one trigger, which associates this function with a cache in a CloudFront distribution and a CloudFront event\. We recommend that you choose **Remove** at this point, so there isn't a trigger for the function when it's created\. Then you can add triggers later\. 
**Important**  
We recommend that you test and debug the function before you add triggers\. If you choose instead to add a trigger now, the function will start to run as soon as you create the function and it finishes replicating to AWS Regions and edge locations, and the corresponding distribution is deployed\.

1. Choose **Create function**\.

   Lambda creates two versions of your function: $LATEST and Version 1\. You can edit only the $LATEST version, but the console initially displays Version 1\.

1. To edit the function, choose **Version 1** near the top of the page, under the ARN for the function\. Then, on the **Versions** tab, choose **$LATEST**\. \(If you left the function and then returned to it, the button label is **Qualifiers**\.\)

1. On the **Configuration** tab, choose the applicable **Code entry type**\. Then follow the prompts to edit or upload your code\.

1. For **Runtime**, accept the default value of **Node\.js 6\.10**\.

1. In the **Tags** section, add any applicable tags\.

1. Choose **Actions**, and then choose **Publish new version**\.

1. Type a description for the new version of the function\.

1. Choose **Publish**\.

1. Test and debug the function\. For more information, see the following:

   + [Using Amazon CloudWatch](monitoring-functions.md)

   + [Lambda X\-Ray](lambda-x-ray.md)

   + [Test Your Serverless Applications Locally Using SAM Local \(Public Beta\)](test-sam-local.md)

1. When you're ready to have the function execute for CloudFront events, publish another version and edit the function to add triggers\. For more information, see [Editing a Lambda Function for Lambda@Edge](#lambda-edge-edit-function)\.

## Adding Triggers for a Lambda@Edge Function \(AWS Lambda Console\)<a name="lambda-edge-add-triggers"></a>

When you create a Lambda function, you can specify only one trigger—only one combination of CloudFront distribution, cache behavior, and event that causes the function to execute\. You can add more triggers to the same function by using the Lambda console or by editing the distribution in the CloudFront console:

+ To use the Lambda console, perform the following procedure\. This method works well if you want to add more triggers to a function for the same CloudFront distribution\.

+ To use the CloudFront console, see [Adding Triggers for CloudFront Events to a Lambda Function](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-create-functions.html#lambda-create-functions-add-triggers) in the *Amazon CloudFront Developer Guide*\. This method works well if you want to add triggers for multiple distributions because it's easier to find the distribution that you want to update\. You can also update other CloudFront settings at the same time\.

**To add triggers to a Lambda@Edge function \(AWS Lambda console\)**

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. In the region list at the top of the page, choose **US East \(N\. Virginia\)**\.

1. On the **Functions** page, choose the name of the function that you want to add triggers for\.

1. Choose **Qualifiers**, and then choose the **Versions** tab\.

1. Choose the version that you want to add triggers to\.
**Important**  
You can't create triggers for the $LATEST version, you must create them for a numbered version\.

   After you choose a version, the name of the button changes to **Version: $LATEST** or **Version:** *version number*\.

1. Choose the **Triggers** tab\.

1. Choose **Add triggers**\.

1. In the **Add trigger** dialog box, choose the dotted box, and then choose **CloudFront**\.
**Note**  
If you've already created one or more triggers, CloudFront is the default service\.

1. Specify the following values to indicate when you want the Lambda function to execute\.  
**Distribution ID**  
Choose the ID of the distribution that you want to add the trigger to\.  
**Cache behavior**  
Choose the cache behavior that specifies the objects that you want to execute the function on\.  
**CloudFront event**  
Choose the CloudFront event that causes the function to execute\.  
**Enable trigger and replicate**  
Select this check box so that AWS Lambda replicates the function to regions globally\. 

1. Choose **Submit**\.

   The function starts to process requests for the specified CloudFront events when the updated CloudFront distribution is deployed\. To determine whether a distribution is deployed, choose **Distributions** in the navigation pane\. When a distribution is deployed, the value of the **Status** column for the distribution changes from **In Progress** to **Deployed**\.

## Writing Functions for Lambda@Edge<a name="lambda-edge-authoring-functions"></a>

The programming model for using Node\.js with Lambda@Edge is the same as using Lambda in an AWS Region\. For more information, see [Programming Model \(Node\.js\)](programming-model.md)\. 

For more information about writing functions for Lambda@Edge, see [Requirements and Restrictions on Lambda Functions](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-requirements-limits.html) in the *Amazon CloudFront Developer Guide*\.

We recommend that you include the `callback` parameter and return the applicable object:

+ **Request events** – Include the `cf.request` object in the response\.

  If you're generating a response, include the `cf.response` object in the response\. For more information, see [Using Lambda Functions to Generate HTTP Responses to Viewer and Origin Requests](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/http-response-generation.html)\. 

+ **Response events** – Include the `cf.response` object in the response\.


+ [Example: A/B Testing](#lambda-edge-authoring-functions-example-ab-testing)
+ [Example: HTTP Redirect](#lambda-edge-authoring-functions-example-http-redirect)

### Example: A/B Testing<a name="lambda-edge-authoring-functions-example-ab-testing"></a>

The following example shows how to use Lambda@Edge for A/B testing\.

```
'use strict';

exports.handler = (event, context, callback) => {
    const request = event.Records[0].cf.request;
    const headers = request.headers;

    if (request.uri !== '/experiment-pixel.jpg') {
        // do not process if this is not an A-B test request
        callback(null, request);
        return;
    }

    const cookieExperimentA = 'X-Experiment-Name=A';
    const cookieExperimentB = 'X-Experiment-Name=B';
    const pathExperimentA = '/experiment-group/control-pixel.jpg';
    const pathExperimentB = '/experiment-group/treatment-pixel.jpg';

    /*
     * Lambda at the Edge headers are array objects.
     *
     * Client may send multiple Cookie headers, i.e.:
     * > GET /viewerRes/test HTTP/1.1
     * > User-Agent: curl/7.18.1 (x86_64-unknown-linux-gnu) libcurl/7.18.1 OpenSSL/1.0.1u zlib/1.2.3
     * > Cookie: First=1; Second=2
     * > Cookie: ClientCode=abc
     * > Host: example.com
     *
     * You can access the first Cookie header at headers["cookie"][0].value
     * and the second at headers["cookie"][1].value.
     *
     * Header values are not parsed. In the example above,
     * headers["cookie"][0].value is equal to "First=1; Second=2"
     */
    let experimentUri;
    if (headers.cookie) {
        for (let i = 0; i < headers.cookie.length; i++) {
            if (headers.cookie[i].value.indexOf(cookieExperimentA) >= 0) {
                console.log('Experiment A cookie found');
                experimentUri = pathExperimentA;
                break;
            } else if (headers.cookie[i].value.indexOf(cookieExperimentB) >= 0) {
                console.log('Experiment B cookie found');
                experimentUri = pathExperimentB;
                break;
            }
        }
    }

    if (!experimentUri) {
        console.log('Experiment cookie has not been found. Throwing dice...');
        if (Math.random() < 0.75) {
            experimentUri = pathExperimentA;
        } else {
            experimentUri = pathExperimentB;
        }
    }

    request.uri = experimentUri;
    console.log(`Request uri set to "${request.uri}"`);
    callback(null, request);
};
```

### Example: HTTP Redirect<a name="lambda-edge-authoring-functions-example-http-redirect"></a>

The following example shows how to generate HTTP redirect responses using Lambda functions that are associated with CloudFront viewer request and origin request events\. If you associate the function with origin requests, the response is cached\.

**Note**  
You can generate HTTP responses only for viewer request and origin request events\. For more information, see [Generating HTTP Responses](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-generating-http-responses.html) in the *Amazon CloudFront Developer Guide*\.

```
'use strict';

exports.handler = (event, context, callback) => {
    /*
     * Generate HTTP redirect response with 302 status code and Location header.
     */
    const response = {
        status: '302',
        statusDescription: 'Found',
        headers: {
            location: [{
                key: 'Location',
                value: 'http://docs.aws.amazon.com/lambda/latest/dg/lambda-edge.html',
            }],
        },
    };
    callback(null, response);
};
```

## Editing a Lambda Function for Lambda@Edge<a name="lambda-edge-edit-function"></a>

When you want to edit a Lambda function, note the following:

+ The original version is labeled $LATEST\.

+ You can edit only the $LATEST version\.

+ Each time you edit the $LATEST version, you must publish a new numbered version\.

+ You can't create triggers for $LATEST\.

+ When you publish a new version of a function, Lambda doesn't automatically copy triggers from the previous version to the new version\. You must reproduce the triggers for the new version\. 

+ When you add a trigger for a CloudFront event to a function, if there's already a trigger for the same distribution, cache behavior, and event for an earlier version of the same function, Lambda deletes the trigger from the earlier version\.

**To edit a Lambda function \(AWS Lambda console\)**

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. In the region list at the top of the page, choose **US East \(N\. Virginia\)**\.

1. In the list of functions, choose the name of the function that you want to edit\.

   By default, the console displays the $LATEST version\. You can view earlier versions \(choose **Qualifiers**\), but you can only edit $LATEST\.

1. On the **Code** tab, for **Code entry type**, choose to edit the code in the browser, upload a \.zip file, or upload a file from Amazon S3\.

1. Choose either **Save** or **Save and test**\.

1. Choose **Actions**, and choose **Publish new version**\. 

1. In the **Publish new version from $LATEST** dialog box, enter a description of the new version\. This description appears in the list of versions, along with an automatically generated version number\. 

1. Choose **Publish**\.

   The new version automatically becomes the latest version\. The version number appears on the **Version** button in the upper\-left corner of the page\.

1. Choose the **Triggers** tab\.

1. Choose **Add trigger**\.

1. In the **Add trigger** dialog box, choose the dotted box, and then choose **CloudFront**\.
**Note**  
If you've already created one or more triggers for a function, CloudFront is the default service\.

1. Specify the following values to indicate when you want the Lambda function to execute\.  
**Distribution ID**  
Choose the ID of the distribution that you want to add the trigger to\.  
**Cache behavior**  
Choose the cache behavior that specifies the objects that you want to execute the function on\.  
**CloudFront event**  
Choose the CloudFront event that causes the function to execute\.  
**Enable trigger and replicate**  
Select this check box so Lambda replicates the function to regions globally\. 

1. Choose **Submit**\.

1. To add more triggers for this function, repeat steps 10 through 13\.

## Testing and Debugging<a name="lambda-edge-testing-debugging"></a>

You can test Lambda@Edge functions on the Lambda console with test events modeled on the CloudFront events\. However, the testing in the console only validates logic, and does not apply service limits that are specific to Lambda@Edge\. 

You can create logging statements for Lambda functions running on Lambda@Edge that will write to CloudWatch Logs\. For more information, see [CloudWatch Metrics and CloudWatch Logs for Lambda Functions](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-cloudwatch-metrics-logging.html)\.

For more information, see the following topics in the *Amazon CloudFront Developer Guide*:

+ [Lambda@Edge Event Structure](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/event-structure.html)

+ [Requirements and Restrictions on Lambda Functions](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/lambda-requirements-limits.html)

## Lambda@Edge Limits<a name="lambda-edge-limits"></a>

Due to the constrained execution environment, Lambda@Edge has restrictions in addition to the default Lambda limits\. For more information, see the following documentation:

+ [AWS Lambda Limits](limits.md) in this guide

+ [Limits on Lambda@Edge](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/cloudfront-limits.html#limits-lambda-at-edge) in the *Amazon CloudFront Developer Guide*