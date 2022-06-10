# Data protection in AWS Lambda<a name="security-dataprotection"></a>

The AWS [shared responsibility model](http://aws.amazon.com/compliance/shared-responsibility-model/) applies to data protection in AWS Lambda\. As described in this model, AWS is responsible for protecting the global infrastructure that runs all of the AWS Cloud\. You are responsible for maintaining control over your content that is hosted on this infrastructure\. This content includes the security configuration and management tasks for the AWS services that you use\. For more information about data privacy, see the [Data Privacy FAQ](http://aws.amazon.com/compliance/data-privacy-faq)\. For information about data protection in Europe, see the [AWS Shared Responsibility Model and GDPR](http://aws.amazon.com/blogs/security/the-aws-shared-responsibility-model-and-gdpr/) blog post on the *AWS Security Blog*\.

For data protection purposes, we recommend that you protect AWS account credentials and set up individual user accounts with AWS Identity and Access Management \(IAM\)\. That way each user is given only the permissions necessary to fulfill their job duties\. We also recommend that you secure your data in the following ways:
+ Use multi\-factor authentication \(MFA\) with each account\.
+ Use SSL/TLS to communicate with AWS resources\. We recommend TLS 1\.2 or later\.
+ Set up API and user activity logging with AWS CloudTrail\.
+ Use AWS encryption solutions, along with all default security controls within AWS services\.
+ Use advanced managed security services such as Amazon Macie, which assists in discovering and securing personal data that is stored in Amazon S3\.
+ If you require FIPS 140\-2 validated cryptographic modules when accessing AWS through a command line interface or an API, use a FIPS endpoint\. For more information about the available FIPS endpoints, see [Federal Information Processing Standard \(FIPS\) 140\-2](http://aws.amazon.com/compliance/fips/)\.

We strongly recommend that you never put confidential or sensitive information, such as your customers' email addresses, into tags or free\-form fields such as a **Name** field\. This includes when you work with Lambda or other AWS services using the console, API, AWS CLI, or AWS SDKs\. Any data that you enter into tags or free\-form fields used for names may be used for billing or diagnostic logs\. If you provide a URL to an external server, we strongly recommend that you do not include credentials information in the URL to validate your request to that server\.

**Topics**
+ [Encryption in transit](#security-privacy-intransit)
+ [Encryption at rest](#security-privacy-atrest)

## Encryption in transit<a name="security-privacy-intransit"></a>

Lambda API endpoints only support secure connections over HTTPS\. When you manage Lambda resources with the AWS Management Console,AWS SDK, or the Lambda API, all communication is encrypted with Transport Layer Security \(TLS\)\. For a full list of API endpoints, see [AWS Regions and endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) in the AWS General Reference\.

When you [connect your function to a file system](configuration-filesystem.md), Lambda uses encryption in transit for all connections\. For more information, see [Data encryption in Amazon EFS](https://docs.aws.amazon.com/efs/latest/ug/encryption.html) in the *Amazon Elastic File System User Guide*\.

When you use [environment variables](configuration-envvars.md), you can enable console encryption helpers to use client\-side encryption to protect the environment variables in transit\. For more information, see [Securing environment variables](configuration-envvars.md#configuration-envvars-encryption)\.

## Encryption at rest<a name="security-privacy-atrest"></a>

You can use [environment variables](configuration-envvars.md) to store secrets securely for use with Lambda functions\. Lambda always encrypts environment variables at rest\. By default, Lambda uses an AWS KMS key that Lambda creates in your account to encrypt your environment variables\. This AWS managed key is named `aws/lambda`\.

On a per\-function basis, you can optionally configure Lambda to use a customer managed key instead of the default AWS managed key to encrypt your environment variables\. For more information, see [Securing environment variables](configuration-envvars.md#configuration-envvars-encryption)\.

Lambda always encrypts files that you upload to Lambda, including [deployment packages](images-create.md) and [layer archives](configuration-layers.md)\.

Amazon CloudWatch Logs and AWS X\-Ray also encrypt data by default, and can be configured to use a customer managed key\. For details, see [Encrypt log data in CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/encrypt-log-data-kms.html) and [Data protection in AWS X\-Ray](https://docs.aws.amazon.com/xray/latest/devguide/xray-console-encryption.html)\.