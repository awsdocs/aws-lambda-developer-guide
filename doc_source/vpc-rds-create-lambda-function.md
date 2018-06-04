# Step 2: Create a Lambda Function<a name="vpc-rds-create-lambda-function"></a>

In this step, you do the following:
+ Create a Lambda function deployment package using the sample code provided\. 
+ Create an IAM role \(execution role\) that you specify at the time of creating your Lambda function\. This is the role AWS Lambda assumes when executing the Lambda function\. 

  The permissions policy associated with this role grants AWS Lambda permissions to set up elastic network interfaces \(ENIs\) to enable your Lambda function to access resources in the VPC\.
+ Create the Lambda function by uploading the deployment package\. 

**Topics**
+ [Step 2\.1: Create a Deployment Package](vpc-rds-deployment-pkg.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](vpc-rds-create-iam-role.md)
+ [Step 2\.3: Create the Lambda Function \(Upload the Deployment Package\)](vpc-rds-upload-deployment-pkg.md)