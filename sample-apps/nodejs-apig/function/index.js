const AWS = require('aws-sdk')
// Create client outside of handler to reuse
const lambda = new AWS.Lambda()

// Handler
exports.handler = async function(event, context) {
  var response = {
    "statusCode": 200,
    "headers": {
      "Content-Type": "application/json"
    },
    "isBase64Encoded": false,
    "multiValueHeaders": { 
      "X-Custom-Header": ["My value", "My other value"],
    },
    "body": ""
  }
  console.log('ENVIRONMENT VARIABLES: ' + serialize(process.env))
  console.log('CONTEXT: ' + serialize(context))
  console.log('EVENT: ' + serialize(event))
  var accountSettings = await getAccountSettings()
  response.body = serialize(accountSettings.AccountUsage)
  return response
}

// Use SDK client
var getAccountSettings = function(){
  return lambda.getAccountSettings().promise()
}

var serialize = function(object) {
  return JSON.stringify(object, null, 2)
}