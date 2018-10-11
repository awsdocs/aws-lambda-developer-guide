# Runtime Support Policy<a name="runtime-support-policy"></a>

AWS Lambda will only deprecate runtime versions that are marked as EOL \(End of Life\) at the end of their maintenance window\. Versions that are marked deprecated will stop supporting creation of new functions and updates of existing functions authored in the deprecated runtime \(unless re\-configured to use a supported runtime version\)\. AWS Lambda will also not provide security updates, technical support or hotfixes for deprecated runtimes and reserves the right to disable invocations of functions configured to run on a deprecated runtime at any time\. 

You can find a list of runtime versions that are marked as deprecated below:


| Runtime | Version | 
| --- | --- | 
| Node\.js |  0\.10  | 
| Node\.js |  4\.3  | 

## Deprecation Status<a name="runtime-support-policy-ongoing"></a>

The Node Foundation declared End\-of\-Life \(EOL\) for Node\.js v4 on April 30, 2018\. As a result, this version of Node\.js is no longer receiving bug fixes, security updates, or performance improvements from the Node Foundation\. Per the AWS Lambda runtime support policy, language runtimes that have been end\-of\-lifed by the supplier are deprecated in AWS Lambda\.

 While invocations of Lambda functions configured to use Node\.js v4\.3 will continue to work normally, the ability to create new Lambda functions configured to use the Node\.js v4\.3 runtime will be disabled on July 31, 2018\. Code updates to existing functions using Node\.js v4\.3 will be disabled on October 31, 2018\. We strongly encourage you to update all your functions to a newer version of Node\.js so that you continue to benefit from important security, performance, and functionality enhancements offered by the Node Foundation via more recent releases\. 