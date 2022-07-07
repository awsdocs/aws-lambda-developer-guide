# Lambda quotas<a name="gettingstarted-limits"></a>

**Important**  
New AWS accounts have reduced concurrency and memory quotas\. AWS raises these quotas automatically based on your usage\. You can also [request a quota increase](https://docs.aws.amazon.com/servicequotas/latest/userguide/request-quota-increase.html)\.

## Compute and storage<a name="compute-and-storage"></a>

Lambda sets quotas for the amount of compute and storage resources that you can use to run and store functions\. The following quotas apply per AWS Region and can be increased\. For more information, see [Requesting a quota increase](https://docs.aws.amazon.com/servicequotas/latest/userguide/request-quota-increase.html) in the *Service Quotas User Guide*\.


| Resource | Default quota | Can be increased up to | 
| --- | --- | --- | 
|  Concurrent executions  |  1,000  |  Tens of thousands  | 
|  Storage for uploaded functions \(\.zip file archives\) and layers\. Each function version and layer version consumes storage\.  For best practices on managing your code storage, see [Monitoring Lambda code storage](https://docs.aws.amazon.com/lambda/latest/operatorguide/code-storage.html) in the *Lambda Operator Guide*\.  |  75 GB  |  Terabytes  | 
|  Storage for functions defined as container images\. These images are stored in Amazon ECR\.  |  See [Amazon ECR service quotas](https://docs.aws.amazon.com/AmazonECR/latest/userguide/service-quotas.html)\.  |     | 
|  [Elastic network interfaces per virtual private cloud \(VPC\)](configuration-vpc.md)  This quota is shared with other services, such as Amazon Elastic File System \(Amazon EFS\)\. See [Amazon VPC quotas](https://docs.aws.amazon.com/vpc/latest/userguide/amazon-vpc-limits.html)\.   |  250  |  Hundreds  | 

For details on concurrency and how Lambda scales your function concurrency in response to traffic, see [Lambda function scaling](invocation-scaling.md)\.

## Function configuration, deployment, and execution<a name="function-configuration-deployment-and-execution"></a>

The following quotas apply to function configuration, deployment, and execution\. They cannot be changed\.

**Note**  
The Lambda documentation, log messages, and console use the abbreviation KB/MB/GB \(rather than KiB/MiB/GiB\) to mean 1024/1024<sup>2</sup>/1024<sup>3</sup> bytes.


| Resource | Quota | 
| --- | --- | 
|  Function [memory allocation](configuration-function-common.md)  |  128 MB to 10,240 MB, in 1\-MB increments\.  | 
|  Function timeout  |  900 seconds \(15 minutes\)  | 
|  Function [environment variables](configuration-envvars.md)  |  4 KB, for all environment variables associated with the function, in aggregate  | 
|  Function [resource\-based policy](access-control-resource-based.md)  |  20 KB  | 
|  Function [layers](configuration-layers.md)  |  five layers  | 
|  Function [burst concurrency](invocation-scaling.md)  |  500 \- 3000 \(varies per Region\)  | 
|  [Invocation payload](lambda-invocation.md) \(request and response\)  |  6 MB \(synchronous\) 256 KB \(asynchronous\)  | 
|  [Deployment package \(\.zip file archive\)](gettingstarted-package.md) size  |  50 MB \(zipped, for direct upload\) 250 MB \(unzipped\) This quota applies to all the files you upload, including layers and custom runtimes\. 3 MB \(console editor\)  | 
|  [Container image](images-create.md) code package size  |  10 GB  | 
|  Test events \(console editor\)  |  10  | 
|  `/tmp` directory storage  |  512 MB to 10,240 MB, in 1\-MB increments\.  | 
|  File descriptors  |  1,024  | 
|  Execution processes/threads  |  1,024  | 

## Lambda API requests<a name="api-requests"></a>

The following quotas are associated with Lambda API requests\.


| Resource | Quota | 
| --- | --- | 
|  Invocation requests per Region \(requests per second\)  |  10 x concurrent executions quota \([synchronous](invocation-sync.md), all sources\) 10 x concurrent executions quota \([asynchronous](invocation-async.md), non\-AWS sources\)  | 
|  Invocation requests per Region \(requests per second\) for asynchronous [AWS service sources](lambda-services.md)  |  Unlimited requests accepted\. Execution rate is based on concurrency available to the function\. See [Asynchronous invocation](invocation-async.md)\.  | 
|  Invocation requests per function version or alias \(requests per second\)  |  10 x allocated [provisioned concurrency](configuration-concurrency.md)  This quota applies only to functions that use provisioned concurrency\.   | 
|  [GetFunction](API_GetFunction.md) API requests  |  100 requests per second  | 
|  [GetPolicy](API_GetPolicy.md) API requests  |  15 requests per second  | 
|  Remainder of the control plane API requests \(excludes invocation, GetFunction, and GetPolicy requests\)  |  15 requests per second  | 

## Other services<a name="quotas-other-services"></a>

Quotas for other services, such as AWS Identity and Access Management \(IAM\), Amazon CloudFront \(Lambda@Edge\), and Amazon Virtual Private Cloud \(Amazon VPC\), can impact your Lambda functions\. For more information, see [AWS service quotas](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *Amazon Web Services General Reference*, and [Using AWS Lambda with other services](lambda-services.md)\.
