# Creating a \.jar Deployment Package Using Maven<a name="java-package-maven"></a>

This section shows how to package your Java code into a deployment package using Maven at the command line\.

**Topics**
+ [Before You Begin](#java-package-maven-prereqs)
+ [Project Structure Overview](#java-package-maven-overview)
+ [Create a Project](#java-package-maven-create)
+ [Build the Project](#java-package-maven-build)

## Before You Begin<a name="java-package-maven-prereqs"></a>

You will need to install the Maven command\-line build tool\. For more information, go to [Maven](https://maven.apache.org/)\. If you are using Linux, check your package manager\.

```
sudo apt-get install mvn
```

if you are using Homebrew

```
brew install maven
```

## Project Structure Overview<a name="java-package-maven-overview"></a>

After you set up the project, you should have the following folder structure:

```
project-dir/pom.xml           
project-dir/src/main/java/  (your code goes here)
```

Your code will then be in the /java folder\. For example, if your package name is `example` and you have a `Hello.java` class in it, the structure will be:

```
project-dir/src/main/java/example/Hello.java
```

After you build the project, the resulting \.jar file \(that is, your deployment package\), will be in the `project-dir/target` subdirectory\.

## Create a Project<a name="java-package-maven-create"></a>

Follow the steps in this section to create a Java project\.

1. Create a project directory \(*project\-dir*\)\. 

1. In the *project\-dir* directory, create the following:
   + Project Object Model file, `pom.xml`\. Add the following project information and configuration details for Maven to build the project\.

     ```
     <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
       <modelVersion>4.0.0</modelVersion>
     
       <groupId>doc-examples</groupId>
       <artifactId>lambda-java-example</artifactId>
       <packaging>jar</packaging>
       <version>1.0-SNAPSHOT</version>
       <name>lambda-java-example</name>
     
       <dependencies>
         <dependency>
           <groupId>com.amazonaws</groupId>
           <artifactId>aws-lambda-java-core</artifactId>
           <version>1.2.0</version>
         </dependency>
       </dependencies>
     
       <build>
         <plugins>
           <plugin>
             <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-shade-plugin</artifactId>
             <version>3.2.1</version>
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
         </plugins>
       </build>
     </project>
     ```
**Note**  
In the `dependencies` section, the `groupId` \(that is, com\.amazonaws\) is the Amazon AWS group ID for Maven artifacts in the Maven Central Repository\. The `artifactId` \(that is, aws\-lambda\-java\-core\) is the AWS Lambda core library that provides definitions of the `RequestHandler`, `RequestStreamHandler`, and the `Context` AWS Lambda interfaces for use in your Java application\. At the build time Maven resolves these dependencies\. 
In the plugins section, the Apache `maven-shade-plugin` is a plugin that Maven will download and use during your build process\. This plugin is used for packaging jars to create a standalone \.jar \(a \.zip file\), your deployment package\.
If you are following other tutorial topics in this guide, the specific tutorials might require you to add more dependencies\. Make sure to add those dependencies as required\.

1. In the `project-dir`, create the following structure:

   ```
   project-dir/src/main/java 
   ```

1. Under the `/java` subdirectory you add your Java files and folder structure, if any\. For example, if you Java package name is `example`, and source code is `Hello.java`, your directory structure looks like this:

   ```
   project-dir/src/main/java/example/Hello.java
   ```

## Build the Project<a name="java-package-maven-build"></a>

Now you can build the project using Maven at the command line\.

1. At a command prompt, change directory to the project directory \(*project\-dir*\)\.

1. Run the following `mvn` command to build the project:

   ```
   $ mvn package
   ```

   The resulting \.jar is saved as `project-dir/target/lambda-java-example-1.0-SNAPSHOT.jar`\. The \.jar name is created by concatenating the `artifactId` and `version` in the `pom.xml` file\. 

   The build creates this resulting \.jar, using information in the `pom.xml` to do the necessary transforms\. This is a standalone \.jar \(\.zip file\) that includes all the dependencies\. This is your deployment package that you can upload to AWS Lambda to create a Lambda function\.