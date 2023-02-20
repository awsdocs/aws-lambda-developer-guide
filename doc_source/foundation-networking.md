# VPC networking for Lambda<a name="foundation-networking"></a>

Amazon Virtual Private Cloud \(Amazon VPC\) is a virtual network in the AWS cloud, dedicated to your AWS account\. You can use Amazon VPC to create a private network for resources such as databases, cache instances, or internal services\. For more information about Amazon VPC, see [What is Amazon VPC?](https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html)

A Lambda function always runs inside a VPC owned by the Lambda service\. Lambda applies network access and security rules to this VPC and Lambda maintains and monitors the VPC automatically\. If your Lambda function needs to access the resources in your account VPC, [ configure the function to access the VPC](configuration-vpc.md)\. Lambda provides managed resources named Hyperplane ENIs, which your Lambda function uses to connect from the Lambda VPC to an ENI \(Elastic network interface\) in your account VPC\.

There's no additional charge for using a VPC or a Hyperplane ENI\. There are charges for some VPC components, such as NAT gateways\. For more information, see [Amazon VPC Pricing](http://aws.amazon.com/vpc/pricing)\.

**Topics**
+ [VPC network elements](#foundation-nw-concepts)
+ [Connecting Lambda functions to your VPC](#foundation-nw-connecting)
+ [Lambda Hyperplane ENIs](#foundation-nw-eni)
+ [Connections](#foundation-nw-addressing)
+ [Security](#foundation-nw-security)
+ [Observability](#foundation-nw-obs)

## VPC network elements<a name="foundation-nw-concepts"></a>

Amazon VPC networks includes the following network elements:
+ Elastic network interface – [elastic network interface](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_ElasticNetworkInterfaces.html) is a logical networking component in a VPC that represents a virtual network card\. 
+ Subnet – A range of IP addresses in your VPC\. You can add AWS resources to a specified subnet\. Use a public subnet for resources that must connect to the internet, and a private subnet for resources that don't connect to the internet\. 
+ Security group – use security groups to control access to the AWS resources in each subnet\.
+ Access control list \(ACL\) – use a network ACL to provide additional security in a subnet\. The default subnet ACL allows all inbound and outbound traffic\.
+ Route table – contains a set of routes that AWS uses to direct the network traffic for your VPC\. You can explicitly associate a subnet with a particular route table\. By default, the subnet is associated with the main route table\.
+ Route – each route in a route table specifies a range of IP addresses and the destination where Lambda sends the traffic for that range\. The route also specifies a target, which is the gateway, network interface, or connection through which to send the traffic\.
+ NAT gateway – An AWS Network Address Translation \(NAT\) service that controls access from a private VPC private subnet to the Internet\.
+ VPC endpoints – You can use an Amazon VPC endpoint to create private connectivity to services hosted in AWS, without requiring access over the internet or through a NAT device, VPN connection, or AWS Direct Connect connection\. For more information, see [AWS PrivateLink and VPC endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/endpoint-services-overview.html)\.

For more information about Amazon VPC networking definitions, see [ How Amazon VPC works](https://docs.aws.amazon.com/vpc/latest/userguide/how-it-works.html) in the Amazon VPC Developer Guide and the [Amazon VPC FAQs\.](http://aws.amazon.com/vpc/faqs.html) 

## Connecting Lambda functions to your VPC<a name="foundation-nw-connecting"></a>

A Lambda function always runs inside a VPC owned by the Lambda service\. By default, a Lambda function isn't connected to VPCs in your account\. When you connect a function to a VPC in your account, the function can't access the internet unless your VPC provides access\.

Lambda accesses resources in your VPC using a Hyperplane ENI\. Hyperplane ENIs provide NAT capabilities from the Lambda VPC to your account VPC using VPC\-to\-VPC NAT \(V2N\)\. V2N provides connectivity from the Lambda VPC to your account VPC, but not in the other direction\.

When you create a Lambda function \(or update its VPC settings\), Lambda allocates a Hyperplane ENI for each subnet in your function's VPC configuration\. Multiple Lambda functions can share a network interface, if the functions share the same subnet and security group\. 

To connect to another AWS service, you can use [VPC endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html) for private communications between your VPC and supported AWS services\. An alternative approach is to use a [NAT gateway](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html) to route outbound traffic to another AWS service\. 

To give your function access to the internet, route outbound traffic to a NAT gateway in a public subnet\. The NAT gateway has a public IP address and can connect to the internet through the VPC's internet gateway\. 

For information about how to configure Lambda VPC networking, see [Lambda networking](configuration-vpc.md)\.

## Lambda Hyperplane ENIs<a name="foundation-nw-eni"></a>

The Hyperplane ENI is a managed network resource that the Lambda service creates and manages\. Multiple execution environments in the Lambda VPC can use a Hyperplane ENI to securely access resources inside of VPCs in your account\. Hyperplane ENIs provide NAT capabilities from the Lambda VPC to your account VPC\. For more information about Hyperplane ENIs, see [Improved VPC networking for AWS Lambda functions ](http://aws.amazon.com/blogs/compute/announcing-improved-vpc-networking-for-aws-lambda-functions/) in the AWS compute blog\.

Each unique security group and subnet combination in your account requires a different network interface\. Functions in the account that share the same security group and subnet combination use the same network interfaces\. 

Because the functions in your account share the ENI resources, the ENI lifecycle is more complex than other Lambda resources\. The following sections describe the ENI lifecycle\.

**Topics**
+ [Creating ENIs](#foundation-nw-eni-create)
+ [Managing ENIs](#foundation-nw-eni-man)
+ [Deleting ENIs](#foundation-nw-eni-delete)

### Creating ENIs<a name="foundation-nw-eni-create"></a>

Lambda may create Hyperplane ENI resources for a newly created VPC\-enabled function or for a VPC configuration change to an existing function\. The function remains in pending state while Lambda creates the required resources\. When the Hyperplane ENI is ready, the function transitions to active state and the ENI becomes available for use\. Lambda can require several minutes to create a Hyperplane ENI\. 

For a newly created VPC\-enabled function, any invocations or other API actions that operate on the function fail until the function state transitions to active\. 

For a VPC configuration change to an existing function, any function invocations continue to use the Hyperplane ENI associated with the old subnet and security group configuration until the function state transitions to active\. 

If a Lambda function remains idle for consecutive weeks, Lambda reclaims the unused Hyperplane ENIs and sets the function state to idle\. The next invocation causes Lambda to reactivate the idle function\. The invocation fails, and the function enters pending state until Lambda completes the creation or allocation of a Hyperplane ENI\. 

For more information about function states, see [Lambda function states](functions-states.md)\.

### Managing ENIs<a name="foundation-nw-eni-man"></a>

Lambda uses permissions in your function's execution role to create and manage network interfaces\. Lambda creates a Hyperplane ENI when you define a unique subnet plus security group combination for a VPC\-enabled function in an account\. Lambda reuses the Hyperplane ENI for other VPC\-enabled functions in your account that use the same subnet and security group combination\. 

There is no quota on the number of Lambda functions that can use the same Hyperplane ENI\. However, each Hyperplane ENI supports up to 65,000 connections/ports\. If the number of connections exceeds 65,000, Lambda creates a new Hyperplane ENI to provide additional connections\.

When you update your function configuration to access a different VPC, Lambda terminates connectivity to the Hyperplane ENI in the previous VPC\. The process to update the connectivity to a new VPC can take several minutes\. During this time, invocations to the function continue to use the previous VPC\. After the update is complete, new invocations start using the Hyperplane ENI in the new VPC\. At this point, the Lambda function is no longer connected to the previous VPC\. 

### Deleting ENIs<a name="foundation-nw-eni-delete"></a>

When you update a function to remove its VPC configuration, Lambda requires up to 20 minutes to delete the attached Hyperplane ENI\. Lambda only deletes the ENI if no other function \(or published function version\) is using that Hyperplane ENI\. 

Lambda relies on permissions in the function [ execution role](lambda-intro-execution-role.md) to delete the Hyperplane ENI\. If you delete the execution role before Lambda deletes the Hyperplane ENI, Lambda won't be able to delete the Hyperplane ENI\. You can manually perform the deletion\.

Lambda doesn't delete network interfaces that are in use by functions or function versions in your account\. You can use the [Lambda ENI Finder](https://github.com/awslabs/aws-support-tools/tree/master/Lambda/FindEniMappings) to identify the functions or function versions that are using a Hyperplane ENI\. For any functions or function versions that you no longer need, you can remove the VPC configuration so that Lambda deletes the Hyperplane ENI\.

## Connections<a name="foundation-nw-addressing"></a>

Lambda supports two types of connections: TCP \(Transmission Control Protocol\) and UDP \(User Datagram Protocol\)\.

When you create a VPC, Lambda automatically creates a set of DHCP options and associates them with the VPC\. You can configure your own DHCP options set for your VPC\. For more details, refer to [Amazon VPC DHCP options](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_DHCP_Options.html)\.

Amazon provides a DNS server \(the Amazon Route 53 resolver\) for your VPC\. For more information, see [DNS support for your VPC](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-dns.html)\.

## Security<a name="foundation-nw-security"></a>

AWS provides [https://docs.aws.amazon.com/vpc/latest/userguide/VPC_SecurityGroups.html](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_SecurityGroups.html) and [https://docs.aws.amazon.com/vpc/latest/userguide/vpc-network-acls.html](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-network-acls.html) to increase security in your VPC\. Security groups control inbound and outbound traffic for your instances, and network ACLs control inbound and outbound traffic for your subnets\. Security groups provide enough access control for most subnets\. You can use network ACLs if you want an additional layer of security for your VPC\. For more information, see [Internetwork traffic privacy in Amazon VPC](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_Security.html)\. Every subnet that you create is automatically associated with the VPC's default network ACL\. You can change the association, and you can change the contents of the default network ACL\. 

For general security best practices, see [VPC security best practices](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-security-best-practices.html)\. For details on how you can use IAM to manage access to the Lambda API and resources, see [AWS Lambda permissions](lambda-permissions.md)\.

You can use Lambda\-specific condition keys for VPC settings to provide additional permission controls for your Lambda functions\. For more information about VPC condition keys, see [Using IAM condition keys for VPC settings](configuration-vpc.md#vpc-conditions)\.

## Observability<a name="foundation-nw-obs"></a>

You can use [VPC Flow Logs](https://docs.aws.amazon.com/vpc/latest/userguide/flow-logs.html) to capture information about the IP traffic going to and from network interfaces in your VPC\. You can publish Flow log data to Amazon CloudWatch Logs or Amazon S3\. After you've created a flow log, you can retrieve and view its data in the chosen destination\. 

Note: when you attach a function to a VPC, the CloudWatch log messages do not use the VPC routes\. Lambda sends them using the regular routing for logs\.