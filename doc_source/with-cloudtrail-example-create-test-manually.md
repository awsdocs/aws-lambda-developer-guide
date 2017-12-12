# Step 2: Create a Lambda Function and Invoke It Manually \(Using Sample Event Data\)<a name="with-cloudtrail-example-create-test-manually"></a>

In this section, you do the following:

+ Create a Lambda function deployment package using the sample code provided\. The sample Lambda function code that you'll use to process Amazon S3 events is provided in various languages\. Select one of the languages and follow the corresponding instructions to create a deployment package\.
**Note**  
Your Lambda function uses an S3 event that provides the bucket name and key name of the object CloudTrail created\. Your Lambda function then reads that object to process CloudTrail records\.

+ Create an IAM role \(execution role\)\. At the time you upload the deployment package, you need to specify an IAM role \(execution role\) that Lambda can assume to execute the function on your behalf\. 

+ Create the Lambda function by uploading the deployment package, and then test it by invoking it manually using sample CloudTrail event data\. 


+ [Step 2\.1: Create a Deployment Package](with-cloudtrail-example-deployment-pkg.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](with-cloudtrail-example-create-iam-role.md)
+ [Step 2\.3: Create the Lambda Function and Test It Manually](with-cloudtrail-example-upload-deployment-pkg.md)