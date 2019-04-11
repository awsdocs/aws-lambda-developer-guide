var AWSXRay = require('aws-xray-sdk-core');

var Chance = require('chance');

var myFunction = function(event, context, callback) {
  var chance = new Chance();
  var name = chance.first();
  var roll = chance.d20();
  var guid = chance.guid();
  console.log("GUID: " + guid);
  console.log("Name: " + name);
  console.log("Roll: " + roll);

  AWSXRay.captureFunc('annotations', function(subsegment){
    subsegment.addAnnotation('name', name);
    subsegment.addAnnotation('roll', roll);
    subsegment.addAnnotation('request_id', context.awsRequestId);
  });

  if (roll < 5) {
    var error = new Error("Bad roll");
    console.log("ERROR");
    callback(error);
  }

  callback(null, {"result": "Success!"});
};

exports.handler = myFunction;