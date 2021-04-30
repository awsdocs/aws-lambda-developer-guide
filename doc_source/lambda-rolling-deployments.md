# Rolling deployments for Lambda functions<a name="lambda-rolling-deployments"></a>

Use rolling deployments to control the risks associated with introducing new versions of your Lambda function\. In a rolling deployment, the system automatically deploys the new version of the function and gradually sends an increasing amount of traffic to the new version\. The amount of traffic and rate of increase are parameters that you can configure\.

You configure a rolling deployment by using AWS CodeDeploy and AWS SAM\. CodeDeploy is a service that automates application deployments to Amazon computing platforms such as Amazon EC2 and AWS Lambda\. For more information, see [What is CodeDeploy?](https://docs.aws.amazon.com/codedeploy/latest/userguide/welcome.html)\. By using CodeDeploy to deploy your Lambda function, you can easily monitor the status of the deployment and initiate a rollback if you detect any issues\.

AWS SAM is an open\-source framework for building serverless applications\. You create an AWS SAM template \(in YAML format\) to specify the configuration of the components required for the rolling deployment\. AWS SAM uses the template to create and configure the components\. For more information, see [What is the AWS SAM?](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html)\.

In a rolling deployment, AWS SAM performs these tasks:
+ It configures your Lambda function and creates an alias\.

  The alias routing configuration is the underlying capability that implements the rolling deployment\.
+ It creates a CodeDeploy application and deployment group\.

  The deployment group manages the rolling deployment and the rollback \(if needed\)\.
+ It detects when you create a new version of your Lambda function\.
+ It triggers CodeDeploy to start the deployment of the new version\.

 

## Example AWS SAM Lambda template<a name="sam-template"></a>

The following example shows an [AWS SAM template](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-basics.html) for a simple rolling deployment\. 

```
AWSTemplateFormatVersion : '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Description: A sample SAM template for deploying Lambda functions.

Resources:
# Details about the myDateTimeFunction Lambda function
  myDateTimeFunction:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: myDateTimeFunction.handler
      Runtime: nodejs12.x
# Creates an alias named "live" for the function, and automatically publishes when you update the function.
      AutoPublishAlias: live
      DeploymentPreference:
# Specifies the deployment configuration
          Type: Linear10PercentEvery2Minutes
```

This template defines a Lambda function named `myDateTimeFunction` with the following properties\. 

**AutoPublishAlias **  
The `AutoPublishAlias` property creates an alias named `live`\. In addition, the AWS SAM framework automatically detects when you save new code for the function\. The framework then publishes a new function version and updates the `live` alias to point to the new version\.

**DeploymentPreference**  
The `DeploymentPreference` property determines the rate at which the CodeDeploy application shifts traffic from the original version of the Lambda function to the new version\. The value `Linear10PercentEvery2Minutes` shifts an additional ten percent of the traffic to the new version every two minutes\.   
For a list of the predefined deployment configurations, see [Deployment configurations](https://docs.aws.amazon.com/codedeploy/latest/userguide/deployment-configurations.html)\. 

For a detailed tutorial on how to use CodeDeploy with Lambda functions, see [Deploy an updated Lambda function with CodeDeploy](https://docs.aws.amazon.com/codedeploy/latest/userguide/tutorial-lambda-sam.html)\. 