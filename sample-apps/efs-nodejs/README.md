# Using Amazon EFS for file storage

This application demonstrates the use of Amazon EFS with AWS Lambda. You can use Amazon EFS to create file systems that provide shared storage to Lambda functions and other compute resources. Your functions mount a folder in the file system to a local directory with the NFS protocol. The sample application creates a VPC network, file system, function, and supporting resources with AWS CloudFormation.

The function takes a event with the following structure:

```
{
  "fileName": "test.bin",
  "fileSize": 1048576
}
```

The function creates a file of the specified size (1MB in this case) and then reads it into memory.

The project source includes function code and supporting resources:

- `dbadmin` - A Node.js function that reads and writes files.
- `lib` - A Lambda layer with the npm modules used by the application's function.
- `event.json` - A JSON document that can be used to test the application's function.
- `template.yml` - An AWS CloudFormation template that creates the application.
- `template-vpcefs.yml` - A template that creates the VPC and Amazon EFS file system.
- `1-create-bucket.sh`, `2-deploy-vpc.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application.

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

To run the sample application in AWS, you need permission to use Lambda and the following services.

- Amazon EFS ([pricing](https://aws.amazon.com/efs/pricing/))
- Amazon VPC ([pricing](https://aws.amazon.com/vpc/pricing/))
- AWS Identity and Access Management
- AWS CloudFormation

Standard charges apply for each service.

# Setup

Download or clone this repository.

    $ git clone https://github.com/awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/efs-nodejs

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    efs-nodejs$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

To create the VPC and EFS file system, run the `2-deploy-vpc.sh` script.

    efs-nodejs$ ./2-deploy-vpc.sh

# Deploy

To deploy the application, run `3-deploy.sh`.

    efs-nodejs$ ./3-deploy.sh
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2678 / 2678.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - efs-nodejs

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test

To invoke the function with a test event, use the invoke script.

    efs-nodejs$ ./4-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    {"writeTimeMs":3.316225,"readTimeMs":166.129772,"fileSizeBytes":1398104}

Let the script invoke the function a few times and then press `CRTL+C` to exit.

# Cleanup

To delete the application, run the cleanup script.

    efs-nodejs$ ./5-cleanup.sh
