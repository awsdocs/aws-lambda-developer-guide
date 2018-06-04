# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-s3-example-create-iam-role"></a>

In this section, you create an IAM role using the following predefined role type and access permissions policy:
+ AWS service role of the type **AWS Lambda** – This role grants AWS Lambda permissions to assume the role\. 
+ **AWSLambdaExecute** access permissions policy that you attach to the role\. 
+ Add a custom policy which allocates permissions for you to add objects to your Amazon S3 bucket\. For more information, see [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. 

 For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. Use the following procedure to create the IAM role\.

**To create an IAM role \(execution role\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Choose **Create role**

1. In **Select type of trusted entity**, choose **AWS service**, and then choose **Lambda**\. This will allow Lambda functions to call AWS services under your account\.

1. Choose **Next: Permissions**

1. In **Filter: Policy type** enter **AWSLambdaExecute** and choose **Next: Review**\. 

1. In **Role name\***, enter a role name that is unique within your AWS account \(for example, **lambda\-s3\-execution\-role**\) and then choose **Create role**\. 

1. Open the service role that you just created\.

1. Under the **Permissions** tab, choose **Add inline policy**\.

1. In **service**, choose **Choose a service**\.

1. In **Select a service below**, choose **S3**\.

1. In **Actions**, choose **Select actions**\.

1. Expand **Write** under **Access level groups** and then choose **PutObject**\.

1. Choose **Resources** and then choose the **Any** checkbox\.

1. Choose **Review policy**\.

1. Enter a **Name\*** and then choose **Create policy**\. Note the policy specifications:

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "VisualEditor0",
               "Effect": "Allow",
               "Action": "s3:PutObject",
               "Resource": "arn:aws:s3:::*/*"
           }
       ]
   }
   ```

1. Under the **Summary** of your role, record the **Role ARN**\. You will need it in the next step when you create your Lambda function\.

## Next Step<a name="with-s3-next-step-4"></a>

[Step 2\.3: Create the Lambda Function and Test It Manually](with-s3-example-upload-deployment-pkg.md)