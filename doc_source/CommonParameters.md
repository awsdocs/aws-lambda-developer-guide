# Common Parameters<a name="CommonParameters"></a>

The following list contains the parameters that all actions use for signing Signature Version 4 requests with a query string\. Any action\-specific parameters are listed in the topic for that action\. For more information about Signature Version 4, see [Signature Version 4 Signing Process](http://docs.aws.amazon.com/general/latest/gr/signature-version-4.html) in the *Amazon Web Services General Reference*\.

 **Action**   <a name="CommonParameters-Action"></a>
The action to be performed\.  
Type: string  
 Required: Yes 

 **Version**   <a name="CommonParameters-Version"></a>
The API version that the request is written for, expressed in the format YYYY\-MM\-DD\.  
Type: string  
 Required: Yes 

 **X\-Amz\-Algorithm**   <a name="CommonParameters-X-Amz-Algorithm"></a>
The hash algorithm that you used to create the request signature\.  
Condition: Specify this parameter when you include authentication information in a query string instead of in the HTTP authorization header\.  
Type: string  
 Valid Values: `AWS4-HMAC-SHA256`   
 Required: Conditional 

 **X\-Amz\-Credential**   <a name="CommonParameters-X-Amz-Credential"></a>
The credential scope value, which is a string that includes your access key, the date, the region you are targeting, the service you are requesting, and a termination string \("aws4\_request"\)\. The value is expressed in the following format: *access\_key*/*YYYYMMDD*/*region*/*service*/aws4\_request\.  
For more information, see [Task 2: Create a String to Sign for Signature Version 4](http://docs.aws.amazon.com/general/latest/gr/sigv4-create-string-to-sign.html) in the *Amazon Web Services General Reference*\.  
Condition: Specify this parameter when you include authentication information in a query string instead of in the HTTP authorization header\.  
Type: string  
 Required: Conditional 

 **X\-Amz\-Date**   <a name="CommonParameters-X-Amz-Date"></a>
The date that is used to create the signature\. The format must be ISO 8601 basic format \(YYYYMMDD'T'HHMMSS'Z'\)\. For example, the following date time is a valid X\-Amz\-Date value: `20120325T120000Z`\.  
Condition: X\-Amz\-Date is optional for all requests; it can be used to override the date used for signing requests\. If the Date header is specified in the ISO 8601 basic format, X\-Amz\-Date is not required\. When X\-Amz\-Date is used, it always overrides the value of the Date header\. For more information, see [Handling Dates in Signature Version 4](http://docs.aws.amazon.com/general/latest/gr/sigv4-date-handling.html) in the *Amazon Web Services General Reference*\.  
Type: string  
 Required: Conditional 

 **X\-Amz\-Security\-Token**   <a name="CommonParameters-X-Amz-Security-Token"></a>
The temporary security token that was obtained through a call to AWS Security Token Service \(AWS STS\)\. For a list of services that support temporary security credentials from AWS Security Token Service, go to [AWS Services That Work with IAM](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_aws-services-that-work-with-iam.html) in the *IAM User Guide*\.  
Condition: If you're using temporary security credentials from the AWS Security Token Service, you must include the security token\.  
Type: string  
 Required: Conditional 

 **X\-Amz\-Signature**   <a name="CommonParameters-X-Amz-Signature"></a>
Specifies the hex\-encoded signature that was calculated from the string to sign and the derived signing key\.  
Condition: Specify this parameter when you include authentication information in a query string instead of in the HTTP authorization header\.  
Type: string  
 Required: Conditional 

 **X\-Amz\-SignedHeaders**   <a name="CommonParameters-X-Amz-SignedHeaders"></a>
Specifies all the HTTP headers that were included as part of the canonical request\. For more information about specifying signed headers, see [ Task 1: Create a Canonical Request For Signature Version 4](http://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-request.html) in the * Amazon Web Services General Reference*\.  
Condition: Specify this parameter when you include authentication information in a query string instead of in the HTTP authorization header\.  
Type: string  
 Required: Conditional 