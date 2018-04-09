# Optional Console Use Cases<a name="optional-console-use-cases"></a>

The Lambda console blueprints provide examples of you how you can use AWS Lambda with other AWS services\. They are designed as quick introductions that you can use as building blocks for larger AWS applications that integrate with your Lambda functions\. 

## Amazon Kinesis Firehose<a name="aws-lambda-kinesis-firehose-blueprints"></a>


| Blueprint name | Use Case | Sample Runtime Code | Related AWS Services | 
| --- | --- | --- | --- | 
| [Kinesis Firehose Syslog to JSON](https://console.aws.amazon.com/lambda/home?region=us-east-1#/create/new?bp=kinesis-firehose-syslog-to-json) |  Converts input records from SYSlog format to JSON\.  |  Node\.js  |  [Kinesis Firehose](http://docs.aws.amazon.com/firehose/latest/dev/)  | 
| [Kinesis Firehose Aachelog To JSON to Python](https://console.aws.amazon.com/lambda/home?region=us-east-1#/create/new?bp=kinesis-firehose-apachelog-to-json-python) |  Converts input records from Apache Commong log format to JSON\.  |  Node\.js  |  [Kinesis Firehose](http://docs.aws.amazon.com/firehose/latest/dev/)  | 

## Amazon Kinesis Analytics<a name="aws-lambda-kinesis-analytics-blueprints"></a>


| Blueprint name | Use Case | Sample Runtime Code | Related AWS Services | 
| --- | --- | --- | --- | 
| [Process Compressed Record](https://console.aws.amazon.com/lambda/home?region=us-east-1#/create/new?bp=kinesis-firehose-syslog-to-json) |  Receives compressed \(GZIP or Deflate compressed\) JSON or CSV records as input and returns decompressed records with a processing status\.  |  node\.js 6\.10  |  Kinesis Analytics  | 
| [Kinesis Analytics Output](https://console.aws.amazon.com/lambda/home?region=us-east-1#/create/new?bp=kinesis-analytics-output) |  Delivers output records from a Kinesis Analytics application to a custom destination\.  |  node\.js 6\.10  |  Kinesis Analytics  | 

## Amazon S3<a name="aws-lambda-s3-blueprints"></a>


| Blueprint name | Use Case | Sample Runtime Code | Related AWS Services | 
| --- | --- | --- | --- | 
| [Using Python to Get an Amazon S3 Object](https://console.aws.amazon.com/lambda/home?region=us-east-1#/create/new?bp=s3-get-object-python) |  Retrieves an object from an Amazon S3 bucket and display its contents\.  |  python 2\.7  |  Amazon S3  | 

## CloudWatch<a name="aws-lambda-cloudwatch-blueprints"></a>


| Blueprint name | Use Case | Sample Runtime Code | Related AWS Services | 
| --- | --- | --- | --- | 
| [Use LogicMonitor REST API to Create LogicMonitor OpsNotes for CloudWatch Events](https://console.aws.amazon.com/lambda/home?region=us-east-1#/create/new?bp=logicmonitor-send-cloudwatch-events) |  Implements the [LogicMonitor](https://www.logicmonitor.com) REST API to create OpsNotes for CloudWatch Events and how to add LogMonitor API tokens as [Environment Variables](env_variables.md)\.  |  python 2\.7  |  CloudWatch   | 