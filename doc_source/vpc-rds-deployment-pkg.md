# Step 2\.1: Create a Deployment Package<a name="vpc-rds-deployment-pkg"></a>

The following example Python code runs a SELECT query against the Employee table in the MySQL RDS instance that you created in the VPC\. The code creates a table in the ExampleDB database, adds sample records, and retrieves those records\. 

1. Open a text editor, and then copy the following code\.

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
       logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
       sys.exit()
   
   logger.info("SUCCESS: Connection to RDS mysql instance succeeded")
   def handler(event, context):
       """
       This function fetches content from mysql RDS instance
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
**Note**  
We recommend that `pymysql.connect()` is executed outside the handler, as shown, for better performance\. 

1. Save the file as `app.py`\. 

1. Install the following library dependencies using **pip**:
   + `pymysql` – The Lambda function code uses this library to access your MySQL instance \(see [PyMySQL](https://pypi.python.org/pypi/PyMySQL)\) \.

1. Create a config file that contains the following information and save it as `rds_config.py`:

   ```
   #config file containing credentials for rds mysql instance
   db_username = "username"
   db_password = "password"
   db_name = "databasename"
   ```

1. Zip all of these files into a file named `app.zip` to create your deployment package\. For step\-by\-step instructions, see [Creating a Deployment Package \(Python\)](lambda-python-how-to-create-deployment-package.md)\. 