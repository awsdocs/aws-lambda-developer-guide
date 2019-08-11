// Utils
var zlib = require('zlib');
// AWS X-Ray SDK
var AWSXRay = require('aws-xray-sdk-core')
// AWS SDK clients
var AWS = AWSXRay.captureAWS(require('aws-sdk'))
var s3 = new AWS.S3()

// environment variable
const bucket = process.env.bucket

// use S3 SDK
var putObject = function(data, filename, context){
  var s3params = {
    Body: JSON.stringify(data, null, 2),
    Bucket: bucket,
    Key: filename
  };
   s3.putObject(s3params, function(err, data) {
     if (err) console.log(err, err.stack)
     else     console.log(data)
  });
}

exports.handler = function(event, context) {
    console.log("Event: " + JSON.stringify(event, null, 2))
    // store event
    setTimeout(function(){putObject(event, "test", context)}, 1000)
};