const AWS = require('aws-sdk')
// Read environment variable
let region = process.env.AWS_REGION
if (process.env.region)
  region = process.env.region
// Create client outside of handler to reuse
const lambda = new AWS.Lambda({region: region})

// Handler
exports.handler = function(event, context) {
  console.log('Region: ' + region)
  console.log('Event: ' + JSON.stringify(event, null, 2))
  return getAccountSettings()
}

// Use SDK client
var getAccountSettings = function(){
  return lambda.getAccountSettings().promise()
}
