# Blank function
This project creates a function and supporting resources:
- function - A Node.js function.
- template.yaml - An AWS CloudFormation template that creates an application.
- install.sh, deploy.sh, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

# TODO
change stack name in `deploy.sh.template`, `invoke.sh`, `cleanup.sh`
(optional) change function name in `template.yaml`, `invoke.sh`
(optional) change function folder name in `template.yaml`, `install.sh`

![Architecture](/sample-apps/blank/images/sample-blank.png)

Use the following instructions to deploy the sample application. For more information on the application's architecture and implementation, see [Blank Application for AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/sample-blank.html) in the developer guide.

# Requirements
- [Node.js 10 with NPM](https://nodejs.org/en/download/releases/)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup
Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/blank

Run `create-bucket.sh` to create a new bucket for deployment artifacts. Or, if you already have a bucket, replace `MY_BUCKET` in `deploy.sh` with the name of an existing bucket.

    blank$ ./create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

Run `install.sh` to install the dependencies for the function, including the X-Ray SDK.

    blank$ ./install.sh
    added 16 packages from 18 contributors and audited 18 packages in 0.926s
    found 0 vulnerabilities

    added 17 packages from 19 contributors and audited 19 packages in 0.916s
    found 0 vulnerabilities

# Deploy
Run `deploy.sh` to deploy the application.

    blank$ ./deploy.sh
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2737254 / 2737254.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - blank

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
Invoke the function.

    blank$ ./invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

The functions in this application are instrumented with AWS X-Ray. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the function calling Amazon S3.

![Service Map](/sample-apps/blank/images/blank-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/blank/images/blank-trace.png)

Finally, view the application in the Lambda console.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **blank**.

  ![Application](/sample-apps/blank/images/blank-application.png)

# Cleanup
To delete the application, run the cleanup script.

    blank$ ./cleanup.sh
