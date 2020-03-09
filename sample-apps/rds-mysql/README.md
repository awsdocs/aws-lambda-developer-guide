# RDS MySQL

This sample application runs SQL queries on a MySQL database. It uses a private VPC to connect to an Amazon Relational Database Service (Amazon RDS) database. The application also uses AWS Secrets Manager and AWS X-Ray.

![Architecture](/sample-apps/rds-mysql/images/sample-rdsmysql.png)

The function takes a event with the following structure:

```
{
  "query": "CREATE TABLE events ( id varchar(255), title varchar(255), timestamp BIGINT, entries varchar(32765));"
}
```

The function executes the query and logs the output.

The project source includes function code and supporting resources:

- dbadmin - A Node.js function that runs SQL commands for administrator use.
- lib - A Lambda layer with the npm modules used by the application's functions.
- events - JSON documents that can be used to test the application's functions.
- template.yml - An AWS CloudFormation template that creates the application.
- template-vpcrds.yml - A template that creates the VPC and Amazon RDS database instance.
- 1-create-bucket.sh, 3-deploy-vpc.sh, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.
- bin - Additional scripts. 

# Requirements

To deploy the sample application, you need the following tools:

- [Node.js 10 with npm](https://nodejs.org/en/download/releases/).
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

To run the sample application in AWS, you need permission to use Lambda and the following services.

- Amazon RDS ([pricing](https://aws.amazon.com/rds/pricing/))
- Amazon VPC ([pricing](https://aws.amazon.com/vpc/pricing/))
- AWS Secrets Manager ([pricing](https://aws.amazon.com/secrets-manager/pricing/))
- AWS X-Ray ([pricing](https://aws.amazon.com/xray/pricing/))
- AWS Identity and Access Management
- AWS CloudFormation

Standard charges apply for each service.

# Setup

Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/rds-mysql

Run `create-bucket.sh` to create a new bucket for deployment artifacts. Or, if you already have a bucket, create a file named `bucket-name.txt` that contains the name of your bucket.

    rds-mysql$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

Run the `2-create-dbpasswordsecret.sh` script to create a database password and store it in AWS Secrets Manager.

    rds-mysql$ ./2-create-dbpasswordsecret.sh

Run the `3-deploy-vpc.sh` script to create the VPC and RDS database instance. This process takes about 15 minutes.

    rds-mysql$ ./3-deploy-vpc.sh

# Deploy

Run `4-deploy.sh` to deploy the application.

    rds-mysql$ ./4-deploy.sh
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2678 / 2678.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - rds-mysql

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

Run `5-create-dbtable.sh` to create a table in the database.

    rds-mysql$ ./5-create-dbtable.sh

# Test

To invoke the function with a test event, use the invoke script.

    rds-mysql$ ./6-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

The functions in this application are instrumented with AWS X-Ray. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the function calling the database to run a query.

![Service Map](/sample-apps/rds-mysql/images/rdsmysql-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/rds-mysql/images/rdsmysql-trace.png)

Finally, view the application in the Lambda console.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **rds-mysql**.

  ![Application](/sample-apps/rds-mysql/images/rdsmysql-application.png)

# Cleanup

To delete the application, run the cleanup script.

    rds-mysql$ ./8-cleanup.sh
