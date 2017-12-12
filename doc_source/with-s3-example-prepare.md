# Step 1: Prepare<a name="with-s3-example-prepare"></a>

In this section, you do the following:

+ Sign up for an AWS account and set up the AWS CLI\. 

+ Create two buckets \(*source* and `sourceresized` bucket\) with a sample \.jpg object \(`HappyFace.jpg`\) in the source bucket\. For instructions, see the following procedure\. 

## Step 1\.1: Sign Up for AWS and Set Up the AWS CLI<a name="with-s3-example-prepare-setup-cli"></a>

Make sure you have completed the following steps:

+ Signed up for an AWS account and created an administrator user in the account \(called **adminuser**\)\. 

+ Installed and set up the AWS CLI\. 

For instructions, see [Set Up an AWS Account](setup.md)\. 

## Step 1\.2: Create Buckets and Upload a Sample Object<a name="with-s3-example-prepare-create-buckets"></a>

Follow the steps to create buckets and upload an object\.

**Important**  
Both the source bucket and your Lambda function must be in the same AWS region\. In addition, the example code used for the Lambda function also assumes that both of the buckets are in the same region\. In this tutorial, we use the `us-west-2` region\.

1. Using the IAM User Sign\-In URL, sign in to the Amazon S3 console as **adminuser**\. 

1. Create two buckets\. The target bucket name must be *source* followed by **resized**, where *source* is the name of the bucket you want to use for the source\. For example, `mybucket` and `mybucketresized`\.

   For instructions, see [Create a Bucket](http://docs.aws.amazon.com/AmazonS3/latest/gsg/CreatingABucket.html) in the *Amazon Simple Storage Service Getting Started Guide*\.

1. In the source bucket, upload a \.jpg object, `HappyFace.jpg`\. 

   When you invoke the Lambda function manually before you connect to Amazon S3, you pass sample event data to the function that specifies the source bucket and `HappyFace.jpg` as the newly created object so you need to create this sample object first\.

## Next Step<a name="with-s3-example-prepare-next-step"></a>

[Step 2: Create a Lambda Function and Invoke It Manually \(Using Sample Event Data\)](with-s3-example-create-test-manually.md)