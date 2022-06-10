# Troubleshoot networking issues in Lambda<a name="troubleshooting-networking"></a>

By default, Lambda runs your functions in an internal virtual private cloud \(VPC\) with connectivity to AWS services and the internet\. To access local network resources, you can [configure your function to connect to a VPC in your account](configuration-vpc.md)\. When you use this feature, you manage the function's internet access and network connectivity with Amazon Virtual Private Cloud \(Amazon VPC\) resources\.

Network connectivity errors can result from issues with your VPC's routing configuration, security group rules, AWS Identity and Access Management \(IAM\) role permissions, or network address translation \(NAT\), or from the availability of resources such as IP addresses or network interfaces\. Depending on the issue, you might see a specific error or timeout if a request can't reach its destination\.

## VPC: Function loses internet access or times out<a name="troubleshooting-networking-cfn"></a>

**Issue:** *Your Lambda function loses internet access after connecting to a VPC\.*

**Error:** *Error: connect ETIMEDOUT 176\.32\.98\.189:443*

**Error:** *Error: Task timed out after 10\.00 seconds*

**Error:** *ReadTimeoutError: Read timed out\. \(read timeout=15\)*

When you connect a function to a VPC, all outbound requests go through the VPC\. To connect to the internet, configure your VPC to send outbound traffic from the function's subnet to a NAT gateway in a public subnet\. For more information and sample VPC configurations, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

If some of your TCP connections are timing out, this may be due to packet fragmentation\. Lambda functions cannot handle incoming fragmented TCP requests, since Lambda does not support IP fragmentation for TCP or ICMP\.

## VPC: Function needs access to AWS services without using the internet<a name="troubleshooting-networking-access"></a>

**Issue:** *Your Lambda function needs access to AWS services without using the internet\.*

To connect a function to AWS services from a private subnet with no internet access, use VPC endpoints\. For a sample AWS CloudFormation template with VPC endpoints for Amazon Simple Storage Service \(Amazon S3\) and Amazon DynamoDB \(DynamoDB\), see [Sample VPC configurations](configuration-vpc.md#vpc-samples)\.

## VPC: Elastic network interface limit reached<a name="troubleshooting-networking-limit"></a>

**Error:** *ENILimitReachedException: The elastic network interface limit was reached for the function's VPC\.*

When you connect a Lambda function to a VPC, Lambda creates an elastic network interface for each combination of subnet and security group attached to the function\. The default service quota is 250 network interfaces per VPC\. To request a quota increase, use the [Service Quotas console](https://console.aws.amazon.com/servicequotas/home/services/lambda/quotas/L-9FEE3D26)\.