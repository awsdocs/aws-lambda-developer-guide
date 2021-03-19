# Lambda quotas<a name="gettingstarted-limits"></a>

Lambda sets quotas for the amount of compute and storage resources that you can use to run and store functions\. The following quotas apply per AWS Region and can be increased\. For more information, see [Requesting a quota increase](https://docs.aws.amazon.com/servicequotas/latest/userguide/request-quota-increase.html) in the *Service Quotas User Guide*\.


| Resource | Default quota | Can be increased up to | 
| --- | --- | --- | 
| Concurrent executions | 1,000 | Hundreds of thousands | 
| Function and layer storage \(\.zip file archives\) | 75 GB | Terabytes | 
| Function storage \(container images\) | See [Amazon ECR service quotas](https://docs.aws.amazon.com/AmazonECR/latest/userguide/service-quotas.html)\. |   | 
|  [Elastic network interfaces per virtual private cloud \(VPC\)](configuration-vpc.md)  This quota is shared with other services, such as Amazon Elastic File System \(Amazon EFS\)\. See [Amazon VPC quotas](https://docs.aws.amazon.com/vpc/latest/userguide/amazon-vpc-limits.html)\.   | 250 | Hundreds | 

For details on concurrency and how Lambda scales your function concurrency in response to traffic, see [AWS Lambda function scaling](invocation-scaling.md)\.

The following quotas apply to function configuration, deployments, and execution\. They cannot be changed\.


| Resource | Quota | 
| --- | --- | 
| Function [memory allocation](configuration-console.md) | 128 MB to 10,240 MB, in 1\-MB increments\. | 
| Function [timeout](configuration-console.md) | 900 seconds \(15 minutes\) | 
| Function [environment variables](configuration-envvars.md) | 4 KB | 
| Function [resource\-based policy](access-control-resource-based.md) | 20 KB | 
| Function [layers](configuration-layers.md) | five layers | 
| Function [burst concurrency](invocation-scaling.md) | 500 \- 3000 \(varies per Region\) | 
| [Invocation payload](lambda-invocation.md) \(request and response\) |  6 MB \(synchronous\) 256 KB \(asynchronous\)  | 
| [Deployment package \(\.zip file archive\)](gettingstarted-package.md) size |  50 MB \(zipped, for direct upload\) 250 MB \(unzipped, including layers\) 3 MB \(console editor\), 512 KB maximum for an individual file  | 
| [Container image](lambda-images.md) code package size |  10 GB  | 
| Test events \(console editor\) | 10 | 
| `/tmp` directory storage | 512 MB | 
| File descriptors | 1,024 | 
| Execution processes/threads | 1,024 | 

The following quotas are associated with Lambda API requests\.


| Resource | Quota | 
| --- | --- | 
| Invocation requests per Region \(requests per second\) |  10 x concurrent executions quota \([synchronous](invocation-sync.md), all sources\) 10 x concurrent executions quota \([asynchronous](invocation-async.md), non\-AWS sources\)  | 
| Invocation requests per Region \(requests per second\) for asynchronous [AWS service sources](lambda-services.md) |  Unlimited requests accepted\. Execution rate is based on concurrency available to the function\. See [Asynchronous invocation](invocation-async.md)\.  | 
| Invocation requests per function version or alias \(requests per second\) |  10 x allocated [provisioned concurrency](configuration-concurrency.md)  This quota applies only to functions that use provisioned concurrency\.   | 
| [GetFunction](API_GetFunction.md) API requests | 100 requests per second | 
| [GetPolicy](API_GetPolicy.md) API requests | 15 requests per second | 
| Remainder of the control plane API requests \(excludes invocation, GetFunction, and GetPolicy requests\) | 15 requests per second | 

Quotas for other services, such as AWS Identity and Access Management \(IAM\), Amazon CloudFront \(Lambda@Edge\), and Amazon Virtual Private Cloud \(Amazon VPC\), can impact your Lambda functions\. For more information, see [AWS service quotas](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *Amazon Web Services General Reference*, and [Using AWS Lambda with other services](lambda-services.md)\.