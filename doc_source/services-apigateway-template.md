# AWS SAM template for an API Gateway application<a name="services-apigateway-template"></a>

Below is a sample AWS SAM template for the Lambda application from the [tutorial](services-apigateway-tutorial.md)\. Copy the text below to a file and save it next to the ZIP package you created previously\. Note that the `Handler` and `Runtime` parameter values should match the ones you used when you created the function in the previous section\. 

**Example template\.yaml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  LambdaFunctionOverHttps:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: index.handler
      Runtime: nodejs12.x
      Policies: AmazonDynamoDBFullAccess
      Events:
        HttpPost:
          Type: Api
          Properties:
            Path: '/DynamoDBOperations/DynamoDBManager'
            Method: post
```

For information on how to package and deploy your serverless application using the package and deploy commands, see [Deploying serverless applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html) in the *AWS Serverless Application Model Developer Guide*\.