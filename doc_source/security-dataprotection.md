# Data protection in AWS Lambda<a name="security-dataprotection"></a>

AWS Lambda conforms to the AWS [shared responsibility model](http://aws.amazon.com/compliance/shared-responsibility-model/), which includes regulations and guidelines for data protection\. AWS is responsible for protecting the global infrastructure that runs all the AWS services\. AWS maintains control over data hosted on this infrastructure, including the security configuration controls for handling customer content and personal data\. AWS customers and APN partners, acting either as data controllers or data processors, are responsible for any personal data that they put in the AWS Cloud\. 

For data protection purposes, we recommend that you protect AWS account credentials and set up individual user accounts with AWS Identity and Access Management \(IAM\), so that each user is given only the permissions necessary to fulfill their job duties\. We also recommend that you secure your data in the following ways:
+ Use multi\-factor authentication \(MFA\) with each account\.
+ Use SSL/TLS to communicate with AWS resources\.
+ Set up API and user activity logging with AWS CloudTrail\.
+ Use AWS encryption solutions, along with all default security controls within AWS services\.
+ Use advanced managed security services such as Amazon Macie, which assists in discovering and securing personal data that is stored in Amazon S3\.

We strongly recommend that you never put sensitive identifying information, such as your customers' account numbers, into free\-form fields or metadata such as function names and tags\. This includes when you work with Lambda or other AWS services using the console, API, AWS CLI, or AWS SDKs\. Any data that you enter into metadata might get picked up for inclusion in diagnostic logs\. When you provide a URL to an external server, don't include credentials information in the URL to validate your request to that server\.

For more information about data protection, see the [AWS shared responsibility model and GDPR](http://aws.amazon.com/blogs/security/the-aws-shared-responsibility-model-and-gdpr/) blog post on the *AWS Security Blog*\.

**Topics**
+ [Encryption in transit](#security-privacy-intransit)
+ [Encryption at rest](#security-privacy-atrest)

## Encryption in transit<a name="security-privacy-intransit"></a>

Lambda API endpoints only support secure connections over HTTPS\. When you manage Lambda resources with the AWS Management Console, AWS SDK, or the Lambda API, all communication is encrypted with Transport Layer Security \(TLS\)\.

When you [connect your function to a file system](configuration-filesystem.md), Lambda uses [Encryption in transit](https://docs.aws.amazon.com/efs/latest/ug/encryption.html) for all connections\.

For a full list of API endpoints, see [AWS Regions and endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) in the AWS General Reference\.

## Encryption at rest<a name="security-privacy-atrest"></a>

You can use environment variables to store secrets securely for use with Lambda functions\. Lambda always encrypts environment variables at rest\.

Additionally, you can use the following features to customize how environment variables are encrypted\.
+ **Key configuration** – On a per\-function basis, you can configure Lambda to use an encryption key that you create and manage in AWS Key Management Service\. These are referred to as *customer managed* customer master keys \(CMKs\) or customer managed keys\. If you don't configure a customer managed key, Lambda uses an AWS managed CMK named `aws/lambda`, which Lambda creates in your account\.
+ **Encryption helpers** – The Lambda console lets you encrypt environment variable values client side, before sending them to Lambda\. This enhances security further by preventing secrets from being displayed unencrypted in the Lambda console, or in function configuration that's returned by the Lambda API\. The console also provides sample code that you can adapt to decrypt the values in your function handler\.

For more information, see [Using AWS Lambda environment variables](configuration-envvars.md)\.

Lambda always encrypts files that you upload to Lambda, including [deployment packages](gettingstarted-features.md#gettingstarted-features-package) and [layer archives](configuration-layers.md)\.

Amazon CloudWatch Logs and AWS X\-Ray also encrypt data by default, and can be configured to use a customer managed key\. For details, see [Encrypt log data in CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/encrypt-log-data-kms.html) and [Data protection in AWS X\-Ray](https://docs.aws.amazon.com/xray/latest/devguide/xray-console-encryption.html)\.