# Step 1: Prepare<a name="with-cloudtrail-example-prepare"></a>

In this section you do the following:

+ Sign up for an AWS account and set up the AWS CLI\. 

+ Turn on CloudTrail in your account\. 

+ Create an SNS topic and subscribe to it\.

Follow the steps in the following sections to walk through the setup process\.

**Note**  
In this tutorial, we assume that you are setting the resources in the `us-west-2` region\.

## Step 1\.1: Sign Up for AWS and Set Up the AWS CLI<a name="with-cloudtrail-example-prepare-setup-cli"></a>

Make sure you have completed the following steps:

+ Signed up for an AWS account and created an administrator user in the account \(called **adminuser**\)\. For instructions, see [Set Up an AWS Account](setup.md)\. 

+ Installed and set up the AWS CLI\. For instructions, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)\.

## Step 1\.2: Turn on CloudTrail<a name="with-cloudtrail-example-prepare-create-buckets"></a>

In the AWS CloudTrail console, turn on the trail in your account by specifying *examplebucket* in the `us-west-2` region for CloudTrail to save logs\. When configuring the trail, do not enable SNS notification\. 

For instructions, see [Creating and Updating Your Trail](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-and-update-a-trail.html) in the *AWS CloudTrail User Guide*\.

**Note**  
Although you turn CloudTrail on now, you do not perform any additional configuration for your Lambda function to process the real CloudTrail logs in the first half of this exercise\. Instead, you will use sample CloudTrail log objects \(that you will upload\) and sample S3 events to manually invoke and test your Lambda function\. In the second half of this tutorial, you perform additional configuration steps that enable your Lambda function to process the CloudTrail logs\. 

## Step 1\.3: Create an SNS Topic and Subscribe to the Topic<a name="with-cloudtrail-example-prepare-create-sns-toppic"></a>

Follow the procedure to create an SNS topic in the `us-west-2` region and subscribe to it by providing an email address as the endpoint\.

**To create and subscribe to a topic**

1. Create an SNS topic\. 

   For instructions, see [Create a Topic](http://docs.aws.amazon.com/sns/latest/dg/CreateTopic.html) in the *Amazon Simple Notification Service Developer Guide*\.

1. Subscribe to the topic by providing an email address as the endpoint\. 

   For instructions, see [Subscribe to a Topic](http://docs.aws.amazon.com/sns/latest/dg/SubscribeTopic.html) in the *Amazon Simple Notification Service Developer Guide*\.

1. Note down the topic ARN\. You will need the value in the following sections\.

## Next Step<a name="with-cloudtrail-example-prepare-next-step"></a>

[Step 2: Create a Lambda Function and Invoke It Manually \(Using Sample Event Data\)](with-cloudtrail-example-create-test-manually.md)