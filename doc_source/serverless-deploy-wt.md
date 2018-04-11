# Create Your Own Serverless Application<a name="serverless-deploy-wt"></a>

In the following tutorial, you create a simple serverless application that consists of a single Node\.js function that returns the name of an Amazon S3 bucket you specify as an environment variable\. Follow these steps:

1. Copy and paste the following Node\.js code into a text file and save it as `index.js`\. This represents your Lambda function\. 

   ```
   var AWS = require('aws-sdk');
        
   
   exports.handler = function(event, context, callback) {  
     var bucketName = process.env.S3_BUCKET;       
     callback(null, bucketName);     
   }
   ```

1. Paste the following into a text file and save it as `example.yaml`\. Note that the `Runtime` parameter uses `nodejs6.10` but you can also specify `nodejs8.10` or `nodejs4.3`\.

   ```
   AWSTemplateFormatVersion: '2010-09-09'
   Transform: AWS::Serverless-2016-10-31
   Resources:
     TestFunction:
       Type: AWS::Serverless::Function
       Properties:
         Handler: index.handler
         Runtime: nodejs6.10
         Environment:
           Variables: 
             S3_BUCKET: bucket-name
   ```

1. Create a folder called *examplefolder* and place the `example.yaml` file and the `index.js` file inside the folder\.

   Your *example* folder now contains the following two files that you can then use to package the serverless application:
   + `example.yaml`
   +  `index.js` 

## Packaging and Deployment<a name="serverless-deploy"></a>

After you create your Lambda function handler and your *example\.yaml* file, you can use the AWS CLI to package and deploy your serverless application\.

### Packaging<a name="serverless-pack"></a>

To package your application, create an Amazon S3 bucket that the `package` command will use to upload your ZIP deployment package \(if you haven't specified one in your *example\.yaml* file\)\. You can use the following command to create the Amazon S3 bucket: 

```
aws s3 mb s3://bucket-name --region region
```

Next, open a command prompt and type the following:

```
aws cloudformation package \
   --template-file file-path/example.yaml \
   --output-template-file serverless-output.yaml \
   --s3-bucket s3-bucket-name
```

The package command returns an AWS SAM template, in this case `serverless-output.yaml` that contains the `CodeUri` that points to the deployment zip in the Amazon S3 bucket that you specified\. This template represents your serverless application\. You are now ready to deploy it\.

### Deployment<a name="serv-deploy"></a>

To deploy the application, run the following command:

```
aws cloudformation deploy \
   --template-file serverless-output.yaml \
   --stack-name new-stack-name \
   --capabilities CAPABILITY_IAM
```

Note that the value you specify for the `--template-file` parameter is the name of the SAM template that was returned by the package command\. In addition, the `--capabilities` parameter is optional\. The `AWS::Serverless::Function` resource will implicitly create a role to execute the Lambda function if one is not specified in the template\. You use the `--capabilities` parameter to explicitly acknowledge that AWS CloudFormation is allowed to create roles on your behalf\.

When you run the `aws cloudformation deploy` command, it creates an AWS CloudFormation `ChangeSet`, which is a list of changes to the AWS CloudFormation stack, and then deploys it\. Some stack templates might include resources that can affect permissions in your AWS account, for example, by creating new AWS Identity and Access Management \(IAM\) users\. For those stacks, you must explicitly acknowledge their capabilities by specifying the `--capabilities` parameter\. For more information, see [CreateChangeSet](http://docs.aws.amazon.com/AWSCloudFormation/latest/APIReference/API_CreateChangeSet.html) in the *AWS CloudFormation API Reference*\.

To verify your results, open the AWS CloudFormation console to view the newly created AWS CloudFormation stack and the Lambda console to view your function\.

For a list of complete serverless application examples, see [Examples of How to Use AWS Lambda](use-cases.md)\.

### Exporting a Serverless Application<a name="serverless-export"></a>

You can export a serverless application and re\-deploy it to, for example, a different AWS region or development stage, using the Lambda console\. When you export a Lambda function, you will be provided with a ZIP deployment package and a SAM template that represents your serverless application\. You can then use the `package` and `deploy` commands described in the previous section for re\-deployment\.

 You can also select one of Lambda blueprints to create a ZIP package for you to package and deploy\. Follow the steps following to do this:

**To export a serverless application using the Lambda console**

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. Do any of the following: 
   + **Create a function using a Lambda blueprint** – Choose a blueprint and follow the steps to create a Lambda function\. For an example, see [Create a Simple Lambda Function](get-started-create-function.md)\. When you reach the **Review** page, choose **Export function**\.
   + **Create a function** – Choose **Create function**, and then create your function\. After your Lambda function is created, you can export it by selecting the function\. Choose **Actions**, then choose **Export function**\. 
   + **Open an existing Lambda function** – Open the function by choosing the **Function name**, choose **Actions**, choose **Export function**\.

1. In the **Export your function** window, you have the following options:
   + Choose **Download AWS SAM file**, which defines the Lambda function and other resources that comprise your serverless application\.
   + Choose **Download deployment package**, which contains your Lambda function code and any dependent libraries\.

Use the AWS SAM file and the ZIP deployment package and follow the steps in [Packaging and Deployment](#serverless-deploy) to re\-deploy the serverless application\.
