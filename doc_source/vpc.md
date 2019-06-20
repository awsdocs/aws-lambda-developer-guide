# Configuring a Lambda Function to Access Resources in an Amazon VPC<a name="vpc"></a>

You can configure a function to connect to private subnets in a virtual private cloud \(VPC\) in your account\. Use Amazon Virtual Private Cloud \(Amazon VPC\) to create a private network for resources such as databases, cache instances, or internal services\. Connect your function to the VPC to access private resources during execution\.

AWS Lambda runs your function code securely within a VPC by default\. However, to enable your Lambda function to access resources inside your private VPC, you must provide additional VPC\-specific configuration information that includes private subnet IDs and security group IDs\. AWS Lambda uses this information to set up elastic network interfaces [\(ENIs\)](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_ElasticNetworkInterfaces.html) that enable your function to connect securely to other resources within your private VPC\.

Lambda functions cannot connect directly to a VPC with [dedicated instance tenancy](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/dedicated-instance.html)\. To connect to resources in a dedicated VPC, [peer it to a second VPC with default tenancy](https://aws.amazon.com/premiumsupport/knowledge-center/lambda-dedicated-vpc/)\.

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

## Configuring a Lambda Function for Amazon VPC Access<a name="vpc-configuring"></a>

You add VPC information to your Lambda function configuration using the `VpcConfig` parameter, either at the time you create a Lambda function \(see [CreateFunction](API_CreateFunction.md)\), or you can add it to the existing Lambda function configuration \(see [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\)\. Following are AWS CLI examples:
+ The `create-function` CLI command specifies the `--vpc-config` parameter to provide VPC information at the time you create a Lambda function\.

  ```
  $ aws lambda create-function --function-name my-function \
  --runtime go1.x --handler main --zip-file fileb://main.zip \
  --role execution-role-arn \
  --vpc-config SubnetIds=comma-separated-vpc-subnet-ids,SecurityGroupIds=comma-separated-security-group-ids
  ```
+ The `update-function-configuration` CLI command specifies the `--vpc-config` parameter to add VPC information to an existing Lambda function configuration\.

  ```
  $ aws lambda update-function-configuration --function-name my-function \
  --vpc-config SubnetIds=comma-separated-vpc-subnet-ids,SecurityGroupIds=security-group-ids
  ```

  To remove VPC\-related information from your Lambda function configuration, use the `UpdateFunctionConfiguration` API by providing an empty list of subnet IDs and security group IDs as shown in the following example CLI command\.

  ```
  $ aws lambda update-function-configuration --function-name my-function \
  --vpc-config SubnetIds=[],SecurityGroupIds=[]
  ```

When you add VPC configuration to a Lambda function, it can only access resources in that VPC\. If a Lambda function needs to access both VPC resources and the public internet, the VPC needs to have a Network Address Translation \(NAT\) instance inside the VPC\. 

When a Lambda function is configured to run within a VPC, it incurs an additional ENI start\-up penalty\. This means address resolution may be delayed when trying to connect to network resources\.

## Internet Access for Lambda Functions<a name="vpc-internet"></a>

AWS Lambda uses the VPC information you provide to set up [ENIs](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_ElasticNetworkInterfaces.html) that allow your Lambda function to access VPC resources\. Each ENI is assigned a private IP address from the IP address range within the subnets you specify\. Functions that are connected to a VPC do not have public IP addresses or internet access by default\.

**Note**  
Several services offer [VPC Endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html)\. You can use VPC endpoints to connect to AWS services from within a VPC without internet access\.

Internet access from a private subnet requires network address translation \(NAT\)\. You can give your function access to the internet by adding a NAT gateway or NAT instance to your VPC\. An internet gateway does not work as it requires public IP addresses\. For more information, see [NAT Gateways](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html) in the *Amazon VPC User Guide*\.

## Guidelines for Setting Up VPC\-Enabled Lambda Functions<a name="vpc-setup-guidelines"></a>

Your Lambda function automatically scales based on the number of events it processes\. The following are general guidelines for setting up VPC\-enabled Lambda functions to support the scaling behavior\. 
+ If your Lambda function accesses a VPC, you must make sure that your VPC has sufficient ENI capacity to support the scale requirements of your Lambda function\. You can use the following formula to approximately determine the ENI requirements\.

  ```
  Projected peak concurrent executions * (Memory in GB / 3GB)
  ```

  Where: 
  + **Projected peak concurrent execution** – Use the information in [Managing Concurrency](concurrent-executions.md) to determine this value\.
  + **Memory** – The amount of memory you configured for your Lambda function\. 
+ The subnets you specify should have sufficient available IP addresses to match the number of ENIs\.

  We also recommend that you specify at least one subnet in each Availability Zone in your Lambda function configuration\. By specifying subnets in each of the Availability Zones, your Lambda function can run in another Availability Zone if one goes down or runs out of IP addresses\. 

If your VPC does not have sufficient ENIs or subnet IPs, your Lambda function will not scale as requests increase, and you will see an increase in invocation errors with EC2 error types like `EC2ThrottledException`\. Amazon VPC applies a [limit to ENI creation](https://docs.aws.amazon.com/vpc/latest/userguide/amazon-vpc-limits.html)\. You can request an increase to this limit in the [Support Center console](https://console.aws.amazon.com/support/v1#/case/create?issueType=service-limit-increase)\. 