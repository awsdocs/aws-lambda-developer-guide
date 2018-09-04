# Step 1: Create an Amazon RDS MySQL Instance and ExampleDB Database<a name="vpc-rds-create-rds-mysql"></a>

In this tutorial, the example Lambda function creates a table \(Employee\), inserts a few records, and then retrieves the records\. The table that the Lambda function creates has the following schema:

```
Employee(EmpID, Name)
```

Where `EmpID` is the primary key\. Now, you need to add a few records to this table\.

First, you launch an RDS MySQL instance in your default VPC with ExampleDB database\. If you already have an RDS MySQL instance running in your default VPC, skip this step\.

**Important**  
This tutorial uses the RDS MySQL DB engine launched in the default VPC in the us\-east\-1 region\.

You can launch an RDS MySQL instance using one of the following methods:
+ Follow the instructions at [Creating a MySQL DB Instance and Connecting to a Database on a MySQL DB Instance](http://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html) in the *Amazon RDS User Guide*\.
+ Use the following AWS CLI command:

  ```
  $ aws rds create-db-instance \
      --db-instance-identifier MySQLForLambdaTest \
      --db-instance-class db.t2.micro \
      --engine MySQL \
      --allocated-storage 5 \
      --no-publicly-accessible \
      --db-name ExampleDB \
      --master-username username \
      --master-user-password password \
      --backup-retention-period 3
  ```

Write down the database name, user name, and password\. You also need the host address \(endpoint\) of the DB instance, which you can get from the RDS console \(you might need to wait until the instance status is available and the Endpoint value appears in the console\)\.

**Next Step**  
[Step 2: Create a Lambda Function](vpc-rds-create-lambda-function.md)