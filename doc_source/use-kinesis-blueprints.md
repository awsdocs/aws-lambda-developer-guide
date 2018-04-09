# More samples<a name="use-kinesis-blueprints"></a>

The Lambda console provides blueprints, which are samples you can use to learn how to integrate your Lambda functions with other AWS services\. The samples below provide some specific use cases for using a Lambda function to work with the distinctive offerings of Kinesis\.

**Note**  
Lambda blueprints are only available in the Node\.js and Python languages\. You can implement functions in other supported runtimes\. For information on AWS Lambda supported runtimes, see [Lambda Execution Environment and Available Libraries](current-supported-versions.md)\.

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