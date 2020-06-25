# Processing a Kinesis stream with database resources in a VPC

This sample application processes records from an Amazon Kinesis stream to create and update lists. It uses a private VPC to connect to an Amazon Relational Database Service (Amazon RDS) database. It uses a VPC endpoint to connect to Amazon DynamoDB. The application also uses AWS Secrets Manager, AWS X-Ray, and AWS CodeDeploy.

![Architecture](/sample-apps/list-manager/images/sample-listmanager.png)

The processor function doesn't read directly from the Kinesis stream. Instead, an AWS Lambda event source mapping reads records from the Kinesis stream and sends them to the processor function in batches. The processor function stores all incoming events in a MySQL database, and maintains the current state of each list in a DynamoDB table. 

The project source includes function code and supporting resources:

- `processor` - A Node.js function that processes records from a Kinesis stream.
- `dbadmin` - A Node.js function that runs SQL commands for administrator use.
- `lib` - A Lambda layer with the npm modules used by the application's functions.
- `events` - JSON documents that can be used to test the application's functions.
- `template.yml` - An AWS CloudFormation template that creates the application.
- `template-vpcrds.yml` - A template that creates the VPC and Amazon RDS database instance.
- `1-create-bucket.sh`, `2-create-dbpasswordsecret.sh`, and other scripts - Shell scripts that use the AWS CLI to deploy and manage the application.
- `bin` - Additional scripts. 

The processor supports the following types of lists:

## Ranking

A list of names with a rank or weight. For example:

  - Top 10 lists - Integer values 1-N.
  - Weighted rankings - Any number values.

Rankings are combined into an aggregate list. When a user updates their ranking, the change (added, removed, and moved entries) is applied to the aggregate list as well.

## Stats

A list of fields to increment. For example:

  - Expenses - Amounts spent on gasoline, meals out, and so on.
  - Character state - Stats such as health, experience points, and steps walked.

Use the following instructions to deploy the sample application. For more information on the application's architecture and implementation, see [List manager sample application for AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/samples-listmanager.html) in the developer guide.

# Requirements

To deploy the sample application, you need the following tools:

- [Node.js 10 with npm](https://nodejs.org/en/download/releases/).
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v1.17 or newer.

If you use the AWS CLI v2, add the following to your [configuration file](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html) (`~/.aws/config`):

```
cli_binary_format=raw-in-base64-out
```

This setting enables the AWS CLI v2 to load JSON events from a file, matching the v1 behavior.

To run the sample application in AWS, you need permission to use Lambda and the following services:

- Amazon RDS ([pricing](https://aws.amazon.com/rds/pricing/))
- Amazon Kinesis ([pricing](https://aws.amazon.com/kinesis/pricing/))
- Amazon DynamoDB ([pricing](https://aws.amazon.com/dynamodb/pricing/))
- Amazon VPC ([pricing](https://aws.amazon.com/vpc/pricing/))
- AWS Secrets Manager ([pricing](https://aws.amazon.com/secrets-manager/pricing/))
- AWS CodeDeploy ([pricing](https://aws.amazon.com/codedeploy/pricing/))
- AWS X-Ray ([pricing](https://aws.amazon.com/xray/pricing/))
- AWS Identity and Access Management
- AWS CloudFormation

Standard charges apply for each service.

# Setup

Download or clone this repository.

    $ git clone https://github.com/awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/list-manager

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    list-manager$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

To create a database password and store it in AWS Secrets Manager, run the `2-create-dbpasswordsecret.sh` script.

    list-manager$ ./2-create-dbpasswordsecret.sh

To create the VPC and RDS database instance, run the `3-deploy-vpc.sh` script. This process takes about 15 minutes.

    list-manager$ ./3-deploy-vpc.sh

# Deploy

To deploy the application, run `4-deploy.sh`.

    list-manager$ ./4-deploy.sh
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2678 / 2678.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - list-manager

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

To create a table in the database, run the `5-create-dbtable.sh` script.

    list-manager$ ./5-create-dbtable.sh

# Test

To invoke the function with a test event, run `6-invoke.sh`.

    list-manager$ ./6-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

Let the script invoke the function a few times and then press `CRTL+C` to exit.

If that succeeds, send records to the Kinesis stream. The processor function's event source mapping pulls records from the stream and invokes the function.

    list-manager$ ./7-put-records.sh

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the function writing to the two DynamoDB tables and the MySQL database.

![Service Map](/sample-apps/list-manager/images/listmanager-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/list-manager/images/listmanager-trace.png)

Finally, view the application in the Lambda console.

*To view the application*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **list-manager**.

  ![Application](/sample-apps/list-manager/images/listmanager-application.png)

# Kinesis record structure

Tally list:

    {
      "title": "stats",
      "type": "tally",
      "user": "andy",
      "entries": [
        "xp": 20
      ]
    }

Rank list:

    {
      "title": "favorite movie",
      "type": "rank",
      "user": "andy",
      "entries": [
        "blade runner": 1,
        "the empire strikes back": 2
        "alien": 3
      ]
    }

# Table structure

Per-user table:

    {
      "id": "#12AB",
      "aggid": "#12AB",
      "title": "stats",
      "type": "tally",
      "last updated": 1234567890123,
      "user": "andy"
      "entries": [
        {
          "xp": 100
        }
      ]
    }

    {
      "id": "#12AB",
      "aggid": "#12AB",
      "title": "stats",
      "type": "rank",
      "last updated": 1234567890123,
      "user": "andy"
      "entries": [
        {
          "blade runner": 1,
          "the empire strikes back": 2,
          "alien": 3
        }
      ]
    }

Aggregate table:

    {
      "id": "#12AB",
      "title": "stats",
      "type": "tally",
      "last updated": 1234567890123,
      "entries": [
        {
          "xp": 500
        }
      ]
    }

    {
      "id": "#12AB",
      "title": "favorite movie",
      "type": "rank",
      "last updated": 1234567890123,
      "entries": [
        {
          "blade runner": 3,
          "the empire strikes back": 5,
          "alien": 4
        }
      ]
    }

# Cleanup

To delete the application, run the cleanup script.

    list-manager$ ./8-cleanup.sh
