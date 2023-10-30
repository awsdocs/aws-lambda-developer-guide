# Configuring a Lambda function to access resources in a VPC<a name="configuration-vpc"></a>

You can configure a Lambda function to connect to private subnets in a virtual private cloud \(VPC\) in your AWS account\. Use Amazon Virtual Private Cloud \(Amazon VPC\) to create a private network for resources such as databases, cache instances, or internal services\. Connect your function to the VPC to access private resources while the function is running\.

When you connect a function to a VPC, Lambda assigns your function to a Hyperplane ENI \(elastic network interface\) for each subnet in your function's VPC configuration\. Lambda creates a Hyperplane ENI the first time a unique subnet and security group combination is defined for a VPC\-enabled function in an account\.

While Lambda creates a Hyperplane ENI, you can't perform additional operations that target the function, such as [creating versions](configuration-versions.md) or updating the function's code\. For new functions, you can't invoke the function until its state changes from `Pending` to `Active`\. For existing functions, you can still invoke an earlier version while the update is in progress\. For details about the Hyperplane ENI lifecycle, see [Lambda Hyperplane ENIs](foundation-networking.md#foundation-nw-eni)\.

Lambda functions can't connect directly to a VPC with [ dedicated instance tenancy](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/dedicated-instance.html)\. To connect to resources in a dedicated VPC, [peer it to a second VPC with default tenancy](https://aws.amazon.com/premiumsupport/knowledge-center/lambda-dedicated-vpc/)\.

**Topics**
+ [Managing VPC connections](#vpc-managing-eni)
+ [Execution role and user permissions](#vpc-permissions)
+ [Configuring VPC access \(console\)](#vpc-configuring)
+ [Configuring VPC access \(API\)](#vpc-configuring-api)
+ [Using IAM condition keys for VPC settings](#vpc-conditions)
+ [Internet and service access for VPC\-connected functions](#vpc-internet)
+ [VPC tutorials](#vpc-tutorials)
+ [Sample VPC configurations](#vpc-samples)

## Managing VPC connections<a name="vpc-managing-eni"></a>

This section provides a summary of Lambda VPC connections\. For details about VPC networking in Lambda, see [ VPC networking for Lambda](foundation-networking.md)\.

Multiple functions can share a network interface, if the functions share the same subnet and security group\. Connecting additional functions to the same VPC configuration \(subnet and security group\) that has an existing Lambda\-managed network interface is much quicker than creating a new network interface\. 

If your functions aren't active for a long period of time, Lambda reclaims its network interfaces, and the functions become `Idle`\. To reactivate an idle function, invoke it\. This invocation fails, and the function enters a `Pending` state again until a network interface is available\.

If you update your function to access a different VPC, it terminates connectivity from the Hyperplane ENI to the previous VPC\. The process to update the connectivity to a new VPC can take several minutes\. During this time, Lambda connects function invocations to the previous VPC\. After the update is complete, new invocations start using the new VPC and the Lambda function is no longer connected to the older VPC\.

For short\-lived operations, such as DynamoDB queries, the latency overhead of setting up a TCP connection might be greater than the operation itself\. To ensure connection reuse for short\-lived/infrequently invoked functions, we recommend that you use *TCP keep\-alive* for connections that were created during your function initialization, to avoid creating new connections for subsequent invokes\. For more information on reusing connections using keep\-alive, refer to [Lambda documentation on reusing connections](https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/node-reusing-connections.html)[\.](https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/node-reusing-connections.html)

## Execution role and user permissions<a name="vpc-permissions"></a>

Lambda uses your function's permissions to create and manage network interfaces\. To connect to a VPC, your function's [execution role](lambda-intro-execution-role.md) must have the following permissions:

**Execution role permissions**
+ **ec2:CreateNetworkInterface**
+ **ec2:DescribeNetworkInterfaces** – This action only works if it's allowed on all resources \(`"Resource": "*"`\)\.
+ **ec2:DeleteNetworkInterface** – If you don't specify a resource ID for **DeleteNetworkInterface** in the execution role, your function may not be able to access the VPC\. Either specify a unique resource ID, or include all resource IDs, for example, `"Resource": "arn:aws:ec2:us-west-2:123456789012:*/*"`\.
+ **ec2:AssignPrivateIpAddresses**
+ **ec2:UnassignPrivateIpAddresses**

These permissions are included in the AWS managed policy **AWSLambdaVPCAccessExecutionRole**\. Note that these permissions are required only to create ENIs, not to invoke your VPC function\. In other words, you are still able to invoke your VPC function successfully even if you remove these permissions from your execution role\. To completely disassociate your Lambda function from the VPC, update the function's VPC configuration settings using the console or the [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) API\.

When you configure VPC connectivity, Lambda uses your permissions to verify network resources\. To configure a function to connect to a VPC, your AWS Identity and Access Management \(IAM\) user needs the following permissions:

**User permissions**
+ **ec2:DescribeSecurityGroups**
+ **ec2:DescribeSubnets**
+ **ec2:DescribeVpcs**

## Configuring VPC access \(console\)<a name="vpc-configuring"></a>

If your [IAM permissions](#vpc-conditions) allow you only to create Lambda functions that connect to your VPC, you must configure the VPC when you create the function\. If your IAM permissions allow you to create functions that aren't connected to your VPC, you can add the VPC configuration after you create the function\.

**To configure a VPC when you create a function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose **Create function**\.

1. Under **Basic information**, for **Function name**, enter a name for your function\.

1. Expand **Advanced settings**\.

1. Under **Network**, choose a **VPC** for your function to access\.

1. Choose subnets and security groups\. When you choose a security group, the console displays the inbound and outbound rules for that security group\.
**Note**  
To access private resources, connect your function to private subnets\. If your function needs internet access, use [network address translation \(NAT\)](#vpc-internet)\. Connecting a function to a public subnet doesn't give it internet access or a public IP address\.

1. Choose **Create function**\.

**To configure a VPC for an existing function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **VPC**\.

1. Under **VPC**, choose **Edit**\.

1. Choose a VPC, subnets, and security groups\.
**Note**  
To access private resources, connect your function to private subnets\. If your function needs internet access, use [network address translation \(NAT\)](#vpc-internet)\. Connecting a function to a public subnet doesn't give it internet access or a public IP address\.

1. Choose **Save**\.

## Configuring VPC access \(API\)<a name="vpc-configuring-api"></a>

To connect a Lambda function to a VPC, you can use the following API operations:
+ [CreateFunction](API_CreateFunction.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

To create a function and connect it to a VPC using the AWS Command Line Interface \(AWS CLI\), you can use the `create-function` command with the `vpc-config` option\. The following example creates a function with a connection to a VPC with two subnets and one security group\.

```
aws lambda create-function --function-name my-function \
--runtime nodejs16.x --handler index.js --zip-file fileb://function.zip \
--role arn:aws:iam::123456789012:role/lambda-role \
--vpc-config SubnetIds=subnet-071f712345678e7c8,subnet-07fd123456788a036,SecurityGroupIds=sg-085912345678492fb
```

To connect an existing function to a VPC, use the `update-function-configuration` command with the `vpc-config` option\.

```
aws lambda update-function-configuration --function-name my-function \
--vpc-config SubnetIds=subnet-071f712345678e7c8,subnet-07fd123456788a036,SecurityGroupIds=sg-085912345678492fb
```

To disconnect your function from a VPC, update the function configuration with an empty list of subnets and security groups\.

```
aws lambda update-function-configuration --function-name my-function \
--vpc-config SubnetIds=[],SecurityGroupIds=[]
```

## Using IAM condition keys for VPC settings<a name="vpc-conditions"></a>

You can use Lambda\-specific condition keys for VPC settings to provide additional permission controls for your Lambda functions\. For example, you can require that all functions in your organization are connected to a VPC\. You can also specify the subnets and security groups that the function's users can and can't use\.

Lambda supports the following condition keys in IAM policies:
+ **lambda:VpcIds** – Allow or deny one or more VPCs\.
+ **lambda:SubnetIds** – Allow or deny one or more subnets\.
+ **lambda:SecurityGroupIds** – Allow or deny one or more security groups\.

The Lambda API operations [CreateFunction](API_CreateFunction.md) and [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) support these condition keys\. For more information about using condition keys in IAM policies, see [IAM JSON Policy Elements: Condition](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_condition.html) in the *IAM User Guide*\.

**Tip**  
If your function already includes a VPC configuration from a previous API request, you can send an `UpdateFunctionConfiguration` request without the VPC configuration\.

### Example policies with condition keys for VPC settings<a name="vpc-condition-examples"></a>

The following examples demonstrate how to use condition keys for VPC settings\. After you create a policy statement with the desired restrictions, append the policy statement for the target IAM user or role\.

#### Ensure that users deploy only VPC\-connected functions<a name="vpc-condition-example-1"></a>

To ensure that all users deploy only VPC\-connected functions, you can deny function create and update operations that don't include a valid VPC ID\. 

Note that VPC ID is not an input parameter to the `CreateFunction` or `UpdateFunctionConfiguration` request\. Lambda retrieves the VPC ID value based on the subnet and security group parameters\.

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "EnforceVPCFunction",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Deny",
      "Resource": "*",
      "Condition": {
        "Null": {
           "lambda:VpcIds": "true"
        }
      }
    }
  ]
}
```

#### Deny users access to specific VPCs, subnets, or security groups<a name="vpc-condition-example-2"></a>

To deny users access to specific VPCs, use `StringEquals` to check the value of the `lambda:VpcIds` condition\. The following example denies users access to `vpc-1` and `vpc-2`\.

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "EnforceOutOfVPC",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Deny",
      "Resource": "*",
      "Condition": {
        "StringEquals": {
            "lambda:VpcIds": ["vpc-1", "vpc-2"]
        }
      }
    }
```

To deny users access to specific subnets, use `StringEquals` to check the value of the `lambda:SubnetIds` condition\. The following example denies users access to `subnet-1` and `subnet-2`\.

```
{
      "Sid": "EnforceOutOfSubnet",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Deny",
      "Resource": "*",
      "Condition": {
        "ForAnyValue:StringEquals": {
            "lambda:SubnetIds": ["subnet-1", "subnet-2"]
        }
      }
    }
```

To deny users access to specific security groups, use `StringEquals` to check the value of the `lambda:SecurityGroupIds` condition\. The following example denies users access to `sg-1` and `sg-2`\.

```
{
      "Sid": "EnforceOutOfSecurityGroups",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Deny",
      "Resource": "*",
      "Condition": {
        "ForAnyValue:StringEquals": {
            "lambda:SecurityGroupIds": ["sg-1", "sg-2"]
        }
      }
    }
  ]
}
```

#### Allow users to create and update functions with specific VPC settings<a name="vpc-condition-example-3"></a>

To allow users to access specific VPCs, use `StringEquals` to check the value of the `lambda:VpcIds` condition\. The following example allows users to access `vpc-1` and `vpc-2`\.

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "EnforceStayInSpecificVpc",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Allow",
      "Resource": "*",
      "Condition": {
        "StringEquals": {
            "lambda:VpcIds": ["vpc-1", "vpc-2"]
        }
      }
    }
```

To allow users to access specific subnets, use `StringEquals` to check the value of the `lambda:SubnetIds` condition\. The following example allows users to access `subnet-1` and `subnet-2`\.

```
{
      "Sid": "EnforceStayInSpecificSubnets",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Allow",
      "Resource": "*",
      "Condition": {
        "ForAllValues:StringEquals": {
            "lambda:SubnetIds": ["subnet-1", "subnet-2"]
        }
      }
    }
```

To allow users to access specific security groups, use `StringEquals` to check the value of the `lambda:SecurityGroupIds` condition\. The following example allows users to access `sg-1` and `sg-2`\.

```
{
      "Sid": "EnforceStayInSpecificSecurityGroup",
      "Action": [
          "lambda:CreateFunction",
          "lambda:UpdateFunctionConfiguration"
       ],
      "Effect": "Allow",
      "Resource": "*",
      "Condition": {
        "ForAllValues:StringEquals": {
            "lambda:SecurityGroupIds": ["sg-1", "sg-2"]
        }
      }
    }
  ]
}
```

## Internet and service access for VPC\-connected functions<a name="vpc-internet"></a>

By default, Lambda runs your functions in a secure VPC with access to AWS services and the internet\. Lambda owns this VPC, which isn't connected to your account's [default VPC](https://docs.aws.amazon.com/vpc/latest/userguide/default-vpc.html)\. When you connect a function to a VPC in your account, the function can't access the internet unless your VPC provides access\.

**Note**  
Several AWS services offer [VPC endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html)\. You can use VPC endpoints to connect to AWS services from within a VPC without internet access\.

Internet access from a private subnet requires network address translation \(NAT\)\. To give your function access to the internet, route outbound traffic to a [NAT gateway](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html) in a public subnet\. The NAT gateway has a public IP address and can connect to the internet through the VPC's internet gateway\. An idle NAT gateway connection will [ time out after 350 seconds\.](https://docs.aws.amazon.com/vpc/latest/userguide/nat-gateway-troubleshooting.html#nat-gateway-troubleshooting-timeout) For more information, see [How do I give internet access to my Lambda function in a VPC?](https://aws.amazon.com/premiumsupport/knowledge-center/internet-access-lambda-function/)

## VPC tutorials<a name="vpc-tutorials"></a>

In the following tutorials, you connect a Lambda function to resources in your VPC\.
+ [Tutorial: Configuring a Lambda function to access Amazon RDS in an Amazon VPC](services-rds-tutorial.md)
+ [Tutorial: Configuring a Lambda function to access Amazon ElastiCache in an Amazon VPC](services-elasticache-tutorial.md)

## Sample VPC configurations<a name="vpc-samples"></a>

You can use the following sample AWS CloudFormation templates to create VPC configurations to use with Lambda functions\. There are two templates available in this guide's GitHub repository:
+ [vpc\-private\.yaml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/main/templates/vpc-private.yaml) – A VPC with two private subnets and VPC endpoints for Amazon Simple Storage Service \(Amazon S3\) and Amazon DynamoDB\. Use this template to create a VPC for functions that don't need internet access\. This configuration supports use of Amazon S3 and DynamoDB with the AWS SDKs, and access to database resources in the same VPC over a local network connection\.
+ [vpc\-privatepublic\.yaml](https://github.com/awsdocs/aws-lambda-developer-guide/blob/main/templates/vpc-privatepublic.yaml) – A VPC with two private subnets, VPC endpoints, a public subnet with a NAT gateway, and an internet gateway\. Internet\-bound traffic from functions in the private subnets is routed to the NAT gateway using a route table\.

To create a VPC using a template, on the AWS CloudFormation console [Stacks page](https://console.aws.amazon.com/cloudformation/home#/stacks), choose **Create stack**, and then follow the instructions in the **Create stack** wizard\.
