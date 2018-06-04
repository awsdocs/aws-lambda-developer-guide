# Step 2\.2: Create the Execution Role \(IAM Role\)<a name="with-cloudtrail-example-create-iam-role"></a>

Now you create an IAM role \(execution role\) that you specify when creating your Lambda function\. This role has a permissions policy that grant the necessary permissions that your Lambda function needs, such as permissions to write CloudWatch logs, permissions to read CloudTrail log objects from an S3 bucket, and permissions to publish events to your SNS topic when your Lambda function finds specific API calls in the CloudTrail records\.

For more information about the execution role, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.

**To create an IAM role \(execution role\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Create a managed policy and attach it to the IAM role\. In this step, you modify an existing AWS Managed Policy, save it using a different name, and then attach the permissions policy to an IAM role that you create\.

   1. In the navigation pane of the IAM console, choose **Policies**, and then choose **Create Policy**\.

   1. Next to **Copy an AWS Managed Policy**, choose **Select**\.

   1. Next to **AWSLambdaExecute**, choose **Select**\.

   1. Copy the following policy into the **Policy Document**, replacing the existing policy, and then update the policy with the ARN of the Amazon SNS topic that you created\.

      ```
      {
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Action": [
              "logs:*"
            ],
            "Resource": "arn:aws:logs:*:*:*"
          },
          {
            "Effect": "Allow",
            "Action": [
              "s3:GetObject"
            ],
            "Resource": "arn:aws:s3:::examplebucket/*"
          },
          {
            "Effect": "Allow",
            "Action": [
              "sns:Publish"
            ],
            "Resource": "your sns topic ARN"
          }
        ]
      }
      ```

1. Note the permissions policy name because you will use it in the next step\. 

1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role and then attach the permissions policy you just created to the role\. As you follow the steps to create a role, note the following:
   + In **Role Name**, use a name that is unique within your AWS account \(for example, **lambda\-cloudtrail\-execution\-role**\)\. 
   + In **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\.
   + In **Attach Policy**, choose the policy you created in the previous step\.

## Next Step<a name="with-cloudtrail-example-create-iam-role-next-step"></a>

 [Step 2\.3: Create the Lambda Function and Test It Manually](with-cloudtrail-example-upload-deployment-pkg.md) 