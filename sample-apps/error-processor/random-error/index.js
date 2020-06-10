const AWSXRay = require('aws-xray-sdk-core')
const aws = AWSXRay.captureAWS(require('aws-sdk'))

const Chance = require('chance')
const lambda = new aws.Lambda()

var myFunction = async function(event, context) {
  var chance = new Chance()
  var name = chance.first()
  var roll1 = chance.integer({ min: 1, max: 1/event["error-rate"] })
  var roll2 = chance.integer({ min: 1, max: 1/event["error-rate"] })

  var guid = chance.guid()
  console.log("## EVENT: " + JSON.stringify(event, null, 2))
  console.log("GUID: " + guid)
  console.log("Name: " + name)
  console.log("Roll 1: " + roll1)
  console.log("Roll 2: " + roll2)

  AWSXRay.captureFunc('annotations', function(subsegment){
    subsegment.addAnnotation('name', name)
    subsegment.addAnnotation('roll1', roll1)
    subsegment.addAnnotation('roll2', roll2)
    subsegment.addAnnotation('request_id', context.awsRequestId)
  })
  
  event["current-depth"] += 1
  if (event["max-depth"] == event["current-depth"]) {
    var error = new Error("Maximum depth reached: " + event["max-depth"])
    error.name = "function.MaxDepthError"
    console.log("ERROR")
    throw error
  }
  if (roll1 == roll2) {
    var error = new Error(`Doubles rolled: ${roll1} & ${roll2}`)
    error.name= "function.DoublesRolledError"
    console.log("ERROR")
    throw error
  }

  var invokeResponse = await lambda.invoke({ FunctionName: context.functionName, Payload: JSON.stringify(event)}).promise()
  var response = JSON.parse(invokeResponse.Payload)
  if ( !response.depth ){
    response.depth = 2
  }
  else {
    response.depth +=1
  }
  if ( response.errorType == "function.MaxDepthError" ){
    var error = new Error(response.errorMessage)
    error.name = "function.MaxDepthError"
    console.log("ERROR")
    throw error
  }
  
  return response
}

exports.handler = myFunction