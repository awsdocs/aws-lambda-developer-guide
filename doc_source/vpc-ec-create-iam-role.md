# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="vpc-ec-create-iam-role"></a>

In this step, you create an AWS Identity and Access Management \(IAM\) role using the following predefined role type and access permissions policy:
+ **AWS Lambda** \(AWS service role\) – This role grants AWS Lambda permissions to assume the role\. 
+ **AWSLambdaVPCAccessExecutionRole** \(access permissions policy\) – This is the policy that you attach to the role\. The policy grants permissions for the EC2 actions that AWS Lambda needs to manage ENIs\. You can view this AWS managed policy in IAM console\.

 For more information about IAM roles, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. Use the following procedure to create the IAM role\.

**To create an IAM role \(execution role\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following:
   + In **Role Name**, use a name that is unique within your AWS account \(for example, **lambda\-vpc\-execution\-role**\)\. 
   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\. This grants the AWS Lambda service permissions to assume the role\.
   + In **Attach Policy**, choose **AWSLambdaVPCAccessExecutionRole**\. The permissions in this policy are sufficient for the Lambda function in this tutorial\.

1. Write down the role ARN\. You will need it in the next step when you create your Lambda function\.