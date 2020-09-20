# AWS Lambda deployment package in Java<a name="java-package"></a>

A deployment package is a ZIP archive that contains your compiled function code and dependencies\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

AWS Lambda provides the following libraries for Java functions:
+ [com\.amazonaws:aws\-lambda\-java\-core](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-core) \(required\) – Defines handler method interfaces and the context object that the runtime passes to the handler\. If you define your own input types, this is the only library you need\.
+ [com\.amazonaws:aws\-lambda\-java\-events](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) – Input types for events from services that invoke Lambda functions\.
+ [com\.amazonaws:aws\-lambda\-java\-log4j2](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-log4j2) – An appender library for Log4j 2 that you can use to add the request ID for the current invocation to your [function logs](java-logging.md)\.

These libraries are available through [Maven central repository](https://search.maven.org/search?q=g:com.amazonaws)\. Add them to your build definition as follows\.

------
#### [ Gradle ]

```
dependencies {
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.1'
    implementation 'com.amazonaws:aws-lambda-java-events:3.1.0'
    runtimeOnly 'com.amazonaws:aws-lambda-java-log4j2:1.2.0'
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
      <version>3.1.0</version>
    </dependency>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-log4j2</artifactId>
      <version>1.2.0</version>
    </dependency>
  </dependencies>
```

------

To create a deployment package, compile your function code and dependencies into a single ZIP or Java Archive \(JAR\) file\. For Gradle, [use the Zip build type](#java-package-gradle)\. For Maven, [use the Maven Shade plugin](#java-package-maven)\.

**Note**  
To keep your deployment package size small, package your function's dependencies in layers\. Layers let you manage your dependencies independently, can be used by multiple functions, and can be shared with other accounts\. For details, see [AWS Lambda layers](configuration-layers.md)\.

You can upload your deployment package by using the Lambda console, the Lambda API, or AWS SAM\.

**To upload a deployment package with the Lambda console**

1. Open the Lambda console [Functions page](https://console.aws.amazon.com/lambda/home#/functions)\.

1. Choose a function\.

1. Under **Function code**, choose **Upload**\.

1. Upload the deployment package\.

1. Choose **Save**\.

**Topics**
+ [Building a deployment package with Gradle](#java-package-gradle)
+ [Building a deployment package with Maven](#java-package-maven)
+ [Uploading a deployment package with the Lambda API](#java-package-cli)
+ [Uploading a deployment package with AWS SAM](#java-package-cloudformation)

## Building a deployment package with Gradle<a name="java-package-gradle"></a>

Use the `Zip` build type to create a deployment package with your function's code and dependencies\.

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

This build configuration produces a deployment package in the `build/distributions` folder\. The `compileJava` task compiles your function's classes\. The `processResources` tasks copies libraries from the build's classpath into a folder named `lib`\.

**Example build\.gradle – Dependencies**  

```
dependencies {
    implementation platform('software.amazon.awssdk:bom:2.10.73')
    implementation 'software.amazon.awssdk:lambda'
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.1'
    implementation 'com.amazonaws:aws-lambda-java-events:3.1.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'org.apache.logging.log4j:log4j-api:2.13.0'
    implementation 'org.apache.logging.log4j:log4j-core:2.13.0'
    runtimeOnly 'org.apache.logging.log4j:log4j-slf4j18-impl:2.13.0'
    runtimeOnly 'com.amazonaws:aws-lambda-java-log4j2:1.2.0'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.6.0'
}
```

Lambda loads JAR files in Unicode alphabetical order\. If multiple JAR files in the `lib` folder contain the same class, the first one is used\. You can use the following shell script to identify duplicate classes\.

**Example test\-zip\.sh**  

```
mkdir -p expanded
unzip path/to/my/function.zip -d expanded
find ./expanded/lib -name '*.jar' | xargs -n1 zipinfo -1 | grep '.*.class' | sort | uniq -c | sort
```

## Building a deployment package with Maven<a name="java-package-maven"></a>

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
[INFO] Including com.amazonaws:aws-lambda-java-events:jar:3.1.0 in the shaded jar.
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

This command generates a JAR file in the `target` folder\.

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

## Uploading a deployment package with the Lambda API<a name="java-package-cli"></a>

To update a function's code with the AWS CLI or AWS SDK, use the [UpdateFunctionCode](API_UpdateFunctionCode.md) API operation\. For the AWS CLI, use the `update-function-code` command\. The following command uploads a deployment package named `my-function.zip` in the current directory\.

```
~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://my-function.zip
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

If your deployment package is larger than 50 MB, you can't upload it directly\. Upload it to an Amazon S3 bucket and point Lambda to the object\. The following example commands upload a deployment package to a bucket named `my-bucket` and use it to update a function's code\.

```
~/my-function$ aws s3 cp my-function.zip s3://my-bucket
 upload: my-function.zip to s3://my-bucket/my-function
 ~/my-function$ aws lambda update-function-code --function-name my-function \
 --s3-bucket my-bucket --s3-key my-function.zip
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

## Uploading a deployment package with AWS SAM<a name="java-package-cloudformation"></a>

You can use the AWS Serverless Application Model to automate deployments of your function code, configuration, and dependencies\. AWS SAM is an extension of AWS CloudFormation that provides a simplified syntax for defining serverless applications\. The following example template defines a function with a deployment package in the `build/distributions` directory that Gradle uses\.

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
        - AWSLambdaReadOnlyAccess
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

For a complete working example, see the following sample applications\.

**Sample Lambda applications in Java**
+ [blank\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/blank-java) – A Java function that shows the use of Lambda's Java libraries, logging, environment variables, layers, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-basic) – A minimal Java function with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events) – A minimal Java function that uses the [aws\-lambda\-java\-events](#java-package) library with event types that don't require the AWS SDK as a dependency, such as Amazon API Gateway\.
+ [java\-events\-v1sdk](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/java-events-v1sdk) – A Java function that uses the [aws\-lambda\-java\-events](#java-package) library with event types that require the AWS SDK as a dependency \(Amazon Simple Storage Service, Amazon DynamoDB, and Amazon Kinesis\)\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/master/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.