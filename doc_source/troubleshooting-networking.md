# Troubleshoot networking issues in AWS Lambda<a name="troubleshooting-networking"></a>

By default, Lambda runs your functions in an internal virtual private cloud \(VPC\) with connectivity to AWS services and the internet\. To access local network resources, you can [configure your function to connect to a VPC in your account](configuration-vpc.md)\. When you use this feature, you manage the function's internet access and network connectivity with VPC resources\.

Network connectivity errors can result from issues in routing configuration, security group rules, role permissions, network address translation, or the availability of resources such as IP addresses or network interfaces\. They may result in a specific error or, if a request can't reach its destination, a timeout\.

**Issue:** *Function loses internet access after connecting to a VPC*

**Error:** *Error: connect ETIMEDOUT 176\.32\.98\.189:443*

**Error:** *Error: Task timed out after 10\.00 seconds*

When you connect a function to a VPC, all outbound requests go through your VPC\. To connect to the internet, configure your VPC to send outbound traffic from the function's subnet to a NAT gateway in a public subnet\. For more information and sample VPC configurations, see [Internet and service access for VPC\-connected functions](configuration-vpc.md#vpc-internet)\.

**Issue:** *Function needs access to AWS services without using the internet*

To connect to AWS services from a private subnet with no internet access, use VPC endpoints\. For a sample template with VPC endpoints for DynamoDB and Amazon S3, see [Sample VPC configurations](configuration-vpc.md#vpc-samples)\.

**Error:** *ENILimitReachedException: The elastic network interface limit was reached for the function's VPC\.*

When you connect a function to a VPC, Lambda creates an elastic network interface for each combination of subnet and security group attached to the function\. These network interfaces are limited to 250 per VPC, but this limit can be increased\. To request an increase, use the [Support Center console](https://console.aws.amazon.com/support/v1#/case/create?issueType=service-limit-increase)\.