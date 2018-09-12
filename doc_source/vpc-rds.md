# Tutorial: Configuring a Lambda Function to Access Amazon RDS in an Amazon VPC<a name="vpc-rds"></a>

In this tutorial, you do the following:
+ Launch an Amazon RDS MySQL database engine instance in your default Amazon VPC\. In the MySQL instance, you create a database \(ExampleDB\) with a sample table \(Employee\) in it\. For more information about Amazon RDS, see [Amazon RDS](https://aws.amazon.com/rds)\.
+ Create a Lambda function to access the ExampleDB database, create a table \(Employee\), add a few records, and retrieve the records from the table\.
+ Invoke the Lambda function manually and verify the query results\. This is how you verify that your Lambda function was able to access the RDS MySQL instance in the VPC\.

**Important**  
This tutorial uses the default Amazon VPC in the us\-east\-1 region in your account\. For more information about Amazon VPC, see [How to Get Started with Amazon VPC](http://docs.aws.amazon.com/vpc/latest/userguide/VPC_Introduction.html#howto) in the *Amazon VPC User Guide*\. 

**Next Step**  
[Step 1: Create an Amazon RDS MySQL Instance and ExampleDB Database](vpc-rds-create-rds-mysql.md)