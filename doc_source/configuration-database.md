# Configuring Database Access for a Lambda Function<a name="configuration-database"></a>

You can use the Lambda console to create an Amazon Relational Database Service \(Amazon RDS\) database proxy for your function\. A database proxy manages a pool of database connections and relays queries from a function\. This enables a function to reach high [concurrency](gettingstarted-concepts.md#gettingstarted-concepts-concurrency) levels without exhausting database connections\.

**To create a database proxy**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Choose **Add database proxy**\.

1. Configure the following options\.
   + **Proxy identifier** – The name of the proxy\.
   + **RDS DB instance** – A MySQL 5\.6 or MySQL 5\.7 DB instance or cluster\.
   + **Secret** – A Secrets Manager secret with the database user name and password\.  
**Example Secret**  

     ```
     {
       "username": "admin",
       "password": "e2abcecxmpldc897"
     }
     ```
   + **IAM role** – An IAM role with permission to use the secret, and a trust policy that allows Amazon RDS to assume the role\.

1. Choose **Add**\.

Proxy creation takes a few minutes\. When the proxy is available, configure your function to connect to the proxy endpoint instead of the database endpoint\.

For more information, see [Managing Connections with the Amazon RDS Proxy](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/rds-proxy.html) in the Amazon Aurora User Guide\.

## Sample Application<a name="configuration-database-sample"></a>

Sample applications that demonstrate the use of Lambda with an Amazon RDS database are available in this guide's GitHub repository\. There are two applications:
+ [RDS MySQL](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/rds-mysql) – The AWS CloudFormation template `template-vpcrds.yml` creates a MySQL 5\.7 database in a private VPC\. In the sample application, a Lambda function proxies queries to the database\. The function and database templates both use Secrets Manager to access database credentials\.

  [ ![\[The dbadmin function relays queries to a database.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-rdsmysql.png)](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/rds-mysql)
+ [List Manager](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/list-manager) – A processor function reads events from a Kinesis stream\. It uses the data from the events to update DynamoDB tables, and stores a copy of the event in a MySQL database\.

  [ ![\[The processor function processes Kinesis events and stores the events in a MySQL database.\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sample-listmanager.png)](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/list-manager)

To use the sample applications, follow the instructions in the GitHub repository: [RDS MySQL](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/rds-mysql/README.md), [List Manager](https://github.com/awsdocs/aws-lambda-developer-guide/blob/master/sample-apps/list-manager/README.md)\.