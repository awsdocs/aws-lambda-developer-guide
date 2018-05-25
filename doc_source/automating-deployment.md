# Automating Deployment of Lambda\-based Applications<a name="automating-deployment"></a>

 In the previous section, you learned how to create a SAM template, generate your deployment package, and use the AWS CLI to manually deploy your serverless application\. In this section, you will leverage the following AWS services to fully automate the deployment process\.
+ **CodePipeline**: You use CodePipeline to model, visualize, and automate the steps required to release your serverless application\. For more information, see [What is AWS CodePipeline?](http://docs.aws.amazon.com/codepipeline/latest/APIReference/)
+ **CodeBuild**: You use CodeBuild to build, locally test, and package your serverless application\. For more information, see [What is AWS CodeBuild?](http://docs.aws.amazon.com/codebuild/latest/userguide/)
+ **AWS CloudFormation**: You use AWS CloudFormation to deploy your application\. For more information, see [What is AWS CloudFormation?](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/)
+ **CodeDeploy**: You use [AWS CodeDeploy](https://docs.aws.amazon.com/codedeploy/latest/userguide/welcome.html) to gradually deploy updates to your serverless applications\. For more information on how to do this, see [Gradual Code Deployment](automating-updates-to-serverless-apps.md)\.

The sections below demonstrate how to incorporate all these tools to incorporate your serverless applications\.

## Next Step<a name="automating-deployment-next-step"></a>

[Building a Pipeline for Your Serverless Application](build-pipeline.md)