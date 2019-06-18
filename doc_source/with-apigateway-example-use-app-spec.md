# AWS SAM Template for an API Gateway Application<a name="with-apigateway-example-use-app-spec"></a>

You can build this application using [AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/)\. To learn more about creating AWS SAM templates, see [ AWS SAM Template Basics](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-basics.html) in the *AWS Serverless Application Model Developer Guide*\.

Below is a sample AWS SAM template for the Lambda application from the [tutorial](with-on-demand-https-example.md)\. Copy the text below to a \.yaml file and save it next to the ZIP package you created previously\. Note that the `Handler` and `Runtime` parameter values should match the ones you used when you created the function in the previous section\. 

**Example template\.yaml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  LambdaFunctionOverHttps:
    Type: AWS::Serverless::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs8.10
      Policies: AmazonDynamoDBFullAccess
      Events:
        HttpPost:
          Type: Api
          Properties:
            Path: '/DynamoDBOperations/DynamoDBManager'
            Method: post
```

For information on how to package and deploy your serverless application using the package and deploy commands, see [Deploying Serverless Applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html) in the *AWS Serverless Application Model Developer Guide*\.