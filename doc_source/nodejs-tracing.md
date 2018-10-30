# Node\.js<a name="nodejs-tracing"></a>

In Node\.js, you can have Lambda emit subsegments to X\-Ray to show you information about downstream calls to other AWS services made by your function\. To do so, you first need to include the [the AWS X\-Ray SDK for Node\.js](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-nodejs.html) in your deployment package\. In addition, wrap your AWS SDK `require` statement in the following manner:

```
var AWSXRay = require('aws-xray-sdk-core');
var AWS = AWSXRay.captureAWS(require('aws-sdk'));
```

Then, use the AWS variable defined in the preceding example to initialize any service client that you want to trace with X\-Ray, for example:

```
s3Client = AWS.S3();
```

After following these steps, any call made from your function using `s3Client` results in an X\-Ray subsegment that represents that call\. As an example, you can run the Node\.js function following to see how the trace looks in X\-Ray:

```
var AWSXRay = require('aws-xray-sdk-core');
var AWS = AWSXRay.captureAWS(require('aws-sdk'));

s3 = new AWS.S3({signatureVersion: 'v4'});

 
exports.handler = (event, context, callback) => {
 
    
    var params = {Bucket: BUCKET_NAME, Key: BUCKET_KEY, Body: BODY};
 
    s3.putObject(params, function(err, data) {
        if (err)
          {  console.log(err) }
        else {
          console.log('success!') 
        }
    });
};
```

Following is what a trace emitted by the code preceding looks like \(asynchronous invocation\): 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/Trace_Node.png)