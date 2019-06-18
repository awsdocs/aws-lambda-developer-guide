# Accessing AWS Resources from a Lambda Function<a name="accessing-resources"></a>

Lambda does not enforce any restrictions on your function logic – if you can code for it, you can run it within a Lambda function\. As part of your function, you may need to call other APIs, or access other AWS services like databases\. 

## Accessing AWS Services<a name="accessing-aws-services"></a>

To access other AWS services, you can use the AWS SDK\. AWS Lambda automatically sets the credentials required by the SDK to those of the IAM role associated with your function – you do not need to take any additional steps\. For example, here’s sample code using the Python SDK for accessing an S3 object\.

```
import boto3
import botocore

BUCKET_NAME = 'my-bucket' # replace with your bucket name
KEY = 'my_image_in_s3.jpg' # replace with your object key

s3 = boto3.resource('s3')

try:
    s3.Bucket(BUCKET_NAME).download_file(KEY, 'my_local_image.jpg')
except botocore.exceptions.ClientError as e:
    if e.response['Error']['Code'] == "404":
        print("The object does not exist.")
    else:
        raise
```

For convenience, AWS Lambda includes versions of the AWS SDK as part of the execution environment so you don’t have to include it\. See [AWS Lambda Runtimes](lambda-runtimes.md) for the version of the included SDK\. We recommend including your own copy of the AWS SDK for production applications so you can control your dependencies\.

## Accessing non AWS Services<a name="accessing-non-aws-services"></a>

You can include any SDK to access any service as part of your Lambda function\. For example, you can include the [SDK for Twilio](https://www.twilio.com/docs/libraries) to access information from your Twilio account\. You can use [AWS Lambda Environment Variables](env_variables.md) for storing the credential information for the SDKs after encrypting the credentials\. 

## Accessing Private Services or Resources<a name="accessing-private-resources"></a>

By default, your service or API must be accessible over the public internet for AWS Lambda to access it\. However, you may have APIs or services that are not exposed this way\. Typically, you create these resources inside Amazon Virtual Private Cloud \(Amazon VPC\) so that they cannot be accessed over the public Internet\. These resources could be AWS service resources, such as Amazon Redshift data warehouses, Amazon ElastiCache clusters, or Amazon RDS instances\. They could also be your own services running on your own EC2 instances\. By default, resources within a VPC are not accessible from within a Lambda function\.

AWS Lambda runs your function code securely within a VPC by default\. However, to enable your Lambda function to access resources inside your private VPC, you must provide additional VPC\-specific configuration information that includes VPC subnet IDs and security group IDs\. AWS Lambda uses this information to set up elastic network interfaces \(ENIs\) that enable your function to connect securely to other resources within your private VPC\. 

**Important**  
AWS Lambda does not support connecting to resources within Dedicated Tenancy VPCs\. For more information, see [Dedicated VPCs](https://docs.aws.amazon.com/vpc/latest/userguide/dedicated-instance.html)\. 

To learn how to configure a Lambda function to access resources within a VPC, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)