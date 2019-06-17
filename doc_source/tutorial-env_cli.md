# Create a Lambda Function Using Environment Variables<a name="tutorial-env_cli"></a>

This section will illustrate how you can modify a Lambda function's behavior through configuration changes that require no changes to the Lambda function code\. 

In this tutorial, you will do the following: 
+ Create a deployment package with sample code that returns the value of an environment variable that specifies the name of an Amazon S3 bucket\.
+ Invoke a Lambda function and verify that the Amazon S3 bucket name that is returned matches the value set by the environment variable\.
+ Update the Lambda function by changing the Amazon S3 bucket name specified by the environment variable\.
+ Invoke the Lambda function again and verify that the Amazon S3 bucket name that is returned matches the updated value\.

## Set Up the Lambda Environment<a name="env-test-function"></a>

The code sample below reads the environment variable of a Lambda function that returns the name of an Amazon S3 bucket\.

1. Open a text editor and copy the following code:

   ```
   exports.handler = function(event, context, callback) {
     var bucketName = process.env.S3_BUCKET;
     callback(null, bucketName);
   };
   ```

1.  Save the file as *index\.js*\. 

1. Zip the *index\.js\.* file as *Test\_Environment\_Variables\.zip*\.

## Create an Execution Role<a name="env-create-exec-role"></a>

Create an IAM role \(execution role\) that you can specify at the time you create your Lambda function\. 

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Follow the steps in [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following: 
   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\.
   + In **Attach Policy**, choose the policy named **AWSLambdaBasicExecutionRole**\. 

1. Write down the Amazon Resource Name \(ARN\) of the IAM role\. You need this value when you create your Lambda function in the next step\.

## Create the Lambda function and Test It<a name="with-env-create-function"></a>

In this section, you create a Lambda function containing an environment variable that specifies an Amazon S3 bucket named `Test`\. When invoked, the function simply returns the name of the Amazon S3 bucket\. Then you update the configuration by changing the Amazon S3 bucket name to `Prod` and when invoked again, the function returns the updated name of the Amazon S3 bucket\. 

To create the Lambda function, open a command prompt and run the following Lambda AWS CLI `create-function` command\. You need to provide the \.zip file path and the execution role ARN\.

```
$ aws lambda  create-function --function-name ReturnBucketName \
--zip-file fileb://file-path/Test_Environment_Variables.zip \
--role role-arn \
--environment Variables={S3_BUCKET=Test} \
--handler index.handler --runtime nodejs8.10
```

Next, run the following Lambda CLI `invoke` command to invoke the function\.

```
$ aws lambda invoke --function-name ReturnBucketName outputfile.txt
```

The Lambda function will return the name of the Amazon S3 bucket as "Test"\.

Next, run the following Lambda CLI `update-function-configuration` command to update the Amazon S3 environment variable by pointing it to the `Prod` bucket\.

```
$ aws lambda update-function-configuration --function-name ReturnBucketName \
--environment Variables={S3_BUCKET=Prod}
```

Run the `aws lambda invoke` command again using the same parameters\. This time, the Lambda function will return the Amazon S3 bucket name as `Prod`\.