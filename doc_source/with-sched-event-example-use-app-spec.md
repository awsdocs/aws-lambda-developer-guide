# \(Optional\): Deploy With AWS SAM and AWS CloudFormation<a name="with-sched-event-example-use-app-spec"></a>

In the previous section, you used AWS Lambda APIs to create and update a Lambda function by providing a deployment package as a ZIP file\. However, this mechanism may not be convenient for automating deployment steps for functions, or coordinating deployments and updates to other elements of a serverless application, like event sources and downstream resources\.

You can use AWS CloudFormation to easily specify, deploy, and configure serverless applications\. AWS CloudFormation is a service that helps you model and set up your Amazon Web Services resources so that you can spend less time managing those resources and more time focusing on your applications that run in AWS\. You create a template that describes all the AWS resources that you want \(like Lambda functions and DynamoDB tables\), and AWS CloudFormation takes care of provisioning and configuring those resources for you\.

In addition, you can use the AWS Serverless Application Model to express resources that comprise the serverless application\. These resource types, such as Lambda functions and APIs, are fully supported by AWS CloudFormation and make it easier for you to define and deploy your serverless application\.

For more information, see [Deploying Lambda\-based Applications](deploying-lambda-apps.md)\.

## Specification for Scheduled Event Application<a name="sched-event-tutorial-spec"></a>

The following contains the SAM template for this application\. Copy the text below to a \.yaml file and save it next to the ZIP package you created previously\. Make sure the `Runtime:` parameter value matches the one you chose in the previous section\.

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Parameters: 
  NotificationEmail:
    Type: String
Resources:
  CheckWebsitePeriodically:
    Type: AWS::Serverless::Function
    Properties:
      Handler: LambdaFunctionOverHttps.handler
      Runtime: runtime
      Policies: AmazonDynamoDBFullAccess
      Events:
        CheckWebsiteScheduledEvent:
          Type: Schedule
          Properties:
            Schedule: rate(1 minute)

  AlarmTopic:
    Type: AWS::SNS::Topic
    Properties:
      Subscription:
      - Protocol: email
        Endpoint: !Ref NotificationEmail

  Alarm:
    Type: AWS::CloudWatch::Alarm
    Properties:
      AlarmActions:
        - !Ref AlarmTopic
      ComparisonOperator: GreaterThanOrEqualToThreshold
      Dimensions:
        - Name: FunctionName
          Value: !Ref CheckWebsitePeriodically
      EvaluationPeriods: String
      MetricName: Errors
      Namespace: AWS/Lambda
      Period: '60'
      Statistic: Sum
      Threshold: '1'
```

## Deploying the Serverless Application<a name="with-sched-event-example-use-app-spec-deploy"></a>

 For information on how to package and deploy your serverless application using the package and deploy commands, see [Packaging and Deployment](serverless-deploy-wt.md#serverless-deploy)\.