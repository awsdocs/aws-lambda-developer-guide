# Lambda sample applications<a name="lambda-samples"></a>

The GitHub repository for this guide includes sample applications that demonstrate the use of various languages and AWS services\. Each sample application includes scripts for easy deployment and cleanup, an AWS SAM template, and supporting resources\.

------
#### [ Node\.js ]

**Sample Lambda applications in Node\.js**
+ [blank\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-nodejs) – A Node\.js function that shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\.
+ [nodejs\-apig](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/nodejs-apig) – A function with a public API endpoint that processes an event from API Gateway and returns an HTTP response\.
+ [rds\-mysql](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/rds-mysql) – A function that relays queries to a MySQL for RDS Database\. This sample includes a private VPC and database instance configured with a password in AWS Secrets Manager\.
+ [efs\-nodejs](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/efs-nodejs) – A function that uses an Amazon EFS file system in a Amazon VPC\. This sample includes a VPC, file system, mount targets, and access point configured for use with Lambda\.
+ [list\-manager](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/list-manager) – A function processes events from an Amazon Kinesis data stream and update aggregate lists in Amazon DynamoDB\. The function stores a record of each event in a MySQL for RDS Database in a private VPC\. This sample includes a private VPC with a VPC endpoint for DynamoDB and a database instance\.
+ [error\-processor](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/error-processor) – A Node\.js function generates errors for a specified percentage of requests\. A CloudWatch Logs subscription invokes a second function when an error is recorded\. The processor function uses the AWS SDK to gather details about the request and stores them in an Amazon S3 bucket\.

------
#### [ Python ]

**Sample Lambda applications in Python**
+ [blank\-python](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-python) – A Python function that shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\.

------
#### [ Ruby ]

**Sample Lambda applications in Ruby**
+ [blank\-ruby](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-ruby) – A Ruby function that shows the use of logging, environment variables, AWS X\-Ray tracing, layers, unit tests and the AWS SDK\.
+ [Ruby Code Samples for AWS Lambda](https://docs.aws.amazon.com/code-samples/latest/catalog/code-catalog-ruby-example_code-lambda.html) – Code samples written in Ruby that demonstrate how to interact with AWS Lambda\.

------
#### [ Java ]

**Sample Lambda applications in Java**
+ [java\-basic](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic) – A collection of minimal Java functions with unit tests and variable logging configuration\.
+ [java\-events](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-events) – A collection of Java functions that contain skeleton code for how to handle events from various services such as Amazon API Gateway, Amazon SQS, and Amazon Kinesis\. These functions use the latest version of the [aws\-lambda\-java\-events](java-package.md) library \(3\.0\.0 and newer\)\. These examples do not require the AWS SDK as a dependency\.
+ [s3\-java](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/s3-java) – A Java function that processes notification events from Amazon S3 and uses the Java Class Library \(JCL\) to create thumbnails from uploaded image files\.
+ [Use API Gateway to invoke a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/example_cross_LambdaAPIGateway_section.html) – A Java function that scans a Amazon DynamoDB table that contains employee information\. It then uses Amazon Simple Notification Service to send a text message to employees celebrating their work anniversaries\. This example uses API Gateway to invoke the function\.

------
#### [ Go ]

Lambda provides the following sample applications for the Go runtime:

**Sample Lambda applications in Go**
+ [blank\-go](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-go) – A Go function that shows the use of Lambda's Go libraries, logging, environment variables, and the AWS SDK\.

------
#### [ C\# ]

**Sample Lambda applications in C\#**
+ [blank\-csharp](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-csharp) – A C\# function that shows the use of Lambda's \.NET libraries, logging, environment variables, AWS X\-Ray tracing, unit tests, and the AWS SDK\.
+ [ec2\-spot](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/ec2-spot) – A function that manages spot instance requests in Amazon EC2\.

------
#### [ PowerShell ]

Lambda provides the following sample applications for the PowerShell runtime:
+ [blank\-powershell](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/blank-powershell) – A PowerShell function that shows the use of logging, environment variables, and the AWS SDK\.

------

To deploy a sample application, follow the instructions in its README file\. To learn more about the architecture and use cases of an application, read the topics in this chapter\.

**Topics**
+ [Blank function sample application for AWS Lambda](samples-blank.md)
+ [Error processor sample application for AWS Lambda](samples-errorprocessor.md)
+ [List manager sample application for AWS Lambda](samples-listmanager.md)