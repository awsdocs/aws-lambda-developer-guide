# Step 2: Create a Lambda Function and Invoke It Manually \(Using Sample Event Data\)<a name="with-s3-example-create-test-manually"></a>

In this section, you do the following:
+ Create a Lambda function deployment package using the sample code provided\.
**Note**  
To see more examples of using other AWS services within your function, including calling other Lambda functions, see [AWS SDK for JavaScript](http://docs.aws.amazon.com/AWSJavaScriptSDK/latest/frames.html)
+ Create an IAM role \(execution role\)\. At the time you upload the deployment package, you need to specify an IAM role \(execution role\) that Lambda can assume to execute the function on your behalf\. 
+ Create the Lambda function by uploading the deployment package, and then test it by invoking it manually using sample Amazon S3 event data\.

**Topics**
+ [Step 2\.1: Create a Deployment Package](with-s3-example-deployment-pkg.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](with-s3-example-create-iam-role.md)
+ [Step 2\.3: Create the Lambda Function and Test It Manually](with-s3-example-upload-deployment-pkg.md)