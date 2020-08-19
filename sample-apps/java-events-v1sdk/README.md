# Basic function with event library types and the AWS SDK (Java)

This sample application shows the use of the `aws-lambda-java-events` library with event types that require AWS SDK as a dependency. A separate handler class is defined for each input type. For other event types (which don't require the AWS SDK), see the `java-events` sample.

![Architecture](/sample-apps/java-events-v1sdk/images/sample-java-events-v1sdk.png)

The project includes function code and supporting resources:
- `src/main` - A Java function.
- `src/test` - A unit test and helper classes.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `build.gradle` - A Gradle build file.
- `pom.xml` - A Maven build file.
- `1-create-bucket.sh`, `2-build-layer.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

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
    $ cd aws-lambda-developer-guide/sample-apps/java-events-v1sdk

Run `1-create-bucket.sh` to create a new bucket for deployment artifacts.

    java-events-v1sdk$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e4xmplb5b22e0d

To build a Lambda layer that contains the function's runtime dependencies, run `2-build-layer.sh`. Packaging dependencies in a layer reduces the size of the deployment package that you upload when you modify your code.

    java-events-v1sdk$ ./2-build-layer.sh

# Deploy
Run `3-deploy.sh` to build the application with Gradle and deploy it.

    java-events-v1sdk$ ./3-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - java-events-v1sdk

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

You can also build the application with Maven. To use maven, add `mvn` to the command.

    java-events-v1sdk$ ./3-deploy.sh mvn
    [INFO] Scanning for projects...
    [INFO] -----------------------< com.example:java-events-v1sdk >-----------------------
    [INFO] Building java-events-v1sdk-function 1.0-SNAPSHOT
    [INFO] --------------------------------[ jar ]---------------------------------
    ...

# Test
Run `4-invoke.sh` to invoke the function.

    java-events-v1sdk$ ./4-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    "200 OK"

Let the script invoke the function a few times and then press `CRTL+C` to exit.

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](/sample-apps/java-events-v1sdk/images/java-events-v1sdk-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/java-events-v1sdk/images/java-events-v1sdk-trace.png)

# Configure Handler Class

By default, the function uses a handler class named `Handler` that takes an Amazon S3 notification event as input and returns a string. The project also includes handlers that use other input and output types. The handlers are defined in the following files under `src/main/java/example`:

- `Handler.java` - Takes `S3Event` as input.
- `HandlerDynamoDB.java` - Takes `DynamoDBEvent` as input.
- `HandlerKinesis.java` - Takes `KinesisEvent` as input.

To use a different handler, change the value of the Handler setting in the application template (`template.yml` or `template-mvn.yaml`). For example, to use the Kinesis handler:

    Properties:
      CodeUri: build/distributions/java-events-v1sdk.zip
      Handler: example.HandlerKinesis

Deploy the change, and then use the invoke script to test the new configuration. Pass the handler type key as an argument to the invoke script.

    ./4-invoke.sh kin
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    "200 OK"

The following event type keys are supported:
- none - S3 notification (`events/s3-notification.json`)
- `kin` - Kinesis record (`events/kinesis-record.json`)
- `ddb` - DynamoDB record (`events/dynamodb-record.json`)

# Cleanup
To delete the application, run `5-cleanup.sh`.

    java-events-v1sdk$ ./5-cleanup.sh
