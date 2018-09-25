# Configuring a Lambda Function to Access Resources in an Amazon VPC<a name="vpc"></a>

Typically, you create resources inside Amazon Virtual Private Cloud \(Amazon VPC\) so that they cannot be accessed over the public Internet\. These resources could be AWS service resources, such as Amazon Redshift data warehouses, Amazon ElastiCache clusters, or Amazon RDS instances\. They could also be your own services running on your own EC2 instances\. By default, resources within a VPC are not accessible from within a Lambda function\. 

AWS Lambda runs your function code securely within a VPC by default\. However, to enable your Lambda function to access resources inside your private VPC, you must provide additional VPC\-specific configuration information that includes VPC subnet IDs and security group IDs\. AWS Lambda uses this information to set up elastic network interfaces [\(ENIs\)](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_ElasticNetworkInterfaces.html) that enable your function to connect securely to other resources within your private VPC\.

**Important**  
AWS Lambda does not support connecting to resources within Dedicated Tenancy VPCs\. For more information, see [Dedicated VPCs](https://docs.aws.amazon.com/vpc/latest/userguide/dedicated-instance.html)\.

## Configuring a Lambda Function for Amazon VPC Access<a name="vpc-configuring"></a>

You add VPC information to your Lambda function configuration using the `VpcConfig` parameter, either at the time you create a Lambda function \(see [CreateFunction](API_CreateFunction.md)\), or you can add it to the existing Lambda function configuration \(see [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)\)\. Following are AWS CLI examples:
+ The `create-function` CLI command specifies the `--vpc-config` parameter to provide VPC information at the time you create a Lambda function\. Note that the `--runtime` parameter specifies `python3.6`\. You can also use `python2.7`\.

  ```
  $  aws lambda create-function \
  --function-name ExampleFunction \
  --runtime python3.6 \
  --role execution-role-arn \
  --zip-file fileb://path/app.zip \
  --handler app.handler \
  --vpc-config SubnetIds=comma-separated-vpc-subnet-ids,SecurityGroupIds=comma-separated-security-group-ids \
  --memory-size 1024
  ```
**Note**  
The Lambda function execution role must have permissions to create, describe and delete ENIs\. AWS Lambda provides a permissions policy, `AWSLambdaVPCAccessExecutionRole`, with permissions for the necessary EC2 actions \(`ec2:CreateNetworkInterface`, ` ec2:DescribeNetworkInterfaces`, and `ec2:DeleteNetworkInterface`\) that you can use when creating a role\. You can review the policy in the IAM console\. Do not delete this role immediately after your Lambda function execution\. There is a delay between the time your Lambda function executes and ENI deletion\. If you do delete the role immediately after function execution, you are responsible for deleting the ENIs\.
+ The `update-function-configuration` CLI command specifies the `--vpc-config` parameter to add VPC information to an existing Lambda function configuration\.

  ```
  $ aws lambda update-function-configuration \
  --function-name ExampleFunction \
  --vpc-config SubnetIds=comma-separated-vpc-subnet-ids,SecurityGroupIds=security-group-ids
  ```

  To remove VPC\-related information from your Lambda function configuration, use the `UpdateFunctionConfiguration` API by providing an empty list of subnet IDs and security group IDs as shown in the following example CLI command\.

  ```
  $ aws lambda update-function-configuration \
  --function-name ExampleFunction \
  --vpc-config SubnetIds=[],SecurityGroupIds=[]
  ```

Note the following additional considerations:
+ When you add VPC configuration to a Lambda function, it can only access resources in that VPC\. If a Lambda function needs to access both VPC resources and the public Internet, the VPC needs to have a Network Address Translation \(NAT\) instance inside the VPC\. 
+ When a Lambda function is configured to run within a VPC, it incurs an additional ENI start\-up penalty\. This means address resolution may be delayed when trying to connect to network resources\.

## Internet Access for Lambda Functions<a name="vpc-internet"></a>

AWS Lambda uses the VPC information you provide to set up [ENIs](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_ElasticNetworkInterfaces.html) that allow your Lambda function to access VPC resources\. Each ENI is assigned a private IP address from the IP address range within the Subnets you specify, but is not assigned any public IP addresses\. *Therefore, if your Lambda function requires Internet access \(for example, to access AWS services that don't have VPC endpoints \), you can configure a NAT instance inside your VPC or you can use the Amazon VPC NAT gateway*\. For more information, see [NAT Gateways](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html) in the *Amazon VPC User Guide*\. You cannot use an Internet gateway attached to your VPC, since that requires the ENI to have public IP addresses\. 

**Important**  
If your Lambda function needs Internet access, do not attach it to a public subnet or to a private subnet without Internet access\. Instead, attach it only to private subnets with Internet access through a NAT instance or an Amazon VPC NAT gateway\. 

## Guidelines for Setting Up VPC\-Enabled Lambda Functions<a name="vpc-setup-guidelines"></a>

Your Lambda function automatically scales based on the number of events it processes\. The following are general guidelines for setting up VPC\-enabled Lambda functions to support the scaling behavior\. 
+ If your Lambda function accesses a VPC, you must make sure that your VPC has sufficient ENI capacity to support the scale requirements of your Lambda function\. You can use the following formula to approximately determine the ENI capacity\.

  ```
  Projected peak concurrent executions * (Memory in GB / 3GB)
  ```

  Where: 
  + **Projected peak concurrent execution** – Use the information in  [Managing Concurrency](concurrent-executions.md) to determine this value\.
  + **Memory** – The amount of memory you configured for your Lambda function\. 
+ The subnets you specify should have sufficient available IP addresses to match the number of ENIs\.

  We also recommend that you specify at least one subnet in each Availability Zone in your Lambda function configuration\. By specifying subnets in each of the Availability Zones, your Lambda function can run in another Availability Zone if one goes down or runs out of IP addresses\. 

**Note**  
If your VPC does not have sufficient ENIs or subnet IPs, your Lambda function will not scale as requests increase, and you will see an increase in function failures\. AWS Lambda currently does not log errors to CloudWatch Logs that are caused by insufficient ENIs or IP addresses\. If you see an increase in errors without corresponding CloudWatch Logs, you can invoke the Lambda function synchronously to get the error responses \(for example, test your Lambda function in the AWS Lambda console because the console invokes your Lambda function synchronously and displays errors\)\.