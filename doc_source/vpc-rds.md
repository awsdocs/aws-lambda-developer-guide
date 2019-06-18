# Tutorial: Configuring a Lambda Function to Access Amazon RDS in an Amazon VPC<a name="vpc-rds"></a>

In this tutorial, you do the following:
+ Launch an Amazon RDS MySQL database engine instance in your default Amazon VPC\. In the MySQL instance, you create a database \(ExampleDB\) with a sample table \(Employee\) in it\. For more information about Amazon RDS, see [Amazon RDS](https://aws.amazon.com/rds)\.
+ Create a Lambda function to access the ExampleDB database, create a table \(Employee\), add a few records, and retrieve the records from the table\.
+ Invoke the Lambda function and verify the query results\. This is how you verify that your Lambda function was able to access the RDS MySQL instance in the VPC\.

## Prerequisites<a name="vpc-rds-prereqs"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting Started with AWS Lambda](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

## Create the Execution Role<a name="vpc-rds-create-iam-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – Lambda\.
   + **Permissions** – **AWSLambdaVPCAccessExecutionRole**\.
   + **Role name** – **lambda\-vpc\-role**\.

The **AWSLambdaVPCAccessExecutionRole** has the permissions that the function needs to manage network connections to a VPC\.

## Create an Amazon RDS Database Instance<a name="vpc-rds-create-rds-mysql"></a>

In this tutorial, the example Lambda function creates a table \(Employee\), inserts a few records, and then retrieves the records\. The table that the Lambda function creates has the following schema:

```
Employee(EmpID, Name)
```

Where `EmpID` is the primary key\. Now, you need to add a few records to this table\.

First, you launch an RDS MySQL instance in your default VPC with ExampleDB database\. If you already have an RDS MySQL instance running in your default VPC, skip this step\.

You can launch an RDS MySQL instance using one of the following methods:
+ Follow the instructions at [Creating a MySQL DB Instance and Connecting to a Database on a MySQL DB Instance](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html) in the *Amazon RDS User Guide*\.
+ Use the following AWS CLI command:

  ```
  $ aws rds create-db-instance --db-name ExampleDB --engine MySQL \
  --db-instance-identifier MySQLForLambdaTest --backup-retention-period 3 \
  --db-instance-class db.t2.micro --allocated-storage 5 --no-publicly-accessible \
  --master-username username --master-user-password password
  ```

Write down the database name, user name, and password\. You also need the host address \(endpoint\) of the DB instance, which you can get from the RDS console\. You might need to wait until the instance status is available and the Endpoint value appears in the console\.

## Create a Deployment Package<a name="vpc-rds-deployment-pkg"></a>

The following example Python code runs a SELECT query against the Employee table in the MySQL RDS instance that you created in the VPC\. The code creates a table in the ExampleDB database, adds sample records, and retrieves those records\. 

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
    conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)
except:
    logger.error("ERROR: Unexpected error: Could not connect to MySQL instance.")
    sys.exit()

logger.info("SUCCESS: Connection to RDS MySQL instance succeeded")
def handler(event, context):
    """
    This function fetches content from MySQL RDS instance
    """

    item_count = 0

    with conn.cursor() as cur:
        cur.execute("create table Employee3 ( EmpID  int NOT NULL, Name varchar(255) NOT NULL, PRIMARY KEY (EmpID))")  
        cur.execute('insert into Employee3 (EmpID, Name) values(1, "Joe")')
        cur.execute('insert into Employee3 (EmpID, Name) values(2, "Bob")')
        cur.execute('insert into Employee3 (EmpID, Name) values(3, "Mary")')
        conn.commit()
        cur.execute("select * from Employee3")
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

**Dependencies**
+ `pymysql` – The Lambda function code uses this library to access your MySQL instance \(see [PyMySQL](https://pypi.python.org/pypi/PyMySQL)\) \.

Install dependencies with Pip and create a deployment package\. For instructions, see [AWS Lambda Deployment Package in Python](lambda-python-how-to-create-deployment-package.md)\.

## Create the Lambda Function<a name="vpc-rds-upload-deployment-pkg"></a>

Create the Lambda function with the `create-function` command\.

```
$ aws lambda create-function --function-name  CreateTableAddRecordsAndRead --runtime python3.7 \
--zip-file fileb://app.zip --handler app.handler \
--role execution-role-arn \
--vpc-config SubnetIds=comma-separated-subnet-ids,SecurityGroupIds=default-vpc-security-group-id
```

## Test the Lambda Function<a name="vpc-rds-invoke-lambda-function"></a>

In this step, you invoke the Lambda function manually using the `invoke` command\. When the Lambda function executes, it runs the SELECT query against the Employee table in the RDS MySQL instance and prints the results, which also go to the CloudWatch Logs\.

1. Invoke the Lambda function with the `invoke` command\. 

   ```
   $ aws lambda invoke --function-name CreateTableAddRecordsAndRead output.txt
   ```

1. Verify that the Lambda function executed successfully as follows:
   + Review the output\.txt file\.
   + Review the results in the AWS Lambda console\.
   + Verify the results in CloudWatch Logs\.

Now that you have created a Lambda function that accesses a database in your VPC, you can have the function invoked in response to events\. For information about configuring event sources and examples, see [Using AWS Lambda with Other Services](lambda-services.md)\.