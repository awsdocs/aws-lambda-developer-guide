# Data protection in AWS Lambda<a name="security-dataprotection"></a>

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