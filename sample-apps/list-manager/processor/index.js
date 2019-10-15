// AWS X-Ray SDK
var AWSXRay = require('aws-xray-sdk-core')
var captureMySQL = require('aws-xray-sdk-mysql')
var mysql = captureMySQL(require('mysql'))
// AWS SDK clients
// NOTE: Function needs permission for each service in execution role
var AWS = AWSXRay.captureAWS(require('aws-sdk'))
// Create client outside of handler to reuse 
var docClient = new AWS.DynamoDB.DocumentClient()
var md5 = require('md5')
// Read bucket name from environment variable
const table = process.env.table
const aggtable = process.env.aggtable
const username = process.env.databaseUser
const password = process.env.databasePassword
const host = process.env.databaseHost
const dbtable = "events"

// Use Amazon DynamoDB client
var updateList = async function(event){
  // shallow copy of event
  let item = { ... event }
  // aggregate ID (all entries with same title)
  item.aggid = md5(item.title)
  // individual ID (from same user only)
  item.id = md5(item.title + item.user)
  // TODO: check for existing documents
  // get agg item
  var aggdata = await docClient.get({TableName:aggtable, Key: { id: item.aggid }}).promise()
  console.log("AGGDATA: " + JSON.stringify(aggdata, null, 2))

  // get indv item
  var data = await docClient.get({TableName:table, Key: { id: item.id, aggid: item.aggid }}).promise()
  console.log("DATA: " + JSON.stringify(data, null, 2))

  var newEntries = JSON.parse(event.entries)
  var oldEntries = {}
  var aggItem = {}
  if (data.Item)
    oldEntries = JSON.parse(data.Item.entries)
  var aggregateEntries = {}
  if (aggdata.Item) {
    aggregateEntries = JSON.parse(aggdata.Item.entries)
    aggItem = aggdata.Item
  } else {
    aggItem.id = item.aggid
    aggItem.title = item.title
    aggItem.type = item.type
    aggItem['last updated'] = item.timestamp
  }
  // TODO: store contributor user IDs in aggregate item and confirm their presence before applying delta
  var deltaEntries = newEntries
  console.log("NEW ENTRIES: " + JSON.stringify(newEntries, null, 2))
  console.log("OLD ENTRIES: " + JSON.stringify(oldEntries, null, 2))
  if ( event.type == "rank" ) {
    // calculate changes vs existing indv list
    Object.keys(newEntries).forEach(function(key,index) {
      if (oldEntries.hasOwnProperty(key))
        deltaEntries[key] -= oldEntries[key]
    })
    Object.keys(oldEntries).forEach(function(key,index) {
      if (!newEntries.hasOwnProperty(key))
        deltaEntries[key] = -oldEntries[key]
    })
    // update aggregate list
    Object.keys(deltaEntries).forEach(function(key,index) {
      if (aggregateEntries.hasOwnProperty(key))
        aggregateEntries[key] += deltaEntries[key]
      else
        aggregateEntries[key] = newEntries[key]
    })
  }
  if ( event.type == "tally" ) {
    // update existing indv list
    Object.keys(newEntries).forEach(function(key,index) {
      //TODO: check type of value = number
      if (oldEntries.hasOwnProperty(key))
        oldEntries[key] += newEntries[key]
      else
        oldEntries[key] = newEntries[key]
    })
    Object.keys(deltaEntries).forEach(function(key,index) {
      if (aggregateEntries.hasOwnProperty(key))
        aggregateEntries[key] += deltaEntries[key]
      else
        aggregateEntries[key] = newEntries[key]
    })
    item.entries = JSON.stringify(oldEntries, null, 0)
    console.log("TALLIED ENTRIES: " + item.entries)
  }
  console.log("DELTA ENTRIES: " + JSON.stringify(deltaEntries, null, 0))
  item['last updated'] = item.timestamp
  delete item.timestamp

  var params = {
    TableName: table,
    Item: item
  }

  await docClient.put(params).promise()

  aggItem.entries = JSON.stringify(aggregateEntries, null, 0)
  console.log("AGGREGATE ENTRIES: " + aggItem.entries)

  params.Item = aggItem
  params.TableName = aggtable
  await docClient.put(params).promise()

}

// Use mysql client
var storeEvent = async function(event, connection){
  // update database
  var query = "INSERT INTO " + dbtable + " (id, title, timestamp, entries) VALUES ?;"
  var values = [[event.id, event.title, event.timestamp, event.entries]]
  console.log("storing event:")
  console.log(event.id + "," + event.title  + "," + event.timestamp  + "," + event.entries)
  return new Promise( ( resolve, reject ) => {
    connection.query(query, [values], function (error, results, fields) {
      if (error) 
        return reject(error)
      resolve(results)
    })
  })
}

var processRecords = async function(records, connection) {
  for (const record of records) {
    // Decode kinesis data
    const data = Buffer.from(record.kinesis.data, 'base64').toString('ascii')
    var event = {}
    const item = JSON.parse(data)
    event.title = item.title
    event.user = item.user
    event.type = item.type
    event.entries = JSON.stringify(item.entries, null, 0)
    event.id = record.eventID
    event.timestamp = record.kinesis.approximateArrivalTimestamp * 1000
    console.log("EVENT TIMESTAMP:" + event.timestamp)
    // update table
    await updateList(event)
    await storeEvent(event, connection)
  }
}

exports.handler = async function(kinesisEvent, context) {
  console.log("EVENT\n" + JSON.stringify(kinesisEvent, null, 2))
  var connection = mysql.createConnection({
    host     : host,
    user     : username,
    password : password,
    database : 'lambdadb'
  })
  console.log("connecting to db")
  connection.connect()
  var result
  // process event
  await processRecords(kinesisEvent.Records, connection)
  return new Promise( ( resolve, reject ) => {
    connection.end( err => {
      if ( err )
        return reject( err )
      const response = {
        statusCode: 200,
        body: JSON.stringify(result),
      }
      resolve(response)
    } )
  } )
}