var fs = require('fs').promises
const crypto = require('crypto')
const dir = process.env.mountPath

exports.handler = async function(event) {
  console.log("EVENT: %s", JSON.stringify(event, null, 2))
  const filePath = dir + "/" + event.fileName
  const fileSize = event.fileSize
  // generate file
  const buffer = await crypto.randomBytes(fileSize)
  // write operation
  const writeTimeMs = await writeFile(filePath, buffer)
  // read file
  const readTimeMs = await readFile(filePath)
  // stat file
  const fileStat = await fs.stat(filePath)
  const fileSizeBytes = fileStat.size
  console.log("File size: %s bytes", fileSizeBytes)
  // format response
  var response = {
    "writeTimeMs": writeTimeMs,
    "readTimeMs": readTimeMs,
    "fileSizeBytes": fileSizeBytes
  }
  return response
}

var readFile = async function(filePath){
  console.log("Attempting to read file: %s", filePath)
  const readstart = process.hrtime()
  var fileContents
  try {
    fileContents = await fs.readFile(filePath, "utf8")
    const readend = process.hrtime(readstart)
    const readTimeMs = readend[0] * 1000 + readend[1] / 1000000
    console.log("Read completed in %dms", readTimeMs)
    return readTimeMs
  } catch (error){
    console.error(error)
    return "Read error: " + error
  }
}

var writeFile = async function(filePath, buffer){
  console.log("Attempting to write file: %s", filePath)
  var writestart = process.hrtime()
  try {
    const fileBase64 = buffer.toString('base64')
    fs.writeFile(filePath, fileBase64)
    var writeend = process.hrtime(writestart)
    const writeTimeMs = writeend[0] * 1000 + writeend[1] / 1000000
    console.log("Write completed in %dms", writeTimeMs)
    return writeTimeMs
  } catch (error){
    console.error(error)
    return "Write error: " + error
  }
}