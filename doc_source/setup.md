# Set Up an AWS Account<a name="setup"></a>

If you have not already done so, you need to sign up for an AWS account and create an administrator user in the account\. You also need to set up the AWS Command Line Interface \(AWS CLI\)\. Many of the tutorials use the AWS CLI\.

To complete the setup, follow the instructions in the following topics:

## Set Up an AWS Account and Create an Administrator User<a name="setting-up"></a>

### Sign up for AWS<a name="setting-up-signup"></a>

When you sign up for Amazon Web Services \(AWS\), your AWS account is automatically signed up for all services in AWS, including AWS Lambda\. You are charged only for the services that you use\.

With AWS Lambda, you pay only for the resources you use\. For more information about AWS Lambda usage rates, see the [AWS Lambda product page](https://aws.amazon.com/lambda/)\. If you are a new AWS customer, you can get started with AWS Lambda for free\. For more information, see [AWS Free Usage Tier](https://aws.amazon.com/free/)\.

If you already have an AWS account, skip to the next task\. If you don't have an AWS account, use the following procedure to create one\.

**To create an AWS account**

1. Open [https://aws\.amazon\.com/](https://aws.amazon.com/), and then choose **Create an AWS Account**\.
**Note**  
This might be unavailable in your browser if you previously signed into the AWS Management Console\. In that case, choose **Sign in to a different account**, and then choose **Create a new AWS account**\.

1. Follow the online instructions\.

   Part of the sign\-up procedure involves receiving a phone call and entering a PIN using the phone keypad\.

Note your AWS account ID, because you'll need it for the next task\.

### Create an IAM User<a name="setting-up-iam"></a>

Services in AWS, such as AWS Lambda, require that you provide credentials when you access them, so that the service can determine whether you have permissions to access the resources owned by that service\. The console requires your password\. You can create access keys for your AWS account to access the AWS CLI or API\. However, we don't recommend that you access AWS using the credentials for your AWS account\. Instead, we recommend that you use AWS Identity and Access Management \(IAM\)\. Create an IAM user, add the user to an IAM group with administrative permissions, and then grant administrative permissions to the IAM user that you created\. You can then access AWS using a special URL and that IAM user's credentials\.

If you signed up for AWS, but you haven't created an IAM user for yourself, you can create one using the IAM console\.

The Getting Started exercises and tutorials in this guide assume you have a user \(`adminuser`\) with administrator privileges\. When you follow the procedure, create a user with name `adminuser`\.

**To create an IAM user for yourself and add the user to an Administrators group**

1. Use your AWS account email address and password to sign in as the *[AWS account root user](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_root-user.html)* to the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.
**Note**  
We strongly recommend that you adhere to the best practice of using the **Administrator** IAM user below and securely lock away the root user credentials\. Sign in as the root user only to perform a few [account and service management tasks](http://docs.aws.amazon.com/general/latest/gr/aws_tasks-that-require-root.html)\.

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

You can use this same process to create more groups and users, and to give your users access to your AWS account resources\. To learn about using policies to restrict users' permissions to specific AWS resources, go to [Access Management](http://docs.aws.amazon.com/IAM/latest/UserGuide/access.html) and [Example Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_examples.html)\.

**To sign in as the new IAM user**

1. Sign out of the AWS Management Console\.

1. Use the following URL format to log in to the console:

   ```
   https://aws_account_number.signin.aws.amazon.com/console/
   ```

   The *aws\_account\_number* is your AWS account ID without hyphen\. For example, if your AWS account ID is `1234-5678-9012`, your AWS account number is `123456789012`\. For information about how to find your account number, see [Your AWS Account ID and Its Alias](http://docs.aws.amazon.com/IAM/latest/UserGuide/console_account-alias.html) in the *IAM User Guide*\.

1. Enter the IAM user name and password that you just created\. When you're signed in, the navigation bar displays *your\_user\_name* @ *your\_aws\_account\_id*\.

If you don't want the URL for your sign\-in page to contain your AWS account ID, you can create an account alias\. 

**To create or remove an account alias**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. On the navigation pane, choose **Dashboard**\.

1. Find the IAM users sign\-in link\.

1. To create the alias, click **Customize**, enter the name you want to use for your alias, and then choose **Yes, Create**\.

1. To remove the alias, choose **Customize**, and then choose **Yes, Delete**\. The sign\-in URL reverts to using your AWS account ID\.

To sign in after you create an account alias, use the following URL:

```
https://your_account_alias.signin.aws.amazon.com/console/
```

To verify the sign\-in link for IAM users for your account, open the IAM console and check under **IAM users sign\-in link:** on the dashboard\.

For more information about IAM, see the following:
+ [AWS Identity and Access Management \(IAM\)](https://aws.amazon.com/iam/)
+ [Getting Started](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started.html)
+ [IAM User Guide](http://docs.aws.amazon.com/IAM/latest/UserGuide/)

### Next Step<a name="setting-up-next-step"></a>
[Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)