# Configuring file system access for Lambda functions<a name="configuration-filesystem"></a>

You can configure a function to mount an Amazon Elastic File System \(Amazon EFS\) file system to a local directory\. With Amazon EFS, your function code can access and modify shared resources safely and at high concurrency\.

**Topics**
+ [Execution role and user permissions](#configuration-filesystem-permissions)
+ [Configuring a file system and access point](#configuration-filesystem-setup)
+ [Connecting to a file system \(console\)](#configuration-filesystem-config)
+ [Configuring file system access with the Lambda API](#configuration-filesystem-api)
+ [AWS CloudFormation and AWS SAM](#configuration-filesystem-cloudformation)
+ [Sample applications](#configuration-filesystem-samples)

## Execution role and user permissions<a name="configuration-filesystem-permissions"></a>

If the file system doesn't have a user\-configured IAM policy, EFS uses a default policy that grants full access to any client that can connect to the file system using a file system mount target\. If the file system has a user\-configured IAM policy, your function's execution role must have the correct `elasticfilesystem` permissions\.

**Execution role permissions**
+ **elasticfilesystem:ClientMount**
+ **elasticfilesystem:ClientWrite \(not required for read\-only connections\)**

These permissions are included in the **AmazonElasticFileSystemClientReadWriteAccess** managed policy\. Additionally, your execution role must have the [permissions required to connect to the file system's VPC](configuration-vpc.md#vpc-permissions)\.

When you configure a file system, Lambda uses your permissions to verify mount targets\. To configure a function to connect to a file system, your IAM user needs the following permissions:

**User permissions**
+ **elasticfilesystem:DescribeMountTargets**

## Configuring a file system and access point<a name="configuration-filesystem-setup"></a>

Create a file system in Amazon EFS with a mount target in every Availability Zone that your function connects to\. For performance and resilience, use at least two Availability Zones\. For example, in a simple configuration you could have a VPC with two private subnets in separate Availability Zones\. The function connects to both subnets and a mount target is available in each\. Ensure that NFS traffic \(port 2049\) is allowed by the security groups used by the function and mount targets\.

**Note**  
When you create a file system, you choose a performance mode that can't be changed later\. **General purpose** mode has lower latency, and **Max I/O** mode supports a higher maximum throughput and IOPS\. For help choosing, see [Amazon EFS performance](https://docs.aws.amazon.com/efs/latest/ug/performance.html) in the *Amazon Elastic File System User Guide*\.

An access point connects each instance of the function to the right mount target for the Availability Zone it connects to\. For best performance, create an access point with a non\-root path, and limit the number of files that you create in each directory\. The following example creates a directory named `my-function` on the file system and sets the owner ID to 1001 with standard directory permissions \(755\)\.

**Example access point configuration**  
+ **Name** – `files`
+ **User ID** – `1001`
+ **Group ID** – `1001`
+ **Path** – `/my-function`
+ **Permissions** – `755`
+ **Owner user ID** – `1001`
+ **Group user ID** – `1001`

When a function uses the access point, it is given user ID 1001 and has full access to the directory\.

For more information, see the following topics in the *Amazon Elastic File System User Guide*:
+ [Creating resources for Amazon EFS](https://docs.aws.amazon.com/efs/latest/ug/creating-using.html)
+ [Working with users, groups, and permissions](https://docs.aws.amazon.com/efs/latest/ug/accessing-fs-nfs-permissions.html)

## Connecting to a file system \(console\)<a name="configuration-filesystem-config"></a>

A function connects to a file system over the local network in a VPC\. The subnets that your function connects to can be the same subnets that contain mount points for your file system, or subnets in the same Availability Zone that can route NFS traffic \(port 2049\) to the file system\.

**Note**  
If your function is not already connected to a VPC, see [Configuring a Lambda function to access resources in a VPC](configuration-vpc.md)\.

**To configure file system access**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **File systems**\.

1. Under **File system**, choose **Add file system**\.

1. Configure the following properties:
   + **EFS file system** – The access point for a file system in the same VPC\.
   + **Local mount path** – The location where the file system is mounted on the Lambda function, starting with `/mnt/`\.

**Pricing**  
Amazon EFS charges for storage and throughput, with rates that vary by storage class\. For details, see [Amazon EFS pricing](https://aws.amazon.com/efs/pricing)\.  
Lambda charges for data transfer between VPCs\. This only applies if your function's VPC is peered to another VPC with a file system\. The rates are the same as for Amazon EC2 data transfer between VPCs in the same Region\. For details, see [Lambda pricing](https://aws.amazon.com/lambda/pricing)\.

For more information about Lambda's integration with Amazon EFS, see [Using Amazon EFS with Lambda](services-efs.md)\.

## Configuring file system access with the Lambda API<a name="configuration-filesystem-api"></a>

Use the following API operations to connect your Lambda function to a file system:
+ [CreateFunction](API_CreateFunction.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

To connect a function to a file system, use the `update-function-configuration` command\. The following example connects a function named `my-function` to a file system with ARN of an access point\.

```
ARN=arn:aws:elasticfilesystem:us-east-2:123456789012:access-point/fsap-015cxmplb72b405fd
aws lambda update-function-configuration --function-name my-function \
      --file-system-configs Arn=$ARN,LocalMountPath=/mnt/efs0
```

You can get the ARN of a file system's access point with the `describe-access-points` command\.

```
aws efs describe-access-points
```

You should see the following output:

```
{
    "AccessPoints": [
        {
            "ClientToken": "console-aa50c1fd-xmpl-48b5-91ce-57b27a3b1017",
            "Name": "lambda-ap",
            "Tags": [
                {
                    "Key": "Name",
                    "Value": "lambda-ap"
                }
            ],
            "AccessPointId": "fsap-015cxmplb72b405fd",
            "AccessPointArn": "arn:aws:elasticfilesystem:us-east-2:123456789012:access-point/fsap-015cxmplb72b405fd",
            "FileSystemId": "fs-aea3xmpl",
            "RootDirectory": {
                "Path": "/"
            },
            "OwnerId": "123456789012",
            "LifeCycleState": "available"
        }
    ]
}
```

## AWS CloudFormation and AWS SAM<a name="configuration-filesystem-cloudformation"></a>

You can use AWS CloudFormation and the AWS Serverless Application Model \(AWS SAM\) to automate the creation of Lambda applications\. To enable a file system connection on an AWS SAM `AWS::Serverless::Function` resource, use the `FileSystemConfigs` property\.

**Example template\.yml – File system configuration**  

```
Transform: AWS::Serverless-2016-10-31
Resources:
  VPC:
    Type: AWS::EC2::VPC
    Properties:
      CidrBlock: 10.0.0.0/16
  Subnet1:
    Type: AWS::EC2::Subnet
    Properties:
      VpcId:
        Ref: VPC
      CidrBlock: 10.0.1.0/24
      AvailabilityZone: "us-west-2a"
  EfsSecurityGroup:
    Type: AWS::EC2::SecurityGroup
    Properties:
      VpcId:
        Ref: VPC
      GroupDescription: "mnt target sg"
      SecurityGroupIngress:
      - IpProtocol: -1
        CidrIp: "0.0.0.0/0"
  FileSystem:
    Type: AWS::EFS::FileSystem
    Properties:
      PerformanceMode: generalPurpose
  AccessPoint:
    Type: AWS::EFS::AccessPoint
    Properties:
      FileSystemId:
        Ref: FileSystem
      PosixUser:
        Uid: "1001"
        Gid: "1001"
      RootDirectory:
        CreationInfo:
          OwnerGid: "1001"
          OwnerUid: "1001"
          Permissions: "755"
  MountTarget1:
    Type: AWS::EFS::MountTarget
    Properties:
      FileSystemId:
        Ref: FileSystem
      SubnetId:
        Ref: Subnet1
      SecurityGroups:
      - Ref: EfsSecurityGroup
  MyFunctionWithEfs:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      Handler: index.handler
      Runtime: python3.9
      VpcConfig:
        SecurityGroupIds:
        - Ref: EfsSecurityGroup
        SubnetIds:
        - Ref: Subnet1
      FileSystemConfigs:
      - Arn: !GetAtt AccessPoint.Arn
        LocalMountPath: "/mnt/efs"
      Description: Use a file system.
    DependsOn: "MountTarget1"
```

You must add the `DependsOn` to ensure that the mount targets are fully created before the Lambda runs for the first time\.

For the AWS CloudFormation `AWS::Lambda::Function` type, the property name and fields are the same\. For more information, see [Using AWS Lambda with AWS CloudFormation](services-cloudformation.md)\.

## Sample applications<a name="configuration-filesystem-samples"></a>

The GitHub repository for this guide includes a sample application that demonstrates the use of Amazon EFS with a Lambda function\.
+ [efs\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/efs-nodejs) – A function that uses an Amazon EFS file system in a Amazon VPC\. This sample includes a VPC, file system, mount targets, and access point configured for use with Lambda\.