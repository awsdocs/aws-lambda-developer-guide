# Command Line Tools<a name="gettingstarted-tools"></a>

Install the AWS Command Line Interface to manage and use Lambda functions from the command line\. Tutorials in this guide use the AWS CLI, which has commands for all Lambda API actions\. Some functionality is not available in the Lambda console and can only be accessed with the AWS CLI or the AWS SDK\.

The AWS SAM CLI is a separate command line tool that you can use to manage and test AWS SAM applications\. In addition to commands for uploading artifacts and launching AWS CloudFormation stacks that are also available in the AWS CLI, the SAM CLI provides additional commands for validating templates and running applications locally in a Docker container\.

## Set Up the AWS CLI<a name="setup-awscli"></a>

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
     $ aws help
     ```
   + Try a Lambda command to verify the user can reach AWS Lambda\. This command lists Lambda functions in the account, if any\. The AWS CLI uses the `adminuser` credentials to authenticate the request\.

     ```
     $ aws lambda list-functions
     ```

## AWS Serverless Application Model CLI<a name="sam-cli-requirements"></a>

The AWS SAM CLI is a command line tool that operates on an AWS SAM template and application code\. With the AWS SAM CLI, you can invoke Lambda functions locally, create a deployment package for your serverless application, deploy your serverless application to the AWS Cloud, and so on\. 

For more details about installing the AWS SAM CLI, see [ Installing the AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) in the *AWS Serverless Application Model Developer Guide*\.