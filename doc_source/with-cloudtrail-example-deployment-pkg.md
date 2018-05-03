# Step 2\.1: Create a Deployment Package<a name="with-cloudtrail-example-deployment-pkg"></a>

The deployment package is a \.zip file containing your Lambda function code\. For this tutorial, you will need to install the `async` library\. To do this, open a command window and navigate to the directory where you intend to store the code file you will copy and save below\. Use *npm* to install the async library as shown below :

`npm install async`

## Node\.js<a name="with-cloudtrail-example-deployment-pkg-nodejs"></a>

1. Open a text editor, and then copy the following code\. 

   ```
   var aws  = require('aws-sdk');
   var zlib = require('zlib');
   var async = require('async');
   
   var EVENT_SOURCE_TO_TRACK = /sns.amazonaws.com/;  
   var EVENT_NAME_TO_TRACK   = /CreateTopic/; 
   var DEFAULT_SNS_REGION  = 'us-west-2';
   var SNS_TOPIC_ARN       = 'The ARN of your SNS topic';
   
   var s3 = new aws.S3();
   var sns = new aws.SNS({
       apiVersion: '2010-03-31',
       region: DEFAULT_SNS_REGION
   });
   
   exports.handler = function(event, context, callback) {
       var srcBucket = event.Records[0].s3.bucket.name;
       var srcKey = event.Records[0].s3.object.key;
      
       async.waterfall([
           function fetchLogFromS3(next){
               console.log('Fetching compressed log from S3...');
               s3.getObject({
                  Bucket: srcBucket,
                  Key: srcKey
               },
               next);
           },
           function uncompressLog(response, next){
               console.log("Uncompressing log...");
               zlib.gunzip(response.Body, next);
           },
           function publishNotifications(jsonBuffer, next) {
               console.log('Filtering log...');
               var json = jsonBuffer.toString();
               console.log('CloudTrail JSON from S3:', json);
               var records;
               try {
                   records = JSON.parse(json);
               } catch (err) {
                   next('Unable to parse CloudTrail JSON: ' + err);
                   return;
               }
               var matchingRecords = records
                   .Records
                   .filter(function(record) {
                       return record.eventSource.match(EVENT_SOURCE_TO_TRACK)
                           && record.eventName.match(EVENT_NAME_TO_TRACK);
                   });
                   
               console.log('Publishing ' + matchingRecords.length + ' notification(s) in parallel...');
               async.each(
                   matchingRecords,
                   function(record, publishComplete) {
                       console.log('Publishing notification: ', record);
                       sns.publish({
                           Message:
                               'Alert... SNS topic created: \n TopicARN=' + record.responseElements.topicArn + '\n\n' + 
                               JSON.stringify(record),
                           TopicArn: SNS_TOPIC_ARN
                       }, publishComplete);
                   },
                   next
               );
           }
       ], function (err) {
           if (err) {
               console.error('Failed to publish notifications: ', err);
           } else {
               console.log('Successfully published all notifications.');
           }
           callback(null,"message");
       });
   };
   ```
**Note**  
The code sample is compliant with the Node\.js runtimes v8\.10 or v6\.10\. For more information, see [Programming Model\(Node\.js\)](programming-model.md)

1. Save the file as `CloudTrailEventProcessing.js`\. 

1. Zip the `CloudTrailEventProcessing.js` file as `CloudTrailEventProcessing.zip`\. 

**Note**  
We're using Node\.js in this tutorial example, but you can author your Lambda functions in Java or Python too\.

### Next Step<a name="create-deployment-pkg-nodejs-cloudtrail-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-cloudtrail-example-create-iam-role.md) 