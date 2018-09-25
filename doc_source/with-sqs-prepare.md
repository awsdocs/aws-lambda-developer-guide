# Step 1: Prepare<a name="with-sqs-prepare"></a>

Make sure you have completed the following steps:
+ **Signed up for an AWS account** and created an administrator user in the account \(called **adminuser**\), as explained in the following steps:

**To create an IAM user for yourself and add the user to an Administrators group**

  1. Use your AWS account email address and password to sign in as the *[AWS account root user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_root-user.html)* to the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.
**Note**  
We strongly recommend that you adhere to the best practice of using the **Administrator** IAM user below and securely lock away the root user credentials\. Sign in as the root user only to perform a few [account and service management tasks](https://docs.aws.amazon.com/general/latest/gr/aws_tasks-that-require-root.html)\.

  1. In the navigation pane of the console, choose **Users**, and then choose **Add user**\.

  1. For **User name**, type **Administrator**\.

  1. Select the check box next to **AWS Management Console access**, select **Custom password**, and then type the new user's password in the text box\. You can optionally select **Require password reset** to force the user to create a new password the next time the user signs in\.

  1. Choose **Next: Permissions**\.

  1. On the **Set permissions** page, choose **Add user to group**\.

  1. Choose **Create group**\.

  1. In the **Create group** dialog box, for **Group name** type **Administrators**\.

  1. For **Filter policies**, select the check box for **AWS managed \- job function**\.

  1. In the policy list, select the check box for **AdministratorAccess**\. Then choose **Create group**\.

  1. Back in the list of groups, select the check box for your new group\. Choose **Refresh** if necessary to see the group in the list\.

  1. Choose **Next: Review** to see the list of group memberships to be added to the new user\. When you are ready to proceed, choose **Create user**\.

  You can use this same process to create more groups and users, and to give your users access to your AWS account resources\. To learn about using policies to restrict users' permissions to specific AWS resources, go to [Access Management](https://docs.aws.amazon.com/IAM/latest/UserGuide/access.html) and [Example Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_examples.html)\.
+ **Installed and set up the AWS CLI**\. For instructions, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)\.

## Next Step<a name="with-sqs-next-step-2"></a>

[Step 2: Create a Lambda Function and Invoke It Manually \(Using Sample Event Data\)](with-sqs-create-test-function.md)