# API reference<a name="API_Reference"></a>

This section contains the AWS Lambda API Reference documentation\. When making the API calls, you will need to authenticate your request by providing a signature\. AWS Lambda supports signature version 4\. For more information, see [Signature Version 4 signing process](https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html) in the *Amazon Web Services General Reference*\. 

For an overview of the service, see [What is AWS Lambda?](welcome.md)\. 

You can use the AWS CLI to explore the AWS Lambda API\. This guide provides several tutorials that use the AWS CLI\.

**Topics**
+ [Actions](API_Operations.md)
+ [Data Types](API_Types.md)

## Certificate errors when using an SDK<a name="cert-errors"></a>

Because AWS SDKs use the CA certificates from your computer, changes to the certificates on the AWS servers can cause connection failures when you attempt to use an SDK\. You can prevent these failures by keeping your computer's CA certificates and operating system up\-to\-date\. If you encounter this issue in a corporate environment and do not manage your own computer, you might need to ask an administrator to assist with the update process\. The following list shows minimum operating system and Java versions:
+ Microsoft Windows versions that have updates from January 2005 or later installed contain at least one of the required CAs in their trust list\. 
+ Mac OS X 10\.4 with Java for Mac OS X 10\.4 Release 5 \(February 2007\), Mac OS X 10\.5 \(October 2007\), and later versions contain at least one of the required CAs in their trust list\. 
+ Red Hat Enterprise Linux 5 \(March 2007\), 6, and 7 and CentOS 5, 6, and 7 all contain at least one of the required CAs in their default trusted CA list\. 
+ Java 1\.4\.2\_12 \(May 2006\), 5 Update 2 \(March 2005\), and all later versions, including Java 6 \(December 2006\), 7, and 8, contain at least one of the required CAs in their default trusted CA list\. 

When accessing the AWS Lambda management console or AWS Lambda API endpoints, whether through browsers or programmatically, you will need to ensure your client machines support any of the following CAs: 
+ Amazon Root CA 1
+ Starfield Services Root Certificate Authority \- G2
+ Starfield Class 2 Certification Authority

Root certificates from the first two authorities are available from [Amazon trust services](https://www.amazontrust.com/repository/), but keeping your computer up\-to\-date is the more straightforward solution\. To learn more about ACM\-provided certificates, see [AWS Certificate Manager FAQs\.](https://aws.amazon.com/certificate-manager/faqs/#certificates) 