# Gradual Code Deployment<a name="automating-updates-to-serverless-apps"></a>

If you use AWS SAM to create your serverless application, it comes built\-in with [AWS CodeDeploy](https://docs.aws.amazon.com/codedeploy/latest/userguide/welcome.html) for safe Lambda deployments\. With just a few lines of configuration, SAM will do the following for you:
+ Deploy new versions of your Lambda function and automatically create aliases that point to the new version\. 
+ Gradually shift customer traffic to the new version until you are satisfied it is working as expected or roll back the update\. 
+ Define pre\-traffic and post\-traffic test functions to verify the newly deployed code is configured correctly and your application operates as expected\. 
+ Roll back the deployment if CloudWatch alarms are triggered\. 

This can all be done by updating your SAM template\. The example below demonstrates a simple version of using Code Deploy to gradually shift customers to your newly deployed version:

```
Resources:
 MyLambdaFunction:
   Type: AWS::Serverless::Function
   Properties:
     Handler: index.handler
     Runtime: nodejs4.3
     CodeUri: s3://bucket/code.zip

     AutoPublishAlias: live

     DeploymentPreference:
       Type: Canary10Percent10Minutes 
       Alarms:
         # A list of alarms that you want to monitor
         - !Ref AliasErrorMetricGreaterThanZeroAlarm
         - !Ref LatestVersionErrorMetricGreaterThanZeroAlarm
       Hooks:
         # Validation Lambda functions that are run before & after traffic shifting
         PreTraffic: !Ref PreTrafficLambdaFunction
         PostTraffic: !Ref PostTrafficLambdaFunction
```

The above revisions to a SAM template do the following:
+ **AutoPublishAlias** \- By adding this property and specifying an alias name, AWS SAM will do the following:
  + Detect when new code is being deployed based on changes to the Lambda function's Amazon S3 URI\.
  + Create and publish an updated version of that function with the latest code\.
  + Create an alias with a name you provide \(unless an alias already exists\) and points to the updated version of the Lambda function\. Function invocations should use the alias qualifier to take advantage of this\. If you are not familiar with Lambda function versioning and aliases, see [AWS Lambda Function Versioning and Aliases](versioning-aliases.md)\.
+ **Deployment Preference Type** \- In the above example, 10 percent of your customer traffic will be immediately shifted to your new version and after 10 minutes all traffic will be shifted to the new version\. However, if either your pre\- and post hook tests fail or a CloudWatch alarm is triggered, CodeDeploy will roll back your deployment\. The table below outlines other traffic\-shifting options available beyond the one used above\. Note the following: 
  + **Canary:** Traffic is shifted in two increments\. You can choose from predefined canary options that specify the percentage of traffic shifted to your updated Lambda function version in the first increment and the interval, in minutes, before the remaining traffic is shifted in the second increment\. 
  + **Linear:** Traffic is shifted in equal increments with an equal number of minutes between each increment\. You can choose from predefined linear options that specify the percentage of traffic shifted in each incremement and the number of minutes between each increment\. 
  + ** All\-at\-once:** All traffic is shifted from the original Lambda function to the updated Lambda function version at once\. 

        
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/automating-updates-to-serverless-apps.html)
+ **Alarms** \- CloudWatch alarms that will be triggered by any errors raised by the deployment and automatically roll back your deployment\. For instance, if the updated code you are deploying is creating errors within the application or any [AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/monitoring-functions-metrics.html) or custom CloudWatch metrics you have specified have breached the alarm threshold\.
+ **Hooks** \- Pre\-traffic and Post\-traffic test functions that run sanity checks before traffic\-shifting starts to the new version and after traffic\-shifting completes\. 
  + **PreTraffic:** Before traffic shifting starts, CodeDeploy will invoke the pre\-traffic hook Lambda function\. This Lambda function must call back to CodeDeploy denoting success or failure\. On failure, it will abort and report a failure back to AWS CloudFormation\. On success, CodeDeploy will proceed to traffic shifting\.
  + **PostTraffic:** After traffic shifting completes, CodeDeploy will invoke the post\-traffic hook Lambda function\. This is similar to pre\-traffic hook, where the function must call back to CodeDeploy to report a success or failure\. Use post\-traffic hooks to run integration tests or other validation actions\.

  For more information, see [SAM Reference to Safe Deployments](https://github.com/awslabs/serverless-application-model/blob/master/docs/safe_lambda_deployments.rst)\. 