# Step 2: Create a Lambda Function<a name="vpc-ec-create-lambda-function"></a>

In this step, you do the following:
+ Create a Lambda function deployment package using the sample code provided\. 
+ Create an IAM role \(execution role\)\. At the time you upload the deployment package, you need to specify this role so that Lambda can assume the role and then execute the function on your behalf\. 

  The permissions policy grants AWS Lambda permissions to set up elastic network interfaces \(ENIs\) to enable your Lambda function to access resources in the VPC\. In this example, your Lambda function accesses an ElastiCache cluster in the VPC\.
+ Create the Lambda function by uploading the deployment package\. 

**Topics**
+ [Step 2\.1: Create a Deployment Package](vpc-ec-deployment-pkg.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](vpc-ec-create-iam-role.md)
+ [Step 2\.3: Create the Lambda Function \(Upload the Deployment Package\)](vpc-ec-upload-deployment-pkg.md)