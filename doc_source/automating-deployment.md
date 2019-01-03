# Automating Deployment of Lambda Applications<a name="automating-deployment"></a>

 In the previous section, you learned how to create a SAM template, generate your deployment package, and use the AWS CLI to manually deploy your serverless application\. In this section, you will leverage the following AWS services to fully automate the deployment process\.
+ **AWS CodePipeline**: You use AWS CodePipeline to model, visualize, and automate the steps required to release your serverless application\. For more information, see [What is AWS CodePipeline?](https://docs.aws.amazon.com/codepipeline/latest/APIReference/)
+ **AWS CodeBuild**: You use AWS CodeBuild to build, locally test, and package your serverless application\. For more information, see [What is AWS CodeBuild?](https://docs.aws.amazon.com/codebuild/latest/userguide/)
+ **AWS CloudFormation**: You use AWS CloudFormation to deploy your application\. For more information, see [What is AWS CloudFormation?](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/)
+ **AWS CodeDeploy**: You use [AWS CodeDeploy](https://docs.aws.amazon.com/codedeploy/latest/userguide/welcome.html) to gradually deploy updates to your serverless applications\. For more information on how to do this, see [Gradual Code Deployment](automating-updates-to-serverless-apps.md)\.

The sections below demonstrate how to incorporate all these tools to incorporate your serverless applications\.

## Next Step<a name="automating-deployment-next-step"></a>

 [Building a Pipeline for Your Serverless Application](build-pipeline.md) 