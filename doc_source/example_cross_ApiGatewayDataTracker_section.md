# Create an API Gateway REST API to track COVID\-19 data<a name="example_cross_ApiGatewayDataTracker_section"></a>

The following code example shows how to create a REST API that simulates a system to track daily cases of COVID\-19 in the United States, using fictional data\.

**Note**  
The source code for these examples is in the [AWS Code Examples GitHub repository](https://github.com/awsdocs/aws-doc-sdk-examples)\. Have feedback on a code example? [Create an Issue](https://github.com/awsdocs/aws-doc-sdk-examples/issues/new/choose) in the code examples repo\. 

------
#### [ Python ]

**SDK for Python \(Boto3\)**  
 Shows how to use AWS Chalice with the AWS SDK for Python \(Boto3\) to create a serverless REST API that uses Amazon API Gateway, AWS Lambda, and Amazon DynamoDB\. The REST API simulates a system that tracks daily cases of COVID\-19 in the United States, using fictional data\. Learn how to:   
+ Use AWS Chalice to define routes in Lambda functions that are called to handle REST requests that come through API Gateway\.
+ Use Lambda functions to retrieve and store data in a DynamoDB table to serve REST requests\.
+ Define table structure and security role resources in an AWS CloudFormation template\.
+ Use AWS Chalice and CloudFormation to package and deploy all necessary resources\.
+ Use CloudFormation to clean up all created resources\.
 For complete source code and instructions on how to set up and run, see the full example on [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/python/cross_service/apigateway_covid-19_tracker)\.   

**Services used in this example**
+ API Gateway
+ AWS CloudFormation
+ DynamoDB
+ Lambda

------

For a complete list of AWS SDK developer guides and code examples, see [Using Lambda with an AWS SDK](sdk-general-information-section.md)\. This topic also includes information about getting started and details about previous SDK versions\.