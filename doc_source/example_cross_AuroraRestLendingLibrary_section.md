# Create a lending library REST API<a name="example_cross_AuroraRestLendingLibrary_section"></a>

The following code example shows how to create a lending library where patrons can borrow and return books by using a REST API backed by an Amazon Aurora database\.

**Note**  
The source code for these examples is in the [AWS Code Examples GitHub repository](https://github.com/awsdocs/aws-doc-sdk-examples)\. Have feedback on a code example? [Create an Issue](https://github.com/awsdocs/aws-doc-sdk-examples/issues/new/choose) in the code examples repo\. 

------
#### [ Python ]

**SDK for Python \(Boto3\)**  
 Shows how to use the AWS SDK for Python \(Boto3\) with the Amazon Relational Database Service \(Amazon RDS\) API and AWS Chalice to create a REST API backed by an Amazon Aurora database\. The web service is fully serverless and represents a simple lending library where patrons can borrow and return books\. Learn how to:   
+ Create and manage a serverless Aurora database cluster\.
+ Use AWS Secrets Manager to manage database credentials\.
+ Implement a data storage layer that uses Amazon RDS to move data into and out of the database\.
+ Use AWS Chalice to deploy a serverless REST API to Amazon API Gateway and AWS Lambda\.
+ Use the Requests package to send requests to the web service\.
 For complete source code and instructions on how to set up and run, see the full example on [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/python/cross_service/aurora_rest_lending_library)\.   

**Services used in this example**
+ API Gateway
+ Lambda
+ Amazon RDS
+ Secrets Manager

------

For a complete list of AWS SDK developer guides and code examples, see [Using Lambda with an AWS SDK](sdk-general-information-section.md)\. This topic also includes information about getting started and details about previous SDK versions\.