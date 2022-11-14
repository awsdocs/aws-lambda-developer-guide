# AWS Lambda applications<a name="deploying-lambda-apps"></a>

An AWS Lambda application is a combination of Lambda functions, event sources, and other resources that work together to perform tasks\. You can use AWS CloudFormation and other tools to collect your application's components into a single package that can be deployed and managed as one resource\. Applications make your Lambda projects portable and enable you to integrate with additional developer tools, such as AWS CodePipeline, AWS CodeBuild, and the AWS Serverless Application Model command line interface \(SAM CLI\)\.

The [AWS Serverless Application Repository](https://docs.aws.amazon.com/serverlessrepo/latest/devguide/) provides a collection of Lambda applications that you can deploy in your account with a few clicks\. The repository includes both ready\-to\-use applications and samples that you can use as a starting point for your own projects\. You can also submit your own projects for inclusion\.

[AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-whatis-concepts.html) enables you to create a template that defines your application's resources and lets you manage the application as a *stack*\. You can more safely add or modify resources in your application stack\. If any part of an update fails, AWS CloudFormation automatically rolls back to the previous configuration\. With AWS CloudFormation parameters, you can create multiple environments for your application from the same template\. [AWS SAM](lambda-settingup.md#lambda-settingup-awssam) extends AWS CloudFormation with a simplified syntax focused on Lambda application development\.

The [AWS CLI](lambda-settingup.md#lambda-settingup-awscli) and [SAM CLI](lambda-settingup.md#lambda-settingup-samcli) are command line tools for managing Lambda application stacks\. In addition to commands for managing application stacks with the AWS CloudFormation API, the AWS CLI supports higher\-level commands that simplify tasks like uploading deployment packages and updating templates\. The AWS SAM CLI provides additional functionality, including validating templates, testing locally, and integrating with CI/CD systems\.

When creating an application, you can create its Git repository using either CodeCommit or an AWS CodeStar connection to GitHub\. CodeCommit enables you to use the IAM console to manage SSH keys and HTTP credentials for your users\. AWS CodeStar connections enables you to connect to your GitHub account\. For more information about connections, see [ What are connections?](https://docs.aws.amazon.com/connect/latest/userguide/welcome-connections.html) in the *Developer Tools console User Guide*\.

For more information about designing Lambda applications, see [Application design](https://docs.aws.amazon.com/lambda/latest/operatorguide/application-design.html) in the *Lambda operator guide*\.

**Topics**
+ [Managing applications in the AWS Lambda console](applications-console.md)
+ [Creating an application with continuous delivery in the Lambda console](applications-tutorial.md)
+ [Rolling deployments for Lambda functions](lambda-rolling-deployments.md)
+ [Invoking Lambda functions with the AWS Mobile SDK for Android](with-on-demand-custom-android.md)