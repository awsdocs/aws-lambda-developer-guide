var AWSXRay = require('aws-xray-sdk-core')
var captureMySQL = require('aws-xray-sdk-mysql')
var mysql = captureMySQL(require('mysql2'))
const username = process.env.databaseUser
const password = process.env.databasePassword
const host = process.env.databaseHost

exports.handler = async (event) => {
    var connection = mysql.createConnection({
      host     : host,
      user     : username,
      password : password,
      database : 'lambdadb'
    })
    var query = event.query
    var result
    connection.connect()

    connection.query(query, function (error, results, fields) {
      if (error) throw error
      console.log("Ran query: " + query)
      for (result in results)
        console.log(results[result])
    })

    return new Promise( ( resolve, reject ) => {
        connection.end( err => {
            if ( err )
                return reject( err )
            const response = {
                statusCode: 200,
                body: JSON.stringify(result),
            }
            resolve(response)
        })
    })
}
