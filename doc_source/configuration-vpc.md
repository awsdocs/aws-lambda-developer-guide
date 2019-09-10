# Configuring a Lambda Function to Access Resources in a VPC<a name="configuration-vpc"></a>

You can configure a function to connect to private subnets in a virtual private cloud \(VPC\) in your account\. Use Amazon Virtual Private Cloud \(Amazon VPC\) to create a private network for resources such as databases, cache instances, or internal services\. Connect your function to the VPC to access private resources during execution\.

Lambda creates an [elastic network interface](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_ElasticNetworkInterfaces.html) for each combination of security group and subnet in your function's VPC configuration in a process that can take a minute or so\.  Multiple functions connected to the same subnets share network interfaces, so connecting additional functions to a subnet that already has a Lambda\-managed network interface is much quicker\. Lambda might create additional network interfaces if you have many functions or very busy functions\.

Lambda functions cannot connect directly to a VPC with [dedicated instance tenancy](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/dedicated-instance.html)\. To connect to resources in a dedicated VPC, [peer it to a second VPC with default tenancy](https://aws.amazon.com/premiumsupport/knowledge-center/lambda-dedicated-vpc/)\.

**VPC Tutorials**
+ [Tutorial: Configuring a Lambda Function to Access Amazon RDS in an Amazon VPC](services-rds-tutorial.md)
+ [Tutorial: Configuring a Lambda Function to Access Amazon ElastiCache in an Amazon VPC](services-elasticache-tutorial.md)

## Execution Role and User Permissions<a name="vpc-permissions"></a>

To connect to a VPC, your function's execution role must have the following permissions\.

**Execution Role Permissions**
+ **ec2:CreateNetworkInterface**
+ **ec2:DescribeNetworkInterfaces**
+ **ec2:DeleteNetworkInterface**

These permissions are included in the **AWSLambdaVPCAccessExecutionRole** managed policy\.

To configure a function to connect to a VPC, you need the following permissions\.

**User Permissions**
+ **ec2:DescribeSecurityGroups**
+ **ec2:DescribeSubnets**
+ **ec2:DescribeVpcs**

## Configuring Amazon VPC Access with the Lambda API<a name="vpc-configuring"></a>

To connect your function to a VPC during creation, use the `vpc-config` option with a list of private subnet IDs and security groups\.

```
$ aws lambda create-function --function-name my-function \
--runtime nodejs10.x --handler index.js --zip-file fileb://function.zip \
--role arn:aws:iam::123456789012:role/lambda-role \
--vpc-config SubnetIds=subnet-071f712345678e7c8,subnet-07fd123456788a036,SecurityGroupIds=sg-085912345678492fb
```

For an existing function, use the same option with the `update-function-configuration` command\.

```
$ aws lambda update-function-configuration --function-name my-function \
--vpc-config SubnetIds=subnet-071f712345678e7c8,subnet-07fd123456788a036,SecurityGroupIds=sg-085912345678492fb
```

To disconnect your function from a VPC, update the function configuration with an empty list of subnets and security groups\.

```
$ aws lambda update-function-configuration --function-name my-function \
--vpc-config SubnetIds=[],SecurityGroupIds=[]
```

These commands use the following APIs\.
+ [CreateFunction](API_CreateFunction.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

## Internet and Service Access for VPC\-Connected Functions<a name="vpc-internet"></a>

By default, Lambda runs your functions in a secure VPC with access to AWS services and the internet\. When you connect a function to a VPC in your account, it does not have access to the internet unless the VPC provides access\.

**Note**  
Several services offer [VPC endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html)\. You can use VPC endpoints to connect to AWS services from within a VPC without internet access\.

Internet access from a private subnet requires network address translation \(NAT\)\. To give your function access to the internet, route outbound traffic to a NAT gateway in a public subnet\. The NAT gateway has a public IP address and can connect to the internet through the VPC's internet gateway\. For more information, see [NAT Gateways](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html) in the *Amazon VPC User Guide*\.

## Sample VPC Configurations<a name="vpc-samples"></a>

Sample AWS CloudFormation templates for VPC configurations that you can use with Lambda functions are available in this guide's GitHub repository\. There are two templates:
+ [vpc\-private\.yaml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/templates/vpc-private.yaml) – A VPC with two private subnets and VPC endpoints for Amazon Simple Storage Service and Amazon DynamoDB\. You can use this template to create a VPC for functions that do not need internet access\. This configuration supports use of Amazon S3 and DynamoDB with the AWS SDK, and access to database resources in the same VPC over a local network connection\.
+ [vpc\-privatepublic\.yaml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/templates/vpc-privatepublic.yaml) – A VPC with two private subnets, VPC endpoints, a public subnet with a NAT gateway, and an internet gateway\. Internet\-bound traffic from functions in the private subnets is routed to the NAT gateway by a route table\.

To use a template to create a VPC, choose **Create stack** in the [AWS CloudFormation console](https://console.aws.amazon.com/cloudformation) and follow the instructions\.