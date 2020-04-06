var zlib = require('zlib')
var AWSXRay = require('aws-xray-sdk-core')
var AWS = AWSXRay.captureAWS(require('aws-sdk'))

var cloudwatchlogs = new AWS.CloudWatchLogs()
var s3 = new AWS.S3()
var xray = new AWS.XRay()

const bucket = process.env.bucket

var putObject = function(data, filename, context){
  var s3params = {
    Body: JSON.stringify(data, null, 2),
    Bucket: bucket,
    Key: filename
  }
   s3.putObject(s3params, function(err, data) {
     if (err) console.log(err, err.stack)
     else     console.log(data)
  })
}

var getLogStream = function(context, group, stream){
  // download log stream
  var params = {
    logGroupName: group,
    logStreamName: stream,
  }
  console.log("GROUP: " + group)
  cloudwatchlogs.getLogEvents(params, function(err, data) {
    if (err) console.log(err, err.stack)
    else {
      var filename = "errors/" + context.requestid + "/log-events.json"
      putObject(data, filename, context)
    }
  })
}

var getTrace = function(context){
  var startTime = new Date
  var endTime = new Date
  startTime.setHours(endTime.getHours() - 1)
  var xrayparams = {
    EndTime: endTime,
    StartTime: startTime,
    FilterExpression: 'annotation.request_id="' + context.requestid + '"',
    NextToken: context.NextToken
  }
  console.log(xrayparams)
  xray.getTraceSummaries(xrayparams, function(err, data) {
    if (err) console.log(err, err.stack)
    else if (data.TraceSummaries[0] == null && data.NextToken != null) {
      context.NextToken = data.NextToken
      getTrace(context)
    } else if (data.TraceSummaries[0] == null) {
      console.log("Trace not found.")
    }
    else {
      console.log(data)
      var traceid = data.TraceSummaries[0].Id
      console.log(traceid)
      var params = {
        TraceIds: [ traceid ]
      }
      xray.batchGetTraces(params, function(err, data) {
        if (err) console.log(err, err.stack)
        else {
            console.log(data)
            var s3params = {
                Body: JSON.stringify(data, null, 2),
                Bucket: bucket,
                Key: "errors/" + context.requestid + "/request-trace.json"
              }
               s3.putObject(s3params, function(err, data) {
                 if (err) console.log(err, err.stack)
                 else     console.log(data)
              })
        }
      })
    }
  })
}

exports.handler = function(event, context) {
    console.log("Event: " + JSON.stringify(event, null, 2))
    var payload = new Buffer(event.awslogs.data, 'base64')
    zlib.gunzip(payload, function(e, decodedEvent) {
        if (e) {
            context.fail(e)
        } else {
            console.log("Decoded event: " + decodedEvent)
            decodedEvent = JSON.parse(decodedEvent.toString('ascii'))
            try {
              context.requestid = decodedEvent.logEvents[0].message.match(/\w{8}-\w{4}-\w{4}-\w{4}-\w{12}/)[0]
            } catch (TypeError){
              console.log("Request ID not found. Error is outside of traced request loop.")
              return
            }
            console.log(context.requestid)
            // download log stream
            setTimeout(function(){getLogStream(context, decodedEvent.logGroup, decodedEvent.logStream)}, 10000)
            // download trace
            setTimeout(function(){getTrace(context)}, 20000)
            //context.succeed()
        }
    })
}