# AWS SAM template for an Amazon S3 application<a name="with-s3-example-use-app-spec"></a>

You can build this application using [AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/)\. To learn more about creating AWS SAM templates, see [AWS SAM template basics](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-basics.html) in the *AWS Serverless Application Model Developer Guide*\.

Below is a sample AWS SAM template for the Lambda application from the [tutorial](with-s3-example.md)\. Copy the text below to a \.yaml file and save it next to the ZIP package you created previously\. Note that the `Handler` and `Runtime` parameter values should match the ones you used when you created the function in the previous section\. 

**Example template\.yaml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  CreateThumbnail:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: handler
      Runtime: runtime
      Timeout: 60
      Policies: AWSLambdaExecute
      Events:
        CreateThumbnailEvent:
          Type: S3
          Properties:
            Bucket: !Ref SrcBucket
            Events: s3:ObjectCreated:*

  SrcBucket:
    Type: AWS::S3::Bucket
```

For information on how to package and deploy your serverless application using the package and deploy commands, see [Deploying serverless applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html) in the *AWS Serverless Application Model Developer Guide*\.