# Building a Pipeline for Your Serverless Application<a name="build-pipeline"></a>

 In the following tutorial, you will create an AWS CodePipeline that automates the deployment of your serverless application\.  First, you will need to set up a **source stage** to trigger your pipeline\. For the purposes of this tutorial:
+ We will use GitHub\. For instructions on how to create a GitHub repository, see [Create a Repository in GitHub](https://help.github.com/articles/create-a-repo/)\.
+ You will need to create an AWS CloudFormation role and add the **AWSLambdaExecute** policy to that role, as outlined below:

  1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

  1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\) and go to the **To create a role for an AWS service** section\. As you follow the steps to create a role, note the following:
     + In **Select Role Type**, choose **AWS Service Roles**, and then choose **CloudFormation**\. Choose **Next: Permissions**\.
     + In **Attach permissions policies**, use the search bar to find and then choose **AWSLambdaExecute**\. Choose **Next: Review**\. 
     + In **Role Name**, use a name that is unique within your AWS account \(for example, **cloudformation\-lambda\-execution\-role**\) and then choose **Create role**\. 
     + Open the role you just created and under the **Permissions** tab, choose **Add inline policy**\. 
     + In **Create Policy** choose the **JSON** tab and enter the following: 
**Note**  
Make sure to replace the *region* and *id* placeholders with your region and account id\.

       ```
       {
       	"Statement": [
       	  {
       			"Action": [
       				"s3:GetObject",
       				"s3:GetObjectVersion",
       				"s3:GetBucketVersioning"
       			],
       			"Resource": "*",
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"s3:PutObject"
       			],
       			"Resource": [
       				"arn:aws:s3:::codepipeline*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"lambda:*"
       			],
       			"Resource": [
       				"arn:aws:lambda:region:id:function:*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"apigateway:*"
       			],
       			"Resource": [
       				"arn:aws:apigateway:region::*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"iam:GetRole",
       				"iam:CreateRole",
       				"iam:DeleteRole",
       				"iam:PutRolePolicy"
       			],
       			"Resource": [
       				"arn:aws:iam::id:role/*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"iam:AttachRolePolicy",
       				"iam:DeleteRolePolicy",
       				"iam:DetachRolePolicy"
       			],
       			"Resource": [
       				"arn:aws:iam::id:role/*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"iam:PassRole"
       			],
       			"Resource": [
       				"*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"cloudformation:CreateChangeSet"
       			],
       			"Resource": [
       				"arn:aws:cloudformation:region:aws:transform/Serverless-2016-10-31"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"codedeploy:CreateApplication",
       				"codedeploy:DeleteApplication",
       				"codedeploy:RegisterApplicationRevision"
       			],
       			"Resource": [
       				"arn:aws:codedeploy:region:id:application:*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"codedeploy:CreateDeploymentGroup",
       				"codedeploy:CreateDeployment",
       				"codedeploy:GetDeployment"
       			],
       			"Resource": [
       				"arn:aws:codedeploy:region:id:deploymentgroup:*"
       			],
       			"Effect": "Allow"
       		},
       		{
       			"Action": [
       				"codedeploy:GetDeploymentConfig"
       			],
       			"Resource": [
       				"arn:aws:codedeploy:region:id:deploymentconfig:*"
       			],
       			"Effect": "Allow"
       		}
       	],
       	"Version": "2012-10-17"
       }
       ```
     + Choose **Validate Policy** and then choose **Apply Policy**\. 

## Step 1: Set Up Your Repository<a name="setup-repository"></a>

You can use any of the Lambda supported runtimes when setting up a repository\. The following example uses Node\.js\.

To set up your repository, do the following:
+ Add an *index\.js file* containing the code following:

  ```
  var time = require('time');
  exports.handler = (event, context, callback) => {
      var currentTime = new time.Date(); 
      currentTime.setTimezone("America/Los_Angeles");
      callback(null, {
          statusCode: '200',
          body: 'The time in Los Angeles is: ' + currentTime.toString(),
      });
  };
  ```
+ Add a *samTemplate\.yaml* file, containing the content following\. This is for the SAM template that defines the resources in your application\. This SAM template defines a Lambda function that is triggered by API Gateway\. Note that the `runtime` parameter uses `nodejs6.10` but you can also specify `nodejs8.10`\. For more information about AWS SAM see [AWS Serverless Application Model](https://github.com/awslabs/serverless-application-model)\.

  ```
  AWSTemplateFormatVersion: '2010-09-09'
  Transform: AWS::Serverless-2016-10-31
  Description: Outputs the time
  Resources:
    TimeFunction:
      Type: AWS::Serverless::Function
      Properties:
        Handler: index.handler
        Runtime: nodejs6.10
        CodeUri: ./
        Events:
          MyTimeApi:
            Type: Api
            Properties:
              Path: /TimeResource
              Method: GET
  ```
+ Add a *buildspec\.yml* file\. A build spec is a collection of build commands and related settings, in YAML format, that AWS CodeBuild uses to run a build\. For more information, see [Build Specification Reference for AWS CodeBuild](http://docs.aws.amazon.com/codebuild/latest/userguide/build-spec-ref.html)\. In this example, the build action will be:
  + Use [npm](https://www.npmjs.com/) to install the time package\.
  + Run the `Package` command to prepare your deployment package for subsequent deployment steps in your pipeline\. For more information on the package command, see [Uploading Local Artifacts to an S3 Bucket](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-cli-package.html)

    ```
    version: 0.2
    phases:
      install:
        commands:
          - npm install time
          - aws cloudformation package --template-file samTemplate.yaml --kms-key-id kms-key-id --s3-bucket bucket-name 
                                       --output-template-file outputSamTemplate.yaml
    artifacts:
      type: zip
      files:
        - samTemplate.yaml
        - outputSamTemplate.yaml
    ```

    Note that you need to supply the `--s3-bucket` parameter value with the name of the your Amazon S3 bucket, similar to the step you would take if you were manually going to package the deployment package with SAM, as discussed in the [Packaging](serverless-deploy-wt.md#serverless-pack) step of the previous tutorial\.

## Step 2: Create Your Pipeline<a name="create-pipeline1"></a>

****

Follow the steps following to create your AWS CodePipeline\.

1. Sign in to the AWS Management Console and open the AWS CodePipeline console\.

1. Choose **Get Started Now**\.

1. In **Pipeline name:** enter a name for your pipeline and then choose **Next step**\.

1. In **Source provider:** choose **GitHub**\.

1. Choose **Connect to GitHub:** and then choose the **Repository **and **Branch** you want to connect to\. Every git push to the branch you select will trigger your pipeline\. Choose **Next step**\.

1. Choose **AWS CodeBuild** as your **Build provider**\.

1. Choose **Create a new build project** and enter a project name\.

1. Choose **Ubuntu** as the operating system\.

1. Choose **Node\.js** as the runtime\.

1. In **Version** choose `aws/codebuild/nodejs:version`

1. In **Build specification** choose `Use the buildspec.yml in the source code root directory`

1. Choose **Save build project**\. 
**Note**  
A service role for AWS CodeBuild will automatically be created on your behalf\.

   Choose **Next step**\.

1. In **Deployment provider\* ** choose **AWS CloudFormation**\.

   By selecting this option, AWS CloudFormation commands will be used to deploy the SAM template\. For more information see [Serverless Resources Within AWS SAM](serverless_app.md#serverless_app_resources)\.

1. In **Action mode:** choose **Create or replace a change set**\.

1. In **Stack name:** enter **MyBetaStack**\.

1. In **Change set name:** enter **MyChangeSet**\.

1. In **Template file:** enter **outputSamTemplate\.yaml**\.

1. In **Capabilities:** choose **CAPABILITY\_IAM**\.

1. In **Role** select the AWS CloudFormation role you created at the beginning of this tutorial and then choose **Next step**\.

1. Choose **Create role**\. Choose **Next** and then choose **Allow\.** Choose **Next step**\.

1. Review your pipeline and then choose **Create pipeline**\. 

## Step 3: Update the Generated Service Policy<a name="update-policy"></a>

****

Complete the following steps to allow CodeBuild to upload build artifacts to your Amazon S3 bucket\.

1. Go to the IAM Management Console\.

1. Choose **Roles**\.

1. Open the service role that was generated for your project, typically **code\-build\-*project\-name*\-service\-role**\.

1. Under the **Permissions** tab, choose **Add inline policy**\.

1. In **service**, choose **Choose a service**\.

1. In **Select a service below**, choose **S3**\.

1. In **Actions**, choose **Select actions**\.

1. Expand **Write** under **Access level groups** and then choose **PutObject**\.

1. Choose **Resources** and then choose the **Any** checkbox\.

1. Choose **Review policy**\.

1. Enter a **Name\*** and then choose **Create policy**\. Then return to the pipeline you created in the previous section\.

## Step 4: Complete Your Beta Deployment Stage<a name="create-pipeline2"></a>

****

Use the following steps to complete your Beta stage\.

1. Choose **Edit**\.

1. Choose the **\+** icon next to **MyBetaStack**\.

1. In **Action category:**, if not already selected, choose **Deploy**\.

1. In **Deployment provider\***, if not already selected, choose **AWS CloudFormation**\.

1. In **Action mode\* ** choose **Execute a change set**\. This is similar to the step you would take if you were manually going to deploy the package, as discussed in the [Deployment](serverless-deploy-wt.md#serv-deploy) step of the previous tutorial\. `CreateChangeSet` transforms the SAM template to the full AWS CloudFormation format and `deployChangeSet` deploys the AWS CloudFormation template\. 

1. In **Stack name\* ** enter or choose **MyBetaStack**\.

1. In **Change set name\* ** enter **MyChangeSet**\.

1. Choose **Add action**\.

1. Choose **Save pipeline changes**\.

1. Choose **Save and continue**\.

 Your pipeline is ready\. Any git push to the branch you connected to this pipeline is going to trigger a deployment\. To test your pipeline and deploy your application for the first time, do one of the following: 
+ Perform a git push to the branch connected to your pipeline\.
+ Go the AWS CodePipeline console, choose the name of the pipeline you created and then choose **Release change**\. 

## Next Step<a name="automating-deployment-next-step1"></a>

[Gradual Code Deployment](automating-updates-to-serverless-apps.md)