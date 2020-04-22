# Blank function
This sample application is a Lambda function that calls the Lambda API. It shows the use of logging, environment variables, AWS X-Ray tracing, layers, unit tests and the AWS SDK. You can use it to learn about Lambda features or use it as a starting point for your own projects.

![Architecture](/sample-apps/blank/images/sample-blank.png)

The project source includes function code and supporting resources:

- `function` - A Node.js function.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Variants of this sample application are available for the following languages:

- Python – [blank-python](https://github.com/awsdocs/aws-lambda-developer-guide/edit/master/sample-apps/blank-python).
- Ruby – [blank-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/edit/master/sample-apps/blank-ruby).
- Java – [blank-java](https://github.com/awsdocs/aws-lambda-developer-guide/edit/master/sample-apps/blank-java).
- Go – [blank-go](https://github.com/awsdocs/aws-lambda-developer-guide/edit/master/sample-apps/blank-go).
- C# – [blank-csharp](https://github.com/awsdocs/aws-lambda-developer-guide/edit/master/sample-apps/blank-csharp).
- PowerShell – [blank-powershell](https://github.com/awsdocs/aws-lambda-developer-guide/edit/master/sample-apps/blank-powershell).

Use the following instructions to deploy the sample application. For an in-depth look at its architecture and features, see [Blank Function Sample Application for AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/samples-blank.html) in the developer guide.

# Requirements
- [Node.js 10 with npm](https://nodejs.org/en/download/releases/)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup
Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/blank

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`. Or, if you already have a bucket, create a file named `bucket-name.txt` that contains the name of your bucket.

    blank$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

# Deploy
To deploy the application, run `2-deploy.sh`.

    blank$ ./2-deploy.sh
    added 16 packages from 18 contributors and audited 18 packages in 0.926s
    added 17 packages from 19 contributors and audited 19 packages in 0.916s
    Uploading to e678bc216e6a0d510d661ca9ae2fd941  2737254 / 2737254.0  (100.00%)
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Waiting for stack create/update to complete
    Successfully created/updated stack - blank

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
To invoke the function, run `3-invoke.sh`.

    blank$ ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map. The following service map shows the function calling Amazon S3.

![Service Map](/sample-apps/blank/images/blank-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/blank/images/blank-trace.png)

Finally, view the application in the Lambda console.

*To view the application*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **blank**.

  ![Application](/sample-apps/blank/images/blank-application.png)

# Cleanup
To delete the application, run `4-cleanup.sh`.

    blank$ ./4-cleanup.sh
