# Tutorial: Configuring a Lambda function to access Amazon ElastiCache in an Amazon VPC<a name="services-elasticache-tutorial"></a>

In this tutorial, you do the following:
+ Create an Amazon ElastiCache cluster in your default Amazon Virtual Private Cloud\. For more information about Amazon ElastiCache, see [Amazon ElastiCache](https://aws.amazon.com/elasticache/)\.
+ Create a Lambda function to access the ElastiCache cluster\. When you create the Lambda function, you provide subnet IDs in your Amazon VPC and a VPC security group to allow the Lambda function to access resources in your VPC\. For illustration in this tutorial, the Lambda function generates a UUID, writes it to the cache, and retrieves it from the cache\.
+ Invoke the Lambda function and verify that it accessed the ElastiCache cluster in your VPC\.

For details on using Lambda with Amazon VPC, see [Configuring a Lambda function to access resources in a VPC](configuration-vpc.md)\.

## Prerequisites<a name="vpc-ec-prereqs"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Create a Lambda function with the console](getting-started.md#getting-started-create-function) to create your first Lambda function\.

To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

```
aws --version
```

You should see the following output:

```
aws-cli/2.0.57 Python/3.7.4 Darwin/19.6.0 exe/x86_64
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\.

**Note**  
On Windows, some Bash CLI commands that you commonly use with Lambda \(such as `zip`\) are not supported by the operating system's built\-in terminals\. To get a Windows\-integrated version of Ubuntu and Bash, [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. 

## Create the execution role<a name="vpc-ec-create-iam-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – Lambda\.
   + **Permissions** – **AWSLambdaVPCAccessExecutionRole**\.
   + **Role name** – **lambda\-vpc\-role**\.

The **AWSLambdaVPCAccessExecutionRole** has the permissions that the function needs to manage network connections to a VPC\.

## Create an ElastiCache cluster<a name="vpc-ec-create-ec-cluster"></a>

Create an ElastiCache cluster in your default VPC\.

1. Run the following AWS CLI command to create a Memcached cluster\. 

   ```
   aws elasticache create-cache-cluster --cache-cluster-id ClusterForLambdaTest --cache-node-type cache.t3.medium --engine memcached --num-cache-nodes 1 --security-group-ids sg-0123a1b123456c1de
   ```

   You can look up the default VPC security group in the VPC console under **Security Groups**\. Your example Lambda function will add and retrieve an item from this cluster\.

1. Write down the configuration endpoint for the cache cluster that you launched\. You can get this from the Amazon ElastiCache console\. You will specify this value in your Lambda function code in the next section\.

## Create a deployment package<a name="vpc-ec-deployment-pkg"></a>

The following example Python code reads and writes an item to your ElastiCache cluster\. 

**Example app\.py**  

```
from __future__ import print_function
import time
import uuid
import sys
import socket
import elasticache_auto_discovery
from pymemcache.client.hash import HashClient

#elasticache settings
elasticache_config_endpoint = "your-elasticache-cluster-endpoint:port"
nodes = elasticache_auto_discovery.discover(elasticache_config_endpoint)
nodes = map(lambda x: (x[1], int(x[2])), nodes)
memcache_client = HashClient(nodes)

def handler(event, context):
    """
    This function puts into memcache and get from it.
    Memcache is hosted using elasticache
    """

    #Create a random UUID... this will be the sample element we add to the cache.
    uuid_inserted = uuid.uuid4().hex
    #Put the UUID to the cache.
    memcache_client.set('uuid', uuid_inserted)
    #Get item (UUID) from the cache.
    uuid_obtained = memcache_client.get('uuid')
    if uuid_obtained.decode("utf-8") == uuid_inserted:
        # this print should go to the CloudWatch Logs and Lambda console.
        print ("Success: Fetched value %s from memcache" %(uuid_inserted))
    else:
        raise Exception("Value is not the same as we put :(. Expected %s got %s" %(uuid_inserted, uuid_obtained))

    return "Fetched value from memcache: " + uuid_obtained.decode("utf-8")
```

**Dependencies**
+  [pymemcache](https://pypi.python.org/pypi/pymemcache) – The Lambda function code uses this library to create a `HashClient` object to set and get items from memcache\. 
+ [elasticache\-auto\-discovery](https://pypi.python.org/pypi/elasticache-auto-discovery) – The Lambda function uses this library to get the nodes in your Amazon ElastiCache cluster\.

Install dependencies with Pip and create a deployment package\. For instructions, see [Deploy Python Lambda functions with \.zip file archives](python-package.md)\.

## Create the Lambda function<a name="vpc-ec-upload-deployment-pkg"></a>

Create the Lambda function with the `create-function` command\.

```
aws lambda create-function --function-name AccessMemCache --timeout 30 --memory-size 1024 \
--zip-file fileb://function.zip --handler app.handler --runtime python3.8 \
--role arn:aws:iam::123456789012:role/lambda-vpc-role \
--vpc-config SubnetIds=subnet-0532bb6758ce7c71f,subnet-d6b7fda068036e11f,SecurityGroupIds=sg-0897d5f549934c2fb
```

You can find the subnet IDs and the default security group ID of your VPC from the VPC console\.

## Test the Lambda function<a name="vpc-ec-invoke-lambda-function"></a>

In this step, you invoke the Lambda function manually using the `invoke` command\. When the Lambda function runs, it generates a UUID and writes it to the ElastiCache cluster that you specified in your Lambda code\. The Lambda function then retrieves the item from the cache\.

1. Invoke the Lambda function with the `invoke` command\.

   ```
   aws lambda invoke --function-name AccessMemCache output.txt
   ```

1. Verify that the Lambda function executed successfully as follows:
   + Review the output\.txt file\.
   + Review the results in the AWS Lambda console\.
   + Verify the results in CloudWatch Logs\.

Now that you have created a Lambda function that accesses an ElastiCache cluster in your VPC, you can have the function invoked in response to events\. For information about configuring event sources and examples, see [Using AWS Lambda with other services](lambda-services.md)\.

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, then choose **Delete**\.

1. Choose **Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Select the execution role that you created\.

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

**To delete the ElastiCache cluster**

1. Open the [Memcached page](https://console.aws.amazon.com/elasticache/home#memcached:) of the ElastiCache console\.

1. Select the cluster you created\.

1. Choose **Actions**, **Delete**\.

1. Choose **Delete**\.