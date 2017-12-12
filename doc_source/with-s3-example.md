# Tutorial: Using AWS Lambda with Amazon S3<a name="with-s3-example"></a>

Suppose you want to create a thumbnail for each image \(\.jpg and \.png objects\) that is uploaded to a bucket\. You can create a Lambda function \(`CreateThumbnail`\) that Amazon S3 can invoke when objects are created\. Then, the Lambda function can read the image object from the *source* bucket and create a thumbnail image target bucket \(in this tutorial, it's called the `sourceresized` bucket\)\.

**Important**  
You must use two buckets\. If you use the same bucket as the source and the target, each thumbnail uploaded to the source bucket triggers another object\-created event, which then invokes the Lambda function again, creating an unwanted recursion\. 

## Implementation Summary<a name="with-s3-example-impl-summary"></a>

The following diagram illustrates the application flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/s3-admin-iser-walkthrough-20.png)

1. A user uploads an object to the source bucket in Amazon S3 \(object\-created event\)\.

1. Amazon S3 detects the object\-created event\.

1. Amazon S3 publishes the `s3:ObjectCreated:*` event to AWS Lambda by invoking the Lambda function and passing event data as a function parameter\. 

1. AWS Lambda executes the Lambda function by assuming the execution role that you specified at the time you created the Lambda function\.

1. From the event data it receives, the Lambda function knows the source bucket name and object key name\. The Lambda function reads the object and creates a thumbnail using graphics libraries, and saves it to the target bucket\.

Note that upon completing this tutorial, you will have the following Amazon S3, Lambda, and IAM resources in your account: 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/s3-admin-iser-walkthrough-10.png)

In Lambda:

+ A Lambda function\.

+ An access permissions policy associated with your Lambda function – You grant Amazon S3 permissions to invoke the Lambda function using this permissions policy\. You will also restrict the permissions so that Amazon S3 can invoke the Lambda function only for object\-created events from a specific bucket that is owned by a specific AWS account\. 
**Note**  
It is possible for an AWS account to delete a bucket and some other AWS account to later create a bucket with the same name\. The additional conditions ensure that Amazon S3 can invoke the Lambda function only if Amazon S3 detects object\-created events from a specific bucket owned by a specific AWS account\. 

In IAM:

+ Administrator user – Called **adminuser**\. Using root credentials of an AWS account is not recommended\. Instead, use the **adminuser** credentials to perform the steps in this tutorial\. 

+ An IAM role \(execution role\) – You grant permissions that your Lambda function needs through the permissions policy associated with this role\. 

In Amazon S3:

+ Two buckets named *source* and `sourceresized`\. Note that *source* is a placeholder name and you need to replace it with your actual bucket name\. For example, if you have a bucket named `example` as your source, you will create `exampleresized` as the target bucket\.

+ Notification configuration on the source bucket – You add notification configuration on your source bucket identifying the type of events \(object\-created events\) you want Amazon S3 to publish to AWS Lambda and the Lambda function to invoke\. For more information about the Amazon S3 notification feature, see [Setting Up Notification of Bucket Events](http://docs.aws.amazon.com/AmazonS3/latest/dev/NotificationHowTo.html) in *Amazon Simple Storage Service Developer Guide*\.\.

Now you are ready to start the tutorial\. Note that after the initial preparation, the tutorial is divided into two main sections:

+ First, you complete the necessary setup steps to create a Lambda function and invoke it manually using Amazon S3 sample event data\. This intermediate testing verifies that the function works\.

+ Second, you add notification configuration to your source bucket so that Amazon S3 can invoke your Lambda function when it detects object\-created events\. 

## Next Step<a name="with-s3-example-impl-summary-next-step"></a>

[Step 1: Prepare](with-s3-example-prepare.md)