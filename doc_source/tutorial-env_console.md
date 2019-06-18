# Create a Lambda Function Using Environment Variables To Store Sensitive Information<a name="tutorial-env_console"></a>

Along with specifying configuration settings for your Lambda function, you can also use environment variables to store sensitive information, such as a database password, using [AWS Key Management Service](https://docs.aws.amazon.com/kms/latest/developerguide/) and the Lambda console's encryption helpers\. For more information, see [Environment Variable Encryption](env_variables.md#env_encrypt)\. The following example shows you how to do this and also how to use KMS to decrypt that information\.

This tutorial will demonstrate how you can use the Lambda console to encrypt an environment variable containing sensitive information\. Note that if you are updating an existing function, you can skip ahead to the instruction step outlining how to Expand the ** Environment Variables ** section of [Step 2: Configure the Lambda Function](#tutorial-env-configure-function)\.

## Step 1: Create the Lambda Function<a name="tutorial-env-create-function"></a>

1. Sign in to the AWS Management Console and open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. Choose **Create a Lambda function**\.

1. In **Select blueprint**, choose the **Author from scratch** button\.

1. In **Basic information**, do the following:
   + In **Name**, specify your Lambda function name\.
   + In **Role**, choose **Choose an existing role**\.
   + In **Existing role**, choose **lambda\_basic\_execution**\.
**Note**  
If the policy of the execution role does not have the `decrypt` permission, you will need add it\.
   + Choose **Create function**\.

## Step 2: Configure the Lambda Function<a name="tutorial-env-configure-function"></a>

1. Under **Configuration**, specify the **Runtime** of your choice\.

1. Under the **Lambda function code** section you can take advantage of the **Edit code inline** option to replace the Lambda function handler code with your custom code\. 

1. Note the **Triggers** tab\. Under the **Triggers** page, you can optionally choose a service that automatically triggers your Lambda function by choosing the **Add trigger** button and then choosing the gray box with ellipses \(\.\.\.\) to display a list of available services\. For this example, do not configure a trigger and choose **Configuration**\.

1. Note the **Monitoring** tab\. This page will provide immediate CloudWatch metrics for your Lambda function invocations, as well as links to other helpful guides, including [Using AWS X\-Ray](lambda-x-ray.md)\. 

1. Expand the **Environment variables** section\.

1. Enter your key\-value pair\. Expand the **Encryption configuration** section\. Note that Lambda provides a default service key under **KMS key to encrypt at rest** which encrypts your information after it has been uploaded\. If the information you provided is sensitive, you can additionally check the **Enable helpers for encryption in transit** checkbox and supply a custom key\. This masks the value you entered and results in a call to AWS KMS to encrypt the value and return it as `Ciphertext`\. If you haven't created a KMS key for your account, you will be provided a link to the AWS IAM console to create one\. The account must have `encrypt` and `decrypt` permissions for that key\. Note that the **Encrypt** button toggles to **Decrypt** after you choose it\. This affords you the option to update the information\. Once you have done that, choose the **Encrypt** button\.

   The **Code** button provides sample decrypt code specific to the runtime of your Lambda function that you can use with your application\.
**Note**  
You cannot use the default Lambda service key for encrypting sensitive information on the client side\. For more information, see [Environment Variable Encryption](env_variables.md#env_encrypt)\. 