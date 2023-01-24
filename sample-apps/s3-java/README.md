# S3 image resizer (Java)

![Architecture](/sample-apps/s3-java/images/sample-s3-java.png)

The project source includes function code and supporting resources:

- `src/main` - A Java Lambda function that scales down an image stored in S3.
- `src/test` - A unit test and helper classes.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `build.gradle` - A Gradle build file.
- `pom.xml` - A Maven build file.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application.

# Requirements
- [Java 8 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Gradle 5](https://gradle.org/releases/) or [Maven 3](https://maven.apache.org/docs/history.html)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v1.17 or newer.

If you use the AWS CLI v2, add the following to your [configuration file](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html) (`~/.aws/config`):

```
cli_binary_format=raw-in-base64-out
```

This setting enables the AWS CLI v2 to load JSON events from a file, matching the v1 behavior.

# Setup
Download or clone this repository.

    $ git clone https://github.com/awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/s3-java

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    s3-java$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d

To build a Lambda layer that contains the function's runtime dependencies, run `2-build-layer.sh`. Packaging dependencies in a layer reduces the size of the deployment package that you upload when you modify your code.

    s3-java$ ./2-build-layer.sh

# Deploy
To deploy the application, run `3-deploy.sh`.

    s3-java$ ./3-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - s3-java

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

You can also build the application with Maven. To use maven, add `mvn` to the command.

    java-basic$ ./3-deploy.sh mvn
    [INFO] Scanning for projects...
    [INFO] -----------------------< com.example:s3-java >-----------------------
    [INFO] Building s3-java-function 1.0-SNAPSHOT
    [INFO] --------------------------------[ jar ]---------------------------------
    ...

# Test
This Lambda function takes an image that's currently stored in S3, and scales it down into
a thumbnail-sized image. To upload an image file to the application bucket, run `4-upload.sh`.

    s3-java$ ./4-upload.sh

In your `s3-java-bucket-<random_uuid>` bucket that was created in step 3, you should now see a
key `inbound/sample-s3-java.png` file, which represents the original image.

To invoke the function directly, run `5-invoke.sh`.

    s3-java$ ./5-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }

Let the script invoke the function a few times and then press `CRTL+C` to exit. Note that you
may see function timeouts in the first few iterations due to cold starts; after a while, they
should begin to succeed.

If you look at the `s3-java-bucket-<random_uuid>` bucket in your account, you should now see a
key `resized-inbound/sample-s3-java.png` file, which represents the new, shrunken image.

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](/sample-apps/s3-java/images/s3-java-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/s3-java/images/s3-java-trace.png)

# Cleanup
To delete the application, run `6-cleanup.sh`.

    blank$ ./6-cleanup.sh
