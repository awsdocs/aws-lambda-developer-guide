# Set Up the AWS Command Line Interface \(AWS CLI\)<a name="setup-awscli"></a>

All the exercises in this guide assume that you are using administrator user credentials \(`adminuser`\) in your account to perform the operations\. For instructions on creating an administrator user in your AWS account, see [Set Up an AWS Account and Create an Administrator User](setup.md#setting-up), and then follow the steps to download and configure the AWS Command Line Interface \(AWS CLI\)\.

**To set up the AWS CLI**

1. Download and configure the AWS CLI\. For instructions, see the following topics in the *AWS Command Line Interface User Guide*\. 
   + [Getting Set Up with the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-set-up.html)
   + [Configuring the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)

1. Add a named profile for the administrator user in the [AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cli-config-files.html)\. You use this profile when executing the AWS CLI commands\. For more information on creating this profile, see [Named Profiles\.](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html)

   ```
   [profile adminuser]
   aws_access_key_id = adminuser access key ID
   aws_secret_access_key = adminuser secret access key
   region = aws-region
   ```

   For a list of available AWS regions, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) in the *Amazon Web Services General Reference*\.

1. Verify the setup by entering the following commands at the command prompt\. 
   + Try the help command to verify that the AWS CLI is installed on your computer:

     ```
     aws help
     ```
   + Try a Lambda command to verify the user can reach AWS Lambda\. This command lists Lambda functions in the account, if any\. The AWS CLI uses the `adminuser` credentials to authenticate the request\.

     ```
     aws lambda list-functions --profile adminuser
     ```

## Next Step<a name="setting-up-next-step-create-function"></a>

 [Install SAM CLI](sam-cli-requirements.md) 