# Create a messenger application with Step Functions<a name="example_cross_StepFunctionsMessenger_section"></a>

The following code example shows how to create an AWS Step Functions messenger application that retrieves message records from a database table\.

**Note**  
The source code for these examples is in the [AWS Code Examples GitHub repository](https://github.com/awsdocs/aws-doc-sdk-examples)\. Have feedback on a code example? [Create an Issue](https://github.com/awsdocs/aws-doc-sdk-examples/issues/new/choose) in the code examples repo\. 

------
#### [ Python ]

**SDK for Python \(Boto3\)**  
 Shows how to use the AWS SDK for Python \(Boto3\) with AWS Step Functions to create a messenger application that retrieves message records from an Amazon DynamoDB table and sends them with Amazon Simple Queue Service \(Amazon SQS\)\. The state machine integrates with an AWS Lambda function to scan the database for unsent messages\.   
+ Create a state machine that retrieves and updates message records from an Amazon DynamoDB table\.
+ Update the state machine definition to also send messages to Amazon Simple Queue Service \(Amazon SQS\)\.
+ Start and stop state machine runs\.
+ Connect to Lambda, DynamoDB, and Amazon SQS from a state machine by using service integrations\.
 For complete source code and instructions on how to set up and run, see the full example on [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/python/cross_service/stepfunctions_messenger)\.   

**Services used in this example**
+ DynamoDB
+ Lambda
+ Amazon SQS
+ Step Functions

------

For a complete list of AWS SDK developer guides and code examples, see [Using Lambda with an AWS SDK](sdk-general-information-section.md)\. This topic also includes information about getting started and details about previous SDK versions\.