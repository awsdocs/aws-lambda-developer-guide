# Create a Lambda Function Using Environment Variables<a name="tutorial-env_cli"></a>

This section will illustrate how you can modify a Lambda function's behavior through configuration changes that require no changes to the Lambda function code\. 

In this tutorial, you will do the following: 
+ Create a deployment package with sample code that returns the value of an environment variable that specifies the name of an Amazon S3 bucket\.
+ Invoke a Lambda function and verify that the Amazon S3 bucket name that is returned matches the value set by the environment variable\.
+ Update the Lambda function by changing the Amazon S3 bucket name specified by the environment variable\.
+ Invoke the Lambda function again and verify that the Amazon S3 bucket name that is returned matches the updated value\.

## Step 1: Prepare<a name="with-env-prepare"></a>

Make sure you have completed the following steps:
+ Signed up for an AWS account and created an administrator user in the account \(called **adminuser**\)\. For instructions, see [Set Up an AWS Account](setup.md) 
+ Installed and set up the AWS CLI\. For instructions, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)

## Step 2: Set Up the Lambda Environment<a name="env-test-function"></a>

In this section, you do the following:
+ Create the Lambda function deployment package using the sample code provided\.
+ Create a Lambda execution role\.
+ Create the Lambda function by uploading the deployment package, and then test it by invoking it manually\.

### Step 2\.1: Create the Deployment Package<a name="env-create-package"></a>

The code sample below reads the environment variable of a Lambda function that returns the name of an Amazon S3 bucket\.

1. Open a text editor and copy the following code:

   ```
   var AWS = require('aws-sdk');
        
       exports.handler = function(event, context, callback) {
           
           var bucketName = process.env.S3_BUCKET;       
       	callback(null, bucketName);        
       }
   ```

1.  Save the file as *index\.js*\. 

1. Zip the *index\.js\.* file as *Test\_Environment\_Variables\.zip*\.

### Step 2\.2: Create an Execution Role<a name="env-create-exec-role"></a>

Create an IAM role \(execution role\) that you can specify at the time you create your Lambda function\. 

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Follow the steps in [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following: 
   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\.
   + In **Attach Policy**, choose the policy named **AWSLambdaBasicExecutionRole**\. 

1. Write down the Amazon Resource Name \(ARN\) of the IAM role\. You need this value when you create your Lambda function in the next step\.

### Step 2\.3 Create the Lambda function and Test It<a name="with-env-create-function"></a>

In this section, you create a Lambda function containing an environment variable that specifies an Amazon S3 bucket named `Test`\. When invoked, the function simply returns the name of the Amazon S3 bucket\. Then you update the configuration by changing the Amazon S3 bucket name to `Prod` and when invoked again, the function returns the updated name of the Amazon S3 bucket\. 

To create the Lambda function, open a command prompt and run the following Lambda AWS CLI `create-function` command\. You need to provide the \.zip file path and the execution role ARN\. Note that the `Runtime` parameter uses `nodejs6.10` but you can also specify `nodejs8.10`\.

```
aws lambda  create-function \
--region us-east-1 \
--function-name ReturnBucketName \
--zip-file fileb://file-path/Test_Environment_Variables.zip \
--role role-arn \
--environment Variables={S3_BUCKET=Test} \
--handler index.handler \
--runtime nodejs6.10 \
--version  version \
--profile default
```

**Note**  
Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter with the `--code` parameter\. For example:  

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

Next, run the following Lambda CLI `invoke` command to invoke the function\. Note that the command requests asynchronous execution\. You can optionally invoke it synchronously by specifying `RequestResponse` as the `invocation-type` parameter value\.

```
aws lambda invoke \
--invocation-type Event \
--function-name ReturnBucketName \
--region us-east-1 \
--profile default \
outputfile.txt
```

The Lambda function will return the name of the Amazon S3 bucket as "Test"\.

Next, run the following Lambda CLI `update-function-configuration` command to update the Amazon S3 environment variable by pointing it to the `Prod` bucket\.

```
aws lambda update-function-configuration
--function-name ReturnBucketName \
--region us-east-1 \
--environment Variables={S3_BUCKET=Prod}
```

Run the `aws lambda invoke` command again using the same parameters\. This time, the Lambda function will return the Amazon S3 bucket name as `Prod`\.