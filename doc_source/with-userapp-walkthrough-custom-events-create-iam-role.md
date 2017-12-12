# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-userapp-walkthrough-custom-events-create-iam-role"></a>

When the Lambda function in this tutorial executes, it needs permissions to write logs to Amazon CloudWatch\. You grant these permissions by creating an IAM role \(execution role\)\. AWS Lambda assumes this role when executing your Lambda function on your behalf\. In this section, you create an IAM role using the following predefined role type and access policy:

+ AWS service role of the "AWS Lambda" type\. This role grants AWS Lambda permission to assume the role\. 

+ "AWSLambdaBasicExecutionRole" access policy that you attach to the role\. This existing policy grants permissions that include permissions for Amazon CloudWatch actions that your Lambda function needs\.

For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. 

In this section, you create an IAM role using the following predefined role type and access permissions policy:

+ AWS service role of the type **AWS Lambda** – This role grants AWS Lambda permissions to assume the role\. 

+ **AWSLambdaBasicExecutionRole** access permissions policy that you attach to the role\. 

For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. Use the following procedure to create the IAM role\.

**To create an IAM role \(execution role\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following:

   + In **Role Name**, use a name that is unique within your AWS account \(for example, **lambda\-custom\-app\-execution\-role**\)\. 

   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\. This grants the AWS Lambda service permissions to assume the role\.

   + In **Attach Policy**, choose **AWSLambdaBasicExecutionRole**\.

1. Write down the role ARN\. You will need it in the next step when you create your Lambda function\.