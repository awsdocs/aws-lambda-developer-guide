# Instrumenting Node\.js Code in AWS Lambda<a name="nodejs-tracing"></a>

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

Following is what a trace emitted by the code preceding looks like\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/nodejs-xray-timeline.png)