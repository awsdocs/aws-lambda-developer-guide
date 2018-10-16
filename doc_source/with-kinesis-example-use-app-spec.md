# AWS Serverless Application Model Specification for a Amazon Kinesis Application<a name="with-kinesis-example-use-app-spec"></a>

The following contains the SAM template for the Lambda application from the [tutorial](with-kinesis-example.md)\. Copy the text below to a YAML file and save it next to the ZIP package you created previously\. Note that the `Handler` and `Runtime` parameter values should match the ones you used when you created the function in the previous section\.

**Example kinesis\-trigger\.yml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  ProcessKinesisRecords:
    Type: AWS::Serverless::Function
    Properties:
      Handler: handler
      Runtime: runtime
      Policies: AWSLambdaKinesisExecutionRole
      Events:
        Stream:
          Type: Kinesis
          Properties:
            Stream: !GetAtt ExampleStream.Arn
            BatchSize: 100
            StartingPosition: TRIM_HORIZON

  ExampleStream:
    Type: AWS::Kinesis::Stream
    Properties:
      ShardCount: 1
```

For information on how to package and deploy your serverless application using the package and deploy commands, see [Packaging and Deployment](serverless-deploy-wt.md#serverless-deploy)\.