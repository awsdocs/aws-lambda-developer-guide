# Blank function
This project creates a function and supporting resources:
- src - A Java function.
- template.yml - An AWS CloudFormation template that creates an application.
- build.gradle - A Gradle build file.
- 1-create-bucket.sh, 2-deploy.sh, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

![Architecture](/sample-apps/blank/images/sample-blank.png)

Use the following instructions to deploy the sample application. For more information on the application's architecture and implementation, see [Blank Application for AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/sample-blank.html) in the developer guide.

# Requirements
- [Java 8 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Gradle 5.6 or newer](https://gradle.org/releases/)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup
Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/blank-java

Run `1-create-bucket.sh` to create a new bucket for deployment artifacts. Or, if you already have a bucket, rename `2-deploy.sh.template` to `2-deploy.sh` and replace `MY_BUCKET` in it with the name of an existing bucket.

    blank-java$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

# Deploy
Run `2-deploy.sh` to deploy the application.

    blank-java$ ./2-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - blank-java

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
Run `3-invoke.sh` to invoke the function.

    blank$ ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

The functions in this application are instrumented with AWS X-Ray. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](/sample-apps/blank/images/blank-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/blank/images/blank-trace.png)

Finally, view the application in the Lambda console.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **blank-java**.

  ![Application](/sample-apps/blank/images/blank-application.png)

# Cleanup
To delete the application, run `4-cleanup.sh`.

    blank$ ./4-cleanup.sh
