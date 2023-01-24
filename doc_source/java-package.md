# Deploy Java Lambda functions with \.zip or JAR file archives<a name="java-package"></a>

Your AWS Lambda function's code consists of scripts or compiled programs and their dependencies\. You use a *deployment package* to deploy your function code to Lambda\. Lambda supports two types of deployment packages: container images and \.zip file archives\.

This page describes how to create your deployment package as a \.zip file or Jar file, and then use the deployment package to deploy your function code to AWS Lambda using the AWS Command Line Interface \(AWS CLI\)\.

**Topics**
+ [Prerequisites](#java-package-prereqs)
+ [Tools and libraries](#java-package-libraries)

## Prerequisites<a name="java-package-prereqs"></a>

The AWS CLI is an open\-source tool that enables you to interact with AWS services using commands in your command line shell\. To complete the steps in this section, you must have the following:
+ [AWS CLI – Install version 2](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
+ [AWS CLI – Quick configuration with `aws configure`](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)

## Tools and libraries<a name="java-package-libraries"></a>

Lambda provides the following libraries for Java functions:
+ [com\.amazonaws:aws\-lambda\-java\-core](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-core) \(required\) – Defines handler method interfaces and the context object that the runtime passes to the handler\. If you define your own input types, this is the only library that you need\.
+ [com\.amazonaws:aws\-lambda\-java\-events](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) – Input types for events from services that invoke Lambda functions\.
+ [com\.amazonaws:aws\-lambda\-java\-log4j2](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-log4j2) – An appender library for Apache Log4j 2 that you can use to add the request ID for the current invocation to your [function logs](java-logging.md)\.
+ [AWS SDK for Java 2\.0](https://github.com/aws/aws-sdk-java-v2) – The official AWS SDK for the Java programming language\.

These libraries are available through [Maven Central Repository](https://search.maven.org/search?q=g:com.amazonaws)\. Add them to your build definition as follows:

------
#### [ Gradle ]

```
dependencies {
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.1'
    implementation 'com.amazonaws:aws-lambda-java-events:3.11.0'
    runtimeOnly 'com.amazonaws:aws-lambda-java-log4j2:1.5.1'
}
```

------
#### [ Maven ]

```
  <dependencies>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-core</artifactId>
      <version>1.2.1</version>
    </dependency>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-events</artifactId>
      <version>3.11.0</version>
    </dependency>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-log4j2</artifactId>
      <version>1.5.1</version>
    </dependency>
  </dependencies>
```

------

To create a deployment package, compile your function code and dependencies into a single \.zip file or Java Archive \(JAR\) file\. For Gradle, [use the `Zip` build type](#java-package-gradle)\. For Apache Maven, [use the Maven Shade plugin](#java-package-maven)\.

**Note**  
To keep your deployment package size small, package your function's dependencies in layers\. Layers enable you to manage your dependencies independently, can be used by multiple functions, and can be shared with other accounts\. For more information, see [Creating and sharing Lambda layers](configuration-layers.md)\.

You can upload your deployment package by using the Lambda console, the Lambda API, or AWS Serverless Application Model \(AWS SAM\)\.

**To upload a deployment package with the Lambda console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose a function\.

1. Under **Code source**, choose **Upload from**\.

1. Upload the deployment package\.

1. Choose **Save**\.

**Topics**
+ [Building a deployment package with Gradle](#java-package-gradle)
+ [Building a deployment package with Maven](#java-package-maven)
+ [Uploading a deployment package with the Lambda API](#java-package-cli)
+ [Uploading a deployment package with AWS SAM](#java-package-cloudformation)

### Building a deployment package with Gradle<a name="java-package-gradle"></a>

To create a deployment package with your function's code and dependencies, use the `Zip` build type\.

**Example build\.gradle – Build task**  

```
task buildZip(type: Zip) {
    from compileJava
    from processResources
    into('lib') {
        from configurations.runtimeClasspath
    }
}
```

This build configuration produces a deployment package in the `build/distributions` directory\. The `compileJava` task compiles your function's classes\. The `processResources` task copies the Java project resources into their target directory, potentially processing then\. The statement `into('lib')` then copies dependency libraries from the build's classpath into a folder named `lib`\.

**Example build\.gradle – Dependencies**  

```
dependencies {
    implementation platform('software.amazon.awssdk:bom:2.10.73')
    implementation 'software.amazon.awssdk:lambda'
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.1'
    implementation 'com.amazonaws:aws-lambda-java-events:3.11.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'org.apache.logging.log4j:log4j-api:2.17.1'
    implementation 'org.apache.logging.log4j:log4j-core:2.17.1'
    runtimeOnly 'org.apache.logging.log4j:log4j-slf4j18-impl:2.17.1'
    runtimeOnly 'com.amazonaws:aws-lambda-java-log4j2:1.5.1'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.6.0'
}
```

Lambda loads JAR files in Unicode alphabetical order\. If multiple JAR files in the `lib` directory contain the same class, the first one is used\. You can use the following shell script to identify duplicate classes:

**Example test\-zip\.sh**  

```
mkdir -p expanded
unzip path/to/my/function.zip -d expanded
find ./expanded/lib -name '*.jar' | xargs -n1 zipinfo -1 | grep '.*.class' | sort | uniq -c | sort
```

### Building a deployment package with Maven<a name="java-package-maven"></a>

To build a deployment package with Maven, use the [Maven Shade plugin](https://maven.apache.org/plugins/maven-shade-plugin/)\. The plugin creates a JAR file that contains the compiled function code and all of its dependencies\.

**Example pom\.xml – Plugin configuration**  

```
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.2</version>
        <configuration>
          <createDependencyReducedPom>false</createDependencyReducedPom>
        </configuration>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

To build the deployment package, use the `mvn package` command\.

```
[INFO] Scanning for projects...
[INFO] -----------------------< com.example:java-maven >-----------------------
[INFO] Building java-maven-function 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
...
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ java-maven ---
[INFO] Building jar: target/java-maven-1.0-SNAPSHOT.jar
[INFO]
[INFO] --- maven-shade-plugin:3.2.2:shade (default) @ java-maven ---
[INFO] Including com.amazonaws:aws-lambda-java-core:jar:1.2.1 in the shaded jar.
[INFO] Including com.amazonaws:aws-lambda-java-events:jar:3.11.0 in the shaded jar.
[INFO] Including joda-time:joda-time:jar:2.6 in the shaded jar.
[INFO] Including com.google.code.gson:gson:jar:2.8.6 in the shaded jar.
[INFO] Replacing original artifact with shaded artifact.
[INFO] Replacing target/java-maven-1.0-SNAPSHOT.jar with target/java-maven-1.0-SNAPSHOT-shaded.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.321 s
[INFO] Finished at: 2020-03-03T09:07:19Z
[INFO] ------------------------------------------------------------------------
```

This command generates a JAR file in the `target` directory\.

If you use the appender library \(`aws-lambda-java-log4j2`\), you must also configure a transformer for the Maven Shade plugin\. The transformer library combines versions of a cache file that appear in both the appender library and in Log4j\.

**Example pom\.xml – Plugin configuration with Log4j 2 appender**  

```
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.2</version>
        <configuration>
          <createDependencyReducedPom>false</createDependencyReducedPom>
        </configuration>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <transformers>
                <transformer implementation="com.github.edwgiz.maven_shade_plugin.log4j2_cache_transformer.PluginsCacheFileTransformer">
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
        <dependencies>
          <dependency>
            <groupId>com.github.edwgiz</groupId>
            <artifactId>maven-shade-plugin.log4j2-cachefile-transformer</artifactId>
            <version>2.13.0</version>
          </dependency>
        </dependencies>
      </plugin>
```

### Uploading a deployment package with the Lambda API<a name="java-package-cli"></a>

To update a function's code with the AWS Command Line Interface \(AWS CLI\) or AWS SDK, use the [UpdateFunctionCode](API_UpdateFunctionCode.md) API operation\. For the AWS CLI, use the `update-function-code` command\. The following command uploads a deployment package named `my-function.zip` in the current directory:

```
aws lambda update-function-code --function-name my-function --zip-file fileb://my-function.zip
```

You should see the following output:

```
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "java8",
    "Role": "arn:aws:iam::123456789012:role/lambda-role",
    "Handler": "example.Handler",
    "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
    "Version": "$LATEST",
    "TracingConfig": {
        "Mode": "Active"
    },
    "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
    ...
}
```

If your deployment package is larger than 50 MB, you can't upload it directly\. Upload it to an Amazon Simple Storage Service \(Amazon S3\) bucket and point Lambda to the object\. The following example commands upload a deployment package to an S3 bucket named `my-bucket` and use it to update a function's code:

```
aws s3 cp my-function.zip s3://my-bucket
```

You should see the following output:

```
upload: my-function.zip to s3://my-bucket/my-function
```

```
aws lambda update-function-code --function-name my-function \
 --s3-bucket my-bucket --s3-key my-function.zip
```

You should see the following output:

```
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
    "Runtime": "java8",
    "Role": "arn:aws:iam::123456789012:role/lambda-role",
    "Handler": "example.Handler",
    "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
    "Version": "$LATEST",
    "TracingConfig": {
        "Mode": "Active"
    },
    "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
    ...
}
```

You can use this method to upload function packages up to 250 MB \(decompressed\)\.

### Uploading a deployment package with AWS SAM<a name="java-package-cloudformation"></a>

You can use AWS SAM to automate deployments of your function code, configuration, and dependencies\. AWS SAM is an extension of AWS CloudFormation that provides a simplified syntax for defining serverless applications\. The following example template defines a function with a deployment package in the `build/distributions` directory that Gradle uses:

**Example template\.yml**  

```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: An AWS Lambda application that calls the Lambda API.
Resources:
  function:
    Type: [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html)
    Properties:
      CodeUri: build/distributions/java-basic.zip
      Handler: example.Handler
      Runtime: java8
      Description: Java function
      MemorySize: 512
      Timeout: 10
      # Function's execution role
      Policies:
        - AWSLambdaBasicExecutionRole
        - AWSLambda_ReadOnlyAccess
        - AWSXrayWriteOnlyAccess
        - AWSLambdaVPCAccessExecutionRole
      Tracing: Active
```

To create the function, use the `package` and `deploy` commands\. These commands are customizations to the AWS CLI\. They wrap other commands to upload the deployment package to Amazon S3, rewrite the template with the object URI, and update the function's code\.

The following example script runs a Gradle build and uploads the deployment package that it creates\. It creates an AWS CloudFormation stack the first time you run it\. If the stack already exists, the script updates it\.

**Example deploy\.sh**  

```
#!/bin/bash
set -eo pipefail
aws cloudformation package --template-file template.yml --s3-bucket MY_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name java-basic --capabilities CAPABILITY_NAMED_IAM
```

For a complete working example, see the following sample applications:

**Sample Lambda applications in Java**
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A collection of minimal Java functions with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A collection of Java functions that contain skeleton code for how to handle events from various services such as Amazon API Gateway, Amazon SQS, and Amazon Kinesis\. These functions use the latest version of the [aws\-lambda\-java\-events](#java-package) library \(3\.0\.0 and newer\)\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.
+ [Use API Gateway to invoke a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/example_cross_LambdaAPIGateway_section.html) – A Java function that scans a Amazon DynamoDB table that contains employee information\. It then uses Amazon Simple Notification Service to send a text message to employees celebrating their work anniversaries\. This example uses API Gateway to invoke the function\.