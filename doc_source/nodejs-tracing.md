# Instrumenting Node\.js code in AWS Lambda<a name="nodejs-tracing"></a>

In Node\.js, you can have Lambda emit subsegments to X\-Ray to show you information about downstream calls to other AWS services made by your function\. To do so, you first need to include the [the AWS X\-Ray SDK for Node\.js](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-nodejs.html) in your deployment package\. In addition, wrap your AWS SDK `require` statement in the following manner\.

```
const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))
```

Then, use the AWS variable defined in the preceding example to initialize any service client that you want to trace with X\-Ray, for example:

```
const s3 = new AWS.S3()
```

After following these steps, any call made from your function using `s3Client` results in an X\-Ray subsegment that represents that call\. As an example, you can run the Node\.js function following to see how the trace looks in X\-Ray\.

**Example index\.js**  

```
const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))
const s3 = new AWS.S3()

exports.handler = async function(event) {
  return s3.listBuckets().promise()
}
```

The following example shows a trace with 2 segments, both named **my\-function**\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/nodejs-xray-timeline.png)

The first segment represents the invocation request processed by the Lambda service\. The second segment records the work done by your function\. The function segment has 3 subsegments\.

****
+ **Initialization** – Represents time spent loading your function and running [initialization code](gettingstarted-features.md#gettingstarted-features-programmingmodel)\. This subsegment only appears for the first event processed by each instance of your function\.
+ **Invocation** – Represents the work done by your handler code\. By instrumenting your code, you can extend this subsegment with additional subsegments\.
+ **Overhead** – Represents the work done by the Lambda runtime to prepare to handle the next event\.