// AWS X-Ray SDK
var AWSXRay = require('aws-xray-sdk-core')
// AWS SDK clients
// NOTE: Function needs permission for each service in execution role
var AWS = AWSXRay.captureAWS(require('aws-sdk'))
// Create client outside of handler to reuse 
var s3 = new AWS.S3()
// Read bucket name from environment variable
const bucket = process.env.bucket

// Use Amazon S3 client
var putObject = function(data, filename, context){
  // Create request for PutObject action
  var s3params = {
    Body: JSON.stringify(data, null, 2),
    Bucket: bucket,
    Key: filename
  };
  // Send request and log result
  s3.putObject(s3params, function(err, data) {
     if (err) console.log(err, err.stack)
     else     console.log(data)
  });
}

exports.handler = function(event, context) {
    console.log("Event: " + JSON.stringify(event, null, 2))
    // Wait one second and store event in S3
    setTimeout(function(){putObject(event, "test", context)}, 1000)
};