# AWS Lambda Limits<a name="gettingstarted-limits"></a>

AWS Lambda limits the amount of compute and storage resources that you can use to run and store functions\. The following limits apply per\-region and can be increased\. To request an increase, use the [Support Center console](https://console.aws.amazon.com/support/v1#/case/create?issueType=service-limit-increase)\.


| Resource | Default Limit | 
| --- | --- | 
| Concurrent executions | 1,000 | 
| Function and layer storage | 75 GB | 
| [Elastic network interfaces per VPC](configuration-vpc.md) | 250 | 

For details on concurrency and how Lambda scales your function concurrency in response to traffic, see [AWS Lambda Function Scaling](invocation-scaling.md)\.

The following limits apply to function configuration, deployments, and execution\. They cannot be changed\.


| Resource | Limit | 
| --- | --- | 
| Function [memory allocation](configuration-console.md) | 128 MB to 3,008 MB, in 64 MB increments\. | 
| Function [timeout](configuration-console.md) | 900 seconds \(15 minutes\) | 
| Function [environment variables](configuration-envvars.md) | 4 KB | 
| Function [resource\-based policy](access-control-resource-based.md) | 20 KB | 
| Function [layers](configuration-layers.md) | 5 layers | 
| Function [burst concurrency](invocation-scaling.md) | 500 \- 3000 \([varies per region](invocation-scaling.md)\) | 
| Invocation frequency per Region \(requests per second\) |  10 x concurrent executions limit \([synchronous](invocation-sync.md) – all sources\) 10 x concurrent executions limit \([asynchronous](invocation-async.md) – non\-AWS sources\) Unlimited \(asynchronous – [AWS service sources](lambda-services.md)\)  | 
| Invocation frequency per function version or alias \(requests per second\) |  10 x allocated [provisioned concurrency](configuration-concurrency.md) This limit only applies to functions that use provisioned concurrency\.  | 
| [Invocation payload](lambda-invocation.md) \(request and response\) |  6 MB \(synchronous\) 256 KB \(asynchronous\)  | 
| [Deployment package](gettingstarted-features.md#gettingstarted-features-package) size |  50 MB \(zipped, for direct upload\) 250 MB \(unzipped, including layers\) 3 MB \(console editor\)  | 
| Test events \(console editor\) | 10 | 
| `/tmp` directory storage | 512 MB | 
| File descriptors | 1,024 | 
| Execution processes/threads | 1,024 | 

Limits for other services, such as AWS Identity and Access Management, Amazon CloudFront \(Lambda@Edge\), and Amazon Virtual Private Cloud, can impact your Lambda functions\. For more information, see [AWS Service Limits](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) and [Using AWS Lambda with Other Services](lambda-services.md)\.