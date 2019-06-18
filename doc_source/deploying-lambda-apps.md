# AWS Lambda Applications<a name="deploying-lambda-apps"></a>

An AWS Lambda application is a combination of Lambda functions, event sources, and other resources that work together to perform tasks\. You can use AWS CloudFormation and other tools to collect your application's components into a single package that can be deployed and managed as one resource\. Applications make your Lambda projects portable and enable you to integrate with additional developer tools, such as AWS CodePipeline, AWS CodeBuild, and the AWS Serverless Application Model command line interface \(SAM CLI\)\.

The [AWS Serverless Application Repository](https://docs.aws.amazon.com/serverlessrepo/latest/devguide/) provides a collection of Lambda applications that you can deploy in your account with a few clicks\. The repository includes both ready\-to\-use applications and samples that you can use as a starting point for your own projects\. You can also submit your own projects for inclusion\.

[AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-whatis-concepts.html) enables you to create a template that defines your application's resources and lets you manage the application as a *stack*\. You can more safely add or modify resources in your application stack\. If any part of an update fails, AWS CloudFormation automatically rolls back to the previous configuration\. With AWS CloudFormation parameters, you can create multiple environments for your application from the same template\.

The [AWS Serverless Application Model](serverless_app.md) \(AWS SAM\) is an extension for the AWS CloudFormation template language that lets you define serverless applications at a higher level\. It abstracts away common tasks such as function role creation, which makes it easier to write templates\. AWS SAM is supported directly by AWS CloudFormation, and includes additional functionality through the AWS CLI and AWS SAM CLI\.

The [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/) and [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-reference.html) are command line tools for managing Lambda application stacks\. In addition to commands for managing application stacks with the AWS CloudFormation API, the AWS CLI supports higher\-level commands that simplify tasks like uploading deployment packages and updating templates\. The AWS SAM CLI provides additional functionality, including validating templates and testing locally\.

**Topics**
+ [Managing Applications in the AWS Lambda Console](applications-console.md)
+ [Using the AWS Serverless Application Model \(AWS SAM\)](serverless_app.md)
+ [Error Processor Sample Application for AWS Lambda](sample-errorprocessor.md)
+ [Building a Continuous Delivery Pipeline for a Lambda Application with AWS CodePipeline](build-pipeline.md)
+ [Best Practices for Working with AWS Lambda Functions](best-practices.md)