# Configuring database access for a Lambda function<a name="configuration-database"></a>

You can create an Amazon RDS Proxy database proxy for your function\. A database proxy manages a pool of database connections and relays queries from a function\. This enables a function to reach high [concurrency](gettingstarted-concepts.md#gettingstarted-concepts-concurrency) levels without exhausting database connections\.

**Topics**
+ [Creating a database proxy \(console\)](#configuration-database-config)
+ [Using the function's permissions for authentication](#configuration-database-auth)
+ [Sample application](#configuration-database-sample)

## Creating a database proxy \(console\)<a name="configuration-database-config"></a>

You can use the Lambda console to create an Amazon RDS Proxy database proxy\. 

**To create a database proxy**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Choose **Configuration** and then choose **Database proxies**\.

1. Choose **Add database proxy**\.

1. Configure the following options\.
   + **Proxy identifier** – The name of the proxy\.
   + **RDS DB instance** – A [supported MySQL or PostgreSQL](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/rds-proxy.html#rds-proxy.limitations) DB instance or cluster\.
   + **Secret** – A Secrets Manager secret with the database user name and password\.  
**Example secret**  

     ```
     {
         "username": "admin",
         "password": "e2abcecxmpldc897"
       }
     ```
   + **IAM role** – An IAM role with permission to use the secret, and a trust policy that allows Amazon RDS to assume the role\.
   + **Authentication** – The authentication and authorization method for connecting to the proxy from your function code\.

1. Choose **Add**\.

**Pricing**  
Amazon RDS charges a hourly price for proxies that is determined by the instance size of your database\. For details, see [RDS Proxy pricing](https://aws.amazon.com/rds/proxy/pricing/)\.

Proxy creation takes a few minutes\. When the proxy is available, configure your function to connect to the proxy endpoint instead of the database endpoint\.

Standard [Amazon RDS Proxy pricing](https://aws.amazon.com/rds/proxy/pricing/) applies\. For more information, see [Managing connections with the Amazon RDS Proxy](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/rds-proxy.html) in the Amazon Aurora User Guide\.

## Using the function's permissions for authentication<a name="configuration-database-auth"></a>

By default, you can connect to a proxy with the same username and password that it uses to connect to the database\. The only difference in your function code is the endpoint that the database client connects to\. The drawback of this method is that you must expose the password to your function code, either by configuring it in a secure environment variable or by retrieving it from Secrets Manager\.

You can create a database proxy that uses the function's IAM credentials for authentication and authorization instead of a password\. To use the function's permissions to connect to the proxy, set **Authentication** to **Execution role**\.

The Lambda console adds the required permission \(`rds-db:connect`\) to the execution role\. You can then use the AWS SDK to generate a token that allows it to connect to the proxy\. The following example shows how to configure a database connection with the `mysql2` library in Node\.js\.

**Example [dbadmin/index\-iam\.js](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql/dbadmin/index-iam.js) – AWS SDK signer**  

```
const signer = new AWS.RDS.Signer({
  region: region,
  hostname: host,
  port: sqlport,
  username: username
})

exports.handler = async (event) => {
  let connectionConfig = {
    host     : host,
    user     : username,
    database : database,
    ssl: 'Amazon RDS',
    authPlugins: { mysql_clear_password: () => () => signer.getAuthToken() }
  }
  var connection = mysql.createConnection(connectionConfig)
  var query = event.query
  var result
  connection.connect()
}
```

For more information, see [IAM database authentication](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.html) in the Amazon RDS User Guide\.

## Sample application<a name="configuration-database-sample"></a>

Sample applications that demonstrate the use of Lambda with an Amazon RDS database are available in this guide's GitHub repository\. There are two applications:
+ [RDS MySQL](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql) – The AWS CloudFormation template `template-vpcrds.yml` creates a MySQL 5\.7 database in a private VPC\. In the sample application, a Lambda function proxies queries to the database\. The function and database templates both use Secrets Manager to access database credentials\.

  [https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql)
+ [List Manager](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/list-manager) – A processor function reads events from a Kinesis stream\. It uses the data from the events to update DynamoDB tables, and stores a copy of the event in a MySQL database\.

  [https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/list-manager](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/list-manager)

To use the sample applications, follow the instructions in the GitHub repository: [RDS MySQL](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql/README.md), [List Manager](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/list-manager/README.md)\.