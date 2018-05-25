# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-on-demand-https-example-create-iam-role"></a>

In this section, you create an IAM role using the following predefined role type:
+ AWS service role of the type **AWS Lambda** – This role grants AWS Lambda permissions to assume the role\. 

 For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. Use the following procedure to create the IAM role\.

**To create an IAM role \(execution role\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following:
   + In **Role Name**, use a name that is unique within your AWS account \(for example, **lambda\-gateway\-execution\-role**\)\. 
   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\. This grants the AWS Lambda service permissions to assume the role\.
   + You create an IAM role without attaching a permissions policy in the console\. After you create the role, you update the role, and then attach the following permissions policy to the role\.

   ```
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "Stmt1428341300017",
         "Action": [
           "dynamodb:DeleteItem",
           "dynamodb:GetItem",
           "dynamodb:PutItem",
           "dynamodb:Query",
           "dynamodb:Scan",
           "dynamodb:UpdateItem"
         ],
         "Effect": "Allow",
         "Resource": "*"
       },
       {
         "Sid": "",
         "Resource": "*",
         "Action": [
           "logs:CreateLogGroup",
           "logs:CreateLogStream",
           "logs:PutLogEvents"
         ],
         "Effect": "Allow"
       }
     ]
   }
   ```

1. Write down the role ARN \(Amazon Resource Name\)\. You need it in the next step when you create your Lambda function\.

## Next Step<a name="with-on-demand-https-example-exe-role-next-step_1"></a>

 [Step 2\.3: Create the Lambda Function and Test It Manually](with-on-demand-https-example-upload-deployment-pkg_1.md) 