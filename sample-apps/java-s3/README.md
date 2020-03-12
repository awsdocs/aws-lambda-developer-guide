# S3 image resizer (Java)
This project creates a function and supporting resources:
- src - A Java function.
- template.yml - An AWS CloudFormation template that creates an application.
- build.gradle - A Gradle build file.
- 1-create-bucket.sh, 2-deploy.sh, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

![Architecture](/sample-apps/blank/images/sample-blank.png)

Use the following instructions to deploy the sample application.

# Requirements
- [Java 8 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Gradle 5](https://gradle.org/releases/)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup
Download or clone this repository.

    $ git clone git@github.com:awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/java-s3

Run `1-create-bucket.sh` to create a new bucket for deployment artifacts. Or, if you already have a bucket, create a file named `bucket-name.txt` that contains the name of your bucket.

    java-s3$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

Run `2-build-layer.sh` to build a Lambda layer that contains the function's runtime dependencies. Packaging dependencies in a layer reduces the size of the deployment package that you upload when you modify your code.

    java-s3$ ./2-build-layer.sh

# Deploy
Run `3-deploy.sh` to deploy the application.

    java-s3$ ./3-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - java-s3

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
Run `4-upload.sh` to upload an image file to the application bucket, and trigger the function.

    java-s3$ ./4-upload.sh

Run `5-invoke.sh` to invoke the function directly.

    java-s3$ ./5-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

The functions in this application are instrumented with AWS X-Ray. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](/sample-apps/java-s3/images/java-s3-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/java-s3/images/java-s3-trace.png)

Finally, view the application in the Lambda console.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **java-s3**.

  ![Application](/sample-apps/java-s3/images/java-s3-application.png)

# Cleanup
To delete the application, run `6-cleanup.sh`.

    blank$ ./6cleanup.sh
