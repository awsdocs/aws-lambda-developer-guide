# Creating an application with continuous delivery in the Lambda console<a name="applications-tutorial"></a>

You can use the Lambda console to create an application with an integrated continuous delivery pipeline\. With continuous delivery, every change that you push to your source control repository triggers a pipeline that builds and deploys your application automatically\. The Lambda console provides starter projects for common application types with Node\.js sample code and templates that create supporting resources\.

In this tutorial, you create the following resources\.
+ **Application** – A Node\.js Lambda function, build specification, and AWS Serverless Application Model \(AWS SAM\) template\.
+ **Pipeline** – An AWS CodePipeline pipeline that connects the other resources to enable continuous delivery\.
+ **Repository** – A Git repository in AWS CodeCommit\. When you push a change, the pipeline copies the source code into an Amazon S3 bucket and passes it to the build project\.
+ **Trigger** – An Amazon EventBridge \(CloudWatch Events\) rule that watches the main branch of the repository and triggers the pipeline\.
+ **Build project** – An AWS CodeBuild build that gets the source code from the pipeline and packages the application\. The source includes a build specification with commands that install dependencies and prepare the application template for deployment\.
+ **Deployment configuration** – The pipeline's deployment stage defines a set of actions that take the processed AWS SAM template from the build output, and deploy the new version with AWS CloudFormation\.
+ **Bucket** – An Amazon Simple Storage Service \(Amazon S3\) bucket for deployment artifact storage\.
+ **Roles** – The pipeline's source, build, and deploy stages have IAM roles that allow them to manage AWS resources\. The application's function has an [execution role](lambda-intro-execution-role.md) that allows it to upload logs and can be extended to access other services\.

Your application and pipeline resources are defined in AWS CloudFormation templates that you can customize and extend\. Your application repository includes a template that you can modify to add Amazon DynamoDB tables, an Amazon API Gateway API, and other application resources\. The continuous delivery pipeline is defined in a separate template outside of source control and has its own stack\.

The pipeline maps a single branch in a repository to a single application stack\. You can create additional pipelines to add environments for other branches in the same repository\. You can also add stages to your pipeline for testing, staging, and manual approvals\. For more information about AWS CodePipeline, see [What is AWS CodePipeline](https://docs.aws.amazon.com/codepipeline/latest/userguide/welcome.html)\.

**Topics**
+ [Prerequisites](#applications-tutorial-prepare)
+ [Create an application](#applications-tutorial-wizard)
+ [Invoke the function](#applications-tutorial-invoke)
+ [Add an AWS resource](#applications-tutorial-update)
+ [Update the permissions boundary](#applications-tutorial-permissions)
+ [Update the function code](#applications-tutorial-code)
+ [Next steps](#applications-tutorial-nextsteps)
+ [Troubleshooting](#applications-tutorial-troubleshooting)
+ [Clean up](#applications-tutorial-cleanup)

## Prerequisites<a name="applications-tutorial-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Create a Lambda function with the console](getting-started.md#getting-started-create-function) to create your first Lambda function\.

To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

```
aws --version
```

You should see the following output:

```
aws-cli/2.0.57 Python/3.7.4 Darwin/19.6.0 exe/x86_64
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\.

**Note**  
On Windows, some Bash CLI commands that you commonly use with Lambda \(such as `zip`\) are not supported by the operating system's built\-in terminals\. To get a Windows\-integrated version of Ubuntu and Bash, [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. 

This tutorial uses CodeCommit for source control\. To set up your local machine to access and update application code, see [Setting up](https://docs.aws.amazon.com/codecommit/latest/userguide/setting-up.html) in the *AWS CodeCommit User Guide*\.

## Create an application<a name="applications-tutorial-wizard"></a>

Create an application in the Lambda console\. In Lambda, an application is an AWS CloudFormation stack with a Lambda function and any number of supporting resources\. In this tutorial, you create an application that has a function and its execution role\.

**To create an application**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose **Create application**\.

1. Choose **Author from scratch**\.

1. Configure application settings\.
   + **Application name** – **my\-app**\.
   + **Runtime** – **Node\.js 14\.x**\.
   + **Source control service** – **CodeCommit**\.
   + **Repository name** – **my\-app\-repo**\.
   + **Permissions** – **Create roles and permissions boundary**\.

1. Choose **Create**\.

Lambda creates the pipeline and related resources and commits the sample application code to the Git repository\. As resources are created, they appear on the overview page\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/application-create-resources.png)

The **Infrastructure** stack contains the repository, build project, and other resources that combine to form a continuous delivery pipeline\. When this stack finishes deploying, it in turn deploys the application stack that contains the function and execution role\. These are the application resources that appear under **Resources**\.

## Invoke the function<a name="applications-tutorial-invoke"></a>

When the deployment process completes, invoke the function from the Lambda console\.

**To invoke the application's function**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose **my\-app**\.

1. Under **Resources**, choose **helloFromLambdaFunction**\.

1. Choose **Test**\.

1. Configure a test event\.
   + **Event name** – **event**
   + **Body** – **\{\}**

1. Choose **Create**\.

1. Choose **Test**\.

The Lambda console runs your function and displays the result\. Expand the **Details** section under the result to see the output and execution details\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/application-create-result.png)

## Add an AWS resource<a name="applications-tutorial-update"></a>

In the previous step, Lambda console created a Git repository that contains function code, a template, and a build specification\. You can add resources to your application by modifying the template and pushing changes to the repository\. To get a copy of the application on your local machine, clone the repository\.

**To clone the project repository**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose **my\-app**\.

1. Choose **Code**\.

1. Under **Repository details**, copy the HTTP or SSH repository URI, depending on the authentication mode that you configured during [setup](#applications-tutorial-prepare)\.

1. To clone the repository, use the `git clone` command\.

   ```
   git clone ssh://git-codecommit.us-east-2.amazonaws.com/v1/repos/my-app-repo
   ```

To add a DynamoDB table to the application, define an `AWS::Serverless::SimpleTable` resource in the template\.

**To add a DynamoDB table**

1. Open `template.yml` in a text editor\.

1. Add a table resource, an environment variable that passes the table name to the function, and a permissions policy that allows the function to manage it\.  
**Example template\.yml \- resources**  

   ```
   ...
   Resources:
     ddbTable:
       Type: AWS::Serverless::SimpleTable
       Properties:
         PrimaryKey:
           Name: id
           Type: String
         ProvisionedThroughput:
           ReadCapacityUnits: 1
           WriteCapacityUnits: 1
     helloFromLambdaFunction:
       Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
       Properties:
         CodeUri: ./
         Handler: src/handlers/hello-from-lambda.helloFromLambdaHandler
         Runtime: nodejs14.x
         MemorySize: 128
         Timeout: 60
         Description: A Lambda function that returns a static string.
         Environment:
           Variables:
             DDB_TABLE: !Ref ddbTable
         Policies:
           - DynamoDBCrudPolicy:
               TableName: !Ref ddbTable
           - AWSLambdaBasicExecutionRole
   ```

1. Commit and push the change\.

   ```
   git commit -am "Add DynamoDB table"
   git push
   ```

When you push a change, it triggers the application's pipeline\. Use the **Deployments** tab of the application screen to track the change as it flows through the pipeline\. When the deployment is complete, proceed to the next step\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/application-create-deployment.png)

## Update the permissions boundary<a name="applications-tutorial-permissions"></a>

The sample application applies a *permissions boundary* to its function's execution role\. The permissions boundary limits the permissions that you can add to the function's role\. Without the boundary, users with write access to the project repository could modify the project template to give the function permission to access resources and services outside of the scope of the sample application\.

In order for the function to use the DynamoDB permission that you added to its execution role in the previous step, you must extend the permissions boundary to allow the additional permissions\. The Lambda console detects resources that aren't in the permissions boundary and provides an updated policy that you can use to update it\.

**To update the application's permissions boundary**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose your application\.

1. Under **Resources**, choose **Edit permissions boundary**\.

1. Follow the instructions shown to update the boundary to allow access to the new table\.

For more information about permissions boundaries, see [Using permissions boundaries for AWS Lambda applications](permissions-boundary.md)\.

## Update the function code<a name="applications-tutorial-code"></a>

Next, update the function code to use the table\. The following code uses the DynamoDB table to track the number of invocations processed by each instance of the function\. It uses the log stream ID as a unique identifier for the function instance\.

**To update the function code**

1. Add a new handler named `index.js` to the `src/handlers` folder with the following content\.  
**Example src/handlers/index\.js**  

   ```
   const dynamodb = require('aws-sdk/clients/dynamodb');
   const docClient = new dynamodb.DocumentClient();
   
   exports.handler = async (event, context) => {
       const message = 'Hello from Lambda!';
       const tableName = process.env.DDB_TABLE;
       const logStreamName = context.logStreamName;
       var params = {
           TableName : tableName,
           Key: { id : logStreamName },
           UpdateExpression: 'set invocations = if_not_exists(invocations, :start) + :inc',
           ExpressionAttributeValues: {
               ':start': 0,
               ':inc': 1
           },
           ReturnValues: 'ALL_NEW'
       };
       await docClient.update(params).promise();
   
       const response = {
           body: JSON.stringify(message)
       };
       console.log(`body: ${response.body}`);
       return response;
   }
   ```

1. Open the application template and change the handler value to `src/handlers/index.handler`\.  
**Example template\.yml**  

   ```
   ...
     helloFromLambdaFunction:
       Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
       Properties:
         CodeUri: ./
         Handler: src/handlers/index.handler
         Runtime: nodejs14.x
   ```

1. Commit and push the change\.

   ```
   git add . && git commit -m "Use DynamoDB table"
   git push
   ```

After the code change is deployed, invoke the function a few times to update the DynamoDB table\.

**To view the DynamoDB table**

1. Open the [Tables page of the DynamoDB console](https://console.aws.amazon.com/dynamodb/home#tables:)\.

1. Choose the table that starts with **my\-app**\.

1. Choose **Items**\.

1. Choose **Start search**\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/application-create-ddbtable.png)

Lambda creates additional instances of your function to handle multiple concurrent invocations\. Each log stream in the CloudWatch Logs log group corresponds to a function instance\. A new function instance is also created when you change your function's code or configuration\. For more information on scaling, see [Lambda function scaling](invocation-scaling.md)\.

## Next steps<a name="applications-tutorial-nextsteps"></a>

The AWS CloudFormation template that defines your application resources uses the AWS Serverless Application Model transform to simplify the syntax for resource definitions, and automate uploading the deployment package and other artifacts\. AWS SAM also provides a command line interface \(the AWS SAM CLI\), which has the same packaging and deployment functionality as the AWS CLI, with additional features specific to Lambda applications\. Use the AWS SAM CLI to test your application locally in a Docker container that emulates the Lambda execution environment\.
+ [Installing the AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
+ [Testing and debugging serverless applications with AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-test-and-debug.html)
+ [Deploying serverless applications using CI/CD systems with AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html)

AWS Cloud9 provides an online development environment that includes Node\.js, the AWS SAM CLI, and Docker\. With AWS Cloud9, you can start developing quickly and access your development environment from any computer\. For instructions, see [Getting started](https://docs.aws.amazon.com/cloud9/latest/user-guide/get-started.html) in the *AWS Cloud9 User Guide*\.

For local development, AWS toolkits for integrated development environments \(IDEs\) let you test and debug functions before pushing them to your repository\.
+ [AWS Toolkit for JetBrains](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/) – Plugin for PyCharm \(Python\) and IntelliJ \(Java\) IDEs\.
+ [AWS Toolkit for Eclipse](https://docs.aws.amazon.com/AWSToolkitEclipse/latest/GettingStartedGuide/) – Plugin for Eclipse IDE \(multiple languages\)\.
+ [AWS Toolkit for Visual Studio Code](https://docs.aws.amazon.com/toolkit-for-vscode/latest/userguide/) – Plugin for Visual Studio Code IDE \(multiple languages\)\.
+ [AWS Toolkit for Visual Studio](https://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/) – Plugin for Visual Studio IDE \(multiple languages\)\.

## Troubleshooting<a name="applications-tutorial-troubleshooting"></a>

As you develop your application, you will likely encounter the following types of errors\.
+ **Build errors** – Issues that occur during the build phase, including compilation, test, and packaging errors\.
+ **Deployment errors** – Issues that occur when AWS CloudFormation isn't able to update the application stack\. These include permissions errors, account quotas, service issues, or template errors\.
+ **Invocation errors** – Errors that are returned by a function's code or runtime\.

For build and deployment errors, you can identify the cause of an error in the Lambda console\.

**To troubleshoot application errors**

1. Open the Lambda console [Applications page](https://console.aws.amazon.com/lambda/home#/applications)\.

1. Choose an application\.

1. Choose **Deployments**\.

1. To view the application's pipeline, choose **Deployment pipeline**\.

1. Identify the action that encountered an error\.

1. To view the error in context, choose **Details**\.

For deployment errors that occur during the **ExecuteChangeSet** action, the pipeline links to a list of stack events in the AWS CloudFormation console\. Search for an event with the status **UPDATE\_FAILED**\. Because AWS CloudFormation rolls back after an error, the relevant event is under several other events in the list\. If AWS CloudFormation could not create a change set, the error appears under **Change sets** instead of under **Events**\.

A common cause of deployment and invocation errors is a lack of permissions in one or more roles\. The pipeline has a role for deployments \(`CloudFormationRole`\) that's equivalent to the [user permissions](access-control-identity-based.md) that you would use to update an AWS CloudFormation stack directly\. If you add resources to your application or enable Lambda features that require user permissions, the deployment role is used\. You can find a link to the deployment role under **Infrastructure** in the application overview\.

If your function accesses other AWS services or resources, or if you enable features that require the function to have additional permissions, the function's [execution role](lambda-intro-execution-role.md) is used\. All execution roles that are created in your application template are also subject to the application's permissions boundary\. This boundary requires you to explicitly grant access to additional services and resources in IAM after adding permissions to the execution role in the template\.

For example, to [connect a function to a virtual private cloud](configuration-vpc.md) \(VPC\), you need user permissions to describe VPC resources\. The execution role needs permission to manage network interfaces\. This requires the following steps\.

1. Add the required user permissions to the deployment role in IAM\.

1. Add the execution role permissions to the permissions boundary in IAM\.

1. Add the execution role permissions to the execution role in the application template\.

1. Commit and push to deploy the updated execution role\.

After you address permissions errors, choose **Release change** in the pipeline overview to rerun the build and deployment\.

## Clean up<a name="applications-tutorial-cleanup"></a>

You can continue to modify and use the sample to develop your own application\. If you are done using the sample, delete the application to avoid paying for the pipeline, repository, and storage\.

**To delete the application**

1. Open the [AWS CloudFormation console](https://console.aws.amazon.com/cloudformation)\.

1. Delete the application stack – **my\-app**\.

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Delete the artifact bucket – ***us\-east\-2*\-*123456789012*\-my\-app\-pipe**\.

1. Return to the AWS CloudFormation console and delete the infrastructure stack – **serverlessrepo\-my\-app\-toolchain**\.

Function logs are not associated with the application or infrastructure stack in AWS CloudFormation\. Delete the log group separately in the CloudWatch Logs console\.

**To delete the log group**

1. Open the [Log groups page](https://console.aws.amazon.com/cloudwatch/home#logs:) of the Amazon CloudWatch console\.

1. Choose the function's log group \(`/aws/lambda/my-app-helloFromLambdaFunction-YV1VXMPLK7QK`\)\.

1. Choose **Actions**, and then choose **Delete log group**\.

1. Choose **Yes, Delete**\.