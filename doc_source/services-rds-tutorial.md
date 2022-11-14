# Tutorial: Configuring a Lambda function to access Amazon RDS in an Amazon VPC<a name="services-rds-tutorial"></a>

In this tutorial, you do the following:
+ Launch an Amazon RDS MySQL database engine instance in your default Amazon VPC\. In the MySQL instance, you create a database \(ExampleDB\) with a sample table \(Employee\) in it\. For more information about Amazon RDS, see [Amazon RDS](https://aws.amazon.com/rds)\.
+ Create a Lambda function to access the ExampleDB database, create a table \(Employee\), add a few records, and retrieve the records from the table\.
+ Invoke the Lambda function and verify the query results\. This is how you verify that your Lambda function was able to access the RDS MySQL instance in the VPC\.

For details on using Lambda with Amazon VPC, see [Configuring a Lambda function to access resources in a VPC](configuration-vpc.md)\.

## Prerequisites<a name="vpc-rds-prereqs"></a>

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

## Create the execution role<a name="vpc-rds-create-iam-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – Lambda\.
   + **Permissions** – **AWSLambdaVPCAccessExecutionRole**\.
   + **Role name** – **lambda\-vpc\-role**\.

The **AWSLambdaVPCAccessExecutionRole** has the permissions that the function needs to manage network connections to a VPC\.

## Create an Amazon RDS database instance<a name="vpc-rds-create-rds-mysql"></a>

In this tutorial, the example Lambda function creates a table \(Employee\), inserts a few records, and then retrieves the records\. The table that the Lambda function creates has the following schema:

```
Employee(EmpID, Name)
```

Where `EmpID` is the primary key\. Now, you need to add a few records to this table\.

First, you launch an RDS MySQL instance in your default VPC with ExampleDB database\. If you already have an RDS MySQL instance running in your default VPC, skip this step\.

You can launch an RDS MySQL instance using one of the following methods:
+ Follow the instructions at [Creating a MySQL DB instance and connecting to a database on a MySQL DB instance](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html) in the *Amazon RDS User Guide*\.
+ Use the following AWS CLI command:

  ```
  aws rds create-db-instance --db-name ExampleDB --engine MySQL \
  --db-instance-identifier MySQLForLambdaTest --backup-retention-period 3 \
  --db-instance-class db.t2.micro --allocated-storage 5 --no-publicly-accessible \
  --master-username username --master-user-password password
  ```

Write down the database name, user name, and password\. You also need the host address \(endpoint\) of the DB instance, which you can get from the RDS console\. You might need to wait until the instance status is available and the Endpoint value appears in the console\.

## Create a deployment package<a name="vpc-rds-deployment-pkg"></a>

The following example Python code runs a SELECT query against the Employee table in the MySQL RDS instance that you created in the VPC\. The code creates a table in the ExampleDB database, adds sample records, and retrieves those records\.

The following method for handling database credentials is for illustrative purposes only\. In a production environment, we recommend using AWS Secrets Manager instead of environment variables to store database credentials\. For more information, see [Configuring database access for a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/configuration-database.html)\.

**Example app\.py**  

```
import sys
import logging
import rds_config
import pymysql
#rds settings
rds_host  = "rds-instance-endpoint"
name = rds_config.db_username
password = rds_config.db_password
db_name = rds_config.db_name

logger = logging.getLogger()
logger.setLevel(logging.INFO)

try:
    conn = pymysql.connect(host=rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)
except pymysql.MySQLError as e:
    logger.error("ERROR: Unexpected error: Could not connect to MySQL instance.")
    logger.error(e)
    sys.exit()

logger.info("SUCCESS: Connection to RDS MySQL instance succeeded")
def handler(event, context):
    """
    This function fetches content from MySQL RDS instance
    """

    item_count = 0

    with conn.cursor() as cur:
        cur.execute("create table Employee ( EmpID  int NOT NULL, Name varchar(255) NOT NULL, PRIMARY KEY (EmpID))")
        cur.execute('insert into Employee (EmpID, Name) values(1, "Joe")')
        cur.execute('insert into Employee (EmpID, Name) values(2, "Bob")')
        cur.execute('insert into Employee (EmpID, Name) values(3, "Mary")')
        conn.commit()
        cur.execute("select * from Employee")
        for row in cur:
            item_count += 1
            logger.info(row)
            #print(row)
    conn.commit()

    return "Added %d items from RDS MySQL table" %(item_count)
```

Executing `pymysql.connect()` outside of the handler allows your function to re\-use the database connection for better performance\.

A second file contains connection information for the function\.

**Example rds\_config\.py**  

```
#config file containing credentials for RDS MySQL instance
db_username = "username"
db_password = "password"
db_name = "ExampleDB"
```

A deployment package is a \.zip file containing your Lambda function code and dependencies\. The sample function code has the following dependencies:

**Dependencies**
+ `pymysql` – The Lambda function code uses this library to access your MySQL instance \(see [PyMySQL](https://pypi.python.org/pypi/PyMySQL)\) \.

**To create a deployment package**
+ Install dependencies with Pip and create a deployment package\. For instructions, see [Deploy Python Lambda functions with \.zip file archives](python-package.md)\.

## Create the Lambda function<a name="vpc-rds-upload-deployment-pkg"></a>

Create the Lambda function with the `create-function` command\. You can find the subnet IDs and security group ID for your default VPC in the [Amazon VPC console](https://console.aws.amazon.com/vpc)\.

```
aws lambda create-function --function-name  CreateTableAddRecordsAndRead --runtime python3.8 \
--zip-file fileb://app.zip --handler app.handler \
--role arn:aws:iam::123456789012:role/lambda-vpc-role \
--vpc-config SubnetIds=subnet-0532bb6758ce7c71f,subnet-d6b7fda068036e11f,SecurityGroupIds=sg-0897d5f549934c2fb
```

## Test the Lambda function<a name="vpc-rds-invoke-lambda-function"></a>

In this step, you invoke the Lambda function manually using the `invoke` command\. When the Lambda function runs, it runs the SELECT query against the Employee table in the RDS MySQL instance and prints the results, which also go to the CloudWatch Logs\.

1. Invoke the Lambda function with the `invoke` command\. 

   ```
   aws lambda invoke --function-name CreateTableAddRecordsAndRead output.txt
   ```

1. Verify that the Lambda function executed successfully as follows:
   + Review the output\.txt file\.
   + Review the results in the AWS Lambda console\.
   + Verify the results in CloudWatch Logs\.

Now that you have created a Lambda function that accesses a database in your VPC, you can have the function invoked in response to events\. For information about configuring event sources and examples, see [Using AWS Lambda with other services](lambda-services.md)\.

## Clean up your resources<a name="rds-tutorial-cleanup"></a>

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

**To delete the MySQL DB instance**

1. Open the [Databases page](https://console.aws.amazon.com/rds/home#databases:) of the Amazon RDS console\.

1. Select the database you created\.

1. Choose **Actions**, **Delete**\.

1. Clear the **Create final snapshot** check box\.

1. Enter **delete me** in the text box\.

1. Choose **Delete**\.