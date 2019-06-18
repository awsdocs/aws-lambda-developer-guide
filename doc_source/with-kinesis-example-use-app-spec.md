# AWS SAM Template for a Kinesis Application<a name="with-kinesis-example-use-app-spec"></a>

You can build this application using [AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/)\. To learn more about creating AWS SAM templates, see [AWS SAM Template Basics](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-basics.html) in the *AWS Serverless Application Model Developer Guide*\.

Below is a sample AWS SAM template for the Lambda application from the [tutorial](with-kinesis-example.md)\. The function and handler in the template are for the Node\.js code\. If you use a different code sample, update the values accordingly\.

**Example template\.yaml \- Kinesis Stream**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  LambdaFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs8.10
      Timeout: 10
      Tracing: Active
      Events:
        Stream:
          Type: Kinesis
          Properties:
            Stream: !GetAtt KinesisStream.Arn
            BatchSize: 100
            StartingPosition: LATEST
  KinesisStream:
    Type: AWS::Kinesis::Stream
    Properties:
      ShardCount: 1
Outputs:
  FunctionName:
    Description: "Function name"
    Value: !Ref LambdaFunction
  StreamARN:
    Description: "Stream ARN"
    Value: !GetAtt KinesisStream.Arn
```

The template creates a Lambda function, a Kinesis stream, and an event source mapping\. The event source mapping reads from the stream and invokes the function\.

To use an [HTTP/2 stream consumer](with-kinesis.md#services-kinesis-configure), create the consumer in the template and configure the event source mapping to read from the consumer instead of from the stream\.

**Example template\.yaml \- Kinesis Stream Consumer**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Description: A function that processes data from a Kinesis stream.
Resources:
  kinesisprocessrecordpython:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs8.10
      Timeout: 10
      Tracing: Active
      Events:
        Stream:
          Type: Kinesis
          Properties:
            Stream: !GetAtt StreamConsumer.ConsumerARN
            StartingPosition: LATEST
            BatchSize: 100
  KinesisStream:
    Type: "AWS::Kinesis::Stream"
    Properties:
      ShardCount: 1
  StreamConsumer:
    Type: "AWS::Kinesis::StreamConsumer"
    Properties:
      StreamARN: !GetAtt KinesisStream.Arn
      ConsumerName: "TestConsumer"
Outputs:
  FunctionName:
    Description: "Function name"
    Value: !Ref LambdaFunction
  StreamARN:
    Description: "Stream ARN"
    Value: !GetAtt KinesisStream.Arn
  ConsumerARN:
    Description: "Stream consumer ARN"
    Value: !GetAtt StreamConsumer.ConsumerARN
```

For information on how to package and deploy your serverless application using the package and deploy commands, see [Deploying Serverless Applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html) in the *AWS Serverless Application Model Developer Guide*\.