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

Lambda API endpoints only support secure connections over HTTPS\. When you manage Lambda resources with the AWS Management Console,AWS SDK, or the Lambda API, all communication is encrypted with Transport Layer Security \(TLS\)\.

When you [connect your function to a file system](configuration-filesystem.md), Lambda uses [Encryption in transit](https://docs.aws.amazon.com/efs/latest/ug/encryption.html) for all connections\.

For a full list of API endpoints, see [AWS Regions and endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) in the AWS General Reference\.

## Encryption at rest<a name="security-privacy-atrest"></a>

You can use environment variables to store secrets securely for use with Lambda functions\. Lambda always encrypts environment variables at rest\.

Additionally, you can use the following features to customize how environment variables are encrypted\.
+ **Key configuration** – On a per\-function basis, you can configure Lambda to use an encryption key that you create and manage in AWS Key Management Service\. These are referred to as *customer managed* CMKs\. For functions that do not have a configured customer managed CMK, Lambda uses an AWS managed CMK named `aws/lambda`, which Lambda creates in your account\.
+ **Encryption helpers** – The Lambda console lets you encrypt environment variable values client side, before sending them to Lambda\. This enhances security further by preventing secrets from being displayed unencrypted in the Lambda console, or in function configuration that's returned by the Lambda API\. The console also provides sample code that you can adapt to decrypt the values in your function handler\.

For more information, see [Using AWS Lambda environment variables](configuration-envvars.md)\.

Lambda always encrypts files that you upload to Lambda, including [deployment packages](gettingstarted-images.md#gettingstarted-images-package) and [layer archives](configuration-layers.md)\.

Amazon CloudWatch Logs and AWS X\-Ray also encrypt data by default, and can be configured to use a customer managed key\. For details, see [Encrypt log data in CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/encrypt-log-data-kms.html) and [Data protection in AWS X\-Ray](https://docs.aws.amazon.com/xray/latest/devguide/xray-console-encryption.html)\.