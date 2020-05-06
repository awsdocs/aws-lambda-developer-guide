# Basic function with minimal dependencies (Java)

![Architecture](/sample-apps/java-basic/images/sample-java-basic.png)

The project source includes function code and supporting resources:
- `src/main` - A Java function.
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
- [The AWS CLI v1](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup
Download or clone this repository.

    $ git clone https://github.com/awsdocs/aws-lambda-developer-guide.git
    $ cd aws-lambda-developer-guide/sample-apps/java-basic

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    java-basic$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e4xmplb5b22e0d

# Deploy
To deploy the application, run `2-deploy.sh`.

    java-basic$ ./2-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - java-basic

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

You can also build the application with Maven. To use maven, add `mvn` to the command.

    java-basic$ ./2-deploy.sh mvn
    [INFO] Scanning for projects...
    [INFO] -----------------------< com.example:java-basic >-----------------------
    [INFO] Building java-basic-function 1.0-SNAPSHOT
    [INFO] --------------------------------[ jar ]---------------------------------
    ...

# Test
To invoke the function, run `3-invoke.sh`.

    java-basic$ ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    "200 OK"

Let the script invoke the function a few times and then press `CRTL+C` to exit.

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](/sample-apps/java-basic/images/java-basic-servicemap.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](/sample-apps/java-basic/images/java-basic-trace.png)

# Configure Handler Class

By default, the function uses a handler class named `Handler` that takes a map as input and returns a string. The project also includes handlers that use other input and output types. These are defined in the following files under src/main/java/example:

- `Handler.java` – Takes a `Map<String,String>` as input.
- `HandlerInteger.java` – Takes an `Integer` as input.
- `HandlerList.java` – Takes a `List<Integer>` as input.
- `HandlerDivide.java` – Takes a `List<Integer>` with two integers as input.
- `HandlerStream.java` – Takes an `InputStream` and `OutputStream` as input.
- `HandlerString.java` – Takes a `String` as input.
- `HandlerWeatherData.java` – Takes a custom type as input.

To use a different handler, change the value of the Handler setting in the application template (`template.yml` or `template-mvn.yaml`). For example, to use the list handler:

    Properties:
      CodeUri: build/distributions/java-basic.zip
      Handler: example.HandlerList

Deploy the change, and then use the invoke script to test the new configuration. For handlers, that don't take a JSON object as input, pass the type (`string`, `int` or `list`) as an argument to the invoke script.

    ./3-invoke.sh list
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    9979

# Cleanup
To delete the application, run `4-cleanup.sh`.

    java-basic$ ./4-cleanup.sh
