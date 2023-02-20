# Create a websocket chat application with API Gateway<a name="example_cross_ApiGatewayWebsocketChat_section"></a>

The following code example shows how to create a chat application that is served by a websocket API built on Amazon API Gateway\.

**Note**  
The source code for these examples is in the [AWS Code Examples GitHub repository](https://github.com/awsdocs/aws-doc-sdk-examples)\. Have feedback on a code example? [Create an Issue](https://github.com/awsdocs/aws-doc-sdk-examples/issues/new/choose) in the code examples repo\. 

------
#### [ Python ]

**SDK for Python \(Boto3\)**  
 Shows how to use the AWS SDK for Python \(Boto3\) with Amazon API Gateway V2 to create a websocket API that integrates with AWS Lambda and Amazon DynamoDB\.   
+ Create a websocket API served by API Gateway\.
+ Define a Lambda handler that stores connections in DynamoDB and posts messages to other chat participants\.
+ Connect to the websocket chat application and send messages with the Websockets package\.
 For complete source code and instructions on how to set up and run, see the full example on [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/python/cross_service/apigateway_websocket_chat)\.   

**Services used in this example**
+ API Gateway
+ DynamoDB
+ Lambda

------

For a complete list of AWS SDK developer guides and code examples, see [Using Lambda with an AWS SDK](sdk-general-information-section.md)\. This topic also includes information about getting started and details about previous SDK versions\.