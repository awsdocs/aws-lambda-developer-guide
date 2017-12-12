# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-s3-example-create-iam-role"></a>

In this section, you create an IAM role using the following predefined role type and access permissions policy:

+ AWS service role of the type **AWS Lambda** – This role grants AWS Lambda permissions to assume the role\. 

+ **AWSLambdaExecute** access permissions policy that you attach to the role\. 

+ Enter a **Role name** and then choose **Create role**\. 

 For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. Use the following procedure to create the IAM role\.

**To create an IAM role \(execution role\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following:

   + In **Role Name**, use a name that is unique within your AWS account \(for example, **lambda\-s3\-execution\-role**\)\. 

   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\. This grants the AWS Lambda service permissions to assume the role\.

   + In **Attach Policy**, choose **AWSLambdaBasicExecuteRole**\.

1. Write down the role ARN\. You will need it in the next step when you create your Lambda function\.

## Next Step<a name="with-s3-next-step-4"></a>

[Step 2\.3: Create the Lambda Function and Test It Manually](with-s3-example-upload-deployment-pkg.md)